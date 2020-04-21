package it.ar;


import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONArray;
import org.json.JSONException;


import com.wikitude.architect.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView;


import it.arFub.R;
import it.arFub.SingletonParametersBridge;
import it.dataSource.FubMarker;
import it.dataSource.Marker;
import it.dataSource.WikipediaMarker;
import it.map.OverlayManager;

import android.app.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;

import android.preference.PreferenceManager;


import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;



public class AR_Viewer extends Activity implements ArchitectUrlListener {
	private ArchitectView architectView;
	private LocationManager locationManager;
	private Location Loc;
	private double Lat,Lng,Alt=0;
	private List<PoiBean> poiBeanList;
	Bitmap thumbnail;

	/*private final static float  TEST_LATITUDE =  51.909048f;
	private final static float  TEST_LONGITUDE = 12.51199f;
	private final static float 	TEST_ALTITUDE = 0;*/
	private final String wikitudeApiKey = "kYUuHGKDfBO/3brZSjkPBgiX/6T9eLfuRAVKS2cfelFQdjhWomjlPl9Fg2nNuqV7AQ9yP5oDmpmSsLxXTgmGuqjX74nxU75zO3EozdljrL8J5AL9T3ub0zDGckjZMCl6hN9/WVLPQK/X4p14mQdthO88/Ms668yfGtQ2TOZt7/JTYWx0ZWRfXwLgkYG1i90nai7Lr6s7l50iUCTpLMOY2ptD6kRyYL1GQhLR92vTB+kWoQ0ACGD31BYE9qPIwAVNmnYjiksBXv0xfhHCPQt5J3SGPZ4qEwd2Jyu0aeXI5oFADBWS8q5IEPPf8cXspUYUlOEBtw/pP5/ZPHKDT0fH8yf/qI9NYsIazDhUE9EY32Rf3ms2HHrabJ+wvwVf2LhamsKaMLpvE0OAav9SKj2uhGwrtZcgYpDg4eSGOmOgkCDhAtf4z6WLLbzeKOmdEPlQQQ4QLNqjSKmmF8p9xqs/GjTA530nXnnK6LtLWk4EoKfpFn2sytf9HV2YPXU2pPVFMHcsII+rcEbNantk40plm/F+5wocXZo8K2fHpfup7k24VuqBK993fL6zzZ8VQ/sTCsLudkvLGMNUnZSBPYG5p1fIhq0XwDjEoaB09+viiIFXkp3lciRrX/6VIA4XzhVe";
	int CAMERA_PIC_REQUEST = 1337; 
	
	//Rifermimento al controller delle preferenze
	SharedPreferences prefs;
	String testo;
	OverlayManager overMan2;
	ImageButton takeFoto;
	Location loc3;
	List<Marker> listM;

	//creazione activity di AR Manager
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("ArViewer", "chiamato on create");
		//let the application be fullscreen
		this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		Log.i("ArViewer", "ok full screen");
		//check if the device fulfills the SDK'S minimum requirements
		if(!ArchitectView.isDeviceSupported(this))
		{ 
			Toast.makeText(this, "Requisiti minimi non soddisfatti", Toast.LENGTH_LONG).show();
			this.finish();
			return;
		}
		setContentView(R.layout.arviewer);
		Log.i("ArViewer", "ok set content");
		//set the devices' volume control to music to be able to change the volume of possible soundfiles to play
		this.setVolumeControlStream( AudioManager.STREAM_MUSIC );
		this.architectView = (ArchitectView) this.findViewById(R.id.architectView);
		//onCreate method for setting the license key for the SDK
		architectView.onCreate(wikitudeApiKey);
		Log.i("ArViewer", "ok architect");

		//in order to inform the ARchitect framework about the user's location Androids LocationManager is used in this case
		// Otteniamo il riferimento al LocationManager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Verifichiamo se il GPS e abilitato altrimenti avvisiamo l'utente
		if(!locationManager.isProviderEnabled("gps")){
			Toast.makeText(this, "GPS è attualmente disabilitato. E' possibile abilitarlo dal menu impostazioni.", Toast.LENGTH_LONG).show();}

		// Registriamo il LocationListener al provider GPS
		else {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		}

		//associamo prefs al preference manager
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		overMan2 = new OverlayManager(getApplicationContext());
		loc3=null;
		takeFoto =(ImageButton) findViewById(R.id.takeAFoto);
		
		takeFoto.setOnClickListener(new OnClickListener(){
		        @Override
		        public void onClick(View view){
		            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		            // request code
		            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
		           
		        }
		    });

	}//end on create


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		//IMPORTANT: creates ARchitect core modules
		if(this.architectView != null)
			this.architectView.onPostCreate();
		Log.i("ArViewer", "ok on post");

		//register this activity as handler of "architectsdk://" urls
		this.architectView.registerUrlListener(this);
