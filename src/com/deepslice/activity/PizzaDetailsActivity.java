package com.deepslice.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.AllProducts;
import com.deepslice.model.DealOrder;
import com.deepslice.model.Favourites;
import com.deepslice.model.LabelValueBean;
import com.deepslice.model.Order;
import com.deepslice.model.ProdAndSubCategory;
import com.deepslice.model.SubCategoryVo;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.model.ToppingsHashmap;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.DeepsliceApplication;

public class PizzaDetailsActivity extends Activity{

    private static final int REQUEST_CODE_IS_PIZZA_HALF = 1001;

    Boolean isHalf = false;

    TextView favCountTxt;
    int currentCount=1;
    AllProducts selectedBean;

    ImageLoader imageLoader;

    Spinner spinnerCat;

    int SELECT_TOPPINGS=112233;
    int SELECT_CRUST=112244;

    String crustName="", crustCatId="", crustSubCatId="",couponGroupID,productId="",curentPId;
    boolean isDeal=false;
    TextView selectedToppings,selectedSauces,selectedCrusts;
    DealOrder dealOrderVo;
    HashMap<String, ToppingsHashmap> toppingsSelected;
    DeepsliceApplication appInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pizza_details);

        imageLoader = new ImageLoader(PizzaDetailsActivity.this);
        appInstance=(DeepsliceApplication)getApplication();

        Bundle b = this.getIntent().getExtras();
        isHalf = b.getBoolean("isHalf", false);

        ImageView ivHideLeftHalf = (ImageView) findViewById(R.id.iv_hide_left_half);
        ImageView ivHideRightHalf = (ImageView) findViewById(R.id.iv_hide_right_half);

        if(isHalf){
            if(AppProperties.isFirstPizzaChosen){
                ivHideLeftHalf.setVisibility(View.VISIBLE);
            }
            else{
                ivHideRightHalf.setVisibility(View.VISIBLE);
            }
        }

        Button buttonAddOrders= (Button)findViewById(R.id.buttonAddOrders);
        TextView pDesc=(TextView)findViewById(R.id.pDesc);
        ImageView pImage = (ImageView) findViewById(R.id.imageView1);
        TextView pKJ=(TextView)findViewById(R.id.pKJ);
        TextView headerTextView=(TextView)findViewById(R.id.headerTextView);
        selectedCrusts=(TextView)findViewById(R.id.selectedCrust);
        if(b.containsKey("selectedProduct")){
            selectedBean=(AllProducts)b.getSerializable("selectedProduct");
            productId=selectedBean.getProdID();
            pDesc.setText(selectedBean.getProdDesc());
            imageLoader.DisplayImage(selectedBean.getFullImage(), pImage);
            pKJ.setText(selectedBean.getCaloriesQty()+"kj");
        }

        isDeal=b.getBoolean("isDeal",false);
        if (isDeal){
            RelativeLayout rlCount = (RelativeLayout) findViewById(R.id.rl_count);
            rlCount.setVisibility(View.GONE);
            dealOrderVo=appInstance.getDealOrderVo();
            couponGroupID=b.getString("couponGroupID");
            productId=dealOrderVo.getProdID();
            buttonAddOrders.setText("Add to Deal");
            headerTextView.setText("Add to Deal");
            String defCrusts="";
            for(ProdAndSubCategory subCategory:appInstance.getCouponData().getProdAndSubCategories()){
                if(b.getString("prdID").equalsIgnoreCase(subCategory.getProdID())){
                    defCrusts=subCategory.getSubCat2Code();
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
        //        if (b.getBoolean("isHalf",false)){
        //            imageViewLogoH.setVisibility(View.VISIBLE);
        //        }else {
        //            imageViewLogoH.setVisibility(View.INVISIBLE);
        //        }



        selectedToppings=(TextView)findViewById(R.id.selectedToppings);
        selectedSauces=(TextView)findViewById(R.id.selectedSauces);



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

        LinearLayout ltToppings= (LinearLayout)findViewById(R.id.ltToppings);
        ltToppings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i=new Intent(PizzaDetailsActivity.this,PizzaToppingsActivity.class);
                Bundle bundle=new Bundle();
                AppProperties.selectedToppings=toppingsSelected;
                if (isDeal){
                    bundle.putSerializable("selectedProduct",appInstance.getDealOrderVo());
                    bundle.putBoolean("isDeal",isDeal);
                }  else {
                    bundle.putSerializable("selectedProduct",selectedBean);
                }
                //  bundle.putSerializable("selectedProduct",selectedBean);
                i.putExtras(bundle);
                startActivityForResult(i, SELECT_TOPPINGS);


            }
        });

        LinearLayout ltCrusts= (LinearLayout)findViewById(R.id.ltCrusts);
        final String pId=b.getString("prdID");
        ltCrusts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isDeal){
                    Intent i=new Intent(PizzaDetailsActivity.this,PizzaCrustActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putBoolean("isDeal", true);
                    bundle.putString("prdID",pId);
                    bundle.putString("catId", crustCatId);
                    bundle.putString("subCatId", crustSubCatId);
                    bundle.putSerializable("selectedProduct",selectedBean);
                    i.putExtras(bundle);
                    startActivityForResult(i, SELECT_CRUST);
                } else {
                    Intent i=new Intent(PizzaDetailsActivity.this,PizzaCrustActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("catId", crustCatId);
                    bundle.putString("subCatId", crustSubCatId);
                    bundle.putSerializable("selectedProduct",selectedBean);
                    i.putExtras(bundle);
                    startActivityForResult(i, SELECT_CRUST);
                }

            }
        });

        spinnerCat=(Spinner)findViewById(R.id.spinner1);
        populateCategoryData();

        LinearLayout ltSauces= (LinearLayout)findViewById(R.id.ltSauces);
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

                Intent intent=new Intent(PizzaDetailsActivity.this,FavsListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PizzaDetailsActivity.this,MenuActivity.class);
                startActivity(intent);

            }
        });

        Button buttonAddFav= (Button)findViewById(R.id.buttonAddFav);
        buttonAddFav.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaDetailsActivity.this);
                dbInstance.open();
                boolean favAdded=dbInstance.favAlreadyAdded(selectedBean.getProdID(),selectedBean.getDisplayName());
                if(favAdded)
                {
                    Toast.makeText(PizzaDetailsActivity.this, "Already added to Favourites", Toast.LENGTH_LONG).show();
                }
                else
                {
                    dbInstance.insertFav(getFavBean());
                    doResumeWork();
                    Toast.makeText(PizzaDetailsActivity.this, "Successfully added to Favourites", Toast.LENGTH_LONG).show();
                }
                dbInstance.close();



                //                AppDao dao=null;
                //                try {
                //                    dao=AppDao.getSingleton(getApplicationContext());
                //                    dao.openConnection();
                //                    boolean favAdded=dao.favAlreadyAdded(selectedBean.getProdID(),selectedBean.getDisplayName());
                //                    if(favAdded)
                //                    {
                //                        Toast.makeText(PizzaDetailsActivity.this, "Already added to Favourites", Toast.LENGTH_LONG).show();
                //                    }
                //                    else
                //                    {
                //                        dao.insertFav(getFavBean());
                //                        doResumeWork();
                //                        Toast.makeText(PizzaDetailsActivity.this, "Successfully added to Favourites", Toast.LENGTH_LONG).show();
                //                    }
                //
                //
                //                } catch (Exception ex)
                //                {
                //                    System.out.println(ex.getMessage());
                //                }finally{
                //                    if(null!=dao)
                //                        dao.closeConnection();
                //                }
            }


            private Favourites getFavBean() {

                Favourites f = new Favourites();
                f.setProdCatID(selectedBean.getProdCatID());
                f.setSubCatID1(selectedBean.getSubCatID1());
                f.setSubCatID2(selectedBean.getSubCatID2());
                f.setProdID(selectedBean.getProdID());
                f.setProdCode(selectedBean.getProdCode());
                f.setDisplayName(selectedBean.getDisplayName());
                f.setProdAbbr(selectedBean.getProdAbbr());
                f.setProdDesc(selectedBean.getProdDesc());
                f.setIsAddDeliveryAmount(selectedBean.getIsAddDeliveryAmount());
                f.setDisplaySequence(selectedBean.getDisplaySequence());
                f.setCaloriesQty(selectedBean.getCaloriesQty());
                f.setPrice(selectedBean.getPrice());
                f.setThumbnail(selectedBean.getThumbnail());
                f.setFullImage(selectedBean.getFullImage());
                f.setCustomName(selectedBean.getDisplayName());
                f.setProdCatName("Pizza");
                return f;
            }
        });

        //populating default crust
        AppDao dao=null;
        if(!isDeal){

            //            try {

            DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaDetailsActivity.this);
            dbInstance.open();
            ArrayList<SubCategoryVo> crustList=new ArrayList<SubCategoryVo>();
            if (isDeal){
                AllProducts allProductsVo=dbInstance.getProductById(dealOrderVo.getProdID());
                crustList = dbInstance.getPizzaCrusts(allProductsVo.getProdCatID(),allProductsVo.getSubCatID1());
            }else {
                crustList = dbInstance.getPizzaCrusts(selectedBean.getProdCatID(),selectedBean.getSubCatID1());
            }
            if(crustList != null && crustList.size()>0 ) {
                SubCategoryVo crLocal = crustList.get(0);
                crustName=crLocal.getSubCatDesc();
                crustCatId=crLocal.getProdCatID();
                crustSubCatId=crLocal.getSubCatID();

                selectedCrusts.setText(crustName);
            }
            dbInstance.close();


            //                dao=AppDao.getSingleton(getApplicationContext());
            //                dao.openConnection();
            //
            //                ArrayList<SubCategoryVo> crustList=new ArrayList<SubCategoryVo>();
            //                if (isDeal){
            //                    AllProductsVo allProductsVo=dao.getProductById(dealOrderVo.getProdID());
            //                    crustList = dao.getPizzaCrusts(allProductsVo.getProdCatID(),allProductsVo.getSubCatID1());
            //                }else {
            //                    crustList = dao.getPizzaCrusts(selectedBean.getProdCatID(),selectedBean.getSubCatID1());
            //                }
            //                if(crustList != null && crustList.size()>0 )
            //                {
            //                    SubCategoryVo crLocal = crustList.get(0);
            //                    crustName=crLocal.getSubCatDesc();
            //                    crustCatId=crLocal.getProdCatID();
            //                    crustSubCatId=crLocal.getSubCatID();
            //
            //                    selectedCrusts.setText(crustName);
            //                }
            //            } catch (Exception ex)
            //            {
            //                System.out.println(ex.getMessage());
            //            }finally{
            //                if(null!=dao)
            //                    dao.closeConnection();
            //            }
        }
        ////////////////////

        // populating default toppings

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaDetailsActivity.this);
        dbInstance.open();
        toppingsSelected=new HashMap<String, ToppingsHashmap>();
        ArrayList<ToppingsAndSauces> toppingsList = dbInstance.getPizzaToppings(productId);
        for (ToppingsAndSauces toppingsAndSaucesVo : toppingsList) {
            if("True".equalsIgnoreCase(toppingsAndSaucesVo.getIsFreeWithPizza()))
                toppingsSelected.put(toppingsAndSaucesVo.getToppingID(), getHashBean(toppingsAndSaucesVo,"Single"));
        }
        dbInstance.close();

        //        dao=null;
        //        try {
        //            dao=AppDao.getSingleton(getApplicationContext());
        //            dao.openConnection();
        //            toppingsSelected=new HashMap<String, ToppingsHashmap>();
        //            ArrayList<ToppingsAndSauces> toppingsList = dao.getPizzaToppings(productId);
        //            for (ToppingsAndSauces toppingsAndSaucesVo : toppingsList) {
        //                if("True".equalsIgnoreCase(toppingsAndSaucesVo.getIsFreeWithPizza()))
        //                    toppingsSelected.put(toppingsAndSaucesVo.getToppingID(), getHashBean(toppingsAndSaucesVo,"Single"));
        //            }
        //        } catch (Exception ex)
        //        {
        //            System.out.println(ex.getMessage());
        //        }finally{
        //            if(null!=dao)
        //                dao.closeConnection();
        //        }

        String tempList="";
        Iterator it = toppingsSelected.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            ToppingsHashmap gm=(ToppingsHashmap)pairs.getValue();
            tempList+=gm.getToppingCode()+",";
        }

        selectedToppings.setText(AppProperties.trimLastComma(tempList));





        buttonAddOrders.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Order tempOrderBean = getOrderBean();

                DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaDetailsActivity.this);
                dbInstance.open();
                if (isDeal){
                    dealOrderVo.setQuantity(String.valueOf(currentCount));
                    dealOrderVo.setProdID(crustCatId);
                    AppSharedPreference.putBoolean(PizzaDetailsActivity.this, couponGroupID, true);
                    if(dbInstance.isDealProductAvailable(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID())){
                        boolean b= dbInstance.deleteDuplicateDealOrderRec(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID());
                        dbInstance.resetDealOrder(dealOrderVo.getCouponID());
                    }
                    dbInstance.insertDealOrder(dealOrderVo);
                    if(dbInstance.getDealOrderCount(dealOrderVo.getCouponID())==AppSharedPreference.getInteger(PizzaDetailsActivity.this,"numDeals",0)){
                        AppSharedPreference.putBoolean(PizzaDetailsActivity.this,dealOrderVo.getCouponID(),true);
                        Toast.makeText(PizzaDetailsActivity.this, "complete your deal by tapping GET A DEAL", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(PizzaDetailsActivity.this, "Select product from deal groups", Toast.LENGTH_LONG).show();
                    }
                    //                    finish();
                }
                else {
                    if(isHalf){
                        if(AppProperties.isFirstPizzaChosen){
                            Log.d("HALF PIZZA", "adding orders to cart");
                            Order firstHalfOrder = appInstance.getHalfOder();
                            dbInstance.insertOrder(firstHalfOrder);
                            dbInstance.insertOrder(tempOrderBean);
                            AppProperties.isFirstPizzaChosen = false;
                            //                            finish();
                        }
                        else{
                            Log.d("HALF PIZZA", "returning from PizzaDetailsActivity after set half-pizza");
                            appInstance.setHalfOder(tempOrderBean);
                            AppProperties.isFirstPizzaChosen = true;
                            //                            finish();
                        }
                    }
                    else{
                        dbInstance.insertOrder(tempOrderBean);
                        Toast.makeText(PizzaDetailsActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
                        //                        finish();
                    }

                }
                dbInstance.close();
                finish();


                //                AppDao dao=null;
                //                try {
                //                    dao=AppDao.getSingleton(getApplicationContext());
                //                    dao.openConnection();
                //                    if (isDeal){
                //                        dealOrderVo.setQuantity(String.valueOf(currentCount));
                //                        dealOrderVo.setProdID(crustCatId);
                //                        AppSharedPreference.putBoolean(PizzaDetailsActivity.this, couponGroupID, true);
                //                        if(dao.isDealProductAvailable(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID())){
                //                            boolean b= dao.deleteDuplicateDealOrderRec(dealOrderVo.getCouponGroupID(),dealOrderVo.getCouponID());
                //                            dao.resetDealOrder(dealOrderVo.getCouponID());
                //                        }
                //                        dao.insertDealOrder(dealOrderVo);
                //                        if(dao.getDealOrderCount(dealOrderVo.getCouponID())==AppSharedPreference.getInteger(PizzaDetailsActivity.this,"numDeals",0)){
                //                            AppSharedPreference.putBoolean(PizzaDetailsActivity.this,dealOrderVo.getCouponID(),true);
                //                            Toast.makeText(PizzaDetailsActivity.this, "complete your deal by tapping GET A DEAL", Toast.LENGTH_LONG).show();
                //                        }else {
                //                            Toast.makeText(PizzaDetailsActivity.this, "Select product from deal groups", Toast.LENGTH_LONG).show();
                //                        }
                //                        finish();
                //                    }
                //                    else {
                //                        if(isHalf){
                //                            if(AppProperties.isFirstPizzaChosen){
                //                                Log.d("HALF PIZZA", "adding orders to cart");
                //                                Order firstHalfOrder = appInstance.getHalfOder();
                //                                dao.insertOrder(firstHalfOrder);
                //                                dao.insertOrder(tempOrderBean);
                //                                AppProperties.isFirstPizzaChosen = false;
                //                                finish();
                //                            }
                //                            else{
                //                                Log.d("HALF PIZZA", "returning from PizzaDetailsActivity after set half-pizza");
                //                                appInstance.setHalfOder(tempOrderBean);
                //                                AppProperties.isFirstPizzaChosen = true;
                //                                finish();
                //                            }
                //                        }
                //                        else{
                //                            dao.insertOrder(tempOrderBean);
                //                            Toast.makeText(PizzaDetailsActivity.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
                //                            finish();
                //                        }
                //
                //                    }
                //
                //                } catch (Exception ex)
                //                {
                //                    System.out.println(ex.getMessage());
                //                }finally{
                //                    if(null!=dao)
                //                        dao.closeConnection();
                //                }
            }
        });
        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PizzaDetailsActivity.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });
    }
    private ToppingsHashmap getHashBean(
            ToppingsAndSauces toppingsAndSaucesVo, String toppingSize) {

        ToppingsHashmap hMap=new ToppingsHashmap();

        hMap.setToppingCode(toppingsAndSaucesVo.getToppingCode());
        hMap.setToppingDesc(toppingsAndSaucesVo.getToppingDesc());
        hMap.setToppingID(toppingsAndSaucesVo.getToppingID());
        hMap.setToppingPrice(toppingsAndSaucesVo.getOwnPrice());
        hMap.setToppingSize(toppingSize);

        return hMap;
    }
    private void populateCategoryData(){
        ArrayList<ToppingsAndSauces> saucesList=null;
        //        AppDao dao=null;
        //        try {
        //            dao=AppDao.getSingleton(getApplicationContext());
        //            dao.openConnection();
        //
        //            saucesList=dao.getPizzaSauces(selectedBean.getProdID());
        //
        //        } catch (Exception ex)
        //        {
        //            System.out.println(ex.getMessage());
        //        }finally{
        //            if(null!=dao)
        //                dao.closeConnection();
        //        }

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaDetailsActivity.this);
        dbInstance.open();
        saucesList=dbInstance.getPizzaSauces(selectedBean.getProdID());
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
            String tempList="";
            toppingsSelected=AppProperties.selectedToppings;
            if(toppingsSelected==null)
                toppingsSelected=new HashMap<String, ToppingsHashmap>();

            Iterator it = toppingsSelected.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                ToppingsHashmap gm=(ToppingsHashmap)pairs.getValue();
                tempList+=gm.getToppingCode()+",";
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

    private Order getOrderBean() {

        Order f = new Order();
        f.setProdCatID(selectedBean.getProdCatID());
        f.setSubCatID1(selectedBean.getSubCatID1());
        f.setSubCatID2(selectedBean.getSubCatID2());
        f.setProdID(selectedBean.getProdID());
        f.setProdCode(selectedBean.getProdCode());
        f.setDisplayName(selectedBean.getDisplayName());
        f.setProdAbbr(selectedBean.getProdAbbr());
        f.setProdDesc(selectedBean.getProdDesc());
        f.setIsAddDeliveryAmount(selectedBean.getIsAddDeliveryAmount());
        f.setDisplaySequence(selectedBean.getDisplaySequence());
        f.setCaloriesQty(selectedBean.getCaloriesQty());

        double finalPrice=generateTotalPrice();

        if(isHalf)
            finalPrice = finalPrice/2;

        finalPrice=AppProperties.roundTwoDecimals(finalPrice);

        f.setPrice(String.valueOf(finalPrice));

        f.setThumbnail(selectedBean.getThumbnail());
        f.setFullImage(selectedBean.getFullImage());

        f.setQuantity(String.valueOf(currentCount));

        f.setCrust("");
        f.setSauce("");
        f.setToppings("");

        f.setProdCatName("Pizza");
        return f;
    }

    private double generateTotalPrice() {

        double totalPrice=Double.parseDouble(selectedBean.getPrice());

        totalPrice=currentCount*totalPrice;
        double tempDoublePrice;

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaDetailsActivity.this);
        dbInstance.open();
        String tempPrice="0.00";
        Iterator it = toppingsSelected.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            ToppingsHashmap gm=(ToppingsHashmap)pairs.getValue();

            if("False".equalsIgnoreCase(gm.getIsFreeWithPizza())){
                tempPrice=dbInstance.getToppingPrice(gm.getToppingID(),gm.getToppingSize());
            }
            tempDoublePrice=Double.parseDouble(tempPrice);
            totalPrice=totalPrice+tempDoublePrice;
        }
        dbInstance.close();


        //        AppDao dao=null;
        //        try {
        //            dao=AppDao.getSingleton(getApplicationContext());
        //            dao.openConnection();
        //
        //            String tempPrice="0.00";
        //            Iterator it = toppingsSelected.entrySet().iterator();
        //            while (it.hasNext()) {
        //                Map.Entry pairs = (Map.Entry)it.next();
        //                ToppingsHashmapVo gm=(ToppingsHashmapVo)pairs.getValue();
        //
        //                if("False".equalsIgnoreCase(gm.getIsFreeWithPizza())){
        //                    tempPrice=dao.getToppingPrice(gm.getToppingID(),gm.getToppingSize());
        //                }
        //                tempDoublePrice=Double.parseDouble(tempPrice);
        //                totalPrice=totalPrice+tempDoublePrice;
        //            }
        //
        //        } catch (Exception ex)
        //        {
        //            System.out.println(ex.getMessage());
        //        }finally{
        //            if(null!=dao)
        //                dao.closeConnection();
        //        }

        return totalPrice;
    }
    @Override
    protected void onResume() {

        doResumeWork();

        super.onResume();
    }
    private void doResumeWork() {
        // //////////////////////////////////////////////////////////////////////////////
        // here we calculate total pricing of already ordered item(Deal+normal order)
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaDetailsActivity.this);
        dbInstance.open();
        ArrayList<String> orderInfo = dbInstance.getOrderInfo();
        ArrayList<DealOrder>dealOrderVos1= dbInstance.getDealOrdersList();
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



        //        AppDao dao = null;
        //        try {
        //            dao = AppDao.getSingleton(getApplicationContext());
        //            dao.openConnection();
        //            // dao.updateDealOrder();
        //            ArrayList<String> orderInfo = dao.getOrderInfo();
        //            ArrayList<DealOrder>dealOrderVos1= dao.getDealOrdersList();
        //            TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
        //            double tota=0.00;
        //            int dealCount=0;
        //            if((dealOrderVos1!=null && dealOrderVos1.size()>0)){
        //                dealCount=dealOrderVos1.size();
        //                for (int x=0;x<dealOrderVos1.size();x++){
        //                    tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice()))*(Integer.parseInt(dealOrderVos1.get(x).getQuantity()));
        //                }
        //            }
        //
        //            int orderInfoCount= 0;
        //            double  orderInfoTotal=0.0;
        //            if ((null != orderInfo && orderInfo.size() == 2) ) {
        //                orderInfoCount=Integer.parseInt(orderInfo.get(0));
        //                orderInfoTotal=Double.parseDouble(orderInfo.get(1));
        //            }
        //            int numPro=orderInfoCount+dealCount;
        //            double subTotal=orderInfoTotal+tota;
        //            DecimalFormat twoDForm = new DecimalFormat("#.##");
        //            subTotal= Double.valueOf(twoDForm.format(subTotal));
        //            if(numPro>0){
        //                itemsPrice.setText(numPro+" Items "+"\n$" +subTotal );
        //                itemsPrice.setVisibility(View.VISIBLE);
        //            }
        //
        //            else{
        //                itemsPrice.setVisibility(View.INVISIBLE);
        //
        //            }
        //
        //            TextView favCount = (TextView) findViewById(R.id.favCount);
        //            String fvs=dao.getFavCount();
        //            if (null != fvs && !fvs.equals("0")) {
        //                favCount.setText(fvs);
        //                favCount.setVisibility(View.VISIBLE);
        //            }
        //            else{
        //                favCount.setVisibility(View.INVISIBLE);
        //            }
        //
        //
        //
        //        } catch (Exception ex) {
        //            System.out.println(ex.getMessage());
        //        } finally {
        //            if (null != dao)
        //                dao.closeConnection();
        //        }
        // ///////////////////////////////////////////////////////////////////////


    }
}
