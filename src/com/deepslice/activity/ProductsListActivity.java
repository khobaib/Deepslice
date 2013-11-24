package com.deepslice.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.cache.ImageLoader;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Product;
import com.deepslice.model.ProductSubCategory;
import com.deepslice.model.ServerResponse;
import com.deepslice.model.ToppingPrices;
import com.deepslice.model.ToppingSizes;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;
import com.deepslice.utilities.Utils;

public class ProductsListActivity extends Activity{

    List<Product> allProductsList;
    List<ToppingsAndSauces> toppingsAndSaucesList;

    ProgressDialog pDialog;
    JsonParser jsonParser = new JsonParser();

    ListView listview;
    TextView tvItemsPrice, tvFavCount;

    MyListAdapterProd myAdapter;

    Boolean isHalf;

    String catType;
    String catId;
    String subCatId;

    public ImageLoader imageLoader;
    Product selectedProduct;

    DeepsliceApplication appInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        BugSenseHandler.initAndStartSession(ProductsListActivity.this, "92b170cf");
        setContentView(R.layout.sub_menu_products);

        pDialog = new ProgressDialog(ProductsListActivity.this);
        appInstance = (DeepsliceApplication) getApplication();

        imageLoader=new ImageLoader(this);	
        Bundle b = this.getIntent().getExtras();

        catId = b.getString("catId");
        subCatId = b.getString("subCatId"); 
        catType = b.getString("catType");
        isHalf = b.getBoolean("isHalf", false);
        Log.d("...................dgh..............",subCatId);
        String titeDisplay = b.getString("titeDisplay");

