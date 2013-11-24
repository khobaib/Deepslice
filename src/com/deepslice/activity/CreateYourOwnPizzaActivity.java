package com.deepslice.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.adapter.PizzaTypeMenuAdapter;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.CreateOwnPizzaData;
import com.deepslice.model.DealOrder;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;
import com.deepslice.utilities.Utils;

public class CreateYourOwnPizzaActivity extends Activity {

    ListView pizzaTypeList;             // chicken/veggie...
    PizzaTypeMenuAdapter pizzaTypeMenuAdapter;
    String crustName;

    DeepsliceApplication appInstance;
    List<CreateOwnPizzaData> pizzaArray;
    List<CreateOwnPizzaData> crustSpecificPizzaArray;
    
    TextView tvItemsPrice, tvFavCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(this, "92b170cf");
        
        setContentView(R.layout.sub_menu_pizza3);
        
        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);
        
        findViewById(R.id.imageButtonCreateUown).setVisibility(View.GONE);
        findViewById(R.id.imageButtonHalf).setVisibility(View.GONE);
        findViewById(R.id.favCount).setVisibility(View.GONE);
        findViewById(R.id.itemPrice).setVisibility(View.GONE);

        crustName = getIntent().getExtras().getString("selected_crust");

        appInstance = (DeepsliceApplication) getApplication();
        pizzaArray = appInstance.getCreatePizzaDataList();
        
        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(CreateYourOwnPizzaActivity.this, FavoriteListActivity.class);
                startActivity(intent);

            }
        });        
        
        
        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(CreateYourOwnPizzaActivity.this, MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        
        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(CreateYourOwnPizzaActivity.this, MyOrderActivity.class);
                startActivity(intent);

            }
        });
        
        

        int numPizza = pizzaArray.size();
        crustSpecificPizzaArray = new ArrayList<CreateOwnPizzaData>();
        for(int pizzaIndex = 0; pizzaIndex < numPizza; pizzaIndex++){
            if(pizzaArray.get(pizzaIndex).getSubCatCode().equals(crustName)){
                crustSpecificPizzaArray.add(pizzaArray.get(pizzaIndex));
            }
        }

        pizzaTypeList = (ListView) findViewById(R.id.listView1);
        pizzaTypeList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                CreateOwnPizzaData selectedPizzaData = (CreateOwnPizzaData) parent.getItemAtPosition(position);
                
                Intent i=new Intent(CreateYourOwnPizzaActivity.this, CreateYourOwnPizzaDetails.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("selected_pizza", selectedPizzaData);
                i.putExtras(bundle);
                startActivity(i);

            }
        });

        pizzaTypeMenuAdapter = new PizzaTypeMenuAdapter(CreateYourOwnPizzaActivity.this, crustSpecificPizzaArray);
        pizzaTypeList.setAdapter(pizzaTypeMenuAdapter);
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
    
    
    
    @Override
    protected void onResume() {
        super.onResume();

        List<String> orderInfo = Utils.OrderInfo(CreateYourOwnPizzaActivity.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
        
        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }

        
        String favCount = Utils.FavCount(CreateYourOwnPizzaActivity.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }
    }

}
