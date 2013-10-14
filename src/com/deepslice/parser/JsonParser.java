package com.deepslice.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.deepslice.model.ServerResponse;
import com.deepslice.utilities.Constants;

public class JsonParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jArray = null;
    static String json = "";

    private static final String TAG = JsonParser.class.getSimpleName();

    public JsonParser() {

    }

    public ServerResponse retrieveGETResponse(String url, List<NameValuePair> params, int responseType) {
        Log.d(TAG, "in retrieveGETResponse method, get url = " + url);

        int status = 0;

        try {
            // request method is GET
            DefaultHttpClient httpClient = new DefaultHttpClient();

            if(params != null){
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;

            }
            Log.d(TAG, "final url = " + url);

            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            Log.d(TAG, "GETTING RESPONSE.");
            status = httpResponse.getStatusLine().getStatusCode();
            Log.d(TAG, "STAUS = " + status);

            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        try {
            if(responseType == Constants.API_RESPONSE_TYPE_JSON_ARRAY){
                jArray = new JSONArray(json);
                jObj = jArray.getJSONObject(0);
            }
            else{
                jObj = new JSONObject(json);
            }
            Log.d("in JSONPARSER, jObj VALUE = ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return ServerResponse
        return new ServerResponse(jObj, status);
    }


    public ServerResponse retrievePostResponse(String url, String content) {
        Log.d(TAG, "in getPostResponse method");

        int status = 0;


        // Making HTTP request
        try {
            Log.d("FInal url in post", url);
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            StringEntity se = new StringEntity(content);
            se.setContentEncoding("UTF-8");
            se.setContentType("application/json");
            //            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            //            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "text/html"));
            httpPost.setEntity(se);

            Log.d(TAG, "http-post final request = " + httpPost.toString());

            HttpResponse httpResponse = httpClient.execute(httpPost);

            status = httpResponse.getStatusLine().getStatusCode();
            Log.d(TAG, "STAUS = " + status);

            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.d("test", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj VALUE = ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return new ServerResponse(jObj, status);
    }



}
