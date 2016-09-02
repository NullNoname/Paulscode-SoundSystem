package paulscode.sound.codecs;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import paulscode.sound.FilenameURL;
import de.jarnbjo.ogg.EndOfOggStreamException;
import de.jarnbjo.ogg.LogicalOggStream;
import de.jarnbjo.ogg.LogicalOggStreamImpl;
import de.jarnbjo.ogg.OggFormatException;
import de.jarnbjo.ogg.OggPage;
import de.jarnbjo.ogg.PhysicalOggStream;

/**
 * The JOggCustomCachedUrlStream class is a modified version of CachedUrlStream.
 * It uses FilenameURL instead of URL.
 *<b><br><br>
 *    This software is based on or using the J-Ogg library available from
 *    http://www.j-ogg.de and copyrighted by Tor-Einar Jarnbjo.
 *</b><br><br>
 *<b><i>    J-Ogg License:</b></i>
 *<br><i>
 * You are free to use, modify, resdistribute or include this software in your
 * own free or commercial software. The only restriction is, that you make it
 * obvious that your software is based on J-Ogg by including this notice in the
 * documentation, about box or whereever you feel apropriate:
 *<br>
 * "This software is based on or using the J-Ogg library available from
 * http://www.j-ogg.de and copyrighted by Tor-Einar Jarnbjo."
 * <br><br><br></i>
 *<b><i>    SoundSystem CodecJOgg License:</b></i><br>
 *<b><br>
 *    You are free to use this class for any purpose, commercial or otherwise.
 *    You may modify this class or source code, and distribute it any way you
 *    like, provided the following conditions are met:
 *<br>
 *    1) You must abide by the conditions of the aforementioned J-Ogg License.
 *<br>
 *    2) You may not falsely claim to be the author of this class or any
 *    unmodified portion of it.
 *<br>
 *    3) You may not copyright this class or a modified version of it and then
 *    sue me for copyright infringement.
 *<br>
 *    4) If you modify the source code, you must clearly document the changes
 *    made before redistributing the modified source code, so other users know
 *    it is not the original code.
 *<br>
 *    5) You are not required to give me credit for this class in any derived
 *    work, but if you do, you must also mention my website:
 *    http://www.paulscode.com
 *<br>
 *    6) I the author will not be responsible for any damages (physical,
 *    financial, or otherwise) caused by the use if this class or any portion
 *    of it.
 *<br>
 *    7) I the author do not guarantee, warrant, or make any representations,
 *    either expressed or implied, regarding the use of this class or any
 *    portion of it.
 * <br><br>
 *    Author: Paul Lamb
 * <br>
 *    http://www.paulscode.com
 * </b>
 * @author Tor-Einar Jarnbjo (original codes)
 * @author Paul Lamb (J-Ogg codec)
 * @author NullNoname (Modifications)
 */
public class JOggCustomCachedUrlStream implements PhysicalOggStream {
	   private boolean closed=false;
	   private InputStream sourceStream;
	   private Object drainLock=new Object();
	   private RandomAccessFile drain;
	   private byte[] memoryCache;
	   private ArrayList pageOffsets=new ArrayList();
	   private ArrayList pageLengths=new ArrayList();
	   private long numberOfSamples=-1;
	   private long cacheLength;

	   private HashMap logicalStreams=new HashMap();

	   private LoaderThread loaderThread;

		/**
		 *  Creates an instance of this class, using a memory cache.
		 */

	   public JOggCustomCachedUrlStream(FilenameURL filenameURL) throws OggFormatException, IOException {
	      this(filenameURL, null);
	   }

		/**
		 *  Creates an instance of this class, using the specified file as cache. The
		 *  file is not automatically deleted when this class is disposed.
		 */

	   public JOggCustomCachedUrlStream(FilenameURL filenameURL, RandomAccessFile drain) throws OggFormatException, IOException {
	      if(drain==null) {
	         int contentLength=filenameURL.getContentLength();
	         if(contentLength==-1) {
	            throw new IOException("The URLConncetion's content length must be set when operating with a in-memory cache.");
	         }
	         memoryCache=new byte[contentLength];
	      }

	      this.drain=drain;
	      this.sourceStream=filenameURL.openStream();

	      loaderThread=new LoaderThread(sourceStream, drain, memoryCache);
	      new Thread(loaderThread).start();

	      while(!loaderThread.isBosDone() || pageOffsets.size()<20) {
	         //System.out.print("pageOffsets.size(): "+pageOffsets.size()+"\r");
	         try {
	            Thread.sleep(200);
	         }
	         catch (InterruptedException ex) {
	         }
	      }
	      //System.out.println();
	      //System.out.println("caching "+pageOffsets.size()+"/20 pages\r");
	   }

