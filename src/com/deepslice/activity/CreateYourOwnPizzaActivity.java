package com.deepslice.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.deepslice.adapter.PizzaTypeMenuAdapter;
import com.deepslice.model.CreateOwnPizzaData;
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


}
