package com.idmission.libtestproject.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.InitializationException;
import com.idmission.client.Response;
import com.idmission.client.ResponseStatusCode;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.activity.NavigationActivity;
import com.idmission.libtestproject.classes.CustomizeUIConfigManager;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AccountSetup extends Fragment implements ImageProcessingResponseListener {

    private static final String[] LANG_TYPE = {"English", "Spanish","Myanmar"};
    private Spinner spinnerLang;
    // private Button buttonSaveAccount, buttonEditAccount;
    private Button buttonContinue, buttonBarcodeScan;
    private EditText urlEdtTxt, loginIdEdtTxt, passEdtTxt, merchantIdEdtTxt, productIdEdtTxt, productNameEdtTxt;
    private TextView urlTxtView, loginIdTxtView, passwordTxtView, merchantIdTxtView, productIdTxtView, productNameTxtView, languageTxtView,
            debugModeTxtView, offTxtView, onTxtView, configUITxtView;
    public static final String SDK_SETTINGS = "SDKSettings", URL = "URL", LOGINID = "LOGINID", PASSWORD = "PASSWORD",
            MERCHANTID = "MERCHANTID", PRODUCTID = "PRODUCTID", PRODUCTNAME = "PRODUCTNAME", LANGUAGE = "LANGUAGE";
    private String url, loginId, password, merchantId, productId, productName, language;
    private SwitchCompat switchBtnDebug, switchBtnCustomUIConfig, switchBtnGPS;
    private EVolvApp eVolvApp;
    private boolean isInitializeAsycCall = false;
    private boolean askCameraPermission = true;
    public static final String DEFAULT_LANGUAGE="en";
    public static final String CUSTOM_UI_CONFIG="custom_ui_config";
    public static final String ENABLE_GPS="enable_gps";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), retrieveSetting(LANGUAGE, DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.account_setup, container, false);

        NavigationActivity.toolbar.setTitle(getString(R.string.account_setup));
        eVolvApp = (EVolvApp) (getActivity().getApplicationContext());

        urlEdtTxt = (EditText) view.findViewById(R.id.edit_text_url);
        loginIdEdtTxt = (EditText) view.findViewById(R.id.edit_text_login_id);
        passEdtTxt = (EditText) view.findViewById(R.id.edit_text_password);
        merchantIdEdtTxt = (EditText) view.findViewById(R.id.edit_text_merchant_id);
        productIdEdtTxt = (EditText) view.findViewById(R.id.edit_text_product_id);
        productNameEdtTxt = (EditText) view.findViewById(R.id.edit_text_product_name);
        spinnerLang = (Spinner) view.findViewById(R.id.spinner_lang);
        buttonContinue = (Button) view.findViewById(R.id.button_continue);
        switchBtnDebug = (SwitchCompat) view.findViewById(R.id.switch_btn_debug);
        switchBtnCustomUIConfig = (SwitchCompat) view.findViewById(R.id.switch_btn_custom_ui_config);
        switchBtnGPS = (SwitchCompat) view.findViewById(R.id.switch_btn_gps);
        buttonBarcodeScan = (Button) view.findViewById(R.id.button_barcode_scan);

        urlTxtView = (TextView) view.findViewById(R.id.text_view_url);
        loginIdTxtView = (TextView) view.findViewById(R.id.text_view_login_id);
        passwordTxtView = (TextView) view.findViewById(R.id.text_view_password);
        merchantIdTxtView = (TextView) view.findViewById(R.id.text_view_merchant_id);
        productIdTxtView = (TextView) view.findViewById(R.id.text_view_product_id);
        productNameTxtView = (TextView) view.findViewById(R.id.text_view_product_name);
        languageTxtView = (TextView) view.findViewById(R.id.text_view_lang);
        debugModeTxtView = (TextView) view.findViewById(R.id.text_view_debug_mode);
        offTxtView = (TextView) view.findViewById(R.id.text_debug_off);
        onTxtView = (TextView) view.findViewById(R.id.text_debug_on);
        configUITxtView = (TextView) view.findViewById(R.id.text_view_custom_ui_config);

        //buttonSaveAccount = (Button) view.findViewById(R.id.button_save_account);
        //buttonEditAccount = (Button) view.findViewById(R.id.button_edit_account);

        spinnerLang.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, LANG_TYPE));

        setDefaultValue();
        hideKeyboard(getActivity(), view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

//        buttonSaveAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveAccountDetails(view);
//            }
//        });
//
//        buttonEditAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // setEnabledView(true);
//            }
//        });

        buttonBarcodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageProcessingSDK.getInstance().setImageProcessingResponseListener(AccountSetup.this);
                String qrCodeString = "#IDM#" + getResources().getString(R.string.qr_code_inst);
                ImageProcessingSDK.getInstance().scanBarcode(getActivity(), null, true, qrCodeString);
            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (saveAccountDetails(view)) {
                    if (ImageProcessingSDK.isFPDeviceSupportEnabled() && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE}, 0);
                        askCameraPermission = false;
                    } else if(!ImageProcessingSDK.isFPDeviceSupportEnabled() && askCameraPermission && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        askCameraPermission = false;
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},0);
                    } else if (ImageProcessingSDK.isFPDeviceSupportEnabled() && askCameraPermission && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA)) {
                            askCameraPermission = false;
                            initializeAccountSetup();
                            //showErrorMessage(getView(), "Need to enable CAMERA permission.");
                        } else {
                            askCameraPermission = false;
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
                        }
                    } else {
                        //API call
                        initializeAccountSetup();
//                        int productID = 0;
//                        if (!StringUtil.isEmpty(productId)) {
//                            productID = Integer.parseInt(productId);
//                        }
//
//                        if(isInitializeAsycCall) {
//
//                            try {
//                                ImageProcessingSDK.getInstance().setImageProcessingResponseListener(AccountSetup.this);
//                                ImageProcessingSDK.getInstance().initializeAsync(getActivity(), url, loginId, password, merchantId, productID, productName, language, switchBtnDebug.isChecked(), true);
//
//                                ImageProcessingSDK.getInstance().deleteData();  //Delete the previously captured images from SDK
//                                eVolvApp.clearEvolvImageDirectory();            //Delete the previously captured images from testapp
//                                eVolvApp.clearEvolvData();                      //Delete all previously captured data
//
//                            } catch (Exception e) {
//                                StringUtil.printLogInDebugMode("INIT Exception", e.getMessage());
//                            }
//
//                        } else {
//
//                            try {
//                                ImageProcessingSDK.getInstance().initialize(getActivity(), url, loginId, password, merchantId, productID, productName, language, switchBtnDebug.isChecked());
//                                ImageProcessingSDK.getInstance().setImageProcessingResponseListener(AccountSetup.this);
//
//                                ImageProcessingSDK.getInstance().deleteData();  //Delete the previously captured images from SDK
//                                eVolvApp.clearEvolvImageDirectory();            //Delete the previously captured images from testapp
//                                eVolvApp.clearEvolvData();                      //Delete all previously captured data
//
//                                FragmentManager fm = getFragmentManager();
//                                ProcessFlow processFlow = new ProcessFlow();
//                                fm.beginTransaction().replace(R.id.flContent, processFlow).addToBackStack(null).commit();
//                                NavigationActivity.toolbar.setTitle(R.string.process_flow);
//
//                            } catch (DeviceNotSupportedException e) {
//                                StringUtil.printLogInDebugMode("INIT Exception", e.getMessage());
//                                showErrorMessage(view, "Not Initialized : " + "Device not supported");
//                            } catch (IllegalArgumentException e) {
//                                StringUtil.printLogInDebugMode("INIT Exception", e.getMessage());
//                                showErrorMessage(view, e.getMessage());
//                            } catch (PlayServiceException e) {
//                                StringUtil.printLogInDebugMode("INIT Exception", e.getMessage());
//                                showErrorMessage(view, "Not Initialized : " + "Play service exception");
//                            }
//
//                        }
                    }
                }
