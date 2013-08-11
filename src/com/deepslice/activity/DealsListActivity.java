package com.deepslice.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.cache.ImageLoader;
import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Products;
import com.deepslice.model.CouponGroups;
import com.deepslice.model.Coupons;
import com.deepslice.model.DealOrder;
import com.deepslice.model.Order;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DealsListActivity extends Activity{

    ArrayList<CouponGroups> allProductsList;

    ListView listview;
    MyListAdapterFav myAdapter;

    ProgressDialog pd;
    String couponId,couponDesc,productCatId="0",prodCatCode;
    public ImageLoader imageLoader;
    int numOfDeals=0;
    Products selectedBean;
    Coupons couponsVo;
    ArrayList<String> couponsId;
    ArrayList<ArrayList<String>> arrayListProductName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_list);
        imageLoader=new ImageLoader(this);	
        Bundle b = this.getIntent().getExtras();
        couponId=b.getString("couponId");
        couponDesc=b.getString("couponDesc");
        couponsVo=(Coupons)b.getSerializable("couponsVo");
        TextView title = (TextView) findViewById(R.id.headerTextView);
        title.setText(couponDesc);
        couponsId=new ArrayList<String>();
        arrayListProductName=new ArrayList<ArrayList<String>>();
        getCouponDetails(couponId);

        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealsListActivity.this,FavsListActivity.class);
                startActivity(intent);

            }
        });

        allProductsList=new ArrayList<CouponGroups>();
        listview = (ListView) findViewById(R.id.listView1);				


        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position,
                    long id) {
                CouponGroups eBean = (CouponGroups) v.getTag();
                if (eBean != null) {

                    //rukshan add
                    TextView title=(TextView)v.findViewById(R.id.textView1);
                    Intent i=new Intent(DealsListActivity.this,DealGroup.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("couponId",couponId);
                    bundle.putString("couponGroupID",eBean.getCouponGroupID());
                    bundle.putString("Qty",eBean.getQty());
                    bundle.putSerializable("couponsVo", couponsVo);
                    bundle.putStringArrayList("couponsID",couponsId);
                    bundle.putString("title",title.getText().toString());
                    // Toast.makeText(DealsListActivity.this,"cou"+couponId+"gr"+eBean.getCouponGroupID(),Toast.LENGTH_SHORT).show();
                    i.putExtras(bundle);
                    startActivity(i);


                    //comment by rukshan
                    //					String prodType=AppProperties.getCatName(DealsListActivity.this, eBean.getProdCatID());
                    //
                    //					AppDao dao=null;
                    //					try {
                    //						dao=AppDao.getSingleton(getApplicationContext());
                    //
                    //						dao.openConnection();
                    //
                    //							dao.insertOrder(getOrderBean(eBean,prodType));
                    //
                    //							Toast.makeText(DealsListActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
                    //
                    //					} catch (Exception ex)
                    //					{
                    //						System.out.println(ex.getMessage());
                    //					}finally{
                    //						if(null!=dao)
                    //							dao.closeConnection();
                    //					}
                    //					doResumeWork();
                }

            }
        });


        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealsListActivity.this,MenuActivity.class);
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

