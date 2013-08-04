package com.deepslice.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepslice.activity.R;
import com.deepslice.adapter.CrustAdapter.ViewHolder;
import com.deepslice.cache.ImageLoader;
import com.deepslice.model.CreateOwnPizzaData;

public class PizzaTypeMenuAdapter extends ArrayAdapter<CreateOwnPizzaData> {
    
    private Context mContext;
    private LayoutInflater mInflater;

    public PizzaTypeMenuAdapter(Context context, List<CreateOwnPizzaData> mPizza) {
        super(context, R.layout.line_item_yello, mPizza);
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    
    public static class ViewHolder {
        TextView pizzaName;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.line_item_yello, null);

            holder = new ViewHolder();
            holder.pizzaName = (TextView) convertView.findViewById(R.id.itemName);  

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        CreateOwnPizzaData item = getItem(position);

        holder.pizzaName.setText(item.getSubCat1code() + " Pizza");
        
        return convertView;
    }

}
