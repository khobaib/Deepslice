package com.deepslice.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.activity.R.id;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.LocationDetails;
import com.deepslice.model.NewDealsOrder;
import com.deepslice.model.NewProductOrder;
import com.deepslice.model.OrderInfo;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;
import com.deepslice.utilities.Utils;

public class MyOrderActivity extends Activity{

    List<NewProductOrder> pizzaList, wholePizzaList, halfPizzaList, createOwnPizzaList;
    List<NewProductOrder> drinksList;
    List<NewProductOrder> sidesList;
    List<NewProductOrder> pastaList;
    List<NewDealsOrder> dealOrderList;

    TextView totalPrice;

    DeepsliceApplication appInstance;

    List<Integer> layoutIds;

    private static final int PIZZA_HEADER_ID = 1;
    private static final int HNH_HEADER_ID = 2;
    private static final int PASTA_HEADER_ID = 3;
    private static final int DRINKS_HEADER_ID = 4;
    private static final int SIDES_HEADER_ID = 5;
    private static final int DEALS_HEADER_ID = 6;
    private static final int CREATE_YOUR_OWN_HEADER_ID = 7;

    private static final int DUMMY_OTHER_HALF_ORDER_ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(this, "92b170cf");
        
        setContentView(R.layout.my_order);

        totalPrice = (TextView)findViewById(R.id.textTextView12);

        appInstance = (DeepsliceApplication) getApplication();

        TextView mOrderType = (TextView)findViewById(id.headerTextView);

        ///////////////////////////

        String dets=AppSharedPreference.getData(MyOrderActivity.this, "customerName", "");
        dets+=", ";
        dets+=AppSharedPreference.getData(MyOrderActivity.this, "customerEmail", "");
        dets+=", ";
        dets+=AppSharedPreference.getData(MyOrderActivity.this, "customerPhone", "");
        String orderType=AppSharedPreference.getData(MyOrderActivity.this, "orderType", "");
        mOrderType.setText(orderType);

