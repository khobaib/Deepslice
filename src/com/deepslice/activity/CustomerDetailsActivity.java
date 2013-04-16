package com.deepslice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.deepslice.database.HelperSharedPreferences;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Constants;
import com.deepslice.vo.CustomerDetailsVo;

public class CustomerDetailsActivity extends Activity implements
		OnClickListener {

	EditText nameEditText;
	EditText emailEditText;
	EditText phoneEditText;
	Button registerButton;
	Button startOrtderButton;
	CheckBox sendEmailCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_details);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initializeAllViews();

		ImageView emgBack= (ImageView)findViewById(R.id.farwordImageView);
		emgBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initializeAllViews() {

		nameEditText = (EditText) findViewById(R.id.yourNameEditText);
		emailEditText = (EditText) findViewById(R.id.yourEmailEditText);
		phoneEditText = (EditText) findViewById(R.id.yourPhoneEditText);
		registerButton = (Button) findViewById(R.id.registerButton);
		startOrtderButton = (Button) findViewById(R.id.startOrderingButton);
		sendEmailCheckBox = (CheckBox) findViewById(R.id.checkBox);
		registerButton.setOnClickListener(this);
		startOrtderButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.registerButton:
			 if (velidateregisterButton()) {

			Bundle bundle = new Bundle();
			bundle.putSerializable(Constants.CUSTOMER_DETAILS,
					getCustomerDetailsVo());
           HelperSharedPreferences.putSharedPreferencesString(CustomerDetailsActivity.this, "userName", nameEditText.getText().toString());
           HelperSharedPreferences.putSharedPreferencesString(CustomerDetailsActivity.this,"emailName" ,emailEditText.getText().toString());
           HelperSharedPreferences.putSharedPreferencesLong(CustomerDetailsActivity.this,"phoneNo" ,Long.parseLong(phoneEditText.getText().toString()));
           HelperSharedPreferences.putSharedPreferencesBoolean(CustomerDetailsActivity.this,"sendEmailCheckbox" ,sendEmailCheckBox.isChecked());
			startActivity(new Intent(this, RegisterActivity.class)
					.putExtras(bundle));

			 }
			break;

		case R.id.startOrderingButton:
			if (velidateregisterButton())
				{
				AppSharedPreference.putData(CustomerDetailsActivity.this, "customerName", nameEditText.getText().toString());
				AppSharedPreference.putData(CustomerDetailsActivity.this, "customerEmail", emailEditText.getText().toString());
				AppSharedPreference.putData(CustomerDetailsActivity.this, "customerPhone", phoneEditText.getText().toString());
				startActivity(new Intent(new Intent(this, MyOrderActivity.class)));
				}
			break;

		default:
			break;
		}

	}

	private CustomerDetailsVo getCustomerDetailsVo() {
		CustomerDetailsVo customerDetailsVo = new CustomerDetailsVo();
		customerDetailsVo.setName(nameEditText.getText().toString().trim());
		customerDetailsVo.setEmail(emailEditText.getText().toString().trim());
		customerDetailsVo.setPhone(phoneEditText.getText().toString().trim());
		customerDetailsVo.setSendEmail(sendEmailCheckBox.isSelected());
		return customerDetailsVo;
	}

	private boolean velidateregisterButton() {

		if (nameEditText.getText().toString() != null
				&& nameEditText.getText().toString().trim().length() > 0) {

			if (emailEditText.getText().toString().trim() != null) {

				if (phoneEditText.getText().toString() != null
						&& phoneEditText.getText().toString().trim().length() == 10) {
					return true;
				} else {
					Toast.makeText(this, R.string.phoneErrorMessage,
							Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(this, R.string.emailErrorMessage,
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, R.string.nameErrorMessage, Toast.LENGTH_LONG)
					.show();
		}
		return false;
	}

}
