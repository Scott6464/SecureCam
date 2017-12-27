package com.example.android.securecam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;


public class ImageDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        File imgFile = new File(getFilesDir().getAbsolutePath() + "/0.jpg");
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView myImage = (ImageView) findViewById(R.id.photo);
            myImage.setImageBitmap(myBitmap);
        }
        else {
            Toast.makeText(this, "Couldn't find photo.", Toast.LENGTH_LONG).show();
        }
    }

}
