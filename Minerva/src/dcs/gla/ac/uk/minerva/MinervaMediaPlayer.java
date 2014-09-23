package dcs.gla.ac.uk.minerva;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.view.WindowManager;
import android.widget.ImageButton;

/**
 * MediaPlayer control class to allow the MediaPlayer to be used easily over
 * different activities and fragments
 * 
 * @author Paul Cairney
 */
public class MinervaMediaPlayer implements OnAudioFocusChangeListener,
		OnCompletionListener {
	public static final String RES_PREFIX = "android.resource://";
	public MediaPlayer mediaPlayer = new MediaPlayer();
	private AudioManager a;
	private static int streamType;
	private Activity context;
	private int audioId;
	private ImageButton btnPlay;
	/**
	 * 
	 * Parameterised constructor
	 * 
	 * @param context
	 *            - Actiity context
	 */
	public MinervaMediaPlayer(Activity context) {
		this.context = context;
		btnPlay=(ImageButton) this.context.findViewById(R.id.btnPlay);
		SharedPreferences settings = context
				.getPreferences(ActivityMain.MODE_PRIVATE);
		streamType = settings
				.getInt("audioOut", AudioManager.STREAM_VOICE_CALL);
		context.setVolumeControlStream(streamType);
		a = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
	}

	/**
	 * @param i
	 *            - resource identifier
	 * @return - true if successful.
	 */
	public boolean setupMediaPlayer(int i) {
		// store locally to handle audio focus loss
		audioId = i;
		btnPlay.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_play));
		// reset and create media player
		if (mediaPlayer != null)
			mediaPlayer.reset();
		// try to initialise media using audio file
		try {
			mediaPlayer.setDataSource(
					context,
					Uri.parse(RES_PREFIX
							+ context.getResources().getResourcePackageName(
									audioId)
							+ "/"
							+ context.getResources().getResourceTypeName(
									audioId) + "/" + i));
			mediaPlayer.setAudioStreamType(streamType);
			mediaPlayer.prepare();

		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | NotFoundException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void noMedia() {
		if (mediaPlayer != null)
			mediaPlayer.reset();
	}

	/**
	 * begin playing audio
	 */
	public void play() {
		if (mediaPlayer.isPlaying()) {
			pause();
			return;
		}
		int result = a.requestAudioFocus(this, streamType,
				AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			try {
				btnPlay.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_pause));
				mediaPlayer.start();
				context.getWindow().addFlags(
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				mediaPlayer.setOnCompletionListener(this);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * pause audio
	 */
	public void pause() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			btnPlay.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_play));
			context.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.media.AudioManager.OnAudioFocusChangeListener#onAudioFocusChange
	 * (int)
	 */
	@Override
	public void onAudioFocusChange(int focusChange) {
		if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
			if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
			btnPlay.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_play));
			}
		} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
			if (mediaPlayer == null) {
				mediaPlayer = new MediaPlayer();
				setupMediaPlayer(audioId);
			}
		} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
			if(mediaPlayer.isPlaying()){
				mediaPlayer.stop();
				btnPlay.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_play));
				}
			mediaPlayer.release();
			mediaPlayer = null;
			a.abandonAudioFocus(this);
		}
	}

	/**
	 * move back to start of track
	 */
	public void restart() {
		boolean playing = mediaPlayer.isPlaying();
		mediaPlayer.seekTo(0);
		if (playing) {
			mediaPlayer.start();
		}
	}

	/**
	 * release mediaPlayer resources
	 */
	public void release() {
		mediaPlayer.release();
		mediaPlayer = null;
	}

	/**
	 * @param s
	 *            - name of audio file
	 * @return associated resource identifier
	 */
	public int getAudioFile(String s) {
		// get audio file
		return context.getResources().getIdentifier(s, "raw",
				context.getPackageName());
	}

	/**
	 * 
	 * continue audio playback in new stream output
	 * 
	 * @param s
	 *            name of audio file
	 */
	public void continuePlayingAfterChange(String s) {
		boolean playing = mediaPlayer.isPlaying();
		int t = mediaPlayer.getCurrentPosition();
		setupMediaPlayer(getAudioFile(s));
		mediaPlayer.seekTo(t);
		if (playing) {
			btnPlay.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_pause));
			mediaPlayer.start();
		}
		context.setVolumeControlStream(streamType);
	}

	/**
	 * 
	 * change current stream type
	 * 
	 * @param cont
	 *            - activity context
	 * @return String for options menu
	 */
	public static int changeStreamType(Activity cont) {

		int rId;
		if (streamType == AudioManager.STREAM_VOICE_CALL) {
			streamType = AudioManager.STREAM_MUSIC;
			rId = R.drawable.selecta;
		} else {
			streamType = AudioManager.STREAM_VOICE_CALL;
			rId = R.drawable.selectb;
		}
		savePref(cont);
		return rId;
	}

	/**
	 * 
	 * setup default stream type
	 * 
	 * @return String for options menu
	 */
	public static int initialiseStreamType() {
		if (streamType == AudioManager.STREAM_MUSIC) {
			return R.drawable.selecta;
		} else {
			return R.drawable.selectb;
		}
	}

	/**
	 * save stream type to preferences
	 */
	private static void savePref(Activity cont) {
		SharedPreferences settings = cont
				.getPreferences(ActivityMain.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("audioOut", streamType);
		editor.commit();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		context.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		btnPlay.setImageDrawable(context.getResources().getDrawable(android.R.drawable.ic_media_play));
	}
}
