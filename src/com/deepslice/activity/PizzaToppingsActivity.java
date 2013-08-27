package com.deepslice.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.deepslice.adapter.ToppingsListAdapter;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.DealOrder;
import com.deepslice.model.NewToppingsOrder;
import com.deepslice.model.Product;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Utils;

public class PizzaToppingsActivity extends Activity{

//    TextView favCountTxt;
//    int currentCount = 1;

    Product selectedProduct;
    DealOrder dealOrder;
    ListView listview;
    ToppingsListAdapter toppingsListAdapter;

    List<ToppingsAndSauces> toppingsList;

    public List<NewToppingsOrder> toppingsSelected;
    //    List<HashMap<String, String>> toppingsSelected;
    //    HashMap<String, ToppingsHashmap> toppingsSelected;
    //    String namesList="";
    //    String sizesList="";
    String productId="";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toppings);
        Bundle b = this.getIntent().getExtras();

        if (b.getBoolean("isDeal",false)){
            dealOrder = (DealOrder)b.getSerializable("selectedProduct");             
            productId = dealOrder.getProdID();
        }else {
            selectedProduct = (Product)b.getSerializable("selectedProduct");
            productId = selectedProduct.getProdID();
        }

        toppingsSelected = AppProperties.selectedToppings;
        if(toppingsSelected==null)
            toppingsSelected = new ArrayList<NewToppingsOrder>();
        //            toppingsSelected=new HashMap<String, ToppingsHashmap>();


        DeepsliceDatabase dbInstance = new DeepsliceDatabase(PizzaToppingsActivity.this);
        dbInstance.open();
        toppingsList = dbInstance.retrievePizzaToppings(productId);
        dbInstance.close();

        listview = (ListView) findViewById(R.id.listView1);				
        toppingsListAdapter = new ToppingsListAdapter(PizzaToppingsActivity.this, toppingsList, toppingsSelected);
        listview.setAdapter(toppingsListAdapter);


        //        listview.setOnItemClickListener(new OnItemClickListener() {
        //            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        //
        //                ToppingsAndSauces eBean = (ToppingsAndSauces) v.getTag();
        //                if (eBean != null) {
        //
        //                }
        //            }
        //        });

        Button startOrderingButton=(Button)findViewById(R.id.startOrderingButton);
        startOrderingButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                toppingsSelected = ToppingsListAdapter.getSelectedToppingsList();
                if(toppingsSelected.size()>11){
                    Utils.openErrorDialog(PizzaToppingsActivity.this, "No more than 11 Toppings allowed!\nCurrently "+toppingsSelected.size()+" selected.");
                    return;
                }

                AppProperties.selectedToppings = toppingsSelected;
                Intent resultData = new Intent();
                //                resultData.putExtra("namesList", namesList);
                //                resultData.putExtra("sizesList", sizesList);
                setResult(Activity.RESULT_OK, resultData);
                finish();
            }
        });
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////

    //    private class MyListAdapterSides extends ArrayAdapter<ToppingsAndSauces> {
    //
    //        private ArrayList<ToppingsAndSauces> items;
    //
    //        public MyListAdapterSides(Context context, int viewResourceId,
    //                ArrayList<ToppingsAndSauces> items) {
    //            super(context, viewResourceId, items);
    //            this.items = items;
    //
    //        }
    //
    //        @Override
    //        public View getView(int position, View convertView, ViewGroup parent) {
    //            if (convertView == null) {
    //                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //                convertView = mInflater.inflate(R.layout.line_item_toppings, null);
    //            }
    //            final ToppingsAndSauces event = items.get(position);
    //            if (event != null) {
    //                final SeekBar mySeekBar = (SeekBar) convertView.findViewById(R.id.my_seekbar);
    //                TextView title = (TextView) convertView.findViewById(R.id.textView1);
    //                final TextView subtext = (TextView) convertView.findViewById(R.id.textView2);
    //
    //
    //                mySeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
    //
    //                    @Override
    //                    public void onStopTrackingTouch(SeekBar seekBar) {
    //                        if(toppingsSelected.size()>11)
    //                        {
    //                            Utils.openErrorDialog(PizzaToppingsActivity.this, "No more than 11 Toppings allowed!");
    //                            toppingsSelected.remove(event.getToppingID());
    //                            subtext.setText("No");
    //                            mySeekBar.setProgress(0);
    //                        }
    //                    }
    //
    //                    @Override
    //                    public void onStartTrackingTouch(SeekBar seekBar) {
    //
    //                    }
    //
    //                    @Override
    //                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    //                        if(fromUser==false) {
    //                            return;
    //                        }
    //
    //
    //
    //                        if(progress==0){
    //                            subtext.setText("No");
    //                            toppingsSelected.remove(event.getToppingID());
    //                        }
    //                        else if(progress==1)
    //                        {
    //                            subtext.setText("Light");
    //                            toppingsSelected.put(event.getToppingID(), getHashBean(event,"Light"));
    //
    //                        }
    //                        else if(progress==2)
    //                        {
    //                            subtext.setText("Single");
    //                            toppingsSelected.put(event.getToppingID(), getHashBean(event,"Single"));
    //                        }
    //                        else if(progress==3)
    //                        {
    //                            subtext.setText("Extra");
    //                            toppingsSelected.put(event.getToppingID(), getHashBean(event,"Extra"));
    //                        }
    //                        else if(progress==4)
    //                        {
    //                            subtext.setText("Double");
    //                            toppingsSelected.put(event.getToppingID(), getHashBean(event,"Double"));
    //                        }
    //                        else if(progress==5)
    //                        {
    //                            subtext.setText("Triple");
    //                            toppingsSelected.put(event.getToppingID(), getHashBean(event,"Triple"));
    //                        }
    //                    }
    //                });
    //
    //
    //                title.setText(event.getToppingCode());
    //                ToppingsHashmap tempObj = toppingsSelected.get(event.getToppingID());
    //                if(tempObj != null)
    //                {
    //                    subtext.setText(tempObj.getToppingSize());
    //                    if("No".equals(tempObj.getToppingSize()))
    //                        mySeekBar.setProgress(0);
    //                    else if("Light".equalsIgnoreCase(tempObj.getToppingSize()))
    //                        mySeekBar.setProgress(1);
    //                    else if("Single".equalsIgnoreCase(tempObj.getToppingSize()))
    //                        mySeekBar.setProgress(2);
    //                    else if("Extra".equalsIgnoreCase(tempObj.getToppingSize()))
    //                        mySeekBar.setProgress(3);
    //                    else if("Double".equalsIgnoreCase(tempObj.getToppingSize()))
    //                        mySeekBar.setProgress(4);
    //                    else if("Triple".equalsIgnoreCase(tempObj.getToppingSize()))
    //                        mySeekBar.setProgress(5);
    //                }else{
    //                    mySeekBar.setProgress(0);
    //                    subtext.setText("No");
    //                }
    //
    //                convertView.setTag(event);
    //            }
    //            return convertView;
    //        }
    //
    //    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { // Back key pressed
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
