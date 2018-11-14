package com.example.android.battledroidscomix_ursearching_4.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.battledroidscomix_ursearching_4.R;
import com.example.android.battledroidscomix_ursearching_4.data.ComiContract;

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor c) {
        super(context,c,0);
    }

    /**
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

    @Override
    public void bindView(View view, final Context context, final Cursor cursor){
        TextView product_name = view.findViewById(R.id.product_name);

        String name = cursor.getString(cursor.getColumnIndex(ComiContract.TitleEntry.COLUMN_PRODUCT_NAME));
        product_name.setText(name);
    }

}
