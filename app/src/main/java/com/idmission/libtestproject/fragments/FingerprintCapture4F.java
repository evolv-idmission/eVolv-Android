package com.idmission.libtestproject.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.idmission.client.FingerType;
import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.Response;
import com.idmission.client.ResponseStatusCode;
import com.idmission.client.UIConfigurationParameters;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.adapter.PageAdapter;
import com.idmission.libtestproject.adapter.SpinnerAdapterForPair;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FingerprintCapture4F extends Fragment implements ImageProcessingResponseListener {

    private static final String[] FINGER_TYPE = {"ALL", "LLFINGER", "LIFINGER", "LRFINGER", "LMFINGER", "RRFINGER", "RIFINGER", "RLFINGER", "RMFINGER"};
    //private static final String[] TEXT_FONT_TYPE_ARRAY = {"DEFAULT","DEFAULT_BOLD","SANS_SERIF","SERIF","MONOSPACE"};
    //private static final String[] TEXT_FONT_STYLE_ARRAY = {"NORMAL","BOLD","ITALIC","BOLD_ITALIC"};
    private BottomSheetBehavior bottomSheetBehaviorCaptureId;
    private ImageView bottomSheetImage, defaultImage, captureImage;
    private LinearLayout linearLayout, linearLayoutIndicator, linearLayoutFPClear, linearLayoutCapture;
    private boolean isExpand = false, isCapture = true;
    private TextView textViewDefault;
    private Button buttonBack, buttonNext, buttonCapture, buttonSave, buttonReset, buttonClearFP;
    private EditText textLabelColorEdtTxt, textLabelAlphaEdtTxt, instructionContinueBtnColorEdtTxt, instructionContinueBtnAlphaEdtTxt, instructionContinueBtnTxtColorEdtTxt, instructionContinueBtnTxtAlphaEdtTxt, glareThresholdEdtTxt, fingerCheckFocusEdtTxt, focusEdtTxt, indexFingerThresholdEdtTxt, indexFingerThresholdEdtTxtMin, middleFingerThresholdEdtTxt, middleFingerThresholdEdtTxtMin, ringFingerThresholdEdtTxt, ringFingerThresholdEdtTxtMin, babyFingerThresholdEdtTxt, babyFingerThresholdEdtTxtMin, saveImageWidthEdtTxt, aggresivenessFactorEdtTxt, zoomEdtTxt, ridgeWidthTxt, intialFrameHeight,
            fingerLengthPercentageEdtTxt, sharpThresholdEdtTxt, fingerWidthAveCounterEdtTxt, farPercentage, tooFarPercentage;
    private RadioGroup radioGroupHandtype;
    private CheckBox processIndexFingerCb, processMiddelFingerCb, processRingFingerCb, processBabyFingerCb, processCapturedFrame, maskUnwantedArea,
            processOnlyIndexFingerCb, processOnlyMiddelFingerCb, processOnlyRingFingerCb, processOnlyBabyFingerCb;
    private static final String FINGER_FOCUS_RATIO = "FINGER_FOCUS_RATIO", FINGER_GLARE_THRESHOLD = "FINGER_GLARE_THRESHOLD", FINGER_TO_CHECK_FOCUS = "FINGER_TO_CHECK_FOCUS",
            INDEX_FINGER_THRESHOLD = "mINDEX_FINGER_THRESHOLD", MIDDLE_FINGER_THRESHOLD = "mMIDDLE_FINGER_THRESHOLD", RING_FINGER_THRESHOLD = "mRING_FINGER_THRESHOLD", BABY_FINGER_THRESHOLD = "mBABY_FINGER_THRESHOLD",
            INDEX_FINGER_THRESHOLD_MIN = "mINDEX_FINGER_THRESHOLD_MIN", MIDDLE_FINGER_THRESHOLD_MIN = "mMIDDLE_FINGER_THRESHOLD_MIN", RING_FINGER_THRESHOLD_MIN = "mRING_FINGER_THRESHOLD_MIN", BABY_FINGER_THRESHOLD_MIN = "mBABY_FINGER_THRESHOLD_MIN",
            SAVE_IMAGE_WIDTH_4F = "SAVE_IMAGE_WIDTH_4F", AGGRESSIVENESS_FACTOR = "AGGRESSIVENESS_FACTOR", ZOOM_CAMERA = "ZOOM_CAMERA", RIDGE_WIDTH = "RIDGE_WIDTH", FINGER_LENGTH_PERCENTAGE = "FINGER_LENGTH_PERCENTAGE", SHARP_THRESHOLD = "SHARP_THRESHOLD", FINGER_WIDTH_AV_COUNTER = "FINGER_WIDTH_AV_COUNTER", HAND_TYPE_LEFT = "HAND_TYPE_LEFT", FP_INSTRUCTION_BTN_COLOR = "FP_INSTRUCTION_BTN_COLOR", FP_INSTRUCTION_BTN_ALPHA = "FP_INSTRUCTION_BTN_ALPHA",
            FP_INSTRUCTION_BTN_TXT_COLOR = "FP_INSTRUCTION_BTN_TXT_COLOR", FP_INSTRUCTION_BTN_TXT_ALPHA = "FP_INSTRUCTION_BTN_TXT_ALPHA", TEXT_FONT_TYPE_FP = "TEXT_FONT_TYPE_FP", TEXT_FONT_STYLE_FP = "TEXT_FONT_STYLE_FP", TEXT_LABEL_COLOR_FP = "TEXT_LABEL_COLOR_FP", TEXT_LABEL_ALPHA_FP = "TEXT_LABEL_ALPHA_FP", SHOW_INSTRUCTION_SCREEN = "SHOW_INSTRUCTION_SCREEN",
            INDEX_CAPTURE="INDEX_CAPTURE", MIDDLE_CAPTURE="MIDDLE_CAPTURE", RING_CAPTURE="RING_CAPTURE", BABY_CAPTURE="BABY_CAPTURE", PROCESS_ONLY_INDEX_CAPTURE="PROCESS_ONLY_INDEX_CAPTURE", PROCESS_ONLY_MIDDLE_CAPTURE="PROCESS_ONLY_MIDDLE_CAPTURE", PROCESS_ONLY_RING_CAPTURE="PROCESS_ONLY_RING_CAPTURE", PROCESS_ONLY_BABY_CAPTURE="PROCESS_ONLY_BABY_CAPTURE", PROCESS_CAPTURED_FRAME="PROCESS_CAPTURED_FRAME", INITIAL_FRAME_HEIGHT="INITIAL_FRAME_HEIGHT", MASK_UNWANTED_PART="MASK_UNWANTED_PART", FAR_PERCENTAGE = "FAR_PERCENTAGE", TOO_FAR_PERCENTAGE = "TOO_FAR_PERCENTAGE";
    //    private int DEFAULT_FINGER_FOCUS_RATIO_VALUE = 15, DEFAULT_FINGER_GLARE_THRESHOLD = 30, DEFAULT_FINGER_TO_CHECK_FOCUS = 1;
    private ViewPager viewPagerFP;
    private PageAdapter adapter;
    private Spinner spinnerFP;
    private ImageView[] ivArrayDotsPager;
    private Spinner spinnerTextFontType, spinnerTextFontStyle;
    private SwitchCompat showInstructionFPDetect;
    private EVolvApp eVolvApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.fingerprint_4fcapture_layout, container, false);

        bottomSheetBehaviorCaptureId = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheetCaptureFingerPrint));
        bottomSheetImage = (ImageView) view.findViewById(R.id.image_view);
        defaultImage = (ImageView) view.findViewById(R.id.default_image);
        captureImage = (ImageView) view.findViewById(R.id.capture_image);
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_top);
        linearLayoutCapture = (LinearLayout) view.findViewById(R.id.linear_layout_capture);
        linearLayoutIndicator = (LinearLayout) view.findViewById(R.id.linear_layout_indicator);
        linearLayoutFPClear = (LinearLayout) view.findViewById(R.id.linear_layout_clear_fp);
        textViewDefault = (TextView) view.findViewById(R.id.text_view_label);
        spinnerFP = (Spinner) view.findViewById(R.id.spinner_finger);
        buttonCapture = (Button) view.findViewById(R.id.button_capture);
        buttonClearFP = (Button) view.findViewById(R.id.button_clear_fp);
        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);

        glareThresholdEdtTxt = (EditText) view.findViewById(R.id.edit_view_glare_threshold);
        fingerCheckFocusEdtTxt = (EditText) view.findViewById(R.id.edit_view_finger_check_focus);
        focusEdtTxt = (EditText) view.findViewById(R.id.edit_view_focus_threshold);
        indexFingerThresholdEdtTxtMin = (EditText) view.findViewById(R.id.index_finger_threshold_4f_min);
        indexFingerThresholdEdtTxt = (EditText) view.findViewById(R.id.index_finger_threshold_4f);
        middleFingerThresholdEdtTxtMin = (EditText) view.findViewById(R.id.middle_finger_threshold_4f_min);
        middleFingerThresholdEdtTxt = (EditText) view.findViewById(R.id.middle_finger_threshold_4f);
        ringFingerThresholdEdtTxtMin = (EditText) view.findViewById(R.id.ring_finger_threshold_4f_min);
        ringFingerThresholdEdtTxt = (EditText) view.findViewById(R.id.ring_finger_threshold_4f);
        babyFingerThresholdEdtTxtMin = (EditText) view.findViewById(R.id.baby_finger_threshold_4f_min);
        babyFingerThresholdEdtTxt = (EditText) view.findViewById(R.id.baby_finger_threshold_4f);
        saveImageWidthEdtTxt = (EditText) view.findViewById(R.id.save_image_width_4f);
        aggresivenessFactorEdtTxt = (EditText) view.findViewById(R.id.aggresiveness_factor_4f);
        zoomEdtTxt = (EditText) view.findViewById(R.id.zoom_camera_4f);
        farPercentage = (EditText) view.findViewById(R.id.far_percentage);
        tooFarPercentage = (EditText) view.findViewById(R.id.too_far_percentage);
        ridgeWidthTxt = (EditText) view.findViewById(R.id.ridge_width_4f);
        fingerLengthPercentageEdtTxt = (EditText) view.findViewById(R.id.finger_length_perc_4f);
        sharpThresholdEdtTxt = (EditText) view.findViewById(R.id.sharp_threshold_4f);
        fingerWidthAveCounterEdtTxt = (EditText) view.findViewById(R.id.counter_finger_width_average);
        radioGroupHandtype = (RadioGroup) view.findViewById(R.id.radio_group_handtype);

        processIndexFingerCb = (CheckBox)view.findViewById(R.id.process_indexfinger_cb);
        processMiddelFingerCb = (CheckBox)view.findViewById(R.id.process_middlefinger_cb);
        processRingFingerCb = (CheckBox)view.findViewById(R.id.process_ringfinger_cb);
        processBabyFingerCb = (CheckBox)view.findViewById(R.id.process_babyfinger_cb);

        processOnlyIndexFingerCb = (CheckBox)view.findViewById(R.id.process_only_indexfinger_cb);
        processOnlyMiddelFingerCb = (CheckBox)view.findViewById(R.id.process_only_middlefinger_cb);
        processOnlyRingFingerCb = (CheckBox)view.findViewById(R.id.process_only_ringfinger_cb);
        processOnlyBabyFingerCb = (CheckBox)view.findViewById(R.id.process_only_babyfinger_cb);

        processCapturedFrame = (CheckBox)view.findViewById(R.id.process_captured_frame);
        intialFrameHeight = (EditText)view.findViewById(R.id.initial_frame_height);
        maskUnwantedArea = (CheckBox)view.findViewById(R.id.mask_unwanted_part);


        buttonSave = (Button) view.findViewById(R.id.button_save);
        buttonReset = (Button) view.findViewById(R.id.button_reset);

        viewPagerFP = (ViewPager) view.findViewById(R.id.view_pager_fp);

        showInstructionFPDetect = (SwitchCompat) view.findViewById(R.id.show_instruction_fp_detect);
        spinnerTextFontType = (Spinner) view.findViewById(R.id.text_font_type_spinner);
        spinnerTextFontStyle = (Spinner) view.findViewById(R.id.text_font_style_spinner);
        textLabelColorEdtTxt = (EditText) view.findViewById(R.id.text_label_color);
        textLabelAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_text_label_color);
        instructionContinueBtnColorEdtTxt = (EditText) view.findViewById(R.id.instruction_continue_button_color);
        instructionContinueBtnAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_instruction_continue_button);
        instructionContinueBtnTxtColorEdtTxt = (EditText) view.findViewById(R.id.instruction_continue_button_text_color);
        instructionContinueBtnTxtAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_instruction_continue_button_text);

        textViewDefault.setText(getString(R.string.capture));

        eVolvApp = (EVolvApp) getActivity().getApplicationContext();

        //spinnerFP.setAdapter(new ArrayAdapter<FingerType>(getContext(), android.R.layout.simple_dropdown_item_1line, bindFPTypeData()));
        spinnerFP.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, FINGER_TYPE));
       // spinnerTextFontType.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TEXT_FONT_TYPE_ARRAY));
       // spinnerTextFontStyle.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TEXT_FONT_STYLE_ARRAY));

        setSpinnerAdapter();
        setDefaultValue();
        return view;
    }

    private void setSpinnerAdapter(){
        ArrayList<Pair<String,String>> textFontTypeList = new ArrayList<Pair<String,String>>();
        textFontTypeList.add(new Pair<String, String>(getString(R.string.default_font_type),"DEFAULT"));
        textFontTypeList.add(new Pair<String, String>(getString(R.string.default_bold),"DEFAULT_BOLD"));
        textFontTypeList.add(new Pair<String, String>(getString(R.string.san_serif),"SANS_SERIF"));
        textFontTypeList.add(new Pair<String, String>(getString(R.string.serif),"SERIF"));
        textFontTypeList.add(new Pair<String, String>(getString(R.string.monospace),"MONOSPACE"));
        SpinnerAdapterForPair textFontTypeListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                textFontTypeList);
        spinnerTextFontType.setAdapter(textFontTypeListAdapter);

        ArrayList<Pair<String,String>> textFontStyleList = new ArrayList<Pair<String,String>>();
        textFontStyleList.add(new Pair<String, String>(getString(R.string.normal),"NORMAL"));
        textFontStyleList.add(new Pair<String, String>(getString(R.string.bold),"BOLD"));
        textFontStyleList.add(new Pair<String, String>(getString(R.string.italic),"ITALIC"));
        textFontStyleList.add(new Pair<String, String>(getString(R.string.bold_italic),"BOLD_ITALIC"));
        SpinnerAdapterForPair textFontStyleListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                textFontStyleList);
        spinnerTextFontStyle.setAdapter(textFontStyleListAdapter);
    }

    private void setDefaultValue() {
        glareThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FINGER_GLARE_THRESHOLD, "" + ImageProcessingSDK.dGlare4f));
        fingerCheckFocusEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FINGER_TO_CHECK_FOCUS, "" + ImageProcessingSDK.dNumberOfFingerToCheck));
        focusEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FINGER_FOCUS_RATIO, "" + ImageProcessingSDK.dFocus4f));
        indexFingerThresholdEdtTxtMin.setText(PreferenceUtils.getPreference(getActivity(), INDEX_FINGER_THRESHOLD_MIN, "" + ImageProcessingSDK.dIndexFingerThreshold4FMin));
        middleFingerThresholdEdtTxtMin.setText(PreferenceUtils.getPreference(getActivity(), MIDDLE_FINGER_THRESHOLD_MIN, "" + ImageProcessingSDK.dMiddleFingerThreshold4FMin));
        ringFingerThresholdEdtTxtMin.setText(PreferenceUtils.getPreference(getActivity(), RING_FINGER_THRESHOLD_MIN, "" + ImageProcessingSDK.dRingFingerThreshold4FMin));
        babyFingerThresholdEdtTxtMin.setText(PreferenceUtils.getPreference(getActivity(), BABY_FINGER_THRESHOLD_MIN, "" + ImageProcessingSDK.dBabyFingerThreshold4FMin));
        indexFingerThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), INDEX_FINGER_THRESHOLD, "" + ImageProcessingSDK.dIndexFingerThreshold4F));
        middleFingerThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), MIDDLE_FINGER_THRESHOLD, "" + ImageProcessingSDK.dMiddleFingerThreshold4F));
        ringFingerThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), RING_FINGER_THRESHOLD, "" + ImageProcessingSDK.dRingFingerThreshold4F));
        babyFingerThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), BABY_FINGER_THRESHOLD, "" + ImageProcessingSDK.dBabyFingerThreshold4F));
        saveImageWidthEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), SAVE_IMAGE_WIDTH_4F, "" + ImageProcessingSDK.dSaveImageWidth4F));
        aggresivenessFactorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), AGGRESSIVENESS_FACTOR, "" + ImageProcessingSDK.dAggressivenessFactor4F));
        zoomEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ZOOM_CAMERA, "" + ImageProcessingSDK.dZoom4F));
        farPercentage.setText(PreferenceUtils.getPreference(getActivity(), FAR_PERCENTAGE, "" + ImageProcessingSDK.dFarPercentage));
        tooFarPercentage.setText(PreferenceUtils.getPreference(getActivity(), TOO_FAR_PERCENTAGE, "" + ImageProcessingSDK.dTooFarPercenatge));
        ridgeWidthTxt.setText(PreferenceUtils.getPreference(getActivity(), RIDGE_WIDTH, "" + ImageProcessingSDK.dRidgeWidth));
        fingerLengthPercentageEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FINGER_LENGTH_PERCENTAGE, "" + ImageProcessingSDK.dFingerLengthPercentage));
        sharpThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), SHARP_THRESHOLD, "" + ImageProcessingSDK.dSharpThreshold));
        fingerWidthAveCounterEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FINGER_WIDTH_AV_COUNTER, "" + ImageProcessingSDK.dFingerWidthAverageCounter));
        textLabelColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_COLOR_FP, ""+ ImageProcessingSDK.textLabelColor));
        textLabelAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_ALPHA_FP, ""+ ImageProcessingSDK.dAlpha));
        instructionContinueBtnColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FP_INSTRUCTION_BTN_COLOR, ""+ ImageProcessingSDK.instructionContinueButtonColor));
        instructionContinueBtnAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FP_INSTRUCTION_BTN_ALPHA, ""+ ImageProcessingSDK.dAlpha));
        instructionContinueBtnTxtColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FP_INSTRUCTION_BTN_TXT_COLOR, ""+ ImageProcessingSDK.instructionContinueButtonTextColor));
        instructionContinueBtnTxtAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FP_INSTRUCTION_BTN_TXT_ALPHA, ""+ ImageProcessingSDK.dAlpha));
        showInstructionFPDetect.setChecked(PreferenceUtils.getPreference(getActivity(), SHOW_INSTRUCTION_SCREEN, ImageProcessingSDK.dFaceShowInstructionScreen));
        processIndexFingerCb.setChecked(PreferenceUtils.getPreference(getActivity(), INDEX_CAPTURE, ImageProcessingSDK.dIndexCapture));
        processMiddelFingerCb.setChecked(PreferenceUtils.getPreference(getActivity(), MIDDLE_CAPTURE, ImageProcessingSDK.dMiddleCapture));
        processRingFingerCb.setChecked(PreferenceUtils.getPreference(getActivity(), RING_CAPTURE, ImageProcessingSDK.dRingCapture));
        processBabyFingerCb.setChecked(PreferenceUtils.getPreference(getActivity(), BABY_CAPTURE, ImageProcessingSDK.dBabyCapture));
        processOnlyIndexFingerCb.setChecked(PreferenceUtils.getPreference(getActivity(), PROCESS_ONLY_INDEX_CAPTURE, ImageProcessingSDK.dIndexKeep));
        processOnlyMiddelFingerCb.setChecked(PreferenceUtils.getPreference(getActivity(), PROCESS_ONLY_MIDDLE_CAPTURE, ImageProcessingSDK.dMiddleKeep));
        processOnlyRingFingerCb.setChecked(PreferenceUtils.getPreference(getActivity(), PROCESS_ONLY_RING_CAPTURE, ImageProcessingSDK.dRingKeep));
        processOnlyBabyFingerCb.setChecked(PreferenceUtils.getPreference(getActivity(), PROCESS_ONLY_BABY_CAPTURE, ImageProcessingSDK.dBabyKeep));
        processCapturedFrame.setChecked(PreferenceUtils.getPreference(getActivity(), PROCESS_CAPTURED_FRAME, ImageProcessingSDK.dProcessCapturedFrame));
        intialFrameHeight.setText(PreferenceUtils.getPreference(getActivity(), INITIAL_FRAME_HEIGHT, "" + ImageProcessingSDK.dInitialFrameHeight));
        maskUnwantedArea.setChecked(PreferenceUtils.getPreference(getActivity(), MASK_UNWANTED_PART, ImageProcessingSDK.dMaskUnwantedPart));

        String textFontType = PreferenceUtils.getPreference(getActivity(), TEXT_FONT_TYPE_FP, "DEFAULT");
        spinnerTextFontType.setSelection(((ArrayAdapter<String>) spinnerTextFontType.getAdapter()).getPosition(textFontType));

        String textFontStyle = PreferenceUtils.getPreference(getActivity(), TEXT_FONT_STYLE_FP, "NORMAL");
        spinnerTextFontStyle.setSelection(((ArrayAdapter<String>) spinnerTextFontStyle.getAdapter()).getPosition(textFontStyle));


        if(PreferenceUtils.getPreference(getActivity(), HAND_TYPE_LEFT, ImageProcessingSDK.dCaptureLeftHand)){
            radioGroupHandtype.check(radioGroupHandtype.getChildAt(0).getId());
        }else {
            radioGroupHandtype.check(radioGroupHandtype.getChildAt(1).getId());
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
                    captureFPAPICall();
                }
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String focusRatio = focusEdtTxt.getText().toString().trim();
                String glareThreshold = glareThresholdEdtTxt.getText().toString().trim();
                String fingerToCheckFocus = fingerCheckFocusEdtTxt.getText().toString().trim();
                String indexFingerThreshold = indexFingerThresholdEdtTxt.getText().toString().trim();
                String middleFingerThreshold = middleFingerThresholdEdtTxt.getText().toString().trim();
                String ringFingerThreshold = ringFingerThresholdEdtTxt.getText().toString().trim();
                String babyFingerThreshold = babyFingerThresholdEdtTxt.getText().toString().trim();
                String indexFingerThresholdMin = indexFingerThresholdEdtTxtMin.getText().toString().trim();
                String middleFingerThresholdMin = middleFingerThresholdEdtTxtMin.getText().toString().trim();
                String ringFingerThresholdMin = ringFingerThresholdEdtTxtMin.getText().toString().trim();
                String babyFingerThresholdMin = babyFingerThresholdEdtTxtMin.getText().toString().trim();
                String saveImgWidth = saveImageWidthEdtTxt.getText().toString().trim();
                String aggresivenessFactor = aggresivenessFactorEdtTxt.getText().toString().trim();
                String zoomCamera = zoomEdtTxt.getText().toString().trim();
                String farPercent = farPercentage.getText().toString().trim();
                String tooFarPercent = tooFarPercentage.getText().toString().trim();
                String ridgeWidth = ridgeWidthTxt.getText().toString().trim();
                String fingLengthPer = fingerLengthPercentageEdtTxt.getText().toString().trim();
                String sharpThresh = sharpThresholdEdtTxt.getText().toString().trim();
                String fingWidthCounter = fingerWidthAveCounterEdtTxt.getText().toString().trim();
               // String textFontType = spinnerTextFontType.getSelectedItem().toString();
                //String textFontStyle = spinnerTextFontStyle.getSelectedItem().toString();
               // String textFontType = ((Pair)spinnerTextFontType.getSelectedItem()).second.toString();
                //String textFontStyle = ((Pair)spinnerTextFontStyle.getSelectedItem()).second.toString();
                String textLabelColor = textLabelColorEdtTxt.getText().toString().trim();
                String textLabelAlpha = textLabelAlphaEdtTxt.getText().toString().trim();
                String instructionContinueBtnColor = instructionContinueBtnColorEdtTxt.getText().toString().trim();
                String instructionContinueBtnColorAlpha = instructionContinueBtnAlphaEdtTxt.getText().toString().trim();
                String instructionContinueBtnTxtColor = instructionContinueBtnTxtColorEdtTxt.getText().toString().trim();
                String instructionContinueBtnTxtAlpha = instructionContinueBtnTxtAlphaEdtTxt.getText().toString().trim();
                boolean indexCapture = processIndexFingerCb.isChecked();
                boolean middleCapture = processMiddelFingerCb.isChecked();
                boolean ringCapture = processRingFingerCb.isChecked();
                boolean babyCapture = processBabyFingerCb.isChecked();
                boolean processOnlyIndexCapture = processOnlyIndexFingerCb.isChecked();
                boolean processOnlyMiddleCapture = processOnlyMiddelFingerCb.isChecked();
                boolean processOnlyRingCapture = processOnlyRingFingerCb.isChecked();
                boolean processOnlyBabyCapture = processOnlyBabyFingerCb.isChecked();
                boolean processCaptureFrame = processCapturedFrame.isChecked();
                boolean maskUnwantedPart = maskUnwantedArea.isChecked();
                String initialFrameHeight = intialFrameHeight.getText().toString().trim();
                boolean captureLeftHand = true;
                if (radioGroupHandtype.getCheckedRadioButtonId() != R.id.radio_button_lefthand) {
                    captureLeftHand = false;
                }


                PreferenceUtils.setPreference(getActivity(), FINGER_FOCUS_RATIO, focusRatio);
                PreferenceUtils.setPreference(getActivity(), FINGER_GLARE_THRESHOLD, glareThreshold);
                PreferenceUtils.setPreference(getActivity(), FINGER_TO_CHECK_FOCUS, fingerToCheckFocus);
                PreferenceUtils.setPreference(getActivity(), INDEX_FINGER_THRESHOLD_MIN, indexFingerThresholdMin);
                PreferenceUtils.setPreference(getActivity(), MIDDLE_FINGER_THRESHOLD_MIN, middleFingerThresholdMin);
                PreferenceUtils.setPreference(getActivity(), RING_FINGER_THRESHOLD_MIN, ringFingerThresholdMin);
                PreferenceUtils.setPreference(getActivity(), BABY_FINGER_THRESHOLD_MIN, babyFingerThresholdMin);
                PreferenceUtils.setPreference(getActivity(), INDEX_FINGER_THRESHOLD, indexFingerThreshold);
                PreferenceUtils.setPreference(getActivity(), MIDDLE_FINGER_THRESHOLD, middleFingerThreshold);
                PreferenceUtils.setPreference(getActivity(), RING_FINGER_THRESHOLD, ringFingerThreshold);
                PreferenceUtils.setPreference(getActivity(), BABY_FINGER_THRESHOLD, babyFingerThreshold);
                PreferenceUtils.setPreference(getActivity(), SAVE_IMAGE_WIDTH_4F, saveImgWidth);
                PreferenceUtils.setPreference(getActivity(), AGGRESSIVENESS_FACTOR, aggresivenessFactor);
                PreferenceUtils.setPreference(getActivity(), ZOOM_CAMERA, zoomCamera);
                PreferenceUtils.setPreference(getActivity(), RIDGE_WIDTH, ridgeWidth);
                PreferenceUtils.setPreference(getActivity(), FINGER_LENGTH_PERCENTAGE, fingLengthPer);
                PreferenceUtils.setPreference(getActivity(), SHARP_THRESHOLD, sharpThresh);
                PreferenceUtils.setPreference(getActivity(), FINGER_WIDTH_AV_COUNTER, fingWidthCounter);
               // PreferenceUtils.setPreference(getActivity(), TEXT_FONT_TYPE_FP, textFontType);
                //PreferenceUtils.setPreference(getActivity(), TEXT_FONT_STYLE_FP, textFontStyle);
                PreferenceUtils.setPreference(getActivity(), TEXT_LABEL_COLOR_FP, textLabelColor);
                PreferenceUtils.setPreference(getActivity(), TEXT_LABEL_ALPHA_FP, textLabelAlpha);
                PreferenceUtils.setPreference(getActivity(), FP_INSTRUCTION_BTN_COLOR, instructionContinueBtnColor);
                PreferenceUtils.setPreference(getActivity(), FP_INSTRUCTION_BTN_ALPHA, instructionContinueBtnColorAlpha);
                PreferenceUtils.setPreference(getActivity(), FP_INSTRUCTION_BTN_TXT_COLOR, instructionContinueBtnTxtColor);
                PreferenceUtils.setPreference(getActivity(), FP_INSTRUCTION_BTN_TXT_ALPHA, instructionContinueBtnTxtAlpha);
                PreferenceUtils.setPreference(getActivity(), SHOW_INSTRUCTION_SCREEN, showInstructionFPDetect.isChecked());
                PreferenceUtils.setPreference(getActivity(), INDEX_CAPTURE, indexCapture);
                PreferenceUtils.setPreference(getActivity(), MIDDLE_CAPTURE, middleCapture);
                PreferenceUtils.setPreference(getActivity(), RING_CAPTURE, ringCapture);
                PreferenceUtils.setPreference(getActivity(), BABY_CAPTURE, babyCapture);
                PreferenceUtils.setPreference(getActivity(), PROCESS_ONLY_INDEX_CAPTURE, processOnlyIndexCapture);
                PreferenceUtils.setPreference(getActivity(), PROCESS_ONLY_MIDDLE_CAPTURE, processOnlyMiddleCapture);
                PreferenceUtils.setPreference(getActivity(), PROCESS_ONLY_RING_CAPTURE, processOnlyRingCapture);
                PreferenceUtils.setPreference(getActivity(), PROCESS_ONLY_BABY_CAPTURE, processOnlyBabyCapture);
                PreferenceUtils.setPreference(getActivity(), PROCESS_CAPTURED_FRAME, processCaptureFrame);
                PreferenceUtils.setPreference(getActivity(), INITIAL_FRAME_HEIGHT, initialFrameHeight);
                PreferenceUtils.setPreference(getActivity(), HAND_TYPE_LEFT, captureLeftHand);
                PreferenceUtils.setPreference(getActivity(), MASK_UNWANTED_PART, maskUnwantedPart);

                showErrorMessage(view, getString(R.string.id_capture_save_msg));
                bottomSheetBehaviorCaptureId.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                glareThresholdEdtTxt.setText("" + ImageProcessingSDK.dGlare4f);
                fingerCheckFocusEdtTxt.setText("" + ImageProcessingSDK.dNumberOfFingerToCheck);
                focusEdtTxt.setText("" + ImageProcessingSDK.dFocus4f);
                indexFingerThresholdEdtTxt.setText("" + ImageProcessingSDK.dIndexFingerThreshold4F);
                middleFingerThresholdEdtTxt.setText("" + ImageProcessingSDK.dMiddleFingerThreshold4F);
                ringFingerThresholdEdtTxt.setText("" + ImageProcessingSDK.dRingFingerThreshold4F);
                babyFingerThresholdEdtTxt.setText("" + ImageProcessingSDK.dBabyFingerThreshold4F);
                indexFingerThresholdEdtTxtMin.setText("" + ImageProcessingSDK.dIndexFingerThreshold4FMin);
                middleFingerThresholdEdtTxtMin.setText("" + ImageProcessingSDK.dMiddleFingerThreshold4FMin);
                ringFingerThresholdEdtTxtMin.setText("" + ImageProcessingSDK.dRingFingerThreshold4FMin);
                babyFingerThresholdEdtTxtMin.setText("" + ImageProcessingSDK.dBabyFingerThreshold4FMin);
                saveImageWidthEdtTxt.setText("" + ImageProcessingSDK.dSaveImageWidth4F);
                aggresivenessFactorEdtTxt.setText("" + ImageProcessingSDK.dAggressivenessFactor4F);
                zoomEdtTxt.setText("" + ImageProcessingSDK.dZoom4F);
                farPercentage.setText("" + ImageProcessingSDK.dFarPercentage);
                tooFarPercentage.setText("" + ImageProcessingSDK.dTooFarPercenatge);
                ridgeWidthTxt.setText("" + ImageProcessingSDK.dRidgeWidth);
                fingerLengthPercentageEdtTxt.setText("" + ImageProcessingSDK.dFingerLengthPercentage);
                sharpThresholdEdtTxt.setText("" + ImageProcessingSDK.dSharpThreshold);
                fingerWidthAveCounterEdtTxt.setText("" + ImageProcessingSDK.dFingerWidthAverageCounter);
                textLabelColorEdtTxt.setText(""+ ImageProcessingSDK.textLabelColor);
                textLabelAlphaEdtTxt.setText(""+ ImageProcessingSDK.dAlpha);
                instructionContinueBtnColorEdtTxt.setText(""+ ImageProcessingSDK.instructionContinueButtonColor);
                instructionContinueBtnAlphaEdtTxt.setText(""+ ImageProcessingSDK.dAlpha);
                instructionContinueBtnTxtColorEdtTxt.setText(""+ ImageProcessingSDK.instructionContinueButtonTextColor);
                instructionContinueBtnTxtAlphaEdtTxt.setText(""+ ImageProcessingSDK.dAlpha);
                showInstructionFPDetect.setChecked(ImageProcessingSDK.dFaceShowInstructionScreen);
                maskUnwantedArea.setChecked(ImageProcessingSDK.dMaskUnwantedPart);

                spinnerTextFontType.setSelection(0);
                spinnerTextFontStyle.setSelection(0);

                if (ImageProcessingSDK.dCaptureLeftHand) {
                    radioGroupHandtype.check(radioGroupHandtype.getChildAt(0).getId());
                } else {
                    radioGroupHandtype.check(radioGroupHandtype.getChildAt(1).getId());
                }
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
                ImageProcessingSDK.getInstance().setImageProcessingResponseListener(FingerprintCapture4F.this);

