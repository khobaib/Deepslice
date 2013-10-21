package com.deepslice.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.cache.ImageLoader;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.CreateOwnPizzaData;
import com.deepslice.model.Favourite;
import com.deepslice.model.NewProductOrder;
import com.deepslice.model.NewToppingsOrder;
import com.deepslice.model.Product;
import com.deepslice.model.ServerResponse;
import com.deepslice.model.ToppingPrices;
import com.deepslice.model.ToppingSizes;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.Utils;


public class CreateYourOwnPizzaDetails extends Activity {

//    int currentIndex;           // index position of selectedProduct in productList 
    int currentCount;           // qty of that product selected for ordering

    ImageLoader imageLoader;

    int SELECT_TOPPINGS = 112233;
    int SELECT_PIZZA = 112255;

    Spinner sSauceName;
    List<ToppingsAndSauces> saucesList;



//    HashMap<String, ToppingsHashmap> toppingsSelected;

    CreateOwnPizzaData selectedPizzaData;
    List<String> prodIds;
//    List<Product> productList;

    ProgressDialog pd;
    JsonParser jsonParser = new JsonParser();
    
    List<ToppingPrices> toppingsPriceList;
    List<ToppingSizes> toppingsSizeList;
    List<ToppingsAndSauces> toppingsAndSaucesList;
    
    List<NewToppingsOrder> toppingsSelected; 
    
    int selectedSauceIndex;
    Product selectedProduct;

    ImageButton ImageButtonNext, ImageButtonPrv;
    TextView PizzaName;
    ImageView ImageViewCreateYourOwn;
    Button buttonAddOrders;
    TextView pKJ, headerTextView;
    TextView selectedToppings, selectedSauces;
    TextView favCountTxt;
    TextView tvItemsPrice, tvFavCount;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_your_own);

        selectedPizzaData = (CreateOwnPizzaData) getIntent().getExtras().getSerializable("selected_pizza");
        prodIds = selectedPizzaData.getProdIds();
        
        selectedProduct = new Product();
        if(prodIds.size() > 0){
            DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
            dbInstance.open();
            selectedProduct = dbInstance.retrieveProductById(prodIds.get(0));
            dbInstance.close();
        }
        
        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);

//        currentIndex = 0;
        currentCount = 1;
        imageLoader = new ImageLoader(CreateYourOwnPizzaDetails.this);

//        productList = new ArrayList<Product>();
//        DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
//        dbInstance.open();
//        for (int i = 0; i < prodIds.size(); i++){
//            String prodId = prodIds.get(i);
//            productList.add(dbInstance.retrieveProductById(prodId));
//        }
//        dbInstance.close();       

        findViewById(R.id.rl_crust).setVisibility(View.GONE);

        PizzaName = (TextView) findViewById(R.id.pDesc);
        PizzaName.setText(selectedProduct.getProdDesc());

        buttonAddOrders = (Button)findViewById(R.id.buttonAddOrders);
        buttonAddOrders.setText("Add to Order");

        pKJ = (TextView)findViewById(R.id.pKJ);
        pKJ.setText(selectedProduct.getCaloriesQty() + "kj");

        ImageViewCreateYourOwn =(ImageView)findViewById(R.id.imageViewCreateYourOwn);
        imageLoader.DisplayImage(selectedProduct.getFullImage(), ImageViewCreateYourOwn);

        headerTextView = (TextView)findViewById(R.id.headerTextView);
        headerTextView.setText("Create Your Own Pizza");
        
        selectedToppings = (TextView)findViewById(R.id.selectedToppings);
        selectedSauces = (TextView)findViewById(R.id.selectedSauces);
        sSauceName=(Spinner)findViewById(R.id.spinner1);

        // update topping list for default pizza
        updateToppingsAndSaucesData(selectedProduct.getProdID());


