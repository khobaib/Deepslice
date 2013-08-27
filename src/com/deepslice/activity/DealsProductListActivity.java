package com.deepslice.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deepslice.adapter.DealProductListAdapter;
import com.deepslice.database.DealsDbManager;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Coupon;
import com.deepslice.model.CouponDetails;
import com.deepslice.model.DealOrder;
import com.deepslice.model.Product;
import com.deepslice.model.ServerResponse;
import com.deepslice.model.ToppingPrices;
import com.deepslice.model.ToppingSizes;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;
import com.deepslice.utilities.Utils;


public class DealsProductListActivity extends Activity {

    private static final String TAG = DealsDbManager.class.getSimpleName();

    String productCatId, productName;
    int currentPosition;
    String selectedCouponID;
    String selectedCouponGroupID;
    Coupon selectedCoupon;
    int Qty;
    //    boolean syncedPrices = false;
    //    boolean syncedToppings = false;

    DealOrder dealOrder;
    Product selectedProduct;

    List<ToppingPrices> toppingsPriceList;
    List<ToppingSizes> toppingsSizeList;
    List<ToppingsAndSauces> toppingsAndSaucesList;    
    List<CouponDetails> selectedCouponGrpcouponDetailsList;
    List<Product> pList;
    List<DealOrder> dealOrderList;

    DealProductListAdapter dealProductListAdapter;

    ProgressDialog pDialog;
    JsonParser jsonParser = new JsonParser();
    DeepsliceApplication appInstance;

