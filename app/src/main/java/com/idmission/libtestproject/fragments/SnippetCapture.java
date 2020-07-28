package com.idmission.libtestproject.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.idmission.libtestproject.classes.UIConfigConstants;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SnippetCapture extends Fragment implements ImageProcessingResponseListener {

    //private static final String[] TITLE_IMAGE = {"None", "Title Image 1"};
    private BottomSheetBehavior bottomSheetBehaviorCaptureId;
    private ImageView bottomSheetImage, defaultImage;
    private LinearLayout linearLayout, linearLayoutCapture;
    private boolean isExpand = false, isCapture = true;
    private TextView textViewDefault, textViewSnippetResultData, textViewHeading;
    private Button buttonCapture, buttonBack, buttonNext, buttonSave, buttonReset, buttonAdd;
    private EditText addFieldEdtTxt, lightThresholdEdtTxt, maxFocusThresholdEdtTxt, minFocusThresholdEdtTxt, glarePercentageEdtTxt,
            enableCaptureButtonTimeEdtTxt, maxImageSizeEdtTxt, imageHeightEdtTxt, imageWidthEdtTxt, idOutlineColorEdtTxt, idOutlineAlphaEdtTxt,
            detectedIdOutlineColorEdtTxt, detectedIdOutlineAlphaEdtTxt, idOutsideOutlineColorEdtTxt, idOutsideOutlineAlphaEdtTxt, detectedOutsideOutlineColorEdtTxt, detectedOutsideOutlineAlphaEdtTxt;
    private static final String IMAGE_LIGHT_THRESHOLD_SNIPPET = "IMAGE_LIGHT_THRESHOLD_SNIPPET", IMAGE_MAX_FOCUS_THRESHOLD_SNIPPET = "IMAGE_MAX_FOCUS_THRESHOLD_SNIPPET", IMAGE_MIN_FOCUS_THRESHOLD_SNIPPET = "IMAGE_MIN_FOCUS_THRESHOLD_SNIPPET",
            IMAGE_GLARE_PERCENTAGE_SNIPPET = "IMAGE_GLARE_PERCENTAGE_SNIPPET", CAPTURE_BUTTON_TIME_SNIPPET = "CAPTURE_BUTTON_TIME_SNIPPET", MAX_IMAGE_SIZE_SNIPPET = "MAX_IMAGE_SIZE_SNIPPET",
            IMAGE_HEIGHT_SNIPPET = "IMAGE_HEIGHT_SNIPPET", IMAGE_WIDTH_SNIPPET = "IMAGE_WIDTH_SNIPPET", ID_IMG_OUTLINE_COLOR_SNIPPET = "ID_IMG_OUTLINE_COLOR_SNIPPET", ID_IMG_OUTLINE_ALPHA_SNIPPET = "ID_IMG_OUTLINE_ALPHA_SNIPPET",
            DETECTED_ID_IMG_OUTLINE_COLOR_SNIPPET = "DETECTED_ID_IMG_OUTLINE_COLOR_SNIPPET", DETECTED_ID_IMG_OUTLINE_ALPHA_SNIPPET = "DETECTED_ID_IMG_OUTLINE_ALPHA_SNIPPET", ID_IMG_OUTSIDE_OUTLINE_COLOR_SNIPPET = "ID_IMG_OUTSIDE_OUTLINE_COLOR_SNIPPET", ID_IMG_OUTSIDE_OUTLINE_ALPHA_SNIPPET = "ID_IMG_OUTSIDE_OUTLINE_ALPHA_SNIPPET",
            DETECTED_ID_IMG_OUTSIDE_OUTLINE_SNIPPET = "DETECTED_ID_IMG_OUTSIDE_OUTLINE_SNIPPET", DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_SNIPPET = "DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_SNIPPET", SELECT_ID_TITLE_IMG_SNIPPET = "SELECT_ID_TITLE_IMG_SNIPPET", CAPTURE_PORTRAIT_MODE_SNIPPET = "CAPTURE_PORTRAIT_MODE_SNIPPET", SHOW_INSTRUCTION_SNIPPET = "SHOW_INSTRUCTION_SNIPPET", CAPTURE_ENABLE_SNIPPET = "CAPTURE_ENABLE_SNIPPET";
    private RadioGroup radioGroupOrientation, radioGroupInstruction, radioGroupEnabledCapture;
    private Spinner spinnerTitle, spinnerField;
    private HashMap<String, String> snippetFieldNameMap = new HashMap<>();
    private ScrollView scrollView;
    private EVolvApp eVolvApp;
    private RelativeLayout relativeLayoutField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.snippet_capture_tab_layout, container, false);

        eVolvApp = (EVolvApp) (getActivity().getApplicationContext());

        bottomSheetBehaviorCaptureId = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheetCaptureId));
        bottomSheetImage = (ImageView) view.findViewById(R.id.image_view);
        defaultImage = (ImageView) view.findViewById(R.id.default_image);
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_top);
        linearLayoutCapture = (LinearLayout) view.findViewById(R.id.linear_layout_capture);
        textViewDefault = (TextView) view.findViewById(R.id.text_view_label);
        textViewHeading = (TextView) view.findViewById(R.id.text_view_heading);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        textViewSnippetResultData = (TextView) view.findViewById(R.id.text_view_snippet_capture_data);
        buttonCapture = (Button) view.findViewById(R.id.button_capture);
        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);

        addFieldEdtTxt = (EditText) view.findViewById(R.id.edit_view_add_field);
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
        radioGroupEnabledCapture = (RadioGroup) view.findViewById(R.id.radio_group_enable);
        spinnerTitle = (Spinner) view.findViewById(R.id.spinner_title);
        spinnerField = (Spinner) view.findViewById(R.id.spinner_field);
        buttonAdd = (Button) view.findViewById(R.id.button_add);
        buttonSave = (Button) view.findViewById(R.id.button_save);
        buttonReset = (Button) view.findViewById(R.id.button_reset);
        relativeLayoutField = (RelativeLayout) view.findViewById(R.id.relative_layout);
        relativeLayoutField.setVisibility(View.GONE);

        //spinnerTitle.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TITLE_IMAGE));
        ArrayList<Pair<String,String>> titleList = new ArrayList<Pair<String,String>>();
        titleList.add(new Pair<String, String>(getString(R.string.none),"None"));
        titleList.add(new Pair<String, String>(getString(R.string.title_image_1),"Title Image 1"));
        SpinnerAdapterForPair titleListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                titleList);
        spinnerTitle.setAdapter(titleListAdapter);

        textViewDefault.setText(getString(R.string.capture));

        setSavedValue();
        return view;
    }

    private void setSavedValue() {
        lightThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_LIGHT_THRESHOLD_SNIPPET, "" + ImageProcessingSDK.dLightThreshold));
        maxFocusThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_MAX_FOCUS_THRESHOLD_SNIPPET, "" + ImageProcessingSDK.dMaxFocusThreshold));
        minFocusThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_MIN_FOCUS_THRESHOLD_SNIPPET, "" + ImageProcessingSDK.dMinFocusThreshold));
        glarePercentageEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_GLARE_PERCENTAGE_SNIPPET, "" + ImageProcessingSDK.dGlarePercentage));
        enableCaptureButtonTimeEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), CAPTURE_BUTTON_TIME_SNIPPET, "" + ImageProcessingSDK.dCaptureBtnEnableTime));
        maxImageSizeEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), MAX_IMAGE_SIZE_SNIPPET, "" + ImageProcessingSDK.dImageSize));
        imageHeightEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_HEIGHT_SNIPPET, "" + ImageProcessingSDK.dImageHeight));
        imageWidthEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_WIDTH_SNIPPET, "" + ImageProcessingSDK.dImgaeWidth));
        idOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_COLOR_SNIPPET, "" + ImageProcessingSDK.dIdOutlineColor));
        idOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_ALPHA_SNIPPET, "" + ImageProcessingSDK.dAlpha));
        detectedIdOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_COLOR_SNIPPET, "" + ImageProcessingSDK.dDetectedIdOutlineColor));
        detectedIdOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_ALPHA_SNIPPET, "" + ImageProcessingSDK.dAlpha));
        idOutsideOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_COLOR_SNIPPET, "" + ImageProcessingSDK.dOutsideIdOutlineColor));
        idOutsideOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_ALPHA_SNIPPET, "" + ImageProcessingSDK.dAlpha));
        detectedOutsideOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_SNIPPET, "" + ImageProcessingSDK.dDetectedOutsideIdOutlineColor));
        detectedOutsideOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_SNIPPET, "" + ImageProcessingSDK.dAlpha));

        String showTitle = PreferenceUtils.getPreference(getActivity(), SELECT_ID_TITLE_IMG_SNIPPET, "None");
        if (showTitle.equalsIgnoreCase("None")) {
            spinnerTitle.setSelection(0);
        } else {
            spinnerTitle.setSelection(1);
        }

        boolean captureMode = PreferenceUtils.getPreference(getActivity(), CAPTURE_PORTRAIT_MODE_SNIPPET, ImageProcessingSDK.dPortraitOrientation);
        if (captureMode) {
            radioGroupOrientation.check(radioGroupOrientation.getChildAt(0).getId());
        } else {
            radioGroupOrientation.check(radioGroupOrientation.getChildAt(1).getId());
        }

        boolean showInstruction = PreferenceUtils.getPreference(getActivity(), SHOW_INSTRUCTION_SNIPPET, ImageProcessingSDK.dShowInstruction);
        if (showInstruction) {
            radioGroupInstruction.check(radioGroupInstruction.getChildAt(1).getId());
        } else {
            radioGroupInstruction.check(radioGroupInstruction.getChildAt(0).getId());
        }

        boolean captureEnable = PreferenceUtils.getPreference(getActivity(), CAPTURE_ENABLE_SNIPPET, true);
        if (captureEnable) {
            radioGroupEnabledCapture.check(radioGroupEnabledCapture.getChildAt(0).getId());
        } else {
            radioGroupEnabledCapture.check(radioGroupEnabledCapture.getChildAt(1).getId());
        }

    }

    private void resetValue() {
        addFieldEdtTxt.setText("");
        lightThresholdEdtTxt.setText("" + ImageProcessingSDK.dLightThreshold);
        maxFocusThresholdEdtTxt.setText("" + ImageProcessingSDK.dMaxFocusThreshold);
        minFocusThresholdEdtTxt.setText("" + ImageProcessingSDK.dMinFocusThreshold);
        glarePercentageEdtTxt.setText("" + ImageProcessingSDK.dGlarePercentage);
        enableCaptureButtonTimeEdtTxt.setText("" + ImageProcessingSDK.dCaptureBtnEnableTime);
        maxImageSizeEdtTxt.setText("" + ImageProcessingSDK.dImageSize);
        imageHeightEdtTxt.setText("" + ImageProcessingSDK.dImageHeight);
        imageWidthEdtTxt.setText("" + ImageProcessingSDK.dImgaeWidth);
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

        radioGroupEnabledCapture.check(radioGroupEnabledCapture.getChildAt(0).getId());
        snippetFieldNameMap.clear();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

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
                    snippetCaptureAPICall();
                }
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StringUtil.isEmpty(addFieldEdtTxt.getText().toString())) {
                   // snippetFieldNameMap.put(addFieldEdtTxt.getText().toString(), addFieldEdtTxt.getText().toString());
                    snippetFieldNameMap.put(addFieldEdtTxt.getText().toString(), "N");
                    addFieldEdtTxt.setText("");

                    showAddFieldSpinner(snippetFieldNameMap);
                } else {
                    showErrorMessage(view, getString(R.string.fields_blank_msg));
                }
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                //String showTitle = spinnerTitle.getSelectedItem().toString();
                String showTitle = ((Pair)spinnerTitle.getSelectedItem()).second.toString();

                boolean capturePortrait = true;
                if (radioGroupOrientation.getCheckedRadioButtonId() != R.id.radio_button_portrait) {
                    capturePortrait = false;
                }

                boolean showInstruction = false;
                if (radioGroupInstruction.getCheckedRadioButtonId() != R.id.radio_button_no) {
                    showInstruction = true;
                }

                boolean captureEnable = true;
                if (radioGroupEnabledCapture.getCheckedRadioButtonId() != R.id.radio_button_enable_yes) {
                    captureEnable = false;
                }

                PreferenceUtils.setPreference(getActivity(), IMAGE_LIGHT_THRESHOLD_SNIPPET, lightThreshold);
                PreferenceUtils.setPreference(getActivity(), IMAGE_MAX_FOCUS_THRESHOLD_SNIPPET, maxFocusThreshold);
                PreferenceUtils.setPreference(getActivity(), IMAGE_MIN_FOCUS_THRESHOLD_SNIPPET, minFocusThreshold);
                PreferenceUtils.setPreference(getActivity(), IMAGE_GLARE_PERCENTAGE_SNIPPET, glareThreshold);
                PreferenceUtils.setPreference(getActivity(), CAPTURE_BUTTON_TIME_SNIPPET, captureBtnTime);
                PreferenceUtils.setPreference(getActivity(), MAX_IMAGE_SIZE_SNIPPET, maxImagesize);
                PreferenceUtils.setPreference(getActivity(), IMAGE_HEIGHT_SNIPPET, imageHeight);
                PreferenceUtils.setPreference(getActivity(), IMAGE_WIDTH_SNIPPET, imageWidth);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTLINE_COLOR_SNIPPET, idOutlineColor);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTLINE_ALPHA_SNIPPET, "" + idOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_COLOR_SNIPPET, detectedIdOutlineColor);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_ALPHA_SNIPPET, "" + detectedIdOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_COLOR_SNIPPET, idOutsideOutlineColor);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_ALPHA_SNIPPET, "" + idOutsideOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_SNIPPET, "" + detectedIdOutsideOutlineColor);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_SNIPPET, "" + detectedIdOutsideOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), SELECT_ID_TITLE_IMG_SNIPPET, showTitle);
                PreferenceUtils.setPreference(getActivity(), CAPTURE_PORTRAIT_MODE_SNIPPET, capturePortrait);
                PreferenceUtils.setPreference(getActivity(), SHOW_INSTRUCTION_SNIPPET, showInstruction);
                PreferenceUtils.setPreference(getActivity(), CAPTURE_ENABLE_SNIPPET, captureEnable);

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
                snippetCaptureAPICall();
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

    private void snippetCaptureAPICall() {
        ImageProcessingSDK.getInstance().setImageProcessingResponseListener(SnippetCapture.this);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showErrorMessage(getView(), "Need to enable CAMERA permission.");
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
            }
        } else {
            if (snippetFieldNameMap.size() != 0) {
                String lightThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_LIGHT_THRESHOLD_SNIPPET, "" + ImageProcessingSDK.dLightThreshold);
                String minFocusThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_MIN_FOCUS_THRESHOLD_SNIPPET, "" + ImageProcessingSDK.dMinFocusThreshold);
                String maxFocusThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_MAX_FOCUS_THRESHOLD_SNIPPET, "" + ImageProcessingSDK.dMaxFocusThreshold);
                String glareThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_GLARE_PERCENTAGE_SNIPPET, "" + ImageProcessingSDK.dGlarePercentage);
                String captureBtnTime = PreferenceUtils.getPreference(getActivity(), CAPTURE_BUTTON_TIME_SNIPPET, "" + ImageProcessingSDK.dCaptureBtnEnableTime);
                String maxImagesize = PreferenceUtils.getPreference(getActivity(), MAX_IMAGE_SIZE_SNIPPET, "" + ImageProcessingSDK.dImageSize);
                String imageHeight = PreferenceUtils.getPreference(getActivity(), IMAGE_HEIGHT_SNIPPET, "" + ImageProcessingSDK.dImageHeight);
                String imageWidth = PreferenceUtils.getPreference(getActivity(), IMAGE_WIDTH_SNIPPET, "" + ImageProcessingSDK.dImgaeWidth);
                String idImgOutlineColor = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_COLOR_SNIPPET, "" + ImageProcessingSDK.dIdOutlineColor);
                String idImgOutlineAlpha = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_ALPHA_SNIPPET, "" + ImageProcessingSDK.dAlpha);
                String detectedIdImgOutlineColor = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_COLOR_SNIPPET, "" + ImageProcessingSDK.dDetectedIdOutlineColor);
                String detectedIdImgOutlineAlpha = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_ALPHA_SNIPPET, "" + ImageProcessingSDK.dAlpha);
                String idImgOutsideOutlineColor = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_COLOR_SNIPPET, "" + ImageProcessingSDK.dOutsideIdOutlineColor);
                String idImgOutsideOutlineAlpha = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_ALPHA_SNIPPET, "" + ImageProcessingSDK.dAlpha);
                String detectedIdImgOutsideOutlineColor = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_SNIPPET, "" + ImageProcessingSDK.dDetectedOutsideIdOutlineColor);
                String detectedIdImgOutsideOutlineAlpha = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_SNIPPET, "" + ImageProcessingSDK.dAlpha);
                String titleImageType = PreferenceUtils.getPreference(getActivity(), SELECT_ID_TITLE_IMG_SNIPPET, "None");
                boolean capturePortraitMode = PreferenceUtils.getPreference(getActivity(), CAPTURE_PORTRAIT_MODE_SNIPPET, ImageProcessingSDK.dPortraitOrientation);
                boolean showInstruction = PreferenceUtils.getPreference(getActivity(), SHOW_INSTRUCTION_SNIPPET, ImageProcessingSDK.dShowInstruction);
                boolean captureEnable = PreferenceUtils.getPreference(getActivity(), CAPTURE_ENABLE_SNIPPET, true);

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
                   commonFunctioObject.put(UIConfigConstants.ID_CAPTURE_ENABLE, captureEnable == true ? "Y" : "N");
                   commonFunctioObject.put(UIConfigConstants.ID_OPEN_CAMERA,  "Y");

                 //  commonFunctioObject.put(UIConfigConstants.ID_SHOW_INSTRUCTION, showInstruction == true ? "Y" : "N");

