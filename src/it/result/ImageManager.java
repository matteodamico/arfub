package it.result;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class ImageManager {
	private static HashMap<String, Bitmap> imagesCache = new HashMap<String, Bitmap>();
	private static final String TAG="ImageManager";

	
	public ImageManager(){}
	/**
	 * Prende l'immagine presente nell'url passato come parametro e trasforma in
	 * un drowable, metodo ausiliario buono per tutti i marker
	 * 
	 * @param url
	 * @return
	 */
	
	public static Bitmap loadImageFromWeb(String url) {
		return loadImageFromWeb(url, true);
	}

	public static void recycleWebBitmaps() {
		Set<String> keySet = imagesCache.keySet();
		for (String string : keySet) {
			Bitmap bitmap = imagesCache.get(string);
			bitmap.recycle();
		}

		imagesCache.clear();
	}

	public static Bitmap loadImageFromWeb(String url, boolean newImage) {
		Bitmap bitmap;
		Bitmap cached = imagesCache.get(url);
		if (cached != null && !newImage) {
			Log.d(TAG, "loadImageFromWeb load from cache[" + url + "]");
			return cached;
		} else {

			try {
				Log.d(TAG, "loadImageFromWebOperations url[" + url + "]");


				/*
	            InputStream is = (InputStream) new URL(url).getContent();
	            BitmapDrawable d = (BitmapDrawable) Drawable.createFromStream(
	                    is, "src name");

	            imagesCache.put(url, ((BitmapDrawable) d).getBitmap());

	            return d.getBitmap();

	            // questo metodo a volte restiuisce un errore di decodifica 
	            //  --- decoder->decode returned false
	            // e non visualizza l'immagine, usare il codice che segue
				 */

				HttpGet httpRequest = null;
				try {
					httpRequest = new HttpGet(new URL(url).toURI());
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}

				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(
						entity);
				InputStream instream = bufHttpEntity.getContent();
				 bitmap = BitmapFactory.decodeStream(instream);
			

			} catch (Exception e) {
				Log.e(TAG, "oadImageFromWebOperations [" + e + "]");
				return null;
			}

		}
		return bitmap;
	}
	
	public static BitmapDrawable loadImageBitDrawableFromWeb(String url) {
		BitmapDrawable d = new BitmapDrawable( loadImageFromWeb(url, true));
		return d;
	}

}
