package com.deepslice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.deepslice.utilities.Constants;

public class PayByCardActivity extends Activity {

    EditText Name, CardNo, SecurityCode, ExpireYear;
    Spinner sCardType, sMonth;

    String name, cardNo, sCode, expMonth, expYear;
    String selectedCardType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_by_card);

        Name = (EditText) findViewById(R.id.et_name);
        CardNo = (EditText) findViewById(R.id.et_credit_card_no);
        SecurityCode = (EditText) findViewById(R.id.et_credit_card_security_code);
//        ExpireMonth = (EditText) findViewById(R.id.et_month);
        ExpireYear = (EditText) findViewById(R.id.et_year);
        
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


    public void onClickContinue(View v){

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

            // continue
        }
    }
    
    
    private boolean isYearValid(String sYear){
        int year = Integer.parseInt(sYear);
        if(year >= 2013 && year <= 2033)
            return true;
        return false;
    }




}