//        Button buttonCancel=(Button)findViewById(R.id.buttonCancel);
//        buttonCancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsListActivity.this);
//                dbInstance.open();
//                if(dbInstance.deleteDealOrderRec(couponId)){
//                    Toast.makeText(DealsListActivity.this, "successfully canceled", Toast.LENGTH_LONG).show();
//                    doResumeWork();
//                }else {
//                    Toast.makeText(DealsListActivity.this, "no data", Toast.LENGTH_LONG).show();
//                }
//                dbInstance.close();
//            }
//        });
        
        //comment = add new image for GET A DEAL
        RelativeLayout buttonGetADeal=(RelativeLayout)findViewById(R.id.getDeal);
        buttonGetADeal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for (int x=0;x<couponsId.size();x++){
                    if(AppSharedPreference.getBoolean(DealsListActivity.this,couponsId.get(x))){
                        count++;
                    }
                }
                if (count==couponsId.size()){

                    DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsListActivity.this);
                    dbInstance.open();
                    dbInstance.updateDealOrder();
                    Toast.makeText(DealsListActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();

                    ArrayList<DealOrder>dealOrderVos1= dbInstance.getDealOrdersList();
                    int i=dealOrderVos1.size();
                    double tota=0.00;
                    for (int x=0;x<dealOrderVos1.size();x++){
                        tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
                    }
                    TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
                    if (null != dealOrderVos1 && dealOrderVos1.size() > 0) {
                        itemsPrice.setText(i+" Items "+"\n$" + tota);
                        itemsPrice.setVisibility(View.VISIBLE);
                    }
                    else{
                        itemsPrice.setVisibility(View.INVISIBLE);

                    }
                    AppSharedPreference.clearDealCoupon(DealsListActivity.this,couponsId);
                    dbInstance.close();
                    finish();

                }else if(AppSharedPreference.getBoolean(DealsListActivity.this,couponId)){

                    DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsListActivity.this);
                    dbInstance.open();
                    dbInstance.updateDealOrder();
                    Toast.makeText(DealsListActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();

                    ArrayList<DealOrder>dealOrderVos1= dbInstance.getDealOrdersList();
                    int i=dealOrderVos1.size();
                    double tota=0.00;
                    for (int x=0;x<dealOrderVos1.size();x++){
                        tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
                    }
                    TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
                    if (null != dealOrderVos1 && dealOrderVos1.size() > 0) {
                        itemsPrice.setText(i+" Items "+"\n$" + tota);
                        itemsPrice.setVisibility(View.VISIBLE);
                    }
                    else{
                        itemsPrice.setVisibility(View.INVISIBLE);

                    }
                    AppSharedPreference.putBoolean(DealsListActivity.this,couponsId+"",false);
                    dbInstance.close();
                    finish();
                }else {

                    Toast.makeText(DealsListActivity.this, "Select product from deal groups", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void gotoActivity(String prodCatCode) {
        if(prodCatCode.equalsIgnoreCase("Pizza")){
            Intent i=new Intent(DealsListActivity.this, PizzaDetailsActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("selectedProduct",selectedBean);
            i.putExtras(bundle);
            startActivityForResult(i, 112233);
        }else if (prodCatCode.equalsIgnoreCase("Drinks"))
        {

            Intent i=new Intent(DealsListActivity.this, FavAddActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("itemName",selectedBean.getDisplayName());
            bundle.putSerializable("prodBean",selectedBean);
            //bundle.putString("catType",catType);
            i.putExtras(bundle);
            startActivityForResult(i, 112233);
        }
    }

    private Order getOrderBean(Products prodBean, String catType) {

        Order f = new Order();
        f.setProdCatID(prodBean.getProdCatID());
        f.setSubCatID1(prodBean.getSubCatID1());
        f.setSubCatID2(prodBean.getSubCatID2());
        f.setProdID(prodBean.getProdID());
        f.setProdCode(prodBean.getProdCode());
        f.setDisplayName(prodBean.getDisplayName());
        f.setProdAbbr(prodBean.getProdAbbr());
        f.setProdDesc(prodBean.getProdDesc());
        f.setIsAddDeliveryAmount(prodBean.getIsAddDeliveryAmount());
        f.setDisplaySequence(prodBean.getDisplaySequence());
        f.setCaloriesQty(prodBean.getCaloriesQty());

        double finalPrice=Double.parseDouble(prodBean.getPrice());
        finalPrice=AppProperties.roundTwoDecimals(finalPrice);
        f.setPrice(String.valueOf(finalPrice));

        f.setThumbnail(prodBean.getThumbnail());
        f.setFullImage(prodBean.getFullImage());

        f.setQuantity(String.valueOf(1));

        f.setCrust("");
        f.setSauce("");
        f.setToppings("");

        f.setProdCatName(catType);

        return f;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////

    private class MyListAdapterFav extends ArrayAdapter<CouponGroups> {

        private ArrayList<CouponGroups> items;

        public MyListAdapterFav(Context context, int viewResourceId,
                ArrayList<CouponGroups> items) {
            super(context, viewResourceId, items);
            this.items = items;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.line_item_deal, null);
            }
            CouponGroups event = items.get(position);
            if (event != null) {

                TextView title = (TextView) convertView.findViewById(R.id.textView1);
                ImageView imageView=(ImageView)convertView.findViewById(R.id.imageViewDealDone);
                ImageView icon = (ImageView) convertView.findViewById(R.id.imageView1);
                String imgPath=Constants.IMAGES_LOCATION;
                imageLoader.DisplayImage(imgPath+"noimage.png", icon);
                if(arrayListProductName.size()>0){
                    for (int i=0;i<arrayListProductName.size();i++){

                        if(event.getCouponGroupID().equalsIgnoreCase(arrayListProductName.get(i).get(2))){
                            title.setText(arrayListProductName.get(i).get(0));
                            imgPath=Constants.IMAGES_LOCATION;
                            if(AppProperties.isNull(arrayListProductName.get(i).get(1))){
                                imgPath=imgPath+"noimage.png";
                            }
                            else{
                                imgPath=imgPath+arrayListProductName.get(i).get(1);
                            }
                            imageLoader.DisplayImage(imgPath, icon);
                            imageView.setVisibility(View.VISIBLE);
                            break;
                        }else {
                            imgPath=Constants.IMAGES_LOCATION;
                            icon = (ImageView) convertView.findViewById(R.id.imageView1);
                            imageLoader.DisplayImage(imgPath+"noimage.png", icon);
                            title.setText("select product #"+(position+1));
                            imageView.setVisibility(View.INVISIBLE);
                        }
                    }
                }  else {
                    title.setText("select product #"+(position+1));
                    imageView.setVisibility(View.INVISIBLE);
                }

                //				price.setText("$"+AppProperties.getRoundTwoDecimalString(event.getPrice()));
                //				calories.setText(event.getCaloriesQty()+"kj");




                convertView.setTag(event);
            }
            return convertView;
        }

    }
    ////////////////////////// END LIST ADAPTER
    //===========================================================================================
    //===========================================================================================
    //===========================================================================================


    @Override
    protected void onResume() {
        doResumeWork();

        super.onResume();
    }

    private void doResumeWork() {
        // //////////////////////////////////////////////////////////////////////////////

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealsListActivity.this);
        dbInstance.open();
        ArrayList<String> orderInfo = dbInstance.getOrderInfo();
        ArrayList<DealOrder>dealOrderVos1= dbInstance.getDealOrdersList();
        arrayListProductName=new ArrayList<ArrayList<String>>();
        double total=0;
        if(couponsId.size()>0){
            for (int i=0;i<couponsId.size();i++){
                if(dbInstance.getDealData(couponsId.get(i),couponId).size()>0){
                    arrayListProductName.add(dbInstance.getDealData(couponsId.get(i),couponId));
                }
                Log.d("................",arrayListProductName.toString());
            }
        }
        if(arrayListProductName.size()>0){
            for(int i=0;i<arrayListProductName.size();i++){
                total+=(Double.parseDouble(arrayListProductName.get(i).get(3))) * (Double.parseDouble(arrayListProductName.get(i).get(4)) );
            }
        }
        TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
        TextView textView=(TextView)findViewById(R.id.textViewPrice);
        double tota=0.00;
        int dealCount=0;
        if((dealOrderVos1!=null && dealOrderVos1.size()>0)){
            dealCount=dealOrderVos1.size();
            for (int x=0;x<dealOrderVos1.size();x++){
                tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
            }
        }

        textView.setText("$"+total);
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

        listview.invalidate();
        myAdapter = new MyListAdapterFav(this,R.layout.line_item_deal, allProductsList);
        listview.setAdapter(myAdapter);
        dbInstance.close();

    }

    final Handler mHandlerDetails = new Handler();
    final Runnable mUpdateResultsDetails = new Runnable() {        
        public void run() {            
            updateResultsInUiDetails();        
        }    
    };
    String serverResponseDetails;
    String delLocError="";
    protected void getCouponDetails(final String couponId) {        
        pd = ProgressDialog.show(DealsListActivity.this, "", "Please wait...", true, false);

        Thread t = new Thread() {            
            public void run() {                

                try {
                    //calling the auth service
                    fetchCouponDetails(couponId);
                } catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
                mHandlerDetails.post(mUpdateResultsDetails);            
            }


        };        
        t.start();    	
    }

    public void fetchCouponDetails(final String couponId) {

        delLocError="";

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        //comment = new api implementation to get coupon groups for coupon id
        HttpGet httpGet = new HttpGet(Constants.ROOT_URL+"GetCouponGroups.aspx?CouponID="+couponId);
        Log.d("req..deal....", Constants.ROOT_URL+"/GetCouponGroups.aspx?CouponID="+couponId);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                System.out.println("Failed to download file");
            }


            serverResponseDetails = builder.toString();

            //////////////////////////////////////////////////////////
            String errorMessage="";
            if (statusCode==200){
                GsonBuilder gsonb = new GsonBuilder();
                Gson gson = gsonb.create();
                JSONArray results = new JSONArray(serverResponseDetails);
                JSONObject respOuter = results.getJSONObject(0);
                JSONObject resp = respOuter.getJSONObject("Response");
                String status = resp.getString("Status");
                JSONArray resultsArray =null;
                Object data= resp.get("Data");
                boolean dataExists=false;
                if(data instanceof JSONArray)
                {
                    resultsArray =(JSONArray)data;
                    dataExists=true;
                }

                JSONObject errors = resp.getJSONObject("Errors");

                boolean hasError=errors.has("Message");
                if(hasError)
                {
                    errorMessage=errors.getString("Message");
                    System.out.println("Error:"+errorMessage);
                }

                ArrayList<CouponGroups> couponDetails = new ArrayList<CouponGroups>();

                if(dataExists==true)
                {
                    CouponGroups aBean;
                    for(int i=0; i<resultsArray.length(); i++){
                        JSONObject jsResult = resultsArray.getJSONObject(i);
                        if(jsResult!=null){
                            String jsonString = jsResult.toString();
                            aBean=new CouponGroups();
                            aBean=gson.fromJson(jsonString, CouponGroups.class);
                            //                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                            couponDetails.add(aBean);
                        }
                    }
                }
                allProductsList=couponDetails;
                System.out.println(couponDetails.size());
            }else {
                allProductsList=new ArrayList<CouponGroups>();
            }
            //////////////////////////////////////////////////////////
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            delLocError=e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            delLocError=e.getMessage();
        }catch (Exception e) {
            e.printStackTrace();
            delLocError=e.getMessage();
        }	
    }

    private void updateResultsInUiDetails() { 
        pd.dismiss();

        try{
            if (allProductsList.size()>0) {
                numOfDeals=allProductsList.size();
                listview = (ListView) findViewById(R.id.listView1);
                myAdapter = new MyListAdapterFav(this,R.layout.line_item_deal, allProductsList);
                listview.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
                AppSharedPreference.putInteger(DealsListActivity.this, "numDeals", allProductsList.size());
            }
            for(int i=0;i<allProductsList.size();i++){
                AppSharedPreference.putBoolean(DealsListActivity.this, allProductsList.get(i).getCouponGroupID(), false);
                couponsId.add(allProductsList.get(i).getCouponGroupID());
            }


        } catch (Exception e){
            e.printStackTrace();
        }

    }

    //rukshan add
    final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            updateResultsInUi();
        }
    };


    private void updateResultsInUi() {
        pd.dismiss();

        try{


            //////////////////////////////////////////////////////////
            String errorMessage="";
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            JSONArray results = new JSONArray(serverResponse);
            JSONObject respOuter = results.getJSONObject(0);
            JSONObject resp = respOuter.getJSONObject("Response");
            //		      String status = resp.getString("Status");
            JSONArray resultsArray =null;
            Object data= resp.get("Data");
            boolean dataExists=false;
            if(data instanceof JSONArray)
            {
                resultsArray =(JSONArray)data;
                dataExists=true;
            }

            JSONObject errors = resp.getJSONObject("Errors");

            boolean hasError=errors.has("Message");
            if(hasError)
            {
                errorMessage=errors.getString("Message");
                System.out.println("Error:"+errorMessage);
            }



            if(dataExists==true)
            {
                Coupons aBean;
                for(int i=0; i<resultsArray.length(); i++){
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if(jsResult!=null){
                        prodCatCode=jsResult.getString("ProdCatCode");
                    }
                }
            }

            gotoActivity(prodCatCode);

        } catch (Exception e){
            e.printStackTrace();
            AlertDialog alertDialog = new AlertDialog.Builder(DealsListActivity.this).create();
            alertDialog.setTitle("Login");
            alertDialog.setMessage(e.getMessage());
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                } });
            alertDialog.show();
        }

    }
    String serverResponse;
    protected void getCatagoryName(String catID) {
        pd = ProgressDialog.show(DealsListActivity.this, "", "Please wait...", true, false);

        Thread t = new Thread() {
            public void run() {

                try {
                    //calling the auth service
                    startLongRunningOperation();
                } catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
                mHandler.post(mUpdateResults);
            }


        };
        t.start();
    }
    public void startLongRunningOperation() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL+"/GetCouponDetail.aspx?CouponID"+productCatId);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                System.out.println("Failed to download file");
            }


            serverResponse = builder.toString();


            //////////////////////////////////////////////////////////
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            delLocError=e.getMessage();
            e.printStackTrace();
        }
    }
}
