package dcs.gla.ac.uk.minerva;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * Simple Fragment pager for mapView and ListView Fragments
 * 
 * @author user
 *
 */
public class MinervaFragmentPagerAdapter extends FragmentPagerAdapter {
	static final int MENU_ITEMS = 2;

	/**
	 * 
	 * constructor with parameters
	 * 
	 * @param fm-Fragment Manager
	 */
	public MinervaFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int arg0) {
		if(arg0==0)
		return new FragmentListView();
		else
		return new FragmentMapView();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return MENU_ITEMS;
	}

}
