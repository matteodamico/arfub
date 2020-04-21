package it.dataSource;

import android.graphics.drawable.Drawable;

public class OpenMarker extends Marker{
	Drawable draw;
	public OpenMarker(String nome,String descr, int lat, int lon, int alt, Drawable draw) {
		// TODO Auto-generated constructor stub
		super(nome,descr, lat, lon, alt);
		this.draw=draw;
	}

	@Override
	public Drawable getDrawable() {
		// TODO Auto-generated method stub
		return null;
	}

}
