package com.deepslice.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Product;
import com.deepslice.model.ProductCategory;
import com.deepslice.model.ProductSubCategory;
import com.deepslice.model.ServerResponse;
import com.deepslice.model.ToppingPrices;
import com.deepslice.model.ToppingSizes;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.Utils;

public class MainMenuActivity extends Activity {

    ProgressDialog pDialog;
    JsonParser jsonParser = new JsonParser();

    List<ProductCategory> categoryList;
    List<ProductSubCategory> subcategoryList;
    List<Product> productList;
    List<ToppingPrices> toppingsPriceList;
    List<ToppingSizes> toppingsSizeList;

    long dataRetrieveStartTime, dataRetrieveEndTime;

    TextView tvItemsPrice, tvFavCount;
    
    final Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        pDialog = ProgressDialog.show(MainMenuActivity.this, "", "Please wait...", true, false);

        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MainMenuActivity.this);
        dbInstance.open();
        boolean isCatSynced = dbInstance.isProductCategoriesExist();
        dbInstance.close();

        if(isCatSynced) {
            if(pDialog.isShowing())
                pDialog.dismiss();
        }
        else {
            new GetProductCategories().execute();
        }

        ImageView ivPizza=(ImageView)findViewById(R.id.imageView1);
        ImageView ivDrinks=(ImageView)findViewById(R.id.imageView2);
        ImageView ivSides=(ImageView)findViewById(R.id.imageView3);
        ImageView ivPasta=(ImageView)findViewById(R.id.imageView4);
        ImageButton ivDeals=(ImageButton)findViewById(R.id.imageView5);

        ivPizza.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainMenuActivity.this, PizzaMenuActivity.class);
                intent.putExtra("isHalf", false);
                //                Bundle bundle=new Bundle();
                //                bundle.putString("catType","Pizza");
                //                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        
        ivDrinks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainMenuActivity.this,DrinksSubMenuActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("catType","Drinks");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ivSides.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainMenuActivity.this,SubMenuActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("catType","Sides");
                intent.putExtras(bundle);
                startActivity(intent);
            }


        });
        ivPasta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String pastaId = getProdCatId("Pasta");
                Intent intent = new Intent(MainMenuActivity.this,ProductsListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("catId", pastaId);
                bundle.putString("subCatId", "0");
                bundle.putString("catType", "Pasta");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        ivDeals.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                String pastaId=getProdCatId("Deals");
                Intent intent=new Intent(MainMenuActivity.this, DealsListActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("catId",pastaId);
//                bundle.putString("subCatId","0");
//                bundle.putString("catType","Deals");
//                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainMenuActivity.this,FavsListActivity.class);
                startActivity(intent);

            }
        });


        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainMenuActivity.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });


    }


    public class GetProductCategories extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            dataRetrieveStartTime = System.currentTimeMillis();
            String url = Constants.ROOT_URL + "GetProductCategory.aspx?ProdCategoryID=0";
            ServerResponse response = jsonParser.retrieveGETResponse(url, null);

            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    categoryList = ProductCategory.parseProductCategories(data);

                    DeepsliceDatabase dbInstance = new DeepsliceDatabase(MainMenuActivity.this);
                    dbInstance.open();
                    dbInstance.insertProdCatList(categoryList);
                    dbInstance.close();

                    System.out.println("Got product catetgories: " + categoryList.size());

                    return true;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                new GetProductSubcategories().execute();
            }
        }
    }


    public class GetProductSubcategories extends AsyncTask<Void, Void, Boolean>{


        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "GetProductSubCategory.aspx?ProdCategoryID=0&ProdSubCategoryID=0";
            ServerResponse response = jsonParser.retrieveGETResponse(url, null);

            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    subcategoryList = ProductSubCategory.parseProductSubcategories(data);

                    DeepsliceDatabase dbInstance = new DeepsliceDatabase(MainMenuActivity.this);
                    dbInstance.open();
                    dbInstance.insertSubCatList(subcategoryList);
                    dbInstance.close();

                    System.out.println("Got product sub-catetgories: " + subcategoryList.size());

                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                new GetAllProducts().execute();
            }
        }
    }

    
    
    public class GetAllProducts extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Please wait...");
            if(!pDialog.isShowing())
                pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "GetAllProducts.aspx";
            ServerResponse response = jsonParser.retrieveGETResponse(url, null);

            dataRetrieveEndTime = System.currentTimeMillis();
            Log.d("TIME", "time to retrieve cloud data + parse cat+subcat = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    productList = Product.parseAllProducts(data);

                    long productParseEndTime = System.currentTimeMillis();
                    Log.d("TIME", "time to parse product = " + (productParseEndTime - dataRetrieveEndTime)/1000 + " second");

                    insertProductIntoDb();

                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result){                
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(MainMenuActivity.this);
                dbInstance.open();
                boolean isToppingsSynced = dbInstance.isToppingsDataExist();
                dbInstance.close();

                if(isToppingsSynced){
                    if(pDialog.isShowing())
                        pDialog.dismiss();
                }
                else{
                    Thread t = new Thread() {            
                        public void run() {                
                            try {
                                GetPizzaToppingsSizes();
                                GetPizzaToppingsPrices();

                            } catch (Exception ex){
                                System.out.println(ex.getMessage());
                            }
                            mHandler.post(dismissProgressBar);            
                        }
                    };        
                    t.start();
                }
            }
            else{
                if(pDialog.isShowing())
                    pDialog.dismiss();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainMenuActivity.this);
                alertDialog.setTitle("Deepslice");
                alertDialog.setMessage("Failed to retrieve data. Try again later");
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        MainMenuActivity.this.finish();
                        return;
                    } });
                alertDialog.create().show();
            }
        }
    }
    
    final Runnable dismissProgressBar = new Runnable() {        
        public void run() {            
            if(pDialog.isShowing())
                pDialog.dismiss();        
        }    
    };
        


    private void GetPizzaToppingsSizes(){

        String url = Constants.ROOT_URL + "GetToppingSizes.aspx";
        long dataRetrieveStartTime = System.currentTimeMillis();
        ServerResponse response = jsonParser.retrieveGETResponse(url, null);

        long dataRetrieveEndTime = System.currentTimeMillis();
        Log.d("TIME", "time to retrieve topping SIZE data = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


        if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
            JSONObject jsonObj = response.getjObj();
            try {
                JSONObject responseObj = jsonObj.getJSONObject("Response");
                int status = responseObj.getInt("Status");
                JSONArray data = responseObj.getJSONArray("Data");
                JSONObject errors = responseObj.getJSONObject("Errors");

                toppingsSizeList = ToppingSizes.parseToppingsSizes(data);

                long productParseEndTime = System.currentTimeMillis();
                Log.d("TIME", "time to parse topping Size list of item " + toppingsSizeList.size() + " = " + (productParseEndTime - dataRetrieveEndTime)/1000 + " second");
                long dbInsertionStart = System.currentTimeMillis();
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(MainMenuActivity.this);
                dbInstance.open();
                dbInstance.insertToppingSizes(toppingsSizeList);
                dbInstance.close();
                long dbInsertionEnd = System.currentTimeMillis();
                Log.d("TIME", "time to insert topping-size data " + " = " + (dbInsertionEnd - dbInsertionStart)/1000 + " second");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    } 


    private void GetPizzaToppingsPrices(){
        String url = Constants.ROOT_URL + "GetToppingPrices.aspx";
        long dataRetrieveStartTime = System.currentTimeMillis();
        ServerResponse response = jsonParser.retrieveGETResponse(url, null);

        long dataRetrieveEndTime = System.currentTimeMillis();
        Log.d("TIME", "time to retrieve topping PRICE data = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


        if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
            JSONObject jsonObj = response.getjObj();
            try {
                JSONObject responseObj = jsonObj.getJSONObject("Response");
                int status = responseObj.getInt("Status");
                JSONArray data = responseObj.getJSONArray("Data");
                JSONObject errors = responseObj.getJSONObject("Errors");

                toppingsPriceList = ToppingPrices.parseToppingsPriceList(data);

                long productParseEndTime = System.currentTimeMillis();
                Log.d("TIME", "time to parse topping PRICE list of item " + toppingsPriceList.size() + " = " + (productParseEndTime - dataRetrieveEndTime)/1000 + " second");
                long dbInsertionStart = System.currentTimeMillis();
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(MainMenuActivity.this);
                dbInstance.open();
                dbInstance.insertToppingPrices(toppingsPriceList);
                dbInstance.close();
                long dbInsertionEnd = System.currentTimeMillis();
                Log.d("TIME", "time to insert topping-price data " + " = " + (dbInsertionEnd - dbInsertionStart)/1000 + " second");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    } 


    private void insertProductIntoDb(){

        long productDBInsertstartTime = System.currentTimeMillis();
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MainMenuActivity.this);
        dbInstance.open();
        dbInstance.insertAllProducts(productList);
        dbInstance.close();

        long productDBInsertEndTime = System.currentTimeMillis();
        Log.d("TIME", "time to dbInsert " + productList.size() + " product is = " + (productDBInsertEndTime - productDBInsertstartTime)/1000 + " second");



    }


    private String getProdCatId(String abbr) {
        String pCatId="0";

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MainMenuActivity.this);
        dbInstance.open();
        pCatId=dbInstance.getCatIdFromCatCode(abbr);
        dbInstance.close();

        return pCatId;
    }


    @Override
    protected void onResume() {
        super.onResume();
        List<String> orderInfo = Utils.OrderInfo(MainMenuActivity.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);

        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }


        String favCount = Utils.FavCount(MainMenuActivity.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }
    }


}
