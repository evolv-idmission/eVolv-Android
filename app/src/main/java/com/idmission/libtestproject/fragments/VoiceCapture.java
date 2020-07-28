package com.idmission.libtestproject.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.FileUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import org.json.JSONObject;

import java.util.Map;

public class VoiceCapture extends Fragment implements ImageProcessingResponseListener {
    private EditText editTextVoiceRecordingTime;
    private ImageView bottomSheetImage, voiceRecordingPlayerImage, defaultImage;
    private LinearLayout linearLayout, linearLayoutCapture;
    private BottomSheetBehavior bottomSheetBehaviorCaptureVoice;
    private boolean isExpand = false, isCapture = true;
    private TextView textViewDefault;
    private Button buttonBack, buttonNext, buttonSave, buttonReset, buttonCapture;
    private static final String VOICE_RECORDING_TIME = "VOICE_RECORDING_TIME";
    private MediaPlayer mediaPlayer;
    private int DEFAULT_VOICE_RECORDING_TIME = 15;
    private EVolvApp eVolvApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.voice_capture_layout, container, false);
        eVolvApp = (EVolvApp) getActivity().getApplicationContext();

        linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_top);
        linearLayoutCapture = (LinearLayout) view.findViewById(R.id.linear_layout_capture);
        bottomSheetBehaviorCaptureVoice = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheetCaptureVoice));
        bottomSheetImage = (ImageView) view.findViewById(R.id.image_view);
        voiceRecordingPlayerImage = (ImageView) view.findViewById(R.id.voice_recording_player);
        defaultImage = (ImageView) view.findViewById(R.id.default_image);
        textViewDefault = (TextView) view.findViewById(R.id.text_view_label);
        buttonCapture = (Button) view.findViewById(R.id.button_capture);
        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);

        editTextVoiceRecordingTime = (EditText) view.findViewById(R.id.edit_view_voice_recording_time);
        buttonSave = (Button) view.findViewById(R.id.button_save);
        buttonReset = (Button) view.findViewById(R.id.button_reset);

        textViewDefault.setText(getString(R.string.capture));

        setDefaultValue();

        return view;
    }

    private void setDefaultValue() {
        editTextVoiceRecordingTime.setText(PreferenceUtils.getPreference(getActivity(), VOICE_RECORDING_TIME, "" + DEFAULT_VOICE_RECORDING_TIME));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        bottomSheetBehaviorCaptureVoice.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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
                        bottomSheetBehaviorCaptureVoice.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        bottomSheetBehaviorCaptureVoice.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        isExpand = true;
                        bottomSheetBehaviorCaptureVoice.setState(BottomSheetBehavior.STATE_EXPANDED);
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
                    bottomSheetBehaviorCaptureVoice.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehaviorCaptureVoice.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        linearLayoutCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCapture) {
                    voiceCaptureAPICall();
                }
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String voiceRecordingTime = editTextVoiceRecordingTime.getText().toString().trim();

                PreferenceUtils.setPreference(getActivity(), VOICE_RECORDING_TIME, voiceRecordingTime);

                showErrorMessage(view, getString(R.string.id_capture_save_msg));
                bottomSheetBehaviorCaptureVoice.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextVoiceRecordingTime.setText("");
                PreferenceUtils.setPreference(getActivity(), VOICE_RECORDING_TIME, "");
            }
        });
        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        voiceRecordingPlayerImage.setImageResource(R.drawable.play_recording);
                    }
                }

                voiceCaptureAPICall();
            }
        });
        voiceRecordingPlayerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        voiceRecordingPlayerImage.setImageResource(R.drawable.play_recording);
                    } else {
                        mediaPlayer.start();
                        voiceRecordingPlayerImage.setImageResource(R.drawable.pause_recording);
                    }
                }
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

    private void voiceCaptureAPICall() {
        JSONObject addJSON = eVolvApp.getAdditonalData();
        ImageProcessingSDK.getInstance().setImageProcessingResponseListener(VoiceCapture.this);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        } else {
            String voiceRecordingTime = PreferenceUtils.getPreference(getActivity(), VOICE_RECORDING_TIME, "" + DEFAULT_VOICE_RECORDING_TIME);
            if (!StringUtil.isEmpty(voiceRecordingTime)) {
//                        imageProcessingSDK.startVoiceRecording(MainActivity.this, Integer.parseInt(voiceRecordingTimingEdtTxt.getText().toString()));
                //ImageProcessingSDK.getInstance().startVoiceRecording(getActivity(), Integer.parseInt(voiceRecordingTime), true, addJSON);
                try {
                    JSONObject commonUIObject = new JSONObject();
                    commonUIObject.put(UIConfigurationParameters.VOICE_RECORDING_TIME, voiceRecordingTime);

                    ImageProcessingSDK.getInstance().startVoiceRecording(getActivity(), commonUIObject, addJSON);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showErrorMessage(getView(), getString(R.string.enter_time));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setVoice(eVolvApp.getVoiceResult());
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
        Log.d("SDK", "CALLBACK:::: onVoiceRecordingFinished");
        if (null != response) {
            if (response.getStatusCode() == ResponseStatusCode.VOICE_RECORDING_ALREADY_RUNNING.getStatusCode()) {
                // showErrorMessage(getView(), "Failed : " + response.getStatusMessage());
                // Toast.makeText(MainActivity.this, "Failed : "+response.getStatusMessage(), Toast.LENGTH_SHORT).show();
                textViewDefault.setVisibility(View.VISIBLE);
                defaultImage.setVisibility(View.VISIBLE);
                voiceRecordingPlayerImage.setVisibility(View.GONE);
            } else if (response.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()) {
                //showErrorMessage(getView(), "Success : " + response.getStatusMessage());
                //showErrorMessage(getView(), getString(R.string.success)+ " : " + response.getStatusMessage());
                FileUtils.deleteFile(eVolvApp.getBaseDirectoryPath() + Constants.VOICE_RECORDING_DATA);
                FileUtils.convertStringToMp3File(eVolvApp.getBaseDirectoryPath() + Constants.VOICE_RECORDING_DATA, resultMap.get("DATA"));
                //eVolvApp.setVoiceResult(resultMap.get("FILEPATH"));
                //setVoice(resultMap.get("FILEPATH"));
                eVolvApp.setVoiceResult(eVolvApp.getBaseDirectoryPath() + Constants.VOICE_RECORDING_DATA);
                setVoice(eVolvApp.getBaseDirectoryPath() + Constants.VOICE_RECORDING_DATA);
                isCapture = false;
            } else if (response.getStatusCode() == ResponseStatusCode.OPERATION_CANCEL.getStatusCode()) {
                textViewDefault.setVisibility(View.VISIBLE);
                defaultImage.setVisibility(View.VISIBLE);
                voiceRecordingPlayerImage.setVisibility(View.GONE);
                //showErrorMessage(getView(), getString(R.string.cancel)+ " : " + response.getStatusMessage());
            } else if (response.getStatusCode() == ResponseStatusCode.PERMISSION_NOT_GRANTED.getStatusCode()) {
                showErrorMessage(getView(), getString(R.string.permission_not_granted));
            }
            //resultTv.setText(response.getStatusMessage());
        }
    }

    private void setVoice(String filePath) {
        if (!(StringUtil.isEmpty(filePath))) {
            voiceRecordingPlayerImage.setVisibility(View.VISIBLE);
            textViewDefault.setVisibility(View.GONE);
            defaultImage.setVisibility(View.GONE);
            buttonCapture.setText(getString(R.string.re_capture));

            mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(filePath));
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    voiceRecordingPlayerImage.setImageResource(R.drawable.play_recording);
                }
            });
        }
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
