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

import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;

/**
 * A pair of a Closeable and a FileDescriptor.
 *
 * License of this class is Unlicense. For more information, please refer to http://unlicense.org/.
 * @author NullNoname
 */
public class FileDescriptorWrapper {
	/** The Closeable (eg. FileInputStream) that has the FileDescriptor */
	protected Closeable mCloseable;

	/** FileDescriptor */
	protected FileDescriptor mFileDescriptor;

	/**
	 * Constructor
	 * @param mCloseable Closeable
	 * @param mFileDescriptor FileDescriptor
	 */
	public FileDescriptorWrapper(Closeable mCloseable, FileDescriptor mFileDescriptor) {
		this.mCloseable = mCloseable;
		this.mFileDescriptor = mFileDescriptor;
	}

	/**
	 * Get the Closeable (eg. FileInputStream)
	 * @return Closeable
	 */
	public Closeable getCloseable() {
		return mCloseable;
	}

	/**
	 * Get the FileDescriptor
	 * @return FileDescriptor
	 */
	public FileDescriptor getFileDescriptor() {
		return mFileDescriptor;
	}

	/**
	 * Close this Closeable
	 * @throws IOException When something fails
	 */
	public void close() throws IOException {
		mCloseable.close();
	}

	/**
	 * Close this Closable, ignoring any Exceptions
	 * @return true if successful
	 */
	public boolean closeQuietly() {
		try {
			mCloseable.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
