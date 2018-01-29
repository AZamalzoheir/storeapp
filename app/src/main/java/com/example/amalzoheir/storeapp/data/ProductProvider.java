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
            throw new IllegalArgumentException("product requires a price");
        }
        Integer quantity = contentValues.getAsInteger(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity==null) {
            throw new IllegalArgumentException("product requires quantity");
        }
        String supplier= contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER);
        if (supplier==null) {
            throw new IllegalArgumentException("product requires supplier");
        }
        SQLiteDatabase db= mdHelper.getWritableDatabase();

        Long id=db.insert(ProductContract.ProductEntry.TABLE_NAME,null,contentValues);
        if (id == -1) {
            Log.e("product provider", "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mdHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                rowsDeleted=database.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted= database.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                selection = ProductContract.ProductEntry._ID+ "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("product requires a name");
            }
        }
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER)) {
            String supplier = values.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER);
            if (supplier == null) {
                throw new IllegalArgumentException("product requires a supplier");
            }
        }
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE)) {
            Integer price = values.getAsInteger(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("product requires valid price");
            }
        }
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY)) {
            Integer quantity = values.getAsInteger(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("product requires valid quantity");
            }
        }
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_PICTURE)) {
            String image= values.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_PICTURE);
            if (image!= null) {
                throw new IllegalArgumentException("product requires valid image");
            }
        }
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mdHelper.getWritableDatabase();

        int rowsUpdated =database.update(ProductContract.ProductEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
