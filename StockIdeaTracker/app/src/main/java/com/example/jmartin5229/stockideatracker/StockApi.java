package com.example.jmartin5229.stockideatracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jmartin5229.stockideatracker.database.StockBaseHelper;
import com.example.jmartin5229.stockideatracker.database.StockCursorWrapper;
import com.example.jmartin5229.stockideatracker.database.StockDBSchema;
import com.example.jmartin5229.stockideatracker.database.StockDBSchema.StockIdeaTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.util.Log;

/**
 * Created by Jeff on 12/2/2016.
 */

public class StockApi {
    private static String TAG = "StockAPICheck";

    private static StockApi sStockApi;

    private Context mContext;

    private SQLiteDatabase mDatabase;

    public static StockApi get (Context context){
        Log.d(TAG, "---------------------Inside StockApi get--------------------------");
        if (sStockApi == null) {
            Log.d(TAG, "---------------------Inside of StockApi get - sStockApi == null--------------------------");
            sStockApi = new StockApi(context);
        }
        return sStockApi;
    }

    private StockApi(Context context){
        Log.d(TAG, "---------------------Inside of StockApi--------------------------");
        mContext = context.getApplicationContext();
        mDatabase = new StockBaseHelper(mContext).getWritableDatabase();
    }

    public void AddStock(Stock stock){
        Log.d(TAG, "---------------------Inside AddStock--------------------------");
        ContentValues values = GetContentValues(stock);
        mDatabase.insert(StockIdeaTable.NAME, null, values);
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
        Log.d(TAG, "---------------------Inside of GetStock method--------------------------");
        try {
            if (cursorWrapper.getCount() == 0) {
                Log.d(TAG, "--------------------------Cursor did not return a value--------------------");
                return null;
            }
            Log.d(TAG, "----------------------Cursor found a value--------------------------");
            cursorWrapper.moveToFirst();
            return cursorWrapper.getStock();
        } finally {
            //Close the cursor
            Log.d(TAG, "-----------------------------Exiting GetStockMethod--------------------------");
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

    public static ContentValues GetContentValues (Stock stock)
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
        values.put(StockIdeaTable.Cols.NUMBER_STOCK, stock.getNumberStock());
        if (stock.getPurchaseDate() == null)
        {
            values.put(StockIdeaTable.Cols.PURCHASE_DATE, "");
        }else
        {
            values.put(StockIdeaTable.Cols.PURCHASE_DATE, stock.getPurchaseDate().toString());
        }


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
                orderBy  //order by
        );

        return new StockCursorWrapper(cursor);
    }

    public File getPhotoFile(Stock stock){
        return new File("main/res/drawable/", stock.getPicture());
    }
}
