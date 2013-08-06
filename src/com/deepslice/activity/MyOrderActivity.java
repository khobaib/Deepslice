package com.deepslice.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.deepslice.activity.R.id;
import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.database.HelperSharedPreferences;
import com.deepslice.model.DealOrder;
import com.deepslice.model.LocationDetails;
import com.deepslice.model.Order;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MyOrderActivity extends Activity{

    ArrayList<Order> pizzaList;
    ArrayList<Order> drinksList;
    ArrayList<Order> sidesList;
    ArrayList<Order> pastaList;
    ArrayList<DealOrder> dealOrderVos;


    TextView totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order);
        totalPrice=(TextView)findViewById(R.id.textTextView12);

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

                Intent i=new Intent(MyOrderActivity.this,CouponsActivity.class);
                startActivity(i);
                finish();
            }
        });

        ImageView anythingElse=(ImageView)findViewById(R.id.imageView1);
        anythingElse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyOrderActivity.this,MenuActivity.class);
                startActivity(intent);

                finish();
            }
        });

        Button placeOrder=(Button)findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //	Intent i=new Intent(MyOrderActivity.this,PaymentSelectionActivity.class);
                //	startActivity(i);


                //				i have added //

                String orderType=AppSharedPreference.getData(MyOrderActivity.this, "orderType", null);
                Intent i;

                if("Delivery".equalsIgnoreCase(orderType)) {
                    if (!HelperSharedPreferences.getSharedPreferencesString(
                            MyOrderActivity.this, "emailName", "").equals("")
                            || !HelperSharedPreferences.getSharedPreferencesString(
                                    MyOrderActivity.this, "userName", "").equals("")
                                    || AppProperties.isLoogedIn) {

                        i=new Intent(MyOrderActivity.this,LocationFromHistoryActivity.class);
                        startActivity(i);
                        finish();


                    } else{
                        if(!AppSharedPreference.getData(MyOrderActivity.this,"customerName","").equals("")||!AppSharedPreference.getData(MyOrderActivity.this,"customerEmail","").equals("")
                                ||!AppSharedPreference.getData(MyOrderActivity.this,"customerPhone","").equals("")){
                            i=new Intent(MyOrderActivity.this,LocationFromHistoryActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(MyOrderActivity.this, " Please register/login so we can verify you.",  Toast.LENGTH_SHORT).show();
                            AppSharedPreference.putBoolean(MyOrderActivity.this,"MyOrder",true);
                            Intent intent=new Intent(MyOrderActivity.this,CustomerDetailsActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                else {

                    if (!HelperSharedPreferences.getSharedPreferencesString(
                            MyOrderActivity.this, "emailName", "").equals("")
                            || !HelperSharedPreferences.getSharedPreferencesString(
                                    MyOrderActivity.this, "userName", "").equals("")
                                    || AppProperties.isLoogedIn) {

                        i=new Intent(MyOrderActivity.this,StoreFromHistoryActivity.class);
                        startActivity(i);
                        finish();


                    } else{
                        if(!AppSharedPreference.getData(MyOrderActivity.this,"customerName","").equals("")||!AppSharedPreference.getData(MyOrderActivity.this,"customerEmail","").equals("")
                                ||!AppSharedPreference.getData(MyOrderActivity.this,"customerPhone","").equals("")){
                            i=new Intent(MyOrderActivity.this,StoreFromHistoryActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(MyOrderActivity.this, " Please register/login so we can verify you.",  Toast.LENGTH_SHORT).show();
                            AppSharedPreference.putBoolean(MyOrderActivity.this,"MyOrder",true);
                            Intent intent=new Intent(MyOrderActivity.this,CustomerDetailsActivity.class);
                            startActivity(intent);
                        }
                    }

                }
                //////////////////////////////////////////////////////////////////////////////////////////////////

                //				finish();
            }
        });

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
        dbInstance.open();
        pizzaList = dbInstance.getOrdersListWithType("Pizza");
        drinksList = dbInstance.getOrdersListWithType("Drinks");
        sidesList = dbInstance.getOrdersListWithType("Sides");
        pastaList = dbInstance.getOrdersListWithType("Pasta");
        dealOrderVos=dbInstance.getDealOrdersList();
        ArrayList<String> orderInfo = dbInstance.getOrderInfo();
        Double orderTotal=0.0;
        if(null!=orderInfo && orderInfo.size()==2)
        {
            orderTotal=AppProperties.getRoundTwoDecimalString(orderInfo.get(1));

        }
        Double dealTotal=0.0;
        if(dealOrderVos!=null && dealOrderVos.size()>0){

            for (int x=0;x<dealOrderVos.size();x++){

                dealTotal+=Double.parseDouble(dealOrderVos.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos.get(x).getQuantity()));
            }
        }
        double subT=dealTotal+orderTotal;
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        subT= Double.valueOf(twoDForm.format(subT));
        totalPrice.setText("TOTAL: $"+subT);
        dbInstance.close();

        //		AppDao dao=null;
        //		try {
        //			dao=AppDao.getSingleton(getApplicationContext());
        //			dao.openConnection();
        //
        //			pizzaList = dao.getOrdersListWithType("Pizza");
        //			drinksList = dao.getOrdersListWithType("Drinks");
        //			sidesList = dao.getOrdersListWithType("Sides");
        //			pastaList = dao.getOrdersListWithType("Pasta");
        //			dealOrderVos=dao.getDealOrdersList();
        //			ArrayList<String> orderInfo = dao.getOrderInfo();
        //            Double orderTotal=0.0;
        //			if(null!=orderInfo && orderInfo.size()==2)
        //			{
        //                orderTotal=AppProperties.getRoundTwoDecimalString(orderInfo.get(1));
        //
        //			}
        //            Double dealTotal=0.0;
        //            if(dealOrderVos!=null && dealOrderVos.size()>0){
        //
        //                for (int x=0;x<dealOrderVos.size();x++){
        //
        //                    dealTotal+=Double.parseDouble(dealOrderVos.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos.get(x).getQuantity()));
        //                }
        //            }
        //            double subT=dealTotal+orderTotal;
        //            DecimalFormat twoDForm = new DecimalFormat("#.##");
        //            subT= Double.valueOf(twoDForm.format(subT));
        //            totalPrice.setText("TOTAL: $"+subT);
        //		} catch (Exception ex)
        //		{
        //			System.out.println(ex.getMessage());
        //		}finally{
        //			if(null!=dao)
        //				dao.closeConnection();
        //		}

        ////////////////////////////////////////////////////////////////
        ////// PIZZA

        if(pizzaList!=null && pizzaList.size()>0)
        {
            LinearLayout lout=(LinearLayout)findViewById(R.id.wraperLayout);

            TextView tv=new TextView(this);
            tv.setText("Pizza");
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(27);

            LinearLayout.LayoutParams lpYello = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lpYello);
            tv.setPadding(15, 5, 5, 5);


            tv.setBackgroundResource(R.drawable.sides_yellow);
            lout.addView(tv);

            //for
            for (Order orderObj : pizzaList) {

                String itemName=orderObj.getDisplayName();
                String itemPrice="$"+orderObj.getPrice();
                String itemCalaroies=orderObj.getCaloriesQty()+"kj";
                String itemQuantity=orderObj.getQuantity();
                final int orderSerialId=orderObj.getSerialId();


                RelativeLayout ll = new RelativeLayout(this);
                ll.setId(orderSerialId+99);
                RelativeLayout.LayoutParams rlay = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlay.setMargins(5, 10, 5, 5);
                lout.addView(ll);
                ll.setBackgroundResource(R.drawable.kj_line);

                ImageView icon=new ImageView(this);
                icon.setId(orderSerialId+1);
                icon.setImageResource(R.drawable.order_cross);
                RelativeLayout.LayoutParams iocnLO = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //		 iocnLO.width=100;
                //		 iocnLO.height=60;
                iocnLO.addRule(RelativeLayout.CENTER_VERTICAL);
                iocnLO.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                ll.addView(icon, iocnLO);

                TextView title=new TextView(this);
                title.setId(orderSerialId+2);
                title.setText(itemQuantity+" X "+itemName);
                title.setTextColor(Color.WHITE);
                title.setTextSize(18);
                title.setLines(1);
                RelativeLayout.LayoutParams titleLO = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                titleLO.setMargins(10, 5, 20, 5);
                titleLO.addRule(RelativeLayout.CENTER_VERTICAL);
                titleLO.addRule(RelativeLayout.RIGHT_OF,icon.getId());
                ll.addView(title, titleLO);


                TextView price=new TextView(this);
                price.setId(orderSerialId+3);
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

                        deleteFromOrder(orderSerialId);

                    }
                });
            }

        }///////////////// END PIZZZAAA

        // //////////////////////////////////////////////////////////////
        // //// DRINKS

        if (drinksList!= null && drinksList.size() > 0) {
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);

            TextView tv = new TextView(this);
            tv.setText("Drinks");
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(27);

            LinearLayout.LayoutParams lpYello = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lpYello);
            tv.setPadding(15, 5, 5, 5);

            tv.setBackgroundResource(R.drawable.sides_yellow);
            lout.addView(tv);

            // for
            for (Order orderObj : drinksList) {

                String itemName = orderObj.getDisplayName();
                String itemPrice = "$" + orderObj.getPrice();
                String itemCalaroies = orderObj.getCaloriesQty() + "kj";
                String itemQuantity = orderObj.getQuantity();
                final int orderSerialId = orderObj.getSerialId();

                RelativeLayout ll = new RelativeLayout(this);
                ll.setId(orderSerialId + 99);
                RelativeLayout.LayoutParams rlay = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlay.setMargins(5, 10, 5, 5);
                lout.addView(ll);
                ll.setBackgroundResource(R.drawable.kj_line);

                ImageView icon = new ImageView(this);
                icon.setId(orderSerialId + 1);
                icon.setImageResource(R.drawable.order_cross);
                RelativeLayout.LayoutParams iocnLO = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                // iocnLO.width=100;
                // iocnLO.height=60;
                iocnLO.addRule(RelativeLayout.CENTER_VERTICAL);
                iocnLO.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                ll.addView(icon, iocnLO);

                TextView title = new TextView(this);
                title.setId(orderSerialId + 2);
                title.setText(itemQuantity + " X " + itemName);
                title.setTextColor(Color.WHITE);
                title.setTextSize(18);
                title.setLines(1);
                RelativeLayout.LayoutParams titleLO = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                titleLO.setMargins(10, 5, 20, 5);
                titleLO.addRule(RelativeLayout.CENTER_VERTICAL);
                titleLO.addRule(RelativeLayout.RIGHT_OF, icon.getId());
                ll.addView(title, titleLO);

                TextView price = new TextView(this);
                price.setId(orderSerialId + 3);
                price.setText(itemPrice);
                price.setTextColor(Color.WHITE);
                price.setTextSize(14);
                price.setLines(1);
                RelativeLayout.LayoutParams priceLO = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                priceLO.setMargins(5, 5, 15, 5);
                priceLO.addRule(RelativeLayout.CENTER_VERTICAL);
                priceLO.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                ll.addView(price, priceLO);
                titleLO.addRule(RelativeLayout.LEFT_OF , price.getId());

                icon.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        deleteFromOrder(orderSerialId);

                    }
                });
            }

        }// /////////////// END DRINKS


        // //// PASTA

        if (pastaList!= null && pastaList.size() > 0) {
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);

            TextView tv = new TextView(this);
            tv.setText("Pasta");
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(27);

            LinearLayout.LayoutParams lpYello = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lpYello);
            tv.setPadding(15, 5, 5, 5);

            tv.setBackgroundResource(R.drawable.sides_yellow);
            lout.addView(tv);

            // for
            for (Order orderObj : pastaList) {

                String itemName = orderObj.getDisplayName();
                String itemPrice = "$" + orderObj.getPrice();
                String itemCalaroies = orderObj.getCaloriesQty() + "kj";
                String itemQuantity = orderObj.getQuantity();
                final int orderSerialId = orderObj.getSerialId();

                RelativeLayout ll = new RelativeLayout(this);
                ll.setId(orderSerialId + 99);
                RelativeLayout.LayoutParams rlay = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlay.setMargins(5, 10, 5, 5);
                lout.addView(ll);
                ll.setBackgroundResource(R.drawable.kj_line);

                ImageView icon = new ImageView(this);
                icon.setId(orderSerialId + 1);
                icon.setImageResource(R.drawable.order_cross);
                RelativeLayout.LayoutParams iocnLO = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                // iocnLO.width=100;
                // iocnLO.height=60;
                iocnLO.addRule(RelativeLayout.CENTER_VERTICAL);
                iocnLO.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                ll.addView(icon, iocnLO);

                TextView title = new TextView(this);
                title.setId(orderSerialId + 2);
                title.setText(itemQuantity + " X " + itemName);
                title.setTextColor(Color.WHITE);
                title.setTextSize(18);
                title.setLines(1);
                RelativeLayout.LayoutParams titleLO = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                titleLO.setMargins(10, 5, 20, 5);
                titleLO.addRule(RelativeLayout.CENTER_VERTICAL);
                titleLO.addRule(RelativeLayout.RIGHT_OF, icon.getId());
                ll.addView(title, titleLO);

                TextView price = new TextView(this);
                price.setId(orderSerialId + 3);
                price.setText(itemPrice);
                price.setTextColor(Color.WHITE);
                price.setTextSize(14);
                price.setLines(1);
                RelativeLayout.LayoutParams priceLO = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                priceLO.setMargins(5, 5, 15, 5);
                priceLO.addRule(RelativeLayout.CENTER_VERTICAL);
                priceLO.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                ll.addView(price, priceLO);
                titleLO.addRule(RelativeLayout.LEFT_OF , price.getId());

                icon.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        deleteFromOrder(orderSerialId);

                    }
                });
            }

        }// /////////////// END Pasta

        // //// SIDES

        if (sidesList!= null && sidesList.size() > 0) {
            LinearLayout lout = (LinearLayout) findViewById(R.id.wraperLayout);

            TextView tv = new TextView(this);
            tv.setText("Sides");
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(27);

            LinearLayout.LayoutParams lpYello = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lpYello);
            tv.setPadding(15, 5, 5, 5);

            tv.setBackgroundResource(R.drawable.sides_yellow);
            lout.addView(tv);

            // for
            for (Order orderObj : sidesList) {

                String itemName = orderObj.getDisplayName();
                String itemPrice = "$" + orderObj.getPrice();
                String itemCalaroies = orderObj.getCaloriesQty() + "kj";
                String itemQuantity = orderObj.getQuantity();
                final int orderSerialId = orderObj.getSerialId();

                RelativeLayout ll = new RelativeLayout(this);
                ll.setId(orderSerialId + 99);
                RelativeLayout.LayoutParams rlay = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlay.setMargins(5, 10, 5, 5);
                lout.addView(ll);
                ll.setBackgroundResource(R.drawable.kj_line);

                ImageView icon = new ImageView(this);
                icon.setId(orderSerialId + 1);
                icon.setImageResource(R.drawable.order_cross);
                RelativeLayout.LayoutParams iocnLO = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                // iocnLO.width=100;
                // iocnLO.height=60;
                iocnLO.addRule(RelativeLayout.CENTER_VERTICAL);
                iocnLO.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                ll.addView(icon, iocnLO);

                TextView title = new TextView(this);
                title.setId(orderSerialId + 2);
                title.setText(itemQuantity + " X " + itemName);
                title.setTextColor(Color.WHITE);
                title.setTextSize(18);
                title.setLines(1);
                RelativeLayout.LayoutParams titleLO = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                titleLO.setMargins(10, 5, 20, 5);
                titleLO.addRule(RelativeLayout.CENTER_VERTICAL);
                titleLO.addRule(RelativeLayout.RIGHT_OF, icon.getId());
                ll.addView(title, titleLO);

                TextView price = new TextView(this);
                price.setId(orderSerialId + 3);
                price.setText(itemPrice);
                price.setTextColor(Color.WHITE);
                price.setTextSize(14);
                price.setLines(1);
                RelativeLayout.LayoutParams priceLO = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                priceLO.setMargins(5, 5, 15, 5);
                priceLO.addRule(RelativeLayout.CENTER_VERTICAL);
                priceLO.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                ll.addView(price, priceLO);
                titleLO.addRule(RelativeLayout.LEFT_OF , price.getId());

                icon.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        deleteFromOrder(orderSerialId);

                    }
                });
            }

        }// /////////////// END Sides

        //deal ...........................................................
        if(dealOrderVos!=null && dealOrderVos.size()>0)
        {
            LinearLayout lout=(LinearLayout)findViewById(R.id.wraperLayout);

            TextView tv=new TextView(this);
            tv.setText("Deal");
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(27);

            LinearLayout.LayoutParams lpYello = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lpYello);
            tv.setPadding(15, 5, 5, 5);


            tv.setBackgroundResource(R.drawable.sides_yellow);
            lout.addView(tv);

            //for
            for (DealOrder orderObj : dealOrderVos) {

                String itemName=orderObj.getDisplayName();
                String itemPrice="$"+orderObj.getDiscountedPrice();
                String itemCalaroies=orderObj.getQuantity()+"kj";
                String itemQuantity=orderObj.getQuantity();
                final int orderSerialId=orderObj.getSerialId();


                RelativeLayout ll = new RelativeLayout(this);
                ll.setId(orderSerialId+99);
                RelativeLayout.LayoutParams rlay = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlay.setMargins(5, 10, 5, 5);
                lout.addView(ll);
                ll.setBackgroundResource(R.drawable.kj_line);

                ImageView icon=new ImageView(this);
                icon.setId(orderSerialId+1);
                icon.setImageResource(R.drawable.order_cross);
                RelativeLayout.LayoutParams iocnLO = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //		 iocnLO.width=100;
                //		 iocnLO.height=60;
                iocnLO.addRule(RelativeLayout.CENTER_VERTICAL);
                iocnLO.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                ll.addView(icon, iocnLO);

                TextView title=new TextView(this);
                title.setId(orderSerialId+2);
                title.setText(itemQuantity+" X "+itemName);
                title.setTextColor(Color.WHITE);
                title.setTextSize(18);
                title.setLines(1);
                RelativeLayout.LayoutParams titleLO = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                titleLO.setMargins(10, 5, 20, 5);
                titleLO.addRule(RelativeLayout.CENTER_VERTICAL);
                titleLO.addRule(RelativeLayout.RIGHT_OF,icon.getId());
                ll.addView(title, titleLO);


                TextView price=new TextView(this);
                price.setId(orderSerialId+3);
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
                        Toast.makeText(MyOrderActivity.this, "cannot remove a deal", Toast.LENGTH_LONG).show();
                        // deleteFromDealOrder(orderSerialId);

                    }
                });
            }
        }


    }
    @Override
    protected void onResume(){
        super.onResume();
        if(AppSharedPreference.getBoolean(MyOrderActivity.this, "okLogin")){
            AppSharedPreference.putBoolean(MyOrderActivity.this, "okLogin", false);
            String orderType=AppSharedPreference.getData(MyOrderActivity.this, "orderType", null);
            if("Delivery".equalsIgnoreCase(orderType)) {
                Intent i=new Intent(MyOrderActivity.this,LocationFromHistoryActivity.class);
                startActivity(i);
                finish();
            } else {

            }
        }
    }
    private void deleteFromOrder(int serialId){

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
        dbInstance.open();
        dbInstance.deleteOrderRec(serialId);
        ArrayList<DealOrder>dealOrderVos1= dbInstance.getDealOrdersList();
        double tota=0.00;
        for (int x=0;x<dealOrderVos1.size();x++){
            tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
        }
        ArrayList<String> orderInfo = dbInstance.getOrderInfo();
        if(null!=orderInfo && orderInfo.size()==2)
        {
            double temp=AppProperties.getRoundTwoDecimalString(orderInfo.get(1)) ;
            temp=temp+tota;
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            temp= Double.valueOf(twoDForm.format(temp));
            totalPrice.setText("TOTAL: $"+temp);
        }

        RelativeLayout ll = (RelativeLayout)findViewById(serialId + 99);
        ll.setVisibility(View.GONE);
        dbInstance.close();

//        AppDao dao=null;
//        try {
//            dao=AppDao.getSingleton(getApplicationContext());
//            dao.openConnection();
//
//            dao.deleteOrderRec(serialId);
//            ArrayList<DealOrder>dealOrderVos1= dao.getDealOrdersList();
//            double tota=0.00;
//            for (int x=0;x<dealOrderVos1.size();x++){
//                tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
//            }
//            ArrayList<String> orderInfo = dao.getOrderInfo();
//            if(null!=orderInfo && orderInfo.size()==2)
//            {
//                double temp=AppProperties.getRoundTwoDecimalString(orderInfo.get(1)) ;
//                temp=temp+tota;
//                DecimalFormat twoDForm = new DecimalFormat("#.##");
//                temp= Double.valueOf(twoDForm.format(temp));
//                totalPrice.setText("TOTAL: $"+temp);
//            }
//
//            RelativeLayout ll = (RelativeLayout)findViewById(serialId + 99);
//            ll.setVisibility(View.GONE);
//
//        } catch (Exception ex)
//        {
//            System.out.println(ex.getMessage());
//        }finally{
//            if(null!=dao)
//                dao.closeConnection();
//        }
    }
    private void deleteFromDealOrder(int serialId){
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MyOrderActivity.this);
        dbInstance.open();
        dbInstance.deleteDealOrderRec(serialId);
        ArrayList<String> orderInfo = dbInstance.getOrderInfo();
        double temp=AppProperties.getRoundTwoDecimalString(orderInfo.get(1));
        ArrayList<DealOrder>dealOrderVos1= dbInstance.getDealOrdersList();
        double tota=0.00;
        for (int x=0;x<dealOrderVos1.size();x++){
            tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
        }
        if(null!=dealOrderVos1 && dealOrderVos1.size()==2)
        {

            temp=temp+tota;

            totalPrice.setText("TOTAL: $"+temp);
        }

        RelativeLayout ll = (RelativeLayout)findViewById(serialId + 99);
        ll.setVisibility(View.GONE);
        dbInstance.close();

//        AppDao dao=null;
//        try {
//            dao=AppDao.getSingleton(getApplicationContext());
//            dao.openConnection();
//
//            dao.deleteDealOrderRec(serialId);
//            ArrayList<String> orderInfo = dao.getOrderInfo();
//            double temp=AppProperties.getRoundTwoDecimalString(orderInfo.get(1));
//            ArrayList<DealOrder>dealOrderVos1= dao.getDealOrdersList();
//            double tota=0.00;
//            for (int x=0;x<dealOrderVos1.size();x++){
//                tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
//            }
//            if(null!=dealOrderVos1 && dealOrderVos1.size()==2)
//            {
//
//                temp=temp+tota;
//
//                totalPrice.setText("TOTAL: $"+temp);
//            }
//
//            RelativeLayout ll = (RelativeLayout)findViewById(serialId + 99);
//            ll.setVisibility(View.GONE);
//
//        } catch (Exception ex)
//        {
//            System.out.println(ex.getMessage());
//        }finally{
//            if(null!=dao)
//                dao.closeConnection();
//        }
    }
}
