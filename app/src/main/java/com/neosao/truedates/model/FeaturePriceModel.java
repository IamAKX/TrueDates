package com.neosao.truedates.model;

public class FeaturePriceModel {
    private String packagePriceCode;
    private String amount;
    private String quantity;
    private String unit;
    private String isActive;

    public FeaturePriceModel() {
    }

    public FeaturePriceModel(String packagePriceCode, String amount, String quantity, String each, String isActive) {
        this.packagePriceCode = packagePriceCode;
        this.amount = amount;
        this.quantity = quantity;
        this.unit = each;
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
        return "FeaturePriceModel{" +
                "packagePriceCode='" + packagePriceCode + '\'' +
                ", amount='" + amount + '\'' +
                ", quantity='" + quantity + '\'' +
                ", unit='" + unit + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}
