package com.example.danilo.carpics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danilo on 2017-12-29.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    Context ctx;
    List<String> fullDataList;
    // Database Version
    private static final int DATABASE_VERSION = 7;

    // Database Name
    private static final String DATABASE_NAME = "CARPICS_DATABASE";

    // Table Names
    private static final String DB_TABLE = "CAR_TABLE";

    // column names
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_data";
    private static final String KEY_YEAR = "year";
    private static final String KEY_MAKE = "make";
    private static final String KEY_MODEL = "model";
    private static final String KEY_COLOUR = "colour";
    private static final String KEY_STUID = "_id";
    private static final String KEY_MILEAGE = "mileage";

    public String KEYMODEL = KEY_MODEL;
    public String KEYMAKE = KEY_MAKE;
    public String KEYSTUID = KEY_STUID;

    // Table create statement
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE "
            + DB_TABLE + "(" + KEY_STUID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_MODEL + " TEXT," +
            KEY_MAKE + " TEXT" + ")";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating table
        try
        {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            db.execSQL(CREATE_TABLE_IMAGE);
            Log.d("TABLE CREATED", "onCreate: " + DB_TABLE + " created");
        }
        catch(Exception e)
        {
            Log.d("Error", "onCreate: " + e.getMessage());
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        // create new table
        onCreate(db);
    }

    //get last row
    public Cursor GetLastRow()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + DB_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();

        return cursor;
    }

    //insert a new car
    public void addNewCar(CarModel car) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put(KEY_NAME,    car.carImage);

        cv.put(KEY_MAKE,    car.getMake());
        cv.put(KEY_MODEL,    car.getModel());

        try {
            database.insert( DB_TABLE, null, cv );
            Toast.makeText(ctx,"Car Added",Toast.LENGTH_LONG).show();
        }
        catch (SQLiteException ex)
        {
            ex.printStackTrace();
            Log.d("INSERT EROOR", "addNewCar: " + ex.getMessage());
        }

    }

    //get all rows from the table
    public Cursor GetAllRows()
    {
        String where = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.query(true, DB_TABLE, new String[]{KEY_STUID,KEY_MODEL,KEY_MAKE},where,null,null,null,null,null);
        if(c != null)
        {
            c.moveToFirst();
        }
        return c;
    }


}
