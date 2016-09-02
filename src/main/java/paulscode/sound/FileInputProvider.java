package paulscode.sound;

import java.io.IOException;
import java.io.InputStream;

/**
 * An interface for opening an InputStream of the requested file.
 * You can create your own implementation of this to feed your own InputStream instead of using URL#openStream().
 * @author NullNoname
 */
public interface FileInputProvider {
	/**
	 * Open a new InputStream of the requested file.
	 * @param filenameURL FilenameURL which contains an identifier (filename) and a "URL"
	 * @return InputStream to the file
	 * @throws IOException If something goes wrong
	 */
	public InputStream openStream(FilenameURL filenameURL) throws IOException;

	/**
	 * Get the content length (filesize) of the requested file.
	 * @param filenameURL FilenameURL which contains an identifier (filename) and a "URL"
	 * @return File size in bytes, or -1 if unknown or error
	 */
	public int getContentLength(FilenameURL filenameURL);
}
