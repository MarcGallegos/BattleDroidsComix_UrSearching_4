package com.example.android.battledroidscomix_ursearching_4;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.example.android.battledroidscomix_ursearching_4.data.ComiContract;
import com.example.android.battledroidscomix_ursearching_4.data.ComiContract.TitleEntry;
import com.example.android.battledroidscomix_ursearching_4.data.ComixDbHelper;

public class CatalogActivity extends AppCompatActivity {

    //database helper provides access to database
    private ComixDbHelper mComixDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

    //Setup FAB to open EditorActivity
    FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
            startActivity(intent);
        }
    });

        // To access our database, we instantiate our SQLiteOpenHelper subclass and pass the
        // context, which is the current activity.
        mComixDbHelper=new ComixDbHelper(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the DB state.
     */
    private void displayDatabaseInfo() {
        //Create and/or open a database a database to read from it.
        SQLiteDatabase db = mComixDbHelper.getReadableDatabase();

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

        //Perform query on the items database table
        Cursor cursor = db.query(
                TitleEntry.TABLE_NAME,      //The table to query
                projection,                 //The Columns to return
                null,                       //The columns for the WHERE clause
                null,                       //The values for the WHERE clause
                null,                       //Don't group the rows
                null,                        //Don't filter by row groups
                null);                      //The sort order

        TextView displayView = (TextView) findViewById(R.id.txt_vu_item);

        try {
            //Create header in TextView that looks like this:
            //
            //The items table contains <number of rows in Cursor> pets.
            //_id - product name - supplier name - supplier ph num - price - qty - section
            //
            //In the WHILE loop below, iterate through the rows of the cursor and display the
            //information from each column in following order.
            displayView.setText(getString(R.string.table_int_header));
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
                String currentQty = cursor.getString(quantColumnIndex);
                String currentSect = cursor.getString(sectColumnIndex);
                //Display the values from each respective current column of the current row in the
                // cursor in the TextView.
                displayView.append(("\n" + currentId + " - " +
                        currentName + " - " +
                        currentSupplier + " - " +
                        currentSuppPh + " - " +
                        currentPrice + " - " +
                        currentQty + " - " +
                        currentSect));
            }
        } finally {
            //Always close the cursor when done reading from it. This releases all it's resources
            //and makes it invalid.
            cursor.close();
        }
        insertItem();
    }

        /**
         * Helper method to insert hardcoded item data into database, for debugging purposes only.
         */
        private void insertItem() {
            //Gets database into the write mode
            SQLiteDatabase database = mComixDbHelper.getWritableDatabase();

            //Create a ContentValues object where column names are the keys, and misc schwag's item
            //attributes are it's values.
            ContentValues values=new ContentValues();
            values.put(TitleEntry.COLUMN_PRODUCT_NAME,"Superman #1");
            values.put(TitleEntry.COLUMN_SUPPLIER,"Comic World");
            values.put(TitleEntry.COLUMN_SUPPLIER_PH,5551234);
            values.put(TitleEntry.COLUMN_PRICE,19999);
            values.put(TitleEntry.COLUMN_QTY,1 );
            values.put(TitleEntry.COLUMN_SECTION,2 );

            /**
             * Insert new row for Superman #1 in the database, returning the id of that new row.
             * The first arg for {@link db.insert()} is the items table name.
             * The 2nd arg provides the column name in which the framework can insert NULL in the
             * event the ContentView is empty. (If this is set NULL, the framework will not insert
             * a new row when there are no values.
             * The 3rd argument is the ContentValues object containing Superman #1's information.
             */
            long newRowId=database.insert(TitleEntry.TABLE_NAME,null ,values);
        }
    }


