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
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Base64;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.idmission.client.ColorCode;
import com.idmission.client.FaceImageType;
import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.Response;
import com.idmission.client.ResponseStatusCode;
import com.idmission.client.UIConfigurationParameters;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.adapter.SelfieOverridePageAdapter;
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

public class SelfieOverrideCapture extends Fragment implements ImageProcessingResponseListener {
    //private static final String[] TITLE_IMAGE = {"None", "Title Image 1"};
    //private static final String[] FACE_CONTOURS = {"Low", "Medium", "All", "Zero"};

    @Override
    public void onDownloadXsltResultAvailable(Map<String, String> resultMap, Response response) {

    }

    //private static final String[] TEXT_FONT_TYPE_ARRAY = {"DEFAULT","DEFAULT_BOLD","SANS_SERIF","SERIF","MONOSPACE"};
    //private static final String[] TEXT_FONT_STYLE_ARRAY = {"NORMAL","BOLD","ITALIC","BOLD_ITALIC"};
    //private static final String[] FACE_TITLE_ALIGNMENT = {"Top","Bottom"};
    //private static final String[] FACE_HINT_MSG_ALIGNMENT = {"Bottom", "Top"};
    //private static final String[] FACE_HINT_ICON_ALIGNMENT = {"Top", "Bottom"};
   // private static final String[] FACE_TITILE_IMAGE_ALIGNMENT = {"Top", "Bottom"};
    private static final String[] LABELS_KEY = {"too_much_glare_face_capture", "smile_please",
            "camera_error", "turn_left", "turn_right", "move_up", "move_down", "look_straight", "subject_is_too_dark_fc_detect", "out_of_focus_fc_detect", "move_camera_closer_to_your_face",
            "camera_movement_fc_detect", "keep_face_steady", "smile_please", "face_detected", "light", "focus", "smile", "page_title_face_detection","face_detection_instruction", "face_capture_preview_message", "face_capture_preview_header"};
    private static final String FACE_GLARE_THRESHOLD = "FACE_GLARE_THRESHOLD", FACE_LIGHT_THRESHOLD = "FACE_LIGHT_THRESHOLD", FACE_FOCUS_THRESHOLD = "FACE_FOCUS_THRESHOLD", FACE_DETECTION_THRESHOLD = "FACE_DETECTION_THRESHOLD",
            FACE_IMAGE_SIZE = "FACE_IMAGE_SIZE", FACE_OUTLINE_COLOR = "FACE_OUTLINE_COLOR", DETECTED_FACE_OUTLINE_COLOR = "DETECTED_FACE_OUTLINE_COLOR", OUTSIDE_FACE_OUTLINE_COLOR = "OUTSIDE_FACE_OUTLINE_COLOR",
            FACE_OUTLINE_COLOR_ALPHA = "FACE_OUTLINE_COLOR_ALPHA", DETECTED_FACE_OUTLINE_COLOR_ALPHA = "DETECTED_FACE_OUTLINE_COLOR_ALPHA", OUTSIDE_FACE_OUTLINE_COLOR_ALPHA = "OUTSIDE_FACE_OUTLINE_COLOR_ALPHA",
            DETECTED_FACE_OUTSIDE_OUTLINE_COLOR = "DETECTED_FACE_OUTSIDE_OUTLINE_COLOR", DETECTED_FACE_OUTSIDE_OUTLINE_COLOR_ALPHA = "DETECTED_FACE_OUTSIDE_OUTLINE_COLOR_ALPHA",
            ENABLE_FRONT_CAMERA = "ENABLE_FRONT_CAMERA", ENABLE_TOGGLE_CAMERA = "ENABLE_TOGGLE_CAMERA", SHOW_PREVIEW_SCREEN = "SHOW_PREVIEW_SCREEN", SHOW_INSTRUCTION_SCREEN = "SHOW_INSTRUCTION_SCREEN",
            SELECT_FACE_TITLE_IMG = "SELECT_FACE_TITLE_IMG", SELECT_FACE_CONTOURS = "SELECT_FACE_CONTOURS", DELTA_LEFT_THRESHOLD="DELTA_LEFT_THRESHOLD", DELTA_RIGHT_THRESHOLD = "DELTA_RIGHT_THRESHOLD", DELTA_UP_THRESHOLD = "DELTA_UP_THRESHOLD", DELTA_DOWN_THRESHOLD = "DELTA_DOWN_THRESHOLD",
            TEXT_FONT_TYPE_FACE = "TEXT_FONT_TYPE_FACE", TEXT_FONT_STYLE_FACE = "TEXT_FONT_STYLE_FACE", BACK_BUTTON_COLOR_FACE = "BACK_BUTTON_COLOR_FACE", BACK_BUTTON_ALPHA_FACE = "BACK_BUTTON_ALPHA_FACE", RETRY_BUTTON_COLOR_FACE = "RETRY_BUTTON_COLOR_FACE",
            RETRY_BUTTON_ALPHA_FACE = "RETRY_BUTTON_ALPHA_FACE", RETRY_BUTTON_BORDER_COLOR_FACE= "RETRY_BUTTON_BORDER_COLOR_FACE",
            RETRY_BUTTON_BORDER_ALPHA_FACE = "RETRY_BUTTON_BORDER_ALPHA_FACE", CONFIRM_BUTTON_COLOR_FACE = "CONFIRM_BUTTON_COLOR_FACE", CONFIRM_BUTTON_ALPHA_FACE = "CONFIRM_BUTTON_ALPHA_FACE", CONFIRM_BUTTON_STYLE_FACE = "CONFIRM_BUTTON_STYLE_FACE", CONFIRM_BUTTON_STYLE_ALPHA_FACE = "CONFIRM_BUTTON_STYLE_ALPHA_FACE", TEXT_LABEL_COLOR_FACE = "TEXT_LABEL_COLOR_FACE", TEXT_LABEL_ALPHA_FACE = "TEXT_LABEL_ALPHA",
            FACE_TITLE = "FACE_TITLE", FACE_HINT = "FACE_HINT", FACE_ICON = "FACE_ICON", FACE_TITLE_IMAGE = "FACE_TITLE_IMAGE", ID_TITLE = "ID_TITLE", ID_HINT = "ID_HINT", ID_TITLE_IMAGE = "ID_TITLE_IMAGE",
            FACE_TITLE_HIDE = "FACE_TITLE_HIDE", FACE_HINT_HIDE = "FACE_HINT_HIDE", FACE_ICON_HIDE = "FACE_ICON_HIDE", FACE_TITLE_IMAGE_HIDE = "FACE_TITLE_IMAGE_HIDE", ID_TITLE_HIDE = "ID_TITLE_HIDE", ID_HINT_HIDE = "ID_HINT_HIDE", ID_TITLE_IMAGE_HIDE = "ID_TITLE_IMAGE_HIDE",
            FACE_INSTRUCTION_BTN_COLOR = "FACE_INSTRUCTION_BTN_COLOR", FACE_INSTRUCTION_BTN_ALPHA = "FACE_INSTRUCTION_BTN_ALPHA", FACE_INSTRUCTION_BTN_TXT_COLOR = "FACE_INSTRUCTION_BTN_TXT_COLOR", FACE_INSTRUCTION_BTN_TXT_ALPHA = "FACE_INSTRUCTION_BTN_TXT_ALPHA",
            HEADER_TEXT_LABEL_COLOR = "HEADER_TEXT_LABEL_COLOR", HEADER_TEXT_LABEL_ALPHA= "HEADER_TEXT_LABEL_ALPHA", HEADER_TEXT_FONT_TYPE = "HEADER_TEXT_FONT_TYPE", HEADER_TEXT_FONT_STYLE = "HEADER_TEXT_FONT_STYLE", HEADER_TEXT_LABEL_FONT_SIZE = "HEADER_TEXT_LABEL_FONT_SIZE", TEXT_LABEL_FONT_SIZE = "TEXT_LABEL_FONT_SIZE";

    private BottomSheetBehavior bottomSheetBehaviorSelfie;
    private ImageView defaultImage, bottomSheetImage, captureImage;
    private LinearLayout linearLayout, linearLayoutIndicator, linearLayoutCapture;
    private boolean isExpand = false, hideFaceCaptureTitle = false, hideFaceCaptureHintMsg = false, hideFaceCaptureHintIcon = false, hideFaceCaptureTitleImg = false, isCapture = true;
    private TextView textViewDefault;
    private Button buttonCapture, buttonBack, buttonNext, buttonSave, buttonReset, buttonAddLabel;
    private EditText empCodeEdtTxt,faceLightThresholdEdtTxt, faceGlareThresholdEdtTxt, faceFocusThresholdEdtTxt, faceDetectionThresholdEdtTxt, maxImageSizeForFaceDetect,
            faceOutlineColorEdtTxt, detectedFaceOutlineColorEdtTxt, outsideFaceOutlineColorEdtTxt,
            faceOutlineColorAlphaEdtTxt, detectedFaceOutlineColorAlphaEdtTxt, outsideFaceOutlineColorAlphaEdtTxt, outsideDetectedFaceOutlineColor,
            outsideDetectedFaceOutlineColorAlpha, deltaLeftThreshold, deltaRightThreshold, deltaUpThreshold, deltaDownThreshold,
            backButtonColorEdtTxt, retryButtonColorEdtTxt, confirmButtonColorEdtTxt, retryButtonBorderAlphaEdtTxt, confirmButtonStyleAlphaEdtTxt, backButtonAlphaEdtTxt, retryButtonAlphaEdtTxt, confirmButtonAlphaEdtTxt,retryButtonBorderColorEdtTxt, confirmButtonStyleEdtTxt,
            englishLabelEdtTxt, spanishLabelEdtTxt, textLabelColorEdtTxt, textLabelAlphaEdtTxt,
            instructionContinueBtnColorEdtTxt, instructionContinueBtnAlphaEdtTxt, instructionContinueBtnTxtColorEdtTxt, instructionContinueBtnTxtAlphaEdtTxt,headerTextLabelColorEdtTxt, headerTextLabelAlphaEdtTxt, headerTextLabelSizeEdtTxt, textLabelSizeEdtTxt;

