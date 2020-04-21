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




//import com.jwetherell.augmented_reality.ui.Marker;

public class WikipediaDataSource extends DataSource {
	private Logger logger = Logger.getLogger(getClass().getSimpleName());
	private static final String BASE_URL = "http://ws.geonames.org/findNearbyWikipediaJSON";
	private Context mcontext;
	private Drawable draw;

	public WikipediaDataSource() {}

	public WikipediaDataSource(Context c) {
		mcontext=c;
	}

	public String createRequestURL(double lat, double lon, double alt, float radius, String locale) {
		return BASE_URL+
				"?lat=" + lat +
				"&lng=" + lon +
				"&radius="+ radius +
				"&maxRows=50" +
				"&lang=" + locale;

	}

	@Override
	public String createRequestURL(double lat, double lon, double alt, float radius) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Marker> parse(JSONObject root) {
		JSONObject jo = null;
		JSONArray dataArray = null;
		List<Marker> markers=new ArrayList<Marker>();
		try {
			if(root.has("geonames")) dataArray = root.getJSONArray("geonames");
			if (dataArray == null) 
				return markers;
			int top = Math.min(MAX, dataArray.length());
			for (int i = 0; i < top; i++) {          
				jo = dataArray.getJSONObject(i);
				Marker ma = processJSONObject(jo);//vedi metodo
				if(ma!=null) markers.add(ma);
			}
		} catch (JSONException e) {
			logger.info("Exception: "+e.getMessage());
		}
		return markers;
	}
	//DOVREBBE ESSERE PRIVATO:
	//da un oggetto json ritorna un marker(PARSER di un OGGETTO JSON)
	public WikipediaMarker processJSONObject(JSONObject jo) {
		
		WikipediaMarker ma = null;
		if (  jo.has("title") && 
				jo.has("lat") && 
				jo.has("lng") && 
				jo.has("elevation") && 
				jo.has("wikipediaUrl")
				) {
			draw =mcontext.getResources().getDrawable(R.drawable.wiki_marker);
			try {
				double lat=jo.getDouble("lat")*1E6;
				double lng=jo.getDouble("lng")*1E6;
				double elev=jo.getDouble("elevation");
				String urlWiki=jo.getString("wikipediaUrl");
				ma = new WikipediaMarker(jo.getString("title"),jo.getString("summary"),(int)lat,(int)lng,(int)elev,draw,urlWiki);
			} catch (JSONException e) {
				logger.info("Exception: "+e.getMessage());
			}
		}
		return ma;
	}




}