package dcs.gla.ac.uk.minerva;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Dialog fragment that allows the user to enter a number and listen to the
 * relevant audio file
 * 
 * @author Paul Cairney
 */
public class FragmentDialogAudioLookup extends DialogFragment implements
		OnClickListener {

	private MinervaMediaPlayer player;
	private TextView txtNum;
	OnSearchListener mCallback;

	// Container Activity must implement this interface
	public interface OnSearchListener {
		public void onPageSearch(int id,FragmentDialogAudioLookup frag);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// ensure interface has been implemented by caller
		try {
			mCallback = (OnSearchListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " does not implement OnSearchListener");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// Instantiate view
		this.getDialog().setTitle("Search by ID");
		View v = inflater.inflate(R.layout.lookup_layout, container);
		// set up button + listener
		Button btnGo = (Button) v.findViewById(R.id.btnGo);
		btnGo.setOnClickListener(this);
		txtNum = (TextView) v.findViewById(R.id.txtNumIn);
		return v;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnGo:
			String s = txtNum.getText().toString();
			if (s != null || s == "") {
				try {
					mCallback.onPageSearch(Integer.parseInt(s),this);
				} catch (NumberFormatException e) {
					Toast.makeText(getActivity(), "Must be a numerical value",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.DialogFragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		player = new MinervaMediaPlayer(getActivity());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.DialogFragment#onStop()
	 */
	@Override
	public void onStop() {
		player.release();
		super.onStop();
	}
}
