package com.example.danilo.carpics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.StackView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CarImageDisplayActivity extends AppCompatActivity {

    private StackView stackView;
    private Button buttonPrevious;
    private Button buttonNext;
    private TextView carInfo;

    private List<String> IMAGE_NAMES;
    ArrayList<Bitmap> bitmapArray;

    ImageDatabaseHelper imageDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_image_display);

        //database helper
        imageDatabaseHelper = new ImageDatabaseHelper(this);

        //set variables
        carInfo = (TextView)findViewById(R.id.carInfo);
        Intent getIntent = getIntent();
        String carID;
        if(getIntent.getExtras().getString("itemUID") != null)
        {
            carID = getIntent.getExtras().getString("itemUID");
            IMAGE_NAMES = imageDatabaseHelper.GetCarImages(carID);
        }
        else
        {
            Toast.makeText(CarImageDisplayActivity.this,"Sorry Could not find images for car",Toast.LENGTH_SHORT).show();
        }
        //check to see if there is a itemMake and itemModel intent
        String carDetails = "";
        if(getIntent.getExtras().getString("itemMake") != null && getIntent.getExtras().getString("itemModel") != null)
        {
            carDetails = getIntent.getExtras().getString("itemMake") + " " + getIntent.getExtras().getString("itemModel");
            carInfo.setText(carDetails);
        }

        //set the stack and the next and previous buttons
        this.stackView = (StackView) findViewById(R.id.stackView);
        this.buttonNext = (Button) findViewById(R.id.button_next);
        this.buttonPrevious = (Button) findViewById(R.id.button_previous);

        List<StackItem> items = new ArrayList<StackItem>();

        //loop through all the image names
        for (String imageName : IMAGE_NAMES) {

            items.add(new StackItem( carDetails, imageName));
        }

        //set the stack adapter with all the items
        StackAdapter adapt = new StackAdapter(this, R.layout.imageslayout, items);
        stackView.setAdapter(adapt);
        stackView.setHorizontalScrollBarEnabled(true);
        stackView.setBackgroundColor(Color.rgb(230, 255, 255));

        buttonNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stackView.showNext();
            }
        });

        buttonPrevious.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                stackView.showPrevious();
            }
        });
    }


   /* public void bitMapArray()
    {
        bitmapArray = new ArrayList<Bitmap>();
        for (String imageName : IMAGE_NAMES) {
            //items.add(new StackItem(imageName + ".png", imageName));
            //Bitmap myBitMap = StringToBitMap(imageName);
            //bitmapArray.add(myBitMap);

        }

    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
*/

}


