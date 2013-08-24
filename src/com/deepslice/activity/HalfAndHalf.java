package com.deepslice.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 6/4/13
 * Time: 11:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class HalfAndHalf extends Activity {

    ImageView TickFirstHalf;
    TextView tvItemsPrice, tvFavCount;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.half_and_half);

        AppProperties.isFirstPizzaChosen = false;
        
        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);
        
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
        
        List<String> orderInfo = Utils.OrderInfo(HalfAndHalf.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
        
        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }

        
        String favCount = Utils.FavCount(HalfAndHalf.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }


    }
}