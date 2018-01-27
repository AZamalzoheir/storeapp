package com.example.amalzoheir.storeapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.amalzoheir.storeapp.data.ProductContract;
import com.example.amalzoheir.storeapp.data.ProductDbHelper;

public class MainActivity extends AppCompatActivity {
    Button addProductButton;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text=(TextView)findViewById(R.id.product);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertPet();

            }
        });
        addProductButton=(Button)findViewById(R.id.add_product);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, editorActivity.class);
                startActivity(intent);
            }
        });
        displayDatabaseInfo();
    }
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
       ProductDbHelper mDbHelper = new ProductDbHelper(this);

        // Create and/or open a database to read from it

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.

        String []projection={
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER

        };
       /*Cursor cursor=db.query(petContract.petEntry.TABLE_NAME,projection,null,null,null,null,null);*/
        Cursor cursor=getContentResolver().query(ProductContract.ProductEntry.CONTENT_URI,projection,null,null,null);
        if (cursor.moveToFirst()) // data?
            text.setText(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER)));

    }
    private void insertPet(){
        ContentValues contentValues=new ContentValues();
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "book");
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, 6);
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, 7);
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER, "AMC");
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PICTURE,"");
        Uri newUri =getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI,contentValues);
    }
}
