package com.example.danilo.carpics;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.gun0912.tedpicker.Config;
//import com.gun0912.tedpicker.ImagePickerActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import id.zelory.compressor.Compressor;

public class PictureActivity extends AppCompatActivity {


    private Button addPhotoBtn, choosePictureBtn, viewAllCars;
    private DatabaseHelper databaseHelper;
    private ImageDatabaseHelper imageDatabaseHelper;

    String carModel;
    String carMake;
    String carYear;
    String carColour;
    String carMileage;
    ImagePicker imagePicker;
    private static final int CAMERA_IMAGE_REQUEST = 101;
    private String imageName;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);


        //database helper
        databaseHelper = new DatabaseHelper(this);
        imageDatabaseHelper = new ImageDatabaseHelper(this);

        //get the intent of the cars info
        Intent getIntent = getIntent();
        carModel = getIntent.getExtras().get("model").toString();
        carMake = getIntent.getExtras().get("make").toString();
        carYear = getIntent.getExtras().get("year").toString();
        carColour = getIntent.getExtras().get("colour").toString();
        carMileage = getIntent.getExtras().get("mileage").toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
        }

        addPhotoBtn = (Button) findViewById(R.id.addImage);
        choosePictureBtn = (Button) findViewById(R.id.selectPhotos);
        viewAllCars = (Button) findViewById(R.id.viewCars);

        Toast.makeText(PictureActivity.this,
                carModel + " " + carMake + " " + carYear + " " + carColour + " " + carMileage, Toast.LENGTH_LONG).show();


        viewAllCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewCarsIntent = new Intent(PictureActivity.this, CarsActivity.class);
                startActivity(viewCarsIntent);
            }
        });

        choosePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //initialize the image picker with its credentials
                imagePicker = new ImagePicker.Builder(PictureActivity.this)
                        .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                        .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                        .directory(ImagePicker.Directory.DEFAULT)
                        .extension(ImagePicker.Extension.JPG)
                        .scale(600, 600)
                        .allowMultipleImages(true)
                        .enableDebuggingMode(true)
                        .build();

            }
        });


        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

    }

    //capture the image
    public void captureImage() {




        //create a new folder called autofare and add picture or insert picture if folder already created
        String imageFolderPath = Environment.getExternalStorageDirectory().toString()
                + "/AutoFare";
        // Creating image here
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        try
        {
            // Creating folders for Image

            File imagesFolder = new File(imageFolderPath);
            imagesFolder.mkdirs();

            // Generating file name
            imageName = new Date().toString() + ".png";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Eroorrrr", "captureImage: " + e.getMessage());
        }


        //open the camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imageFolderPath, imageName)));

        startActivityForResult(takePictureIntent,
                CAMERA_IMAGE_REQUEST);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {


        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_IMAGE_REQUEST) {

            Toast.makeText(this, "Success",
                    Toast.LENGTH_SHORT).show();

            //Scan new image added
            MediaScannerConnection.scanFile(this, new String[]{new File(Environment.getExternalStorageDirectory()
                    + "/AutoFare/" + imageName).getPath()}, new String[]{"image/png"}, null);


            // Work in few phones
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(Environment.getExternalStorageDirectory()
                        + "/AutoFare/" + imageName)));

            } else {
                this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(Environment.getExternalStorageDirectory()
                        + "/AutoFare/" + imageName)));
            }
        }
        //check if user is select images
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);


            Uri uri;
            Iterator<String> uriPaths = mPaths.iterator();
            CarModel newCar = new CarModel(carMake,carModel);
            //insert car into database
            try
            {
                databaseHelper.addNewCar(newCar);
            }
            catch (SQLiteException ex)
            {
                ex.printStackTrace();
                Log.d("INSERT ERROR", "onActivityResult: " + ex.getMessage());
            }

            //get last inserted id
            Cursor resC = databaseHelper.GetLastRow();
            String lastInsertID = resC.getString(resC.getColumnIndex(databaseHelper.KEYSTUID));

            //Toast.makeText(PictureActivity.this,lastInsertID,Toast.LENGTH_LONG).show();


           while(uriPaths.hasNext())
            {
                //get the bytes of all paths
                byte[] bytes = uriPaths.next().getBytes();
                String s = new String(bytes);

                //insert image to database with car id and image location
                imageDatabaseHelper.addImage(lastInsertID,bytes);

                //Toast.makeText(PictureActivity.this, s, Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }
}


