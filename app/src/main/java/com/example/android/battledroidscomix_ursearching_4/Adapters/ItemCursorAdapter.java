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

import com.example.android.battledroidscomix_ursearching_4.R;

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    /**
     *
     * @param context app context
     * @param cursor  The cursor desired to retrieve data from, already in position
     * @param parent  Parent view new View is attached to
     * @return the newly created list item View
     */
    @Override
    public View nuView(Context context, Cursor cursor, @NonNull ViewGroup parent) {
        //Return List item View.
        return LayoutInflater.from(context).inflate(R.layout.item_construct, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursorData){
        //Find Sale Button
        Button saleButton = view.findViewById(R.id.sale_btn);

        
    }
}
