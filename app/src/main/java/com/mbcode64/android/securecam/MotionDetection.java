package com.mbcode64.android.securecam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by Scott on 1/14/2018.
 */

public class MotionDetection {

    String path;
    Bitmap[] bitmaps;

    public MotionDetection(Context c) {

    }


    public boolean detectMotion(Bitmap b0, Bitmap b1) {
        b0 = resizeImage(b0,9);
        b1 = resizeImage(b1,9);
        for (int x=0; x<3; x++) {
            for(int y=0; y<3; y++) {
                int pixel = b0.getPixel(x, y);
                int pixel1 = b1.getPixel(x, y);
                Log.i("pixels " + Integer.toString(Color.red(pixel)), Integer.toString(Color.red(pixel1)));
                if (Math.abs(Color.red(pixel) - Color.red(pixel1)) > 10){return true;}
                if (Math.abs(Color.blue(pixel) - Color.blue(pixel1)) > 10){return true;}
                if (Math.abs(Color.green(pixel) - Color.green(pixel1)) > 10){return true;}
            }
        }
        return false;
    }



    public Bitmap resizeImage(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}




