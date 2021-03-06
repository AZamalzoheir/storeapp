package com.example.amalzoheir.storeapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.amalzoheir.storeapp.data.ProductContract;

import java.io.File;

public class editorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    private static final int CAM_REQUEST =1 ;
    EditText nameText;
    EditText priceText;
    EditText quantityText;
    EditText supplierText;
    Button   orderButton;
    Button selectButton;
    Button increaseQuantityButton;
    Button decreaseQuantityButton;
    ImageView productImageImageView;
    String imagePath;
    int quantity;
    private boolean mProductHasChanged = false;
    private Uri mCurrentProductUri;
    public static final int EXISTING_PRODUCT_LOADER = 0;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        mCurrentProductUri=intent.getData();
        nameText=(EditText)findViewById(R.id.name);
        priceText=(EditText)findViewById(R.id.price);
        quantityText=(EditText)findViewById(R.id.quantity);
        supplierText=(EditText)findViewById(R.id.supplier);
        imagePath="";
        orderButton=(Button)findViewById(R.id.order_button);
        selectButton=(Button)findViewById(R.id.select_image_button);
        increaseQuantityButton=(Button)findViewById(R.id.increase_quantity_button);
        decreaseQuantityButton=(Button)findViewById(R.id.decrease_quantity_button);
        productImageImageView=(ImageView)findViewById(R.id.product_image_view);
        selectButton.setOnClickListener(this);
        if (mCurrentProductUri== null) {
            setTitle(getString(R.string.editor_activity_title_new_product));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_product));
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        orderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL,"supplier"+supplierText.getText().toString());
                intent.putExtra(Intent.EXTRA_SUBJECT, "Product information");
                intent.putExtra(Intent.EXTRA_TEXT,"name"+nameText.getText().toString()+
                        " price"+priceText.getText().toString()+
                        " quantity"+quantityText.getText().toString()
                );
                startActivity(Intent.createChooser(intent,"Send Email"));
            }

        });
        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityText.setText(Integer.toString(quantity++));
            }
        });
        decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity>0)
                    quantityText.setText(Integer.toString(quantity--));
            }
        });
        nameText.setOnTouchListener(mTouchListener);
        priceText.setOnTouchListener(mTouchListener);
        quantityText.setOnTouchListener(mTouchListener);
        supplierText.setOnTouchListener(mTouchListener);

    }
    private void saveProduct() {


        String nameString= nameText.getText().toString().trim();
        String priceString= priceText.getText().toString().trim();
        String quantityString = quantityText.getText().toString().trim();
        String supplierString = supplierText.getText().toString().trim();
        if((nameString.length()==0||priceString.length()==0||quantityString.length()==0||supplierString.length()==0||imagePath.length()==0)&&mCurrentProductUri == null){
            Toast.makeText(this,"data is empty",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            ContentValues contentValues = new ContentValues();
            int price = Integer.parseInt(priceString);
            int quantity = Integer.parseInt(quantityString);
            contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, nameString);
            contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, price);
            contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
            contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER, supplierString);
            contentValues.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PICTURE, imagePath);
            if (mCurrentProductUri == null) {
                Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, contentValues);
                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_insert_product_successful),
                            Toast.LENGTH_SHORT).show();
                }
            } else {

                int rowsAffected = getContentResolver().update(mCurrentProductUri, contentValues, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.editor_update_product_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.editor_update_product_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteProduct() {
        if (mCurrentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(editorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(editorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String []projection={
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PICTURE
        };
        return new android.content.CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER);
            int iamgeColumnIndex=cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PICTURE);
            String name = cursor.getString(nameColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
           String realImagePath = cursor.getString(iamgeColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            quantity = cursor.getInt(quantityColumnIndex);
            nameText.setText(name);
            supplierText.setText(supplier);
            priceText.setText(Integer.toString(price));
            quantityText.setText(Integer.toString(quantity));
            //productImageImageView.setImageURI(Uri.parse(realImagePath));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
            selectImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String imagePathCamera="store/camera/store_img.jpg";
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==1){
                productImageImageView.setImageURI(Uri.parse(imagePathCamera));
            }
             else {
                Uri selectedImageUri = data.getData();
                imagePath = selectedImageUri.toString();
                productImageImageView.setImageURI(Uri.parse(imagePath));
            }
        }
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(editorActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file=getFile();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,file);
        startActivityForResult(cameraIntent,CAM_REQUEST);
    }
    private  File getFile(){
        File folder=new File("store/camera");
        if(!folder.exists()){
            folder.mkdir();
        }
        File imageFile=new File(folder,"store_img.jpg");
        return imageFile;
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),0);
    }
    }



