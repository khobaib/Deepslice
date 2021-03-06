package com.deepslice.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.deepslice.activity.CustomerDetailsActivity;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;


public class CarerOverlay extends ItemizedOverlay<OverlayItem> {
	Context mContext;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	
	public CarerOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	public CarerOverlay(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
		}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	protected boolean onTap(int index) {
	  
	OverlayItem item = mOverlays.get(index);
	
	TextView tv=new TextView(mContext);
	tv.setId(112223);
	tv.setText(item.getSnippet());
	tv.setPadding(15, 15, 15, 15);
	tv.setTextSize(22);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
//	  dialog.setMessage(item.getSnippet());
	  dialog.setView(tv);
	  dialog.show();
	  
	  tv.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext,CustomerDetailsActivity.class);
			Bundle bundle = new Bundle();
			
			intent.putExtras(bundle);
			mContext.startActivity(intent);
		}
	});
	  
	  return true;
	}
	
	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}
	
	@Override
	public int size() {
		return mOverlays.size();
	}

}
