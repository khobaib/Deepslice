package com.deepslice.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
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
import com.deepslice.database.AppDao;
import com.deepslice.model.AllProductsVo;
import com.deepslice.model.CreateOwnPizzaData;
import com.deepslice.model.DealOrderVo;
import com.deepslice.model.FavouritesVo;
import com.deepslice.model.LabelValueBean;
import com.deepslice.model.OrderVo;
import com.deepslice.model.ToppingPricesVo;
import com.deepslice.model.ToppingSizesVo;
import com.deepslice.model.ToppingsAndSaucesVo;
import com.deepslice.model.ToppingsHashmapVo;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    HashMap<String, ToppingsHashmapVo> toppingsSelected;

    CreateOwnPizzaData selectedPizzaData;
    List<String> prodIds;
    ArrayList<AllProductsVo> productList;

    //    private ProgressDialog pDialog;
    JsonParser jsonParser = new JsonParser();

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

        productList = new ArrayList<AllProductsVo>();
        AppDao dao=null;
        try {
            dao=AppDao.getSingleton(getApplicationContext());
            dao.openConnection();
            for (int i=0;i<prodIds.size();i++){
                String prodId = prodIds.get(i);
                productList.add(dao.getProductById(prodId));
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }finally{
            if(null!=dao)
                dao.closeConnection();
        }



        //        new GetPizzaData().execute();
        //        final int [] imageSet=new int[]{
        //                R.drawable.pizza_det_icon,
        //                R.drawable.pizza_icon,
        //                R.drawable.pizza_det_icon,
        //                R.drawable.pizza_det_icon
        //        };

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
                startActivity(intent);

            }
        });

        Button buttonAddFav= (Button)findViewById(R.id.buttonAddFav);
        buttonAddFav.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AppDao dao=null;
                try {
                    dao=AppDao.getSingleton(getApplicationContext());
                    dao.openConnection();
                    boolean favAdded=dao.favAlreadyAdded(productList.get(currentIndex).getProdID(),productList.get(currentIndex).getDisplayName());
                    if(favAdded)
                    {
                        Toast.makeText(CreateYourOwnPizzaDetails.this, "Already added to Favourites", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        dao.insertFav(getFavBean());
                        doResumeWork();
                        Toast.makeText(CreateYourOwnPizzaDetails.this, "Successfully added to Favourites", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }finally{
                    if(null!=dao)
                        dao.closeConnection();
                }
            }


            private FavouritesVo getFavBean() {

                FavouritesVo f = new FavouritesVo();
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
                OrderVo tempOrderBean = getOrderBean();
                AppDao dao=null;
                try {
                    dao=AppDao.getSingleton(getApplicationContext());
                    dao.openConnection();

                    dao.insertOrder(tempOrderBean);
                    Toast.makeText(CreateYourOwnPizzaDetails.this, "Added to Cart Successfully.", Toast.LENGTH_LONG).show();
                    finish();


                } catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }finally{
                    if(null!=dao)
                        dao.closeConnection();
                }
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
        AppDao dao=null;
        try {
            dao=AppDao.getSingleton(getApplicationContext());
            dao.openConnection();
            toppingsSelected=new HashMap<String, ToppingsHashmapVo>();
            ArrayList<ToppingsAndSaucesVo> toppingsList = dao.getPizzaToppings(productList.get(currentIndex).getProdID());
            for (ToppingsAndSaucesVo toppingsAndSaucesVo : toppingsList) {
                if("True".equalsIgnoreCase(toppingsAndSaucesVo.getIsFreeWithPizza()))
                    toppingsSelected.put(toppingsAndSaucesVo.getToppingID(), getHashBean(toppingsAndSaucesVo,"Single"));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }finally{
            if(null!=dao)
                dao.closeConnection();
        }

        String tempList="";
        Iterator it = toppingsSelected.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            ToppingsHashmapVo gm=(ToppingsHashmapVo)pairs.getValue();
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


        AppDao dao=null;
        try {
            dao=AppDao.getSingleton(getApplicationContext());
            dao.openConnection();

            syncedPrices=dao.recordExistsToppingPrices();
            syncedToppings=dao.recordExistsToppings(prodId);

            //          dao.insertOrUpdateList(questionList);

        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }finally{
            if(null!=dao)
                dao.closeConnection();
        }

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
                            populateToppingsAndSauces(prodId);

                        if(syncedPrices==false)
                        {
                            populateToppingSizes();
                            populateToppingPrices();
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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void populateToppingsAndSauces(String prodId) {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL+"/GetPizzaToppingsAndSauces.aspx?prodID="+prodId);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                System.out.println("Failed to download file");
            }


            serverResponse = builder.toString();

            //////////////////////////////////////////////////////////
            String errorMessage="";
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            JSONArray results = new JSONArray(serverResponse);
            JSONObject respOuter = results.getJSONObject(0);
            JSONObject resp = respOuter.getJSONObject("Response");
            String status = resp.getString("Status");
            JSONArray resultsArray =null;
            Object data= resp.get("Data");
            boolean dataExists=false;
            if(data instanceof JSONArray)
            {
                resultsArray =(JSONArray)data;
                dataExists=true;
            }

            JSONObject errors = resp.getJSONObject("Errors");

            boolean hasError=errors.has("Message");
            if(hasError)
            {
                errorMessage=errors.getString("Message");
                System.out.println("Error:"+errorMessage);
            }

            ArrayList<ToppingsAndSaucesVo> pCatList = new ArrayList<ToppingsAndSaucesVo>();

            if(dataExists==true)
            {
                ToppingsAndSaucesVo aBean;
                for(int i=0; i<resultsArray.length(); i++){
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if(jsResult!=null){
                        String jsonString = jsResult.toString();
                        aBean=new ToppingsAndSaucesVo();
                        aBean=gson.fromJson(jsonString, ToppingsAndSaucesVo.class);
                        //                System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                        pCatList.add(aBean);

                    }
                }
            }

            AppDao dao=null;
            try {
                dao=AppDao.getSingleton(getApplicationContext());
                dao.openConnection();

                dao.insertToppingSauces(pCatList);

            } catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }finally{
                if(null!=dao)
                    dao.closeConnection();
            }

            System.out.println("Got Toppings And Sauces: "+pCatList.size());
            //////////////////////////// LOOOOOOOOOOOOPPPPPPPPPPPPPPP
            //////////////////////////////////////////////////////////
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {

            e.printStackTrace();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void populateToppingSizes() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL + "/GetToppingSizes.aspx");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                System.out.println("Failed to download file");
            }

            serverResponse = builder.toString();

            // ////////////////////////////////////////////////////////
            String errorMessage = "";
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            JSONArray results = new JSONArray(serverResponse);
            JSONObject respOuter = results.getJSONObject(0);
            JSONObject resp = respOuter.getJSONObject("Response");
            String status = resp.getString("Status");
            JSONArray resultsArray = null;
            Object data = resp.get("Data");
            boolean dataExists = false;
            if (data instanceof JSONArray) {
                resultsArray = (JSONArray) data;
                dataExists = true;
            }

            JSONObject errors = resp.getJSONObject("Errors");

            boolean hasError = errors.has("Message");
            if (hasError) {
                errorMessage = errors.getString("Message");
                System.out.println("Error:" + errorMessage);
            }

            ArrayList<ToppingSizesVo> pCatList = new ArrayList<ToppingSizesVo>();

            if (dataExists == true) {
                ToppingSizesVo aBean;
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if (jsResult != null) {
                        String jsonString = jsResult.toString();
                        aBean = new ToppingSizesVo();
                        aBean = gson
                                .fromJson(jsonString, ToppingSizesVo.class);
                        // System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                        pCatList.add(aBean);
                    }
                }
            }

            //AppProperties.subCatList = pCatList;
            AppDao dao=null;
            try {
                dao=AppDao.getSingleton(getApplicationContext());
                dao.openConnection();

                dao.insertToppingSizes(pCatList);

            } catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }finally{
                if(null!=dao)
                    dao.closeConnection();
            }

            System.out.println("Got Topping Sizes : " + pCatList.size());
            // ////////////////////////// LOOOOOOOOOOOOPPPPPPPPPPPPPPP
            // ////////////////////////////////////////////////////////
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void populateToppingPrices() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL + "/GetToppingPrices.aspx");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                System.out.println("Failed to download file");
            }

            serverResponse = builder.toString();

            // ////////////////////////////////////////////////////////
            String errorMessage = "";
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            JSONArray results = new JSONArray(serverResponse);
            JSONObject respOuter = results.getJSONObject(0);
            JSONObject resp = respOuter.getJSONObject("Response");
            String status = resp.getString("Status");
            JSONArray resultsArray = null;
            Object data = resp.get("Data");
            boolean dataExists = false;
            if (data instanceof JSONArray) {
                resultsArray = (JSONArray) data;
                dataExists = true;
            }

            JSONObject errors = resp.getJSONObject("Errors");

            boolean hasError = errors.has("Message");
            if (hasError) {
                errorMessage = errors.getString("Message");
                System.out.println("Error:" + errorMessage);
            }

            ArrayList<ToppingPricesVo> pCatList = new ArrayList<ToppingPricesVo>();

            if (dataExists == true) {
                ToppingPricesVo aBean;
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if (jsResult != null) {
                        String jsonString = jsResult.toString();
                        aBean = new ToppingPricesVo();
                        aBean = gson
                                .fromJson(jsonString, ToppingPricesVo.class);
                        // System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                        pCatList.add(aBean);
                    }
                }
            }

            //AppProperties.allProductsList = pCatList;
            AppDao dao=null;
            try {
                dao=AppDao.getSingleton(getApplicationContext());
                dao.openConnection();

                dao.insertToppingPrices(pCatList);

            } catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }finally{
                if(null!=dao)
                    dao.closeConnection();
            }

            System.out.println("Got product catetgories: " + pCatList.size());
            // ////////////////////////// LOOOOOOOOOOOOPPPPPPPPPPPPPPP
            // ////////////////////////////////////////////////////////
        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////   







    private ToppingsHashmapVo getHashBean(
            ToppingsAndSaucesVo toppingsAndSaucesVo, String toppingSize) {

        ToppingsHashmapVo hMap=new ToppingsHashmapVo();

        hMap.setToppingCode(toppingsAndSaucesVo.getToppingCode());
        hMap.setToppingDesc(toppingsAndSaucesVo.getToppingDesc());
        hMap.setToppingID(toppingsAndSaucesVo.getToppingID());
        hMap.setToppingPrice(toppingsAndSaucesVo.getOwnPrice());
        hMap.setToppingSize(toppingSize);

        return hMap;
    }
    private void populateSauceData(int thisIndex){
        ArrayList<ToppingsAndSaucesVo> saucesList=null;
        AppDao dao=null;
        try {
            dao=AppDao.getSingleton(getApplicationContext());
            dao.openConnection();

            saucesList=dao.getPizzaSauces(productList.get(thisIndex).getProdID());

        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }finally{
            if(null!=dao)
                dao.closeConnection();
        }

        try{

            int i=0;
            LabelValueBean[] albums= new LabelValueBean[saucesList.size()];
            for (ToppingsAndSaucesVo sBean : saucesList) {
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
                toppingsSelected=new HashMap<String, ToppingsHashmapVo>();

            Iterator it = toppingsSelected.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                ToppingsHashmapVo gm=(ToppingsHashmapVo)pairs.getValue();
                tempList+=gm.getToppingCode()+",";
            }

            selectedToppings.setText(AppProperties.trimLastComma(tempList));

        }
    }

    private OrderVo getOrderBean() {

        OrderVo f = new OrderVo();
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
        AppDao dao=null;
        try {
            dao=AppDao.getSingleton(getApplicationContext());
            dao.openConnection();

            String tempPrice="0.00";
            Iterator it = toppingsSelected.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                ToppingsHashmapVo gm=(ToppingsHashmapVo)pairs.getValue();

                if("False".equalsIgnoreCase(gm.getIsFreeWithPizza())){
                    tempPrice=dao.getToppingPrice(gm.getToppingID(),gm.getToppingSize());
                }
                tempDoublePrice=Double.parseDouble(tempPrice);
                totalPrice=totalPrice+tempDoublePrice;
            }

        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }finally{
            if(null!=dao)
                dao.closeConnection();
        }

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
        AppDao dao = null;
        try {
            dao = AppDao.getSingleton(getApplicationContext());
            dao.openConnection();
            // dao.updateDealOrder();
            ArrayList<String> orderInfo = dao.getOrderInfo();
            ArrayList<DealOrderVo>dealOrderVos1= dao.getDealOrdersList();
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
            String fvs=dao.getFavCount();
            if (null != fvs && !fvs.equals("0")) {
                favCount.setText(fvs);
                favCount.setVisibility(View.VISIBLE);
            }
            else{
                favCount.setVisibility(View.INVISIBLE);
            }



        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (null != dao)
                dao.closeConnection();
        }
        // ///////////////////////////////////////////////////////////////////////


    }


}