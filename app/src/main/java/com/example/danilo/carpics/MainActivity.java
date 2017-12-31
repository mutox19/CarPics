package com.example.danilo.carpics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    EditText model, make, year, colour, mileage;
    public Button changeImageBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set all the variables

        model = (EditText) findViewById(R.id.model);
        make = (EditText)findViewById(R.id.make);
        year = (EditText)findViewById(R.id.year);
        colour = (EditText)findViewById(R.id.colour);
        mileage = (EditText)findViewById(R.id.mileage);

        changeImageBtn = (Button)findViewById(R.id.addPhoto);
        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get the text of from the edittext
                String carModel = model.getText().toString();
                String carMake = make.getText().toString();
                String carYear = year.getText().toString();
                String carColour = colour.getText().toString();
                String carMileage = mileage.getText().toString();

                //create an intent
                Intent pictureIntent = new Intent(MainActivity.this, PictureActivity.class);
                //pass variables to pictureIntent
                pictureIntent.putExtra("model", carModel);
                pictureIntent.putExtra("make", carMake);
                pictureIntent.putExtra("year", carYear);
                pictureIntent.putExtra("colour", carColour);
                pictureIntent.putExtra("mileage", carMileage);
                startActivity(pictureIntent);
            }
        });
    }
}
