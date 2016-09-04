package paulscode.sound.codecs;

import java.io.Closeable;
import java.io.IOException;

import paulscode.sound.FilenameURL;
import paulscode.sound.ICodec;
import paulscode.sound.LittleEndianDataInputStream;
import paulscode.sound.PAudioFormat;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;

/**
 * The CodecWavN class provides an ICodec interface for reading from .wav files.
 * This is based on CodecWav but read the header using our own routines and
 * does not rely on JavaSound API.
 *<br><br>
 *<b><i>   SoundSystem CodecWav License:</b></i><br><b><br>
 *    You are free to use this class for any purpose, commercial or otherwise.
 *    You may modify this class or source code, and distribute it any way you
 *    like, provided the following conditions are met:
 *<br>
 *    1) You may not falsely claim to be the author of this class or any
 *    unmodified portion of it.
 *<br>
 *    2) You may not copyright this class or a modified version of it and then
 *    sue me for copyright infringement.
 *<br>
 *    3) If you modify the source code, you must clearly document the changes
 *    made before redistributing the modified source code, so other users know
 *    it is not the original code.
 *<br>
 *    4) You are not required to give me credit for this class in any derived
 *    work, but if you do, you must also mention my website:
 *    http://www.paulscode.com
 *<br>
 *    5) I the author will not be responsible for any damages (physical,
 *    financial, or otherwise) caused by the use if this class or any portion
 *    of it.
 *<br>
 *    6) I the author do not guarantee, warrant, or make any representations,
 *    either expressed or implied, regarding the use of this class or any
 *    portion of it.
 * <br><br>
 *    Author: Paul Lamb
 * <br>
 *    http://www.paulscode.com
 * </b>
 * @author Paul Lamb (original CodecWav codes)
 * @author NullNoname (CodecWavN)
 */
public class CodecWavN implements ICodec {
	/**
	 * Used to return a current value from one of the synchronized boolean-interface methods.
	 */
	private static final boolean GET = false;

	/**
	 * Used to set the value in one of the synchronized boolean-interface methods.
	 */
	private static final boolean SET = true;

	/**
	 * Used when a parameter for one of the synchronized boolean-interface methods is not aplicable.
	 */
	private static final boolean XXX = false;

	/**
	 * True if there is no more data to read in.
	 */
	private boolean endOfStream = false;

	/**
	 * True if the stream has finished initializing.
	 */
	private boolean initialized = false;

	/**
	 * Processes status messages, warnings, and error messages.
	 */
	private SoundSystemLogger logger;

	/**
	 * PAudioFormat of the wav file
	 */
	private PAudioFormat myAudioFormat;

	/**
	 * The data chunk size of the wav file
	 */
	private int dataChunkSize;

	/**
	 * The little endian DataInputStream
	 */
	private LittleEndianDataInputStream ledin;

	/**
	 * Constructor:  Grabs a handle to the logger.
	 */
	public CodecWavN() {
		logger = SoundSystemConfig.getLogger();
	}

	/**
	 * This method is ignored by CodecWavN, because it produces "nice" data.
	 * @param b True if the calling audio library requires byte-reversal from certain codecs
	 */
	public void reverseByteOrder(boolean b) {
	}

	public boolean initialize(FilenameURL filenameURL) {
		initialized(SET, false);
		cleanup();

		if(filenameURL == null) {
			errorMessage("filenameURL null in method 'initialize'");
			cleanup();
			return false;
		}

		try {
			ledin = new LittleEndianDataInputStream(filenameURL.openStream());
		} catch (IOException ioe) {
			errorMessage("error opening an InputStream in method 'initialize'");
			printStackTrace(ioe);
			return false;
		}

		try {
			readHeader();
		} catch (IOException ioe) {
			errorMessage("Error reading the header");
			printStackTrace(ioe);
			return false;
		}

		endOfStream(SET, false);
		initialized(SET, true);
		return true;
	}

	public boolean initialized() {
		return initialized(GET, XXX);
	}

	public SoundBuffer read() {
		if(ledin == null) return null;

		// Check to make sure there is an audio format:
		if(myAudioFormat == null) {
			errorMessage( "Audio Format null in method 'read'" );
			return null;
		}

		// Varriables used when reading from the audio input stream:
		int bytesRead = 0, cnt = 0;

		// Allocate memory for the audio data:
		byte[] streamBuffer = new byte[SoundSystemConfig.getStreamingBufferSize()];

		try {
			// Read until buffer is full or end of stream is reached:
			while((!endOfStream(GET, XXX)) && (bytesRead < streamBuffer.length)) {
				if((cnt = ledin.read(streamBuffer, bytesRead, streamBuffer.length - bytesRead)) <= 0) {
					endOfStream(SET, true);
					break;
				}
				// keep track of how many bytes were read:
				bytesRead += cnt;
			}
		} catch (IOException ioe) {
			// TODO: See if setting endOfStream is needed here
			endOfStream(SET, true);
			return null;
		}

		// Return null if no data was read:
		if(bytesRead <= 0)
			return null;

		// If we didn't fill the stream buffer entirely, trim it down to size:
		if(bytesRead < streamBuffer.length)
			streamBuffer = trimArray(streamBuffer, bytesRead);

		// Wrap the data into a SoundBuffer:
		SoundBuffer buffer = new SoundBuffer(streamBuffer, myAudioFormat);

		// Return the result:
		return buffer;
	}

