package com.deepslice.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.deepslice.database.AppDao;
import com.deepslice.vo.AllProductsVo;
import com.deepslice.vo.GlobalObject;
import com.deepslice.vo.ProdAndSubCategory;
import com.deepslice.vo.SubCategoryVo;

import java.util.ArrayList;

public class PizzaCrustActivity extends Activity{

	TextView favCountTxt;
	int currentCount=1;

	AllProductsVo selectedBean;
	
	ListView listview;
	MyListAdapterSides myAdapter;
    MyListAdapterDealSides myListAdapterDealSides;

	ArrayList<SubCategoryVo> crustList;
    ArrayList<ProdAndSubCategory> prodAndSubCategories;
	String crustCatId="", crustSubCatId="";
    String currentProductId="";
    GlobalObject globalObject;
    boolean isDeal=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crusts);
        globalObject=(GlobalObject)getApplication();
        listview = (ListView) findViewById(R.id.listView1);
        Bundle b = this.getIntent().getExtras();
        if(b.getBoolean("isDeal",false)){
            isDeal=true;
            selectedBean=(AllProductsVo)b.getSerializable("selectedProduct");
            crustCatId=b.getString("catId");
            crustSubCatId=b.getString("subCatId");
            currentProductId=b.getString("prdID");
            myListAdapterDealSides = new MyListAdapterDealSides(this,R.layout.line_item_crust, globalObject.getCouponData().getProdAndSubCategories());
            listview.setAdapter(myListAdapterDealSides);
        }else {
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
            myAdapter = new MyListAdapterSides(this,R.layout.line_item_crust, crustList);
            listview.setAdapter(myAdapter);
        }
	


			
			listview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position,
						long id) {
					if(isDeal){
                        ProdAndSubCategory subCategory=(ProdAndSubCategory)v.getTag();
                        if(subCategory!=null)
                        {
                            Intent resultData = new Intent();
                            resultData.putExtra("name", subCategory.getSubCat2Code());
                            resultData.putExtra("catId", subCategory.getProdID());
                            resultData.putExtra("subCatId", subCategory.getSubCat2Id());
                            setResult(Activity.RESULT_OK, resultData);
                            finish();
                        }
                    }else {
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
    //rukshan add
    private class MyListAdapterDealSides extends ArrayAdapter<ProdAndSubCategory> {

        private ArrayList<ProdAndSubCategory> items;

        public MyListAdapterDealSides(Context context, int viewResourceId,
                                  ArrayList<ProdAndSubCategory> items) {
            super(context, viewResourceId, items);
            this.items = items;

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.line_item_crust, null);
            }
            final ProdAndSubCategory event = items.get(position);
            if (event != null) {

                TextView title = (TextView) convertView.findViewById(R.id.textView1);
                title.setText(event.getSubCat2Code());

                ImageView tick = (ImageView) convertView.findViewById(R.id.imageView2);
                if(event.getProdID().equals(currentProductId) )
                    tick.setVisibility(View.VISIBLE);
                else
                    tick.setVisibility(View.INVISIBLE);


                convertView.setTag(event);
            }
            return convertView;
        }

    }
}
