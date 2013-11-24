package com.deepslice.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bugsense.trace.BugSenseHandler;
import com.deepslice.adapter.CreateYourOwnCrustAdapter;
import com.deepslice.model.CreateOwnPizzaData;
import com.deepslice.model.ServerResponse;
import com.deepslice.parser.JsonParser;
import com.deepslice.utilities.Constants;
import com.deepslice.utilities.DeepsliceApplication;

public class CreateYourOwnCrustActivity extends Activity {

    private ProgressDialog pDialog;
    JsonParser jsonParser = new JsonParser();

    ListView crustList;
    CreateYourOwnCrustAdapter crustAdapter;
    List<CreateOwnPizzaData> pizzaArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(this, "92b170cf");
        
        setContentView(R.layout.crusts);

        crustList = (ListView) findViewById(R.id.listView1);
        crustList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                CreateOwnPizzaData selectedPizzaData = (CreateOwnPizzaData) parent.getItemAtPosition(position);

                Intent i=new Intent(CreateYourOwnCrustActivity.this, CreateYourOwnPizzaActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("selected_crust", selectedPizzaData.getSubCatCode());
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        pDialog = new ProgressDialog(CreateYourOwnCrustActivity.this);
        new GetOwnPizzaData().execute();
    }

    private void updateUI(){
        if(pizzaArray == null || pizzaArray.size() == 0){
            crustList.setAdapter(null);
        }
        else{
            Log.d("Updating UI", "crust array size = " + pizzaArray.size());

            DeepsliceApplication appInstance = (DeepsliceApplication) getApplication();
            appInstance.setCreatePizzaDataList(pizzaArray);
            Set<CreateOwnPizzaData> s = new HashSet<CreateOwnPizzaData>();
            s.addAll(pizzaArray);         
            pizzaArray = new ArrayList<CreateOwnPizzaData>();
            pizzaArray.addAll(s);  
            Log.d("Updating UI", "crust array size after removing duplicate = " + pizzaArray.size());

            crustAdapter = new CreateYourOwnCrustAdapter(CreateYourOwnCrustActivity.this, pizzaArray);
            crustList.setAdapter(crustAdapter);
        }
    }
    
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        BugSenseHandler.startSession(this);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        BugSenseHandler.closeSession(this);
    }




    public class GetOwnPizzaData extends AsyncTask<Void, Void, Boolean>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = Constants.ROOT_URL + Constants.METHOD_CREATE_OWN_PIZZA;
            ServerResponse response = jsonParser.retrieveGETResponse(url, null, Constants.API_RESPONSE_TYPE_JSON_ARRAY);

            if(response.getStatus() == Constants.RESPONSE_STATUS_CODE_SUCCESS){
                JSONObject jsonObj = response.getjObj();
                try {
                    JSONObject responseObj = jsonObj.getJSONObject("Response");
                    int status = responseObj.getInt("Status");
                    JSONArray data = responseObj.getJSONArray("Data");
                    JSONObject errors = responseObj.getJSONObject("Errors");

                    pizzaArray = CreateOwnPizzaData.parsePizzaDataList(data);
                    return true;
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pDialog.isShowing())
                pDialog.dismiss();
            if(result){
                updateUI();
            }
        }
    }

}
