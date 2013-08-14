package com.deepslice.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepslice.activity.DealsProductListActivity;
import com.deepslice.activity.R;
import com.deepslice.cache.ImageLoader;
import com.deepslice.model.Coupon;
import com.deepslice.model.DealOrder;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;

public class DealGroupListAdapter extends ArrayAdapter<DealOrder> {
    
    private Context mContext;
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;
    private Coupon selectedCoupon;

    public DealGroupListAdapter(Context context, List<DealOrder> mDealOrders, Coupon selectedCoupon) {
        super(context, R.layout.row_deal_group, mDealOrders);
        this.mContext = context;
        imageLoader = new ImageLoader((Activity) mContext);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.selectedCoupon = selectedCoupon;
    }
    
    
    private static class ViewHolder {
        ImageView productImage;
        TextView productName;
        Button bCustomize;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_deal_group, null);

            holder = new ViewHolder();
            holder.productImage = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.productName = (TextView) convertView.findViewById(R.id.textView1);  
            holder.bCustomize = (Button) convertView.findViewById(R.id.b_customize);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        DealOrder item = getItem(position);

        holder.productName.setText(item.getDisplayName());
        
        String imgPath=Constants.IMAGES_LOCATION_PRODUCTS;
        if(AppProperties.isNull(item.getImage())){
            imgPath = imgPath + "noimage.png";
        }
        else{
            imgPath = imgPath + item.getImage();
        }
        imageLoader.DisplayImage(imgPath, holder.productImage);
        
        holder.bCustomize.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                DealOrder item = getItem(position);
                
                Intent i=new Intent(mContext, DealsProductListActivity.class);
                
                Bundle bundle=new Bundle();
                bundle.putString("coupon_group_id",item.getCouponGroupID());
                bundle.putString("qty",item.getQuantity());
                bundle.putSerializable("selected_coupon", selectedCoupon);
                i.putExtras(bundle);
                
                mContext.startActivity(i);
                
            }
        });
        
        
        return convertView;
    }

}