        TextView title = (TextView) findViewById(R.id.headerTextView);

        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);


        if(AppProperties.isNull(titeDisplay)){
            title.setText(catType);	
        }
        else{
            title.setText(titeDisplay);
        }

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(ProductsListActivity.this);
        dbInstance.open();
        if(catType.equals(Constants.PRODUCT_CATEGORY_PIZZA)){
            allProductsList = new ArrayList<Product>();
            new GetDistinctPizzaList().execute();
        }
        else {
            allProductsList = dbInstance.retrieveProducts(catId, subCatId);
        }
        dbInstance.close();

        listview = (ListView) findViewById(R.id.listView1);
        myAdapter = new MyListAdapterProd(this,R.layout.line_item_product, allProductsList);
        listview.setAdapter(myAdapter);


        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Product product = (Product) parent.getItemAtPosition(position);
                if (product != null) {
                    selectedProduct = product;

                    if(catType.equals(Constants.PRODUCT_CATEGORY_PIZZA)){

                        /*
                         * This if-block is just for 2nd half of HnH pizza
                         */
                        if(isHalf && AppProperties.isFirstPizzaChosen){                                                           
                            // if 2nd-half, then selectedProduct will be changed because crust is already fixed
                            ProductSubCategory halfCrust = appInstance.getHalfCrust();
                            DeepsliceDatabase dbInstance = new DeepsliceDatabase(ProductsListActivity.this);
                            dbInstance.open();
                            ProductSubCategory subCat = dbInstance.retrievePizzaCrustId(selectedProduct.getProdCatID(), selectedProduct.getSubCatID1(), halfCrust.getSubCatCode()); 
                            selectedProduct = dbInstance.retrieveProductFromSubCrust(selectedProduct.getProdCatID(),
                                    selectedProduct.getSubCatID1(), subCat.getSubCatID(), selectedProduct.getProdCode());
                            dbInstance.close();
                            if(selectedProduct.getProdID() == null){
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductsListActivity.this);
                                alertDialog.setTitle("Deepslice");
                                alertDialog.setMessage(halfCrust.getSubCatCode() + " crust is not available for pizza" + product.getProdDesc());
                                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    } 
                                }); 
                                alertDialog.create().show(); 
                            }
                            else{
                                updateTopingSaucesData(selectedProduct.getProdID());
                            }
                        }
                        else{
                            updateTopingSaucesData(selectedProduct.getProdID());
                        }
                    }
                    else{

                        Intent i=new Intent(ProductsListActivity.this, AddToOrderActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("itemName", product.getDisplayName());
                        bundle.putSerializable("selected_product", product);
                        bundle.putString("catType", catType);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                }
            }
        });

        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ProductsListActivity.this,FavoriteListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ProductsListActivity.this, MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ProductsListActivity.this, MyOrderActivity.class);
                startActivity(intent);

            }
        });	
    }
    
    
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        BugSenseHandler.startSession(this);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        BugSenseHandler.closeSession(this);
    }
        
    

    private class MyListAdapterProd extends ArrayAdapter<Product> {

        private List<Product> items;

        public MyListAdapterProd(Context context, int viewResourceId, List<Product> items) {
            super(context, viewResourceId, items);
            this.items = items;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.line_item_product, null);
            }
            Product product = items.get(position);
            if (product != null) {

                TextView title = (TextView) convertView.findViewById(R.id.textView1);
                TextView tvPrice = (TextView) convertView.findViewById(R.id.textView2);
                TextView tvCalories = (TextView) convertView.findViewById(R.id.textView3);

                title.setText(Html.fromHtml(product.getDisplayName()));

                if(catType.equals(Constants.PRODUCT_CATEGORY_PIZZA) && PizzaMenuActivity.isHalf)
                    tvPrice.setVisibility(View.GONE);
                else{
                    double price = Double.parseDouble(product.getPrice());
                    tvPrice.setText("$" + Constants.twoDForm.format(price));
                }

                if(catType.equals(Constants.PRODUCT_CATEGORY_SIDES))
                    tvCalories.setVisibility(View.INVISIBLE);
                else{
                    if(catType.equals(Constants.PRODUCT_CATEGORY_PIZZA) && PizzaMenuActivity.isHalf){
                        String cal = product.getCaloriesQty().equals("") ? "0.00" : product.getCaloriesQty();
                        double hnhCal = Double.parseDouble(cal)/2.0; 
                        tvCalories.setText(Constants.twoDForm.format(hnhCal)+"kj");
                    }
                    else
                        tvCalories.setText(product.getCaloriesQty()+"kj");
                }


                ImageView icon = (ImageView) convertView.findViewById(R.id.imageView1);
                String imgPath=Constants.IMAGES_LOCATION_PRODUCTS;
                if(AppProperties.isNull(product.getThumbnail())){
                    imgPath=imgPath+"noimage.png";
                }
                else{
                    imgPath=imgPath+product.getThumbnail();
                }
                imageLoader.DisplayImage(imgPath, icon);

                convertView.setTag(product);
            }
            return convertView;
        }

    }
    ////////////////////////// END LIST ADAPTER
    //===========================================================================================
    //===========================================================================================
    //===========================================================================================


    final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {        
        public void run() {            
            goToPizzaDetailsActivity();        
        }    
    };


    protected void updateTopingSaucesData(final String prodId) {

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(ProductsListActivity.this);
        dbInstance.open();
        boolean isToppingsSynced = dbInstance.isProductToppingsExist(prodId);
        dbInstance.close();

        if(isToppingsSynced) {
            goToPizzaDetailsActivity();
        }
        else {
            //            pd = ProgressDialog.show(ProductsListActivity.this, "", "Please wait...", true, false);
            if(!pDialog.isShowing()){
                pDialog.setIndeterminate(true);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            Thread t = new Thread() {            
                public void run() {                

                    try {
                        GetPizzaToppingAndSauces(prodId);

                    } catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    mHandler.post(mUpdateResults);            
                }
            };        
            t.start();
        }

    }


    private void GetPizzaToppingAndSauces(String prodId){

        String url = Constants.ROOT_URL + "GetPizzaToppingsAndSauces.aspx?prodID=" + prodId;
        long dataRetrieveStartTime = System.currentTimeMillis();
        ServerResponse response = jsonParser.retrieveGETResponse(url, null, Constants.API_RESPONSE_TYPE_JSON_ARRAY);

        long dataRetrieveEndTime = System.currentTimeMillis();
        Log.d("TIME", "time to retrieve topping-sauce data for prodId " + prodId + " = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


        if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
            JSONObject jsonObj = response.getjObj();
            try {
                JSONObject responseObj = jsonObj.getJSONObject("Response");
                int status = responseObj.getInt("Status");
                JSONArray data = responseObj.getJSONArray("Data");
                JSONObject errors = responseObj.getJSONObject("Errors");

                toppingsAndSaucesList = ToppingsAndSauces.parseToppingsAndSauces(data);

                long productParseEndTime = System.currentTimeMillis();
                Log.d("TIME", "time to parse topping-sauce list of item " + toppingsAndSaucesList.size() + " = " + (productParseEndTime - dataRetrieveEndTime)/1000 + " second");

                long dbInsertionStart = System.currentTimeMillis();
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(ProductsListActivity.this);
                dbInstance.open();
                dbInstance.insertToppingSauces(toppingsAndSaucesList);
                dbInstance.close();
                long dbInsertionEnd = System.currentTimeMillis();
                Log.d("TIME", "time to insert topping-sauce data " + " = " + (dbInsertionEnd - dbInsertionStart)/1000 + " second");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } 
    }


    private void goToPizzaDetailsActivity() { 

        if(pDialog.isShowing())
            pDialog.dismiss();

        Intent i=new Intent(ProductsListActivity.this, NEW_PizzaDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isHalf", isHalf);
        bundle.putSerializable("selectedProduct", selectedProduct);
        i.putExtras(bundle);
        startActivity(i);
        if(isHalf){
            Log.d("HALF PIZZA", "returning from ProductListActivity after set half-pizza");
            finish();
        }
    }	


    //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        super.onActivityResult(requestCode, resultCode, data);
    //
    //        if (resultCode != RESULT_OK) {
    //            return;
    //        }
    //
    //        switch(requestCode){
    //            case REQUEST_CODE_IS_PIZZA_HALF:
    //                // parse data
    //                Log.d("HALF PIZZA", "returning from ProductListActivity after set half-pizza");
    //                Intent resultData = new Intent();
    //                setResult(Activity.RESULT_OK, resultData);
    //                finish();
    //                break;
    //            default:
    //                break;
    //        }
    //    }




    @Override
    protected void onResume() {
        super.onResume();

        List<String> orderInfo = Utils.OrderInfo(ProductsListActivity.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);

        Log.d(">>>>>>>>>", "item count = " + itemCount);
        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }


        String favCount = Utils.FavCount(ProductsListActivity.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }

    }



    public class GetDistinctPizzaList extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Please wait...");
            if(!pDialog.isShowing())
                pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "GetDistinctPizzas.aspx";
            long dataRetrieveStartTime = System.currentTimeMillis();
            ServerResponse response = jsonParser.retrieveGETResponse(url, null, Constants.API_RESPONSE_TYPE_JSON_ARRAY);

            long dataRetrieveEndTime = System.currentTimeMillis();
            Log.d("TIME", "time to retrieve distinct pizza from cloud = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    allProductsList = Product.parseDistinctPizza(data, subCatId);

                    long productParseEndTime = System.currentTimeMillis();
                    Log.d("TIME", "time to parse distinct pizza = " + (productParseEndTime - dataRetrieveEndTime)/1000 + " second");

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
            if(pDialog.isShowing())
                pDialog.dismiss();
            if(result){
                updateResults();
            }
        }
    }  


    private void updateResults() {

        if(allProductsList.size()>0){

            myAdapter = new MyListAdapterProd(this,R.layout.line_item_product, allProductsList);
            myAdapter.notifyDataSetChanged();
            listview.setAdapter(myAdapter);
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductsListActivity.this);
            alertDialog.setTitle("info");
            alertDialog.setMessage("Try again later");
            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    ProductsListActivity.this.finish();
                    return;
                } });
            alertDialog.create().show();
        }
    }
}
