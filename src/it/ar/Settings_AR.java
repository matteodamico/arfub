package it.ar;




import it.arFub.R;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


public class Settings_AR extends PreferenceActivity   {
	boolean CheckGps;
	boolean CheckRadar;
	boolean CheckBussola;
	SharedPreferences prefs;
	
	//private Preference prefe;
	 	 
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
		CheckRadar=prefs.getBoolean("RADAR", true);
	}

  @Override
  protected void onResume() {
      super.onResume();
      //prefe.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){
      }
  



  @Override
  protected void onPause() {
      super.onPause();

      // Unregister the listener whenever a key changes            
  }




}//end class
