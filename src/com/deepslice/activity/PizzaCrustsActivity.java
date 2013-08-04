package com.deepslice.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.deepslice.database.AppDao;
import com.deepslice.model.AllProductsVo;
import com.deepslice.model.ToppingsAndSaucesVo;

public class PizzaCrustsActivity extends Activity{

	TextView favCountTxt;
	int currentCount=1;

	AllProductsVo selectedBean;
	
	ListView listview;
	MyListAdapterSides myAdapter;

	ArrayList<ToppingsAndSaucesVo> toppingsList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.toppings);
		
		Bundle b = this.getIntent().getExtras();

		selectedBean=(AllProductsVo)b.getSerializable("selectedProduct");
		
		
		  AppDao dao=null;
			try {
				dao=AppDao.getSingleton(getApplicationContext());
				dao.openConnection();
				
				toppingsList=dao.getPizzaToppings(selectedBean.getProdID());
				
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}finally{
				if(null!=dao)
					dao.closeConnection();
			}
	
			listview = (ListView) findViewById(R.id.listView1);				
			myAdapter = new MyListAdapterSides(this,R.layout.line_item_toppings, toppingsList);
			listview.setAdapter(myAdapter);
			
			listview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position,
						long id) {
					
				}
			});
			
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////

	private class MyListAdapterSides extends ArrayAdapter<ToppingsAndSaucesVo> {

		private ArrayList<ToppingsAndSaucesVo> items;

		public MyListAdapterSides(Context context, int viewResourceId,
				ArrayList<ToppingsAndSaucesVo> items) {
			super(context, viewResourceId, items);
			this.items = items;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.line_item_toppings, null);
			}
			ToppingsAndSaucesVo event = items.get(position);
			if (event != null) {

				TextView title = (TextView) convertView
						.findViewById(R.id.textView1);

				title.setText(event.getToppingCode());

				convertView.setTag(event);
			}
			return convertView;
		}

	}
	// /////////////////////// END LIST ADAPTER
}
