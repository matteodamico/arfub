package it.dataSource;

import android.graphics.drawable.Drawable;

public class WikipediaMarker extends Marker{
	Drawable draw;
	String url;
	
	
	public WikipediaMarker(String name, String descr,int latitude, int longitude, int altitude,Drawable img,String url) {
		super(name, descr, latitude, longitude, altitude);
		this.draw=img;
		this.url=url;
	}

	@Override
	public Drawable getDrawable(){
		return draw;
	}
	
	public void setDrawable(Drawable d){
		this.draw=d;
	}
	public String getUrl() {
		return url;
	}

	protected void setUrl(String url) {
		this.url = url;
	}

}
