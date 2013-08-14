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
import com.deepslice.model.Product;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;

public class DealProductListAdapter extends ArrayAdapter<Product> {

    private Context mContext;
    private ImageLoader imageLoader;
    private LayoutInflater mInflater;

    public DealProductListAdapter(Context context, List<Product> mProducts) {
        super(context, R.layout.row_deal_product, mProducts);
        this.mContext = context;
        imageLoader = new ImageLoader((Activity) mContext);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    
    private static class ViewHolder {
        TextView productTitle;
        TextView calorie;
        ImageView productImage;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_deal_product, null);

            holder = new ViewHolder();
            holder.productTitle = (TextView) convertView.findViewById(R.id.textView1);
            holder.calorie = (TextView) convertView.findViewById(R.id.textView2);  
            holder.productImage = (ImageView) convertView.findViewById(R.id.imageView1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        Product item = getItem(position);

        holder.productTitle.setText(item.getDisplayName());
        holder.calorie.setText(item.getCaloriesQty()+" kj");
        
        String imgPath=Constants.IMAGES_LOCATION_PRODUCTS;
        if(AppProperties.isNull(item.getThumbnail())){
            imgPath=imgPath+"noimage.png";
        }
        else{
            imgPath = imgPath + item.getThumbnail();
        }
        imageLoader.DisplayImage(imgPath, holder.productImage);        
        
        return convertView;
    }

}
