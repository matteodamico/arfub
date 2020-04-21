package it.result;

import java.util.ArrayList;
import java.util.List;

import it.arFub.R;
import it.arFub.SingletonParametersBridge;
import it.dataSource.FubMarker;
import it.dataSource.Marker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EditPOI extends Activity{

	private FubMarker fbm;
	private TextView TWnome,TWdescrizione;
	private EditText ETnome,ETdesc;
	private ImageButton savebutton,takeafoto,takeavoice,takeavideo;
	private SingletonParametersBridge bridge;
	Drawable saveFull;
	int CAMERA_PIC_REQUEST = 1337; 
	int IMAGE_FROM_GALLERY=1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("editPOI", "chiamato onCreate()");
		setContentView(R.layout.editpoi);
		bridge = SingletonParametersBridge.getInstance();
		fbm = (FubMarker) bridge.getParameter("FubMarker");

		TWnome   = (TextView) findViewById(R.id.nome);
		TWdescrizione   = (TextView) findViewById(R.id.descrizione);

		ETnome = (EditText) findViewById(R.id.nomeRisorsa);
		ETdesc   = (EditText) findViewById(R.id.descrizioneRisorsa);

		savebutton = (ImageButton) findViewById(R.id.saveToPref);

		takeafoto = (ImageButton) findViewById(R.id.takeafoto);
		takeavideo = (ImageButton) findViewById(R.id.takeavideo);	
		takeavoice = (ImageButton) findViewById(R.id.takeavoice);	

		ETnome.setText(fbm.getName());
		ETdesc.setText(fbm.getDescr());
		saveFull=getResources().getDrawable(R.drawable.stella_full2);


		View.OnClickListener gestore = new View.OnClickListener() {
			public void onClick(View view) { 
				Bundle bundle;
				switch(view.getId()){

				case R.id.takeafoto:
					Log.i("edit", "chiamato takeafoto.onclick()");
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditPOI.this);
					alertDialog.setTitle("Da dove prendo la foto?");
					alertDialog.setCancelable(true);
					alertDialog.setPositiveButton("dalla Fotocamera", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							takefromCamera();							
						}
					});

					alertDialog.setNegativeButton("dalla Galleria", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							takefromGallery();
							dialog.cancel();
						}

						private void takefromGallery() {
							Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI );
							startActivityForResult( i, IMAGE_FROM_GALLERY );
							//db.insertImg(MediaStore.Images.Media.INTERNAL_CONTENT_URI.toString());
							// ((ImageView)findViewById(R.id.image)).setImageURI(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
						}
					});

					AlertDialog alert = alertDialog.create();
					alert.show();
					/*
					bundle= new Bundle();
					bundle.putString("destination","GestionFoto");
					startSubActivity(bundle); */
					break;

				case R.id.saveToPref:
					Log.i("resultView", "chiamo salva nei preferiti()");
					salva();
					bundle= new Bundle();
					bundle.putString("destination","ResultView");
					startSubActivity(bundle); 
					finish();
					break;

				}
			}

			private void takefromCamera() {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
			}


		};
		takeafoto.setOnClickListener(gestore);
		takeavoice.setOnClickListener(gestore);
		takeavideo.setOnClickListener(gestore);
		savebutton.setOnClickListener(gestore);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( requestCode == 1337)	{
			//  data.getExtras()
			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
			/*
	          Now you have received the bitmap..you can pass that bitmap to other activity
	          and play with it in this activity or pass this bitmap to other activity
	          and then upload it to server.*/
			super.onActivityResult(requestCode, resultCode, data);}
		else if ( requestCode == IMAGE_FROM_GALLERY ) {
			if ( resultCode == RESULT_OK ) {
				Uri selectedImage = data.getData();
				//assegni alla tua ImageView l'immagine selezionata
			}
			super.onActivityResult( requestCode, resultCode, data );
		}else{
			Toast.makeText(EditPOI.this, "Picture Not taken", Toast.LENGTH_LONG).show();
		}
	}

	private void salva() {
		List<Marker> list_edited_poi;
		list_edited_poi= (List<Marker>) bridge.getParameter("list_edited_poi");
		String nome=ETnome.getText().toString();
		fbm.setName(nome);
		String desc=ETdesc.getText().toString();
		fbm.setDescr(desc);
		if(list_edited_poi!=null){		
			list_edited_poi.add(fbm);
			SingletonParametersBridge.getInstance().addParameter("list_edited_poi", list_edited_poi);}
		else {
			list_edited_poi=new ArrayList<Marker>();		
			list_edited_poi.add(fbm);
			SingletonParametersBridge.getInstance().addParameter("list_edited_poi", list_edited_poi);
		}
		savebutton.setImageDrawable(saveFull);
	}

	private void startSubActivity(Bundle bundle) {
		String classe;
		Intent intent;
		classe= bundle.getString("destination");
		if (classe.compareTo("ResultView")==0)
			intent =  new Intent(this, ResultView.class);
		else {
			intent=new Intent();
			Log.i("main", "Avvio terza activity fallito per mancanza di contenuto");
			finish();
		}	
		Log.i("main","quasi avviato");
		intent.putExtras(bundle);
		startActivity(intent);
	}

}
