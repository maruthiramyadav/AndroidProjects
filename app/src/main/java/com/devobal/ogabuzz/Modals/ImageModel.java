package com.devobal.ogabuzz.Modals;

import android.graphics.Bitmap;

/**
 * Created by Devobal on 3/14/2019 on 10:51 PM.
 */
public class ImageModel {

    String path;

    Bitmap bitmap;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