    private SwitchCompat launchFrontCameraCheckBox, toggleCameraCheckBox, showPreviewScreenCheckBox, showInstructionFaceDetect;
    private Spinner spinnerTitle, spinnerTitleAlignment, spinnerHintMsgAlignment, spinnerHintIconAlignment, spinnerTitleImageAlignment, spinnerLabel, spinnerTextFontType, spinnerTextFontStyle, spinnerFaceContours, faceImageTypeSpinner, spinnerHeaderTextFontType, spinnerHeaderTextFontStyle;
    private CheckBox titleAlignmentCB, hintMsgAlignmentCB, hintIconAlignmentCB, titleImageAlignmentCB;
    private String faceTitleAlignment, faceHintMsgAlignment, faceHintIconAlignment, faceTitleImageAlignment;

    private ViewPager viewPagerSelfie;
    private SelfieOverridePageAdapter adapter;
    private ImageView imageSelfieFace, imageOvalFace, imageProcessedFace, imageFourth;
    private HashMap<String, String> englishLabelMap = new HashMap<>();
    private HashMap<String, String> spanishLabelMap = new HashMap<>();
    private EVolvApp eVolvApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.selfie_override_capture_layout, container, false);

        eVolvApp = (EVolvApp) (getActivity().getApplicationContext());

        bottomSheetBehaviorSelfie = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheetSelfie));
        bottomSheetImage = (ImageView) view.findViewById(R.id.image_view);
        defaultImage = (ImageView) view.findViewById(R.id.default_image);
        captureImage = (ImageView) view.findViewById(R.id.capture_image);
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);
        linearLayoutCapture = (LinearLayout) view.findViewById(R.id.linear_layout_capture);
        textViewDefault = (TextView) view.findViewById(R.id.text_view_label);
        buttonCapture = (Button) view.findViewById(R.id.button_capture);
        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);
        buttonAddLabel = (Button) view.findViewById(R.id.add_label_button);

        empCodeEdtTxt = (EditText) view.findViewById(R.id.edit_view_emp_code);
        faceGlareThresholdEdtTxt = (EditText) view.findViewById(R.id.faceGlareThreshold_ET);
        faceLightThresholdEdtTxt = (EditText) view.findViewById(R.id.faceLightThreshold_ET);
        faceFocusThresholdEdtTxt = (EditText) view.findViewById(R.id.faceFocusThreshold_ET);
        faceDetectionThresholdEdtTxt = (EditText) view.findViewById(R.id.faceDetectionThreshold_ET);
        maxImageSizeForFaceDetect = (EditText) view.findViewById(R.id.maxImageSize_FACE_ET);
        launchFrontCameraCheckBox = (SwitchCompat) view.findViewById(R.id.launchFrontCameraCheckBox);
        toggleCameraCheckBox = (SwitchCompat) view.findViewById(R.id.toggleCameraCheckBox);
        showPreviewScreenCheckBox = (SwitchCompat) view.findViewById(R.id.showPreviewScreenCheckBox);
        faceOutlineColorEdtTxt = (EditText) view.findViewById(R.id.face_outline_normal);
        faceOutlineColorAlphaEdtTxt = (EditText) view.findViewById(R.id.face_outline_normal_alpha);
        detectedFaceOutlineColorEdtTxt = (EditText) view.findViewById(R.id.face_outline_detected);
        detectedFaceOutlineColorAlphaEdtTxt = (EditText) view.findViewById(R.id.face_outline_detected_alpha);
        outsideFaceOutlineColorEdtTxt = (EditText) view.findViewById(R.id.outside_face_outline_color);
        outsideFaceOutlineColorAlphaEdtTxt = (EditText) view.findViewById(R.id.outside_face_outline_color_alpha);
        outsideDetectedFaceOutlineColor = (EditText) view.findViewById(R.id.outside_detected_face_outline_color);
        outsideDetectedFaceOutlineColorAlpha = (EditText) view.findViewById(R.id.outside_detected_face_outline_color_alpha);
        showInstructionFaceDetect = (SwitchCompat) view.findViewById(R.id.show_instruction_face_detect);
        spinnerTitle = (Spinner) view.findViewById(R.id.spinner_title);
        spinnerTextFontType = (Spinner) view.findViewById(R.id.text_font_type_spinner);
        spinnerTextFontStyle = (Spinner) view.findViewById(R.id.text_font_style_spinner);
        spinnerFaceContours = (Spinner) view.findViewById(R.id.spinner_face_contours);
        spinnerHeaderTextFontType= (Spinner) view.findViewById(R.id.text_header_font_type_spinner);
        spinnerHeaderTextFontStyle= (Spinner) view.findViewById(R.id.text_header_font_style_spinner);

        spinnerTitleAlignment = (Spinner) view.findViewById(R.id.spinner_face_title_alignment);
        spinnerHintMsgAlignment = (Spinner) view.findViewById(R.id.spinner_face_hint_msg_alignment);
        spinnerHintIconAlignment = (Spinner) view.findViewById(R.id.spinner_face_hint_icon_alignment);
        spinnerTitleImageAlignment = (Spinner) view.findViewById(R.id.spinner_face_title_image_alignment);
        faceImageTypeSpinner = (Spinner) view.findViewById(R.id.faceImageType_ET);

        buttonSave = (Button) view.findViewById(R.id.button_save);
        buttonReset = (Button) view.findViewById(R.id.button_reset);
        deltaLeftThreshold = (EditText) view.findViewById(R.id.delta_left_threshold);
        deltaRightThreshold = (EditText) view.findViewById(R.id.delta_right_threshold);
        deltaUpThreshold = (EditText) view.findViewById(R.id.delta_up_threshold);
        deltaDownThreshold = (EditText) view.findViewById(R.id.delta_down_threshold);

        backButtonColorEdtTxt = (EditText)view.findViewById(R.id.back_button_color);
        retryButtonColorEdtTxt = (EditText)view.findViewById(R.id.retry_button_color);
        retryButtonBorderColorEdtTxt = (EditText)view.findViewById(R.id.retry_button_border_color);
        retryButtonBorderAlphaEdtTxt = (EditText)view.findViewById(R.id.transparency_retry_button_border_color);
        confirmButtonColorEdtTxt = (EditText)view.findViewById(R.id.confirm_button_color);
        confirmButtonStyleEdtTxt = (EditText)view.findViewById(R.id.confirm_button_style);
        confirmButtonStyleAlphaEdtTxt = (EditText)view.findViewById(R.id.transparency_confirm_button_style);
        backButtonAlphaEdtTxt = (EditText)view.findViewById(R.id.transparency_back_button_color);
        retryButtonAlphaEdtTxt = (EditText)view.findViewById(R.id.transparency_retry_button_color);
        confirmButtonAlphaEdtTxt = (EditText)view.findViewById(R.id.transparency_confirm_button_color);
        textLabelColorEdtTxt = (EditText) view.findViewById(R.id.text_label_color);
        textLabelAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_text_label_color);
        instructionContinueBtnColorEdtTxt = (EditText) view.findViewById(R.id.instruction_continue_button_color);
        instructionContinueBtnAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_instruction_continue_button);
        instructionContinueBtnTxtColorEdtTxt = (EditText) view.findViewById(R.id.instruction_continue_button_text_color);
        instructionContinueBtnTxtAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_instruction_continue_button_text);

        englishLabelEdtTxt = (EditText) view.findViewById(R.id.english_label);
        spanishLabelEdtTxt = (EditText) view.findViewById(R.id.spanish_label);
        spinnerLabel = (Spinner) view.findViewById(R.id.spinner_label_type);
        titleAlignmentCB = (CheckBox) view.findViewById(R.id.checkbox_face_title_alignment);
        hintMsgAlignmentCB = (CheckBox) view.findViewById(R.id.checkbox_face_hint_msg_alignment);
        hintIconAlignmentCB = (CheckBox) view.findViewById(R.id.checkbox_face_hint_icon_alignment);
        titleImageAlignmentCB = (CheckBox) view.findViewById(R.id.checkbox_face_title_image_alignment);

        headerTextLabelColorEdtTxt = (EditText) view.findViewById(R.id.text_header_label_color);
        headerTextLabelAlphaEdtTxt = (EditText) view.findViewById(R.id.transparency_text_header_label_color);
        headerTextLabelSizeEdtTxt = (EditText) view.findViewById(R.id.Header_text_size);
        textLabelSizeEdtTxt = (EditText) view.findViewById(R.id.text_size);

        viewPagerSelfie = (ViewPager) view.findViewById(R.id.view_pager_selfie);
        imageSelfieFace = (ImageView) view.findViewById(R.id.image_view_first);
        imageOvalFace = (ImageView) view.findViewById(R.id.image_view_third);
        imageProcessedFace = (ImageView) view.findViewById(R.id.image_view_second);
        imageFourth = (ImageView) view.findViewById(R.id.image_view_fourth);
        linearLayoutIndicator = (LinearLayout) view.findViewById(R.id.linear_layout_indicator);

        spinnerLabel.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, LABELS_KEY));
        //spinnerTitle.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TITLE_IMAGE));
        //spinnerTextFontType.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TEXT_FONT_TYPE_ARRAY));
        //spinnerTextFontStyle.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TEXT_FONT_STYLE_ARRAY));

        //spinnerTitleAlignment.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, FACE_TITLE_ALIGNMENT));
        //spinnerHintMsgAlignment.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, FACE_HINT_MSG_ALIGNMENT));
        //spinnerHintIconAlignment.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, FACE_HINT_ICON_ALIGNMENT));
        //spinnerTitleImageAlignment.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, FACE_TITILE_IMAGE_ALIGNMENT));
        //spinnerFaceContours.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, FACE_CONTOURS));
        //faceImageTypeSpinner.setAdapter(new ArrayAdapter<FaceImageType>(getContext(), android.R.layout.simple_dropdown_item_1line, FaceImageType.values()));
        //spinnerHeaderTextFontType.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TEXT_FONT_TYPE_ARRAY));
        //spinnerHeaderTextFontStyle.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, TEXT_FONT_STYLE_ARRAY));

        setSpinnerAdapter();
        textViewDefault.setText(getString(R.string.capture));
        imageFourth.setVisibility(View.GONE);

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
        titleAlignmentList.add(new Pair<String, String>(getString(R.string.bottom),"Bottom"));
        SpinnerAdapterForPair titleAlignmentListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                titleAlignmentList);
        spinnerTitleAlignment.setAdapter(titleAlignmentListAdapter);

        ArrayList<Pair<String,String>> hintMsgAlignmentList = new ArrayList<Pair<String,String>>();
        hintMsgAlignmentList.add(new Pair<String, String>(getString(R.string.bottom),"Bottom"));
        hintMsgAlignmentList.add(new Pair<String, String>(getString(R.string.top),"Top"));
        SpinnerAdapterForPair hintMsgAlignmentListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                hintMsgAlignmentList);
        spinnerHintMsgAlignment.setAdapter(hintMsgAlignmentListAdapter);

        ArrayList<Pair<String,String>> hintIconAlignmentList = new ArrayList<Pair<String,String>>();
        hintIconAlignmentList.add(new Pair<String, String>(getString(R.string.top),"Top"));
        hintIconAlignmentList.add(new Pair<String, String>(getString(R.string.bottom),"Bottom"));
        SpinnerAdapterForPair hintIconAlignmentListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                hintIconAlignmentList);
        spinnerHintIconAlignment.setAdapter(hintIconAlignmentListAdapter);

        ArrayList<Pair<String,String>> titleImageAlignmentList = new ArrayList<Pair<String,String>>();
        titleImageAlignmentList.add(new Pair<String, String>(getString(R.string.top),"Top"));
        titleImageAlignmentList.add(new Pair<String, String>(getString(R.string.bottom),"Bottom"));
        SpinnerAdapterForPair titleImageAlignmentListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                titleImageAlignmentList);
        spinnerTitleImageAlignment.setAdapter(titleImageAlignmentListAdapter);

        ArrayList<Pair<String,String>> faceContoursList = new ArrayList<Pair<String,String>>();
        faceContoursList.add(new Pair<String, String>(getString(R.string.low),"Low"));
        faceContoursList.add(new Pair<String, String>(getString(R.string.medium),"Medium"));
        faceContoursList.add(new Pair<String, String>(getString(R.string.all),"All"));
        faceContoursList.add(new Pair<String, String>(getString(R.string.zero),"Zero"));
        SpinnerAdapterForPair faceContoursListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                faceContoursList);
        spinnerFaceContours.setAdapter(faceContoursListAdapter);

        ArrayList<Pair<String,String>> faceImageTypeList = new ArrayList<Pair<String,String>>();
        faceImageTypeList.add(new Pair<String, String>(getString(R.string.face),"FACE"));
        faceImageTypeList.add(new Pair<String, String>(getString(R.string.processed_face),"PROCESSED_FACE"));
        faceImageTypeList.add(new Pair<String, String>(getString(R.string.oval_face),"OVAL_FACE"));
        SpinnerAdapterForPair faceImageTypeAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                faceImageTypeList);
        faceImageTypeSpinner.setAdapter(faceImageTypeAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        Bitmap bitmapFace = BitmapUtils.getBitmapFromFile(eVolvApp.getBaseDirectoryPath() + Constants.FACE_IMAGE_OVERRIDE);
        Bitmap bitmapOval = BitmapUtils.getBitmapFromFile(eVolvApp.getBaseDirectoryPath() + Constants.OVAL_FACE_IMAGE_OVERRIDE);
        Bitmap bitmapProcessed = BitmapUtils.getBitmapFromFile(eVolvApp.getBaseDirectoryPath() + Constants.PROCESSED_FACE_IMAGE_OVERRIDE);

        // setImage(bitmap);
        if ((bitmapFace != null) && (bitmapOval != null) && (bitmapProcessed != null)) {
            viewPagerSelfie.setVisibility(View.VISIBLE);
            linearLayoutIndicator.setVisibility(View.VISIBLE);
            defaultImage.setVisibility(View.GONE);
            textViewDefault.setVisibility(View.GONE);
            buttonCapture.setText(R.string.re_capture);

            setUpView();
            setTab();
            onCircleButtonClick();
        } else {
            viewPagerSelfie.setVisibility(View.GONE);
            linearLayoutIndicator.setVisibility(View.GONE);
            defaultImage.setVisibility(View.VISIBLE);
            textViewDefault.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        eVolvApp.setOverrideEmpCode(empCodeEdtTxt.getText().toString());
    }

    private void setSavedValue() {
        faceGlareThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FACE_GLARE_THRESHOLD, "" + ImageProcessingSDK.dFaceGlareThreshold));
        faceLightThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FACE_LIGHT_THRESHOLD, "" + ImageProcessingSDK.dFaceLightThreshold));
        faceFocusThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FACE_FOCUS_THRESHOLD, "" + ImageProcessingSDK.dFaceFocusThreshold));
        faceDetectionThresholdEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FACE_DETECTION_THRESHOLD, "" + ImageProcessingSDK.dFaceDetectionThreshold));
        maxImageSizeForFaceDetect.setText(PreferenceUtils.getPreference(getActivity(), FACE_IMAGE_SIZE, "" + ImageProcessingSDK.dImageSizeForFace));
        faceOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FACE_OUTLINE_COLOR, "" + ImageProcessingSDK.dFaceOutlineColor));
        faceOutlineColorAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FACE_OUTLINE_COLOR_ALPHA, "" + ImageProcessingSDK.dAlpha));
        detectedFaceOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_FACE_OUTLINE_COLOR, "" + ImageProcessingSDK.dDetectedFaceOutlineColor));
        detectedFaceOutlineColorAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_FACE_OUTLINE_COLOR_ALPHA, "" + ImageProcessingSDK.dAlpha));
        outsideFaceOutlineColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), OUTSIDE_FACE_OUTLINE_COLOR, "" + ImageProcessingSDK.dOutsideFaceOutlineColor));
        outsideFaceOutlineColorAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), OUTSIDE_FACE_OUTLINE_COLOR_ALPHA, "" + ImageProcessingSDK.dAlpha));
        outsideDetectedFaceOutlineColor.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_FACE_OUTSIDE_OUTLINE_COLOR, "" + ImageProcessingSDK.dDetectedOutsideFaceOutlineColor));
        outsideDetectedFaceOutlineColorAlpha.setText(PreferenceUtils.getPreference(getActivity(), DETECTED_FACE_OUTSIDE_OUTLINE_COLOR_ALPHA, "" + ImageProcessingSDK.dAlpha));
        launchFrontCameraCheckBox.setChecked(PreferenceUtils.getPreference(getActivity(), ENABLE_FRONT_CAMERA, ImageProcessingSDK.dFaceLaunchFrontCamera));
        toggleCameraCheckBox.setChecked(PreferenceUtils.getPreference(getActivity(), ENABLE_TOGGLE_CAMERA, ImageProcessingSDK.dFaceEnableToggleButton));
        showPreviewScreenCheckBox.setChecked(PreferenceUtils.getPreference(getActivity(), SHOW_PREVIEW_SCREEN, ImageProcessingSDK.dFaceShowPreviewScreen));
        showInstructionFaceDetect.setChecked(PreferenceUtils.getPreference(getActivity(), SHOW_INSTRUCTION_SCREEN, ImageProcessingSDK.dFaceShowInstructionScreen));
        deltaLeftThreshold.setText(PreferenceUtils.getPreference(getActivity(), DELTA_LEFT_THRESHOLD, "" + ImageProcessingSDK.deltaLeftThreshold));
        deltaRightThreshold.setText(PreferenceUtils.getPreference(getActivity(), DELTA_RIGHT_THRESHOLD, "" + ImageProcessingSDK.deltaRightThreshold));
        deltaUpThreshold.setText(PreferenceUtils.getPreference(getActivity(), DELTA_UP_THRESHOLD, "" + ImageProcessingSDK.deltaUpThreshold));
        deltaDownThreshold.setText(PreferenceUtils.getPreference(getActivity(), DELTA_DOWN_THRESHOLD, "" + ImageProcessingSDK.deltaDownThreshold));
        retryButtonBorderColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_BORDER_COLOR_FACE, ""+ ImageProcessingSDK.retryButtonBorderColor));
        retryButtonBorderAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_BORDER_ALPHA_FACE, ""+ ImageProcessingSDK.dAlpha));
        confirmButtonStyleEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_STYLE_FACE, ""+ ImageProcessingSDK.confirmButtonStyle));
        confirmButtonStyleAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_STYLE_ALPHA_FACE, ""+ ImageProcessingSDK.dAlpha));

        backButtonColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), BACK_BUTTON_COLOR_FACE, ""+ ImageProcessingSDK.backButtonColor));
        backButtonAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), BACK_BUTTON_ALPHA_FACE, ""+ ImageProcessingSDK.dAlpha));
        retryButtonColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_COLOR_FACE, ""+ ImageProcessingSDK.retryButtonColor));
        retryButtonAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_ALPHA_FACE, ""+ ImageProcessingSDK.dAlpha));
        confirmButtonColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_COLOR_FACE, ""+ ImageProcessingSDK.confirmButtonColor));
        confirmButtonAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_ALPHA_FACE, ""+ ImageProcessingSDK.dAlpha));
        textLabelColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_COLOR_FACE, ""+ ImageProcessingSDK.textLabelColor));
        textLabelAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_ALPHA_FACE, ""+ ImageProcessingSDK.dAlpha));
        instructionContinueBtnColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FACE_INSTRUCTION_BTN_COLOR, ""+ ImageProcessingSDK.instructionContinueButtonColor));
        instructionContinueBtnAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FACE_INSTRUCTION_BTN_ALPHA, ""+ ImageProcessingSDK.dAlpha));
        instructionContinueBtnTxtColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FACE_INSTRUCTION_BTN_TXT_COLOR, ""+ ImageProcessingSDK.instructionContinueButtonTextColor));
        instructionContinueBtnTxtAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), FACE_INSTRUCTION_BTN_TXT_ALPHA, ""+ ImageProcessingSDK.dAlpha));
        headerTextLabelColorEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_LABEL_COLOR, ""+ ImageProcessingSDK.textLabelColor));
        headerTextLabelAlphaEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_LABEL_ALPHA, ""+ ImageProcessingSDK.dAlpha));
        headerTextLabelSizeEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_LABEL_FONT_SIZE, ""));
        textLabelSizeEdtTxt.setText(PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_FONT_SIZE, ""));

        String showTitle = PreferenceUtils.getPreference(getActivity(), SELECT_FACE_TITLE_IMG, "None");
        if (showTitle.equalsIgnoreCase("None")) {
            spinnerTitle.setSelection(0);
        } else {
            spinnerTitle.setSelection(1);
        }

        String textFontType = PreferenceUtils.getPreference(getActivity(), TEXT_FONT_TYPE_FACE, "DEFAULT");
        //spinnerTextFontType.setSelection(Arrays.asList(TEXT_FONT_TYPE_ARRAY).indexOf(textFontType));
        spinnerTextFontType.setSelection(((ArrayAdapter<String>) spinnerTextFontType.getAdapter()).getPosition(textFontType));

        String textFontStyle = PreferenceUtils.getPreference(getActivity(), TEXT_FONT_STYLE_FACE, "NORMAL");
        //spinnerTextFontStyle.setSelection(Arrays.asList(TEXT_FONT_STYLE_ARRAY).indexOf(textFontStyle));
        spinnerTextFontStyle.setSelection(((ArrayAdapter<String>) spinnerTextFontStyle.getAdapter()).getPosition(textFontStyle));

        String titleAlignment = PreferenceUtils.getPreference(getActivity(), FACE_TITLE, "Bottom");
        spinnerTitleAlignment.setSelection(((ArrayAdapter<String>) spinnerTitleAlignment.getAdapter()).getPosition(titleAlignment));

        String hintMsgAlignment = PreferenceUtils.getPreference(getActivity(), FACE_HINT, "Top");
        spinnerHintMsgAlignment.setSelection(((ArrayAdapter<String>) spinnerHintMsgAlignment.getAdapter()).getPosition(hintMsgAlignment));

        String hintIconAlignment = PreferenceUtils.getPreference(getActivity(), FACE_ICON, "Top");
        spinnerHintIconAlignment.setSelection(((ArrayAdapter<String>) spinnerHintIconAlignment.getAdapter()).getPosition(hintIconAlignment));

        String titleImageAlignment = PreferenceUtils.getPreference(getActivity(), FACE_TITLE, "Top");
        spinnerTitleImageAlignment.setSelection(((ArrayAdapter<String>) spinnerTitleImageAlignment.getAdapter()).getPosition(titleImageAlignment));

        String faceContours = PreferenceUtils.getPreference(getActivity(), SELECT_FACE_CONTOURS, ImageProcessingSDK.dFaceContour);
        spinnerFaceContours.setSelection(((ArrayAdapter<String>) spinnerFaceContours.getAdapter()).getPosition(faceContours));

        String headerTextFontType = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_FONT_TYPE, "DEFAULT");
        //spinnerHeaderTextFontType.setSelection(Arrays.asList(TEXT_FONT_TYPE_ARRAY).indexOf(headerTextFontType));
        spinnerHeaderTextFontType.setSelection(((ArrayAdapter<String>) spinnerHeaderTextFontType.getAdapter()).getPosition(headerTextFontType));

        String headerTextFontStyle = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_FONT_STYLE, "NORMAL");
        //spinnerHeaderTextFontStyle.setSelection(Arrays.asList(TEXT_FONT_STYLE_ARRAY).indexOf(headerTextFontStyle));
        spinnerHeaderTextFontStyle.setSelection(((ArrayAdapter<String>) spinnerHeaderTextFontStyle.getAdapter()).getPosition(headerTextFontStyle));

        boolean faceTitleHide = PreferenceUtils.getPreference(getActivity(), FACE_TITLE_HIDE, false);
        if (faceTitleHide) {
            titleAlignmentCB.setChecked(true);
        }

        boolean faceHintMsgHide = PreferenceUtils.getPreference(getActivity(), FACE_HINT_HIDE, false);
        if (faceHintMsgHide) {
            hintMsgAlignmentCB.setChecked(true);
        }

        boolean faceHintIconHide = PreferenceUtils.getPreference(getActivity(), FACE_ICON_HIDE, false);
        if (faceHintIconHide) {
            hintIconAlignmentCB.setChecked(true);
        }

        boolean faceTitleImageHide = PreferenceUtils.getPreference(getActivity(), FACE_TITLE_IMAGE_HIDE, false);
        if (faceTitleImageHide) {
            titleImageAlignmentCB.setChecked(true);
        }
    }

    private void resetValue() {
        faceGlareThresholdEdtTxt.setText("" + ImageProcessingSDK.dFaceGlareThreshold);
        faceLightThresholdEdtTxt.setText("" + ImageProcessingSDK.dFaceLightThreshold);
        faceFocusThresholdEdtTxt.setText("" + ImageProcessingSDK.dFaceFocusThreshold);
        faceDetectionThresholdEdtTxt.setText("" + ImageProcessingSDK.dFaceDetectionThreshold);
        maxImageSizeForFaceDetect.setText("" + ImageProcessingSDK.dImageSizeForFace);
        faceOutlineColorEdtTxt.setText("" + ImageProcessingSDK.dFaceOutlineColor);
        faceOutlineColorAlphaEdtTxt.setText("" + ImageProcessingSDK.dAlpha);
        detectedFaceOutlineColorEdtTxt.setText("" + ImageProcessingSDK.dDetectedFaceOutlineColor);
        detectedFaceOutlineColorAlphaEdtTxt.setText("" + ImageProcessingSDK.dAlpha);
        outsideFaceOutlineColorEdtTxt.setText("" + ImageProcessingSDK.dOutsideFaceOutlineColor);
        outsideFaceOutlineColorAlphaEdtTxt.setText("" + ImageProcessingSDK.dAlpha);
        outsideDetectedFaceOutlineColor.setText("" + ImageProcessingSDK.dDetectedOutsideFaceOutlineColor);
        outsideDetectedFaceOutlineColorAlpha.setText("" + ImageProcessingSDK.dAlpha);
        launchFrontCameraCheckBox.setChecked(ImageProcessingSDK.dFaceLaunchFrontCamera);
        deltaLeftThreshold.setText("" + ImageProcessingSDK.deltaLeftThreshold);
        deltaRightThreshold.setText("" + ImageProcessingSDK.deltaRightThreshold);
        deltaUpThreshold.setText("" + ImageProcessingSDK.deltaUpThreshold);
        deltaDownThreshold.setText("" + ImageProcessingSDK.deltaDownThreshold);
        englishLabelEdtTxt.setText("");
        spanishLabelEdtTxt.setText("");
        toggleCameraCheckBox.setChecked(ImageProcessingSDK.dFaceEnableToggleButton);
        showPreviewScreenCheckBox.setChecked(ImageProcessingSDK.dFaceShowPreviewScreen);
        showInstructionFaceDetect.setChecked(ImageProcessingSDK.dFaceShowInstructionScreen);
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

        spinnerTitle.setSelection(0);
        spinnerTextFontType.setSelection(0);
        spinnerTextFontStyle.setSelection(0);
        spinnerTitleAlignment.setSelection(0);
        spinnerHintIconAlignment.setSelection(0);
        spinnerHintMsgAlignment.setSelection(0);
        spinnerTitleImageAlignment.setSelection(0);
        spanishLabelEdtTxt.setSelection(0);
        spinnerFaceContours.setSelection(0);
        spinnerHeaderTextFontStyle.setSelection(0);
        spinnerHeaderTextFontType.setSelection(0);
        titleAlignmentCB.setChecked(false);
        hintMsgAlignmentCB.setChecked(false);
        hintIconAlignmentCB.setChecked(false);
        titleImageAlignmentCB.setChecked(false);

        //Add label API call for Reset Value
        englishLabelMap.clear();
        spanishLabelMap.clear();
        ImageProcessingSDK.getInstance().initializeLabels(englishLabelMap, spanishLabelMap);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        bottomSheetBehaviorSelfie.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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
                        bottomSheetBehaviorSelfie.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        bottomSheetBehaviorSelfie.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        isExpand = true;
                        bottomSheetBehaviorSelfie.setState(BottomSheetBehavior.STATE_EXPANDED);
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
                    bottomSheetBehaviorSelfie.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehaviorSelfie.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        linearLayoutCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCapture) {
                    selfieCaptureAPICall();
                }
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String glareThreshold = faceGlareThresholdEdtTxt.getText().toString().trim();
                String lightThreshold = faceLightThresholdEdtTxt.getText().toString().trim();
                String focusThreshold = faceFocusThresholdEdtTxt.getText().toString().trim();
                String detectionThreshold = faceDetectionThresholdEdtTxt.getText().toString().trim();
                String maxImagesize = maxImageSizeForFaceDetect.getText().toString().trim();
                String faceOutlineColor = faceOutlineColorEdtTxt.getText().toString().trim();
                String faceOutlineColorAlpha = faceOutlineColorAlphaEdtTxt.getText().toString().trim();
                String detectedFaceOutlineColor = detectedFaceOutlineColorEdtTxt.getText().toString().trim();
                String detectedFaceOutlineColorAlpha = detectedFaceOutlineColorAlphaEdtTxt.getText().toString().trim();
                String outsideFaceOutlineColor = outsideFaceOutlineColorEdtTxt.getText().toString().trim();
                String outsideFaceOutlineColorAlpha = outsideFaceOutlineColorAlphaEdtTxt.getText().toString().trim();
                String detectedOutsideFaceOutlineColor = outsideDetectedFaceOutlineColor.getText().toString().trim();
                String detectedOutsideFaceOutlineColorAlpha = outsideDetectedFaceOutlineColorAlpha.getText().toString().trim();
               // String showTitle = spinnerTitle.getSelectedItem().toString();