//                ImageProcessingSDK.getInstance().autoCapture(getActivity(), ImageType.SNIPPET_CAPTURE, capturePortraitMode, minimumLightThreshold, minFocusScoreThreshold,
//                        focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
//                        highResolutionImageHeight, highResolutionImageWidth, null,
//                        new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor, detectedIdImageOutsideOutlineAlpha), titleBitmap,
//                        showInstruction, 0, "", snippetFieldNameMap, captureEnable);

                   ImageProcessingSDK.getInstance().clearSnippetData();
                   ImageProcessingSDK.getInstance().autoCapture(getActivity(), ImageType.SNIPPET_CAPTURE, commonFunctioObject, null, snippetFieldNameMap);

               }catch (JSONException exception) {
                   exception.printStackTrace();
               }

            } else {
                showErrorMessage(getView(), getString(R.string.please_add_field));
            }
        }
    }

    private void showAddFieldSpinner(HashMap<String, String> snippetFieldNameMap) {
        //  if ((null != snippetFieldNameMap) || (snippetFieldNameMap.size()<0)) {
        if (snippetFieldNameMap.size() > 0) {
            relativeLayoutField.setVisibility(View.VISIBLE);
            // spinnerField.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, bindAddFieldSpinner(snippetFieldNameMap)));
            spinnerField.setAdapter(new CustomSpinnerAdapter(getActivity(), R.layout.custom_snippet_spinner, bindAddFieldSpinner(snippetFieldNameMap)));
        } else {
            relativeLayoutField.setVisibility(View.GONE);
        }
    }

    private ArrayList<String> bindAddFieldSpinner(HashMap<String, String> listIDCountry) {
        ArrayList<String> listCountry = new ArrayList<String>();
        for (Map.Entry<String, String> entry : listIDCountry.entrySet()) {
            String value = entry.getKey();
            listCountry.add(value);
        }
        return listCountry;
    }
