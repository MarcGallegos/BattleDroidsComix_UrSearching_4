package com.example.android.battledroidscomix_ursearching_4.Adapters;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.battledroidscomix_ursearching_4.R;
import com.example.android.battledroidscomix_ursearching_4.data.ComiContract;
import com.example.android.battledroidscomix_ursearching_4.data.ComiContract.TitleEntry;

// CursorAdapter is an adapter for Grid/ListView that uses a Cursor of Item Data as it's data source
// This adapter knows how to create list items for each row of data in the Cursor.
public class ItemCursorAdapter extends CursorAdapter {

    //Global Variables used by Sale Button
    private int quantityVar;
    private int itemId;

    public ItemCursorAdapter(Context context, Cursor c) {
        super(context,c,0);
    }

    /** Constructs a new ItemCursorAdapter
     *
     * @param context app context
     * @param cursor  The cursor desired to retrieve data from, already in position
     * @param parent  Parent view new View is attached to
     * @return the newly created list item View
     */
    @Override
    public View newView(Context context, Cursor cursor, @NonNull ViewGroup parent) {

        //Return List item View.
        return LayoutInflater.from(context).inflate(R.layout.item_construct, parent, false);
    }

    /**
     * This method binds the item data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current item can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor){

        TextView product_name = view.findViewById(R.id.product_name);
        TextView supplier_name = view.findViewById(R.id.supplier_name);
        TextView supplier_ph = view.findViewById(R.id.supplier_ph);
        TextView product_price = view.findViewById(R.id.product_price);
        TextView product_quantity = view.findViewById(R.id.product_quantity);
        TextView product_section = view.findViewById(R.id.product_section);
        Button saleButton = view.findViewById(R.id.sale);

        String name = cursor.getString(cursor.getColumnIndex(ComiContract.TitleEntry.COLUMN_PRODUCT_NAME));
        String supplier = cursor.getString(cursor.getColumnIndex(ComiContract.TitleEntry.COLUMN_SUPPLIER));
        String reorder_ph = cursor.getString(cursor.getColumnIndex(ComiContract.TitleEntry.COLUMN_SUPPLIER_PH));
        String price = cursor.getString(cursor.getColumnIndex(ComiContract.TitleEntry.COLUMN_PRICE));
        String quantity = cursor.getString(cursor.getColumnIndex(ComiContract.TitleEntry.COLUMN_QTY));
        String section = cursor.getString(cursor.getColumnIndex(ComiContract.TitleEntry.COLUMN_SECTION));

        product_name.setText("Item: " + name);
        supplier_name.setText("Supplier: " + supplier);
        supplier_ph.setText("Contact: " + reorder_ph);
        product_price.setText("Price: " + price);
        product_quantity.setText("In-Stock: " + quantity);
        product_section.setText("Section:" + section);

        //Sale Button Listener Decrements Quantity
        // Find position cursor is at
        final  int position = cursor.getPosition();

        //Set onClickListener for Sold Button to reflect a sale
        saleButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                //Set cursor to position of the button selected.
                cursor.moveToPosition(position);

                //Get item _ID for current row
                itemId = cursor.getInt(cursor.getColumnIndex(TitleEntry._ID));
                quantityVar = cursor.getInt(cursor.getColumnIndex(TitleEntry.COLUMN_QTY));

                //IF quantity is greater than 0 decrease by 1 and update database and swap cursor out
                if (quantityVar > 0){
                    quantityVar = quantityVar -1;
                    ContentValues values = new ContentValues();
                    values.put(TitleEntry.COLUMN_QTY, quantityVar );

                    Uri updateUri = ContentUris.withAppendedId(TitleEntry.CONTENT_URI, itemId);
                    context.getContentResolver().update(updateUri, values, null, null);

                }
            }
        });
    }
}