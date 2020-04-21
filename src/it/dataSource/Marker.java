package it.dataSource;



import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;

public abstract class Marker implements Comparable<Marker> {

	//Unique identifier of Marker
	protected String name = null;
	//Marker's physical location

	protected String desc=null;

	//variabile per sapere se un marker ? visible o meno
	private boolean visible;


	protected int color = Color.WHITE;
	private int lat;
	private int lon;
	private int alt;

	public Marker(String name, String desc, int latitude, int longitude, int altitude) {
		this.name = name;
		this.lat=latitude;
		this.lon=longitude;
		this.alt=altitude;
		this.desc=desc;



	}

	public void setName(String nam){
		this.name=nam;
	}

	public String getName(){
		return name;
	}
	public abstract Drawable getDrawable() ;

	public int getLatitude() {
		return this.lat;
	}

	public int getAltitude() {
		return this.alt;
	}

	public int getLongitude() {
		return this.lon;
	}
	public String getDescr(){
		return this.desc;
	}
	public void setDescr(String de){
		this.desc=de;
	}
	/*
  public double getLatitude() {
    return geoLoc.getLatitudeE6();
  }

  public double getLongitude() {
    return geoLoc.getLongitudeE6();
  }

  public double getAltitude() {
    return geoLoc.getAltitudine();
  }
	 */
	//TO DO: distanza dalla mia locatione a questo marker
	public  float getDistance(Location location) {
		float[] dist=new float[3];
		Location.distanceBetween(getLatitude(), getLongitude(), location.getLatitude(), location.getLongitude(), dist);
		float distance = dist[0];
		return  distance;
	}



	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public int compareTo(Marker another,Location myLoc) {
		return Double.compare(this.getDistance(myLoc), another.getDistance(myLoc));
	}

	@Override
	public boolean equals (Object marker) {
		return this.name.equals(((Marker)marker).getName());
	}


	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public int compareTo(Marker another) {
		return this.name.compareTo(another.getName());
	}
}
