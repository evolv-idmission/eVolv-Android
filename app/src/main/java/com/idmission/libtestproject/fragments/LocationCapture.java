package com.idmission.libtestproject.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.Response;
import com.idmission.client.ResponseStatusCode;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import java.util.Map;

public class LocationCapture extends Fragment implements ImageProcessingResponseListener {
    private ImageView defaultImage;
    private TextView textViewDefault, textViewLocationHeading, textViewLat, textViewLong;
    private Button buttonCapture, buttonBack, buttonNext;
    private LinearLayout linearLayoutCapture;
    private boolean isCapture = true;
    private EVolvApp eVolvApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.location_capture_layout, container, false);

        eVolvApp = (EVolvApp) (getActivity().getApplicationContext());

        textViewDefault = (TextView) view.findViewById(R.id.text_view_label);
        textViewLocationHeading = (TextView) view.findViewById(R.id.text_view_location_heading);
        textViewLat = (TextView) view.findViewById(R.id.text_view_latitude);
        textViewLong = (TextView) view.findViewById(R.id.text_view_longitude);
        defaultImage = (ImageView) view.findViewById(R.id.default_image);
        buttonCapture = (Button) view.findViewById(R.id.button_capture);
        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);
        linearLayoutCapture = (LinearLayout) view.findViewById(R.id.linear_layout_capture);

        textViewDefault.setText(getString(R.string.capture));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        linearLayoutCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCapture) {
                    locationCaptureAPICall();
                }
            }
        });
        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationCaptureAPICall();
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

    private void locationCaptureAPICall() {
        ImageProcessingSDK.getInstance().setImageProcessingResponseListener(LocationCapture.this);
        ImageProcessingSDK.getInstance().getGPSCoordinate(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
        setData(eVolvApp.getLocationLat(), eVolvApp.getLocationLong());
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
        Log.d("SDK", "CALLBACK:::: onGPSCoordinateAvailable");
        if (null != response) {
            //Toast.makeText(LocationCapture.this, "GPS : " + response.getStatusMessage(), Toast.LENGTH_SHORT).show();
            //resultTv.setText(response.getStatusMessage());

            if (response.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()) {
                if (null != resultMap) {
                    String latitude = resultMap.get("Latitude");
                    String longitude = resultMap.get("Longitude");

                    eVolvApp.setLocationLat(latitude);
                    eVolvApp.setLocationLong(longitude);
                    setData(latitude, longitude);
                    isCapture = false;
                }
            } else if (response.getStatusCode() == ResponseStatusCode.SOME_ERROR_OCCURRED.getStatusCode()) {
                textViewDefault.setVisibility(View.VISIBLE);
                defaultImage.setVisibility(View.VISIBLE);
                textViewLocationHeading.setVisibility(View.GONE);
                textViewLat.setVisibility(View.GONE);
                textViewLong.setVisibility(View.GONE);
                buttonCapture.setText(getString(R.string.re_capture));

                showErrorMessage(getView(), response.getStatusMessage());
            } else if (response.getStatusCode() == ResponseStatusCode.ENABLE_GPS.getStatusCode()) {
                textViewDefault.setVisibility(View.VISIBLE);
                defaultImage.setVisibility(View.VISIBLE);
                textViewLocationHeading.setVisibility(View.GONE);
                textViewLat.setVisibility(View.GONE);
                textViewLong.setVisibility(View.GONE);
                buttonCapture.setText(getString(R.string.re_capture));

                showErrorMessage(getView(), response.getStatusMessage());
            }else if (response.getStatusCode() == ResponseStatusCode.PERMISSION_NOT_GRANTED.getStatusCode()) {
                showErrorMessage(getView(), getString(R.string.permission_not_granted));
            }
        }
    }

    private void setData(String latitude, String longitude) {
        if (StringUtil.isEmpty(latitude) && StringUtil.isEmpty(longitude)) {
            textViewDefault.setVisibility(View.VISIBLE);
            defaultImage.setVisibility(View.VISIBLE);
            textViewLocationHeading.setVisibility(View.GONE);
            textViewLat.setVisibility(View.GONE);
            textViewLong.setVisibility(View.GONE);
        } else {
            textViewDefault.setVisibility(View.GONE);
            defaultImage.setVisibility(View.GONE);
            textViewLocationHeading.setVisibility(View.VISIBLE);
            textViewLat.setVisibility(View.VISIBLE);
            textViewLong.setVisibility(View.VISIBLE);

           // textViewLat.setText("Latitude:    " + latitude);
            //textViewLong.setText("Longitude:  " + longitude);
            textViewLat.setText(getString(R.string.latitude) +" "+ latitude);
            textViewLong.setText(getString(R.string.longitude)+" "+ longitude);
            buttonCapture.setText(getString(R.string.re_capture));
        }
    }

    public void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
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
