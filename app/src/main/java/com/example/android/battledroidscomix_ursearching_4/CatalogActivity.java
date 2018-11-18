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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.battledroidscomix_ursearching_4.Adapters.ItemCursorAdapter;
import com.example.android.battledroidscomix_ursearching_4.data.ComiContract.TitleEntry;

import static com.example.android.battledroidscomix_ursearching_4.data.ComiContract.TitleEntry.CONTENT_URI;


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

        // Setup an Adapter to create a list item for each row of item data in the Cursor.
        // There is no item data yet (until the loader finishes) so pass in null for the Cursor.
        mItemCursorAdapter = new ItemCursorAdapter(this, null);
        //Set adapter to List
        list.setAdapter(mItemCursorAdapter);

        //Setup an item click listener to listen for touch on the List elements
        //Note android:focusable="false" is applied to the button in item_construct.xml
        // if you do not add that line you will not be able to click the list item at all
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //create new intent to goto EditorActivity
                Intent detailTransition = new Intent(CatalogActivity.this, EditorActivity.class);

                //Form the CONTENT_URI that represents the specific List item selected,
                //by appending "id"(passed in as input) onto {@link TitleEntry.CONTENT_URI).
                // e.g. the URI would be "content://com.example.android.battledroids_comix_ursearching_4/items/item2"
                //if item 2 was selected from the list.
                Uri currentItemUri = ContentUris.withAppendedId(TitleEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent.
                detailTransition.setData(currentItemUri);

                //Launch the EditorActivity to display data for the current pet.
                startActivity(detailTransition);
            }
        });

        //Kick off the loader
        getLoaderManager().initLoader(URL_LOADER,null ,this);
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
    
    @Override
    public void onResume() {
        super.onResume();
        Cursor cursor = getContentResolver().query(CONTENT_URI, projection,null,null, TitleEntry._ID + " DESC");
        mItemCursorAdapter.swapCursor(cursor);
    }
}