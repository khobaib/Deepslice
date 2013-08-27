package com.deepslice.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.deepslice.activity.R;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.NewToppingsOrder;
import com.deepslice.model.ToppingSizes;
import com.deepslice.model.ToppingsAndSauces;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.Utils;

public class ToppingsListAdapter extends ArrayAdapter<ToppingsAndSauces> {
    
    private Context mContext;
    private LayoutInflater mInflater;
    public static List<NewToppingsOrder> toppingsSelected;
    
    public ToppingsListAdapter(Context context, List<ToppingsAndSauces> itemList, List<NewToppingsOrder> toppingsSelected) {
        super(context, R.layout.line_item_toppings, itemList);
        this.mContext = context;
        this.toppingsSelected = toppingsSelected;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder {
        SeekBar ToppingSizeBar;
        TextView ToppingCode;
        TextView ToppingSize;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.line_item_toppings, null);

            holder = new ViewHolder();
            holder.ToppingSizeBar = (SeekBar) convertView.findViewById(R.id.my_seekbar);
            holder.ToppingCode = (TextView) convertView.findViewById(R.id.textView1);
            holder.ToppingSize = (TextView) convertView.findViewById(R.id.textView2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ToppingsAndSauces item = getItem(position);
        
        holder.ToppingSizeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(toppingsSelected.size() > Constants.MAX_TOPPING_COUNT_FOR_PIZZA){
                    Utils.openErrorDialog(mContext, "No more than 11 Toppings allowed!");
                    int toppingsPosition = isThisToppingsAlreadySelected(item.getToppingID());
                    toppingsSelected.remove(toppingsPosition);
                    holder.ToppingSize.setText("No");
                    holder.ToppingSizeBar.setProgress(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser==false) {
                    return;
                }

                int toppingsPosition = isThisToppingsAlreadySelected(item.getToppingID());
                if(toppingsPosition == -1){
                    NewToppingsOrder thisToppingsOrder = Utils.convertToppingAndSauceObjectToToppingsOrder(item);
//                    NewToppingsOrder thisToppingsOrder = new NewToppingsOrder(item.getToppingID(), item.getToppingCode(),
//                            Constants.SINGLE_SIZE_TOPPING_ID, item.getIsSauce().equalsIgnoreCase("true"), item.getOwnPrice(),
//                            item.getIsFreeWithPizza().equalsIgnoreCase("true"));
                    toppingsSelected.add(thisToppingsOrder);
                    toppingsPosition = toppingsSelected.size() - 1;
                }
                if(progress==0){
                    holder.ToppingSize.setText("No");                    
                    toppingsSelected.remove(toppingsPosition);
                }
                else if(progress == 1){
                    holder.ToppingSize.setText("Light");
//                    int toppingsPosition = isThisToppingsAlreadySelected(item.getToppingID());
                    updateToppingsSize(toppingsPosition, "12");

                }
                else if(progress==2){
                    holder.ToppingSize.setText("Single");
//                    int toppingsPosition = isThisToppingsAlreadySelected(item.getToppingID());
                    updateToppingsSize(toppingsPosition, "13");
                }
                else if(progress==3){
                    holder.ToppingSize.setText("Extra");
//                    int toppingsPosition = isThisToppingsAlreadySelected(item.getToppingID());
                    updateToppingsSize(toppingsPosition, "14");
                }
                else if(progress==4){
                    holder.ToppingSize.setText("Double");
//                    int toppingsPosition = isThisToppingsAlreadySelected(item.getToppingID());
                    updateToppingsSize(toppingsPosition, "15");
                }
                else if(progress==5){
                    holder.ToppingSize.setText("Triple");
//                    int toppingsPosition = isThisToppingsAlreadySelected(item.getToppingID());
                    updateToppingsSize(toppingsPosition, "16");
                }
            }
        });


        holder.ToppingCode.setText(item.getToppingCode());
        
        int toppingsPosition = isThisToppingsAlreadySelected(item.getToppingID());
        if(toppingsPosition == -1){                     // this toppings isn't selected already
            holder.ToppingSizeBar.setProgress(0);
            holder.ToppingSize.setText("No");
        }
        else{
            NewToppingsOrder thisToppingsOrder = toppingsSelected.get(toppingsPosition);
            String toppingSizeId = thisToppingsOrder.getToppingSizeId();
            
            DeepsliceDatabase dbInstance = new DeepsliceDatabase(mContext);
            dbInstance.open();
            ToppingSizes thisToppingsSize = dbInstance.retrieveToppingSizeById(toppingSizeId);
            dbInstance.close();
            
            String toppingsSizeCode = thisToppingsSize.getToppingSizeCode();
            holder.ToppingSize.setText(toppingsSizeCode);
            
            if(toppingsSizeCode.equals("No"))
                holder.ToppingSizeBar.setProgress(0);
            else if(toppingsSizeCode.equals("Light"))
                holder.ToppingSizeBar.setProgress(1);
            else if(toppingsSizeCode.equals("Single"))
                holder.ToppingSizeBar.setProgress(2);
            else if(toppingsSizeCode.equals("Extra"))
                holder.ToppingSizeBar.setProgress(3);
            else if(toppingsSizeCode.equals("Double"))
                holder.ToppingSizeBar.setProgress(4);
            else if(toppingsSizeCode.equals("Triple"))
                holder.ToppingSizeBar.setProgress(5);
        }       

        return convertView;
    }
    
    
    // if this toppings already in the toppingsSelected list, then returns the position
    // else return -1
    private int isThisToppingsAlreadySelected(String toppingsId){
        int selectedToppingsSize = toppingsSelected.size();
        for(int toppingsIndex = 0; toppingsIndex < selectedToppingsSize; toppingsIndex++){
            if(toppingsSelected.get(toppingsIndex).getToppingsId().equals(toppingsId))
                return toppingsIndex;
        }
        return -1;
    }
    
    private void updateToppingsSize(int listPosition, String toppingSizeId){
        toppingsSelected.get(listPosition).setToppingSizeId(toppingSizeId);
    }
    
    
    public static List<NewToppingsOrder> getSelectedToppingsList(){
        return toppingsSelected;
    }
}
