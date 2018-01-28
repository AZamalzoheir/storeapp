package com.example.amalzoheir.storeapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Amalzoheir on 1/26/2018.
 */

public class ProductDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="inventory.db";
    private static final int DATABASE_VERSION=1;
    public  ProductDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + ProductContract.ProductEntry.TABLE_NAME + " ("
                +ProductContract.ProductEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductContract.ProductEntry.COLUMN_PRODUCT_NAME+ " Text, "
                + ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE+ "  INTEGER NOT NULL DEFAULT 0, "
                +  ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY+ "  INTEGER NOT NULL DEFAULT 0, "
                + ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER+ " Text, "
                +ProductContract.ProductEntry.COLUMN_PRODUCT_PICTURE  + " Text null);";
        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
