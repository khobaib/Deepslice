package com.deepslice.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.deepslice.adapter.DealCrustAdapter;
import com.deepslice.adapter.ProductCrustAdapter;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.CouponDetails.CrustProducts;
import com.deepslice.model.ProdAndSubCategory;
import com.deepslice.model.ProductSubCategory;
import com.deepslice.model.Product;
import com.deepslice.utilities.DeepsliceApplication;

public class PizzaCrustActivity extends Activity{

    TextView favCountTxt;
    int currentCount=1;

    Product selectedBean;

    ListView lvCrustList;
    
    ProductCrustAdapter productCrustAdapter;
    DealCrustAdapter dealCrustAdapter;

    ArrayList<ProductSubCategory> crustList;
    ArrayList<ProdAndSubCategory> prodAndSubCategories;
    String crustCatId="", crustSubCatId="";
    String currentProductId="";
    DeepsliceApplication appInstance;
    boolean isDeal=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crusts);

        appInstance=(DeepsliceApplication)getApplication();
        lvCrustList = (ListView) findViewById(R.id.listView1);
        Bundle b = this.getIntent().getExtras();
        if(b.getBoolean("isDeal",false)){
            isDeal=true;
            selectedBean=(Product)b.getSerializable("selectedProduct");
            crustCatId=b.getString("catId");
            crustSubCatId=b.getString("subCatId");
            currentProductId=b.getString("prdID");
            //            myListAdapterDealSides = new MyListAdapterDealSides(this,R.layout.line_item_crust, appInstance.getCouponData().getProdAndSubCategories());
            dealCrustAdapter = new DealCrustAdapter(PizzaCrustActivity.this, appInstance.getCouponDetails().getProdAndSubCatID(), currentProductId);

            lvCrustList.setAdapter(dealCrustAdapter);
        }else {
            selectedBean=(Product)b.getSerializable("selectedProduct");
            crustCatId=b.getString("catId");
            crustSubCatId=b.getString("subCatId");

            DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaCrustActivity.this);
            dbInstance.open(); 
            crustList=dbInstance.getPizzaCrusts(selectedBean.getProdCatID(),selectedBean.getSubCatID1());
            dbInstance.close();

            productCrustAdapter = new ProductCrustAdapter(PizzaCrustActivity.this, crustList, crustCatId, crustSubCatId);
            lvCrustList.setAdapter(productCrustAdapter);
        }




        lvCrustList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(isDeal){
                    CrustProducts crustProducts=(CrustProducts) parent.getItemAtPosition(position);

                    Intent resultData = new Intent();
                    resultData.putExtra("name", crustProducts.getSubCat2Code());
                    resultData.putExtra("catId", crustProducts.getProdID());
                    resultData.putExtra("subCatId", crustProducts.getSubCat2Id());
                    setResult(Activity.RESULT_OK, resultData);
                    finish();

                }else {
                    ProductSubCategory eBean = (ProductSubCategory) parent.getItemAtPosition(position);

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

//    private class MyListAdapterSides extends ArrayAdapter<ProductSubCategory> {
//
//        private ArrayList<ProductSubCategory> items;
//
//        public MyListAdapterSides(Context context, int viewResourceId,
//                ArrayList<ProductSubCategory> items) {
//            super(context, viewResourceId, items);
//            this.items = items;
//
//        }
//
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = mInflater.inflate(R.layout.line_item_crust, null);
//            }
//            final ProductSubCategory event = items.get(position);
//            if (event != null) {
//
//                TextView title = (TextView) convertView.findViewById(R.id.textView1);
//                title.setText(event.getSubCatDesc());
//
//                ImageView tick = (ImageView) convertView.findViewById(R.id.imageView2);
//                if(event.getProdCatID().equals(crustCatId) && event.getSubCatID().equals(crustSubCatId))
//                    tick.setVisibility(View.VISIBLE);
//                else
//                    tick.setVisibility(View.INVISIBLE);
//
//
//                convertView.setTag(event);
//            }
//            return convertView;
//        }
//
//    }
    
    
    // /////////////////////// END LIST ADAPTER
    //rukshan add
//    private class MyListAdapterDealSides extends ArrayAdapter<CrustProducts> {
//
////        private List<CrustProducts> items;
//
//        public MyListAdapterDealSides(Context context, int viewResourceId, List<CrustProducts> items) {
//            super(context, viewResourceId, items);
//            //            this.items = items;
//
//        }
//
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = mInflater.inflate(R.layout.line_item_crust, null);
//            }
//            final CrustProducts event = getItem(position);
//            if (event != null) {
//
//                TextView title = (TextView) convertView.findViewById(R.id.textView1);
//                title.setText(event.getSubCat2Code());
//
//                ImageView tick = (ImageView) convertView.findViewById(R.id.imageView2);
//                if(event.getProdID().equals(currentProductId) )
//                    tick.setVisibility(View.VISIBLE);
//                else
//                    tick.setVisibility(View.INVISIBLE);
//
//
//                convertView.setTag(event);
//            }
//            return convertView;
//        }
//
//    }
}
