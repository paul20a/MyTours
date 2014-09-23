package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 
 * this activity allows the user to select Waypoints either from a
 * FragmentListView or a FragemntMapView ActionBar tabs are used to navigate
 * between the two fragments, Swipe is disabled due to the way a user interacts
 * with a map
 * 
 * @author Paul Cairney
 * 
 */
public class ActivitySelect extends ActionBarActivity implements TabListener,FragmentDialogAudioLookup.OnSearchListener  {
	MinervaFragmentPagerAdapter pAdapter;
	SwipelessPager vPager;
	public ArrayList<Object> pList = new ArrayList<Object>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final ActionBar navBar = getActionBar();
		XmlPointParser xParser = new XmlPointParser();
		Resources resources = getResources();
		setContentView(R.layout.pager);
		// try and parse xml document
		try {
			int rID = resources.getIdentifier(
					getIntent().getStringExtra(ActivityTour.TRAILPATH), "raw",
					getPackageName());
			InputStream in = resources.openRawResource(rID);
			resources = null;
			pList = xParser.parse(in);
			// close stream
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		// Setup Action bar for navigation
		navBar.setHomeButtonEnabled(true);
		navBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// initialise FragmentPagerAdapter
		pAdapter = new MinervaFragmentPagerAdapter(getSupportFragmentManager());
		// use a custom Swipeless ViewPager to avoid annoying accidental swipes
		// while panning in the map view
		vPager = (SwipelessPager) findViewById(R.id.pagerSwipeless);
		vPager.setAdapter(pAdapter);
		// listen for page changes
		vPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				navBar.setSelectedNavigationItem(position);
			}
		});

		// for element in thePagerAdapter
		for (int i = 0; i < pAdapter.getCount(); i++) {
			// add a tab and set text
			navBar.addTab(navBar.newTab().setText(getTabTitle(i))
					.setTabListener(this));
		}

	}

	/**
	 * 
	 * return the name of a tab given its position in the navigation bar
	 * 
	 * @param i
	 *            position in tab
	 * @return Name associated with tab
	 */
	private CharSequence getTabTitle(int i) {
		String s = "";
		if (i == 0)
			s = getResources().getString(R.string.list_section);
		else
			s = getResources().getString(R.string.map_section);
		return s;
	}

	/**
	 * 
	 * accessor for pList array, accessing in this way could be better done
	 * 
	 * @return
	 */
	public ArrayList<Object> getpList() {
		// TODO - change how pList is accessed, bundle it rather than providing
		// public access
		return pList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		final Bundle args=new Bundle();
		final FragmentManager m = getFragmentManager();

		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.help:
			final AboutFragment help = new AboutFragment();
			args.putString("title", "Help");
			args.putString("filename", "a_help");
			help.setArguments(args);
			help.show(m, "Help");
			break;
		case R.id.about:
			final AboutFragment about = new AboutFragment();
			args.putString("title", "About");
			args.putString("filename", "a_about");
			about.setArguments(args);
			about.show(m, "About");
			break;
		case R.id.page_search:
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
	 * @see
	 * android.app.ActionBar.TabListener#onTabSelected(android.app.ActionBar
	 * .Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// set the page on tab select
		vPager.setCurrentItem(tab.getPosition());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.ActionBar.TabListener#onTabUnselected(android.app.ActionBar
	 * .Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// feature not enabled
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.ActionBar.TabListener#onTabReselected(android.app.ActionBar
	 * .Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// feature not enabled
	}

	@Override
	public void onPageSearch(int id, FragmentDialogAudioLookup frag) {
		for(int i=0;i<pList.size();i++){
			if(((Waypoint)pList.get(i)).getId()==id){
				Intent detailIntent = new Intent(this,
						ActivityMain.class);
				// Need to update this class is too dependent on ActivitySelect
				detailIntent.putExtra("pList",pList);
				detailIntent.putExtra("pos", i);
				Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
						.show();
				startActivity(detailIntent);
				frag.dismiss();
				return;
			}
		}
	}
}
