package com.deepslice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.*;
import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.AllProductsVo;
import com.deepslice.model.DealOrder;
import com.deepslice.model.Favourites;
import com.deepslice.model.Order;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FavAddActivity extends Activity {
    TextView favCountTxt;
    int currentCount=1;
    AllProductsVo prodBean;
    EditText editView;
    String catType,couponGroupID,productId="";
    boolean isDeal=false;
    DealOrder dealOrderVo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_add);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle b = this.getIntent().getExtras();
        String itemName=b.getString("itemName");
        catType=b.getString("catType");
        prodBean=(AllProductsVo)b.getSerializable("prodBean");
        Button buttonAddOrders= (Button)findViewById(R.id.buttonAddOrders);
        isDeal=b.getBoolean("isDeal",false);
        if (isDeal){
            dealOrderVo=(DealOrder)b.getSerializable("dealData");
            couponGroupID=b.getString("couponGroupID");
            productId=dealOrderVo.getProdID();
            buttonAddOrders.setText("Add to Deal");
        } else {
            buttonAddOrders.setText("Add to Order");
        }
        TextView headerTextView=(TextView)findViewById(R.id.headerTextView);
        headerTextView.setText(itemName);

        editView=(EditText)findViewById(R.id.favPizzaName);
        editView.setText(itemName);//"My "+


        favCountTxt=(TextView)findViewById(R.id.favCountTxt);
        ImageView favArrowDown= (ImageView)findViewById(R.id.favArrowDown);
        favArrowDown.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(currentCount>1)
                {
                    currentCount--;
                    favCountTxt.setText(currentCount+"");
                }
            }
        });
        ImageView favArrowUp= (ImageView)findViewById(R.id.favArrowUp);
        favArrowUp.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(currentCount<10)
                {
                    currentCount++;
                    favCountTxt.setText(currentCount+"");
                }
            }
        });


        Button buttonAddFav= (Button)findViewById(R.id.buttonAddFav);
        buttonAddFav.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                DeepsliceDatabase dbInstance = new DeepsliceDatabase(FavAddActivity.this);
                dbInstance.open();
                boolean favAdded=dbInstance.favAlreadyAdded(prodBean.getProdID(),editView.getText().toString());
                if(favAdded)
                {
                    Toast.makeText(FavAddActivity.this, "Already added to Favourites", Toast.LENGTH_LONG).show();
                }
                else
                {
                    dbInstance.insertFav(getFavBean());
                    doOnResumeWork();
                    Toast.makeText(FavAddActivity.this, "Successfully added to Favourites", Toast.LENGTH_LONG).show();
                }
                dbInstance.close();

                //				AppDao dao=null;
                //				try {
                //					dao=AppDao.getSingleton(getApplicationContext());
                //					dao.openConnection();
                //					
                //					boolean favAdded=dao.favAlreadyAdded(prodBean.getProdID(),editView.getText().toString());
                //					if(favAdded)
                //					{
                //						Toast.makeText(FavAddActivity.this, "Already added to Favourites", Toast.LENGTH_LONG).show();
                //					}
                //					else
                //					{
                //						dao.insertFav(getFavBean());
                //						doOnResumeWork();
                //						Toast.makeText(FavAddActivity.this, "Successfully added to Favourites", Toast.LENGTH_LONG).show();
                //					}
                //					
                //					
                //				} catch (Exception ex)
                //				{
                //					System.out.println(ex.getMessage());
                //				}finally{
                //					if(null!=dao)
                //						dao.closeConnection();
                //				}

            }


        });


        buttonAddOrders.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                DeepsliceDatabase dbInstance = new DeepsliceDatabase(FavAddActivity.this);
                dbInstance.open();
                if (isDeal){
                    dealOrderVo.setQuantity(String.valueOf(currentCount));
                    if(dbInstance.isDealProductAvailable(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID())){
                        dbInstance.deleteDuplicateDealOrderRec(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID());
                        dbInstance.resetDealOrder(dealOrderVo.getCouponID());
                    }
                    dbInstance.insertDealOrder(dealOrderVo);
                    if(dbInstance.getDealOrderCount(dealOrderVo.getCouponID())==AppSharedPreference.getInteger(FavAddActivity.this,"numDeals",0)){
                        AppSharedPreference.putBoolean(FavAddActivity.this,dealOrderVo.getCouponID(),true);
                        Toast.makeText(FavAddActivity.this, "complete your deal by tapping GET A DEAL", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(FavAddActivity.this, "Select product from deal groups", Toast.LENGTH_LONG).show();
                    }
                    finish();
                }else {
                    dbInstance.insertOrder(getOrderBean());
                    Toast.makeText(FavAddActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
                    finish();
                }
                dbInstance.close();


                //				AppDao dao=null;
                //				try {
                //					dao=AppDao.getSingleton(getApplicationContext());
                //					dao.openConnection();
                //                    if (isDeal){
                //                        dealOrderVo.setQuantity(String.valueOf(currentCount));
                //                        if(dao.isDealProductAvailable(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID())){
                //                            dao.deleteDuplicateDealOrderRec(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID());
                //                            dao.resetDealOrder(dealOrderVo.getCouponID());
                //                        }
                //                        dao.insertDealOrder(dealOrderVo);
                //                        if(dao.getDealOrderCount(dealOrderVo.getCouponID())==AppSharedPreference.getInteger(FavAddActivity.this,"numDeals",0)){
                //                            AppSharedPreference.putBoolean(FavAddActivity.this,dealOrderVo.getCouponID(),true);
                //                            Toast.makeText(FavAddActivity.this, "complete your deal by tapping GET A DEAL", Toast.LENGTH_LONG).show();
                //                        }else {
                //                            Toast.makeText(FavAddActivity.this, "Select product from deal groups", Toast.LENGTH_LONG).show();
                //                        }
                //                        finish();
                //                    }else {
                //						dao.insertOrder(getOrderBean());
                //                        Toast.makeText(FavAddActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
                //                        finish();
                //                    }
                //
                //				} catch (Exception ex)
                //				{
                //					System.out.println(ex.getMessage());
                //				}finally{
                //					if(null!=dao)
                //						dao.closeConnection();
                //				}
            }
        });

        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(FavAddActivity.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });
        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(FavAddActivity.this,FavsListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(FavAddActivity.this,MenuActivity.class);
                startActivity(intent);

            }
        });
    }

    private Favourites getFavBean() {

        Favourites f = new Favourites();
        f.setProdCatID(prodBean.getProdCatID());
        f.setSubCatID1(prodBean.getSubCatID1());
        f.setSubCatID2(prodBean.getSubCatID2());
        f.setProdID(prodBean.getProdID());
        f.setProdCode(prodBean.getProdCode());
        f.setDisplayName(editView.getText().toString());
        f.setProdAbbr(prodBean.getProdAbbr());
        f.setProdDesc(prodBean.getProdDesc());
        f.setIsAddDeliveryAmount(prodBean.getIsAddDeliveryAmount());
        f.setDisplaySequence(prodBean.getDisplaySequence());
        f.setCaloriesQty(prodBean.getCaloriesQty());
        f.setPrice(prodBean.getPrice());
        f.setThumbnail(prodBean.getThumbnail());
        f.setFullImage(prodBean.getFullImage());
        f.setCustomName(editView.getText().toString());
        f.setProdCatName(catType);
        return f;
    }
    private Order getOrderBean() {

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

        double finalPrice=currentCount*Double.parseDouble(prodBean.getPrice());
        finalPrice=AppProperties.roundTwoDecimals(finalPrice);
        f.setPrice(String.valueOf(finalPrice));

        f.setThumbnail(prodBean.getThumbnail());
        f.setFullImage(prodBean.getFullImage());

        f.setQuantity(String.valueOf(currentCount));

        f.setCrust("");
        f.setSauce("");
        f.setToppings("");

        f.setProdCatName(catType);

        return f;
    }

    @Override
    protected void onResume() {
        // //////////////////////////////////////////////////////////////////////////////
        doOnResumeWork();
        // ///////////////////////////////////////////////////////////////////////

        super.onResume();
    }

    private void doOnResumeWork() {
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(FavAddActivity.this);
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
    }

}
