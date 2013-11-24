package com.deepslice.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.adapter.DealCrustAdapter;
import com.deepslice.adapter.ProductCrustAdapter;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.CouponDetails.CrustProducts;
import com.deepslice.model.Product;
import com.deepslice.model.ProductSubCategory;
import com.deepslice.model.ServerResponse;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;

public class PizzaCrustActivity extends Activity{

//    Product selectedProduct;
    boolean isDeal;

    ListView lvCrustList;

    ProductCrustAdapter productCrustAdapter;
    DealCrustAdapter dealCrustAdapter;

    List<ProductSubCategory> crustList;
    String prodCatId, prodSubCatId, prodCode, prodId;
    
    Product selectedProduct;
    
    List<ToppingsAndSauces> toppingsAndSaucesList;
    
    ProgressDialog pDialog;
    JsonParser jsonParser = new JsonParser();
    DeepsliceApplication appInstance;
    
//    String currentProductId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(this, "92b170cf");
        
        setContentView(R.layout.crusts);

        pDialog = new ProgressDialog(PizzaCrustActivity.this);
        pDialog.setMessage("Please wait...");
        
        appInstance=(DeepsliceApplication)getApplication();
        lvCrustList = (ListView) findViewById(R.id.listView1);

        Bundle b = this.getIntent().getExtras();        
//        selectedProduct = (Product)b.getSerializable("selectedProduct");
//        prodCatId = selectedProduct.getProdCatID();
//        prodSubCatId = selectedProduct.getSubCatID1();
        prodCatId = b.getString("prodCatId");
        prodSubCatId = b.getString("subCatId1");
        prodCode = b.getString("prodCode");

        if(b.getBoolean("isDeal",false)){
            isDeal = true;
            prodId = b.getString("prodId");

            dealCrustAdapter = new DealCrustAdapter(PizzaCrustActivity.this, appInstance.getCouponDetails().getProdAndSubCatID(), prodId);
            lvCrustList.setAdapter(dealCrustAdapter);
        }else {
            DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaCrustActivity.this);
            dbInstance.open(); 
            crustList = dbInstance.retrievePizzaCrustList(prodCatId, prodSubCatId);
            dbInstance.close();

            productCrustAdapter = new ProductCrustAdapter(PizzaCrustActivity.this, crustList, prodCatId, prodSubCatId);
            lvCrustList.setAdapter(productCrustAdapter);
        }


        lvCrustList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(isDeal){
                    CrustProducts crustProducts = (CrustProducts) parent.getItemAtPosition(position);
                    Log.d(">>>><<<", "selected crustId = " + crustProducts.getSubCat2Id());
                    
                    DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaCrustActivity.this);
                    dbInstance.open();
                    selectedProduct = dbInstance.retrieveProductById(crustProducts.getProdID());       
                    dbInstance.close();
                    Log.d(">>>><<<", "selectedProd Id = " + selectedProduct.getProdID());
                    updateTopingSaucesData();  
                    
//                    Intent resultData = new Intent();
//                    resultData.putExtra("name", crustProducts.getSubCat2Code());
//                    resultData.putExtra("catId", crustProducts.getProdID());
//                    resultData.putExtra("subCatId", crustProducts.getSubCat2Id());
//                    setResult(Activity.RESULT_OK, resultData);
//                    finish();

                }else {
                    ProductSubCategory selectedSubCat = (ProductSubCategory) parent.getItemAtPosition(position);
                    Log.d(">>>><<<", "selected crustId = " + selectedSubCat.getSubCatID());
                    
                    DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaCrustActivity.this);
                    dbInstance.open();
                    selectedProduct = dbInstance.retrieveProductFromSubCrust(selectedSubCat.getProdCatID(),
                            selectedSubCat.getSubCatOf(), selectedSubCat.getSubCatID(), prodCode);       
                    dbInstance.close();
                    Log.d(">>>><<<", "selectedProd Id = " + selectedProduct.getProdID());
                    updateTopingSaucesData();                    
                }
            }
        });

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
    
    
    private void goBackToPizzaDetailsActivity() { 

        if(pDialog.isShowing())
            pDialog.dismiss();

        Bundle bundle=new Bundle();
        bundle.putSerializable("selectedProduct",selectedProduct);
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
            goBackToPizzaDetailsActivity();        
        }    
    };
    
    
    protected void updateTopingSaucesData() {

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaCrustActivity.this);
        dbInstance.open();
        boolean isToppingsSynced = dbInstance.isProductToppingsExist(selectedProduct.getProdID());
        dbInstance.close();

        if(isToppingsSynced) {
            goBackToPizzaDetailsActivity();
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
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaCrustActivity.this);
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
