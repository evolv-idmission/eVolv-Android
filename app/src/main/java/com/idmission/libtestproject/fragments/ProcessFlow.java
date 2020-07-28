package com.idmission.libtestproject.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.idmission.client.DeviceNotSupportedException;
import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.InitializationException;
import com.idmission.client.PlayServiceException;
import com.idmission.client.Response;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.activity.NavigationActivity;
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.classes.features.FeatureFlow;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

 public class ProcessFlow extends Fragment implements View.OnClickListener, ImageProcessingResponseListener {

     private Spinner spinnerService;
     private TextView serviceIdDetail;
     private SwitchCompat switchBtnAdditional, switchBtnDebug;
     private LinearLayout layoutTransparent, layoutSignature, layoutDocument, layoutVoice, layoutFingerPrint, layoutFingerPrint4F, layoutLocation, layoutVideo, layoutSnippet, layoutScanBarcode, layoutCard, layoutSlant, layoutVideoConference, layoutSecondaryIdCaptureFront;
     private CheckBox checkBoxSignature, checkBoxDocument, checkBoxVoice, checkBoxFP, checkBoxLocation, checkBoxVideo,
             checkBoxSnippet, checkBoxFP4F, checkBoxBarcode, checkBoxCard, checkBoxSlant, checkBoxVideoConference, checkBoxSecondaryIDCaptureFront;
     private ImageView imageViewSignature, imageViewDocument, imageViewVoice, imageViewFP, imageViewFP4F, imageViewLocation, imageViewVideo,
             imageViewSnippet, imageViewBarcode, imageViewCard, imageViewSlant, imageViewVideoConference, imageViewSecondaryIDCaptureFront;
     private Button buttonOkey, buttonInitialize, buttonContinue;
     private Dialog dialog;
     private ProgressDialog progressDialog;
     //public static String ADDITIONAL_FEATURES = "ADDITIONAL_FEATURES", SELECTED_SERVICE = "SELECTED_SERVICE";
     //SDK Instance
     // public ImageProcessingSDK imageProcessingSDK = null;
     private boolean enableFingerprint = true, enableAdditionalFeatures = false;
     private EVolvApp eVolvApp;
     private LinkedHashMap<String, String> services_map = new LinkedHashMap<>();

     @Override
     public void onAttach(Context context) {
         super.onAttach(context);
     }

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

         CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
         View view = inflater.inflate(R.layout.process_flow, container, false);

         eVolvApp = (EVolvApp) (getActivity().getApplicationContext());
         NavigationActivity.toolbar.setTitle(getString(R.string.process_flow));

         spinnerService = (Spinner) view.findViewById(R.id.spinner_service);
         serviceIdDetail = (TextView) view.findViewById(R.id.service_id_detail);
         switchBtnAdditional = (SwitchCompat) view.findViewById(R.id.switch_btn_additional);
         switchBtnDebug = (SwitchCompat) view.findViewById(R.id.switch_btn_debug);
         layoutTransparent = (LinearLayout) view.findViewById(R.id.linear_layout_transparent);
         layoutSignature = (LinearLayout) view.findViewById(R.id.linear_layout_signature);
         checkBoxSignature = (CheckBox) view.findViewById(R.id.checkbox_signature);
         checkBoxDocument = (CheckBox) view.findViewById(R.id.checkbox_document);
         checkBoxVoice = (CheckBox) view.findViewById(R.id.checkbox_voice);
         checkBoxFP = (CheckBox) view.findViewById(R.id.checkbox_fingerprint);
         checkBoxFP4F = (CheckBox) view.findViewById(R.id.checkbox_fingerprint4f);
         checkBoxLocation = (CheckBox) view.findViewById(R.id.checkbox_location);
         checkBoxVideo = (CheckBox) view.findViewById(R.id.checkbox_video);
         checkBoxSnippet = (CheckBox) view.findViewById(R.id.checkbox_snippet);
         checkBoxBarcode = (CheckBox) view.findViewById(R.id.checkbox_scan_barcode);
         checkBoxCard = (CheckBox) view.findViewById(R.id.checkbox_card);
         checkBoxSlant = (CheckBox) view.findViewById(R.id.checkbox_slant);
         checkBoxVideoConference = (CheckBox) view.findViewById(R.id.checkbox_videoConference);
         checkBoxSecondaryIDCaptureFront = (CheckBox) view.findViewById(R.id.checkbox_IDSecondaryFront);
//         checkBoxSecondaryIDCaptureBack = (CheckBox) view.findViewById(R.id.checkbox_IDSecondaryBack);

         imageViewSignature = (ImageView) view.findViewById(R.id.image_view_signature);
         imageViewDocument = (ImageView) view.findViewById(R.id.image_view_document);
         imageViewVoice = (ImageView) view.findViewById(R.id.image_view_voice);
         imageViewFP = (ImageView) view.findViewById(R.id.image_view_fingerprint);
         imageViewFP4F = (ImageView) view.findViewById(R.id.image_view_fingerprint4f);
         imageViewBarcode = (ImageView) view.findViewById(R.id.image_view_scan_barcode);
         imageViewLocation = (ImageView) view.findViewById(R.id.image_view_location);
         imageViewVideo = (ImageView) view.findViewById(R.id.image_view_video);
         imageViewSnippet = (ImageView) view.findViewById(R.id.image_view_snippet);
         imageViewCard = (ImageView) view.findViewById(R.id.image_view_card);
         imageViewSlant = (ImageView) view.findViewById(R.id.image_view_slant);
         imageViewVideoConference = (ImageView) view.findViewById(R.id.image_view_videoConference);
         imageViewSecondaryIDCaptureFront = (ImageView) view.findViewById(R.id.image_view_IDSecondaryFront);
//         imageViewSecondaryIDCaptureBack = (ImageView) view.findViewById(R.id.image_view_IDSecondaryBack);

         buttonInitialize = (Button) view.findViewById(R.id.button_initialize);
         buttonContinue = (Button) view.findViewById(R.id.button_continue);

         layoutSignature = (LinearLayout) view.findViewById(R.id.linear_layout_signature);
         layoutDocument = (LinearLayout) view.findViewById(R.id.linear_layout_document);
         layoutVoice = (LinearLayout) view.findViewById(R.id.linear_layout_voice);
         layoutFingerPrint = (LinearLayout) view.findViewById(R.id.linear_layout_fingerprint);
         layoutFingerPrint4F = (LinearLayout) view.findViewById(R.id.linear_layout_fingerprint4f);
         layoutLocation = (LinearLayout) view.findViewById(R.id.linear_layout_location);
         layoutVideo = (LinearLayout) view.findViewById(R.id.linear_layout_video);
         layoutSnippet = (LinearLayout) view.findViewById(R.id.linear_layout_snippet);
         layoutScanBarcode = (LinearLayout) view.findViewById(R.id.linear_layout_scan_barcode);
         layoutCard = (LinearLayout) view.findViewById(R.id.linear_layout_card);
         layoutSlant = (LinearLayout) view.findViewById(R.id.linear_layout_slant);
         layoutVideoConference = (LinearLayout) view.findViewById(R.id.linear_layout_videoConference);
         layoutSecondaryIdCaptureFront = (LinearLayout) view.findViewById(R.id.linear_layout_IDSecondaryFront);
//         layoutSecondaryIdCaptureBack = (LinearLayout) view.findViewById(R.id.linear_layout_IDSecondaryBack);

         toggleCheckboxView(false);

         initilizeProgressBar();

//        if (eVolvApp.isFormKeyClear()) {
//            buttonContinue.setVisibility(View.GONE);
//        } else {
//            buttonContinue.setVisibility(View.VISIBLE);
//        }

//        if (eVolvApp.isFormKeyClear()) {
//            buttonInitialize.setVisibility(View.GONE);
//        } else {
//            buttonInitialize.setVisibility(View.VISIBLE);
//        }

         imageViewSignature.setOnClickListener(this);
         imageViewDocument.setOnClickListener(this);
         imageViewVoice.setOnClickListener(this);
         imageViewFP.setOnClickListener(this);
         layoutFingerPrint4F.setOnClickListener(this);
         imageViewLocation.setOnClickListener(this);
         imageViewVideo.setOnClickListener(this);
         imageViewSnippet.setOnClickListener(this);
         imageViewBarcode.setOnClickListener(this);
         imageViewCard.setOnClickListener(this);
         imageViewSlant.setOnClickListener(this);
         imageViewVideoConference.setOnClickListener(this);
         imageViewSecondaryIDCaptureFront.setOnClickListener(this);
//         imageViewSecondaryIDCaptureBack.setOnClickListener(this);

         layoutSignature.setOnClickListener(this);
         layoutDocument.setOnClickListener(this);
         layoutVoice.setOnClickListener(this);
         layoutFingerPrint.setOnClickListener(this);
         layoutFingerPrint4F.setOnClickListener(this);
         layoutLocation.setOnClickListener(this);
         layoutVideo.setOnClickListener(this);
         layoutSnippet.setOnClickListener(this);
         layoutScanBarcode.setOnClickListener(this);
         layoutCard.setOnClickListener(this);
         layoutSlant.setOnClickListener(this);
         layoutVideoConference.setOnClickListener(this);
         layoutSecondaryIdCaptureFront.setOnClickListener(this);
//         layoutSecondaryIdCaptureBack.setOnClickListener(this);

//        //Supported service list
//        services_map.put("10 IDV+Face Match", "ID Validation and Facial Biometric Matching: Service ID 10");
//        services_map.put("20 IDV Only", "ID Validation Only: Service ID 20");
//        services_map.put("25 IDV Only w/Cust Enroll", "ID Validation Only w/Customer Enrollment: Service ID 25");
//        services_map.put("30 IDV Only w/Emp Enroll", "ID Validation Only w/Employee Enrollment: Service ID 30");
//        services_map.put("50 IDV+Face Match w/Cust Enroll", "ID Validation and Facial Biometric Matching w/Customer Enrollment: Service ID 50");
//        services_map.put("55 IDV+Face Match w/Emp Enroll", "ID Validation and Facial Biometric Matching w/Employee Enrollment: Service ID 55");
//        services_map.put("60 Face Match Only w/Cust Enroll", "Facial Biometric Matching Only w/Customer Enrollment: Service ID 60");
//        services_map.put("65 Face Match Only", "Facial Biometric Matching Only: Service ID 65");
//        services_map.put("70 Customer Update", "Customer Update: Service ID 70");
//        services_map.put("75 Employee Update", "Employee Update: Service ID 75");
//        services_map.put("100 Customer Verification", "Customer Biometric Verification: Service ID 100");
//        services_map.put("155 IDV+Video Match", "ID Validation and Facial Biometric Matching with Video Recording instead of Photo/Live Face Detection: Service ID 155");
//        services_map.put("160 IDV+Video Match w/Cust Enroll", "ID Validation and Facial Biometric Matching with Video Recording instead of Photo/Live Face Detection w/Customer Enrollment: Service ID 160");
//        services_map.put("165 IDV+Video Match w/Emp Enroll", "ID Validation and Facial Biometric Matching with Video Recording instead of Photo/Live Face Detection w/Employee Enrollment: Service ID 165");
//        services_map.put("175 Cust Enroll w/FP", "Customer Enrollment with FP Biometrics: Service ID 175");
//        services_map.put("180 Emp Enroll w/FP", "Employee Enrollment with FP Biometrics: Service ID 180");
//        services_map.put("200 Address Processing", "Address Matching / Verification: Service ID 200");
//        services_map.put("300 Employee Verification", "Employee Biometric Verification: Service ID 300");
//        services_map.put("310 Face Match Only w/Emp Enroll", "Facial Biometric Matching Only w/Employee Enrollment: Service ID 310");
//        services_map.put("320 Create Customer(Override)", "Create Customer using override feature: Service ID 320");
//        services_map.put("330 Create Employee(Override)", "Create Employee using override feature: Service ID 330");
//        .put("360 Id Validation & Face Match(Primary ID)", "Id Validation & Face Match(Primary ID) 360");
//        .put("361 Id Validation & Face Match(Secondary ID)", "Face Match Between Secondary ID and Selfie 361");
//        .put("400 OTP Generation", "One Time PIN Generation: Service ID 400");
//        .put("401 Token Generation", "Token Number Generation: Service ID 401");
//        services_map.put("410 OTP Verification", "One Time PIN Verification: Service ID 410");

         getServiceList();

         ArrayList<String> services_list = new ArrayList<>();
         services_list.addAll(services_map.keySet());

//        spinnerService.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, FeatureFlow.functionality));
         //spinnerService.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.custom_service_id_spinner, services_list));

         ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.custom_service_id_spinner, R.id.text_view_custom, services_list);
         spinnerService.setAdapter(adapter);

         return view;
     }

     @Override
     public void onActivityCreated(@Nullable Bundle savedInstanceState) {

         spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 String service = spinnerService.getSelectedItem().toString();

                 //clear initilizeCallExecutionParameter
                 eVolvApp.clearCallExecutionParameter();

                 serviceIdDetail.setText(services_map.get(service));

                 if (enableAdditionalFeatures) {
                     disableViewForService(service);
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

             }
         });

         switchBtnAdditional.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                 if (isChecked) {
                     toggleCheckboxView(true);
                     disableViewForService(spinnerService.getSelectedItem().toString());
                     enableAdditionalFeatures = true;
                 } else {
                     toggleCheckboxView(false);
                     enableAdditionalFeatures = false;
                 }
             }
         });

         buttonInitialize.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 buttonInitialize.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         if (enableFingerprint && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                             ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                         } else {
                             progressDialog.show();
                             try {
                                 String url = retrieveSetting(AccountSetup.URL, "https://demo.idmission.com/IDS/service/integ/idm/thirdparty/upsert");
                                 String loginId = retrieveSetting(AccountSetup.LOGINID, "");
                                 String password = retrieveSetting(AccountSetup.PASSWORD, "");
                                 String merchantId = retrieveSetting(AccountSetup.MERCHANTID, "");
                                 String productId = retrieveSetting(AccountSetup.PRODUCTID, "");
                                 String productName = retrieveSetting(AccountSetup.PRODUCTNAME, "");
                                 String language = retrieveSetting(AccountSetup.LANGUAGE, "en");

                                 int productID = 0;
                                 if (!StringUtil.isEmpty(productId)) {
                                     productID = Integer.parseInt(productId);
                                 }

                                 ImageProcessingSDK.getInstance().initialize(getActivity(), url, loginId, password, merchantId, productID, productName, language, switchBtnDebug.isChecked());
                                 ImageProcessingSDK.getInstance().setImageProcessingResponseListener(ProcessFlow.this);
                                 ImageProcessingSDK.getInstance().deleteData();  //Delete the previously captured images from SDK
                                 eVolvApp.clearEvolvImageDirectory();            //Delete the previously captured images from testapp
                                 eVolvApp.clearEvolvData();                      //Delete all previously captured data

                                 generateFeatureListAndContinue();

                             } catch (InitializationException e) {
                                 StringUtil.printLogInDebugMode("INIT Exception", e.getMessage());
//                              showErrorMessage(view, "Not Initialized : " + e.getMessage());
                                 showErrorMessage(view, e.getMessage());
                             } finally {
                                 cancelProgressDialog();
                             }
                         }
                     }
                 });

             }
         });

         buttonContinue.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 generateFeatureListAndContinue();
             }
         });
         super.onActivityCreated(savedInstanceState);
     }

     private void generateFeatureListAndContinue() {

         ArrayList<String> listAdditionalFeatures = new ArrayList<String>();

         String selectedServiceValue = spinnerService.getSelectedItem().toString();

         String[] serviceID_ServiceName = selectedServiceValue.split(" ", 2);

         int serviceId = Integer.parseInt(serviceID_ServiceName[0]);
         String serviceName = serviceID_ServiceName[1];
         String selectedService = FeatureFlow.getServiceTypeForServiceID(serviceId);

         LinkedHashMap<String, ArrayList<String>> featureForService = FeatureFlow.getRequiredFeatureForService();
         LinkedHashMap<String, ArrayList<String>> additionalFeatureForService = FeatureFlow.getAdditionalFeatureForService();

         ArrayList<String> requiredFeature = featureForService.get(selectedService);
         if (requiredFeature != null && !requiredFeature.isEmpty()) {
             listAdditionalFeatures.addAll(requiredFeature);
         }

         if (switchBtnAdditional.isChecked()) {
             if (checkBoxSignature.isChecked()) {
                 listAdditionalFeatures.add(Constants.FEATURE_SIGNATURE_CAPTURE);
             }
             if (checkBoxDocument.isChecked()) {
                 listAdditionalFeatures.add(Constants.FEATURE_DOCUMENT_CAPTURE);
             }
             if (checkBoxVoice.isChecked()) {
                 listAdditionalFeatures.add(Constants.FEATURE_VOICE_CAPTURE);
             }
             if (checkBoxFP.isChecked()) {
                 listAdditionalFeatures.add(Constants.FEATURE_FINGERPRINT_CAPTURE);
             }
             if (checkBoxLocation.isChecked()) {
                 listAdditionalFeatures.add(Constants.FEATURE_GPS_CAPTURE);
             }
             if (checkBoxVideo.isChecked()) {
                 listAdditionalFeatures.add(Constants.FEATURE_VIDEO_CAPTURE);
             }
             if (checkBoxSnippet.isChecked()) {
                 listAdditionalFeatures.add(Constants.FEATURE_SNIPPET_CAPTURE);
             }
             if (checkBoxFP4F.isChecked()) {
                 listAdditionalFeatures.add(Constants.FEATURE_FINGERPRINT4F_CAPTURE);
             }
             if (checkBoxBarcode.isChecked()) {
                 listAdditionalFeatures.add(Constants.FEATURE_SCAN_BARCODE);
             }
             if (checkBoxCard.isChecked()) {
                 listAdditionalFeatures.add(Constants.FEATURE_CARD_CAPTURE);
             }
             if (checkBoxSlant.isChecked()) {
                 listAdditionalFeatures.add(Constants.FEATURE_SLANT_ID_CAPTURE);
             }
             if (checkBoxVideoConference.isChecked()) {
                 listAdditionalFeatures.add(Constants.FEATURE_VIDEO_CONFERENCE);
             }
             if (checkBoxSecondaryIDCaptureFront.isChecked()) {
                 listAdditionalFeatures.add(Constants.FEATURE_SECONDARY_ID_DETAILS);
                 listAdditionalFeatures.add(Constants.FEATURE_SECONDARY_ID_CAPTURE_FRONT);
                 listAdditionalFeatures.add(Constants.FEATURE_SECONDARY_ID_CAPTURE_BACK);
             }
//             if(checkBoxSecondaryIDCaptureBack.isChecked()) {
//                 listAdditionalFeatures.add(Constants.FEATURE_SECONDARY_ID_CAPTURE_BACK);
//             }
         }

         ArrayList<String> additionalFeature = additionalFeatureForService.get(selectedService);
         if (additionalFeature != null && !additionalFeature.isEmpty()) {
             listAdditionalFeatures.addAll(additionalFeature);
         }

         eVolvApp.clearListAdditionalFeatures();
         eVolvApp.setListAdditionalFeatures(listAdditionalFeatures);
         eVolvApp.setCurrentService(selectedService);
         eVolvApp.setCurrentServiceName(serviceName);
         eVolvApp.setCurrentServiceID("" + serviceId);
         eVolvApp.setSecondaryIdCapture(checkBoxSecondaryIDCaptureFront.isChecked());

         if (selectedService.equalsIgnoreCase(Constants.SERVICE_CUSTOMER_UPDATE) || selectedService.equalsIgnoreCase(Constants.SERVICE_OTP_GENERATION) || selectedService.equalsIgnoreCase(Constants.SERVICE_OTP_VERIFICATION) || selectedService.equalsIgnoreCase(Constants.SERVICE_GENERATE_TOKEN) || selectedService.equalsIgnoreCase(Constants.SERVICE_FP_CUSTOMER_ENROLLMENT) || selectedService.equalsIgnoreCase(Constants.SERVICE_FP_EMPLOYEE_ENROLLMENT) || selectedService.equalsIgnoreCase(Constants.SERVICE_EMPLOYEE_UPDATE) || selectedService.equalsIgnoreCase(Constants.SERVICE_ADDRESS_MATCHING) || selectedService.equalsIgnoreCase(Constants.SERVICE_FACE_MATCH)
                 || selectedService.equalsIgnoreCase(Constants.SERVICE_CUSTOMER_VERIFICATION) || selectedService.equalsIgnoreCase(Constants.SERVICE_EMPLOYEE_VERIFICATION) || selectedService.equalsIgnoreCase(Constants.SERVICE_FACE_MATCH_EMPLOYEE_ENROLLMENT) || selectedService.equalsIgnoreCase(Constants.SERVICE_EMPLOYEE_OVERRIDE) || selectedService.equalsIgnoreCase(Constants.SERVICE_CUSTOMER_OVERRIDE) || selectedService.equalsIgnoreCase(Constants.SERVICE_CUSTOMER_SEARCH) || selectedService.equalsIgnoreCase(Constants.SERVICE_EMPLOYEE_SEARCH) || selectedService.equalsIgnoreCase(Constants.SERVICE_SEARCH_CUSTOMER_BIOMETRICS) || selectedService.equalsIgnoreCase(Constants.SERVICE_SEARCH_EMPLOYEE_BIOMETRICS) || selectedService.equalsIgnoreCase(Constants.SERVICE_VIDEOCONFERENCE_FACE_MATCH) || selectedService.equalsIgnoreCase(Constants.SERVICE_OFFLINE_FACE)) {
             ArrayList<String> skipFeaturesList = eVolvApp.getListAdditionalFeatures();

             FragmentManager fragmentManager = getFragmentManager();
             Bundle bundle = new Bundle();
             bundle.putSerializable(IDDetails.SKIP_FEATURES_LIST, skipFeaturesList);

             if (PreferenceUtils.getPreference(getActivity(), AccountSetup.CUSTOM_UI_CONFIG, false)) {
                 CustomizeUIConfiguration customizeUIConfig = new CustomizeUIConfiguration();
                 customizeUIConfig.setArguments(bundle);
                 fragmentManager.beginTransaction().replace(R.id.flContent, customizeUIConfig).addToBackStack(null).commit();
                 NavigationActivity.toolbar.setTitle(getString(R.string.customize_ui_configuration));
             } else {
                 IDValidationFaceMatch idValidationFaceMatch = new IDValidationFaceMatch();
                 idValidationFaceMatch.setArguments(bundle);
                 fragmentManager.beginTransaction().replace(R.id.flContent, idValidationFaceMatch).addToBackStack(null).commit();
                 NavigationActivity.toolbar.setTitle(R.string.id_validation);
             }
         } else {
             FragmentManager fm = getFragmentManager();
             IDDetails idDetails = new IDDetails();
             fm.beginTransaction().replace(R.id.flContent, idDetails).addToBackStack(null).commit();
         }

     }

     @Override
     public void onResume() {
         super.onResume();
     }

     private void disableViewForService(String service) {
         String[] serviceID_ServiceName = service.split(" ", 2);
         int serviceId = Integer.parseInt(serviceID_ServiceName[0]);

         //disable Video View
         if (serviceId == 160 || serviceId == 165 || serviceId == 155) {
             toggleCheckboxDisableVideoView(false);
         } else {
             toggleCheckboxDisableVideoView(true);
         }

         //disable FP View
         if (serviceId == 175 || serviceId == 180 || serviceId == 100 || serviceId == 300 || serviceId == 105 || serviceId == 305 || serviceId == 185 || serviceId == 190) {
             toggleCheckboxDisableFPView(false);
         } else {
             toggleCheckboxDisableFPView(true);
         }

         if (serviceId == 105 || serviceId == 305 || serviceId == 185 || serviceId == 190 || serviceId == 175 || serviceId == 180) {
             toggleCheckboxDisableVoiceView(false);
         } else {
             toggleCheckboxDisableVoiceView(true);
         }

         if (serviceId == 505 || serviceId == 500 || serviceId == 510 || serviceId == 515) {
            toggleCheckboxDisableVoideoConferenceView(false);
         } else {
             toggleCheckboxDisableVoideoConferenceView(true);
         }

         //disable secondary ID View
         if (serviceId == 175 || serviceId == 105 || serviceId == 305 || serviceId == 70 || serviceId == 75 || serviceId == 180 || serviceId == 185 || serviceId == 190) {
             toggleCheckboxDisableSecondaryIDView(false);
         } else {
             toggleCheckboxDisableSecondaryIDView(true);
         }
     }

     private void toggleCheckboxView(boolean enable) {
         if (enable) {
             layoutTransparent.setAlpha((float) 1.0);

         } else {
             layoutTransparent.setAlpha((float) 0.4);
             setCheckboxDisableView(false);
         }
         checkBoxSignature.setEnabled(enable);
         checkBoxDocument.setEnabled(enable);
         checkBoxVoice.setEnabled(enable);
         checkBoxFP.setEnabled(enable);
         checkBoxLocation.setEnabled(enable);
         checkBoxVideo.setEnabled(enable);
         checkBoxSnippet.setEnabled(enable);
         checkBoxFP4F.setEnabled(enable);
         checkBoxBarcode.setEnabled(enable);
         checkBoxCard.setEnabled(enable);
         checkBoxSlant.setEnabled(enable);
         checkBoxVideoConference.setEnabled(enable);
         checkBoxSecondaryIDCaptureFront.setEnabled(enable);
//         checkBoxSecondaryIDCaptureBack.setEnabled(enable);

         imageViewSignature.setEnabled(enable);
         imageViewDocument.setEnabled(enable);
         imageViewVoice.setEnabled(enable);
         imageViewFP.setEnabled(enable);
         imageViewLocation.setEnabled(enable);
         imageViewVideo.setEnabled(enable);
         imageViewSnippet.setEnabled(enable);
         imageViewFP4F.setEnabled(enable);
         imageViewBarcode.setEnabled(enable);
         imageViewCard.setEnabled(enable);
         imageViewSlant.setEnabled(enable);
         imageViewVideoConference.setEnabled(enable);
         imageViewSecondaryIDCaptureFront.setEnabled(enable);
//         imageViewSecondaryIDCaptureBack.setEnabled(enable);

         layoutSignature.setEnabled(enable);
         layoutDocument.setEnabled(enable);
         layoutVoice.setEnabled(enable);
         layoutFingerPrint.setEnabled(enable);
         layoutFingerPrint4F.setEnabled(enable);
         layoutLocation.setEnabled(enable);
         layoutVideo.setEnabled(enable);
         layoutSnippet.setEnabled(enable);
         layoutScanBarcode.setEnabled(enable);
         layoutCard.setEnabled(enable);
         layoutSlant.setEnabled(enable);
         layoutVideoConference.setEnabled(enable);
         layoutSecondaryIdCaptureFront.setEnabled(enable);
//         layoutSecondaryIdCaptureBack.setEnabled(enable);
     }

     private void toggleCheckboxDisableVideoView(boolean enable) {
         if (enable) {
             layoutVideo.setAlpha((float) 1.0);
         } else {
             layoutVideo.setAlpha((float) 0.4);
             checkBoxVideo.setChecked(enable);
         }
         checkBoxVideo.setEnabled(enable);
         imageViewVideo.setEnabled(enable);
         layoutVideo.setEnabled(enable);
     }

     private void toggleCheckboxDisableFPView(boolean enable) {
         if (enable) {
             layoutFingerPrint.setAlpha((float) 1.0);
             layoutFingerPrint4F.setAlpha((float) 1.0);
         } else {
             layoutFingerPrint.setAlpha((float) 0.4);
             layoutFingerPrint4F.setAlpha((float) 0.4);
             checkBoxFP.setChecked(enable);
             checkBoxFP4F.setChecked(enable);
         }
         checkBoxFP.setEnabled(enable);
         imageViewFP.setEnabled(enable);
         layoutFingerPrint.setEnabled(enable);

         checkBoxFP4F.setEnabled(enable);
         imageViewFP4F.setEnabled(enable);
         layoutFingerPrint4F.setEnabled(enable);
     }

     private void toggleCheckboxDisableVoideoConferenceView(boolean enable) {
         if (enable) {
             layoutVideoConference.setAlpha((float) 1.0);
         } else {
             layoutVideoConference.setAlpha((float) 0.4);
             checkBoxVideoConference.setChecked(enable);
         }
         checkBoxVideoConference.setEnabled(enable);
         imageViewVideoConference.setEnabled(enable);
         layoutVideoConference.setEnabled(enable);
     }

     private void toggleCheckboxDisableSecondaryIDView(boolean enable) {
         if (enable) {
             layoutSecondaryIdCaptureFront.setAlpha((float) 1.0);
         } else {
             layoutSecondaryIdCaptureFront.setAlpha((float) 0.4);
             checkBoxSecondaryIDCaptureFront.setChecked(enable);
         }
         checkBoxSecondaryIDCaptureFront.setEnabled(enable);
         imageViewSecondaryIDCaptureFront.setEnabled(enable);
         layoutSecondaryIdCaptureFront.setEnabled(enable);
     }

    private void toggleCheckboxDisableVoiceView(boolean enable) {
        if (enable) {
            layoutVoice.setAlpha((float) 1.0);
        } else {
            layoutVoice.setAlpha((float) 0.4);
            checkBoxVoice.setChecked(enable);
        }
        checkBoxVoice.setEnabled(enable);
        imageViewVoice.setEnabled(enable);
        layoutVoice.setEnabled(enable);
    }

    private void setCheckboxDisableView(boolean enable) {
        checkBoxSignature.setChecked(enable);
        checkBoxDocument.setChecked(enable);
        checkBoxVoice.setChecked(enable);
        checkBoxFP.setChecked(enable);
        checkBoxLocation.setChecked(enable);
        checkBoxVideo.setChecked(enable);
        checkBoxSnippet.setChecked(enable);
        checkBoxFP4F.setChecked(enable);
        checkBoxBarcode.setChecked(enable);
        checkBoxCard.setChecked(enable);
        checkBoxSlant.setChecked(enable);
        checkBoxVideoConference.setChecked(enable);
        checkBoxSecondaryIDCaptureFront.setChecked(enable);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_signature:
                showDialog();
                break;
            case R.id.image_view_document:
                showDialog();
                break;
            case R.id.image_view_voice:
                showDialog();
                break;
            case R.id.image_view_fingerprint:
                showDialog();
                break;
            case R.id.image_view_fingerprint4f:
                showDialog();
                break;
            case R.id.image_view_location:
                showDialog();
                break;
            case R.id.image_view_video:
                showDialog();
                break;
            case R.id.image_view_snippet:
                showDialog();
                break;
            case R.id.image_view_scan_barcode:
                showDialog();
                break;
            case R.id.image_view_card:
                showDialog();
                break;
            case R.id.image_view_videoConference:
                showDialog();
                break;
            case R.id.image_view_IDSecondaryFront:
                showDialog();
                break;
//            case R.id.image_view_IDSecondaryBack:
//                showDialog();
//                break;
            case R.id.button_ok:
                cancelDialog();
                break;
            case R.id.linear_layout_signature:
                if (checkBoxSignature.isChecked()) {
                    checkBoxSignature.setChecked(false);
                } else {
                    checkBoxSignature.setChecked(true);
                }
                break;
            case R.id.linear_layout_document:
                if (checkBoxDocument.isChecked()) {
                    checkBoxDocument.setChecked(false);
                } else {
                    checkBoxDocument.setChecked(true);
                }
                break;
            case R.id.linear_layout_voice:
                if (checkBoxVoice.isChecked()) {
                    checkBoxVoice.setChecked(false);
                } else {
                    checkBoxVoice.setChecked(true);
                }
                break;
            case R.id.linear_layout_fingerprint:
                if (checkBoxFP.isChecked()) {
                    checkBoxFP.setChecked(false);
                } else {
                    checkBoxFP.setChecked(true);
                }
                break;
            case R.id.linear_layout_fingerprint4f:
                if (checkBoxFP4F.isChecked()) {
                    checkBoxFP4F.setChecked(false);
                } else {
                    checkBoxFP4F.setChecked(true);
                }
                break;
            case R.id.linear_layout_location:
                if (checkBoxLocation.isChecked()) {
                    checkBoxLocation.setChecked(false);
                } else {
                    checkBoxLocation.setChecked(true);
                }
                break;
            case R.id.linear_layout_video:
                if (checkBoxVideo.isChecked()) {
                    checkBoxVideo.setChecked(false);
                } else {
                    checkBoxVideo.setChecked(true);
                }
                break;
            case R.id.linear_layout_snippet:
                if (checkBoxSnippet.isChecked()) {
                    checkBoxSnippet.setChecked(false);
                } else {
                    checkBoxSnippet.setChecked(true);
                }
                break;
            case R.id.linear_layout_scan_barcode:
                if (checkBoxBarcode.isChecked()) {
                    checkBoxBarcode.setChecked(false);
                } else {
                    checkBoxBarcode.setChecked(true);
                }
                break;
            case R.id.linear_layout_card:
                if (checkBoxCard.isChecked()) {
                    checkBoxCard.setChecked(false);
                } else {
                    checkBoxCard.setChecked(true);
                }
                break;
            case R.id.linear_layout_slant:
                checkBoxSlant.toggle();
                break;
            case R.id.linear_layout_videoConference:
                checkBoxVideoConference.toggle();
                break;
            case R.id.linear_layout_IDSecondaryFront:
                checkBoxSecondaryIDCaptureFront.toggle();
                break;
//            case R.id.linear_layout_IDSecondaryBack:
//                checkBoxSecondaryIDCaptureBack.toggle();
//                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void showDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.help_custom_dialog);
        dialog.setCancelable(true);
        dialog.show();
        buttonOkey = (Button) dialog.findViewById(R.id.button_ok);

        buttonOkey.setOnClickListener(this);
    }

    private void cancelDialog() {
        if (dialog != null || dialog.isShowing()) {
            dialog.dismiss();
        }
    }

