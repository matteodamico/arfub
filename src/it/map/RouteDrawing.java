package it.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class RouteDrawing extends Overlay{

	private Projection projection;
	GeoPoint gP1,gP2;
	
	    public RouteDrawing(){
	    }
	       

	    public void draw(Canvas canvas, MapView mapView, boolean shadow){
	        super.draw(canvas, mapView, shadow);
	        projection = mapView.getProjection();
	        
	        Paint   mPaint = new Paint();
	        mPaint.setDither(true);
	        mPaint.setColor(Color.RED);
	        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	        mPaint.setStrokeJoin(Paint.Join.ROUND);
	        mPaint.setStrokeCap(Paint.Cap.ROUND);
	        mPaint.setStrokeWidth(2);

	         

	        Point p1 = new Point();
	        Point p2 = new Point();
	        Path path = new Path();

	        projection.toPixels(gP1, p1);
	        projection.toPixels(gP2, p2);

	        path.moveTo(p2.x, p2.y);
	        path.lineTo(p1.x,p1.y);

	        canvas.drawPath(path, mPaint);
	     }
	    
	    
	    public void setGeopoints(GeoPoint geo1,GeoPoint geo2){
	    	gP1 = geo1;
	        gP2 = geo2;
	    	
	    }
	    
}