package com.deepslice.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.database.AppDao;
import com.deepslice.utilities.AppProperties;
import com.deepslice.vo.AllProductsVo;
import com.deepslice.vo.FavouritesVo;
import com.deepslice.vo.OrderVo;

public class FavAddActivity extends Activity {
	TextView favCountTxt;
	int currentCount=1;
	AllProductsVo prodBean;
	EditText editView;
	String catType;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fav_add);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Bundle b = this.getIntent().getExtras();
		String itemName=b.getString("itemName");
		catType=b.getString("catType");
		prodBean=(AllProductsVo)b.getSerializable("prodBean");
		
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
				
				AppDao dao=null;
				try {
					dao=AppDao.getSingleton(getApplicationContext());
					dao.openConnection();
					
					boolean favAdded=dao.favAlreadyAdded(prodBean.getProdID(),editView.getText().toString());
					if(favAdded)
					{
						Toast.makeText(FavAddActivity.this, "Already added to Favourites", Toast.LENGTH_LONG).show();
					}
					else
					{
						dao.insertFav(getFavBean());
						doOnResumeWork();
						Toast.makeText(FavAddActivity.this, "Successfully added to Favourites", Toast.LENGTH_LONG).show();
					}
					
					
				} catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}finally{
					if(null!=dao)
						dao.closeConnection();
				}

			}


		});
		
		Button buttonAddOrders= (Button)findViewById(R.id.buttonAddOrders);
		buttonAddOrders.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				AppDao dao=null;
				try {
					dao=AppDao.getSingleton(getApplicationContext());
					dao.openConnection();
					
						dao.insertOrder(getOrderBean());
						Toast.makeText(FavAddActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
						finish();
				} catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}finally{
					if(null!=dao)
						dao.closeConnection();
				}
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

	private FavouritesVo getFavBean() {
		
		FavouritesVo f = new FavouritesVo();
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
private OrderVo getOrderBean() {
		
	OrderVo f = new OrderVo();
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
	AppDao dao = null;
		try {
			dao = AppDao.getSingleton(getApplicationContext());
			dao.openConnection();

			ArrayList<String> orderInfo = dao.getOrderInfo();

			TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
			if (null != orderInfo && orderInfo.size() == 2) {
				itemsPrice.setText(orderInfo.get(0)+" Items "+"\n$" + orderInfo.get(1));
				itemsPrice.setVisibility(View.VISIBLE);
			} else {
				itemsPrice.setVisibility(View.INVISIBLE);

			}

			TextView favCount = (TextView) findViewById(R.id.favCount);
			String fvs = dao.getFavCount();
			if (null != fvs && !fvs.equals("0")) {
				favCount.setText(fvs);
				favCount.setVisibility(View.VISIBLE);
			} else {
				favCount.setVisibility(View.INVISIBLE);
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (null != dao)
				dao.closeConnection();
		}
	
}

}
