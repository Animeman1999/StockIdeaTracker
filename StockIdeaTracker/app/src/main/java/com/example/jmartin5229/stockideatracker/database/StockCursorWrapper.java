package com.example.jmartin5229.stockideatracker.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.example.jmartin5229.stockideatracker.Stock;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Jeff on 12/2/2016.
 */

public class StockCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public StockCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Stock getStock(){
        UUID uuid = UUID.fromString(getString(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.UUID)));
        String name = getString(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.NAME));
        String  ticker = getString(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.TICKER));
        String creationDate = getString( getColumnIndex(StockDBSchema.StockIdeaTable.Cols.CREATION_DATE));
        String  description = getString(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.DESCRIPTION));
        String  picture = getString(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.PICTURE));
        String  coordinates = getString(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.COORDINATES));
        double  purchasePrice = getDouble(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.PURCHASE_PRICE));
        int numberStock = getInt(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.NUMBER_STOCK));
        String purchaseDate = (getString(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.PURCHASE_DATE)));
        Double soldPrice = getDouble(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.SOLD_PRICE_PER_STOCK));
        String soldDate = getString(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.SOLD_DATE));

        Log.d("Test", "^^^^^^^^^^^^^^^^^^^^^^^^StockCursorWrapper - creationDate = " + creationDate + "^^^^^^^^^^^^^^^^^^^^^^^");
        Stock stock = new Stock(uuid);
        stock.setName(name);
        stock.setTicker(ticker);
        stock.setCreationDate(creationDate);
        stock.setDescription(description);
        stock.setPicture(picture);
        stock.setCoordinates(coordinates);
        stock.setPurchasePrice(purchasePrice);
        stock.setNumberStock(numberStock);
        stock.setPurchaseDate(purchaseDate);
        stock.setSoldDate(soldDate);
        stock.setSoldPrice(soldPrice);
        Log.d("Test", "^^^^^^^^^^^^^^^^^^^^^^^^^^^StockCursorWrapper - stock.getPurchaseDate(new Date(purchaseDate)) = " + stock.getPurchaseDate().toString()+ "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");


        return stock;
    }
}