//        ImageButtonNext = (ImageButton)findViewById(R.id.imageButtonNext);
//        if(productList.size() > 1)
//            ImageButtonNext.setVisibility(View.VISIBLE);
//
//        ImageButtonNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                currentIndex++;
//                imageLoader.DisplayImage(productList.get(currentIndex).getFullImage(), ImageViewCreateYourOwn);
//                PizzaName.setText(productList.get(currentIndex).getProdDesc());
//                updateToppingsAndSaucesData(productList.get(currentIndex).getProdID());
//                if(currentIndex < (productList.size()-1)){
//                    ImageButtonNext.setVisibility(View.VISIBLE);
//                    ImageButtonPrv.setVisibility(View.VISIBLE);
//                    Log.d("............",""+currentIndex);
//
//                } else {
//                    ImageButtonNext.setVisibility(View.GONE);
//
//                }
//            }
//        });
//
//
//
//        ImageButtonPrv=(ImageButton)findViewById(R.id.imageButtonPriv);
//        ImageButtonPrv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                currentIndex--;
//                imageLoader.DisplayImage(productList.get(currentIndex).getFullImage(), ImageViewCreateYourOwn);
//                PizzaName.setText(productList.get(currentIndex).getProdDesc());
//                updateToppingsAndSaucesData(productList.get(currentIndex).getProdID());
//                //                populateSauceData(currentIndex);
//                if(currentIndex > 0){
//                    ImageButtonPrv.setVisibility(View.VISIBLE);
//                    ImageButtonNext.setVisibility(View.VISIBLE);
//                }else if(currentIndex == 0) {
//                    ImageButtonPrv.setVisibility(View.GONE);
//                }
//            }
//        });

        favCountTxt=(TextView)findViewById(R.id.qVal);
                
        Button bMinus= (Button)findViewById(R.id.buttonMinus);
        bMinus.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(currentCount>1)
                {
                    currentCount--;
                    favCountTxt.setText(currentCount+"");
                }


            }
        });
        
        Button bPlus = (Button)findViewById(R.id.buttonPlus);
        bPlus.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if(currentCount<10)
                {
                    currentCount++;
                    favCountTxt.setText(currentCount+"");
                }


            }
        });
        
        LinearLayout ltToppings = (LinearLayout)findViewById(R.id.ltToppings);
        ltToppings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i=new Intent(CreateYourOwnPizzaDetails.this, PizzaToppingsActivity.class);
                Bundle bundle = new Bundle();
                AppProperties.selectedToppings = toppingsSelected;

                    bundle.putSerializable("selectedProduct", selectedProduct);

                i.putExtras(bundle);
                startActivityForResult(i, SELECT_TOPPINGS);


            }
        });
        
        
        RelativeLayout llPizza = (RelativeLayout)findViewById(R.id.rl_pizza_name);
        llPizza.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i=new Intent(CreateYourOwnPizzaDetails.this, CreateYourOwnPizzaSelectionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selected_pizza_data", selectedPizzaData);
                i.putExtras(bundle);
                startActivityForResult(i, SELECT_PIZZA);
            }
        });
        
        
        LinearLayout ltSauces = (LinearLayout)findViewById(R.id.ltSauces);
        ltSauces.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sSauceName.performClick();
            }
        });
        
        sSauceName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                selectedSauceIndex = position;
                selectedSauces.setText(saucesList.get(position).getToppingCode());      
                Log.d("<<<>>>", "selected sauceID = " + saucesList.get(position).getToppingID());
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
                
        

        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(CreateYourOwnPizzaDetails.this, FavoriteListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(CreateYourOwnPizzaDetails.this, MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        Button buttonAddFav= (Button)findViewById(R.id.buttonAddFav);
        buttonAddFav.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
                dbInstance.open();
                boolean favAdded=dbInstance.favAlreadyAdded(selectedProduct.getProdID(),selectedProduct.getDisplayName());
                if(favAdded)
                {
                    Toast.makeText(CreateYourOwnPizzaDetails.this, "Already added to Favourites", Toast.LENGTH_LONG).show();
                }
                else
                {
                    dbInstance.insertFav(getFavBean());
                    doResumeWork();
                    Toast.makeText(CreateYourOwnPizzaDetails.this, "Successfully added to Favourites", Toast.LENGTH_LONG).show();
                }
                dbInstance.close();
               
            }


            private Favourite getFavBean() {

                Favourite f = new Favourite();
                f.setProdCatID(selectedProduct.getProdCatID());
                f.setSubCatID1(selectedProduct.getSubCatID1());
                f.setSubCatID2(selectedProduct.getSubCatID2());
                f.setProdID(selectedProduct.getProdID());
                f.setProdCode(selectedProduct.getProdCode());
                f.setDisplayName(selectedProduct.getDisplayName());
                f.setProdAbbr(selectedProduct.getProdAbbr());
                f.setProdDesc(selectedProduct.getProdDesc());
                f.setIsAddDeliveryAmount(selectedProduct.getIsAddDeliveryAmount());
                f.setDisplaySequence(selectedProduct.getDisplaySequence());
                f.setCaloriesQty(selectedProduct.getCaloriesQty());
                f.setPrice(selectedProduct.getPrice());
                f.setThumbnail(selectedProduct.getThumbnail());
                f.setFullImage(selectedProduct.getFullImage());
                f.setCustomName(selectedProduct.getDisplayName());
                f.setProdCatName("Pizza");
                return f;
            }
        });








        buttonAddOrders.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                
                NewProductOrder tempOrder = getOrder(Constants.PRODUCT_SELECTION_WHOLE);
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
                dbInstance.open();
                long orderPId = dbInstance.insertOrder(tempOrder);                        

                // inserting toppingsData to the local DB
                if(toppingsSelected != null){               
                    for(NewToppingsOrder thisToppingsOrder : toppingsSelected){
                        thisToppingsOrder.setIsDeal(false);  
                        thisToppingsOrder.setProdOrderId((int) orderPId);
                        thisToppingsOrder.setDealOrderDetailsId(Constants.DUMMY_ID);    
                        thisToppingsOrder.setToppingPrice(String.valueOf(generateToppingsPrice(thisToppingsOrder)));
                        dbInstance.insertToppingsOrder(thisToppingsOrder);
                    }
                }
                dbInstance.close();

                // inserting sauceData to the local DB
                if(selectedSauceIndex != -1){
                    NewToppingsOrder thisSauceOrder = Utils.convertToppingAndSauceObjectToToppingsOrder(saucesList.get(selectedSauceIndex));
                    thisSauceOrder.setIsDeal(false);  
                    thisSauceOrder.setProdOrderId((int) orderPId);
                    thisSauceOrder.setDealOrderDetailsId(Constants.DUMMY_ID);   
                    thisSauceOrder.setToppingPrice(String.valueOf(generateToppingsPrice(thisSauceOrder)));
                    dbInstance.open();
                    dbInstance.insertToppingsOrder(thisSauceOrder);
                    dbInstance.close();
                }
                
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateYourOwnPizzaDetails.this);
                alertDialog.setTitle("Deepslice");
                alertDialog.setMessage("Your pizza is added to Cart Successfully");
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    } 
                }); 
                alertDialog.create().show();   

            }
        });
        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(CreateYourOwnPizzaDetails.this, MyOrderActivity.class);
                startActivity(intent);

            }
        });
    }
    
    
    private void generateDefaultToppings() {

            DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
            dbInstance.open();
            List<ToppingsAndSauces> toppingsList = dbInstance.retrievePizzaToppings(selectedProduct.getProdID());
            dbInstance.close();

            toppingsSelected = new ArrayList<NewToppingsOrder>();
            for (ToppingsAndSauces thisToppings : toppingsList) {
                if(thisToppings.getIsFreeWithPizza().equalsIgnoreCase("True")){
                    NewToppingsOrder thisToppingsOrder = Utils.convertToppingAndSauceObjectToToppingsOrder(thisToppings);
                    toppingsSelected.add(thisToppingsOrder);
                }
            }        


            String toppingsCodeToDisplay = "";
            for(NewToppingsOrder thisToppingsOrder : toppingsSelected){
                toppingsCodeToDisplay += thisToppingsOrder.getToppingsCode() + "," ;
            }
            selectedToppings.setText(AppProperties.trimLastCommaAddAnd(toppingsCodeToDisplay));
       
    }


    private void generateSauceList(){
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
        dbInstance.open();
        saucesList = dbInstance.retrievePizzaSauces(selectedProduct.getProdID());
        dbInstance.close();

        selectedSauceIndex = -1;
        List<String> sauceName = new ArrayList<String>();

        for(int sIndex = 0; sIndex < saucesList.size(); sIndex++){
            sauceName.add(saucesList.get(sIndex).getToppingCode());
            if(saucesList.get(sIndex).getIsFreeWithPizza().equals("True"))
                selectedSauceIndex = sIndex;
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sauceName);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sSauceName.setAdapter(spinnerArrayAdapter); 
        if(selectedSauceIndex != -1)
            sSauceName.setSelection(selectedSauceIndex);                // by-default, free sauce will be selected.
    } 
    
    

    final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {        
        public void run() {         
            
            if(pd.isShowing())
                pd.dismiss();
            
            generateDefaultToppings();
            generateSauceList();   
        }    
    };   

    String serverResponse;
    protected void updateToppingsAndSaucesData(final String prodId) {
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
        dbInstance.open();
        boolean isToppingsSynced = dbInstance.isProductToppingsExist(prodId);
        dbInstance.close();
        
        
        if(isToppingsSynced) {
            generateDefaultToppings();
            generateSauceList();
        }
        else {
            pd = ProgressDialog.show(CreateYourOwnPizzaDetails.this, "", "Please wait...", true, false);

            Thread t = new Thread() {            
                public void run() {                

                    try {
                        GetPizzaToppingAndSauces(prodId);

                    } catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    mHandler.post(mUpdateResults);            
                }
            };        
            t.start();
        }
    }
    
    
    private void GetPizzaToppingAndSauces(String prodId){

        String url = Constants.ROOT_URL + "GetPizzaToppingsAndSauces.aspx?prodID=" + prodId;
        long dataRetrieveStartTime = System.currentTimeMillis();
        ServerResponse response = jsonParser.retrieveGETResponse(url, null, Constants.API_RESPONSE_TYPE_JSON_ARRAY);

        long dataRetrieveEndTime = System.currentTimeMillis();
        Log.d("TIME", "time to retrieve topping-sauce data for prodId " + prodId + " = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


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
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
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
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        
        if (requestCode == SELECT_TOPPINGS) {
            //            String tempList="";
            toppingsSelected = AppProperties.selectedToppings;
            if(toppingsSelected == null)
                toppingsSelected = new ArrayList<NewToppingsOrder>();

            String toppingsCodeToDisplay = "";
            for(NewToppingsOrder thisToppingsOrder : toppingsSelected){
                toppingsCodeToDisplay += thisToppingsOrder.getToppingsCode() + "," ;
            }

            selectedToppings.setText(AppProperties.trimLastCommaAddAnd(toppingsCodeToDisplay));

        }
        
        if (requestCode == SELECT_PIZZA) {
            Bundle b = data.getExtras();
            selectedProduct = (Product)b.getSerializable("selectedProduct"); 
//            Log.d(TAG, "retrieved selectedProduct from new crust, prodId = " + selectedProduct.getProdID());

            generateDefaultToppings();
            generateSauceList();
            
            PizzaName.setText(selectedProduct.getProdDesc());
        }
    }
    
    
    private NewProductOrder getOrder(int selection) {       // selection = left/right/whole

        NewProductOrder order = new NewProductOrder();
        order.setProdCatId(selectedProduct.getProdCatID());
        order.setSubCatId1(selectedProduct.getSubCatID1());
        order.setSubCatId2(selectedProduct.getSubCatID2());
        order.setProdId(selectedProduct.getProdID());
        order.setProdCode(selectedProduct.getProdCode());
        order.setDisplayName(selectedProduct.getDisplayName());
        order.setCaloriesQty(selectedProduct.getCaloriesQty());
        order.setPrice(selectedProduct.getPrice());
        order.setThumbnailImage(selectedProduct.getThumbnail());
        order.setFullImage(selectedProduct.getFullImage());
        order.setQuantity(String.valueOf(currentCount));
        order.setProdCatName(Constants.PRODUCT_CATEGORY_PIZZA);
        order.setIsCreateByOwn(true);
        order.setSelection(selection);    
        order.setOtherHalfProdId(0);               // default

        return order;
    }
    
    
    private double generateToppingsPrice(NewToppingsOrder toppingsOrder){
        double ownPrice = Double.parseDouble(toppingsOrder.getToppingPrice());
        if(toppingsOrder.getIsFreeWithPizza() && toppingsOrder.getToppingSizeId().equals(Constants.SINGLE_SIZE_TOPPING_ID))
            return 0.000;
        else if(ownPrice > 0.000){
            return ownPrice;
        }
        else{
            DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
            dbInstance.open();
            double tempPrice = dbInstance.getToppingPrice(toppingsOrder.getToppingsId(),toppingsOrder.getToppingSizeId());
            dbInstance.close();
            Log.d(">>>><<<", "topping PRICE = " + tempPrice);
            return tempPrice;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        doResumeWork();
    }
    
    
    private void doResumeWork() {
        List<String> orderInfo = Utils.OrderInfo(CreateYourOwnPizzaDetails.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
        
        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }

        
        String favCount = Utils.FavCount(CreateYourOwnPizzaDetails.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }    

    }
}


