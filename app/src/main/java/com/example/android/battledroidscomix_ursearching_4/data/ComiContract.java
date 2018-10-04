package com.example.android.battledroidscomix_ursearching_4.data;

import android.provider.BaseColumns;

public final class ComiContract {

    // private empty constructor to prevent accidental instantiation of Contract Class.
    private ComiContract(){}

    /**
     * Inner class that defines constant values for the inventory database table.
     * Each entry in the table represents a single title.
     */
    public static final class TitleEntry implements BaseColumns{

        /**Name of database table for items*/
        public final static String TABLE_NAME="items";

        /**
         * Unique DB Table identifier number for the item (only used in DB Table)
         *
         * Type: INTEGER
         */
        public final static String _ID=BaseColumns._ID;

        /**
         * Item Title or Name
         *
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME="Product Name";

        /**
         * Supplier Name
         *
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER="Supplier Name";

        /**
         * Supplier Phone Number
         *
         * Type: INTEGER
         */
        public final static String COLUMN_SUPPLIER_PH="Supplier Phone Number";

        /**
         * Item Price
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PRICE="Item Price";

        /**
         * Quantity In-Stock
         *
         * Type: INTEGER
         */
        public final static String COLUMN_QTY="Quantity In-Stock";

    }
}