/*
		try {
			loadSampleWorld();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location loc) {
			if(architectView != null)
				architectView.setLocation((float)(loc.getLatitude()), (float)(loc.getLongitude()), loc.getAccuracy());
			Loc=loc;
			if(loc3==null)
				loc3=loc;
			else if (loc3.distanceTo(Loc)>100)
				try {
					loc3=loc;
					loadSampleWorld();
				} catch (IOException e) {
					Toast.makeText(AR_Viewer.this, "errore nel caricare i POI", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			// Aggiorna la location
			Lat = loc.getLatitude();
			Lng = loc.getLongitude();
			Alt = loc.getAltitude();

		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status,
				Bundle extras) {
			// TODO Auto-generated method stub

		}

	};

	@Override
	protected void onResume() {
		super.onResume();

		//ATTENZIONE VEDERE NELLE JAVADOC DI WIKITUDE IL METODO SETLOCATION CHE VALORI VUOLE IN INPUT
		this.architectView.onResume();

		this.architectView.setLocation((float)Lat, (float)Lng, (float)Alt,1f);
		try {
			loadSampleWorld();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}
		//this.architectView.setLocation(TEST_LATITUDE, TEST_LONGITUDE, TEST_ALTITUDE,1f);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(this.architectView != null)
			this.architectView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if(this.architectView != null)
			this.architectView.onDestroy();
		LocationManager locationManager = (LocationManager) getSystemService(	LOCATION_SERVICE	);
		if(locationManager != null)
			locationManager.removeUpdates(locationListener);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();

		if(this.architectView != null)
			this.architectView.onLowMemory();
	}

	@Override
	public boolean urlWasInvoked(String url) {
		//parsing the retrieved url string
		Log.i("ArViewer", ""+url);


		List<NameValuePair> queryParams = URLEncodedUtils.parse(URI.create(url), "UTF-8");
		String id = "";
		Intent intent;
		// getting the values of the contained GET-parameters
		for(NameValuePair pair : queryParams)
		{
			if(pair.getName().equals("id"))
			{
				id = pair.getValue();
			}
		}

		//get the corresponding poi bean for the given id
		//PoiBean bean = poiBeanList.get(Integer.parseInt(id));
		//start a new intent for displaying the content of the bean
		String urlDestination;
		SingletonParametersBridge bridge =	SingletonParametersBridge.getInstance();
		//bridge.removeParameter("FubMarker");
		try{
		FubMarker fbm=(FubMarker)listM.get(Integer.parseInt(id));
		intent = new Intent(this, it.result.ResultView.class);
		bridge.addParameter("FubMarker", fbm);;}
		catch(Exception e){
		WikipediaMarker wm=(WikipediaMarker)listM.get(Integer.parseInt(id));
		urlDestination ="http://"+wm.getUrl();
		intent = new Intent(Intent.ACTION_VIEW,Uri.parse(urlDestination));
		}
		/*
		intent.putExtra("POI_NAME", bean.getName());
		intent.putExtra("POI_DESC", bean.getDescription());*/
		this.startActivity(intent);
		return true;
	}


	//creazione e visualizzazione del menù
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_of_arview, menu);
		return true; 
	}

	//ascoltatore del menù selezionato
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch ( item.getItemId() ) {
		case R.id.settings: //se scelto menù : SETTINGS
			startActivity(new Intent(this, Settings_AR.class));
			return true;
		case R.id.toMapView: // se scelto menù: MODALITA' MAPPA
			startActivity(new Intent(this, it.map.MapManager2.class));
			finish();
			return true;
		case R.id.strati:
			startActivity(new Intent(this, it.map.Layars.class));
			Log.i("ar view","cliccato strati");
			return true;
		case R.id.myloc:
			
			startActivity(new Intent(this, it.result.My_location.class));
			return true;
		}
		return false;
	}



	/**
	 * loads a sample architect world and
	 * creates a definable amount of pois in beancontainers 
	 * and converts them into a jsonstring that can be sent to the framework
	 * @throws IOException exception thrown while loading an Architect world
	 */
	private void loadSampleWorld() throws IOException {
		//this.architectView.load("tutorial1.html");
		this.architectView.load("myWorld.html");
		Log.i("ar view","clicc load ok ");
		JSONArray array = new JSONArray();
		poiBeanList = new ArrayList<PoiBean>();
		listM = scaricaInfo();
		Log.i("ar view","scaricainfo ok");
		int p=20;
		try {
			if(listM!=null){
				if(listM.size()<20)
					p=listM.size();
				//metto solo i primi 20 Markers
				for (int i = 0; i< p; i++) {
					PoiBean bean = new PoiBean(
							""+i,
							""+listM.get(i).getName(),
							""+listM.get(i).getDescr(),
							
							(int) (Math.random() * 3), 
							(listM.get(i).getLatitude()/1E6),
							
							(listM.get(i).getLongitude()/1E6),
							(listM.get(i).getAltitude()));
					Log.i("ar view","bean:++++list.getAlt: "+listM.get(i).getLatitude());
					array.put(bean.toJSONObject());
					
					poiBeanList.add(bean);
				}	}
			else Toast.makeText(this, "Non ci sono punti di interesse in zona.", Toast.LENGTH_LONG).show();
			this.architectView.callJavascript("newData(" + array.toString() + ");");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "Err:Non ci sono punti di interesse in zona.", Toast.LENGTH_LONG).show();
			e.printStackTrace();}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	    if( requestCode == 1337)
	    {
	    //  data.getExtras()
	        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
/*
	          Now you have received the bitmap..you can pass that bitmap to other activity
	          and play with it in this activity or pass this bitmap to other activity
	          and then upload it to server.*/
	    }
	    else 
	    {
	        Toast.makeText(AR_Viewer.this, "Picture NOt taken", Toast.LENGTH_LONG).show();
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}

	private List<Marker> scaricaInfo(){

		//if(Loc!=null){
		Log.i("ArViewer", "prendo i marker");
		return overMan2.getMarkerAroundMe(prefs,Loc,2);}
	/*else{
			testo="Attenzione non trovo la tua posizione.";
			Toast.makeText(AR_Viewer.this,testo, Toast.LENGTH_LONG).show();

		}
		return null;

	}*/
	


}
