package it.map;

import it.arFub.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


public class SettingsMap extends PreferenceActivity   {
	boolean CheckGps;
	boolean CheckNetwork;
	boolean CheckBussola;
	boolean CheckCoordinate;
	SharedPreferences prefs;
	
	 	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_map);
    
	}//end on create	

	public void onStart(Intent intent, int startId) {
		getPreference();
		onResume();
	}

	private void getPreference() {
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		CheckGps = prefs.getBoolean("GPS", true);
		CheckBussola=prefs.getBoolean("BUSSOLA", true);
		CheckNetwork=prefs.getBoolean("NETWORK", true);
		CheckCoordinate=prefs.getBoolean("COORDINATE", true);
	
	}

  @Override
  protected void onResume() {
      super.onResume();
      //quando ritorno dentro IMPOSTAZIONI mi interessa fare qualcosa??
      
      //prefe.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
      }
  



  @Override
  protected void onPause() {
      super.onPause();

      // Unregister the listener whenever a key changes            
  }




}//end class
