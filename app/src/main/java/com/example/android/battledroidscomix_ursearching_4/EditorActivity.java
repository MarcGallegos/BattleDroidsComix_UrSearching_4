package com.example.android.battledroidscomix_ursearching_4;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.battledroidscomix_ursearching_4.data.ComiContract.TitleEntry;

//TODO: Create db delete method...

/**
 * Allows user to create a new inventory entry or edit an existing one
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String[] projection = {TitleEntry._ID, TitleEntry.COLUMN_PRODUCT_NAME, TitleEntry.COLUMN_SUPPLIER, TitleEntry.COLUMN_SUPPLIER_PH, TitleEntry.COLUMN_PRICE, TitleEntry.COLUMN_QTY, TitleEntry.COLUMN_SECTION};

    private static final int EXISTING_INVENTORY_LOADER = 0;

    private Uri mCurrentProductUri;

    /**
     * EditText field for user to enter product name
     */
    private EditText mProdNameEditText;
    /**
     * EditText field for user to enter supplier name
     */
    private EditText mSuppNameEditText;
    /**
     * EditText field for user to enter supplier phone number
     */
    private EditText mSuppPhoneEditText;
    /**
     * EditText field for user to enter product price
     */
    private EditText mProdPriceEditText;
    /**
     * EditText field for user to enter product price
     */
    private EditText mInventoryQtyEditText;

    /**
     * Spinner widget for user to select section
     */
    private Spinner mSectionSpinner;
    /**
     * Section for Categorical Stocking. Possible valid values in the ComiContract.java file:
     * {@link TitleEntry#MISC_MERCH}, {@link TitleEntry#ACTION}, {@link TitleEntry#MANGA},
     * {@link TitleEntry#HORROR}, {@link TitleEntry#DRAMA}, {@link TitleEntry#FANTASY},
     * {@link TitleEntry#SCI_FI},
     */
    private int mSection = TitleEntry.MISC_MERCH;
    /**
     * "Check" Variable is true if TextUtils is empty
     */
    private boolean check = false;
    private boolean mTouched = false;

    //OnTouch Listener to listen to EditTextViews
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mTouched = true;
            view.performClick();
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Examine the intent used to Launch this activity to determine if a new item needs created
        // or an existing one needs edited
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        //If intent does not have a content URI, create new entry
        if (mCurrentProductUri == null){
            //new entry- change title to "Add Comic".
            setTitle(getString(R.string.add_comic));

            //Invalidate options menu, so "Delete" menu option can be hidden. As no need to delete.
            invalidateOptionsMenu();
        } else {
            //Existing Book, change app bar to "Edit Comic"
            setTitle(getString(R.string.edit_comic));

            //Initialize a loader to read row data from the database, and display current values in editor
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        //Find all relevant views needed to read user input from
        mProdNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mSuppNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSuppPhoneEditText = (EditText) findViewById(R.id.edit_supplier_ph);
        mProdPriceEditText = (EditText) findViewById(R.id.edit_price);
        mInventoryQtyEditText = (EditText) findViewById(R.id.edit_quantity);
        mSectionSpinner = (Spinner) findViewById(R.id.spinner_section);

        // Setup onTouch Listeners on all input fields
        mProdNameEditText.setOnTouchListener(mTouchListener);
        mSuppNameEditText.setOnTouchListener(mTouchListener);
        mSuppPhoneEditText.setOnTouchListener(mTouchListener);
        mProdPriceEditText.setOnTouchListener(mTouchListener);
        mInventoryQtyEditText.setOnTouchListener(mTouchListener);
        mSectionSpinner.setOnTouchListener(mTouchListener);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner to allow user to select the Section to zone the product.
     */
    private void setupSpinner() {
        //Create adapter for the spinner. List options are from the string array it will use.
        //this spinner will use the default simple_spinner_item layout.
        ArrayAdapter sectionSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_section_options, android.R.layout.simple_spinner_item);

        //Specify dropdown layout style- simple ListView with 1 item per line.
        sectionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        //Apply SpinnerAdapter to Spinner.
        mSectionSpinner.setAdapter(sectionSpinnerAdapter);

        //Set the Integer mSelected to the constant values
        mSectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.section_action))) {
                        mSection = TitleEntry.ACTION;
                    } else if (selection.equals(getString(R.string.section_manga))) {
                        mSection = TitleEntry.MANGA;
                    } else if (selection.equals(getString(R.string.section_horror))) {
                        mSection = TitleEntry.HORROR;
                    } else if (selection.equals(getString(R.string.section_drama))) {
                        mSection = TitleEntry.DRAMA;
                    } else if (selection.equals(getString(R.string.section_fantasy))) {
                        mSection = TitleEntry.FANTASY;
                    } else if (selection.equals(getString(R.string.section_fantasy))) {
                        mSection = TitleEntry.SCI_FI;
                    } else {
                        mSection = TitleEntry.MISC_MERCH;
                    }
                }
            }

            //As AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSection = TitleEntry.MISC_MERCH;
            }
        });
    }

    /**
     * Get user input from editor and save new inventory entry into database
     */
    private void insertItem() {
        //Read from input fields
        //Use trim to eliminate leading and trailing whitespace
        String nameString = mProdNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, R.string.populate_name , Toast.LENGTH_LONG).show();
            check = true;

        }
        String suppString = mSuppNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(suppString)) {
            Toast.makeText(this, R.string.populate_supplier , Toast.LENGTH_LONG).show();
            check = true;

        }
        String supPhString = mSuppPhoneEditText.getText().toString().trim();
        if (TextUtils.isEmpty(supPhString)) {
            Toast.makeText(this, R.string.populate_contact , Toast.LENGTH_LONG).show();
            check = true;

        }
        String priceString = mProdPriceEditText.getText().toString().trim();
        if (TextUtils.isEmpty(priceString)) {
            Toast.makeText(this, R.string.populate_price , Toast.LENGTH_LONG).show();
            check = true;

        }
        String qtyString = mInventoryQtyEditText.getText().toString().trim();
        if (TextUtils.isEmpty(qtyString)) {
            Toast.makeText(this, R.string.populate_section , Toast.LENGTH_LONG).show();
            check = true;

        }

        // Quantity variable for future use with Sale and +/- buttons
        int qty = Integer.parseInt(qtyString);

        //Create a ContentValues object where column names are the keys,
        //and product attributes from the editor are values.
        ContentValues values = new ContentValues();
        values.put(TitleEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(TitleEntry.COLUMN_SUPPLIER, suppString);
        values.put(TitleEntry.COLUMN_SUPPLIER_PH, supPhString);
        values.put(TitleEntry.COLUMN_PRICE, priceString);
        values.put(TitleEntry.COLUMN_QTY, qtyString);
        values.put(TitleEntry.COLUMN_SECTION, mSection);

        //Insert new row for item in database, returning the content URI for that row
        Uri newUri = getContentResolver().insert(TitleEntry.CONTENT_URI, values);

        //Show toast message depending on whether or not insertion was successful
        if (newUri == null) {
            //If new row ID is -1, there was an error with the insertion
            Toast.makeText(this, getString(R.string.editor_item_save_failure), Toast.LENGTH_SHORT).show();
        } else {
            //Otherwise, insertion was successful and we can display new rowID in toast
            Toast.makeText(this, getString(R.string.editor_item_save_affirm) + newUri, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu options from the res/menu/menu_editor.xml file
        //This adds menu items to the app bar
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //User selects menu option in app bar overflow menu
        switch (item.getItemId()) {

            //Respond to a click on the "Save" menu option
            case R.id.action_save:

                //Check variable for exception l249
                // Save Product Entry to Database l250,
                // and exit- finish operation l207.
                if (!check) {
                    insertItem();
                    finish();
//                    return true;
                }

            //Respond to "Delete" menu item being selected
            case R.id.action_delete:
                showDeleteConfirmDialog();
                return true;

            //Respond to "Up" arrow button selection on app bar
            case R.id.home:

                if (!mTouched){
                    //Navigate back to Parent Activity (CatalogActivity)
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, navigate to parent activity.
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                };

                // Show a dialog that notifies the user they have unsaved changes
                showUnfinishedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDeleteConfirmDialog() {
        //AlertDialog to prompt user's action for data deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.delete_comic);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //User selects "cancel," dismiss dialog and continue editing item.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        //Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void showUnfinishedChangesDialog(DialogInterface.OnClickListener discardListener) {
        //AlertDialog to prompt user's action for data deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.unsaved_changes);
        builder.setPositiveButton(R.string.confirm, discardListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //User selects "cancel," dismiss dialog and continue editing item.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        //Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader cl = new CursorLoader(this, TitleEntry.CONTENT_URI, projection, null, null, TitleEntry._ID + " DESC");
        return cl;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            //Find Columns with attributes desired.
            int nameColumnIndex = cursor.getColumnIndex(TitleEntry.COLUMN_PRODUCT_NAME);
            int supplierColumnIndex = cursor.getColumnIndex(TitleEntry.COLUMN_SUPPLIER);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(TitleEntry.COLUMN_SUPPLIER_PH);
            int priceColumnIndex = cursor.getColumnIndex(TitleEntry.COLUMN_PRICE);
            int qtyColumnIndex = cursor.getColumnIndex(TitleEntry.COLUMN_QTY);
            int sectionColumnIndex = cursor.getColumnIndex(TitleEntry.COLUMN_SECTION);

            // Extract out the value from the Cursor for the given column index
            String currentName = cursor.getString(nameColumnIndex);
            String currentSupplier = cursor.getString(supplierColumnIndex);
            String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);
            double currentPrice = cursor.getDouble(priceColumnIndex);
            int currentQty = cursor.getInt(qtyColumnIndex);
            int currentSection = cursor.getInt(sectionColumnIndex);

            // Update the views on the screen with the values from the database
            mProdNameEditText.setText(String.valueOf(currentName));
            mSuppNameEditText.setText(String.valueOf(currentSupplier));
            mSuppPhoneEditText.setText(String.valueOf(currentSupplierPhone));
            mProdPriceEditText.setText(String.valueOf(currentPrice));
            mInventoryQtyEditText.setText(String.valueOf(currentQty));


            switch (currentSection) {
                case TitleEntry.MISC_MERCH:
                    mSectionSpinner.setSelection(0);
                    break;
                case TitleEntry.ACTION:
                    mSectionSpinner.setSelection(1);
                    break;
                case TitleEntry.MANGA:
                    mSectionSpinner.setSelection(2);
                    break;
                case TitleEntry.HORROR:
                    mSectionSpinner.setSelection(3);
                    break;
                case TitleEntry.DRAMA:
                    mSectionSpinner.setSelection(4);
                    break;
                case TitleEntry.FANTASY:
                    mSectionSpinner.setSelection(5);
                    break;
                case TitleEntry.SCI_FI:
                    mSectionSpinner.setSelection(6);
                    break;
                default:
                    mSectionSpinner.setSelection(0);
                    break;
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        //TODO: Make all textviews empty strings
    }
}