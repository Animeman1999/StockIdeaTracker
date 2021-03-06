package com.example.jmartin5229.stockideatracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jmartin5229.stockideatracker.database.StockDBSchema.StockIdeaTable;

/**
 * Created by Jeff on 12/2/2016.
 */

public class StockBaseHelper extends SQLiteOpenHelper {

    Context mContext;


    private static final int VERSION = 1;

    private static final String DATABASE_NAME = "stockBase.db";

    public StockBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + StockIdeaTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                StockIdeaTable.Cols.UUID + ", " +
                StockIdeaTable.Cols.NAME + ", " +
                StockIdeaTable.Cols.TICKER + ", " +
                StockIdeaTable.Cols.CREATION_DATE + ", " +
                StockIdeaTable.Cols.DESCRIPTION + ", " +
                StockIdeaTable.Cols.PICTURE + ", " +
                StockIdeaTable.Cols.COORDINATES + ", " +
                StockIdeaTable.Cols.PURCHASE_PRICE + ", " +
                StockIdeaTable.Cols.NUMBER_STOCK + ", " +
                StockIdeaTable.Cols.PURCHASE_DATE + ", " +
                StockIdeaTable.Cols.SOLD_PRICE_PER_STOCK + ", " +
                StockIdeaTable.Cols.SOLD_DATE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //No Upgrade Implemented
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        }
    }

