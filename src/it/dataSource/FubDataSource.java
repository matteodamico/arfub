package it.dataSource;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import it.arFub.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

public class FubDataSource extends DataSource {
	private Context mcontext;
	private Logger logger = Logger.getLogger(getClass().getSimpleName());

	public FubDataSource(Context mcontext) {
		// TODO Auto-generated constructor stub
		this.mcontext=mcontext;
	}

	@Override
	public String createRequestURL(double lat, double lon, double alt,float radius) {
		String url="URL";
		return url;
	}

	@Override
	public List<Marker> parse(JSONObject root) {
		//IL METODO RITORNA UNA LISTA DI MARKER DA UN JSON
		//da un oggetto Json ritorno la Lista dei Marker
		FubMarker ma=null;
		JSONObject jo = null;
		JSONArray dataArray = null;
		List<Marker> markers=new ArrayList<Marker>();
		try {
			if(root.has("nodes")){ 
				dataArray = root.getJSONArray("nodes");
			}
			if (dataArray == null){ 
				return markers;
			}
			int top = Math.min(MAX, dataArray.length());
			//int n=0;
			for (int i = 0; i < top; i++) {          
				jo = dataArray.getJSONObject(i);
				ma = processJSONObject(jo);//vedi metodo
				if(ma!=null){ 
					//	n++;
					markers.add(ma);}
			}//end for
		} //end try
		catch (JSONException e) {
			logger.info("Exception: "+e.getMessage());
		}//end catch
		return markers;
	}//end metodo



	//IN TEORIA DOVREBBE ESSERE PRIVATO...
	//ritorna da un oggetto Json il Marker
	public FubMarker processJSONObject(JSONObject jo) {
		Drawable draw;
		/*
//SE L'OGGETTO JSON NON HA NODI--RETURN NULL
		  	if (!jo.has("nodes")) {
		  		 Log.i("Fub data source","il jo non ha nodi -> return null");
		  		return null;
		  	}*/
		FubMarker ma = null;
		//ALTRIMENTI 
		try {

			int lat=0,lon=0;
			//parser nativo del linguaggio android/java senza importare librerie esterne	
			//SE L'OGGETTO JSON HA UN NODO:		      
			if(!jo.isNull("node")) {
				JSONObject nodo = jo.getJSONObject("node");
				//Log.i("Fub data source--system","jo: "+jo.toString(0));
				/*		        
//COORDINATE
		       	if(jo.has("Coordinates")){
		        JSONArray coordinates = nodo.getJSONArray("Coordinates");
		        lat=Integer.parseInt(coordinates.getString(0));
		        lon=Integer.parseInt(coordinates.getString(1));
		      }
//ALTRIMENTI SE L'OGGETTO HA IL CAMPO LOCATION
		      else if(jo.has("location")) {
		      	 Log.i("Fub data source","jo ha location");
		        Pattern pattern = Pattern.compile("\\D*([0-9.]+),\\s?([0-9.]+)");
		        //ELIMINA TUTTO QUELLO CHE NON e NUMERO DOPO IL CAMPO location: 
		        Matcher matcher = pattern.matcher(jo.getString("location"));
		         if(matcher.find()){
		        	lat=Integer.parseInt(matcher.group(1));
		        	lon=Integer.parseInt(matcher.group(2));
		       	}          
		      }

//ALTRIMENTI SE L'OGGETTO HA IL CAMPO LAT E LON
			else */if(nodo.has("lat")){
				Log.i("Fub data source","il nodo ha il campo lat");
				lat=Integer.parseInt(nodo.getString("lat"));

			}

			if(lat!=0) {
				lon=Integer.parseInt(nodo.getString("lon"));
				Log.i("Fub data source","lat p "+lat+ " lon p"+lon);
				//NOME
				String nome=nodo.getString("Nome");
				Log.i("Fub data source","nome "+nome);
				//SOMMARIO	
				String sommario=nodo.getString("sommario") ;

				//NUMERO_DI_MEDIA

				int nimmagini=0;
				int nvideo=0;
				int naudio=0;     	
				try{
					String appoggio=nodo.getString("numberImages");
					Log.i("Fub data source",""+appoggio);
					nimmagini=Integer.parseInt(appoggio);
					appoggio=nodo.getString("numberVideo");
					nvideo=Integer.parseInt(appoggio);
					appoggio=nodo.getString("numberAudio");
					naudio=Integer.parseInt(appoggio);
				}
				catch(JSONException e){
					Toast.makeText(mcontext, "Non riesco a scaricare i media allegati", Toast.LENGTH_SHORT).show();
					Log.i("Fub data source","errore nello scaricare il numero di media");
				}
				//IMMAGINI
				String media,campo;
				int p;
				List<String> URLimmagini=new ArrayList<String>();
				List<String> URLaudio=new ArrayList<String>();
				List<String> URLvideo=new ArrayList<String>();
				
				try{
				
				for(int i=1;i<=nimmagini;i++){
					campo="image"+i;		
					media = nodo.getString(campo);			
					URLimmagini.add(media);}
				//VIDEO
				
				for(int i=1;i<=nvideo;i++){
					campo="video"+i;		
					media = nodo.getString(campo);
					URLvideo.add(media);}
				//AUDIO	
				
				for(int i=1;i<=naudio;i++){
					campo="audio"+i;		
					media = nodo.getString(campo);
					URLaudio.add(media);}
				
				}catch(Exception e){
					Toast.makeText(mcontext, "Non riesco a scaricare i media allegati", Toast.LENGTH_SHORT).show();
					Log.i("Fub data source","errore nello scaricare gli url dei media");
				}

				//CATEGORIA
				String categoria ="";
				categoria=nodo.getString("categoria");

				//ICONA 


				if (categoria.compareTo("Chiesa")==0)
					draw =mcontext.getResources().getDrawable(R.drawable.puntatore_mappe_giallo);
				else if (categoria.compareTo("Obelisco")==0)
					draw =mcontext.getResources().getDrawable(R.drawable.puntatore_mappe_rosso);
				else if(categoria.compareTo("altro")==0)
					draw =mcontext.getResources().getDrawable(R.drawable.puntatore_mappe_verde);
				else if(categoria.compareTo("Museo")==0)
					draw =mcontext.getResources().getDrawable(R.drawable.puntatore_mappe_blu);
				else if(categoria.compareTo("Piazza")==0)
					draw =mcontext.getResources().getDrawable(R.drawable.puntatore_mappe_viola);
				else if(categoria.compareTo("Palazzo")==0)
					draw =mcontext.getResources().getDrawable(R.drawable.puntatore_mappe_grigio);
				else  
					draw=null;


				/*
		        if(img!=null)
		   			 //INSERIRE UN PARSE DELL IMAGINE DA URL IN DRAWABLE
		        	draw =mcontext.getResources().getDrawable(R.drawable.logopiccolo);
		     		 else
		     			draw =mcontext.getResources().getDrawable(R.drawable.logopiccolo);
				 */		        Log.i("Fub data source","ok ok ok ok ");
				 //CREAZIONE FUB MARKER
				 ma = new FubMarker(nome,sommario,lat,lon,0,categoria,URLimmagini,URLvideo,URLaudio,draw);
			}//end if lat!=0
			else return null;
			} //end if node is not null
		}//end try

		catch (Exception e) {
			logger.info("Exception: "+e.getMessage());
		}//end catch
		return ma;
	}//end metodo


}//end class
