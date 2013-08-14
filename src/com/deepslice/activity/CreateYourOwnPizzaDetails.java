package com.deepslice.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.cache.ImageLoader;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.CreateOwnPizzaData;
import com.deepslice.model.DealOrder;
import com.deepslice.model.Favourite;
import com.deepslice.model.LabelValueBean;
import com.deepslice.model.Order;
import com.deepslice.model.Product;
import com.deepslice.model.ServerResponse;
import com.deepslice.model.ToppingPrices;
import com.deepslice.model.ToppingSizes;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.model.ToppingsHashmap;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 6/5/13
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateYourOwnPizzaDetails extends Activity {

    int currentIndex;
    int currentCount;

    ImageLoader imageLoader;

    int SELECT_TOPPINGS=112233;
    int SELECT_CRUST=112244;

    Spinner spinnerCat;

    ProgressDialog pd;

    boolean syncedPrices = false;
    boolean syncedToppings = false;

    HashMap<String, ToppingsHashmap> toppingsSelected;

    CreateOwnPizzaData selectedPizzaData;
    List<String> prodIds;
    ArrayList<Product> productList;

    //    private ProgressDialog pDialog;
    JsonParser jsonParser = new JsonParser();
    
    List<ToppingPrices> toppingsPriceList;
    List<ToppingSizes> toppingsSizeList;
    List<ToppingsAndSauces> toppingsAndSaucesList;

    ImageButton ImageButtonNext, ImageButtonPrv;
    TextView PizzaName;
    ImageView ImageViewCreateYourOwn;
    Button buttonAddOrders;
    TextView pKJ, headerTextView;
    TextView selectedToppings,selectedSauces;
    TextView favCountTxt;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_your_own);

        selectedPizzaData = (CreateOwnPizzaData) getIntent().getExtras().getSerializable("selected_pizza");
        prodIds = selectedPizzaData.getProdIds();

        currentIndex = 0;
        currentCount = 1;
        imageLoader = new ImageLoader(CreateYourOwnPizzaDetails.this);

        productList = new ArrayList<Product>();
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
        dbInstance.open();
        for (int i=0;i<prodIds.size();i++){
            String prodId = prodIds.get(i);
            productList.add(dbInstance.getProductById(prodId));
        }
        dbInstance.close();       

        findViewById(R.id.rl_crust).setVisibility(View.GONE);

        PizzaName = (TextView) findViewById(R.id.pDesc);
        PizzaName.setText(productList.get(0).getProdDesc());

        buttonAddOrders= (Button)findViewById(R.id.buttonAddOrders);
        buttonAddOrders.setText("Add to Order");

        pKJ = (TextView)findViewById(R.id.pKJ);
        pKJ.setText(productList.get(0).getCaloriesQty()+"kj");

        ImageViewCreateYourOwn =(ImageView)findViewById(R.id.imageViewCreateYourOwn);
        imageLoader.DisplayImage(productList.get(0).getFullImage(), ImageViewCreateYourOwn);

        headerTextView=(TextView)findViewById(R.id.headerTextView);
        selectedToppings=(TextView)findViewById(R.id.selectedToppings);
        selectedSauces=(TextView)findViewById(R.id.selectedSauces);
        spinnerCat=(Spinner)findViewById(R.id.spinner1);

        // update topping list
        updateTopingSaucesData(productList.get(0).getProdID());
        // update sauce spinner

        //        populateSauceData(0);

        ImageButtonNext=(ImageButton)findViewById(R.id.imageButtonNext);
        if(productList.size() > 1)
            ImageButtonNext.setVisibility(View.VISIBLE);

        ImageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex++;
                imageLoader.DisplayImage(productList.get(currentIndex).getFullImage(), ImageViewCreateYourOwn);
                PizzaName.setText(productList.get(currentIndex).getProdDesc());
                updateTopingSaucesData(productList.get(currentIndex).getProdID());
                //                populateSauceData(currentIndex);
                if(currentIndex < (productList.size()-1)){
                    ImageButtonNext.setVisibility(View.VISIBLE);
                    ImageButtonPrv.setVisibility(View.VISIBLE);
                    Log.d("............",""+currentIndex);

                    //                    imageViewCreateYourOwn.setImageResource(imageSet[currentIndex]);
                } else {
                    ImageButtonNext.setVisibility(View.GONE);

                }

            }
        });




        ImageButtonPrv=(ImageButton)findViewById(R.id.imageButtonPriv);
        ImageButtonPrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex--;
                imageLoader.DisplayImage(productList.get(currentIndex).getFullImage(), ImageViewCreateYourOwn);
                PizzaName.setText(productList.get(currentIndex).getProdDesc());
                updateTopingSaucesData(productList.get(currentIndex).getProdID());
                //                populateSauceData(currentIndex);
                if(currentIndex > 0){
                    ImageButtonPrv.setVisibility(View.VISIBLE);
                    ImageButtonNext.setVisibility(View.VISIBLE);
                }else if(currentIndex == 0) {
                    ImageButtonPrv.setVisibility(View.GONE);
                }
            }
        });





        favCountTxt=(TextView)findViewById(R.id.qVal);
        Button favArrowDown= (Button)findViewById(R.id.buttonMinus);
        favArrowDown.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(currentCount>1)
                {
                    currentCount--;
                    favCountTxt.setText(currentCount+"");
                }


            }
        });
        Button favArrowUp = (Button)findViewById(R.id.buttonPlus);
        favArrowUp.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if(currentCount<10)
                {
                    currentCount++;
                    favCountTxt.setText(currentCount+"");
                }


            }
        });

        LinearLayout ltToppings= (LinearLayout)findViewById(R.id.ltToppings);
        ltToppings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i=new Intent(CreateYourOwnPizzaDetails.this,PizzaToppingsActivity.class);
                Bundle bundle=new Bundle();
                AppProperties.selectedToppings=toppingsSelected;

                bundle.putSerializable("selectedProduct",productList.get(currentIndex));

                //  bundle.putSerializable("selectedProduct",selectedBean);
                i.putExtras(bundle);
                startActivityForResult(i, SELECT_TOPPINGS);


            }
        });




        //        populateCategoryData();

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

                Intent intent=new Intent(CreateYourOwnPizzaDetails.this, FavsListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(CreateYourOwnPizzaDetails.this, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        Button buttonAddFav= (Button)findViewById(R.id.buttonAddFav);
        buttonAddFav.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
                dbInstance.open();
                boolean favAdded=dbInstance.favAlreadyAdded(productList.get(currentIndex).getProdID(),productList.get(currentIndex).getDisplayName());
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
                f.setProdCatID(productList.get(currentIndex).getProdCatID());
                f.setSubCatID1(productList.get(currentIndex).getSubCatID1());
                f.setSubCatID2(productList.get(currentIndex).getSubCatID2());
                f.setProdID(productList.get(currentIndex).getProdID());
                f.setProdCode(productList.get(currentIndex).getProdCode());
                f.setDisplayName(productList.get(currentIndex).getDisplayName());
                f.setProdAbbr(productList.get(currentIndex).getProdAbbr());
                f.setProdDesc(productList.get(currentIndex).getProdDesc());
                f.setIsAddDeliveryAmount(productList.get(currentIndex).getIsAddDeliveryAmount());
                f.setDisplaySequence(productList.get(currentIndex).getDisplaySequence());
                f.setCaloriesQty(productList.get(currentIndex).getCaloriesQty());
                f.setPrice(productList.get(currentIndex).getPrice());
                f.setThumbnail(productList.get(currentIndex).getThumbnail());
                f.setFullImage(productList.get(currentIndex).getFullImage());
                f.setCustomName(productList.get(currentIndex).getDisplayName());
                f.setProdCatName("Pizza");
                return f;
            }
        });








        buttonAddOrders.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Order tempOrderBean = getOrderBean();
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
                dbInstance.open();
                dbInstance.insertOrder(tempOrderBean);
                Toast.makeText(CreateYourOwnPizzaDetails.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
                dbInstance.close();
                
                Intent intent=new Intent(CreateYourOwnPizzaDetails.this, PizzaSubMenuActivity.class);
                intent.putExtra("isHalf", false);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

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


    private void updateResultsInUi() { 

        if(null != pd)
            pd.dismiss();

        // populating default toppings
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
        dbInstance.open();
        toppingsSelected=new HashMap<String, ToppingsHashmap>();
        ArrayList<ToppingsAndSauces> toppingsList = dbInstance.getPizzaToppings(productList.get(currentIndex).getProdID());
        for (ToppingsAndSauces toppingsAndSaucesVo : toppingsList) {
            if("True".equalsIgnoreCase(toppingsAndSaucesVo.getIsFreeWithPizza()))
                toppingsSelected.put(toppingsAndSaucesVo.getToppingID(), getHashBean(toppingsAndSaucesVo,"Single"));
        }
        dbInstance.close();        

        String tempList="";
        Iterator it = toppingsSelected.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            ToppingsHashmap gm=(ToppingsHashmap)pairs.getValue();
            tempList+=gm.getToppingCode()+",";
        }

        Log.d("????????<<<<", "tempList = " + tempList);
        selectedToppings.setText(AppProperties.trimLastComma(tempList));

        populateSauceData(currentIndex);

        //        Intent i=new Intent(CreateYourOwnPizzaDetails.this, PizzaDetailsActivity.class);
        //        Bundle bundle=new Bundle();
        //        bundle.putSerializable("selectedProduct",productList.get(currentIndex));
        //        i.putExtras(bundle);
        //        startActivityForResult(i, 112233);
    }

    final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {        
        public void run() {            
            updateResultsInUi();        
        }    
    };   

    String serverResponse;
    protected void updateTopingSaucesData(final String prodId) {
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
        dbInstance.open();
        syncedPrices = dbInstance.recordExistsToppingPrices();
        syncedToppings=dbInstance.recordExistsToppings(prodId);
        dbInstance.close();

        if(syncedPrices && syncedToppings)
        {
            updateResultsInUi();
        }
        else
        {
            pd = ProgressDialog.show(CreateYourOwnPizzaDetails.this, "", "Please wait...", true, false);

            Thread t = new Thread() {            
                public void run() {                

                    try {

                        if(syncedToppings==false)
                            GetPizzaToppingAndSauces(prodId);

                        if(syncedPrices==false)
                        {
                            GetPizzaToppingsSizes();
                            GetPizzaToppingsPrices();
                        }

                    } catch (Exception ex)
                    {
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
        ServerResponse response = jsonParser.retrieveGETResponse(url, null);

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


    private void GetPizzaToppingsSizes(){

        String url = Constants.ROOT_URL + "GetToppingSizes.aspx";
        long dataRetrieveStartTime = System.currentTimeMillis();
        ServerResponse response = jsonParser.retrieveGETResponse(url, null);

        long dataRetrieveEndTime = System.currentTimeMillis();
        Log.d("TIME", "time to retrieve topping SIZE data = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


        if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
            JSONObject jsonObj = response.getjObj();
            try {
                JSONObject responseObj = jsonObj.getJSONObject("Response");
                int status = responseObj.getInt("Status");
                JSONArray data = responseObj.getJSONArray("Data");
                JSONObject errors = responseObj.getJSONObject("Errors");

                toppingsSizeList = ToppingSizes.parseToppingsSizes(data);

                long productParseEndTime = System.currentTimeMillis();
                Log.d("TIME", "time to parse topping Size list of item " + toppingsSizeList.size() + " = " + (productParseEndTime - dataRetrieveEndTime)/1000 + " second");
                long dbInsertionStart = System.currentTimeMillis();
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
                dbInstance.open();
                dbInstance.insertToppingSizes(toppingsSizeList);
                dbInstance.close();
                long dbInsertionEnd = System.currentTimeMillis();
                Log.d("TIME", "time to insert topping-size data " + " = " + (dbInsertionEnd - dbInsertionStart)/1000 + " second");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    } 


    private void GetPizzaToppingsPrices(){
        String url = Constants.ROOT_URL + "GetToppingPrices.aspx";
        long dataRetrieveStartTime = System.currentTimeMillis();
        ServerResponse response = jsonParser.retrieveGETResponse(url, null);

        long dataRetrieveEndTime = System.currentTimeMillis();
        Log.d("TIME", "time to retrieve topping PRICE data = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


        if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
            JSONObject jsonObj = response.getjObj();
            try {
                JSONObject responseObj = jsonObj.getJSONObject("Response");
                int status = responseObj.getInt("Status");
                JSONArray data = responseObj.getJSONArray("Data");
                JSONObject errors = responseObj.getJSONObject("Errors");

                toppingsPriceList = ToppingPrices.parseToppingsPriceList(data);

                long productParseEndTime = System.currentTimeMillis();
                Log.d("TIME", "time to parse topping PRICE list of item " + toppingsPriceList.size() + " = " + (productParseEndTime - dataRetrieveEndTime)/1000 + " second");
                long dbInsertionStart = System.currentTimeMillis();
                DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
                dbInstance.open();
                syncedPrices = dbInstance.insertToppingPrices(toppingsPriceList);
                dbInstance.close();
                long dbInsertionEnd = System.currentTimeMillis();
                Log.d("TIME", "time to insert topping-price data " + " = " + (dbInsertionEnd - dbInsertionStart)/1000 + " second");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
    private void populateSauceData(int thisIndex){
        ArrayList<ToppingsAndSauces> saucesList=null;
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
        dbInstance.open();
        saucesList=dbInstance.getPizzaSauces(productList.get(thisIndex).getProdID());
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
            Log.d("SAUCE", "SAUCE LIST SIZE = " + spinnerArrayAdapter.getCount() + " for prodId - " + productList.get(thisIndex).getProdID());
            spinnerArrayAdapter.notifyDataSetChanged();
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
    }

    private Order getOrderBean() {

        Order f = new Order();
        f.setProdCatID(productList.get(currentIndex).getProdCatID());
        f.setSubCatID1(productList.get(currentIndex).getSubCatID1());
        f.setSubCatID2(productList.get(currentIndex).getSubCatID2());
        f.setProdID(productList.get(currentIndex).getProdID());
        f.setProdCode(productList.get(currentIndex).getProdCode());
        f.setDisplayName(productList.get(currentIndex).getDisplayName());
        f.setProdAbbr(productList.get(currentIndex).getProdAbbr());
        f.setProdDesc(productList.get(currentIndex).getProdDesc());
        f.setIsAddDeliveryAmount(productList.get(currentIndex).getIsAddDeliveryAmount());
        f.setDisplaySequence(productList.get(currentIndex).getDisplaySequence());
        f.setCaloriesQty(productList.get(currentIndex).getCaloriesQty());

        double finalPrice=generateTotalPrice();

        finalPrice=AppProperties.roundTwoDecimals(finalPrice);

        f.setPrice(String.valueOf(finalPrice));

        f.setThumbnail(productList.get(currentIndex).getThumbnail());
        f.setFullImage(productList.get(currentIndex).getFullImage());

        f.setQuantity(String.valueOf(currentCount));

        f.setCrust("");
        f.setSauce("");
        f.setToppings("");

        f.setProdCatName("Pizza");
        return f;
    }

    private double generateTotalPrice() {

        double totalPrice=Double.parseDouble(productList.get(currentIndex).getPrice());

        totalPrice=currentCount*totalPrice;
        double tempDoublePrice;
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
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
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(CreateYourOwnPizzaDetails.this);
        dbInstance.open();
        ArrayList<String> orderInfo = dbInstance.getOrderInfo();
        List<DealOrder>dealOrderVos1= dbInstance.getDealOrdersList(true);
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

    }


}