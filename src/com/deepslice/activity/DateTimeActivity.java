package com.deepslice.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.database.HelperSharedPreferences;
import com.deepslice.model.LocationDetails;
import com.deepslice.model.OrderInfo;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.DeepsliceApplication;
import com.deepslice.utilities.Utils;

public class DateTimeActivity extends Activity implements OnClickListener {

    EditText nameEditText;
    EditText emailEditText;
    EditText phoneEditText;
    Button registerButton;
    Button startOrtderButton;
    CheckBox sendEmailCheckBox;
    TextView datePick, timePick;
    Date openTime;
    Date closeTime;
    DatePickerDialog datePickDialog = null;
    TimePickerDialog timePickDiag = null;
    Calendar cal;
    long setUpTime;
    int yr, mnth, dy, hr;
    boolean isOpen;
    boolean firstTime;

    String dateToSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(this, "92b170cf");
        
        setContentView(R.layout.date_time_select);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        TextView storeName = (TextView) findViewById(R.id.textView2);

        cal = Calendar.getInstance();
        Date d = cal.getTime();
        Log.e("Time in millis :", cal.getTimeInMillis() + "");
        Log.e("Date :", d + "");
        Log.e("Hour", d.getHours() + "");
        yr = cal.get(Calendar.YEAR);
        mnth = cal.get(Calendar.MONTH);
        dy = cal.get(Calendar.DAY_OF_MONTH);

        //		DelLocations eBean;
        Bundle b = this.getIntent().getExtras();

        // String location=b.getString("location");
        String store = b.getString("store");
        storeName.setText(store);

        cal = Calendar.getInstance();

        SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy");
        dateToSave = date_format.format(cal.getTime());

