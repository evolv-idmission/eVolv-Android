package com.idmission.libtestproject.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.idmission.client.CardImageType;
import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.Response;
import com.idmission.client.ResponseStatusCode;
import com.idmission.client.UIConfigurationParameters;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.utils.BitmapUtils;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class CardCapture extends Fragment implements ImageProcessingResponseListener {
    private ImageView defaultImage, captureImage;
    private TextView textViewDefault, textViewDetailsLabel, textViewDetails;
    private Button buttonCapture, buttonBack, buttonNext, buttonCaptureVision;
    private LinearLayout linearLayoutCapture;
    private boolean isCapture = true;
    private EVolvApp eVolvApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.card_capture_layout, container, false);

        eVolvApp = (EVolvApp) (getActivity().getApplicationContext());

        textViewDefault = (TextView) view.findViewById(R.id.text_view_label);
        textViewDetailsLabel = (TextView) view.findViewById(R.id.text_view_details_label);
        textViewDetails = (TextView) view.findViewById(R.id.text_view_details);
        defaultImage = (ImageView) view.findViewById(R.id.default_image);
        captureImage = (ImageView) view.findViewById(R.id.capture_image);
        buttonCapture = (Button) view.findViewById(R.id.button_capture);
        buttonCaptureVision = (Button) view.findViewById(R.id.button_capture_vision);
        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);
        linearLayoutCapture = (LinearLayout) view.findViewById(R.id.linear_layout_capture);

        textViewDefault.setText(getString(R.string.capture));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        linearLayoutCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCapture) {
                    captureAPICall(true);
                }
            }
        });
        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureAPICall(true);
            }
        });
        buttonCaptureVision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureAPICall(false);
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
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();
                } else {
                    IDValidationFaceMatch.viewPager.setCurrentItem(IDValidationFaceMatch.viewPager.getCurrentItem() - 1);
                }
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void captureAPICall(boolean isEmbossed) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showErrorMessage(getView(), getString(R.string.camera_permission));
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
            }
        } else {
            ImageProcessingSDK.getInstance().setImageProcessingResponseListener(CardCapture.this);
//            JSONObject addJSON = eVolvApp.getAdditonalData();
//            if (null != addJSON) {
//              //  ImageProcessingSDK.getInstance().detectCard(getActivity(), addJSON);
//                ImageProcessingSDK.getInstance().detectCard(getActivity());
//            } else {
//                ImageProcessingSDK.getInstance().detectCard(getActivity());
//            }

            JSONObject cardConfig = new JSONObject();
            try {
                cardConfig.put(UIConfigurationParameters.CARD_IS_EMBOSSED_CARD, isEmbossed ? "Y" : "N");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ImageProcessingSDK.getInstance().detectCard(getActivity(), null, cardConfig);
       }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bitmap bitmap = BitmapUtils.getBitmapFromFile(eVolvApp.getBaseDirectoryPath() + Constants.CARD_CAPTURE_IMAGE);
        setImage(bitmap, eVolvApp.getCardDetails());
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
        Log.d("SDK", "CALLBACK:::: onCardDetectionResultAvailable");
        if (null != response) {
            // showErrorMessage(getView(),response.getStatusMessage());
            if (response.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()) {

                String data = "";
                String cardImageBase64 = null;
                if (null != resultMap && !resultMap.isEmpty()) {
                    for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();

                        if (!key.contains(CardImageType.CARD.toString()))
                            data += key + " : " + value + "\n";
                    }

                    if (resultMap.containsKey(CardImageType.CARD.toString())) {
                        cardImageBase64 = resultMap.get(CardImageType.CARD.toString());
                    }
                }

                eVolvApp.setCardDetails(data);

                if (!StringUtil.isEmpty(cardImageBase64)) {
                    byte[] decodedString = Base64.decode(cardImageBase64, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    BitmapUtils.writeBitmapToFilepath(eVolvApp.getBaseDirectoryPath() + Constants.CARD_CAPTURE_IMAGE, bitmap);
                    setImage(bitmap, data);
                    isCapture = false;
                }
            } else if (response.getStatusCode() == ResponseStatusCode.PERMISSION_NOT_GRANTED.getStatusCode()) {
                showErrorMessage(getView(), getString(R.string.permission_not_granted));
            } else {
                textViewDefault.setVisibility(View.VISIBLE);
                textViewDetailsLabel.setVisibility(View.GONE);
                textViewDetails.setVisibility(View.GONE);
                defaultImage.setVisibility(View.VISIBLE);
//                buttonCapture.setText(getString(R.string.re_capture));
            }
        }
    }

    private void setImage(Bitmap bitmap, String data) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));

        if (bitmap == null) {
//            buttonCapture.setText(getString(R.string.capture));
            return;
        }
        if (!StringUtil.isEmpty(data)) {
            textViewDetails.setText(data);
//            buttonCapture.setText(getString(R.string.re_capture));
        }
        textViewDefault.setVisibility(View.GONE);
        textViewDetailsLabel.setVisibility(View.VISIBLE);
        textViewDetails.setVisibility(View.VISIBLE);
        defaultImage.setVisibility(View.GONE);
        captureImage.setVisibility(View.VISIBLE);
        captureImage.setImageBitmap(bitmap);

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

