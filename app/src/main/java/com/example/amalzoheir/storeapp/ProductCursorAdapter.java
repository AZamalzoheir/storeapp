package com.example.amalzoheir.storeapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.amalzoheir.storeapp.data.ProductContract;

/**
 * Created by Amalzoheir on 1/27/2018.
 */

public class ProductCursorAdapter extends CursorAdapter {
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final TextView nameTextView = (TextView) view.findViewById(R.id.product_name);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.product_quantity);
        final TextView priceTextView = (TextView) view.findViewById(R.id.product_price);
        final Button saleButton = (Button) view.findViewById(R.id.reduce_quantity);
        int idColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        final int productID = cursor.getInt(idColumnIndex);
        String productName = cursor.getString(nameColumnIndex);
        final int productQuantity = cursor.getInt(quantityColumnIndex);
        int productPrice = cursor.getInt(priceColumnIndex);
        nameTextView.setText(productName);
        quantityTextView.setText(toString().valueOf(productQuantity));
        priceTextView.setText(toString().valueOf(productPrice));
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productQuantity > 0) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity - 1);
                    Uri quantityProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, productID);
                    context.getContentResolver().update(quantityProductUri, contentValues, null, null);
                    quantityTextView.setText(toString().valueOf(productQuantity - 1));
                }
            }
        });
    }
}
