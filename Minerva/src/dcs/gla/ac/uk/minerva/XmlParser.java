package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * 
 * This is an abstract class for Parsing XML documents
 * 
 */
public abstract class XmlParser {
	// namespace constant placeholder, currently not required
	protected static final String ns = null;

	/**
	 * 
	 * Parse an XML input Stream
	 * 
	 * @param in
	 *            - InputStream of XML data
	 * @return ArrayList - returns an ArrayList of Trails
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public ArrayList<Object> parse(InputStream in)
			throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			// namespace processing turned off
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

			// initiate input and begin reading XML
			parser.setInput(in, null);
			parser.nextTag();

			// return ArrayList processed by ReadFeed
			return readFeed(parser);
		} finally {
			// close stream
			in.close();
		}
	}

	/**
	 * 
	 * read a feed of entries
	 * 
	 * @param parser
	 *            -XmlPullParser
	 * @return entries - ArrayList of Objects read
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	protected abstract ArrayList<Object> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException;

	/**
	 * 
	 * Read a single entry
	 * 
	 * @param parser
	 *            - XmlPullParser
	 * @return - Object
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	protected abstract Object readEntry(XmlPullParser parser)
			throws XmlPullParserException, IOException;

	/**
	 * 
	 * skip unnecessary content
	 * 
	 * @param parser
	 *            - XmlPullParser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	protected void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		// ensure process starts on a start tag
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			// throw exception if called from wrong state
			throw new IllegalStateException();
		}
		int depth = 1;
		// until depth = 0 continue to skip data
		while (depth != 0) {
			switch (parser.next()) {
			// decrement depth on closing tag
			case XmlPullParser.END_TAG:
				depth--;
				break;
			// increment on start tag
			case XmlPullParser.START_TAG:
				depth++;
			}
		}
	}

	/**
	 * @param parser
	 *            - XmlPullParser
	 * @param tagName
	 *            - String
	 * @return String - content from within start and end tag
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	protected String readTag(XmlPullParser parser, String tagName)
			throws IOException, XmlPullParserException {
		// parse information within start and end tag
		parser.require(XmlPullParser.START_TAG, ns, tagName);
		String content = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, tagName);
		return content;
	}

	/**
	 * 
	 * read and trim plain text from the XML document
	 * 
	 * @param parser
	 *            - XmlPullParser
	 * @return String containing plain text content from XML document
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		// process strings
		String text = "";
		// if next read is text data
		if (parser.next() == XmlPullParser.TEXT) {
			// process text
			text = parser.getText();
			parser.nextTag();
		}
		text = text.trim();
		return text;
	}
	
	/**
	 * 
	 * This method removes file extensions if present to ensure compatibility as a raw resource.
	 * 
	 * @param filename
	 * @return edited filename suitable for use in raw folder
	 */
	protected String removeExtension(String filename){
		if(filename!=null&&filename.contains(".")){
			int i=filename.indexOf(".");
			filename=filename.substring(0, i+1);
		}
		return filename;
	}

}