	public SoundBuffer readAll() {
		// Check to make sure there is an audio format:
		if(ledin == null) {
			errorMessage("Audio input stream null in method 'readAll'");
			return null;
		}

		// Check to make sure there is an audio format:
		if(myAudioFormat == null) {
			errorMessage( "Audio Format null in method 'readAll'" );
			return null;
		}

		// Array to contain the audio data:
		byte[] fullBuffer = null;

		// Determine how much data will be read in:
		int fileSize = Math.min(dataChunkSize, SoundSystemConfig.getMaxFileSize());

		// Allocate memory for the audio data:
		fullBuffer = new byte[fileSize];
		int read = 0, total = 0;
		try {
			// Read until the end of the stream is reached:
			while((read = ledin.read(fullBuffer, total, fullBuffer.length - total)) != -1 && total < fullBuffer.length) {
				total += read;
			}
		} catch (IOException e) {
			errorMessage("Exception thrown while reading from the " + "LittleEndianDataInputStream.");
			printStackTrace(e);
			return null;
		}

		// Wrap the data into an SoundBuffer:
		SoundBuffer soundBuffer = new SoundBuffer(fullBuffer, myAudioFormat);

		// Close the audio input stream
		closeQuietly(ledin);

		// Return the result:
		return soundBuffer;
	}

	public boolean endOfStream() {
		return endOfStream(GET, XXX);
	}

	public void cleanup() {
		closeQuietly(ledin);
		ledin = null;
	}

	public PAudioFormat getAudioFormat() {
		return myAudioFormat;
	}

	/**
	 * Read the RIFF/WAVE header
	 * @throws IOException If something bad happens
	 */
	private void readHeader() throws IOException {
		// Check RIFF at the beginning
		int riff = ledin.readInt();
		if(riff != 0x46464952) {
			throw new IOException("Not a RIFF (0x" + Integer.toHexString(riff).toUpperCase() + ")");
		}

		int totalFileSizeMinus8 = ledin.readInt();
		message("Wav file size:" + (totalFileSizeMinus8 + 8));

		// Check WAVE header
		int wave = ledin.readInt();
		if(wave != 0x45564157) {
			throw new IOException("Not a WAVE (0x" + Integer.toHexString(wave).toUpperCase() + ")");
		}

		boolean exit = false;
		while(!exit) {
			// Read a Chunk ID and its size
			int chunkID = ledin.readInt();
			int chunkSize = ledin.readInt();

			// "fmt "
			if(chunkID == 0x20746D66) {
				//message("fmt chunkSize:" + chunkSize);
				short compressionFormat = ledin.readShort();
				short channels = ledin.readShort();
				int sampleRate = ledin.readInt();
				int bytesPerSecond = ledin.readInt();
				short frameSize = ledin.readShort();
				short sampleSizeInBits = ledin.readShort();
				/*
				message("compressionFormat:" + compressionFormat);
				message("channels:" + channels);
				message("sampleRate:" + sampleRate);
				message("bytesPerSecond:" + bytesPerSecond);
				message("frameSize:" + frameSize);
				message("sampleSizeInBits:" + sampleSizeInBits);
				*/

				PAudioFormat.Encoding encoding = PAudioFormat.Encoding.PCM_SIGNED;
				if(compressionFormat == 6) {
					encoding = PAudioFormat.Encoding.ALAW;
				} else if(compressionFormat == 7) {
					encoding = PAudioFormat.Encoding.ULAW;
				} else if(sampleSizeInBits == 8) {
					encoding = PAudioFormat.Encoding.PCM_UNSIGNED;
				}

				myAudioFormat = new PAudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, bytesPerSecond/frameSize, false);

				if(chunkSize != 16) {
					int skip = chunkSize - 16;
					message("Skipping " + skip + " bytes of unknown data in fmt chunk");
					ledin.skipBytes(skip);
				}
			}
			// "data"
			else if(chunkID == 0x61746164) {
				// done
				//message("data chunk size:" + chunkSize);
				dataChunkSize = chunkSize;
				exit = true;
			}
			// ????
			else {
				message("Skipping an unknown chunk (0x" + Integer.toHexString(wave).toUpperCase() + ") for " + chunkSize + " bytes");
				ledin.skipBytes(chunkSize);
			}
		}

		if(myAudioFormat == null) {
			throw new IOException("This file does not contain \"fmt \" chunk");
		}

