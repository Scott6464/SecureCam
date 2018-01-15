package com.example.android.securecam;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET},
                1);
    }

    public void detectMotion(View v) {
        boolean motionDetected;
        MotionDetection md = new MotionDetection(this);
        if (md.detectMotion()){Toast.makeText(this, "Motion Detected", Toast.LENGTH_LONG).show();}
        else {Toast.makeText(this, "No Motion", Toast.LENGTH_LONG).show();}
    }

    public void sendReport(View v) {
        Thread t = new Thread() {
            @Override
            public void run() {
                makeGif();
                emailGif();
            }
        };
        t.start();
    }

    public void emailButton(View v) {
        Thread t = new Thread() {
            @Override
            public void run() {
                emailGif();
            }
        };
        t.start();
    }


    private void emailGif() {

        try {
            String pathForAppFiles = getFilesDir().getAbsolutePath() + "/output.gif"; //+ STILL_IMAGE_FILE;
            GMailSender sender = new GMailSender("ruddercontracting@gmail.com", "croutons");
            sender.sendMail("SecureCam daily digest",
                    "Today's Images",
                    "ruddercontracting@gmail.com",
                    "sengle64@gmail.com",
                    pathForAppFiles);
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Email Sent", Toast.LENGTH_SHORT).show();
                }
            });
            Log.i("hi", "email sent");
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Email Not Sent", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void makeGif() {
        String path = getFilesDir().getAbsolutePath();
        AnimatedGIFWriter writer = new AnimatedGIFWriter(true);
        try {
            OutputStream os = new FileOutputStream(path + "/output.gif");
            Bitmap[] bitmap = new Bitmap[3];
            int[] delays = {1000, 1000, 1000};
            bitmap[0] = getResizedBitmap(rotateImage(BitmapFactory.decodeStream(new FileInputStream(path + "/0.jpg")), 90), 320);
            bitmap[1] = getResizedBitmap(rotateImage(BitmapFactory.decodeStream(new FileInputStream(path + "/1.jpg")), 90), 320);
            bitmap[2] = getResizedBitmap(rotateImage(BitmapFactory.decodeStream(new FileInputStream(path + "/2.jpg")), 90), 320);

            writer.writeAnimatedGIF(bitmap, delays, os);
            Toast.makeText(getApplicationContext(), "Gif generated.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(e.toString(), e.getMessage());
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
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


    public void takePicture(View v) {
        startActivity(new Intent(getApplicationContext(), ImageCaptureActivity.class));
    }

    public void showPicture(View v) {
        startActivity(new Intent(getApplicationContext(), ImageDisplayActivity.class));
    }
}




