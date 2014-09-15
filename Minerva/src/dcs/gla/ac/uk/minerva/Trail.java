package dcs.gla.ac.uk.minerva;


public class Trail extends TourPoint{	
	private String file;
	
	/**
	 * @param title - waypoint title
	 * @param description - description of waypoint
	 * @param file - filename of XML document containing Waypoints
	 * @param image - filename of image
	 */
	public Trail(String title, String description,String image,String file) {
		super(title,description,image);
		this.file=file;
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
}
