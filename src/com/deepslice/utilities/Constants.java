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
    
    public static final int API_RESPONSE_TYPE_JSON_OBJECT = 187;
    public static final int API_RESPONSE_TYPE_JSON_ARRAY = 188;

    public static final int RESPONSE_STATUS_CODE_SUCCESS = 200;

    public static final String TAG_PARSE_ERROR = "PARSE ERROR";
    
    public static final DecimalFormat twoDForm = new DecimalFormat("0.00");
    
    public static final int INDEX_ORDER_ITEM_COUNT = 0; 
    public static final int INDEX_ORDER_PRICE = 1; 
    
    public static final String PRODUCT_CATEGORY_PIZZA = "Pizza";
    public static final String PRODUCT_CATEGORY_PASTA = "Pasta";
    public static final String PRODUCT_CATEGORY_SIDES = "Sides";
    public static final String PRODUCT_CATEGORY_DRINKS = "Drinks";
    public static final String PRODUCT_CATEGORY_CREATE_YOUR_OWN = "create_your_own";
    public static final String PRODUCT_CATEGORY_HALF_N_HALF = "half_n_half";
    
    public static final int PRODUCT_SELECTION_WHOLE = 99;
    public static final int PRODUCT_SELECTION_LEFT = 100;
    public static final int PRODUCT_SELECTION_RIGHT = 101;
    
    public static final int MAX_TOPPING_COUNT_FOR_PIZZA = 11;
    public static final String SINGLE_SIZE_TOPPING_ID = "13";
    
    public static final String[] CREDIT_CARD_TYPE = {
      "Visa", "MasterCard", "American Express"  
    };
    
    public static final String[] MONTH_NAME = {
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
      };
    
    public static final int DUMMY_ID = -1;
    
    public static final String ORDER_LOCATION_ID = "order_location_id";
    public static final String ORDER_CUSTOMER_ID = "order_customer_id";
    public static final String ORDER_SERVICE_METHOD = "order_service_method";
    public static final String ORDER_IS_TIMED_ORDER = "order_is_timed_order";
    public static final String ORDER_TIMED_ORDER_DATE = "order_timed_order_date";
    public static final String ORDER_TIMED_ORDER_TIME = "order_timed_order_time";
    public static final String ORDER_PAYMENT_STATUS = "order_payment_status";
    public static final String ORDER_TOTAL_PRICE = "order_total_price";
    public static final String ORDER_NO_OF_ITEMS = "order_no_of_items";
    
    public static final String PAYMENT_NO = "payment_no";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String PAYMENT_SUB_TYPE = "payment_sub_type";
    public static final String PAYMENT_AMOUNT = "payment_amount";
    public static final String PAYMENT_CARD_TYPE = "payment_card_type";
    public static final String PAYMENT_NAME_ON_CARD = "payment_name_on_card";
    public static final String PAYMENT_CARD_NO = "payment_card_no";
    public static final String PAYMENT_CARD_SECURITY_CODE = "payment_card_security_code";
    public static final String PAYMENT_EXPIRY_MONTH = "payment_expiry_month";
    public static final String PAYMENT_EXPIRY_YEAR = "payment_expiry_year";
    
    public static final String CUSTOMER_ID = "customer_id";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMER_EMAIL = "customer_email";
    public static final String CUSTOMER_PHONE = "customer_phone";
    public static final String CUSTOMER_PHONE_EXT = "customer_phone_ext";
    public static final String CUSTOMER_PASSWORD = "customer_password";
    public static final String CUSTOMER_FLAG_SEND_EMAIL = "customer_flag_send_email";
    public static final String CUSTOMER_SUBURB_ID = "customer_suburb_id";
    public static final String CUSTOMER_POSTAL_CODE = "customer_postal_code";
    public static final String CUSTOMER_UNIT = "customer_unit";
    public static final String CUSTOMER_STREET_NAME = "customer_street_name";
    public static final String CUSTOMER_CROSS_STREET = "customer_cross_street";
    public static final String CUSTOMER_DELIVERY_INSTRUCTIONS = "customer_delivery_instructions";  
    
    public static final String APP_INFO_CURRENCY_SIGN = "app_info_currency_sign";
    public static final String APP_INFO_DELIVERY_CHARGES = "app_info_delivery_charges";
    public static final String APP_INFO_TAX_PER = "app_info_tax_per";
    
    public static final String ORDER_TYPE = "order_type";
    public static final int ORDER_TYPE_DELIVERY = 1;
    public static final int ORDER_TYPE_PICKUP = 2;
    
    public static final String IS_ORDER_READY = "is_order_ready";
    public static final String IS_REMEMBER_ME = "is_remember_me";
    public static final String PARTIAL_SELECTION_SURCHARGE = "partial_selection_surcharge";
    
    
}
