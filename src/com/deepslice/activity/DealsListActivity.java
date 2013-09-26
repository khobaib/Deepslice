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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.adapter.DealListAdapter;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Coupon;
import com.deepslice.model.DealOrder;
import com.deepslice.model.ServerResponse;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.Utils;

public class DealsListActivity extends Activity {

    private static final String TAG = DealsListActivity.class.getSimpleName();

    ProgressDialog pDialog;
    JsonParser jsonParser = new JsonParser();

    List<Coupon> couponsList;

    ListView lvCpuponList;
    TextView tvItemsPrice, tvFavCount;
    
    DealListAdapter dealListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(DealsListActivity.this, "92b170cf");
        setContentView(R.layout.deals_list);

        pDialog = new ProgressDialog(DealsListActivity.this);

        lvCpuponList = (ListView) findViewById(R.id.listView1);
        
        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);

        new GetDealsCoupon().execute();

        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealsListActivity.this,FavoriteListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealsListActivity.this,MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealsListActivity.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });	

        lvCpuponList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Coupon selectedCoupon = (Coupon) parent.getItemAtPosition(position);
                removeUnfinishedDeals();        
                Intent i=new Intent(DealsListActivity.this, DealsGroupListActivity.class);

                Bundle bundle=new Bundle();
                bundle.putSerializable("selected_coupon", selectedCoupon);                
                i.putExtras(bundle);

                startActivity(i);
            }
        });
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        BugSenseHandler.closeSession(DealsListActivity.this);
    }

    private void removeUnfinishedDeals(){

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsListActivity.this);
        dbInstance.open();
        dbInstance.deleteUnfinishedDealOrder();
        dbInstance.close();
    }


    public class GetDealsCoupon extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Please wait...");
            if(!pDialog.isShowing())
                pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "GetCoupons.aspx?CouponCode=0&Filter=Deals";
            long dataRetrieveStartTime = System.currentTimeMillis();
            ServerResponse response = jsonParser.retrieveGETResponse(url, null);

            long dataRetrieveEndTime = System.currentTimeMillis();
            Log.d("TIME", "time to retrieve coupons from cloud = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    couponsList = Coupon.parseCoupons(data);

                    long productParseEndTime = System.currentTimeMillis();
                    Log.d("TIME", "time to parse coupons = " + (productParseEndTime - dataRetrieveEndTime)/1000 + " second");

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
//                DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsListActivity.this);
//                dbInstance.open();
//                for(Coupon coupon : couponsList){
//                    Log.d(TAG, "coupon code = " + coupon.getCouponCode());
//                    dbInstance.insertDeals(coupon);
//                }
//                dbInstance.close();

                dealListAdapter = new DealListAdapter(DealsListActivity.this, couponsList);
                lvCpuponList.setAdapter(dealListAdapter);
            }
            else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DealsListActivity.this);
                alertDialog.setTitle("info");
                alertDialog.setMessage("Try again later");
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        DealsListActivity.this.finish();
                        return;
                    } }); 
                alertDialog.create().show();
            }
        }
    }  


    @Override
    protected void onResume() {
        super.onResume();

        List<String> orderInfo = Utils.OrderInfo(DealsListActivity.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
        
        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }

        
        String favCount = Utils.FavCount(DealsListActivity.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }
    }
}
