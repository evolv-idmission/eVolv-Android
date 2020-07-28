package com.idmission.libtestproject.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.Response;
import com.idmission.client.ResponseStatusCode;
import com.idmission.client.UIConfigurationParameters;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class VideoConferenceFragment extends Fragment implements ImageProcessingResponseListener{

    private LinearLayout linearLayout, linearLayoutPlayPause, linearLayoutCapture;
    private ImageView bottomSheetImage, defaultImage;
    private BottomSheetBehavior bottomSheetBehaviorVideoConf;
    private boolean isExpand = false, isCapture =true;
    private TextView textViewDefault;
    private EditText editTextVideoConferenceWidth, editTextVideoConferenceHeight, editTextVideoConferenceFramerate;
    private Button buttonBack, buttonNext, buttonSave, buttonReset, buttonCapture;
    private static final String VIDEO_CONFERENCE_WIDTH = "VIDEO_CONFERENCE_WIDTH", VIDEO_CONFERENCE_HEIGHT = "VIDEO_CONFERENCE_HEIGHT", VIDEO_CONFERENCE_FRAMERATE = "VIDEO_CONFERENCE_FRAMERATE";
    private EVolvApp eVolvApp;
    private FrameLayout frameLayoutVideo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.fragment_video_conference, container, false);

        linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_top);
        linearLayoutCapture = (LinearLayout) view.findViewById(R.id.linear_layout_capture);
        bottomSheetBehaviorVideoConf = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheetSelfie));
        bottomSheetImage = (ImageView) view.findViewById(R.id.image_view);
        textViewDefault = (TextView) view.findViewById(R.id.text_view_label);
        defaultImage = (ImageView) view.findViewById(R.id.default_image);
        frameLayoutVideo = (FrameLayout) view.findViewById(R.id.frame_layout);
        linearLayoutPlayPause = (LinearLayout) view.findViewById(R.id.layout_Play_Pause);
        buttonCapture = (Button) view.findViewById(R.id.button_capture);
        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);

        editTextVideoConferenceWidth = (EditText) view.findViewById(R.id.vdo_conference_width_ET);
        editTextVideoConferenceHeight = (EditText) view.findViewById(R.id.vdo_conference_height_ET);
        editTextVideoConferenceFramerate = (EditText) view.findViewById(R.id.vdo_conference_framerate_ET);
        buttonSave = (Button) view.findViewById(R.id.button_save);
        buttonReset = (Button) view.findViewById(R.id.button_reset);

        textViewDefault.setText(getString(R.string.initiate));
        eVolvApp = (EVolvApp) getActivity().getApplicationContext();

        setDefaultValue();
        return view;
    }

    private void setDefaultValue() {
        editTextVideoConferenceWidth.setText(PreferenceUtils.getPreference(getActivity(), VIDEO_CONFERENCE_WIDTH, "" + ImageProcessingSDK.dVideoConferenceWidth));
        editTextVideoConferenceHeight.setText(PreferenceUtils.getPreference(getActivity(), VIDEO_CONFERENCE_HEIGHT, "" + ImageProcessingSDK.dVideoConferenceHeight));
        editTextVideoConferenceFramerate.setText(PreferenceUtils.getPreference(getActivity(), VIDEO_CONFERENCE_FRAMERATE, "" + ImageProcessingSDK.dVideoConferenceFrameRate));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        bottomSheetBehaviorVideoConf.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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
                        bottomSheetBehaviorVideoConf.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        bottomSheetBehaviorVideoConf.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        isExpand = true;
                        bottomSheetBehaviorVideoConf.setState(BottomSheetBehavior.STATE_EXPANDED);
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
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpand) {
                    bottomSheetBehaviorVideoConf.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehaviorVideoConf.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        linearLayoutCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCapture) {
                    videoConferenceAPICall();
                }
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String width = editTextVideoConferenceWidth.getText().toString().trim();
                String height = editTextVideoConferenceHeight.getText().toString().trim();
                String framerate = editTextVideoConferenceFramerate.getText().toString().trim();

                PreferenceUtils.setPreference(getActivity(), VIDEO_CONFERENCE_WIDTH, width);
                PreferenceUtils.setPreference(getActivity(), VIDEO_CONFERENCE_HEIGHT, height);
                PreferenceUtils.setPreference(getActivity(), VIDEO_CONFERENCE_FRAMERATE, framerate);

                showErrorMessage(view, getString(R.string.id_capture_save_msg));
                bottomSheetBehaviorVideoConf.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextVideoConferenceWidth.setText("" + ImageProcessingSDK.dVideoConferenceWidth);
                editTextVideoConferenceHeight.setText("" + ImageProcessingSDK.dVideoConferenceHeight);
                editTextVideoConferenceFramerate.setText("" + ImageProcessingSDK.dVideoConferenceFrameRate);
            }
        });

        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoConferenceAPICall();
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

    private void videoConferenceAPICall() {
        ImageProcessingSDK.getInstance().setImageProcessingResponseListener(VideoConferenceFragment.this);
        JSONObject addJSON = eVolvApp.getAdditonalData();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showErrorMessage(getView(), getString(R.string.camera_permission));
            }
        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO}, 0);
        } else {
            frameLayoutVideo.setVisibility(View.GONE);

            String width = PreferenceUtils.getPreference(getActivity(), VIDEO_CONFERENCE_WIDTH, "" + ImageProcessingSDK.dVideoConferenceWidth);
            String height = PreferenceUtils.getPreference(getActivity(), VIDEO_CONFERENCE_HEIGHT, "" + ImageProcessingSDK.dVideoConferenceHeight);
            String framerate = PreferenceUtils.getPreference(getActivity(), VIDEO_CONFERENCE_FRAMERATE, "" + ImageProcessingSDK.dVideoConferenceFrameRate);

            JSONObject commonUIObject = new JSONObject();
            try {
                commonUIObject.put(UIConfigurationParameters.VDOCONF_WIDTH,width+"");
                commonUIObject.put(UIConfigurationParameters.VDOCONF_HEIGHT,height+"");
                commonUIObject.put(UIConfigurationParameters.VDOCONF_FRAMERATE,framerate+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ImageProcessingSDK.getInstance().startVideoConferencing(getActivity(), commonUIObject);

        }
    }

    @Override
    public void onResume() {
//        setVideo(eVolvApp.getVideoResult());
        super.onResume();
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
//        Log.d("SDK", "CALLBACK:::: onVideoRecordingFinished");
//        // showErrorMessage(getView(), response.getStatusMessage());
//        if (response.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()) {
//            // Toast.makeText(MainActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();
//            if (null != resultMap) {
//                String filePath = resultMap.get("FILEPATH");
//                if (null != response) {
//                    eVolvApp.setVideoResult(filePath);
//                    setVideo(filePath);
//                    isCapture = false;
//                }
//            } else {
//                frameLayoutVideo.setVisibility(View.GONE);
//                defaultImage.setVisibility(View.VISIBLE);
//                textViewDefault.setVisibility(View.VISIBLE);
//            }
//        }else if (!(response.getStatusCode() == ResponseStatusCode.OPERATION_CANCEL.getStatusCode())){
//            showErrorMessage(getView(), response.getStatusMessage());
//        }
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
    public void onInitializationResultAvailable(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void genericApiCallResponse(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void onVideoConferencingFinished(Map<String, String> resultMap, Response response) {
        Log.d("","Video Conference Finished");
        if(null != response) {

            final Response mresponse = response;

            if(mresponse.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()) {
                buttonCapture.setEnabled(false);
                buttonCapture.setAlpha(0.2f);
                linearLayoutCapture.setEnabled(false);
                linearLayoutCapture.setAlpha(0.4f);
                textViewDefault.setText(getString(R.string.conference_completed));
            }else if (response.getStatusCode() == ResponseStatusCode.PERMISSION_NOT_GRANTED.getStatusCode()) {
                showErrorMessage(getView(), getString(R.string.permission_not_granted));
            } else {
                buttonCapture.setEnabled(true);
                buttonCapture.setAlpha(1f);
                linearLayoutCapture.setEnabled(true);
                linearLayoutCapture.setAlpha(1f);
                textViewDefault.setText(getString(R.string.conference_incompleted));
            }

        }
    }

    @Override
    public void onDownloadXsltResultAvailable(Map<String, String> resultMap, Response response) {

    }

}
