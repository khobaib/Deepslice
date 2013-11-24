package com.deepslice.activity;

import java.util.ArrayList;
import java.util.Collections;
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

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.adapter.DealGroupListAdapter;
import com.deepslice.cache.ImageLoader;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Coupon;
import com.deepslice.model.CouponDetails;
import com.deepslice.model.CouponGroup;
import com.deepslice.model.NewDealsOrder;
import com.deepslice.model.NewDealsOrderDetails;
import com.deepslice.model.Product;
import com.deepslice.model.ServerResponse;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.CouponGroupIdComparator;
import com.deepslice.utilities.Utils;

public class DealsGroupListActivity extends Activity{

    private static final String TAG = DealsGroupListActivity.class.getSimpleName();

    List<CouponGroup> couponGroupList;
    List<NewDealsOrderDetails> dealOrderDetailsList;

    ProgressDialog pDialog;
    JsonParser jsonParser = new JsonParser();
    ImageLoader imageLoader;

    ListView lvDealGroup;
    TextView DealPrice;
    TextView tvItemsPrice, tvFavCount;

    DealGroupListAdapter dealGroupListAdapter;

    String selectedCouponID, selectedCouponDesc;
    Coupon selectedCoupon;
    double unfinishedDealPrice;

    List<String> couponGroupIds;                       // CouponGroupID list from GetCouponGroups api 
    List<Integer> sequenceIds;
    public static List<Boolean> isDealItemCustomized;
    long dealOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(this, "92b170cf");
        
        setContentView(R.layout.deals_group_list);

        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);

        pDialog = new ProgressDialog(DealsGroupListActivity.this);

        Bundle b = this.getIntent().getExtras();
        selectedCoupon = (Coupon)b.getSerializable("selected_coupon");

        selectedCouponID = selectedCoupon.getCouponID();
        selectedCouponDesc = selectedCoupon.getCouponDesc();

        TextView title = (TextView) findViewById(R.id.headerTextView);
        title.setText(selectedCouponDesc);

        isDealItemCustomized = new ArrayList<Boolean>();
        sequenceIds = new ArrayList<Integer>();
        couponGroupIds = new ArrayList<String>();
        couponGroupList = new ArrayList<CouponGroup>();

        DealPrice = (TextView)findViewById(R.id.textViewPrice);
        lvDealGroup = (ListView) findViewById(R.id.listView1);  

        new GetCouponGroups().execute();


        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealsGroupListActivity.this,FavoriteListActivity.class);
                startActivity(intent);

            }
        });


        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealsGroupListActivity.this,MainMenuActivity.class);
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


        RelativeLayout buttonGetADeal=(RelativeLayout)findViewById(R.id.getDeal);
        buttonGetADeal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isAllCustomized = true;
                for(Boolean isCustomized : isDealItemCustomized){
                    if(isCustomized.equals(false)){
                        isAllCustomized = false;
                        break;
                    }                       
                }

                if(isAllCustomized == true){
                    DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsGroupListActivity.this);
                    dbInstance.open();
                    dbInstance.updateDealTotalPrice((int) dealOrderId);
                    dbInstance.finalizedDealOrder((int) dealOrderId);
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
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DealsGroupListActivity.this);
                    alertDialog.setTitle("Deepslice");
                    alertDialog.setMessage("Please customize all the deal items.");
                    alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        } 
                    }); 
                    alertDialog.create().show();  
                }
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
            ServerResponse response = jsonParser.retrieveGETResponse(url, null, Constants.API_RESPONSE_TYPE_JSON_ARRAY);

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
                    Log.e("????", "couponGroupList size = " + couponGroupList.size());
                    Collections.sort(couponGroupList, new CouponGroupIdComparator());

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
                    sequenceIds.add(couponGroupList.get(i).getSequenceNo());
                    couponGroupIds.add(couponGroupList.get(i).getCouponGroupID());
                    isDealItemCustomized.add(false);                    // initially all false
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
            ServerResponse response = jsonParser.retrieveGETResponse(url, null, Constants.API_RESPONSE_TYPE_JSON_ARRAY);

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

                    dealOrderDetailsList = new ArrayList<NewDealsOrderDetails>();
                    unfinishedDealPrice = 0;

                    // here we have to add the deal in NEW_DealsOrderDbManager table first
                    dealOrderId = setNewDealOrder();
                    Log.d(TAG, "dealOrder ID = " + dealOrderId);

