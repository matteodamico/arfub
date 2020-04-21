package it.result;

import it.arFub.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import android.widget.ImageView;


public class FotoViewer extends Activity {


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("main", "chiamato onCreate()");
		setContentView(R.layout.fotoview);
		ImageView img =(ImageView) findViewById(R.id.fotoView);
		ImageManager im=new ImageManager();
		String valore = getIntent().getExtras().getString("urlImage");
		Bitmap bit=im.loadImageFromWeb(valore);
		img.setImageBitmap(bit);
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
