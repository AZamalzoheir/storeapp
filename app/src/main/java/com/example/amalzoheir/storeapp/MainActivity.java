package com.example.amalzoheir.storeapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.amalzoheir.storeapp.data.ProductContract;
public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    ImageButton addProductButton;
    ProductCursorAdapter mCursorAdapter;
    private static final int PRODUCT_LOADER=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addProductButton=(ImageButton) findViewById(R.id.add_product);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, editorActivity.class);
                startActivity(intent);
            }
        });

        ListView lViewItem=(ListView)findViewById(R.id.list);
        mCursorAdapter=new ProductCursorAdapter(this,null);
        lViewItem.setAdapter(mCursorAdapter);
        lViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,editorActivity.class);
                Uri currentPetUri= ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI,id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(PRODUCT_LOADER,null,this);
    }
    private void insertProduct(){
        ContentValues contentValues=new ContentValues();
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "book");
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, 6);
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, 7);
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER, "AMC");
        contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PICTURE,"");
        Uri newUri =getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI,contentValues);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String []projection={
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER

        };
        return new android.content.CursorLoader(this,
                ProductContract.ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
