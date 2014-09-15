package dcs.gla.ac.uk.minerva;

import android.os.Parcel;

/**
 * @author user
 *
 *Abstract class to define a basic display point
 *
 */
public abstract class TourPoint {
	protected String title;
	protected String description;
	protected String image;
	
	/**
	 * 
	 * constructor using parameters
	 * 
	 * @param title - point title
	 * @param description - description of point
	 * @param image - name of image file
	 */
	protected TourPoint(String title, String description,String image) {
		this.title = title;
		this.description = description;
		this.image=image;
	}
	/**
	 * 
	 *Constructor for reconstructing an object from a parcel
	 * 
	 * @param in - Parcel object
	 */
	public TourPoint(Parcel in) {
		title=in.readString();
		description=in.readString();
		image=in.readString();
	}
	/**
	 * @return title of point
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title - title of point
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return - description of point
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description - description of point
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return - filename of image
	 */
	public String getImage() {
		return image;
	}
	/**
	 * @param image - filename of image
	 */
	public void setImage(String image) {
		this.image = image;
	}
}
