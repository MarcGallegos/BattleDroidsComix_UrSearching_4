package com.example.android.battledroidscomix_ursearching_4;

import android.content.ContentValues;
import android.content.Intent;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.content.CursorLoader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.battledroidscomix_ursearching_4.Adapters.ItemCursorAdapter;
import com.example.android.battledroidscomix_ursearching_4.data.ComiContract.TitleEntry;

import static com.example.android.battledroidscomix_ursearching_4.data.ComiContract.TitleEntry.CONTENT_URI;

//TODO: Add button in ListView to call Supplier, must open phone via intent (intent.ACTION_DIAL)

//TODO: Add Sale btn (decrementing)


public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int URL_LOADER = 0;

    ItemCursorAdapter mItemCursorAdapter;

    static final String[] projection = {TitleEntry._ID, TitleEntry.COLUMN_PRODUCT_NAME, TitleEntry.COLUMN_SUPPLIER, TitleEntry.COLUMN_SUPPLIER_PH, TitleEntry.COLUMN_PRICE, TitleEntry.COLUMN_QTY, TitleEntry.COLUMN_SECTION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent illestIntentions = new Intent
                        (CatalogActivity.this, EditorActivity.class);
                startActivity(illestIntentions);
            }
        });

        //Find ListView to be populated with db data
        ListView list = findViewById(R.id.list);

        //Find/Set empty view on ListView
        View emptyView = findViewById(R.id.empty_view);
        list.setEmptyView(emptyView);

        mItemCursorAdapter = new ItemCursorAdapter(this, null);

        list.setAdapter(mItemCursorAdapter);

        getLoaderManager().initLoader(URL_LOADER,null ,this);
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the DB state.
     */
    private void displayDatabaseInfo() {


        //Define a projection that specifies which columns from the items database you will use after
        //this query.
        String[] projection = {
                TitleEntry._ID,
                TitleEntry.COLUMN_PRODUCT_NAME,
                TitleEntry.COLUMN_SUPPLIER,
                TitleEntry.COLUMN_SUPPLIER_PH,
                TitleEntry.COLUMN_PRICE,
                TitleEntry.COLUMN_QTY,
                TitleEntry.COLUMN_SECTION};

        //Perform query on provider using ContentResolver.
        //Use the {@link TitleEntry#CONTENT_URI} to access the pet data
        Cursor cursor = getContentResolver().query(
                CONTENT_URI, //The content URI of the words table
                projection,             //The columns to return for each row
                null,          //Selection Criteria
                null,       //Selection Criteria
                null);    //Sort order for returned rows

        TextView displayView = (TextView) findViewById(R.id.txt_vu_item);

        try {
            //Create header in TextView that looks like this:
            //
            //The items table contains <number of rows in Cursor> pets.
            //_id - product name - supplier name - supplier ph num - price - qty - section
            //
            //In the WHILE loop below, iterate through the rows of the cursor and display the
            //information from each column in following order.
            displayView.setText("The items table contains " + cursor.getCount() + " items. \n\n");
            displayView.append(TitleEntry._ID + " - " +
                    TitleEntry.COLUMN_PRODUCT_NAME + " - " +
                    TitleEntry.COLUMN_SUPPLIER + " - " +
                    TitleEntry.COLUMN_SUPPLIER_PH + " - " +
                    TitleEntry.COLUMN_PRICE + " - " +
                    TitleEntry.COLUMN_QTY + " - " +
                    TitleEntry.COLUMN_SECTION + "\n");

            //Find/Bind index of each column.
            int idColumnIndex = cursor.getColumnIndex(TitleEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(TitleEntry.COLUMN_PRODUCT_NAME);
            int supplColumnIndex = cursor.getColumnIndex(TitleEntry.COLUMN_SUPPLIER);
            int suppPhColumnIndex = cursor.getColumnIndex(TitleEntry.COLUMN_SUPPLIER_PH);
            int priceColumnIndex = cursor.getColumnIndex(TitleEntry.COLUMN_PRICE);
            int quantColumnIndex = cursor.getColumnIndex(TitleEntry.COLUMN_QTY);
            int sectColumnIndex = cursor.getColumnIndex(TitleEntry.COLUMN_SECTION);

            //Iterate thru all returned rows in cursor.
            while (cursor.moveToNext()) {
                //Use that index to extract string or Int value of the word @ current row
                //cursor is on.
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentSupplier = cursor.getString(supplColumnIndex);
                String currentSuppPh = cursor.getString(suppPhColumnIndex);
                String currentPrice = cursor.getString(priceColumnIndex);
                int currentQty = cursor.getInt(quantColumnIndex);
                int currentSect = cursor.getInt(sectColumnIndex);
                //Display the values from each respective current column of the current row in the
                // cursor in the TextView.
                displayView.append(("\n" + currentId + " - " +
                        currentName + " - " +
                        currentSupplier + " - " +
                        currentSuppPh + " - " +
                        currentPrice + " - " +
                        currentQty + " - " +
                        currentSect));
                //Log cursor count to verify != null
                Log.v("CatalogActivity dispDB", "Cursor Count" + cursor.getCount());
            }
        } finally {
            //Always close the cursor when done reading from it. This releases all it's resources
            //and makes it invalid.
            cursor.close();
        }
    }

    /**
     * Helper method to insert hardcoded item data into database, for debugging purposes only.
     */
    private void insertItem() {
        //Create a ContentValues object where column names are the keys, and misc schwag's item
        //attributes are it's values.
        ContentValues values = new ContentValues();
        values.put(TitleEntry.COLUMN_PRODUCT_NAME, "DroidPool #1");
        values.put(TitleEntry.COLUMN_SUPPLIER, "Comic World");
        values.put(TitleEntry.COLUMN_SUPPLIER_PH, 5551234);
        values.put(TitleEntry.COLUMN_PRICE, 9999);
        values.put(TitleEntry.COLUMN_QTY, 1);
        values.put(TitleEntry.COLUMN_SECTION, 2);

        // Insert a new row for DroidPool #1 into the provider using Content Resolver.
        // Use the {@link TitleEntry#CONTENT_URI} to indicate that we want to insert
        // into the items database table.
        //Receive the new content URI that will allow us to access DroidPool #1's data via
        Uri newUri = getContentResolver().insert(CONTENT_URI, values);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu options in the app bar overflow menu
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        //Add menu items to app bar
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //User selected menu option in app bar overflow menu
        switch (item.getItemId()) {
            //Respond to "Insert Dummy Data" menu item selection
            case R.id.action_insert_dummy_data:
                insertItem();
                displayDatabaseInfo();
                return true;
            //Respond to "Delete ALL Database Entries" menu item selection
            case R.id.action_delete_all_entries:
                //Do nothing for this stage. Will call to yet-to-be created delete method.
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch(id) {
            case URL_LOADER:
                CursorLoader cl = new CursorLoader(this, TitleEntry.CONTENT_URI, projection, null, null, TitleEntry._ID + " DESC");
                return cl;
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mItemCursorAdapter.swapCursor(cursor);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mItemCursorAdapter.swapCursor(null);

    }
}


