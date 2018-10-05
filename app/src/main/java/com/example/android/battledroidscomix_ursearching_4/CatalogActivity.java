package com.example.android.battledroidscomix_ursearching_4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.battledroidscomix_ursearching_4.data.ComixDbHelper;

public class CatalogActivity extends AppCompatActivity {

    //database helper provides access to database
    private ComixDbHelper mComixDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // To access our database, we instantiate our SQLiteOpenHelper subclass and pass the
        // context, which is the current activity.
        mComixDbHelper=new ComixDbHelper(this);

    }


}
