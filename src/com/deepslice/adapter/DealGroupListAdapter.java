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

import com.deepslice.activity.DealsGroupListActivity;
import com.deepslice.activity.DealsProductListActivity;
import com.deepslice.activity.R;
import com.deepslice.cache.ImageLoader;
import com.deepslice.model.Coupon;
import com.deepslice.model.DealOrder;
import com.deepslice.model.NewDealsOrderDetails;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;

public class DealGroupListAdapter extends ArrayAdapter<NewDealsOrderDetails> {
    
    private Context mContext;
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;
    private Coupon selectedCoupon;
    private long dealOrderId;

    public DealGroupListAdapter(Context context, List<NewDealsOrderDetails> mDealOrderDetails, Coupon selectedCoupon, long dealOrderId) {
        super(context, R.layout.row_deal_group, mDealOrderDetails);
        this.mContext = context;
        imageLoader = new ImageLoader((Activity) mContext);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.selectedCoupon = selectedCoupon;
        this.dealOrderId = dealOrderId;
    }
    
    
    private static class ViewHolder {
        ImageView productImage;
        TextView productName;
        Button bCustomize;
        ImageView Tick;
//        TextView Qty;
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
            holder.Tick = (ImageView) convertView.findViewById(R.id.iv_tick_customized);
//            holder.Qty = (TextView) convertView.findViewById(R.id.tv_qty);  

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        NewDealsOrderDetails item = getItem(position);

        holder.productName.setText(item.getDisplayName());
//        holder.Qty.setText(item.getQty());
        
        String imgPath=Constants.IMAGES_LOCATION_PRODUCTS;
        if(AppProperties.isNull(item.getThumbnail())){
            imgPath = imgPath + "noimage.png";
        }
        else{
            imgPath = imgPath + item.getThumbnail();
        }
        imageLoader.DisplayImage(imgPath, holder.productImage);
        
        if(DealsGroupListActivity.isDealItemCustomized.get(position).equals(true))
            holder.Tick.setVisibility(View.VISIBLE);
        else
            holder.Tick.setVisibility(View.GONE);
        
        holder.bCustomize.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                NewDealsOrderDetails item = getItem(position);
                
                Intent i=new Intent(mContext, DealsProductListActivity.class);
                
                Bundle bundle=new Bundle();
                bundle.putInt("item_position", position);
                bundle.putString("coupon_group_id", item.getCouponGroupId());
                bundle.putString("qty", item.getQty());
                bundle.putLong("dealOrderId", dealOrderId);
                bundle.putInt("sequence", item.getSequence());
                bundle.putSerializable("selected_coupon", selectedCoupon);
                i.putExtras(bundle);
                
                mContext.startActivity(i);
                
            }
        });
        
        
        return convertView;
    }

}
