package com.deepslice.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.deepslice.database.AppDao;
import com.deepslice.model.DealOrderVo;
import com.deepslice.utilities.AppProperties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 6/4/13
 * Time: 11:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class HalfAndHalf extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.half_and_half);

        AppProperties.isFirstPizzaChosen = false;

        final Intent intent=new Intent(this,PizzaSubMenuActivity.class);

        Button buttonStHalf=(Button)findViewById(R.id.buttonStHalf);
        buttonStHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putBoolean("isHalf",true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Button buttonNdHalf=(Button)findViewById(R.id.buttonNdHalf);
        buttonNdHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppProperties.isFirstPizzaChosen){
                    Bundle bundle=new Bundle();
                    bundle.putBoolean("isHalf",true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(HalfAndHalf.this, "Please choose first half", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    
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
}