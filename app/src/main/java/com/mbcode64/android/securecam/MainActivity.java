package com.mbcode64.android.securecam;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    @SuppressWarnings("deprecation")

    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET}, 1);
        setEmailAlarm();
    }

    public void setEmailAlarm() {
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, EmailGif.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // Set the alarm to start at 21:32 PM
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        calendar.set(Calendar.MINUTE, 05);
        calendar.add(Calendar.DATE,1);

// setRepeating() lets you specify a precise custom interval--in this case,
// 1 day
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
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
            File directory = new File(path + "/");
            File[] files = directory.listFiles();
            List<Bitmap> bitmap = new ArrayList<>();
            for (File file : files) {
                String fileName = file.getName();
                Log.i("filename1 ", fileName);
                if (fileName.contains("jpg")) {
                    bitmap.add(getResizedBitmap(rotateImage(BitmapFactory.decodeStream
                            (new FileInputStream(path + "/" + fileName)), 90), 320));
                }
            }
            Bitmap[] bitmapArray = bitmap.toArray(new Bitmap[bitmap.size()]);
            int[] delayArray = new int[bitmapArray.length];
            for (int i = 0; i < delayArray.length; i++) {
                delayArray[i] = 1000;
            }

            writer.writeAnimatedGIF(bitmapArray, delayArray, os);
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


    public void startCamera(View v) {
        startActivity(new Intent(getApplicationContext(), ImageCaptureActivity.class));
    }

    public void viewPhotos(View v) {
        startActivity(new Intent(getApplicationContext(), GoogleDrive.class));
        }

}