//                else {
//                    showErrorMessage(view, getString(R.string.save_account_detail_error));
//                }

                PreferenceUtils.setPreference(getActivity(), CUSTOM_UI_CONFIG, switchBtnCustomUIConfig.isChecked());
                PreferenceUtils.setPreference(getActivity(), ENABLE_GPS, switchBtnGPS.isChecked());
            }
        });
        spinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String language;
                String selectLang= parent.getItemAtPosition(position).toString();
                if (selectLang.equalsIgnoreCase("Spanish")) {
                    language = "es";
                }else if (selectLang.equalsIgnoreCase("Myanmar")){
                    language = "my";
                } else {
                    language = "en";
                }

                String lang = retrieveSetting(LANGUAGE, "en");
                if(!lang.equals(language)){
                    CustomizeUIConfigManager.reinitWithLanguageChange(getContext(), language);
                }

                CommonUtils.updateLanguage(getActivity(), language);
                saveSetting(LANGUAGE, language);

                CustomizeUIConfigManager.initCustomizeUIConfig(getContext());

                ImageProcessingSDK.getInstance().customizeUserInterface(CustomizeUIConfigManager.getCompleteUIConfigJSON());

                reloadLabel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void reloadLabel() {
        NavigationActivity.toolbar.setTitle(getString(R.string.account_setup));
        urlTxtView.setText(R.string.test_url);
        loginIdTxtView.setText(R.string.login_id);
        passwordTxtView.setText(R.string.password);
        merchantIdTxtView.setText(R.string.merchant_id);
        productIdTxtView.setText(R.string.product_id);
        productNameTxtView.setText(R.string.product);
        languageTxtView.setText(R.string.language);
        debugModeTxtView.setText(R.string.debaug_mode);
        onTxtView.setText(R.string.on);
        offTxtView.setText(R.string.off);
        buttonContinue.setText(R.string.initialize);
        buttonBarcodeScan.setText(R.string.qr_button_title);
        configUITxtView.setText(R.string.customize_ui_configuration);
    }

    private void initializeAccountSetup(){
        int productID = 0;
        if (!StringUtil.isEmpty(productId)) {
            productID = Integer.parseInt(productId);
        }

        if(isInitializeAsycCall) {

            try {
                ImageProcessingSDK.getInstance().setImageProcessingResponseListener(AccountSetup.this);
                ImageProcessingSDK.getInstance().initializeAsync(getActivity(), url, loginId, password, merchantId, productID, productName, language, switchBtnDebug.isChecked(), true);

                ImageProcessingSDK.getInstance().deleteData();  //Delete the previously captured images from SDK
                eVolvApp.clearEvolvImageDirectory();            //Delete the previously captured images from testapp
                eVolvApp.clearEvolvData();                      //Delete all previously captured data

            } catch (Exception e) {
                StringUtil.printLogInDebugMode("INIT Exception", e.getMessage());
            }

        } else {

            try {
                ImageProcessingSDK.getInstance().initialize(getActivity(), url, loginId, password, merchantId, productID, productName, language, switchBtnDebug.isChecked(), switchBtnGPS.isChecked());
                ImageProcessingSDK.getInstance().setImageProcessingResponseListener(AccountSetup.this);

                ImageProcessingSDK.getInstance().deleteData();  //Delete the previously captured images from SDK
                eVolvApp.clearEvolvImageDirectory();            //Delete the previously captured images from testapp
                eVolvApp.clearEvolvData();                      //Delete all previously captured data

                FragmentManager fm = getFragmentManager();
                ProcessFlow processFlow = new ProcessFlow();
                fm.beginTransaction().replace(R.id.flContent, processFlow).addToBackStack(null).commit();
                NavigationActivity.toolbar.setTitle(R.string.process_flow);

            } catch (InitializationException e) {
                StringUtil.printLogInDebugMode("INIT Exception", e.getMessage());
                showErrorMessage(getView(), e.getMessage());
            }

        }
    }

    private void setDefaultValue() {
        String url, loginId, password, merchantId, productId, productName, lang;

        url = retrieveSetting(URL, getString(R.string.url));
        loginId = retrieveSetting(LOGINID, null);
        password = retrieveSetting(PASSWORD, null);
        merchantId = retrieveSetting(MERCHANTID, null);
        productId = retrieveSetting(PRODUCTID, null);
        productName = retrieveSetting(PRODUCTNAME, getString(R.string.product_name));
        lang = retrieveSetting(LANGUAGE, "en");

        urlEdtTxt.setText(url);
        loginIdEdtTxt.setText(loginId);
        passEdtTxt.setText(password);
        merchantIdEdtTxt.setText(merchantId);
        productIdEdtTxt.setText(productId);
        productNameEdtTxt.setText(productName);

        switchBtnCustomUIConfig.setChecked(PreferenceUtils.getPreference(getActivity(), CUSTOM_UI_CONFIG, false));
        switchBtnGPS.setChecked(PreferenceUtils.getPreference(getActivity(), ENABLE_GPS, true));

        if (lang.equalsIgnoreCase("en")) {
            spinnerLang.setSelection(0);
        } else if (lang.equalsIgnoreCase("es")){
            spinnerLang.setSelection(1);
        }else {
            spinnerLang.setSelection(2);
        }

        if (!StringUtil.isEmpty(url)
                && !StringUtil.isEmpty(loginId)
                && !StringUtil.isEmpty(password)
                && !StringUtil.isEmpty(merchantId)
                && !StringUtil.isEmpty(productId)
                && !StringUtil.isEmpty(productName)
                && !StringUtil.isEmpty(lang)) {
            //setEnabledView(false);
        }
    }

    private boolean saveAccountDetails(View view) {
        url = urlEdtTxt.getText().toString();
        loginId = loginIdEdtTxt.getText().toString();
        password = passEdtTxt.getText().toString();
        merchantId = merchantIdEdtTxt.getText().toString();
        productId = productIdEdtTxt.getText().toString();
        productName = productNameEdtTxt.getText().toString();
        language = spinnerLang.getSelectedItem().toString();

        if (language.equalsIgnoreCase("Spanish")) {
            language = "es";
        }else if (language.equalsIgnoreCase("Myanmar")){
            language = "my";
        }else {
            language = "en";
        }

        if (StringUtil.isEmpty(url)) {
            showErrorMessage(view, getString(R.string.enter_url));
        } else if (StringUtil.isEmpty(loginId)) {
            showErrorMessage(view, getString(R.string.enter_login_id));
        } else if (StringUtil.isEmpty(password)) {
            showErrorMessage(view, getString(R.string.enter_password));
        } else if (StringUtil.isEmpty(merchantId)) {
            showErrorMessage(view, getString(R.string.enter_merchant_id));
        } else if (StringUtil.isEmpty(productId)) {
            showErrorMessage(view, getString(R.string.enter_product_id));
        } else if (StringUtil.isEmpty(productName)) {
            showErrorMessage(view, getString(R.string.enter_product_name));
        } else {
            saveSetting(URL, urlEdtTxt.getText().toString());
            saveSetting(LOGINID, loginIdEdtTxt.getText().toString());
            saveSetting(PASSWORD, passEdtTxt.getText().toString());
            saveSetting(MERCHANTID, merchantIdEdtTxt.getText().toString());
            saveSetting(PRODUCTID, productIdEdtTxt.getText().toString());
            saveSetting(PRODUCTNAME, productNameEdtTxt.getText().toString());
            saveSetting(LANGUAGE, language);

            //setEnabledView(false);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        PreferenceUtils.setPreference(getActivity(), CUSTOM_UI_CONFIG, false);
        super.onDestroy();
    }

//    private void setEnabledView(boolean enable) {
//        urlEdtTxt.setEnabled(enable);
//        loginIdEdtTxt.setEnabled(enable);
//        passEdtTxt.setEnabled(enable);
//        merchantIdEdtTxt.setEnabled(enable);
//        productIdEdtTxt.setEnabled(enable);
//        productNameEdtTxt.setEnabled(enable);
//        spinnerLang.setEnabled(enable);
//
//        if (!enable) {
//            urlEdtTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
//            loginIdEdtTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
//            passEdtTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
//            merchantIdEdtTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
//            productIdEdtTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
//            productNameEdtTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
//        }else {
//            urlEdtTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
//            loginIdEdtTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
//            passEdtTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
//            merchantIdEdtTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
//            productIdEdtTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
//            productNameEdtTxt.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
//        }
//    }

    public void saveSetting(String key, String value) {
        PreferenceUtils.setPreference(getActivity(), key, value);
    }

    public String retrieveSetting(String key, String def) {
        return PreferenceUtils.getPreference(getActivity(), key, def);
    }

    public void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onImageProcessingResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onAutoImageCaptureResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onAutoFillResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onAutoFillFieldInformationAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onFaceDetectionResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onFaceMatchingResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onCardDetectionResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onCaptureProofOfAddressResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onCaptureBankStatementResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onCaptureGenericDocumentResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onCaptureBirthCertificateResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onImageProcessingAndFaceMatchingResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onOperationResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onCustomerVerificationResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onCustomizeUserInterfaceResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onVoiceRecordingFinished(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onGPSCoordinateAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onFourFingerCaptureFinished(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onFingerprintCaptureFinished(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onFingerprintEnrolmentFinished(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onFingerprintVerificationFinished(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onVideoRecordingFinished(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onScanBarcodeFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK", "CALLBACK:::: onScanBarcodeFinished");
        if (null != response) {
            if (response.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()) {

                if (null != resultMap) {
                    if (resultMap.get("content") == null) {

                    } else {
                        try {
                            String checkString = resultMap.get("content");
                            if(checkString.startsWith("[")){
                                JSONArray jsonArray = new JSONArray(checkString);
                                if(null != jsonArray && jsonArray.length() > 0) {
                                    JSONObject accountCredentialsObject = jsonArray.getJSONObject(0);
                                    if(null != accountCredentialsObject) {
                                        setAccountCredentailafromQRcodeResponse(accountCredentialsObject);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onCaptureSignatureFinished(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void onVerifyAddressFinished(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void onCreateEmployeeFinished(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void onVerifyEmployeeFinished(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void onGenerateTokenFinished(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void onVerifyTokenFinished(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void onSnippetImageCaptureResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onUpdateCustomerFinished(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onGenerateOTPFinished(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void onVerifyOTPFinished(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void onExecuteCustomProductCall(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onUpdateEmployeeFinished(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void onIDValidationAndVideoMatchingFinished(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void genericApiCallResponse(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void onVideoConferencingFinished(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onDownloadXsltResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onInitializationResultAvailable(Map<String, String> resultMap, Response responses) {
        if(responses.getStatusCode() == 0) {
            FragmentManager fm = getFragmentManager();
            ProcessFlow processFlow = new ProcessFlow();
            fm.beginTransaction().replace(R.id.flContent, processFlow).addToBackStack(null).commit();
            NavigationActivity.toolbar.setTitle(R.string.process_flow);
        } else {
            showErrorMessage(getView(), responses.getStatusMessage());
        }
    }

    private void setAccountCredentailafromQRcodeResponse (JSONObject accountCredentialObj) {

        try {
                if (accountCredentialObj.has("URL")) {
                    urlEdtTxt.setText(accountCredentialObj.get("URL").toString());
                }
                if (accountCredentialObj.has("LoginId")) {
                    loginIdEdtTxt.setText(accountCredentialObj.get("LoginId").toString());
                }
                if (accountCredentialObj.has("Password")) {
                    passEdtTxt.setText(accountCredentialObj.get("Password").toString());
                }
                if (accountCredentialObj.has("ProductId")) {
                    productIdEdtTxt.setText(accountCredentialObj.get("ProductId").toString());
                }
                if (accountCredentialObj.has("MerchantId")) {
                    merchantIdEdtTxt.setText(accountCredentialObj.get("MerchantId").toString());
                }
                if (accountCredentialObj.has("PRODUCT_NAME")) {
                    productNameEdtTxt.setText(accountCredentialObj.get("PRODUCT_NAME").toString());
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
