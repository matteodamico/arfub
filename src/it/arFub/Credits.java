package it.arFub;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Credits extends Activity {

	//creazione activity di AR Manager
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("credits","avvio in corso");
		setContentView(R.layout.credits);

		String testo= "Questa applicazione è stata sviluppata da Matteo D'Amico laureando in Ingegneria informatica presso l Università Roma Tre in stretta collaborazione con la Fondazione Ugo Bordoni per la quale ha svolto il seguente tirocinio";
		TextView text= (TextView)this.findViewById(R.id.textCredits);
		text.setText(testo);
		ImageButton link_uni2 = (ImageButton) findViewById(R.id.link_uni2);
		TextView version= (TextView) findViewById(R.id.version);
		try{
		testo ="version "+ getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		}catch(Exception e){}
		version.setText(testo);
		View.OnClickListener gestoreClick = new View.OnClickListener() {
			public void onClick(View view) { 
				Bundle bundle;
				bundle= new Bundle();
				switch(view.getId()){

				case R.id.link_uni2:
					Log.i("credits", "link al sito dell uni");
					String testo ="collegamento al sito di Roma 3";
					Toast.makeText(Credits.this,testo,Toast.LENGTH_SHORT).show();
					bundle.putString("destination","WebBrowser");
					bundle.putString("url","http://www.uniroma3.it");
					startSubActivity(bundle);
					break;
				}//end switch
			} //end on click
			};//end gestore clic 
			link_uni2.setOnClickListener(gestoreClick);
	}
	
			private void startSubActivity(Bundle bundle) {
				String classe;
				Intent intent;
				classe= bundle.getString("destination");

				if(classe.compareTo("WebBrowser")==0)
					intent = new Intent(this, it.result.WebBrowser.class);
				else {
					intent=new Intent();
					Log.i("mapManager2", "Avvio seconda activity fallito per mancanza di contenuto");
					finish();
				}	//end else
				intent.putExtras(bundle);

				startActivity(intent);
			}//end metodo
		}
