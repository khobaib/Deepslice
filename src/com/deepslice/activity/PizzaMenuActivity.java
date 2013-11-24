package com.deepslice.activity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.ProductSubCategory;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.Utils;

public class  PizzaMenuActivity extends Activity{

    private static final int REQUEST_CODE_IS_PIZZA_HALF = 1001;

    List<ProductSubCategory> subCatList;

    public static Boolean isHalf;

    ListView listview;
    TextView tvItemsPrice, tvFavCount;
    
    MyListAdapterPizza myAdapter;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(PizzaMenuActivity.this, "92b170cf");
        setContentView(R.layout.sub_menu_pizza3);
//        Bundle b = this.getIntent().getExtras();

//        catType=b.getString("catType");
        isHalf = getIntent().getExtras().getBoolean("isHalf", false);
        
        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaMenuActivity.this);
        dbInstance.open();
        subCatList = dbInstance.retrievePizzaSubMenu();
        dbInstance.close();

        listview = (ListView) findViewById(R.id.listView1);				
        myAdapter = new MyListAdapterPizza(this,R.layout.line_item_yello, subCatList);
        listview.setAdapter(myAdapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ProductSubCategory subCat = (ProductSubCategory) parent.getItemAtPosition(position);
                if (subCat != null) {
                    Intent intent = new Intent(PizzaMenuActivity.this, ProductsListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("catId", subCat.getProdCatID());
                    bundle.putString("subCatId", subCat.getSubCatID());
                    bundle.putString("catType", Constants.PRODUCT_CATEGORY_PIZZA);
                    bundle.putString("titeDisplay", subCat.getSubCatCode() + " Pizza");
                    bundle.putBoolean("isHalf", isHalf);

                    intent.putExtras(bundle);
                    startActivity(intent);
                    if(isHalf){
                        Log.d("HALF PIZZA", "returning from PizzaSubMenuActivity after set half-pizza");
                        finish();
                    }
                }					
            }
        });

        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PizzaMenuActivity.this, FavoriteListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PizzaMenuActivity.this, MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PizzaMenuActivity.this, MyOrderActivity.class);
                startActivity(intent);

            }
        });

        ImageButton imageButtonHalf=(ImageButton)findViewById(R.id.imageButtonHalf);
        if(isHalf)
            imageButtonHalf.setVisibility(View.GONE);
        imageButtonHalf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PizzaMenuActivity.this, HalfAndHalf.class);
                startActivity(intent);
            }
        });

        ImageButton imageButtonCreateYourOwn=(ImageButton)findViewById(R.id.imageButtonCreateUown);
        if(isHalf)
            imageButtonCreateYourOwn.setVisibility(View.GONE);
        imageButtonCreateYourOwn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PizzaMenuActivity.this, CreateYourOwnCrustActivity.class);
                startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode != RESULT_OK) {
            return;
        }
        
        switch(requestCode){
            case REQUEST_CODE_IS_PIZZA_HALF:
                // parse data
                // redirect to previous HalfAndHalf activity
                break;
            default:
                break;
        }
    }


    private class MyListAdapterPizza extends ArrayAdapter<ProductSubCategory> {

        private List<ProductSubCategory> items;

        public MyListAdapterPizza(Context context, int viewResourceId, List<ProductSubCategory> items) {
            super(context, viewResourceId, items);
            this.items = items;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.line_item_yello, null);
            }
            ProductSubCategory event = items.get(position);
            if (event != null) {

                TextView title = (TextView) convertView
                        .findViewById(R.id.itemName);

                title.setText(event.getSubCatCode()+" Pizza");

                convertView.setTag(event);
            }
            return convertView;
        }

    }
    // /////////////////////// END LIST ADAPTER
    @Override
    protected void onResume() {
        super.onResume();

        List<String> orderInfo = Utils.OrderInfo(PizzaMenuActivity.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
        
        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }

        
        String favCount = Utils.FavCount(PizzaMenuActivity.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }      

    }

}
