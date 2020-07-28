package com.idmission.libtestproject.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
import android.view.Gravity;
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

import com.idmission.client.ColorCode;
import com.idmission.client.IdType;
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

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CaptureFront extends Fragment implements ImageProcessingResponseListener {

   // private static final String[] TITLE_IMAGE = {"None", "Title Image 1"};
    //private static final String[] TEXT_FONT_TYPE_ARRAY = {"DEFAULT","DEFAULT_BOLD","SANS_SERIF","SERIF","MONOSPACE"};
    //private static final String[] TEXT_FONT_STYLE_ARRAY = {"NORMAL","BOLD","ITALIC","BOLD_ITALIC"};
    //private static final String[] ID_CAPTURE_TITLE_ALIGNMENT = {"Top", "Center", "Bottom"};
    //private static final String[] ID_CAPTURE_HINT_MSG_ALIGNMENT = {"Center", "Bottom", "Top"};
    //private static final String[] ID_CAPTURE_TITLE_IMG_MSG_ALIGNMENT = {"Bottom", "Top", "Center"};
    private static final String[] LABELS_KEY = {"align_document_img_capture", "subject_is_too_dark_img_capture",
            "out_of_focus_img_capture", "too_much_glare_img_capture", "light", "focus", "glare", "page_title_image_capture", "id_capture_instruction",
            "id_capture_preview_header", "id_capture_preview_message"};
    //private static final String[] ID_CAPTURE_BORDER = {"Thick", "Thin"};
    private BottomSheetBehavior bottomSheetBehaviorCaptureId;
    private ImageView bottomSheetImage, defaultImage, captureImage;
    private LinearLayout linearLayout, linearLayoutCapture;
    private boolean isExpand = false, hideIdCaptureTitle = false, hideIdCaptureHintMsg = false, hideIdCaptureTitleImg = false, isCapture = true;
    private TextView textViewDefault;
    private Button buttonCapture, buttonBack, buttonNext, buttonSave, buttonReset, buttonAddLabel;
    private EditText lightThresholdEdtTxt, maxFocusThresholdEdtTxt, minFocusThresholdEdtTxt, glarePercentageEdtTxt,
            enableCaptureButtonTimeEdtTxt, maxImageSizeEdtTxt, imageHeightEdtTxt, imageWidthEdtTxt, idOutlineColorEdtTxt, idOutlineAlphaEdtTxt,
            detectedIdOutlineColorEdtTxt, detectedIdOutlineAlphaEdtTxt, idOutsideOutlineColorEdtTxt, idOutsideOutlineAlphaEdtTxt, detectedOutsideOutlineColorEdtTxt, detectedOutsideOutlineAlphaEdtTxt,
            backButtonColorEdtTxt, retryButtonColorEdtTxt, confirmButtonColorEdtTxt, backButtonAlphaEdtTxt, retryButtonAlphaEdtTxt, confirmButtonAlphaEdtTxt, maxBarcodeScanEdtTxt,
            englishLabelEdtTxt, spanishLabelEdtTxt, textLabelColorEdtTxt, textLabelAlphaEdtTxt,
            instructionContinueBtnColorEdtTxt, instructionContinueBtnAlphaEdtTxt, instructionContinueBtnTxtColorEdtTxt, instructionContinueBtnTxtAlphaEdtTxt,
            retryButtonBorderColorEdtTxt, confirmButtonStyleEdtTxt, retryButtonBorderAlphaEdtTxt, confirmButtonStyleAlphaEdtTxt, headerTextLabelColorEdtTxt, headerTextLabelAlphaEdtTxt, headerTextLabelSizeEdtTxt, textLabelSizeEdtTxt,
            maxMRZRetryCountEdtTxt;
    private static final String IMAGE_LIGHT_THRESHOLD_FRONT = "IMAGE_LIGHT_THRESHOLD_FRONT", IMAGE_MAX_FOCUS_THRESHOLD_FRONT = "IMAGE_MAX_FOCUS_THRESHOLD_FRONT", IMAGE_MIN_FOCUS_THRESHOLD_FRONT = "IMAGE_MIN_FOCUS_THRESHOLD_FRONT",
            IMAGE_GLARE_PERCENTAGE_FRONT = "IMAGE_GLARE_PERCENTAGE_FRONT", CAPTURE_BUTTON_TIME_FRONT = "CAPTURE_BUTTON_TIME_FRONT", MAX_IMAGE_SIZE_FRONT = "MAX_IMAGE_SIZE_FRONT",
            IMAGE_HEIGHT_FRONT = "IMAGE_HEIGHT_FRONT", IMAGE_WIDTH_FRONT = "IMAGE_WIDTH_FRONT", ID_IMG_OUTLINE_COLOR_FRONT = "ID_IMG_OUTLINE_COLOR_FRONT", ID_IMG_OUTLINE_ALPHA_FRONT = "ID_IMG_OUTLINE_ALPHA_FRONT",
            DETECTED_ID_IMG_OUTLINE_COLOR_FRONT = "DETECTED_ID_IMG_OUTLINE_COLOR_FRONT", DETECTED_ID_IMG_OUTLINE_ALPHA_FRONT = "DETECTED_ID_IMG_OUTLINE_ALPHA_FRONT", ID_IMG_OUTSIDE_OUTLINE_COLOR_FRONT = "ID_IMG_OUTSIDE_OUTLINE_COLOR_FRONT", ID_IMG_OUTSIDE_OUTLINE_ALPHA_FRONT = "ID_IMG_OUTSIDE_OUTLINE_ALPHA_FRONT",
            DETECTED_ID_IMG_OUTSIDE_OUTLINE_FRONT = "DETECTED_ID_IMG_OUTSIDE_OUTLINE_FRONT", DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_FRONT = "DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_FRONT", SELECT_ID_TITLE_IMG_FRONT = "SELECT_ID_TITLE_IMG_FRONT", CAPTURE_PORTRAIT_MODE_FRONT = "CAPTURE_PORTRAIT_MODE_FRONT", SHOW_INSTRUCTION_FRONT = "SHOW_INSTRUCTION_FRONT",
            TEXT_FONT_TYPE_FRONT = "TEXT_FONT_TYPE_FRONT", TEXT_FONT_STYLE_FRONT = "TEXT_FONT_STYLE_FRONT", BACK_BUTTON_COLOR_FRONT = "BACK_BUTTON_COLOR_FRONT", BACK_BUTTON_ALPHA_FRONT = "BACK_BUTTON_ALPHA_FRONT", RETRY_BUTTON_COLOR_FRONT = "RETRY_BUTTON_COLOR_FRONT",
            RETRY_BUTTON_ALPHA_FRONT = "RETRY_BUTTON_ALPHA_FRONT",  RETRY_BUTTON_BORDER_COLOR_FRONT = "RETRY_BUTTON_BORDER_COLOR_FRONT",
            RETRY_BUTTON_BORDER_ALPHA_FRONT = "RETRY_BUTTON_BORDER_ALPHA_FRONT", CONFIRM_BUTTON_COLOR_FRONT = "CONFIRM_BUTTON_COLOR_FRONT", CONFIRM_BUTTON_ALPHA_FRONT = "CONFIRM_BUTTON_ALPHA_FRONT", CONFIRM_BUTTON_STYLE_FRONT = "CONFIRM_BUTTON_STYLE_FRONT", CONFIRM_BUTTON_STYLE_ALPHA_FRONT = "CONFIRM_BUTTON_STYLE_ALPHA_FRONT",TEXT_LABEL_COLOR_FRONT = "TEXT_LABEL_COLOR_FRONT", TEXT_LABEL_ALPHA_FRONT= "TEXT_LABEL_ALPHA_FRONT",
            INSTRUCTION_BTN_COLOR_FRONT = "INSTRUCTION_BTN_COLOR_FRONT", INSTRUCTION_BTN_ALPHA_FRONT = "INSTRUCTION_BTN_ALPHA_FRONT", INSTRUCTION_BTN_TXT_COLOR_FRONT = "INSTRUCTION_BTN_TXT_COLOR_FRONT", INSTRUCTION_BTN_TXT_ALPHA_FRONT = "INSTRUCTION_BTN_TXT_ALPHA_FRONT",
            FACE_TITLE_FRONT = "FACE_TITLE_FRONT", FACE_HINT_FRONT = "FACE_HINT_FRONT", FACE_ICON_FRONT = "FACE_ICON_FRONT", FACE_TITLE_IMAGE_FRONT = "FACE_TITLE_IMAGE_FRONT", ID_TITLE_FRONT = "ID_TITLE_FRONT", ID_HINT_FRONT = "ID_HINT_FRONT", ID_TITLE_IMAGE_FRONT = "ID_TITLE_IMAGE_FRONT",
            FACE_TITLE_HIDE_FRONT = "FACE_TITLE_HIDE_FRONT", FACE_HINT_HIDE_FRONT = "FACE_HINT_HIDE_FRONT", FACE_ICON_HIDE_FRONT = "FACE_ICON_HIDE_FRONT", FACE_TITLE_IMAGE_HIDE_FRONT = "FACE_TITLE_IMAGE_HIDE_FRONT", ID_TITLE_HIDE_FRONT = "ID_TITLE_HIDE_FRONT", ID_HINT_HIDE_FRONT = "ID_HINT_HIDE_FRONT", ID_TITLE_IMAGE_HIDE_FRONT = "ID_TITLE_IMAGE_HIDE_FRONT",
            HEADER_TEXT_LABEL_COLOR_FRONT = "HEADER_TEXT_LABEL_COLOR_FRONT", HEADER_TEXT_LABEL_ALPHA_FRONT = "HEADER_TEXT_LABEL_ALPHA_FRONT", HEADER_TEXT_FONT_TYPE_FRONT = "HEADER_TEXT_FONT_TYPE_FRONT", HEADER_TEXT_FONT_STYLE_FRONT = "HEADER_TEXT_FONT_STYLE_FRONT", HEADER_TEXT_LABEL_FONT_SIZE = "HEADER_TEXT_LABEL_FONT_SIZE_FRONT", TEXT_LABEL_FONT_SIZE = "TEXT_LABEL_FONT_SIZE_FRONT", ID_CAPTURE_BORDER_TYPE_FRONT = "ID_CAPTURE_BORDER_TYPE_FRONT", ID_FRONT_BARCODE_ENABLE = "ID_FRONT_BARCODE_ENABLE", ID_BARCODE_SCAN_MAX = "ID_BARCODE_SCAN_MAX",
            MRZ_SCAN_COUNT_MAX_FRONT = "MRZ_SCAN_COUNT_MAX_FRONT";

    private RadioGroup radioGroupOrientation, radioGroupInstruction;

    private Spinner spinnerTitle, spinnerTitleAlignment, spinnerHintAlignment, spinnerTitleImageAlignment, spinnerLabel, spinnerTextFontType, spinnerTextFontStyle, spinnerHeaderTextFontType, spinnerHeaderTextFontStyle,
            spinnerIDCaptureBorder;
    private CheckBox titleAlignmentCB, hintAlignmentCB, titleImageAlignmentCB, barcodeCB;
    private String idTitleAlignment, idHintAlignment, idTitleImageAlignment;
    private HashMap<String, String> englishLabelMap = new HashMap<>();
    private HashMap<String, String> spanishLabelMap = new HashMap<>();
    private EVolvApp eVolvApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.id_capture_layout, container, false);
        eVolvApp = (EVolvApp) (getActivity().getApplicationContext());

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
        spinnerTextFontType = (Spinner) view.findViewById(R.id.text_font_type_spinner);
        spinnerTextFontStyle = (Spinner) view.findViewById(R.id.text_font_style_spinner);
        spinnerTitleAlignment = (Spinner) view.findViewById(R.id.spinner_title_alignment);
        spinnerHintAlignment = (Spinner) view.findViewById(R.id.spinner_hint_alignment);
        spinnerTitleImageAlignment = (Spinner) view.findViewById(R.id.spinner_title_image_alignment);
        spinnerHeaderTextFontType= (Spinner) view.findViewById(R.id.text_header_font_type_spinner);
        spinnerHeaderTextFontStyle= (Spinner) view.findViewById(R.id.text_header_font_style_spinner);
        spinnerLabel = (Spinner) view.findViewById(R.id.spinner_label_type);
        spinnerIDCaptureBorder=(Spinner) view.findViewById(R.id.spinner_id_capture_border);
        buttonSave = (Button) view.findViewById(R.id.button_save);
        buttonReset = (Button) view.findViewById(R.id.button_reset);
        backButtonColorEdtTxt = (EditText)view.findViewById(R.id.back_button_color);
        retryButtonColorEdtTxt = (EditText)view.findViewById(R.id.retry_button_color);
        confirmButtonColorEdtTxt = (EditText)view.findViewById(R.id.confirm_button_color);
        retryButtonBorderColorEdtTxt = (EditText)view.findViewById(R.id.retry_button_border_color);
        confirmButtonStyleEdtTxt = (EditText)view.findViewById(R.id.confirm_button_style);
        backButtonAlphaEdtTxt = (EditText)view.findViewById(R.id.transparency_back_button_color);
        retryButtonAlphaEdtTxt = (EditText)view.findViewById(R.id.transparency_retry_button_color);
        confirmButtonAlphaEdtTxt = (EditText)view.findViewById(R.id.transparency_confirm_button_color);
        retryButtonBorderAlphaEdtTxt = (EditText)view.findViewById(R.id.transparency_retry_button_border_color);
        confirmButtonStyleAlphaEdtTxt = (EditText)view.findViewById(R.id.transparency_confirm_button_style);
        titleAlignmentCB = (CheckBox) view.findViewById(R.id.checkbox_title_alignment);
        hintAlignmentCB = (CheckBox) view.findViewById(R.id.checkbox_hint_alignment);
        titleImageAlignmentCB = (CheckBox) view.findViewById(R.id.checkbox_title_image_alignment);
        textLabelColorEdtTxt = (EditText) view.findViewById(R.id.text_label_color);
        textLabelAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_text_label_color);
        instructionContinueBtnColorEdtTxt = (EditText) view.findViewById(R.id.instruction_continue_button_color);
        instructionContinueBtnAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_instruction_continue_button);
        instructionContinueBtnTxtColorEdtTxt = (EditText) view.findViewById(R.id.instruction_continue_button_text_color);
        instructionContinueBtnTxtAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_instruction_continue_button_text);
        headerTextLabelColorEdtTxt = (EditText) view.findViewById(R.id.text_header_label_color);
        headerTextLabelAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_text_header_label_color);
        headerTextLabelSizeEdtTxt = (EditText) view.findViewById(R.id.Header_text_size);
        textLabelSizeEdtTxt = (EditText) view.findViewById(R.id.text_size);
        maxBarcodeScanEdtTxt = (EditText) view.findViewById(R.id.max_barcode_scan);
        barcodeCB = (CheckBox) view.findViewById(R.id.checkbox_barcode);
        maxMRZRetryCountEdtTxt = (EditText) view.findViewById(R.id.mrz_retry_count_edttext);

        spinnerLabel.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, LABELS_KEY));
        //spinnerTitle.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TITLE_IMAGE));
        //spinnerTextFontType.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TEXT_FONT_TYPE_ARRAY));
        //spinnerTextFontStyle.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TEXT_FONT_STYLE_ARRAY));
        //spinnerTitleAlignment.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, ID_CAPTURE_TITLE_ALIGNMENT));
        //spinnerHintAlignment.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, ID_CAPTURE_HINT_MSG_ALIGNMENT));
        //spinnerTitleImageAlignment.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, ID_CAPTURE_TITLE_IMG_MSG_ALIGNMENT));
        //spinnerHeaderTextFontType.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TEXT_FONT_TYPE_ARRAY));
        //spinnerHeaderTextFontStyle.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TEXT_FONT_STYLE_ARRAY));
        //spinnerIDCaptureBorder.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, ID_CAPTURE_BORDER));

        setSpinnerAdapter();
        textViewDefault.setText(getString(R.string.capture));

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

        ArrayList<Pair<String,String>> headerTextFontTypeList = new ArrayList<Pair<String,String>>();
        headerTextFontTypeList.add(new Pair<String, String>(getString(R.string.default_font_type),"DEFAULT"));
        headerTextFontTypeList.add(new Pair<String, String>(getString(R.string.default_bold),"DEFAULT_BOLD"));
        headerTextFontTypeList.add(new Pair<String, String>(getString(R.string.san_serif),"SANS_SERIF"));
        headerTextFontTypeList.add(new Pair<String, String>(getString(R.string.serif),"SERIF"));
        headerTextFontTypeList.add(new Pair<String, String>(getString(R.string.monospace),"MONOSPACE"));
        SpinnerAdapterForPair headerTextFontTypeListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                headerTextFontTypeList);
        spinnerHeaderTextFontType.setAdapter(headerTextFontTypeListAdapter);

        ArrayList<Pair<String,String>> headerTextFontStyleList = new ArrayList<Pair<String,String>>();
        headerTextFontStyleList.add(new Pair<String, String>(getString(R.string.normal),"NORMAL"));
        headerTextFontStyleList.add(new Pair<String, String>(getString(R.string.bold),"BOLD"));
        headerTextFontStyleList.add(new Pair<String, String>(getString(R.string.italic),"ITALIC"));
        headerTextFontStyleList.add(new Pair<String, String>(getString(R.string.bold_italic),"BOLD_ITALIC"));
        SpinnerAdapterForPair headerTextFontStyleListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                headerTextFontStyleList);
        spinnerHeaderTextFontStyle.setAdapter(headerTextFontStyleListAdapter);

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
        spinnerTitleImageAlignment.setAdapter(titleImageAlignmentListAdapter);

        ArrayList<Pair<String,String>> idCaptureBorderList = new ArrayList<Pair<String,String>>();
        idCaptureBorderList.add(new Pair<String, String>(getString(R.string.thin),"Thin"));
        idCaptureBorderList.add(new Pair<String, String>(getString(R.string.thick),"Thick"));
        SpinnerAdapterForPair idCaptureBorderListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                idCaptureBorderList);
        spinnerIDCaptureBorder.setAdapter(idCaptureBorderListAdapter);
    }

    private void setSavedValue() {
        lightThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_LIGHT_THRESHOLD_FRONT, "" + ImageProcessingSDK.dLightThreshold));
        maxFocusThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_MAX_FOCUS_THRESHOLD_FRONT, "" + ImageProcessingSDK.dMaxFocusThreshold));
        minFocusThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_MIN_FOCUS_THRESHOLD_FRONT, "" + ImageProcessingSDK.dMinFocusThreshold));
        glarePercentageEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_GLARE_PERCENTAGE_FRONT, "" + ImageProcessingSDK.dGlarePercentage));
        enableCaptureButtonTimeEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), CAPTURE_BUTTON_TIME_FRONT, "" + ImageProcessingSDK.dCaptureBtnEnableTime));
        maxImageSizeEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), MAX_IMAGE_SIZE_FRONT, "" + ImageProcessingSDK.dImageSize));
        imageHeightEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_HEIGHT_FRONT, "" + ImageProcessingSDK.dImageHeight));
        imageWidthEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), IMAGE_WIDTH_FRONT, "" + ImageProcessingSDK.dImgaeWidth));
        idOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_COLOR_FRONT, "" + ImageProcessingSDK.dIdOutlineColor));
        idOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_ALPHA_FRONT, "" + ImageProcessingSDK.dAlpha));
        detectedIdOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_COLOR_FRONT, "" + ImageProcessingSDK.dDetectedIdOutlineColor));
        detectedIdOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_ALPHA_FRONT, "" + ImageProcessingSDK.dAlpha));
        idOutsideOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_COLOR_FRONT, "" + ImageProcessingSDK.dOutsideIdOutlineColor));
        idOutsideOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_ALPHA_FRONT, "" + ImageProcessingSDK.dAlpha));
        detectedOutsideOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_FRONT, "" + ImageProcessingSDK.dDetectedOutsideIdOutlineColor));
        detectedOutsideOutlineAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_FRONT, "" + ImageProcessingSDK.dAlpha));
        backButtonColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), BACK_BUTTON_COLOR_FRONT, ""+ ImageProcessingSDK.backButtonColor));
        backButtonAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), BACK_BUTTON_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha));
        retryButtonColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_COLOR_FRONT, ""+ ImageProcessingSDK.retryButtonColor));
        retryButtonAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha));
        retryButtonBorderColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_BORDER_COLOR_FRONT, ""+ ImageProcessingSDK.retryButtonBorderColor));
        retryButtonBorderAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_BORDER_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha));
        confirmButtonColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_COLOR_FRONT, ""+ ImageProcessingSDK.confirmButtonColor));
        confirmButtonAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha));
        confirmButtonStyleEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_STYLE_FRONT, ""+ ImageProcessingSDK.confirmButtonStyle));
        confirmButtonStyleAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_STYLE_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha));
        textLabelColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_COLOR_FRONT, ""+ ImageProcessingSDK.textLabelColor));
        textLabelAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha));
        instructionContinueBtnColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), INSTRUCTION_BTN_COLOR_FRONT, ""+ ImageProcessingSDK.instructionContinueButtonColor));
        instructionContinueBtnAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), INSTRUCTION_BTN_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha));
        instructionContinueBtnTxtColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), INSTRUCTION_BTN_TXT_COLOR_FRONT, ""+ ImageProcessingSDK.instructionContinueButtonTextColor));
        instructionContinueBtnTxtAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), INSTRUCTION_BTN_TXT_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha));
        headerTextLabelColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_LABEL_COLOR_FRONT, ""+ ImageProcessingSDK.textLabelColor));
        headerTextLabelAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_LABEL_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha));
        headerTextLabelSizeEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_LABEL_FONT_SIZE, ""));
        textLabelSizeEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_FONT_SIZE, ""));
        maxBarcodeScanEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), ID_BARCODE_SCAN_MAX, "" + ImageProcessingSDK.maxBarcodeScan));
        maxMRZRetryCountEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), MRZ_SCAN_COUNT_MAX_FRONT, "" + ImageProcessingSDK.dIDCaptureMRZRetryCount));

        String showTitle = PreferenceUtils.getPreference(getActivity(), SELECT_ID_TITLE_IMG_FRONT, "None");
        if (showTitle.equalsIgnoreCase("None")) {
            spinnerTitle.setSelection(0);
        } else {
            spinnerTitle.setSelection(1);
        }

        String textFontType = PreferenceUtils.getPreference(getActivity(), TEXT_FONT_TYPE_FRONT, "DEFAULT");
        //spinnerTextFontType.setSelection(Arrays.asList(TEXT_FONT_TYPE_ARRAY).indexOf(textFontType));
        spinnerTextFontType.setSelection(((ArrayAdapter<String>) spinnerTextFontType.getAdapter()).getPosition(textFontType));

        String textFontStyle = PreferenceUtils.getPreference(getActivity(), TEXT_FONT_STYLE_FRONT, "NORMAL");
        //spinnerTextFontStyle.setSelection(Arrays.asList(TEXT_FONT_STYLE_ARRAY).indexOf(textFontStyle));
        spinnerTextFontStyle.setSelection(((ArrayAdapter<String>) spinnerTextFontStyle.getAdapter()).getPosition(textFontStyle));

        String showTitleAlignment = PreferenceUtils.getPreference(getActivity(), ID_TITLE_FRONT, "Top");
        spinnerTitleAlignment.setSelection(((ArrayAdapter<String>) spinnerTitleAlignment.getAdapter()).getPosition(showTitleAlignment));

        String showHintAlignment = PreferenceUtils.getPreference(getActivity(), ID_HINT_FRONT, "Center");
        spinnerHintAlignment.setSelection(((ArrayAdapter<String>) spinnerHintAlignment.getAdapter()).getPosition(showHintAlignment));

        String showTitleImageAlignemnt = PreferenceUtils.getPreference(getActivity(), ID_TITLE_IMAGE_FRONT, "Bottom");
        spinnerTitleImageAlignment.setSelection(((ArrayAdapter<String>) spinnerTitleImageAlignment.getAdapter()).getPosition(showTitleImageAlignemnt));

        String headerTextFontType = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_FONT_TYPE_FRONT, "DEFAULT");
       // spinnerHeaderTextFontType.setSelection(Arrays.asList(TEXT_FONT_TYPE_ARRAY).indexOf(headerTextFontType));
        spinnerHeaderTextFontType.setSelection(((ArrayAdapter<String>) spinnerHeaderTextFontType.getAdapter()).getPosition(headerTextFontType));

        String headerTextFontStyle = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_FONT_STYLE_FRONT, "NORMAL");
        //spinnerHeaderTextFontStyle.setSelection(Arrays.asList(TEXT_FONT_STYLE_ARRAY).indexOf(headerTextFontStyle));
        spinnerHeaderTextFontStyle.setSelection(((ArrayAdapter<String>) spinnerHeaderTextFontStyle.getAdapter()).getPosition(headerTextFontStyle));

        String idCaptureBorderType = PreferenceUtils.getPreference(getActivity(), ID_CAPTURE_BORDER_TYPE_FRONT, ""+ ImageProcessingSDK.dIdCaptureBorder);
        //spinnerIDCaptureBorder.setSelection(Arrays.asList(ID_CAPTURE_BORDER).indexOf(idCaptureBorderType));
        spinnerIDCaptureBorder.setSelection(((ArrayAdapter<String>) spinnerIDCaptureBorder.getAdapter()).getPosition(idCaptureBorderType));

        boolean idTitleHide = PreferenceUtils.getPreference(getActivity(), ID_TITLE_HIDE_FRONT, false);
        if (idTitleHide) {
            titleAlignmentCB.setChecked(true);
        }

        boolean idBarcodeEnable = PreferenceUtils.getPreference(getActivity(), ID_FRONT_BARCODE_ENABLE, false);
        if (idBarcodeEnable) {
            barcodeCB.setChecked(true);
        }

        boolean idHintHide = PreferenceUtils.getPreference(getActivity(), ID_HINT_HIDE_FRONT, false);
        if (idHintHide) {
            hintAlignmentCB.setChecked(true);
        }

        boolean idTitleImageHide = PreferenceUtils.getPreference(getActivity(), ID_TITLE_IMAGE_HIDE_FRONT, false);
        if (idTitleImageHide) {
            titleImageAlignmentCB.setChecked(true);
        }

        boolean captureMode = PreferenceUtils.getPreference(getActivity(), CAPTURE_PORTRAIT_MODE_FRONT, ImageProcessingSDK.dPortraitOrientation);
        if (captureMode) {
            radioGroupOrientation.check(radioGroupOrientation.getChildAt(0).getId());
        } else {
            radioGroupOrientation.check(radioGroupOrientation.getChildAt(1).getId());
        }

        boolean showInstruction = PreferenceUtils.getPreference(getActivity(), SHOW_INSTRUCTION_FRONT, ImageProcessingSDK.dShowInstruction);
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
        backButtonColorEdtTxt.setText(""+ ImageProcessingSDK.backButtonColor);
        backButtonAlphaEdtTxt.setText(""+ ImageProcessingSDK.dAlpha);
        retryButtonColorEdtTxt.setText(""+ ImageProcessingSDK.retryButtonColor);
        retryButtonAlphaEdtTxt.setText( ""+ ImageProcessingSDK.dAlpha);
        retryButtonBorderColorEdtTxt.setText(""+ ImageProcessingSDK.retryButtonBorderColor);
        retryButtonBorderAlphaEdtTxt.setText( ""+ ImageProcessingSDK.dAlpha);
        confirmButtonColorEdtTxt.setText( ""+ ImageProcessingSDK.confirmButtonColor);
        confirmButtonAlphaEdtTxt.setText(""+ ImageProcessingSDK.dAlpha);
        confirmButtonStyleEdtTxt.setText( ""+ ImageProcessingSDK.confirmButtonStyle);
        confirmButtonStyleAlphaEdtTxt.setText(""+ ImageProcessingSDK.dAlpha);
        textLabelColorEdtTxt.setText(""+ ImageProcessingSDK.textLabelColor);
        textLabelAlphaEdtTxt.setText(""+ ImageProcessingSDK.dAlpha);
        instructionContinueBtnColorEdtTxt.setText(""+ ImageProcessingSDK.instructionContinueButtonColor);
        instructionContinueBtnAlphaEdtTxt.setText(""+ ImageProcessingSDK.dAlpha);
        instructionContinueBtnTxtColorEdtTxt.setText(""+ ImageProcessingSDK.instructionContinueButtonTextColor);
        instructionContinueBtnTxtAlphaEdtTxt.setText(""+ ImageProcessingSDK.dAlpha);
        headerTextLabelColorEdtTxt.setText(""+ ImageProcessingSDK.textLabelColor);
        headerTextLabelAlphaEdtTxt.setText(""+ ImageProcessingSDK.dAlpha);
        headerTextLabelSizeEdtTxt.setText("");
        textLabelSizeEdtTxt.setText("");
        maxBarcodeScanEdtTxt.setText("" + ImageProcessingSDK.maxBarcodeScan);
        maxMRZRetryCountEdtTxt.setText("" + ImageProcessingSDK.dIDCaptureMRZRetryCount);

        englishLabelEdtTxt.setText("");
        spanishLabelEdtTxt.setText("");
        spinnerTitle.setSelection(0);
        spinnerTextFontType.setSelection(0);
        spinnerTextFontStyle.setSelection(0);
        spinnerTitleAlignment.setSelection(0);
        spinnerHintAlignment.setSelection(0);
        spinnerTitleImageAlignment.setSelection(0);
        spanishLabelEdtTxt.setSelection(0);
        spinnerHeaderTextFontStyle.setSelection(0);
        spinnerHeaderTextFontType.setSelection(0);
        spinnerIDCaptureBorder.setSelection(0);
        titleAlignmentCB.setChecked(false);
        hintAlignmentCB.setChecked(false);
        titleImageAlignmentCB.setChecked(false);
        barcodeCB.setChecked(false);

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
                    captureCameraAPICall();
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
               // String showTitle = spinnerTitle.getSelectedItem().toString();
                //String textFontType = spinnerTextFontType.getSelectedItem().toString();
                //String textFontStyle = spinnerTextFontStyle.getSelectedItem().toString();
                String showTitle = ((Pair)spinnerTitle.getSelectedItem()).second.toString();
              //  String textFontType = ((Pair)spinnerTextFontType.getSelectedItem()).second.toString();
               // String textFontStyle = ((Pair)spinnerTextFontStyle.getSelectedItem()).second.toString();
                String backButtonColor = backButtonColorEdtTxt.getText().toString().trim();
                String backButtonAlpha = backButtonAlphaEdtTxt.getText().toString().trim();
                String retryButtonColor = retryButtonColorEdtTxt.getText().toString().trim();
                String retryButtonAlpha = retryButtonAlphaEdtTxt.getText().toString().trim();
                String retryButtonBorderColor = retryButtonBorderColorEdtTxt.getText().toString().trim();
                String retryButtonBorderAlpha = retryButtonBorderAlphaEdtTxt.getText().toString().trim();
                String confirmButtonColor = confirmButtonColorEdtTxt.getText().toString().trim();
                String confirmButtonAlpha = confirmButtonAlphaEdtTxt.getText().toString().trim();
                String confirmButtonStyle = confirmButtonStyleEdtTxt.getText().toString().trim();
                String confirmButtonStyleAlpha = confirmButtonStyleAlphaEdtTxt.getText().toString().trim();
                String textLabelColor = textLabelColorEdtTxt.getText().toString().trim();
                String textLabelAlpha = textLabelAlphaEdtTxt.getText().toString().trim();
                String instructionContinueBtnColor = instructionContinueBtnColorEdtTxt.getText().toString().trim();
                String instructionContinueBtnColorAlpha = instructionContinueBtnAlphaEdtTxt.getText().toString().trim();
                String instructionContinueBtnTxtColor = instructionContinueBtnTxtColorEdtTxt.getText().toString().trim();
                String instructionContinueBtnTxtAlpha = instructionContinueBtnTxtAlphaEdtTxt.getText().toString().trim();
                String headerTextColorEdtTxt = headerTextLabelColorEdtTxt.getText().toString().trim();
                String headerTextAlphaEdtTxt = headerTextLabelAlphaEdtTxt.getText().toString().trim();
                String headerTextSizeEdtTxt = headerTextLabelSizeEdtTxt.getText().toString().trim();
                String textSizeEdtTxt = textLabelSizeEdtTxt.getText().toString().trim();
                String maxBarcodeScan = maxBarcodeScanEdtTxt.getText().toString().trim();
                boolean isBarcodeEnable = barcodeCB.isChecked();
                String mrzRetryCount = maxMRZRetryCountEdtTxt.getText().toString().trim();

                //String headerTextFontType = spinnerHeaderTextFontType.getSelectedItem().toString();
                //String headerTextFontStyle = spinnerHeaderTextFontStyle.getSelectedItem().toString();
                //String headerTextFontType = ((Pair)spinnerHeaderTextFontType.getSelectedItem()).second.toString();
                //String headerTextFontStyle = ((Pair)spinnerHeaderTextFontStyle.getSelectedItem()).second.toString();
                //String idCaptureBorderType = spinnerIDCaptureBorder.getSelectedItem().toString();
                //String idCaptureBorderType = ((Pair)spinnerIDCaptureBorder.getSelectedItem()).second.toString();

                boolean capturePortrait = true;
                if (radioGroupOrientation.getCheckedRadioButtonId() != R.id.radio_button_portrait) {
                    capturePortrait = false;
                }

                boolean showInstruction = false;
                if (radioGroupInstruction.getCheckedRadioButtonId() != R.id.radio_button_no) {
                    showInstruction = true;
                }

                PreferenceUtils.setPreference(getActivity(), IMAGE_LIGHT_THRESHOLD_FRONT, lightThreshold);
                PreferenceUtils.setPreference(getActivity(), IMAGE_MAX_FOCUS_THRESHOLD_FRONT, maxFocusThreshold);
                PreferenceUtils.setPreference(getActivity(), IMAGE_MIN_FOCUS_THRESHOLD_FRONT, minFocusThreshold);
                PreferenceUtils.setPreference(getActivity(), IMAGE_GLARE_PERCENTAGE_FRONT, glareThreshold);
                PreferenceUtils.setPreference(getActivity(), CAPTURE_BUTTON_TIME_FRONT, captureBtnTime);
                PreferenceUtils.setPreference(getActivity(), MAX_IMAGE_SIZE_FRONT, maxImagesize);
                PreferenceUtils.setPreference(getActivity(), IMAGE_HEIGHT_FRONT, imageHeight);
                PreferenceUtils.setPreference(getActivity(), IMAGE_WIDTH_FRONT, imageWidth);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTLINE_COLOR_FRONT, idOutlineColor);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTLINE_ALPHA_FRONT, "" + idOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_COLOR_FRONT, detectedIdOutlineColor);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_ALPHA_FRONT, "" + detectedIdOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_COLOR_FRONT, idOutsideOutlineColor);
                PreferenceUtils.setPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_ALPHA_FRONT, "" + idOutsideOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_FRONT, "" + detectedIdOutsideOutlineColor);
                PreferenceUtils.setPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_FRONT, "" + detectedIdOutsideOutlineAlpha);
                PreferenceUtils.setPreference(getActivity(), SELECT_ID_TITLE_IMG_FRONT, showTitle);
               // PreferenceUtils.setPreference(getActivity(), TEXT_FONT_TYPE_FRONT, textFontType);
                //PreferenceUtils.setPreference(getActivity(), TEXT_FONT_STYLE_FRONT, textFontStyle);
                PreferenceUtils.setPreference(getActivity(), CAPTURE_PORTRAIT_MODE_FRONT, capturePortrait);
                PreferenceUtils.setPreference(getActivity(), SHOW_INSTRUCTION_FRONT, showInstruction);
                PreferenceUtils.setPreference(getActivity(), BACK_BUTTON_COLOR_FRONT, backButtonColor);
                PreferenceUtils.setPreference(getActivity(), BACK_BUTTON_ALPHA_FRONT, backButtonAlpha);
                PreferenceUtils.setPreference(getActivity(), RETRY_BUTTON_COLOR_FRONT, retryButtonColor);
                PreferenceUtils.setPreference(getActivity(), RETRY_BUTTON_ALPHA_FRONT, retryButtonAlpha);
                PreferenceUtils.setPreference(getActivity(), RETRY_BUTTON_BORDER_COLOR_FRONT, retryButtonBorderColor);
                PreferenceUtils.setPreference(getActivity(), RETRY_BUTTON_BORDER_ALPHA_FRONT, retryButtonBorderAlpha);
                PreferenceUtils.setPreference(getActivity(), CONFIRM_BUTTON_COLOR_FRONT, confirmButtonColor);
                PreferenceUtils.setPreference(getActivity(), CONFIRM_BUTTON_ALPHA_FRONT, confirmButtonAlpha);
                PreferenceUtils.setPreference(getActivity(), CONFIRM_BUTTON_STYLE_FRONT, confirmButtonStyle);
                PreferenceUtils.setPreference(getActivity(), CONFIRM_BUTTON_STYLE_ALPHA_FRONT, confirmButtonStyleAlpha);
                PreferenceUtils.setPreference(getActivity(), TEXT_LABEL_COLOR_FRONT, textLabelColor);
                PreferenceUtils.setPreference(getActivity(), TEXT_LABEL_ALPHA_FRONT, textLabelAlpha);
                PreferenceUtils.setPreference(getActivity(), INSTRUCTION_BTN_COLOR_FRONT, instructionContinueBtnColor);
                PreferenceUtils.setPreference(getActivity(), INSTRUCTION_BTN_ALPHA_FRONT, instructionContinueBtnColorAlpha);
                PreferenceUtils.setPreference(getActivity(), INSTRUCTION_BTN_TXT_COLOR_FRONT, instructionContinueBtnTxtColor);
                PreferenceUtils.setPreference(getActivity(), INSTRUCTION_BTN_TXT_ALPHA_FRONT, instructionContinueBtnTxtAlpha);
                PreferenceUtils.setPreference(getActivity(), HEADER_TEXT_LABEL_COLOR_FRONT, headerTextColorEdtTxt);
                PreferenceUtils.setPreference(getActivity(), HEADER_TEXT_LABEL_ALPHA_FRONT, headerTextAlphaEdtTxt);
                PreferenceUtils.setPreference(getActivity(), HEADER_TEXT_LABEL_FONT_SIZE, headerTextSizeEdtTxt);
                PreferenceUtils.setPreference(getActivity(), TEXT_LABEL_FONT_SIZE, textSizeEdtTxt);
                PreferenceUtils.setPreference(getActivity(), ID_FRONT_BARCODE_ENABLE, isBarcodeEnable);
                PreferenceUtils.setPreference(getActivity(), ID_BARCODE_SCAN_MAX, maxBarcodeScan);
                PreferenceUtils.setPreference(getActivity(), MRZ_SCAN_COUNT_MAX_FRONT, mrzRetryCount);
                // PreferenceUtils.setPreference(getActivity(), HEADER_TEXT_FONT_TYPE_FRONT, headerTextFontType);
               // PreferenceUtils.setPreference(getActivity(), HEADER_TEXT_FONT_STYLE_FRONT, headerTextFontStyle);
               // PreferenceUtils.setPreference(getActivity(), ID_CAPTURE_BORDER_TYPE_FRONT, idCaptureBorderType);

                //API call for custom UI
               // idTitleAlignment = ((Pair)spinnerTitleAlignment.getSelectedItem()).second.toString();
               // idHintAlignment = ((Pair)spinnerHintAlignment.getSelectedItem()).second.toString();
                //idTitleImageAlignment = ((Pair)spinnerTitleImageAlignment.getSelectedItem()).second.toString();
                String faceTitle = PreferenceUtils.getPreference(getActivity(), FACE_TITLE_FRONT, "Bottom");
                String faceHint = PreferenceUtils.getPreference(getActivity(), FACE_HINT_FRONT, "Top");
                String faceHintIcon = PreferenceUtils.getPreference(getActivity(), FACE_ICON_FRONT, "Top");
                String faceTitleImage = PreferenceUtils.getPreference(getActivity(), FACE_TITLE_IMAGE_FRONT, "Top");
                boolean faceTitleHide = PreferenceUtils.getPreference(getActivity(), FACE_TITLE_HIDE_FRONT, false);
                boolean faceHintHide = PreferenceUtils.getPreference(getActivity(), FACE_HINT_HIDE_FRONT, false);
                boolean faceHintIconHide = PreferenceUtils.getPreference(getActivity(), FACE_ICON_HIDE_FRONT, false);
                boolean faceTitleImageHide = PreferenceUtils.getPreference(getActivity(), FACE_TITLE_IMAGE_HIDE_FRONT, false);

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

