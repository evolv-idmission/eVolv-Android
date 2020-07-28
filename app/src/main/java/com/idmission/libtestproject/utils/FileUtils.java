package com.idmission.libtestproject.utils;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by dipenp on 19/10/18.
 */

public class FileUtils {

    public static void createDir(String filePath) {
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void cleanDir(String filePath) {

        File dir = new File(filePath);

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (null != files) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }

    public static String getStringFromFile(String filePath) {

        InputStream in = null;
        BufferedReader reader = null;
        StringBuilder out = new StringBuilder();

        try {
            File file = new File(filePath);
            in = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return out.toString();
    }

    public static void writeStringToFile(String filename, String text) {

        if (text.isEmpty())
            return;

        try {
            PrintWriter out = new PrintWriter(filename);
            out.print(text);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(String file) {
        deleteFile(new File(file));
    }

    /**
     * Delete File
     *
     * @param file
     */
    public static void deleteFile(File file) {
        try {
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void convertStringToMp3File(String filePath, String audioDataString) {

        try {
            byte[] decoded = Base64.decode(audioDataString, Base64.DEFAULT);
            try {
                File file2 = new File(filePath);
                FileOutputStream os = new FileOutputStream(file2, true);
                os.write(decoded);
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
