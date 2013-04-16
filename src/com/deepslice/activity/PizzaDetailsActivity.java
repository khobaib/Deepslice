package com.deepslice.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.database.AppDao;
import com.deepslice.utilities.AppProperties;
import com.deepslice.vo.AllProductsVo;
import com.deepslice.vo.FavouritesVo;
import com.deepslice.vo.LabelValueBean;
import com.deepslice.vo.OrderVo;
import com.deepslice.vo.SubCategoryVo;
import com.deepslice.vo.ToppingsAndSaucesVo;
import com.deepslice.vo.ToppingsHashmapVo;

public class PizzaDetailsActivity extends Activity{

	TextView favCountTxt;
	int currentCount=1;
	AllProductsVo selectedBean;
	
	Spinner spinnerCat;

	int SELECT_TOPPINGS=112233;
	int SELECT_CRUST=112244;
	
	String crustName="", crustCatId="", crustSubCatId="";
	
	TextView selectedToppings,selectedSauces,selectedCrusts;
	HashMap<String, ToppingsHashmapVo> toppingsSelected;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pizza_details);
		
		Bundle b = this.getIntent().getExtras();

		selectedBean=(AllProductsVo)b.getSerializable("selectedProduct");
		
		
		TextView pDesc=(TextView)findViewById(R.id.pDesc);
		pDesc.setText(selectedBean.getProdDesc());
		
		TextView pKJ=(TextView)findViewById(R.id.pKJ);
		pKJ.setText(selectedBean.getCaloriesQty()+"kj");
		
		selectedToppings=(TextView)findViewById(R.id.selectedToppings);
		selectedSauces=(TextView)findViewById(R.id.selectedSauces);
		selectedCrusts=(TextView)findViewById(R.id.selectedCrust);
		selectedCrusts.setText("");
		
		favCountTxt=(TextView)findViewById(R.id.qVal);
		Button favArrowDown= (Button)findViewById(R.id.buttonMinus);
		favArrowDown.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(currentCount>1)
				{
					currentCount--;
					favCountTxt.setText(currentCount+"");
				}
			}
		});
		Button favArrowUp= (Button)findViewById(R.id.buttonPlus);
		favArrowUp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(currentCount<10)
				{
					currentCount++;
					favCountTxt.setText(currentCount+"");
				}
			}
		});
		
		LinearLayout ltToppings= (LinearLayout)findViewById(R.id.ltToppings);
		ltToppings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent i=new Intent(PizzaDetailsActivity.this,PizzaToppingsActivity.class);
				Bundle bundle=new Bundle();
				AppProperties.selectedToppings=toppingsSelected;
				bundle.putSerializable("selectedProduct",selectedBean);
				i.putExtras(bundle);
				startActivityForResult(i, SELECT_TOPPINGS);

				
			}
		});
		
		LinearLayout ltCrusts= (LinearLayout)findViewById(R.id.ltCrusts);
		ltCrusts.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent i=new Intent(PizzaDetailsActivity.this,PizzaCrustActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("catId", crustCatId);
				bundle.putString("subCatId", crustSubCatId);
				bundle.putSerializable("selectedProduct",selectedBean);
				i.putExtras(bundle);
				startActivityForResult(i, SELECT_CRUST);

				
			}
		});
		
		spinnerCat=(Spinner)findViewById(R.id.spinner1);
		populateCategoryData();
		
		LinearLayout ltSauces= (LinearLayout)findViewById(R.id.ltSauces);
		ltSauces.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				spinnerCat.performClick();

				
			}
		});
		
		spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				LabelValueBean lvl=(LabelValueBean)spinnerCat.getItemAtPosition(arg2);
    	    	selectedSauces.setText(lvl.getLabel());			
    	    	}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
		Button openFavs=(Button)findViewById(R.id.favs);
		openFavs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(PizzaDetailsActivity.this,FavsListActivity.class);
				startActivity(intent);
				
			}
		});
		
		Button mainMenu=(Button)findViewById(R.id.mainMenu);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(PizzaDetailsActivity.this,MenuActivity.class);
				startActivity(intent);
				
			}
		});
		
		Button buttonAddFav= (Button)findViewById(R.id.buttonAddFav);
		buttonAddFav.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				AppDao dao=null;
				try {
					dao=AppDao.getSingleton(getApplicationContext());
					dao.openConnection();
					
					boolean favAdded=dao.favAlreadyAdded(selectedBean.getProdID(),selectedBean.getDisplayName());
					if(favAdded)
					{
						Toast.makeText(PizzaDetailsActivity.this, "Already added to Favourites", Toast.LENGTH_LONG).show();
					}
					else
					{
						dao.insertFav(getFavBean());
						doResumeWork();
						Toast.makeText(PizzaDetailsActivity.this, "Successfully added to Favourites", Toast.LENGTH_LONG).show();
					}
					
					
				} catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}finally{
					if(null!=dao)
						dao.closeConnection();
				}
	}
			
			
			private FavouritesVo getFavBean() {
				
				FavouritesVo f = new FavouritesVo();
            	f.setProdCatID(selectedBean.getProdCatID());
            	f.setSubCatID1(selectedBean.getSubCatID1());
            	f.setSubCatID2(selectedBean.getSubCatID2());
                f.setProdID(selectedBean.getProdID());
                f.setProdCode(selectedBean.getProdCode());
                f.setDisplayName(selectedBean.getDisplayName());
                f.setProdAbbr(selectedBean.getProdAbbr());
                f.setProdDesc(selectedBean.getProdDesc());
                f.setIsAddDeliveryAmount(selectedBean.getIsAddDeliveryAmount());
                f.setDisplaySequence(selectedBean.getDisplaySequence());
                f.setCaloriesQty(selectedBean.getCaloriesQty());
                f.setPrice(selectedBean.getPrice());
                f.setThumbnail(selectedBean.getThumbnail());
                f.setFullImage(selectedBean.getFullImage());
                f.setCustomName(selectedBean.getDisplayName());
                f.setProdCatName("Pizza");
                return f;
			}
		});
		
		//populating default crust
		 AppDao dao=null;
			try {
				dao=AppDao.getSingleton(getApplicationContext());
				dao.openConnection();
				
				ArrayList<SubCategoryVo> crustList = dao.getPizzaCrusts(selectedBean.getProdCatID(),selectedBean.getSubCatID1());
				if(crustList != null && crustList.size()>0 )
				{
				SubCategoryVo crLocal = crustList.get(0);
				crustName=crLocal.getSubCatDesc();
				crustCatId=crLocal.getProdCatID();
				crustSubCatId=crLocal.getSubCatID();
				
				selectedCrusts.setText(crustName);
				}
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}finally{
				if(null!=dao)
					dao.closeConnection();
			}
	////////////////////
			
			// populating default toppings
			dao=null;
			try {
				dao=AppDao.getSingleton(getApplicationContext());
				dao.openConnection();
				toppingsSelected=new HashMap<String, ToppingsHashmapVo>();
				ArrayList<ToppingsAndSaucesVo> toppingsList = dao.getPizzaToppings(selectedBean.getProdID());
				for (ToppingsAndSaucesVo toppingsAndSaucesVo : toppingsList) {
					if("True".equalsIgnoreCase(toppingsAndSaucesVo.getIsFreeWithPizza()))
						toppingsSelected.put(toppingsAndSaucesVo.getToppingID(), getHashBean(toppingsAndSaucesVo,"Single"));
				}
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}finally{
				if(null!=dao)
					dao.closeConnection();
			}
		
			String tempList="";
			Iterator it = toppingsSelected.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        ToppingsHashmapVo gm=(ToppingsHashmapVo)pairs.getValue();
		        tempList+=gm.getToppingCode()+",";
		    }
			
		    selectedToppings.setText(AppProperties.trimLastComma(tempList));
		    
		    
		
		
		Button buttonAddOrders= (Button)findViewById(R.id.buttonAddOrders);
		buttonAddOrders.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				OrderVo tempOrderBean = getOrderBean();
				AppDao dao=null;
				try {
					dao=AppDao.getSingleton(getApplicationContext());
					dao.openConnection();
					
						dao.insertOrder(tempOrderBean);
						Toast.makeText(PizzaDetailsActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
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
				
				Intent intent=new Intent(PizzaDetailsActivity.this,MyOrderActivity.class);
				startActivity(intent);
				
			}
		});
	}
	private ToppingsHashmapVo getHashBean(
			ToppingsAndSaucesVo toppingsAndSaucesVo, String toppingSize) {

		ToppingsHashmapVo hMap=new ToppingsHashmapVo();
		
		hMap.setToppingCode(toppingsAndSaucesVo.getToppingCode());
		hMap.setToppingDesc(toppingsAndSaucesVo.getToppingDesc());
		hMap.setToppingID(toppingsAndSaucesVo.getToppingID());
		hMap.setToppingPrice(toppingsAndSaucesVo.getOwnPrice());
		hMap.setToppingSize(toppingSize);
		
		return hMap;
	}
	private void populateCategoryData(){
		ArrayList<ToppingsAndSaucesVo> saucesList=null;
		AppDao dao=null;
		try {
			dao=AppDao.getSingleton(getApplicationContext());
			dao.openConnection();
			
			saucesList=dao.getPizzaSauces(selectedBean.getProdID());
			
		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}finally{
			if(null!=dao)
				dao.closeConnection();
		}
		
		 try{
			 
		int i=0;
		LabelValueBean[] albums= new LabelValueBean[saucesList.size()];
		for (ToppingsAndSaucesVo sBean : saucesList) {
			albums[i++]=new LabelValueBean(sBean.getToppingCode(), sBean.getToppingID() ) ;
		}
		
       ArrayAdapter<Object> spinnerArrayAdapter = new ArrayAdapter<Object>(this,
               android.R.layout.simple_spinner_item, albums);
       spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       spinnerCat.setAdapter(spinnerArrayAdapter);  
		 
       
    	    
		}catch (Exception e) {
       	e.printStackTrace();
		}
	}	 
	protected void onActivityResult(int requestCode, int resultCode,
	          Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
	      if (requestCode == SELECT_TOPPINGS) {
	  			String tempList="";
	  			toppingsSelected=AppProperties.selectedToppings;
	  			if(toppingsSelected==null)
	  				toppingsSelected=new HashMap<String, ToppingsHashmapVo>();

				Iterator it = toppingsSelected.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			        ToppingsHashmapVo gm=(ToppingsHashmapVo)pairs.getValue();
			        tempList+=gm.getToppingCode()+",";
			    }
				
			    selectedToppings.setText(AppProperties.trimLastComma(tempList));

	      }
	      if (requestCode == SELECT_CRUST) {
	          
	           	  String namesList = data.getStringExtra("name"); 
	           	  crustCatId = data.getStringExtra("catId"); 
	           	crustSubCatId = data.getStringExtra("subCatId");
	           	  selectedCrusts.setText(namesList);
	      }
	}
	
	private OrderVo getOrderBean() {
		
		OrderVo f = new OrderVo();
	    	f.setProdCatID(selectedBean.getProdCatID());
	    	f.setSubCatID1(selectedBean.getSubCatID1());
	    	f.setSubCatID2(selectedBean.getSubCatID2());
	        f.setProdID(selectedBean.getProdID());
	        f.setProdCode(selectedBean.getProdCode());
	        f.setDisplayName(selectedBean.getDisplayName());
	        f.setProdAbbr(selectedBean.getProdAbbr());
	        f.setProdDesc(selectedBean.getProdDesc());
	        f.setIsAddDeliveryAmount(selectedBean.getIsAddDeliveryAmount());
	        f.setDisplaySequence(selectedBean.getDisplaySequence());
	        f.setCaloriesQty(selectedBean.getCaloriesQty());
	        
	        double finalPrice=generateTotalPrice();
	        
	        finalPrice=AppProperties.roundTwoDecimals(finalPrice);
	        		
	        f.setPrice(String.valueOf(finalPrice));
	        
	        f.setThumbnail(selectedBean.getThumbnail());
	        f.setFullImage(selectedBean.getFullImage());
	        
	        f.setQuantity(String.valueOf(currentCount));
	        
	        f.setCrust("");
	        f.setSauce("");
	        f.setToppings("");
			
	        f.setProdCatName("Pizza");
	        return f;
		}

	private double generateTotalPrice() {
		
		double totalPrice=Double.parseDouble(selectedBean.getPrice());
		
		totalPrice=currentCount*totalPrice;
		double tempDoublePrice;
		AppDao dao=null;
		try {
			dao=AppDao.getSingleton(getApplicationContext());
			dao.openConnection();
			
				String tempPrice="0.00";
				Iterator it = toppingsSelected.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			        ToppingsHashmapVo gm=(ToppingsHashmapVo)pairs.getValue();
			        
			        if("False".equalsIgnoreCase(gm.getIsFreeWithPizza())){
			    			tempPrice=dao.getToppingPrice(gm.getToppingID(),gm.getToppingSize());
			        }
			        tempDoublePrice=Double.parseDouble(tempPrice);
			        totalPrice=totalPrice+tempDoublePrice;
			    }

		} catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}finally{
			if(null!=dao)
				dao.closeConnection();
		}
		
		return totalPrice;
	}
	@Override
	protected void onResume() {

		doResumeWork();
		
		super.onResume();
	}
	private void doResumeWork() {
		// //////////////////////////////////////////////////////////////////////////////
		AppDao dao = null;
		try {
			dao = AppDao.getSingleton(getApplicationContext());
			dao.openConnection();

			ArrayList<String> orderInfo = dao.getOrderInfo();
			
			TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
			if (null != orderInfo && orderInfo.size() == 2) {
				itemsPrice.setText(orderInfo.get(0)+" Items "+"\n$" + orderInfo.get(1));
				itemsPrice.setVisibility(View.VISIBLE);
			}
			else{
				itemsPrice.setVisibility(View.INVISIBLE);
				
			}
			
			TextView favCount = (TextView) findViewById(R.id.favCount);
			String fvs=dao.getFavCount();
			if (null != fvs && !fvs.equals("0")) {
				favCount.setText(fvs);
				favCount.setVisibility(View.VISIBLE);
			}
			else{
				favCount.setVisibility(View.INVISIBLE);
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			if (null != dao)
				dao.closeConnection();
		}
		// ///////////////////////////////////////////////////////////////////////

		
	}
}
