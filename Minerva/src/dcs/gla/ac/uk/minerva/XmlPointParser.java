package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author Paul Cairney
 * 
 *         This class extracts information on waypoints from an xml document
 * 
 */
public class XmlPointParser extends XmlParser {
	// Currently unused parameter
	private static final String ns = null;
	String image;
	String audio;
	Double lat;
	Double lon;
	int id;

	/* (non-Javadoc)
	 * @see dcs.gla.ac.uk.minerva.XmlParser#readFeed(org.xmlpull.v1.XmlPullParser)
	 */
	@Override
	protected ArrayList<Object> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		id = 0;
		ArrayList<Object> entries = new ArrayList<Object>();
		// define start of list
		parser.require(XmlPullParser.START_TAG, ns, "audio_tour");
		while (parser.next() != XmlPullParser.END_TAG) {
			// Search for entries
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// locate an entry in the document
			if (name.equals("point")) {
				// parse entry
				image = parser.getAttributeValue(ns, "image");
				audio = parser.getAttributeValue(ns, "audio");
				id = Integer.parseInt(parser.getAttributeValue(ns, "id"));
				try {
					lon = Double.parseDouble(parser.getAttributeValue(ns,
							"longitude"));
					lat = Double.parseDouble(parser.getAttributeValue(ns,
							"latitude"));
				} catch (NumberFormatException e) {
					lon = null;
					lat = null;
				}
				Waypoint p = readEntry(parser);
				entries.add(p);
			} else {
				// ignore data
				skip(parser);
			}
		}
		return entries;
	}

	/* (non-Javadoc)
	 * @see dcs.gla.ac.uk.minerva.XmlParser#readEntry(org.xmlpull.v1.XmlPullParser)
	 */
	protected Waypoint readEntry(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		//initialise variables
		String name = null;
		String description = null;
		// define start tag
		parser.require(XmlPullParser.START_TAG, ns, "point");
		// while next is not at an end tag
		while (parser.next() != XmlPullParser.END_TAG) {
			// Restart search if next is not a start tag
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			// get tag name
			String tag = parser.getName();
			//extract information form selected tags
			if (tag.equals("title")) {
				name = readTag(parser, "title");
			} else if (tag.equals("summary")) {
				description = readTag(parser, "summary");
			} else {
				skip(parser);
			}
		}
		return new Waypoint(name, description,image, lat, lon,  id, audio);
	}

}
