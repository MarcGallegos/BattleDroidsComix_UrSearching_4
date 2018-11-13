package com.example.android.battledroidscomix_ursearching_4;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.battledroidscomix_ursearching_4.data.ComiContract;
import com.example.android.battledroidscomix_ursearching_4.data.ComiContract.TitleEntry;
import com.example.android.battledroidscomix_ursearching_4.data.ComixDbHelper;

/**
 * Allows user to create a new inventory entry or edit an existing one
 */
public class EditorActivity extends AppCompatActivity {

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
    /**"Check" Variable is true if TextUtils is empty*/
    private boolean check;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Find all relevant views needed to read user input from
        mProdNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mSuppNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSuppPhoneEditText = (EditText) findViewById(R.id.edit_supplier_ph);
        mProdPriceEditText = (EditText) findViewById(R.id.edit_price);
        mInventoryQtyEditText = (EditText) findViewById(R.id.edit_quantity);
        mSectionSpinner = (Spinner) findViewById(R.id.spinner_section);

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
        if (TextUtils.isEmpty(nameString)){
            check = true;

        }
        String suppString = mSuppNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(suppString)){
            check = true;

        }
        String supPhString = mSuppPhoneEditText.getText().toString().trim();
        if (TextUtils.isEmpty(supPhString)){
            check = true;

        }
        String priceString = mProdPriceEditText.getText().toString().trim();
        if (TextUtils.isEmpty(priceString)){
            check = true;

        }
        String qtyString = mInventoryQtyEditText.getText().toString().trim();
        if (TextUtils.isEmpty(qtyString)){
            check = true;

        }

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
            Toast.makeText(this, getString(R.string.editor_item_save_failure) + newUri, Toast.LENGTH_SHORT).show();
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
                // Save Product Entry to Database(L187),
                // and exit- finish operation(L188), and return true(L189)
                if (!check) {
                    insertItem();
                    finish();
                }
                return true;
            //Respond to "Delete" menu item being selected
            case R.id.action_delete:
                //AlertDialog to prompt user's action for data deletion
                AlertDialog.Builder dbDeleteDialog = new AlertDialog.Builder(final Context context, final Uri){
                    dbDeleteDialog.setMessage(R.string.delete_dialog);
            }
                //TODO: Write method to Delete Item
                return true;
            //Respond to "Up" arrow button selection on app bar
            case R.id.home:
                //Navigate back to Parent Activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


