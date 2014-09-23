package dcs.gla.ac.uk.minerva;

import org.osmdroid.util.BoundingBoxE6;


public class Trail extends TourPoint{	
	private String file;
	private String map;
	private int latNorth, lngEast, latSouth, lonWest;	
	
	/**
	 * @param title - waypoint title
	 * @param description - description of waypoint
	 * @param file - filename of XML document containing Waypoints
	 * @param image - filename of image
	 */
	public Trail(String title, String description,String image,String file,String map,BoundingBoxE6 mapBounds) {
		super(title,description,image);
		this.file=file;
		this.setMap(map);
		this.latNorth=mapBounds.getLatNorthE6();
		this.latSouth=mapBounds.getLatSouthE6();
		this.lngEast=mapBounds.getLonEastE6();
		this.lonWest=mapBounds.getLonWestE6();
	}
	
	/**
	 * @return filename of XML document containing Waypoints
	 */
	public String getFile() {
		return file;
	}
	
	/**
	 * @param file - filename of XML document containing Waypoints
	 */
	public void setFile(String file) {
		this.file = file;
	}


	/**
	 * @return map filename
	 */
	public String getMap() {
		return map;
	}

	/**
	 * @param map set map filename
	 */
	public void setMap(String map) {
		this.map = map;
	}

	public int getLonWest() {
		return lonWest;
	}

	public void setLonWest(int lonWest) {
		this.lonWest = lonWest;
	}

	public int getLatSouth() {
		return latSouth;
	}

	public void setLatSouth(int latSouth) {
		this.latSouth = latSouth;
	}

	public int getLngEast() {
		return lngEast;
	}

	public void setLngEast(int lngEast) {
		this.lngEast = lngEast;
	}

	public int getLatNorth() {
		return latNorth;
	}

	public void setLatNorth(int latNorth) {
		this.latNorth = latNorth;
	}
}
