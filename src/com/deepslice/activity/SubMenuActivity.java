package com.deepslice.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.DealOrder;
import com.deepslice.model.Product;
import com.deepslice.model.ProductCategory;
import com.deepslice.model.ProductSubCategory;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.Utils;

public class SubMenuActivity extends Activity{

    List<ProductCategory> productCatList;
    ArrayList<ProductSubCategory> subCatList;
    ArrayList<Product> allProductsList;

    ListView listview;
    TextView tvItemsPrice, tvFavCount;
    
    MyListAdapterSides myAdapter;

    //	String catIds;
    String catType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_menu_drinks);

        Bundle b = this.getIntent().getExtras();

        catType=b.getString("catType");
        TextView title = (TextView) findViewById(R.id.headerTextView);
        title.setText(catType);
        
        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(SubMenuActivity.this);
        dbInstance.open(); 
        productCatList=dbInstance.getSides();
        dbInstance.close();

        listview = (ListView) findViewById(R.id.listView1);				
        myAdapter = new MyListAdapterSides(this,R.layout.line_item_yello, productCatList);
        listview.setAdapter(myAdapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position,
                    long id) {

                ProductCategory eBean = (ProductCategory) v.getTag();
                if (eBean != null) {

                    Intent intent=new Intent(SubMenuActivity.this,ProductsListActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("catId",eBean.getProdCatID());
                    bundle.putString("subCatId","0");
                    bundle.putString("catType","Sides");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SubMenuActivity.this,FavsListActivity.class);
                startActivity(intent);

            }
        });
        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SubMenuActivity.this,MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SubMenuActivity.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////

    private class MyListAdapterSides extends ArrayAdapter<ProductCategory> {

        private List<ProductCategory> items;

        public MyListAdapterSides(Context context, int viewResourceId, List<ProductCategory> items) {
            super(context, viewResourceId, items);
            this.items = items;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.line_item_yello, null);
            }
            ProductCategory event = items.get(position);
            if (event != null) {

                TextView title = (TextView) convertView
                        .findViewById(R.id.itemName);

                title.setText(event.getProdCatCode());

                convertView.setTag(event);
            }
            return convertView;
        }

    }
    // /////////////////////// END LIST ADAPTER
    @Override
    protected void onResume() {
        super.onResume();
        
        List<String> orderInfo = Utils.OrderInfo(SubMenuActivity.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
        
        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }

        
        String favCount = Utils.FavCount(SubMenuActivity.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }
    }
}
