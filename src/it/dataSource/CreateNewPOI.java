package it.dataSource;

import java.util.ArrayList;
import java.util.List;

import it.arFub.R;
import it.arFub.SingletonParametersBridge;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;



public class CreateNewPOI extends Activity{

	private static final int GET_LAT_DIALOG = 4;
	private static final int GET_LON_DIALOG = 5;
	private static final int GET_ALT_DIALOG = 6;
	private static int CAMERA_PIC_REQUEST = 1337; 
	private static int IMAGE_FROM_GALLERY=1;
	private Context mcontext;
	private int n;
	private List<Marker> list_created_poi;
	//private TextView TWnome,TWdescrizione;

	private EditText ETnome,ETdesc, Etext;
	private ImageButton savebutton,takeafoto,takeavoice,takeavideo;
	private Button GetfromGPS,GetfromManual;
	private SingletonParametersBridge bridge;
	Drawable saveFull;
	private TextView lat,lon;
	private LocationManager locationManager;
	private Location location;
	private LocationListener locationListener;

	//int latitude,longitude,altitude;

	double geoAlt,geoLat,geoLon;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("newPOI", "chiamato onCreate()");
		setContentView(R.layout.newpoi);
		bridge = SingletonParametersBridge.getInstance();
		list_created_poi = (List<Marker>) bridge.getParameter("list_created_poi");
		mcontext= getApplicationContext();
		/*TWnome   = (TextView) findViewById(R.id.nome);
		TWdescrizione   = (TextView) findViewById(R.id.descrizione);*/
		geoLat=0;

		ETnome = (EditText) findViewById(R.id.nomeRisorsa);
		ETdesc   = (EditText) findViewById(R.id.descrizioneRisorsa);

		lat=(TextView) findViewById(R.id.lat);
		lon=(TextView) findViewById(R.id.lon);
		savebutton = (ImageButton) findViewById(R.id.saveToPref);

		takeafoto = (ImageButton) findViewById(R.id.takeafoto);
		takeavideo = (ImageButton) findViewById(R.id.takeavideo);	
		takeavoice = (ImageButton) findViewById(R.id.takeavoice);	
		GetfromGPS = (Button) findViewById (R.id.coordGps);
		GetfromManual= (Button) findViewById(R.id.coordManual);
		/*ETnome.setText(fbm.getName());
		ETdesc.setText(fbm.getDescr());*/
		saveFull=getResources().getDrawable(R.drawable.stella_full2);
		lat.setVisibility(View.INVISIBLE);
		lon.setVisibility(View.INVISIBLE);

