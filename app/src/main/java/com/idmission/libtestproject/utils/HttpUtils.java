package com.idmission.libtestproject.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sanketg on 14/02/19.
 */

public class HttpUtils {

    private static void transfer(InputStream inputStream,
                                 OutputStream outputStream, boolean closeStreamsWhenDone,
                                 int bufferSize) throws IOException {

        try {
            byte[] bytes = new byte[bufferSize];
            int bytesRead;

            while ((bytesRead = inputStream.read(bytes, 0, bytes.length)) > 0) {
                outputStream.write(bytes, 0, bytesRead);
            }
        } finally {
            if (closeStreamsWhenDone) {
                close(inputStream, outputStream);
                inputStream.close();
            }
        }
    }

    private static void close(Closeable... closeables) {
        if (closeables == null) {
            return;
        }

        for (Closeable c : closeables) {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
                Log.e("SDK", "Error - ", e);
            }
        }
    }

    private static byte[] readFully(InputStream in, boolean closeStreamsWhenDone) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        transfer(in, out, closeStreamsWhenDone, 1024);
        return out.toByteArray();
    }


    public static String postDataApp(String requestXML, String urlStr) {

        try {
            byte[] bytes = requestXML.getBytes("UTF-8");

            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            conn.setReadTimeout(0);
            if (conn instanceof HttpsURLConnection) {
                // Add this part of you are facing issue with SSL handshake, but
                // prefer avoiding "NonCheckingSSLContext".
                HttpsURLConnection httpConn = (HttpsURLConnection) conn;
                httpConn.setSSLSocketFactory(SimpleSSLContextManager.getNonCheckingSSLContext().getSocketFactory());
                httpConn.setHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
                        return true;
                    }
                });
            }
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestProperty("content-type", "text/xml; charset=UTF-8");
            conn.setDoOutput(true);
            OutputStream outStr = conn.getOutputStream();
            outStr.write(bytes);
            outStr.close();

            InputStream inStr = conn.getInputStream();
            byte[] result = readFully(inStr, false);

            return new String(result);

        } catch (Throwable e) {
            return null;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean isInternetAvailable = false;

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && (networkInfo.isConnected()) && networkInfo.isAvailable()) {
                isInternetAvailable = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isInternetAvailable;
    }
}
