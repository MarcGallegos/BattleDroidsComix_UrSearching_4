package com.example.android.battledroidscomix_ursearching_4.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ComiContract {

    // private empty constructor to prevent accidental instantiation of Contract Class.
    private ComiContract(){}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY="com.example.android.battledroidscomix_ursearching_4";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.BattledroidsComix_Ursearching_4/items/ is a valid path for
     * looking at item data. content://com.example.android.BattledroidsComix_Ursearching_4/items/.../ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "...".
     */
    public static final String PATH_ITEMS = "items";

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

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

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
