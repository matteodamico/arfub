package it.dataSource;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;



public abstract class DataSource {
  
	
	private static Logger logger = Logger.getLogger(DataSource.class.getSimpleName());
  protected static final int MAX = 12;
  public abstract String createRequestURL(  double lat, double lon,double alt,float radius);
  public abstract List<Marker> parse(JSONObject root);
  
  
  //ritorna un imput string passandogli una stringa url che parserˆ autamaticamente in URL
  protected static InputStream getHttpGETInputStream(String urlStr) {
      InputStream is = null;
      
      // non instanzio direttamente la classe HttpURLConnection perche la richiesta potrebbe avvenire all interno del cellulare quindi non so se e un httpConnection
      URLConnection conn = null;

      try {
      	//se la fonte da cui prendere i dati e memorizzata su cellulare accedigli tramite un input stream
          if (urlStr.startsWith("file://")) 
          	return new FileInputStream(urlStr.replace("file://", ""));
          logger.info("Exception1:eccezzione111 dopo qui in data source ");
        URL url = new URL(urlStr);
        conn =  url.openConnection();
        conn.setReadTimeout(2000);
        conn.setConnectTimeout(2000);

        is = conn.getInputStream();
        //logger.info("Exception1:eccezzione dopo qui in data source ");
        
        return is;
      } catch (Exception ex) {
        try {
          is.close();
        } catch (Exception e) {
          // Ignore
        }
        try {
          if(conn instanceof HttpURLConnection) ((HttpURLConnection)conn).disconnect();
        } catch (Exception e) {
          // Ignore
        }
        logger.info("Exception1: "+ex.getMessage());
      
      }
      
      return null;
    }
    
    protected String getHttpInputString(InputStream is) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8 * 1024);
      StringBuilder sb = new StringBuilder();

      try {
        String line;
        while ((line = reader.readLine()) != null) {
          sb.append(line + "\n");
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      return sb.toString();
    }
    
  //da una stringaUrl mi ritorna la lista dei marker
    //si occupa del collegamento vero e proprio.
  public List<Marker> parse(String url) {
    InputStream stream = null;
      stream = getHttpGETInputStream(url);
      if (stream.equals(null)){
      	return null;
      	}
      
      String string = null;
      string = getHttpInputString(stream);
      if (string==null) return null;
      
      JSONObject json = null;
      try {
        json = new JSONObject(string);
      } catch (JSONException e) {
        logger.info("Exception: "+e.getMessage());
      }
      if (json==null) return null;
      
      return parse(json);
  }
}