//                String showTitle = ((Pair)spinnerTitle.getSelectedItem()).second.toString();
                String deltaLeft = deltaLeftThreshold.getText().toString().trim();
                String deltaRight = deltaRightThreshold.getText().toString().trim();
                String deltaUp = deltaUpThreshold.getText().toString().trim();
                String deltaDown = deltaDownThreshold.getText().toString().trim();
               // String textFontType = spinnerTextFontType.getSelectedItem().toString();
                //String textFontStyle = spinnerTextFontStyle.getSelectedItem().toString();
//                String textFontType = ((Pair)spinnerTextFontType.getSelectedItem()).second.toString();
//                String textFontStyle = ((Pair)spinnerTextFontStyle.getSelectedItem()).second.toString();
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
               // String faceContours = spinnerFaceContours.getSelectedItem().toString().trim();
//                String faceContours = ((Pair)spinnerFaceContours.getSelectedItem()).second.toString();
                String headerTextColorEdtTxt = headerTextLabelColorEdtTxt.getText().toString().trim();
                String headerTextAlphaEdtTxt = headerTextLabelAlphaEdtTxt.getText().toString().trim();
                String headerTextSizeEdtTxt = headerTextLabelSizeEdtTxt.getText().toString().trim();
                String textSizeEdtTxt = textLabelSizeEdtTxt.getText().toString().trim();
               // String headerTextFontType = spinnerHeaderTextFontType.getSelectedItem().toString();
               // String headerTextFontStyle = spinnerHeaderTextFontStyle.getSelectedItem().toString();
