package com.idmission.libtestproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

/**
 * Created by dipenp on 10/10/18.
 */

public class PreferenceUtils {

    private static final String SDK_SETTINGS = "SDKSettings";

    public static void setPreference(Context _context, String key, String value) {
        SharedPreferences sharedpreferences = _context.getSharedPreferences(SDK_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPreference(Context _context, String key, String def) {
        SharedPreferences sharedpreferences = _context.getSharedPreferences(SDK_SETTINGS, Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, def);
    }

    public static void setPreference(Context _context, String key, boolean value) {
        SharedPreferences sharedpreferences = _context.getSharedPreferences(SDK_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getPreference(Context _context, String key, boolean def) {
        SharedPreferences sharedpreferences = _context.getSharedPreferences(SDK_SETTINGS, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(key, def);
    }

}