        timePickDiag = new TimePickerDialog(DateTimeActivity.this,
                callBackTimePick, cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), true);

        Calendar cal2 = Calendar.getInstance();
        datePickDialog = new DatePickerDialog(DateTimeActivity.this, callBackDatePick, cal2.get(Calendar.YEAR),
                cal2.get(Calendar.MONTH), cal2.get(Calendar.DAY_OF_MONTH));

        LocationDetails selectedLocation = AppProperties.getLocationObj(DateTimeActivity.this);
        openTime = AppProperties.parseStringDate(selectedLocation.getOpeningTime(), "M/dd/yyyy h:mm:ss a");
        closeTime = AppProperties.parseStringDate(selectedLocation.getClosingTime(), "M/dd/yyyy h:mm:ss a");
        Date currentTime = Calendar.getInstance().getTime();

        isOpen = false;
        if (currentTime.getHours() > openTime.getHours()
                && currentTime.getHours() < closeTime.getHours())
            isOpen = true;

        if (isOpen == false) {
            TextView storeOpenStatus = (TextView) findViewById(R.id.textView4);
            storeOpenStatus
            .setText("Your selected store is currently Closed. Please select your order date and time below:");

        }
        initializeAllViews();
        firstTime = true;

    }
    
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        BugSenseHandler.startSession(this);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        BugSenseHandler.closeSession(this);
    }

    private void initializeAllViews() {
        startOrtderButton = (Button) findViewById(R.id.startOrderingButton);
        startOrtderButton.setOnClickListener(this);
        
        Button ASAP = (Button) findViewById(R.id.b_asap);
        ASAP.setOnClickListener(this);

        datePick = (TextView) findViewById(R.id.datePicker);
        datePick.setOnClickListener(this);

        timePick = (TextView) findViewById(R.id.timePicker);
        timePick.setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat date_format = new SimpleDateFormat("EEE dd-MMM");
        String formated = date_format.format(cal.getTime());
        datePick.setText(formated);

        timePick.setText(getFullTime(cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE)));

        TextView storeTime = (TextView) findViewById(R.id.textView3);
        storeTime.setText("The store current time is " + getFullTime(cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE)));

        //		ImageView emgBack = (ImageView) findViewById(R.id.farwordImageView);
        //		emgBack.setOnClickListener(new OnClickListener() {
        //			public void onClick(View v) {
        //				finish();
        //			}
        //		});

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.b_asap:
                if(cal.get(Calendar.HOUR_OF_DAY) >= openTime.getHours() && 
                cal.get(Calendar.HOUR_OF_DAY) < closeTime.getHours()){
                    OrderInfo orderInfo = ((DeepsliceApplication) getApplication()).loadOrderInfo();
                    orderInfo.setIsTimedOrder(false);
                    ((DeepsliceApplication) getApplication()).saveOrderInfo(orderInfo);
                    startActivity(new Intent(new Intent(this, PaymentSelectionActivity.class)));
                }else {
                    Utils.openErrorDialog(DateTimeActivity.this,
                            "Store is not opened right now.\nPlease select some other time!");
                }
                break;

            case R.id.startOrderingButton:
                String[] timeStorage = timePick.getText().toString().split("[:]");

                if(startOrtderButton.getText().toString().equals("Delivery Time")){
                    Utils.openErrorDialog(DateTimeActivity.this,
                            "Please choose a time first.");
                }
                else if ((Integer.parseInt(timeStorage[0]) >= openTime.getHours() && Integer
                        .parseInt(timeStorage[0]) < closeTime.getHours())) {

                    OrderInfo orderInfo = ((DeepsliceApplication) getApplication()).loadOrderInfo();

                    //                    if(startOrtderButton.getText().toString().equals("ASAP"))
                    //                        orderInfo.setIsTimedOrder(false);
                    //                    else{
                    orderInfo.setIsTimedOrder(true);
                    orderInfo.setTimedOrder_Time(timePick.getText().toString());
                    orderInfo.setTimedOrder_Date(dateToSave);
                    //                    }

                    ((DeepsliceApplication) getApplication()).saveOrderInfo(orderInfo);

                    //              Toast.makeText(DateTimeActivity.this, "Thank you, Your order is taken.",  Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(new Intent(this, PaymentSelectionActivity.class)));

                }else {
                    Utils.openErrorDialog(DateTimeActivity.this,
                            "Store is not open on selected time.\nPlease select some other time!");
                }


                //            
                //			// if(AppProperties.isLoogedIn)
                //			if (!HelperSharedPreferences.getSharedPreferencesString(
                //					DateTimeActivity.this, "emailName", "").equals("")
                //					|| !HelperSharedPreferences.getSharedPreferencesString(
                //							DateTimeActivity.this, "userName", "").equals("")
                //					|| AppProperties.isLoggedIn) {
                //				if ((Integer.parseInt(timeStorage[0]) > openTime.getHours() && Integer
                //						.parseInt(timeStorage[0]) < closeTime.getHours())
                //						|| ("ASAP".equalsIgnoreCase(startOrtderButton.getText()
                //								.toString()) && (cal.get(Calendar.HOUR_OF_DAY) > openTime
                //								.getHours() && cal.get(Calendar.HOUR_OF_DAY) < closeTime
                //								.getHours()))) {
                ////				    Toast.makeText(DateTimeActivity.this, "Thank you, Your order is taken.",  Toast.LENGTH_SHORT).show();
                //					startActivity(new Intent(new Intent(this, PaymentSelectionActivity.class)));
                //
                //				}else {
                //					Utils.openErrorDialog(DateTimeActivity.this,
                //							"Store is not open on selected time.\nPlease select some other time!");
                //				}
                //
                //			}else if((!AppSharedPreference.getData(DateTimeActivity.this,"customerName","").equals("")||!AppSharedPreference.getData(DateTimeActivity.this,"customerEmail","").equals("")
                //                    ||!AppSharedPreference.getData(DateTimeActivity.this,"customerPhone","").equals(""))){
                ////                Toast.makeText(DateTimeActivity.this, "Thank you, Your order is taken.",  Toast.LENGTH_SHORT).show();
                //                startActivity(new Intent(new Intent(this, PaymentSelectionActivity.class)));
                //            } else {
                //				// if (Integer.parseInt(timeStorage[0]) > openTime.getHours()
                //				// && Integer.parseInt(timeStorage[0]) < closeTime
                //				// .getHours()) {
                //				//
                //				// startActivity(new Intent(new Intent(this,
                //				// CustomerDetailsActivity.class)));
                //				// }
                //			
                //				Date currentTime = Calendar.getInstance().getTime();
                //				// Calendar cal = Calendar.getInstance();
                //				
                //                if ((Integer.parseInt(timeStorage[0]) > openTime.getHours() && Integer
                //                        .parseInt(timeStorage[0]) < closeTime.getHours())
                //                        || ("ASAP".equalsIgnoreCase(startOrtderButton.getText()
                //                                .toString()) && (cal.get(Calendar.HOUR_OF_DAY) > openTime
                //                                .getHours() && cal.get(Calendar.HOUR_OF_DAY) < closeTime
                //                                .getHours()))) {
                ////                    Toast.makeText(DateTimeActivity.this, "Thank you, Your order is taken.",  Toast.LENGTH_SHORT).show();
                //                    startActivity(new Intent(new Intent(this, PaymentSelectionActivity.class)));
                //
                //                }
                ////				if (currentTime.getHours() > openTime.getHours()
                ////						&& currentTime.getHours() < closeTime.getHours()) {
                ////					
                ////					startActivity(new Intent(new Intent(this,
                ////							CustomerDetailsActivity.class)));
                ////				} 
                //                else {
                //					Utils.openErrorDialog(DateTimeActivity.this,
                //							"Store is not open on selected time.\nPlease select some other time!");
                //				}
                //			
                //			}

                //			AppSharedPreference.putData(DateTimeActivity.this, "deliveryTime", startOrtderButton.getText().toString());

                break;
            case R.id.datePicker:
                datePickDialog.show();

                break;
            case R.id.timePicker:
                firstTime = true;
                timePickDiag.show();
                break;

            default:
                break;
        }

    }

    private TimePickerDialog.OnTimeSetListener callBackTimePick = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Log.e("In the time picker dialog ------------------------",""+view.getId());
            if (hourOfDay >= openTime.getHours()
                    && hourOfDay <= closeTime.getHours()) {


                //				timePick.setText(getFullTime(hourOfDay, minute));
                //				startOrtderButton.setText(timePick.getText() + ", "
                //						+ datePick.getText());
                // hr=hourOfDay;

                //				Calendar calendar = Calendar.getInstance();
                //				calendar.set(yr, mnth, dy, hourOfDay, minute);
                //				setUpTime = calendar.getTimeInMillis();
                //				Log.e("Time: ", setUpTime + "");


                if(yr>cal.get(Calendar.YEAR) || mnth>cal.get(Calendar.MONTH) || dy>cal.get(Calendar.DAY_OF_MONTH)){
                    timePick.setText(getFullTime(hourOfDay, minute));
                    startOrtderButton.setText(timePick.getText() + ", " + datePick.getText());
                    Log.e("It is vald","sd");
                    Log.e("year", yr + "");
                    Log.e("Month", mnth + "");
                    Log.e("Day", dy + "");
                }

                else if (hourOfDay < cal.get(Calendar.HOUR_OF_DAY)  ) {

                    minute= cal.get(Calendar.MINUTE);
                    hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
                    view.setCurrentHour(hourOfDay);
                    view.setCurrentMinute(minute);					
                    Toast.makeText(DateTimeActivity.this, "Please select future time !", Toast.LENGTH_SHORT).show();
                } else if (hourOfDay == cal.get(Calendar.HOUR_OF_DAY) && minute < cal.get(Calendar.MINUTE)) {

                    minute= cal.get(Calendar.MINUTE);
                    hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
                    view.setCurrentHour(hourOfDay);
                    view.setCurrentMinute(minute);

                    Toast.makeText(DateTimeActivity.this, "Please select future time !", Toast.LENGTH_SHORT).show();
                } else {
                    timePick.setText(getFullTime(hourOfDay, minute));
                    startOrtderButton.setText(timePick.getText() + ", " + datePick.getText());
                }
            } else {
                dialogCount();
            }

        }

    };

    private void dialogCount() {
        if (firstTime == true) {
            firstTime = false;
            startOrtderButton.setText("Delivery Time");
            Utils.openErrorDialog(DateTimeActivity.this,
                    "Store is not open on selected time.\nPlease select some other time!");
        }
    }
    
    private DatePickerDialog.OnDateSetListener callBackDatePick = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Calendar cal = Calendar.getInstance();
            if (year < cal.get(Calendar.YEAR)
                    || monthOfYear < cal.get(Calendar.MONTH)
                    || dayOfMonth < cal.get(Calendar.DAY_OF_MONTH)) {

                year = cal.get(Calendar.YEAR);
                monthOfYear = cal.get(Calendar.MONTH);
                dayOfMonth= cal.get(Calendar.DAY_OF_MONTH);

                view.updateDate(year, monthOfYear, dayOfMonth);

                Toast.makeText(DateTimeActivity.this, "Please Select future date!", Toast.LENGTH_SHORT).show();
                startOrtderButton.setText("Delivery Time");
            } else {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy");
                dateToSave = date_format.format(cal.getTime());

                date_format = new SimpleDateFormat("EEE dd-MMM");
                String formated = date_format.format(cal.getTime());
                datePick.setText(formated);
                startOrtderButton.setText(timePick.getText() + ", " + datePick.getText());

                yr = year;
                mnth = monthOfYear;
                dy = dayOfMonth;
            }
        }
    };

    public String getFullTime(int h, int m) {
        String time = "00:00";
        String hour = String.valueOf(h);
        String minute = String.valueOf(m);
        if (h < 10)
            hour = "0" + hour;
        if (m < 10)
            minute = "0" + minute;

        time = hour + ":" + minute;
        return time;
    }
}