//                String headerTextFontType = ((Pair)spinnerHeaderTextFontType.getSelectedItem()).second.toString();
//                String headerTextFontStyle = ((Pair)spinnerHeaderTextFontStyle.getSelectedItem()).second.toString();

                PreferenceUtils.setPreference(getActivity(), FACE_GLARE_THRESHOLD, glareThreshold);
                PreferenceUtils.setPreference(getActivity(), FACE_LIGHT_THRESHOLD, lightThreshold);
                PreferenceUtils.setPreference(getActivity(), FACE_FOCUS_THRESHOLD, focusThreshold);
                PreferenceUtils.setPreference(getActivity(), FACE_DETECTION_THRESHOLD, detectionThreshold);
                PreferenceUtils.setPreference(getActivity(), FACE_IMAGE_SIZE, maxImagesize);
                PreferenceUtils.setPreference(getActivity(), FACE_OUTLINE_COLOR, faceOutlineColor);
                PreferenceUtils.setPreference(getActivity(), DETECTED_FACE_OUTLINE_COLOR, detectedFaceOutlineColor);
                PreferenceUtils.setPreference(getActivity(), OUTSIDE_FACE_OUTLINE_COLOR, outsideFaceOutlineColor);
                PreferenceUtils.setPreference(getActivity(), FACE_OUTLINE_COLOR_ALPHA, faceOutlineColorAlpha);
                PreferenceUtils.setPreference(getActivity(), DETECTED_FACE_OUTLINE_COLOR_ALPHA, detectedFaceOutlineColorAlpha);
                PreferenceUtils.setPreference(getActivity(), OUTSIDE_FACE_OUTLINE_COLOR_ALPHA, outsideFaceOutlineColorAlpha);
                PreferenceUtils.setPreference(getActivity(), DETECTED_FACE_OUTSIDE_OUTLINE_COLOR, detectedOutsideFaceOutlineColor);
                PreferenceUtils.setPreference(getActivity(), DETECTED_FACE_OUTSIDE_OUTLINE_COLOR_ALPHA, detectedOutsideFaceOutlineColorAlpha);
                PreferenceUtils.setPreference(getActivity(), ENABLE_FRONT_CAMERA, launchFrontCameraCheckBox.isChecked());
                PreferenceUtils.setPreference(getActivity(), ENABLE_TOGGLE_CAMERA, toggleCameraCheckBox.isChecked());
                PreferenceUtils.setPreference(getActivity(), SHOW_PREVIEW_SCREEN, showPreviewScreenCheckBox.isChecked());
                PreferenceUtils.setPreference(getActivity(), SHOW_INSTRUCTION_SCREEN, showInstructionFaceDetect.isChecked());
                //PreferenceUtils.setPreference(getActivity(), SHOW_INSTRUCTION_SCREEN, showInstructionFaceDetect.isChecked());
