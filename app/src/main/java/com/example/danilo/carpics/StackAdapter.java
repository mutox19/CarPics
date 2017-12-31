package com.example.danilo.carpics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/**
 * Created by Danilo on 2017-12-30.
 */

public class StackAdapter extends ArrayAdapter<StackItem> {

    private List<StackItem> items;
    private Context context;

    public StackAdapter(Context context, int textViewResourceId,
                        List<StackItem> objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = layoutInflater.inflate(R.layout.imageslayout, null);
        }
        //point the stack item to the specific stack
        StackItem stackItem = items.get(position);
        if (stackItem != null) {
            // TextView
            TextView textView = (TextView) itemView.findViewById(R.id.textView);
            // ImageView
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);


            if (textView != null) {
                textView.setText(stackItem.getItemText());

                imageView.setImageBitmap(GetPath(stackItem.getImageName()));
                imageView.setBackgroundColor(Color.rgb(211,204,188));
            }

        }
        return itemView;
    }

    //get the path of the image
    public Bitmap GetPath(String path)
    {
        File imgFile = new File(path);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            return myBitmap;

        }
        return null;
    }


}
