package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MinervaFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Waypoint> points;
	
	/**
	 * 
	 * constructor with parameters
	 * 
	 * @param fm - FragmentManager
	 * @param s - Int size
	 * @param a - ArrayList of waypoints
	 */
	public MinervaFragmentStatePagerAdapter(FragmentManager fm,ArrayList<Waypoint> a) {
		super(fm);
		this.points=a;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int i) {
        Fragment fragment = new FragmentViewPoint();
        Bundle args = new Bundle();
        // insert arguments as a bundle
        args.putString("image", points.get(i).getImage());
        args.putString("title", points.get(i).getTitle());
        args.putString("description", points.get(i).getDescription());
        fragment.setArguments(args);
        return fragment;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return points.size();
	}

}
