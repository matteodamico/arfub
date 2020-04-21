package it.map;

import it.arFub.R;
import it.arFub.SingletonParametersBridge;
import it.dataSource.FubMarker;

import it.dataSource.WikipediaMarker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.text.format.Time;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup.LayoutParams;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

public class MapManager2 extends MapActivity  {

	public static TextView textCoordinate,textDescr,textTitolo,textDistance;
	public ImageButton moreInfo,showInfo;
	public ImageView imageView;

	private GeoPoint point;

	private String testo;

	//Riferimento alla MapView
	private MapView mapView;

	//Riferimento al MapController
	MapController mapController;

	// Riferimento al LocationManager
	LocationManager locationManager;

	//Riferimento all'Overlay Manager
	OverlayManager overMan;

	//Rifermimento al controller delle preferenze
	SharedPreferences prefs;

	//Raggio di ricerca
	int raggio;
	Time t2,t1;
	long time1,time2;

	//copia temporanea di Location(location2--punto di riferimento quando scaricati i dati---location 3 punto di riferimento che si muove dopo 5 metri dal mio spostamento)
	Location location2,location3;
	private static Location loc4;
	//indice di riferimento dell itemOverlayMap in Map Overlays
	private static int indiceMapOverlays;
	//indice di riferimento dell item in mOverlays
	private static int indicemOverlays=-1;
	View infoLayout,mappaLayout; 
	LayoutParams params2,params3;



