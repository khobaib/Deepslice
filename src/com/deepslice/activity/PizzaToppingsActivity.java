package com.deepslice.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.deepslice.database.AppDao;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Utils;
import com.deepslice.vo.AllProductsVo;
import com.deepslice.vo.DealOrderVo;
import com.deepslice.vo.ToppingsAndSaucesVo;
import com.deepslice.vo.ToppingsHashmapVo;

import java.util.ArrayList;
import java.util.HashMap;

public class PizzaToppingsActivity extends Activity{

	TextView favCountTxt;
	int currentCount=1;

	AllProductsVo selectedBean;
    DealOrderVo dealOrderVo;
	ListView listview;
	MyListAdapterSides myAdapter;

	ArrayList<ToppingsAndSaucesVo> toppingsList;

	HashMap<String, ToppingsHashmapVo> toppingsSelected;
	String namesList="";
	String sizesList="",productId="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.toppings);
		Bundle b = this.getIntent().getExtras();

        if (b.getBoolean("isDeal",false)){
         dealOrderVo=(DealOrderVo)b.getSerializable("selectedProduct");
            productId=dealOrderVo.getProdID();
        }else {
		    selectedBean=(AllProductsVo)b.getSerializable("selectedProduct");
            productId=selectedBean.getProdID();
        }

		toppingsSelected=AppProperties.selectedToppings;
		if(toppingsSelected==null)
			toppingsSelected=new HashMap<String, ToppingsHashmapVo>();
		
		
		  AppDao dao=null;
			try {
				dao=AppDao.getSingleton(getApplicationContext());
				dao.openConnection();
				
				toppingsList=dao.getPizzaToppings(productId);
				
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}finally{
				if(null!=dao)
					dao.closeConnection();
			}
	
			listview = (ListView) findViewById(R.id.listView1);				
			myAdapter = new MyListAdapterSides(this,R.layout.line_item_toppings, toppingsList);
			listview.setAdapter(myAdapter);
		
			
			listview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position,
						long id) {
					
					ToppingsAndSaucesVo eBean = (ToppingsAndSaucesVo) v.getTag();
					if (eBean != null) {

					}
				}
			});
		
			Button startOrderingButton=(Button)findViewById(R.id.startOrderingButton);
			startOrderingButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(toppingsSelected.size()>11)
					{
						Utils.openErrorDialog(PizzaToppingsActivity.this, "No more than 11 Toppings allowed!\nCurrently "+toppingsSelected.size()+" selected.");
						return;
					}

					
					AppProperties.selectedToppings=toppingsSelected;
					Intent resultData = new Intent();
					resultData.putExtra("namesList", namesList);
					resultData.putExtra("sizesList", sizesList);
					setResult(Activity.RESULT_OK, resultData);
					finish();

				}
			});
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////

	private class MyListAdapterSides extends ArrayAdapter<ToppingsAndSaucesVo> {

		private ArrayList<ToppingsAndSaucesVo> items;

		public MyListAdapterSides(Context context, int viewResourceId,
				ArrayList<ToppingsAndSaucesVo> items) {
			super(context, viewResourceId, items);
			this.items = items;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.line_item_toppings, null);
			}
			final ToppingsAndSaucesVo event = items.get(position);
			if (event != null) {
				final SeekBar mySeekBar = (SeekBar) convertView.findViewById(R.id.my_seekbar);
				TextView title = (TextView) convertView.findViewById(R.id.textView1);
				final TextView subtext = (TextView) convertView.findViewById(R.id.textView2);
				
				
				mySeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						if(toppingsSelected.size()>11)
						{
							Utils.openErrorDialog(PizzaToppingsActivity.this, "No more than 11 Toppings allowed!");
							toppingsSelected.remove(event.getToppingID());
							subtext.setText("No");
							mySeekBar.setProgress(0);
						}
					}
					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						
					}
					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						if(fromUser==false)
						{
							
							return;
						}
							
						
							
						if(progress==0)
							{
							
							subtext.setText("No");
							toppingsSelected.remove(event.getToppingID());
							}
						else if(progress==1)
							{
							subtext.setText("Light");
							toppingsSelected.put(event.getToppingID(), getHashBean(event,"Light"));
							
							}
						else if(progress==2)
							{
							subtext.setText("Single");
							toppingsSelected.put(event.getToppingID(), getHashBean(event,"Single"));
							}
						else if(progress==3)
							{
							subtext.setText("Extra");
							toppingsSelected.put(event.getToppingID(), getHashBean(event,"Extra"));
							}
						else if(progress==4)
							{
							subtext.setText("Double");
							toppingsSelected.put(event.getToppingID(), getHashBean(event,"Double"));
							}
						else if(progress==5)
							{
							subtext.setText("Triple");
							toppingsSelected.put(event.getToppingID(), getHashBean(event,"Triple"));
							}
					}
				});
				
				
				title.setText(event.getToppingCode());
				ToppingsHashmapVo tempObj = toppingsSelected.get(event.getToppingID());
				if(tempObj != null)
				{
					subtext.setText(tempObj.getToppingSize());
					if("No".equals(tempObj.getToppingSize()))
						mySeekBar.setProgress(0);
					else if("Light".equalsIgnoreCase(tempObj.getToppingSize()))
						mySeekBar.setProgress(1);
					else if("Single".equalsIgnoreCase(tempObj.getToppingSize()))
						mySeekBar.setProgress(2);
					else if("Extra".equalsIgnoreCase(tempObj.getToppingSize()))
						mySeekBar.setProgress(3);
					else if("Double".equalsIgnoreCase(tempObj.getToppingSize()))
						mySeekBar.setProgress(4);
					else if("Triple".equalsIgnoreCase(tempObj.getToppingSize()))
						mySeekBar.setProgress(5);
				}else{
					mySeekBar.setProgress(0);
					subtext.setText("No");
				}
				
				convertView.setTag(event);
			}
			return convertView;
		}

	}
	// /////////////////////// END LIST ADAPTER
	private ToppingsHashmapVo getHashBean(
			ToppingsAndSaucesVo toppingsAndSaucesVo, String toppingSize) {

		ToppingsHashmapVo hMap=new ToppingsHashmapVo();
		
		hMap.setToppingCode(toppingsAndSaucesVo.getToppingCode());
		hMap.setToppingDesc(toppingsAndSaucesVo.getToppingDesc());
		hMap.setToppingID(toppingsAndSaucesVo.getToppingID());
		hMap.setToppingPrice(toppingsAndSaucesVo.getOwnPrice());
		hMap.setToppingSize(toppingSize);
		hMap.setIsFreeWithPizza(toppingsAndSaucesVo.getIsFreeWithPizza());
		
		return hMap;
	}
}
