package dcs.gla.ac.uk.minerva;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Dialog fragment that allows the user to enter a number and listen to the
 * relevant audio file
 * 
 * @author Paul Cairney
 */
public class FragmentDialogAudioLookup extends DialogFragment implements
		OnClickListener {

	private ImageButton btnPlay;
	private ImageButton btnPause;
	private ImageButton btnStop;
	private MinervaMediaPlayer player;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//Instantiate view
		this.getDialog().setTitle("Numbered Audio Search");
		View v = inflater.inflate(R.layout.lookup_layout, container);
		//get Buttons
		Button btnGo = (Button) v.findViewById(R.id.goBtn);
		btnPlay = (ImageButton) v.findViewById(R.id.btnPlay);
		btnPause = (ImageButton) v.findViewById(R.id.btnPause);
		btnStop = (ImageButton) v.findViewById(R.id.btnReplay);
		//set listeners
		btnGo.setOnClickListener(this);
		btnPlay.setOnClickListener(this);
		btnPause.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		//disable buttons
		btnPlay.setEnabled(false);
		btnPause.setEnabled(false);
		btnStop.setEnabled(false);
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
		case R.id.goBtn:
			searchAudioFiles();
			break;
		case R.id.btnPlay:
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

	/**
	 * search for audio files in format _XXX.mp3 where XXX can be any number.
	 */
	private void searchAudioFiles() {
		int r = Integer.parseInt(((EditText) getView().findViewById(
				R.id.numberInTxt)).getText().toString().trim());

		r = getActivity().getResources().getIdentifier("g" + r+"p", "raw",
				getActivity().getPackageName());
		boolean setupCheck = player.setupMediaPlayer(r);
		if (setupCheck) {
			//enable buttons
			btnPlay.setEnabled(true);
			btnPause.setEnabled(true);
			btnStop.setEnabled(true);
		} else {
			//inform user audio file isn't present
			Toast.makeText(getActivity(), "Audio file not found",
					Toast.LENGTH_SHORT).show();
		}
	}
}
