package com.idmission.libtestproject.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by sanketg on 14/02/19.
 */

public class CommonUtils {

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "";
    }

    public static String getWifiMacAddress(Context _context) {
        String macAddr;
        try {
            WifiManager wifiMan = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInf = wifiMan.getConnectionInfo();
            macAddr = wifiInf.getMacAddress();
        } catch (Exception e) {
            macAddr = null;
        }
        if (macAddr.equals("02:00:00:00:00:00")) {
            return getMacAddress();
        }
        return macAddr;
    }

    public static enum LANGUAGE {
        EN, ES, MY
    }


    public static void updateLanguage(Context baseContext, String language) {
        try {
            if (null != baseContext && !StringUtil.isEmpty(language)) {
                String languageToLoad = language.toLowerCase();
                if (isLanguageSupported(languageToLoad)) {
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    baseContext.getResources().updateConfiguration(config, baseContext.getResources().getDisplayMetrics());
                }
            }
        } catch (Exception e) {
            Log.d("SDK", "LanguageUtils.updateLanguage : " + e);
        }
    }

    public static boolean isLanguageSupported(String language) {
        for (CommonUtils.LANGUAGE c : CommonUtils.LANGUAGE.values()) {
            if (c.name().equalsIgnoreCase(language)) {
                return true;
            }
        }
        return false;
    }

    public static String readStringFromAssetFile(Context _context, String filename) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = _context.getAssets().open(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
