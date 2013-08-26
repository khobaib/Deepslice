package com.deepslice.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.deepslice.adapter.DealCrustAdapter;
import com.deepslice.adapter.ProductCrustAdapter;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.CouponDetails.CrustProducts;
import com.deepslice.model.Product;
import com.deepslice.model.ProductSubCategory;
import com.deepslice.utilities.DeepsliceApplication;

public class PizzaCrustActivity extends Activity{

    Product selectedProduct;
    DeepsliceApplication appInstance;
    boolean isDeal;

    ListView lvCrustList;

    ProductCrustAdapter productCrustAdapter;
    DealCrustAdapter dealCrustAdapter;

    List<ProductSubCategory> crustList;
    String crustCatId, crustSubCatId, currentProductId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crusts);

        appInstance=(DeepsliceApplication)getApplication();
        lvCrustList = (ListView) findViewById(R.id.listView1);

        Bundle b = this.getIntent().getExtras();        
        selectedProduct = (Product)b.getSerializable("selectedProduct");
        crustCatId = b.getString("catId");
        crustSubCatId = b.getString("subCatId");

        if(b.getBoolean("isDeal",false)){
            isDeal = true;
            currentProductId = b.getString("prdID");

            dealCrustAdapter = new DealCrustAdapter(PizzaCrustActivity.this, appInstance.getCouponDetails().getProdAndSubCatID(), currentProductId);
            lvCrustList.setAdapter(dealCrustAdapter);
        }else {
            DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaCrustActivity.this);
            dbInstance.open(); 
            crustList = dbInstance.retrievePizzaCrustList(selectedProduct.getProdCatID(),selectedProduct.getSubCatID1());
            dbInstance.close();

            productCrustAdapter = new ProductCrustAdapter(PizzaCrustActivity.this, crustList, crustCatId, crustSubCatId);
            lvCrustList.setAdapter(productCrustAdapter);
        }


        lvCrustList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(isDeal){
                    CrustProducts crustProducts=(CrustProducts) parent.getItemAtPosition(position);

                    Intent resultData = new Intent();
                    resultData.putExtra("name", crustProducts.getSubCat2Code());
                    resultData.putExtra("catId", crustProducts.getProdID());
                    resultData.putExtra("subCatId", crustProducts.getSubCat2Id());
                    setResult(Activity.RESULT_OK, resultData);
                    finish();

                }else {
                    ProductSubCategory selectedSubCat = (ProductSubCategory) parent.getItemAtPosition(position);

                    Intent resultData = new Intent();
                    resultData.putExtra("name", selectedSubCat.getSubCatDesc());
                    resultData.putExtra("catId", selectedSubCat.getProdCatID());
                    resultData.putExtra("subCatId", selectedSubCat.getSubCatID());
                    setResult(Activity.RESULT_OK, resultData);
                    finish();
                }
            }
        });

    }
}
