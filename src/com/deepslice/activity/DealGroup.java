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
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.deepslice.model.AllProducts;
import com.deepslice.model.CouponData;
import com.deepslice.model.Coupons;
import com.deepslice.model.DealOrder;
import com.deepslice.model.ProdAndSubCategory;
import com.deepslice.model.ToppingPrices;
import com.deepslice.model.ToppingSizes;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 5/18/13
 * Time: 10:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class DealGroup extends Activity {
    String couponGroupID,couponID,productCatId="0",prodCatCode="",productName="",productId="",prdId="";
    ProgressDialog pd;
    ArrayList<CouponData> couponData;
    ArrayList<AllProducts> pList;
    ArrayList<DealOrder> dealOrderVos;
    int currentPosition,Qty=0;
    public ImageLoader imageLoader;
    ListView listView;
    MyListAdapter myAdapter;
    AllProducts selectedBean;
    Coupons couponsVo;
    ArrayList<String> couponsId;
    TextView textViewTitle,textViewSub;
    ImageView subTitleBg;
    RelativeLayout relativeLayoutTit;
    boolean syncedPrices =false;
    boolean syncedToppings =false;
    DealOrder dealOrderVo;
    DeepsliceApplication globalObject;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deal_group_data);
         Bundle bundle=this.getIntent().getExtras();

        couponGroupID=bundle.getString("couponGroupID");
        couponID=bundle.getString("couponId");
        couponsVo=(Coupons)bundle.getSerializable("couponsVo");
        couponsId=bundle.getStringArrayList("couponsID");
        Qty=Integer.parseInt(bundle.getString("Qty"));
        couponData = new ArrayList<CouponData>();
         pList = new ArrayList<AllProducts>();
        dealOrderVos=new ArrayList<DealOrder>();
        imageLoader=new ImageLoader(this);
        listView=(ListView)findViewById(R.id.listView1);
        textViewTitle=(TextView)findViewById(R.id.headerTextView);
        textViewSub=(TextView)findViewById(R.id.textViewSub);
        subTitleBg=(ImageView)findViewById(R.id.imageViewListHead);
        relativeLayoutTit=(RelativeLayout)findViewById(R.id.titleLay);
        //textViewTitle.setText(bundle.getString("title"));
        globalObject=(DeepsliceApplication)getApplication();
        getCouponDataForID(couponID);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition=position;
                AllProducts eBean = (AllProducts) view.getTag();
                if (eBean != null) {
                    selectedBean = eBean;
//                    if(!selectedBean.getProdCatID().equalsIgnoreCase(prodCatCode)){
//                        productCatId=selectedBean.getProdCatID();
//                        getCatagoryName(productCatId);
//                    }else {
//                        gotoActivity(prodCatCode);
//                    }
                    String prodType=AppProperties.getCatName(DealGroup.this, eBean.getProdCatID());
                    Log.d("",".........product type............."+prodType);
                    if(prodType.equalsIgnoreCase("Pizza")){
                       //Toast.makeText(DealGroup.this,"pizza"+eBean.getProdID(),Toast.LENGTH_LONG).show();
                        prdId=eBean.getProdID();
                        for (CouponData couponData1:couponData){
                            if(couponData1.getProdID().equalsIgnoreCase(eBean.getProdID())){
                                globalObject.setCouponData(couponData1);
                                break;
                            }
                        }
                        gotoActivity(true);
                       //
                    } else {
                        gotoActivity(false);
                    }
                    //comment by rukshan

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

        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealGroup.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });
        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealGroup.this,FavsListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealGroup.this,MenuActivity.class);
                startActivity(intent);

            }
        });

    }
    final Handler mHandlerDetails = new Handler();
    final Runnable mUpdateResultsDetails = new Runnable() {
        public void run() {
            updateResultsInUiDetails();
        }
    };
    String serverResponseDetails;
    String delLocError="";

    private void getCouponDataForID(String couponID) {
        pd = ProgressDialog.show(DealGroup.this, "", "Please wait...", true, false);

        Thread t = new Thread() {
            public void run() {

                try {
                    //calling the auth service
                    fetchCouponDetails();
                } catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
                mHandlerDetails.post(mUpdateResultsDetails);
            }


        };
        t.start();
    }
    private void updateResultsInUiDetails() {
        pd.dismiss();

        try{
            int i=pList.size();
            if (pList.size()>0) {
                productCatId=pList.get(0).getProdCatID();
                getCatagoryName("");
                listView = (ListView) findViewById(R.id.listView1);
                myAdapter = new MyListAdapter(this,R.layout.deal_single_item, pList,couponData);
                listView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public void fetchCouponDetails() {

        delLocError="";

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
   //comment = new api implementation to get deals for coupon id
        HttpGet httpGet = new HttpGet(Constants.ROOT_URL+"/GetCouponDetail.aspx?CouponID="+couponID);
        Log.d("req......",Constants.ROOT_URL+"/GetCouponDetail.aspx?CouponID="+couponID);
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

                ArrayList<CouponData> couponDetails = new ArrayList<CouponData>();

                if(dataExists==true)
                {
                    CouponData aBean;
                    for(int i=0; i<resultsArray.length(); i++){
                        JSONObject jsResult = resultsArray.getJSONObject(i);
                        if(jsResult!=null){
                           // String jsonString = jsResult.toString();
                            aBean=new CouponData();
                            aBean.setCouponGroupID(jsResult.getString("CouponGroupID"));
                            aBean.setCouponID(jsResult.getString("CouponID"));
                            aBean.setID(jsResult.getString("ID"));
                            aBean.setProdID(jsResult.getString("ProdID"));
                            aBean.setQty(jsResult.getString("Qty"));
                            aBean.setDiscountedPrice(jsResult.getString("DiscountedPrice"));
                            aBean.setIsAddProductWithCoupon(jsResult.getString("IsAddProductWithCoupon"));
                            JSONArray jsonArray=jsResult.getJSONArray("ProdAndSubCatID");
                            ArrayList<ProdAndSubCategory>prodAndSubCategories=new ArrayList<ProdAndSubCategory>();
                            for(int s=0;s<jsonArray.length();s++){
                                JSONObject object=jsonArray.getJSONObject(s);
                                ProdAndSubCategory subCategory=new ProdAndSubCategory();
                                subCategory.setProdID(object.getString("ProdID"));
                                subCategory.setSubCat2Id(object.getString("SubCat2Id"));
                                subCategory.setSubCat2Code(object.getString("subCat2Code"));
                                subCategory.setFullImage(object.getString("FullImage"));
                                subCategory.setThumbnail(object.getString("Thumbnail"));
                                prodAndSubCategories.add(subCategory);
                            }
                            aBean.setProdAndSubCategories(prodAndSubCategories);
                           // aBean=gson.fromJson(jsonString, CouponData.class);
                            //                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                            couponDetails.add(aBean);
                        }
                    }
                    
                    DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealGroup.this);
                    dbInstance.open();
                    for (int i=0;i<couponDetails.size();i++){
                        aBean=new CouponData();
                        aBean=couponDetails.get(i);
                        if (aBean.getCouponGroupID().equalsIgnoreCase(couponGroupID)){
                            pList.add(dbInstance.getProductById(aBean.getProdID()));
                            couponData.add(aBean);
                        }

                    }
                    dbInstance.close();
                    
//                    AppDao dao=null;
//                    try {
//                        dao=AppDao.getSingleton(getApplicationContext());
//                        dao.openConnection();
//                        for (int i=0;i<couponDetails.size();i++){
//                            aBean=new CouponData();
//                            aBean=couponDetails.get(i);
//                            if (aBean.getCouponGroupID().equalsIgnoreCase(couponGroupID)){
//                                pList.add(dao.getProductById(aBean.getProdID()));
//                                couponData.add(aBean);
//                            }
//
//                        }
//
//                    } catch (Exception ex)
//                    {
//                        System.out.println(ex.getMessage());
//                    }finally{
//                        if(null!=dao)
//                            dao.closeConnection();
//                    }


                }

                System.out.println(couponDetails.size());
            }else {


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

    private class MyListAdapter extends ArrayAdapter<AllProducts> {

        private ArrayList<AllProducts> items;
        private ArrayList<CouponData> couponData;

        public MyListAdapter(Context context, int viewResourceId,
                                ArrayList<AllProducts> items,ArrayList<CouponData> couponDatas) {
            super(context, viewResourceId, items);
            this.items = items;
            Log.d("count...................",items.size()+"");
            couponData=couponDatas;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.deal_single_item, null);
            }
            AllProducts event = items.get(position);
            if (event != null) {

                TextView title = (TextView) convertView.findViewById(R.id.textView1);
                TextView calories = (TextView) convertView.findViewById(R.id.textView2);
                ImageView imageViewArr=(ImageView)convertView.findViewById(R.id.imageViewArrow);
//                if (!productName.equalsIgnoreCase("pizza")){
//                    imageViewArr.setVisibility(View.INVISIBLE);
//                }

                title.setText(event.getDisplayName());
                calories.setText(event.getCaloriesQty()+" kj");
//				price.setText("$"+AppProperties.getRoundTwoDecimalString(event.getPrice()));
//				calories.setText(event.getCaloriesQty()+"kj");


				ImageView icon = (ImageView) convertView.findViewById(R.id.imageView1);
				String imgPath=Constants.IMAGES_LOCATION;
				if(AppProperties.isNull(event.getThumbnail())){
					imgPath=imgPath+"noimage.png";
				}
				else{
					imgPath=imgPath+event.getThumbnail();
				}
				imageLoader.DisplayImage(imgPath, icon);

                convertView.setTag(event);
            }
            return convertView;
        }

    }

    @Override
    protected void onResume() {
        // //////////////////////////////////////////////////////////////////////////////
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealGroup.this);
        dbInstance.open();
        ArrayList<String> orderInfo = dbInstance.getOrderInfo();
        ArrayList<DealOrder>dealOrderVos1= dbInstance.getDealOrdersList();
        TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
        double tota=0.00;
        int dealCount=0;
        if((dealOrderVos1!=null && dealOrderVos1.size()>0)){
            dealCount=dealOrderVos1.size();
            for (int x=0;x<dealOrderVos1.size();x++){
                tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice()))*(Integer.parseInt(dealOrderVos1.get(x).getQuantity()));
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
//           // dao.updateDealOrder();
//            ArrayList<String> orderInfo = dao.getOrderInfo();
//            ArrayList<DealOrder>dealOrderVos1= dao.getDealOrdersList();
//            TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
//            double tota=0.00;
//            int dealCount=0;
//            if((dealOrderVos1!=null && dealOrderVos1.size()>0)){
//                dealCount=dealOrderVos1.size();
//                for (int x=0;x<dealOrderVos1.size();x++){
//                    tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice()))*(Integer.parseInt(dealOrderVos1.get(x).getQuantity()));
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
                        //prodCatCode=jsResult.getString("ProdCatCode");
                        productName=jsResult.getString("ProdCatCode");
                        if(productName.equalsIgnoreCase("pizza")){
                            relativeLayoutTit.setVisibility(View.GONE);
                            textViewTitle.setText("Select Value "+productName);
                        } else {
                            textViewSub.setText(productName);
                            textViewTitle.setText("Select "+productName);
                        }

                    }
                }
            }

            //gotoActivity(prodCatCode);

        } catch (Exception e){
            e.printStackTrace();
            AlertDialog alertDialog = new AlertDialog.Builder(DealGroup.this).create();
            alertDialog.setTitle("info");
            alertDialog.setMessage("Try again later");
            Log.d("error",e.getMessage());
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                } });
            alertDialog.show();
        }

    }
    String serverResponse;
    protected void getCatagoryName(String catID) {
        pd = ProgressDialog.show(DealGroup.this, "", "Please wait...", true, false);

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

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL+"/GetProductCategory.aspx?ProdCategoryID="+productCatId);
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
    private void gotoActivity(boolean isPizza) {
//        boolean isAdd= AppSharedPreference.getBoolean(DealGroup.this,couponGroupID);
//        if(isAdd)
//        {
//            AppDao dao=null;
//            try {
//                dao=AppDao.getSingleton(getApplicationContext());
//                dao.openConnection();
//                dao.deleteDealOrderRecByGroupID(couponGroupID);
//                AppSharedPreference.putBoolean(DealGroup.this, couponGroupID, false);
//
//            } catch (Exception ex)
//            {
//                System.out.println(ex.getMessage());
//            }finally{
//                if(null!=dao)
//                    dao.closeConnection();
//            }
//        }
//
        dealOrderVo=new DealOrder();
        dealOrderVo.setCouponID(couponsVo.getCouponID());
        dealOrderVo.setCouponTypeID(couponsVo.getCouponTypeID());
        dealOrderVo.setCouponCode(couponsVo.getCouponCode());
        dealOrderVo.setCouponGroupID(couponGroupID);
        dealOrderVo.setDiscountedPrice(couponData.get(currentPosition).getDiscountedPrice());
        productId=couponData.get(currentPosition).getProdID();
        dealOrderVo.setProdID(couponData.get(currentPosition).getProdID());
        dealOrderVo.setQuantity(Qty+"");
        dealOrderVo.setDisplayName(selectedBean.getDisplayName());
        dealOrderVo.setImage(selectedBean.getThumbnail());
        dealOrderVo.setUpdate("0");
        if(isPizza){
        	updateTopingSaucesData(dealOrderVo.getProdID());
        	Log.d("????????", "Deal's product id in updateTopingSaucesData = " + dealOrderVo.getProdID());
//            globalObject.setDealOrderVo(dealOrderVo);
//            Intent intent=new Intent(DealGroup.this,PizzaDetailsActivity.class);
//            Bundle bundle=new Bundle();
//            bundle.putSerializable("selectedProduct",selectedBean);
//            bundle.putString("prdID",prdId);
//            bundle.putBoolean("isDeal",true);
//            intent.putExtras(bundle);
//            startActivity(intent);
//            finish();
        }else {
            addDeals(dealOrderVo);
        }
    }
    
    
    
    
    /*
     * Added by Khobaib 2013-08-01 1639
     */
    
    final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            updateResultsInUi();
        }
    };
    
    final Handler mHandler1 = new Handler();
    final Runnable mUpdateResults1 = new Runnable() {
        public void run() {
            pd.dismiss();
            updateResults();
        }
    };
    
    
    protected void updateTopingSaucesData(final String prodId) {


//        AppDao dao=null;
//        try {
//            dao=AppDao.getSingleton(getApplicationContext());
//            dao.openConnection();
//
//            syncedPrices=dao.recordExistsToppingPrices();
//            syncedToppings=dao.recordExistsToppings(prodId);
//
//        } catch (Exception ex)
//        {
//            System.out.println(ex.getMessage());
//        }finally{
//            if(null!=dao)
//                dao.closeConnection();
//        }
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealGroup.this);
        dbInstance.open();
        syncedPrices = dbInstance.recordExistsToppingPrices();
        syncedToppings=dbInstance.recordExistsToppings(prodId);
        dbInstance.close();

        if(syncedPrices && syncedToppings)
        {
            updateResults();
        }
        else
        {
            pd = ProgressDialog.show(DealGroup.this, "", "Please wait...", true, false);

            Thread t = new Thread() {
                public void run() {

                    try {

                        if(syncedToppings==false)
                            populateToppingsAndSauces(prodId);

                        if(syncedPrices==false)
                        {
                            populateToppingSizes();
                            populateToppingPrices();
                        }

                    } catch (Exception ex)
                    {
                        System.out.println(ex.getMessage());
                    }
                    mHandler1.post(mUpdateResults1);
                }


            };
            t.start();
        }

    }

    private void updateResults() {
    	
        globalObject.setDealOrderVo(dealOrderVo);
        Intent intent=new Intent(DealGroup.this,PizzaDetailsActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("selectedProduct",selectedBean);
        bundle.putString("prdID",prdId);
        bundle.putBoolean("isDeal",true);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

//        if(productName.equalsIgnoreCase("Pizza")){
//            //AppSharedPreference.putBoolean(DealGroup.this, couponGroupID, true);
//            Intent i=new Intent(DealGroup.this, PizzaDetailsActivity.class);
//            Bundle bundle=new Bundle();
//            bundle.putSerializable("selectedProduct",selectedBean);
//            bundle.putSerializable("dealData",dealOrderVo);
//            bundle.putBoolean("isDeal", true);
//            bundle.putStringArrayList("couponsId", couponsId);
//            bundle.putString("couponGroupID",couponGroupID);
//            bundle.putString("couponID",couponID);
//            bundle.putString("productId",productId);
//            i.putExtras(bundle);
//            startActivityForResult(i, 112233);
//            finish();
//        }else if (productName.equalsIgnoreCase("Drinks"))
//        {
//            //AppSharedPreference.putBoolean(DealGroup.this, couponGroupID, true);
//            Intent i=new Intent(DealGroup.this, FavAddActivity.class);
//            Bundle bundle=new Bundle();
//            bundle.putString("itemName",selectedBean.getDisplayName());
//            bundle.putSerializable("prodBean",selectedBean);
//            bundle.putSerializable("dealData",dealOrderVo);
//            bundle.putBoolean("isDeal",true);
//            bundle.putStringArrayList("couponsId",couponsId);
//            bundle.putString("couponGroupID",couponGroupID);
//            bundle.putString("couponID",couponID);
//            bundle.putString("productId",productId);
//            i.putExtras(bundle);
//            startActivityForResult(i, 112233);
//            finish();
//        }else if(productName.equalsIgnoreCase("Breads")) {
//            Intent i=new Intent(DealGroup.this, FavAddActivity.class);
//            Bundle bundle=new Bundle();
//            bundle.putString("itemName",selectedBean.getDisplayName());
//            bundle.putSerializable("prodBean",selectedBean);
//            bundle.putSerializable("dealData",dealOrderVo);
//            bundle.putBoolean("isDeal",true);
//            bundle.putStringArrayList("couponsId",couponsId);
//            bundle.putString("couponGroupID",couponGroupID);
//            bundle.putString("couponID",couponID);
//            bundle.putString("productId",productId);
//            i.putExtras(bundle);
//            startActivityForResult(i, 112233);
//            finish();
//            //setDeals(dealOrderVo);
//        }

    }
	
//	protected void updateTopingSaucesData(final String prodId) {
//		
//		
//		AppDao dao=null;
//		try {
//			dao=AppDao.getSingleton(getApplicationContext());
//			dao.openConnection();
//			
//			syncedPrices=dao.recordExistsToppingPrices();
//			syncedToppings=dao.recordExistsToppings(prodId);
//	
////			dao.insertOrUpdateList(questionList);
//			
//		} catch (Exception ex)
//		{
//			System.out.println(ex.getMessage());
//		}finally{
//			if(null!=dao)
//				dao.closeConnection();
//		}
//
//	if(syncedPrices && syncedToppings)
//	{
//	updateResultsInUi();
//	}
//	else
//	{
//		pd = ProgressDialog.show(DealGroup.this, "", "Please wait...", true, false);
//
//		Thread t = new Thread() {            
//			public void run() {                
//			
//				try {
//					
//					if(syncedToppings==false)
//						populateToppingsAndSauces(prodId);
//					
//					if(syncedPrices==false)
//					{
//						populateToppingSizes();
//						populateToppingPrices();
//					}
//					
//				} catch (Exception ex)
//				{
//					System.out.println(ex.getMessage());
//				}
//				mHandler.post(mUpdateResults);            
//			}
//
//			
//		};        
//		t.start();
//	}
//		
//	}
	// END
	
    
    
    
    
    
    
    
    
    

    public void addDeals(DealOrder dealOrderVo){
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealGroup.this);
        dbInstance.open();
        AppSharedPreference.putBoolean(DealGroup.this, couponGroupID, true);
        if(dbInstance.isDealProductAvailable(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID())){
            boolean b= dbInstance.deleteDuplicateDealOrderRec(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID());
            dbInstance.resetDealOrder(dealOrderVo.getCouponID());
        }
        dbInstance.insertDealOrder(dealOrderVo);
        if(dbInstance.getDealOrderCount(dealOrderVo.getCouponID())==AppSharedPreference.getInteger(DealGroup.this,"numDeals",0)){
            AppSharedPreference.putBoolean(DealGroup.this,dealOrderVo.getCouponID(),true);
            Toast.makeText(DealGroup.this, "complete your deal by tapping GET A DEAL", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(DealGroup.this, "Select product from deal groups", Toast.LENGTH_LONG).show();
        }
        dbInstance.close();
        finish();
        
        
//        AppDao dao=null;
//        try {
//            dao=AppDao.getSingleton(getApplicationContext());
//            dao.openConnection();
//
//                AppSharedPreference.putBoolean(DealGroup.this, couponGroupID, true);
//                if(dao.isDealProductAvailable(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID())){
//                    boolean b= dao.deleteDuplicateDealOrderRec(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID());
//                    dao.resetDealOrder(dealOrderVo.getCouponID());
//                }
//                dao.insertDealOrder(dealOrderVo);
//                if(dao.getDealOrderCount(dealOrderVo.getCouponID())==AppSharedPreference.getInteger(DealGroup.this,"numDeals",0)){
//                    AppSharedPreference.putBoolean(DealGroup.this,dealOrderVo.getCouponID(),true);
//                    Toast.makeText(DealGroup.this, "complete your deal by tapping GET A DEAL", Toast.LENGTH_LONG).show();
//                }else {
//                    Toast.makeText(DealGroup.this, "Select product from deal groups", Toast.LENGTH_LONG).show();
//                }
//                finish();
//
//        } catch (Exception ex)
//        {
//            System.out.println(ex.getMessage());
//        }finally{
//            if(null!=dao)
//                dao.closeConnection();
//        }
    }

    public void setDeals(DealOrder dealOrderVo){
        AppSharedPreference.putBoolean(DealGroup.this, couponGroupID, true);
        int numDeals=AppSharedPreference.getInteger(DealGroup.this,"",0);
        int count=0;
        for (int x=0;x<couponsId.size();x++){
            if(AppSharedPreference.getBoolean(DealGroup.this,couponsId.get(x))){
                count++;
            }
        }
        if (count==couponsId.size()){
            DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealGroup.this);
            dbInstance.open();
            dbInstance.insertDealOrder(dealOrderVo);
            Toast.makeText(DealGroup.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
            dbInstance.updateDealOrder();
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
            AppSharedPreference.clearDealCoupon(DealGroup.this,couponsId);
            dbInstance.close();
            finish();

            
            
//            AppDao dao=null;
//            try {
//                dao=AppDao.getSingleton(getApplicationContext());
//                dao.openConnection();
//                dao.insertDealOrder(dealOrderVo);
//                Toast.makeText(DealGroup.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
//                dao.updateDealOrder();
//                ArrayList<DealOrder>dealOrderVos1= dao.getDealOrdersList();
//                int i=dealOrderVos1.size();
//                double tota=0.00;
//                for (int x=0;x<dealOrderVos1.size();x++){
//                    tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
//                }
//                TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
//                if (null != dealOrderVos1 && dealOrderVos1.size() > 0) {
//                    itemsPrice.setText(i+" Items "+"\n$" + tota);
//                    itemsPrice.setVisibility(View.VISIBLE);
//                }
//                else{
//                    itemsPrice.setVisibility(View.INVISIBLE);
//
//                }
//                AppSharedPreference.clearDealCoupon(DealGroup.this,couponsId);
//                finish();
//
//            } catch (Exception ex)
//            {
//                System.out.println(ex.getMessage());
//            }finally{
//                if(null!=dao)
//                    dao.closeConnection();
//            }
        }else {
            DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealGroup.this);
            dbInstance.open();
            dbInstance.insertDealOrder(dealOrderVo);
            Toast.makeText(DealGroup.this, "Select product from deal groups", Toast.LENGTH_LONG).show();
            dbInstance.close();
            finish();

            
            
//            AppDao dao=null;
//            try {
//                dao=AppDao.getSingleton(getApplicationContext());
//                dao.openConnection();
//                dao.insertDealOrder(dealOrderVo);
//                Toast.makeText(DealGroup.this, "Select product from deal groups", Toast.LENGTH_LONG).show();
//                finish();
//
//            } catch (Exception ex)
//            {
//                System.out.println(ex.getMessage());
//            }finally{
//                if(null!=dao)
//                    dao.closeConnection();
//            }
        }
    }

    public void populateToppingsAndSauces(String prodId) {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        Log.d("--------",prodId);
        HttpGet httpGet = new HttpGet(Constants.ROOT_URL+"/GetPizzaToppingsAndSauces.aspx?prodID="+prodId);
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


            serverResponse1 = builder.toString();

            //////////////////////////////////////////////////////////
            String errorMessage="";
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            JSONArray results = new JSONArray(serverResponse1);
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

            ArrayList<ToppingsAndSauces> pCatList = new ArrayList<ToppingsAndSauces>();

            if(dataExists==true)
            {
                ToppingsAndSauces aBean;
                for(int i=0; i<resultsArray.length(); i++){
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if(jsResult!=null){
                        String jsonString = jsResult.toString();
                        aBean=new ToppingsAndSauces();
                        aBean=gson.fromJson(jsonString, ToppingsAndSauces.class);
                        //                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                        pCatList.add(aBean);

                    }
                }
            }

//            AppDao dao=null;
//            try {
//                dao=AppDao.getSingleton(getApplicationContext());
//                dao.openConnection();
//
//                dao.insertToppingSauces(pCatList);
//
//            } catch (Exception ex)
//            {
//                System.out.println(ex.getMessage());
//            }finally{
//                if(null!=dao)
//                    dao.closeConnection();
//            }
            
            DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealGroup.this);
            dbInstance.open();
            dbInstance.insertToppingSauces(pCatList);
            dbInstance.close();

            System.out.println("Got Toppings And Sauces: "+pCatList.size());
            //////////////////////////// LOOOOOOOOOOOOPPPPPPPPPPPPPPP
            //////////////////////////////////////////////////////////
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {

            e.printStackTrace();
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void populateToppingSizes() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL + "/GetToppingSizes.aspx");
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

            // ////////////////////////////////////////////////////////
            String errorMessage = "";
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            JSONArray results = new JSONArray(serverResponse);
            JSONObject respOuter = results.getJSONObject(0);
            JSONObject resp = respOuter.getJSONObject("Response");
            String status = resp.getString("Status");
            JSONArray resultsArray = null;
            Object data = resp.get("Data");
            boolean dataExists = false;
            if (data instanceof JSONArray) {
                resultsArray = (JSONArray) data;
                dataExists = true;
            }

            JSONObject errors = resp.getJSONObject("Errors");

            boolean hasError = errors.has("Message");
            if (hasError) {
                errorMessage = errors.getString("Message");
                System.out.println("Error:" + errorMessage);
            }

            ArrayList<ToppingSizes> pCatList = new ArrayList<ToppingSizes>();

            if (dataExists == true) {
                ToppingSizes aBean;
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if (jsResult != null) {
                        String jsonString = jsResult.toString();
                        aBean = new ToppingSizes();
                        aBean = gson
                                .fromJson(jsonString, ToppingSizes.class);
                        // System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                        pCatList.add(aBean);
                    }
                }
            }