	   public Collection getLogicalStreams() {
	      return logicalStreams.values();
	   }

	   public boolean isOpen() {
	      return !closed;
	   }

	   public void close() throws IOException {
	      closed=true;
	      sourceStream.close();
	   }

	   public long getCacheLength() {
	      return cacheLength;
	   }

	   /*
	   private OggPage getNextPage() throws EndOfOggStreamException, IOException, OggFormatException  {
	      return getNextPage(false);
	   }

	   private OggPage getNextPage(boolean skipData) throws EndOfOggStreamException, IOException, OggFormatException  {
	      return OggPage.create(sourceStream, skipData);
	   }
	   */

	   public OggPage getOggPage(int index) throws IOException {
	      synchronized(drainLock) {
	         Long offset=(Long)pageOffsets.get(index);
	         Long length=(Long)pageLengths.get(index);
	         if(offset!=null) {
	            if(drain!=null) {
	               drain.seek(offset.longValue());
	               return OggPage.create(drain);
	            }
	            else {
	               byte[] tmpArray=new byte[length.intValue()];
	               System.arraycopy(memoryCache, offset.intValue(), tmpArray, 0, length.intValue());
	               return OggPage.create(tmpArray);
	            }
	         }
	         else {
	            return null;
	         }
	      }
	   }

	   private LogicalOggStream getLogicalStream(int serialNumber) {
	      return (LogicalOggStream)logicalStreams.get(new Integer(serialNumber));
	   }

	   public void setTime(long granulePosition) throws IOException {
	      for(Iterator iter=logicalStreams.values().iterator(); iter.hasNext(); ) {
	         LogicalOggStream los=(LogicalOggStream)iter.next();
	         los.setTime(granulePosition);
	      }
	   }

	   public class LoaderThread implements Runnable {

	      private InputStream source;
	      private RandomAccessFile drain;
	      private byte[] memoryCache;

	      private boolean bosDone=false;

	      private int pageNumber;

	      public LoaderThread(InputStream source, RandomAccessFile drain, byte[] memoryCache) {
	         this.source=source;
	         this.drain=drain;
	         this.memoryCache=memoryCache;
	      }

	      public void run() {
	         try {
	            boolean eos=false;
	            byte[] buffer=new byte[8192];
	            while(!eos) {
	               OggPage op=OggPage.create(source);
	               synchronized (drainLock) {
	                  int listSize=pageOffsets.size();

	                  long pos=
	                     listSize>0?
	                        ((Long)pageOffsets.get(listSize-1)).longValue()+
	                        ((Long)pageLengths.get(listSize-1)).longValue():
	                        0;

	                  byte[] arr1=op.getHeader();
	                  byte[] arr2=op.getSegmentTable();
	                  byte[] arr3=op.getData();

	                  if(drain!=null) {
	                     drain.seek(pos);
	                     drain.write(arr1);
	                     drain.write(arr2);
	                     drain.write(arr3);
	                  }
	                  else {
	                     System.arraycopy(arr1, 0, memoryCache, (int)pos, arr1.length);
	                     System.arraycopy(arr2, 0, memoryCache, (int)pos+arr1.length, arr2.length);
	                     System.arraycopy(arr3, 0, memoryCache, (int)pos+arr1.length+arr2.length, arr3.length);
	                  }

	                  pageOffsets.add(new Long(pos));
	                  pageLengths.add(new Long(arr1.length+arr2.length+arr3.length));
	               }

	               if(!op.isBos()) {
	                  bosDone=true;
	                  //System.out.println("bosDone=true;");
	               }
	               if(op.isEos()) {
	                  eos=true;
	               }

	               LogicalOggStreamImpl los=(LogicalOggStreamImpl)getLogicalStream(op.getStreamSerialNumber());
	               if(los==null) {
	                  los=new LogicalOggStreamImpl(JOggCustomCachedUrlStream.this, op.getStreamSerialNumber());
	                  logicalStreams.put(new Integer(op.getStreamSerialNumber()), los);
	                  los.checkFormat(op);
	               }

	               los.addPageNumberMapping(pageNumber);
	               los.addGranulePosition(op.getAbsoluteGranulePosition());

	               pageNumber++;
	               cacheLength=op.getAbsoluteGranulePosition();
	               //System.out.println("read page: "+pageNumber);
	            }
	         }
	         catch(EndOfOggStreamException e) {
	            // ok
	         }
	         catch(IOException e) {
	            e.printStackTrace();
	         }
	      }

	      public boolean isBosDone() {
	         return bosDone;
	      }
	   }

	   public boolean isSeekable() {
	      return true;
	   }
}
