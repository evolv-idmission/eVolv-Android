package com.idmission.libtestproject.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.idmission.client.IdType;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.classes.CrashReportGenerator;
import com.idmission.libtestproject.classes.CustomizeUIConfigManager;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.FileUtils;
import com.idmission.libtestproject.utils.HttpUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;
import com.idmission.imageprocessing.AppitApp;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

@ReportsCrashes(formUri = "",
        customReportContent = {
                ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
                ReportField.PACKAGE_NAME, ReportField.PHONE_MODEL, ReportField.BRAND,
                ReportField.PRODUCT, ReportField.ANDROID_VERSION, ReportField.BUILD,
                ReportField.TOTAL_MEM_SIZE, ReportField.AVAILABLE_MEM_SIZE,
                ReportField.CUSTOM_DATA, ReportField.STACK_TRACE,
                ReportField.INITIAL_CONFIGURATION, ReportField.CRASH_CONFIGURATION,
                ReportField.DISPLAY, ReportField.USER_APP_START_DATE,
                ReportField.USER_CRASH_DATE, ReportField.DUMPSYS_MEMINFO,
                ReportField.LOGCAT, ReportField.DEVICE_ID, ReportField.INSTALLATION_ID,
                ReportField.DEVICE_FEATURES, ReportField.ENVIRONMENT,
                ReportField.SHARED_PREFERENCES, ReportField.SETTINGS_SYSTEM,
                ReportField.SETTINGS_SECURE })
public class EVolvApp extends AppitApp {

    private static final String TAG = "EVolvApp";

    private String evolvBaseDirectoryPath = "";
    private String currentService = "";
    private String currentServiceName = "";
    private String currentServiceID = "";
    private String country = "";
    private String state = "";
    private String countrySecondary = "";
    private String stateSecondary = "";
    private String faceImageType = "";
    private String idImageType = "";
    private IdType idType;
    private IdType idTypeSecondary;
    private ArrayList<String> listAdditionalFeatures = new ArrayList<>();
    private JSONObject additonaldata = new JSONObject();
    private String locationLat = "";
    private String locationLong = "";
    private String snippetResult = "";
    private String videoResult = "";
    private String voiceResult = "";
    private String empCode = "";
    private JSONObject jsonObjAddressDetails = new JSONObject();
    private JSONObject jsonObjEmployeeData = new JSONObject();
    private JSONObject jsonObjCustomProduct = new JSONObject();
    private HashMap<String, String> resultFPMap = new HashMap<>();
    private HashMap<String, String> resultDeviceFPMap = new HashMap<>();
    private String overrideEmpCode = "";
    private String barcodeDetails = "";

    //Generate OTP param
    private String otpEmailId = "";
    private String otpMobileNumber = "";
    private String otpNotificationType = "";

    //Verify OTP param
    private String verifyOTPValue = "";

    private String cardDetails = "";

    private boolean isFormKeyClear = true;

    private boolean isSecondaryIdCapture = false;

    private static final String SERVER_URL = "https://lab.idmission.com:9043/IDS/service/ids/uploadCrashLog";
    private Thread.UncaughtExceptionHandler defaultUEH;

    public EVolvApp() {
        super();

        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }

    // UncaughtExceptionHandler listener
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            Log.d(TAG, "uncaughtException");
            Log.d(TAG, ":::#################### ACRA CRASH START ####################");
            ex.printStackTrace();
            Log.d(TAG, ":::##################### ACRA CRASH END #####################");

            String crashReportPath = getBaseDirectoryPath() + Constants.CRASH_REPORT_FILEPATH + System.currentTimeMillis() + ".txt";
            CrashReportGenerator crashReportGenerator = new CrashReportGenerator(getApplicationContext(), "eVolv", "Evolv", PreferenceUtils.getPreference(getApplicationContext(), "LOGINID", ""), "", PreferenceUtils.getPreference(getApplicationContext(), "URL", getApplicationContext().getString(R.string.url)), getMacAddr(), getIMEINumber(), getOsVersion(), getDeviceModel());
            crashReportPath = crashReportGenerator.generateCrashReportFile(thread, ex, crashReportPath);

