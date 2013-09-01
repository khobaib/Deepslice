package com.deepslice.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.deepslice.adapter.ToppingsListAdapter;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.DealOrder;
import com.deepslice.model.NewDealsOrderDetails;
import com.deepslice.model.NewToppingsOrder;
import com.deepslice.model.Product;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Utils;

public class PizzaToppingsActivity extends Activity{

    Product selectedProduct;
    NewDealsOrderDetails dealOrderDetails;
    ListView listview;
    ToppingsListAdapter toppingsListAdapter;

    List<ToppingsAndSauces> toppingsList;

    public List<NewToppingsOrder> toppingsSelected;
    String productId;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toppings);
        Bundle b = this.getIntent().getExtras();

        if (b.getBoolean("isDeal",false)){
            dealOrderDetails = (NewDealsOrderDetails) b.getSerializable("selectedProduct");             
            productId = dealOrderDetails.getProdId();
        }else {
            selectedProduct = (Product)b.getSerializable("selectedProduct");
            productId = selectedProduct.getProdID();
        }

        toppingsSelected = AppProperties.selectedToppings;
        if(toppingsSelected == null)
            toppingsSelected = new ArrayList<NewToppingsOrder>();

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaToppingsActivity.this);
        dbInstance.open();
        toppingsList = dbInstance.retrievePizzaToppings(productId);
        dbInstance.close();

        listview = (ListView) findViewById(R.id.listView1);				
        toppingsListAdapter = new ToppingsListAdapter(PizzaToppingsActivity.this, toppingsList, toppingsSelected);
        listview.setAdapter(toppingsListAdapter);

        Button startOrderingButton = (Button)findViewById(R.id.startOrderingButton);
        startOrderingButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                toppingsSelected = ToppingsListAdapter.getSelectedToppingsList();
                if(toppingsSelected.size() > 11){
                    Utils.openErrorDialog(PizzaToppingsActivity.this, "No more than 11 Toppings allowed!\nCurrently "+toppingsSelected.size()+" selected.");
                    return;
                }

                AppProperties.selectedToppings = toppingsSelected;
                Intent resultData = new Intent();
                setResult(Activity.RESULT_OK, resultData);
                finish();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { // Back key pressed
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
