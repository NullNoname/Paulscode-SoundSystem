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

import java.io.IOException;
import java.io.InputStream;

/**
 * An interface for opening an InputStream of the requested file.
 * You can create your own implementation of this to feed your own InputStream instead of using URL#openStream().
 * License of this class is Unlicense. For more information, please refer to http://unlicense.org/.
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

	/**
	 * Get the offset where the actual content starts in the stream.
	 * Only called and used by those weird codecs or MidiChannels that rely on it.
	 * Unless you know what to do, return 0.
	 * @param filenameURL FilenameURL which contains an identifier (filename) and a "URL"
	 * @return Offset
	 */
	public long getContentStartOffset(FilenameURL filenameURL);

	/**
	 * Returns an instance of FileDescriptorWrapper which contains a Closeable (most likely FileInputStream on PC) and a FileDescriptor.
	 * Returns null if not a local file.
	 * Only called and used by those weird codecs or MidiChannels that rely on it.
	 * If you are unsure, just use the default implementation of DefaultFileInputProvider.
	 * @param filenameURL FilenameURL which contains an identifier (filename) and a "URL"
	 * @return FileDescriptorWrapper, or null if it is not a local file
	 * @throws IOException When something fails
	 * @throws IllegalArgumentException When something fails
	 */
	public FileDescriptorWrapper openFileDescriptorWrapper(FilenameURL filenameURL) throws IOException;
}
