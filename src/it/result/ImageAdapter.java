package it.result;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	List<String> imageList;
	ImageManager im;

	public ImageAdapter(Context c) {
		this.mContext = c;
		this.imageList=new ArrayList<String>();
		im=new ImageManager();
	}
	public ImageAdapter(Context c, List<String> imageList) {
		this.mContext = c;
		this.imageList=imageList;
		im=new ImageManager();
	}  

	public int getCount() {
		return  imageList.size();  }

	public Object getItem(int position) {
		imageList.get(position);
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView=null;
		try {Bitmap bm= im.loadImageFromWeb(imageList.get(position));
		//Uri myUri = Uri.parse(imageList.get(position));
		//Uri myUri = Uri.parse("http://lnx.ginevra2000.it/Disney/peterpan1/crockodile.gif");
		//Uri myUri = Uri.parse("http://www.fidomicio.provincia.ancona.it/Immagini/foto_cane1_imagelarge.jpg");
		if (convertView == null) {  // if it's not recycled, initialize some attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(88, 88));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}

		//imageView.setImageURI(myUri);
		imageView.setImageBitmap(bm);
		}
		catch(Exception e){
			Toast.makeText(mContext, "errore nel caricare le foto", Toast.LENGTH_SHORT).show();
		}
		return imageView;
	}
	
}
