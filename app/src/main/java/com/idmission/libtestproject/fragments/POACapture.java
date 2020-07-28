package com.idmission.libtestproject.fragments;

import android.Manifest;
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
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.idmission.client.ColorCode;
import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.ImageType;
import com.idmission.client.Response;
import com.idmission.client.ResponseStatusCode;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.adapter.SpinnerAdapterForPair;
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.utils.BitmapUtils;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class POACapture extends Fragment implements ImageProcessingResponseListener {

    //private static final String[] TITLE_IMAGE = {"None", "Title Image 1"};
    //private static final String[] ID_CAPTURE_TITLE_ALIGNMENT = {"Top", "Center", "Bottom"};
    //private static final String[] ID_CAPTURE_HINT_MSG_ALIGNMENT = {"Center", "Bottom", "Top"};
    //private static final String[] ID_CAPTURE_TITLE_IMG_MSG_ALIGNMENT = {"Bottom", "Top", "Center"};
    private static final String[] LABELS_KEY = {"align_document_img_capture", "subject_is_too_dark_img_capture",
            "out_of_focus_img_capture", "too_much_glare_img_capture", "light", "focus", "glare", "page_title_image_capture"};
    private BottomSheetBehavior bottomSheetBehaviorCaptureId;
    private ImageView bottomSheetImage, defaultImage, captureImage;
    private LinearLayout linearLayout, linearLayoutCapture;
    private boolean isExpand = false, hideIdCaptureTitle = false, hideIdCaptureHintMsg = false, hideIdCaptureTitleImg = false, isCapture;
    private TextView textViewDefault;
    private Button buttonCapture, buttonBack, buttonNext, buttonSave, buttonReset, buttonAddLabel;
    private EditText lightThresholdEdtTxt, maxFocusThresholdEdtTxt, minFocusThresholdEdtTxt, glarePercentageEdtTxt,
            enableCaptureButtonTimeEdtTxt, maxImageSizeEdtTxt, imageHeightEdtTxt, imageWidthEdtTxt, idOutlineColorEdtTxt, idOutlineAlphaEdtTxt,
            detectedIdOutlineColorEdtTxt, detectedIdOutlineAlphaEdtTxt, idOutsideOutlineColorEdtTxt, idOutsideOutlineAlphaEdtTxt, detectedOutsideOutlineColorEdtTxt, detectedOutsideOutlineAlphaEdtTxt,
            address1EdtTxt, address2EdtTxt, countryEdtTxt, stateEdtTxt, zipCodeEdtTxt, englishLabelEdtTxt, spanishLabelEdtTxt;
    private static final String IMAGE_LIGHT_THRESHOLD = "IMAGE_LIGHT_THRESHOLD", IMAGE_MAX_FOCUS_THRESHOLD = "IMAGE_MAX_FOCUS_THRESHOLD", IMAGE_MIN_FOCUS_THRESHOLD = "IMAGE_MIN_FOCUS_THRESHOLD",
            IMAGE_GLARE_PERCENTAGE = "IMAGE_GLARE_PERCENTAGE", CAPTURE_BUTTON_TIME = "CAPTURE_BUTTON_TIME", MAX_IMAGE_SIZE = "MAX_IMAGE_SIZE",
            IMAGE_HEIGHT = "IMAGE_HEIGHT", IMAGE_WIDTH = "IMAGE_WIDTH", ID_IMG_OUTLINE_COLOR = "ID_IMG_OUTLINE_COLOR", ID_IMG_OUTLINE_ALPHA = "ID_IMG_OUTLINE_ALPHA",
            DETECTED_ID_IMG_OUTLINE_COLOR = "DETECTED_ID_IMG_OUTLINE_COLOR", DETECTED_ID_IMG_OUTLINE_ALPHA = "DETECTED_ID_IMG_OUTLINE_ALPHA", ID_IMG_OUTSIDE_OUTLINE_COLOR = "ID_IMG_OUTSIDE_OUTLINE_COLOR", ID_IMG_OUTSIDE_OUTLINE_ALPHA = "ID_IMG_OUTSIDE_OUTLINE_ALPHA",
            DETECTED_ID_IMG_OUTSIDE_OUTLINE = "DETECTED_ID_IMG_OUTSIDE_OUTLINE", DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA = "DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA", SELECT_TITLE = "SELECT_TITLE", CAPTURE_PORTRAIT_MODE = "CAPTURE_PORTRAIT_MODE", SHOW_INSTRUCTION = "SHOW_INSTRUCTION",
            FACE_TITLE = "FACE_TITLE", FACE_HINT = "FACE_HINT", FACE_ICON = "FACE_ICON", FACE_TITLE_IMAGE = "FACE_TITLE_IMAGE", ID_TITLE = "ID_TITLE", ID_HINT = "ID_HINT", ID_TITLE_IMAGE = "ID_TITLE_IMAGE",
            FACE_TITLE_HIDE = "FACE_TITLE_HIDE", FACE_HINT_HIDE = "FACE_HINT_HIDE", FACE_ICON_HIDE = "FACE_ICON_HIDE", FACE_TITLE_IMAGE_HIDE = "FACE_TITLE_IMAGE_HIDE", ID_TITLE_HIDE = "ID_TITLE_HIDE", ID_HINT_HIDE = "ID_HINT_HIDE", ID_TITLE_IMAGE_HIDE = "ID_TITLE_IMAGE_HIDE";
    private RadioGroup radioGroupOrientation, radioGroupInstruction;
    private Spinner spinnerTitle, spinnerTitleAlignment, spinnerHintAlignment, spinnerTitleImageAlignemnt, spinnerLabel;
    private CheckBox titleAlignmentCB, hintAlignmentCB, titleImageAlignmentCB;
    private String idTitleAlignment, idHintAlignment, idTitleImageAlignment;
    private HashMap<String, String> englishLabelMap = new HashMap<>();
    private HashMap<String, String> spanishLabelMap = new HashMap<>();
    private EVolvApp eVolvApp;
    private static final String LOGTAG = "TESTAPP";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.poa_capture, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        eVolvApp = (EVolvApp) (getActivity().getApplicationContext());

        bottomSheetBehaviorCaptureId = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheetCaptureId));
        bottomSheetImage = (ImageView) view.findViewById(R.id.image_view);
        defaultImage = (ImageView) view.findViewById(R.id.default_image);
        captureImage = (ImageView) view.findViewById(R.id.capture_image);
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_top);
        linearLayoutCapture = (LinearLayout) view.findViewById(R.id.linear_layout_capture);
        textViewDefault = (TextView) view.findViewById(R.id.text_view_label);
        buttonCapture = (Button) view.findViewById(R.id.button_capture);
        address1EdtTxt = (EditText) view.findViewById(R.id.edit_text_address1);
        address2EdtTxt = (EditText) view.findViewById(R.id.edit_text_address2);
        countryEdtTxt = (EditText) view.findViewById(R.id.edit_text_country);
        stateEdtTxt = (EditText) view.findViewById(R.id.edit_text_state);
        zipCodeEdtTxt = (EditText) view.findViewById(R.id.edit_text_zip_code);
        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);
        buttonAddLabel = (Button) view.findViewById(R.id.add_label_button);

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
        englishLabelEdtTxt = (EditText) view.findViewById(R.id.english_label);
        spanishLabelEdtTxt = (EditText) view.findViewById(R.id.spanish_label);
        radioGroupOrientation = (RadioGroup) view.findViewById(R.id.radio_group_orientation);
        radioGroupInstruction = (RadioGroup) view.findViewById(R.id.radio_group_instruction);
        spinnerTitle = (Spinner) view.findViewById(R.id.spinner_title);
        spinnerTitleAlignment = (Spinner) view.findViewById(R.id.spinner_title_alignment);
        spinnerHintAlignment = (Spinner) view.findViewById(R.id.spinner_hint_alignment);
        spinnerTitleImageAlignemnt = (Spinner) view.findViewById(R.id.spinner_title_image_alignment);
        spinnerLabel = (Spinner) view.findViewById(R.id.spinner_label_type);
        buttonSave = (Button) view.findViewById(R.id.button_save);
        buttonReset = (Button) view.findViewById(R.id.button_reset);
        titleAlignmentCB = (CheckBox) view.findViewById(R.id.checkbox_title_alignment);
        hintAlignmentCB = (CheckBox) view.findViewById(R.id.checkbox_hint_alignment);
        titleImageAlignmentCB = (CheckBox) view.findViewById(R.id.checkbox_title_image_alignment);

        spinnerLabel.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, LABELS_KEY));
       // spinnerTitle.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TITLE_IMAGE));
        //spinnerTitleAlignment.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, ID_CAPTURE_TITLE_ALIGNMENT));
        //spinnerHintAlignment.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, ID_CAPTURE_HINT_MSG_ALIGNMENT));
        //spinnerTitleImageAlignemnt.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, ID_CAPTURE_TITLE_IMG_MSG_ALIGNMENT));

        setSpinnerAdapter();
        textViewDefault.setText(getString(R.string.address_capture));

        setSavedValue();
        return view;
    }

    private void setSpinnerAdapter(){
        ArrayList<Pair<String,String>> titleList = new ArrayList<Pair<String,String>>();
        titleList.add(new Pair<String, String>(getString(R.string.none),"None"));
        titleList.add(new Pair<String, String>(getString(R.string.title_image_1),"Title Image 1"));
        SpinnerAdapterForPair titleListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                titleList);
        spinnerTitle.setAdapter(titleListAdapter);

        ArrayList<Pair<String,String>> titleAlignmentList = new ArrayList<Pair<String,String>>();
        titleAlignmentList.add(new Pair<String, String>(getString(R.string.top),"Top"));
        titleAlignmentList.add(new Pair<String, String>(getString(R.string.center),"Center"));
        titleAlignmentList.add(new Pair<String, String>(getString(R.string.bottom),"Bottom"));
        SpinnerAdapterForPair titleAlignmentListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                titleAlignmentList);
        spinnerTitleAlignment.setAdapter(titleAlignmentListAdapter);

        ArrayList<Pair<String,String>> hintAlignmentList = new ArrayList<Pair<String,String>>();
        hintAlignmentList.add(new Pair<String, String>(getString(R.string.center),"Center"));
        hintAlignmentList.add(new Pair<String, String>(getString(R.string.bottom),"Bottom"));
        hintAlignmentList.add(new Pair<String, String>(getString(R.string.top),"Top"));
        SpinnerAdapterForPair hintAlignmentListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                hintAlignmentList);
        spinnerHintAlignment.setAdapter(hintAlignmentListAdapter);

        ArrayList<Pair<String,String>> titleImageAlignmentList = new ArrayList<Pair<String,String>>();
        titleImageAlignmentList.add(new Pair<String, String>(getString(R.string.bottom),"Bottom"));
        titleImageAlignmentList.add(new Pair<String, String>(getString(R.string.top),"Top"));
        titleImageAlignmentList.add(new Pair<String, String>(getString(R.string.center),"Center"));
        SpinnerAdapterForPair titleImageAlignmentListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                titleImageAlignmentList);
        spinnerTitleImageAlignemnt.setAdapter(titleImageAlignmentListAdapter);

    }

    private void setSavedValue() {
        lightThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_LIGHT_THRESHOLD, "" + ImageProcessingSDK.dLightThreshold));
        maxFocusThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_MAX_FOCUS_THRESHOLD, "" + ImageProcessingSDK.dMaxFocusThreshold));
        minFocusThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_MIN_FOCUS_THRESHOLD, "" + ImageProcessingSDK.dMinFocusThreshold));
        glarePercentageEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_GLARE_PERCENTAGE, "" + ImageProcessingSDK.dGlarePercentage));
        enableCaptureButtonTimeEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), CAPTURE_BUTTON_TIME, "" + ImageProcessingSDK.dCaptureBtnEnableTime));
        maxImageSizeEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), MAX_IMAGE_SIZE, "" + ImageProcessingSDK.dImageSize));
        imageHeightEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_HEIGHT, "" + ImageProcessingSDK.dImageHeight));
        imageWidthEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_WIDTH, "" + ImageProcessingSDK.dImgaeWidth));
        idOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_COLOR, "" + ImageProcessingSDK.dIdOutlineColor));
        idOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_ALPHA, "" + ImageProcessingSDK.dAlpha));
        detectedIdOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_COLOR, "" + ImageProcessingSDK.dDetectedIdOutlineColor));
        detectedIdOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_ALPHA, "" + ImageProcessingSDK.dAlpha));
        idOutsideOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_COLOR, "" + ImageProcessingSDK.dOutsideIdOutlineColor));
        idOutsideOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_ALPHA, "" + ImageProcessingSDK.dAlpha));
        detectedOutsideOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE, "" + ImageProcessingSDK.dDetectedOutsideIdOutlineColor));
        detectedOutsideOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA, "" + ImageProcessingSDK.dAlpha));

        String showTitle = PreferenceUtils.getPreference(getActivity(), SELECT_TITLE, "None");
        if (showTitle.equalsIgnoreCase("None")) {
            spinnerTitle.setSelection(0);
        } else {
            spinnerTitle.setSelection(1);
        }

        String showTitleAlignment = PreferenceUtils.getPreference(getActivity(), ID_TITLE, "Top");
        spinnerTitleAlignment.setSelection(((ArrayAdapter<String>) spinnerTitleAlignment.getAdapter()).getPosition(showTitleAlignment));

        String showHintAlignment = PreferenceUtils.getPreference(getActivity(), ID_HINT, "Center");
        spinnerHintAlignment.setSelection(((ArrayAdapter<String>) spinnerHintAlignment.getAdapter()).getPosition(showHintAlignment));

        String showTitleImageAlignemnt = PreferenceUtils.getPreference(getActivity(), ID_TITLE_IMAGE, "Bottom");
        spinnerTitleImageAlignemnt.setSelection(((ArrayAdapter<String>) spinnerTitleImageAlignemnt.getAdapter()).getPosition(showTitleImageAlignemnt));

        boolean idTitleHide = PreferenceUtils.getPreference(getActivity(), ID_TITLE_HIDE, false);
        if (idTitleHide) {
            titleAlignmentCB.setChecked(true);
        }

        boolean idHintHide = PreferenceUtils.getPreference(getActivity(), ID_HINT_HIDE, false);
        if (idHintHide) {
            hintAlignmentCB.setChecked(true);
        }

        boolean idTitleImageHide = PreferenceUtils.getPreference(getActivity(), ID_TITLE_IMAGE_HIDE, false);
        if (idTitleImageHide) {
            titleImageAlignmentCB.setChecked(true);
        }

        boolean captureMode = PreferenceUtils.getPreference(getActivity(), CAPTURE_PORTRAIT_MODE, ImageProcessingSDK.dPortraitOrientation);
        if (captureMode) {
            radioGroupOrientation.check(radioGroupOrientation.getChildAt(0).getId());
        } else {
            radioGroupOrientation.check(radioGroupOrientation.getChildAt(1).getId());
        }

        boolean showInstruction = PreferenceUtils.getPreference(getActivity(), SHOW_INSTRUCTION, ImageProcessingSDK.dShowInstruction);
        if (showInstruction) {
            radioGroupInstruction.check(radioGroupInstruction.getChildAt(1).getId());
        } else {
            radioGroupInstruction.check(radioGroupInstruction.getChildAt(0).getId());
        }
    }

    private void resetValue() {
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
        englishLabelEdtTxt.setText("");
        spanishLabelEdtTxt.setText("");
        spinnerTitle.setSelection(0);
        spinnerTitleAlignment.setSelection(0);
        spinnerHintAlignment.setSelection(0);
        spinnerTitleImageAlignemnt.setSelection(0);
        spanishLabelEdtTxt.setSelection(0);
        titleAlignmentCB.setChecked(false);
        hintAlignmentCB.setChecked(false);
        titleImageAlignmentCB.setChecked(false);

        //radioGroupOrientation.check(radioGroupOrientation.getChildAt(0).getId());
        //radioGroupInstruction.check(radioGroupInstruction.getChildAt(0).getId());
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

        //Add label API call for Reset Value
        englishLabelMap.clear();
        spanishLabelMap.clear();
        ImageProcessingSDK.getInstance().initializeLabels(englishLabelMap, spanishLabelMap);
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
                    poaCaptureAPICall();
                }
            }
        });
        buttonAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String engLabel = englishLabelEdtTxt.getText().toString().trim();
                if (!StringUtil.isEmpty(engLabel)) {
                    englishLabelMap.put((String) spinnerLabel.getSelectedItem(), engLabel);
                }

                String espLabel = spanishLabelEdtTxt.getText().toString().trim();
                if (!StringUtil.isEmpty(espLabel)) {
                    spanishLabelMap.put((String) spinnerLabel.getSelectedItem(), espLabel);
                }
                englishLabelEdtTxt.setText("");
                spanishLabelEdtTxt.setText("");
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

                PreferenceUtils.setPreference(getActivity(), IMAGE_LIGHT_THRESHOLD, lightThreshold);
                PreferenceUtils.setPreference(getActivity(), IMAGE_MAX_FOCUS_THRESHOLD, maxFocusThreshold);
                PreferenceUtils.setPreference(getActivity(), IMAGE_MIN_FOCUS_THRESHOLD, minFocusThreshold);
                PreferenceUtils.setPreference(getActivity(), IMAGE_GLARE_PERCENTAGE, glareThreshold);
                PreferenceUtils.setPreference(getActivity(), CAPTURE_BUTTON_TIME, captureBtnTime);
                PreferenceUtils.setPreference(getActivity(), MAX_IMAGE_SIZE, maxImagesize);
                PreferenceUtils.setPreference(getActivity(), IMAGE_HEIGHT, imageHeight);
                PreferenceUtils.setPreference(getActivity(), IMAGE_WIDTH, imageWidth);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTLINE_COLOR, idOutlineColor);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTLINE_ALPHA, "" + idOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_COLOR, detectedIdOutlineColor);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_ALPHA, "" + detectedIdOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_COLOR, idOutsideOutlineColor);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_ALPHA, "" + idOutsideOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE, "" + detectedIdOutsideOutlineColor);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA, "" + detectedIdOutsideOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), SELECT_TITLE, showTitle);
                PreferenceUtils.setPreference(getActivity(), CAPTURE_PORTRAIT_MODE, capturePortrait);
                PreferenceUtils.setPreference(getActivity(), SHOW_INSTRUCTION, showInstruction);

                //API call for custom UI
                idTitleAlignment = ((Pair)spinnerTitleAlignment.getSelectedItem()).second.toString();
                idHintAlignment = ((Pair)spinnerHintAlignment.getSelectedItem()).second.toString();
                idTitleImageAlignment = ((Pair)spinnerTitleImageAlignemnt.getSelectedItem()).second.toString();
                String faceTitle = PreferenceUtils.getPreference(getActivity(), FACE_TITLE, "Bottom");
                String faceHint = PreferenceUtils.getPreference(getActivity(), FACE_HINT, "Top");
                String faceHintIcon = PreferenceUtils.getPreference(getActivity(), FACE_ICON, "Top");
                String faceTitleImage = PreferenceUtils.getPreference(getActivity(), FACE_TITLE_IMAGE, "Top");
                boolean faceTitleHide = PreferenceUtils.getPreference(getActivity(), FACE_TITLE_HIDE, false);
                boolean faceHintHide = PreferenceUtils.getPreference(getActivity(), FACE_HINT_HIDE, false);
                boolean faceHintIconHide = PreferenceUtils.getPreference(getActivity(), FACE_ICON_HIDE, false);
                boolean faceTitleImageHide = PreferenceUtils.getPreference(getActivity(), FACE_TITLE_IMAGE_HIDE, false);

                boolean faceTitleOnTop, faceHintOnTop, faceHintIconOnTop, faceTitleImageAlignment;
                if (faceTitle.equalsIgnoreCase("Bottom")) {
                    faceTitleOnTop = false;
                } else {
                    faceTitleOnTop = true;
                }

                if (faceHint.equalsIgnoreCase("Bottom")) {
                    faceHintOnTop = false;
                } else {
                    faceHintOnTop = true;
                }

                if (faceHintIcon.equalsIgnoreCase("Bottom")) {
                    faceHintIconOnTop = false;
                } else {
                    faceHintIconOnTop = true;
                }

                if (faceTitleImage.equalsIgnoreCase("Bottom")) {
                    faceTitleImageAlignment = false;
                } else {
                    faceTitleImageAlignment = true;
                }

                if (titleAlignmentCB.isChecked()) {
                    hideIdCaptureTitle = true;
                } else {
                    hideIdCaptureTitle = false;
                }

                if (hintAlignmentCB.isChecked()) {
                    hideIdCaptureHintMsg = true;
                } else {
                    hideIdCaptureHintMsg = false;
                }

                if (titleImageAlignmentCB.isChecked()) {
                    hideIdCaptureTitleImg = true;
                } else {
                    hideIdCaptureTitleImg = false;
                }

                ImageProcessingSDK.getInstance().imageProcessingSDK.customizeUserInterface(faceTitleOnTop, faceTitleHide,
                        faceHintOnTop, faceHintHide,
                        faceHintIconOnTop, faceHintIconHide,
                        faceTitleImageAlignment, faceTitleImageHide,
                        idTitleAlignment, hideIdCaptureTitle,
                        idHintAlignment, hideIdCaptureHintMsg,
                        idTitleImageAlignment, hideIdCaptureTitleImg, ImageProcessingSDK.getInstance());

                //API call for add label
                ImageProcessingSDK.getInstance().initializeLabels(englishLabelMap, spanishLabelMap);

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
                poaCaptureAPICall();
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

    private void poaCaptureAPICall() {
        ImageProcessingSDK.getInstance().setImageProcessingResponseListener(POACapture.this);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showErrorMessage(getView(), "Need to enable CAMERA permission.");
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
            }
        } else {
            String lightThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_LIGHT_THRESHOLD, "" + ImageProcessingSDK.dLightThreshold);
            String minFocusThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_MIN_FOCUS_THRESHOLD, "" + ImageProcessingSDK.dMinFocusThreshold);
            String maxFocusThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_MAX_FOCUS_THRESHOLD, "" + ImageProcessingSDK.dMaxFocusThreshold);
            String glareThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_GLARE_PERCENTAGE, "" + ImageProcessingSDK.dGlarePercentage);
            String captureBtnTime = PreferenceUtils.getPreference(getActivity(), CAPTURE_BUTTON_TIME, "" + ImageProcessingSDK.dCaptureBtnEnableTime);
            String maxImagesize = PreferenceUtils.getPreference(getActivity(), MAX_IMAGE_SIZE, "" + ImageProcessingSDK.dImageSize);
            String imageHeight = PreferenceUtils.getPreference(getActivity(), IMAGE_HEIGHT, "" + ImageProcessingSDK.dImageHeight);
            String imageWidth = PreferenceUtils.getPreference(getActivity(), IMAGE_WIDTH, "" + ImageProcessingSDK.dImgaeWidth);
            String idImgOutlineColor = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_COLOR, "" + ImageProcessingSDK.dIdOutlineColor);
            String idImgOutlineAlpha = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_ALPHA, "" + ImageProcessingSDK.dAlpha);
            String detectedIdImgOutlineColor = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_COLOR, "" + ImageProcessingSDK.dDetectedIdOutlineColor);
            String detectedIdImgOutlineAlpha = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_ALPHA, "" + ImageProcessingSDK.dAlpha);
            String idImgOutsideOutlineColor = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_COLOR, "" + ImageProcessingSDK.dOutsideIdOutlineColor);
            String idImgOutsideOutlineAlpha = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_ALPHA, "" + ImageProcessingSDK.dAlpha);

            String detectedIdImgOutsideOutlineColor = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE, "" + ImageProcessingSDK.dDetectedOutsideIdOutlineColor);
            String detectedIdImgOutsideOutlineAlpha = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA, "" + ImageProcessingSDK.dAlpha);
            String titleImageType = PreferenceUtils.getPreference(getActivity(), SELECT_TITLE, "None");
            boolean capturePortraitMode = PreferenceUtils.getPreference(getActivity(), CAPTURE_PORTRAIT_MODE, ImageProcessingSDK.dPortraitOrientation);
            boolean showInstruction = PreferenceUtils.getPreference(getActivity(), SHOW_INSTRUCTION, ImageProcessingSDK.dShowInstruction);

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

