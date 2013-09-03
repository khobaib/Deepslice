package com.deepslice.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.DealOrder;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.AppSharedPreference;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.impl.client.DefaultHttpClient;

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
	
	TextView totalPrice,descriptionText;
    TextView tvItemsPrice, tvFavCount;
    
	String myIp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_cash);
		
		totalPrice=(TextView)findViewById(R.id.totalPrice);
		descriptionText=(TextView)findViewById(R.id.textView1);
        TextView txtUserName=(TextView)findViewById(R.id.textUserName);
        
        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);
		
/////////////////		
		
		Button addCoupon=(Button)findViewById(R.id.btnPayByCash);
		addCoupon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				/* i have changed here
				//////////////////////
				String orderType=AppSharedPreference.getData(PayByCashActivity.this, "orderType", null);
				Intent i;
				
				if("Delivery".equalsIgnoreCase(orderType))
					i=new Intent(PayByCashActivity.this,LocationFromHistoryActivity.class);
				else
					i=new Intent(PayByCashActivity.this,StoreFromHistoryActivity.class);
				
				startActivity(i);
				finish();
				////////////////////////////
				 */
				
				Intent intent = new Intent(PayByCashActivity.this,PickupDeliverActivity.class);
				String location=getIntent().getStringExtra("location");
				String store=getIntent().getStringExtra("store");
				String suburbId=getIntent().getStringExtra("suburbId");
				
				
				
				
				Bundle bundle=new Bundle();
				bundle.putString("location",location);
				bundle.putString("store",store);
				bundle.putString("suburbId",suburbId);
				intent.putExtras(bundle);

                if (!AppProperties.isLoggedIn){
                    AppSharedPreference.putData(PayByCashActivity.this, "customerName", "");
                    AppSharedPreference.putData(PayByCashActivity.this, "customerEmail","");
                    AppSharedPreference.putData(PayByCashActivity.this, "customerPhone", "");
                }
				
				startActivity(intent);
				finish();
				
				 
			}
		});
		
		
		////////////////////////////////////////////////////////////////
		myIp = getIPAddress(true);
	
		StringBuffer theMessageString=new StringBuffer("");
		theMessageString.append("By placing your order, you are confirming your order of ");
		theMessageString.append(totalPrice.getText().toString());
		theMessageString.append(".\n\n");
		theMessageString.append("Your IP address of ");
		theMessageString.append(myIp);
		theMessageString.append(" will be logged when your order is placed.\n\n");
		theMessageString.append("You may receive a phone call to confirm your order. If you are unavailable, then your order may be canceled.");
		
		descriptionText.setText(theMessageString.toString());
         if(AppSharedPreference.getData(PayByCashActivity.this, "customerName", "").equals("")){
             txtUserName.setText("Dear "+AppSharedPreference.getData(PayByCashActivity.this, "customerName", ""));
         }else {
            txtUserName.setText("Dear "+AppSharedPreference.getData(PayByCashActivity.this, "customerName", ""));
         }
	}
	
	
    @Override
    protected void onResume() {
        super.onResume();
        List<String> orderInfo = Utils.OrderInfo(PayByCashActivity.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);
        
        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }

        
        String favCount = Utils.FavCount(PayByCashActivity.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }
    }
    
    

	
	public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                        if (useIPv4) {
                            if (isIPv4) 
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

	// //////////////////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////getting coupon info
	// ///////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////////////////
	final Handler mHandler = new Handler();
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			updateResultsInUi();
		}
	};
	String serverResponse;
	ProgressDialog pd;
	protected void getCouponInfo() {
		pd = ProgressDialog.show(PayByCashActivity.this, "", "Please wait...",
				true, false);
		serverResponse="";
		Thread t = new Thread() {
			public void run() {

				try {
					// calling the auth service
					startLongRunningOperation();
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
				mHandler.post(mUpdateResults);
			}

		};
		t.start();
	}

	String delLocError = "";

	public void startLongRunningOperation() {

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet("http://api.externalip.net/ip/");
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
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			delLocError = e.getMessage();
			e.printStackTrace();
		}
	}

	private void updateResultsInUi() {
		pd.dismiss();

		try {

			if(!AppProperties.isNull(serverResponse))
			{
				myIp = serverResponse;
				
				StringBuffer theMessageString=new StringBuffer("");
				theMessageString.append("By placing your order, you are confirming your order of ");
				theMessageString.append(totalPrice.getText().toString());
				theMessageString.append(".\n\n");
				theMessageString.append("Your IP address of ");
				theMessageString.append(myIp);
				theMessageString.append(" will be logged when your order is placed.\n\n");
				theMessageString.append("You may receive a phone call to confirm your order. If you are unavailable, then your order may be canceled.");
				
				descriptionText.setText(theMessageString.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

}