//                if (spinnerFP.getSelectedItem().toString().equalsIgnoreCase("ALL")) {
//                    ImageProcessingSDK.getInstance().clearFingerprint(null);
//                    eVolvApp.getResultFPMap().clear();
//
//                    viewPagerFP.setVisibility(View.GONE);
//                    linearLayoutIndicator.setVisibility(View.GONE);
//                    defaultImage.setVisibility(View.VISIBLE);
//                    textViewDefault.setVisibility(View.VISIBLE);
//                    linearLayoutFPClear.setVisibility(View.GONE);
//                    buttonCapture.setText(R.string.capture);
//                } else {
//                    FingerType fingerType = getFPType(spinnerFP.getSelectedItem().toString());
//                    ImageProcessingSDK.getInstance().clearFingerprint(fingerType);
//                    eVolvApp.removeFPData(spinnerFP.getSelectedItem().toString());
//                }

                int selectedFingerprint = viewPagerFP.getCurrentItem();
                HashMap<String, String> mapFP = eVolvApp.getResultFPMap();
                String fingerName = (new ArrayList<String>(mapFP.keySet())).get(selectedFingerprint);

                FingerType fingerType = getFPType(fingerName);
                ImageProcessingSDK.getInstance().clearFingerprint(fingerType);
                eVolvApp.removeFPData(fingerName);

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
        String fingerFocusRatio = PreferenceUtils.getPreference(getActivity(), FINGER_FOCUS_RATIO, "" + ImageProcessingSDK.dFocus4f);
        String fingerGlareThreshold = PreferenceUtils.getPreference(getActivity(), FINGER_GLARE_THRESHOLD, "" + ImageProcessingSDK.dGlare4f);
        String fingerToCheckFocus = PreferenceUtils.getPreference(getActivity(), FINGER_TO_CHECK_FOCUS, "" + ImageProcessingSDK.dNumberOfFingerToCheck);
        boolean instructionScreen = PreferenceUtils.getPreference(getActivity(), SHOW_INSTRUCTION_SCREEN, ImageProcessingSDK.dFaceShowInstructionScreen);
        String textFontType =  PreferenceUtils.getPreference(getActivity(), TEXT_FONT_TYPE_FP, "DEFAULT");
        String textFontStyle = PreferenceUtils.getPreference(getActivity(), TEXT_FONT_STYLE_FP, "NORMAL");
        String textLabelColor = PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_COLOR_FP, ""+ ImageProcessingSDK.textLabelColor);
        String textLabelAlpha = PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_ALPHA_FP,""+ ImageProcessingSDK.dAlpha);
        String instrBtnColor = PreferenceUtils.getPreference(getActivity(), FP_INSTRUCTION_BTN_COLOR, ""+ ImageProcessingSDK.instructionContinueButtonColor);
        String instrBtnAlpha = PreferenceUtils.getPreference(getActivity(), FP_INSTRUCTION_BTN_ALPHA,""+ ImageProcessingSDK.dAlpha);
        String instrBtnTxtColor = PreferenceUtils.getPreference(getActivity(), FP_INSTRUCTION_BTN_TXT_COLOR, ""+ ImageProcessingSDK.instructionContinueButtonTextColor);
        String instrBtnTxtAlpha = PreferenceUtils.getPreference(getActivity(), FP_INSTRUCTION_BTN_TXT_ALPHA,""+ ImageProcessingSDK.dAlpha);
        boolean indexCapture = PreferenceUtils.getPreference(getActivity(), INDEX_CAPTURE, ImageProcessingSDK.dIndexCapture);
        boolean middleCapture = PreferenceUtils.getPreference(getActivity(), MIDDLE_CAPTURE, ImageProcessingSDK.dMiddleCapture);
        boolean ringCapture = PreferenceUtils.getPreference(getActivity(), RING_CAPTURE, ImageProcessingSDK.dRingCapture);
        boolean babyCapture = PreferenceUtils.getPreference(getActivity(), BABY_CAPTURE, ImageProcessingSDK.dBabyCapture);
        boolean processOnlyIndexCapture = PreferenceUtils.getPreference(getActivity(), PROCESS_ONLY_INDEX_CAPTURE, ImageProcessingSDK.dIndexKeep);
        boolean processOnlyMiddleCapture = PreferenceUtils.getPreference(getActivity(), PROCESS_ONLY_MIDDLE_CAPTURE, ImageProcessingSDK.dMiddleKeep);
        boolean processOnlyRingCapture = PreferenceUtils.getPreference(getActivity(), PROCESS_ONLY_RING_CAPTURE, ImageProcessingSDK.dRingKeep);
        boolean processOnlyBabyCapture = PreferenceUtils.getPreference(getActivity(), PROCESS_ONLY_BABY_CAPTURE, ImageProcessingSDK.dBabyKeep);

        boolean processCaptureFrame = PreferenceUtils.getPreference(getActivity(), PROCESS_CAPTURED_FRAME, ImageProcessingSDK.dProcessCapturedFrame);
        String initialFrameHeight = PreferenceUtils.getPreference(getActivity(), INITIAL_FRAME_HEIGHT, "" + ImageProcessingSDK.dInitialFrameHeight);
        boolean captureLeftHand = PreferenceUtils.getPreference(getActivity(), HAND_TYPE_LEFT, ImageProcessingSDK.dCaptureLeftHand);
        boolean maskUnwantedPart = PreferenceUtils.getPreference(getActivity(), MASK_UNWANTED_PART, ImageProcessingSDK.dMaskUnwantedPart);

        ImageProcessingSDK.getInstance().setImageProcessingResponseListener(FingerprintCapture4F.this);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showErrorMessage(getView(), getString(R.string.camera_permission));
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
            }
        } else {
            if (StringUtil.isEmpty(fingerToCheckFocus)) {
                showErrorMessage(getView(), getString(R.string.enter_finger_count));
                return;
            }
            if (StringUtil.isEmpty(fingerGlareThreshold)) {
                showErrorMessage(getView(), getString(R.string.enter_glare_percentage));
                return;
            }

            if (!StringUtil.isEmpty(fingerFocusRatio)) {
                int fourFingFocusedRatio = Integer.valueOf(focusEdtTxt.getText().toString());
                int fourFingGlarePerc = Integer.valueOf(glareThresholdEdtTxt.getText().toString());
                int numOfFingerToCheck = Integer.valueOf(fingerCheckFocusEdtTxt.getText().toString());

                int minindex = Integer.valueOf(indexFingerThresholdEdtTxtMin.getText().toString());
                int minmiddle = Integer.valueOf(middleFingerThresholdEdtTxtMin.getText().toString());
                int minring = Integer.valueOf(ringFingerThresholdEdtTxtMin.getText().toString());
                int minbaby = Integer.valueOf(babyFingerThresholdEdtTxtMin.getText().toString());
                int index = Integer.valueOf(indexFingerThresholdEdtTxt.getText().toString());
                int middle = Integer.valueOf(middleFingerThresholdEdtTxt.getText().toString());
                int ring = Integer.valueOf(ringFingerThresholdEdtTxt.getText().toString());
                int baby = Integer.valueOf(babyFingerThresholdEdtTxt.getText().toString());

                int save = Integer.valueOf(saveImageWidthEdtTxt.getText().toString());
                Float aggr = Float.valueOf(aggresivenessFactorEdtTxt.getText().toString());
                int zoom = Integer.valueOf(zoomEdtTxt.getText().toString());
                int far = Integer.valueOf(farPercentage.getText().toString());
                int toofar = Integer.valueOf(tooFarPercentage.getText().toString());
                int ridge = Integer.valueOf(ridgeWidthTxt.getText().toString());
                int fingerLength = Integer.valueOf(fingerLengthPercentageEdtTxt.getText().toString());
                int sharpThreshold = Integer.valueOf(sharpThresholdEdtTxt.getText().toString());
                int avFingerWidth = Integer.valueOf(fingerWidthAveCounterEdtTxt.getText().toString());
                int textLabelColorAlpha = (!StringUtil.isEmpty(textLabelAlpha) ? Integer.parseInt(textLabelAlpha) : ImageProcessingSDK.dAlpha);

                try {
                    JSONObject config = new JSONObject();
//                    config.put(UIConfigurationParameters.FOCUS_THRESHOLD_4F, "" + fourFingFocusedRatio);
//                    config.put(UIConfigurationParameters.GLARE_PERCENTAGE_4F, "" + fourFingGlarePerc);
//                    config.put(UIConfigurationParameters.NO_OF_FINGER_TO_VERIFY_4F, "" + numOfFingerToCheck);
                    config.put(UIConfigurationParameters.CFC_INDEX_FINGER_THRESHOLD, "" + index);
                    config.put(UIConfigurationParameters.CFC_MIDDLE_FINGER_THRESHOLD, "" + middle);
                    config.put(UIConfigurationParameters.CFC_RING_FINGER_THRESHOLD, "" + ring);
                    config.put(UIConfigurationParameters.CFC_BABY_FINGER_THRESHOLD, "" + baby);
                    config.put(UIConfigurationParameters.CFC_INDEX_FINGER_MIN_THRESHOLD, "" + minindex);
                    config.put(UIConfigurationParameters.CFC_MIDDLE_FINGER_MIN_THRESHOLD, "" + minmiddle);
                    config.put(UIConfigurationParameters.CFC_RING_FINGER_MIN_THRESHOLD, "" + minring);
                    config.put(UIConfigurationParameters.CFC_BABY_FINGER_MIN_THRESHOLD, "" + minbaby);
                    config.put(UIConfigurationParameters.CFC_IMAGE_WIDTH, "" + save);
                    config.put(UIConfigurationParameters.CFC_AGGRESSIVENESS_FACTOR, "" + aggr);
                    config.put(UIConfigurationParameters.CFC_ZOOM_CAMERA, "" + zoom);
                    config.put(UIConfigurationParameters.CFC_FAR_PERCENTAGE, "" + far);
                    config.put(UIConfigurationParameters.CFC_TOO_FAR_PERCENTAGE, "" + toofar);
                    config.put(UIConfigurationParameters.CFC_RIDGE_WIDTH, "" + ridge);
                    config.put(UIConfigurationParameters.CFC_FINGER_LENGTH_PERCENTAGE, "" + fingerLength);
                    config.put(UIConfigurationParameters.CFC_SHARP_THRESHOLD, "" + sharpThreshold);
                    config.put(UIConfigurationParameters.CFC_AVG_FINGER_WIDTH_COUNTER, "" + avFingerWidth);
                    config.put(UIConfigurationParameters.CFC_SHOW_INSTRUCTION_SCREEN, instructionScreen == true ? "Y" : "N");
                    config.put(UIConfigurationParameters.CFC_LABEL_TEXT_TYPEFACE_TYPE, textFontType);
                   // config.put(UIConfigurationParameters.TYPEFACE_STYLE, Arrays.asList(TEXT_FONT_STYLE_ARRAY).indexOf(textFontStyle)+"");
                    //config.put(UIConfigurationParameters.TYPEFACE_STYLE,textFontStyle+"");
                   // config.put(UIConfigurationParameters.CFC_LABEL_TEXT_COLOR, textLabelColor);
                    //config.put(UIConfigurationParameters.CFC_LABEL_TEXT_COLOR_ALPHA, textLabelColorAlpha+"");
                    //config.put(UIConfigurationParameters.CFC_INSTRUCTION_BUTTON_COLOR, instrBtnColor+"");
                    //config.put(UIConfigurationParameters.CFC_INSTRUCTION_BUTTON_ALPHA, instrBtnAlpha+"");
                    //config.put(UIConfigurationParameters.CFC_INSTRUCTION_BUTTON_TXT_COLOR, instrBtnTxtColor+"");
                    //config.put(UIConfigurationParameters.CFC_INSTRUCTION_BUTTON_TXT_ALPHA, instrBtnTxtAlpha+"");

                    config.put(UIConfigurationParameters.CFC_PROCESS_INDEX_FINGER, indexCapture ? "Y" : "N");
                    config.put(UIConfigurationParameters.CFC_PROCESS_MIDDLE_FINGER, middleCapture ? "Y" : "N");
                    config.put(UIConfigurationParameters.CFC_PROCESS_RING_FINGER, ringCapture ? "Y" : "N");
                    config.put(UIConfigurationParameters.CFC_PROCESS_BABY_FINGER, babyCapture ? "Y" : "N");
                    config.put(UIConfigurationParameters.CFC_CAPTURE_LEFT_HAND, captureLeftHand ? "Y" : "N");

                    config.put(UIConfigurationParameters.CFC_KEEP_INDEX_FINGER, processOnlyIndexCapture ? "Y" : "N");
                    config.put(UIConfigurationParameters.CFC_KEEP_MIDDLE_FINGER, processOnlyMiddleCapture ? "Y" : "N");
                    config.put(UIConfigurationParameters.CFC_KEEP_RING_FINGER, processOnlyRingCapture ? "Y" : "N");
                    config.put(UIConfigurationParameters.CFC_KEEP_BABY_FINGER, processOnlyBabyCapture ? "Y" : "N");

                    config.put(UIConfigurationParameters.CFC_PROCESS_CAPTURED_FINGER, processCaptureFrame ? "Y" : "N");
                    config.put(UIConfigurationParameters.CFC_INITIAL_FRAME_HEIGHT, initialFrameHeight);
                    config.put(UIConfigurationParameters.CFC_MASK_UNWANTED_PART, maskUnwantedPart ? "Y" : "N");

//                    ImageProcessingSDK.getInstance().captureFourFingerprint(getActivity(), fourFingFocusedRatio, fourFingGlarePerc, numOfFingerToCheck);
                    ImageProcessingSDK.getInstance().captureFourFingerprint(getActivity(), config);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showErrorMessage(getView(), getString(R.string.enter_focus_threshold));
            }
        }
    }

    public void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (eVolvApp.getResultFPMap().size() > 0) {
            viewPagerFP.setVisibility(View.VISIBLE);
            linearLayoutIndicator.setVisibility(View.VISIBLE);
            defaultImage.setVisibility(View.GONE);
            textViewDefault.setVisibility(View.GONE);
            buttonCapture.setText(R.string.re_capture);
//            linearLayoutFPClear.setVisibility(View.VISIBLE);
            buttonClearFP.setVisibility(View.VISIBLE);

            setUpView();
            setTab();
            // onCircleButtonClick();
        } else {
            viewPagerFP.setVisibility(View.GONE);
            linearLayoutIndicator.setVisibility(View.GONE);
            defaultImage.setVisibility(View.VISIBLE);
            textViewDefault.setVisibility(View.VISIBLE);
            linearLayoutFPClear.setVisibility(View.GONE);
            buttonClearFP.setVisibility(View.GONE);
            buttonCapture.setText(R.string.capture);
        }
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
        Log.d("SDK", "CALLBACK:::: onFourFingerCaptureFinished");
        if (null != response) {
            //showErrorMessage(getView(), response.getStatusMessage());
            if (response.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()) {
                viewPagerFP.setVisibility(View.VISIBLE);
                linearLayoutIndicator.setVisibility(View.VISIBLE);
                defaultImage.setVisibility(View.GONE);
                textViewDefault.setVisibility(View.GONE);
                buttonCapture.setText(R.string.re_capture);
//                linearLayoutFPClear.setVisibility(View.VISIBLE);
                buttonClearFP.setVisibility(View.VISIBLE);

//                eVolvApp.setResultFPMap((HashMap<String, String>) resultMap);
                eVolvApp.getResultFPMap().putAll((HashMap<String, String>) resultMap);
                isCapture = false;
            } else if (response.getStatusCode() == ResponseStatusCode.PERMISSION_NOT_GRANTED.getStatusCode()) {
                showErrorMessage(getView(), getString(R.string.permission_not_granted));
            } else if (response.getStatusCode() == ResponseStatusCode.DEVICE_NOT_SUPPORTED.getStatusCode()) {
                showErrorMessage(getView(), getString(R.string.device_not_supported_4f_feature_msg));
            } else if (!(response.getStatusCode() == ResponseStatusCode.OPERATION_CANCEL.getStatusCode())) {
                buttonCapture.setText(R.string.re_capture);
            }
        }
    }

    private void setUpView() {
        adapter = new PageAdapter(getChildFragmentManager(), eVolvApp.getResultFPMap().size());

        for (int i = 0; i < eVolvApp.getResultFPMap().size(); i++) {
            adapter.addFrag(new FingerprintView4F());
        }

        viewPagerFP.setAdapter(adapter);
        viewPagerFP.setCurrentItem(0);
        setupPagerIndicatorDots();
        ivArrayDotsPager[0].setImageResource(R.drawable.fill_circle);
        sendFingerPrintCaptureData(viewPagerFP.getCurrentItem());
    }

    private void setupPagerIndicatorDots() {
        ivArrayDotsPager = new ImageView[eVolvApp.getResultFPMap().size()];
        linearLayoutIndicator.removeAllViews();
        for (int i = 0; i < ivArrayDotsPager.length; i++) {
            ivArrayDotsPager[i] = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(5, 20, 5, 25);
            ivArrayDotsPager[i].setLayoutParams(params);
            ivArrayDotsPager[i].setImageResource(R.drawable.holo_circle);
            //ivArrayDotsPager[i].setAlpha(0.4f);
//            ivArrayDotsPager[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    view.setAlpha(1);
//                }
//            });
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
        FingerType[] fingerTypes = FingerType.values();
        FingerType ft = null;
        for (int i = 0; i < fingerTypes.length; i++) {
            if (fingerTypes[i].name().equals(fingerType)) {
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
        FingerprintView4F.handler.sendMessage(message);
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
