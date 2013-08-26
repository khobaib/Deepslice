package com.deepslice.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
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
import com.deepslice.model.DealOrder;
import com.deepslice.model.Favourite;
import com.deepslice.model.LabelValueBean;
import com.deepslice.model.NewProductOrder;
import com.deepslice.model.NewToppingsOrder;
import com.deepslice.model.Order;
import com.deepslice.model.Product;
import com.deepslice.model.ProductSubCategory;
import com.deepslice.model.ToppingSizes;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.model.ToppingsHashmap;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;
import com.deepslice.utilities.Utils;

public class NEW_PizzaDetailsActivity extends Activity {


    private static final String TAG = NEW_PizzaDetailsActivity.class.getSimpleName();
    //  private static final int REQUEST_CODE_IS_PIZZA_HALF = 1001;

    Boolean isHalf = false;

    TextView favCountTxt;
    int currentCount=1;
    Product selectedProduct;

    ImageLoader imageLoader;

    Spinner spinnerCat;

    int SELECT_TOPPINGS=112233;
    int SELECT_CRUST=112244;

    String crustName="", crustCatId="", crustSubCatId="",couponGroupID,productId="",curentPId;
    boolean isDeal=false;

    TextView selectedToppings,selectedSauces,selectedCrusts;
    TextView tvItemsPrice, tvFavCount;

    DealOrder dealOrder;

    public static List<NewToppingsOrder> toppingsSelected;    
    //    List<HashMap<String, String>> toppingsSelected;
    //HashMap<String, ToppingsHashmap> toppingsSelected;

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
            productId = selectedProduct.getProdID();

