package com.example.android.securecam;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Scott on 12/14/2017.
 */

public class MovieMaker {

    public MovieMaker(Context c) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setDelay(500);  // ディレイ 500/ms
        encoder.setRepeat(0);   // 0:ループする -1:ループしない
        encoder.start(bos);     // gitデータ生成先ををbosに設定
        //String path = c.getApplicationContext().getFilesDir().getAbsolutePath();
        String path = c.getFilesDir().getAbsolutePath();
        try {

            Bitmap bmp1, bmp2, bmp3;
            // ファイルの読み込み
            bmp1 = BitmapFactory.decodeStream(new FileInputStream(path + "/0.jpg"));
            encoder.addFrame(bmp1);  // gifに追加
            bmp1.recycle();

            bmp2 = BitmapFactory.decodeStream(new FileInputStream(path + "/1.jpg"));
            //encoder.addFrame(bmp2);  // gifに追加
            bmp2.recycle();

            bmp3 = BitmapFactory.decodeStream(new FileInputStream(path + "/2.jpg"));
            //encoder.addFrame(bmp3);  // gifに追加
            bmp3.recycle();

        } catch (FileNotFoundException e) {
            Log.e(e.getMessage(), e.toString());
        }
        encoder.finish();  // 終了
/*        File filePath = new File(path, "sample.gif");
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(filePath);
            // bosに生成されたgifデータをファイルに吐き出す
            outputStream.write(bos.toByteArray());
        } catch (FileNotFoundException e) {
            Log.e(e.getMessage(), e.toString());
        } catch (IOException e) {
            Log.e(e.getMessage(), e.toString());
        }

*/    }
}