//                PreferenceUtils.setPreference(getActivity(), SELECT_FACE_TITLE_IMG, showTitle);
                PreferenceUtils.setPreference(getActivity(), DELTA_LEFT_THRESHOLD, deltaLeft);
                PreferenceUtils.setPreference(getActivity(), DELTA_RIGHT_THRESHOLD, deltaRight);
                PreferenceUtils.setPreference(getActivity(), DELTA_UP_THRESHOLD, deltaUp);
                PreferenceUtils.setPreference(getActivity(), DELTA_DOWN_THRESHOLD, deltaDown);
//                PreferenceUtils.setPreference(getActivity(), TEXT_FONT_TYPE_FACE, textFontType);
//                PreferenceUtils.setPreference(getActivity(), TEXT_FONT_STYLE_FACE, textFontStyle);
                PreferenceUtils.setPreference(getActivity(), BACK_BUTTON_COLOR_FACE, backButtonColor);
                PreferenceUtils.setPreference(getActivity(), BACK_BUTTON_ALPHA_FACE, backButtonAlpha);
                PreferenceUtils.setPreference(getActivity(), RETRY_BUTTON_COLOR_FACE, retryButtonColor);
                PreferenceUtils.setPreference(getActivity(), RETRY_BUTTON_ALPHA_FACE, retryButtonAlpha);
                PreferenceUtils.setPreference(getActivity(), RETRY_BUTTON_BORDER_COLOR_FACE, retryButtonBorderColor);
                PreferenceUtils.setPreference(getActivity(), RETRY_BUTTON_BORDER_ALPHA_FACE, retryButtonBorderAlpha);
                PreferenceUtils.setPreference(getActivity(), CONFIRM_BUTTON_COLOR_FACE, confirmButtonColor);
                PreferenceUtils.setPreference(getActivity(), CONFIRM_BUTTON_ALPHA_FACE, confirmButtonAlpha);
                PreferenceUtils.setPreference(getActivity(), CONFIRM_BUTTON_STYLE_FACE, confirmButtonStyle);
                PreferenceUtils.setPreference(getActivity(), CONFIRM_BUTTON_STYLE_ALPHA_FACE, confirmButtonStyleAlpha);
                PreferenceUtils.setPreference(getActivity(), TEXT_LABEL_COLOR_FACE, textLabelColor);
                PreferenceUtils.setPreference(getActivity(), TEXT_LABEL_ALPHA_FACE, textLabelAlpha);
                PreferenceUtils.setPreference(getActivity(), FACE_INSTRUCTION_BTN_COLOR, instructionContinueBtnColor);
                PreferenceUtils.setPreference(getActivity(), FACE_INSTRUCTION_BTN_ALPHA, instructionContinueBtnColorAlpha);
                PreferenceUtils.setPreference(getActivity(), FACE_INSTRUCTION_BTN_TXT_COLOR, instructionContinueBtnTxtColor);
                PreferenceUtils.setPreference(getActivity(), FACE_INSTRUCTION_BTN_TXT_ALPHA, instructionContinueBtnTxtAlpha);
//                PreferenceUtils.setPreference(getActivity(), SELECT_FACE_CONTOURS, faceContours);
                PreferenceUtils.setPreference(getActivity(), HEADER_TEXT_LABEL_COLOR, headerTextColorEdtTxt);
                PreferenceUtils.setPreference(getActivity(), HEADER_TEXT_LABEL_ALPHA, headerTextAlphaEdtTxt);
                PreferenceUtils.setPreference(getActivity(), HEADER_TEXT_LABEL_FONT_SIZE, headerTextSizeEdtTxt);
                PreferenceUtils.setPreference(getActivity(), TEXT_LABEL_FONT_SIZE, textSizeEdtTxt);
//                PreferenceUtils.setPreference(getActivity(), HEADER_TEXT_FONT_TYPE, headerTextFontType);
//                PreferenceUtils.setPreference(getActivity(), HEADER_TEXT_FONT_STYLE, headerTextFontStyle);

                //API call for custom UI
