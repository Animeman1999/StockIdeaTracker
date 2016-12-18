package com.example.jmartin5229.stockideatracker.database;

/**
 * Created by Jeff on 12/2/2016.
 */

public class StockDBSchema {
    public static final class StockIdeaTable {
        public static final String NAME = "stock_ideas";
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String TICKER = "ticker";
            public static final String CREATION_DATE = "creation_date";
            public static final String DESCRIPTION = "description";
            public static final String PICTURE = "picture";
            public static final String COORDINATES = "coordinates";
            public static final String PURCHASE_PRICE = "purchase_price";
            public static final String NUMBER_STOCK = "number_stock";
            public static final String PURCHASE_DATE = "purchase_date";
            public static final String SOLD_PRICE_PER_STOCK = "sold_price_per_stock";
            public static final String SOLD_DATE ="sold_date";
        }
    }
}