//                ImageProcessingSDK.getInstance().imageProcessingSDK.customizeUserInterface(faceTitleOnTop, faceTitleHide,
//                        faceHintOnTop, faceHintHide,
//                        faceHintIconOnTop, faceHintIconHide,
//                        faceTitleImageAlignment, faceTitleImageHide,
//                        idTitleAlignment, hideIdCaptureTitle,
//                        idHintAlignment, hideIdCaptureHintMsg,
//                        idTitleImageAlignment, hideIdCaptureTitleImg, ImageProcessingSDK.getInstance());
//
//                //API call for add label
//                ImageProcessingSDK.getInstance().initializeLabels(englishLabelMap, spanishLabelMap);

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
                captureCameraAPICall();
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

    private void captureCameraAPICall() {
        ImageProcessingSDK.getInstance().setImageProcessingResponseListener(CaptureFront.this);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showErrorMessage(getView(), getString(R.string.camera_permission));
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
            }
        } else {
            String lightThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_LIGHT_THRESHOLD_FRONT, "" + ImageProcessingSDK.dLightThreshold);
            String minFocusThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_MIN_FOCUS_THRESHOLD_FRONT, "" + ImageProcessingSDK.dMinFocusThreshold);
            String maxFocusThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_MAX_FOCUS_THRESHOLD_FRONT, "" + ImageProcessingSDK.dMaxFocusThreshold);
            String glareThreshold = PreferenceUtils.getPreference(getActivity(), IMAGE_GLARE_PERCENTAGE_FRONT, "" + ImageProcessingSDK.dGlarePercentage);
            String captureBtnTime = PreferenceUtils.getPreference(getActivity(), CAPTURE_BUTTON_TIME_FRONT, "" + ImageProcessingSDK.dCaptureBtnEnableTime);
            String maxImagesize = PreferenceUtils.getPreference(getActivity(), MAX_IMAGE_SIZE_FRONT, "" + ImageProcessingSDK.dImageSize);
            String imageHeight = PreferenceUtils.getPreference(getActivity(), IMAGE_HEIGHT_FRONT, "" + ImageProcessingSDK.dImageHeight);
            String imageWidth = PreferenceUtils.getPreference(getActivity(), IMAGE_WIDTH_FRONT, "" + ImageProcessingSDK.dImgaeWidth);
            String idImgOutlineColor = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_COLOR_FRONT, "" + ImageProcessingSDK.dIdOutlineColor);
            String idImgOutlineAlpha = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTLINE_ALPHA_FRONT, "" + ImageProcessingSDK.dAlpha);
            String detectedIdImgOutlineColor = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_COLOR_FRONT, "" + ImageProcessingSDK.dDetectedIdOutlineColor);
            String detectedIdImgOutlineAlpha = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTLINE_ALPHA_FRONT, "" + ImageProcessingSDK.dAlpha);
            String idImgOutsideOutlineColor = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_COLOR_FRONT, "" + ImageProcessingSDK.dOutsideIdOutlineColor);
            String idImgOutsideOutlineAlpha = PreferenceUtils.getPreference(getActivity(), ID_IMG_OUTSIDE_OUTLINE_ALPHA_FRONT, "" + ImageProcessingSDK.dAlpha);
            String detectedIdImgOutsideOutlineColor = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_FRONT, "" + ImageProcessingSDK.dDetectedOutsideIdOutlineColor);
            String detectedIdImgOutsideOutlineAlpha = PreferenceUtils.getPreference(getActivity(), DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA_FRONT, "" + ImageProcessingSDK.dAlpha);
            String titleImageType = PreferenceUtils.getPreference(getActivity(), SELECT_ID_TITLE_IMG_FRONT, "None");
            boolean capturePortraitMode = PreferenceUtils.getPreference(getActivity(), CAPTURE_PORTRAIT_MODE_FRONT, ImageProcessingSDK.dPortraitOrientation);
            boolean showInstruction = PreferenceUtils.getPreference(getActivity(), SHOW_INSTRUCTION_FRONT, ImageProcessingSDK.dShowInstruction);
            String textFontType =  PreferenceUtils.getPreference(getActivity(), TEXT_FONT_TYPE_FRONT, ImageProcessingSDK.typeFaceType);
            String textFontStyle = PreferenceUtils.getPreference(getActivity(), TEXT_FONT_STYLE_FRONT, ImageProcessingSDK.typeFaceStyle);
            String backButtonColor = PreferenceUtils.getPreference(getActivity(), BACK_BUTTON_COLOR_FRONT, ""+ ImageProcessingSDK.backButtonColor);
            String backButtonAlpha = PreferenceUtils.getPreference(getActivity(), BACK_BUTTON_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha);
            String retryButtonColor = PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_COLOR_FRONT, ""+ ImageProcessingSDK.retryButtonColor);
            String retryButtonAlpha = PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha);
            String retryButtonBorderColor = PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_BORDER_COLOR_FRONT, ""+ ImageProcessingSDK.retryButtonBorderColor);
            String retryButtonBorderAlpha = PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_BORDER_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha);
            String confirmButtonColor = PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_COLOR_FRONT, ""+ ImageProcessingSDK.confirmButtonColor);
            String confirmButtonAlpha = PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha);
            String confirmButtonStyle = PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_STYLE_FRONT, ""+ ImageProcessingSDK.confirmButtonStyle);
            String confirmButtonStyleAlpha = PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_STYLE_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha);
            String textLabelColor = PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_COLOR_FRONT, ""+ ImageProcessingSDK.textLabelColor);
            String textLabelAlpha = PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_ALPHA_FRONT,""+ ImageProcessingSDK.dAlpha);
            String instrBtnColor = PreferenceUtils.getPreference(getActivity(), INSTRUCTION_BTN_COLOR_FRONT, ""+ ImageProcessingSDK.instructionContinueButtonColor);
            String instrBtnAlpha = PreferenceUtils.getPreference(getActivity(), INSTRUCTION_BTN_ALPHA_FRONT,""+ ImageProcessingSDK.dAlpha);
            String instrBtnTxtColor = PreferenceUtils.getPreference(getActivity(), INSTRUCTION_BTN_TXT_COLOR_FRONT, ""+ ImageProcessingSDK.instructionContinueButtonTextColor);
            String instrBtnTxtAlpha = PreferenceUtils.getPreference(getActivity(), INSTRUCTION_BTN_TXT_ALPHA_FRONT,""+ ImageProcessingSDK.dAlpha);
            String headerTextColorEdtTxt = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_LABEL_COLOR_FRONT, ""+ ImageProcessingSDK.textLabelColor);
            String headerTextAlphaEdtTxt = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_LABEL_ALPHA_FRONT, ""+ ImageProcessingSDK.dAlpha);
            String headerTextSizeEdtTxt = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_LABEL_FONT_SIZE, "");
            String textSizeEdtTxt = PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_FONT_SIZE, "");
            String headerTextFontType = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_FONT_TYPE_FRONT, ""+ ImageProcessingSDK.typeFaceType);
            String headerTextFontStyle = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_FONT_STYLE_FRONT, ""+ ImageProcessingSDK.typeFaceStyle);
            String idCaptureBorderType = PreferenceUtils.getPreference(getActivity(), ID_CAPTURE_BORDER_TYPE_FRONT, ""+ ImageProcessingSDK.dIdCaptureBorder);
            boolean isBarcodeEnable = PreferenceUtils.getPreference(getActivity(), ID_FRONT_BARCODE_ENABLE, false);
            String maxBarcodeScan = PreferenceUtils.getPreference(getActivity(), ID_BARCODE_SCAN_MAX, "" + ImageProcessingSDK.maxBarcodeScan);
            String mrzRetryCount = PreferenceUtils.getPreference(getActivity(), MRZ_SCAN_COUNT_MAX_FRONT, "" + ImageProcessingSDK.dMRZRetryCount);

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
            int backButtonColorAlpha = (!StringUtil.isEmpty(backButtonAlpha) ? Integer.parseInt(backButtonAlpha) : ImageProcessingSDK.dAlpha);
            int textLabelColorAlpha = (!StringUtil.isEmpty(textLabelAlpha) ? Integer.parseInt(textLabelAlpha) : ImageProcessingSDK.dAlpha);

            String detectedIdImageOutsideOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineColor) ? detectedIdImgOutsideOutlineColor : ImageProcessingSDK.dDetectedOutsideIdOutlineColor);
            int detectedIdImageOutsideOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineAlpha) ? Integer.parseInt(detectedIdImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

//                    ImageProcessingSDK.getInstance().autoCapture(getActivity(), ImageType.FRONT, capturePortraitMode, minimumLightThreshold, minFocusScoreThreshold,
//                            focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
//                            highResolutionImageHeight, highResolutionImageWidth, null,
//                            new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor, detectedIdImageOutsideOutlineAlpha), titleBitmap,
//                            showInstruction, 0);

            try {

                ColorCode idOutlineColorCode = new ColorCode(idImageOutlineColor, idImageOutlineAlpha);
                ColorCode detectedIDOutlineColorCode = new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha);
                ColorCode idOutsideOutlineColorCode = new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha);
                ColorCode detectedIdOutsideOutlineColorCode = new ColorCode(detectedIdImageOutsideOutlineColor, detectedIdImageOutsideOutlineAlpha);

                JSONObject commonFunctioObject = new JSONObject();
                commonFunctioObject.put(UIConfigConstants.ID_CAPTURE_PORTRAIT, capturePortraitMode == true ? "Y" : "N");
                commonFunctioObject.put(UIConfigConstants.ID_LIGHT_THRESHOLD, minimumLightThreshold+"");
                commonFunctioObject.put(UIConfigConstants.ID_MIN_FOCUS_THRESHOLD, minFocusScoreThreshold+"");
                commonFunctioObject.put(UIConfigConstants.ID_MAX_FOCUS_THRESHOLD, focusScoreThreshold+"");
                commonFunctioObject.put(UIConfigConstants.ID_GLARE_PERCENTAGE, glarePercentageThreshold+"");
                commonFunctioObject.put(UIConfigConstants.ID_ENABLE_CAPTURE_BUTTON_TIME, enableCaptureButtonTime+"");
                commonFunctioObject.put(UIConfigConstants.ID_MAX_IMAGE_SIZE, maxImageSize+"");
                commonFunctioObject.put(UIConfigConstants.ID_IMAGE_HEIGHT, highResolutionImageHeight+"");
                commonFunctioObject.put(UIConfigConstants.ID_IMAGE_WIDTH, highResolutionImageWidth+"");
                commonFunctioObject.put(UIConfigConstants.BARCODE_SCAN_MAX_LIMIT, maxBarcodeScan);
               // if (isBarcodeEnable) {
                    commonFunctioObject.put(UIConfigConstants.ID_COUNTRY, eVolvApp.getCountry());
                    commonFunctioObject.put(UIConfigConstants.ID_STATE, eVolvApp.getState());
                   // if (!eVolvApp.getSelectIdType().toString().isEmpty()) {
                   if (eVolvApp.getSelectIdType()!=null) {
                    commonFunctioObject.put(UIConfigConstants.ID_TYPE, IdType.valueOf(eVolvApp.getSelectIdType().toString()).getIdType());
                    }
                commonFunctioObject.put(UIConfigConstants.ID_MRZ_RETRY_COUNT, mrzRetryCount);

                // }
