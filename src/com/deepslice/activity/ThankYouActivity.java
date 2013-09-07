package com.deepslice.activity;

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
import android.widget.Toast;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.CustomerInfo;
import com.deepslice.model.DeliveryLocation;
import com.deepslice.model.NewDealsOrder;
import com.deepslice.model.NewDealsOrderDetails;
import com.deepslice.model.NewProductOrder;
import com.deepslice.model.NewToppingsOrder;
import com.deepslice.model.OrderInfo;
import com.deepslice.model.PaymentInfo;
import com.deepslice.model.ServerResponse;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;
import com.google.gson.Gson;

public class ThankYouActivity extends Activity {
    
    DeepsliceApplication appInstance;
    ProgressDialog pDialog;
    JsonParser jsonParser;
    
    String finalOrderData;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thank_you);
        
        pDialog = new ProgressDialog(ThankYouActivity.this);
        jsonParser = new JsonParser();
        appInstance = (DeepsliceApplication) getApplication();
    }
    
    public void onClickProceed(View v){
        
        formOrder();
        sendOrder();
        
//        DeepsliceDatabase dbInstance = new DeepsliceDatabase(ThankYouActivity.this);
//        dbInstance.open();
//        dbInstance.cleanAllOrderTable();
//        dbInstance.close(); 

        Toast.makeText(ThankYouActivity.this, "Your order is taken. thank you",  Toast.LENGTH_SHORT).show();
        
//        Intent intent = new Intent(ThankYouActivity.this, PickupDeliverActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();      
    }

    private void sendOrder() {
        new SendOrderToServer().execute();
        
    }
    
    
    
    
    public class SendOrderToServer extends AsyncTask<Void, Void, Boolean>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.setMessage("Your order is being processed. Please wait...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "SaveOrder.aspx";
            long reqSendingTime = System.currentTimeMillis();
            ServerResponse response = jsonParser.retrievePostResponse(url, finalOrderData);
            long responseReceivedTime = System.currentTimeMillis();
            Log.d(">>>><<<", "time for cloud retrieve = " + (responseReceivedTime - reqSendingTime)/1000 + " second");
            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

//                    deliveryLocationList = DeliveryLocation.parseDeliveryLocations(data);
                    long arrayParsedTime = System.currentTimeMillis();
                    Log.d(">>>><<<", "time for array parsing = " + (arrayParsedTime - responseReceivedTime)/1000 + " second");

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
            if(pDialog.isShowing())
                pDialog.dismiss();
            if(result){
                Log.d("SUCCESS", "success");
            }
        }
    }
    
    
    

    private void formOrder() {
        
        PaymentInfo pInfo = appInstance.loadPaymentInfo();
        CustomerInfo cInfo = appInstance.loadCustomerInfo();
        OrderInfo oInfo = appInstance.loadOrderInfo();
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(ThankYouActivity.this);
        dbInstance.open();
        List<NewProductOrder> productOrderList = dbInstance.getOrdersList();
        List<NewDealsOrder> dealOrderList = dbInstance.retrieveDealOrderList(true);
        dbInstance.close();
             
        Gson gson = new Gson();
        String paymentData = gson.toJson(pInfo);
        String customerData = gson.toJson(cInfo);
        String orderData = gson.toJson(oInfo);
        
        Log.d("PAYMENT DATA", paymentData);
        Log.d("CUSTOMER DATA", customerData);
        Log.d("ORDER DATA", orderData);
        
//        String finalOrderData;
        JSONObject finalOrderObj = new JSONObject();
        try {
            finalOrderObj.put("OrderInfo", orderData);
            finalOrderObj.put("PaymentInfo", paymentData);
            finalOrderObj.put("CustomerInfo", customerData);
            
            // inserting products
            JSONArray productListArray = new JSONArray();
            
            for(NewProductOrder pOrder : productOrderList){
                JSONObject thisProd = new JSONObject();
                
                int selection = pOrder.getSelection();
                thisProd.put("prodID", Integer.parseInt(pOrder.getProdId()));
                thisProd.put("IsWhole", (selection == Constants.PRODUCT_SELECTION_WHOLE));  
                thisProd.put("IsLeft", (selection == Constants.PRODUCT_SELECTION_LEFT));
                thisProd.put("IsRight", (selection == Constants.PRODUCT_SELECTION_RIGHT));
                thisProd.put("IsCreateByOwn", pOrder.getIsCreateByOwn());  
                thisProd.put("CaloriesQty", Double.parseDouble(pOrder.getCaloriesQty()));
                
                thisProd.put("Qty", Integer.parseInt(pOrder.getQuantity()));
                thisProd.put("Price", Double.parseDouble(pOrder.getPrice()));
                
                dbInstance.open();
                double orderToppingsPrice = dbInstance.retrieveProductToppingsPrice(pOrder.getPrimaryId());
                dbInstance.close();                
                thisProd.put("TotalPrice", (Double.parseDouble(pOrder.getPrice()) + orderToppingsPrice)); 
                
                JSONArray toppingsArray = new JSONArray();
                
                dbInstance.open();
                List<NewToppingsOrder> productToppingsList = dbInstance.retrieveToppingsOrderByProdOrderId(pOrder.getPrimaryId());
                dbInstance.close();  
                
                for(NewToppingsOrder toppingsOrder : productToppingsList){
                    JSONObject thisToppings = new JSONObject();
                    thisToppings.put("ToppingId", Integer.parseInt(toppingsOrder.getToppingsId()));
                    thisToppings.put("ToppingSizeId", Integer.parseInt(toppingsOrder.getToppingSizeId()));
                    thisToppings.put("IsSauce", toppingsOrder.getIsSauce());
                    thisToppings.put("ToppingPrice", Double.parseDouble(toppingsOrder.getToppingPrice()));
                    thisToppings.put("IsFreeWithPizza", toppingsOrder.getIsFreeWithPizza());
                    toppingsArray.put(thisToppings);
                }                
               
                thisProd.put("Toppings", toppingsArray);
                
                productListArray.put(thisProd);
            }
            
            finalOrderObj.put("ProductList", productListArray);
            
            // inserting deals
            JSONArray dealListArray= new JSONArray();
            for(NewDealsOrder dealsOrder : dealOrderList){
                JSONObject thisDeals = new JSONObject();
                
                thisDeals.put("CouponID", Integer.parseInt(dealsOrder.getCouponID()));
                thisDeals.put("DealPrice", Double.parseDouble(dealsOrder.getDealPrice()));
                thisDeals.put("TotalPrice", Double.parseDouble(dealsOrder.getTotalPrice()));
                thisDeals.put("Qty", Integer.parseInt(dealsOrder.getQuantity()));
                thisDeals.put("DealItem", dealsOrder.getDealItemCount());
                
                JSONArray dealsProductArray = new JSONArray();
                dbInstance.open();
                List<NewDealsOrderDetails> dealsOrderDetailsList = dbInstance.retrieveDealOrderDetailsList(dealsOrder.getPrimaryId());
                dbInstance.close(); 
 
                for(NewDealsOrderDetails dealsOrderDetails : dealsOrderDetailsList){
                    JSONObject thisDealOrderDetails = new JSONObject();
                    
                    thisDealOrderDetails.put("prodID", Integer.parseInt(dealsOrderDetails.getProdId()));
                    thisDealOrderDetails.put("CouponGroupID", Integer.parseInt(dealsOrderDetails.getCouponGroupId()));
                    thisDealOrderDetails.put("Qty", Integer.parseInt(dealsOrderDetails.getQty()));
                    thisDealOrderDetails.put("Price", Double.parseDouble(dealsOrderDetails.getDiscountedPrice()));
                    
                    JSONArray dealsToppingsArray = new JSONArray();
                    dbInstance.open();
                    List<NewToppingsOrder> dealsOrderToppingsList = dbInstance.retrieveDealToppings(dealsOrderDetails.getPrimaryId());
                    dbInstance.close();  
                    
                    for(NewToppingsOrder dealOrderToppings : dealsOrderToppingsList){
                        JSONObject thisToppings = new JSONObject();
                        
                        thisToppings.put("ToppingId", Integer.parseInt(dealOrderToppings.getToppingsId()));
                        thisToppings.put("ToppingSizeId", Integer.parseInt(dealOrderToppings.getToppingSizeId()));
                        thisToppings.put("IsSauce", dealOrderToppings.getIsSauce());
                        thisToppings.put("ToppingPrice", Double.parseDouble(dealOrderToppings.getToppingPrice()));
                        thisToppings.put("IsFreeWithPizza", dealOrderToppings.getIsFreeWithPizza());
                        
                        dealsToppingsArray.put(thisToppings);
                    }                   
                    
                    thisDealOrderDetails.put("Toppings", dealsToppingsArray);
                    
                    dealsProductArray.put(thisDealOrderDetails);
                }
                
                thisDeals.put("DealProduct", dealsProductArray);
                
                dealListArray.put(thisDeals);
            }
            finalOrderObj.put("DealsList", dealListArray);
            
            JSONObject jsonToPost = new JSONObject();
            jsonToPost.put("order_detail", finalOrderObj);
            
            finalOrderData = jsonToPost.toString();
            Log.d("FINAL FINAL FINAL FINAL FINAL", finalOrderData);  
            
            
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }

}