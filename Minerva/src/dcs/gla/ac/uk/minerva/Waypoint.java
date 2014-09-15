package dcs.gla.ac.uk.minerva;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author user
 *
 *class for describing a waypoint
 *
 */
public class Waypoint extends TourPoint implements Parcelable {
	private int id;
	private Double lat;
	private Double lng;
	private String audio;

	/**
	 * @param title - waypoint title
	 * @param description - description of waypoint
	 * @param file - filename of XML document containing Waypoints
	 * @param image - filename of image
	 * @param lat - latitude
	 * @param lng - longitude
	 * @param id - identifier
	 * @param audio - name of audio file
	 */
	public Waypoint(String title, String description,String image, Double lat, Double lon,
			 int id,String audio) {	
		super(title,description,image);
		this.id = id;
		this.lat = lat;
		this.lng = lon;
		this.setAudio(audio);
	}

	/**
	 * @return longitude
	 */
	public Double getLng() {
		return lng;
	}

	/**
	 * @param lng - longitude
	 */
	public void setLng(Double lon) {
		this.lng = lon;
	}

	/**
	 * @return latitude
	 */
	public Double getLat() {
		return lat;
	}

	/**
	 * @param lat - latitude
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * @return int identifier
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id - int identifier
	 */
	public void setId(int id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * @return name of audio file
	 */
	public String getAudio() {
		return audio;
	}

	/**
	 * @param audio - name of audio file
	 */
	public void setAudio(String audio) {
		this.audio = audio;
	}
	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(description);		
		dest.writeString(image);
		dest.writeInt(id);
		dest.writeValue(lat);
		dest.writeValue(lng);
		dest.writeString(audio);
	}

	/**
	 * 
	 */
	public static final Parcelable.Creator<Waypoint> CREATOR = new Parcelable.Creator<Waypoint>() {
		/* (non-Javadoc)
		 * @see android.os.Parcelable.Creator#createFromParcel(android.os.Parcel)
		 */
		public Waypoint createFromParcel(Parcel in) {
			return new Waypoint(in);
		}
		/* (non-Javadoc)
		 * @see android.os.Parcelable.Creator#newArray(int)
		 */
		public Waypoint[] newArray(int size) {
			return new Waypoint[size];
		}

	};

	/**
	 * @param in - Parcel object containing waypoint information
	 */
	public Waypoint(Parcel in) {
		super(in);
		id=in.readInt();
		Double d = Double.valueOf(0);
		lat=(Double) in.readValue(d.getClass().getClassLoader());
		lng=(Double) in.readValue(d.getClass().getClassLoader());
		audio=in.readString();	
	}
}