        ///////////////////////////
        ImageView addCoupon=(ImageView)findViewById(R.id.imageView2);
        addCoupon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //                Intent i=new Intent(MyOrderActivity.this, CouponsActivity.class);
                //                startActivity(i);
                //                finish();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyOrderActivity.this);
                alertDialog.setTitle("Discount Coupons");
                alertDialog.setMessage("Coming Soon");
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    } 
                }); 
                alertDialog.create().show(); 
            }
        });

        ImageView anythingElse=(ImageView)findViewById(R.id.imageView1);
        anythingElse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyOrderActivity.this, MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        Button placeOrder = (Button)findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int orderItemCount = 0;
                List<String> orderInfo = Utils.OrderInfo(MyOrderActivity.this);
                if(orderInfo != null && orderInfo.size() == 2){
                    orderItemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
                }
                if(orderItemCount == 0){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyOrderActivity.this);
                    alertDialog.setTitle("Deepslice");
                    alertDialog.setMessage("No Item in your cart");
                    alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MyOrderActivity.this.finish();
                        } 
                    }); 
                    alertDialog.create().show(); 
                }

                else{                    
                    appInstance.setOrderReady(true);
                    if(AppProperties.isLoggedIn || 
                            (appInstance.loadCustomer().getCustomerName() != null && !appInstance.loadCustomer().getCustomerName().equals(""))){
                        Log.d("????????>>>>>", "customer id = " + appInstance.loadCustomer().getCustomerID());
                        int orderType = appInstance.getOrderType();
                        if(orderType == Constants.ORDER_TYPE_DELIVERY) {
                            startActivity(new Intent(new Intent(MyOrderActivity.this, LocationFromHistoryActivity.class)));
                        }else if(orderType == Constants.ORDER_TYPE_PICKUP){
                            startActivity(new Intent(new Intent(MyOrderActivity.this, StoreFromHistoryActivity.class)));
                        }   
                        else{
                            Toast.makeText(MyOrderActivity.this, "Order method is not selected yet, please choose pickup or delivery.",  Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        startActivity(new Intent(new Intent(MyOrderActivity.this, CustomerDetailsActivity.class)));
                    }
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



    private void showOrderList() {

        layoutIds = new ArrayList<Integer>();

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
        dbInstance.open();
        pizzaList = dbInstance.getOrdersListWithCategory(Constants.PRODUCT_CATEGORY_PIZZA);
        drinksList = dbInstance.getOrdersListWithCategory(Constants.PRODUCT_CATEGORY_DRINKS);
        sidesList = dbInstance.getOrdersListWithCategory(Constants.PRODUCT_CATEGORY_SIDES);
        pastaList = dbInstance.getOrdersListWithCategory(Constants.PRODUCT_CATEGORY_PASTA);
        dealOrderList = dbInstance.retrieveDealOrderList(true);
        dbInstance.close();

        wholePizzaList = new ArrayList<NewProductOrder>();
        halfPizzaList = new ArrayList<NewProductOrder>();
        createOwnPizzaList = new ArrayList<NewProductOrder>();

        // separating whole & HnH pizza
        for (NewProductOrder order : pizzaList) {
            if(order.getIsCreateByOwn())
                createOwnPizzaList.add(order);
            else if(order.getSelection() == Constants.PRODUCT_SELECTION_WHOLE)
                wholePizzaList.add(order);
            else
                halfPizzaList.add(order);
        }

        List<String> orderInfo = Utils.OrderInfo(MyOrderActivity.this);
        if(orderInfo != null && orderInfo.size() == 2){
            String orderPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
            totalPrice.setText("TOTAL: $" + orderPrice);
        }


        if(wholePizzaList != null && wholePizzaList.size() > 0){
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
            addCatHeader("Pizza", lout, PIZZA_HEADER_ID);

            for (NewProductOrder order : wholePizzaList) {
                dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
                dbInstance.open();
                double toppingsPrice = dbInstance.retrieveProductToppingsPrice(order.getPrimaryId());
                dbInstance.close();
                double pizzaTotalPrice = (toppingsPrice + Double.parseDouble(order.getPrice()))
                        * Integer.parseInt(order.getQuantity());
                String itemPrice = "$" + Constants.twoDForm.format(pizzaTotalPrice); 

                addItem(lout, false, false, false, order.getPrimaryId(), order.getDisplayName(),
                        itemPrice, order.getQuantity(), DUMMY_OTHER_HALF_ORDER_ID);
            }
        }


        if(halfPizzaList != null && halfPizzaList.size() > 0){
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
            addCatHeader("Half-n-Half", lout, HNH_HEADER_ID);

            for (NewProductOrder order : halfPizzaList) {
                
                boolean isSecondHalf = false;
                double otherHalfToppingPrice = 0.0;
                String otherHalfPriceStr = null;
                if(order.getSelection() == Constants.PRODUCT_SELECTION_RIGHT){
                    isSecondHalf = true;
                    int otherHalfPrimaryId = order.getOtherHalfProdId();
                    
                    dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
                    dbInstance.open();
                    otherHalfToppingPrice = dbInstance.retrieveProductToppingsPrice(otherHalfPrimaryId);
                    otherHalfPriceStr = dbInstance.getOrderPriceFromId(otherHalfPrimaryId);
                    dbInstance.close();
                } 
                double otherHalfPrice = (otherHalfPriceStr == null) ? 0.0 : Double.parseDouble(otherHalfPriceStr);
                
                dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
                dbInstance.open();
                double toppingsPrice = otherHalfToppingPrice + dbInstance.retrieveProductToppingsPrice(order.getPrimaryId());
                dbInstance.close();
                double pizzaTotalPrice = (toppingsPrice + Double.parseDouble(order.getPrice()) + otherHalfPrice)
                        * Integer.parseInt(order.getQuantity());
                String itemPrice = "$" + Constants.twoDForm.format(pizzaTotalPrice);



                //                int otherHalfOrderId = DUMMY_OTHER_HALF_ORDER_ID;
                //                if(order.getSelection() == Constants.PRODUCT_SELECTION_LEFT){
                //                    otherHalfOrderId = order.getSecondHalfProdId();
                //                }
                int otherHalfOrderId = order.getOtherHalfProdId();

                addItem(lout, false, true, isSecondHalf, order.getPrimaryId(), order.getDisplayName(),
                        itemPrice, order.getQuantity(), otherHalfOrderId);
            }
        }


        if(createOwnPizzaList != null && createOwnPizzaList.size() > 0){
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
            addCatHeader("Create Your Own", lout, CREATE_YOUR_OWN_HEADER_ID);

            for (NewProductOrder order : createOwnPizzaList) {
                dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
                dbInstance.open();
                double toppingsPrice = dbInstance.retrieveProductToppingsPrice(order.getPrimaryId());
                dbInstance.close();
                double pizzaTotalPrice = (toppingsPrice + Double.parseDouble(order.getPrice()))
                        * Integer.parseInt(order.getQuantity());
                String itemPrice = "$" + Constants.twoDForm.format(pizzaTotalPrice); 

                addItem(lout, false, false, false, order.getPrimaryId(), order.getDisplayName(),
                        itemPrice, order.getQuantity(), DUMMY_OTHER_HALF_ORDER_ID);
            }
        }


        if (drinksList!= null && drinksList.size() > 0) {
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
            addCatHeader("Drinks", lout, DRINKS_HEADER_ID);

            for (NewProductOrder order : drinksList) {
                double pizzaTotalPrice = Double.parseDouble(order.getPrice()) * Integer.parseInt(order.getQuantity());                
                addItem(lout, false, false, false, order.getPrimaryId(), order.getDisplayName(),
                        "$" + Constants.twoDForm.format(pizzaTotalPrice), order.getQuantity(), DUMMY_OTHER_HALF_ORDER_ID);
            }

        }

        if (pastaList!= null && pastaList.size() > 0) {
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
            addCatHeader("Pasta", lout, PASTA_HEADER_ID);

            for (NewProductOrder order : pastaList) {
                double pizzaTotalPrice = Double.parseDouble(order.getPrice()) * Integer.parseInt(order.getQuantity());  
                addItem(lout, false, false, false, order.getPrimaryId(), order.getDisplayName(),
                        "$" + Constants.twoDForm.format(pizzaTotalPrice), order.getQuantity(), DUMMY_OTHER_HALF_ORDER_ID);
            }

        }

        if (sidesList!= null && sidesList.size() > 0) {
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
            addCatHeader("Sides", lout, SIDES_HEADER_ID);

            for (NewProductOrder order : sidesList) {
                double pizzaTotalPrice = Double.parseDouble(order.getPrice()) * Integer.parseInt(order.getQuantity());  
                addItem(lout, false, false, false, order.getPrimaryId(), order.getDisplayName(),
                        "$" + Constants.twoDForm.format(pizzaTotalPrice), order.getQuantity(), DUMMY_OTHER_HALF_ORDER_ID);
            }

        }


        if(dealOrderList != null && dealOrderList.size() > 0) {
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
            addCatHeader("Deals", lout, DEALS_HEADER_ID);

            for (NewDealsOrder order : dealOrderList) {
                double pizzaTotalPrice = Double.parseDouble(order.getTotalPrice());  
                addItem(lout, true, false, false, order.getPrimaryId(), order.getCouponDesc(),
                        "$" + Constants.twoDForm.format(pizzaTotalPrice), order.getQuantity(), DUMMY_OTHER_HALF_ORDER_ID);
            }
        }

    }



    private void addCatHeader(String header, LinearLayout lout, int layoutId){
        TextView tv = new TextView(this);
        tv.setText(header);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(25);

        LinearLayout.LayoutParams lpYello = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setId(layoutId);
        layoutIds.add(layoutId);
        tv.setLayoutParams(lpYello);
        tv.setPadding(25, 5, 5, 5);

        tv.setBackgroundResource(R.drawable.sides_yellow);
        lout.addView(tv);
    }


    private void addItem(final LinearLayout lout, final boolean isDeal, boolean isHalf, boolean isSecondHalf,
            final int orderId, String itemName, String itemPrice, String itemQuantity,
            final int secHalfOrderId) {
        RelativeLayout ll = new RelativeLayout(this);

        if(isDeal){
            ll.setId(orderId + 999999);
            layoutIds.add(orderId + 999999);
        }
        else{
            ll.setId(orderId + 99);
            layoutIds.add(orderId + 99);
        }

        RelativeLayout.LayoutParams rlay = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlay.setMargins(5, 10, 5, 5);
        lout.addView(ll);
        ll.setBackgroundResource(R.drawable.kj_line);

        ImageView icon = new ImageView(this);
        if(!isSecondHalf){
            icon.setId(orderId + 1);
            icon.setScaleType(ScaleType.FIT_XY);
            icon.setImageResource(R.drawable.order_cross);
            RelativeLayout.LayoutParams iocnLO = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            iocnLO.width=60;
            iocnLO.height=60;
            iocnLO.addRule(RelativeLayout.CENTER_VERTICAL);
            iocnLO.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            ll.addView(icon, iocnLO);
        }

        TextView title=new TextView(this);
        title.setId(orderId + 2);
        if(isHalf)
            title.setText(itemQuantity + "(1/2) X " + itemName);
        else
            title.setText(itemQuantity + " X " + itemName);
        title.setTextColor(Color.WHITE);
        title.setTextSize(16);
        title.setMaxLines(2);
        RelativeLayout.LayoutParams titleLO = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleLO.setMargins(65, 5, 10, 5);
        titleLO.addRule(RelativeLayout.CENTER_VERTICAL);
        titleLO.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //            titleLO.addRule(RelativeLayout.RIGHT_OF, icon.getId());
        ll.addView(title, titleLO);

        if(!isHalf || isSecondHalf){
            TextView price = new TextView(this);
            price.setId(orderId + 3);
            price.setText(itemPrice);
            price.setTextColor(Color.WHITE);
            price.setTextSize(14);
            price.setLines(1);
            RelativeLayout.LayoutParams priceLO = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            priceLO.setMargins(5, 5, 15, 5);
            priceLO.addRule(RelativeLayout.CENTER_VERTICAL);
            priceLO.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ll.addView(price, priceLO);
            titleLO.addRule(RelativeLayout.LEFT_OF , price.getId());
        }

        icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isDeal)
                    deleteFromDealOrder(lout, orderId);
                else{
                    deleteFromOrder(lout, orderId, secHalfOrderId);
                }

            }
        });
    }




    @Override
    protected void onResume(){
        super.onResume();

        LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
        lout.removeAllViews();

        showOrderList();

    }

    private void deleteFromOrder(final LinearLayout lout, int orderId, int secHalfOrderId){

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
        dbInstance.open();
        dbInstance.deleteProductOrder(orderId);
        if(secHalfOrderId != DUMMY_OTHER_HALF_ORDER_ID)
            dbInstance.deleteProductOrder(secHalfOrderId);              // 2n-half of HnH
        dbInstance.close();

        List<String> orderInfo = Utils.OrderInfo(MyOrderActivity.this);
        if(orderInfo != null && orderInfo.size() == 2){
            String orderPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
            totalPrice.setText("TOTAL: $" + orderPrice);
        }
        lout.removeAllViews();

        //        RelativeLayout ll = (RelativeLayout)findViewById(orderId + 99);
        //        ll.setVisibility(View.GONE);
        //        for(Integer layoutId : layoutIds){
        //            if(layoutId < CREATE_YOUR_OWN_HEADER_ID){
        //                LinearLayout ll = (LinearLayout) findViewById(layoutId);
        //
        //            }
        //        }


        showOrderList();
    }


    private void deleteFromDealOrder(final LinearLayout lout, int orderId){

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
        dbInstance.open();
        dbInstance.deleteDealsOrder(orderId);
        dbInstance.close();

        List<String> orderInfo = Utils.OrderInfo(MyOrderActivity.this);
        if(orderInfo != null && orderInfo.size() == 2){
            String orderPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
            totalPrice.setText("TOTAL: $" + orderPrice);
        }

        lout.removeAllViews();

        //        RelativeLayout ll = (RelativeLayout)findViewById(orderId + 999999);
        //        ll.setVisibility(View.GONE);   

        showOrderList();
    }
}
