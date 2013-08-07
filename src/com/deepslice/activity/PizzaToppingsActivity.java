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
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.AllProducts;
import com.deepslice.model.DealOrder;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.model.ToppingsHashmap;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class PizzaToppingsActivity extends Activity{

    TextView favCountTxt;
    int currentCount=1;

    AllProducts selectedBean;
    DealOrder dealOrderVo;
    ListView listview;
    MyListAdapterSides myAdapter;

    ArrayList<ToppingsAndSauces> toppingsList;

    HashMap<String, ToppingsHashmap> toppingsSelected;
    String namesList="";
    String sizesList="",productId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toppings);
        Bundle b = this.getIntent().getExtras();

        if (b.getBoolean("isDeal",false)){
            dealOrderVo=(DealOrder)b.getSerializable("selectedProduct");
            productId=dealOrderVo.getProdID();
        }else {
            selectedBean=(AllProducts)b.getSerializable("selectedProduct");
            productId=selectedBean.getProdID();
        }

        toppingsSelected=AppProperties.selectedToppings;
        if(toppingsSelected==null)
            toppingsSelected=new HashMap<String, ToppingsHashmap>();


//        AppDao dao=null;
//        try {
//            dao=AppDao.getSingleton(getApplicationContext());
//            dao.openConnection();
//
//            toppingsList=dao.getPizzaToppings(productId);
//
//        } catch (Exception ex)
//        {
//            System.out.println(ex.getMessage());
//        }finally{
//            if(null!=dao)
//                dao.closeConnection();
//        }
        
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaToppingsActivity.this);
        dbInstance.open();
        toppingsList=dbInstance.getPizzaToppings(productId);
        dbInstance.close();

        listview = (ListView) findViewById(R.id.listView1);				
        myAdapter = new MyListAdapterSides(this,R.layout.line_item_toppings, toppingsList);
        listview.setAdapter(myAdapter);


        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position,
                    long id) {

                ToppingsAndSauces eBean = (ToppingsAndSauces) v.getTag();
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

    private class MyListAdapterSides extends ArrayAdapter<ToppingsAndSauces> {

        private ArrayList<ToppingsAndSauces> items;

        public MyListAdapterSides(Context context, int viewResourceId,
                ArrayList<ToppingsAndSauces> items) {
            super(context, viewResourceId, items);
            this.items = items;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.line_item_toppings, null);
            }
            final ToppingsAndSauces event = items.get(position);
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
                ToppingsHashmap tempObj = toppingsSelected.get(event.getToppingID());
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
    private ToppingsHashmap getHashBean(
            ToppingsAndSauces toppingsAndSaucesVo, String toppingSize) {

        ToppingsHashmap hMap=new ToppingsHashmap();

        hMap.setToppingCode(toppingsAndSaucesVo.getToppingCode());
        hMap.setToppingDesc(toppingsAndSaucesVo.getToppingDesc());
        hMap.setToppingID(toppingsAndSaucesVo.getToppingID());
        hMap.setToppingPrice(toppingsAndSaucesVo.getOwnPrice());
        hMap.setToppingSize(toppingSize);
        hMap.setIsFreeWithPizza(toppingsAndSaucesVo.getIsFreeWithPizza());

        return hMap;
    }
}
