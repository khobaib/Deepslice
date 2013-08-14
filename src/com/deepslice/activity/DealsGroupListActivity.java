package com.deepslice.activity;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deepslice.adapter.DealGroupListAdapter;
import com.deepslice.cache.ImageLoader;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Coupon;
import com.deepslice.model.CouponDetails;
import com.deepslice.model.CouponGroup;
import com.deepslice.model.DealOrder;
import com.deepslice.model.Product;
import com.deepslice.model.ServerResponse;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Constants;

public class DealsGroupListActivity extends Activity{

    private static final String TAG = DealsGroupListActivity.class.getSimpleName();

    List<CouponGroup> couponGroupList;
    List<DealOrder> dealOrderList;

    ProgressDialog pDialog;
    JsonParser jsonParser = new JsonParser();
    ImageLoader imageLoader;

    ListView lvDealGroup;
    TextView DealPrice;

    DealGroupListAdapter dealGroupListAdapter;

    String selectedCouponID, selectedCouponDesc;
    Coupon selectedCoupon;
    double unfinishedDealPrice;

    List<String> couponGroupIds;                       // CouponGroupID list from GetCouponGroups api 


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deals_group_list);

        pDialog = new ProgressDialog(DealsGroupListActivity.this);

        Bundle b = this.getIntent().getExtras();
        selectedCoupon = (Coupon)b.getSerializable("selected_coupon");

        selectedCouponID = selectedCoupon.getCouponID();
        selectedCouponDesc = selectedCoupon.getCouponDesc();

        TextView title = (TextView) findViewById(R.id.headerTextView);
        title.setText(selectedCouponDesc);

        couponGroupIds = new ArrayList<String>();
        couponGroupList = new ArrayList<CouponGroup>();

        DealPrice = (TextView)findViewById(R.id.textViewPrice);
        lvDealGroup = (ListView) findViewById(R.id.listView1);  

        new GetCouponGroups().execute();


        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealsGroupListActivity.this,FavsListActivity.class);
                startActivity(intent);

            }
        });


        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealsGroupListActivity.this,MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealsGroupListActivity.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });


        // NEED TO UNDERSTAND THIS PART
        //comment = add new image for GET A DEAL
        RelativeLayout buttonGetADeal=(RelativeLayout)findViewById(R.id.getDeal);
        buttonGetADeal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsGroupListActivity.this);
                dbInstance.open();
                dbInstance.finalizedDealOrder();
                dbInstance.close();
                
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DealsGroupListActivity.this);
                alertDialog.setTitle("Deepslice");
                alertDialog.setMessage("Deal is added to Cart Successfully");
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    } 
                }); 
                alertDialog.create().show();                  
            }
        });
    }


    public class GetCouponGroups extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Please wait...");
            if(!pDialog.isShowing())
                pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "GetCouponGroups.aspx?CouponID=" + selectedCouponID;
            long dataRetrieveStartTime = System.currentTimeMillis();
            ServerResponse response = jsonParser.retrieveGETResponse(url, null);

            long dataRetrieveEndTime = System.currentTimeMillis();
            Log.d("TIME", "time to retrieve coupon-groups from cloud = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    couponGroupList = CouponGroup.parseCouponGroups(data);

                    long productParseEndTime = System.currentTimeMillis();
                    Log.d("TIME", "time to parse coupongroup = " + (productParseEndTime - dataRetrieveEndTime)/1000 + " second");

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
                for(int i=0 ; i < couponGroupList.size(); i++){
                    couponGroupIds.add(couponGroupList.get(i).getCouponGroupID());
                }
                new GetCouponDetails().execute();
            }
            else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DealsGroupListActivity.this);
                alertDialog.setTitle("info");
                alertDialog.setMessage("Try again later");
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        DealsGroupListActivity.this.finish();
                        return;
                    } }); 
                alertDialog.create().show();
            }
        }
    }  



    public class GetCouponDetails extends AsyncTask<Void, Void, Boolean>{

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

                    // prepare initial dealOrder for every couponGroup & save it to the DB
                    dealOrderList = new ArrayList<DealOrder>();
                    unfinishedDealPrice = 0;

                    for(int couponGrpIndex = 0; couponGrpIndex < couponGroupIds.size(); couponGrpIndex++){
                        String couponGrpId = couponGroupIds.get(couponGrpIndex);
                        for(int couponDetailsIndex = 0; couponDetailsIndex < allCouponDetailsList.size(); couponDetailsIndex++){
                            if(allCouponDetailsList.get(couponDetailsIndex).getCouponGroupID().equals(couponGrpId)){
                                String prodId = allCouponDetailsList.get(couponDetailsIndex).getProdID();
                                String discountedPrice = allCouponDetailsList.get(couponDetailsIndex).getDiscountedPrice();
                                String qty = allCouponDetailsList.get(couponDetailsIndex).getQty();
                                DealOrder thisDealOrder = setDealOrder(couponGrpId, prodId, discountedPrice, qty);
                                dealOrderList.add(thisDealOrder);
                                break;
                            }
                        }
                    }

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
                updateDealsGroupList();
            }
            else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DealsGroupListActivity.this);
                alertDialog.setTitle("info");
                alertDialog.setMessage("Deal items can't be retrieved.");
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        DealsGroupListActivity.this.finish();
                        return;
                    } }); 
                alertDialog.create().show();
            }
        }
    }  

    private DealOrder setDealOrder(String couponGroupId, String prodId, String DiscountedPrice, String qty){

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsGroupListActivity.this);
        dbInstance.open();
        Product selectedProduct = dbInstance.getProductById(prodId);
        dbInstance.close();

        DealOrder dealOrder = new DealOrder();
        dealOrder.setCouponID(selectedCoupon.getCouponID());
        dealOrder.setCouponTypeID(selectedCoupon.getCouponTypeID());
        dealOrder.setCouponCode(selectedCoupon.getCouponCode());
        dealOrder.setCouponGroupID(couponGroupId);
        dealOrder.setDiscountedPrice(DiscountedPrice);
        dealOrder.setProdID(selectedProduct.getProdID());
        dealOrder.setQuantity(qty);
        dealOrder.setDisplayName(selectedProduct.getDisplayName());
        dealOrder.setImage(selectedProduct.getThumbnail());
        dealOrder.setUpdate("0");               // 0 for temporary save

        addDeal(dealOrder);
        unfinishedDealPrice += (Double.parseDouble(DiscountedPrice) * Double.parseDouble(qty));

        return dealOrder;
    }


    public void addDeal(DealOrder dealOrder){

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsGroupListActivity.this);
        dbInstance.open();
        if(dbInstance.isDealGroupAlreadySelected(dealOrder.getCouponID(), dealOrder.getCouponGroupID())){
            Log.d(TAG, "YES");
            boolean b = dbInstance.deleteAlreadySelectedDealGroup(dealOrder.getCouponID(), dealOrder.getCouponGroupID());
            Log.d(TAG, "delete already selected deal? = " + b);
        }
        dbInstance.insertDealOrder(dealOrder);
        dbInstance.close();
    }



    private void updateDealsGroupList() {
        if(dealOrderList != null && dealOrderList.size() > 0){
            dealGroupListAdapter = new DealGroupListAdapter(this, dealOrderList, selectedCoupon);
            lvDealGroup.setAdapter(dealGroupListAdapter);
        }
        else{
            lvDealGroup.setAdapter(null);
        }
        DealPrice.setText("$"+Double.valueOf(Constants.twoDForm.format(unfinishedDealPrice)));
    } 


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { // Back key pressed
            finish();
            DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsGroupListActivity.this);
            dbInstance.open();
            dbInstance.deleteUnfinishedDealOrder();
            dbInstance.close();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        doResumeWork();
    }

    private void doResumeWork() {

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsGroupListActivity.this);
        dbInstance.open();
        ArrayList<String> orderInfo = dbInstance.getOrderInfo();
        List<DealOrder> finishedDealOrderList= dbInstance.getDealOrdersList(true);

        unfinishedDealPrice = 0;

        List<DealOrder> unfinishedDealOrderList = dbInstance.getDealOrdersList(false);
        dealOrderList = new ArrayList<DealOrder>();
        for(int couponGrpIndex = 0; couponGrpIndex < couponGroupIds.size(); couponGrpIndex++){
            String couponGrpId = couponGroupIds.get(couponGrpIndex);
            for(int unfinishedDealIndex = 0; unfinishedDealIndex < unfinishedDealOrderList.size(); unfinishedDealIndex++){
                if(unfinishedDealOrderList.get(unfinishedDealIndex).getCouponGroupID().equals(couponGrpId)){
                    dealOrderList.add(unfinishedDealOrderList.get(unfinishedDealIndex));
                    Log.d(TAG, "couponGrpId = " + couponGrpId + " & imageUrl = " + unfinishedDealOrderList.get(unfinishedDealIndex).getImage());
                    unfinishedDealPrice += (Double.parseDouble(unfinishedDealOrderList.get(unfinishedDealIndex).getDiscountedPrice())
                            * Double.parseDouble(unfinishedDealOrderList.get(unfinishedDealIndex).getQuantity()));
                    break;
                }
            }
        }

        updateDealsGroupList();

        TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
        double tota=0.00;
        int dealCount=0;
        if((finishedDealOrderList!=null && finishedDealOrderList.size()>0)){
            dealCount=finishedDealOrderList.size();
            for (int x=0;x<finishedDealOrderList.size();x++){
                tota+=(Double.parseDouble(finishedDealOrderList.get(x).getDiscountedPrice())*(Integer.parseInt(finishedDealOrderList.get(x).getQuantity())));
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

        subTotal= Double.valueOf(Constants.twoDForm.format(subTotal));
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