//    private ArrayList<String> bindAddFieldSpinner(HashMap<String, String> listIDCountry) {
//        ArrayList<String> listCountry = new ArrayList<String>();
//        for (Map.Entry<String, String> entry : listIDCountry.entrySet()) {
//            String value = entry.getValue();
//            listCountry.add(value);
//        }
//        return listCountry;
//    }

    @Override
    public void onResume() {
        super.onResume();
        setData(eVolvApp.getSnippetResult());
        showAddFieldSpinner(snippetFieldNameMap);
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
        if (null != response) {
            if (response.getStatusCode() == ResponseStatusCode.PERMISSION_NOT_GRANTED.getStatusCode()) {
                showErrorMessage(getView(), getString(R.string.permission_not_granted));
            } else {
                String data = "";
                if (null != resultMap && !resultMap.isEmpty()) {
                    for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();

                        if (!key.equalsIgnoreCase("snippetImage")) {
                            data += key + " : " + value + "\n";
                        }

                    }
                }

                if (!StringUtil.isEmpty(data)) {
                    eVolvApp.setSnippetResult(data);
                    setData(data);
                    isCapture = false;
                } else {
                    CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));

                    textViewHeading.setVisibility(View.GONE);
                    textViewDefault.setVisibility(View.VISIBLE);
                    defaultImage.setVisibility(View.VISIBLE);

                    textViewSnippetResultData.setText("");
                    buttonCapture.setText(getString(R.string.re_capture));
                    showErrorMessage(getView(), response.getStatusCode() + ": " + response.getStatusMessage());

                }
            }
        }
    }

    private void setData(String resultData) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        if (StringUtil.isEmpty(eVolvApp.getSnippetResult())) {
            textViewHeading.setVisibility(View.GONE);
            textViewDefault.setVisibility(View.VISIBLE);
            defaultImage.setVisibility(View.VISIBLE);
        } else {
            textViewSnippetResultData.setText(resultData);
            textViewHeading.setVisibility(View.VISIBLE);
            textViewDefault.setVisibility(View.GONE);
            defaultImage.setVisibility(View.GONE);
            buttonCapture.setText(getString(R.string.re_capture));
        }
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


    public class CustomSpinnerAdapter extends ArrayAdapter<String> {
        ArrayList<String> list = new ArrayList<String>();

        public CustomSpinnerAdapter(Context ctx, int txtViewResourceId, ArrayList<String> list) {

            super(ctx, txtViewResourceId, list);
            this.list = list;
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(final int position, final View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.custom_snippet_spinner, parent,
                    false);
            TextView fieldName = (TextView) mySpinner
                    .findViewById(R.id.text_view_name);
            fieldName.setText(list.get(position).toString());

            ImageView imageView = (ImageView) mySpinner
                    .findViewById(R.id.image_view_cancel);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String fieldName = list.get(position);
                    list.remove(fieldName);
                   // snippetFieldNameMap.values().remove(fieldName);
                    snippetFieldNameMap.keySet().remove(fieldName);
                    showAddFieldSpinner(snippetFieldNameMap);

                    if (snippetFieldNameMap.size() == 0) {
                        relativeLayoutField.setVisibility(View.GONE);
                    }
                }
            });

            return mySpinner;
        }

    }
}
