package com.deepslice.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.CarerOverlay;
import com.deepslice.utilities.MyLocation;
import com.deepslice.vo.LocationDetails;
import com.deepslice.vo.StoreLocationMod;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
 
public class CityMapActivity extends MapActivity 
{    Geocoder geoCoder;
	MapController mc;
	MapView mapView;
	MyLocation myLocation = null;
	ProgressDialog pd;
	CarerOverlay itemizedoverlay;
	List<Overlay> mapOverlays;
	
	EditText input_text ;
	ImageView searchIcon;
	ArrayList<StoreLocationMod> storesList;
	 
	 
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_location_map);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mc = mapView.getController();
        geoCoder = new Geocoder(this);
        myLocation = new MyLocation();
        
        Bundle b=this.getIntent().getExtras();
        double cLongi=b.getDouble("cLongi");
        double cLati=b.getDouble("cLati");
        
        
		ImageView emgBack1= (ImageView)findViewById(R.id.farwordImageView);
		emgBack1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		ImageView emgBack= (ImageView)findViewById(R.id.imageView1);
		emgBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		
		Button navShowList= (Button)findViewById(R.id.navShowList);
		navShowList.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		
        mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.flag);

        itemizedoverlay =  new CarerOverlay(drawable,CityMapActivity.this);
        boolean firstRec=true;
        ArrayList<LocationDetails> mpList = AppProperties.locationPointsSearched;
        if(mpList != null && mpList.size()>0)
	        {
	        GeoPoint point=null;
	        for (Iterator<LocationDetails> iterator = mpList.iterator(); iterator.hasNext();) {
	        	LocationDetails cBean = (LocationDetails) iterator.next();
	        	try{
	        	point= new GeoPoint(
                        (int) (Double.parseDouble(cBean.getLocLatitude()) * 1E6), 
                        (int) (Double.parseDouble(cBean.getLocLongitude()) * 1E6));
				
		        OverlayItem overlayitem = new OverlayItem(point, cBean.getLocName(), cBean.getLocStreet()+" \n"+cBean.getLocAddress());
		        itemizedoverlay.addOverlay(overlayitem);
		        
		        if(firstRec==true){
		        	mc.animateTo(point);
		        	firstRec=false;
		        }
		        
	        	}catch (Exception e) {
					e.printStackTrace();
				}
			}
        }
        
        
		    	GeoPoint rpoint = new GeoPoint((int) (cLongi*1000000 ),(int) (cLati*1000000));
    		    OverlayItem overlayitem = new OverlayItem(rpoint, "Your current location", "Your current location");
    		    Drawable icon_green = getResources().getDrawable(R.drawable.current_location);
    	        icon_green.setBounds(-icon_green.getIntrinsicWidth() / 2,-icon_green.getIntrinsicHeight(), icon_green.getIntrinsicWidth() / 2,0);
    		    overlayitem.setMarker(icon_green);
    		    
    	        itemizedoverlay.addOverlay(overlayitem);
    	        mapOverlays.add(itemizedoverlay);
    	        mc.setZoom(15);
//    	        mc.animateTo(rpoint);

    	        
    	        if(itemizedoverlay != null && itemizedoverlay.size() > 0)
    	        	mapOverlays.add(itemizedoverlay);
    }
	
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    	
	
}