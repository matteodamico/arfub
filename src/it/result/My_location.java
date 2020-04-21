package it.result;

import java.util.ArrayList;
import java.util.List;

import it.arFub.R;
import it.arFub.SingletonParametersBridge;
import it.dataSource.FubMarker;
import it.dataSource.Marker;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/*Classe per la gestione dell activity dove sono mostrati in una listView 
 * tutti i marker Salvati(FUB marker modificati o creati nuovi)*/

public class My_location extends Activity {

	private SingletonParametersBridge bridge;
	//static final String[] SavedPOI = new String[] { "Colosseo","Piazza Colonna" };
	String[] saved;
	FubMarker fbm;
	Button addPOI;
	List<Marker> list_edited_poi,list_created_poi;
	ListView listView;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_loc_list);

		//Prendo riferimento al bottone per inserimento nuovo POI
		addPOI = (Button) findViewById(R.id.addNewPOI);

		//Prendo riferimento alla classe singleton 
		bridge = SingletonParametersBridge.getInstance();

		//Prendo riferimento alla list view dove saranno visti i marker salavti
		listView = (ListView) findViewById(R.id.list);
		listView.setTextFilterEnabled(true);

		//Gestore click del bottone della view(inserimento new poi)
		View.OnClickListener gestoreClick = new View.OnClickListener() {
			public void onClick(View view) { 
				Bundle bundle;
				switch(view.getId()){

				case R.id.addNewPOI:
					bundle= new Bundle();
					bundle.putString("destination","addNewPOI");
					//SingletonParametersBridge.getInstance().addParameter("FubMarker", fbm);;

					startSubActivity(bundle);
					break;

				}//end switch
			}//end on click
		};//end listener on click
		addPOI.setOnClickListener(gestoreClick);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				Bundle bundle= new Bundle();
				bundle.putString("destination","ResultView");
				String nomeClickato=(String) ((TextView) view).getText();
				fbm=checkEditedMarker(view,nomeClickato);
				if(fbm!=null)
					bridge.addParameter("FubMarker", fbm);
				else{
					Marker poi=checkCreateMarker(view,nomeClickato);
					if(poi!=null)
						bridge.addParameter("Marker", poi);
				}
				startSubActivity(bundle);

			}


		});//end listView listner

	}//end  on create	

	protected void onResume() {
		super.onResume();
		saved=inizializza();
		if (saved!=null){
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, saved);
			listView.setAdapter(adapter);}
		//setListAdapter(new ArrayAdapter<String>(this, R.layout.my_loc_list,saved));
		else{
			Toast.makeText(getApplicationContext(),"Non ci sono POI salvati", Toast.LENGTH_LONG).show();
			//finish();
		}
	}

	private String[] inizializza() {

		list_edited_poi= (List<Marker>) bridge.getParameter("list_edited_poi");
		list_created_poi =	(List<Marker>) bridge.getParameter("list_created_poi");
		List<Marker> listaSalvati=new ArrayList<Marker>();
		String[] s=null;
		if(list_edited_poi!=null&&list_created_poi!=null){
			listaSalvati.addAll(list_created_poi);
			listaSalvati.addAll(list_edited_poi);
			s=creaArray(listaSalvati);
		}
		else if(list_edited_poi!=null){
			listaSalvati.addAll(list_edited_poi);
			s=creaArray(listaSalvati);
		}

		else if(list_created_poi!=null){
			listaSalvati.addAll(list_created_poi);
			s=creaArray(listaSalvati);
		}

		return s;
	}



	private String[] creaArray(List<Marker> listaSalvati) {
		int size=listaSalvati.size();
		String[] s=new String[size];
		for(int i=0;i<listaSalvati.size();i++)
			s[i]=listaSalvati.get(i).getName();
		return s;

	}

	private FubMarker checkEditedMarker(View view,String nomeClickato) {
		fbm=null;
		if(list_edited_poi!=null)
			for(Marker m:list_edited_poi)
				if(m.getName().compareTo(nomeClickato)==0)
					fbm=(FubMarker) m;
		return fbm;
	}
	private Marker checkCreateMarker(View view,String nomeClickato) {
		Marker poi=null;
		if(list_created_poi!=null)
			for(Marker m:list_created_poi)
				if(m.getName().compareTo(nomeClickato)==0)
					poi=m;
		return poi;
	}	

	private void startSubActivity(Bundle bundle) {
		String classe;
		Intent intent;
		classe= bundle.getString("destination");

		if(classe.compareTo("ResultView")==0)
			intent = new Intent(this, it.result.ResultView.class); 
		else if (classe.compareTo("addNewPOI")==0)
			intent =new Intent(this, it.dataSource.CreateNewPOI.class);

		else {
			intent=new Intent();
			Log.i("my_saved_location", "Avvio seconda activity fallito per mancanza di contenuto");
			finish();
		}	//end else
		intent.putExtras(bundle);

		startActivity(intent);
	}//end metodo

}//end class


