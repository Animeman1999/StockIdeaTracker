package com.example.jmartin5229.stockideatracker;

import android.text.format.DateFormat;
import android.util.Log;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Jeff on 12/2/2016.
 */

public class Stock {


    private UUID mUUID;
    private String mName;
    private String  mTicker;
    private String mCreationDate;
    private String  mDescription;
    private String  mPicture;
    private String  mCoordinates;
    private double  mPurchasePrice;
    private int mNumberStock;
    private String mPurchaseDate;

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public String getTicker() {
        return mTicker;
    }

    public void setTicker(String ticker) {
        mTicker = ticker;
    }

    public String getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(String creationDate) {
        mCreationDate = creationDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPicture() {
        return mPicture;
    }

    public String getNewPicture() {
        return "IMG_" + getUUID().toString() + ".jpg";
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }

    public String getCoordinates() {
        return mCoordinates;
    }

    public void setCoordinates(String coordinates) {
        mCoordinates = coordinates;
    }

    public double getPurchasePrice() {
        return mPurchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        mPurchasePrice = purchasePrice;
    }

    public int getNumberStock() {
        return mNumberStock;
    }

    public void setNumberStock(int numberStock) {
        mNumberStock = numberStock;
    }

    public String getPurchaseDate() {
        return mPurchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        mPurchaseDate = purchaseDate;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Stock (){
        this(UUID.randomUUID());
    }

    public Stock (UUID id){
        mUUID = id;
        Date date = new Date();

        //Declare a date format to use on the date
        String dateFormat = "yyyy MMM dd hh:mm";
        //Get a string version of the data formated by the date format
        mCreationDate = DateFormat.format(dateFormat, date)
                .toString();
        Log.d("Check","#######################public Stock (UUID id){\n" +
                "        mUUID = id;\n" +
                "        mCreationDate = new Date();" + date +  " mCreationDate " + mCreationDate + " ############################");
    }


    public Stock(UUID uuid, String Name, String ticker, String CreationDate, String Description, String Picture,
                 String Coordiantes, double PurchasePrice, int NumberStock, String PurchaseDate){
        mUUID = uuid;
        mName = Name;
        mTicker = ticker;
        mCreationDate = CreationDate;
        mDescription = Description;
        mPicture = Picture;
        mCoordinates = Coordiantes;
        mPurchasePrice = PurchasePrice;
        mNumberStock = NumberStock;
        mPurchaseDate = PurchaseDate;
    }

}