//                commonFunctioObject.put(UIConfigurationParameters.ID_OUTLINE_COLOR, idOutlineColorCode.getHexColorCode()+"");
//                commonFunctioObject.put(UIConfigurationParameters.ID_OUTLINE_COLOR_ALPHA, idOutlineColorCode.getTransparency()+"");
//                commonFunctioObject.put(UIConfigurationParameters.DETECTED_ID_OUTLINE_COLOR, detectedIDOutlineColorCode.getHexColorCode()+"");
//                commonFunctioObject.put(UIConfigurationParameters.DETECTED_ID_OUTLINE_COLOR_ALPHA, detectedIDOutlineColorCode.getTransparency()+"");
//                commonFunctioObject.put(UIConfigurationParameters.ID_OUTSIDE_OUTLINE_COLOR, idOutsideOutlineColorCode.getHexColorCode()+"");
//                commonFunctioObject.put(UIConfigurationParameters.ID_OUTSIDE_OUTLINE_COLOR_APLHA, idOutsideOutlineColorCode.getTransparency()+"");
//                commonFunctioObject.put(UIConfigurationParameters.DETECTED_ID_OUTSIDE_OUTLINE_COLOR, detectedIdOutsideOutlineColorCode.getHexColorCode()+"");
//                commonFunctioObject.put(UIConfigurationParameters.DETECTED_ID_OUTSIDE_OUTLINE_COLOR_ALPHA, detectedIdOutsideOutlineColorCode.getTransparency()+"");
                //commonFunctioObject.put(UIConfigConstants.ID_SHOW_INSTRUCTION, showInstruction == true ? "Y" : "N");
                //commonFunctioObject.put(UIConfigurationParameters.CAPTURE_ENABLE,captureEnable == true ? "Y" : "N");
