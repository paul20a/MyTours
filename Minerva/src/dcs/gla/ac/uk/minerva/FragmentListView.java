package dcs.gla.ac.uk.minerva;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * Class to display a listViw object
 * 
 * @author Paul Cairney
 *
 */
public class FragmentListView extends Fragment {	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate selection view
		View v = inflater.inflate(R.layout.activity_select, container, false);
		// create the listView
		final ListView lView = (ListView) v.findViewById(R.id.listView);
		
		// update this so ActivitySelect is not required, stop using pList from
		// ActivitySelect
		lView.setAdapter(new PointBaseAdapter(getActivity(),
				(((ActivitySelect) getActivity()).getpList())));
		
		// listen for click Actions
		lView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Intent detailIntent = new Intent(getActivity(),
						ActivityMain.class);
				// Need to update this class is too dependent on ActivitySelect
				detailIntent.putExtra("pList",
						((ActivitySelect) getActivity()).getpList());
				detailIntent.putExtra("pos", position);
				Toast.makeText(getActivity(), "Loading", Toast.LENGTH_SHORT)
						.show();
				startActivity(detailIntent);
			}
		});

		return v;
	}	
}