//                faceTitleAlignment = spinnerTitleAlignment.getSelectedItem().toString();
//                faceHintMsgAlignment = spinnerHintMsgAlignment.getSelectedItem().toString();
//                faceHintIconAlignment = spinnerHintIconAlignment.getSelectedItem().toString();
//                faceTitleImageAlignment = spinnerTitleImageAlignment.getSelectedItem().toString();
                String idTitle = PreferenceUtils.getPreference(getActivity(), ID_TITLE, "Top");
                String idHint = PreferenceUtils.getPreference(getActivity(), ID_HINT, "Center");
                String idTitleImage = PreferenceUtils.getPreference(getActivity(), ID_TITLE_IMAGE, "Bottom");
                boolean idTitleHide = PreferenceUtils.getPreference(getActivity(), ID_TITLE_HIDE, false);
                boolean idHintHide = PreferenceUtils.getPreference(getActivity(), ID_HINT_HIDE, false);
                boolean idTitleImageHide = PreferenceUtils.getPreference(getActivity(), ID_TITLE_IMAGE_HIDE, false);

//                boolean faceTitleOnTop, faceHintMsgOnTop, faceHintIconOnTop, faceTitleImageOnTop;
//                if (faceTitleAlignment.equalsIgnoreCase("Bottom")) {
//                    faceTitleOnTop = false;
//                } else {
//                    faceTitleOnTop = true;
//                }
//
//                if (faceHintMsgAlignment.equalsIgnoreCase("Bottom")) {
//                    faceHintMsgOnTop = false;
//                } else {
//                    faceHintMsgOnTop = true;
//                }
//
//                if (faceHintIconAlignment.equalsIgnoreCase("Bottom")) {
//                    faceHintIconOnTop = false;
//                } else {
//                    faceHintIconOnTop = true;
//                }
//
//                if (faceTitleImageAlignment.equalsIgnoreCase("Bottom")) {
//                    faceTitleImageOnTop = false;
//                } else {
//                    faceTitleImageOnTop = true;
//                }

                if (titleAlignmentCB.isChecked()) {
                    hideFaceCaptureTitle = true;
                } else {
                    hideFaceCaptureTitle = false;
                }

                if (hintMsgAlignmentCB.isChecked()) {
                    hideFaceCaptureHintMsg = true;
                } else {
                    hideFaceCaptureHintMsg = false;
                }

                if (hintIconAlignmentCB.isChecked()) {
                    hideFaceCaptureHintIcon = true;
                } else {
                    hideFaceCaptureHintIcon = false;
                }
                if (titleImageAlignmentCB.isChecked()) {
                    hideFaceCaptureTitleImg = true;
                } else {
                    hideFaceCaptureTitleImg = false;
                }

//                ImageProcessingSDK.getInstance().imageProcessingSDK.customizeUserInterface(faceTitleOnTop, hideFaceCaptureTitle,
//                        faceHintMsgOnTop, hideFaceCaptureHintMsg,
//                        faceHintIconOnTop, hideFaceCaptureHintIcon,
//                        faceTitleImageOnTop, hideFaceCaptureTitleImg,
//                        idTitle, idTitleHide,
//                        idHint, idHintHide,
//                        idTitleImage, idTitleImageHide, ImageProcessingSDK.getInstance());
//
//                //API call for add label
//                ImageProcessingSDK.getInstance().initializeLabels(englishLabelMap, spanishLabelMap);

                showErrorMessage(view, getString(R.string.id_capture_save_msg));
                bottomSheetBehaviorSelfie.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
                selfieCaptureAPICall();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set Face Image Type
              //  String faceImageType = ((FaceImageType) faceImageTypeSpinner.getSelectedItem()).getFaceImageType().toString();
                String faceImageType = ((Pair) faceImageTypeSpinner.getSelectedItem()).second.toString();
                eVolvApp.setFaceImageType(faceImageType);
                eVolvApp.setEmpCode(empCodeEdtTxt.getText().toString());

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

    private void selfieCaptureAPICall() {
        ImageProcessingSDK.getInstance().setImageProcessingResponseListener(SelfieOverrideCapture.this);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showErrorMessage(getView(), "Need to enable CAMERA permission.");
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
            }
        } else {
            String glareThreshold = PreferenceUtils.getPreference(getActivity(), FACE_GLARE_THRESHOLD, "" + ImageProcessingSDK.dFaceGlareThreshold);
            String lightThreshold = PreferenceUtils.getPreference(getActivity(), FACE_LIGHT_THRESHOLD, "" + ImageProcessingSDK.dFaceLightThreshold);
            String focusThreshold = PreferenceUtils.getPreference(getActivity(), FACE_FOCUS_THRESHOLD, "" + ImageProcessingSDK.dFaceFocusThreshold);
            String detectionThreshold = PreferenceUtils.getPreference(getActivity(), FACE_DETECTION_THRESHOLD, "" + ImageProcessingSDK.dFaceDetectionThreshold);
            String maxImagesize = PreferenceUtils.getPreference(getActivity(), FACE_IMAGE_SIZE, "" + ImageProcessingSDK.dImageSizeForFace);
            String faceOutlineColor = PreferenceUtils.getPreference(getActivity(), FACE_OUTLINE_COLOR, "" + ImageProcessingSDK.dFaceOutlineColor);
            String faceOutlineColorAlpha = PreferenceUtils.getPreference(getActivity(), FACE_OUTLINE_COLOR_ALPHA, "" + ImageProcessingSDK.dAlpha);
            String detectedFaceOutlineColor = PreferenceUtils.getPreference(getActivity(), DETECTED_FACE_OUTLINE_COLOR, "" + ImageProcessingSDK.dDetectedFaceOutlineColor);
            String detectedFaceOutlineColorAlpha = PreferenceUtils.getPreference(getActivity(), DETECTED_FACE_OUTLINE_COLOR_ALPHA, "" + ImageProcessingSDK.dAlpha);
            String outsideFaceOutlineColor = PreferenceUtils.getPreference(getActivity(), OUTSIDE_FACE_OUTLINE_COLOR, "" + ImageProcessingSDK.dOutsideFaceOutlineColor);
            String outsideFaceOutlineColorAlpha = PreferenceUtils.getPreference(getActivity(), OUTSIDE_FACE_OUTLINE_COLOR_ALPHA, "" + ImageProcessingSDK.dAlpha);
            String detectedOutsideFaceOutlineColor = PreferenceUtils.getPreference(getActivity(), DETECTED_FACE_OUTSIDE_OUTLINE_COLOR, "" + ImageProcessingSDK.dDetectedOutsideFaceOutlineColor);
            String detectedOutsideFaceOutlineColorAlpha = PreferenceUtils.getPreference(getActivity(), DETECTED_FACE_OUTSIDE_OUTLINE_COLOR_ALPHA, "" + ImageProcessingSDK.dAlpha);
            String titleImageType = PreferenceUtils.getPreference(getActivity(), SELECT_FACE_TITLE_IMG, "None");
            boolean frontCamera = PreferenceUtils.getPreference(getActivity(), ENABLE_FRONT_CAMERA, ImageProcessingSDK.dFaceLaunchFrontCamera);
            boolean previewScreen = PreferenceUtils.getPreference(getActivity(), SHOW_PREVIEW_SCREEN, ImageProcessingSDK.dFaceShowPreviewScreen);
            boolean instructionScreen = PreferenceUtils.getPreference(getActivity(), SHOW_INSTRUCTION_SCREEN, ImageProcessingSDK.dFaceShowInstructionScreen);
            boolean toggleCamera = PreferenceUtils.getPreference(getActivity(), ENABLE_TOGGLE_CAMERA, ImageProcessingSDK.dFaceEnableToggleButton);
            String deltaLeft = PreferenceUtils.getPreference(getActivity(), DELTA_LEFT_THRESHOLD, "" + ImageProcessingSDK.deltaLeftThreshold);
            String deltaRight = PreferenceUtils.getPreference(getActivity(), DELTA_RIGHT_THRESHOLD, "" + ImageProcessingSDK.deltaRightThreshold);
            String deltaUp = PreferenceUtils.getPreference(getActivity(), DELTA_UP_THRESHOLD, "" + ImageProcessingSDK.deltaUpThreshold);
            String deltaDown = PreferenceUtils.getPreference(getActivity(), DELTA_DOWN_THRESHOLD, "" + ImageProcessingSDK.deltaDownThreshold);
            String textFontType =  PreferenceUtils.getPreference(getActivity(), TEXT_FONT_TYPE_FACE, "DEFAULT");
            String textFontStyle = PreferenceUtils.getPreference(getActivity(), TEXT_FONT_STYLE_FACE, "NORMAL");
            String backButtonColor = PreferenceUtils.getPreference(getActivity(), BACK_BUTTON_COLOR_FACE, ""+ ImageProcessingSDK.backButtonColor);
            String backButtonAlpha = PreferenceUtils.getPreference(getActivity(), BACK_BUTTON_ALPHA_FACE, ""+ ImageProcessingSDK.dAlpha);
            String retryButtonColor = PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_COLOR_FACE, ""+ ImageProcessingSDK.retryButtonColor);
            String retryButtonAlpha = PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_ALPHA_FACE, ""+ ImageProcessingSDK.dAlpha);
            String retryButtonBorderColor = PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_BORDER_COLOR_FACE, ""+ ImageProcessingSDK.retryButtonBorderColor);
            String retryButtonBorderAlpha = PreferenceUtils.getPreference(getActivity(), RETRY_BUTTON_BORDER_ALPHA_FACE, ""+ ImageProcessingSDK.dAlpha);
            String confirmButtonColor = PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_COLOR_FACE, ""+ ImageProcessingSDK.confirmButtonColor);
            String confirmButtonAlpha = PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_ALPHA_FACE, ""+ ImageProcessingSDK.dAlpha);
            String confirmButtonStyle = PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_STYLE_FACE, ""+ ImageProcessingSDK.confirmButtonStyle);
            String confirmButtonStyleAlpha = PreferenceUtils.getPreference(getActivity(), CONFIRM_BUTTON_STYLE_ALPHA_FACE, ""+ ImageProcessingSDK.dAlpha);
            String textLabelColor = PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_COLOR_FACE, ""+ ImageProcessingSDK.textLabelColor);
            String textLabelAlpha = PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_ALPHA_FACE,""+ ImageProcessingSDK.dAlpha);
            String instrBtnColor = PreferenceUtils.getPreference(getActivity(), FACE_INSTRUCTION_BTN_COLOR, ""+ ImageProcessingSDK.instructionContinueButtonColor);
            String instrBtnAlpha = PreferenceUtils.getPreference(getActivity(), FACE_INSTRUCTION_BTN_ALPHA,""+ ImageProcessingSDK.dAlpha);
            String instrBtnTxtColor = PreferenceUtils.getPreference(getActivity(), FACE_INSTRUCTION_BTN_TXT_COLOR, ""+ ImageProcessingSDK.instructionContinueButtonTextColor);
            String instrBtnTxtAlpha = PreferenceUtils.getPreference(getActivity(), FACE_INSTRUCTION_BTN_TXT_ALPHA,""+ ImageProcessingSDK.dAlpha);
            String faceContours = PreferenceUtils.getPreference(getActivity(), SELECT_FACE_CONTOURS, ImageProcessingSDK.dFaceContour);
            String headerTextColorEdtTxt = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_LABEL_COLOR, ""+ ImageProcessingSDK.textLabelColor);
            String headerTextAlphaEdtTxt = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_LABEL_ALPHA, ""+ ImageProcessingSDK.dAlpha);
            String headerTextSizeEdtTxt = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_LABEL_FONT_SIZE, "");
            String textSizeEdtTxt = PreferenceUtils.getPreference(getActivity(), TEXT_LABEL_FONT_SIZE, "");
            String headerTextFontType = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_FONT_TYPE, ""+ ImageProcessingSDK.typeFaceType);
            String headerTextFontStyle = PreferenceUtils.getPreference(getActivity(), HEADER_TEXT_FONT_STYLE, ""+ ImageProcessingSDK.typeFaceStyle);
            String faceTitleAlignment = PreferenceUtils.getPreference(getActivity(), FACE_TITLE, "Bottom");
            String faceHintAlignment = PreferenceUtils.getPreference(getActivity(), FACE_HINT, "Top");

            Bitmap titleBitmap = null;
            if (!titleImageType.equalsIgnoreCase("None")) {
                titleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title_image);
            }

            int deltaLeftVal = (!StringUtil.isEmpty(deltaLeft) ? Integer.parseInt(deltaLeft) : ImageProcessingSDK.deltaLeftThreshold);
            int deltaRightVal = (!StringUtil.isEmpty(deltaRight) ? Integer.parseInt(deltaRight) : ImageProcessingSDK.deltaRightThreshold);
            int deltaUpVal = (!StringUtil.isEmpty(deltaUp) ? Integer.parseInt(deltaUp) : ImageProcessingSDK.deltaUpThreshold);
            int deltaDownVal = (!StringUtil.isEmpty(deltaDown) ? Integer.parseInt(deltaDown) : ImageProcessingSDK.deltaDownThreshold);
            int glareThresholdVal = (!StringUtil.isEmpty(glareThreshold) ? Integer.parseInt(glareThreshold) : ImageProcessingSDK.dFaceGlareThreshold);
            int lightThresholdVal = (!StringUtil.isEmpty(lightThreshold) ? Integer.parseInt(lightThreshold) : ImageProcessingSDK.dFaceLightThreshold);
            int focusThresholdVal = (!StringUtil.isEmpty(focusThreshold) ? Integer.parseInt(focusThreshold) : ImageProcessingSDK.dFaceFocusThreshold);
            int detectionThresholdVal = (!StringUtil.isEmpty(detectionThreshold) ? Integer.parseInt(detectionThreshold) : ImageProcessingSDK.dFaceDetectionThreshold);
            int maxImageSize = (!StringUtil.isEmpty(maxImagesize) ? Integer.parseInt(maxImagesize) : ImageProcessingSDK.dFaceDetectionThreshold);
            faceOutlineColor = (!StringUtil.isEmpty(faceOutlineColor) ? faceOutlineColor : ImageProcessingSDK.dFaceOutlineColor);
            detectedFaceOutlineColor = (!StringUtil.isEmpty(detectedFaceOutlineColor) ? detectedFaceOutlineColor : ImageProcessingSDK.dDetectedFaceOutlineColor);
            outsideFaceOutlineColor = (!StringUtil.isEmpty(outsideFaceOutlineColor) ? outsideFaceOutlineColor : ImageProcessingSDK.dOutsideFaceOutlineColor);
            detectedOutsideFaceOutlineColor = (!StringUtil.isEmpty(detectedOutsideFaceOutlineColor) ? detectedOutsideFaceOutlineColor : ImageProcessingSDK.dDetectedOutsideFaceOutlineColor);
            int faceOutlineAlpha = (!StringUtil.isEmpty(faceOutlineColorAlpha) ? Integer.parseInt(faceOutlineColorAlpha) : ImageProcessingSDK.dAlpha);
            int detectedFaceOutlineAlpha = (!StringUtil.isEmpty(detectedFaceOutlineColorAlpha) ? Integer.parseInt(detectedFaceOutlineColorAlpha) : ImageProcessingSDK.dAlpha);
            int outsideFaceOutlineAlpha = (!StringUtil.isEmpty(outsideFaceOutlineColorAlpha) ? Integer.parseInt(outsideFaceOutlineColorAlpha) : ImageProcessingSDK.dAlpha);
            int detectedOutsideFaceOutlineAlpha = (!StringUtil.isEmpty(detectedOutsideFaceOutlineColorAlpha) ? Integer.parseInt(detectedOutsideFaceOutlineColorAlpha) : ImageProcessingSDK.dAlpha);
            int textLabelColorAlpha = (!StringUtil.isEmpty(textLabelAlpha) ? Integer.parseInt(textLabelAlpha) : ImageProcessingSDK.dAlpha);

