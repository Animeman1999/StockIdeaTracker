package com.example.jmartin5229.stockideatracker.database;

import android.database.Cursor;
import android.database.CursorWrapper;
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
        Date creationDate = new Date(getLong(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.CREATION_DATE)));
        String  description = getString(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.DESCRIPTION));
        String  picture = getString(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.PICTURE));
        String  coordinates = getString(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.COORDINATES));
        double  purchasePrice = getDouble(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.PURCHASE_PRICE));
        int numberStock = getInt(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.Number_Stock));
        Date purchaseDate = new Date(getLong(getColumnIndex(StockDBSchema.StockIdeaTable.Cols.PURCHASE_DATE)));

        Stock stock = new Stock(uuid, ticker, creationDate, description,picture,coordinates, purchasePrice, numberStock, purchaseDate);

        return stock;
    }
}
