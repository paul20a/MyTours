package dcs.gla.ac.uk.minerva;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Dialog fragment that allows the user to enter a number and listen to the
 * relevant audio file
 * 
 * @author Paul Cairney
 */
public class AboutFragment extends DialogFragment {

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
		Bundle b= this.getArguments();
		String title=b.getString("title");
		String filename=b.getString("filename");
		this.getDialog().setTitle(title);
		View v = inflater.inflate(R.layout.about, container);
		TextView textView = (TextView) v.findViewById(R.id.txtAbout);
		try{
			int rID = getActivity().getResources().getIdentifier(filename, "raw",
					getActivity().getPackageName());
		InputStream inStream = getResources().openRawResource(rID);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int i;
		i = inStream.read();
		while (i != -1)
		  {
		   byteArrayOutputStream.write(i);
		   i = inStream.read();
		  }
		  textView.setText(byteArrayOutputStream.toString());
		  byteArrayOutputStream.close();
		  inStream.close();
		} catch (IOException e) {
		e.printStackTrace();
		}
		return v;
	}
}
