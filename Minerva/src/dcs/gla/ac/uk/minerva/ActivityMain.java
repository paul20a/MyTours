package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * 
 * Activity that displays a TourPoint on screen using an instance of
 * FragmentViewPoint
 * 
 * @author Paul
 * 
 */
public class ActivityMain extends ActionBarActivity implements OnClickListener {
	public static final String RES_PREFIX = "android.resource://";
	private ArrayList<Waypoint> pList;
	private Resources resources;
	private ViewPager vPager;
	protected MinervaMediaPlayer player;
	private MinervaFragmentStatePagerAdapter sPagerAdapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("Create", "calling on create");

		super.onCreate(savedInstanceState);
		resources = getResources();
		// included to set logo to relevant icon
		getActionBar()
				.setIcon(
						resources.getIdentifier("logo", "raw",
								"dcs.gla.ac.uk.minerva"));
		// allow up nav
		getActionBar().setHomeButtonEnabled(true);

		setContentView(R.layout.point_pager);
		// retrieve information from intent
		int start;
		Bundle b = this.getIntent().getExtras();
		pList = b.getParcelableArrayList("pList");
		if(savedInstanceState!=null){
			start=savedInstanceState.getInt("pos");
		}
		else{
		start = b.getInt("pos");
		}
		// setup pager and adapter
		sPagerAdapter = new MinervaFragmentStatePagerAdapter(
				getSupportFragmentManager(), pList);
		vPager = (ViewPager) findViewById(R.id.pagerPoints);
		vPager.setAdapter(sPagerAdapter);
		vPager.setCurrentItem(start);

		// setup buttons
		ImageButton speakBtn = (ImageButton) findViewById(R.id.btnPlay);
		speakBtn.setOnClickListener(this);
		ImageButton pauseBtn = (ImageButton) findViewById(R.id.btnPause);
		pauseBtn.setOnClickListener(this);
		ImageButton replayBtn = (ImageButton) findViewById(R.id.btnReplay);
		replayBtn.setOnClickListener(this);
		vPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// check audio file id for current page
				boolean audioPresent = checkAudio(vPager.getCurrentItem());
				if (audioPresent) {
					setMediaButtonsEnabled(audioPresent);
					player.setupMediaPlayer(player.getAudioFile(pList.get(
							position).getAudio()));
				}else {
					player.noMedia();
				}

			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	public void onStart() {
		// check if audio file is available
		int i = vPager.getCurrentItem();
		
		Log.d("Start", "calling on Start at"+i);

		checkAudio(i);
		// get audio output method from shared preferences
		player = new MinervaMediaPlayer(this);
		boolean audioPresent = checkAudio(i);
		if (audioPresent) {
			setMediaButtonsEnabled(audioPresent);
			player.setupMediaPlayer(player
					.getAudioFile(pList.get(i).getAudio()));
		} else {
			player.noMedia();
		}
		super.onStart();
	}

	/**
	 * 
	 * Check if a audio file was provided in the xml
	 * 
	 * @param i
	 *            - position in list
	 * @return - true if a media file is given
	 */
	private boolean checkAudio(int i) {
		if (pList.get(i).getAudio() != null) {
			return true;
		}
		return false;
	}

	/**
	 * @param enable
	 *            - boolean to control if mediaPlyer buttons are enabled or not
	 */
	private void setMediaButtonsEnabled(boolean enable) {
		ImageButton a = (ImageButton) this.findViewById(R.id.btnPlay);
		ImageButton b = (ImageButton) this.findViewById(R.id.btnPause);
		ImageButton c = (ImageButton) this.findViewById(R.id.btnReplay);

		a.setEnabled(enable);
		b.setEnabled(enable);
		c.setEnabled(enable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onStop()
	 */
	@Override
	protected void onStop() {
		// release the mediaPlayer
		super.onStop();
		player.release();
		// update preferences to store audio output

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu);
		MenuItem item = menu.findItem(R.id.audio_settings);
		item.setIcon(MinervaMediaPlayer.initialiseStreamType());
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			this.finish();
			break;
		case R.id.audio_file_search:
			player.pause();
			FragmentManager m = getFragmentManager();
			FragmentDialogAudioLookup dialog = new FragmentDialogAudioLookup();
			dialog.show(m, "Audio Playback");
			break;
		case R.id.audio_settings:
			int i = vPager.getCurrentItem();
			item.setIcon(MinervaMediaPlayer.changeStreamType(this));
			// check if audio is applicable
			if (checkAudio(i)) {
				// setup media player to same state user had before using
				// different output
				player.continuePlayingAfterChange(pList.get(i).getAudio());
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPlay:
			// Request audio focus for playback
			player.play();
			break;
		case R.id.btnPause:
			player.pause();
			break;
		case R.id.btnReplay:
			player.restart();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os
	 * .Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// check audio state to continue
		outState.putInt("pos", vPager.getCurrentItem());
		outState.putBoolean("isPlaying", player.mediaPlayer.isPlaying());
		Log.d("MeidaPlayer", "currently playing ="+player.mediaPlayer.isPlaying());
		outState.putInt("progress", player.mediaPlayer.getCurrentPosition());
		super.onSaveInstanceState(outState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d("MeidaPlayer", "restoring palying was = "+savedInstanceState.getBoolean("isPlaying"));
		savedInstanceState.getBoolean("isPlaying");
		player.mediaPlayer.seekTo(savedInstanceState.getInt("progress"));
		if (savedInstanceState.getBoolean("isPlaying")) {
			Log.d("mediaPlayer", "calling play");
			player.play();
		}
		super.onRestoreInstanceState(savedInstanceState);
	}
}
