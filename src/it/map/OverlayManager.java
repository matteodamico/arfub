package it.map;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import it.arFub.R;
import it.arFub.SingletonParametersBridge;
import it.dataSource.DataSource;
import it.dataSource.FubDataSource;

import it.dataSource.Marker;
import it.dataSource.OpenDataSource;
import it.dataSource.TwitterDataSource;
import it.dataSource.WikipediaDataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;

import android.util.Log;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class OverlayManager //extends Activity
{


	//Riferimento alla mappa
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;	//Riferimento al MyLocationOverlay
	private List<Overlay> mapOverlays;//Riferimento alla lista di Overlay
	private Drawable drawable;
	private Context mcontext;
	private ItemOverlayMap itemOverlay;
	private OverlayItem overlayitem;
	private GeoPoint point;
	private double lat,lon;
	SingletonParametersBridge bridge;


	protected List<Marker> webMarkers, webMarkersWiki,webMarkersTwitter ,webMarkersFub , webMarkersOpen,webMarkersPersonal;
	boolean PrefWikipedia, PrefFub, PrefTwitter, PrefOpenStreetMap,PrefPersonal;

	private Logger logger = Logger.getLogger(getClass().getSimpleName());


	public OverlayManager(Context cont){
		bridge = SingletonParametersBridge.getInstance();
		webMarkers=new ArrayList<Marker>();
		this.mcontext=cont;
		webMarkersWiki=new ArrayList<Marker>();
		webMarkersTwitter = new ArrayList<Marker>();
		webMarkersFub = new ArrayList<Marker>();
		webMarkersOpen = new ArrayList<Marker>();
		webMarkersPersonal=(List<Marker>) bridge.getParameter("list_created_poi");
	}

	public OverlayManager(MyLocationOverlay item,MapView item2,Drawable item3,Context context) {
		this.drawable=item3;
		this.myLocationOverlay=item;
		this.mapView=item2;
		//finalmapView=(MapView)View.findViewById(R.id.mapView1);
		bridge = SingletonParametersBridge.getInstance();
		this.mcontext=context;
		webMarkers=new ArrayList<Marker>();

		webMarkersWiki=new ArrayList<Marker>();
		webMarkersTwitter = new ArrayList<Marker>();
		webMarkersFub = new ArrayList<Marker>();
		webMarkersOpen = new ArrayList<Marker>();
		webMarkersPersonal=(List<Marker>) bridge.getParameter("list_created_poi");
	}
	/*
	public void  inizializza(Context context,MapView map){
		Log.i("OverlayManager", "preso riferimento mapa");
		myLocationOverlay = new MyLocationOverlay(context, map);
	}
	 */
	public void aggiungiOverlayMyLocation(){
		//Aggiungiamo l'overlay sulla mappa della nostra posizione

		mapOverlays = mapView.getOverlays();
		mapOverlays.add(myLocationOverlay);

	}

	public void disattivaBussola(){
		myLocationOverlay.disableCompass();
	}
	public void attivaBussola(){
		myLocationOverlay.enableCompass();
	}
	public void attivaMyLocation(){
		myLocationOverlay.enableMyLocation();
	}
	public void diattivaMyLocation(){
		myLocationOverlay.disableMyLocation();
	}


	public void aggiungiOverlaysWeb(SharedPreferences prefs,Location loc,int ray) {

		Log.i("overMan", "aggiungi overWeb chiamato");
		mapOverlays = mapView.getOverlays();
		mapOverlays.clear();
		aggiungiOverlayMyLocation();

		//webmarkers con immagine variabile--INUTILIZZATO
		webMarkers.clear();

		try {

			Log.i("overMan", "test loca"+loc.getLatitude());


			webMarkersWiki.clear();
			webMarkersTwitter.clear();
			webMarkersFub.clear();
			webMarkersOpen.clear();
			PrefWikipedia= prefs.getBoolean("WIKIPEDIA", false);
			PrefFub= prefs.getBoolean("FUB", false);
			PrefTwitter= prefs.getBoolean("TWITTER", false);
			PrefOpenStreetMap = prefs.getBoolean("OPEN_STREET_MAP",false);
			PrefPersonal=prefs.getBoolean("Personal_POIs", false);

			Log.i("overMan", "aggiungi overweb inizializzato");

			if(PrefPersonal){
				if(webMarkersPersonal!=null)
					if(webMarkersPersonal.size()!=0)
						popolaOverlaysOne(mapOverlays,webMarkersPersonal);
					else
						Toast.makeText(mcontext, "Non ci sono POI personali in zona.", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(mcontext, "Non ci sono POI personali.", Toast.LENGTH_LONG).show();
			}

			if(PrefFub){
				webMarkersFub=getFubMarkers(loc,ray);
				if(webMarkersFub.size()!=0){
					webMarkers.addAll(webMarkersFub);
					Log.i("overMan", "FINE FUB:: aggiunti "+webMarkersFub.size()+" FUB markers");
				}else 
					Toast.makeText(mcontext, "Non ci sono Fub-info con coordinate in zona.", Toast.LENGTH_LONG).show();
			}

			if(PrefTwitter){
				webMarkersTwitter=getTwitterMarkers(loc,ray);
				if(webMarkersTwitter.size()!=0){
					popolaOverlaysOne(mapOverlays,webMarkersTwitter);
					Log.i("overMan", "FINE TWEET:: aggiunti "+webMarkersTwitter.size()+" TWEET markers");
				}else 
					Toast.makeText(mcontext, "Non ci sono tweet con coordinate in zona.", Toast.LENGTH_LONG).show();
			}

			if (PrefWikipedia){
				webMarkersWiki=getWikipediaMarkers(loc,ray);
				if(webMarkersWiki.size()!=0){
					popolaOverlaysOne(mapOverlays,webMarkersWiki);
					Log.i("overMan", "FINE WIKI:: aggiunti "+webMarkersWiki.size()+" WIKI Markers");
				}else {Log.i("overMan", "FINE WIKI");
				Toast.makeText(mcontext, "Non ci sono Wiki-info in zona.", Toast.LENGTH_LONG).show();
				}}


			if(PrefOpenStreetMap){
				webMarkersOpen = getOpenMarkers(loc,ray);
				if(webMarkersOpen.size()!=0){
					popolaOverlaysOne(mapOverlays,webMarkersOpen);
					Log.i("overMan", "FINE OPEN:: aggiunti "+webMarkersOpen.size()+" OPEN markers");
				}else 
					Toast.makeText(mcontext, "Non ci sono OpenStreetMap -info con coordinate in zona.", Toast.LENGTH_LONG).show();
			}
		}catch(Exception e){ 
			logger.info("Exception: "+e.getMessage());
			Toast.makeText(mcontext, "Cerco la posizione...", Toast.LENGTH_LONG).show();
		}

		if(webMarkers.size()!=0)	
			popolaOverlays(mapOverlays,webMarkers);
	}

	protected List<Marker> getWebMarkersFub() {
		return webMarkersFub;
	}
	protected void setWebMarkersFub(List<Marker> webMarkersFub) {
		this.webMarkersFub = webMarkersFub;
	}

	private List<Marker> getOpenMarkers(Location loca, int ray) {
		// TODO Auto-generated method stub
		Log.i("overMan", "INIZIO OPEN::chiamato getOpenMarkers");
		DataSource OpenDataS=new OpenDataSource(mcontext);
		lat = loca.getLatitude();
		lon = loca.getLongitude();
		int alt= (int)loca.getAltitude();
		String url = OpenDataS.createRequestURL(lat, lon, alt, ray);
		Log.i("overMan", "url per Open: "+ url);
		List<Marker> list= OpenDataS.parse(url);
		return list;
	}

	private List<Marker> getFubMarkers(Location loca, int ray) {
		// TODO Auto-generated method stub
		Log.i("overMan", "INIZIO FUB::chiamato getFubMarkers");
		DataSource FubDataS=new FubDataSource(mcontext);
		/*lat = loca.getLatitude();
		lon = loca.getLongitude();
		int alt= (int)loca.getAltitude();*/
		lat=lon=0; int alt=0;
		String url = FubDataS.createRequestURL(lat, lon, alt, ray);
		Log.i("overMan", "url per FUB: "+ url);
		List<Marker> list= FubDataS.parse(url);
		return list;
	}

	private List<Marker> getTwitterMarkers(Location loca,int radius) {
		// TODO Auto-generated method stub
		Log.i("overMan", "INIZIO TWEET::chiamato getTwitterMarkers");
		DataSource TweetDataS=new TwitterDataSource(mcontext);
		lat = loca.getLatitude();
		lon = loca.getLongitude();
		int alt= (int)loca.getAltitude();
		String url = TweetDataS.createRequestURL(lat, lon, alt, radius);
		Log.i("overMan", "url per TWITTER: "+ url);
		List<Marker> list= TweetDataS.parse(url);
		return list;
	}

	private List<Marker> getWikipediaMarkers(Location loca,int radius) {
		Log.i("overMan", "INIZIO WIKI::chiamato getWikipediaMarkers");
		WikipediaDataSource WikiDataS= new WikipediaDataSource(mcontext);
		lat = loca.getLatitude();
		lon = loca.getLongitude();
		int alt= (int)loca.getAltitude();
		String url = WikiDataS.createRequestURL(lat, lon, alt, radius,"it");
		Log.i("overMan", "url per WIKIPEDIA: "+ url);
		List<Marker> list =WikiDataS.parse(url);
		return list;
	}

	//POPOLA LA MAPPA CON MARKER DI ICONA FISSA
	private void popolaOverlaysOne(List<Overlay> mapOverlays,List<Marker> webMarker) {
		int lat,lon;

		drawable=webMarker.get(0).getDrawable();
		if(drawable==null)
			drawable=mcontext.getResources().getDrawable(R.drawable.ic_launcher);
		Log.i("overMan", "INIZIOoooooooooooooooooo"+webMarker.get(0).getClass().getName());
		Log.i("overMan", "INIZIO POPOLAMENTO::: OVERLAY");
		itemOverlay = new ItemOverlayMap(drawable,mapOverlays.size(),webMarker.get(0).getClass().getName());
		for(int i=0; i<webMarker.size();i++){


			lat= webMarker.get(i).getLatitude();
			lon=webMarker.get(i).getLongitude();
			point =new GeoPoint(lat,lon);


			Log.i("overMan;POPOLO", "lat marker :" + lat);
			Log.i("overMan:POPOLO", "lon marker :" + lon);

			overlayitem = new OverlayItem(point, webMarker.get(i).getName(), webMarker.get(i).getDescr());
			itemOverlay.addOverlay(overlayitem);

		}
		mapOverlays.add(itemOverlay);
		Log.i("overMan", "FINE POPOLAMENTO:::");
	}

	//INUTILIZZATO ----PROSSIMAMENTE
	//POPOLA LA MAPPA CON MARKER DI ICONA VARIABILE
	private void popolaOverlays(List<Overlay> mapOverlays,List<Marker> webMarker) {
		int lat,lon;
		double distance;

		Log.i("overMan", "INIZIO POPOLAMENTO ad icona variabile::: OVERLAY");
		for(int i=0; i<webMarker.size();i++){
			drawable=webMarker.get(i).getDrawable();
			if(drawable==null)
				drawable=mcontext.getResources().getDrawable(R.drawable.ic_launcher);

			lat= webMarker.get(i).getLatitude();
			lon=webMarker.get(i).getLongitude();
			point =new GeoPoint(lat,lon);
			Log.i("overMan", "lat "+lat+"lon"+lon);

			//Imposto distance=0 perhcè se la Location è 0 rialsa un errore nel calcolo della distanza
			distance=0;
			//distance=getDistance(point,loc);
			itemOverlay = new ItemOverlayMap(drawable,distance,mapOverlays.size(),webMarker.get(i).getClass().getName());
			//Log.i("overMan", "lat marker :" + lat);
			//Log.i("overMan", "lon marker :" + lon);

			overlayitem = new OverlayItem(point, webMarker.get(i).getName(), webMarker.get(i).getDescr());
			itemOverlay.addOverlay(overlayitem);
			mapOverlays.add(itemOverlay);
		}
		Log.i("overMan", "FINE POPOLAMENTO:::");
	}

	protected double getDistance(GeoPoint point2,Location loc) {
		double distance;//fare il try che ritorna distance =0 se c'e errore
		float []result=new float[3];
		try {
			loc.distanceBetween(loc.getLatitude(),loc.getLongitude(),point2.getLatitudeE6()/1E6,point2.getLongitudeE6()/1E6,result);
		}catch(Exception e){ 
			logger.info("Exception: "+e.getMessage());
			return distance =0;
		}
		distance= result[0];
		return distance;
	}
	/*
	itemOverlay = new ItemOverlayMap(drawable,mcontext);
			Log.i("overMan", "Ok-drawable");	
		//Passaggio duro-forzato di parametri per il primo POIoverlay
		GeoPoint point = new GeoPoint(41906365, 12505531);
		OverlayItem overlayitem = new OverlayItem(point, "hole", "sono a castro petrorio");
		itemOverlay.addOverlay(overlayitem);
		Log.i("overMan", "aggiuntoOverlay all oggetto");
		mapOverlays.add(itemOverlay);
	}
	 */
	public void attivaRadar() {
		// TODO Auto-generated method stub

	}

	public void disattivaRadar() {
		// TODO Auto-generated method stub

	}
	protected List<Marker> getWebMarkersWiki() {
		return webMarkersWiki;
	}
	protected void setWebMarkersWiki(List<Marker> webMarkersWiki) {
		this.webMarkersWiki = webMarkersWiki;
	}

	public void addDirection(GeoPoint geoA,GeoPoint geoB){
		RouteDrawing draw= new RouteDrawing(); 
		draw.setGeopoints(geoA, geoB);
		mapOverlays.add(draw);
	}

	private String[] getDirectionData(String sourceLat, String sourceLong, String destinationLat, String destinationLong) {


		String urlString = "http://maps.google.com/maps?f=d&hl=en&" +"saddr="+sourceLat+","+sourceLong+"&daddr="+destinationLat+","+destinationLong + "&ie=UTF8&0&om=0&output=kml";
		Log.d("URL", urlString);
		Document doc = null;
		HttpURLConnection urlConnection = null;
		URL url = null;
		String pathConent = "";

		try {

			url = new URL(urlString.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.connect();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(urlConnection.getInputStream());

		} catch (Exception e) {
		}

		NodeList nl = doc.getElementsByTagName("LineString");
		for (int s = 0; s < nl.getLength(); s++) {
			Node rootNode = nl.item(s);
			NodeList configItems = rootNode.getChildNodes();
			for (int x = 0; x < configItems.getLength(); x++) {
				Node lineStringNode = configItems.item(x);
				NodeList path = lineStringNode.getChildNodes();
				pathConent = path.item(0).getNodeValue();
			}
		}
		String[] tempContent = pathConent.split(" ");
		return tempContent;
	}

	public List<Marker> getMarkerAroundMe(SharedPreferences prefs,Location loc,int ray) {
		webMarkers.clear();

		webMarkersWiki.clear();
		webMarkersTwitter.clear();
		webMarkersFub.clear();
		webMarkersOpen.clear();
		try {
			PrefPersonal=prefs.getBoolean("Personal_POIs",false);
			PrefWikipedia= prefs.getBoolean("WIKIPEDIA", false);
			PrefFub= prefs.getBoolean("FUB", false);
			PrefTwitter= prefs.getBoolean("TWITTER", false);
			PrefOpenStreetMap = prefs.getBoolean("OPEN_STREET_MAP",false);
			
			if(PrefPersonal){
				webMarkersPersonal=(List<Marker>) bridge.getParameter("list_created_poi");
				if(webMarkersPersonal!=null)
					webMarkers.addAll(webMarkersPersonal);}
			if(PrefTwitter){
				webMarkersTwitter=getTwitterMarkers(loc,ray);
				webMarkers.addAll(webMarkersTwitter);}
			if (PrefWikipedia){
				webMarkersWiki=getWikipediaMarkers(loc,ray);
				webMarkers.addAll(webMarkersWiki);}
			if(PrefFub){
				webMarkersFub=getFubMarkers(loc,ray);
				webMarkers.addAll(webMarkersFub);}
			if(PrefOpenStreetMap){
				webMarkersOpen = getOpenMarkers(loc,ray);
				webMarkers.addAll(webMarkersOpen);}
		}catch(Exception e){
			Toast.makeText(mcontext, "Posizione non ancora trovata, non posso scaricare i dati", Toast.LENGTH_LONG).show();
		}

		return webMarkers;
		// TODO Auto-generated method stub

	}




}//end class