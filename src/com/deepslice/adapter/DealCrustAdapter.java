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
import com.deepslice.model.CouponDetails.CrustProducts;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;

public class DealCrustAdapter extends ArrayAdapter<CrustProducts> {
    
    private Context mContext;
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;
    private String currentProductId;

    public DealCrustAdapter(Context context, List<CrustProducts> mCrusts, String currentProductId) {
        super(context, R.layout.line_item_crust, mCrusts);
        this.mContext = context;
        imageLoader = new ImageLoader((Activity) mContext);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentProductId = currentProductId;
    }
    
    
    private static class ViewHolder {
        ImageView crustImage;
        TextView crustName;
        ImageView Tick;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.line_item_crust, null);

            holder = new ViewHolder();
            holder.crustImage = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.crustName = (TextView) convertView.findViewById(R.id.textView1);  
            holder.Tick = (ImageView) convertView.findViewById(R.id.imageView2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        CrustProducts item = getItem(position);

        holder.crustName.setText(item.getSubCat2Code());
        
        String imgPath = Constants.IMAGES_LOCATION_CRUSTS;
        if(AppProperties.isNull(item.getThumbnail())){
            imgPath = imgPath + "noimage.png";
        }
        else{
            imgPath = imgPath + item.getThumbnail();
        }
        imageLoader.DisplayImage(item.getThumbnail(), holder.crustImage);
        
        if(item.getProdID().equals(currentProductId) )
            holder.Tick.setVisibility(View.VISIBLE);
        else
            holder.Tick.setVisibility(View.INVISIBLE);
        
        return convertView;
    }
}