		// OK
	}

	/**
	 * Internal method for synchronizing access to the boolean 'initialized'.
	 * @param action GET or SET.
	 * @param value New value if action == SET, or XXX if action == GET.
	 * @return True if steam is initialized.
	 */
	private synchronized boolean initialized(boolean action, boolean value) {
		if(action == SET)
			initialized = value;
		return initialized;
	}

	/**
	 * Internal method for synchronizing access to the boolean 'endOfStream'.
	 * @param action GET or SET.
	 * @param value New value if action == SET, or XXX if action == GET.
	 * @return True if end of stream was reached.
	 */
	private synchronized boolean endOfStream(boolean action, boolean value) {
		if(action == SET)
			endOfStream = value;
		return endOfStream;
	}

	/**
	 * Trims down the size of the array if it is larger than the specified
	 * maximum length.
	 * @param array Array containing audio data.
	 * @param maxLength Maximum size this array may be.
	 * @return New array.
	 */
	private static byte[] trimArray(byte[] array, int maxLength) {
		byte[] trimmedArray = null;
		if(array != null && array.length > maxLength) {
			trimmedArray = new byte[maxLength];
			System.arraycopy(array, 0, trimmedArray, 0, maxLength);
		}
		return trimmedArray;
	}

	/**
	 * Creates a new array with the second array appended to the end of the first
	 * array.
	 * @param arrayOne The first array.
	 * @param arrayTwo The second array.
	 * @param arrayTwoBytes The number of bytes to append from the second array.
	 * @return Byte array containing information from both arrays.
	 */
	private static byte[] appendByteArrays(byte[] arrayOne, byte[] arrayTwo, int arrayTwoBytes) {
		byte[] newArray;
		int bytes = arrayTwoBytes;

		// Make sure we aren't trying to append more than is there:
		if(arrayTwo == null || arrayTwo.length == 0)
			bytes = 0;
		else if(arrayTwo.length < arrayTwoBytes)
			bytes = arrayTwo.length;

		if(arrayOne == null && (arrayTwo == null || bytes <= 0)) {
			// no data, just return
			return null;
		} else if(arrayOne == null) {
			// create the new array, same length as arrayTwo:
			newArray = new byte[bytes];
			// fill the new array with the contents of arrayTwo:
			System.arraycopy(arrayTwo, 0, newArray, 0, bytes);
			arrayTwo = null;
		} else if(arrayTwo == null || bytes <= 0) {
			// create the new array, same length as arrayOne:
			newArray = new byte[arrayOne.length];
			// fill the new array with the contents of arrayOne:
			System.arraycopy(arrayOne, 0, newArray, 0, arrayOne.length);
			arrayOne = null;
		} else {
			// create the new array large enough to hold both arrays:
			newArray = new byte[arrayOne.length + bytes];
			System.arraycopy(arrayOne, 0, newArray, 0, arrayOne.length);
			// fill the new array with the contents of both arrays:
			System.arraycopy(arrayTwo, 0, newArray, arrayOne.length, bytes);
			arrayOne = null;
			arrayTwo = null;
		}

		return newArray;
	}

	/**
	 * Creates a new array with the second array appended to the end of the first
	 * array.
	 * @param arrayOne The first array.
	 * @param arrayTwo The second array.
	 * @return Byte array containing information from both arrays.
	 */
	private static byte[] appendByteArrays(byte[] arrayOne, byte[] arrayTwo) {
		byte[] newArray;
		if(arrayOne == null && arrayTwo == null) {
			// no data, just return
			return null;
		} else if(arrayOne == null) {
			// create the new array, same length as arrayTwo:
			newArray = new byte[arrayTwo.length];
			// fill the new array with the contents of arrayTwo:
			System.arraycopy(arrayTwo, 0, newArray, 0, arrayTwo.length);
			arrayTwo = null;
		} else if(arrayTwo == null) {
			// create the new array, same length as arrayOne:
			newArray = new byte[arrayOne.length];
			// fill the new array with the contents of arrayOne:
			System.arraycopy(arrayOne, 0, newArray, 0, arrayOne.length);
			arrayOne = null;
		} else {
			// create the new array large enough to hold both arrays:
			newArray = new byte[arrayOne.length + arrayTwo.length];
			System.arraycopy(arrayOne, 0, newArray, 0, arrayOne.length);
			// fill the new array with the contents of both arrays:
			System.arraycopy(arrayTwo, 0, newArray, arrayOne.length, arrayTwo.length);
			arrayOne = null;
			arrayTwo = null;
		}

		return newArray;
	}

	private static void closeQuietly(Closeable c) {
		try {c.close();} catch (Exception e) {}
	}

	/**
	 * Prints a message.
	 * @param message Message to print.
	 */
	private void message( String message ) {
		logger.message(message, 0);
	}

	/**
	 * Prints an error message.
	 * @param message Message to print.
	 */
	private void errorMessage( String message ) {
		logger.errorMessage( "CodecWav", message, 0 );
	}

	/**
	 * Prints an exception's error message followed by the stack trace.
	 * @param e Exception containing the information to print.
	 */
	private void printStackTrace( Exception e ) {
		logger.printStackTrace( e, 1 );
	}
}
