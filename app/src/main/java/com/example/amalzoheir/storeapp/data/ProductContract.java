package com.example.amalzoheir.storeapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Amalzoheir on 1/26/2018.
 */

public final  class ProductContract {
    private ProductContract(){}
    public static final String CONTENT_AUTHORITY ="com.example.amalzoheir.storeapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_Product = "product";
public static final class ProductEntry implements BaseColumns{
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_Product);
    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_Product;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_Product;
    public final static String TABLE_NAME="Product";
    public final static String _ID= BaseColumns._ID;
    public final static String COLUMN_PRODUCT_NAME="name";
    public final static String COLUMN_PRODUCT_PRICE="price";
    public final static String COLUMN_PRODUCT_QUANTITY="quantity";
    public final static String COLUMN_PRODUCT_SUPPLIER="supplier";
    public final static String COLUMN_PRODUCT_PICTURE="picture";
}
}
