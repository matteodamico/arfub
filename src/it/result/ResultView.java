package it.result;

import java.util.ArrayList;
import java.util.List;

import it.arFub.R;
import it.arFub.SingletonParametersBridge;
import it.dataSource.FubMarker;
import it.dataSource.Marker;
import it.map.MapManager2;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;


import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


public class ResultView extends Activity {
	private TextView TWnome,TWcategoria;
	private EditText ETdesc;
	private ImageButton fotobutton,playButton,indietro,avanti,savePref;
	private VideoView video;

	String testo;
	Spinner selectList;
	Uri uri;
	FubMarker fbm;
	Marker poi;
	String nomeRisorsa,categoria;
	ImageManager im;
	MediaPlayer mp;
	Drawable play,pause,saveFull;
	List<Marker> list_edited_poi;
	SingletonParametersBridge bridge;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("resultView", "chiamato onCreate()");

		setContentView(R.layout.template_info);
		bridge =SingletonParametersBridge.getInstance();
		//bridge.removeParameter("FubMarker");
		playButton= (ImageButton) findViewById(R.id.play);
		playButton.setVisibility(View.INVISIBLE);
		Log.i("resultView", "ok content view");
		play=getResources().getDrawable(R.drawable.play_butt);
		pause=getResources().getDrawable(R.drawable.pause_butt);
		saveFull=getResources().getDrawable(R.drawable.stella_full2);


		TWnome   = (TextView) findViewById(R.id.nome);
		ETdesc   = (EditText) findViewById(R.id.descrizioneRisorsa);
		fotobutton = (ImageButton) findViewById(R.id.fotobutton);
		video = (VideoView) findViewById(R.id.videoInfoView);
		avanti = (ImageButton) findViewById(R.id.buttonGoNextResource);
		indietro = (ImageButton) findViewById(R.id.buttonGoPrevResource);	
		savePref = (ImageButton) findViewById(R.id.saveToPref);	
		TWcategoria   = (TextView) findViewById(R.id.categoria);
		selectList=(Spinner) findViewById(R.id.displaytype);
		Log.i("resultView", "ok bottoni");

		//Setto la visibilitˆ del video e della foto = INVISIBILE
		video.setVisibility(View.INVISIBLE);
		fotobutton.setVisibility(View.INVISIBLE);

		//Setto Lo Spinner
		
		addListenerOnSpinnerItemSelection(selectList);


		/*uri =Uri.parse("");
		fotobutton.setImageURI(uri);
		 */
		View.OnClickListener gestore = new View.OnClickListener() {
			public void onClick(View view) { 
				Bundle bundle;
				switch(view.getId()){

				case R.id.fotobutton:
					Log.i("resultView", "chiamato fotobutton.onclick()");
					testo ="Galleria";
					Toast.makeText(ResultView.this,testo,Toast.LENGTH_SHORT).show();
					bundle= new Bundle();
					bundle.putString("destination","ImageListManager");
					startSubActivity(bundle); 
					finish();
					break;

				case R.id.buttonGoNextResource:
					Log.i("resultView", "chiamato avanti.onclick()");

					finish();
					break;

				case R.id.buttonGoPrevResource:
					Log.i("resultView", "chiamato indietro.onclick()");

					finish();
					break;

				case R.id.play:
					if(!mp.isPlaying()){
						mp.start();
						playButton.setImageDrawable(pause);}
					else{
						mp.pause();
						playButton.setImageDrawable(play);
					}
					break;

				case R.id.saveToPref:

					Log.i("resultView", "salvare nei preferiti");
					bundle= new Bundle();
					bundle.putString("destination","EditPOI");
					startSubActivity(bundle); 

					break;

				}//end switch


			}//end on click
		};//end on click listener

		savePref.setOnClickListener(gestore);
		fotobutton.setOnClickListener(gestore);
		avanti.setOnClickListener(gestore);
		indietro.setOnClickListener(gestore);
		playButton.setOnClickListener(gestore);
	}//end ocreate

	@Override
	protected void onResume() {
		super.onResume();
		try{
			fbm = (FubMarker) bridge.getParameter("FubMarker");
			poi = (Marker) bridge.getParameter("Marker");
		}
		catch(Exception e){}

		if(fbm!=null){
			settaSpinner(selectList);
			if(fbm.getURLaudios().size()!=0)		
				settaAudio();
			list_edited_poi= (List<Marker>) bridge.getParameter("list_edited_poi");
			if(list_edited_poi!=null)
				for(Marker m:list_edited_poi)
					if(m.getName().compareTo(fbm.getName())==0){
						fbm=(FubMarker) m;
						savePref.setImageDrawable(saveFull);}
			Log.i("resultView", ""+fbm.getName());
			nomeRisorsa=fbm.getName();
			testo=fbm.getDescr();
			categoria=fbm.getCategoria();
			Log.i("resultView", "ok fbm e risultati");}
		else if (poi!=null){
			bridge.removeParameter("Marker");
			nomeRisorsa= poi.getName();
			categoria="Personale";
			testo= poi.getDescr();
			savePref.setVisibility(View.INVISIBLE);
			settaSpinnerSoloTesto(selectList);			
		}
		else { nomeRisorsa="Errore";
				categoria="Errore";
				testo="Errore";
				settaSpinnerSoloTesto(selectList);
				savePref.setVisibility(View.INVISIBLE);
		}


	TWnome.setText(nomeRisorsa);
	TWcategoria.setText(categoria);
	ETdesc.setText(testo);
	ETdesc.setVerticalScrollBarEnabled(true);
	ETdesc.setMovementMethod(new ScrollingMovementMethod());
	Log.i("resultView", "ok set");
}


