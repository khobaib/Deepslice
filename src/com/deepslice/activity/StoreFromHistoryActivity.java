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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.LocationDetails;
import com.deepslice.utilities.AppProperties;

public class StoreFromHistoryActivity extends Activity {
	ImageView addPic;
	ListView listview;
	StateListAdapter eventsAdapter;	
	static String cityName="";
	static String stName="";
	EditText input_text ;
	ProgressDialog pd;
	ImageView clearTextIcon;
	List<LocationDetails> deliveryLocationList;
	 LocationDetails selectedLocDetails;
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_history);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		listview = (ListView) findViewById(R.id.listView1);
		
		deliveryLocationList = new ArrayList<LocationDetails>();
		
		DeepsliceDatabase dbInstance = new DeepsliceDatabase(StoreFromHistoryActivity.this);
		dbInstance.open();
		deliveryLocationList = dbInstance.getLocationsHistory("False");
		dbInstance.close();


		if(deliveryLocationList==null || deliveryLocationList.size()<=0) {
			TextView restext = (TextView) findViewById(R.id.tv22);
			restext.setText("No recent Pick Up stores exists");
		}
		
		eventsAdapter = new StateListAdapter(this,R.layout.line_item, deliveryLocationList);
		listview.setAdapter(eventsAdapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				selectedLocDetails = (LocationDetails) parent.getItemAtPosition(position);
				if (selectedLocDetails != null) {					
					AppProperties.saveLocationObj(StoreFromHistoryActivity.this, selectedLocDetails);

					Intent intent = new Intent(StoreFromHistoryActivity.this, DateTimeActivity.class);
					Bundle bundle = new Bundle();
//					bundle.putString("location",selectedLocDetails.getLocSuburb()+" " + selectedLocDetails.getLocPostalCode());
					bundle.putString("store",selectedLocDetails.getLocName());
//					bundle.putString("suburbId",selectedLocDetails.getLocationID());
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});

		
		Button addLoc= (Button)findViewById(R.id.addNewLocation);
		addLoc.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(StoreFromHistoryActivity.this, StoreListActivity.class));
//				finish();
			}
		});
	}
	
	
////////////////////////////////////////LIST ADAPTER	
	private class StateListAdapter extends ArrayAdapter<LocationDetails> {

		private List<LocationDetails> items;
		 
		
		public StateListAdapter(Context context, int viewResourceId, List<LocationDetails> items) {
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
				
				title.setText(event.getLocSuburb());
				subtitle.setText(event.getLocStreet() +" "+event.getLocPostalCode());

				convertView.setTag(event);
			}
			return convertView;
		}

	}
///////////////////////// END LIST ADAPTER	

}