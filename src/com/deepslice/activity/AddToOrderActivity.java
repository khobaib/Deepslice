package com.deepslice.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deepslice.database.DeepsliceDatabase;
import com.deepslice.model.Favourite;
import com.deepslice.model.NewDealsOrderDetails;
import com.deepslice.model.NewProductOrder;
import com.deepslice.model.Order;
import com.deepslice.model.Product;
import com.deepslice.utilities.AppProperties;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.Utils;

public class AddToOrderActivity extends Activity {

    TextView favCountTxt;
    int currentCount = 1;
    Product selectedProduct;

    EditText editView;
    TextView tvItemsPrice, tvFavCount;

    String catType, couponGroupID, productId;
    NewDealsOrderDetails dealOrderDetails;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_add);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle b = this.getIntent().getExtras();
        String itemName = b.getString("itemName");
        catType = b.getString("catType");
        selectedProduct = (Product) b.getSerializable("selected_product");

        tvItemsPrice = (TextView) findViewById(R.id.itemPrice);
        tvFavCount = (TextView) findViewById(R.id.favCount);

        Button buttonAddOrders= (Button)findViewById(R.id.buttonAddOrders);

        buttonAddOrders.setText("Add to Order");

        TextView headerTextView=(TextView)findViewById(R.id.headerTextView);
        headerTextView.setText(itemName);

        editView=(EditText)findViewById(R.id.favPizzaName);
        editView.setText(itemName);//"My "+
        editView.setSelection(itemName.length());


        favCountTxt=(TextView)findViewById(R.id.favCountTxt);

        ImageView favArrowDown= (ImageView)findViewById(R.id.favArrowDown);
        favArrowDown.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(currentCount>1)
                {
                    currentCount--;
                    favCountTxt.setText(currentCount+"");
                }
            }
        });

        ImageView favArrowUp= (ImageView)findViewById(R.id.favArrowUp);
        favArrowUp.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(currentCount<10)
                {
                    currentCount++;
                    favCountTxt.setText(currentCount+"");
                }
            }
        });


        Button buttonAddFav= (Button)findViewById(R.id.buttonAddFav);
        buttonAddFav.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                DeepsliceDatabase dbInstance = new DeepsliceDatabase(AddToOrderActivity.this);
                dbInstance.open();
                boolean favAdded=dbInstance.favAlreadyAdded(selectedProduct.getProdID(),editView.getText().toString());
                if(favAdded){
                    Toast.makeText(AddToOrderActivity.this, "Already added to Favourites", Toast.LENGTH_LONG).show();
                }
                else{
                    dbInstance.insertFav(getFavBean());
                    doOnResumeWork();
                    Toast.makeText(AddToOrderActivity.this, "Successfully added to Favourites", Toast.LENGTH_LONG).show();
                }
                dbInstance.close();

            }


        });


        buttonAddOrders.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                DeepsliceDatabase dbInstance = new DeepsliceDatabase(AddToOrderActivity.this);
                dbInstance.open();
                dbInstance.insertOrder(getOrder(Constants.PRODUCT_SELECTION_WHOLE));
                dbInstance.close();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddToOrderActivity.this);
                alertDialog.setTitle("Deepslice");
                alertDialog.setMessage("Added to Cart Successfully");
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    } 
                }); 
                alertDialog.create().show();   

            }
        });

        LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
        myOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AddToOrderActivity.this,MyOrderActivity.class);
                startActivity(intent);

            }
        });
        Button openFavs=(Button)findViewById(R.id.favs);
        openFavs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AddToOrderActivity.this,FavoriteListActivity.class);
                startActivity(intent);

            }
        });

        Button mainMenu=(Button)findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AddToOrderActivity.this,MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    private Favourite getFavBean() {

        Favourite f = new Favourite();
        f.setProdCatID(selectedProduct.getProdCatID());
        f.setSubCatID1(selectedProduct.getSubCatID1());
        f.setSubCatID2(selectedProduct.getSubCatID2());
        f.setProdID(selectedProduct.getProdID());
        f.setProdCode(selectedProduct.getProdCode());
        f.setDisplayName(editView.getText().toString());
        f.setProdAbbr(selectedProduct.getProdAbbr());
        f.setProdDesc(selectedProduct.getProdDesc());
        f.setIsAddDeliveryAmount(selectedProduct.getIsAddDeliveryAmount());
        f.setDisplaySequence(selectedProduct.getDisplaySequence());
        f.setCaloriesQty(selectedProduct.getCaloriesQty());
        f.setPrice(selectedProduct.getPrice());
        f.setThumbnail(selectedProduct.getThumbnail());
        f.setFullImage(selectedProduct.getFullImage());
        f.setCustomName(editView.getText().toString());
        f.setProdCatName(catType);
        return f;
    }


    private NewProductOrder getOrder(int selection) {       // selection = left/right/whole

        NewProductOrder order = new NewProductOrder();
        order.setProdCatId(selectedProduct.getProdCatID());
        order.setSubCatId1(selectedProduct.getSubCatID1());
        order.setSubCatId2(selectedProduct.getSubCatID2());
        order.setProdId(selectedProduct.getProdID());
        order.setProdCode(selectedProduct.getProdCode());
        order.setDisplayName(selectedProduct.getDisplayName());
        order.setCaloriesQty(selectedProduct.getCaloriesQty());
        order.setPrice(selectedProduct.getPrice());
        order.setThumbnailImage(selectedProduct.getThumbnail());
        order.setFullImage(selectedProduct.getFullImage());
        order.setQuantity(String.valueOf(currentCount));
        order.setProdCatName(catType);
        order.setIsCreateByOwn(false);
        order.setSelection(selection);      
        order.setOtherHalfProdId(0);               // default

        return order;
    }


    @Override
    protected void onResume() {
        super.onResume();
        doOnResumeWork();
    }

    private void doOnResumeWork() {
        List<String> orderInfo = Utils.OrderInfo(AddToOrderActivity.this);
        int itemCount = Integer.parseInt(orderInfo.get(Constants.INDEX_ORDER_ITEM_COUNT));
        String totalPrice = orderInfo.get(Constants.INDEX_ORDER_PRICE);

        if(itemCount > 0){
            tvItemsPrice.setText(itemCount + " Items "+"\n$" + totalPrice);
            tvItemsPrice.setVisibility(View.VISIBLE);
        }
        else{
            tvItemsPrice.setVisibility(View.INVISIBLE);
        }


        String favCount = Utils.FavCount(AddToOrderActivity.this);
        if (favCount != null && !favCount.equals("0")) {
            tvFavCount.setText(favCount);
            tvFavCount.setVisibility(View.VISIBLE);
        }
        else{
            tvFavCount.setVisibility(View.INVISIBLE);
        }
    }
}
