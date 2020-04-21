package it.dataSource;


//import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class TweetMarker extends Marker{
	// private Bitmap bitmap = null;

	Drawable draw;
	public TweetMarker(String name,String desc, int latitude, int longitude, int altitude,Drawable img) {
		super(name,desc, latitude, longitude, altitude);
		this.draw=img;
		
	}
	public TweetMarker(String name,String desc, double latitude, double longitude, int  altitude,Drawable img){
		super(name,desc,(int)latitude,(int)longitude,altitude);
		this.draw=img;
	}

	@Override
	public Drawable getDrawable(){
		return draw;
	}
	
	public void setDrawable(Drawable d){
		this.draw=d;
	}
	/*
	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
*/

}
