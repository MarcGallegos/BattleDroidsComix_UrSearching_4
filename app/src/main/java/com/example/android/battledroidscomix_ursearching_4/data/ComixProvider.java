package com.example.android.battledroidscomix_ursearching_4.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.example.android.battledroidscomix_ursearching_4.data.ComiContract.TitleEntry;

import android.support.annotation.NonNull;
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
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
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
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
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

        //This is for the name sanity check. It gets stored string value, passing in Prod. Name column key
        String name = values.getAsString(TitleEntry.COLUMN_PRODUCT_NAME);
        //This is for the supplier sanity check. Gets stored string val., passing in Supplier column key
        String supplier = values.getAsString(TitleEntry.COLUMN_SUPPLIER);
        //This is for the supplier contact sanity check. Gets stored int. val., passing in Supplier Phone column key
        Integer contact = values.getAsInteger(TitleEntry.COLUMN_SUPPLIER_PH);
        //This is for the price sanity check. Gets stored int. val., passing in Price column key
        Integer price = values.getAsInteger(TitleEntry.COLUMN_PRICE);
        //This is for the quantity sanity check. Gets stored int. val., passing in Quantity column key
        Integer quantity = values.getAsInteger(TitleEntry.COLUMN_QTY);
        //This is for the section sanity check. It gets stored int, passing in Section column key
        Integer section = values.getAsInteger(TitleEntry.COLUMN_SECTION);

        //Name Sanity Check, If name is null, throw exception with error "Name Required"
        //Check name is not null
        if (TextUtils.isEmpty(name)){
            throw new IllegalArgumentException("Requires Item Name or Description");
        }

        //Supplier Name Sanity Check, If supplier name is null, throw exception with error "Supplier Required"
        //Check supplier is not null
        if (TextUtils.isEmpty(supplier)){
            throw new IllegalArgumentException("Requires Supplier Name");
        }

        //Supplier Contact Sanity Check, If contact is null, throw exception with error "Requires Contact"
        //Check contact is not null
        if (contact == null || contact < 0){
            throw new IllegalArgumentException("Supplier Contact Required");
        }

        //Price Sanity Check, If price is null, fine, else  throw exception with error "Valid Price Required"
        //Check price is not null
        if (price == null || price < 0){
            throw new IllegalArgumentException("Valid Price Required");
        }

        //Quantity Sanity Check, If quantity is null, fine, else  throw exception with error "Valid Quantity Required"
        //Check price is not null
        if (quantity == null || quantity < 0){
            throw new IllegalArgumentException("Valid Quantity Required");
        }

        // Section Sanity Check, If section is null or not a valid section value, throw exception with
        //error "Pet requires a valid section."  IF section is null OR section invalid- (using section
        // and the inverse of result returned from TitleEntry.isValid(section). )
        if (section == null || !TitleEntry.isValidSection(section)){
            throw new IllegalArgumentException("Section is Required");
        }

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
     * Returns the MIME type of data for the content URI
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                           String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEM_ID:
                //For the ITEM_ID code, extract out the ID from the URI
                //so we know which row to update. Selection will be "_id=?" and selection arguments
                //will be a String Array containing the actual id.
                selection = TitleEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri)) };
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update items in the database with the given content values. Apply the changes to the rows
     * specified in the selection arguments- which could be 0, 1, or more items. Return # of rows
     * updated.
     */
    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        //If the {@link TitleEntry COLUMN_NAME} key is present, verify that it's value isn't null.
        if (values.containsKey(TitleEntry.COLUMN_PRODUCT_NAME)){
        String name = values.getAsString(TitleEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Item Name Required");
        }
    }
        //If the {@link TitleEntry COLUMN_SUPPLIER} key is present, verify that it's value isn't null.
        if (values.containsKey(TitleEntry.COLUMN_SUPPLIER)) {
            String supplier = values.getAsString(TitleEntry.COLUMN_SUPPLIER);
            if (supplier == null) {
                throw new IllegalArgumentException("Item Supplier Required");
            }
        }

        //If the {@link TitleEntry COLUMN_SUPPLIER_PH} key is present, verify that it's value isn't null.
        if (values.containsKey(TitleEntry.COLUMN_SUPPLIER_PH)) {
            String supplier = values.getAsString(TitleEntry.COLUMN_SUPPLIER_PH);
            if (supplier == null) {
                throw new IllegalArgumentException("Supplier Contact Required");
            }
        }

        //If the {@link TitleEntry COLUMN_PRICE} key is present, verify that it's value isn't null.
        if (values.containsKey(TitleEntry.COLUMN_PRICE)) {
            String supplier = values.getAsString(TitleEntry.COLUMN_PRICE);
            if (supplier == null) {
                throw new IllegalArgumentException("Item Price Required");
            }
        }

        //If the {@link TitleEntry COLUMN_QTY} key is present, verify that it's value isn't null.
        if (values.containsKey(TitleEntry.COLUMN_QTY)) {
            String supplier = values.getAsString(TitleEntry.COLUMN_QTY);
            if (supplier == null) {
                throw new IllegalArgumentException("Item Quantity Required");
            }
        }

        //If the {@link TitleEntry COLUMN_SECTION} key is present, verify that it's value isn't null.
        if (values.containsKey(TitleEntry.COLUMN_SECTION)) {
            String supplier = values.getAsString(TitleEntry.COLUMN_SECTION);
            if (supplier == null) {
                throw new IllegalArgumentException("Item Section Required");
            }
        }

        //If no values to update, do nothing, return values size as 0.
        if (values.size() == 0) {
            return 0;
        }

        //Else, get writable database to update data.
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //preform update on the database and get number of rows updated
        int rowsUpdated = database.update(TitleEntry.TABLE_NAME, values, selection, selectionArgs);

        //If 1 or more rows were updated, notify all listeners that data at given URI has changed.
        if (rowsUpdated != 0) {
            try {
                getContext().getContentResolver().notifyChange(uri, null);
            }catch (NullPointerException npe) {
                Log.e(LOG_TAG, "Notify Change Failed! " + npe);
            }
        }
        //Return number of rows updated
        return rowsUpdated;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        //Get Writable Mode Database for delete operation
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Track Number of Rows Deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                //Delete all rows that match selection and selectionArgs.
                rowsDeleted = database.delete(TitleEntry.TABLE_NAME,selection , selectionArgs);
                break;
                default:
                    throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        //If one or more rows were deleted, notify all listeners that data at the given URI changed.
        if (rowsDeleted != 0) {
            try {
                getContext().getContentResolver().notifyChange(uri, null);
            } catch (NullPointerException npe) {
                Log.e(LOG_TAG, "notify change() failed! " + npe);
            }
        }
        //Return number of rows deleted
        return rowsDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return TitleEntry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return TitleEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

}
