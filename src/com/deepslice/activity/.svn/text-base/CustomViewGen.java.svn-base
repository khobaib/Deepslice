package com.deepslice.activity;

import com.deepslice.utilities.Utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomViewGen extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_menu);
		
		LinearLayout lout=(LinearLayout)findViewById(R.id.outerLayout);
		
	{	
		TextView tv=new TextView(this);
		tv.setText("Bread");
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(27);
		
	    LinearLayout.LayoutParams lpYello = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(lpYello);
		tv.setPadding(15, 5, 5, 5);

		
		tv.setBackgroundResource(R.drawable.yellow_butt_big);
		lout.addView(tv);
	}
	
	{
		String itemName="Test Item";
		String itemPrice="$3.99";
		String itemCalaroies="100kj";
		String prodId="14";
		final int prodIdInt=Integer.parseInt(prodId);
			
		 RelativeLayout ll = new RelativeLayout(this);
		 ll.setId(prodIdInt+99);
		 RelativeLayout.LayoutParams rlay = new RelativeLayout.LayoutParams(
				 RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		 rlay.setMargins(5, 10, 5, 5);
		 lout.addView(ll);
		 ll.setBackgroundResource(R.drawable.footer);
			
		 ImageView icon=new ImageView(this);
		 icon.setId(prodIdInt+1);
		 icon.setImageResource(R.drawable.menu_pista);
		 RelativeLayout.LayoutParams iocnLO = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		 iocnLO.width=100;
		 iocnLO.height=60;
		 iocnLO.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		 iocnLO.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		 ll.addView(icon, iocnLO);
	
		 TextView title=new TextView(this);
		 title.setId(prodIdInt+2);
		 title.setText(itemName);
		 title.setTextColor(Color.WHITE);
		 title.setTextSize(18);
		 title.setLines(1);
		 RelativeLayout.LayoutParams titleLO = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		 titleLO.setMargins(10, 5, 5, 5);
		 titleLO.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		 titleLO.addRule(RelativeLayout.RIGHT_OF,icon.getId());
		 ll.addView(title, titleLO);
	
		 
		 TextView price=new TextView(this);
		 price.setId(prodIdInt+3);
		 price.setText(itemPrice);
		 price.setBackgroundResource(R.drawable.list_arrow);
		 price.setTextColor(Color.WHITE);
		 price.setTextSize(14);
		 price.setLines(1);
		 RelativeLayout.LayoutParams priceLO = new RelativeLayout.LayoutParams(
				 RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		 priceLO.setMargins(5, 5, 5, 5);
		 priceLO.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		 priceLO.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	//	 priceLO.addRule(RelativeLayout.RIGHT_OF,icon.getId());
		 ll.addView(price, priceLO);
	
		 
		 TextView calories=new TextView(this);
		 calories.setId(prodIdInt+4);
		 calories.setText(itemCalaroies);
		 calories.setTextColor(Color.YELLOW);
		 calories.setTextSize(18);
		 calories.setLines(1);
		 RelativeLayout.LayoutParams calLO = new RelativeLayout.LayoutParams(
				 RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		 calLO.setMargins(5, 5, 5, 5);
		 calLO.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		 calLO.addRule(RelativeLayout.LEFT_OF,price.getId());
		 ll.addView(calories, calLO);
		 
		 ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Utils.openErrorDialog(CustomViewGen.this, String.valueOf(prodIdInt));
				
			}
		});
	}
	 
	 
	 
	 
	}

}
