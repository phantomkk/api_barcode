package com.project.barcodechecker.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lucky on 31-Oct-17.
 */

public class Sale {
    @SerializedName("UserID")
    private int userID;
    @SerializedName("ProductID")
    private int productID;
    @SerializedName("Price")
    private double price;
    @SerializedName("DateCreate")
    private String dateCreate;
    @SerializedName("ImagePath")
    private String imagePath;
    @SerializedName("Name")
    private String name;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
