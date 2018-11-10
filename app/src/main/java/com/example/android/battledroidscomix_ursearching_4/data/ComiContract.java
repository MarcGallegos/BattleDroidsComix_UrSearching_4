package com.example.android.battledroidscomix_ursearching_4.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class ComiContract {

    /**
     * URI CONTENT_AUTHORITY String Constant
     */
    public static final String CONTENT_AUTHORITY="com.example.android.battledroidscomix_ursearching_4";
    /**
     * URI Scheme String Constant
     */
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * URI PATH_Tablename Constant
     */
    public static final String PATH_ITEMS = "items";

    // private empty constructor to prevent accidental instantiation of Contract Class.
    private ComiContract(){}

    /**
     * Inner class that defines constant values for the inventory database table.
     * Each entry in the table represents a single title.
     */
    public static final class TitleEntry implements BaseColumns{

        /**Returns whether section is Misc, Action, Manga, Horror, Drama, Fantasy, or Sci Fi
         */
        public static boolean isValidSection(int section) {
            if (section == MISC_MERCH || section == ACTION || section == MANGA
                || section == HORROR || section == DRAMA || section == FANTASY
                    || section == SCI_FI){
                return true;
            }
            return false;
        }

        /** The content URI to access the item data in the ComixProvider*/
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_ITEMS );

        /**Name of database table for items*/
        public final static String TABLE_NAME="items";

        /**Unique DB Table identifier number for the item (only used in DataBase Table)
         * Type: INTEGER
         */
        public final static String _ID=BaseColumns._ID;

        /**Item Title or Name
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME="product_name";

        /**Supplier Name
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER="supplier_name";

        /**Supplier Phone Number
         * Type: INTEGER
         */
        public final static String COLUMN_SUPPLIER_PH="supplier_phone_number";

        /**Item Price
         * Type: INTEGER
         */
        public final static String COLUMN_PRICE="item_price";

        /**Quantity In-Stock
         * Type: INTEGER
         */
        public final static String COLUMN_QTY="quantity";

        /**Section or Genre
         * Type: INTEGER
         */
        public final static String COLUMN_SECTION="section";
        //int Constants for section spinner
        public static final int MISC_MERCH = 0;
        public static final int ACTION = 1;
        public static final int MANGA = 2;
        public static final int HORROR = 3;
        public static final int DRAMA = 4;
        public static final int FANTASY = 5;
        public static final int SCI_FI = 6;



    }
}
