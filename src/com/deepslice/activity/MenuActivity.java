package com.deepslice.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.DealOrder;
import com.deepslice.model.ProductCategory;
import com.deepslice.model.ProductSubCategory;
import com.deepslice.model.Products;
import com.deepslice.model.ServerResponse;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.Constants;

public class MenuActivity extends Activity {

    ProgressDialog pDialog;
    JsonParser jsonParser = new JsonParser();

    List<ProductCategory> categoryList;
    List<ProductSubCategory> subcategoryList;
    List<Products> productList;

    long dataRetrieveStartTime, dataRetrieveEndTime;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_scr);

        pDialog = ProgressDialog.show(MenuActivity.this, "", "Please wait...", true, false);

        boolean synced = false;

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MenuActivity.this);
        dbInstance.open();
        synced=dbInstance.recordExists();
        dbInstance.close();

        //        synced = false;          // test
        if(synced) {
            if(pDialog.isShowing())
                pDialog.dismiss();
        }
        else {
            new GetProductCategories().execute();
        }

        ImageView ivPizza=(ImageView)findViewById(R.id.imageView1);
        ImageView ivDrinks=(ImageView)findViewById(R.id.imageView2);
        ImageView ivSides=(ImageView)findViewById(R.id.imageView3);
        ImageView ivPasta=(ImageView)findViewById(R.id.imageView4);
        ImageButton ivDeals=(ImageButton)findViewById(R.id.imageView5);

        ivPizza.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MenuActivity.this,PizzaSubMenuActivity.class);
                intent.putExtra("isHalf", false);
