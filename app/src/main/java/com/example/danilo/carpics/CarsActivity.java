package com.example.danilo.carpics;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class CarsActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);

        //database helper
        databaseHelper = new DatabaseHelper(this);

        //set the list view
        myList = (ListView) findViewById(R.id.carsListView);

        //populate the list view with the cars database
        PopulateListView();


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {


                Cursor c = (Cursor) myList.getItemAtPosition(position);
                //set cursor to specific columns
                String itemMake = c.getString(c.getColumnIndex(databaseHelper.KEYMAKE));
                String itemModel = c.getString(c.getColumnIndex(databaseHelper.KEYMODEL));
                String itemUID = c.getString(c.getColumnIndex(databaseHelper.KEYSTUID));
                Intent carImageDisplayIntent = new Intent(CarsActivity.this,CarImageDisplayActivity.class);

                //pass variables to carImageDisplay activity
                carImageDisplayIntent.putExtra("itemUID",itemUID);
                carImageDisplayIntent.putExtra("itemMake",itemMake);
                carImageDisplayIntent.putExtra("itemModel",itemModel);
                Toast.makeText(CarsActivity.this,"You selected : " + itemMake + itemModel,Toast.LENGTH_SHORT).show();
                startActivity(carImageDisplayIntent);
            }
        });
    }

    private void PopulateListView()
    {
        //get all the rows
        Cursor res = databaseHelper.GetAllRows();
        //use the columns names from the database
        String[] fromFieldNames = new String[]{databaseHelper.KEYMAKE, databaseHelper.KEYMODEL};
        int[] toViewIDS = new int[]{R.id.carMakeTxt, R.id.carModelTxt};
        //set up a simple cursor
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.carslayout,
                res,
                fromFieldNames,
                toViewIDS, 1);

        myList.setAdapter(myCursorAdapter);
    }
}
