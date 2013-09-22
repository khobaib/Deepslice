package com.deepslice.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.OrderInfo;
import com.deepslice.model.PaymentInfo;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;
import com.deepslice.utilities.Utils;

public class PayByCardActivity extends Activity {

    EditText Name, CardNo, SecurityCode, ExpireYear;
    Spinner sCardType, sMonth;
    TextView tvTotalPrice;

    String name, cardNo, sCode, expMonth, expYear;
    String selectedCardType;
    String totalPrice;
    int expMonthIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_by_card);

        Name = (EditText) findViewById(R.id.et_name);
        CardNo = (EditText) findViewById(R.id.et_credit_card_no);
        SecurityCode = (EditText) findViewById(R.id.et_credit_card_security_code);
        //        ExpireMonth = (EditText) findViewById(R.id.et_month);
        ExpireYear = (EditText) findViewById(R.id.et_year);

        tvTotalPrice = (TextView) findViewById(R.id.totalPrice);
        List<String> orderInfo = Utils.OrderInfo(PayByCardActivity.this);
        totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);

        tvTotalPrice.setText("$" + totalPrice);

        sCardType = (Spinner) findViewById(R.id.s_card_type);
        generateSpinner(sCardType, Constants.CREDIT_CARD_TYPE);
        sCardType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                selectedCardType = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        sMonth = (Spinner) findViewById(R.id.s_month);
        generateSpinner(sMonth, Constants.MONTH_NAME);
        sMonth.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                expMonth = (String)parent.getItemAtPosition(position);
                expMonthIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }



    private void generateSpinner(Spinner spinner, String[] arrayToSpinner) {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
                PayByCardActivity.this, android.R.layout.simple_spinner_item, arrayToSpinner);
        spinner.setAdapter(myAdapter);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }


    public void onClickPlaceOrder(View v){

        name = Name.getText().toString();
        cardNo = CardNo.getText().toString();
        sCode = SecurityCode.getText().toString();
        expYear = ExpireYear.getText().toString();

        if(name == null || name.equals("")){
            Toast.makeText(PayByCardActivity.this, "Please insert your name correctly.", Toast.LENGTH_SHORT).show();
        }
        else if(cardNo == null || cardNo.equals("")){
            Toast.makeText(PayByCardActivity.this, "Please insert a valid Card Number.", Toast.LENGTH_SHORT).show();
        }
        else if(sCode == null || sCode.equals("")){
            Toast.makeText(PayByCardActivity.this, "You didn't enter security code.", Toast.LENGTH_SHORT).show();
        }
        else if(expYear == null || expYear.equals("") || !isYearValid(expYear)){
            Toast.makeText(PayByCardActivity.this, "Please insert a valid expire year.", Toast.LENGTH_SHORT).show();
        }

        else{
            
            setPaymentInfo();

            Intent intent = new Intent(PayByCardActivity.this, ThankYouActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
//            finish();
            // continue
        }
    }


    private void setPaymentInfo() {
        PaymentInfo pInfo = ((DeepsliceApplication) getApplication()).loadPaymentInfo();
        
        pInfo.setPaymentNo(1);
        pInfo.setPaymentType("Card");
        pInfo.setPaymentSubType("Now");
        pInfo.setAmount(Double.parseDouble(totalPrice));
        pInfo.setCardType(selectedCardType);
        pInfo.setNameOnCard(name);
//        pInfo.setCardNo(cardNo);
//      pInfo.setCardSecurityCode(sCode);
//      pInfo.setExpiryMonth(expMonthIndex + 1);
//        pInfo.setExpiryYear(Integer.parseInt(expYear.substring(2, 4)));         // last-2 digit of the year
        
        pInfo.setCardNo("4444333322221111");
        pInfo.setCardSecurityCode("999");
        pInfo.setExpiryMonth(9);
        pInfo.setExpiryYear(15);
        
        ((DeepsliceApplication) getApplication()).savePaymentInfo(pInfo);
        
        OrderInfo oInfo = ((DeepsliceApplication) getApplication()).loadOrderInfo();
        oInfo.setPaymentStatus("PAID");
        ((DeepsliceApplication) getApplication()).saveOrderInfo(oInfo);
                
    }



    private boolean isYearValid(String sYear){
        int year = Integer.parseInt(sYear);
        if(year >= 2013 && year <= 2033)
            return true;
        return false;
    }




}
