package com.example.android.battledroidscomix_ursearching_4.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.battledroidscomix_ursearching_4.data.ComiContract.TitleEntry;

public class ComixDbHelper extends SQLiteOpenHelper{

    public static final String LOG_TAG=ComixDbHelper.class.getSimpleName();

    /** Name of the Database file */
    private static final String DATABASE_NAME="items.db";

    /** Database version. If schema is changed DB version must be incremented */
    private static final int DATABASE_VERSION=1;

    /** Drop Existing Table String */
    private static final String SQL_DELETE_ENTRIES=
            "DROP TABLE IF EXISTS " + TitleEntry.TABLE_NAME;

    /** input_TYPE strings for SQL Statement */
    private static final String TEXT_TYPE=" TEXT";

    private static final String INT_TYPE=" INTEGER";

    private static final String REAL_TYPE=" REAL";

    /** Primary Key AutoIncrement String w/ comma separator for SQL statement */
    private static final String PRI_KEY_AUTOINCR=" PRIMARY KEY AUTOINCREMENT, ";

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
                + TitleEntry._ID + INT_TYPE + PRI_KEY_AUTOINCR
                + TitleEntry.COLUMN_PRODUCT_NAME + TEXT_TYPE + " NOT NULL, "
                + TitleEntry.COLUMN_SUPPLIER + TEXT_TYPE + " NOT NULL, "
                + TitleEntry.COLUMN_SUPPLIER_PH + TEXT_TYPE + " NOT NULL, "
                + TitleEntry.COLUMN_PRICE + REAL_TYPE + " NOT NULL DEFAULT 0, "
                + TitleEntry.COLUMN_QTY + INT_TYPE + " NOT NULL DEFAULT 0, "
                + TitleEntry.COLUMN_SECTION + INT_TYPE + " NOT NULL );";

        Log.v(LOG_TAG + "Database",SQL_CREATE_ITEMS_TABLE);
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    /** This is called when the database needs to be upgraded. */
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        if (newVersion > oldVersion) {
            //Delete entries.
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);

        }
    }
}
