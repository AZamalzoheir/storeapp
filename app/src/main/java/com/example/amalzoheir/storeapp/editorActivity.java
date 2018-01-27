package com.example.amalzoheir.storeapp;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amalzoheir.storeapp.data.ProductContract;

public class editorActivity extends AppCompatActivity {
    EditText nameText;
    EditText priceText;
    EditText quantityText;
    EditText supplierText;
    Button addProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        nameText=(EditText)findViewById(R.id.name);
        priceText=(EditText)findViewById(R.id.price);
        quantityText=(EditText)findViewById(R.id.quantity);
        supplierText=(EditText)findViewById(R.id.supplier);
        addProduct=(Button)findViewById(R.id.add_product_in_store);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePet();
            }
        });
    }
    private void savePet() {
        String nameString= nameText.getText().toString().trim();
        String priceString= priceText.getText().toString().trim();
        String quantityString = quantityText.getText().toString().trim();
        String supplierString = supplierText.getText().toString().trim();
        ContentValues contentValues = new ContentValues();

        int price= 0;
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        int quantity= 0;
        if (!TextUtils.isEmpty(priceString)) {
            quantity= Integer.parseInt(quantityString);
        }
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, nameString);
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, price);
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER, supplierString);
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PICTURE,"");
            Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, contentValues);
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }


