package com.deepslice.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Products;
import com.deepslice.model.DealOrder;
import com.deepslice.model.ProductCategory;
import com.deepslice.model.ProductSubCategory;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DrinksSubMenuActivity extends Activity{

    ArrayList<ProductCategory> productCatList;
    ArrayList<ProductSubCategory> subCatList;
    ArrayList<Products> allProductsList;

    ListView listview;
    MyListAdapterDrinks myAdapter;

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

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DrinksSubMenuActivity.this);
        dbInstance.open();
        subCatList=dbInstance.getSubCategoriesDrinks();
        dbInstance.close();
        
//        AppDao dao=null;
//        try {
//            dao=AppDao.getSingleton(getApplicationContext());
//            dao.openConnection();
//
//            subCatList=dao.getSubCategoriesDrinks();
//
//        } catch (Exception ex)
//        {
//            System.out.println(ex.getMessage());
//        }finally{
//            if(null!=dao)
//                dao.closeConnection();
//        }

        listview = (ListView) findViewById(R.id.listView1);				
        myAdapter = new MyListAdapterDrinks(this,R.layout.line_item_yello, subCatList);
        listview.setAdapter(myAdapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position,
                    long id) {
                ProductSubCategory eBean = (ProductSubCategory) v.getTag();
                if (eBean != null) {

                    Intent intent=new Intent(DrinksSubMenuActivity.this,ProductsListActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("catId",eBean.getProdCatID());
                    bundle.putString("subCatId",eBean.getSubCatID());
                    bundle.putString("catType","Drinks");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DrinksSubMenuActivity.this,FavsListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DrinksSubMenuActivity.this,MenuActivity.class);
                startActivity(intent);

            }
        });
        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DrinksSubMenuActivity.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });	
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////

    private class MyListAdapterDrinks extends ArrayAdapter<ProductSubCategory> {

        private ArrayList<ProductSubCategory> items;

        public MyListAdapterDrinks(Context context, int viewResourceId,
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

                title.setText(event.getSubCatCode()+" Drink");

                convertView.setTag(event);
            }
            return convertView;
        }

    }
    // /////////////////////// END LIST ADAPTER
    @Override
    protected void onResume() {
        // //////////////////////////////////////////////////////////////////////////////

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DrinksSubMenuActivity.this);
        dbInstance.open();
        ArrayList<String> orderInfo = dbInstance.getOrderInfo();
        ArrayList<DealOrder>dealOrderVos1= dbInstance.getDealOrdersList();
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


        //        AppDao dao = null;
        //        try {
        //            dao = AppDao.getSingleton(getApplicationContext());
        //            dao.openConnection();
        //            ArrayList<String> orderInfo = dao.getOrderInfo();
        //            ArrayList<DealOrder>dealOrderVos1= dao.getDealOrdersList();
        //            TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
        //            double tota=0.00;
        //            int dealCount=0;
        //            if((dealOrderVos1!=null && dealOrderVos1.size()>0)){
        //                dealCount=dealOrderVos1.size();
        //                for (int x=0;x<dealOrderVos1.size();x++){
        //                    tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
        //                }
        //            }
        //
        //            int orderInfoCount= 0;
        //            double  orderInfoTotal=0.0;
        //            if ((null != orderInfo && orderInfo.size() == 2) ) {
        //                orderInfoCount=Integer.parseInt(orderInfo.get(0));
        //                orderInfoTotal=Double.parseDouble(orderInfo.get(1));
        //            }
        //            int numPro=orderInfoCount+dealCount;
        //            double subTotal=orderInfoTotal+tota;
        //            DecimalFormat twoDForm = new DecimalFormat("#.##");
        //            subTotal= Double.valueOf(twoDForm.format(subTotal));
        //            if(numPro>0){
        //                itemsPrice.setText(numPro+" Items "+"\n$" +subTotal );
        //                itemsPrice.setVisibility(View.VISIBLE);
        //            }
        //
        //            else{
        //                itemsPrice.setVisibility(View.INVISIBLE);
        //
        //            }
        //
        //            TextView favCount = (TextView) findViewById(R.id.favCount);
        //            String fvs=dao.getFavCount();
        //            if (null != fvs && !fvs.equals("0")) {
        //                favCount.setText(fvs);
        //                favCount.setVisibility(View.VISIBLE);
        //            }
        //            else{
        //                favCount.setVisibility(View.INVISIBLE);
        //            }
        //
        //
        //
        //        } catch (Exception ex) {
        //            System.out.println(ex.getMessage());
        //        } finally {
        //            if (null != dao)
        //                dao.closeConnection();
        //        }
        // ///////////////////////////////////////////////////////////////////////

        super.onResume();
    }
}
