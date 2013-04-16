package com.deepslice.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deepslice.database.AppDao;
import com.deepslice.utilities.AppProperties;
import com.deepslice.vo.AllProductsVo;
import com.deepslice.vo.SubCategoryVo;

public class PizzaCrustActivity extends Activity{

	TextView favCountTxt;
	int currentCount=1;

	AllProductsVo selectedBean;
	
	ListView listview;
	MyListAdapterSides myAdapter;

	ArrayList<SubCategoryVo> crustList;
	String crustCatId="", crustSubCatId="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crusts);
		Bundle b = this.getIntent().getExtras();
		selectedBean=(AllProductsVo)b.getSerializable("selectedProduct");
		crustCatId=b.getString("catId");
		crustSubCatId=b.getString("subCatId");
		  AppDao dao=null;
			try {
				dao=AppDao.getSingleton(getApplicationContext());
				dao.openConnection();
				
				crustList=dao.getPizzaCrusts(selectedBean.getProdCatID(),selectedBean.getSubCatID1());
				
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}finally{
				if(null!=dao)
					dao.closeConnection();
			}
	
			listview = (ListView) findViewById(R.id.listView1);				
			myAdapter = new MyListAdapterSides(this,R.layout.line_item_crust, crustList);
			listview.setAdapter(myAdapter);
			
			listview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position,
						long id) {
					
					SubCategoryVo eBean = (SubCategoryVo) v.getTag();
					if (eBean != null) {
						Intent resultData = new Intent();
						resultData.putExtra("name", eBean.getSubCatDesc());
						resultData.putExtra("catId", eBean.getProdCatID());
						resultData.putExtra("subCatId", eBean.getSubCatID());
						setResult(Activity.RESULT_OK, resultData);
						finish();
						
					}
				}
			});
		
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////

	private class MyListAdapterSides extends ArrayAdapter<SubCategoryVo> {

		private ArrayList<SubCategoryVo> items;

		public MyListAdapterSides(Context context, int viewResourceId,
				ArrayList<SubCategoryVo> items) {
			super(context, viewResourceId, items);
			this.items = items;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.line_item_crust, null);
			}
			final SubCategoryVo event = items.get(position);
			if (event != null) {

				TextView title = (TextView) convertView.findViewById(R.id.textView1);
				title.setText(event.getSubCatDesc());
				
				ImageView tick = (ImageView) convertView.findViewById(R.id.imageView2);
					if(event.getProdCatID().equals(crustCatId) && event.getSubCatID().equals(crustSubCatId))
						tick.setVisibility(View.VISIBLE);
					else
						tick.setVisibility(View.INVISIBLE);
				
				
				convertView.setTag(event);
			}
			return convertView;
		}

	}
	// /////////////////////// END LIST ADAPTER
}
