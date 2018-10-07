package com.example.android.battledroidscomix_ursearching_4.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.battledroidscomix_ursearching_4.data.ComiContract.TitleEntry;

public class ComixDbHelper extends SQLiteOpenHelper{

    public static final String LOG_TAG=ComixDbHelper.class.getSimpleName();

    /** Name of the Database file */
    private static final String DATABASE_NAME="items.db";

    /** Database version. If schema is changed DB version must be incremented */
    private static final int DATABASE_VERSION=1;

    /**
     *  Constructs a new instance of {@link ComixDbHelper}
     *
     *  @param context of the app
     */
    public ComixDbHelper(Context context){super(context,DATABASE_NAME,null,DATABASE_VERSION);}

    /** This is called when the database is created for the first time. */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a string that contains the SQL statement to create the items table.
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + TitleEntry.TABLE_NAME + " ("
                + TitleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TitleEntry.COLUMN_PRODUCT_NAME + "TEXT NOT NULL, "
                + TitleEntry.COLUMN_SUPPLIER + "TEXT NOT NULL, "
                + TitleEntry.COLUMN_SUPPLIER_PH + "INTEGER NOT NULL, "
                + TitleEntry.COLUMN_PRICE + "INTEGER NOT NULL DEFAULT 0, "
                + TitleEntry.COLUMN_QTY + "INTEGER NOT NULL DEFAULT 0, "
                + TitleEntry.COLUMN_SECTION + "TEXT NOT NULL );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    /** This is called when the database needs to be upgraded. */
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        //Nothing to do here yet, database is still at version 1.
    }

}