//                    for(int couponGrpIndex = 0; couponGrpIndex < couponGroupIds.size(); couponGrpIndex++){
//                        String couponGrpId = couponGroupIds.get(couponGrpIndex);
                    for(int seqIdIndex = 0; seqIdIndex < sequenceIds.size(); seqIdIndex++){
                        int seqId = sequenceIds.get(seqIdIndex);
                        String couponGrpId = couponGroupIds.get(seqIdIndex);
                        
                        for(int couponDetailsIndex = 0; couponDetailsIndex < allCouponDetailsList.size(); couponDetailsIndex++){
                            if(allCouponDetailsList.get(couponDetailsIndex).getCouponGroupID().equals(couponGrpId)){
                                String prodId = allCouponDetailsList.get(couponDetailsIndex).getProdID();
                                String discountedPrice = allCouponDetailsList.get(couponDetailsIndex).getDiscountedPrice();
                                String qty = allCouponDetailsList.get(couponDetailsIndex).getQty();
                                NewDealsOrderDetails thisDealOrderDetails = setDealOrderDetails(couponGrpId, prodId, discountedPrice, qty, seqId);
                                dealOrderDetailsList.add(thisDealOrderDetails);
                                break;
                            }
                        }
                    } 

                    //                    for(int couponGrpIndex = 0; couponGrpIndex < couponGroupIds.size(); couponGrpIndex++){
                    //                        String couponGrpId = couponGroupIds.get(couponGrpIndex);
                    //                        for(int couponDetailsIndex = 0; couponDetailsIndex < allCouponDetailsList.size(); couponDetailsIndex++){
                    //                            if(allCouponDetailsList.get(couponDetailsIndex).getCouponGroupID().equals(couponGrpId)){
                    //                                String prodId = allCouponDetailsList.get(couponDetailsIndex).getProdID();
                    //                                String discountedPrice = allCouponDetailsList.get(couponDetailsIndex).getDiscountedPrice();
                    //                                String qty = allCouponDetailsList.get(couponDetailsIndex).getQty();
                    //                                NewDealsOrder thisDealOrder = setDealOrder(couponGrpId, prodId, discountedPrice, qty);
                    //                                dealOrderList.add(thisDealOrder);
                    //                                break;
                    //                            }
                    //                        }
                    //                    }

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

    private long setNewDealOrder() {
        NewDealsOrder dealOrder = new NewDealsOrder();
        dealOrder.setCouponID(selectedCoupon.getCouponID());
        dealOrder.setDealPrice(selectedCoupon.getAmount());
        dealOrder.setTotalPrice(selectedCoupon.getAmount());
        dealOrder.setQuantity(String.valueOf(1));                   // WHY???
        dealOrder.setDealItemCount(couponGroupList.size());
        dealOrder.setCouponCode(selectedCoupon.getCouponCode());
        dealOrder.setCouponDesc(selectedCoupon.getCouponDesc());
        dealOrder.setCouponPic(selectedCoupon.getPic());
        dealOrder.setIsCompleted(false);

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsGroupListActivity.this);
        dbInstance.open();
        long dealOrderId = dbInstance.insertDealOrder(dealOrder);
        dbInstance.close();

        return dealOrderId;
    }

    private NewDealsOrderDetails setDealOrderDetails(String couponGrpId, String prodId, String discountedPrice, String qty, int sequence) {

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsGroupListActivity.this);
        dbInstance.open();
        Product selectedProduct = dbInstance.retrieveProductById(prodId);
        dbInstance.close();


        NewDealsOrderDetails dealOrderDetails = new NewDealsOrderDetails();
        dealOrderDetails.setDealOrderId((int) dealOrderId);
        dealOrderDetails.setCouponGroupId(couponGrpId);
        dealOrderDetails.setProdId(prodId);
        dealOrderDetails.setDiscountedPrice(discountedPrice);
        dealOrderDetails.setDisplayName(selectedProduct.getDisplayName());
        dealOrderDetails.setThumbnail(selectedProduct.getThumbnail());
        dealOrderDetails.setQty(qty);
        dealOrderDetails.setSequence(sequence);


        dbInstance = new DeepsliceDatabase(DealsGroupListActivity.this);
        dbInstance.open();
        if(dbInstance.isDealGroupSeqAlreadySelected((int) dealOrderId, sequence)){
            Log.d(TAG, "YES, this deal group already selected");
            boolean b = dbInstance.deleteAlreadySelectedDealGroupSeq((int) dealOrderId, sequence);
            Log.d(TAG, "delete already selected deal? = " + b);
        }
        long dealOrderDetailsId = dbInstance.insertDealOrderDetails(dealOrderDetails);
        dbInstance.close();

        unfinishedDealPrice += (Double.parseDouble(discountedPrice) * Double.parseDouble(qty));

        return dealOrderDetails;
    }



    //    private NewDealsOrder setDealOrder(String couponGroupId, String prodId, String DiscountedPrice, String qty){
    //
    //        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsGroupListActivity.this);
    //        dbInstance.open();
    //        Product selectedProduct = dbInstance.retrieveProductById(prodId);
    //        dbInstance.close();
    //
    //        NewDealsOrder dealOrder = new DealOrder();
    //        dealOrder.setCouponID(selectedCoupon.getCouponID());
    //        dealOrder.setCouponTypeID(selectedCoupon.getCouponTypeID());
    //        dealOrder.setCouponCode(selectedCoupon.getCouponCode());
    //        dealOrder.setCouponGroupID(couponGroupId);
    //        dealOrder.setDiscountedPrice(DiscountedPrice);
    //        dealOrder.setProdID(selectedProduct.getProdID());
    //        dealOrder.setQuantity(qty);
    //        dealOrder.setDisplayName(selectedProduct.getDisplayName());
    //        dealOrder.setImage(selectedProduct.getThumbnail());
    //        dealOrder.setUpdate("0");               // 0 for temporary save
    //
    //        addDeal(dealOrder);
    //        unfinishedDealPrice += (Double.parseDouble(DiscountedPrice) * Double.parseDouble(qty));
    //
    //        return dealOrder;
    //    }
    //
    //
    //    public void addDeal(DealOrder dealOrder){
    //
    //        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsGroupListActivity.this);
    //        dbInstance.open();
    //        if(dbInstance.isDealGroupAlreadySelected(dealOrder.getCouponID(), dealOrder.getCouponGroupID())){
    //            Log.d(TAG, "YES");
    //            boolean b = dbInstance.deleteAlreadySelectedDealGroup(dealOrder.getCouponID(), dealOrder.getCouponGroupID());
    //            Log.d(TAG, "delete already selected deal? = " + b);
    //        }
    //        dbInstance.insertDealOrder(dealOrder);
    //        dbInstance.close();
    //    }



    private void updateDealsGroupList() {

        // if this deal has any added toppings price for pizza-deals
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsGroupListActivity.this);
        dbInstance.open();
        unfinishedDealPrice = dbInstance.updateDealTotalPrice((int) dealOrderId);
        dbInstance.close();

        if(dealOrderDetailsList != null && dealOrderDetailsList.size() > 0){
            dealGroupListAdapter = new DealGroupListAdapter(this, dealOrderDetailsList, selectedCoupon, dealOrderId);
            lvDealGroup.setAdapter(dealGroupListAdapter);
        }
        else{
            lvDealGroup.setAdapter(null);
        }
        DealPrice.setText("$"+ Constants.twoDForm.format(unfinishedDealPrice));
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
        unfinishedDealPrice = 0;

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsGroupListActivity.this);
        dbInstance.open();
        List<NewDealsOrder> unfinishedDealOrderList = dbInstance.retrieveDealOrderList(false);
        dbInstance.close();

        if(unfinishedDealOrderList.size() > 0){
            dbInstance.open();
            NewDealsOrder unfinishedDealOrder = unfinishedDealOrderList.get(0);             // we know only one unfinishedDealOrder can exist at a time
            dealOrderDetailsList = dbInstance.retrieveDealOrderDetailsList(unfinishedDealOrder.getPrimaryId());
            dbInstance.close();

//            for(int couponGrpIndex = 0; couponGrpIndex < couponGroupIds.size(); couponGrpIndex++){
//                Log.e(">>>>>>>>> new tag", "couponGrpIndex = " + couponGrpIndex);
//                unfinishedDealPrice += (Double.parseDouble(dealOrderDetailsList.get(couponGrpIndex).getDiscountedPrice())
//                        * Double.parseDouble(dealOrderDetailsList.get(couponGrpIndex).getQty()));            
//            }
            
            for(int couponSeqIndex = 0; couponSeqIndex < sequenceIds.size(); couponSeqIndex++){
                Log.e(">>>>>>>>> new tag", "couponSeqIndex = " + couponSeqIndex);
                unfinishedDealPrice += (Double.parseDouble(dealOrderDetailsList.get(couponSeqIndex).getDiscountedPrice())
                        * Double.parseDouble(dealOrderDetailsList.get(couponSeqIndex).getQty()));            
            }
        }


        //        dealOrderList = new ArrayList<NewDealsOrderDetails>();
        //        for(int couponGrpIndex = 0; couponGrpIndex < couponGroupIds.size(); couponGrpIndex++){
        //            String couponGrpId = couponGroupIds.get(couponGrpIndex);
        //            for(int unfinishedDealIndex = 0; unfinishedDealIndex < unfinishedDealOrderList.size(); unfinishedDealIndex++){
        //                if(unfinishedDealOrderList.get(unfinishedDealIndex).getCouponGroupID().equals(couponGrpId)){
        //                    dealOrderList.add(unfinishedDealOrderList.get(unfinishedDealIndex));
        //                    Log.d(TAG, "couponGrpId = " + couponGrpId + " & imageUrl = " + unfinishedDealOrderList.get(unfinishedDealIndex).getImage());
        //                    unfinishedDealPrice += (Double.parseDouble(unfinishedDealOrderList.get(unfinishedDealIndex).getDiscountedPrice())
        //                            * Double.parseDouble(unfinishedDealOrderList.get(unfinishedDealIndex).getQuantity()));
        //                    break;
        //                }
        //            }
        //        }
        updateDealsGroupList();

        List<String> orderInfo = Utils.OrderInfo(DealsGroupListActivity.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);

        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }


        String favCount = Utils.FavCount(DealsGroupListActivity.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }
    }
}


