package it.dataSource;

import java.util.List;
import android.graphics.drawable.Drawable;

public class FubMarker extends Marker {
private Drawable draw;
private String categoria;
private List<String> URLimmagini;
private List<String> URLvideos;
private List<String> URLaudios;

	public FubMarker(String name,String desc, int latitude, int longitude, int altitude,String categoria,List<String>URLimmagini,List<String> URLvideos,List<String> URLaudios,Drawable draw) {
		super(name,desc, latitude, longitude, altitude);
		this.draw=draw;
		this.categoria=categoria;
		this.URLimmagini=URLimmagini;
		this.URLvideos=URLvideos;
		this.URLaudios=URLaudios;		
}

	@Override
	public Drawable getDrawable() {
		return this.draw;
	}
	protected Drawable getDraw() {
		return draw;
	}

	protected void setDraw(Drawable draw) {
		this.draw = draw;
	}

	public String getCategoria() {
		return categoria;
	}

	protected void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public List<String> getURLimmagini() {
		return URLimmagini;
	}

	protected void setURLimmagini(List<String> uRLimmagini) {
		URLimmagini = uRLimmagini;
	}

	public List<String> getURLvideos() {
		return URLvideos;
	}

	protected void setURLvideos(List<String> uRLvideos) {
		URLvideos = uRLvideos;
	}

	public List<String> getURLaudios() {
		return URLaudios;
	}

	protected void setURLaudios(List<String> uRLaudios) {
		URLaudios = uRLaudios;
	}
	
}