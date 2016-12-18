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
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.os.Environment;
import android.util.Log;

/**
 * Created by Jeff on 12/2/2016.
 */

public class StockApi {
    private static String TAG = "StockAPICheck";
    NumberFormat formatter = NumberFormat.getCurrencyInstance();

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

    public void DeleteStock(String uuid)
    {
        Log.d(TAG, "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV uuid sent in " +  uuid );
        if (mDatabase != null) {
            Log.d(TAG, "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV  database found");
            mDatabase.delete(StockIdeaTable.NAME, StockIdeaTable.Cols.UUID + " = \"" + uuid + "\"", null);
        }else{
            Log.d(TAG, "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV NOOOOOO database found");
            mDatabase = new StockBaseHelper(mContext).getWritableDatabase();
            mDatabase.delete(StockIdeaTable.NAME, StockIdeaTable.Cols.UUID + " = " + uuid, null);

        }
    }
    public void DeleteStockByTicker(String ticker)
    {
        Log.d(TAG, "VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV uuid sent in " +  ticker );
        mDatabase.delete(StockIdeaTable.NAME, StockIdeaTable.Cols.TICKER + " = " + ticker, null);
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
        values.put(StockIdeaTable.Cols.SOLD_PRICE_PER_STOCK, stock.getSoldPrice());
        values.put(StockIdeaTable.Cols.SOLD_DATE, stock.getSoldDate());
        if (stock.getPurchaseDate() == null)
        {
            values.put(StockIdeaTable.Cols.PURCHASE_DATE, "");
        }else
        {
            values.put(StockIdeaTable.Cols.PURCHASE_DATE, stock.getPurchaseDate().toString());
        }


        return values;
    }

    public List<Stock> GetStocksByTicker(String sortyBy){
        List<Stock> stocks = new ArrayList<>();
        StockCursorWrapper cursorWrapper = QueryCrimes(null, null, sortyBy);

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
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null){
            externalFilesDir.mkdirs();
        }
        return new File(externalFilesDir, stock.getNewPicture());
    }

    public String getReport(Context context){
        StockFetcher stockFetcher = new StockFetcher();
        String returnString = "";
        Stock returnedStock = new Stock();
        Double purchasedValueAccum = 0.0;
        Double purchasedValue;
        Double currentValueAccum = 0.0;
        Double currentValue;
        Double profitLoss;
        Double soldValueAccum = 0.0;

        List<Stock> stockList = GetStocks();
        for (Stock stock: stockList)
        {
            if (stock.getNumberStock() > 0) {
                if (stock.getSoldPrice() > 0){
                    soldValueAccum += stock.getSoldPrice() * stock.getNumberStock();
                    purchasedValueAccum += stock.getSoldPrice() * stock.getNumberStock();
                }else {
                    returnedStock = stockFetcher.fetchStockPriceName(stock.getTicker(), context);
                    purchasedValue = stock.getPurchasePrice() * stock.getNumberStock();
                    currentValue = returnedStock.getCurrentPrice() * stock.getNumberStock();
                    purchasedValueAccum += purchasedValue;
                    currentValueAccum += currentValue;
                }
            }
        }
        returnString += "\n";
        profitLoss = currentValueAccum + soldValueAccum - purchasedValueAccum;
        returnString += "Value All Stocks Sold = " + formatter.format(soldValueAccum) + "\n";
        returnString += "Value All Stocks Owned = " + formatter.format(currentValueAccum) + "\n\n";
        returnString += "Value All Socks Sold and Owned = " + formatter.format(purchasedValueAccum + soldValueAccum) + "\n\n";
        returnString += "Total Cost of All Stocks =  " + formatter.format(purchasedValueAccum) + "\n";
        if (profitLoss >= 0){
            returnString += "Total profit if all stock sold now is " + formatter.format(profitLoss); //String.valueOf(profitLoss);
        }else {
            returnString += "Total loss if all stock sold now is " + formatter.format(profitLoss);
        }
        return returnString;
    }
}