    ListView lvProductList;
    TextView tvTitle, tvSubtitle, tvItemsPrice, tvFavCount;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deals_product_list);

        appInstance=(DeepsliceApplication)getApplication();  
        pDialog = new ProgressDialog(DealsProductListActivity.this);

        Bundle bundle=this.getIntent().getExtras();
        selectedCouponGroupID = bundle.getString("coupon_group_id");
        selectedCoupon = (Coupon)bundle.getSerializable("selected_coupon");
        selectedCouponID = selectedCoupon.getCouponID();
        Qty = Integer.parseInt(bundle.getString("qty"));


        selectedCouponGrpcouponDetailsList = new ArrayList<CouponDetails>();
        pList = new ArrayList<Product>();
        dealOrderList=new ArrayList<DealOrder>();

        tvTitle = (TextView)findViewById(R.id.tv_title);
        tvSubtitle = (TextView)findViewById(R.id.tv_subtitle);
        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);

        lvProductList=(ListView)findViewById(R.id.listView1);
        lvProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                selectedProduct = (Product) parent.getItemAtPosition(position);

                DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsProductListActivity.this);
                dbInstance.open();
                String prodType=dbInstance.getCatCodeFromCatId(selectedProduct.getProdCatID());
                dbInstance.close();

                if(prodType.equalsIgnoreCase("Pizza")){
                    appInstance.setCouponDetails(selectedCouponGrpcouponDetailsList.get(currentPosition));
                    gotoActivity(true);
                } else {
                    gotoActivity(false);
                }
            }
        });

        new GetCouponDetails().execute();



        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealsProductListActivity.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });
        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealsProductListActivity.this,FavsListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealsProductListActivity.this,MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

    }


    private void updateDealsProductList() {

        if (pList != null && pList.size() > 0) {
            productCatId = pList.get(0).getProdCatID();         // needed to retrieve category code & display in title
            dealProductListAdapter = new DealProductListAdapter(DealsProductListActivity.this, pList);
            lvProductList.setAdapter(dealProductListAdapter);

        }
        else{
            lvProductList.setAdapter(null);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DealsProductListActivity.this);
            alertDialog.setTitle("Deepslice");
            alertDialog.setMessage("No Product under this section.");
            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                } 
            }); 
            alertDialog.create().show();                
        }
    }    


    public class GetCouponDetails extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Please wait...");
            if(!pDialog.isShowing())
                pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "GetCouponDetail.aspx?CouponID=" + selectedCouponID;
            long dataRetrieveStartTime = System.currentTimeMillis();
            ServerResponse response = jsonParser.retrieveGETResponse(url, null);

            long dataRetrieveEndTime = System.currentTimeMillis();
            Log.d("TIME", "time to retrieve coupon-details from cloud = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    List<CouponDetails> allCouponDetailsList = CouponDetails.parseCouponDetails(data);

                    long productParseEndTime = System.currentTimeMillis();
                    Log.d("TIME", "time to parse coupon-details = " + (productParseEndTime - dataRetrieveEndTime)/1000 + " second");

                    DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsProductListActivity.this);
                    dbInstance.open();
                    for (int i=0;i<allCouponDetailsList.size();i++){
                        CouponDetails thisCouponDetails = allCouponDetailsList.get(i);
                        if (thisCouponDetails.getCouponGroupID().equalsIgnoreCase(selectedCouponGroupID)){
                            pList.add(dbInstance.getProductById(thisCouponDetails.getProdID()));
                            selectedCouponGrpcouponDetailsList.add(thisCouponDetails);
                        }

                    }
                    dbInstance.close();
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
                updateDealsProductList();
                new GetCategoryDisplayName().execute();
            }
            else{
                if(pDialog.isShowing())
                    pDialog.dismiss();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DealsProductListActivity.this);
                alertDialog.setTitle("info");
                alertDialog.setMessage("Try again later");
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        DealsProductListActivity.this.finish();
                        return;
                    } }); 
                alertDialog.create().show();
            }
        }
    }  


    public class GetCategoryDisplayName extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "/GetProductCategory.aspx?ProdCategoryID=" + productCatId;
            long dataRetrieveStartTime = System.currentTimeMillis();
            ServerResponse response = jsonParser.retrieveGETResponse(url, null);

            long dataRetrieveEndTime = System.currentTimeMillis();
            Log.d("TIME", "time to retrieve category from cloud = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    List<CouponDetails> allCouponDetailsList = CouponDetails.parseCouponDetails(data);
                    JSONObject productCat = data.getJSONObject(0);
                    if(productCat != null){
                        productName=productCat.getString("ProdCatCode");
                        return true;
                    }                    
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
                if(productName.equalsIgnoreCase("pizza")){
                    tvTitle.setText("Select Value "+ productName);
                } else {                    
                    tvTitle.setText("Select "+ productName);
                }
                tvSubtitle.setText(productName);

            }

        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        List<String> orderInfo = Utils.OrderInfo(DealsProductListActivity.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);

        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }


        String favCount = Utils.FavCount(DealsProductListActivity.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }
    }



    private void gotoActivity(boolean isPizza) {
        dealOrder = new DealOrder();
        dealOrder.setCouponID(selectedCoupon.getCouponID());
        dealOrder.setCouponTypeID(selectedCoupon.getCouponTypeID());
        dealOrder.setCouponCode(selectedCoupon.getCouponCode());
        dealOrder.setCouponGroupID(selectedCouponGroupID);
        dealOrder.setDiscountedPrice(selectedCouponGrpcouponDetailsList.get(currentPosition).getDiscountedPrice());
        dealOrder.setProdID(selectedProduct.getProdID());
        dealOrder.setQuantity(Qty+"");
        dealOrder.setDisplayName(selectedProduct.getDisplayName());
        dealOrder.setImage(selectedProduct.getThumbnail());
        dealOrder.setUpdate("0");               // 0 for temporary save
        if(isPizza){
            updateTopingSaucesData(dealOrder.getProdID());
            Log.d("????????", "Deal's product id in updateTopingSaucesData = " + dealOrder.getProdID());
        }else {
            addDeal(dealOrder);
        }
    }


    /*
     * Added by Khobaib 2013-08-01 1639
     */

    final Handler mHandler1 = new Handler();
    final Runnable mUpdateResults1 = new Runnable() {
        public void run() {
            runPizzaDetailsActivity();
        }
    };

    private void runPizzaDetailsActivity() {        
        if(pDialog.isShowing())
            pDialog.dismiss();

        appInstance.setDealOrder(dealOrder);
        Intent intent=new Intent(DealsProductListActivity.this,PizzaDetailsActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("selectedProduct", selectedProduct);
        bundle.putString("prdID", selectedProduct.getProdID());             // it isnt necessary
        bundle.putBoolean("isDeal",true);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    protected void updateTopingSaucesData(final String prodId) {


        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsProductListActivity.this);
        dbInstance.open();
        boolean isToppingsSynced = dbInstance.isProductToppingsExist(prodId);
        dbInstance.close();

        if(isToppingsSynced) {
            runPizzaDetailsActivity();
        }
        else {
            pDialog = ProgressDialog.show(DealsProductListActivity.this, "", "Please wait...", true, false);

            Thread t = new Thread() {            
                public void run() {                

                    try {
                        GetPizzaToppingAndSauces(prodId);

                    } catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    mHandler1.post(mUpdateResults1);            
                }
            };        
            t.start();
        }

    }



    private void GetPizzaToppingAndSauces(String prodId){

        String url = Constants.ROOT_URL + "GetPizzaToppingsAndSauces.aspx?prodID=" + prodId;
        long dataRetrieveStartTime = System.currentTimeMillis();
        ServerResponse response = jsonParser.retrieveGETResponse(url, null);

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
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsProductListActivity.this);
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


    public void addDeal(DealOrder dealOrder){

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsProductListActivity.this);
        dbInstance.open();
        if(dbInstance.isDealGroupAlreadySelected(dealOrder.getCouponID(), dealOrder.getCouponGroupID())){
            Log.d(TAG, "YES");
            boolean b = dbInstance.deleteAlreadySelectedDealGroup(dealOrder.getCouponID(), dealOrder.getCouponGroupID());
            Log.d(TAG, "delete already selected deal? = " + b);
        }
        dbInstance.insertDealOrder(dealOrder);
        dbInstance.close();
        finish();
    }

}