package com.deepslice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.model.Customer;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;

public class RegisterActivity extends Activity implements OnClickListener {

    Customer customer;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button privacyPolicyButton;
    Button startOrderButton;
    
    DeepsliceApplication appInstance;

    //	final Handler mHandler = new Handler();
    //	final Runnable mUpdateResults = new Runnable() {        
    //		public void run() {            
    //			updateResultsInUi();        
    //		}    
    //	};


    //	ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(this, "92b170cf");
        
        setContentView(R.layout.register);
        
        appInstance = (DeepsliceApplication) getApplication();

        initializeAllViews();

        Bundle b = getIntent().getExtras();
        customer = (Customer) b.getSerializable(Constants.CUSTOMER_DETAILS);


        //		ImageView emgBack= (ImageView)findViewById(R.id.farwordImageView);
        //		emgBack.setOnClickListener(new OnClickListener() {
        //			public void onClick(View v) {
        //				finish();
        //			}
        //		});

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
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.conformPasswordEditText);
        privacyPolicyButton = (Button) findViewById(R.id.privacyPolicyButton);
        startOrderButton = (Button) findViewById(R.id.startOrderingButton);
        privacyPolicyButton.setOnClickListener(this);
        startOrderButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.privacyPolicyButton:

                break;

            case R.id.startOrderingButton:


                if (isPasswordValid()) {
                    customer.setPwd(passwordEditText.getText().toString().trim());
                    appInstance.saveCustomer(customer);
                    Boolean isOrderReady = appInstance.getIsOrderReady();
                    if (isOrderReady){
//                        AppSharedPreference.putBoolean(RegisterActivity.this, "MyOrder",false);
                        int orderType = appInstance.getOrderType();
                        if(orderType == Constants.ORDER_TYPE_DELIVERY) {
                            startActivity(new Intent(new Intent(this, LocationFromHistoryActivity.class)));
                            finish();
                        }else if(orderType == Constants.ORDER_TYPE_PICKUP){
                            startActivity(new Intent(new Intent(this, StoreFromHistoryActivity.class)));
                            finish();
                        }
                        else{
                            Toast.makeText(this, "Order method is not selected yet, please choose pickup or delivery.",  Toast.LENGTH_SHORT).show();
                        }

                    }  else {                       
                        startActivity(new Intent(new Intent(this, PickupDeliverActivity.class)));
                        finish();
                    }
                }
                break;

            default:
                break;
        }

    }

    private boolean isPasswordValid() {
        if (passwordEditText.getText().toString().trim().length() < Constants.PASSWORD_LENGTH){
            Toast.makeText(this, "Password too short (min 8 characters needed)!",Toast.LENGTH_LONG).show();
            return false;
        }
        if (!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())){
            Toast.makeText(this, "Password and confirm password do not match!",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //	public void readFromServer() {
    //		pd = ProgressDialog.show(RegisterActivity.this, "", "Loading...", true, false);
    //
    //		Thread t = new Thread() {            
    //			public void run() {                
    //
    //				try {
    //					startReadingFromServer();
    //				} catch (Exception ex)
    //				{
    //					System.out.println(ex.getMessage());
    //				}
    //				mHandler.post(mUpdateResults);            
    //			}
    //		};        
    //		t.start();    	
    //	}

    //	public void startReadingFromServer() {
    //		StringBuilder builder = new StringBuilder("");
    //		StringBuilder builderPass = new StringBuilder("");
    //		Integer timeoutMs = Integer.valueOf(60 * 1000);
    //		HttpParams httpParams = new BasicHttpParams();
    //		HttpConnectionParams.setConnectionTimeout(httpParams, timeoutMs);
    //		HttpConnectionParams.setSoTimeout(httpParams, timeoutMs);
    //		HttpClient httpclient = new DefaultHttpClient(httpParams);
    //
    //		//Customer details
    //		String queryStringOne="CustomerName="+Utils.getTextForUrl(customerDetailsVo.getName())+
    //				"&Mailing_Address="+Utils.getTextForUrl(customerDetailsVo.getEmail())+"&CustomerPhone="+Utils.getTextForUrl(customerDetailsVo.getPhone())+
    //				"&Set_Discount=0&btnRegister=true";
    //		HttpGet httpGet = new HttpGet(Constants.NAMESPACE+"CustomerDetail.aspx?"+queryStringOne);
    //		
    //		//Password registration
    //		String queryStringTwo= "CustomerPassword=" + Utils.getTextForUrl(customerDetailsVo.getPassword())+ "&";
    //		queryStringTwo += "ConfirmPassword=" + Utils.getTextForUrl(customerDetailsVo.getPassword());
    //		HttpGet httpGetPass = new HttpGet(Constants.NAMESPACE+"Registration.aspx?"+queryStringTwo);
    //		
    //		try {
    //			//do save registration
    //			HttpResponse response = httpclient.execute(httpGet);
    //			StatusLine statusLine = response.getStatusLine();
    //			int statusCode = statusLine.getStatusCode();
    //			if (statusCode == 200) {
    //				HttpEntity entity = response.getEntity();
    //				InputStream content = entity.getContent();
    //				BufferedReader reader = new BufferedReader(
    //						new InputStreamReader(content,"ISO-8859-1"));
    //				String line;
    //				while ((line = reader.readLine()) != null) {
    //					builder.append(line);
    //				}
    //				System.out.println("Customer Detail Response::"+builder.toString());
    //				//do save password call
    //				HttpResponse responsePass = httpclient.execute(httpGetPass);
    //				StatusLine statusLinePass = responsePass.getStatusLine();
    //				int statusCodePass = statusLinePass.getStatusCode();
    //				if (statusCodePass == 200) {
    //					HttpEntity entityPass = responsePass.getEntity();
    //					InputStream contentPass = entityPass.getContent();
    //					BufferedReader readerPass = new BufferedReader(
    //							new InputStreamReader(contentPass,"ISO-8859-1"));
    //					String linePass;
    //					while ((linePass = readerPass.readLine()) != null) {
    //						builderPass.append(linePass);
    //					}
    //					System.out.println("Password Response::"+builderPass.toString());
    //				/////////////////////
    //				}
    //				else {
    //					System.out.println("Failed password api");
    //				}
    //				
    //			} else {
    //				System.out.println("Failed customer details");
    //			}
    //		} catch (ClientProtocolException e) {
    //			e.printStackTrace();
    //		} catch (IOException e) {
    //			
    //			e.printStackTrace();
    //			return;
    //		}
    //		String readFeed = builder.toString();
    //		
    //		
    //		
    //		try {
    //
    //			
    //	} catch (Exception e) {
    //		e.printStackTrace();
    //	}
    //	
    //	}


    //	private void updateResultsInUi() { 
    //		pd.dismiss();
    //	}




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //	class RequistForRegistartion extends
    //			AsyncTask<String, String, HttpResponseModel> {
    //
    //		@Override
    //		protected HttpResponseModel doInBackground(String... params) {
    //			if (customerDetailsVo != null) {
    //				HashMap<String, String> bodyMap = new HashMap<String, String>();
    //				bodyMap.put("Request", getRegistrationPera());
    //				try {
    //					HttpResponseModel data = new HttpProxyConnection(
    //							RegisterActivity.this).httpGET(
    //							Constants.REGISTRATION_URL, null, bodyMap);
    //					Log.d("RegisterActivity ::", data.getData());
    //					return data;
    //
    //				} catch (Exception e) {
    //					Log.d("RegisterActivity ::", e.toString());
    //				}
    //
    //			}
    //			return null;
    //		}
    //
    //		@Override
    //		protected void onPreExecute() {
    //			super.onPreExecute();
    //		}
    //
    //		@Override
    //		protected void onPostExecute(HttpResponseModel result) {
    //
    //		}
    //
    //	}

    //	public String getRegistrationPera() {
    //		String body = "?CustomerPassword=" + customerDetailsVo.getPassword()
    //				+ "&";
    //		body += "ConfirmPassword=" + customerDetailsVo.getPassword();
    //		return body;
    //	}
}