	/*Vari textView in caso si volesse  visualizzare separatamente le info
	private TextView tvLatitudine;
	private TextView tvLongitudine;
	private TextView tvVelocita;
	private TextView tvQuota;
	private TextView tvAccuratezza;
	 */

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)	{

		super.onCreate(savedInstanceState);
		Log.i("mapMan2", "INIZIO SET UP::: MAPPA");
		setContentView(R.layout.mappaview2);

		// Otteniamo il riferimento alla MapView
		mapView = (MapView)findViewById(R.id.mapView1);
		Log.i("mapMan2", "preso riferimento mappa");

		// Otteniamo il riferimento al controller
		mapController = mapView.getController();

		// Rendiamo la mappa cliccabile e quindi ne permettiamo il pan
		mapView.setClickable(true);

		// Utiliziamo i controlli predefiniti per la gestione delle operazioni di zoom
		mapView.setBuiltInZoomControls(true);

		// Impostiamo la distanza dello Zoom a 15 (valori validi da 1 a 21).
		mapController.setZoom(15);
		Log.i("mapMan2", "settato zoom e mappa");
		location2=null;
		location3=null;
		Log.i("mapMan2", "FINE CREAZIONE::MAPPA");
		Log.i("mapMan2", "INIZIO PRENDO POSIZIONE:::location manager");

		// Otteniamo il riferimento al LocationManager

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		// Registriamo il LocationListener al provider GPS
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
		Context cont =getApplicationContext();
		Drawable drawable =null;
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, mapView);
		//Drawable drawable = this.getResources().getDrawable(R.drawable.iconsettings);
		overMan= new OverlayManager(myLocationOverlay,mapView,drawable,cont);
		Log.i("mapMan2", "creato overMan");


		textCoordinate= (TextView)this.findViewById(R.id.textCoordinate);
		textTitolo= (TextView)this.findViewById(R.id.textViewTitolo);
		textDescr= (TextView)this.findViewById(R.id.textViewDescr);
		textDistance= (TextView)this.findViewById(R.id.textDistance);
		/*tvLatitudine = (TextView)this.findViewById(R.id.tvLatitudine);
		tvLongitudine = (TextView)this.findViewById(R.id.tvLongitudine);
		tvVelocita = (TextView)this.findViewById(R.id.tvVelocita);
		tvQuota = (TextView)this.findViewById(R.id.tvQuota);
		tvAccuratezza = (TextView)this.findViewById(R.id.tvAccuratezza);*/
		moreInfo = (ImageButton) findViewById(R.id.moreInfoButton);
		showInfo = (ImageButton) findViewById(R.id.showInfo);
		imageView= (ImageView) findViewById(R.id.imageView1);


		infoLayout= findViewById(R.id.infolayout);
		mappaLayout=findViewById(R.id.mappalayout);


		t1=new Time();
		time1=t1.toMillis(true);
		//Prendo riferimento alle preferenze
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		raggio=5;

		params2= new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, .0f);
		params3= new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 30f);
		//Gestione Bottoni sul layout
		View.OnClickListener gestoreClick = new View.OnClickListener() {
			public void onClick(View view) { 
				Bundle bundle;
				switch(view.getId()){

				case R.id.moreInfoButton:
					Log.i("mapManager2", "chiamato more info"+mapView.getOverlays().size());
					try{
						ItemOverlayMap m=(ItemOverlayMap)mapView.getOverlays().get(indiceMapOverlays);
						Log.i("mapMan2", "prendo classe: "+m.getType());
						int p;						

						if(m.getType().compareTo("it.dataSource.FubMarker")==0){

							//se wikipedia , openstreet map , twitter acceso
							boolean PrefWikipedia,PrefTwitter, PrefOpenStreetMap;
							PrefWikipedia= prefs.getBoolean("WIKIPEDIA", false);
							PrefTwitter= prefs.getBoolean("TWITTER", false);
							PrefOpenStreetMap = prefs.getBoolean("OPEN_STREET_MAP",false);
							//PrefPersonal=prefs.getBoolean("Personal_POIs", false);
							if(PrefWikipedia||PrefTwitter||PrefOpenStreetMap)
								p=indiceMapOverlays-2;
							else
								p=indiceMapOverlays-1;

							testo ="scarico maggiori informazioni";
							Toast.makeText(MapManager2.this,testo,Toast.LENGTH_SHORT).show();
							FubMarker fbm = (FubMarker)overMan.getWebMarkersFub().get(p);
							Log.i("mapMan2", "clickato more info nomeRisorsa:"+fbm.getName());
							bundle= new Bundle();
							bundle.putString("destination","ResultView");
							SingletonParametersBridge.getInstance().addParameter("FubMarker", fbm);;

							startSubActivity(bundle);
							break;
						}
						else if (m.getType().compareTo("it.dataSource.WikipediaMarker")==0){
							//lancia intent wikipage;
							//Toast.makeText(MapManager2.this,"Al momento Non ci sono info",Toast.LENGTH_SHORT).show();
							Log.i("mapMan2", "prendo : "+overMan.getWebMarkersWiki().get(indicemOverlays).getName());
							WikipediaMarker wm=(WikipediaMarker)overMan.getWebMarkersWiki().get(indicemOverlays);
							String url ="http://"+wm.getUrl();
							Intent myIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
							startActivity(myIntent);
							/*bundle= new Bundle();
							bundle.putString("destination","WebBrowser");
							bundle.putString("url",url);
							startSubActivity(bundle); */
							break;
						}
						else
							Toast.makeText(MapManager2.this,"Non ci sono ulteriori informazioni",Toast.LENGTH_SHORT).show();
					}
					catch(Exception e){
						Toast.makeText(MapManager2.this,"Non ci sono ulteriori info",Toast.LENGTH_SHORT).show();}
					break;
					//Inutile tanto per
				case R.id.showInfo:
					Log.i("mapManger", "show info");
					if(infoLayout.isShown()){
						infoLayout.setVisibility(View.INVISIBLE);
						Log.i("mapManger", "cambiata visibilità layout");
						mappaLayout.setLayoutParams(params2);

					}else{
						infoLayout.setVisibility(View.VISIBLE);
						mappaLayout.setLayoutParams(params3);


					}break;
				}//end switch
			}//end metodo
		};//end on click listener
		moreInfo.setOnClickListener(gestoreClick);
		showInfo.setOnClickListener(gestoreClick);


	}//end create

	LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			// Aggiorna il marker della mappa
			mapView.invalidate();

			// Aggiorna la location
			double geoLat = location.getLatitude()*1E6;
			double geoLng = location.getLongitude()*1E6;
			//double geoAlt = location.getAltitude();


			//prende il tempo in millisecondi
			t2=new Time();
			time2=t2.toMillis(true);

			point = new GeoPoint((int)geoLat, (int)geoLng);


			//mapController.setCenter(point);
			Log.i("La locazione passata dal Provider e' ", ""+(int)geoLat);

			aggiornaTextViewCoordinate(location);

			//in caso di avvio
			if(location3==null){
				location3=location;
				location2=location;
				mapController.animateTo(point);
				loc4=location3;
				time1=time2;
				overMan.aggiungiOverlaysWeb(prefs,location,raggio);
			}

			Log.i("mapMan2", "la distanza e di "+ overMan.getDistance(point, location3));

			//aggiorna il pallino con la mia posizione ogni 5 metri di distanza
			if(overMan.getDistance(point,location3)>=5){
				//if((geoLoc.getDistance(geoLoc2)>=5)||time1-time2>15000){
				Log.i("mapMan2", "distanza maggiore di 5:");
				mapController.animateTo(point);
				location3=location;
				loc4=location3;
				aggiornaDistanza(location3);
				time1=time2;

			}



			if(overMan.getDistance(point,location2)>=50){
				//if((geoLoc.getDistance(geoLoc2)>=50)||time1-time2>15000) {
				Log.i("mapMan2", "distanza tra i 2 punti:");
				//Aggiungiamo tutti gli overlays scaricati dal web
				overMan.aggiungiOverlaysWeb(prefs,location,raggio);

				location2=location;
			}
		}

		private void aggiornaDistanza(Location loc) {
			if(indicemOverlays!=-1){
				if(((ItemOverlayMap)mapView.getOverlays().get(indiceMapOverlays))!=null){
					ItemOverlayMap item=(ItemOverlayMap)mapView.getOverlays().get(indiceMapOverlays);
					Log.i("mapMan2", "elemento da settare n: "+indiceMapOverlays+item.createItem(indicemOverlays).getTitle());
					double d=overMan.getDistance(item.createItem(indicemOverlays).getPoint(), loc);
					textDistance.setText("< si trova a "+(int)d+" m >");
				}}
			else return;
		}

		private void aggiornaTextViewCoordinate(Location location) {
			// TODO Auto-generated method stub
			String latitudine,longitudine,velocita,quota,accuratezza;
			latitudine=Double.toString(getRound(location.getLatitude(), 5));
			longitudine=Double.toString(getRound(location.getLongitude(), 5));
			velocita=Double.toString(getRound(location.getSpeed()*3.6, 1))+" km/h";
			quota=Integer.toString((int)location.getAltitude())+" m";
			accuratezza=Integer.toString((int)location.getAccuracy())+" m";
			textCoordinate.setText("("+latitudine+"°,"+ longitudine+"°)\n"+ "Velocita "+velocita+"\n"+"Quota "+quota+"\n"+"Accuratezza "+accuratezza);
			/*
			tvLatitudine.setText(Double.toString(getRound(location.getLatitude(), 5)));
			tvLongitudine.setText(Double.toString(getRound(location.getLongitude(), 5)));
			tvVelocita.setText(Double.toString(getRound(location.getSpeed()*3.6, 1))+" km/h");
			tvQuota.setText(Integer.toString((int)location.getAltitude())+" m");
			tvAccuratezza.setText(Integer.toString((int)location.getAccuracy())+" m");*/

		}

		@Override
		public void onProviderDisabled(String provider) {
			testo="Attenzione "+provider +" disattivato, attivarlo dal menù del telefono.";
			Toast.makeText(MapManager2.this,testo, Toast.LENGTH_LONG).show();
			//Toast.makeText(MapManager2.this,"onProviderDisabled "+provider, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(MapManager2.this, provider+" attivato. ", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status,Bundle extras) {
			//	Toast.makeText(MapManager2.this,"onStatusChanged "+provider+" status: "+status, Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void onPause() {
		super.onPause();
	}
	@Override
	public void onStop() {
		super.onPause();
		// Stoppa Listener delle posizioniprotected void onStop() 
		//stopAscoltoGps();
	}
	@Override
	public void onDestroy() {
		super.onPause();
		stopAscoltoGps();
	}

	protected void stopAscoltoGps() {
		locationManager = (LocationManager) getSystemService(	LOCATION_SERVICE	);
		if(locationManager != null){
			locationManager.removeUpdates(locationListener);
			overMan.diattivaMyLocation();
		}  
	}

	@Override
	protected void onResume() {
		super.onResume();
	
		/*	GeoPoint lastGeoPoint= getLastGeoPoint();
		mapController.animateTo(lastGeoPoint);*/

		// Verifichiamo se il GPS e abilitato altrimenti avvisiamo l'utente
		if(!locationManager.isProviderEnabled("gps")){
			Toast.makeText(this, "GPS è attualmente disabilitato. E' possibile abilitarlo dal menu impostazioni.", Toast.LENGTH_LONG).show();}
		overMan.aggiungiOverlayMyLocation();
		Log.i("mapMan2", "FINE PRENDO POSIZIONE::overlay mia posizione");

		//overMan.attivaBussola();

		risettaParametriSettaggi(prefs);
		//Inizializziamo i settaggi iniziali della mappa(bussola,radar,gps)
		overMan.attivaMyLocation();		
		//risettaParametriLayer(prefs);
		
	}

	private void risettaParametriLayer(SharedPreferences prefs) {
		overMan.aggiungiOverlaysWeb(prefs,location2,raggio);
	}

	private void risettaParametriSettaggi(SharedPreferences prefs) {
		// TODO Auto-generated method stub

		boolean PrefBUSSOLA= prefs.getBoolean("BUSSOLA", true);
		boolean PrefNETWORK= prefs.getBoolean("NETWORK", true);
		boolean PrefGPS= prefs.getBoolean("GPS", true);
		boolean PrefCOORDINATE = prefs.getBoolean("COORDINATE",true);
		//raggio= prefs.getInt("raySearch", 2); // in km

		if(PrefBUSSOLA)
			overMan.attivaBussola();
		else
			overMan.disattivaBussola();
		if(PrefNETWORK)
		{}
		else
		{}
		if (PrefGPS)
			riattivaAscoltoGps();
		else
			stopAscoltoGps();
		if(PrefCOORDINATE)
			attivaTextCoordinate();
		else
			disattivaTextCoordinate();



	}
	private void disattivaTextCoordinate() {
		textCoordinate.setVisibility(View.INVISIBLE);
	}

	private void attivaTextCoordinate() {
		textCoordinate.setVisibility(View.VISIBLE);
	}

	protected void riattivaAscoltoGps() {
		//settiamo l'ultima posizione conosciuta come quella attuale 
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (lastKnownLocation != null) 
				setCurrentLocation(lastKnownLocation);
			// Registriamo il LocationListener al provider GPS
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);}
	}

	private void setCurrentLocation(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		int latitudeE6 = (int) Math.floor(latitude * 1.0E6);
		int longitudeE6 = (int) Math.floor(longitude * 1.0E6);
		GeoPoint geoPoint = new GeoPoint(latitudeE6, longitudeE6);
		mapController.setCenter(geoPoint);
	}

	private GeoPoint getLastGeoPoint() {
		if(point!=null)
			return point;
		else return null;
	}

	public static double getRound(double x, int digits){
		double powerOfTen = Math.pow(10, digits);
		return ((double)Math.round(x * powerOfTen) / powerOfTen);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	//creazione e visualizzazione del menu
	public boolean onCreateOptionsMenu(Menu menu)	{
		new MenuInflater(getApplication()).inflate(R.menu.menu_of_map, menu);
		return(super.onPrepareOptionsMenu(menu));
	}

	//ascoltatore del menu selezionato
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch ( item.getItemId() ) {
		//INIZIO PULSANTI MENU DELLA CLASSE MAPPA
		case R.id.settings2: //se scelto menu: SETTINGS
			startActivity(new Intent(this,it.map.SettingsMap.class));
			return true;
		case R.id.toArView: // se scelto menu: MODALITA' AR
			startActivity(new Intent(this, it.ar.AR_Viewer.class));
			finish();
			return true;
		case R.id.mapStyle:
			Log.i("map manager2","pigiato il bottone map style");
			return true;
		case R.id.strati:
			startActivity(new Intent(this, it.map.Layars.class));
			Log.i("map manager 2","cliccato strati");
			return true;
		case R.id.MyPosition:
			testo="Attenzione non trovo la tua posizione.";
			try{
				point=getLastGeoPoint();
				if (point!=null)
					mapController.setCenter(point);
				else Toast.makeText(MapManager2.this,testo, Toast.LENGTH_LONG).show();}
			catch(Exception e){
				Toast.makeText(MapManager2.this,testo, Toast.LENGTH_LONG).show();}
			return true;

			//INIZIO PULSANTI SUBMENU
		case R.id.modSat:
			mapView.setTraffic(false);
			mapView.setStreetView(false);
			mapView.setSatellite(true);
			break;
		case R.id.modTraffic:
			mapView.setSatellite(false);
			mapView.setStreetView(false);
			mapView.setTraffic(true);
			/*if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);*/
			break;
		case R.id.modMap:
			if(mapView.isSatellite())
				mapView.setSatellite(false);
			else if(mapView.isTraffic())
				mapView.setTraffic(false);
			else if(mapView.isStreetView())
				mapView.setStreetView(false);
			break;	
		case R.id.modStreet:
			mapView.setSatellite(false);
			mapView.setTraffic(false);
			mapView.setStreetView(true);
			break;
		}
		if(item.isChecked())
			item.setChecked(false);
		else
			item.setChecked(true);
		return super.onOptionsItemSelected(item);
	}

	//SEARCH BUTTON IMPLEMENT
	@Override
	public boolean onSearchRequested() {
		Bundle bundle=new Bundle();
		bundle.putString("extra", "extra info");
		// search initial query
		startSearch("Nomi, Cose e citta'", false, bundle, false);
		return true;
	}

	//Setta il textView sullo schermo
	public static void setText(String title, String snippet, double distance, int i,int p) {
		// TODO Auto-generated method stub
		textTitolo.setClickable(true);
		textTitolo.setText(title);
		textDescr.setClickable(true);
		String desc=snippet;
		if(snippet.length()>195)
			desc=snippet.substring(0, 195)+"...";
		textDescr.setText(desc);
		textDistance.setClickable(true);
		int dist=(int)distance;
		indiceMapOverlays= i;
		indicemOverlays=p;
		textDistance.setText("< si trova a "+dist+" m >");
	}
	public static void setText(String title, String snippet,GeoPoint geo,int i,int p) {
		// TODO Auto-generated method stub
		textTitolo.setClickable(true);
		textTitolo.setText(title);
		textDescr.setClickable(true);
		String desc=snippet;
		if(snippet.length()>195)
			desc=snippet.substring(0, 195)+"...";
		textDescr.setText(desc);
		textDistance.setClickable(true);

		indiceMapOverlays= i;
		indicemOverlays=p;
		textDistance.setText("< si trova a "+(int)getDistance(geo,loc4)+" m >");
	}

	protected static double getDistance(GeoPoint point2,Location loc) {
		double distance;//fare il try che ritorna distance =0 se c'e errore
		float []result=new float[3];
		try {
			loc.distanceBetween(loc.getLatitude(),loc.getLongitude(),point2.getLatitudeE6()/1E6,point2.getLongitudeE6()/1E6,result);
		}catch(Exception e){ 

			return distance =0;
		}
		distance= result[0];
		return distance;
	}

	//Metodo per dispatching intents
	private void startSubActivity(Bundle bundle) {
		String classe;
		Intent intent;
		classe= bundle.getString("destination");

		if(classe.compareTo("ResultView")==0)
			intent = new Intent(this, it.result.ResultView.class); 
		else if(classe.compareTo("WebBrowser")==0)
			intent = new Intent(this, it.result.WebBrowser.class);
		else {
			intent=new Intent();
			Log.i("mapManager2", "Avvio seconda activity fallito per mancanza di contenuto");
			finish();
		}	//end else
		intent.putExtras(bundle);

		startActivity(intent);
	}//end metodo




}//end class



