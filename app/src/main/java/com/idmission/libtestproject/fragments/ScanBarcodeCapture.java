package com.idmission.libtestproject.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
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
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.utils.BitmapUtils;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import org.json.JSONObject;

import java.util.Map;

public class ScanBarcodeCapture extends Fragment implements ImageProcessingResponseListener {
    private ImageView captureImage, defaultImage;
    private TextView textViewDefault, textViewDetailsLabel, textViewDetails;
    private Button buttonBack, buttonNext, buttonCapture;
    private LinearLayout linearLayoutCapture;
    private boolean isCapture = true;
    private EVolvApp eVolvApp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.scan_barcode_layout, container, false);

        eVolvApp = (EVolvApp) (getActivity().getApplicationContext());

        textViewDefault = (TextView) view.findViewById(R.id.text_view_label);
        textViewDetailsLabel = (TextView) view.findViewById(R.id.text_view_details_label);
        textViewDetails = (TextView) view.findViewById(R.id.text_view_details);
        defaultImage = (ImageView) view.findViewById(R.id.default_image);
        captureImage = (ImageView) view.findViewById(R.id.capture_image);
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
                    scanBarcodeCaptureAPICall();
                }
            }
        });
        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarcodeCaptureAPICall();
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

    private void scanBarcodeCaptureAPICall() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showErrorMessage(getView(), "Need to enable CAMERA permission.");
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
            }
        } else {
            ImageProcessingSDK.getInstance().setImageProcessingResponseListener(ScanBarcodeCapture.this);
            JSONObject addJSON = eVolvApp.getAdditonalData();
            ImageProcessingSDK.getInstance().scanBarcode(getActivity(), addJSON);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));

        Bitmap bitmap = BitmapUtils.getBitmapFromFile(eVolvApp.getBaseDirectoryPath() + Constants.SCAN_BARCODE_IMAGE);
        setImage(bitmap, eVolvApp.getBarcodeDetails());
    }

    public void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
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
        Log.d("SDK", "CALLBACK:::: onScanBarcodeFinished");
        if (null != response) {
            if (response.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()) {
                // Toast.makeText(MainActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();
                //showErrorMessage(getView(), response.getStatusMessage());
                String imageBase64 = null;
                String data = "";
                if (null != resultMap && !resultMap.isEmpty()) {

                    for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if (!key.equals("defaultBarcodeImage")) {
                            data += key + " : " + value + "\n";
                        }else {
                            imageBase64 = resultMap.get("defaultBarcodeImage");
                            if (!StringUtil.isEmpty(imageBase64)) {

                                byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                BitmapUtils.writeBitmapToFilepath(eVolvApp.getBaseDirectoryPath() + Constants.SCAN_BARCODE_IMAGE, bitmap);
                                setImage(bitmap, data);
                                isCapture = false;
                            }
                        }

                    }

                    eVolvApp.setBarcodeDetails(data);

//                    if(!StringUtil.isEmpty(data)) {
//                        textViewDetails.setText(data);
//                        textViewDetails.setMovementMethod(new ScrollingMovementMethod());
//                    }

//                    if (resultMap.containsKey("defaultBarcodeImage")) {
//                        imageBase64 = resultMap.get("defaultBarcodeImage");
//                        if (!StringUtil.isEmpty(imageBase64)) {
//
//                            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
//                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//
//                            BitmapUtils.writeBitmapToFilepath(eVolvApp.getBaseDirectoryPath() + Constants.SCAN_BARCODE_IMAGE, bitmap);
//                            setImage(bitmap);
//                            isCapture = false;
//                        }
//
//                    }
                }


            } else if (response.getStatusCode() == ResponseStatusCode.PERMISSION_NOT_GRANTED.getStatusCode()) {
                showErrorMessage(getView(), getString(R.string.permission_not_granted));
            } else if (!(response.getStatusCode() == ResponseStatusCode.OPERATION_CANCEL.getStatusCode())) {
                buttonCapture.setText(R.string.re_capture);
                textViewDetailsLabel.setVisibility(View.GONE);
                textViewDetails.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onCaptureSignatureFinished(Map<String, String> resultMap, Response responses) {

    }

    private void setImage(Bitmap bitmap, String data) {
        if (bitmap == null) {
            return;
        }

        if(!StringUtil.isEmpty(data)) {
            textViewDetails.setText(data);
            textViewDetails.setMovementMethod(new ScrollingMovementMethod());
        }

        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        textViewDefault.setVisibility(View.GONE);
        defaultImage.setVisibility(View.GONE);
        captureImage.setVisibility(View.VISIBLE);
        textViewDetailsLabel.setVisibility(View.VISIBLE);
        textViewDetails.setVisibility(View.VISIBLE);
        captureImage.setImageBitmap(bitmap);
        buttonCapture.setText(getString(R.string.re_capture));
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

