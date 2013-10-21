package com.deepslice.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.cache.ImageLoader;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.CouponDetails.CrustProducts;
import com.deepslice.model.Favourite;
import com.deepslice.model.NewDealsOrderDetails;
import com.deepslice.model.NewProductOrder;
import com.deepslice.model.NewToppingsOrder;
import com.deepslice.model.Product;
import com.deepslice.model.ProductSubCategory;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;
import com.deepslice.utilities.Utils;

public class NEW_PizzaDetailsActivity extends Activity {


    private static final String TAG = NEW_PizzaDetailsActivity.class.getSimpleName();

    Boolean isHalf;

    TextView favCountTxt;
    int currentCount = 1;

    Product selectedProduct;
    ProductSubCategory selectedCrust;
    NewDealsOrderDetails dealOrderDetails;
    
    int selectedItemPosition;

    ImageLoader imageLoader;

    Spinner sSauceName;
    List<ToppingsAndSauces> saucesList;
    int selectedSauceIndex;

    int SELECT_TOPPINGS = 112233;
    int SELECT_CRUST = 112244;

    String prodCatId, subCatId1, subCatId2, prodCode;
    String couponGroupID;

    boolean isDeal;

    TextView selectedToppings, selectedSauces, selectedCrusts;
    TextView tvItemsPrice, tvFavCount;

    List<NewToppingsOrder> toppingsSelected;    

