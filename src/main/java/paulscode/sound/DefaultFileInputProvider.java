package paulscode.sound;

import java.io.IOException;
import java.io.InputStream;

/**
 * Default implementation of IFileInputProvider, which just uses URL#openStream() to provide the InputStream.
 * @author NullNoname
 */
public class DefaultFileInputProvider implements FileInputProvider {
	public InputStream openStream(FilenameURL filenameURL) throws IOException {
		return filenameURL.getURL().openStream();
	}

	public int getContentLength(FilenameURL filenameURL) {
		try {
			return filenameURL.getURL().openConnection().getContentLength();
		} catch (IOException e) {
			return -1;
		}
	}
}
