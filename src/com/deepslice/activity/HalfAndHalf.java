package com.deepslice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        final Intent intent=new Intent(this,PizzaDetailsActivity.class);
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
               Bundle bundle=new Bundle();
               bundle.putBoolean("isHalf",true);
               intent.putExtras(bundle);
               startActivity(intent);
           }
       });
    }
}