package it.map;

import it.arFub.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Layars extends PreferenceActivity {
	boolean CheckWikipedia;
	boolean CheckFub;
	boolean CheckOpenStreetMap;
	boolean CheckTwitter;
	SharedPreferences prefs;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.layers);

	}//end on create	

	public void onStart(Intent intent, int startId) {
		getPreference();
		onResume();
	}

	private void getPreference() {
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		CheckWikipedia = prefs.getBoolean("WIKIPEDIA", true);
		CheckFub=prefs.getBoolean("FUB",false);
		CheckOpenStreetMap=prefs.getBoolean("OPEN_STREET_MAP", false);
		CheckTwitter=prefs.getBoolean("TWITTER", false);

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
