package paulscode.sound;

/**
 * The IMidiChannel interface wraps MIDI player class(es).
 *<br><br>
 *<b><i>    SoundSystem License:</b></i><br><b><br>
 *    You are free to use this library for any purpose, commercial or otherwise.
 *    You may modify this library or source code, and distribute it any way you
 *    like, provided the following conditions are met:
 *<br>
 *    1) You may not falsely claim to be the author of this library or any
 *    unmodified portion of it.
 *<br>
 *    2) You may not copyright this library or a modified version of it and then
 *    sue me for copyright infringement.
 *<br>
 *    3) If you modify the source code, you must clearly document the changes
 *    made before redistributing the modified source code, so other users know
 *    it is not the original code.
 *<br>
 *    4) You are not required to give me credit for this library in any derived
 *    work, but if you do, you must also mention my website:
 *    http://www.paulscode.com
 *<br>
 *    5) I the author will not be responsible for any damages (physical,
 *    financial, or otherwise) caused by the use if this library or any part
 *    of it.
 *<br>
 *    6) I the author do not guarantee, warrant, or make any representations,
 *    either expressed or implied, regarding the use of this library or any
 *    part of it.
 * <br><br>
 *    Author: Paul Lamb
 * <br>
 *    http://www.paulscode.com
 * </b>
 * @author Paul Lamb (original codes)
 * @author NullNoname (IMidiChannel)
 */
public interface IMidiChannel {
	/**
	 * Shuts the channel down and removes references to all instantiated objects.
	 */
	public void cleanup();

	/**
	 * Queues up the next MIDI sequence to play when the previous sequence ends.
	 * @param filenameURL MIDI sequence to play next.
	 */
	public void queueSound(FilenameURL filenameURL);

	/**
	 * Removes the first occurrence of the specified filename/identifier from the
	 * list of MIDI sequences to play when the previous sequence ends.
	 * @param filename Filename or identifier of a MIDI sequence to remove from the
	 * queue.
	 */
	public void dequeueSound(String filename);

	/**
	 * Fades out the volume of whatever sequence is currently playing, then
	 * begins playing the specified MIDI file at the previously assigned
	 * volume level.  If the filenameURL parameter is null or empty, playback will
	 * simply fade out and stop.  The miliseconds parameter must be non-negative or
	 * zero.  This method will remove anything that is currently in the list of
	 * queued MIDI sequences that would have played next when current playback
	 * finished.
	 * @param filenameURL MIDI file to play next, or null for none.
	 * @param milis Number of miliseconds the fadeout should take.
	 */
	public void fadeOut(FilenameURL filenameURL, long milis);

	/**
	 * Fades out the volume of whatever sequence is currently playing, then
	 * fades the volume back in playing the specified MIDI file.  Final volume
	 * after fade-in completes will be equal to the previously assigned volume
	 * level.  The filenameURL parameter may not be null or empty.  The miliseconds
	 * parameters must be non-negative or zero.  This method will remove anything
	 * that is currently in the list of queued MIDI sequences that would have
	 * played next when current playback finished.
	 * @param filenameURL MIDI file to play next, or null for none.
	 * @param milisOut Number of miliseconds the fadeout should take.
	 * @param milisIn Number of miliseconds the fadein should take.
	 */
	public void fadeOutIn(FilenameURL filenameURL, long milisOut, long milisIn);

	/**
	 * Plays the MIDI file from the beginning, or from where it left off if it was
	 * paused.
	 */
	public void play();

	/**
	 * Stops playback and rewinds to the beginning.
	 */
	public void stop();

	/**
	 * Temporarily stops playback without rewinding.
	 */
	public void pause();

	/**
	 * Returns playback to the beginning.
	 */
	public void rewind();

	/**
	 * Changes the volume of MIDI playback.
	 * @param value Float value (0.0f - 1.0f).
	 */
	public void setVolume(float value);

	/**
	 * Returns the current volume for the MIDI source.
	 * @return Float value (0.0f - 1.0f).
	 */
	public float getVolume();

	/**
	 * Changes the basic information about the MIDI source.  This method removes
	 * any queued filenames/URLs from the list of MIDI sequences that would have
	 * played after the current sequence ended.
	 * @param toLoop Should playback loop or play only once?
	 * @param sourcename Unique identifier for this source.
	 * @param filenameURL Filename/URL of the MIDI file to play.
	 */
	public void switchSource(boolean toLoop, String sourcename, FilenameURL filenameURL);

	/**
	 * Sets the value of boolean 'toLoop'.
	 * @param value True or False.
	 */
	public void setLooping( boolean value );

	/**
	 * Returns the value of boolean 'toLoop'.
	 * @return True while looping.
	 */
	public boolean getLooping();

	/**
	 * Check if a MIDI file is in the process of loading.
	 * @return True while loading.
	 */
	public boolean loading();

	/**
	 * Defines the unique identifier for this source
	 * @param value New source name.
	 */
	public void setSourcename( String value );

	/**
	 * Returns the unique identifier for this source.
	 * @return The source's name.
	 */
	public String getSourcename();

	/**
	 * Defines which MIDI file to play.
	 * @param value Path to the MIDI file.
	 */
	public void setFilenameURL( FilenameURL value );

	/**
	 * Returns the filename/identifier of the MIDI file being played.
	 * @return Filename of identifier of the MIDI file.
	 */
	public String getFilename();

	/**
	 * Returns the MIDI file being played.
	 * @return Filename/URL of the MIDI file.
	 */
	public FilenameURL getFilenameURL();

	/**
	 * Resets playback volume to the correct level.
	 */
	public void resetGain();
}
