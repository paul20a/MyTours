package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.util.ArrayList;

import org.osmdroid.util.BoundingBoxE6;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * 
 * This class extracts information on a trail
 * 
 */
public class XmlTrailParser extends XmlParser {
	private String image,map;
	private BoundingBoxE6 mapBounds;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dcs.gla.ac.uk.minerva.XmlParser#readFeed(org.xmlpull.v1.XmlPullParser)
	 */
	@Override
	protected ArrayList<Object> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<Object> entries = new ArrayList<Object>();

		// define start of list
		parser.require(XmlPullParser.START_TAG, ns, "data");
		// while next is not at an end tag
		while (parser.next() != XmlPullParser.END_TAG) {
			// Restart search if next is not a start tag
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			// get tag name
			String name = parser.getName();
			// check for correct entry
			if (name.equals("trail")) {
				// get image name
				image = parser.getAttributeValue(ns, "image");
				map = parser.getAttributeValue(ns, "map");
				final double latNE, lngNE, latSW, lngSW;
				latNE = Double.parseDouble(parser.getAttributeValue(ns,
						"latitudeN"));
				lngNE = Double.parseDouble(parser.getAttributeValue(ns,
						"longitudeE"));
				latSW = Double.parseDouble(parser.getAttributeValue(ns,
						"latitudeS"));
				lngSW = Double.parseDouble(parser.getAttributeValue(ns,
						"longitudeW"));
				mapBounds = new BoundingBoxE6(latNE, lngNE,latSW, lngSW);
				// add + parse entry
				entries.add(readEntry(parser));
			} else {
				// ignore data
				skip(parser);
			}
		}
		return entries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dcs.gla.ac.uk.minerva.XmlParser#readEntry(org.xmlpull.v1.XmlPullParser)
	 */
	@Override
	protected Trail readEntry(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		// initialise variables
		String title = null;
		String description = null;
		String file = null;
		// define start tag
		parser.require(XmlPullParser.START_TAG, ns, "trail");
		// while next is not at an end tag
		while (parser.next() != XmlPullParser.END_TAG) {
			// Restart search if next is not a start tag
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			// get tag name
			String tag = parser.getName();
			// extract information form selected tags
			if (tag.equals("title")) {
				title = readTag(parser, "title");
			} else if (tag.equals("summary")) {
				description = readTag(parser, "summary");
			} else if (tag.equals("file")) {
				file = readTag(parser, "file");
				file=removeExtension(file);
			} else {
				skip(parser);
			}
		}
		return new Trail(title, description, image, file,map,mapBounds);
	}
}
