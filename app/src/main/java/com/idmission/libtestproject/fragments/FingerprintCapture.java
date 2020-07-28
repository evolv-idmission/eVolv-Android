package com.idmission.libtestproject.fragments;

import android.os.Bundle;
import android.os.Message;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.idmission.client.FingerType;
import com.idmission.client.FingerprintDeviceType;
import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.Response;
import com.idmission.client.ResponseStatusCode;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.adapter.PageAdapter;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class FingerprintCapture extends Fragment implements ImageProcessingResponseListener {

    private static final String[] FINGER_TYPE = {"ALL", "RTHUMB", "RIFINGER", "RMFINGER", "RRFINGER", "RLFINGER",
            "LTHUMB", "LIFINGER", "LMFINGER", "LRFINGER", "LLFINGER", "UNKNOWN"};
    private BottomSheetBehavior bottomSheetBehaviorCaptureId;
    private ImageView bottomSheetImage, defaultImage, captureImage;
    private LinearLayout linearLayout, linearLayoutIndicator, linearLayoutFPClear, linearLayoutCapture;
    private boolean isExpand = false, isCapture = true;
    private TextView textViewDefault;
    private Button buttonBack, buttonNext, buttonCapture, buttonSave, buttonReset, buttonClearFP;
    private EditText minimumNFIQValueEdtTxt, deviceTimeOutEdtTxt, imageSizeEdtTxt;
    private Spinner spinnerFingerprintDevice, spinnerFingerprintType, spinnerFP;
    private static final String FINGER_DEVICE_TIMEOUT = "FINGER_DEVICE_TIMEOUT", FINGER_MIN_IMAGESIZE = "FINGER_MIN_IMAGESIZE", FINGER_MIN_NFIQ = "FINGER_MIN_NFIQ", FINGER_PRINT_DEVICE = "FINGER_PRINT_DEVICE", FINGER_PRINT_TYPE = "FINGER_PRINT_TYPE";
    private PageAdapter adapter;
    private ViewPager viewPagerFP;
    private ImageView[] ivArrayDotsPager;
    private HashMap<String, String> capturedImageData = new HashMap<>();
    private EVolvApp eVolvApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.fingerprint_capture_layout, container, false);

        bottomSheetBehaviorCaptureId = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheetCaptureFingerPrint));
        bottomSheetImage = (ImageView) view.findViewById(R.id.image_view);
        defaultImage = (ImageView) view.findViewById(R.id.default_image);
        captureImage = (ImageView) view.findViewById(R.id.capture_image);
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_top);
        linearLayoutIndicator = (LinearLayout) view.findViewById(R.id.linear_layout_indicator);
        linearLayoutFPClear = (LinearLayout) view.findViewById(R.id.linear_layout_clear_fp);
        linearLayoutCapture = (LinearLayout) view.findViewById(R.id.linear_layout_capture);
        textViewDefault = (TextView) view.findViewById(R.id.text_view_label);
        spinnerFP = (Spinner) view.findViewById(R.id.spinner_finger);
        buttonCapture = (Button) view.findViewById(R.id.button_capture);
        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);
        buttonClearFP = (Button) view.findViewById(R.id.button_clear_fp);

        spinnerFingerprintDevice = (Spinner) view.findViewById(R.id.fingerprint_device_list_spinner);
        spinnerFingerprintType = (Spinner) view.findViewById(R.id.finger_type_spinner);
        minimumNFIQValueEdtTxt = (EditText) view.findViewById(R.id.edit_view_minimum_nfiq_value);
        deviceTimeOutEdtTxt = (EditText) view.findViewById(R.id.edit_view_device_time_out);
        imageSizeEdtTxt = (EditText) view.findViewById(R.id.edit_view_image_size);
        buttonSave = (Button) view.findViewById(R.id.button_save);
        buttonReset = (Button) view.findViewById(R.id.button_reset);

        viewPagerFP = (ViewPager) view.findViewById(R.id.view_pager_fp);

        spinnerFingerprintDevice.setAdapter(new ArrayAdapter<FingerprintDeviceType>(getActivity(), android.R.layout.simple_list_item_1, FingerprintDeviceType.values()));
        spinnerFingerprintType.setAdapter(new ArrayAdapter<FingerType>(getActivity(), android.R.layout.simple_list_item_1, FingerType.values()));

        textViewDefault.setText(getString(R.string.capture));

        eVolvApp = (EVolvApp) getActivity().getApplicationContext();

        spinnerFP.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, FINGER_TYPE));

        setDefaultValue();
        return view;
    }

    private void setDefaultValue() {
        minimumNFIQValueEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FINGER_MIN_NFIQ, "" + ImageProcessingSDK.dFingerprintNFIQValue));
        deviceTimeOutEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FINGER_DEVICE_TIMEOUT, "" + ImageProcessingSDK.dFingerprintDeviceTimeout));
        imageSizeEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FINGER_MIN_IMAGESIZE, "" + ImageProcessingSDK.dFingerprintImageSize));
        //spinnerFingerprintDevice.setSelection(((ArrayAdapter<String>) spinnerFingerprintDevice.getAdapter()).getPosition(PreferenceUtils.getPreference(getActivity(), FINGER_PRINT_DEVICE, "")));
        //spinnerFingerprintType.setSelection(((ArrayAdapter<String>) spinnerFingerprintType.getAdapter()).getPosition(PreferenceUtils.getPreference(getActivity(), FINGER_PRINT_TYPE, "")));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        bottomSheetBehaviorCaptureId.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetImage.setImageResource(R.drawable.expand_arrow);
                } else {
                    bottomSheetImage.setImageResource(R.drawable.collapse_arrow);
                }

                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        isExpand = false;
                        bottomSheetBehaviorCaptureId.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        bottomSheetBehaviorCaptureId.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        isExpand = true;
                        bottomSheetBehaviorCaptureId.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });
        spinnerFingerprintType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buttonCapture.setText(getString(R.string.capture));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerFingerprintDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buttonCapture.setText(getString(R.string.capture));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpand) {
                    bottomSheetBehaviorCaptureId.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehaviorCaptureId.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        linearLayoutCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCapture) {
                    captureFPAPICall();
                }
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String minNFIQValue = minimumNFIQValueEdtTxt.getText().toString().trim();
                String deviceTimeout = deviceTimeOutEdtTxt.getText().toString().trim();
                String imageSize = imageSizeEdtTxt.getText().toString().trim();
                //  String fingerPrintDevice = spinnerFingerprintDevice.getSelectedItem().toString();
                // String fingerPrintType = spinnerFingerprintType.getSelectedItem().toString();

                PreferenceUtils.setPreference(getActivity(), FINGER_MIN_NFIQ, minNFIQValue);
                PreferenceUtils.setPreference(getActivity(), FINGER_DEVICE_TIMEOUT, deviceTimeout);
                PreferenceUtils.setPreference(getActivity(), FINGER_MIN_IMAGESIZE, imageSize);
                // PreferenceUtils.setPreference(getActivity(), FINGER_PRINT_DEVICE, fingerPrintDevice);
                //PreferenceUtils.setPreference(getActivity(), FINGER_PRINT_TYPE, fingerPrintType);
                showErrorMessage(view, getString(R.string.id_capture_save_msg));
                bottomSheetBehaviorCaptureId.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minimumNFIQValueEdtTxt.setText("" + ImageProcessingSDK.dFingerprintNFIQValue);
                deviceTimeOutEdtTxt.setText("" + ImageProcessingSDK.dFingerprintDeviceTimeout);
                imageSizeEdtTxt.setText("" + ImageProcessingSDK.dFingerprintImageSize);
                spinnerFingerprintDevice.setSelection(0);
                spinnerFingerprintType.setSelection(0);
            }
        });
        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureFPAPICall();
            }
        });
        buttonClearFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageProcessingSDK.getInstance().setImageProcessingResponseListener(FingerprintCapture.this);
                if (spinnerFP.getSelectedItem().toString().equalsIgnoreCase("ALL")) {
                    ImageProcessingSDK.getInstance().clearFingerprint(null);
                    eVolvApp.getDeviceResultFPMap().clear();
                    capturedImageData.clear();

                    viewPagerFP.setVisibility(View.GONE);
                    linearLayoutIndicator.setVisibility(View.GONE);
                    defaultImage.setVisibility(View.VISIBLE);
                    textViewDefault.setVisibility(View.VISIBLE);
                    linearLayoutFPClear.setVisibility(View.GONE);
                    buttonCapture.setText(R.string.capture);
                } else {
                    FingerType fingerType = getFPType(spinnerFP.getSelectedItem().toString());
                    ImageProcessingSDK.getInstance().clearFingerprint(fingerType);
                    eVolvApp.removeDeviceFPData(spinnerFP.getSelectedItem().toString());
                }
                onResume();
                adapter.notifyDataSetChanged();
                showErrorMessage(v, getString(R.string.clear_fp_data));
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((IDValidationFaceMatch.listAdditionalFeatures.size() - IDValidationFaceMatch.viewPager.getCurrentItem()) == 1) {
                    FragmentManager fragmentManager = getParentFragment().getFragmentManager();
                    FinalSteps keyView = new FinalSteps();
                    fragmentManager.beginTransaction().replace(R.id.flContent, keyView).addToBackStack(null).commit();
                } else {
                    IDValidationFaceMatch.viewPager.setCurrentItem(IDValidationFaceMatch.viewPager.getCurrentItem() + 1);
                }
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((IDValidationFaceMatch.listAdditionalFeatures.size() - IDValidationFaceMatch.viewPager.getCurrentItem()) == IDValidationFaceMatch.listAdditionalFeatures.size()) {
//                    FragmentManager fragmentManager = getParentFragment().getFragmentManager();
//                    ProcessFlow processFlow = new ProcessFlow();
//                    fragmentManager.beginTransaction().replace(R.id.flContent, processFlow).addToBackStack(null).commit();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();

                } else {
                    IDValidationFaceMatch.viewPager.setCurrentItem(IDValidationFaceMatch.viewPager.getCurrentItem() - 1);
                }
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void captureFPAPICall() {
        String minNFIQValue = PreferenceUtils.getPreference(getActivity(), FINGER_MIN_NFIQ, "" + ImageProcessingSDK.dFingerprintNFIQValue);
        String deviceTimeout = PreferenceUtils.getPreference(getActivity(), FINGER_DEVICE_TIMEOUT, "" + ImageProcessingSDK.dFingerprintDeviceTimeout);
        String imageSize = PreferenceUtils.getPreference(getActivity(), FINGER_MIN_IMAGESIZE, "" + ImageProcessingSDK.dFingerprintImageSize);
        // String fingerPrintDevice = PreferenceUtils.getPreference(getActivity(), FINGER_PRINT_DEVICE, "");
        //String fingerPrintType =PreferenceUtils.getPreference(getActivity(), FINGER_PRINT_TYPE, "");

        FingerprintDeviceType fingerprintDeviceType = (FingerprintDeviceType) spinnerFingerprintDevice.getSelectedItem();
        FingerType fingerType = (FingerType) spinnerFingerprintType.getSelectedItem();

        int nfiqVal = (!StringUtil.isEmpty(minNFIQValue) ? Integer.parseInt(minNFIQValue) : ImageProcessingSDK.dFingerprintNFIQValue);
        int deviceTimeoutVal = (!StringUtil.isEmpty(deviceTimeout) ? Integer.parseInt(deviceTimeout) : ImageProcessingSDK.dFingerprintDeviceTimeout);
        int imageSizeVal = (!StringUtil.isEmpty(imageSize) ? Integer.parseInt(imageSize) : ImageProcessingSDK.dFingerprintImageSize);

        ImageProcessingSDK.getInstance().setImageProcessingResponseListener(FingerprintCapture.this);
        ImageProcessingSDK.getInstance().captureFingerprint(getActivity(), fingerprintDeviceType, fingerType, nfiqVal, deviceTimeoutVal, imageSizeVal);
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
        Log.d("SDK", "CALLBACK:::: onFingerprintCaptureFinished");

        if (null != response) {
            // Toast.makeText(MainActivity.this, "" + response.getStatusMessage(), Toast.LENGTH_SHORT).show();
            if (response.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()) {
                String fingerType = null;
                String fingerprint = null;
                String fingerprintcount = "0";
                for (Map.Entry<String, String> map : resultMap.entrySet()) {
                    if (map.getKey().equals(ImageProcessingSDK.FINGERPRINT)) {
                        fingerprint = map.getValue();
                    }
                    if (map.getKey().equals(ImageProcessingSDK.FINGER_TYPE)) {
                        fingerType = map.getValue();
                    }
                    if (map.getKey().equals(ImageProcessingSDK.FINGERPRINT_COUNT)) {
                        fingerprintcount = map.getValue();
                    }
                }
                if (null != fingerType && null != fingerprint) {
                    String type = fingerType + fingerprintcount;
                    capturedImageData.put(type, fingerprint);
                    eVolvApp.setDeviceResultFPMap(capturedImageData);
                    isCapture = false;
                }
                viewPagerFP.setVisibility(View.VISIBLE);
                linearLayoutIndicator.setVisibility(View.VISIBLE);
                defaultImage.setVisibility(View.GONE);
                textViewDefault.setVisibility(View.GONE);
                buttonCapture.setText(R.string.re_capture);

            } else if (!(response.getStatusCode() == ResponseStatusCode.OPERATION_CANCEL.getStatusCode())){
                buttonCapture.setText(R.string.re_capture);
                showErrorMessage(getView(), response.getStatusMessage());
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (eVolvApp.getDeviceResultFPMap().size() > 0) {
            viewPagerFP.setVisibility(View.VISIBLE);
            linearLayoutIndicator.setVisibility(View.VISIBLE);
            defaultImage.setVisibility(View.GONE);
            textViewDefault.setVisibility(View.GONE);
            buttonCapture.setText(R.string.re_capture);
            linearLayoutFPClear.setVisibility(View.VISIBLE);

            setUpView();
            setTab();
        } else {
            viewPagerFP.setVisibility(View.GONE);
            linearLayoutIndicator.setVisibility(View.GONE);
            defaultImage.setVisibility(View.VISIBLE);
            textViewDefault.setVisibility(View.VISIBLE);
            linearLayoutFPClear.setVisibility(View.GONE);
            buttonCapture.setText(R.string.capture);
        }
    }

    private void setUpView() {
        adapter = new PageAdapter(getChildFragmentManager(), eVolvApp.getDeviceResultFPMap().size());

        for (int i = 0; i < eVolvApp.getDeviceResultFPMap().size(); i++) {
            adapter.addFrag(new FingerPrintViewDevice());
        }

        viewPagerFP.setAdapter(adapter);
        viewPagerFP.setCurrentItem(0);
        setupPagerIndicatorDots();
        ivArrayDotsPager[0].setImageResource(R.drawable.fill_circle);
        sendFingerPrintCaptureData(viewPagerFP.getCurrentItem());
    }

    private void setupPagerIndicatorDots() {
        ivArrayDotsPager = new ImageView[eVolvApp.getDeviceResultFPMap().size()];
        linearLayoutIndicator.removeAllViews();
        for (int i = 0; i < ivArrayDotsPager.length; i++) {
            ivArrayDotsPager[i] = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(5, 20, 5, 25);
            ivArrayDotsPager[i].setLayoutParams(params);
            ivArrayDotsPager[i].setImageResource(R.drawable.holo_circle);
            linearLayoutIndicator.addView(ivArrayDotsPager[i]);
            linearLayoutIndicator.bringToFront();
        }
    }

    private void setTab() {
        viewPagerFP.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {

                sendFingerPrintCaptureData(position);

                for (int i = 0; i < ivArrayDotsPager.length; i++) {
                    ivArrayDotsPager[i].setImageResource(R.drawable.holo_circle);
                }
                ivArrayDotsPager[position].setImageResource(R.drawable.fill_circle);
            }

        });
    }

    private FingerType getFPType(String fingerType) {
//        FingerType[] fingerTypes = FingerType.values();
//        FingerType[] f = new FingerType[1];
//        for (int i = 0; i < fingerTypes.length; i++) {
//            if (fingerTypes[i].name().replaceAll("[0-9]", "").equals(fingerType)) {
//                f[0] = FingerType.values()[i];
//            }
//        }
//        return f[0];
        FingerType[] fingerTypes = FingerType.values();
        FingerType ft = null;
        for (int i = 0; i < fingerTypes.length; i++) {
            if (fingerTypes[i].name().replaceAll("[0-9]", "").equals(fingerType)) {
                ft = fingerTypes[i];
            }
        }
        return ft;
    }

    private void sendFingerPrintCaptureData(int position) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        message.setData(bundle);
        FingerPrintViewDevice.handler.sendMessage(message);
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
}
