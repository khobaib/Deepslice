package com.deepslice.activity;



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
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.LocationDetails;
import com.deepslice.utilities.AppProperties;

import java.util.ArrayList;

public class LocationFromHistoryActivity extends Activity {
	ImageView addPic;
	ListView listview;
	StateListAdapter eventsAdapter;	
	static String cityName="";
	static String stName="";
	EditText input_text ;
	ProgressDialog pd;
	ImageView clearTextIcon;
	 ArrayList<LocationDetails> deliveryLocationList;
//	 LocationDetails selectedLocation;
	 LocationDetails eBean;
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_history);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		listview = (ListView) findViewById(R.id.listView1);

		deliveryLocationList=new ArrayList<LocationDetails>();
		
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(LocationFromHistoryActivity.this);
        dbInstance.open(); 
        deliveryLocationList = dbInstance.getLocationsHistory("True");
        dbInstance.close();
        
//		AppDao dao=null;
//		try {
//			dao=AppDao.getSingleton(getApplicationContext());
//			dao.openConnection();
//		
//			deliveryLocationList = dao.getLocationsHistory("True");
//			
//
//		} catch (Exception ex)
//		{
//			System.out.println(ex.getMessage());
//		}finally{
//			if(null!=dao)
//				dao.closeConnection();
//		}
		
		if(deliveryLocationList==null || deliveryLocationList.size()<=0)
		{
			TextView restext = (TextView) findViewById(R.id.tv22);
			restext.setText("No recent Address exists");
		}
		
		eventsAdapter = new StateListAdapter(this,R.layout.line_item, deliveryLocationList);
		listview.setAdapter(eventsAdapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				eBean = (LocationDetails) v.getTag();
				if (eBean != null) {
					
					AppProperties.saveLocationObj(LocationFromHistoryActivity.this, eBean);
					
					//Intent intent = new Intent(LocationFromHistoryActivity.this,DateTimeActivity.class);
					
					Intent intent = new Intent(LocationFromHistoryActivity.this,DateTimeActivity.class);
					Bundle bundle = new Bundle();
                    bundle.putString("postCode",eBean.getLocPostalCode());
					bundle.putString("location",eBean.getLocSuburb()+" "+eBean.getLocPostalCode());
					bundle.putString("store",eBean.getLocName());
					bundle.putString("suburbId",eBean.getLocationID());
					intent.putExtras(bundle);
					startActivityForResult(intent, 56);
				}
			}
		});

		
		Button addLoc= (Button)findViewById(R.id.addNewLocation);
		addLoc.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
                Intent intent=new Intent(LocationFromHistoryActivity.this, DeliverySuburbActivity.class);
                startActivity(intent);
				finish();
			}
		});
	}
	
	
////////////////////////////////////////LIST ADAPTER	
	private class StateListAdapter extends ArrayAdapter<LocationDetails> {

		private ArrayList<LocationDetails> items;
		 
		
		public StateListAdapter(Context context, int viewResourceId,
				ArrayList<LocationDetails> items) {
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