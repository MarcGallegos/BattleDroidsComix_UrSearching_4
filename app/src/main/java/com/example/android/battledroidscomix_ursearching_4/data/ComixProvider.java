package com.example.android.battledroidscomix_ursearching_4.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.example.android.battledroidscomix_ursearching_4.data.ComiContract.TitleEntry;
import android.text.TextUtils;
import android.util.Log;

import java.security.Provider;

public class ComixProvider extends ContentProvider {

    /** URI matcher code for the content URI for the pets table */
    private static final int ITEMS = 001;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int ITEM_ID = 002;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() are here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        //This URI string accesses all rows in the items table
        sUriMatcher.addURI(ComiContract.CONTENT_AUTHORITY, ComiContract.PATH_ITEMS , ITEMS);
        //The addition of the wildcard # at the end of this URI string allows
        // substitution for an integer, giving access to a single row from the database.
        sUriMatcher.addURI(ComiContract.CONTENT_AUTHORITY,ComiContract.PATH_ITEMS+"/#" , ITEM_ID);
    }

    /** Tag for the log messages */
    public static final String LOG_TAG = ComixProvider.class.getSimpleName();

    /**Global Database Helper Object to allow reference from other ContentProvider methods*/
    private ComixDbHelper mDbHelper;

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // Create a PetDbHelper object to gain access to the pets database.
        mDbHelper = new ComixDbHelper(getContext());

        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        /*
        Choose the table to query and a sort order based on the code returned for the incoming URI.
        Here, the statements for the items table are shown.
         */
        switch (match){
            //If the URI is for all of the items table
            case ITEMS:
                //For ITEMS code, query th items table directly with the given:
                // projection, selection, selection arguments, and sort order.
                //The cursor should contain all rows of the items table.
                cursor = database.query(TitleEntry.TABLE_NAME,projection , selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ITEM_ID:
                // For the ITEM_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.BattleDroidsComix_UrSearching_4/items/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = TitleEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ComiContract.TitleEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query, unknown URI." + uri);
        }
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case ITEMS:
                return insertItem(uri, contentValues);
                default:
                    throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**Insert item into database with given content values. Return new content URI for
     * that specific row in the database*/
    private Uri insertItem(Uri uri, ContentValues values){

        //Get writable database.
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Insert new item with given values
        long id = database.insert(TitleEntry.TABLE_NAME, null,values );
        //If id is -1, insertion failed. Log err and return null.
        if (id == -1){
            Log.e(LOG_TAG, "Failed inserting row " + uri);
            return null;
        }

        //Once we know the id of the new table row,
        //return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id );
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }

}
