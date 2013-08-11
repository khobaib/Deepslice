package com.deepslice.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.DealOrder;
import com.deepslice.utilities.AppProperties;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 6/4/13
 * Time: 11:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class HalfAndHalf extends Activity {

    ImageView TickFirstHalf;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.half_and_half);

        AppProperties.isFirstPizzaChosen = false;
        
        TickFirstHalf = (ImageView) findViewById(R.id.iv_tick_1st_half);

        final Intent intent = new Intent(this,PizzaSubMenuActivity.class);

        Button buttonStHalf=(Button)findViewById(R.id.buttonStHalf);
        buttonStHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putBoolean("isHalf",true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Button buttonNdHalf=(Button)findViewById(R.id.buttonNdHalf);
        buttonNdHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppProperties.isFirstPizzaChosen){
                    Bundle bundle=new Bundle();
                    bundle.putBoolean("isHalf",true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(HalfAndHalf.this, "Please choose first half", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HalfAndHalf.this, FavsListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HalfAndHalf.this, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HalfAndHalf.this, MyOrderActivity.class);
                startActivity(intent);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        
        if(AppProperties.isFirstPizzaChosen)
            TickFirstHalf.setVisibility(View.VISIBLE);
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(HalfAndHalf.this);
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


    }
}