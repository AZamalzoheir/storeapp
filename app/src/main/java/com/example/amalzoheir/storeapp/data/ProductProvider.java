package com.example.amalzoheir.storeapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Amalzoheir on 1/26/2018.
 */

public class ProductProvider extends ContentProvider {
    private static final int PRODUCT=100;
    private static final int PRODUCT_ID=101;
    public static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY,ProductContract.PATH_Product,PRODUCT);
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY,ProductContract.PATH_Product+"/#",PRODUCT_ID);
    }
    private ProductDbHelper mdHelper;
    @Override
    public boolean onCreate() {
        mdHelper=new ProductDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database=mdHelper.getReadableDatabase();
        Cursor cursor = null;
        int match=sUriMatcher.match(uri);
        switch (match){
            case PRODUCT:
                cursor=database.query(ProductContract.ProductEntry.TABLE_NAME,projection,null,null,null,null,sortOrder);
                break;
            case PRODUCT_ID:
                selection= ProductContract.ProductEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=database.query(ProductContract.ProductEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("cannot make query"+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match=sUriMatcher.match(uri);
        switch (match){
            case PRODUCT:
                return insertPet(uri,contentValues);
            default:
                throw new IllegalArgumentException("cannot make query"+uri);
        }
    }
    private  Uri insertPet(Uri uri,ContentValues contentValues){
        Integer price = contentValues.getAsInteger(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("product requires a rice");
        }
        Integer quantity = contentValues.getAsInteger(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity==null) {
            throw new IllegalArgumentException("product requires quantity");
        }
        String supplier= contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER);
        SQLiteDatabase db= mdHelper.getWritableDatabase();

        Long id=db.insert(ProductContract.ProductEntry.TABLE_NAME,null,contentValues);
        if (id == -1) {
            Log.e("product provider", "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);//insert new item in database make notify
        return ContentUris.withAppendedId(uri,id);
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
