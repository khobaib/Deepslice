package com.deepslice.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.AppInfo;
import com.deepslice.model.Customer;
import com.deepslice.model.DealOrder;
import com.deepslice.model.OrderInfo;
import com.deepslice.model.PaymentInfo;
import com.deepslice.model.ServerResponse;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;
import com.deepslice.utilities.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PayByCashActivity extends Activity{

    TextView tvTotalPrice, descriptionText;

    JsonParser jsonParser = new JsonParser();
    ProgressDialog pDialog;

    String myIp;
    String totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_by_cash);

        tvTotalPrice = (TextView)findViewById(R.id.totalPrice);
        descriptionText=(TextView)findViewById(R.id.textView1);
        TextView txtUserName=(TextView)findViewById(R.id.textUserName);


        Button PlaceOrder = (Button)findViewById(R.id.b_place_order);
        PlaceOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                setPaymentInfo();


                Intent intent = new Intent(PayByCashActivity.this, ThankYouActivity.class);
                startActivity(intent);							 
            }
        });

        pDialog = ProgressDialog.show(PayByCashActivity.this, "", "Please wait...", true, false);


        Customer customer = ((DeepsliceApplication) getApplication()).loadCustomer();
        txtUserName.setText("Dear "+ customer.getCustomerName());
    }


    private void setPaymentInfo() {
        PaymentInfo pInfo = ((DeepsliceApplication) getApplication()).loadPaymentInfo();

        pInfo.setPaymentNo(1);
        pInfo.setPaymentType("Cash");
        pInfo.setPaymentSubType("Later");
        pInfo.setAmount(Double.parseDouble(totalPrice));
        pInfo.setCardType("");
        pInfo.setNameOnCard("");
        pInfo.setCardNo("");
        pInfo.setCardSecurityCode("");
        pInfo.setExpiryMonth(0);
        pInfo.setExpiryYear(0);

        ((DeepsliceApplication) getApplication()).savePaymentInfo(pInfo);

        OrderInfo oInfo = ((DeepsliceApplication) getApplication()).loadOrderInfo();
        oInfo.setPaymentStatus("PENDING");
        ((DeepsliceApplication) getApplication()).saveOrderInfo(oInfo);

    }


    @Override
    protected void onResume() {
        super.onResume();

        OrderInfo oInfo = ((DeepsliceApplication) getApplication()).loadOrderInfo();
        totalPrice = Constants.twoDForm.format(oInfo.getTotalPrice());

        tvTotalPrice.setText("$" + totalPrice); 
        
        new GetIPAddress().execute();
    }


    public class GetIPAddress extends AsyncTask<Void, Void, Boolean>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + "GetClientIP.aspx";
            ServerResponse response = jsonParser.retrieveGETResponse(url, null, Constants.API_RESPONSE_TYPE_JSON_OBJECT);

            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();

                try {
                    myIp = jsonObj.getString("client_ip");
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
                Log.d(">>>>>>>", "IP Address successfully.");

                StringBuffer theMessageString = new StringBuffer("");
                theMessageString.append("By placing your order, you are confirming your order of ");
                theMessageString.append(tvTotalPrice.getText().toString());
                theMessageString.append(".\n\n");
                theMessageString.append("Your IP address of ");
                theMessageString.append(myIp);
                theMessageString.append(" will be logged when your order is placed.\n\n");
                theMessageString.append("You may receive a phone call to confirm your order. If you are unavailable, then your order may be canceled.");

                descriptionText.setText(theMessageString.toString());
            }
            else{

            }
        }
    }
}
