package com.deepslice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.deepslice.utilities.Constants;

import android.util.Log;

public class CreateOwnPizzaData  implements Serializable {

    private static final String TAG = CreateOwnPizzaData.class.getSimpleName();
    private static final long serialVersionUID = 1L;

    private String subCatId;
    private String subCatCode;
    private String subCat1Id;
    private String subCat1code;
    private String thumbImage;
    private String fullImage;
    private List<String> prodIds;

    public CreateOwnPizzaData() {
        // TODO Auto-generated constructor stub
    }

    public CreateOwnPizzaData(String subCatId, String subCatCode, String subCat1Id, String subCat1code,
            String thumbImage, String fullImage, List<String> prodIds) {
        this.subCatId = subCatId;
        this.subCatCode = subCatCode;
        this.subCat1Id = subCat1Id;
        this.subCat1code = subCat1code;
        this.thumbImage = thumbImage;
        this.fullImage = fullImage;
        this.prodIds = prodIds;
    }


    public static List<CreateOwnPizzaData> parsePizzaDataList(JSONArray pizzaDataArray){
        List<CreateOwnPizzaData> pizzaList = new ArrayList<CreateOwnPizzaData>();

        try {        
            int pizzaNumber = pizzaDataArray.length();
            for(int pizzaIndex = 0; pizzaIndex < pizzaNumber; pizzaIndex++){

                JSONObject thisPizza = pizzaDataArray.getJSONObject(pizzaIndex);

                String subCatId = thisPizza.getString("SubCatID");
                String subCatCode = thisPizza.getString("SubCatCode");
                String subCat1Id = thisPizza.getString("SubCat1ID");
                String subCat1code = thisPizza.getString("SubCat1Code");
                String thumbImage = thisPizza.getString("Thumbnail");
                String fullImage = thisPizza.getString("FullImage");
                String prodIdString = thisPizza.getString("ProdID");
                List<String> prodIds = Arrays.asList(prodIdString.split("\\s*,\\s*"));

                CreateOwnPizzaData pizza = new CreateOwnPizzaData(subCatId, subCatCode, subCat1Id, subCat1code, thumbImage, fullImage, prodIds);
                pizzaList.add(pizza);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, Constants.TAG_PARSE_ERROR);
        }
        return pizzaList;

    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getSubCatCode() {
        return subCatCode;
    }

    public void setSubCatCode(String subCatCode) {
        this.subCatCode = subCatCode;
    }

    public String getSubCat1Id() {
        return subCat1Id;
    }

    public void setSubCat1Id(String subCat1Id) {
        this.subCat1Id = subCat1Id;
    }

    public String getSubCat1code() {
        return subCat1code;
    }

    public void setSubCat1code(String subCat1code) {
        this.subCat1code = subCat1code;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getFullImage() {
        return fullImage;
    }

    public void setFullImage(String fullImage) {
        this.fullImage = fullImage;
    }

    public List<String> getProdIds() {
        return prodIds;
    }

    public void setProdIds(List<String> prodIds) {
        this.prodIds = prodIds;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CreateOwnPizzaData)
        {
            CreateOwnPizzaData temp = (CreateOwnPizzaData) obj;
            if(this.subCatCode.equals(temp.subCatCode))
                return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (this.subCatCode.hashCode());
    }

}
