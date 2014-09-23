package dcs.gla.ac.uk.minerva;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * Creates the view of a TourPoint showing its title image and description
 * 
 * @author Paul Cairney
 * 
 */
public class FragmentViewPoint extends Fragment {

	private String title;
	private String description;
	private String image;
	private static MinervaLruCache mLruCache;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mLruCache==null){
		mLruCache=new MinervaLruCache();
		}
		// Inflate selection view
		View v = inflater.inflate(R.layout.activity_main, container, false);
		// retrieve information from intent
		image = this.getArguments().getString("image");
		description = this.getArguments().getString("description");
		title = this.getArguments().getString("title");
		// locate widgets
		TextView titleTextView = (TextView) v.findViewById(R.id.txtViewTitle);
		final ImageView imageView = (ImageView) v.findViewById(R.id.imageViewPoint);

		TextView descriptionTextView = (TextView) v
				.findViewById(R.id.txtViewDesc);
		// set widget text
		titleTextView.setText(title);
		descriptionTextView.setText(description);
		// add global layout listener to allow bitmap processing after the view
		// has been drawn to get dimensions of the imageview
		imageView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					// Suppress version warnings, both versions are taken into account
					@SuppressWarnings("deprecation")
					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						// ensure valid values
						if (imageView.getHeight() > 0
								&& imageView.getWidth() > 0) {
							loadImage(imageView);
							// check version to remove listener with correct
							// method
							// version.
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
								imageView.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							else
								imageView.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
						}
					}
				});

		return v;
	}

	/**
	 * 
	 * load an image asynchronously into the fragment
	 * 
	 * @param imageView
	 *            - the ImageView to display the bitmap in
	 */
	private void loadImage(ImageView imageView) {
		Resources r = getResources();
		// get resource id
		int rID = (r
				.getIdentifier(image, "raw", getActivity().getPackageName()));
		// check cache for bitmap
		Bitmap bitmap = mLruCache.getCachedBitmap(String.valueOf(rID));
		// if bitmap is found in cache set it and return
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			return;
		}
		// otherwise if the same working isn't being carried out
		else if (BitmapProcessor.cancelPotentialWork(rID, imageView)) {
			// create and execute anew bitmapProcessor task
			final BitmapProcessor task = new BitmapProcessor(imageView, r,mLruCache);
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			task.execute(rID,size.x,size.y);
			// create and assign an async drawable to the Imageview
			final AsyncDrawable asyncDrawable = new AsyncDrawable(r, null, task);
			imageView.setImageDrawable(asyncDrawable);
		}
	}
}
