package com.deepslice.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.DealOrder;
import com.deepslice.model.Favourite;
import com.deepslice.model.Order;
import com.deepslice.model.Product;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.Utils;

public class FavAddActivity extends Activity {
    
    TextView favCountTxt;
    int currentCount=1;
    Product prodBean;
    
    EditText editView;
    TextView tvItemsPrice, tvFavCount;
    
    String catType,couponGroupID,productId="";
    boolean isDeal=false;
    DealOrder selectedDealOrder;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_add);
        
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle b = this.getIntent().getExtras();
        String itemName=b.getString("itemName");
        catType=b.getString("catType");
        prodBean=(Product)b.getSerializable("prodBean");
        
        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);
        
        Button buttonAddOrders= (Button)findViewById(R.id.buttonAddOrders);
        isDeal=b.getBoolean("isDeal",false);
        if (isDeal){
            selectedDealOrder=(DealOrder)b.getSerializable("dealData");
            couponGroupID=b.getString("couponGroupID");
            productId=selectedDealOrder.getProdID();
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

            }


        });


        buttonAddOrders.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                DeepsliceDatabase dbInstance = new DeepsliceDatabase(FavAddActivity.this);
                dbInstance.open();
                if (isDeal){
                    selectedDealOrder.setQuantity(String.valueOf(currentCount));
                    if(dbInstance.isDealGroupAlreadySelected(selectedDealOrder.getCouponID(), selectedDealOrder.getCouponGroupID())){
                        dbInstance.deleteAlreadySelectedDealGroup(selectedDealOrder.getCouponID(), selectedDealOrder.getCouponGroupID());
                    }
                    dbInstance.insertDealOrder(selectedDealOrder);

                    finish();
                }else {
//                    dbInstance.insertOrder(getOrderBean());
                    Toast.makeText(FavAddActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
                    finish();
                }
                dbInstance.close();
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    private Favourite getFavBean() {

        Favourite f = new Favourite();
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
        super.onResume();
        doOnResumeWork();
    }

    private void doOnResumeWork() {
        List<String> orderInfo = Utils.OrderInfo(FavAddActivity.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
        
        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }

        
        String favCount = Utils.FavCount(FavAddActivity.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }
    }
}
