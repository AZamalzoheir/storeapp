package com.example.amalzoheir.storeapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        nameText=(EditText)findViewById(R.id.name);
        priceText=(EditText)findViewById(R.id.price);
        quantityText=(EditText)findViewById(R.id.quantity);
        supplierText=(EditText)findViewById(R.id.supplier);
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
        if (!TextUtils.isEmpty(quantityString)) {
            quantity= Integer.parseInt(quantityString);
        }
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, nameString);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                savePet();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        return true;
    }
}


