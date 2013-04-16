package com.deepslice.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deepslice.database.AppDao;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.vo.LocationDetails;
import com.deepslice.vo.OrderVo;

public class MyOrderActivity extends Activity{
	
	ArrayList<OrderVo> pizzaList;
	ArrayList<OrderVo> drinksList;
	ArrayList<OrderVo> sidesList;
	ArrayList<OrderVo> pastaList;
	
	TextView totalPrice;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_order);
		
		totalPrice=(TextView)findViewById(R.id.textTextView12);
/////////////////		
		TextView moDeliveryStore=(TextView)findViewById(R.id.moDeliveryStore);
		TextView moDeliveryAddress=(TextView)findViewById(R.id.moDeliveryAddress);
		
		LocationDetails locationObj = AppProperties.getLocationObj(MyOrderActivity.this);
		
		moDeliveryStore.setText("Delivery Store: "+AppProperties.NVL(locationObj.getLocName()));
		moDeliveryAddress.setText(AppProperties.NVL(locationObj.getLocAddress())+" "+AppProperties.NVL(locationObj.getLocName())+", "+AppProperties.NVL(locationObj.getLocPhones()));
///////////////////////////		
		TextView moDeliveryTime=(TextView)findViewById(R.id.moDeliveryTime);
		TextView moYourDetails=(TextView)findViewById(R.id.moYourDetails);
		
		moDeliveryTime.setText(AppSharedPreference.getData(MyOrderActivity.this, "deliveryTime", ""));
		String dets=AppSharedPreference.getData(MyOrderActivity.this, "customerName", "");
		dets+=", ";
		dets+=AppSharedPreference.getData(MyOrderActivity.this, "customerEmail", "");
		dets+=", ";
		dets+=AppSharedPreference.getData(MyOrderActivity.this, "customerPhone", "");
		
		if(dets.length()>5)
			moYourDetails.setText(dets);
		else
			moYourDetails.setText("");
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
				Intent i=new Intent(MyOrderActivity.this,PaymentSelectionActivity.class);
				startActivity(i);
//				finish();
			}
		});

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
		
		AppDao dao=null;
		try {
			dao=AppDao.getSingleton(getApplicationContext());
			dao.openConnection();
		
			pizzaList = dao.getOrdersListWithType("Pizza");
			drinksList = dao.getOrdersListWithType("Drinks");
			sidesList = dao.getOrdersListWithType("Sides");
			pastaList = dao.getOrdersListWithType("Pasta");
			
			ArrayList<String> orderInfo = dao.getOrderInfo();
			
			if(null!=orderInfo && orderInfo.size()==2)
			{
				totalPrice.setText("TOTAL: $"+AppProperties.getRoundTwoDecimalString(orderInfo.get(1)));
			}

		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}finally{
			if(null!=dao)
				dao.closeConnection();
		}
		
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
			for (OrderVo orderObj : pizzaList) {
				
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
			for (OrderVo orderObj : drinksList) {

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
					for (OrderVo orderObj : pastaList) {

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
					for (OrderVo orderObj : sidesList) {

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

	}
	private void deleteFromOrder(int serialId){
		
		AppDao dao=null;
		try {
			dao=AppDao.getSingleton(getApplicationContext());
			dao.openConnection();
		
			dao.deleteOrderRec(serialId);
			
			ArrayList<String> orderInfo = dao.getOrderInfo();
			if(null!=orderInfo && orderInfo.size()==2)
			{
				totalPrice.setText("TOTAL: $"+AppProperties.getRoundTwoDecimalString(orderInfo.get(1)));
			}
			
			RelativeLayout ll = (RelativeLayout)findViewById(serialId + 99);
			ll.setVisibility(View.GONE);
			
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}finally{
			if(null!=dao)
				dao.closeConnection();
		}		
	}

}
