package com.idmission.libtestproject.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.VideoView;

import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.Response;
import com.idmission.client.ResponseStatusCode;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.FileUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import org.json.JSONObject;

import java.util.Map;

public class VideoCapture extends Fragment implements ImageProcessingResponseListener {
    private LinearLayout linearLayout, linearLayoutPlayPause, linearLayoutCapture;
    private ImageView bottomSheetImage, defaultImage;
    private BottomSheetBehavior bottomSheetBehaviorCaptureVideo;
    private boolean isExpand = false, isCapture =true;
    private TextView textViewDefault;
    private EditText editTextVideoRecordingTime, editTextData;
    private Button buttonBack, buttonNext, buttonSave, buttonReset, buttonCapture, buttonPlayPauseVideo;
    private static final String VIDEO_RECORDING_TIME = "VIDEO_RECORDING_TIME", TEXT_DATA = "TEXT_DATA";
    private EVolvApp eVolvApp;
    private FrameLayout frameLayoutVideo;
    private int DEFAULT_VIDEO_RECORDING_TIME = 15;
    private VideoView videoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.video_capture_layout, container, false);

        linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_top);
        linearLayoutCapture = (LinearLayout) view.findViewById(R.id.linear_layout_capture);
        bottomSheetBehaviorCaptureVideo = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheetCaptureVideo));
        bottomSheetImage = (ImageView) view.findViewById(R.id.image_view);
        textViewDefault = (TextView) view.findViewById(R.id.text_view_label);
        defaultImage = (ImageView) view.findViewById(R.id.default_image);
        frameLayoutVideo = (FrameLayout) view.findViewById(R.id.frame_layout);
        videoView = (VideoView) view.findViewById(R.id.video_view);
        buttonPlayPauseVideo = (Button) view.findViewById(R.id.button_play_pause);
        linearLayoutPlayPause = (LinearLayout) view.findViewById(R.id.layout_Play_Pause);
        buttonCapture = (Button) view.findViewById(R.id.button_capture);
        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);

        editTextVideoRecordingTime = (EditText) view.findViewById(R.id.edit_view_video_recording_time);
        editTextData = (EditText) view.findViewById(R.id.edit_view_enter_data);
        buttonSave = (Button) view.findViewById(R.id.button_save);
        buttonReset = (Button) view.findViewById(R.id.button_reset);

        textViewDefault.setText(getString(R.string.capture));
        eVolvApp = (EVolvApp) getActivity().getApplicationContext();

        setDefaultValue();
        return view;
    }

    private void setDefaultValue() {
        editTextVideoRecordingTime.setText(PreferenceUtils.getPreference(getActivity(), VIDEO_RECORDING_TIME, "" + DEFAULT_VIDEO_RECORDING_TIME));
        editTextData.setText(PreferenceUtils.getPreference(getActivity(), TEXT_DATA, ""));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        bottomSheetBehaviorCaptureVideo.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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
                        bottomSheetBehaviorCaptureVideo.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        bottomSheetBehaviorCaptureVideo.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        isExpand = true;
                        bottomSheetBehaviorCaptureVideo.setState(BottomSheetBehavior.STATE_EXPANDED);
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
                    bottomSheetBehaviorCaptureVideo.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehaviorCaptureVideo.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        linearLayoutCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCapture) {
                    videoCaptureAPICall();
                }
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoRecordingTime = editTextVideoRecordingTime.getText().toString().trim();
                String data = editTextData.getText().toString().trim();

                PreferenceUtils.setPreference(getActivity(), VIDEO_RECORDING_TIME, videoRecordingTime);
                PreferenceUtils.setPreference(getActivity(), TEXT_DATA, data);

                showErrorMessage(view, getString(R.string.id_capture_save_msg));
                bottomSheetBehaviorCaptureVideo.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextVideoRecordingTime.setText("");
                editTextData.setText("");

                PreferenceUtils.setPreference(getActivity(), VIDEO_RECORDING_TIME, "");
                PreferenceUtils.setPreference(getActivity(), TEXT_DATA, "");
            }
        });
        buttonPlayPauseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    buttonPlayPauseVideo.setBackground(getResources().getDrawable(R.drawable.play_icon));
                } else {
                    videoView.start();
                    buttonPlayPauseVideo.setBackground(getResources().getDrawable(R.drawable.pause_icon));
                }
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                buttonPlayPauseVideo.setBackground(getResources().getDrawable(R.drawable.play_icon));
            }
        });

        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoCaptureAPICall();
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

    private void videoCaptureAPICall() {
        ImageProcessingSDK.getInstance().setImageProcessingResponseListener(VideoCapture.this);
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
            String videoRecordingTime = PreferenceUtils.getPreference(getActivity(), VIDEO_RECORDING_TIME, "" + DEFAULT_VIDEO_RECORDING_TIME);
            String data = PreferenceUtils.getPreference(getActivity(), TEXT_DATA, "");

            if (!StringUtil.isEmpty(videoRecordingTime)) {
                frameLayoutVideo.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                ImageProcessingSDK.getInstance().startVideoRecording(getActivity(), Integer.parseInt(videoRecordingTime), addJSON, data);
            } else {
                showErrorMessage(getView(), "Please enter time");
            }
        }
    }

    @Override
    public void onResume() {
        setVideo(eVolvApp.getVideoResult());
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
        Log.d("SDK", "CALLBACK:::: onVideoRecordingFinished");
        // showErrorMessage(getView(), response.getStatusMessage());
        if (response.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()) {
            // Toast.makeText(MainActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();
            if (null != resultMap) {
                //String filePath = resultMap.get("FILEPATH");
                if (null != response) {
                    FileUtils.deleteFile(eVolvApp.getBaseDirectoryPath() + Constants.VIDEO_RECORDING_DATA);
                    FileUtils.convertStringToMp3File(eVolvApp.getBaseDirectoryPath() + Constants.VIDEO_RECORDING_DATA, resultMap.get("DATA"));

                    // eVolvApp.setVideoResult(filePath);
                    eVolvApp.setVideoResult(eVolvApp.getBaseDirectoryPath() + Constants.VIDEO_RECORDING_DATA);
                    setVideo(eVolvApp.getBaseDirectoryPath() + Constants.VIDEO_RECORDING_DATA);
                    isCapture = false;
                }
            } else {
                frameLayoutVideo.setVisibility(View.GONE);
                defaultImage.setVisibility(View.VISIBLE);
                textViewDefault.setVisibility(View.VISIBLE);
            }
        }else if (!(response.getStatusCode() == ResponseStatusCode.OPERATION_CANCEL.getStatusCode())){
            showErrorMessage(getView(), response.getStatusMessage());
        }else if ((response.getStatusCode() == ResponseStatusCode.PERMISSION_NOT_GRANTED.getStatusCode())){
            showErrorMessage(getView(), getString(R.string.permission_not_granted));
        }
    }

    private void setVideo(String filePath) {
        if (!StringUtil.isEmpty(filePath)) {
            frameLayoutVideo.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            textViewDefault.setVisibility(View.GONE);
            defaultImage.setVisibility(View.GONE);
            buttonCapture.setText(getString(R.string.re_capture));

            Uri uri = Uri.parse(filePath);
            videoView.setVideoURI(uri);
            // videoView.requestFocus();
            videoView.start();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    videoView.pause();
                    FrameLayout.LayoutParams buttonParams = (FrameLayout.LayoutParams) linearLayoutPlayPause.getLayoutParams();
                    buttonParams.width = videoView.getWidth();
                    linearLayoutPlayPause.setLayoutParams(buttonParams);
                    linearLayoutPlayPause.setX(videoView.getX());
                }
            }, 500);
        }
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
