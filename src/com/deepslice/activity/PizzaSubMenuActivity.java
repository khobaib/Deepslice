package com.deepslice.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.DealOrder;
import com.deepslice.model.Product;
import com.deepslice.model.ProductCategory;
import com.deepslice.model.ProductSubCategory;

public class  PizzaSubMenuActivity extends Activity{

    private static final int REQUEST_CODE_IS_PIZZA_HALF = 1001;

    ArrayList<ProductCategory> productCatList;
    ArrayList<ProductSubCategory> subCatList;
    ArrayList<Product> allProductsList;

    Boolean isHalf = false;

    ListView listview;
    MyListAdapterPizza myAdapter;
    ProgressDialog pd;
//    String catType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_menu_pizza3);
//        Bundle b = this.getIntent().getExtras();

//        catType=b.getString("catType");
        isHalf = getIntent().getExtras().getBoolean("isHalf", false);
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaSubMenuActivity.this);
        dbInstance.open();
        subCatList = dbInstance.getSubCategoriesPizza();
        dbInstance.close();

        listview = (ListView) findViewById(R.id.listView1);				
        myAdapter = new MyListAdapterPizza(this,R.layout.line_item_yello, subCatList);
        listview.setAdapter(myAdapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ProductSubCategory eBean = (ProductSubCategory) v.getTag();
                if (eBean != null) {

                    Intent intent=new Intent(PizzaSubMenuActivity.this,ProductsListActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("catId",eBean.getProdCatID());
                    bundle.putString("subCatId",eBean.getSubCatID());
                    bundle.putString("catType","Pizza");
                    bundle.putString("titeDisplay",eBean.getSubCatCode()+" Pizza");
                    bundle.putBoolean("isHalf", isHalf);

                    intent.putExtras(bundle);
                    if(isHalf){
                        startActivity(intent);
                        Log.d("HALF PIZZA", "returning from PizzaSubMenuActivity after set half-pizza");
                        finish();
//                        startActivityForResult(intent, REQUEST_CODE_IS_PIZZA_HALF);
                    }
                    else
                        startActivity(intent);
                }					
            }
        });

        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PizzaSubMenuActivity.this, FavsListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PizzaSubMenuActivity.this, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PizzaSubMenuActivity.this, MyOrderActivity.class);
                startActivity(intent);

            }
        });

        ImageButton imageButtonHalf=(ImageButton)findViewById(R.id.imageButtonHalf);
        if(isHalf)
            imageButtonHalf.setVisibility(View.GONE);
        imageButtonHalf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PizzaSubMenuActivity.this, HalfAndHalf.class);
                startActivity(intent);
            }
        });

        ImageButton imageButtonCreateYourOwn=(ImageButton)findViewById(R.id.imageButtonCreateUown);
        if(isHalf)
            imageButtonCreateYourOwn.setVisibility(View.GONE);
        imageButtonCreateYourOwn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PizzaSubMenuActivity.this, CreateYourOwnCrustActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode != RESULT_OK) {
            return;
        }
        
        switch(requestCode){
            case REQUEST_CODE_IS_PIZZA_HALF:
                // parse data
                // redirect to previous HalfAndHalf activity
                break;
            default:
                break;
        }
    }


    private class MyListAdapterPizza extends ArrayAdapter<ProductSubCategory> {

        private ArrayList<ProductSubCategory> items;

        public MyListAdapterPizza(Context context, int viewResourceId,
                ArrayList<ProductSubCategory> items) {
            super(context, viewResourceId, items);
            this.items = items;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.line_item_yello, null);
            }
            ProductSubCategory event = items.get(position);
            if (event != null) {

                TextView title = (TextView) convertView
                        .findViewById(R.id.itemName);

                title.setText(event.getSubCatCode()+" Pizza");

                convertView.setTag(event);
            }
            return convertView;
        }

    }
    // /////////////////////// END LIST ADAPTER
    @Override
    protected void onResume() {
        super.onResume();
        // //////////////////////////////////////////////////////////////////////////////
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaSubMenuActivity.this);
        dbInstance.open();
        ArrayList<String> orderInfo = dbInstance.getOrderInfo();
        List<DealOrder>dealOrderVos1= dbInstance.getDealOrdersList(true);
        TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
        double tota=0.00;
        int dealCount=0;
        if((dealOrderVos1!=null && dealOrderVos1.size()>0)){
            dealCount=dealOrderVos1.size();
            for (int x=0;x<dealOrderVos1.size();x++){
                tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
            }
        }

        int orderInfoCount= 0;
        double  orderInfoTotal=0.0;
        if ((null != orderInfo && orderInfo.size() == 2) ) {
            orderInfoCount=Integer.parseInt(orderInfo.get(0));
            orderInfoTotal=Double.parseDouble(orderInfo.get(1));
        }
        int numPro=orderInfoCount+dealCount;
        double subTotal=orderInfoTotal+tota;
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        subTotal= Double.valueOf(twoDForm.format(subTotal));
        if(numPro>0){
            itemsPrice.setText(numPro+" Items "+"\n$" +subTotal );
            itemsPrice.setVisibility(View.VISIBLE);
        }

        else{
            itemsPrice.setVisibility(View.INVISIBLE);

        }

        TextView favCount = (TextView) findViewById(R.id.favCount);
        String fvs=dbInstance.getFavCount();
        if (null != fvs && !fvs.equals("0")) {
            favCount.setText(fvs);
            favCount.setVisibility(View.VISIBLE);
        }
        else{
            favCount.setVisibility(View.INVISIBLE);
        }
        dbInstance.close();        

    }

}