//                    ImageProcessingSDK.getInstance().autoCapture(getActivity(), ImageType.BACK, capturePortraitMode, minimumLightThreshold, minFocusScoreThreshold,
//                            focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
//                            highResolutionImageHeight, highResolutionImageWidth, null,
//                            new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor, detectedIdImageOutsideOutlineAlpha), titleBitmap,
//                            showInstruction, 0);

            eVolvApp.setJsonObjAddressDetails(getAddressDataJSON());

            JSONObject addJSON = eVolvApp.getAdditonalData();

            if (null != addJSON) {
                ImageProcessingSDK.getInstance().captureProofOfAddress(getActivity(), capturePortraitMode, minimumLightThreshold, minFocusScoreThreshold,
                        focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
                        highResolutionImageHeight, highResolutionImageWidth, addJSON,
                        new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor, detectedIdImageOutsideOutlineAlpha), titleBitmap,
                        showInstruction, 0);
            } else {
                ImageProcessingSDK.getInstance().captureProofOfAddress(getActivity(), capturePortraitMode, minimumLightThreshold, minFocusScoreThreshold,
                        focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
                        highResolutionImageHeight, highResolutionImageWidth, null,
                        new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor, detectedIdImageOutsideOutlineAlpha), titleBitmap,
                        showInstruction, 0);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bitmap bitmap = BitmapUtils.getBitmapFromFile(eVolvApp.getBaseDirectoryPath() + Constants.POA_CAPTURE_IMAGE);
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

    private JSONObject getAddressDataJSON() {
        String address1 = address1EdtTxt.getText().toString().trim();
        String address2 = address2EdtTxt.getText().toString().trim();
        String country = countryEdtTxt.getText().toString().trim();
        String state = stateEdtTxt.getText().toString().trim();
        String zipCode = zipCodeEdtTxt.getText().toString().trim();

        JSONObject jObject = new JSONObject();
        try {
            /*
             * Only add key without spaces
             * */
            if (!StringUtil.isEmpty(address1)) {
                jObject.put("AddrLine1", address1);

            }

            if (!StringUtil.isEmpty(address2)) {
                jObject.put("AddrLine2", address2);

            }

            if (!StringUtil.isEmpty(country)) {
                jObject.put("Country", country);
            }

            if (!StringUtil.isEmpty(state)) {
                jObject.put("State", state);
            }

            if (!StringUtil.isEmpty(zipCode)) {
                jObject.put("ZipCode", zipCode);
            }

        } catch (JSONException exc) {
            Log.d(LOGTAG, "getAddressDataJSON Exc : " + exc);
        }

        if (jObject.length() > 0)
            return jObject;

        return null;
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
        Log.d("SDK", "CALLBACK:::: onCaptureProofOfAddressResultAvailable");
        if (null != response) {
            // Toast.makeText(MainActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();
            if (response.getStatusCode() == ResponseStatusCode.PERMISSION_NOT_GRANTED.getStatusCode()) {
                showErrorMessage(getView(), getString(R.string.permission_not_granted));
            } else {
                String poaImageBase64 = null;
                if (null != resultMap) {
                    if (resultMap.containsKey(ImageType.POA_IMAGE.toString())) {
                        poaImageBase64 = resultMap.get(ImageType.POA_IMAGE.toString());
                    }
                }
                if (!StringUtil.isEmpty(poaImageBase64)) {
                    // capturedImageData.put(ResultData.POA_IMAGE_DATA, new ResultData(ResultData.POA_IMAGE_DATA, poaImageBase64));
                    byte[] decodedString = Base64.decode(poaImageBase64, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    BitmapUtils.writeBitmapToFilepath(eVolvApp.getBaseDirectoryPath() + Constants.POA_CAPTURE_IMAGE, bitmap);
                    setImage(bitmap); //Set this image as first image
                    isCapture = false;
                }
            }
        }

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
        PreferenceUtils.setPreference(getActivity(), ID_TITLE, idTitleAlignment);
        PreferenceUtils.setPreference(getActivity(), ID_HINT, idHintAlignment);
        PreferenceUtils.setPreference(getActivity(), ID_TITLE_IMAGE, idTitleImageAlignment);
        PreferenceUtils.setPreference(getActivity(), ID_TITLE_HIDE, hideIdCaptureTitle);
        PreferenceUtils.setPreference(getActivity(), ID_HINT_HIDE, hideIdCaptureHintMsg);
        PreferenceUtils.setPreference(getActivity(), ID_TITLE_IMAGE_HIDE, hideIdCaptureTitleImg);
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

