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

import javax.sound.sampled.AudioFormat;

/**
 * Convert between AudioFormat (the real one in JDK) and PAudioFormat (clone).
 * License of this class is Unlicense. For more information, please refer to http://unlicense.org/.
 * @author NullNoname
 */
public class AudioFormatConverter {
	/**
	 * Convert PAudioFormat.Encoding (clone) to AudioFormat.Encoding (real)
	 * @param src Source (clone)
	 * @return Real
	 */
	public static AudioFormat.Encoding convertAudioFormatEncoding(PAudioFormat.Encoding src) {
		if(src.equals(PAudioFormat.Encoding.PCM_SIGNED)) return AudioFormat.Encoding.PCM_SIGNED;
		if(src.equals(PAudioFormat.Encoding.PCM_UNSIGNED)) return AudioFormat.Encoding.PCM_UNSIGNED;
		if(src.equals(PAudioFormat.Encoding.ULAW)) return AudioFormat.Encoding.ULAW;
		if(src.equals(PAudioFormat.Encoding.ALAW)) return AudioFormat.Encoding.ALAW;
		return new AudioFormat.Encoding(src.toString());
	}

	/**
	 * Convert AudioFormat.Encoding (real) to PAudioFormat.Encoding (clone)
	 * @param src Source (real)
	 * @return Clone
	 */
	public static PAudioFormat.Encoding convertAudioFormatEncoding(AudioFormat.Encoding src) {
		if(src.equals(PAudioFormat.Encoding.PCM_SIGNED)) return PAudioFormat.Encoding.PCM_SIGNED;
		if(src.equals(PAudioFormat.Encoding.PCM_UNSIGNED)) return PAudioFormat.Encoding.PCM_UNSIGNED;
		if(src.equals(PAudioFormat.Encoding.ULAW)) return PAudioFormat.Encoding.ULAW;
		if(src.equals(PAudioFormat.Encoding.ALAW)) return PAudioFormat.Encoding.ALAW;
		return new PAudioFormat.Encoding(src.toString());
	}

	/**
	 * Convert PAudioFormat (clone) to AudioFormat (real)
	 * @param src Source (clone)
	 * @return Real
	 */
	public static AudioFormat convertAudioFormat(PAudioFormat src) {
		return new AudioFormat(convertAudioFormatEncoding(src.getEncoding()), src.getSampleRate(), src.getSampleSizeInBits(),
				src.getChannels(), src.getFrameSize(), src.getFrameRate(), src.isBigEndian(), src.properties());
	}

	/**
	 * Convert PAudioFormat (real) to AudioFormat (clone)
	 * @param src Source (real)
	 * @return Clone
	 */
	public static PAudioFormat convertAudioFormat(AudioFormat src) {
		return new PAudioFormat(convertAudioFormatEncoding(src.getEncoding()), src.getSampleRate(), src.getSampleSizeInBits(),
				src.getChannels(), src.getFrameSize(), src.getFrameRate(), src.isBigEndian(), src.properties());
	}
}
