package com.deepslice.model;

public class PaymentInfo {
    private int PaymentNo;
    private String PaymentType;
    private String PaymentSubType;
    private double Amount;
    private String CardType;
    private String NameOnCard;
    private String CardNo;
    private String CardSecurityCode;
    private int ExpiryMonth;
    private int ExpiryYear;
    
    public PaymentInfo() {
        // TODO Auto-generated constructor stub
    }
    
    

    public PaymentInfo(int paymentNo, String paymentType, String paymentSubType, double amount, String cardType,
            String nameOnCard, String cardNo, String cardSecurityCode, int expiryMonth, int expiryYear) {
        PaymentNo = paymentNo;
        PaymentType = paymentType;
        PaymentSubType = paymentSubType;
        Amount = amount;
        CardType = cardType;
        NameOnCard = nameOnCard;
        CardNo = cardNo;
        CardSecurityCode = cardSecurityCode;
        ExpiryMonth = expiryMonth;
        ExpiryYear = expiryYear;
    }


    public int getPaymentNo() {
        return PaymentNo;
    }

    public void setPaymentNo(int paymentNo) {
        PaymentNo = paymentNo;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getPaymentSubType() {
        return PaymentSubType;
    }

    public void setPaymentSubType(String paymentSubType) {
        PaymentSubType = paymentSubType;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public String getNameOnCard() {
        return NameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        NameOnCard = nameOnCard;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }

    public String getCardSecurityCode() {
        return CardSecurityCode;
    }

    public void setCardSecurityCode(String cardSecurityCode) {
        CardSecurityCode = cardSecurityCode;
    }

    public int getExpiryMonth() {
        return ExpiryMonth;
    }

    public void setExpiryMonth(int expiryMonth) {
        ExpiryMonth = expiryMonth;
    }

    public int getExpiryYear() {
        return ExpiryYear;
    }

    public void setExpiryYear(int expiryYear) {
        ExpiryYear = expiryYear;
    }
    
    

}
