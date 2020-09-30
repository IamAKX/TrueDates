package com.neosao.truedates.model;

public class SubscribtionPriceDataModel {
    private String packagePriceCode;
    private String amount;
    private String quantity;
    private String unit;
    private String isActive;

    public SubscribtionPriceDataModel() {
    }

    public SubscribtionPriceDataModel(String packagePriceCode, String amount, String quantity, String unit, String isActive) {
        this.packagePriceCode = packagePriceCode;
        this.amount = amount;
        this.quantity = quantity;
        this.unit = unit;
        this.isActive = isActive;
    }

    public String getPackagePriceCode() {
        return packagePriceCode;
    }

    public void setPackagePriceCode(String packagePriceCode) {
        this.packagePriceCode = packagePriceCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "SubscribtionPriceDataModel{" +
                "packagePriceCode='" + packagePriceCode + '\'' +
                ", amount='" + amount + '\'' +
                ", quantity='" + quantity + '\'' +
                ", unit='" + unit + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}
