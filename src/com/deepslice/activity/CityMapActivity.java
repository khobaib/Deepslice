package com.deepslice.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.LocationDetails;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CityMapActivity extends FragmentActivity implements
LocationListener, OnMarkerClickListener, OnInfoWindowClickListener {

    //	Geocoder geoCoder;
    //	MapController mc;
    GoogleMap mMap;
    //	MyLocation myLocation = null;
    ProgressDialog pd;
    //	CarerOverlay itemizedoverlay;
    //	List<Overlay> mapOverlays;
    private HashMap<Marker, LocationDetails> storeMarkerMap = new HashMap<Marker, LocationDetails>();

    EditText input_text;
    ImageView searchIcon;
    //	ArrayList<StoreLocationMod> storesList;
    //	private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(this, "92b170cf");
        
        setContentView(R.layout.store_location_map);
        // mapView = (MapView) findViewById(R.id.mapview);

        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapview)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
        setupMarker();

        // mMap.setBuiltInZoomControls(true);
        // mc = mMap.getController();
        // geoCoder = new Geocoder(this);
        // myLocation = new MyLocation();

        Bundle b = this.getIntent().getExtras();
        double cLongi = b.getDouble("cLongi");
        double cLati = b.getDouble("cLati");

        //		ImageView emgBack1 = (ImageView) findViewById(R.id.farwordImageView);
        //		emgBack1.setOnClickListener(new OnClickListener() {
        //			public void onClick(View v) {
        //				finish();
        //			}
        //		});
        //
        
        
        ImageView emgBack = (ImageView) findViewById(R.id.imageView1);
        emgBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Button navShowList = (Button) findViewById(R.id.navShowList);
        navShowList.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // mapOverlays = mapView.getOverlays();
        // Drawable drawable = this.getResources().getDrawable(R.drawable.flag);
        //
        // itemizedoverlay = new CarerOverlay(drawable, CityMapActivity.this);
        // boolean firstRec = true;

        // }
        //
        // GeoPoint rpoint = new GeoPoint((int) (cLongi * 1000000),
        // (int) (cLati * 1000000));
        // OverlayItem overlayitem = new OverlayItem(rpoint,
        // "Your current location", "Your current location");
        // Drawable icon_green = getResources().getDrawable(
        // R.drawable.current_location);
        // icon_green.setBounds(-icon_green.getIntrinsicWidth() / 2,
        // -icon_green.getIntrinsicHeight(),
        // icon_green.getIntrinsicWidth() / 2, 0);
        // overlayitem.setMarker(icon_green);
        //
        // itemizedoverlay.addOverlay(overlayitem);
        // mapOverlays.add(itemizedoverlay);
        // mc.setZoom(15);
        // // mc.animateTo(rpoint);
        //
        // if (itemizedoverlay != null && itemizedoverlay.size() > 0)
        // mapOverlays.add(itemizedoverlay);
    }

    private void setUpMap() {
        // Hide the zoom controls as the button panel will cover it.
        // mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.

        // Set listeners for marker events. See the bottom of this class for
        // their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        mMap.setMyLocationEnabled(true);

        //		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //		locationManager.requestLocationUpdates(
        //				LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

    }
    
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        BugSenseHandler.startSession(this);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        BugSenseHandler.closeSession(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO Auto-generated method stub
        return false;
    }

    private void setupMarker() {	

        ArrayList<LocationDetails> mpList = AppProperties.locationPointsSearched;
        double minDIstInMeter = 999999999;
        LocationDetails nearestLocation = new LocationDetails();
        if (mpList != null && mpList.size() > 0) {
            //			GeoPoint point = null;
            for (Iterator<LocationDetails> iterator = mpList.iterator(); iterator.hasNext();) {
                LocationDetails locDetails = (LocationDetails) iterator.next();
                //				point = new GeoPoint(
                //						(int) (Double.parseDouble(cBean.getLocLatitude()) * 1E6),
                //						(int) (Double.parseDouble(cBean.getLocLongitude()) * 1E6));

                // retrieving nearest location
                Location shopLoc = new Location("Shop Location");
                shopLoc.setLatitude(Double.parseDouble(locDetails.getLocLatitude()));
                shopLoc.setLongitude(Double.parseDouble(locDetails.getLocLongitude()));
                double distInMeter = shopLoc.distanceTo(Utils.mLocation);
                if(distInMeter < minDIstInMeter){
                    nearestLocation = locDetails;
                }

                //				if (firstRec == true) {
                //					LatLng latLng = new LatLng(Double.parseDouble(locDetails.getLocLatitude()),Double.parseDouble(locDetails.getLocLongitude()));
                //					CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                //					mMap.animateCamera(cameraUpdate);
                ////					locationManager.removeUpdates(this);
                //					firstRec = false;
                //				}
            }
            LatLng latLng = new LatLng(Double.parseDouble(nearestLocation.getLocLatitude()),Double.parseDouble(nearestLocation.getLocLongitude()));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
            mMap.animateCamera(cameraUpdate);
        }

        storeMarkerMap = new HashMap<Marker, LocationDetails>();

        Log.d("Location", "-------- how many Location? = " + storeMarkerMap.size());
        for(int i = 0; i < mpList.size(); i++ ){
            LocationDetails location = mpList.get(i);
            LatLng storePosition = new LatLng(Double.parseDouble(location.getLocLatitude()), Double.parseDouble(location.getLocLongitude()));
            Marker marker = mMap.addMarker(new MarkerOptions()
            .position(storePosition)
            .title(location.getLocName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pizza_pin)));
            storeMarkerMap.put(marker, location);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        LocationDetails selectedLocation = storeMarkerMap.get(marker);
        Log.d("??????", "location name = " + selectedLocation.getLocName());

        AppProperties.saveLocationObj(CityMapActivity.this, selectedLocation);

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(CityMapActivity.this);
        dbInstance.open(); 
        dbInstance.insertLocationHistory(selectedLocation, "False");
        dbInstance.close();

        Intent intent = new Intent(CityMapActivity.this, DateTimeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("store", selectedLocation.getLocName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

}