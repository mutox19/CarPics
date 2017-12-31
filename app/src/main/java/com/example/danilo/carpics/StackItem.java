package com.example.danilo.carpics;

import android.graphics.Bitmap;

/**
 * Created by Danilo on 2017-12-30.
 */

public class StackItem {

    private String itemText;
    private String imageName;

    public StackItem(String text, String imageName) {
        this.imageName = imageName;
        this.itemText = text;
    }

    public String getImageName() {
        return imageName;
    }


    public String getItemText() {
        return itemText;
    }
}
