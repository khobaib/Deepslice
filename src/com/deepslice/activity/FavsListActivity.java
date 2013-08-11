package com.deepslice.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deepslice.cache.ImageLoader;
import com.deepslice.database.AppDao;
import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Products;
import com.deepslice.model.Favourites;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;

public class FavsListActivity extends Activity{

    ArrayList<Favourites> allProductsList;

    ListView listview;
    MyListAdapterFav myAdapter;

    ProgressDialog pd;

    public ImageLoader imageLoader;
    Favourites selectedBean;	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_list);
        imageLoader=new ImageLoader(this);	

        TextView title = (TextView) findViewById(R.id.headerTextView);
        title.setText("Favourites");

        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setBackgroundResource(R.drawable.m_nav3_sel);

        DeepsliceDatabase dbInstance = new DeepsliceDatabase(FavsListActivity.this);
        dbInstance.open();
        allProductsList=dbInstance.getFavsList();
        dbInstance.close();

        listview = (ListView) findViewById(R.id.listView1);				
        myAdapter = new MyListAdapterFav(this,R.layout.line_item_product, allProductsList);
        listview.setAdapter(myAdapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position,
                    long id) {
                Favourites eBean = (Favourites) v.getTag();
                if (eBean != null) {
                    selectedBean = eBean;
                    Intent i;
                    Bundle bundle=new Bundle();

                    if("Pizza".equalsIgnoreCase(eBean.getProdCatName()))
                    {
                        i=new Intent(FavsListActivity.this, PizzaDetailsActivity.class);
                        bundle.putSerializable("selectedProduct",getProdBean(eBean));
                    }
                    else
                    {
                        i=new Intent(FavsListActivity.this, FavAddActivity.class);
                        bundle.putSerializable("prodBean",getProdBean(eBean));
                    }


                    bundle.putString("itemName",eBean.getDisplayName());
                    bundle.putString("catType",eBean.getProdCatName());
                    i.putExtras(bundle);
                    startActivityForResult(i, 112233);
                }
            }
        });

        listview.setEmptyView(findViewById(R.id.youterLayout));

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(FavsListActivity.this,MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(FavsListActivity.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });
//        Button buttonCancel=(Button)findViewById(R.id.buttonCancel);
//        buttonCancel.setVisibility(View.INVISIBLE);
        RelativeLayout rlGetDeal=(RelativeLayout)findViewById(R.id.getDeal);
        rlGetDeal.setVisibility(View.INVISIBLE);
        //        Button buttonGetADeal=(Button)findViewById(R.id.buttonGetADeal);
        //        buttonGetADeal.setVisibility(View.INVISIBLE);
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////

    private class MyListAdapterFav extends ArrayAdapter<Favourites> {

        private ArrayList<Favourites> items;

        public MyListAdapterFav(Context context, int viewResourceId,
                ArrayList<Favourites> items) {
            super(context, viewResourceId, items);
            this.items = items;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.line_item_product, null);
            }
            Favourites event = items.get(position);
            if (event != null) {

                TextView title = (TextView) convertView.findViewById(R.id.textView1);
                TextView price = (TextView) convertView.findViewById(R.id.textView2);
                TextView calories = (TextView) convertView.findViewById(R.id.textView3);

                title.setText(event.getCustomName());
                price.setText("$"+event.getPrice());
                calories.setText(event.getCaloriesQty()+"kj");


                ImageView icon = (ImageView) convertView.findViewById(R.id.imageView1);
                String imgPath=Constants.IMAGES_LOCATION;
                if(AppProperties.isNull(event.getThumbnail())){
                    imgPath=imgPath+"noimage.png";
                }
                else{
                    imgPath=imgPath+event.getThumbnail();
                }
                imageLoader.DisplayImage(imgPath, icon);

                convertView.setTag(event);
            }
            return convertView;
        }

    }
    ////////////////////////// END LIST ADAPTER
    //===========================================================================================
    //===========================================================================================
    //===========================================================================================

    private Products getProdBean(Favourites favBean) {

        Products prod = new Products();
        prod.setProdCatID(favBean.getProdCatID());
        prod.setSubCatID1(favBean.getSubCatID1());
        prod.setSubCatID2(favBean.getSubCatID2());
        prod.setProdID(favBean.getProdID());
        prod.setProdCode(favBean.getProdCode());
        prod.setDisplayName(favBean.getDisplayName());
        prod.setProdAbbr(favBean.getProdAbbr());
        prod.setProdDesc(favBean.getProdDesc());
        prod.setIsAddDeliveryAmount(favBean.getIsAddDeliveryAmount());
        prod.setDisplaySequence(favBean.getDisplaySequence());
        prod.setCaloriesQty(favBean.getCaloriesQty());
        prod.setPrice(favBean.getPrice());
        prod.setThumbnail(favBean.getThumbnail());
        prod.setFullImage(favBean.getFullImage());

        return prod;
    }

    @Override
    protected void onResume() {
        // //////////////////////////////////////////////////////////////////////////////
        DeepsliceDatabase dbInstance = new DeepsliceDatabase(FavsListActivity.this);
        dbInstance.open();
        ArrayList<String> orderInfo = dbInstance.getOrderInfo();

        TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
        if (null != orderInfo && orderInfo.size() == 2) {
            itemsPrice.setText(orderInfo.get(0)+" Items "+"\n$" + orderInfo.get(1));
            itemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            itemsPrice.setVisibility(View.INVISIBLE);

        }

        TextView favCount = (TextView) findViewById(R.id.favCount);
        String fvs=dbInstance.getFavCount();
        if (null != fvs && !fvs.equals("0")) {
            favCount.setText(fvs);
            favCount.setVisibility(View.VISIBLE);
        }
        else{
            favCount.setVisibility(View.INVISIBLE);
        }
        dbInstance.close();

        super.onResume();
    }
}
