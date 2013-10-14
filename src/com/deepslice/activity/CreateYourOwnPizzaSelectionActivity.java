package com.deepslice.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.deepslice.adapter.CreateYourOwnPizzaListAdapter;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.CreateOwnPizzaData;
import com.deepslice.model.Product;
import com.deepslice.model.ServerResponse;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CreateYourOwnPizzaSelectionActivity extends Activity {
    
    ListView lvPizzaList;
    
    CreateYourOwnPizzaListAdapter createYourOwnPizzaAdapter;
    Product selectedProduct;
    List<ToppingsAndSauces> toppingsAndSaucesList;
    
    CreateOwnPizzaData selectedPizzaData;
    List<String> prodIds;
    List<Product> productList;
    
    ProgressDialog pDialog;
    JsonParser jsonParser = new JsonParser();
    DeepsliceApplication appInstance;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crusts);

        pDialog = new ProgressDialog(CreateYourOwnPizzaSelectionActivity.this);
        pDialog.setMessage("Please wait...");
        
        appInstance=(DeepsliceApplication)getApplication();
        lvPizzaList = (ListView) findViewById(R.id.listView1);
        
        selectedPizzaData = (CreateOwnPizzaData) getIntent().getExtras().getSerializable("selected_pizza_data");
        prodIds = selectedPizzaData.getProdIds();
        
        productList = new ArrayList<Product>();
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaSelectionActivity.this);
        dbInstance.open();
        for (int i = 0; i < prodIds.size(); i++){
            String prodId = prodIds.get(i);
            productList.add(dbInstance.retrieveProductById(prodId));
        }
        dbInstance.close();
        
        createYourOwnPizzaAdapter = new CreateYourOwnPizzaListAdapter(CreateYourOwnPizzaSelectionActivity.this, productList);
        lvPizzaList.setAdapter(createYourOwnPizzaAdapter);
        
        lvPizzaList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                selectedProduct = (Product) parent.getItemAtPosition(position);
                Log.d(">>>><<<", "selectedProd Id = " + selectedProduct.getProdID());
                updateTopingSaucesData();    
                
            }
        });
    }
    
    
    private void goBackToCreateYourOwnPizzaDetailsActivity() { 

        if(pDialog.isShowing())
            pDialog.dismiss();

        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedProduct", selectedProduct);
        Intent resultData = new Intent();
        resultData.putExtras(bundle);
//        resultData.putExtra("prodCatId", selectedProduct.getProdID());
//        resultData.putExtra("subCatId1", selectedSubCat.getSubCatOf());
//        resultData.putExtra("subCatId2", selectedSubCat.getSubCatID());
        setResult(Activity.RESULT_OK, resultData);
        finish();

    }   
    
    
    
    final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {        
        public void run() {            
            goBackToCreateYourOwnPizzaDetailsActivity();        
        }    
    };
    
    
    
    protected void updateTopingSaucesData() {

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaSelectionActivity.this);
        dbInstance.open();
        boolean isToppingsSynced = dbInstance.isProductToppingsExist(selectedProduct.getProdID());
        dbInstance.close();

        if(isToppingsSynced) {
            goBackToCreateYourOwnPizzaDetailsActivity();
        }
        else {
//            pd = ProgressDialog.show(ProductsListActivity.this, "", "Please wait...", true, false);
            if(!pDialog.isShowing()){
                pDialog.setIndeterminate(true);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            Thread t = new Thread() {            
                public void run() {                

                    try {
                        GetPizzaToppingAndSauces();

                    } catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    mHandler.post(mUpdateResults);            
                }
            };        
            t.start();
        }

    }
    
    
    private void GetPizzaToppingAndSauces(){

        String url = Constants.ROOT_URL + "GetPizzaToppingsAndSauces.aspx?prodID=" + selectedProduct.getProdID();
        long dataRetrieveStartTime = System.currentTimeMillis();
        ServerResponse response = jsonParser.retrieveGETResponse(url, null, Constants.API_RESPONSE_TYPE_JSON_ARRAY);

        long dataRetrieveEndTime = System.currentTimeMillis();
        Log.d("TIME", "time to retrieve topping-sauce data for prodId " + selectedProduct.getProdID() + " = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


        if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
            JSONObject jsonObj = response.getjObj();
            try {
                JSONObject responseObj = jsonObj.getJSONObject("Response");
                int status = responseObj.getInt("Status");
                JSONArray data = responseObj.getJSONArray("Data");
                JSONObject errors = responseObj.getJSONObject("Errors");

                toppingsAndSaucesList = ToppingsAndSauces.parseToppingsAndSauces(data);

                long productParseEndTime = System.currentTimeMillis();
                Log.d("TIME", "time to parse topping-sauce list of item " + toppingsAndSaucesList.size() + " = " + (productParseEndTime - dataRetrieveEndTime)/1000 + " second");

                long dbInsertionStart = System.currentTimeMillis();
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaSelectionActivity.this);
                dbInstance.open();
                dbInstance.insertToppingSauces(toppingsAndSaucesList);
                dbInstance.close();
                long dbInsertionEnd = System.currentTimeMillis();
                Log.d("TIME", "time to insert topping-sauce data " + " = " + (dbInsertionEnd - dbInsertionStart)/1000 + " second");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } 
    }
    
    

}
