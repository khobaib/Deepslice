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
import com.deepslice.utilities.AppProperties;
import com.deepslice.vo.LocationDetails;

import java.util.ArrayList;

public class StoreFromHistoryActivity extends Activity {
	ImageView addPic;
	ListView listview;
	StateListAdapter eventsAdapter;	
	static String cityName="";
	static String stName="";
	EditText input_text ;
	ProgressDialog pd;
	ImageView clearTextIcon;
	 ArrayList<LocationDetails> deliveryLocationList;
	 LocationDetails eBean;
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_history);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		listview = (ListView) findViewById(R.id.listView1);
		
		deliveryLocationList=new ArrayList<LocationDetails>();
		
		AppDao dao=null;
		try {
			dao=AppDao.getSingleton(getApplicationContext());
			dao.openConnection();
		
			deliveryLocationList = dao.getLocationsHistory("False");
			

		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}finally{
			if(null!=dao)
				dao.closeConnection();
		}

		if(deliveryLocationList==null || deliveryLocationList.size()<=0)
		{
			TextView restext = (TextView) findViewById(R.id.tv22);
			restext.setText("No recent Pick Up stores exists");
		}
		
		eventsAdapter = new StateListAdapter(this,R.layout.line_item, deliveryLocationList);
		listview.setAdapter(eventsAdapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				eBean = (LocationDetails) v.getTag();
				if (eBean != null) {
					
					AppProperties.saveLocationObj(StoreFromHistoryActivity.this, eBean);
					
					
//					Intent intent;
//					if(AppProperties.isLoogedIn)
//						intent=new Intent(StoreFromHistoryActivity.this, MenuActivity.class);
//					else
//						intent=new Intent(StoreFromHistoryActivity.this, CustomerDetailsActivity.class);
					Intent intent = new Intent(StoreFromHistoryActivity.this,DateTimeActivity.class);
					Bundle bundle = new Bundle();
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
				startActivity(new Intent(StoreFromHistoryActivity.this, StoreListActivity.class));
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
				
				title.setText(event.getLocSuburb());
				subtitle.setText(event.getLocStreet() +" "+event.getLocPostalCode());

				convertView.setTag(event);
			}
			return convertView;
		}

	}
///////////////////////// END LIST ADAPTER	

}