package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * Activity for displaying initial tour description this reads in tour data and
 * displays it in a FragmentViewPoint
 * 
 * @author Paul Cairney
 */
public class ActivityTour extends FragmentActivity implements OnClickListener {
	public final static String TITLE = "dcs.gla.ac.uk.TITLE";
	public final static String TRAILPATH = "dcs.gla.ac.uk.TRAILPATH";
	private ArrayList<Object> tList;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// As this is lead activity initialise global image cache

		setContentView(R.layout.activity_trail_select);
		XmlTrailParser xParser = new XmlTrailParser();
		getActionBar().setIcon(
				getResources().getIdentifier("logo", "raw",
						"dcs.gla.ac.uk.minerva"));

		try {
			Resources resources = getResources();
			int rID = resources
					.getIdentifier("trails", "raw", getPackageName());
			InputStream in = resources.openRawResource(rID);
			tList = xParser.parse(in);
			in.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// bundle arguements up to give them to the fragment
		Bundle bundle = new Bundle();
		bundle.putString("title", ((Trail) tList.get(0)).getTitle());
		bundle.putString("image", ((Trail) tList.get(0)).getImage());
		bundle.putString("description", ((Trail) tList.get(0)).getDescription());
		// if there isn't a saved instance of this activity
		if (savedInstanceState == null) {
			// create a new FragmentViewPoint and give it the bundle as it's
			// arguement
			FragmentViewPoint vpf = new FragmentViewPoint();
			vpf.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.frLayoutTrail, vpf).commit();
		}
		// setup tour start button
		Button startBtn = (Button) findViewById(R.id.btnStart);
		startBtn.setOnClickListener(this);
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
		case R.id.audio_file_search:
			FragmentManager m = getFragmentManager();
			FragmentDialogAudioLookup dialog = new FragmentDialogAudioLookup();
			dialog.show(m, "Audio Playback");
			return true;
		case R.id.audio_settings:
			item.setIcon(MinervaMediaPlayer.changeStreamType(this));
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == (R.id.btnStart)) {
			Intent intent = new Intent(this, ActivitySelect.class);
			intent.putExtra(TRAILPATH, ((Trail) tList.get(0)).getFile());
			startActivity(intent);
		}

	}
}