//                Bundle bundle=new Bundle();
//                bundle.putString("catType","Pizza");
//                intent.putExtras(bundle);
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
                Intent intent=new Intent(MenuActivity.this, DealsActivity.class);
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


    public class GetProductCategories extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //            pDialog.setMessage("Please wait...");
            //            if(!pDialog.isShowing())
            //                pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            dataRetrieveStartTime = System.currentTimeMillis();
            String url = Constants.ROOT_URL + "GetProductCategory.aspx?ProdCategoryID=0";
            ServerResponse response = jsonParser.retrieveGETResponse(url, null);

            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    categoryList = ProductCategory.parseProductCategories(data);

                    DeepsliceDatabase dbInstance = new DeepsliceDatabase(MenuActivity.this);
                    dbInstance.open();
                    dbInstance.insertProdCat(categoryList);
                    dbInstance.close();

                    System.out.println("Got product catetgories: "+categoryList.size());

                    return true;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                new GetProductSubcategories().execute();
            }
        }
    }


    public class GetProductSubcategories extends AsyncTask<Void, Void, Boolean>{


        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "GetProductSubCategory.aspx?ProdCategoryID=0&ProdSubCategoryID=0";
            ServerResponse response = jsonParser.retrieveGETResponse(url, null);

            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    subcategoryList = ProductSubCategory.parseProductSubcategories(data);

                    DeepsliceDatabase dbInstance = new DeepsliceDatabase(MenuActivity.this);
                    dbInstance.open();
                    dbInstance.insertSubCatList(subcategoryList);
                    dbInstance.close();

                    System.out.println("Got product catetgories: " + subcategoryList.size());

                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                new GetAllProducts().execute();
            }
        }
    }


    public class GetAllProducts extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Please wait...");
            if(!pDialog.isShowing())
                pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "GetAllProducts.aspx";
            ServerResponse response = jsonParser.retrieveGETResponse(url, null);

            dataRetrieveEndTime = System.currentTimeMillis();
            Log.d("TIME", "time to retrieve cloud data + parse cat+subcat = " + (dataRetrieveEndTime - dataRetrieveStartTime)/1000 + " second");


            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    productList = Products.parseAllProducts(data);

                    long productParseEndTime = System.currentTimeMillis();
                    Log.d("TIME", "time to parse product = " + (productParseEndTime - dataRetrieveEndTime)/1000 + " second");

                    insertProductIntoDb();

                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pDialog.isShowing())
                pDialog.dismiss();
            if(result){
            }
        }
    }


    private void insertProductIntoDb(){


        long productDBInsertstartTime = System.currentTimeMillis();
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MenuActivity.this);
        dbInstance.open();
        dbInstance.insertAllProducts(productList);
        dbInstance.close();

        long productDBInsertEndTime = System.currentTimeMillis();
        Log.d("TIME", "time to dbInsert " + productList.size() + " product is = " + (productDBInsertEndTime - productDBInsertstartTime)/1000 + " second");



    }

    //
    //
    //
    //    protected void getProductCategories() {
    //
    //
    //
    //    }

    private String getProdCatId(String abbr) {
        String pCatId="0";

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(MenuActivity.this);
        dbInstance.open();
        pCatId=dbInstance.getCatIdByCatCode(abbr);
        dbInstance.close();

        return pCatId;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //    private void updateResultsInUi() { 
    //
    //        if(pDialog.isShowing())
    //            pDialog.dismiss();
    //
    //    }

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
        super.onResume();
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////

    //    private static class UpdateMenuTask extends ResumableTask implements ProgressListener {
    //
    //        private Context ctx;
    //        private int petid = -1;
    //        private ProgressDialog pd;
    //        private long uploadingFileSize;
    //        private File uploadableFile;
    //
    //        private String isDefault;
    //        private String rotateable;
    //        private String commentable;
    //        private String profilePet;
    //        private String comments;
    //        private String tags;
    //        private String albumId;
    //        private String watermarkLocation;
    //
    //        HttpClient httpclient;
    //
    //        public UpdateMenuTask(ResumableTaskStarter starter, int id, int _petid, File _uploadableFile,
    //                String isDefault,String rotateable,String commentable,String profilePet,String comments,String tags,String albumId,String watermarkLocation) {
    //            super(starter, id);
    //            this.ctx = ((Context) starter);
    //            this.petid = _petid;
    //            this.uploadableFile = _uploadableFile;
    //
    //            this.isDefault=isDefault;
    //            this.rotateable=rotateable;
    //            this.commentable=commentable;
    //            this.profilePet=profilePet;
    //            this.comments=comments;
    //            this.tags=tags;
    //            this.albumId=albumId;
    //            this.watermarkLocation=watermarkLocation;
    //
    //
    //        }
    //
    //        @Override
    //        public void resume(ResumableTaskStarter starter) {
    //            super.resume(starter);
    //            if (!isFinished()) {
    //                pd = new ProgressDialog(ctx);
    //                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    //                pd.setMessage("Uploading...");
    //                pd.setCancelable(false);
    //                pd.setMax(100);
    //                pd.setButton(DialogInterface.BUTTON_NEGATIVE,
    //                        "Cancel", new DialogInterface.OnClickListener() {
    //                    public void onClick(DialogInterface dialog, int whichButton) {
    //
    //                        ClientConnectionManager cm = httpclient.getConnectionManager();
    //                        cm.shutdown();
    //                        //	                	photoTask.pause();
    //
    //                    }
    //                });
    //                pd.show();
    //            }
    //        }
    //
    //        @Override
    //        public void pause() {
    //            super.pause();
    //            pd.dismiss();
    //        }
    //
    //        @Override
    //        public void doStuff() {
    //            data = false;
    //            StringBuilder builder = new StringBuilder();
    //            try {
    //
    //                uploadingFileSize = uploadableFile.length();
    //
    //                httpclient = new DefaultHttpClient();
    //
    //                try {
    //                    HttpGet httpgets = new HttpGet(Constants.ROOT_URL + "/GetAllProducts.aspx");
    //
    //                    System.out.println("executing request " + httpgets.getRequestLine());
    //
    //                    HttpResponse response = null;
    //                    try {
    //                        response = httpclient.execute(httpgets);
    //                    } catch (ClientProtocolException e) {
    //                        e.printStackTrace();
    //
    //                        AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
    //                        alertDialog.setTitle("Error Uploading");
    //                        alertDialog.setMessage(e.getMessage());
    //                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    //                            public void onClick(DialogInterface dialog, int which) {
    //                                return;
    //                            } }); 
    //                        alertDialog.show();
    //
    //                        return;
    //                    } catch (IOException e) {
    //                        e.printStackTrace();
    //
    //                        final String msg=e.getMessage();
    //                        ((Activity) ctx).runOnUiThread(new Runnable() {
    //                            public void run() {
    //                                AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
    //                                alertDialog.setTitle("Error Uploading");
    //                                alertDialog.setMessage(msg);
    //                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    //                                    public void onClick(DialogInterface dialog, int which) {
    //                                        return;
    //                                    } }); 
    //                                alertDialog.show();
    //                            }
    //                        });
    //
    //                        return;
    //                    } catch (Exception e) {
    //                        e.printStackTrace();
    //
    //                        final String msg=e.getMessage();
    //
    //                        if(msg.contains("Adapter is detached"))
    //                        {
    //                            ((Activity) ctx).runOnUiThread(new Runnable() {
    //                                public void run() {
    //                                    AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
    //                                    alertDialog.setTitle("Upload Canceled");
    //                                    alertDialog.setMessage("You have canceled upload request!");
    //                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    //                                        public void onClick(DialogInterface dialog, int which) {
    //                                            return;
    //                                        } }); 
    //                                    alertDialog.show();
    //                                }
    //                            });
    //
    //                        }else{
    //                            ((Activity) ctx).runOnUiThread(new Runnable() {
    //                                public void run() {
    //                                    AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
    //                                    alertDialog.setTitle("Error Uploading");
    //                                    alertDialog.setMessage(msg);
    //                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    //                                        public void onClick(DialogInterface dialog, int which) {
    //                                            return;
    //                                        } }); 
    //                                    alertDialog.show();
    //                                }
    //                            });
    //                        }
    //                        return;
    //                    }
    //
    //                    HttpEntity resEntity = response.getEntity();
    //
    //                    System.out.println("----------------------------------------");
    //                    System.out.println(response.getStatusLine());
    //                    System.out.println(response.getAllHeaders());
    //
    //                    if (resEntity != null) {
    //                        System.out.println("Response content length: " + resEntity.getContentLength());
    //
    //
    //                        InputStream content = resEntity.getContent();
    //                        BufferedReader reader = new BufferedReader(
    //                                new InputStreamReader(content));
    //                        String line;
    //                        while ((line = reader.readLine()) != null) {
    //                            builder.append(line);
    //                        }
    //
    //
    //                    }
    //                    String readFeed = builder.toString();
    //                    System.out.println(">>>>>>>>>>"+readFeed);
    //                    JSONObject jsOb = new JSONObject(readFeed);
    //
    //                    String status=jsOb.getString("success");
    //                    String errorMsg=null;
    //                    if(status != null && status.equals("false") ){
    //                        errorMsg=jsOb.getString("message");							
    //                    }
    //                    System.out.println("status::"+status);
    //                    System.out.println("errorMsg::"+errorMsg);
    //
    //                    if (pd!=null && pd.isShowing()) {
    //                        pd.dismiss();
    //                    }
    //
    //                    data = true;
    //
    //                } finally {
    //                }
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //
    //                final String msg=e.getMessage();
    //                ((Activity) ctx).runOnUiThread(new Runnable() {
    //                    public void run() {
    //                        AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
    //                        alertDialog.setTitle("Error Uploading");
    //                        alertDialog.setMessage(msg);
    //                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    //                            public void onClick(DialogInterface dialog, int which) {
    //                                return;
    //                            } }); 
    //                        alertDialog.show();
    //                    }
    //                });
    //
    //                return;
    //            }
    //        }
    //
    //        @Override
    //        public void onProgress(int num) {
    //            if (pd != null) {
    //                int progress = (int) (num * 100 / uploadingFileSize);
    //                Log.d("Petsie", "Data transferred:" + num + "/" + uploadingFileSize + ", setting progress to : " + progress);
    //                pd.setProgress(progress);
    //            }
    //        }
    //
    //    }		


}