//			AppProperties.subCatList = pCatList;
            
            
            
//            AppDao dao=null;
//            try {
//                dao=AppDao.getSingleton(getApplicationContext());
//                dao.openConnection();
//
//                dao.insertToppingSizes(pCatList);
//
//            } catch (Exception ex)
//            {
//                System.out.println(ex.getMessage());
//            }finally{
//                if(null!=dao)
//                    dao.closeConnection();
//            }
            DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealGroup.this);
            dbInstance.open();
            dbInstance.insertToppingSizes(pCatList);
            dbInstance.close();

            System.out.println("Got Topping Sizes : " + pCatList.size());
            // ////////////////////////// LOOOOOOOOOOOOPPPPPPPPPPPPPPP
            // ////////////////////////////////////////////////////////
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void populateToppingPrices() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL + "/GetToppingPrices.aspx");
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

            // ////////////////////////////////////////////////////////
            String errorMessage = "";
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            JSONArray results = new JSONArray(serverResponse);
            JSONObject respOuter = results.getJSONObject(0);
            JSONObject resp = respOuter.getJSONObject("Response");
            String status = resp.getString("Status");
            JSONArray resultsArray = null;
            Object data = resp.get("Data");
            boolean dataExists = false;
            if (data instanceof JSONArray) {
                resultsArray = (JSONArray) data;
                dataExists = true;
            }

            JSONObject errors = resp.getJSONObject("Errors");

            boolean hasError = errors.has("Message");
            if (hasError) {
                errorMessage = errors.getString("Message");
                System.out.println("Error:" + errorMessage);
            }

            ArrayList<ToppingPrices> pCatList = new ArrayList<ToppingPrices>();

            if (dataExists == true) {
                ToppingPrices aBean;
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if (jsResult != null) {
                        String jsonString = jsResult.toString();
                        aBean = new ToppingPrices();
                        aBean = gson
                                .fromJson(jsonString, ToppingPrices.class);
                        // System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                        pCatList.add(aBean);
                    }
                }
            }

//			AppProperties.allProductsList = pCatList;
            
            DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealGroup.this);
            dbInstance.open();
            syncedPrices = dbInstance.insertToppingPrices(pCatList);
            dbInstance.close();
            
            
//            AppDao dao=null;
//            try {
//                dao=AppDao.getSingleton(getApplicationContext());
//                dao.openConnection();
//
//                dao.insertToppingPrices(pCatList);
//
//            } catch (Exception ex)
//            {
//                System.out.println(ex.getMessage());
//            }finally{
//                if(null!=dao)
//                    dao.closeConnection();
//            }

            System.out.println("Got product catetgories: " + pCatList.size());
            // ////////////////////////// LOOOOOOOOOOOOPPPPPPPPPPPPPPP
            // ////////////////////////////////////////////////////////
        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    String serverResponse1;

}