//            commonFunctioObject.put(UIConfigurationParameters.TYPEFACE_TYPE,Typeface.DEFAULT_BOLD.toString());
//                commonFunctioObject.put(UIConfigurationParameters.TYPEFACE_TYPE,textFontType);
//                commonFunctioObject.put(UIConfigurationParameters.TYPEFACE_STYLE,textFontStyle);
//                // commonFunctioObject.put(UIConfigurationParameters.FIELD_NAME,fieldName);
//                commonFunctioObject.put(UIConfigurationParameters.BACK_BUTTON_COLOR,backButtonColor);//
//                commonFunctioObject.put(UIConfigurationParameters.BACK_BUTTON_COLOR_ALPHA, backButtonColorAlpha+"");
//                commonFunctioObject.put(UIConfigurationParameters.RETRY_BUTTON_COLOR,retryButtonColor);
//                commonFunctioObject.put(UIConfigurationParameters.RETRY_BUTTON_COLOR_ALPHA,retryButtonAlpha);
//                commonFunctioObject.put(UIConfigurationParameters.RETRY_BUTTON_BORDER_COLOR,retryButtonBorderColor);
//                commonFunctioObject.put(UIConfigurationParameters.RETRY_BUTTON_BORDER_COLOR_ALPHA,retryButtonBorderAlpha);
//                commonFunctioObject.put(UIConfigurationParameters.CONFIRM_BUTTON_COLOR,confirmButtonColor);
//                commonFunctioObject.put(UIConfigurationParameters.CONFIRM_BUTTON_COLOR_ALPHA,confirmButtonAlpha);
//                commonFunctioObject.put(UIConfigurationParameters.CONFIRM_BUTTON_STYLE,confirmButtonStyle);
//                commonFunctioObject.put(UIConfigurationParameters.CONFIRM_BUTTON_STYLE_ALPHA,confirmButtonStyleAlpha);
//                commonFunctioObject.put(UIConfigurationParameters.TEXT_LABEL_COLOR, textLabelColor);
//                commonFunctioObject.put(UIConfigurationParameters.TEXT_LABEL_ALPHA, textLabelColorAlpha+"");
//                commonFunctioObject.put(UIConfigurationParameters.INSTRUCTIN_BUTTON_COLOR, instrBtnColor+"");
//                commonFunctioObject.put(UIConfigurationParameters.INSTRUCTIN_BUTTON_ALPHA, instrBtnAlpha+"");
//                commonFunctioObject.put(UIConfigurationParameters.INSTRUCTIN_BUTTON_TXT_COLOR, instrBtnTxtColor+"");
//                commonFunctioObject.put(UIConfigurationParameters.INSTRUCTIN_BUTTON_TXT_ALPHA, instrBtnTxtAlpha+"");
//                commonFunctioObject.put(UIConfigurationParameters.HEADER_TEXT_LABEL_COLOR, headerTextColorEdtTxt);
//                commonFunctioObject.put(UIConfigurationParameters.HEADER_TEXT_LABEL_ALPHA, headerTextAlphaEdtTxt+"");
//                commonFunctioObject.put(UIConfigurationParameters.HEADER_TYPEFACE_TYPE, headerTextFontType);
//                commonFunctioObject.put(UIConfigurationParameters.HEADER_TYPEFACE_STYLE, headerTextFontStyle);
//                commonFunctioObject.put(UIConfigurationParameters.HEADER_TEXT_LABEL_SIZE, headerTextSizeEdtTxt);
//                commonFunctioObject.put(UIConfigurationParameters.TEXT_LABEL_SIZE, textSizeEdtTxt);
//                commonFunctioObject.put(UIConfigurationParameters.ID_CAPTURE_BORDER, idCaptureBorderType);

                //Type =  DEFAULT_BOLD,SANS_SERIF,SERIF,MONOSPACE;
                //Style = NORMAL = 0; BOLD = 1; ITALIC = 2;BOLD_ITALIC = 3;

