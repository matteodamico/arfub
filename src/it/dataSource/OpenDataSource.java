package it.dataSource;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import it.arFub.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;


public class OpenDataSource extends DataSource {
private Context mcontext;
private static final String URL = "http://search.twitter.com/search.json";
private Logger logger = Logger.getLogger(getClass().getSimpleName());

	public OpenDataSource(Context mcontext) {
		// TODO Auto-generated constructor stub
	this.mcontext=mcontext;
	}



	@Override
	public String createRequestURL(double lat, double lon, double alt,	float radius) {
		// TODO Auto-generated method stub
		 
  	return URL+"?geocode=" + lat + "%2C" + lon + "%2C" + Math.max(radius, 1.0) + "km";
  }
  
	

	@Override
	public List<Marker> parse(JSONObject root) {
		// TODO Auto-generated method stub
		//IL METODO RITORNA UNA LISTA DI MARKER DA UN JSON
	  //da un oggetto Json ritorno la Lista dei Marker
	    OpenMarker ma;
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
	  public OpenMarker processJSONObject(JSONObject jo) {
	    Drawable draw;
	    
	  	if (!jo.has("nodes")) {
	  		 Log.i("Fub data source","il jo non ha nodi -> return null");
	  		return null;
	  	}
	   OpenMarker ma = null;
	    try {
	    	
	      int lat=0,lon=0;
	      //parser nativo del linguaggio android/java senza importare librerie esterne
	      if(!jo.isNull("node")) {
	        JSONObject nodo = jo.getJSONObject("node");
	        JSONArray coordinates = nodo.getJSONArray("Coordinates");
	        
	        lat=Integer.parseInt(coordinates.getString(0));
	        lon=Integer.parseInt(coordinates.getString(1));
	      
	      } 
	      else if(jo.has("lat")) {
	      	 Log.i("Open data source","jo ha lat");
	        Pattern pattern = Pattern.compile("\\D*([0-9.]+),\\s?([0-9.]+)");
	        //ELIMINA TUTTO QUELLO CHE NON e NUMERO DOPO IL CAMPO location: 
	        Matcher matcher = pattern.matcher(jo.getString("location"));
	         if(matcher.find()){
	        	lat=Integer.parseInt(matcher.group(1));
	        	
	        	lon=Integer.parseInt(matcher.group(2));
	        	
	         	}          
	      }
	      if(lat!=0) {
	        String user=jo.getString("Nome");
	        String descr= user +": "+jo.getString("sommario") ;
	       String img=null;
	        // String img = jo.getString("profile_image_url");
	        
	        if(img!=null)
	   			 //INSERIRE UN PARSE DELL IMAGINE DA URL IN DRAWABLE
	        	draw =mcontext.getResources().getDrawable(R.drawable.openmarker);
	     		 else
	     			draw =mcontext.getResources().getDrawable(R.drawable.openmarker);
	        
	        //per mettere l icona di chi ha pubblicato il tweet
	        ma = new OpenMarker(user,descr,lat,lon,0,draw);
	        
	      }
	    } catch (Exception e) {
	      logger.info("Exception: "+e.getMessage());
	    }
	    return ma;
	  }
	  
	  
}
