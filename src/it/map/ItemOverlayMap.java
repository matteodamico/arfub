package it.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;


public class ItemOverlayMap extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private double distance=0;
	private String type;
	//indice di riferimento in mapOverlays
	private int curr;

	
	public ItemOverlayMap(Drawable defaultMarker,double distanc,int indice,String type) {
		super(boundCenterBottom(defaultMarker));
		this.distance=distanc;
		this.curr=indice;
		this.type=type;
	}

	public ItemOverlayMap(Drawable defaultMarker,int indice,String type) {
		super(boundCenterBottom(defaultMarker));
		this.curr=indice;
		this.type=type;
	}	
	
	
	public ItemOverlayMap(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}
	protected String getType() {
		return type;
	}

	protected void setType(String type) {
		this.type = type;
	}


	@Override
	public int size() {
		return mOverlays.size();
	}
/*
	public void setDistance(double d){
		this.distance=d;
		OverlayItem item = mOverlays.get(0);
		Log.i("settata distanza",distance+"all elem"+item.getTitle());
	}

	public double getDistance(){
		return this.distance;
	}
*/
	
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}
	
	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
		//Toast.makeText(mContext, "titolo"+item.getTitle(), Toast.LENGTH_LONG).show();
		/*AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle("hola");
	  //item.getTitle()/item.getSnippet()
	  dialog.setMessage("homo");
	  dialog.show();*/
		
		MapManager2.setText(item.getTitle(),item.getSnippet(),item.getPoint(),curr,index);
		return true;
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) { 

		menu.setHeaderTitle("Menù Info"); 
		menu.add(Menu.NONE, 1, 1, "Notificami quando in zona"); 
		menu.add(Menu.NONE, 2, 2, "Prendi indicazioni"); 
	}

	public boolean onContextItemSelected(MenuItem item) { 
		int id = item.getItemId();
		switch (id) { 
		case 1: 
			Log.i("itemOverlay", "Avvio");
			//Toast.makeText(this, "attivazione Notifica in corso", Toast.LENGTH_SHORT).show(); 
			return true; 
		case 2: 
			Log.i("itemOverlay", "Avvio2");
			//Toast.makeText(this, "prendo indicazioni", Toast.LENGTH_SHORT).show(); 
			return true; 

		} 
		return false; 
	} 
	public void onContextMenuClosed(Menu menu) { 
		Log.i("itemOverlay", "Avvio");
		//Toast.makeText(this, "Menù chiuso!", Toast.LENGTH_SHORT).show(); 
	} 
}



