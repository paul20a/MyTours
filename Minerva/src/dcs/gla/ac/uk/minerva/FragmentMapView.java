package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.GroundOverlay;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Toast;

public class FragmentMapView extends Fragment implements
		OnItemGestureListener<OverlayItem> {
	private MapView minervaMapView;
	private CustomItemizedOverlay customItemizedOverlay = null;
	private MyLocationNewOverlay myLocationOverlay;
	private double latMin = 1000, latMax = -1000, lngMin = 1000,
			lngMax = -1000;
	private Drawable marker;
	private GroundOverlay groundOverlay;
	private MapLoaderTask mTask;
	private boolean firstRun;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTask = new MapLoaderTask();
		mTask.execute();
		firstRun = true;
	}

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
		// inflate view
		View v = inflater.inflate(R.layout.map_view, container, false);
		groundOverlay = new GroundOverlay(getActivity());
		// create the map
		StartMap(v);
		return v;
	}

	/**
	 * 
	 * setup the MapView, this will initialise the map, add markers to it,
	 * manage controls, and control default zoom and center locations
	 * 
	 * @param currentView
	 *            - the current fragment view
	 */
	private void StartMap(View currentView) {
		// get the MapView
		minervaMapView = (MapView) currentView.findViewById(R.id.openmapview);
		// turn built in zoom controls and pinch zooming on
		minervaMapView.setBuiltInZoomControls(true);
		minervaMapView.setMultiTouchControls(true);
		// disable Internet access
		minervaMapView.setUseDataConnection(false);
		final MapController minervaMapController = (MapController) minervaMapView
				.getController();
		// create a custom marker
		createMarkerDrawable();
		// create a default resource proxy
		ResourceProxy resourceProxy = new DefaultResourceProxyImpl(
				getActivity().getApplicationContext());
		// allow gps to locate position
		setupMyLocationGps();
		// process items
		ArrayList<OverlayItem> itemList = processItems();

		// create new itemised overlay and add to mapView
		customItemizedOverlay = new CustomItemizedOverlay(itemList, marker,
				marker, 0, this, resourceProxy);
		minervaMapView.getOverlays().add(customItemizedOverlay);

		// this is a work around for centring issue in osmdriod v4.2
		minervaMapView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					// Suppress version warnings
					@SuppressWarnings("deprecation")
					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						// set center point to mid point of trail and zoom to it
						minervaMapController.zoomToSpan(
								(int) ((latMax - latMin) * 1E6),
								(int) ((lngMax - lngMin) * 1E6));
						minervaMapController.zoomIn();

						// check version to remove listener with correct method
						// version.
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
							minervaMapView.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						else
							minervaMapView.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
					}
				});
	}

	/**
	 * 
	 * process a list of Points into a list of overlay items minimum and maximum
	 * latitudes and longitude are also worked out here
	 * 
	 * @return ArrayList of Overaly items
	 */
	private ArrayList<OverlayItem> processItems() {
		ArrayList<Object> pList = ((ActivitySelect) getActivity()).getpList();
		ArrayList<OverlayItem> itemList = new ArrayList<OverlayItem>();
		// for each item in list add it to items array
		for (int i = 0; i < pList.size(); i++) {
			// get the Waypoint
			Waypoint p = (Waypoint) pList.get(i);
			// extract coordinates
			Double newLat = p.getLat();
			Double newLng = p.getLng();
			// if not null coordinates
			if (newLng != null && newLat != null) {
				// create a new Geopint
				GeoPoint mPoint = new GeoPoint((int) (newLat * 1E6),
						(int) (newLng * 1E6));
				// check for min and max latitude
				if (newLat < latMin) {
					latMin = newLat;
				} else if (newLat > latMax) {
					latMax = newLat;
				}
				// check for min and max longitude
				if (newLng < lngMin) {
					lngMin = newLng;
				} else if (newLng > lngMax) {
					lngMax = newLng;
				}
				// add new overlay item into item list
				itemList.add(new OverlayItem(Integer.toString(i), p.getTitle(),
						"", mPoint));
			}

		}
		return itemList;
	}

	private void createGroundOverlay() {
		// position overlay
		BoundingBoxE6 bounds = new BoundingBoxE6(55.967402, -3.202895,
				55.962836, -3.214181);
		GeoPoint eastPoint = new GeoPoint(bounds.getLatNorthE6(),
				bounds.getLonEastE6());
		GeoPoint westPoint = new GeoPoint(bounds.getLatNorthE6(),
				bounds.getLonWestE6());
		int length = eastPoint.distanceTo(westPoint);
		GeoPoint centerPoint = bounds.getCenter();
		minervaMapView.setScrollableAreaLimit(bounds);
		groundOverlay.setPosition(centerPoint);
		groundOverlay.setDimensions(length);
		minervaMapView.getOverlays().add(0, groundOverlay);
	}

	/**
	 * creates a custom marker to display items on the map
	 */
	private void createMarkerDrawable() {
		// create a new marker for the map
		marker = this.getResources().getDrawable(R.drawable.flag);
		int markerWidth = marker.getIntrinsicWidth();
		int markerHeight = marker.getIntrinsicHeight();
		marker.setBounds(0, markerHeight, markerWidth, 0);
	}

	/**
	 * setup Location Overlay, this code enables the user location to display on
	 * the map
	 */
	private void setupMyLocationGps() {
		myLocationOverlay = new MyLocationNewOverlay(getActivity(),
				minervaMapView);
		minervaMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableMyLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// stop gps tracking when view loses focus
		myLocationOverlay.disableMyLocation();
		GroundOverlay groundOverlay = (GroundOverlay) minervaMapView
				.getOverlays().get(0);
		groundOverlay.setImage(null);
		firstRun = false;
		mTask.cancel(true);
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// enable gps tracking when view comes back into focus
		myLocationOverlay.enableMyLocation();
		createGroundOverlay();
		if (!firstRun) {
			mTask = new MapLoaderTask();
			mTask.execute();
		}
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener#
	 * onItemLongPress(int, java.lang.Object)
	 */
	@Override
	public boolean onItemLongPress(int arg0, OverlayItem arg1) {
		// TODO Currently no different from single tap, possibly swap with
		// single tap and allow single tap to pop up?
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener#
	 * onItemSingleTapUp(int, java.lang.Object)
	 */
	@Override
	public boolean onItemSingleTapUp(int arg0, OverlayItem item) {
		Toast.makeText(getActivity(), item.getTitle() + "\n",
				Toast.LENGTH_SHORT).show();
		Intent detailIntent = new Intent(getActivity(), ActivityMain.class);
		// Need to update this class is too dependent on ActivitySelect
		detailIntent.putExtra("pList",
				((ActivitySelect) getActivity()).getpList());
		detailIntent.putExtra("pos", Integer.parseInt(item.getUid()));
		startActivity(detailIntent);
		return true;
	}

	public Point getScreenDimensions() {
		// get screen dimensions
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}

	class MapLoaderTask extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... params) {
			// get screen dimensions
			Point size = getScreenDimensions();
			int width = size.x;
			int height = size.y;

			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(getActivity().getResources(),
					R.raw.map, options);
			options.inJustDecodeBounds = false;
			final int h = options.outHeight;
			final int w = options.outWidth;
			int sampleSize = 1;

			if (h > height || w > width) {
				while (((h) / sampleSize) > height
						&& ((w) / sampleSize) > width) {
					sampleSize *= 2;
				}
			}
			options.inSampleSize = sampleSize;
			Log.d("SampleSize", "" + sampleSize);
			if (!this.isCancelled())
				return BitmapFactory.decodeResource(getActivity()
						.getResources(), R.raw.map, options);
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			if (!this.isCancelled()) {
				groundOverlay.setImage(new BitmapDrawable(getActivity()
						.getResources(), result));
				createGroundOverlay();
			}
		}
	}
}