package com.example.amalzoheir.storeapp;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView=(TextView)view.findViewById(R.id.product_name);
        TextView quantityTextView=(TextView)view.findViewById(R.id.product_quantity);
        TextView priceTextView=(TextView)view.findViewById(R.id.product_price);
        int nameColumnIndex=cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex=cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex=cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        String productName=cursor.getString(nameColumnIndex);
        int productQuantity=cursor.getInt(quantityColumnIndex);
        int productPrice=cursor.getInt(priceColumnIndex);
        nameTextView.setText(productName);
        quantityTextView.setText(productQuantity);
        priceTextView.setText(productPrice);
    }
}
