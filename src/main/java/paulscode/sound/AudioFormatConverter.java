package paulscode.sound;

import javax.sound.sampled.AudioFormat;

/**
 * Convert between AudioFormat (the real one in JDK) and PAudioFormat (clone)
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
