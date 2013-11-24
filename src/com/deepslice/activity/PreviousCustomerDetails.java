package com.deepslice.activity;

import com.bugsense.trace.BugSenseHandler;

import android.app.Activity;
import android.os.Bundle;

public class PreviousCustomerDetails extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BugSenseHandler.initAndStartSession(this, "92b170cf");
		
		setContentView(R.layout.previous_customer_details);
		
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

}
