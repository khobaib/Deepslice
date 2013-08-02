package com.deepslice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created with IntelliJ IDEA.
 * User: rukshan
 * Date: 6/5/13
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateYourOwn extends Activity {
    int currentIndex=0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_your_own);
        final int [] imageSet=new int[]{
                R.drawable.pizza_det_icon,
                R.drawable.pizza_icon,
                R.drawable.pizza_det_icon,
                R.drawable.pizza_det_icon
        };
       final ImageView imageViewCreateYourOwn=(ImageView)findViewById(R.id.imageViewCreateYourOwn);
        final ImageButton imageButtonNext=(ImageButton)findViewById(R.id.imageButtonNext);
        final ImageButton imageButtonPrv=(ImageButton)findViewById(R.id.imageButtonPriv);
        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex++;
                if(currentIndex<(imageSet.length-1)){
                    imageButtonNext.setVisibility(View.VISIBLE);
                    imageButtonPrv.setVisibility(View.VISIBLE);
                    Log.d("............",""+currentIndex);
                    imageViewCreateYourOwn.setImageResource(imageSet[currentIndex]);
                } else {
                    imageButtonNext.setVisibility(View.INVISIBLE);

                }

            }
        });


        imageButtonPrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex--;
                if(currentIndex>0){
                    imageButtonPrv.setVisibility(View.VISIBLE);
                    imageButtonNext.setVisibility(View.VISIBLE);
                    imageViewCreateYourOwn.setImageResource(imageSet[currentIndex]);
                }else if(currentIndex==0) {
                    imageButtonPrv.setVisibility(View.INVISIBLE);
                    imageViewCreateYourOwn.setImageResource(imageSet[currentIndex]);
                }
            }
        });

    }
}