//                    ImageProcessingSDK.getInstance().detectFace(getActivity(), lightThresholdVal, focusThresholdVal, detectionThresholdVal, maxImageSize, frontCamera, previewScreen,
//                            new ColorCode(faceOutlineColor, faceOutlineAlpha),
//                            new ColorCode(detectedFaceOutlineColor, detectedFaceOutlineAlpha),
//                            new ColorCode(outsideFaceOutlineColor, outsideFaceOutlineAlpha),
//                            new ColorCode(detectedOutsideFaceOutlineColor, detectedOutsideFaceOutlineAlpha),
//                            titleBitmap, instructionScreen, R.mipmap.face_combine_instruction, toggleCamera, deltaLeftVal, deltaRightVal, deltaUpVal, deltaDownVal, glareThresholdVal);
//                    ImageProcessingSDK.getInstance().detectFace(getActivity(), lightThresholdVal, focusThresholdVal, detectionThresholdVal, maxImageSize, frontCamera, previewScreen,
//                            new ColorCode(faceOutlineColor, faceOutlineAlpha),
//                            new ColorCode(detectedFaceOutlineColor, detectedFaceOutlineAlpha),
//                            new ColorCode(outsideFaceOutlineColor, outsideFaceOutlineAlpha),
//                            new ColorCode(detectedOutsideFaceOutlineColor, detectedOutsideFaceOutlineAlpha),
//                            titleBitmap, instructionScreen, R.mipmap.face_combine_instruction, toggleCamera);

            try {

                ColorCode faceOutlineHexColorCode = new ColorCode(faceOutlineColor, faceOutlineAlpha);
                ColorCode detectedFaceOutlineHexColorCode = new ColorCode(detectedFaceOutlineColor, detectedFaceOutlineAlpha);
                ColorCode outsideFaceOutlineColorCode = new ColorCode(outsideFaceOutlineColor, outsideFaceOutlineAlpha);
                ColorCode detectedFaceOutsideColorCode = new ColorCode(detectedOutsideFaceOutlineColor, detectedOutsideFaceOutlineAlpha);

                JSONObject commonUIObject = new JSONObject();
                commonUIObject.put(UIConfigurationParameters.FD_LIGHT_THRESHOLD,lightThresholdVal+"");
                commonUIObject.put(UIConfigurationParameters.FD_FOCUS_THRESHOLD, focusThresholdVal+"");
                commonUIObject.put(UIConfigurationParameters.FD_DETECTION_THRESHOLD, detectionThresholdVal+"");
                commonUIObject.put(UIConfigurationParameters.FD_MAX_IMAGE_SIZE, maxImageSize+"");
                commonUIObject.put(UIConfigurationParameters.FD_LAUNCH_FRONT_CAMERA, frontCamera == true ? "Y" : "N");
                //commonUIObject.put(UIConfigurationParameters.FD_SHOW_PREVIEW_SCREEN, previewScreen == true ? "Y" : "N");
                //commonUIObject.put(UIConfigurationParameters.FD_SHOW_INSTRUCTION_SCREEN, instructionScreen == true ? "Y" : "N");
                commonUIObject.put(UIConfigurationParameters.FD_TOGGLE_CAMERA, toggleCamera == true ? "Y" : "N");

//                commonUIObject.put(UIConfigurationParameters.FACE_OUTLINE_HEX_COLOR, faceOutlineHexColorCode.getHexColorCode()+"");
//                commonUIObject.put(UIConfigurationParameters.FACE_OUTLINE_HEX_COLOR_ALPHA, faceOutlineHexColorCode.getTransparency()+"");
//                commonUIObject.put(UIConfigurationParameters.DETECTED_FACE_OUTLINE_HEX_COLOR, detectedFaceOutlineHexColorCode.getHexColorCode()+"");
//                commonUIObject.put(UIConfigurationParameters.DETECTED_FACE_OUTLINE_HEX_COLOR_ALPHA, detectedFaceOutlineHexColorCode.getTransparency()+"");
//                commonUIObject.put(UIConfigurationParameters.OUTSIDE_FACE_OUTLINE_COLOR, outsideFaceOutlineColorCode.getHexColorCode()+"");
//                commonUIObject.put(UIConfigurationParameters.OUTSIDE_FACE_OUTLINE_COLOR_ALPHA, outsideFaceOutlineColorCode.getTransparency()+"");
//                commonUIObject.put(UIConfigurationParameters.DETECTED_FACE_OUTSIDE_COLOR, detectedFaceOutsideColorCode.getHexColorCode()+"");
//                commonUIObject.put(UIConfigurationParameters.DETECTED_FACE_OUTSIDE_COLOR_ALPHA, detectedFaceOutsideColorCode.getTransparency()+"");
//                commonUIObject.put(UIConfigurationParameters.TYPEFACE_TYPE,textFontType);
//              //  commonUIObject.put(UIConfigurationParameters.TYPEFACE_STYLE,Arrays.asList(TEXT_FONT_STYLE_ARRAY).indexOf(textFontStyle)+"");
//                commonUIObject.put(UIConfigurationParameters.TYPEFACE_STYLE,textFontStyle);
//                commonUIObject.put(UIConfigurationParameters.BACK_BUTTON_COLOR,backButtonColor);//
//                commonUIObject.put(UIConfigurationParameters.BACK_BUTTON_COLOR_ALPHA, backButtonAlpha);
//                commonUIObject.put(UIConfigurationParameters.RETRY_BUTTON_COLOR,retryButtonColor);
//                commonUIObject.put(UIConfigurationParameters.RETRY_BUTTON_COLOR_ALPHA,retryButtonAlpha);
//                commonUIObject.put(UIConfigurationParameters.RETRY_BUTTON_BORDER_COLOR,retryButtonBorderColor);
//                commonUIObject.put(UIConfigurationParameters.RETRY_BUTTON_BORDER_COLOR_ALPHA,retryButtonBorderAlpha);
//                commonUIObject.put(UIConfigurationParameters.CONFIRM_BUTTON_COLOR,confirmButtonColor);
//                commonUIObject.put(UIConfigurationParameters.CONFIRM_BUTTON_COLOR_ALPHA,confirmButtonAlpha);
//                commonUIObject.put(UIConfigurationParameters.CONFIRM_BUTTON_STYLE,confirmButtonStyle);
//                commonUIObject.put(UIConfigurationParameters.CONFIRM_BUTTON_STYLE_ALPHA,confirmButtonStyleAlpha);
//                commonUIObject.put(UIConfigurationParameters.TEXT_LABEL_COLOR, textLabelColor);
//                commonUIObject.put(UIConfigurationParameters.TEXT_LABEL_ALPHA, textLabelColorAlpha+"");
//                commonUIObject.put(UIConfigurationParameters.INSTRUCTIN_BUTTON_COLOR, instrBtnColor+"");
//                commonUIObject.put(UIConfigurationParameters.INSTRUCTIN_BUTTON_ALPHA, instrBtnAlpha+"");
//                commonUIObject.put(UIConfigurationParameters.INSTRUCTIN_BUTTON_TXT_COLOR, instrBtnTxtColor+"");
//                commonUIObject.put(UIConfigurationParameters.INSTRUCTIN_BUTTON_TXT_ALPHA, instrBtnTxtAlpha+"");
//                commonUIObject.put(UIConfigurationParameters.FACE_CONTOURS, faceContours);
//                commonUIObject.put(UIConfigurationParameters.HEADER_TEXT_LABEL_COLOR, headerTextColorEdtTxt);
//                commonUIObject.put(UIConfigurationParameters.HEADER_TEXT_LABEL_ALPHA, headerTextAlphaEdtTxt+"");
//                commonUIObject.put(UIConfigurationParameters.HEADER_TYPEFACE_TYPE, headerTextFontType);
//                commonUIObject.put(UIConfigurationParameters.HEADER_TYPEFACE_STYLE, headerTextFontStyle);
//                commonUIObject.put(UIConfigurationParameters.HEADER_TEXT_LABEL_SIZE, headerTextSizeEdtTxt);
//                commonUIObject.put(UIConfigurationParameters.TEXT_LABEL_SIZE, textSizeEdtTxt);
//                commonUIObject.put(UIConfigurationParameters.FACE_TITLE_ON_TOP, (faceTitleAlignment.equals("Top") ? "Y" : "N"));
//                commonUIObject.put(UIConfigurationParameters.FACE_HINT_MESSAGE_ON_TOP, (faceHintAlignment.equals("Top") ? "Y" : "N"));

                //Type =  DEFAULT_BOLD,SANS_SERIF,SERIF,MONOSPACE;
                //Style = NORMAL = 0; BOLD = 1; ITALIC = 2;BOLD_ITALIC = 3;
//                ImageProcessingSDK.getInstance().detectFace(getActivity(),titleBitmap, 0,  commonUIObject);
                ImageProcessingSDK.getInstance().detectFace(getActivity(), commonUIObject);

            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
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
        Log.d("SDK", "CALLBACK:::: onFaceDetectionResultAvailable");
        if (null != response) {
            if (response.getStatusCode() == ResponseStatusCode.PERMISSION_NOT_GRANTED.getStatusCode()) {
                showErrorMessage(getView(), getString(R.string.permission_not_granted));
            } else {
                if (null != resultMap) {
                    if (resultMap.containsKey(FaceImageType.FACE.toString())) {
                        String faceImageBase64 = resultMap.get(FaceImageType.FACE.toString());
                        if (!StringUtil.isEmpty(faceImageBase64)) {
                            byte[] decodedString = Base64.decode(faceImageBase64, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            BitmapUtils.writeBitmapToFilepath(eVolvApp.getBaseDirectoryPath() + Constants.FACE_IMAGE_OVERRIDE, bitmap);
                            //setImage(bitmap); //Set this image as first image
                            isCapture = false;
                        }
                    }
                    if (resultMap.containsKey(FaceImageType.PROCESSED_FACE.toString())) {
                        String processedFaceImageBase64 = resultMap.get(FaceImageType.PROCESSED_FACE.toString());
                        if (!StringUtil.isEmpty(processedFaceImageBase64)) {
                            byte[] decodedString = Base64.decode(processedFaceImageBase64, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            BitmapUtils.writeBitmapToFilepath(eVolvApp.getBaseDirectoryPath() + Constants.PROCESSED_FACE_IMAGE_OVERRIDE, bitmap);
                        }
                    }
                    if (resultMap.containsKey(FaceImageType.OVAL_FACE.toString())) {
                        String ovalFaceImageBase64 = resultMap.get(FaceImageType.OVAL_FACE.toString());
                        if (!StringUtil.isEmpty(ovalFaceImageBase64)) {
                            byte[] decodedString = Base64.decode(ovalFaceImageBase64, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            BitmapUtils.writeBitmapToFilepath(eVolvApp.getBaseDirectoryPath() + Constants.OVAL_FACE_IMAGE_OVERRIDE, bitmap);
                        }
                    }
                }
            }
        }
    }

    private void setUpView() {
        adapter = new SelfieOverridePageAdapter(getActivity(), getChildFragmentManager());
        viewPagerSelfie.setAdapter(adapter);
        viewPagerSelfie.setCurrentItem(0);
        imageSelfieFace.setImageResource(R.drawable.fill_circle);
    }

    private void setTab() {
        viewPagerSelfie.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {
                imageSelfieFace.setImageResource(R.drawable.holo_circle);
                imageProcessedFace.setImageResource(R.drawable.holo_circle);
                imageOvalFace.setImageResource(R.drawable.holo_circle);
                btnAction(position);
            }

        });

    }

    private void btnAction(int action) {
        switch (action) {
            case 0:
                imageSelfieFace.setImageResource(R.drawable.fill_circle);
                break;
            case 1:
                imageProcessedFace.setImageResource(R.drawable.fill_circle);
                break;
            case 2:
                imageOvalFace.setImageResource(R.drawable.fill_circle);
                break;
        }
    }

    private void onCircleButtonClick() {

        imageSelfieFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelfieFace.setImageResource(R.drawable.fill_circle);
                viewPagerSelfie.setCurrentItem(0);
            }
        });

        imageProcessedFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageProcessedFace.setImageResource(R.drawable.fill_circle);
                viewPagerSelfie.setCurrentItem(1);
            }
        });
        imageOvalFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageProcessedFace.setImageResource(R.drawable.fill_circle);
                viewPagerSelfie.setCurrentItem(2);
            }
        });
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
        PreferenceUtils.setPreference(getActivity(), FACE_TITLE, faceTitleAlignment);
        PreferenceUtils.setPreference(getActivity(), FACE_HINT, faceHintMsgAlignment);
        PreferenceUtils.setPreference(getActivity(), FACE_ICON, faceHintIconAlignment);
        PreferenceUtils.setPreference(getActivity(), FACE_TITLE_IMAGE, faceTitleImageAlignment);
        PreferenceUtils.setPreference(getActivity(), FACE_TITLE_HIDE, hideFaceCaptureTitle);
        PreferenceUtils.setPreference(getActivity(), FACE_HINT_HIDE, hideFaceCaptureHintMsg);
        PreferenceUtils.setPreference(getActivity(), FACE_ICON_HIDE, hideFaceCaptureHintIcon);
        PreferenceUtils.setPreference(getActivity(), FACE_TITLE_IMAGE_HIDE, hideFaceCaptureTitleImg);
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
}
