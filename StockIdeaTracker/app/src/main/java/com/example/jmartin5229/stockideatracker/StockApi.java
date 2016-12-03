package com.example.jmartin5229.stockideatracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jmartin5229.stockideatracker.database.StockBaseHelper;
import com.example.jmartin5229.stockideatracker.database.StockCursorWrapper;
import com.example.jmartin5229.stockideatracker.database.StockDBSchema;
import com.example.jmartin5229.stockideatracker.database.StockDBSchema.StockIdeaTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jeff on 12/2/2016.
 */

public class StockApi {

    private static StockApi sStockApi;

    private Context mContext;

    private SQLiteDatabase mDatabase;

    public static StockApi get (Context context){
        if (sStockApi == null)
        {
            sStockApi = new StockApi(context);
        }
        return sStockApi;
    }

    private StockApi(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new StockBaseHelper(mContext).getWritableDatabase();
    }

    public void AddStock(Stock stock){
        ContentValues values = GetContentValues(stock);
        mDatabase.insert(StockDBSchema.StockIdeaTable.NAME, null, values);
    }

    public List<Stock> GetStocks(){
        List<Stock> stocks = new ArrayList<>();
        StockCursorWrapper cursorWrapper = QueryCrimes(null, null, null);

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()){
                stocks.add(cursorWrapper.getStock());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }
        return stocks;
    }

    public Stock GetStock (UUID uuid){
        StockCursorWrapper cursorWrapper = QueryCrimes(
                StockIdeaTable.Cols.UUID + " = ?",
                new String[] {uuid.toString()},
                null
        );
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getStock();
        } finally {
            //Close the cursor
            cursorWrapper.close();
        }
    }

    public void UpdateStock(Stock stock) {
        String uuidString = stock.getUUID().toString();
        ContentValues values = GetContentValues(stock);
        mDatabase.update(StockDBSchema.StockIdeaTable.NAME, values,
                StockDBSchema.StockIdeaTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public ContentValues GetContentValues (Stock stock)
    {
        ContentValues values = new ContentValues();
        values.put(StockIdeaTable.Cols.UUID, stock.getUUID().toString());
        values.put(StockIdeaTable.Cols.NAME, stock.getName());
        values.put(StockIdeaTable.Cols.TICKER, stock.getTicker());
        values.put(StockIdeaTable.Cols.CREATION_DATE, stock.getCreationDate().toString());
        values.put(StockIdeaTable.Cols.DESCRIPTION, stock.getDescription());
        values.put(StockIdeaTable.Cols.PICTURE, stock.getPicture());
        values.put(StockIdeaTable.Cols.COORDINATES, stock.getCoordinates());
        values.put(StockIdeaTable.Cols.PURCHASE_PRICE, stock.getPurchasePrice());
        values.put(StockIdeaTable.Cols.Number_Stock, stock.getNumberStock());
        values.put(StockIdeaTable.Cols.PURCHASE_DATE, stock.getPurchaseDate().toString());

        return values;
    }

    public StockCursorWrapper QueryCrimes(String whereClause, String[] whereArgs, String orderBy){
        Cursor cursor = mDatabase.query(
                StockIdeaTable.NAME,
                null, // columns
                whereClause,
                whereArgs,
                null, //group by
                null, //having
                orderBy
        );

        return new StockCursorWrapper(cursor);
    }
}
