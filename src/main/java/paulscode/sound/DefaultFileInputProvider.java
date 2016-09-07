/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>
 */
package paulscode.sound;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Default implementation of IFileInputProvider, which just uses URL#openStream() to provide the InputStream.
 * License of this class is Unlicense. For more information, please refer to http://unlicense.org/.
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

	public long getContentStartOffset(FilenameURL filenameURL) {
		return 0;
	}

	public FileDescriptorWrapper openFileDescriptorWrapper(FilenameURL filenameURL) throws IOException {
		if("file".equals(filenameURL.getURL().getProtocol()) == false) {
			// Not a file protocol? Go away.
			return null;
		}

		URI uri = null;
		try {
			uri = filenameURL.getURL().toURI();
		} catch (URISyntaxException e) {
			throw new IOException("Could not create a valid URI from:" + filenameURL.getURL());
		}

		File file = new File(uri);
		FileInputStream fin = new FileInputStream(file);
		FileDescriptor fd = fin.getFD();

		return new FileDescriptorWrapper(fin, fd);
	}

}
