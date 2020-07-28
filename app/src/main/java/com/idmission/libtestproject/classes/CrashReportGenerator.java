package com.idmission.libtestproject.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.builder.ReportBuilder;
import org.acra.collector.ConfigurationCollector;
import org.acra.collector.CrashReportData;
import org.acra.collector.CrashReportDataFactory;
import org.acra.config.ACRAConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by dipenp on 15/02/19.
 */

public class CrashReportGenerator {


    private static final String TAG = "CrashReportGenerator";
    public static final String NOT_AVAILABLE = "Not Available";
    private static final String SERVER_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";

    //private String appCode, webviewURL, wifiMac, imei;
	private String appCode, environemntURL, wifiMac, companyID, password, flavour, loginID,osVersion,IMEINumber,deviceModel;
    private Context context;

	public CrashReportGenerator(Context context, String appCode, String flavour, String loginID, String companyID, String environemntURL, String wifiMac,String IMEINumber,String osVersion,String deviceModel ) {
		this.context = context;
		this.appCode = appCode;
		this.environemntURL = environemntURL;
		this.wifiMac = wifiMac;
		this.companyID = companyID;
		this.flavour = flavour;
		this.loginID = loginID;
		this.osVersion =osVersion;
		this.IMEINumber=IMEINumber;
		this.deviceModel =deviceModel;
		password=null;
//		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		StrictMode.setThreadPolicy(policy);
	}

    public String generateCrashReportFile(Thread thread, Throwable ex, String filepath) {

        File crashReportFile = new File("");
        if (!StringUtils.isEmpty(filepath)) {
            crashReportFile = new File(filepath.toString());

            if (crashReportFile.exists()) {
                if (!crashReportFile.delete()) {
                    Log.i(TAG, "could not delete file.");
                    return null;
                }
            }

            try {
                crashReportFile.createNewFile();
            } catch (IOException e) {
                Log.i(TAG, "could not create file.");
                e.printStackTrace();
                return null;
            }
        }

        // Store the initial Configuration state.
        final String initialConfiguration = ConfigurationCollector.collectConfiguration(context);

        //ACRA preferrences
        final SharedPreferences prefs = ACRA.getACRASharedPreferences();

        // Sets the application start date.
        // This will be included in the reports, will be helpful compared to
        // user_crash date.
        final Calendar appStartDate = new GregorianCalendar();
        ACRAConfiguration acraConfig = ACRA.getConfig();

        CrashReportDataFactory crashReportDataFactory = new CrashReportDataFactory(context, acraConfig, prefs, appStartDate, initialConfiguration);
        ReportBuilder reportBuilder = new ReportBuilder();
        CrashReportData crashReportData = crashReportDataFactory.createCrashData(reportBuilder);

        if (!writeCrashReportFieldValues(crashReportData, crashReportFile)) {
            return null;
        }

        return crashReportFile.getAbsolutePath();
    }

    private boolean writeCrashReportFieldValues(CrashReportData crashReportData, File crashReportFile) {

        //Server parses in this sequence.
        ReportField[] requiredFields = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.PACKAGE_NAME,
                ReportField.PHONE_MODEL,
                ReportField.BRAND,
                ReportField.PRODUCT,
                ReportField.ANDROID_VERSION,
                ReportField.BUILD,
                ReportField.TOTAL_MEM_SIZE,
                ReportField.AVAILABLE_MEM_SIZE,
                ReportField.CUSTOM_DATA,
                ReportField.STACK_TRACE,
                ReportField.INITIAL_CONFIGURATION,
                ReportField.CRASH_CONFIGURATION,
                ReportField.DISPLAY,
                ReportField.USER_APP_START_DATE,
                ReportField.USER_CRASH_DATE,
                ReportField.DUMPSYS_MEMINFO,
                ReportField.LOGCAT,
                ReportField.DEVICE_ID,
                ReportField.INSTALLATION_ID,
                ReportField.DEVICE_FEATURES,
                ReportField.ENVIRONMENT,
                ReportField.SHARED_PREFERENCES,
                ReportField.SETTINGS_SYSTEM,
                ReportField.SETTINGS_SECURE
        };

        StringBuilder sb = new StringBuilder();

        final String SEPERATOR = "##";

        // Append Time Stamp
        Date currentTime = new Date();
        String timeStamp = DateFormatUtils.format(currentTime, SERVER_TIME_PATTERN);
        sb.append(timeStamp).append(SEPERATOR);


        for (ReportField field : requiredFields) {
            String value = crashReportData.get(field);
            if (!TextUtils.isEmpty(value)) {
                value = value.replace(SEPERATOR, "$$");
                //If field is is some date, convert to date format used by IDS.
                if (field.name().equalsIgnoreCase("USER_APP_START_DATE") ||
                        field.name().equalsIgnoreCase("USER_CRASH_DATE")) {
                    value = convertDateToServerFormat(value);
                }
                sb.append(value).append(SEPERATOR);
            } else {
                sb.append("Not Available").append(SEPERATOR);
            }

        }

		// Append app_code
		sb.append(StringUtils.isEmpty(appCode)?NOT_AVAILABLE :appCode).append(SEPERATOR);

		// Append 5 custom fields.

		//CUSTOME_1 = Flavour
		sb.append(StringUtils.isEmpty(flavour)?NOT_AVAILABLE :flavour).append(SEPERATOR);

		//CUSTOME_2 = Username
		sb.append(StringUtils.isEmpty(loginID)?NOT_AVAILABLE :loginID).append(SEPERATOR);

		//CUSTOM_3 = Company ID
		sb.append((StringUtils.isEmpty(companyID)|| companyID.equalsIgnoreCase("0"))?NOT_AVAILABLE :companyID).append(SEPERATOR);

		//CUSTOM_4 = Environemnt URL
		sb.append(StringUtils.isEmpty(environemntURL)?NOT_AVAILABLE :environemntURL).append(SEPERATOR);

		//CUSTOM_5 = Wifi_Mac
		sb.append(StringUtils.isEmpty(wifiMac)?NOT_AVAILABLE :wifiMac).append(SEPERATOR);

		//CUSTOM_6 = IMEI_Number
		sb.append(StringUtils.isEmpty(IMEINumber)?NOT_AVAILABLE :IMEINumber).append(SEPERATOR);

		//CUSTOM_7 = OS_Version
		sb.append(StringUtils.isEmpty(osVersion)?NOT_AVAILABLE :osVersion).append(SEPERATOR);

		//CUSTOM_8 = Device_Model
		sb.append(StringUtils.isEmpty(deviceModel)?NOT_AVAILABLE :deviceModel);

        BufferedWriter br = null;
        try {
            // Don't forget to open it for append mode.
            br = new BufferedWriter(new FileWriter(crashReportFile, true));
            br.append(sb.toString());
            br.newLine();
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        return true;
    }

    private String convertDateToServerFormat(String date) {
        int plus_index = date.indexOf('+');
        if (plus_index != -1) {
            date = date.substring(0, 19); //Stripping off extra after dd/MM/yyyy HH:mm:ss
        }

        try {
            Date d = DateUtils.parseDate(date, new String[]{DateFormatUtils.ISO_DATETIME_FORMAT.getPattern()});
            SimpleDateFormat sdf = new SimpleDateFormat(SERVER_TIME_PATTERN);
            return sdf.format(d);
        } catch (ParseException e1) {
            e1.printStackTrace();
            return null;
        }
    }
}
