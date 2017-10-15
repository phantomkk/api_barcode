package com.project.barcodechecker.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lucky on 15-Oct-17.
 */

public class Rating implements Serializable {
    @SerializedName("ID")
    private int id;
    @SerializedName("ProductID")
    private int productID;
    @SerializedName("UserID")
    private int userID;
    @SerializedName("Rating")
    private int rating;
}