            System.exit(1);
            // re-throw critical exception further to the os (important)
            defaultUEH.uncaughtException(thread, ex);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        initAcra();

        FileUtils.createDir(getBaseDirectoryPath() + Constants.EVOLVE_BASE_FOLDER_PATH);
        FileUtils.createDir(getBaseDirectoryPath() + Constants.EVOLVE_IMAGE_FOLDER_PATH);
        FileUtils.createDir(getBaseDirectoryPath() + Constants.EVOLVE_REQ_RESP_FOLDER_PATH);
        FileUtils.createDir(getBaseDirectoryPath() + Constants.CRASH_REPORTS_PATH);

        CustomizeUIConfigManager.initCustomizeUIConfig(getApplicationContext());
    }

    /**
     * Initializing ACRA for crash report
     **/
    private void initAcra(){

        //Initializing ACRA
        ACRA.init(this);

        //Enable device id for report
        SharedPreferences acra_prefs = ACRA.getACRASharedPreferences();
        SharedPreferences.Editor acra_pref_editor = acra_prefs.edit();
        acra_pref_editor.putBoolean(ACRA.PREF_ENABLE_DEVICE_ID, true);
        acra_pref_editor.commit();
    }


    public synchronized void uploadAllCrashReports() {

        File dir = new File(getBaseDirectoryPath() + Constants.CRASH_REPORTS_PATH);

        if (dir.exists() && dir.isDirectory() && !(dir.listFiles() == null)) {
            File[] files = dir.listFiles();
            for (File file : files) {
                boolean uploadResult = uploadCrashReportInExecutorService(file.getPath());
            }
        }
    }

    public Boolean uploadCrashReportInExecutorService(final String crashReportPath) {

        // Initialize AtomicBoolean false
        final AtomicBoolean uploadResult = new AtomicBoolean();

        // Single thread in unbounded queue
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Runnable
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                try {
                    if(!StringUtil.isEmpty(crashReportPath)){

                        File crashReportFile = new File(crashReportPath);

                        if(crashReportFile.exists()){
                            Log.d(TAG, ":::Evolv.uploadCrashReport- crash-report-file found");

                            if(HttpUtils.isNetworkAvailable(getApplicationContext()) && !StringUtil.isEmpty(SERVER_URL)){
                                String requestString = FileUtils.getStringFromFile(crashReportPath);

                                if(StringUtil.isEmpty(requestString)){
                                    FileUtils.deleteFile(crashReportFile);
                                }else{
                                    String responseString = HttpUtils.postDataApp(requestString, SERVER_URL);
                                    if (!StringUtil.isEmpty(responseString) && responseString.equals("success")) {
                                        FileUtils.deleteFile(crashReportFile);
                                    }

                                    Log.d(TAG, ":::Evolv- crash report upload status: "+responseString);
                                }
                            }
                        }
                    }
                    uploadResult.set(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return uploadResult.get();
            }
        };

        Future<Boolean> future = executorService.submit(callable);

        try {
            return future.get();
        } catch (Exception e) {
            return false;
        }
    }

    public String getOtpEmailId() {
        return otpEmailId;
    }

    public void setOtpEmailId(String otpEmailId) {
        this.otpEmailId = otpEmailId;
    }

    public String getOtpMobileNumber() {
        return otpMobileNumber;
    }

    public void setOtpMobileNumber(String otpMobileNumber) {
        this.otpMobileNumber = otpMobileNumber;
    }

    public String getOtpNotificationType() {
        return otpNotificationType;
    }

    public void setOtpNotificationType(String otpNotificationType) {
        this.otpNotificationType = otpNotificationType;
    }

    public String getVerifyOTPValue() {
        return verifyOTPValue;
    }

    public void setVerifyOTPValue(String verifyOTPValue) {
        this.verifyOTPValue = verifyOTPValue;
    }

    public String getCurrentServiceID() {
        return currentServiceID;
    }

    public void setCurrentServiceID(String currentServiceID) {
        this.currentServiceID = currentServiceID;
    }

    public String getCurrentServiceName() {
        return currentServiceName;
    }

    public void setCurrentServiceName(String currentServiceName) {
        this.currentServiceName = currentServiceName;
    }

    public String getCurrentService() {
        return currentService;
    }

    public String getSecondaryCountry() {
        return countrySecondary;
    }

    public void setSecondaryCountry(String country) {
        this.countrySecondary = country;
    }

    public String getSecondaryState() {
        return stateSecondary;
    }

    public void setSecondaryState(String state) {
        this.stateSecondary = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFaceImageType() {
        return faceImageType;
    }

    public void setFaceImageType(String faceImageType) {
        this.faceImageType = faceImageType;
    }

    public String getIdImageType() {
        return idImageType;
    }

    public void setIdImageType(String idImageType) {
        this.idImageType = idImageType;
    }

    public IdType getSelectIdType() {
        return idType;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }

    public IdType getSecondarySelectIdType() {
        return idTypeSecondary;
    }

    public void setSecondaryIdType(IdType idType) {
        this.idTypeSecondary = idType;
    }

    public String getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(String locationLong) {
        this.locationLong = locationLong;
    }

    public String getSnippetResult() {
        return snippetResult;
    }

    public void setSnippetResult(String snippetResult) {
        this.snippetResult = snippetResult;
    }

    public void setCurrentService(String currentService) {
        this.currentService = currentService;
    }

    public String getVideoResult() {
        return videoResult;
    }

    public void setVideoResult(String videoResult) {
        this.videoResult = videoResult;
    }

    public String getVoiceResult() {
        return voiceResult;
    }

    public void setVoiceResult(String voiceResult) {
        this.voiceResult = voiceResult;
    }

    public ArrayList<String> getListAdditionalFeatures() {
        return listAdditionalFeatures;
    }

    public JSONObject getJsonObjAddressDetails() {
        return jsonObjAddressDetails;
    }

    public void setJsonObjAddressDetails(JSONObject jsonObjAddressDetails) {
        this.jsonObjAddressDetails = jsonObjAddressDetails;
    }

    public void setListAdditionalFeatures(ArrayList<String> listAdditionalFeatures) {
        this.listAdditionalFeatures = listAdditionalFeatures;
    }

    public void removeListAdditionalFeatures(String features) {
        this.listAdditionalFeatures.remove(features);
    }

    public void clearListAdditionalFeatures() {
        this.listAdditionalFeatures.clear();
    }

    public JSONObject getAdditonalData() {
        if(additonaldata == null){
            additonaldata = new JSONObject();
        }
        if(!StringUtil.isEmpty(getCurrentServiceID())){
            try {
                additonaldata.put("Service_ID", getCurrentServiceID());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return additonaldata;
    }

    public void setAdditonalData(JSONObject additonalData) {
        this.additonaldata = additonalData;
    }

    public JSONObject getJsonObjEmployeeData() {
        return jsonObjEmployeeData;
    }

    public void setJsonObjEmployeeData(JSONObject jsonObjEmployeeData) {
        this.jsonObjEmployeeData = jsonObjEmployeeData;
    }

    public JSONObject getJsonObjCustomProduct() {
        return jsonObjCustomProduct;
    }

    public void setJsonObjCustomProduct(JSONObject jsonObjCustomProduct) {
        this.jsonObjCustomProduct = jsonObjCustomProduct;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public HashMap<String, String> getResultFPMap() {
        return resultFPMap;
    }

    public void setResultFPMap(HashMap<String, String> resultFPMap) {
        this.resultFPMap = resultFPMap;
    }

    public boolean isSecondaryIdCapture() { return isSecondaryIdCapture; }

    public void setSecondaryIdCapture(boolean secondaryIdCapture) { isSecondaryIdCapture = secondaryIdCapture; }

    public void removeFPData(String key) {
        if (resultFPMap.containsKey(key)) {
            if (!resultFPMap.get(key).isEmpty()) {
                resultFPMap.remove(key);
            }
        }
    }

    public HashMap<String, String> getDeviceResultFPMap() {
        return resultDeviceFPMap;
    }

    public void setDeviceResultFPMap(HashMap<String, String> resultDeviceFPMap) {
        this.resultDeviceFPMap = resultDeviceFPMap;
    }

    public void removeDeviceFPData(String key) {
        try {
            for (Map.Entry<String, String> entry : resultDeviceFPMap.entrySet()) {
                String mapKey = entry.getKey().replaceAll("[0-9]", "");
                if (mapKey.equalsIgnoreCase(key)) {
                    resultDeviceFPMap.remove(entry.getKey());
                }
            }
        } catch (Exception e) {
        }
    }

    public String getOverrideEmpCode() {
        return overrideEmpCode;
    }

    public void setOverrideEmpCode(String overrideEmpCode) {
        this.overrideEmpCode = overrideEmpCode;
    }

    public String getCardDetails() {
        return cardDetails;
    }

    public void setCardDetails(String cardDetails) {
        this.cardDetails = cardDetails;
    }

    public String getBarcodeDetails() {
        return barcodeDetails;
    }

    public void setBarcodeDetails(String barcodeDetails) {
        this.barcodeDetails = barcodeDetails;
    }

    public boolean isFormKeyClear() {
        return isFormKeyClear;
    }

    public void setFormKeyClear(boolean formKeyClear) {
        isFormKeyClear = formKeyClear;
    }

    public String getExternalDirectoryPath() {
        return Environment.getExternalStorageDirectory().getPath();   //External folder;
    }

    public String getBaseDirectoryPath() {
        if (StringUtil.isEmpty(evolvBaseDirectoryPath)) {
//            evolvBaseDirectoryPath = Environment.getExternalStorageDirectory().getPath();   //External folder
            evolvBaseDirectoryPath = (getApplicationContext() != null && getApplicationContext().getFilesDir() != null) ? getApplicationContext().getFilesDir().getParent() : ""; //Internal folder
        }
        return evolvBaseDirectoryPath;
    }

    public void clearEvolvImageDirectory() {
        FileUtils.cleanDir(getBaseDirectoryPath() + Constants.EVOLVE_IMAGE_FOLDER_PATH);
    }

    public void clearEvolvData() {
        currentService = "";
        currentServiceName = "";
        currentServiceID = "";
        country = "";
        state = "";
        faceImageType = "";
        idImageType = "";
        idType = null;
        listAdditionalFeatures = new ArrayList<>();
        additonaldata = new JSONObject();
        locationLat = "";
        locationLong = "";
        snippetResult = "";
        videoResult = "";
        voiceResult = "";
        empCode = "";
        jsonObjAddressDetails = new JSONObject();
        jsonObjEmployeeData = new JSONObject();
        jsonObjCustomProduct = new JSONObject();
        resultFPMap = new HashMap<>();
        resultDeviceFPMap = new HashMap<>();
        overrideEmpCode = "";
        otpEmailId = "";
        otpMobileNumber = "";
        otpNotificationType = "";
        verifyOTPValue = "";
        cardDetails = "";
        isFormKeyClear = true;
        isSecondaryIdCapture = false;
        barcodeDetails = "";

        clearCallExecutionParameter();
    }

    public void clearCallExecutionParameter() {
        try {
            JSONObject callExeParam = new JSONObject();
            callExeParam.put("Service_ID", "");
            callExeParam.put("Manual_Review_Required", "");
            callExeParam.put("Bypass_Age_Validation", "");
            callExeParam.put("Bypass_Name_Matching", "");
            callExeParam.put("Deduplication_Required", "");
            callExeParam.put("Need_Immediate_Response", "");
            ImageProcessingSDK.getInstance().setCallExecutionParameter(callExeParam);
        } catch (Exception e) {
        }
    }

    public String getRequestXMLPath() {
        return getExternalDirectoryPath() + Constants.REQ_XML_PATH;
    }

    public String getResponseXMLPath() {
        return getExternalDirectoryPath() + Constants.RESP_XML_PATH;
    }

    //@SuppressWarnings("deprecation")
    private String getIMEINumber() {
        String IMEINumber = "";
        try {
            TelephonyManager telephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            IMEINumber = telephonyMgr.getImei();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return IMEINumber;
    }

    public static String getOsVersion() {
        String release = Build.VERSION.RELEASE;
        return release;
    }

    public static String getDeviceModel() {
        String model = Build.MODEL;
        return model;
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif: all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b: macBytes) {
//res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {}
        return "02:00:00:00:00:00";
    }
}
