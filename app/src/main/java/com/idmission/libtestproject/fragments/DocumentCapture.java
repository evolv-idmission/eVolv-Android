package com.idmission.libtestproject.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.ImageType;
import com.idmission.client.Response;
import com.idmission.client.ResponseStatusCode;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.adapter.SpinnerAdapterForPair;
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.classes.UIConfigConstants;
import com.idmission.libtestproject.utils.BitmapUtils;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class DocumentCapture extends Fragment implements ImageProcessingResponseListener {

    //private static final String[] TITLE_IMAGE = {"None", "Title Image 1"};
    private BottomSheetBehavior bottomSheetBehaviorCaptureId;
    private ImageView bottomSheetImage, defaultImage, captureImage;
    private LinearLayout linearLayout, linearLayoutCapture;
    private boolean isExpand = false, isCapture = true;
    private TextView textViewDefault;
    private Button buttonCapture, buttonBack, buttonNext, buttonSave, buttonReset;
    private EditText editTextFieldName, lightThresholdEdtTxt, maxFocusThresholdEdtTxt, minFocusThresholdEdtTxt, glarePercentageEdtTxt,
            enableCaptureButtonTimeEdtTxt, maxImageSizeEdtTxt, imageHeightEdtTxt, imageWidthEdtTxt, idOutlineColorEdtTxt, idOutlineAlphaEdtTxt,
            detectedIdOutlineColorEdtTxt, detectedIdOutlineAlphaEdtTxt, idOutsideOutlineColorEdtTxt, idOutsideOutlineAlphaEdtTxt, detectedOutsideOutlineColorEdtTxt, detectedOutsideOutlineAlphaEdtTxt;
    private static final String IMAGE_LIGHT_THRESHOLD_POA = "IMAGE_LIGHT_THRESHOLD_POA", IMAGE_MAX_FOCUS_THRESHOLD_POA = "IMAGE_MAX_FOCUS_THRESHOLD_POA", IMAGE_MIN_FOCUS_THRESHOLD_POA = "IMAGE_MIN_FOCUS_THRESHOLD_POA",
            IMAGE_GLARE_PERCENTAGE_POA = "IMAGE_GLARE_PERCENTAGE_POA", CAPTURE_BUTTON_TIME_POA = "CAPTURE_BUTTON_TIME_POA", MAX_IMAGE_SIZE_POA = "MAX_IMAGE_SIZE_POA",
            IMAGE_HEIGHT_POA = "IMAGE_HEIGHT_POA", IMAGE_WIDTH_POA = "IMAGE_WIDTH_POA", ID_IMG_OUTLINE_COLOR_POA = "ID_IMG_OUTLINE_COLOR_POA", ID_IMG_OUTLINE_ALPHA_POA = "ID_IMG_OUTLINE_ALPHA_POA",
            DETECTED_ID_IMG_OUTLINE_COLOR_POA = "DETECTED_ID_IMG_OUTLINE_COLOR_POA", DETECTED_ID_IMG_OUTLINE_ALPHA_POA = "DETECTED_ID_IMG_OUTLINE_ALPHA_POA", ID_IMG_OUTSIDE_OUTLINE_COLOR_POA = "ID_IMG_OUTSIDE_OUTLINE_COLOR_POA", ID_IMG_OUTSIDE_OUTLINE_ALPHA_POA = "ID_IMG_OUTSIDE_OUTLINE_ALPHA_POA",
            DETECTED_ID_IMG_OUTSIDE_OUTLINE_POA = "DETECTED_ID_IMG_OUTSIDE_OUTLINE_POA", DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_POA = "DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_POA", SELECT_ID_TITLE_IMG_POA = "SELECT_ID_TITLE_IMG_POA", CAPTURE_PORTRAIT_MODE_POA = "CAPTURE_PORTRAIT_MODE_POA", SHOW_INSTRUCTION_POA = "SHOW_INSTRUCTION_POA";
    private RadioGroup radioGroupOrientation, radioGroupInstruction;
    private Spinner spinnerTitle;
    private EVolvApp eVolvApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.document_capture_layout, container, false);
        bottomSheetBehaviorCaptureId = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheetCaptureId));
        bottomSheetImage = (ImageView) view.findViewById(R.id.image_view);
        defaultImage = (ImageView) view.findViewById(R.id.default_image);
        captureImage = (ImageView) view.findViewById(R.id.capture_image);
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_top);
        linearLayoutCapture = (LinearLayout) view.findViewById(R.id.linear_layout_capture);
        textViewDefault = (TextView) view.findViewById(R.id.text_view_label);
        buttonCapture = (Button) view.findViewById(R.id.button_capture);
        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);

        editTextFieldName = (EditText) view.findViewById(R.id.edit_text_field_name);
        lightThresholdEdtTxt = (EditText) view.findViewById(R.id.light_threshold);
        maxFocusThresholdEdtTxt = (EditText) view.findViewById(R.id.max_focus_threshold);
        minFocusThresholdEdtTxt = (EditText) view.findViewById(R.id.min_focus_threshold);
        glarePercentageEdtTxt = (EditText) view.findViewById(R.id.glare_percentage);
        enableCaptureButtonTimeEdtTxt = (EditText) view.findViewById(R.id.enable_capture_button);
        maxImageSizeEdtTxt = (EditText) view.findViewById(R.id.max_image_size);
        imageHeightEdtTxt = (EditText) view.findViewById(R.id.image_height);
        imageWidthEdtTxt = (EditText) view.findViewById(R.id.image_width);
        idOutlineColorEdtTxt = (EditText) view.findViewById(R.id.id_outline_color);
        idOutlineAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_id_outline_color);
        detectedIdOutlineColorEdtTxt = (EditText) view.findViewById(R.id.detected_id_outline_color);
        detectedIdOutlineAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_detected_id_outline_color);
        idOutsideOutlineColorEdtTxt = (EditText) view.findViewById(R.id.color_outside_outline);
        idOutsideOutlineAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_color_outside_outline);
        detectedOutsideOutlineColorEdtTxt = (EditText) view.findViewById(R.id.detected_color_outside_outline);
        detectedOutsideOutlineAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_detected_color_outside_outline);
        radioGroupOrientation = (RadioGroup) view.findViewById(R.id.radio_group_orientation);
        radioGroupInstruction = (RadioGroup) view.findViewById(R.id.radio_group_instruction);
        spinnerTitle = (Spinner) view.findViewById(R.id.spinner_title);
        buttonSave = (Button) view.findViewById(R.id.button_save);
        buttonReset = (Button) view.findViewById(R.id.button_reset);

        ArrayList<Pair<String,String>> titleList = new ArrayList<Pair<String,String>>();
        titleList.add(new Pair<String, String>(getString(R.string.none),"None"));
        titleList.add(new Pair<String, String>(getString(R.string.title_image_1),"Title Image 1"));
        SpinnerAdapterForPair titleListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                titleList);
        spinnerTitle.setAdapter(titleListAdapter);

        textViewDefault.setText(getString(R.string.capture));

        eVolvApp = (EVolvApp) getActivity().getApplicationContext();

        setSavedValue();
        return view;
    }

    private void setSavedValue() {
        //editTextFieldName.setText(PreferenceUtils.getPreference(getActivity(), FIELD_NAME, ""));
        lightThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_LIGHT_THRESHOLD_POA, "" + ImageProcessingSDK.dLightThresholdPOA));
        maxFocusThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_MAX_FOCUS_THRESHOLD_POA, "" + ImageProcessingSDK.dMaxFocusThresholdPOA));
        minFocusThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_MIN_FOCUS_THRESHOLD_POA, "" + ImageProcessingSDK.dMinFocusThresholdPOA));
        glarePercentageEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_GLARE_PERCENTAGE_POA, "" + ImageProcessingSDK.dGlarePercentagePOA));
        enableCaptureButtonTimeEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), CAPTURE_BUTTON_TIME_POA, "" + ImageProcessingSDK.dCaptureBtnEnableTimePOA));
        maxImageSizeEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), MAX_IMAGE_SIZE_POA, "" + ImageProcessingSDK.dImageSizePOA));
        imageHeightEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_HEIGHT_POA, "" + ImageProcessingSDK.dImageHeightPOA));
        imageWidthEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_WIDTH_POA, "" + ImageProcessingSDK.dImgaeWidthPOA));
        idOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_COLOR_POA, "" + ImageProcessingSDK.dIdOutlineColor));
        idOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_ALPHA_POA, "" + ImageProcessingSDK.dAlpha));
        detectedIdOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_COLOR_POA, "" + ImageProcessingSDK.dDetectedIdOutlineColor));
        detectedIdOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_ALPHA_POA, "" + ImageProcessingSDK.dAlpha));
        idOutsideOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_COLOR_POA, "" + ImageProcessingSDK.dOutsideIdOutlineColor));
        idOutsideOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_ALPHA_POA, "" + ImageProcessingSDK.dAlpha));
        detectedOutsideOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_POA, "" + ImageProcessingSDK.dDetectedOutsideIdOutlineColor));
        detectedOutsideOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_POA, "" + ImageProcessingSDK.dAlpha));

        String showTitle = PreferenceUtils.getPreference(getActivity(), SELECT_ID_TITLE_IMG_POA, "None");
        if (showTitle.equalsIgnoreCase("None")) {
            spinnerTitle.setSelection(0);
        } else {
            spinnerTitle.setSelection(1);
        }

        boolean captureMode = PreferenceUtils.getPreference(getActivity(), CAPTURE_PORTRAIT_MODE_POA, ImageProcessingSDK.dPortraitOrientation);
        if (captureMode) {
            radioGroupOrientation.check(radioGroupOrientation.getChildAt(0).getId());
        } else {
            radioGroupOrientation.check(radioGroupOrientation.getChildAt(1).getId());
        }

        boolean showInstruction = PreferenceUtils.getPreference(getActivity(), SHOW_INSTRUCTION_POA, ImageProcessingSDK.dShowInstruction);
        if (showInstruction) {
            radioGroupInstruction.check(radioGroupInstruction.getChildAt(1).getId());
        } else {
            radioGroupInstruction.check(radioGroupInstruction.getChildAt(0).getId());
        }
    }

    private void resetValue() {
        //editTextFieldName.setText("");
        lightThresholdEdtTxt.setText("" + ImageProcessingSDK.dLightThresholdPOA);
        maxFocusThresholdEdtTxt.setText("" + ImageProcessingSDK.dMaxFocusThresholdPOA);
        minFocusThresholdEdtTxt.setText("" + ImageProcessingSDK.dMinFocusThresholdPOA);
        glarePercentageEdtTxt.setText("" + ImageProcessingSDK.dGlarePercentagePOA);
        enableCaptureButtonTimeEdtTxt.setText("" + ImageProcessingSDK.dCaptureBtnEnableTimePOA);
        maxImageSizeEdtTxt.setText("" + ImageProcessingSDK.dImageSizePOA);
        imageHeightEdtTxt.setText("" + ImageProcessingSDK.dImageHeightPOA);
        imageWidthEdtTxt.setText("" + ImageProcessingSDK.dImgaeWidthPOA);
        idOutlineColorEdtTxt.setText("" + ImageProcessingSDK.dIdOutlineColor);
        idOutlineAlphaEdtTxt.setText("" + ImageProcessingSDK.dAlpha);
        detectedIdOutlineColorEdtTxt.setText("" + ImageProcessingSDK.dDetectedIdOutlineColor);
        detectedIdOutlineAlphaEdtTxt.setText("" + ImageProcessingSDK.dAlpha);
        idOutsideOutlineColorEdtTxt.setText("" + ImageProcessingSDK.dOutsideIdOutlineColor);
        idOutsideOutlineAlphaEdtTxt.setText("" + ImageProcessingSDK.dAlpha);
        detectedOutsideOutlineColorEdtTxt.setText("" + ImageProcessingSDK.dDetectedOutsideIdOutlineColor);
        detectedOutsideOutlineAlphaEdtTxt.setText("" + ImageProcessingSDK.dAlpha);
        spinnerTitle.setSelection(0);
        if (ImageProcessingSDK.dPortraitOrientation) {
            radioGroupOrientation.check(radioGroupOrientation.getChildAt(0).getId());
        } else {
            radioGroupOrientation.check(radioGroupOrientation.getChildAt(1).getId());
        }

        if (ImageProcessingSDK.dShowInstruction) {
            radioGroupInstruction.check(radioGroupInstruction.getChildAt(1).getId());
        } else {
            radioGroupInstruction.check(radioGroupInstruction.getChildAt(0).getId());
        }
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
                    captureDocumentAPICall();
                }
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String fieldName = editTextFieldName.getText().toString().trim();
                String lightThreshold = lightThresholdEdtTxt.getText().toString().trim();
                String minFocusThreshold = minFocusThresholdEdtTxt.getText().toString().trim();
                String maxFocusThreshold = maxFocusThresholdEdtTxt.getText().toString().trim();
                String glareThreshold = glarePercentageEdtTxt.getText().toString().trim();
                String captureBtnTime = enableCaptureButtonTimeEdtTxt.getText().toString().trim();
                String maxImagesize = maxImageSizeEdtTxt.getText().toString().trim();
                String imageHeight = imageHeightEdtTxt.getText().toString().trim();
                String imageWidth = imageWidthEdtTxt.getText().toString().trim();
                String idOutlineColor = idOutlineColorEdtTxt.getText().toString().trim();
                String idOutlineAlpha = idOutlineAlphaEdtTxt.getText().toString().trim();
                String detectedIdOutlineColor = detectedIdOutlineColorEdtTxt.getText().toString().trim();
                String detectedIdOutlineAlpha = detectedIdOutlineAlphaEdtTxt.getText().toString().trim();
                String idOutsideOutlineColor = idOutsideOutlineColorEdtTxt.getText().toString().trim();
                String idOutsideOutlineAlpha = idOutsideOutlineAlphaEdtTxt.getText().toString().trim();
                String detectedIdOutsideOutlineColor = detectedOutsideOutlineColorEdtTxt.getText().toString().trim();
                String detectedIdOutsideOutlineAlpha = detectedOutsideOutlineAlphaEdtTxt.getText().toString().trim();
               // String showTitle = spinnerTitle.getSelectedItem().toString();
                String showTitle = ((Pair)spinnerTitle.getSelectedItem()).second.toString();

                boolean capturePortrait = true;
                if (radioGroupOrientation.getCheckedRadioButtonId() != R.id.radio_button_portrait) {
                    capturePortrait = false;
                }

                boolean showInstruction = false;
                if (radioGroupInstruction.getCheckedRadioButtonId() != R.id.radio_button_no) {
                    showInstruction = true;
                }

                //PreferenceUtils.setPreference(getActivity(), FIELD_NAME, fieldName);
                PreferenceUtils.setPreference(getActivity(), IMAGE_LIGHT_THRESHOLD_POA, lightThreshold);
                PreferenceUtils.setPreference(getActivity(), IMAGE_MAX_FOCUS_THRESHOLD_POA, maxFocusThreshold);
                PreferenceUtils.setPreference(getActivity(), IMAGE_MIN_FOCUS_THRESHOLD_POA, minFocusThreshold);
                PreferenceUtils.setPreference(getActivity(), IMAGE_GLARE_PERCENTAGE_POA, glareThreshold);
                PreferenceUtils.setPreference(getActivity(), CAPTURE_BUTTON_TIME_POA, captureBtnTime);
                PreferenceUtils.setPreference(getActivity(), MAX_IMAGE_SIZE_POA, maxImagesize);
                PreferenceUtils.setPreference(getActivity(), IMAGE_HEIGHT_POA, imageHeight);
                PreferenceUtils.setPreference(getActivity(), IMAGE_WIDTH_POA, imageWidth);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTLINE_COLOR_POA, idOutlineColor);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTLINE_ALPHA_POA, "" + idOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_COLOR_POA, detectedIdOutlineColor);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_ALPHA_POA, "" + detectedIdOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_COLOR_POA, idOutsideOutlineColor);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_ALPHA_POA, "" + idOutsideOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_POA, "" + detectedIdOutsideOutlineColor);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_POA, "" + detectedIdOutsideOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), SELECT_ID_TITLE_IMG_POA, showTitle);
                PreferenceUtils.setPreference(getActivity(), CAPTURE_PORTRAIT_MODE_POA, capturePortrait);
                PreferenceUtils.setPreference(getActivity(), SHOW_INSTRUCTION_POA, showInstruction);

                showErrorMessage(view, getString(R.string.id_capture_save_msg));
                bottomSheetBehaviorCaptureId.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetValue();
            }
        });
        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureDocumentAPICall();
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
//                    IDDetails processFlow = new IDDetails();
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

    private void captureDocumentAPICall() {
        ImageProcessingSDK.getInstance().setImageProcessingResponseListener(DocumentCapture.this);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showErrorMessage(getView(), getString(R.string.camera_permission));
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
            }
        } else {
            String lightThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_LIGHT_THRESHOLD_POA, "" + ImageProcessingSDK.dLightThresholdPOA);
            String minFocusThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_MIN_FOCUS_THRESHOLD_POA, "" + ImageProcessingSDK.dMinFocusThresholdPOA);
            String maxFocusThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_MAX_FOCUS_THRESHOLD_POA, "" + ImageProcessingSDK.dMaxFocusThresholdPOA);
            String glareThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_GLARE_PERCENTAGE_POA, "" + ImageProcessingSDK.dGlarePercentagePOA);
            String captureBtnTime = PreferenceUtils.getPreference(getActivity(), CAPTURE_BUTTON_TIME_POA, "" + ImageProcessingSDK.dCaptureBtnEnableTimePOA);
            String maxImagesize = PreferenceUtils.getPreference(getActivity(), MAX_IMAGE_SIZE_POA, "" + ImageProcessingSDK.dImageSizePOA);
            String imageHeight = PreferenceUtils.getPreference(getActivity(), IMAGE_HEIGHT_POA, "" + ImageProcessingSDK.dImageHeightPOA);
            String imageWidth = PreferenceUtils.getPreference(getActivity(), IMAGE_WIDTH_POA, "" + ImageProcessingSDK.dImgaeWidthPOA);
            String idImgOutlineColor = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_COLOR_POA, "" + ImageProcessingSDK.dIdOutlineColor);
            String idImgOutlineAlpha = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_ALPHA_POA, "" + ImageProcessingSDK.dAlpha);
            String detectedIdImgOutlineColor = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_COLOR_POA, "" + ImageProcessingSDK.dDetectedIdOutlineColor);
            String detectedIdImgOutlineAlpha = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_ALPHA_POA, "" + ImageProcessingSDK.dAlpha);
            String idImgOutsideOutlineColor = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_COLOR_POA, "" + ImageProcessingSDK.dOutsideIdOutlineColor);
            String idImgOutsideOutlineAlpha = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_ALPHA_POA, "" + ImageProcessingSDK.dAlpha);
            String detectedIdImgOutsideOutlineColor = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_POA, "" + ImageProcessingSDK.dDetectedOutsideIdOutlineColor);
            String detectedIdImgOutsideOutlineAlpha = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_POA, "" + ImageProcessingSDK.dAlpha);
            String titleImageType = PreferenceUtils.getPreference(getActivity(), SELECT_ID_TITLE_IMG_POA, "None");
            boolean capturePortraitMode = PreferenceUtils.getPreference(getActivity(), CAPTURE_PORTRAIT_MODE_POA, ImageProcessingSDK.dPortraitOrientation);
            boolean showInstruction = PreferenceUtils.getPreference(getActivity(), SHOW_INSTRUCTION_POA, ImageProcessingSDK.dShowInstruction);
            // String fieldName = PreferenceUtils.getPreference(getActivity(), FIELD_NAME, "");
            String fieldName = editTextFieldName.getText().toString().trim();

            Bitmap titleBitmap = null;
            if (!titleImageType.equalsIgnoreCase("None")) {
                titleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title_image);
            }

            int minimumLightThreshold = (!StringUtil.isEmpty(lightThreshold) ? Integer.parseInt(lightThreshold) : ImageProcessingSDK.dLightThreshold);
            int minFocusScoreThreshold = (!StringUtil.isEmpty(minFocusThreshold) ? Integer.parseInt(minFocusThreshold) : ImageProcessingSDK.dMinFocusThreshold);
            int focusScoreThreshold = (!StringUtil.isEmpty(maxFocusThreshold) ? Integer.parseInt(maxFocusThreshold) : ImageProcessingSDK.dMaxFocusThreshold);
            int glarePercentageThreshold = (!StringUtil.isEmpty(glareThreshold) ? Integer.parseInt(glareThreshold) : ImageProcessingSDK.dGlarePercentage);
            int enableCaptureButtonTime = (!StringUtil.isEmpty(captureBtnTime) ? Integer.parseInt(captureBtnTime) : ImageProcessingSDK.dCaptureBtnEnableTime);
            int maxImageSize = (!StringUtil.isEmpty(maxImagesize) ? Integer.parseInt(maxImagesize) : ImageProcessingSDK.dImageSize);
            int highResolutionImageHeight = (!StringUtil.isEmpty(imageHeight) ? Integer.parseInt(imageHeight) : ImageProcessingSDK.dImageHeight);
            int highResolutionImageWidth = (!StringUtil.isEmpty(imageWidth) ? Integer.parseInt(imageWidth) : ImageProcessingSDK.dImgaeWidth);
            String idImageOutlineColor = (!StringUtil.isEmpty(idImgOutlineColor) ? idImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
            int idImageOutlineAlpha = (!StringUtil.isEmpty(idImgOutlineAlpha) ? Integer.parseInt(idImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
            String detectedIdImageOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutlineColor) ? detectedIdImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
            int detectedIdImageOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutlineAlpha) ? Integer.parseInt(detectedIdImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
            String idImageOutsideOutlineColor = (!StringUtil.isEmpty(idImgOutsideOutlineColor) ? idImgOutsideOutlineColor : ImageProcessingSDK.dOutsideIdOutlineColor);
            int idImageOutsideOutlineAlpha = (!StringUtil.isEmpty(idImgOutsideOutlineAlpha) ? Integer.parseInt(idImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

            String detectedIdImageOutsideOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineColor) ? detectedIdImgOutsideOutlineColor : ImageProcessingSDK.dDetectedOutsideIdOutlineColor);
            int detectedIdImageOutsideOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineAlpha) ? Integer.parseInt(detectedIdImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

            try {

                JSONObject commonFunctioObject = new JSONObject();
                commonFunctioObject.put(UIConfigConstants.ID_CAPTURE_PORTRAIT, capturePortraitMode == true ? "Y" : "N");
                commonFunctioObject.put(UIConfigConstants.ID_LIGHT_THRESHOLD, minimumLightThreshold + "");
                commonFunctioObject.put(UIConfigConstants.ID_MIN_FOCUS_THRESHOLD, minFocusScoreThreshold + "");
                commonFunctioObject.put(UIConfigConstants.ID_MAX_FOCUS_THRESHOLD, focusScoreThreshold + "");
                commonFunctioObject.put(UIConfigConstants.ID_GLARE_PERCENTAGE, glarePercentageThreshold + "");
                commonFunctioObject.put(UIConfigConstants.ID_ENABLE_CAPTURE_BUTTON_TIME, enableCaptureButtonTime + "");
                commonFunctioObject.put(UIConfigConstants.ID_MAX_IMAGE_SIZE, maxImageSize + "");
                commonFunctioObject.put(UIConfigConstants.ID_IMAGE_HEIGHT, highResolutionImageHeight + "");
                commonFunctioObject.put(UIConfigConstants.ID_IMAGE_WIDTH, highResolutionImageWidth + "");
               // commonFunctioObject.put(UIConfigConstants.ID_SHOW_INSTRUCTION, showInstruction == true ? "Y" : "N");
                commonFunctioObject.put(UIConfigConstants.ID_GENERIC_DOC_FIELDNAME, fieldName);

            if (!StringUtil.isEmpty(fieldName)) {
                JSONObject addJSON = eVolvApp.getAdditonalData();
                if (null != addJSON) {
//                    ImageProcessingSDK.getInstance().captureGenericDocument(getActivity(), capturePortraitMode, minimumLightThreshold, minFocusScoreThreshold,
//                            focusScoreThreshold, glarePercent/ageThreshold, enableCaptureButtonTime, maxImageSize,
//                            highResolutionImageHeight, highResolutionImageWidth, addJSON,
//                            new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor, detectedIdImageOutsideOutlineAlpha), titleBitmap,
//                            showInstruction, 0, fieldName);
                    ImageProcessingSDK.getInstance().autoCapture(getActivity(), ImageType.GENERIC_DOCUMENT, commonFunctioObject, null, null);

                } else {
//                    ImageProcessingSDK.getInstance().captureGenericDocument(getActivity(), capturePortraitMode, minimumLightThreshold, minFocusScoreThreshold,
//                            focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
//                            highResolutionImageHeight, highResolutionImageWidth, null,
//                            new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor, detectedIdImageOutsideOutlineAlpha), titleBitmap,
//                            showInstruction, 0, fieldName);
                    ImageProcessingSDK.getInstance().autoCapture(getActivity(), ImageType.GENERIC_DOCUMENT, commonFunctioObject, addJSON, null);

                }
            } else {
                showErrorMessage(getView(), getString(R.string.enter_document_name));
            }
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        Bitmap bitmap = BitmapUtils.getBitmapFromFile(eVolvApp.getBaseDirectoryPath() + Constants.DOCUMENT_CAPTURE_IMAGE);
        setImage(bitmap);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

        Log.d("SDK", "CALLBACK:::: onCaptureGenericDocumentResultAvailable");
        if (null != response) {
            if (response.getStatusCode() == ResponseStatusCode.PERMISSION_NOT_GRANTED.getStatusCode()) {
                showErrorMessage(getView(), getString(R.string.permission_not_granted));
            }else {
                String imageBase64 = null, fieldName = null;
                if (null != resultMap) {
                    if (resultMap.containsKey(ImageType.GENERIC_DOCUMENT.toString())) {
                        imageBase64 = resultMap.get(ImageType.GENERIC_DOCUMENT.toString());
                    }
//                if (resultMap.containsKey("fieldName")){
//                    fieldName=resultMap.get("fieldName");
//                    // System.out.println("******Field Name******"  +fieldName);
//                }
                }
                if (!StringUtil.isEmpty(imageBase64)) {
                    byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    BitmapUtils.writeBitmapToFilepath(eVolvApp.getBaseDirectoryPath() + Constants.DOCUMENT_CAPTURE_IMAGE, bitmap);
                    setImage(bitmap); //Set this image as first image
                    isCapture = false;
                }
            }
        }
    }

    private void setImage(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        textViewDefault.setVisibility(View.GONE);
        defaultImage.setVisibility(View.GONE);
        captureImage.setVisibility(View.VISIBLE);
        captureImage.setImageBitmap(bitmap);
        buttonCapture.setText(getString(R.string.re_capture));
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
