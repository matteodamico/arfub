package it.dataSource;

import java.util.List;

import android.graphics.drawable.Drawable;

public class My_POI extends Marker{
	
	public My_POI(String name, String desc, int latitude, int longitude,
			int altitude) {
		super(name, desc, latitude, longitude, altitude);
		// TODO Auto-generated constructor stub
	}

	private Drawable draw;
	//private String categoria;
	private List<String> immagini;
	private List<String> videos;
	private List<String> audios;

		public My_POI(String name,String desc, int latitude, int longitude, int altitude,List<String>URLimmagini,List<String> URLvideos,List<String> URLaudios,Drawable draw) {
			super(name,desc, latitude, longitude, altitude);
			this.draw=draw;
			//this.categoria=categoria;
			this.immagini=URLimmagini;
			this.videos=URLvideos;
			this.audios=URLaudios;		
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

		/*public String getCategoria() {
			return categoria;
		}

		protected void setCategoria(String categoria) {
			this.categoria = categoria;
		}*/

		public List<String> getURLimmagini() {
			return immagini;
		}

		protected void setURLimmagini(List<String> uRLimmagini) {
			immagini = uRLimmagini;
		}

		public List<String> getURLvideos() {
			return videos;
		}

		protected void setURLvideos(List<String> uRLvideos) {
			videos = uRLvideos;
		}

		public List<String> getURLaudios() {
			return audios;
		}

		protected void setURLaudios(List<String> uRLaudios) {
			audios = uRLaudios;
		}
		
	}