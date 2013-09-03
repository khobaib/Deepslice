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

import com.deepslice.activity.R.id;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.LocationDetails;
import com.deepslice.model.NewDealsOrder;
import com.deepslice.model.NewProductOrder;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order);

        totalPrice = (TextView)findViewById(R.id.textTextView12);

        appInstance = (DeepsliceApplication) getApplication();

        /////////////////
        /*
		TextView moDeliveryStore=(TextView)findViewById(R.id.moDeliveryStore);
		TextView moDeliveryAddress=(TextView)findViewById(R.id.moDeliveryAddress);
		TextView moDeliveryTime=(TextView)findViewById(R.id.moDeliveryTime);
		TextView moYourDetails=(TextView)findViewById(R.id.moYourDetails);
		moDeliveryStore.setText("Delivery Store: "+AppProperties.NVL(locationObj.getLocName()));
		moDeliveryAddress.setText(AppProperties.NVL(locationObj.getLocAddress())+" "+AppProperties.NVL(locationObj.getLocName())+", "+AppProperties.NVL(locationObj.getLocPhones()));
		moDeliveryTime.setText(AppSharedPreference.getData(MyOrderActivity.this, "deliveryTime", ""));
		if(dets.length()>5)
			moYourDetails.setText(dets);
		else
			moYourDetails.setText("");
		///////////////////////////////
		ImageView startNewOrder=(ImageView)findViewById(R.id.startNewOrder);
		startNewOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MyOrderActivity.this);
				builder.setCancelable(true);
				builder.setTitle("DeepSlice");
				builder.setMessage("Are you want to clear current order and start new?");
				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

						AppDao dao=null;
						try {
							dao=AppDao.getSingleton(getApplicationContext());
							dao.openConnection();

							dao.cleanOrderTable();

						} catch (Exception ex)
						{
							System.out.println(ex.getMessage());
						}finally{
							if(null!=dao)
								dao.closeConnection();
						}

						AppSharedPreference.clearCouponInformation(MyOrderActivity.this);

						finish();
					}
				});
				AlertDialog alerta = builder.create();
				alerta.show();
			}
		});
         */

        TextView mOrderType = (TextView)findViewById(id.headerTextView);
        LocationDetails locationObj = AppProperties.getLocationObj(MyOrderActivity.this);

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

                Intent i=new Intent(MyOrderActivity.this, CouponsActivity.class);
                startActivity(i);
                finish();
            }
        });

        ImageView anythingElse=(ImageView)findViewById(R.id.imageView1);
        anythingElse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyOrderActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button placeOrder = (Button)findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //	Intent i=new Intent(MyOrderActivity.this,PaymentSelectionActivity.class);
                //	startActivity(i);

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
                        }else {
                            startActivity(new Intent(new Intent(MyOrderActivity.this, StoreFromHistoryActivity.class)));
                        }   
                    }
                    else{
                        startActivity(new Intent(new Intent(MyOrderActivity.this, CustomerDetailsActivity.class)));
                    }
                }
            }
        });

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
        dbInstance.open();
        pizzaList = dbInstance.getOrdersListWithCategory(Constants.PRODUCT_CATEGORY_PIZZA);
        drinksList = dbInstance.getOrdersListWithCategory(Constants.PRODUCT_CATEGORY_DRINKS);
        sidesList = dbInstance.getOrdersListWithCategory(Constants.PRODUCT_CATEGORY_SIDES);
        pastaList = dbInstance.getOrdersListWithCategory(Constants.PRODUCT_CATEGORY_PASTA);
        dealOrderList=dbInstance.retrieveDealOrderList(true);
        dbInstance.close();

        wholePizzaList = new ArrayList<NewProductOrder>();
        halfPizzaList = new ArrayList<NewProductOrder>();
        createOwnPizzaList = new ArrayList<NewProductOrder>();
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
            addCatHeader("Pizza", lout);

            for (NewProductOrder order : wholePizzaList) {
                dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
                dbInstance.open();
                double toppingsPrice = dbInstance.retrieveProductToppingsPrice(order.getPrimaryId());
                dbInstance.close();
                double pizzaTotalPrice = toppingsPrice + Double.parseDouble(order.getPrice());
                String itemPrice = "$" + pizzaTotalPrice; 

                addItem(lout, false, false, order.getPrimaryId(), order.getDisplayName(), itemPrice, order.getQuantity());
            }
        }


        if(halfPizzaList != null && halfPizzaList.size() > 0){
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
            addCatHeader("Half-n-Half", lout);

            for (NewProductOrder order : halfPizzaList) {
                dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
                dbInstance.open();
                double toppingsPrice = dbInstance.retrieveProductToppingsPrice(order.getPrimaryId());
                dbInstance.close();
                double pizzaTotalPrice = toppingsPrice + Double.parseDouble(order.getPrice());
                String itemPrice = "$" + pizzaTotalPrice; 

                addItem(lout, false, true, order.getPrimaryId(), order.getDisplayName(), itemPrice, order.getQuantity());
            }
        }


        if(createOwnPizzaList != null && createOwnPizzaList.size() > 0){
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
            addCatHeader("Create Your Own", lout);

            for (NewProductOrder order : createOwnPizzaList) {
                dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
                dbInstance.open();
                double toppingsPrice = dbInstance.retrieveProductToppingsPrice(order.getPrimaryId());
                dbInstance.close();
                double pizzaTotalPrice = toppingsPrice + Double.parseDouble(order.getPrice());
                String itemPrice = "$" + pizzaTotalPrice; 

                addItem(lout, false, false, order.getPrimaryId(), order.getDisplayName(), itemPrice, order.getQuantity());
            }
        }


        if (drinksList!= null && drinksList.size() > 0) {
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
            addCatHeader("Drinks", lout);

            for (NewProductOrder order : drinksList) {
                addItem(lout, false, false, order.getPrimaryId(), order.getDisplayName(), "$" + order.getPrice(), order.getQuantity());
            }

        }

        if (pastaList!= null && pastaList.size() > 0) {
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
            addCatHeader("Pasta", lout);

            for (NewProductOrder order : pastaList) {
                addItem(lout, false, false, order.getPrimaryId(), order.getDisplayName(), "$" + order.getPrice(), order.getQuantity());
            }

        }

        if (sidesList!= null && sidesList.size() > 0) {
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
            addCatHeader("Sides", lout);

            for (NewProductOrder order : sidesList) {
                addItem(lout, false, false, order.getPrimaryId(), order.getDisplayName(), "$" + order.getPrice(), order.getQuantity());
            }

        }


        if(dealOrderList != null && dealOrderList.size() > 0) {
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);
            addCatHeader("Deals", lout);

            for (NewDealsOrder order : dealOrderList) {
                addItem(lout, true, false, order.getPrimaryId(), order.getCouponDesc(), "$" + order.getTotalPrice(), order.getQuantity());
            }
        }
    }



    private void addCatHeader(String header, LinearLayout lout){
        TextView tv = new TextView(this);
        tv.setText(header);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(25);

        LinearLayout.LayoutParams lpYello = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lpYello);
        tv.setPadding(25, 5, 5, 5);

        tv.setBackgroundResource(R.drawable.sides_yellow);
        lout.addView(tv);
    }


    private void addItem(LinearLayout lout, final boolean isDeal, boolean isHalf,
            final int orderId, String itemName, String itemPrice, String itemQuantity) {
        RelativeLayout ll = new RelativeLayout(this);

        if(isDeal)
            ll.setId(orderId + 99999);
        else
            ll.setId(orderId + 99);

        RelativeLayout.LayoutParams rlay = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlay.setMargins(5, 10, 5, 5);
        lout.addView(ll);
        ll.setBackgroundResource(R.drawable.kj_line);

        ImageView icon = new ImageView(this);
        icon.setId(orderId + 1);
        icon.setScaleType(ScaleType.FIT_XY);
        icon.setImageResource(R.drawable.order_cross);
        RelativeLayout.LayoutParams iocnLO = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        iocnLO.width=60;
        iocnLO.height=60;
        iocnLO.addRule(RelativeLayout.CENTER_VERTICAL);
        iocnLO.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ll.addView(icon, iocnLO);

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
        titleLO.setMargins(5, 5, 10, 5);
        titleLO.addRule(RelativeLayout.CENTER_VERTICAL);
        titleLO.addRule(RelativeLayout.RIGHT_OF, icon.getId());
        ll.addView(title, titleLO);


        TextView price=new TextView(this);
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

        icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isDeal)
                    deleteFromDealOrder(orderId);
                else
                    deleteFromOrder(orderId);

            }
        });
    }




    @Override
    protected void onResume(){
        super.onResume();
//        if(AppSharedPreference.getBoolean(MyOrderActivity.this, "okLogin")){
//            AppSharedPreference.putBoolean(MyOrderActivity.this, "okLogin", false);
//            String orderType=AppSharedPreference.getData(MyOrderActivity.this, "orderType", null);
//            if("Delivery".equalsIgnoreCase(orderType)) {
//                Intent i=new Intent(MyOrderActivity.this,LocationFromHistoryActivity.class);
//                startActivity(i);
//                finish();
//            } else {
//
//            }
//        }
    }
    private void deleteFromOrder(int orderId){

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
        dbInstance.open();
        dbInstance.deleteProductOrder(orderId);
        dbInstance.close();

        List<String> orderInfo = Utils.OrderInfo(MyOrderActivity.this);
        if(orderInfo != null && orderInfo.size() == 2){
            String orderPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);

            totalPrice.setText("TOTAL: $" + orderPrice);
        }

        RelativeLayout ll = (RelativeLayout)findViewById(orderId + 99);
        ll.setVisibility(View.GONE);
    }


    private void deleteFromDealOrder(int orderId){

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
        dbInstance.open();
        dbInstance.deleteDealsOrder(orderId);
        dbInstance.close();

        List<String> orderInfo = Utils.OrderInfo(MyOrderActivity.this);
        if(orderInfo != null && orderInfo.size() == 2){
            String orderPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);

            totalPrice.setText("TOTAL: $" + orderPrice);
        }

        RelativeLayout ll = (RelativeLayout)findViewById(orderId + 99999);
        ll.setVisibility(View.GONE);    
    }
}
