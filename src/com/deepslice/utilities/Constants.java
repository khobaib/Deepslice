package com.deepslice.utilities;

import java.text.DecimalFormat;

public class Constants {
    public static final String CUSTOMER_DETAILS = "customerDetails";
    public static final String TAG = "Deepslice";
    public static final String NAMESPACE="http://apps.deepslice.com.au/";
    public static final String REGISTRATION_URL = "http://apps.deepslice.com.au/Registration.aspx";
    public static final int PASSWORD_LENGTH = 8;

    public static String ROOT_URL="http://apps.deepslice.com.au/";
    public static String DEFAULT_IMAGE_URL = "noimage.png";
    public static String IMAGES_LOCATION_PRODUCTS = "http://apps.deepslice.com.au/images/products/";
    public static String IMAGES_LOCATION_DEALS = "http://apps.deepslice.com.au/images/products/deals/";
    public static String IMAGES_LOCATION_CRUSTS = "http://apps.deepslice.com.au/images/products/crust";

    public static final String METHOD_CREATE_OWN_PIZZA = "CreateOwnPizzaData.aspx";

    public static final int RESPONSE_STATUS_CODE_SUCCESS = 200;

    public static final String TAG_PARSE_ERROR = "PARSE ERROR";
    
    public static final DecimalFormat twoDForm = new DecimalFormat("#.##");
    
    public static final int INDEX_ORDER_ITEM_COUNT = 0; 
    public static final int INDEX_ORDER_PRICE = 1; 
    
    public static final String PRODUCT_CATEGORY_PIZZA = "Pizza";
    public static final String PRODUCT_CATEGORY_PASTA = "Pasta";
    public static final String PRODUCT_CATEGORY_SIDES = "Sides";
    public static final String PRODUCT_CATEGORY_DRINKS = "Drinks";
    public static final String PRODUCT_CATEGORY_CREATE_YOUR_OWN = "create_your_own";
    public static final String PRODUCT_CATEGORY_HALF_N_HALF = "half_n_half";
}
