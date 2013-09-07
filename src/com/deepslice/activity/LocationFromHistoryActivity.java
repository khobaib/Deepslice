package com.deepslice.activity;



import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Customer;
import com.deepslice.model.CustomerInfo;
import com.deepslice.model.LocationDetails;
import com.deepslice.model.OrderInfo;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;

public class LocationFromHistoryActivity extends Activity {

    ListView listview;
    LocationListAdapter eventsAdapter;	
    ProgressDialog pd;
    List<LocationDetails> deliveryLocationList;
    LocationDetails locationDetails;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_history);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        listview = (ListView) findViewById(R.id.listView1);

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(LocationFromHistoryActivity.this);
        dbInstance.open(); 
        deliveryLocationList = dbInstance.getLocationsHistory("True");
        dbInstance.close();


        if(deliveryLocationList == null || deliveryLocationList.size() <= 0){
            TextView restext = (TextView) findViewById(R.id.tv22);
            restext.setText("No recent Address exists");
        }

        eventsAdapter = new LocationListAdapter(this,R.layout.line_item, deliveryLocationList);
        listview.setAdapter(eventsAdapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                locationDetails = (LocationDetails) parent.getItemAtPosition(position);
                if (locationDetails != null) {                                      
                    AppProperties.saveLocationObj(LocationFromHistoryActivity.this, locationDetails);

                    Intent intent = new Intent(LocationFromHistoryActivity.this, DateTimeActivity.class);
                    Bundle bundle = new Bundle();
//                    bundle.putString("postCode", locationDetails.getLocPostalCode());
//                    bundle.putString("location", locationDetails.getLocSuburb()+" "+locationDetails.getLocPostalCode());
                    bundle.putString("store", locationDetails.getLocName());
//                    bundle.putString("suburbId", locationDetails.getLocationID());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });


        Button addLoc= (Button)findViewById(R.id.addNewLocation);
        addLoc.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(LocationFromHistoryActivity.this, DeliverySuburbActivity.class);
                startActivity(intent);
                //				finish();
            }
        });
    }


    ////////////////////////////////////////LIST ADAPTER	
    private class LocationListAdapter extends ArrayAdapter<LocationDetails> {

        private List<LocationDetails> items;


        public LocationListAdapter(Context context, int viewResourceId, List<LocationDetails> items) {
            super(context, viewResourceId, items);
            this.items = items;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.line_item,null);
            }
            LocationDetails event = items.get(position);
            if (event != null) {

                TextView title = (TextView) convertView.findViewById(R.id.textView1);
                TextView subtitle = (TextView) convertView.findViewById(R.id.textView2);
                StringBuilder sb = new StringBuilder();
                if(event.getUnit() != null && !event.getUnit().isEmpty())
                    sb.append(event.getUnit() + ", ");
                sb.append(event.getStreetNum() + ", "+ event.getStreetName());
                title.setText(sb.toString());
                subtitle.setText(event.getLocSuburb()  +" "+event.getLocPostalCode());

                convertView.setTag(event);
            }
            return convertView;
        }

    }
    ///////////////////////// END LIST ADAPTER	

}