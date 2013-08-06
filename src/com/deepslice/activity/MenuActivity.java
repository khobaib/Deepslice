package com.deepslice.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RecoverySystem.ProgressListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.AllProductsVo;
import com.deepslice.model.DealOrder;
import com.deepslice.model.ProductCategory;
import com.deepslice.model.SubCategoryVo;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.ResumableTask;
import com.deepslice.utilities.ResumableTaskStarter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MenuActivity extends Activity {

    ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_scr);

        getProductCategories();

        ImageView ivPizza=(ImageView)findViewById(R.id.imageView1);
        ImageView ivDrinks=(ImageView)findViewById(R.id.imageView2);
        ImageView ivSides=(ImageView)findViewById(R.id.imageView3);
        ImageView ivPasta=(ImageView)findViewById(R.id.imageView4);
        ImageButton ivDeals=(ImageButton)findViewById(R.id.imageView5);

        ivPizza.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MenuActivity.this,PizzaSubMenuActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("catType","Pizza");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ivDrinks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MenuActivity.this,DrinksSubMenuActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("catType","Drinks");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ivSides.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MenuActivity.this,SubMenuActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("catType","Sides");
                intent.putExtras(bundle);
                startActivity(intent);
            }


        });
        ivPasta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String pastaId=getProdCatId("Pasta");
                Intent intent=new Intent(MenuActivity.this,ProductsListActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("catId",pastaId);
                bundle.putString("subCatId","0");
                bundle.putString("catType","Pasta");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        ivDeals.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String pastaId=getProdCatId("Deals");
                Intent intent=new Intent(MenuActivity.this,DealsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("catId",pastaId);
                bundle.putString("subCatId","0");
                bundle.putString("catType","Deals");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MenuActivity.this,FavsListActivity.class);
                startActivity(intent);

            }
        });


        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MenuActivity.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });


    }

    final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {        
        public void run() {            
            updateResultsInUi();        
        }    
    };
    String serverResponse;
    protected void getProductCategories() {

        boolean synced =false;
        AppDao dao=null;
        try {
            dao=AppDao.getSingleton(getApplicationContext());
            dao.openConnection();

            synced=dao.recordExists();

        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }finally{
            if(null!=dao)
                dao.closeConnection();
        }

        if(synced)
        {
            updateResultsInUi();
        }
        else
        {
            pd = ProgressDialog.show(MenuActivity.this, "", "Please wait...", true, false);

            Thread t = new Thread() {            
                public void run() {                

                    try {

                        populateProductCategories();
                        populateSubCategories();
                        populateAllProducts();

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

    private String getProdCatId(String abbr) {
        String pCatId="0";
        AppDao dao=null;
        try {
            dao=AppDao.getSingleton(getApplicationContext());
            dao.openConnection();

            pCatId=dao.getCatIdByCatCode(abbr);

        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }finally{
            if(null!=dao)
                dao.closeConnection();
        }

        return pCatId;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void populateProductCategories() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL+"/GetProductCategory.aspx?ProdCategoryID=0");
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

            ArrayList<ProductCategory> pCatList = new ArrayList<ProductCategory>();

            if(dataExists==true)
            {
                ProductCategory aBean;
                for(int i=0; i<resultsArray.length(); i++){
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if(jsResult!=null){
                        String jsonString = jsResult.toString();
                        aBean=new ProductCategory();
                        aBean=gson.fromJson(jsonString, ProductCategory.class);
                        pCatList.add(aBean);

                    }
                }
            }

            AppDao dao=null;
            try {
                dao=AppDao.getSingleton(getApplicationContext());
                dao.openConnection();

                dao.insertProdCat(pCatList);

            } catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }finally{
                if(null!=dao)
                    dao.closeConnection();
            }

            System.out.println("Got product catetgories: "+pCatList.size());
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
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void populateSubCategories() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL
                + "/GetProductSubCategory.aspx?ProdCategoryID=0&ProdSubCategoryID=0");
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

            ArrayList<SubCategoryVo> pCatList = new ArrayList<SubCategoryVo>();

            if (dataExists == true) {
                SubCategoryVo aBean;
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if (jsResult != null) {
                        String jsonString = jsResult.toString();
                        aBean = new SubCategoryVo();
                        aBean = gson
                                .fromJson(jsonString, SubCategoryVo.class);
                        // System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                        pCatList.add(aBean);
                    }
                }
            }

            //			AppProperties.subCatList = pCatList;
            AppDao dao=null;
            try {
                dao=AppDao.getSingleton(getApplicationContext());
                dao.openConnection();

                dao.insertSubCatList(pCatList);

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
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void populateAllProducts() {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(Constants.ROOT_URL + "/GetAllProducts.aspx");
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

            ArrayList<AllProductsVo> pCatList = new ArrayList<AllProductsVo>();

            if (dataExists == true) {
                AllProductsVo aBean;
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if (jsResult != null) {
                        String jsonString = jsResult.toString();
                        aBean = new AllProductsVo();
                        aBean = gson
                                .fromJson(jsonString, AllProductsVo.class);
                        // System.out.println("++++++++++++++++++++"+aBean.getAuto_name());
                        pCatList.add(aBean);
                    }
                }
            }

            //			AppProperties.allProductsList = pCatList;
            AppDao dao=null;
            try {
                dao=AppDao.getSingleton(getApplicationContext());
                dao.openConnection();

                dao.insertAllProducts(pCatList);

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

    private void updateResultsInUi() { 

        if(null != pd)
            pd.dismiss();

    }

    @Override
    protected void onResume() {
        // //////////////////////////////////////////////////////////////////////////////
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MenuActivity.this);
        dbInstance.open();
        ArrayList<String> orderInfo = dbInstance.getOrderInfo();
        ArrayList<DealOrder>dealOrderVos1= dbInstance.getDealOrdersList();
        TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
        double tota=0.00;
        int dealCount=0;
        if((dealOrderVos1!=null && dealOrderVos1.size()>0)){
            dealCount=dealOrderVos1.size();
            for (int x=0;x<dealOrderVos1.size();x++){
                tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
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
        //            ArrayList<String> orderInfo = dao.getOrderInfo();
        //            ArrayList<DealOrder>dealOrderVos1= dao.getDealOrdersList();
        //            TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
        //            double tota=0.00;
        //            int dealCount=0;
        //            if((dealOrderVos1!=null && dealOrderVos1.size()>0)){
        //                dealCount=dealOrderVos1.size();
        //                for (int x=0;x<dealOrderVos1.size();x++){
        //                    tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
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

        super.onResume();
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////

    private static class UpdateMenuTask extends ResumableTask implements ProgressListener {

        private Context ctx;
        private int petid = -1;
        private ProgressDialog pd;
        private long uploadingFileSize;
        private File uploadableFile;

        private String isDefault;
        private String rotateable;
        private String commentable;
        private String profilePet;
        private String comments;
        private String tags;
        private String albumId;
        private String watermarkLocation;

        HttpClient httpclient;

        public UpdateMenuTask(ResumableTaskStarter starter, int id, int _petid, File _uploadableFile,
                String isDefault,String rotateable,String commentable,String profilePet,String comments,String tags,String albumId,String watermarkLocation) {
            super(starter, id);
            this.ctx = ((Context) starter);
            this.petid = _petid;
            this.uploadableFile = _uploadableFile;

            this.isDefault=isDefault;
            this.rotateable=rotateable;
            this.commentable=commentable;
            this.profilePet=profilePet;
            this.comments=comments;
            this.tags=tags;
            this.albumId=albumId;
            this.watermarkLocation=watermarkLocation;


        }

        @Override
        public void resume(ResumableTaskStarter starter) {
            super.resume(starter);
            if (!isFinished()) {
                pd = new ProgressDialog(ctx);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setMessage("Uploading...");
                pd.setCancelable(false);
                pd.setMax(100);
                pd.setButton(DialogInterface.BUTTON_NEGATIVE,
                        "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        ClientConnectionManager cm = httpclient.getConnectionManager();
                        cm.shutdown();
                        //	                	photoTask.pause();

                    }
                });
                pd.show();
            }
        }

        @Override
        public void pause() {
            super.pause();
            pd.dismiss();
        }

        @Override
        public void doStuff() {
            data = false;
            StringBuilder builder = new StringBuilder();
            try {

                uploadingFileSize = uploadableFile.length();

                httpclient = new DefaultHttpClient();

                try {
                    HttpGet httpgets = new HttpGet(Constants.ROOT_URL + "/GetAllProducts.aspx");

                    System.out.println("executing request " + httpgets.getRequestLine());

                    HttpResponse response = null;
                    try {
                        response = httpclient.execute(httpgets);
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();

                        AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                        alertDialog.setTitle("Error Uploading");
                        alertDialog.setMessage(e.getMessage());
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            } }); 
                        alertDialog.show();

                        return;
                    } catch (IOException e) {
                        e.printStackTrace();

                        final String msg=e.getMessage();
                        ((Activity) ctx).runOnUiThread(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                                alertDialog.setTitle("Error Uploading");
                                alertDialog.setMessage(msg);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    } }); 
                                alertDialog.show();
                            }
                        });

                        return;
                    } catch (Exception e) {
                        e.printStackTrace();

                        final String msg=e.getMessage();

                        if(msg.contains("Adapter is detached"))
                        {
                            ((Activity) ctx).runOnUiThread(new Runnable() {
                                public void run() {
                                    AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                                    alertDialog.setTitle("Upload Canceled");
                                    alertDialog.setMessage("You have canceled upload request!");
                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        } }); 
                                    alertDialog.show();
                                }
                            });

                        }else{
                            ((Activity) ctx).runOnUiThread(new Runnable() {
                                public void run() {
                                    AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                                    alertDialog.setTitle("Error Uploading");
                                    alertDialog.setMessage(msg);
                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        } }); 
                                    alertDialog.show();
                                }
                            });
                        }
                        return;
                    }

                    HttpEntity resEntity = response.getEntity();

                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    System.out.println(response.getAllHeaders());

                    if (resEntity != null) {
                        System.out.println("Response content length: " + resEntity.getContentLength());


                        InputStream content = resEntity.getContent();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(content));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }


                    }
                    String readFeed = builder.toString();
                    System.out.println(">>>>>>>>>>"+readFeed);
                    JSONObject jsOb = new JSONObject(readFeed);

                    String status=jsOb.getString("success");
                    String errorMsg=null;
                    if(status != null && status.equals("false") ){
                        errorMsg=jsOb.getString("message");							
                    }
                    System.out.println("status::"+status);
                    System.out.println("errorMsg::"+errorMsg);

                    if (pd!=null && pd.isShowing()) {
                        pd.dismiss();
                    }

                    data = true;

                } finally {
                }
            } catch (Exception e) {
                e.printStackTrace();

                final String msg=e.getMessage();
                ((Activity) ctx).runOnUiThread(new Runnable() {
                    public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                        alertDialog.setTitle("Error Uploading");
                        alertDialog.setMessage(msg);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            } }); 
                        alertDialog.show();
                    }
                });

                return;
            }
        }

        @Override
        public void onProgress(int num) {
            if (pd != null) {
                int progress = (int) (num * 100 / uploadingFileSize);
                Log.d("Petsie", "Data transferred:" + num + "/" + uploadingFileSize + ", setting progress to : " + progress);
                pd.setProgress(progress);
            }
        }

    }		


}