//                ImageProcessingSDK.getInstance().autoCapture(getActivity(), ImageType.FRONT, null, titleBitmap, 0,  null, commonFunctioObject);

                ImageProcessingSDK.getInstance().autoCapture(getActivity(), ImageType.FRONT, commonFunctioObject, null, null);
            } catch (JSONException exception) {
                exception.printStackTrace();
            }

       }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bitmap bitmap = BitmapUtils.getBitmapFromFile(eVolvApp.getBaseDirectoryPath() + Constants.FRONT_IMAGE);
        setImage(bitmap);
        setSavedValue();
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
        Log.d("SDK", "CALLBACK:::: onAutoImageCaptureResultAvailable");
        if (null != response) {
            if (response.getStatusCode() == ResponseStatusCode.PERMISSION_NOT_GRANTED.getStatusCode()) {
                showErrorMessage(getView(), getString(R.string.permission_not_granted));
            }else {
                String imageBase64 = null;
                HashMap<String, String> mapBarcodeData = new HashMap<>();
                if (null != resultMap) {
                    if (resultMap.containsKey(ImageType.FRONT.toString())) {
                        imageBase64 = resultMap.get(ImageType.FRONT.toString());
                        if (!StringUtil.isEmpty(imageBase64)) {
                            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            BitmapUtils.writeBitmapToFilepath(eVolvApp.getBaseDirectoryPath() + Constants.FRONT_IMAGE, bitmap);
                            setImage(bitmap); //Set this image as first image
                            isCapture = false;
                        }
                    }

                    if (resultMap.containsKey("barcode_data")) {
                        String jsonData = resultMap.get("barcode_data");
                        if (!StringUtil.isEmpty(jsonData)) {
                            try {
                                JSONObject jsonObject = new JSONObject(jsonData);
                                JSONObject jsonIdData = jsonObject.getJSONObject("IDData");

                                Iterator<String> barcodeFields = jsonIdData.keys();
                                while (barcodeFields.hasNext()) {
                                    String key = barcodeFields.next();
                                    String value = jsonIdData.optString(key);
                                    value = StringEscapeUtils.escapeXml11(value);

                                   // mapBarcodeData.put(key, value.trim());
                                    mapBarcodeData.put(key, StringEscapeUtils.unescapeXml(value).trim());
                                }
                                showBarcodeData(mapBarcodeData, 0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (resultMap.containsKey("mrz_data")) {
                        String jsonData = resultMap.get("mrz_data");
                        if (!StringUtil.isEmpty(jsonData)) {
                            try {
                                JSONObject jsonObject = new JSONObject(jsonData);
                                JSONObject jsonIdData = jsonObject.getJSONObject("IDData");

                                Iterator<String> barcodeFields = jsonIdData.keys();
                                while (barcodeFields.hasNext()) {
                                    String key = barcodeFields.next();
                                    String value = jsonIdData.optString(key);
                                    value = StringEscapeUtils.escapeXml11(value);

                                    // mapBarcodeData.put(key, value.trim());
                                    mapBarcodeData.put(key, StringEscapeUtils.unescapeXml(value).trim());
                                }
                                showBarcodeData(mapBarcodeData, 1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (resultMap.containsKey("extract_data")) {
                        String jsonData = resultMap.get("extract_data");
                        if (!StringUtil.isEmpty(jsonData)) {
                            try {
                                JSONObject jsonObject = new JSONObject(jsonData);
                                JSONObject jsonIdData = jsonObject.getJSONObject("IDData");

                                Iterator<String> barcodeFields = jsonIdData.keys();
                                while (barcodeFields.hasNext()) {
                                    String key = barcodeFields.next();
                                    String value = jsonIdData.optString(key);
                                   // value = StringEscapeUtils.escapeXml11(value);

                                    // mapBarcodeData.put(key, value.trim());
                                    mapBarcodeData.put(key, StringEscapeUtils.unescapeXml(value).trim());
                                }
                                showBarcodeData(mapBarcodeData, 2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
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
        PreferenceUtils.setPreference(getActivity(), ID_TITLE_FRONT, idTitleAlignment);
        PreferenceUtils.setPreference(getActivity(), ID_HINT_FRONT, idHintAlignment);
        PreferenceUtils.setPreference(getActivity(), ID_TITLE_IMAGE_FRONT, idTitleImageAlignment);
        PreferenceUtils.setPreference(getActivity(), ID_TITLE_HIDE_FRONT, hideIdCaptureTitle);
        PreferenceUtils.setPreference(getActivity(), ID_HINT_HIDE_FRONT, hideIdCaptureHintMsg);
        PreferenceUtils.setPreference(getActivity(), ID_TITLE_IMAGE_HIDE_FRONT, hideIdCaptureTitleImg);
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

    private void showBarcodeData(HashMap<String, String> resultData, int showBarcodeData) {
        LinearLayout linearLayoutParent = null;
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.barcode_popup_layout, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        LinearLayout linearLayoutTop = (LinearLayout) alertDialog.findViewById(R.id.linear_layout_top);
        TextView headerTextView = (TextView) alertDialog.findViewById(R.id.header);
        if (showBarcodeData == 0) {
            headerTextView.setText("Barcode Data");
        } else if (showBarcodeData == 1) {
            headerTextView.setText("MRZ Data");
        } else if (showBarcodeData == 2) {
            headerTextView.setText("Extracted Data");
        }

        for (Map.Entry<String, String> entry : resultData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            LinearLayout.LayoutParams linLayoutParamParent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayoutParent = new LinearLayout(getActivity());
            linearLayoutParent.setLayoutParams(linLayoutParamParent);

            linearLayoutParent.setGravity(Gravity.CENTER);
            linearLayoutParent.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams linLayoutParamKey = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            linLayoutParamKey.setMargins(2, 2, 2, 2);
            LinearLayout linearLayoutKey = new LinearLayout(getActivity());
            linearLayoutKey.setPadding(10, 20, 10, 20);
            linearLayoutKey.setLayoutParams(linLayoutParamKey);
            linearLayoutKey.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            linearLayoutKey.setGravity(Gravity.CENTER);
            linearLayoutKey.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutParent.addView(linearLayoutKey);

            LinearLayout.LayoutParams linLayoutText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linLayoutText.setMargins(10, 0, 0, 0);
            TextView textViewKey = new TextView(getActivity());
            textViewKey.setLayoutParams(linLayoutText);
            textViewKey.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray_dark));
            textViewKey.setText(key);
            textViewKey.setTextSize(12);
            textViewKey.setTypeface(textViewKey.getTypeface(), Typeface.BOLD);
            linearLayoutKey.addView(textViewKey);

            LinearLayout.LayoutParams linLayoutParamValue = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            linLayoutParamValue.setMargins(2, 2, 2, 2);
            LinearLayout linearLayoutValue = new LinearLayout(getActivity());
            linearLayoutValue.setPadding(10, 20, 10, 20);
            linearLayoutValue.setLayoutParams(linLayoutParamValue);
            linearLayoutValue.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            linearLayoutValue.setGravity(Gravity.CENTER);
            linearLayoutValue.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutParent.addView(linearLayoutValue);

            TextView textViewValue = new TextView(getActivity());
            textViewValue.setLayoutParams(linLayoutText);
            textViewValue.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray_dark));
            textViewValue.setText(value);
            textViewValue.setTextSize(12);
            textViewValue.setTypeface(textViewKey.getTypeface(), Typeface.BOLD);
            linearLayoutValue.addView(textViewValue);
            linearLayoutTop.addView(linearLayoutParent);
        }

        ImageView imageViewCancel=(ImageView)alertDialog.findViewById(R.id.cancel_btn);
        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }
}
