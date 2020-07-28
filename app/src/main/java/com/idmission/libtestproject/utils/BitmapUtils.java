package com.idmission.libtestproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

/**
 * Created by dipenp on 19/10/18.
 */

public class BitmapUtils {
    public static int IMAGE_SIZE = 0;

    public static void writeBitmapToFilepath(String filepath, Bitmap bitmap) {
        BufferedOutputStream bos = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filepath);
            bos = new BufferedOutputStream(fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 92, bos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.flush();
                bos.close();
            } catch (Exception e2) {
            }
        }
    }

    public static Bitmap getBitmapFromFile(String filepath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(filepath, bmOptions);
    }

    public static String bitmapToBase64(Bitmap bmp) {
        if (bmp == null || bmp.isRecycled())
            return "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos); // bm is the bitmap
        // object
        byte[] b = baos.toByteArray();
        String imageBase64 = Base64.encodeToString(b, Base64.DEFAULT);
        IMAGE_SIZE = imageBase64.length() / 1000;
        Log.d("", "bitmapToBase64 IMAGE_SIZE " + IMAGE_SIZE);
        return imageBase64;
    }

}