//    public void saveSetting(String key, String value) {
//        PreferenceUtils.setPreference(getActivity(), key, value);
//    }

    public String retrieveSetting(String key, String def) {
        return PreferenceUtils.getPreference(getActivity(), key, def);
    }

    public void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
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

//    @Override
//    public void onInitializationResultAvailable(Map<String, String> resultMap, Response responses) {
//
//        cancelProgressDialog();
//        if(responses != null){
//            if (responses.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()) {
//                if (resultMap != null) {
//                    String companyID = resultMap.get("Company_Id");
//                }
//
//                ImageProcessingSDK.getInstance().deleteData();  //Delete the previously captured images from SDK
//                eVolvApp.clearEvolvImageDirectory();            //Delete the previously captured images from testapp
//                eVolvApp.clearEvolvData();                      //Delete all previously captured data
//
//                generateFeatureListAndContinue();
//            } else{
//                if(resultMap != null){
//                    String errorMsg = resultMap.get("Status_Message");
//                    if(StringUtil.isEmpty(errorMsg)){
//                        errorMsg = "Request unsuccessful";
//                    }
//                    showErrorMessage(getView(), errorMsg);
//                }else {
//                    showErrorMessage(getView(), responses.getStatusMessage());
//                }
//            }
//        }
//    }

    @Override
    public void onExecuteCustomProductCall(Map<String, String> resultMap, Response responses) {

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
    public void onInitializationResultAvailable(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void onVideoConferencingFinished(Map<String, String> resultMap, Response response) {

    }

     @Override
     public void onDownloadXsltResultAvailable(Map<String, String> resultMap, Response response) {

     }

     public void initilizeProgressBar() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    public void cancelProgressDialog() {
        if (null != progressDialog || progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void getServiceList() {
        ArrayList<String> serviceList = ImageProcessingSDK.getInstance().getServiceDetails();
        for (Map.Entry<String, String> serviceMap : getDefaultService().entrySet()) {
            if (serviceList.contains(serviceMap.getKey().split(" ", 2)[0])) {
                services_map.put(serviceMap.getKey(), serviceMap.getValue());
            }
        }
    }

    public LinkedHashMap<String, String> getDefaultService() {
        LinkedHashMap<String, String> services_map = new LinkedHashMap<>();
        services_map.put(getString(R.string.idv_face_match), getString(R.string.idv_face_match_des));
        services_map.put(getString(R.string.idv_only), getString(R.string.idv_only_des));
        services_map.put(getString(R.string.idv_only_cust), getString(R.string.idv_only_cust_des));
        services_map.put(getString(R.string.idv_only_emp), getString(R.string.idv_only_emp_des));
        services_map.put(getString(R.string.idv_face_match_cust), getString(R.string.idv_face_match_cust_des));
        services_map.put(getString(R.string.idv_face_match_emp), getString(R.string.idv_face_match_emp_des));
        services_map.put(getString(R.string.face_match_cus), getString(R.string.face_match_cus_des));
        services_map.put(getString(R.string.face_match_only), getString(R.string.face_match_only_des));
        services_map.put(getString(R.string.customer_update_service), getString(R.string.customer_update_service_des));
        services_map.put(getString(R.string.employee_update_service), getString(R.string.employee_update_service_des));
        services_map.put(getString(R.string.idv_face_match_cust_enroll_77), getString(R.string.idv_face_match_cust_enroll_77_des));
        services_map.put(getString(R.string.idv_face_match_cust_enroll_78), getString(R.string.idv_face_match_cust_enroll_78_des));
        services_map.put(getString(R.string.idv_face_match_cust_minor), getString(R.string.idv_face_match_cust_minor_des));
        services_map.put(getString(R.string.customer_verification), getString(R.string.customer_verification_des));
        services_map.put(getString(R.string.customer_verification_105), getString(R.string.customer_verification_des));
        services_map.put(getString(R.string.idv_video_match), getString(R.string.idv_video_match_des));
        services_map.put(getString(R.string.idv_video_match_cust), getString(R.string.idv_video_match_cust_des));
        services_map.put(getString(R.string.idv_video_match_emp), getString(R.string.idv_video_match_emp_des));
        services_map.put(getString(R.string.cust_enroll_biometrics), getString(R.string.cust_enroll_biometrics_des));
        services_map.put(getString(R.string.emp_enroll_biometrics), getString(R.string.emp_enroll_biometrics_des));
        services_map.put(getString(R.string.identify_cus_with_biometrics), getString(R.string.identify_cus_with_biometrics_des));
        services_map.put(getString(R.string.customer_search), getString(R.string.customer_search_des));
        services_map.put(getString(R.string.identify_emp_with_biometrics), getString(R.string.identify_emp_with_biometrics_des));
        services_map.put(getString(R.string.employee_search), getString(R.string.employee_search_des));
        services_map.put(getString(R.string.address_processing), getString(R.string.address_processing_des));
        services_map.put(getString(R.string.employee_verification), getString(R.string.employee_verification_des));
        services_map.put(getString(R.string.employee_verification_305), getString(R.string.employee_verification_des));
        services_map.put(getString(R.string.face_match_only_emp), getString(R.string.face_match_only_emp_des));
        services_map.put(getString(R.string.create_customer_override_service), getString(R.string.create_customer_override_service_des));
        services_map.put(getString(R.string.create_employee_override_service), getString(R.string.create_employee_override_service_des));
        services_map.put(getString(R.string.id_validation_face_match_primary_id), getString(R.string.id_validation_face_match_primary_id_des));
        services_map.put(getString(R.string.id_validation_face_match_secondary_id), getString(R.string.id_validation_face_match_secondary_id_des));
        services_map.put(getString(R.string.otp_generation), getString(R.string.otp_generation_des));
        services_map.put(getString(R.string.token_generation), getString(R.string.token_generation_des));
        services_map.put(getString(R.string.otp_verification), getString(R.string.otp_verification_des));
        services_map.put(getString(R.string.video_conf_match), getString(R.string.idv_video_conf_match_des));
        services_map.put(getString(R.string.idv_video_conf_match), getString(R.string.idv_video_conf_match_des));
        services_map.put(getString(R.string.idv_video_conf_cust_match), getString(R.string.idv_video_conf_cust_match_des));
        services_map.put(getString(R.string.idv_video_conf_emp_match), getString(R.string.idv_video_conf_emp_match_des));
        services_map.put(getString(R.string.auto_fill_service), getString(R.string.auto_fill_service_des));
        services_map.put(getString(R.string.offline_face_service), getString(R.string.offline_face_service_des));

        return services_map;
    }
}
