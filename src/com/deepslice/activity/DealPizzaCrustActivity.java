package com.deepslice.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.cache.ImageLoader;
import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.CouponData;
import com.deepslice.model.DealOrder;
import com.deepslice.model.ProdAndSubCategory;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 7/20/13
 * Time: 8:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class DealPizzaCrustActivity extends Activity {
    public ImageLoader imageLoader;
    ListView listView;
    MyListAdapter myAdapter;
    DealOrder dealOrderVo;
    DeepsliceApplication globalObject;
    TextView textViewTitle,textViewSub;
    String currentProductId="";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deal_pizza_crust);
        currentProductId=getIntent().getStringExtra("prdID");
        globalObject=(DeepsliceApplication)getApplication();
        final CouponData couponData= globalObject.getCouponData();
        dealOrderVo=globalObject.getDealOrderVo();
        listView=(ListView)findViewById(R.id.listView1);
        textViewTitle=(TextView)findViewById(R.id.headerTextView);
        textViewTitle.setText("Select Value Pizza");
        imageLoader=new ImageLoader(this);
        myAdapter=new MyListAdapter(this,R.layout.deal_single_item, couponData.getProdAndSubCategories());
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dealOrderVo.setDisplayName(couponData.getProdAndSubCategories().get(position).getSubCat2Code());
                dealOrderVo.setProdID(couponData.getProdAndSubCategories().get(position).getProdID());
                addDeals(dealOrderVo);
            }
        });

        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DealPizzaCrustActivity.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });

    }
    private class MyListAdapter extends ArrayAdapter<ProdAndSubCategory> {

        private ArrayList<ProdAndSubCategory> items;


        public MyListAdapter(Context context, int viewResourceId,
                             ArrayList<ProdAndSubCategory> items) {
            super(context, viewResourceId, items);
            this.items = items;
            Log.d("count...................", items.size() + "");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.deal_single_item, null);
            }
            ProdAndSubCategory event = items.get(position);
            if (event != null) {

                TextView title = (TextView) convertView.findViewById(R.id.textView1);
                TextView calories = (TextView) convertView.findViewById(R.id.textView2);
                ImageView imageViewArr=(ImageView)convertView.findViewById(R.id.imageViewArrow);
//                if (!productName.equalsIgnoreCase("pizza")){
//                    imageViewArr.setVisibility(View.INVISIBLE);
//                }
                Log.d("","....."+event.getProdID());
                Log.d("","..cc..."+currentProductId);
                if(event.getProdID().equalsIgnoreCase(currentProductId)){
                    imageViewArr.setBackgroundResource(R.drawable.deal_done);
                }else {
                    imageViewArr.setVisibility(View.INVISIBLE);
                }
                title.setText(event.getSubCat2Code());
                calories.setVisibility(View.INVISIBLE);

//				price.setText("$"+AppProperties.getRoundTwoDecimalString(event.getPrice()));
//				calories.setText(event.getCaloriesQty()+"kj");


                ImageView icon = (ImageView) convertView.findViewById(R.id.imageView1);
                String imgPath= Constants.IMAGES_LOCATION;
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

    public void addDeals(DealOrder dealOrderVo){
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealPizzaCrustActivity.this);
        dbInstance.open();
        AppSharedPreference.putBoolean(DealPizzaCrustActivity.this, dealOrderVo.getCouponGroupID(), true);
        if(dbInstance.isDealProductAvailable(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID())){
            boolean b= dbInstance.deleteDuplicateDealOrderRec(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID());
            dbInstance.resetDealOrder(dealOrderVo.getCouponID());
        }
        dbInstance.insertDealOrder(dealOrderVo);
        if(dbInstance.getDealOrderCount(dealOrderVo.getCouponID())==AppSharedPreference.getInteger(DealPizzaCrustActivity.this,"numDeals",0)){
            AppSharedPreference.putBoolean(DealPizzaCrustActivity.this,dealOrderVo.getCouponID(),true);
            Toast.makeText(DealPizzaCrustActivity.this, "complete your deal by tapping GET A DEAL", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(DealPizzaCrustActivity.this, "Select product from deal groups", Toast.LENGTH_LONG).show();
        }
        dbInstance.close();
        finish();

//        AppDao dao=null;
//        try {
//            dao=AppDao.getSingleton(getApplicationContext());
//            dao.openConnection();
//
//            AppSharedPreference.putBoolean(DealPizzaCrustActivity.this, dealOrderVo.getCouponGroupID(), true);
//            if(dao.isDealProductAvailable(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID())){
//                boolean b= dao.deleteDuplicateDealOrderRec(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID());
//                dao.resetDealOrder(dealOrderVo.getCouponID());
//            }
//            dao.insertDealOrder(dealOrderVo);
//            if(dao.getDealOrderCount(dealOrderVo.getCouponID())==AppSharedPreference.getInteger(DealPizzaCrustActivity.this,"numDeals",0)){
//                AppSharedPreference.putBoolean(DealPizzaCrustActivity.this,dealOrderVo.getCouponID(),true);
//                Toast.makeText(DealPizzaCrustActivity.this, "complete your deal by tapping GET A DEAL", Toast.LENGTH_LONG).show();
//            }else {
//                Toast.makeText(DealPizzaCrustActivity.this, "Select product from deal groups", Toast.LENGTH_LONG).show();
//            }
//            finish();
//
//        } catch (Exception ex)
//        {
//            System.out.println(ex.getMessage());
//        }finally{
//            if(null!=dao)
//                dao.closeConnection();
//        }
    }
    @Override
    protected void onResume() {
        // //////////////////////////////////////////////////////////////////////////////
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(DealPizzaCrustActivity.this);
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
//            // dao.updateDealOrder();
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
}