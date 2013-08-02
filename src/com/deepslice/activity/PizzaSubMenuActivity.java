package com.deepslice.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.deepslice.database.AppDao;
import com.deepslice.vo.AllProductsVo;
import com.deepslice.vo.DealOrderVo;
import com.deepslice.vo.ProductCategory;
import com.deepslice.vo.SubCategoryVo;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class  PizzaSubMenuActivity extends Activity{
	
	ArrayList<ProductCategory> productCatList;
	ArrayList<SubCategoryVo> subCatList;
	ArrayList<AllProductsVo> allProductsList;

	ListView listview;
	MyListAdapterPizza myAdapter;
    ProgressDialog pd;
	String catType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_menu_pizza3);
        Bundle b = this.getIntent().getExtras();
		
		catType=b.getString("catType");
	
	      AppDao dao=null;
				try {
					dao=AppDao.getSingleton(getApplicationContext());
					dao.openConnection();
					
					subCatList=dao.getSubCategoriesPizza();
					
				} catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}finally{
					if(null!=dao)
						dao.closeConnection();
				}
		
				listview = (ListView) findViewById(R.id.listView1);				
				myAdapter = new MyListAdapterPizza(this,R.layout.line_item_yello, subCatList);
				listview.setAdapter(myAdapter);
				
				listview.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v, int position,
							long id) {
						SubCategoryVo eBean = (SubCategoryVo) v.getTag();
						if (eBean != null) {
						
						Intent intent=new Intent(PizzaSubMenuActivity.this,ProductsListActivity.class);
						Bundle bundle=new Bundle();
						bundle.putString("catId",eBean.getProdCatID());
						bundle.putString("subCatId",eBean.getSubCatID());
						bundle.putString("catType","Pizza");
						bundle.putString("titeDisplay",eBean.getSubCatCode()+" Pizza");
						
						intent.putExtras(bundle);
						startActivity(intent);
						}					}
				});
				
				Button openFavs=(Button)findViewById(R.id.favs);
				openFavs.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						Intent intent=new Intent(PizzaSubMenuActivity.this,FavsListActivity.class);
						startActivity(intent);
						
					}
				});
				
				Button mainMenu=(Button)findViewById(R.id.mainMenu);
				mainMenu.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						Intent intent=new Intent(PizzaSubMenuActivity.this,MenuActivity.class);
						startActivity(intent);
						
					}
				});
				
				LinearLayout myOrder=(LinearLayout)findViewById(R.id.cartDummy);
				myOrder.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						Intent intent=new Intent(PizzaSubMenuActivity.this,MyOrderActivity.class);
						startActivity(intent);
						
					}
				});

                ImageButton imageButtonCreateOwn=(ImageButton)findViewById(R.id.imageButtonCreateUown);
                imageButtonCreateOwn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                ImageButton imageButtonHalf=(ImageButton)findViewById(R.id.imageButtonHalf);
                imageButtonHalf.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent intent=new Intent(PizzaSubMenuActivity.this,HalfAndHalf.class);
                        startActivity(intent);
                    }
                });

                ImageButton imageButtonCreateYourOwn=(ImageButton)findViewById(R.id.imageButtonCreateUown);
                imageButtonCreateOwn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(PizzaSubMenuActivity.this,CreateYourOwn.class);
                        startActivity(intent);
                    }
                });
			}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////

	private class MyListAdapterPizza extends ArrayAdapter<SubCategoryVo> {

		private ArrayList<SubCategoryVo> items;

		public MyListAdapterPizza(Context context, int viewResourceId,
				ArrayList<SubCategoryVo> items) {
			super(context, viewResourceId, items);
			this.items = items;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.line_item_yello, null);
			}
			SubCategoryVo event = items.get(position);
			if (event != null) {

				TextView title = (TextView) convertView
						.findViewById(R.id.itemName);

				title.setText(event.getSubCatCode()+" Pizza");

				convertView.setTag(event);
			}
			return convertView;
		}

	}
	// /////////////////////// END LIST ADAPTER
	@Override
	protected void onResume() {
		// //////////////////////////////////////////////////////////////////////////////
        AppDao dao = null;
        try {
            dao = AppDao.getSingleton(getApplicationContext());
            dao.openConnection();
            ArrayList<String> orderInfo = dao.getOrderInfo();
            ArrayList<DealOrderVo>dealOrderVos1= dao.getDealOrdersList();
            TextView itemsPrice = (TextView) findViewById(R.id.itemPrice);
            double tota=0.00;
            int dealCount=0;
            if((dealOrderVos1!=null && dealOrderVos1.size()>0)){
                dealCount=dealOrderVos1.size();
                for (int x=0;x<dealOrderVos1.size();x++){
                    tota+=(Double.parseDouble(dealOrderVos1.get(x).getDiscountedPrice())*(Integer.parseInt(dealOrderVos1.get(x).getQuantity())));
                }
            }

            int orderInfoCount= 0;
            double  orderInfoTotal=0.0;
            if ((null != orderInfo && orderInfo.size() == 2) ) {
                orderInfoCount=Integer.parseInt(orderInfo.get(0));
                orderInfoTotal=Double.parseDouble(orderInfo.get(1));
            }
            int numPro=orderInfoCount+dealCount;
            double subTotal=orderInfoTotal+tota;
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            subTotal= Double.valueOf(twoDForm.format(subTotal));
            if(numPro>0){
                itemsPrice.setText(numPro+" Items "+"\n$" +subTotal );
                itemsPrice.setVisibility(View.VISIBLE);
            }

            else{
                itemsPrice.setVisibility(View.INVISIBLE);

            }

            TextView favCount = (TextView) findViewById(R.id.favCount);
            String fvs=dao.getFavCount();
            if (null != fvs && !fvs.equals("0")) {
                favCount.setText(fvs);
                favCount.setVisibility(View.VISIBLE);
            }
            else{
                favCount.setVisibility(View.INVISIBLE);
            }



        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (null != dao)
                dao.closeConnection();
        }
		// ///////////////////////////////////////////////////////////////////////

		super.onResume();
	}


    //rukshan add

    }
