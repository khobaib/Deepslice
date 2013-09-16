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
import com.deepslice.cache.ImageLoader;
import com.deepslice.model.Coupon;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;

public class DealListAdapter extends ArrayAdapter<Coupon> {

    private Context mContext;
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;

    public DealListAdapter(Context context, List<Coupon> items) {
        super(context, R.layout.row_deal, items);
        this.mContext = context;
        imageLoader = new ImageLoader((Activity) mContext);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    private static class ViewHolder {
        ImageView dealImage;
        TextView dealName;
        TextView dealPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_deal, null);

            holder = new ViewHolder();
            holder.dealImage = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.dealName = (TextView) convertView.findViewById(R.id.textView1);
            holder.dealPrice = (TextView) convertView.findViewById(R.id.textView2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Coupon item = getItem(position);
        holder.dealName.setText(item.getCouponDesc());
        holder.dealPrice.setText(item.getAmount());
        
        String imgPath = Constants.IMAGES_LOCATION_DEALS;
        if(AppProperties.isNull(item.getPic())){
            imgPath = imgPath + "noimage.png";
        }
        else{
            imgPath = imgPath + item.getPic();
        }
        imageLoader.DisplayImage(imgPath, holder.dealImage);
        System.out.println(",,,,,,,,,,,,,,,,,,"+imgPath);

        return convertView;
    }

}