private void settaSpinnerSoloTesto(Spinner selectList2) {
	List<String> list = new ArrayList<String>();
	list.add("TESTO");
	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	selectList2.setAdapter(dataAdapter);
		
	}

private void settaAudio() {
	if(mp == null) 
		mp = new MediaPlayer();
	AudioPlayerManager apm=new AudioPlayerManager();
	String url=fbm.getURLaudios().get(0);
	apm.prepare(mp,url);
	mp.setOnPreparedListener(new OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			playButton.setVisibility(View.VISIBLE);
			playButton.clearFocus();
			playButton.setClickable(true);

		}
	});


}


//Setta valori dello spinner
private void settaSpinner(Spinner selects) {
	//Gestisce spinner a tempo di esecuzione dinamicamente
	List<String> list = new ArrayList<String>();
	list.add("TESTO");
	list.add("TESTO + IMMAGINI");
	list.add("TESTO + IMMAGINI + VIDEO");
	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	selects.setAdapter(dataAdapter);
}

//Gestore della selezione dello spinner
public void addListenerOnSpinnerItemSelection(Spinner spinner1) {
	spinner1.setOnItemSelectedListener(new OnItemSelectedListener()	{


		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			//Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),	Toast.LENGTH_SHORT).show();
			switch(pos){
			case 0:
				//caso che fosse selezionato solo Testo
				video.setVisibility(View.INVISIBLE);
				fotobutton.setVisibility(View.INVISIBLE);
				break;

			case 1:
				//caso che fosse selezionato Testo + Immagini

				video.setVisibility(View.INVISIBLE);
				Toast.makeText(ResultView.this, "Clicka sulla foto per andare alla galleria", Toast.LENGTH_SHORT).show();
				settaFoto();
				fotobutton.setVisibility(View.VISIBLE);
				break;

			case 2:
				//caso che fosse selezionato Testo + Immagini +Video
				Toast.makeText(ResultView.this, "Clicka sulla foto per andare alla galleria", Toast.LENGTH_SHORT).show();
				settaFoto();
				fotobutton.setVisibility(View.VISIBLE);
				settaVideo();
				video.setVisibility(View.VISIBLE);
				break;

			}// end switch
		}//end metodo

		private void settaVideo() {
			try {
				if(fbm.getURLvideos().size()!=0){
					Log.i("ResultView","indiriz img : "+ fbm.getURLvideos().get(0));
					String Url=fbm.getURLvideos().get(0);
					video.setVideoURI(Uri.parse(Url));
					video.setMediaController(new MediaController(ResultView.this));
					video.requestFocus();
					video.start();

				}else{
					Toast.makeText(ResultView.this, "Non ci sono video da caricare", Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception e){
				Toast.makeText(ResultView.this, "errore nel caricare i video", Toast.LENGTH_SHORT).show();
			}
		}

		private void settaFoto() {

			try {
				if(fbm.getURLimmagini().size()!=0){
					Log.i("ResultView","indiriz img : "+fbm.getURLimmagini().get(0));
					im=new ImageManager();
					Bitmap bit=im.loadImageFromWeb(fbm.getURLimmagini().get(0));
					fotobutton.setImageBitmap(bit);}
				else{
					Toast.makeText(ResultView.this, "Non ci sono foto da caricare", Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception e){
				Toast.makeText(ResultView.this, "errore nel caricare la foto", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0)
		{

		}
	});//end onItemSelected
} //end metodo


//android:inputType="textUri"


private void startSubActivity(Bundle bundle) {
	String classe;
	Intent intent;
	classe= bundle.getString("destination");
	if (classe.compareTo("ImageListManager")==0)
		intent =  new Intent(this, ImageListManager.class);
	else if(classe.compareTo("MapManager")==0)
		intent = new Intent(this, MapManager2.class); 
	else if(classe.compareTo("EditPOI")==0)
		intent = new Intent(this, EditPOI.class); 
	else {
		intent=new Intent();
		Log.i("main", "Avvio terza activity fallito per mancanza di contenuto");
		finish();
	}	
	Log.i("resultView","quasi avviato");
	intent.putExtras(bundle);
	startActivity(intent);
}






}//end class