		View.OnClickListener gestore = new View.OnClickListener() {
			public void onClick(View view) { 
				//Bundle bundle;
				switch(view.getId()){

				case R.id.takeafoto:
					Log.i("edit", "chiamato takeafoto.onclick()");
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateNewPOI.this);
					alertDialog.setTitle("Da dove prendo la foto?");

					alertDialog.setCancelable(false);
					alertDialog.setPositiveButton("dalla Fotocamera", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							takefromCamera();   

						}//end on click
					});//end positive button
					alertDialog.setNegativeButton("dalla Galleria", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							takefromGallery();
							dialog.cancel();
						}});//end negative button

					AlertDialog alert = alertDialog.create();
					alert.show();
					/*bundle= new Bundle();
					bundle.putString("destination","GestionFoto");
					startSubActivity(bundle); */
					break;

				case R.id.saveToPref:
					Log.i("new_Poi", "chiamo salva nei preferiti()");
					salva();
					/*bundle= new Bundle();
					bundle.putString("destination","ResultView");
					startSubActivity(bundle); */

					break;
				case R.id.coordGps:
					Log.i("new_Poi","clicckato prendi coordinate dal gps");
					Toast.makeText(it.dataSource.CreateNewPOI.this, "Prendo le coordinate dal GPS", Toast.LENGTH_SHORT).show();
					prendiCoordGPS();
					break;
				case R.id.coordManual:
					n=0;
					showDialog(GET_LAT_DIALOG);
					/*
					showDialog(GET_ALT_DIALOG);
					showDialog(GET_LON_DIALOG);
					*/
					break;

				}
			}
		};
		takeafoto.setOnClickListener(gestore);
		takeavoice.setOnClickListener(gestore);
		takeavideo.setOnClickListener(gestore);
		savebutton.setOnClickListener(gestore);
		GetfromGPS.setOnClickListener(gestore);
		GetfromManual.setOnClickListener(gestore);
	}//end create



	protected void onResume() {
		super.onResume();
	}
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
		case GET_LAT_DIALOG:
			return getCoordinateDialog(4);
			
		case GET_LON_DIALOG:
			return getCoordinateDialog(5);
			
		case GET_ALT_DIALOG:
			return getCoordinateDialog(6);
			
		}
		return null;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( requestCode == 1337)	{
			//  data.getExtras()
			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
			/* Now you have received the bitmap..you can pass that bitmap to other activity
          and play with it in this activity or pass this bitmap to other activity
          and then upload it to server.*/
			super.onActivityResult(requestCode, resultCode, data);}
		else if ( requestCode == IMAGE_FROM_GALLERY ) {
			if ( resultCode == RESULT_OK ) {
				Uri selectedImage = data.getData();

				//assegni alla tua ImageView l'immagine selezionata
			}
			super.onActivityResult( requestCode, resultCode, data );
		}
		else{
			Toast.makeText(CreateNewPOI.this, "Picture NOt taken", Toast.LENGTH_LONG).show();
		}

	}


	private AlertDialog getCoordinateDialog(final int p){
		String testo="";
		Etext = new EditText(this);
		if(p==4){
			testo="Latitudine";			
			Etext.setHint("es. 41195119");}
		if(p==5){
			testo="Longitudine";
			Etext.setHint("es.12195119");}
		else if(p==6){
			testo="Altitudine";
			Etext.setHint("in metri");}

		Etext.setInputType(0x00000002);
		//Etext.setInputType(InputType.TYPE_CLASS_NUMBER);
		//Etext.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		return new AlertDialog.Builder(this).setTitle("Inserisci la "+testo).setView(Etext)
		.setPositiveButton("Conferma", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				int xxx=Integer.parseInt(Etext.getText().toString());
				settaparametro(p,xxx);
				n++;
				if(n==1)
					showDialog(GET_LON_DIALOG);
				else if(n==2)
					showDialog(GET_ALT_DIALOG);
				

			}

		})
		.setNegativeButton("Cancella", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.cancel();
			}
		}).create();
	}


	private void settaparametro(int p, int xxx) {
		if(p==4){
			geoLat=xxx;
			lat.setText("Lat: "+String.valueOf(geoLat/1E6));
			lat.setVisibility(View.VISIBLE);}

		else if(p==5){
			geoLon=xxx;
			lon.setText("Long: "+String.valueOf(geoLon/1E6));
			lon.setVisibility(View.VISIBLE);}
		else if(p==6)
			geoAlt=xxx;	
	}

	private void prendiCoordGPS() {
		this.getCurrentLocation();
		if  ( location != null ) {
			Log.i("mapManager.loadclick", "location full");
			locationManager.removeUpdates(locationListener);	
			geoLat=location.getLatitude()*1E6;
			geoLon=location.getLongitude()*1E6;
			geoAlt=location.getAltitude();
			lat.setText(String.valueOf(geoLat));
			lat.setVisibility(View.VISIBLE);
			lon.setText(String.valueOf(geoLon));
			lon.setVisibility(View.VISIBLE);
			Toast.makeText(it.dataSource.CreateNewPOI.this, "Prese Coordinate ed associate al POI.", Toast.LENGTH_LONG).show();
		}

		else{Toast.makeText(it.dataSource.CreateNewPOI.this, "Impossibile ricevere segnale GPS.", Toast.LENGTH_LONG).show();
		}
	}

	private void takefromCamera() {
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		// request code
		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
	}

	private void takefromGallery() {
		Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI );
		startActivityForResult( i, IMAGE_FROM_GALLERY );
		// ((ImageView)findViewById(R.id.image)).setImageURI(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
	}

	private void getCurrentLocation() {

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if(!locationManager.isProviderEnabled("gps")){
			Toast.makeText(this, "GPS è attualmente disabilitato. E' possibile abilitarlo dal menu impostazioni.", Toast.LENGTH_LONG).show();}
		else {locationListener = new LocationListener() {


			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onLocationChanged(Location loc) {
				// TODO Auto-generated method stub
				location = loc;
			}
		};	
		// Registriamo il LocationListener al provider GPS
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		}}//end metodo


	private void salva() {
		try {
			String nome=ETnome.getText().toString();
			String desc=ETdesc.getText().toString();
			Drawable draw =mcontext.getResources().getDrawable(R.drawable.mark);
			if (geoLat==0){
				Toast.makeText(CreateNewPOI.this, "Coordinate non inserite", Toast.LENGTH_LONG).show();
			}
			else{
				My_POI mypoi=new My_POI(nome, desc, (int)geoLat, (int)geoLon, (int)geoAlt,new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),draw);
				if(list_created_poi!=null){		
					list_created_poi.add(mypoi);
					Toast.makeText(this, "POI salvato.", Toast.LENGTH_LONG).show();
				}
				else {
					list_created_poi=new ArrayList<Marker>();		
					list_created_poi.add(mypoi);
					Toast.makeText(this, "Salvato.", Toast.LENGTH_LONG).show();
				}
				bridge.addParameter("list_created_poi", list_created_poi);
				bridge.removeParameter("FubMarker");
				savebutton.setImageDrawable(saveFull);
				finish();
			}}catch(Exception e){
				Toast.makeText(this, "Impossibile salvare il POI, ricontrolla i campi inseriti.", Toast.LENGTH_LONG).show();}

	}//end salva


}//end class