            pDesc.setText(selectedProduct.getProdDesc());
            imageLoader.DisplayImage(selectedProduct.getFullImage(), pImage);
            pKJ.setText(selectedProduct.getCaloriesQty()+"kj");
        }

        isDeal = b.getBoolean("isDeal",false);
        if (isDeal){
            RelativeLayout rlCount = (RelativeLayout) findViewById(R.id.rl_count);
            rlCount.setVisibility(View.GONE);
            dealOrder = appInstance.getDealOrder();
            couponGroupID = b.getString("couponGroupID");
            productId = dealOrder.getProdID();
            buttonAddOrders.setText("Add to Deal");
            headerTextView.setText("Add to Deal");
            String defCrusts="";

            for(CrustProducts crustProducts : appInstance.getCouponDetails().getProdAndSubCatID()){
                if(b.getString("prdID").equalsIgnoreCase(crustProducts.getProdID())){
                    defCrusts = crustProducts.getSubCat2Code();
                    break;
                }
            }

            Log.d("...","de...."+defCrusts);
            selectedCrusts.setText(defCrusts);
        }else {
            buttonAddOrders.setText("Add to Order");
            selectedCrusts.setText("");
        }

        ImageView imageViewLogoH=(ImageView)findViewById(R.id.imageViewLogoHalf);


        selectedToppings = (TextView)findViewById(R.id.selectedToppings);
        selectedSauces = (TextView)findViewById(R.id.selectedSauces);

        favCountTxt=(TextView)findViewById(R.id.qVal);
        Button favArrowDown= (Button)findViewById(R.id.buttonMinus);
        favArrowDown.setOnClickListener(new OnClickListener() {
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
        Button favArrowUp= (Button)findViewById(R.id.buttonPlus);
        favArrowUp.setOnClickListener(new OnClickListener() {
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
                Bundle bundle=new Bundle();
                AppProperties.selectedToppings = toppingsSelected;
                if (isDeal){
                    bundle.putSerializable("selectedProduct",appInstance.getDealOrder());
                    bundle.putBoolean("isDeal",isDeal);
                }  else {
                    bundle.putSerializable("selectedProduct",selectedProduct);
                }
                i.putExtras(bundle);
                startActivityForResult(i, SELECT_TOPPINGS);


            }
        });

        final String pId = b.getString("prdID");            // ??? need to check it
        LinearLayout ltCrusts= (LinearLayout)findViewById(R.id.ltCrusts);
        ltCrusts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!(isHalf && AppProperties.isFirstPizzaChosen)){
                    Intent i = new Intent(NEW_PizzaDetailsActivity.this, PizzaCrustActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("catId", crustCatId);
                    bundle.putString("subCatId", crustSubCatId);
                    if(isDeal){
                        bundle.putBoolean("isDeal", true);
                        bundle.putString("prdID", pId);
                    }
                    bundle.putSerializable("selectedProduct",selectedProduct);
                    i.putExtras(bundle);
                    startActivityForResult(i, SELECT_CRUST);
                }
            }
        });

        spinnerCat = (Spinner)findViewById(R.id.spinner1);
        populateCategoryData();

        LinearLayout ltSauces = (LinearLayout)findViewById(R.id.ltSauces);
        ltSauces.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                spinnerCat.performClick();


            }
        });

        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {

                LabelValueBean lvl=(LabelValueBean)spinnerCat.getItemAtPosition(arg2);
                selectedSauces.setText(lvl.getLabel());         
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(NEW_PizzaDetailsActivity.this,FavsListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(NEW_PizzaDetailsActivity.this,MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        Button buttonAddFav= (Button)findViewById(R.id.buttonAddFav);
        buttonAddFav.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
                dbInstance.open();
                boolean favAdded=dbInstance.favAlreadyAdded(selectedProduct.getProdID(),selectedProduct.getDisplayName());
                if(favAdded)
                {
                    Toast.makeText(NEW_PizzaDetailsActivity.this, "Already added to Favourites", Toast.LENGTH_LONG).show();
                }
                else
                {
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

        //populating default crust
        if(!isDeal){

            if(isHalf && AppProperties.isFirstPizzaChosen){
                String halfCrust = appInstance.getHalfCrust();
                selectedCrusts.setText(halfCrust);
            }
            else{
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
                dbInstance.open();
                List<ProductSubCategory> crustList=new ArrayList<ProductSubCategory>();
                if (isDeal){
                    Product allProductsVo=dbInstance.getProductById(dealOrder.getProdID());
                    crustList = dbInstance.retrievePizzaCrustList(allProductsVo.getProdCatID(),allProductsVo.getSubCatID1());
                }else {
                    crustList = dbInstance.retrievePizzaCrustList(selectedProduct.getProdCatID(),selectedProduct.getSubCatID1());
                }
                if(crustList != null && crustList.size()>0 ) {
                    ProductSubCategory crLocal = crustList.get(0);
                    crustName = crLocal.getSubCatDesc();
                    crustCatId = crLocal.getProdCatID();
                    crustSubCatId = crLocal.getSubCatID();

                    selectedCrusts.setText(crustName);
                }
                dbInstance.close();
            }
        }
        ////////////////////

        // populating default toppings

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
        dbInstance.open();
        ArrayList<ToppingsAndSauces> toppingsList = dbInstance.getPizzaToppings(productId);
        dbInstance.close();

        toppingsSelected = new ArrayList<NewToppingsOrder>();
        for (ToppingsAndSauces thisToppings : toppingsList) {
            if("True".equalsIgnoreCase(thisToppings.getIsFreeWithPizza())){
                //                HashMap<String, String> thisToppingsInfo = new HashMap<String, String>();
                NewToppingsOrder thisToppingsOrder = new NewToppingsOrder(thisToppings.getToppingID(),
                        Constants.SINGLE_SIZE_TOPPING_ID, thisToppings.getIsSauce().equalsIgnoreCase("true"), thisToppings.getOwnPrice(),
                        thisToppings.getIsFreeWithPizza().equalsIgnoreCase("true"));
                toppingsSelected.add(thisToppingsOrder);
            }
        }


        String tempList="";
        for(NewToppingsOrder thisToppingsOrder : toppingsSelected){
            dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
            dbInstance.open();
            ToppingSizes toppingsSize = dbInstance.retrieveToppingSizeById(thisToppingsOrder.getToppingSizeId());
            dbInstance.close();
            tempList+=toppingsSize.getToppingSizeCode() + ",";
        }

        //        for(HashMap<String, String> thisToppings : toppingsSelected){
        //            Map.Entry<String, String> m = (Entry<String, String>) thisToppings.entrySet();
        //            String toppingsId = m.getKey();
        //            String toppingSizeId = m.getValue();
        //            tempList+=toppingSizeId + ",";
        //        }

        //        Iterator it = toppingsSelected.entrySet().iterator();
        //        while (it.hasNext()) {
        //            Map.Entry pairs = (Map.Entry)it.next();
        //            ToppingsHashmap gm = (ToppingsHashmap)pairs.getValue();
        //            tempList+=gm.getToppingCode()+",";
        //        }

        selectedToppings.setText(AppProperties.trimLastComma(tempList));





        buttonAddOrders.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                NewProductOrder tempOrder = getOrder();

                DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
                dbInstance.open();
                if (isDeal){
                    dealOrder.setQuantity(String.valueOf(currentCount));
                    dealOrder.setProdID(crustCatId);

                    if(dbInstance.isDealGroupAlreadySelected(dealOrder.getCouponID(), dealOrder.getCouponGroupID())){
                        Log.d(TAG, "YES");
                        boolean b = dbInstance.deleteAlreadySelectedDealGroup(dealOrder.getCouponID(), dealOrder.getCouponGroupID());
                        Log.d(TAG, "delete already selected deal? = " + b);
                    }
                    dbInstance.insertDealOrder(dealOrder);
                }
                else {
                    if(isHalf){
                        if(AppProperties.isFirstPizzaChosen){
                            Log.d("HALF PIZZA", "adding orders to cart");
                            NewProductOrder firstHalfOrder = appInstance.getHalfOder();
                            long firstHalfOrderPId = dbInstance.insertOrder(firstHalfOrder);         // first half
                            long secondHalfOrderPId = dbInstance.insertOrder(tempOrder);             // 2nd half
                            AppProperties.isFirstPizzaChosen = false;
                        }
                        else{
                            Log.d("HALF PIZZA", "returning from NEW_PizzaDetailsActivity after set half-pizza");
                            String halfCrust = selectedCrusts.getText().toString();
                            appInstance.setHalfOder(tempOrder, halfCrust);
                            AppProperties.isFirstPizzaChosen = true;
                        }
                    }
                    else{
                        long orderPId = dbInstance.insertOrder(tempOrder);
                        Toast.makeText(NEW_PizzaDetailsActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();

                        if(toppingsSelected != null){
                            for(NewToppingsOrder thisToppingsOrder : toppingsSelected){
                                thisToppingsOrder.setProdOrderId((int) orderPId);
                                thisToppingsOrder.setToppingPrice(String.valueOf(generateToppingsPrice(thisToppingsOrder)));
                                dbInstance.open();
                                dbInstance.insertToppingsOrder(thisToppingsOrder);
                                dbInstance.close();
                            }
                        }



                    }

                }
                dbInstance.close();
                finish();
            }
        });
        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(NEW_PizzaDetailsActivity.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });
    }


    //    private ToppingsHashmap getHashBean(ToppingsAndSauces toppingsAndSauces, String toppingSize) {
    //
    //        ToppingsHashmap hMap=new ToppingsHashmap();
    //
    //        hMap.setToppingCode(toppingsAndSauces.getToppingCode());
    //        hMap.setToppingDesc(toppingsAndSauces.getToppingDesc());
    //        hMap.setToppingID(toppingsAndSauces.getToppingID());
    //        hMap.setToppingPrice(toppingsAndSauces.getOwnPrice());
    //        hMap.setToppingSize(toppingSize);
    //        hMap.setIsFreeWithPizza(toppingsAndSauces.getIsFreeWithPizza());
    //
    //        return hMap;
    //    }


    private void populateCategoryData(){
        ArrayList<ToppingsAndSauces> saucesList=null;

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
        dbInstance.open();
        saucesList=dbInstance.getPizzaSauces(selectedProduct.getProdID());
        dbInstance.close();

        try{

            int i=0;
            LabelValueBean[] albums= new LabelValueBean[saucesList.size()];
            for (ToppingsAndSauces sBean : saucesList) {
                albums[i++]=new LabelValueBean(sBean.getToppingCode(), sBean.getToppingID() ) ;
            }

            ArrayAdapter<Object> spinnerArrayAdapter = new ArrayAdapter<Object>(this,
                    android.R.layout.simple_spinner_item, albums);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerCat.setAdapter(spinnerArrayAdapter);  



        }catch (Exception e) {
            e.printStackTrace();
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

            String tempList = "";
            for(NewToppingsOrder thisToppingsOrder : toppingsSelected){
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
                dbInstance.open();
                ToppingSizes toppingsSize = dbInstance.retrieveToppingSizeById(thisToppingsOrder.getToppingSizeId());
                dbInstance.close();
                tempList+=toppingsSize.getToppingSizeCode() + ",";
            }

            selectedToppings.setText(AppProperties.trimLastComma(tempList));

        }
        if (requestCode == SELECT_CRUST) {

            String namesList = data.getStringExtra("name"); 
            crustCatId = data.getStringExtra("catId"); 
            crustSubCatId = data.getStringExtra("subCatId");
            selectedCrusts.setText(namesList);
        }
    }


    private NewProductOrder getOrder() {
        // this activity is for whole/left/right pizza ordering, create your own is in different activity
        // so we set isCreateYourOwn = false
        // currently lets take all pizza as WHOLE, will fix it asap

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
        order.setIsCreateByOwn(false);
        order.setSelection(Constants.PRODUCT_SELECTION_WHOLE);            // will change it


        //      Order f = new Order();
        //      f.setProdCatID(selectedProduct.getProdCatID());
        //      f.setSubCatID1(selectedProduct.getSubCatID1());
        //      f.setSubCatID2(selectedProduct.getSubCatID2());
        //      f.setProdID(selectedProduct.getProdID());
        //      f.setProdCode(selectedProduct.getProdCode());
        //      f.setDisplayName(selectedProduct.getDisplayName());
        //      f.setProdAbbr(selectedProduct.getProdAbbr());
        //      f.setProdDesc(selectedProduct.getProdDesc());
        //      f.setIsAddDeliveryAmount(selectedProduct.getIsAddDeliveryAmount());
        //      f.setDisplaySequence(selectedProduct.getDisplaySequence());
        //      f.setCaloriesQty(selectedProduct.getCaloriesQty());
        //
        //      double finalPrice=generateTotalPrice();
        //
        //      if(isHalf)
        //          finalPrice = finalPrice/2;
        //
        //      finalPrice=AppProperties.roundTwoDecimals(finalPrice);
        //
        //      f.setPrice(String.valueOf(finalPrice));
        //
        //      f.setThumbnail(selectedProduct.getThumbnail());
        //      f.setFullImage(selectedProduct.getFullImage());
        //
        //      f.setQuantity(String.valueOf(currentCount));
        //
        //      f.setCrust("");
        //      f.setSauce("");
        //      f.setToppings("");
        //
        //      f.setProdCatName(Constants.PRODUCT_CATEGORY_PIZZA);
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

    //    private double generateTotalPrice() {
    //
    //        double totalPrice=Double.parseDouble(selectedProduct.getPrice());
    //
    //        totalPrice = currentCount*totalPrice;
    //        double tempDoublePrice;
    //
    //        DeepsliceDatabase dbInstance = new DeepsliceDatabase(NEW_PizzaDetailsActivity.this);
    //        dbInstance.open();
    //        String tempPrice="0.00";
    //        Iterator it = toppingsSelected.entrySet().iterator();
    //        while (it.hasNext()) {
    //            Map.Entry pairs = (Map.Entry)it.next();
    //            ToppingsHashmap gm=(ToppingsHashmap)pairs.getValue();
    //
    //            if("False".equalsIgnoreCase(gm.getIsFreeWithPizza())){
    //                tempPrice=dbInstance.getToppingPrice(gm.getToppingID(),gm.getToppingSize());
    //            }
    //            tempDoublePrice=Double.parseDouble(tempPrice);
    //            totalPrice=totalPrice+tempDoublePrice;
    //        }
    //        dbInstance.close();
    //        return totalPrice;
    //    }



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