package dcs.gla.ac.uk.minerva;

import java.util.List;

import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import android.graphics.drawable.Drawable;

/**
 * 
 * Class to describe an item on the map this class alters the position of the
 * marker to align it to the lower left corner of the drawable
 * 
 * @author user
 */
public class CustomItemizedOverlay extends
		ItemizedOverlayWithFocus<OverlayItem> {

	/**
	 * @param aList
	 * @param pMarker
	 * @param pMarkerFocused
	 * @param pFocusedBackgroundColor
	 * @param aOnItemTapListener
	 * @param pResourceProxy
	 */
	public CustomItemizedOverlay(List<OverlayItem> aList, Drawable pMarker,
			Drawable pMarkerFocused, int pFocusedBackgroundColor,
			OnItemGestureListener<OverlayItem> aOnItemTapListener,
			ResourceProxy pResourceProxy) {
		super(aList, pMarker, pMarkerFocused, pFocusedBackgroundColor,
				aOnItemTapListener, pResourceProxy);
	}

	/**
	 * @param p
	 *            -Geo location of item
	 * @param title
	 *            - Title of item
	 * @param snippet
	 *            - String description of item
	 */
	public void addItem(GeoPoint p, String title, String snippet) {
		// create new item
		OverlayItem newItem = new OverlayItem(title, snippet, p);
		// align image
		newItem.setMarkerHotspot(OverlayItem.HotspotPlace.LOWER_LEFT_CORNER);
		// add image to overlay and populate it
		super.addItem(newItem);
		populate();
	}

}
