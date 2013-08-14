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

import com.deepslice.adapter.PizzaTypeMenuAdapter;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.CreateOwnPizzaData;
import com.deepslice.model.DealOrder;
import com.deepslice.utilities.DeepsliceApplication;

public class CreateYourOwnPizzaActivity extends Activity {

    ListView pizzaTypeList;             // chicken/veggie...
    PizzaTypeMenuAdapter pizzaTypeMenuAdapter;
    String crustName;

    DeepsliceApplication appInstance;
    List<CreateOwnPizzaData> pizzaArray;
    List<CreateOwnPizzaData> crustSpecificPizzaArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_menu_pizza3);
        
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

                Intent intent=new Intent(CreateYourOwnPizzaActivity.this, FavsListActivity.class);
                startActivity(intent);

            }
        });        
        
        
        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(CreateYourOwnPizzaActivity.this, MenuActivity.class);
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
    protected void onResume() {

        doResumeWork();

        super.onResume();
    }
    private void doResumeWork() {
        // //////////////////////////////////////////////////////////////////////////////
        // here we calculate total pricing of already ordered item(Deal+normal order)
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaActivity.this);
        dbInstance.open();
        ArrayList<String> orderInfo = dbInstance.getOrderInfo();
        List<DealOrder>dealOrderVos1= dbInstance.getDealOrdersList(true);
        TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
        double tota=0.00;
        int dealCount=0;
        if((dealOrderVos1!=null && dealOrderVos1.size()>0)){
            dealCount=dealOrderVos1.size();
            for (int x=0;x<dealOrderVos1.size();x++){
                tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice()))*(Integer.parseInt(dealOrderVos1.get(x).getQuantity()));
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
