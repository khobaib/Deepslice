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

import com.deepslice.database.HelperSharedPreferences;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Utils;
import com.deepslice.vo.LocationDetails;

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
	boolean isOpen;
	boolean firstTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_time_select);
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		TextView storeName = (TextView) findViewById(R.id.textView2);
		Bundle b = this.getIntent().getExtras();
		// String location=b.getString("location");
		String store = b.getString("store");
		storeName.setText(store);

		cal = Calendar.getInstance();
		timePickDiag = new TimePickerDialog(DateTimeActivity.this,
				callBackTimePick, cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), true);

		Calendar cal2 = Calendar.getInstance();
		datePickDialog = new DatePickerDialog(DateTimeActivity.this,
				callBackDatePick, cal2.get(Calendar.YEAR),
				cal2.get(Calendar.MONTH), cal2.get(Calendar.DAY_OF_MONTH));
		LocationDetails selectedLocation = AppProperties
				.getLocationObj(DateTimeActivity.this);
		openTime = AppProperties.parseStringDate(
				selectedLocation.getOpeningTime(), "M/dd/yyyy h:mm:ss a");
		closeTime = AppProperties.parseStringDate(
				selectedLocation.getClosingTime(), "M/dd/yyyy h:mm:ss a");
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

		
	
	private void initializeAllViews() {
		startOrtderButton = (Button) findViewById(R.id.startOrderingButton);
		startOrtderButton.setOnClickListener(this);

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
		storeTime.setText("The store current time is "
				+ getFullTime(cal.get(Calendar.HOUR_OF_DAY),
						cal.get(Calendar.MINUTE)));

		ImageView emgBack = (ImageView) findViewById(R.id.farwordImageView);
		emgBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.startOrderingButton:
			String[] timeStorage = timePick.getText().toString().split("[:]");
			// if(AppProperties.isLoogedIn)
			if (!HelperSharedPreferences.getSharedPreferencesString(DateTimeActivity.this, "emailName", "").equals("")|| !HelperSharedPreferences.getSharedPreferencesString(
							DateTimeActivity.this, "userName", "").equals("")||AppProperties.isLoogedIn) {
				if ((Integer.parseInt(timeStorage[0]) > openTime.getHours() && Integer.parseInt(timeStorage[0]) < closeTime.getHours())
						|| ("ASAP".equalsIgnoreCase(startOrtderButton.getText().toString()) && (cal.get(Calendar.HOUR_OF_DAY) > openTime
								.getHours() && cal.get(Calendar.HOUR_OF_DAY) < closeTime.getHours()))) {
					startActivity(new Intent(new Intent(this,
							MyOrderActivity.class)));
					
				} else {
					Utils.openErrorDialog(DateTimeActivity.this,
							"Store is not open on selected time.\nPlease select some other time!");
				}
				
			} else {				
//				if (Integer.parseInt(timeStorage[0]) > openTime.getHours()
//						&& Integer.parseInt(timeStorage[0]) < closeTime
//								.getHours()) {
//					
//					startActivity(new Intent(new Intent(this,
//							CustomerDetailsActivity.class)));
//				}
				//  Changed By Faysal///////////////////////////////////////////////////////////////
				Date currentTime = Calendar.getInstance().getTime();
				if (currentTime.getHours() > openTime.getHours()
						&& currentTime.getHours() < closeTime.getHours()){
					startActivity(new Intent(new Intent(this,
							CustomerDetailsActivity.class)));
				}
				else {
					Utils.openErrorDialog(DateTimeActivity.this,
							"Store is not open on selected time.\nPlease select some other time!");				
				}	
				/////////////////////////////////////////////////////////////////////////////
			}

			AppSharedPreference.putData(DateTimeActivity.this, "deliveryTime",
					startOrtderButton.getText().toString());
			
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
			if (hourOfDay >= openTime.getHours()
					&& hourOfDay <= closeTime.getHours()) {
				timePick.setText(getFullTime(hourOfDay, minute));
				startOrtderButton.setText(timePick.getText() + ", "
						+ datePick.getText());
			} else {
				dialogCount();
			}
			
		}

	};

	private void dialogCount() {
		if (firstTime == true) {
			firstTime = false;
			startOrtderButton.setText("ASAP");
			Utils.openErrorDialog(DateTimeActivity.this,
					"Store is not open on selected time.\nPlease select some other time!");
		}
	}

	private DatePickerDialog.OnDateSetListener callBackDatePick = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			Calendar cal = Calendar.getInstance();
			if (year < cal.get(Calendar.YEAR)
					|| monthOfYear < cal.get(Calendar.MONTH)
					|| dayOfMonth < cal.get(Calendar.DAY_OF_MONTH)) {
				Toast.makeText(DateTimeActivity.this,
						"Please Select future date!", Toast.LENGTH_SHORT)
						.show();
				startOrtderButton.setText("ASAP");
			} else {
				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.MONTH, monthOfYear);
				cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

				SimpleDateFormat date_format = new SimpleDateFormat(
						"EEE dd-MMM");
				String formated = date_format.format(cal.getTime());
				datePick.setText(formated);
				startOrtderButton.setText(timePick.getText() + ", "
						+ datePick.getText());
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
