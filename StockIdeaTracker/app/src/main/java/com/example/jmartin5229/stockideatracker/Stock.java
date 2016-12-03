package com.example.jmartin5229.stockideatracker;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Jeff on 12/2/2016.
 */

public class Stock {

    private UUID mUUID;
    private String mName;
    private String  mTicker;
    private Date mCreationDate;
    private String  mDescription;
    private String  mPicture;
    private String  mCoordinates;
    private double  mPurchasePrice;
    private int mNumberStock;
    private Date mPurchaseDate;

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

    public Date getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(Date creationDate) {
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

    public Date getPurchaseDate() {
        return mPurchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        mPurchaseDate = purchaseDate;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Stock(UUID uuid, String Name, String ticker, Date CreationDate, String Description, String Picture,
                 String Coordiantes, double PurchasePrice, int NumberStock, Date PurchaseDate){
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
