package it.result;

import java.util.List;


import it.arFub.R;
import it.arFub.SingletonParametersBridge;
import it.dataSource.FubMarker;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class ImageListManager extends Activity {
	List<String> UrlImages;
	FubMarker fbm;
	Context c;
	GridView gridview;
	SingletonParametersBridge bridge;
	
	public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.imagegrid);
    gridview = (GridView) findViewById(R.id.gridview);
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		bridge =	SingletonParametersBridge.getInstance();
		fbm = (FubMarker) bridge.getParameter("FubMarker");
		Log.i("imagelist adapter","nomeXX :"+fbm.getName() );
		UrlImages=fbm.getURLimmagini();
		c=getApplicationContext();
	    
	    gridview.setAdapter(new ImageAdapter(this,UrlImages));
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	int numero=position+1;
	            
	        	Toast.makeText(ImageListManager.this, ""+ fbm.getName() +" "+ numero, Toast.LENGTH_SHORT).show();
	            Intent intent = new Intent(c, FotoViewer.class);
	            intent.putExtra("urlImage", fbm.getURLimmagini().get(position));
	            startActivity(intent);
	        }
	    });
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();

	            
	}
	protected void onStop() {
		super.onStop();

	            
	}
	protected void onDestroy() {
		super.onDestroy();

	            
	}





}//end class

