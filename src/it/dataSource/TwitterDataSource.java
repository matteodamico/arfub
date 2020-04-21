package it.dataSource;



import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import it.arFub.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;


public class TwitterDataSource extends DataSource {
  private Logger logger = Logger.getLogger(getClass().getSimpleName());
  public Context mcontext;
  
  private static final String URL = "http://search.twitter.com/search.json";
  private static Bitmap icon = null;

  double lat2;
  double lon2;
  public TwitterDataSource(Resources res) {
    createIcon(res);
  }
  
  public TwitterDataSource(Context c) {
    mcontext=c;
  }
  
  public TwitterDataSource(){}
  
  
  
  
  
  protected void createIcon(Resources res) {
    icon=BitmapFactory.decodeResource(res, R.drawable.tweet_marker);
  }
  //DOVREBBE ESSERE PRIVATO
  public Bitmap getBitmap() {
    return icon;
  }
  
  
  
  
  
  @Override
public String createRequestURL(double lat, double lon, double alt, float radius) {
   
  	return URL+"?geocode=" + lat + "%2C" + lon + "%2C" + Math.max(radius, 1.0) + "km";
  }
  
  
  //IL METODO RITORNA UNA LISTA DI MARKER DA UN JSON
  //da un oggetto Json ritorno la Lista dei Marker
  @Override
public List<Marker> parse(JSONObject root) {
    TweetMarker ma;
  	JSONObject jo = null;
    JSONArray dataArray = null;
      List<Marker> markers=new ArrayList<Marker>();
    try {
      if(root.has("results")){ 
      	dataArray = root.getJSONArray("results");
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
  public TweetMarker processJSONObject(JSONObject jo) {
    Drawable draw;
    
  	if (!jo.has("geo")) {
  		 Log.i("Tweeter data source","il jo non ha geo -> return null");
  		return null;
  	}
    TweetMarker ma = null;
    try {
    	
      Double lat=null, lon=null;
      //parser nativo del linguaggio android/java senza importare librerie esterne
      if(!jo.isNull("geo")) {
        JSONObject geo = jo.getJSONObject("geo");
        JSONArray coordinates = geo.getJSONArray("coordinates");
        lat=Double.parseDouble(coordinates.getString(0));
        lon=Double.parseDouble(coordinates.getString(1));
        lat2=lat;
        lat2=lat2*1E6;
        lon2=lon;
      	lon2=lon2*1E6;      	   
      
      } 
      else if(jo.has("location")) {
      	 Log.i("Tweeter data source","jo ha location");
        Pattern pattern = Pattern.compile("\\D*([0-9.]+),\\s?([0-9.]+)");
        //ELIMINA TUTTO QUELLO CHE NON e NUMERO DOPO IL CAMPO location: 
        Matcher matcher = pattern.matcher(jo.getString("location"));
         if(matcher.find()){
        	lat=Double.parseDouble(matcher.group(1));
        	lat2 =lat;
        	lat2=lat2*1E6;
        	lon=Double.parseDouble(matcher.group(2));
        	lon2=lon;
        	lon2=lon2*1E6;
         	}          
      }
      if(lat!=null) {
        String user=jo.getString("from_user");
        String descr= jo.getString("text") ;
        String img = jo.getString("profile_image_url");
        
        if(img!=null)
   			 //INSERIRE UN PARSE DELL IMAGINE DA URL IN DRAWABLE
        	draw =mcontext.getResources().getDrawable(R.drawable.tweet_marker);
     		 else
     			draw =mcontext.getResources().getDrawable(R.drawable.tweet_marker);
        
        //per mettere l icona di chi ha pubblicato il tweet
        ma = new TweetMarker(user,descr,lat2,lon2,0,draw);
        
      }
    } catch (Exception e) {
      logger.info("Exception: "+e.getMessage());
    }
    return ma;
  }
}