    DeepsliceApplication appInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pizza_details);

        imageLoader = new ImageLoader(NEW_PizzaDetailsActivity.this);
        appInstance=(DeepsliceApplication)getApplication();

        Bundle b = this.getIntent().getExtras();
        isHalf = b.getBoolean("isHalf", false);

        ImageView ivHideLeftHalf = (ImageView) findViewById(R.id.iv_hide_left_half);
        ImageView ivHideRightHalf = (ImageView) findViewById(R.id.iv_hide_right_half);

        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);

        if(isHalf){
            if(AppProperties.isFirstPizzaChosen){
                ivHideLeftHalf.setVisibility(View.VISIBLE);
            }
            else{
                ivHideRightHalf.setVisibility(View.VISIBLE);
            }
        }

        Button buttonAddOrders = (Button)findViewById(R.id.buttonAddOrders);
        TextView pDesc = (TextView)findViewById(R.id.pDesc);
        ImageView pImage = (ImageView) findViewById(R.id.imageView1);
        TextView pKJ = (TextView)findViewById(R.id.pKJ);
        TextView headerTextView = (TextView)findViewById(R.id.headerTextView);
        selectedCrusts = (TextView)findViewById(R.id.selectedCrust);


        if(b.containsKey("selectedProduct")){
            selectedProduct = (Product)b.getSerializable("selectedProduct");
            //            productId = selectedProduct.getProdID();

            pDesc.setText(selectedProduct.getProdDesc());
            imageLoader.DisplayImage(selectedProduct.getFullImage(), pImage);
            pKJ.setText(selectedProduct.getCaloriesQty()+"kj");
        }

        isDeal = b.getBoolean("isDeal",false);
        if (isDeal){
            RelativeLayout rlCount = (RelativeLayout) findViewById(R.id.rl_count);
            rlCount.setVisibility(View.GONE);
            selectedItemPosition = b.getInt("item_position");
            dealOrderDetails = appInstance.getDealOrderDetails();
            couponGroupID = b.getString("couponGroupID");
            //            productId = dealOrder.getProdID();            // here's some confusion, will sort out later
            buttonAddOrders.setText("Add to Deal");
            headerTextView.setText("Add to Deal");
            String crustToDisplay = "";

            for(CrustProducts crustProducts : appInstance.getCouponDetails().getProdAndSubCatID()){
                if(b.getString("prdID").equalsIgnoreCase(crustProducts.getProdID())){
                    crustToDisplay = crustProducts.getSubCat2Code();
                    break;
                }
            }

            Log.d("........","crustToDisplay = " + crustToDisplay);
            selectedCrusts.setText(crustToDisplay);

        }else {
            buttonAddOrders.setText("Add to Order");
            selectedCrust = new ProductSubCategory();
            selectedCrusts.setText("");
        }

        prodCatId = selectedProduct.getProdCatID();     // prodCatId will be unchanged throughout all crusts
        subCatId1 = selectedProduct.getSubCatID1();     // subCatId1 will be unchanged throughout all crusts
        subCatId2 = selectedProduct.getSubCatID2();
        prodCode = selectedProduct.getProdCode();       // prodCode will be unchanged throughout all crusts


        selectedToppings = (TextView)findViewById(R.id.selectedToppings);
        selectedSauces = (TextView)findViewById(R.id.selectedSauces);

        favCountTxt=(TextView)findViewById(R.id.qVal);


        Button bMinus = (Button)findViewById(R.id.buttonMinus);
        bMinus.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(!isDeal){
                    if(currentCount>1)
                    {
                        currentCount--;
                        favCountTxt.setText(currentCount+"");
                    }
                }

            }
        });
        Button bPlus = (Button)findViewById(R.id.buttonPlus);
        bPlus.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d(",,,,",isDeal+".....");
                if(!isDeal){
                    if(currentCount<10)
                    {
                        currentCount++;
                        favCountTxt.setText(currentCount+"");
                    }
                }

            }
        });

        LinearLayout ltToppings = (LinearLayout)findViewById(R.id.ltToppings);
        ltToppings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i=new Intent(NEW_PizzaDetailsActivity.this, PizzaToppingsActivity.class);
                Bundle bundle = new Bundle();
                AppProperties.selectedToppings = toppingsSelected;
                if (isDeal){
                    bundle.putSerializable("selectedProduct", dealOrderDetails);       // need to check
                    bundle.putBoolean("isDeal", isDeal);
                }  else {
                    bundle.putSerializable("selectedProduct", selectedProduct);
                }
                i.putExtras(bundle);
                startActivityForResult(i, SELECT_TOPPINGS);


            }
        });

        //        final String pId = b.getString("prdID");            // ??? need to check it
        LinearLayout ltCrusts = (LinearLayout)findViewById(R.id.ltCrusts);
        ltCrusts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!(isHalf && AppProperties.isFirstPizzaChosen)){
                    Intent i = new Intent(NEW_PizzaDetailsActivity.this, PizzaCrustActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("prodCatId", prodCatId);
                    bundle.putString("subCatId1", subCatId1);
                    bundle.putString("prodCode", prodCode);                    
                    if(isDeal){
                        bundle.putBoolean("isDeal", true);
                        bundle.putString("prodId", selectedProduct.getProdID());
                    }
                    i.putExtras(bundle);
                    startActivityForResult(i, SELECT_CRUST);
                }
            }
        });

        sSauceName = (Spinner)findViewById(R.id.spinner1);

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
                Log.d(TAG, "selected sauceID = " + saucesList.get(position).getToppingID());
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });



        //populating default crust - if coming from Deal, then crust already set in line-145
        if(!isDeal){
            if(isHalf && AppProperties.isFirstPizzaChosen){
                selectedCrust = appInstance.getHalfCrust();
                selectedCrusts.setText(selectedCrust.getSubCatCode());
            }
            else{
                generateCrustInfo();
            }
        }

        // populating default toppings & sauce
        generateDefaultToppings();
        generateSauceList();



        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(NEW_PizzaDetailsActivity.this,FavoriteListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(NEW_PizzaDetailsActivity.this,MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        Button buttonAddFav = (Button)findViewById(R.id.buttonAddFav);
        buttonAddFav.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
                dbInstance.open();
                boolean favAdded = dbInstance.favAlreadyAdded(selectedProduct.getProdID(),selectedProduct.getDisplayName());
                if(favAdded){
                    Toast.makeText(NEW_PizzaDetailsActivity.this, "Already added to Favourites", Toast.LENGTH_LONG).show();
                }
                else{
                    dbInstance.insertFav(getFavBean());
                    doResumeWork();
                    Toast.makeText(NEW_PizzaDetailsActivity.this, "Successfully added to Favourites", Toast.LENGTH_LONG).show();
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

                if (isDeal){
                    dealOrderDetails.setProdId(selectedProduct.getProdID());
                    dealOrderDetails.setDisplayName(selectedProduct.getDisplayName());
                    dealOrderDetails.setThumbnail(selectedProduct.getThumbnail());

                    DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
                    dbInstance.open();
                    if(dbInstance.isDealGroupAlreadySelected(dealOrderDetails.getDealOrderId(), dealOrderDetails.getCouponGroupId())){
                        Log.d(TAG, "YES, this deal group already selected");
                        boolean b = dbInstance.deleteAlreadySelectedDealGroup(dealOrderDetails.getDealOrderId(), dealOrderDetails.getCouponGroupId());
                        Log.d(TAG, "delete already selected deal? = " + b);
                    }
                    long dealOrderDetailsId = dbInstance.insertDealOrderDetails(dealOrderDetails);
                    
                    DealsGroupListActivity.isDealItemCustomized.set(selectedItemPosition, true);

                    // inserting toppingsData to the local DB
                    if(toppingsSelected != null){               
                        for(NewToppingsOrder thisToppingsOrder : toppingsSelected){
                            thisToppingsOrder.setIsDeal(true);
                            thisToppingsOrder.setProdOrderId(Constants.DUMMY_ID);     
                            thisToppingsOrder.setDealOrderDetailsId((int) dealOrderDetailsId);     
                            thisToppingsOrder.setToppingPrice(Constants.twoDForm.format(generateToppingsPrice(thisToppingsOrder)));
                            dbInstance.insertToppingsOrder(thisToppingsOrder);
                        }
                    }
                    dbInstance.close();

                    // inserting sauceData to the local DB
                    if(selectedSauceIndex != -1){
                        NewToppingsOrder thisSauceOrder = Utils.convertToppingAndSauceObjectToToppingsOrder(saucesList.get(selectedSauceIndex));
                        thisSauceOrder.setIsDeal(true);
                        thisSauceOrder.setProdOrderId(Constants.DUMMY_ID);     
                        thisSauceOrder.setDealOrderDetailsId((int) dealOrderDetailsId); 
                        thisSauceOrder.setToppingPrice(Constants.twoDForm.format(generateToppingsPrice(thisSauceOrder)));
                        dbInstance.open();
                        dbInstance.insertToppingsOrder(thisSauceOrder);
                        dbInstance.close();
                    }
                    finish();
                }
                else {

                    if(isHalf){                        
                        if(AppProperties.isFirstPizzaChosen){           // both half are chosen, so insert into DB
                            NewProductOrder tempOrder = getOrder(Constants.PRODUCT_SELECTION_RIGHT);
                            
                            // adding HnH surcharge for 2nd-half
                            String partialSelectionSurcharge = appInstance.getPartialSelectionSurcharge();
                            double halfPrice = Double.parseDouble(tempOrder.getPrice()) + Double.parseDouble(partialSelectionSurcharge)/2.0;
                            tempOrder.setPrice(Constants.twoDForm.format(halfPrice));
                            
                            Log.d("HALF PIZZA", "adding orders to cart");
                            NewProductOrder firstHalfOrder = appInstance.getHalfOder();
                            DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
                            dbInstance.open();
                            long firstHalfOrderPId = dbInstance.insertOrder(firstHalfOrder);         // first half
                            long secondHalfOrderPId = dbInstance.insertOrder(tempOrder);             // 2nd half
                            dbInstance.setOtherHalfPrimaryId((int)firstHalfOrderPId, (int)secondHalfOrderPId);
                            dbInstance.setOtherHalfPrimaryId((int)secondHalfOrderPId, (int)firstHalfOrderPId);
                            dbInstance.close();
                            AppProperties.isFirstPizzaChosen = false;

                            // inserting toppingsData to the local DB
                            // 2nd-half toppings
                            if(toppingsSelected != null){               
                                dbInstance.open();
                                for(NewToppingsOrder thisToppingsOrder : toppingsSelected){
                                    thisToppingsOrder.setIsDeal(false);  
                                    thisToppingsOrder.setProdOrderId((int) secondHalfOrderPId);
                                    thisToppingsOrder.setDealOrderDetailsId(Constants.DUMMY_ID);    
                                    thisToppingsOrder.setToppingPrice(Constants.twoDForm.format(generateToppingsPrice(thisToppingsOrder)/2.0));
                                    dbInstance.insertToppingsOrder(thisToppingsOrder);

                                }
                                dbInstance.close();
                            }

                            // first-half toppings
                            List<NewToppingsOrder> firstHalfToppingsSelected = appInstance.getHalfToppings();
                            if(firstHalfToppingsSelected != null){              
                                dbInstance.open();
                                for(NewToppingsOrder thisToppingsOrder : firstHalfToppingsSelected){
                                    thisToppingsOrder.setIsDeal(false);  
                                    thisToppingsOrder.setProdOrderId((int) firstHalfOrderPId);
                                    thisToppingsOrder.setDealOrderDetailsId(Constants.DUMMY_ID);    
                                    thisToppingsOrder.setToppingPrice(Constants.twoDForm.format(generateToppingsPrice(thisToppingsOrder)/2.0));
                                    dbInstance.insertToppingsOrder(thisToppingsOrder);
                                }
                                dbInstance.close();
                            }


                            // inserting sauceData to the local DB
                            // first-half sauce
                            if(selectedSauceIndex != -1){
                                NewToppingsOrder thisSauceOrder = Utils.convertToppingAndSauceObjectToToppingsOrder(saucesList.get(selectedSauceIndex));
                                thisSauceOrder.setIsDeal(false);  
                                thisSauceOrder.setProdOrderId((int) firstHalfOrderPId);
                                thisSauceOrder.setDealOrderDetailsId(Constants.DUMMY_ID);   
                                thisSauceOrder.setToppingPrice(Constants.twoDForm.format(generateToppingsPrice(thisSauceOrder)/2.0));
                                dbInstance.open();
                                dbInstance.insertToppingsOrder(thisSauceOrder);
                                dbInstance.close();
                            }
                            
                            // 2nd-half sauce
                            NewToppingsOrder firstHalfSauceOrder = appInstance.getHalfSauce();
                            if(firstHalfSauceOrder.getToppingsId() != null){ 
                                firstHalfSauceOrder.setProdOrderId((int) secondHalfOrderPId);                                  
                                dbInstance.open();
                                dbInstance.insertToppingsOrder(firstHalfSauceOrder);
                                dbInstance.close();
                            }

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(NEW_PizzaDetailsActivity.this);
                            alertDialog.setTitle("Deepslice");
                            alertDialog.setMessage("Your Pizza is added to Cart Successfully");
                            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                } 
                            }); 
                            alertDialog.create().show();  

                        }
                        else{
                            NewToppingsOrder thisSauceOrder = Utils.convertToppingAndSauceObjectToToppingsOrder(saucesList.get(selectedSauceIndex));
                            thisSauceOrder.setToppingPrice(Constants.twoDForm.format(generateToppingsPrice(thisSauceOrder)/2.0));
                            thisSauceOrder.setDealOrderDetailsId(Constants.DUMMY_ID);
                            thisSauceOrder.setIsDeal(false); 
                            
                            NewProductOrder tempOrder = getOrder(Constants.PRODUCT_SELECTION_LEFT);
                            
                            // adding HnH surcharge for 1st-half
                            String partialSelectionSurcharge = appInstance.getPartialSelectionSurcharge();
                            double halfPrice = Double.parseDouble(tempOrder.getPrice()) + Double.parseDouble(partialSelectionSurcharge)/2.0;
                            tempOrder.setPrice(Constants.twoDForm.format(halfPrice));
                            
                            Log.d("HALF PIZZA", "returning from NEW_PizzaDetailsActivity after set half-pizza");

                            appInstance.setHalfOder(tempOrder, selectedCrust, toppingsSelected, thisSauceOrder);
                            AppProperties.isFirstPizzaChosen = true;
                            finish();
                        }
                    }
                    else{
                        NewProductOrder tempOrder = getOrder(Constants.PRODUCT_SELECTION_WHOLE);
                        DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
                        dbInstance.open();
                        long orderPId = dbInstance.insertOrder(tempOrder);                        

                        // inserting toppingsData to the local DB
                        if(toppingsSelected != null){               
                            for(NewToppingsOrder thisToppingsOrder : toppingsSelected){
                                thisToppingsOrder.setIsDeal(false);  
                                thisToppingsOrder.setProdOrderId((int) orderPId);
                                thisToppingsOrder.setDealOrderDetailsId(Constants.DUMMY_ID);    
                                thisToppingsOrder.setToppingPrice(Constants.twoDForm.format(generateToppingsPrice(thisToppingsOrder)));
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
                            thisSauceOrder.setToppingPrice(Constants.twoDForm.format(generateToppingsPrice(thisSauceOrder)));
                            dbInstance.open();
                            dbInstance.insertToppingsOrder(thisSauceOrder);
                            dbInstance.close();
                        }

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NEW_PizzaDetailsActivity.this);
                        alertDialog.setTitle("Deepslice");
                        alertDialog.setMessage("Your Pizza is added to Cart Successfully");
                        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            } 
                        }); 
                        alertDialog.create().show();   
                    }
                }
            }
        });


        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(NEW_PizzaDetailsActivity.this, MyOrderActivity.class);
                startActivity(intent);

            }
        });
    }


    private void generateDefaultToppings() {
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
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


    private void generateCrustInfo() {
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
        dbInstance.open();
        selectedCrust = dbInstance.retrievePizzaCrust(prodCatId, subCatId1, subCatId2);       
        dbInstance.close();
        selectedCrusts.setText(selectedCrust.getSubCatCode());       
    }


    private void generateSauceList(){
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
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
        if (requestCode == SELECT_CRUST) {
            Bundle b = data.getExtras();
            selectedProduct = (Product)b.getSerializable("selectedProduct");
            subCatId2 = selectedProduct.getSubCatID2(); 
            Log.d(TAG, "retrieved selectedProduct from new crust, prodId = " + selectedProduct.getProdID());

            generateCrustInfo();

            generateDefaultToppings();
            generateSauceList();
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
        order.setThumbnailImage(selectedProduct.getThumbnail());
        order.setFullImage(selectedProduct.getFullImage());
        order.setQuantity(String.valueOf(currentCount));
        order.setProdCatName(Constants.PRODUCT_CATEGORY_PIZZA);
        order.setIsCreateByOwn(false);
        order.setSelection(selection); 
        if(selection != Constants.PRODUCT_SELECTION_WHOLE){
            double hnhPrice = Double.parseDouble(selectedProduct.getPrice())/2.0;
            order.setPrice(Constants.twoDForm.format(hnhPrice));
        }
        else
            order.setPrice(selectedProduct.getPrice());
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
            DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
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

        List<String> orderInfo = Utils.OrderInfo(NEW_PizzaDetailsActivity.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);

        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }


        String favCount = Utils.FavCount(NEW_PizzaDetailsActivity.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }
    }
}


