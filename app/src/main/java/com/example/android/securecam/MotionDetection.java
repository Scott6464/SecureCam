package com.example.android.securecam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;

/**
 * Created by Scott on 1/14/2018.
 */

public class MotionDetection {

    String path;
    Bitmap[] bitmaps;

    public MotionDetection(Context c) {
        path = c.getApplicationContext().getFilesDir().getAbsolutePath();
        getImages();
    }

    public boolean detectMotion() {
        return comparePixels(bitmaps[0], bitmaps[1]);
    }

    private boolean comparePixels(Bitmap b0, Bitmap b1) {
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




    private void getImages() {
        try {
            bitmaps = new Bitmap[3];
            int[] delays = {1000, 1000, 1000};
            bitmaps[0] = resizeImages(BitmapFactory.decodeStream(new FileInputStream(path + "/0.jpg")), 9);
            bitmaps[1] = resizeImages(BitmapFactory.decodeStream(new FileInputStream(path + "/1.jpg")), 9);
            bitmaps[2] = resizeImages(BitmapFactory.decodeStream(new FileInputStream(path + "/2.jpg")), 9);
        } catch (Exception e) {
            Log.e(e.toString(), e.getMessage());
        }
    }


    public Bitmap resizeImages(Bitmap image, int maxSize) {
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




