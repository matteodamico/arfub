package it.arFub;




import it.ar.AR_Viewer;
import it.arFub.R;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
//import android.widget.ImageView;

public class AR_appActivity extends Activity {

	ImageButton imgbutt1,imgbutt2,bottone3,bottone2,bottone1;
	TextView augmentedReality;
	Intent link;
	String testo;
	//private ImageView imgbackg;

	//stringa per il logging sul cms
	public static final int PORTRAIT_ORIENTATION = 0x00000001;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("main", "chiamato onCreate()");
		setContentView(R.layout.main);

		//imgbackg = (ImageView) findViewById(R.id.pattern_back_mdpi);
		//imgbackg.setImageResource(R.drawable.pattern_back_mdpi);
		bottone1 = (ImageButton) findViewById(R.id.Esco);
		imgbutt1 = (ImageButton) findViewById(R.id.roma3);
		imgbutt2 = (ImageButton) findViewById(R.id.logofub);
		bottone2 = (ImageButton) findViewById(R.id.Maps);
		bottone3 = (ImageButton) findViewById(R.id.VistaAR);
		augmentedReality=(TextView) findViewById(R.id.Textvista);
		if (getResources().getConfiguration().orientation==PORTRAIT_ORIENTATION)
			augmentedReality.setText("Augmented \nReality");
		else  augmentedReality.setText("A-Reality");

		View.OnClickListener gestore = new View.OnClickListener() {
			public void onClick(View view) { 
				Bundle bundle;
				switch(view.getId()){

				case R.id.Esco:
					Log.i("main", "chiamato butt1.onclick()");
					testo ="Esco";
					Toast.makeText(AR_appActivity.this,testo,Toast.LENGTH_SHORT).show();
					//link = new Intent(this, MapManager.class);
					//startActivity(link);
					finish();
					break;

				case R.id.VistaAR:
					Log.i("main", "chiamato arView.onclick()");
					testo ="Attivazione Vista AR ";
					Toast.makeText(AR_appActivity.this,testo,Toast.LENGTH_SHORT).show();
					//link = new Intent(this, AR_Manager.class);
					//startActivity(link);
					bundle= new Bundle();
					bundle.putString("destination","AR_Viewer");
					startSubActivity(bundle); 
					break;


				case R.id.Maps:
					Log.i("main", "chiamato maps.onclick()");
					testo ="Attivazione Mappa";
					Toast.makeText(AR_appActivity.this,testo,Toast.LENGTH_SHORT).show();
					bundle= new Bundle();
					bundle.putString("destination","MapManager");
					startSubActivity(bundle); 
					break;

				case R.id.roma3:
					Log.i("main", "chiamato bottone roma3.onclick()");
					bundle= new Bundle();
					bundle.putString("destination","Credits");
					startSubActivity(bundle);
					break;


				case R.id.logofub:
					Log.i("main", "chiamato bottone roma3.onclick()");
					testo ="collegamento al sito della fub";
					Toast.makeText(AR_appActivity.this,testo,Toast.LENGTH_SHORT).show();
					bundle= new Bundle();
					bundle.putString("destination","WebBrowser");
					bundle.putString("url","http://www.fub.it");
					startSubActivity(bundle);
					break;





				}//end switch	
			}//end on click
		};//end on click listener

		bottone1.setOnClickListener(gestore);
		bottone2.setOnClickListener(gestore);
		bottone3.setOnClickListener(gestore);
		imgbutt1.setOnClickListener(gestore);
		imgbutt2.setOnClickListener(gestore);
	}//end onCreate()


	private void startSubActivity(Bundle bundle) {
		String classe;
		Intent intent;
		classe= bundle.getString("destination");
		if (classe.compareTo("AR_Viewer")==0)
			intent = new Intent(this, AR_Viewer.class); 
		else if(classe.compareTo("MapManager")==0)
			intent = new Intent(this, it.map.MapManager2.class); 
		else if(classe.compareTo("Credits")==0)
			intent = new Intent(this, Credits.class); 
		else if(classe.compareTo("WebBrowser")==0)
			intent = new Intent(this, it.result.WebBrowser.class);
		else {
			intent=new Intent();
			Log.i("main", "Avvio seconda activity fallito per mancanza di contenuto");
			finish();
		}	
		Log.i("main","quasi avviato");
		intent.putExtras(bundle);
		startActivity(intent);
	}
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
