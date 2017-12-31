package com.example.danilo.carpics;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danilo on 2017-12-29.
 */

public class ImageDatabaseHelper extends SQLiteOpenHelper {

    Context ctx;
    List<String> fullDataList;
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "IMAGE_DATABASE";

    // Table Names
    private static final String DB_TABLE = "IMAGE_TABLE";

    private static final String KEY_IMAGE = "image_data";
    private static final String KEY_CARID = "carUID";

    // Table create statement
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + DB_TABLE + "("+
            KEY_CARID + " TEXT," +
            KEY_IMAGE + " BLOB);";


    public ImageDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
        fullDataList = new ArrayList<String>();
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
    //insert all new images
    public void addImage(String carId, byte[] image) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_CARID,    carId);
        cv.put(KEY_IMAGE,   image);
        database.insert( DB_TABLE, null, cv );
    }

//get the images for a specific car
    public List<String> GetCarImages(String carID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM "+DB_TABLE+" WHERE "+KEY_CARID +" ='"+carID+"'";
        Cursor imageCursor = db.rawQuery(selectQuery, null);


        if (imageCursor.moveToFirst()) {
            while (!imageCursor.isAfterLast()) {
                //String carModel = cursor.getString(cursor.getColumnIndex(KEY_MODEL));
                //String carMake = cursor.getString(cursor.getColumnIndex(KEY_MAKE));
                //String carYear = cursor.getString(cursor.getColumnIndex(KEY_YEAR));
                //String carColor= cursor.getString(cursor.getColumnIndex(KEY_COLOUR));

                byte[] img = imageCursor.getBlob(imageCursor.getColumnIndex(KEY_IMAGE));
                String carImage = new String(img);

                //fullDataList.add(carModel);
                //fullDataList.add(carMake);
                //fullDataList.add(carYear);
                //fullDataList.add(carColor);
                fullDataList.add(carImage);
                imageCursor.moveToNext();
            }
        }

        return fullDataList;

    }

}
