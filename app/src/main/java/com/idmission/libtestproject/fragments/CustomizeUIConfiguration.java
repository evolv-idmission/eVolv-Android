package com.idmission.libtestproject.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.idmission.client.ImageProcessingSDK;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.NavigationActivity;
import com.idmission.libtestproject.adapter.SpinnerAdapterForPair;
import com.idmission.libtestproject.classes.CustomizeUIConfig;
import com.idmission.libtestproject.classes.CustomizeUIConfigManager;
import com.idmission.libtestproject.classes.UIConfigConstants;
import com.idmission.libtestproject.utils.BitmapUtils;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CustomizeUIConfiguration extends Fragment {
    private Spinner spinnerUIConfig, spinnerTitle, spinnerFaceTitle ,spinnerTitleAlignment, spinnerHintAlignment, spinnerTitleImageAlignment, spinnerLabel, spinnerTextFontType, spinnerTextFontStyle, spinnerHeaderTextFontType, spinnerHeaderTextFontStyle,
            spinnerIDCaptureBorder;
    private View idCaptureView, selfieCaptureView, fingerprintCaptureView, voiceRecordingView;
    private Button backButton, nextButton, addLabelButton, addFaceLabelButton, resetButton;
    public static ArrayList<String> skipFeaturesList = new ArrayList<>();

    //Id Capture
    private EditText idOutlineColorEdtTxt, idOutlineAlphaEdtTxt, detectedIdOutlineColorEdtTxt, detectedIdOutlineAlphaEdtTxt, idOutsideOutlineColorEdtTxt,
            idOutsideOutlineAlphaEdtTxt, detectedOutsideOutlineColorEdtTxt, detectedOutsideOutlineAlphaEdtTxt, backButtonColorEdtTxt, retryButtonColorEdtTxt,
            confirmButtonColorEdtTxt, backButtonAlphaEdtTxt, retryButtonAlphaEdtTxt, confirmButtonAlphaEdtTxt, textLabelColorEdtTxt, textLabelAlphaEdtTxt,
            instructionContinueBtnColorEdtTxt, instructionContinueBtnAlphaEdtTxt, instructionContinueBtnTxtColorEdtTxt, instructionContinueBtnTxtAlphaEdtTxt,
            retryButtonBorderColorEdtTxt, confirmButtonStyleEdtTxt, retryButtonBorderAlphaEdtTxt, confirmButtonStyleAlphaEdtTxt, headerTextLabelColorEdtTxt,
            headerTextLabelAlphaEdtTxt, headerTextLabelSizeEdtTxt, textLabelSizeEdtTxt, labelValueEdtTxt, labelValueFaceEdtTxt,  captureButtonColorEdtTxt, captureButtonColorAlphaEdtTxt;
    private CheckBox titleAlignmentCB, hintAlignmentCB, titleImageAlignmentCB;
    private ArrayList<Pair<String, String>> textFontTypeList, textFontStyleList, headerTextFontTypeList, headerTextFontStyleList,
            titleAlignmentList, hintAlignmentList, titleImageAlignmentList, idCaptureBorderList;

    //selfie capture
    private EditText faceOutlineColorEdtTxt, detectedFaceOutlineColorEdtTxt, outsideFaceOutlineColorEdtTxt,
            faceOutlineColorAlphaEdtTxt, detectedFaceOutlineColorAlphaEdtTxt, outsideFaceOutlineColorAlphaEdtTxt, outsideDetectedFaceOutlineColor,
            outsideDetectedFaceOutlineColorAlpha, faceBackButtonColorEdtTxt, faceRetryButtonColorEdtTxt, faceConfirmButtonColorEdtTxt, faceRetryButtonBorderAlphaEdtTxt,
            faceConfirmButtonStyleAlphaEdtTxt, faceBackButtonAlphaEdtTxt, faceRetryButtonAlphaEdtTxt, faceConfirmButtonAlphaEdtTxt, faceRetryButtonBorderColorEdtTxt,
            faceConfirmButtonStyleEdtTxt, faceTextLabelColorEdtTxt, faceTextLabelAlphaEdtTxt, faceInstructionContinueBtnColorEdtTxt, faceInstructionContinueBtnAlphaEdtTxt,
            faceInstructionContinueBtnTxtColorEdtTxt, faceInstructionContinueBtnTxtAlphaEdtTxt, faceHeaderTextLabelColorEdtTxt, faceHeaderTextLabelAlphaEdtTxt, faceHeaderTextLabelSizeEdtTxt, faceTextLabelSizeEdtTxt, faceInstructionPreviewBackgroundEdtTxt, faceInstructionPreviewBackgroundAlphaEdtTxt;
    private Spinner faceSpinnerTextFontType, faceSpinnerTextFontStyle, faceSpinnerFaceContours, faceSpinnerHeaderTextFontType,
            faceSpinnerHeaderTextFontStyle, faceSpinnerTitleAlignment, faceSpinnerHintMsgAlignment, faceSpinnerHintIconAlignment,
            faceSpinnerTitleImageAlignment, faceSpinnerLabel;
    private CheckBox faceTitleAlignmentCB, faceHintMsgAlignmentCB, faceHintIconAlignmentCB, faceTitleImageAlignmentCB, showCustomOverlay;
    private ArrayList<Pair<String, String>> faceTextFontTypeList, faceTextFontStyleList, faceHeaderTextFontTypeList, facehHeaderTextFontStyleList, faceTitleAlignmentList, faceHintMsgAlignmentList,
            faceHintIconAlignmentList, faceTitleImageAlignmentList, faceContoursList;

    //4F Capture
    private EditText fpTextLabelColorEdtTxt, fpTextLabelAlphaEdtTxt, fpInstructionContinueBtnColorEdtTxt, fpInstructionContinueBtnAlphaEdtTxt,
            fpInstructionContinueBtnTxtColorEdtTxt, fpInstructionContinueBtnTxtAlphaEdtTxt;
    private String previously_selected_feature;
    private RadioGroup radioGroupInstruction;
    private SwitchCompat showPreviewScreenCheckBox, showInstructionFaceDetect, enableLabelShadowSwitch, faceEnableLabelShadowSwitch;

    //Voice Recording
    private EditText voiceButtonColorEdtTxt, voiceButtonColorAlphaEdtTxt, voiceBackgroundColorEdtTxt, voiceBackgroundColorAlphaEdtTxt, voiceTextColorEdtTxt, voiceTextColorAlphaEdtTxt,
            voiceTitleColorEdtTxt, voiceTitleColorAlphaEdtTxt, voiceTitleLabelSizeEdtTxt, voiceTextLabelSizeEdtTxt, voiceCounterLabelSizeEdtTxt, voiceLabelValueEdtTxt;
    private Spinner voiceDisplayPositionSpinner, voiceLabelSpinner;
    private Button voiceAddLabelButton;
    private SwitchCompat voiceTitleOnTopSwitch, voiceAutoPlaySwitch;
    private ArrayList<Pair<String, String>> voiceDisplayPositionList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.customize_ui_configuration, container, false);

        skipFeaturesList = (ArrayList<String>) getArguments().getSerializable(IDDetails.SKIP_FEATURES_LIST);

        spinnerUIConfig = (Spinner) view.findViewById(R.id.spinner_configuration);
        backButton = (Button) view.findViewById(R.id.button_back);
        nextButton = (Button) view.findViewById(R.id.button_next);
        resetButton= (Button) view.findViewById(R.id.button_reset);
        idCaptureView = view.findViewById(R.id.custom_id_capture_lay);
        selfieCaptureView = view.findViewById(R.id.custom_selfie_capture_lay);
        fingerprintCaptureView = view.findViewById(R.id.custom_4f_fingerprint_capture_lay);
        voiceRecordingView = view.findViewById(R.id.custom_voice_recording_lay);

        initializedIdCapture();
        initializedSelfieCapture();
        initializedFPCapture();
        initializedVoiceRecording();
        setIDCaptureSpinnerAdapter();
        setSelfieCaptureSpinnerAdapter();
        setVoiceRecordingSpinnerAdapter();
        setIDCaptureDefaultValues();

        return view;
    }

    private void initializedIdCapture() {
        idOutlineColorEdtTxt = (EditText) idCaptureView.findViewById(R.id.id_outline_color);
        idOutlineAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_id_outline_color);
        detectedIdOutlineColorEdtTxt = (EditText) idCaptureView.findViewById(R.id.detected_id_outline_color);
        detectedIdOutlineAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_detected_id_outline_color);
        idOutsideOutlineColorEdtTxt = (EditText) idCaptureView.findViewById(R.id.color_outside_outline);
        idOutsideOutlineAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_color_outside_outline);
        detectedOutsideOutlineColorEdtTxt = (EditText) idCaptureView.findViewById(R.id.detected_color_outside_outline);
        detectedOutsideOutlineAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_detected_color_outside_outline);
        backButtonColorEdtTxt = (EditText) idCaptureView.findViewById(R.id.back_button_color);
        retryButtonColorEdtTxt = (EditText) idCaptureView.findViewById(R.id.retry_button_color);
        confirmButtonColorEdtTxt = (EditText) idCaptureView.findViewById(R.id.confirm_button_color);
        retryButtonBorderColorEdtTxt = (EditText) idCaptureView.findViewById(R.id.retry_button_border_color);
        confirmButtonStyleEdtTxt = (EditText) idCaptureView.findViewById(R.id.confirm_button_style);
        backButtonAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_back_button_color);
        retryButtonAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_retry_button_color);
        confirmButtonAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_confirm_button_color);
        retryButtonBorderAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_retry_button_border_color);
        confirmButtonStyleAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_confirm_button_style);
        textLabelColorEdtTxt = (EditText) idCaptureView.findViewById(R.id.text_label_color);
        textLabelAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_text_label_color);
        instructionContinueBtnColorEdtTxt = (EditText) idCaptureView.findViewById(R.id.instruction_continue_button_color);
        instructionContinueBtnAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_instruction_continue_button);
        instructionContinueBtnTxtColorEdtTxt = (EditText) idCaptureView.findViewById(R.id.instruction_continue_button_text_color);
        instructionContinueBtnTxtAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_instruction_continue_button_text);
        headerTextLabelColorEdtTxt = (EditText) idCaptureView.findViewById(R.id.text_header_label_color);
        headerTextLabelAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_text_header_label_color);
        headerTextLabelSizeEdtTxt = (EditText) idCaptureView.findViewById(R.id.Header_text_size);
        textLabelSizeEdtTxt = (EditText) idCaptureView.findViewById(R.id.text_size);
        labelValueEdtTxt = (EditText) idCaptureView.findViewById(R.id.label_value);
        spinnerTextFontType = (Spinner) idCaptureView.findViewById(R.id.text_font_type_spinner);
        spinnerTextFontStyle = (Spinner) idCaptureView.findViewById(R.id.text_font_style_spinner);
        spinnerTitle = (Spinner) idCaptureView.findViewById(R.id.spinner_title);
        spinnerTitleAlignment = (Spinner) idCaptureView.findViewById(R.id.spinner_title_alignment);
        spinnerHintAlignment = (Spinner) idCaptureView.findViewById(R.id.spinner_hint_alignment);
        spinnerTitleImageAlignment = (Spinner) idCaptureView.findViewById(R.id.spinner_title_image_alignment);
        spinnerHeaderTextFontType = (Spinner) idCaptureView.findViewById(R.id.text_header_font_type_spinner);
        spinnerHeaderTextFontStyle = (Spinner) idCaptureView.findViewById(R.id.text_header_font_style_spinner);
        spinnerLabel = (Spinner) idCaptureView.findViewById(R.id.spinner_label_type);
        spinnerIDCaptureBorder = (Spinner) idCaptureView.findViewById(R.id.spinner_id_capture_border);
        titleAlignmentCB = (CheckBox) idCaptureView.findViewById(R.id.checkbox_title_alignment);
        hintAlignmentCB = (CheckBox) idCaptureView.findViewById(R.id.checkbox_hint_alignment);
        titleImageAlignmentCB = (CheckBox) idCaptureView.findViewById(R.id.checkbox_title_image_alignment);
        addLabelButton = (Button) idCaptureView.findViewById(R.id.add_label_button);
        radioGroupInstruction = (RadioGroup) idCaptureView.findViewById(R.id.radio_group_instruction);
        enableLabelShadowSwitch = (SwitchCompat) idCaptureView.findViewById(R.id.enable_shade);
        captureButtonColorEdtTxt = (EditText) idCaptureView.findViewById(R.id.capture_button_text_color);
        captureButtonColorAlphaEdtTxt = (EditText) idCaptureView.findViewById(R.id.transparency_capture_button_text);
    }

    private void initializedSelfieCapture() {
        faceOutlineColorEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.face_outline_normal);
        faceOutlineColorAlphaEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.face_outline_normal_alpha);
        detectedFaceOutlineColorEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.face_outline_detected);
        detectedFaceOutlineColorAlphaEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.face_outline_detected_alpha);
        outsideFaceOutlineColorEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.outside_face_outline_color);
        outsideFaceOutlineColorAlphaEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.outside_face_outline_color_alpha);
        outsideDetectedFaceOutlineColor = (EditText) selfieCaptureView.findViewById(R.id.outside_detected_face_outline_color);
        outsideDetectedFaceOutlineColorAlpha = (EditText) selfieCaptureView.findViewById(R.id.outside_detected_face_outline_color_alpha);
        faceBackButtonColorEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.back_button_color);
        faceRetryButtonColorEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.retry_button_color);
        faceRetryButtonBorderColorEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.retry_button_border_color);
        faceRetryButtonBorderAlphaEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.transparency_retry_button_border_color);
        faceConfirmButtonColorEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.confirm_button_color);
        faceConfirmButtonStyleEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.confirm_button_style);
        faceConfirmButtonStyleAlphaEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.transparency_confirm_button_style);
        faceBackButtonAlphaEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.transparency_back_button_color);
        faceRetryButtonAlphaEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.transparency_retry_button_color);
        faceConfirmButtonAlphaEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.transparency_confirm_button_color);
        faceTextLabelColorEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.text_label_color);
        faceTextLabelAlphaEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.transparency_text_label_color);
        faceInstructionContinueBtnColorEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.instruction_continue_button_color);
        faceInstructionContinueBtnAlphaEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.transparency_instruction_continue_button);
        faceInstructionContinueBtnTxtColorEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.instruction_continue_button_text_color);
        faceInstructionContinueBtnTxtAlphaEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.transparency_instruction_continue_button_text);
        faceHeaderTextLabelColorEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.text_header_label_color);
        faceHeaderTextLabelAlphaEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.transparency_text_header_label_color);
        faceHeaderTextLabelSizeEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.Header_text_size);
        faceTextLabelSizeEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.text_size);
        faceInstructionPreviewBackgroundEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.instruction_preview_background_color);
        faceInstructionPreviewBackgroundAlphaEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.transparency_instruction_preview_background_color);

        spinnerFaceTitle = (Spinner) selfieCaptureView.findViewById(R.id.spinner_face_title);
        faceSpinnerTextFontType = (Spinner) selfieCaptureView.findViewById(R.id.text_font_type_spinner);
        faceSpinnerTextFontStyle = (Spinner) selfieCaptureView.findViewById(R.id.text_font_style_spinner);
        faceSpinnerFaceContours = (Spinner) selfieCaptureView.findViewById(R.id.spinner_face_contours);
        faceSpinnerHeaderTextFontType = (Spinner) selfieCaptureView.findViewById(R.id.text_header_font_type_spinner);
        faceSpinnerHeaderTextFontStyle = (Spinner) selfieCaptureView.findViewById(R.id.text_header_font_style_spinner);
        faceSpinnerTitleAlignment = (Spinner) selfieCaptureView.findViewById(R.id.spinner_face_title_alignment);
        faceSpinnerHintMsgAlignment = (Spinner) selfieCaptureView.findViewById(R.id.spinner_face_hint_msg_alignment);
        faceSpinnerHintIconAlignment = (Spinner) selfieCaptureView.findViewById(R.id.spinner_face_hint_icon_alignment);
        faceSpinnerTitleImageAlignment = (Spinner) selfieCaptureView.findViewById(R.id.spinner_face_title_image_alignment);
        //faceImageTypeSpinner = (Spinner) selfieCaptureView.findViewById(R.id.faceImageType_ET);
        faceSpinnerLabel = (Spinner) selfieCaptureView.findViewById(R.id.spinner_label_type);

        showPreviewScreenCheckBox = (SwitchCompat) selfieCaptureView.findViewById(R.id.showPreviewScreenCheckBox);
        showInstructionFaceDetect = (SwitchCompat) selfieCaptureView.findViewById(R.id.show_instruction_face_detect);

        faceTitleAlignmentCB = (CheckBox) selfieCaptureView.findViewById(R.id.checkbox_face_title_alignment);
        faceHintMsgAlignmentCB = (CheckBox) selfieCaptureView.findViewById(R.id.checkbox_face_hint_msg_alignment);
        faceHintIconAlignmentCB = (CheckBox) selfieCaptureView.findViewById(R.id.checkbox_face_hint_icon_alignment);
        faceTitleImageAlignmentCB = (CheckBox) selfieCaptureView.findViewById(R.id.checkbox_face_title_image_alignment);
        showCustomOverlay = (CheckBox) selfieCaptureView.findViewById(R.id.checkbox_show_custom_ui);
        labelValueFaceEdtTxt = (EditText) selfieCaptureView.findViewById(R.id.face_label_value);
        addFaceLabelButton = (Button) selfieCaptureView.findViewById(R.id.face_add_label_button);
        faceEnableLabelShadowSwitch = (SwitchCompat) selfieCaptureView.findViewById(R.id.fd_enable_shade);
    }

    private void initializedFPCapture() {
        fpTextLabelColorEdtTxt = (EditText) fingerprintCaptureView.findViewById(R.id.text_label_color);
        fpTextLabelAlphaEdtTxt = (EditText) fingerprintCaptureView.findViewById(R.id.transparency_text_label_color);
        fpInstructionContinueBtnColorEdtTxt = (EditText) fingerprintCaptureView.findViewById(R.id.instruction_continue_button_color);
        fpInstructionContinueBtnAlphaEdtTxt = (EditText) fingerprintCaptureView.findViewById(R.id.transparency_instruction_continue_button);
        fpInstructionContinueBtnTxtColorEdtTxt = (EditText) fingerprintCaptureView.findViewById(R.id.instruction_continue_button_text_color);
        fpInstructionContinueBtnTxtAlphaEdtTxt = (EditText) fingerprintCaptureView.findViewById(R.id.transparency_instruction_continue_button_text);
    }

    private void initializedVoiceRecording() {
        voiceButtonColorEdtTxt = (EditText) voiceRecordingView.findViewById(R.id.voice_button_color);
        voiceButtonColorAlphaEdtTxt = (EditText) voiceRecordingView.findViewById(R.id.voice_button_color_alpha);
        voiceBackgroundColorEdtTxt = (EditText) voiceRecordingView.findViewById(R.id.voice_background_color);
        voiceBackgroundColorAlphaEdtTxt = (EditText) voiceRecordingView.findViewById(R.id.voice_background_color_alpha);
        voiceTextColorEdtTxt = (EditText) voiceRecordingView.findViewById(R.id.voice_text_label_color);
        voiceTextColorAlphaEdtTxt = (EditText) voiceRecordingView.findViewById(R.id.voice_text_label_color_alpha);
        voiceTitleColorEdtTxt = (EditText) voiceRecordingView.findViewById(R.id.voice_title_label_color);
        voiceTitleColorAlphaEdtTxt = (EditText) voiceRecordingView.findViewById(R.id.voice_title_label_color_alpha);
        voiceTitleLabelSizeEdtTxt = (EditText) voiceRecordingView.findViewById(R.id.voice_title_label_size);
        voiceTextLabelSizeEdtTxt = (EditText) voiceRecordingView.findViewById(R.id.voice_text_label_size);
        voiceCounterLabelSizeEdtTxt = (EditText) voiceRecordingView.findViewById(R.id.voice_counter_label_size);
        voiceLabelValueEdtTxt = (EditText) voiceRecordingView.findViewById(R.id.label_value);
        voiceAddLabelButton= (Button) voiceRecordingView.findViewById(R.id.voice_add_label_button);

        voiceDisplayPositionSpinner = (Spinner) voiceRecordingView.findViewById(R.id.voice_display_position_spinner);
        voiceLabelSpinner = (Spinner) voiceRecordingView.findViewById(R.id.spinner_label_type);

        voiceTitleOnTopSwitch = (SwitchCompat) voiceRecordingView.findViewById(R.id.voice_title_label_on_top_switch);
        voiceAutoPlaySwitch = (SwitchCompat) voiceRecordingView.findViewById(R.id.auto_play_switch);
    }

    private void setIDCaptureSpinnerAdapter() {
        ArrayList<Pair<String,String>> titleList = new ArrayList<Pair<String,String>>();
        titleList.add(new Pair<String, String>(getString(R.string.none),"None"));
        titleList.add(new Pair<String, String>(getString(R.string.title_image_1),"Title Image 1"));
        SpinnerAdapterForPair titleListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                titleList);
        spinnerTitle.setAdapter(titleListAdapter);

        ArrayList<Pair<String, String>> uiConfigList = new ArrayList<Pair<String, String>>();
        uiConfigList.add(new Pair<String, String>(getString(R.string.id_front_capture), CustomizeUIConfigManager.ID_CAPTURE_FRONT));
        uiConfigList.add(new Pair<String, String>(getString(R.string.id_back_capture), CustomizeUIConfigManager.ID_CAPTURE_BACK));
        uiConfigList.add(new Pair<String, String>(getString(R.string.document_capture_tab), CustomizeUIConfigManager.DOCUMENT_CAPTURE));
        uiConfigList.add(new Pair<String, String>(getString(R.string.snippet_capture), CustomizeUIConfigManager.SNIPPET_CAPTURE));
        uiConfigList.add(new Pair<String, String>(getString(R.string.selfie_capture), CustomizeUIConfigManager.FACE_CAPTURE));
        uiConfigList.add(new Pair<String, String>(getString(R.string.fingerprint_capture_4F), CustomizeUIConfigManager.FINGER_4F_CAPTURE));
        uiConfigList.add(new Pair<String, String>(getString(R.string.capture_secondary_id_front), CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_FRONT));
        uiConfigList.add(new Pair<String, String>(getString(R.string.capture_secondary_id_back), CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_BACK));
        uiConfigList.add(new Pair<String, String>(getString(R.string.voice_capture), CustomizeUIConfigManager.VOICE_RECORDING));
        SpinnerAdapterForPair uiConfigListAdapter = new SpinnerAdapterForPair(getActivity(), android.R.layout.simple_list_item_1, uiConfigList);
        spinnerUIConfig.setAdapter(uiConfigListAdapter);

        textFontTypeList = new ArrayList<Pair<String, String>>();
        textFontTypeList.add(new Pair<String, String>(getString(R.string.default_font_type), "DEFAULT"));
        textFontTypeList.add(new Pair<String, String>(getString(R.string.default_bold), "DEFAULT_BOLD"));
        textFontTypeList.add(new Pair<String, String>(getString(R.string.san_serif), "SANS_SERIF"));
        textFontTypeList.add(new Pair<String, String>(getString(R.string.serif), "SERIF"));
        textFontTypeList.add(new Pair<String, String>(getString(R.string.monospace), "MONOSPACE"));
        SpinnerAdapterForPair textFontTypeListAdapter = new SpinnerAdapterForPair(getActivity(), android.R.layout.simple_list_item_1, textFontTypeList);
        spinnerTextFontType.setAdapter(textFontTypeListAdapter);

        textFontStyleList = new ArrayList<Pair<String, String>>();
        textFontStyleList.add(new Pair<String, String>(getString(R.string.normal), "NORMAL"));
        textFontStyleList.add(new Pair<String, String>(getString(R.string.bold), "BOLD"));
        textFontStyleList.add(new Pair<String, String>(getString(R.string.italic), "ITALIC"));
        textFontStyleList.add(new Pair<String, String>(getString(R.string.bold_italic), "BOLD_ITALIC"));
        SpinnerAdapterForPair textFontStyleListAdapter = new SpinnerAdapterForPair(getActivity(), android.R.layout.simple_list_item_1, textFontStyleList);
        spinnerTextFontStyle.setAdapter(textFontStyleListAdapter);

        headerTextFontTypeList = new ArrayList<Pair<String, String>>();
        headerTextFontTypeList.add(new Pair<String, String>(getString(R.string.default_font_type), "DEFAULT"));
        headerTextFontTypeList.add(new Pair<String, String>(getString(R.string.default_bold), "DEFAULT_BOLD"));
        headerTextFontTypeList.add(new Pair<String, String>(getString(R.string.san_serif), "SANS_SERIF"));
        headerTextFontTypeList.add(new Pair<String, String>(getString(R.string.serif), "SERIF"));
        headerTextFontTypeList.add(new Pair<String, String>(getString(R.string.monospace), "MONOSPACE"));
        SpinnerAdapterForPair headerTextFontTypeListAdapter = new SpinnerAdapterForPair(getActivity(), android.R.layout.simple_list_item_1, headerTextFontTypeList);
        spinnerHeaderTextFontType.setAdapter(headerTextFontTypeListAdapter);

        headerTextFontStyleList = new ArrayList<Pair<String, String>>();
        headerTextFontStyleList.add(new Pair<String, String>(getString(R.string.normal), "NORMAL"));
        headerTextFontStyleList.add(new Pair<String, String>(getString(R.string.bold), "BOLD"));
        headerTextFontStyleList.add(new Pair<String, String>(getString(R.string.italic), "ITALIC"));
        headerTextFontStyleList.add(new Pair<String, String>(getString(R.string.bold_italic), "BOLD_ITALIC"));
        SpinnerAdapterForPair headerTextFontStyleListAdapter = new SpinnerAdapterForPair(getActivity(), android.R.layout.simple_list_item_1, headerTextFontStyleList);
        spinnerHeaderTextFontStyle.setAdapter(headerTextFontStyleListAdapter);

        titleAlignmentList = new ArrayList<Pair<String, String>>();
        titleAlignmentList.add(new Pair<String, String>(getString(R.string.top), "Top"));
        titleAlignmentList.add(new Pair<String, String>(getString(R.string.center), "Center"));
        titleAlignmentList.add(new Pair<String, String>(getString(R.string.bottom), "Bottom"));
        SpinnerAdapterForPair titleAlignmentListAdapter = new SpinnerAdapterForPair(getActivity(), android.R.layout.simple_list_item_1, titleAlignmentList);
        spinnerTitleAlignment.setAdapter(titleAlignmentListAdapter);

        hintAlignmentList = new ArrayList<Pair<String, String>>();
        hintAlignmentList.add(new Pair<String, String>(getString(R.string.center), "Center"));
        hintAlignmentList.add(new Pair<String, String>(getString(R.string.bottom), "Bottom"));
        hintAlignmentList.add(new Pair<String, String>(getString(R.string.top), "Top"));
        SpinnerAdapterForPair hintAlignmentListAdapter = new SpinnerAdapterForPair(getActivity(), android.R.layout.simple_list_item_1, hintAlignmentList);
        spinnerHintAlignment.setAdapter(hintAlignmentListAdapter);

        titleImageAlignmentList = new ArrayList<Pair<String, String>>();
        titleImageAlignmentList.add(new Pair<String, String>(getString(R.string.bottom), "Bottom"));
        titleImageAlignmentList.add(new Pair<String, String>(getString(R.string.top), "Top"));
        titleImageAlignmentList.add(new Pair<String, String>(getString(R.string.center), "Center"));
        SpinnerAdapterForPair titleImageAlignmentListAdapter = new SpinnerAdapterForPair(getActivity(), android.R.layout.simple_list_item_1, titleImageAlignmentList);
        spinnerTitleImageAlignment.setAdapter(titleImageAlignmentListAdapter);

        idCaptureBorderList = new ArrayList<Pair<String, String>>();
        idCaptureBorderList.add(new Pair<String, String>(getString(R.string.thin), "Thin"));
        idCaptureBorderList.add(new Pair<String, String>(getString(R.string.thick), "Thick"));
        SpinnerAdapterForPair idCaptureBorderListAdapter = new SpinnerAdapterForPair(getActivity(), android.R.layout.simple_list_item_1, idCaptureBorderList);
        spinnerIDCaptureBorder.setAdapter(idCaptureBorderListAdapter);

        // spinnerLabel.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, LABELS_KEY));
    }

    private void setSelfieCaptureSpinnerAdapter() {
        ArrayList<Pair<String,String>> titleList = new ArrayList<Pair<String,String>>();
        titleList.add(new Pair<String, String>(getString(R.string.none),"None"));
        titleList.add(new Pair<String, String>(getString(R.string.title_image_1),"Title Image 1"));
        SpinnerAdapterForPair titleListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                titleList);
        spinnerFaceTitle.setAdapter(titleListAdapter);

        faceTextFontTypeList = new ArrayList<Pair<String, String>>();
        faceTextFontTypeList.add(new Pair<String, String>(getString(R.string.default_font_type), "DEFAULT"));
        faceTextFontTypeList.add(new Pair<String, String>(getString(R.string.default_bold), "DEFAULT_BOLD"));
        faceTextFontTypeList.add(new Pair<String, String>(getString(R.string.san_serif), "SANS_SERIF"));
        faceTextFontTypeList.add(new Pair<String, String>(getString(R.string.serif), "SERIF"));
        faceTextFontTypeList.add(new Pair<String, String>(getString(R.string.monospace), "MONOSPACE"));
        SpinnerAdapterForPair textFontTypeListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                faceTextFontTypeList);
        faceSpinnerTextFontType.setAdapter(textFontTypeListAdapter);

        faceTextFontStyleList = new ArrayList<Pair<String, String>>();
        faceTextFontStyleList.add(new Pair<String, String>(getString(R.string.normal), "NORMAL"));
        faceTextFontStyleList.add(new Pair<String, String>(getString(R.string.bold), "BOLD"));
        faceTextFontStyleList.add(new Pair<String, String>(getString(R.string.italic), "ITALIC"));
        faceTextFontStyleList.add(new Pair<String, String>(getString(R.string.bold_italic), "BOLD_ITALIC"));
        SpinnerAdapterForPair textFontStyleListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                faceTextFontStyleList);
        faceSpinnerTextFontStyle.setAdapter(textFontStyleListAdapter);

        faceHeaderTextFontTypeList = new ArrayList<Pair<String, String>>();
        faceHeaderTextFontTypeList.add(new Pair<String, String>(getString(R.string.default_font_type), "DEFAULT"));
        faceHeaderTextFontTypeList.add(new Pair<String, String>(getString(R.string.default_bold), "DEFAULT_BOLD"));
        faceHeaderTextFontTypeList.add(new Pair<String, String>(getString(R.string.san_serif), "SANS_SERIF"));
        faceHeaderTextFontTypeList.add(new Pair<String, String>(getString(R.string.serif), "SERIF"));
        faceHeaderTextFontTypeList.add(new Pair<String, String>(getString(R.string.monospace), "MONOSPACE"));
        SpinnerAdapterForPair headerTextFontTypeListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                faceHeaderTextFontTypeList);
        faceSpinnerHeaderTextFontType.setAdapter(headerTextFontTypeListAdapter);

        facehHeaderTextFontStyleList = new ArrayList<Pair<String, String>>();
        facehHeaderTextFontStyleList.add(new Pair<String, String>(getString(R.string.normal), "NORMAL"));
        facehHeaderTextFontStyleList.add(new Pair<String, String>(getString(R.string.bold), "BOLD"));
        facehHeaderTextFontStyleList.add(new Pair<String, String>(getString(R.string.italic), "ITALIC"));
        facehHeaderTextFontStyleList.add(new Pair<String, String>(getString(R.string.bold_italic), "BOLD_ITALIC"));
        SpinnerAdapterForPair headerTextFontStyleListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                facehHeaderTextFontStyleList);
        faceSpinnerHeaderTextFontStyle.setAdapter(headerTextFontStyleListAdapter);

        faceTitleAlignmentList = new ArrayList<Pair<String, String>>();
        faceTitleAlignmentList.add(new Pair<String, String>(getString(R.string.top), "Top"));
        faceTitleAlignmentList.add(new Pair<String, String>(getString(R.string.bottom), "Bottom"));
        SpinnerAdapterForPair titleAlignmentListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                faceTitleAlignmentList);
        faceSpinnerTitleAlignment.setAdapter(titleAlignmentListAdapter);

        faceHintMsgAlignmentList = new ArrayList<Pair<String, String>>();
        faceHintMsgAlignmentList.add(new Pair<String, String>(getString(R.string.bottom), "Bottom"));
        faceHintMsgAlignmentList.add(new Pair<String, String>(getString(R.string.top), "Top"));
        SpinnerAdapterForPair hintMsgAlignmentListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                faceHintMsgAlignmentList);
        faceSpinnerHintMsgAlignment.setAdapter(hintMsgAlignmentListAdapter);

        faceHintIconAlignmentList = new ArrayList<Pair<String, String>>();
        faceHintIconAlignmentList.add(new Pair<String, String>(getString(R.string.top), "Top"));
        faceHintIconAlignmentList.add(new Pair<String, String>(getString(R.string.bottom), "Bottom"));
        SpinnerAdapterForPair hintIconAlignmentListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                faceHintIconAlignmentList);
        faceSpinnerHintIconAlignment.setAdapter(hintIconAlignmentListAdapter);

        faceTitleImageAlignmentList = new ArrayList<Pair<String, String>>();
        faceTitleImageAlignmentList.add(new Pair<String, String>(getString(R.string.top), "Top"));
        faceTitleImageAlignmentList.add(new Pair<String, String>(getString(R.string.bottom), "Bottom"));
        SpinnerAdapterForPair titleImageAlignmentListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                faceTitleImageAlignmentList);
        faceSpinnerTitleImageAlignment.setAdapter(titleImageAlignmentListAdapter);

        faceContoursList = new ArrayList<Pair<String, String>>();
        faceContoursList.add(new Pair<String, String>(getString(R.string.low), "Low"));
        faceContoursList.add(new Pair<String, String>(getString(R.string.medium), "Medium"));
        faceContoursList.add(new Pair<String, String>(getString(R.string.all), "All"));
        faceContoursList.add(new Pair<String, String>(getString(R.string.zero), "Zero"));
        SpinnerAdapterForPair faceContoursListAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                faceContoursList);
        faceSpinnerFaceContours.setAdapter(faceContoursListAdapter);

    }

    private void setVoiceRecordingSpinnerAdapter() {
        voiceDisplayPositionList = new ArrayList<Pair<String, String>>();
        voiceDisplayPositionList.add(new Pair<String, String>(getString(R.string.top), "Top"));
        voiceDisplayPositionList.add(new Pair<String, String>(getString(R.string.center), "Center"));
        voiceDisplayPositionList.add(new Pair<String, String>(getString(R.string.bottom), "Bottom"));

        SpinnerAdapterForPair voiceDisplayPositionListAdapter = new SpinnerAdapterForPair(getActivity(), android.R.layout.simple_list_item_1, voiceDisplayPositionList);
        voiceDisplayPositionSpinner.setAdapter(voiceDisplayPositionListAdapter);
    }

    private void setIDCaptureDefaultValues() {
        CustomizeUIConfig uiConfig = null;
        String current_selected = ((Pair) spinnerUIConfig.getSelectedItem()).second.toString();

        if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_FRONT)) {
            uiConfig = CustomizeUIConfigManager.getDefaultIDCaptureFrontConfig();
        } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_BACK)) {
            uiConfig = CustomizeUIConfigManager.getDefaultIDCaptureBackConfig();
        } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.DOCUMENT_CAPTURE)) {
            uiConfig = CustomizeUIConfigManager.getDefaultDocCaptureConfig();
        } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SNIPPET_CAPTURE)) {
            uiConfig = CustomizeUIConfigManager.getDefaultSnippetCaptureConfig();
        } else if(current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_FRONT)) {
            uiConfig = CustomizeUIConfigManager.getDefaultSecIDCaptureFrontConfig();
        } else if(current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_BACK)) {
            uiConfig = CustomizeUIConfigManager.getDefaultSecIDCaptureBackConfig();
        }

        if (null != uiConfig) {
            setIdCaptureField(uiConfig);
        }
    }

    private void setIdCaptureField(CustomizeUIConfig customizeUIConfig) {

        HashMap<String, String> idCaptureUIMap = customizeUIConfig.getUIConfiguration();
        HashMap<String, String> idCaptureLabelMap = customizeUIConfig.getLabelConfiguration();

        String idOutlineColor = idCaptureUIMap.get(UIConfigConstants.ID_ID_OUTLINE_COLOR);
        String idOutlineAlpha = idCaptureUIMap.get(UIConfigConstants.ID_ID_OUTLINE_COLOR_ALPHA);
        String detectedIdOutlineColor = idCaptureUIMap.get(UIConfigConstants.ID_DETECTED_ID_OUTLINE_COLOR);
        String detectedIdOutlineAlpha = idCaptureUIMap.get(UIConfigConstants.ID_DETECTED_ID_OUTLINE_COLOR_ALPHA);
        String idOutsideOutlineColor = idCaptureUIMap.get(UIConfigConstants.ID_ID_OUTSIDE_OUTLINE_COLOR);
        String idOutsideOutlineAlpha = idCaptureUIMap.get(UIConfigConstants.ID_ID_OUTSIDE_OUTLINE_COLOR_APLHA);
        String detectedIdOutsideOutlineColor = idCaptureUIMap.get(UIConfigConstants.ID_DETECTED_ID_OUTSIDE_OUTLINE_COLOR);
        String detectedIdOutsideOutlineAlpha = idCaptureUIMap.get(UIConfigConstants.ID_DETECTED_ID_OUTSIDE_OUTLINE_COLOR_ALPHA);
        String textFontType = idCaptureUIMap.get(UIConfigConstants.ID_LABEL_TEXT_TYPEFACE_TYPE);
        String textFontStyle = idCaptureUIMap.get(UIConfigConstants.ID_LABEL_TEXT_TYPEFACE_STYLE);
        String backButtonColor = idCaptureUIMap.get(UIConfigConstants.ID_BACK_BUTTON_COLOR);
        String backButtonAlpha = idCaptureUIMap.get(UIConfigConstants.ID_BACK_BUTTON_COLOR_ALPHA);
        String retryButtonColor = idCaptureUIMap.get(UIConfigConstants.ID_RETRY_BUTTON_COLOR);
        String retryButtonAlpha = idCaptureUIMap.get(UIConfigConstants.ID_RETRY_BUTTON_COLOR_ALPHA);
        String retryButtonBorderColor = idCaptureUIMap.get(UIConfigConstants.ID_RETRY_BUTTON_BORDER_COLOR);
        String retryButtonBorderAlpha = idCaptureUIMap.get(UIConfigConstants.ID_RETRY_BUTTON_BORDER_COLOR_ALPHA);
        String confirmButtonColor = idCaptureUIMap.get(UIConfigConstants.ID_CONFIRM_BUTTON_COLOR);
        String confirmButtonAlpha = idCaptureUIMap.get(UIConfigConstants.ID_CONFIRM_BUTTON_COLOR_ALPHA);
        String confirmButtonStyle = idCaptureUIMap.get(UIConfigConstants.ID_CONFIRM_BUTTON_BACKGROUND_COLOR);
        String confirmButtonStyleAlpha = idCaptureUIMap.get(UIConfigConstants.ID_CONFIRM_BUTTON_BACKGROUND_COLOR_ALPHA);
        String textLabelColor = idCaptureUIMap.get(UIConfigConstants.ID_LABEL_TEXT_COLOR);
        String textLabelAlpha = idCaptureUIMap.get(UIConfigConstants.ID_LABEL_TEXT_ALPHA);
        String instructionContinueBtnColor = idCaptureUIMap.get(UIConfigConstants.ID_INSTRUCTION_BUTTON_COLOR);
        String instructionContinueBtnColorAlpha = idCaptureUIMap.get(UIConfigConstants.ID_INSTRUCTION_BUTTON_ALPHA);
        String instructionContinueBtnTxtColor = idCaptureUIMap.get(UIConfigConstants.ID_INSTRUCTION_BUTTON_TXT_COLOR);
        String instructionContinueBtnTxtAlpha = idCaptureUIMap.get(UIConfigConstants.ID_INSTRUCTION_BUTTON_TXT_ALPHA);
        String headerTextColorEdtTxt = idCaptureUIMap.get(UIConfigConstants.ID_HEADER_TEXT_LABEL_COLOR);
        String headerTextAlphaEdtTxt = idCaptureUIMap.get(UIConfigConstants.ID_HEADER_TEXT_LABEL_ALPHA);
        String headerTextSizeEdtTxt = idCaptureUIMap.get(UIConfigConstants.ID_HEADER_TEXT_LABEL_SIZE);
        String textSizeEdtTxt = idCaptureUIMap.get(UIConfigConstants.ID_LABEL_TEXT_SIZE);
        String headerTextFontType = idCaptureUIMap.get(UIConfigConstants.ID_HEADER_TEXT_TYPEFACE_TYPE);
        String headerTextFontStyle = idCaptureUIMap.get(UIConfigConstants.ID_HEADER_TEXT_TYPEFACE_STYLE);
        String idCaptureBorderType = idCaptureUIMap.get(UIConfigConstants.ID_ID_CAPTURE_BORDER_STYLE);
        String titleAlignment = idCaptureUIMap.get(UIConfigConstants.ID_TITLE_LABEL_ALIGNMENT);
        String hintAlignment = idCaptureUIMap.get(UIConfigConstants.ID_HINT_MESSAGE_ALIGNMENT);
        String titleImageAlignment = idCaptureUIMap.get(UIConfigConstants.ID_TITLE_IMAGE_ALIGNMENT);
        String hideIdCaptureTitle = idCaptureUIMap.get(UIConfigConstants.ID_HIDE_ID_TITLE_LABEL);
        String hideIdCaptureHintMsg = idCaptureUIMap.get(UIConfigConstants.ID_HIDE_ID_HINT_MESSAGE);
        String hideIdCaptureTitleImg = idCaptureUIMap.get(UIConfigConstants.ID_HIDE_ID_TITLE_IMAGE);
        String idTitleImageBitmap = idCaptureUIMap.get(UIConfigConstants.ID_TITLE_IMG_BITMAP_BASE64);
        String captureButtonColor = idCaptureUIMap.get(UIConfigConstants.ID_CAPTURE_BUTTON_COLOR);
        String captureButtonAlpha = idCaptureUIMap.get(UIConfigConstants.ID_CAPTURE_BUTTON_ALPHA);

        String showInstScreen = idCaptureUIMap.get(UIConfigConstants.ID_SHOW_INSTRUCTION);
        String idLabelShadowEnable = idCaptureUIMap.get(UIConfigConstants.ID_LABEL_SHADOW_ENABLE);

        idOutlineColorEdtTxt.setText(idOutlineColor);
        idOutlineAlphaEdtTxt.setText(idOutlineAlpha);
        detectedIdOutlineColorEdtTxt.setText(detectedIdOutlineColor);
        detectedIdOutlineAlphaEdtTxt.setText(detectedIdOutlineAlpha);
        idOutsideOutlineColorEdtTxt.setText(idOutsideOutlineColor);
        idOutsideOutlineAlphaEdtTxt.setText(idOutsideOutlineAlpha);
        detectedOutsideOutlineColorEdtTxt.setText(detectedIdOutsideOutlineColor);
        detectedOutsideOutlineAlphaEdtTxt.setText(detectedIdOutsideOutlineAlpha);
        backButtonColorEdtTxt.setText(backButtonColor);
        backButtonAlphaEdtTxt.setText(backButtonAlpha);
        retryButtonColorEdtTxt.setText(retryButtonColor);
        retryButtonAlphaEdtTxt.setText(retryButtonAlpha);
        retryButtonBorderColorEdtTxt.setText(retryButtonBorderColor);
        retryButtonBorderAlphaEdtTxt.setText(retryButtonBorderAlpha);
        confirmButtonColorEdtTxt.setText(confirmButtonColor);
        confirmButtonAlphaEdtTxt.setText(confirmButtonAlpha);
        confirmButtonStyleEdtTxt.setText(confirmButtonStyle);
        confirmButtonStyleAlphaEdtTxt.setText(confirmButtonStyleAlpha);
        textLabelColorEdtTxt.setText(textLabelColor);
        textLabelAlphaEdtTxt.setText(textLabelAlpha);
        instructionContinueBtnColorEdtTxt.setText(instructionContinueBtnColor);
        instructionContinueBtnAlphaEdtTxt.setText(instructionContinueBtnColorAlpha);
        instructionContinueBtnTxtColorEdtTxt.setText(instructionContinueBtnTxtColor);
        instructionContinueBtnTxtAlphaEdtTxt.setText(instructionContinueBtnTxtAlpha);
        headerTextLabelColorEdtTxt.setText(headerTextColorEdtTxt);
        headerTextLabelAlphaEdtTxt.setText(headerTextAlphaEdtTxt);
        headerTextLabelSizeEdtTxt.setText(headerTextSizeEdtTxt);
        textLabelSizeEdtTxt.setText(textSizeEdtTxt);
        captureButtonColorEdtTxt.setText(captureButtonColor);
        captureButtonColorAlphaEdtTxt.setText(captureButtonAlpha);

        spinnerTextFontType.setSelection(getPositionInPair(textFontTypeList, textFontType));
        spinnerTextFontStyle.setSelection(getPositionInPair(textFontStyleList, textFontStyle));
        spinnerTitleAlignment.setSelection(getPositionInPair(titleAlignmentList, titleAlignment));
        spinnerHintAlignment.setSelection(getPositionInPair(hintAlignmentList, hintAlignment));
        spinnerTitleImageAlignment.setSelection(getPositionInPair(titleImageAlignmentList, titleImageAlignment));
        spinnerHeaderTextFontType.setSelection(getPositionInPair(headerTextFontTypeList, headerTextFontType));
        spinnerHeaderTextFontStyle.setSelection(getPositionInPair(headerTextFontStyleList, headerTextFontStyle));
        spinnerIDCaptureBorder.setSelection(getPositionInPair(idCaptureBorderList, idCaptureBorderType));
        spinnerTitle.setSelection(idTitleImageBitmap.isEmpty() ? 0 : 1);
        radioGroupInstruction.check(showInstScreen.equalsIgnoreCase("N") ? radioGroupInstruction.getChildAt(0).getId() : radioGroupInstruction.getChildAt(1).getId());
        titleAlignmentCB.setChecked(!hideIdCaptureTitle.equalsIgnoreCase("N"));
        hintAlignmentCB.setChecked(!hideIdCaptureHintMsg.equalsIgnoreCase("N"));
        titleImageAlignmentCB.setChecked(!hideIdCaptureTitleImg.equalsIgnoreCase("N"));
        enableLabelShadowSwitch.setChecked(idLabelShadowEnable.equalsIgnoreCase("Y") ? true : false);

        spinnerLabel.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, setToArray(idCaptureLabelMap.keySet())));

    }

    private void updateIDConfig(String featureType) {

        HashMap<String, String> ui = null;

        if (featureType.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_FRONT)) {
            ui = CustomizeUIConfigManager.getDefaultIDCaptureFrontConfig().getUIConfiguration();
        } else if (featureType.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_BACK)) {
            ui = CustomizeUIConfigManager.getDefaultIDCaptureBackConfig().getUIConfiguration();
        } else if (featureType.equalsIgnoreCase(CustomizeUIConfigManager.DOCUMENT_CAPTURE)) {
            ui = CustomizeUIConfigManager.getDefaultDocCaptureConfig().getUIConfiguration();
        } else if (featureType.equalsIgnoreCase(CustomizeUIConfigManager.SNIPPET_CAPTURE)) {
            ui = CustomizeUIConfigManager.getDefaultSnippetCaptureConfig().getUIConfiguration();
        } else if (featureType.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_FRONT)) {
            ui = CustomizeUIConfigManager.getDefaultSecIDCaptureFrontConfig().getUIConfiguration();
        } else if (featureType.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_BACK)) {
            ui = CustomizeUIConfigManager.getDefaultSecIDCaptureBackConfig().getUIConfiguration();
        }

        if (ui != null) {
            ui.put(UIConfigConstants.ID_ID_OUTLINE_COLOR, idOutlineColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_ID_OUTLINE_COLOR_ALPHA, idOutlineAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_DETECTED_ID_OUTLINE_COLOR, detectedIdOutlineColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_DETECTED_ID_OUTLINE_COLOR_ALPHA, detectedIdOutlineAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_ID_OUTSIDE_OUTLINE_COLOR, idOutsideOutlineColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_ID_OUTSIDE_OUTLINE_COLOR_APLHA, idOutsideOutlineAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_DETECTED_ID_OUTSIDE_OUTLINE_COLOR, detectedOutsideOutlineColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_DETECTED_ID_OUTSIDE_OUTLINE_COLOR_ALPHA, detectedOutsideOutlineAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_BACK_BUTTON_COLOR, backButtonColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_BACK_BUTTON_COLOR_ALPHA, backButtonAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_RETRY_BUTTON_COLOR, retryButtonColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_RETRY_BUTTON_COLOR_ALPHA, retryButtonAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_RETRY_BUTTON_BORDER_COLOR, retryButtonBorderColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_RETRY_BUTTON_BORDER_COLOR_ALPHA, retryButtonBorderAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_CONFIRM_BUTTON_COLOR, confirmButtonColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_CONFIRM_BUTTON_COLOR_ALPHA, confirmButtonAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_CONFIRM_BUTTON_BACKGROUND_COLOR, confirmButtonStyleEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_CONFIRM_BUTTON_BACKGROUND_COLOR_ALPHA, confirmButtonStyleAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_LABEL_TEXT_COLOR, textLabelColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_LABEL_TEXT_ALPHA, textLabelAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_INSTRUCTION_BUTTON_COLOR, instructionContinueBtnColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_INSTRUCTION_BUTTON_ALPHA, instructionContinueBtnAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_INSTRUCTION_BUTTON_TXT_COLOR, instructionContinueBtnTxtColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_INSTRUCTION_BUTTON_TXT_ALPHA, instructionContinueBtnTxtAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_HEADER_TEXT_LABEL_COLOR, headerTextLabelColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_HEADER_TEXT_LABEL_ALPHA, headerTextLabelAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_HEADER_TEXT_LABEL_SIZE, headerTextLabelSizeEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_LABEL_TEXT_SIZE, textLabelSizeEdtTxt.getText().toString().trim());

            ui.put(UIConfigConstants.ID_CAPTURE_BUTTON_COLOR, captureButtonColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.ID_CAPTURE_BUTTON_ALPHA, captureButtonColorAlphaEdtTxt.getText().toString().trim());

            ui.put(UIConfigConstants.ID_LABEL_TEXT_TYPEFACE_TYPE, ((Pair) spinnerTextFontType.getSelectedItem()).second.toString());
            ui.put(UIConfigConstants.ID_LABEL_TEXT_TYPEFACE_STYLE, ((Pair) spinnerTextFontStyle.getSelectedItem()).second.toString());
            ui.put(UIConfigConstants.ID_HEADER_TEXT_TYPEFACE_TYPE, ((Pair) spinnerHeaderTextFontType.getSelectedItem()).second.toString());
            ui.put(UIConfigConstants.ID_HEADER_TEXT_TYPEFACE_STYLE, ((Pair) spinnerHeaderTextFontStyle.getSelectedItem()).second.toString());
            ui.put(UIConfigConstants.ID_ID_CAPTURE_BORDER_STYLE, ((Pair) spinnerIDCaptureBorder.getSelectedItem()).second.toString());
            ui.put(UIConfigConstants.ID_TITLE_LABEL_ALIGNMENT, ((Pair) spinnerTitleAlignment.getSelectedItem()).second.toString());
            ui.put(UIConfigConstants.ID_HINT_MESSAGE_ALIGNMENT, ((Pair) spinnerHintAlignment.getSelectedItem()).second.toString());
            ui.put(UIConfigConstants.ID_TITLE_IMAGE_ALIGNMENT, ((Pair) spinnerTitleImageAlignment.getSelectedItem()).second.toString());

            ui.put(UIConfigConstants.ID_SHOW_INSTRUCTION, (radioGroupInstruction.getCheckedRadioButtonId() == R.id.radio_button_no) ? "N" : "Y");

            ui.put(UIConfigConstants.ID_HIDE_ID_TITLE_LABEL, titleAlignmentCB.isChecked() ? "Y" : "N");
            ui.put(UIConfigConstants.ID_HIDE_ID_HINT_MESSAGE, hintAlignmentCB.isChecked() ? "Y" : "N");
            ui.put(UIConfigConstants.ID_HIDE_ID_TITLE_IMAGE, titleImageAlignmentCB.isChecked() ? "Y" : "N");
            ui.put(UIConfigConstants.ID_TITLE_IMG_BITMAP_BASE64, getConvertedBaset64Image(((Pair) spinnerTitle.getSelectedItem()).second.toString()));
            ui.put(UIConfigConstants.ID_LABEL_SHADOW_ENABLE, enableLabelShadowSwitch.isChecked() ? "Y" : "N");
            CustomizeUIConfigManager.storeConfig(getActivity(), featureType);
        }
    }

    private void setSelfieCaptureDefaultValues() {
        CustomizeUIConfig uiConfig = null;
        String current_selected = ((Pair) spinnerUIConfig.getSelectedItem()).second.toString();

        if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FACE_CAPTURE)) {
            uiConfig = CustomizeUIConfigManager.getDefaultFaceCaptureConfig();
        }

        if (null != uiConfig) {
            setSelfieCaptureField(uiConfig);
        }
    }

    private void setSelfieCaptureField(CustomizeUIConfig customizeUIConfig) {

        HashMap<String, String> selfieCaptureUIMap = customizeUIConfig.getUIConfiguration();
        HashMap<String, String> selfieCaptureLabelMap = customizeUIConfig.getLabelConfiguration();

        String faceOutlineColor = selfieCaptureUIMap.get(UIConfigConstants.FD_OUTLINE_COLOR);
        String faceOutlineColorAlpha = selfieCaptureUIMap.get(UIConfigConstants.FD_OUTLINE_COLOR_ALPHA);
        String detectedFaceOutlineColor = selfieCaptureUIMap.get(UIConfigConstants.FD_DETECTED_FACE_OUTLINE_COLOR);
        String detectedFaceOutlineColorAlpha = selfieCaptureUIMap.get(UIConfigConstants.FD_DETECTED_FACE_OUTLINE_COLOR_ALPHA);
        String outsideFaceOutlineColor = selfieCaptureUIMap.get(UIConfigConstants.FD_OUTSIDE_FACE_OUTLINE_COLOR);
        String outsideFaceOutlineColorAlpha = selfieCaptureUIMap.get(UIConfigConstants.FD_OUTSIDE_FACE_OUTLINE_COLOR_ALPHA);
        String detectedOutsideFaceOutlineColor = selfieCaptureUIMap.get(UIConfigConstants.FD_DETECTED_OUTSIDE_FACE_OUTLINE_COLOR);
        String detectedOutsideFaceOutlineColorAlpha = selfieCaptureUIMap.get(UIConfigConstants.FD_DETECTED_OUTSIDE_FACE_OUTLINE_COLOR_ALPHA);

        String textFontType = selfieCaptureUIMap.get(UIConfigConstants.FD_LABEL_TEXT_TYPEFACE_TYPE);
        String textFontStyle = selfieCaptureUIMap.get(UIConfigConstants.FD_LABEL_TEXT_TYPEFACE_STYLE);
        String backButtonColor = selfieCaptureUIMap.get(UIConfigConstants.FD_BACK_BUTTON_COLOR);
        String backButtonAlpha = selfieCaptureUIMap.get(UIConfigConstants.FD_BACK_BUTTON_COLOR_ALPHA);
        String retryButtonColor = selfieCaptureUIMap.get(UIConfigConstants.FD_RETRY_BUTTON_COLOR);
        String retryButtonAlpha = selfieCaptureUIMap.get(UIConfigConstants.FD_RETRY_BUTTON_COLOR_ALPHA);
        String retryButtonBorderColor = selfieCaptureUIMap.get(UIConfigConstants.FD_RETRY_BUTTON_BORDER_COLOR);
        String retryButtonBorderAlpha = selfieCaptureUIMap.get(UIConfigConstants.FD_RETRY_BUTTON_BORDER_COLOR_ALPHA);
        String confirmButtonColor = selfieCaptureUIMap.get(UIConfigConstants.FD_CONFIRM_BUTTON_COLOR);
        String confirmButtonAlpha = selfieCaptureUIMap.get(UIConfigConstants.FD_CONFIRM_BUTTON_COLOR_ALPHA);
        String confirmButtonStyle = selfieCaptureUIMap.get(UIConfigConstants.FD_CONFIRM_BUTTON_BACKGROUND_COLOR);
        String confirmButtonStyleAlpha = selfieCaptureUIMap.get(UIConfigConstants.FD_CONFIRM_BUTTON_BACKGROUND_COLOR_ALPHA);
        String textLabelColor = selfieCaptureUIMap.get(UIConfigConstants.FD_LABEL_TEXT_COLOR);
        String textLabelAlpha = selfieCaptureUIMap.get(UIConfigConstants.FD_LABEL_TEXT_ALPHA);
        String instructionContinueBtnColor = selfieCaptureUIMap.get(UIConfigConstants.FD_INSTRUCTION_BUTTON_COLOR);
        String instructionContinueBtnColorAlpha = selfieCaptureUIMap.get(UIConfigConstants.FD_INSTRUCTION_BUTTON_ALPHA);
        String instructionContinueBtnTxtColor = selfieCaptureUIMap.get(UIConfigConstants.FD_INSTRUCTION_BUTTON_TXT_COLOR);
        String instructionContinueBtnTxtAlpha = selfieCaptureUIMap.get(UIConfigConstants.FD_INSTRUCTION_BUTTON_TXT_ALPHA);
        String faceContours = selfieCaptureUIMap.get(UIConfigConstants.FD_FACE_CONTOURS);
        String headerTextColorEdtTxt = selfieCaptureUIMap.get(UIConfigConstants.FD_HEADER_TEXT_LABEL_COLOR);
        String headerTextAlphaEdtTxt = selfieCaptureUIMap.get(UIConfigConstants.FD_HEADER_TEXT_LABEL_ALPHA);
        String headerTextSizeEdtTxt = selfieCaptureUIMap.get(UIConfigConstants.FD_HEADER_TEXT_LABEL_SIZE);
        String textSizeEdtTxt = selfieCaptureUIMap.get(UIConfigConstants.FD_LABEL_TEXT_SIZE);
        String headerTextFontType = selfieCaptureUIMap.get(UIConfigConstants.FD_HEADER_TEXT_TYPEFACE_TYPE);
        String headerTextFontStyle = selfieCaptureUIMap.get(UIConfigConstants.FD_HEADER_TEXT_TYPEFACE_STYLE);

        String titleAlignment = selfieCaptureUIMap.get(UIConfigConstants.FD_FACE_TITLE_LABEL_ON_TOP);
        String hintMsgAlignment = selfieCaptureUIMap.get(UIConfigConstants.FD_FACE_HINT_MESSAGE_ON_TOP);
        String hintIconAlignment = selfieCaptureUIMap.get(UIConfigConstants.FD_FACE_HINT_ICON_ON_TOP);
        String titleImageAlignment = selfieCaptureUIMap.get(UIConfigConstants.FD_TITLE_IMAGE_ON_TOP);

        String hideTitleAlignment = selfieCaptureUIMap.get(UIConfigConstants.FD_HIDE_FACE_TITLE_LABEL);
        String hideHintMsgAlignemnt = selfieCaptureUIMap.get(UIConfigConstants.FD_HIDE_FACE_HINT_MESSAGE);
        String hideHintIconAlignment = selfieCaptureUIMap.get(UIConfigConstants.FD_HIDE_FACE_HINT_ICON);
        String hideTitleImageAlignment = selfieCaptureUIMap.get(UIConfigConstants.FD_HIDE_TITLE_IMAGE);
        String faceTitleImageBitmap = selfieCaptureUIMap.get(UIConfigConstants.FD_TITLE_IMG_BITMAP_BASE64);
        String showCustomUI = selfieCaptureUIMap.get(UIConfigConstants.FD_SHOW_CUSTOM_UI);

        String showInstScreen = selfieCaptureUIMap.get(UIConfigConstants.FD_SHOW_INSTRUCTION_SCREEN);
        String showPrevScreen = selfieCaptureUIMap.get(UIConfigConstants.FD_SHOW_PREVIEW_SCREEN);
        String fdLabelShadowEnable = selfieCaptureUIMap.get(UIConfigConstants.FD_LABEL_SHADOW_ENABLE);

        String instructionPreviewBackgroundColor = selfieCaptureUIMap.get(UIConfigConstants.FD_INSTRUCTION_PREVIEW_BACKGROUND_COLOR);
        String instructionPreviewBackgroundColorAlpha = selfieCaptureUIMap.get(UIConfigConstants.FD_INSTRUCTION_PREVIEW_BACKGROUND_COLOR_ALPHA);

        faceOutlineColorEdtTxt.setText(faceOutlineColor);
        faceOutlineColorAlphaEdtTxt.setText(faceOutlineColorAlpha);
        detectedFaceOutlineColorEdtTxt.setText(detectedFaceOutlineColor);
        detectedFaceOutlineColorAlphaEdtTxt.setText(detectedFaceOutlineColorAlpha);
        outsideFaceOutlineColorEdtTxt.setText(outsideFaceOutlineColor);
        outsideFaceOutlineColorAlphaEdtTxt.setText(outsideFaceOutlineColorAlpha);
        outsideDetectedFaceOutlineColor.setText(detectedOutsideFaceOutlineColor);
        outsideDetectedFaceOutlineColorAlpha.setText(detectedOutsideFaceOutlineColorAlpha);
        faceBackButtonColorEdtTxt.setText(backButtonColor);
        faceBackButtonAlphaEdtTxt.setText(backButtonAlpha);
        faceRetryButtonColorEdtTxt.setText(retryButtonColor);
        faceRetryButtonAlphaEdtTxt.setText(retryButtonAlpha);
        faceRetryButtonBorderColorEdtTxt.setText(retryButtonBorderColor);
        faceRetryButtonBorderAlphaEdtTxt.setText(retryButtonBorderAlpha);
        faceConfirmButtonColorEdtTxt.setText(confirmButtonColor);
        faceConfirmButtonAlphaEdtTxt.setText(confirmButtonAlpha);
        faceConfirmButtonStyleEdtTxt.setText(confirmButtonStyle);
        faceConfirmButtonStyleAlphaEdtTxt.setText(confirmButtonStyleAlpha);
        faceTextLabelColorEdtTxt.setText(textLabelColor);
        faceTextLabelAlphaEdtTxt.setText(textLabelAlpha);
        faceInstructionContinueBtnColorEdtTxt.setText(instructionContinueBtnColor);
        faceInstructionContinueBtnAlphaEdtTxt.setText(instructionContinueBtnColorAlpha);
        faceInstructionContinueBtnTxtColorEdtTxt.setText(instructionContinueBtnTxtColor);
        faceInstructionContinueBtnTxtAlphaEdtTxt.setText(instructionContinueBtnTxtAlpha);
        faceHeaderTextLabelColorEdtTxt.setText(headerTextColorEdtTxt);
        faceHeaderTextLabelAlphaEdtTxt.setText(headerTextAlphaEdtTxt);
        faceHeaderTextLabelSizeEdtTxt.setText(headerTextSizeEdtTxt);
        faceTextLabelSizeEdtTxt.setText(textSizeEdtTxt);
        faceInstructionPreviewBackgroundEdtTxt.setText(instructionPreviewBackgroundColor);
        faceInstructionPreviewBackgroundAlphaEdtTxt.setText(instructionPreviewBackgroundColorAlpha);

        faceSpinnerTextFontType.setSelection(getPositionInPair(faceTextFontTypeList, textFontType));
        faceSpinnerTextFontStyle.setSelection(getPositionInPair(faceTextFontStyleList, textFontStyle));
        faceSpinnerHeaderTextFontType.setSelection(getPositionInPair(faceHeaderTextFontTypeList, headerTextFontType));
        faceSpinnerHeaderTextFontStyle.setSelection(getPositionInPair(facehHeaderTextFontStyleList, headerTextFontStyle));
        faceSpinnerFaceContours.setSelection(getPositionInPair(faceContoursList, faceContours));

        faceSpinnerTitleAlignment.setSelection(getPositionInPair(faceTitleAlignmentList, getFacePosition(titleAlignment)));
        faceSpinnerHintMsgAlignment.setSelection(getPositionInPair(faceHintMsgAlignmentList, getFacePosition(hintMsgAlignment)));
        faceSpinnerTitleImageAlignment.setSelection(getPositionInPair(faceTitleImageAlignmentList, getFacePosition(titleImageAlignment)));
        faceSpinnerHintIconAlignment.setSelection(getPositionInPair(faceHintIconAlignmentList, getFacePosition(hintIconAlignment)));
        spinnerFaceTitle.setSelection(faceTitleImageBitmap.isEmpty() ? 0 : 1);

        showInstructionFaceDetect.setChecked(showInstScreen.equalsIgnoreCase("Y") ? true : false);
        showPreviewScreenCheckBox.setChecked(showPrevScreen.equalsIgnoreCase("Y") ? true : false);

        faceTitleAlignmentCB.setChecked(!hideTitleAlignment.equalsIgnoreCase("N"));
        faceHintMsgAlignmentCB.setChecked(!hideHintMsgAlignemnt.equalsIgnoreCase("N"));
        faceHintIconAlignmentCB.setChecked(!hideHintIconAlignment.equalsIgnoreCase("N"));
        faceTitleImageAlignmentCB.setChecked(!hideTitleImageAlignment.equalsIgnoreCase("N"));
        showCustomOverlay.setChecked(showCustomUI.equalsIgnoreCase("Y"));
        faceEnableLabelShadowSwitch.setChecked(fdLabelShadowEnable.equalsIgnoreCase("Y") ? true : false);

        faceSpinnerLabel.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, setToArray(selfieCaptureLabelMap.keySet())));

    }

    private void updateSelfieConfig(String featureType) {

        HashMap<String, String> ui = null;
        ui = CustomizeUIConfigManager.getDefaultFaceCaptureConfig().getUIConfiguration();

        if (ui != null) {

            ui.put(UIConfigConstants.FD_OUTLINE_COLOR, faceOutlineColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_OUTLINE_COLOR_ALPHA, faceOutlineColorAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_DETECTED_FACE_OUTLINE_COLOR, detectedFaceOutlineColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_DETECTED_FACE_OUTLINE_COLOR_ALPHA, detectedFaceOutlineColorAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_OUTSIDE_FACE_OUTLINE_COLOR, outsideFaceOutlineColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_OUTSIDE_FACE_OUTLINE_COLOR_ALPHA, outsideFaceOutlineColorAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_DETECTED_OUTSIDE_FACE_OUTLINE_COLOR, outsideDetectedFaceOutlineColor.getText().toString().trim());
            ui.put(UIConfigConstants.FD_DETECTED_OUTSIDE_FACE_OUTLINE_COLOR_ALPHA, outsideDetectedFaceOutlineColorAlpha.getText().toString().trim());
            ui.put(UIConfigConstants.FD_BACK_BUTTON_COLOR, faceBackButtonColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_BACK_BUTTON_COLOR_ALPHA, faceBackButtonAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_RETRY_BUTTON_COLOR, faceRetryButtonColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_RETRY_BUTTON_COLOR_ALPHA, faceRetryButtonAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_RETRY_BUTTON_BORDER_COLOR, faceRetryButtonBorderColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_RETRY_BUTTON_BORDER_COLOR_ALPHA, faceRetryButtonBorderAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_CONFIRM_BUTTON_COLOR, faceConfirmButtonColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_CONFIRM_BUTTON_COLOR_ALPHA, faceConfirmButtonAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_CONFIRM_BUTTON_BACKGROUND_COLOR, faceConfirmButtonStyleEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_CONFIRM_BUTTON_BACKGROUND_COLOR_ALPHA, faceConfirmButtonStyleAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_LABEL_TEXT_COLOR, faceTextLabelColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_LABEL_TEXT_ALPHA, faceTextLabelAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_INSTRUCTION_BUTTON_COLOR, faceInstructionContinueBtnColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_INSTRUCTION_BUTTON_ALPHA, faceInstructionContinueBtnAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_INSTRUCTION_BUTTON_TXT_COLOR, faceInstructionContinueBtnTxtColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_INSTRUCTION_BUTTON_TXT_ALPHA, faceInstructionContinueBtnTxtAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_HEADER_TEXT_LABEL_COLOR, faceHeaderTextLabelColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_HEADER_TEXT_LABEL_ALPHA, faceHeaderTextLabelAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_HEADER_TEXT_LABEL_SIZE, faceHeaderTextLabelSizeEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_LABEL_TEXT_SIZE, faceTextLabelSizeEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_INSTRUCTION_PREVIEW_BACKGROUND_COLOR, faceInstructionPreviewBackgroundEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.FD_INSTRUCTION_PREVIEW_BACKGROUND_COLOR_ALPHA, faceInstructionPreviewBackgroundAlphaEdtTxt.getText().toString().trim());

            ui.put(UIConfigConstants.FD_FACE_CONTOURS, ((Pair) faceSpinnerFaceContours.getSelectedItem()).second.toString());
            ui.put(UIConfigConstants.FD_HEADER_TEXT_TYPEFACE_TYPE, ((Pair) faceSpinnerHeaderTextFontType.getSelectedItem()).second.toString());
            ui.put(UIConfigConstants.FD_HEADER_TEXT_TYPEFACE_STYLE, ((Pair) faceSpinnerHeaderTextFontStyle.getSelectedItem()).second.toString());
            ui.put(UIConfigConstants.FD_LABEL_TEXT_TYPEFACE_TYPE, ((Pair) faceSpinnerTextFontType.getSelectedItem()).second.toString());
            ui.put(UIConfigConstants.FD_LABEL_TEXT_TYPEFACE_STYLE, ((Pair) faceSpinnerTextFontStyle.getSelectedItem()).second.toString());
            ui.put(UIConfigConstants.FD_FACE_TITLE_LABEL_ON_TOP, setJsonFormatFacePosition(((Pair) faceSpinnerTitleAlignment.getSelectedItem()).second.toString()));
            ui.put(UIConfigConstants.FD_FACE_HINT_MESSAGE_ON_TOP, setJsonFormatFacePosition(((Pair) faceSpinnerHintMsgAlignment.getSelectedItem()).second.toString()));
            ui.put(UIConfigConstants.FD_FACE_HINT_ICON_ON_TOP, setJsonFormatFacePosition(((Pair) faceSpinnerHintIconAlignment.getSelectedItem()).second.toString()));
            ui.put(UIConfigConstants.FD_TITLE_IMAGE_ON_TOP, setJsonFormatFacePosition(((Pair) faceSpinnerTitleImageAlignment.getSelectedItem()).second.toString()));

            ui.put(UIConfigConstants.FD_SHOW_INSTRUCTION_SCREEN, showInstructionFaceDetect.isChecked() ? "Y" : "N");
            ui.put(UIConfigConstants.FD_SHOW_PREVIEW_SCREEN, showPreviewScreenCheckBox.isChecked() ? "Y" : "N");

            ui.put(UIConfigConstants.FD_HIDE_FACE_TITLE_LABEL, faceTitleAlignmentCB.isChecked() ? "Y" : "N");
            ui.put(UIConfigConstants.FD_HIDE_FACE_HINT_MESSAGE, faceHintMsgAlignmentCB.isChecked() ? "Y" : "N");
            ui.put(UIConfigConstants.FD_HIDE_FACE_HINT_ICON, faceHintIconAlignmentCB.isChecked() ? "Y" : "N");
            ui.put(UIConfigConstants.FD_HIDE_TITLE_IMAGE, faceTitleImageAlignmentCB.isChecked() ? "Y" : "N");
            ui.put(UIConfigConstants.FD_TITLE_IMG_BITMAP_BASE64, getConvertedBaset64Image(((Pair) spinnerFaceTitle.getSelectedItem()).second.toString()));

            ui.put(UIConfigConstants.FD_SHOW_CUSTOM_UI, showCustomOverlay.isChecked() ? "Y" : "N");
            if (showCustomOverlay.isChecked()) {
                String progress = R.drawable.fd_progress_1 + "," + R.drawable.fd_progress_2 + "," + R.drawable.fd_progress_3 + ","
                        + R.drawable.fd_progress_4 + "," + R.drawable.fd_progress_5 + "," + R.drawable.fd_progress_6 + ","
                        + R.drawable.fd_progress_7 + "," + R.drawable.fd_progress_8 + "," + R.drawable.fd_progress_9 + ","
                        + R.drawable.fd_progress_10 + "," + R.drawable.fd_progress_11;

                String turnArrow = R.drawable.left_turn_arrow + "," + R.drawable.up_move_arrow + "," + R.drawable.right_turn_arrow + "," + R.drawable.down_move_arrow;

                ui.put(UIConfigConstants.FD_SHOW_CUSTOM_UI, "Y");
                ui.put(UIConfigConstants.FD_FACE_OUTLINE_IMAGE_ID, "" + R.drawable.fd_outline);
                ui.put(UIConfigConstants.FD_OUTSIDE_FACE_OUTLINE_IMAGE_ID, "" + R.drawable.fd_background);
                ui.put(UIConfigConstants.FD_FACE_OUTLINE_PROGRESS_IMAGES, progress);
                ui.put(UIConfigConstants.FD_FACE_OUTLINE_PROGRESS_IMAGES_DELAY, "500");
                ui.put(UIConfigConstants.FD_FACE_TURN_ARROW_LIST, turnArrow);
                ui.put(UIConfigConstants.FD_TOGGLE_CAMERA_BUTTON_ICON, ""+R.drawable.fd_toggle_camera_icon);
            } else {
                ui.put(UIConfigConstants.FD_SHOW_CUSTOM_UI, "N");
                ui.put(UIConfigConstants.FD_FACE_OUTLINE_IMAGE_ID, "");
                ui.put(UIConfigConstants.FD_OUTSIDE_FACE_OUTLINE_IMAGE_ID, "");
                ui.put(UIConfigConstants.FD_FACE_OUTLINE_PROGRESS_IMAGES, "");
                ui.put(UIConfigConstants.FD_FACE_OUTLINE_PROGRESS_IMAGES_DELAY, "500");
                ui.put(UIConfigConstants.FD_FACE_TURN_ARROW_LIST, "");
                ui.put(UIConfigConstants.FD_TOGGLE_CAMERA_BUTTON_ICON, "");
            }
            ui.put(UIConfigConstants.FD_LABEL_SHADOW_ENABLE, faceEnableLabelShadowSwitch.isChecked() ? "Y" : "N");

            CustomizeUIConfigManager.storeConfig(getActivity(), featureType);
            Log.d("", "" + ui);
        }
    }

    private void setFPCaptureDefaultValues() {
        CustomizeUIConfig uiConfig = null;
        String current_selected = ((Pair) spinnerUIConfig.getSelectedItem()).second.toString();

        if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FINGER_4F_CAPTURE)) {
            uiConfig = CustomizeUIConfigManager.getDefaultFingerPrintCaptureConfig();
        }

        if (null != uiConfig) {
            setFPCaptureField(uiConfig);
        }
    }

    private void setFPCaptureField(CustomizeUIConfig customizeUIConfig) {

        HashMap<String, String> fpCaptureUIMap = customizeUIConfig.getUIConfiguration();
        // HashMap<String, String> selfieCaptureLabelMap = customizeUIConfig.getLabelConfiguration();

        String textFontType = fpCaptureUIMap.get(UIConfigConstants.CFC_LABEL_TEXT_COLOR);
        String textFontStyle = fpCaptureUIMap.get(UIConfigConstants.CFC_LABEL_TEXT_COLOR_ALPHA);
        String instButtonColor = fpCaptureUIMap.get(UIConfigConstants.CFC_INSTRUCTION_BUTTON_COLOR);
        String intButtonAlpha = fpCaptureUIMap.get(UIConfigConstants.CFC_INSTRUCTION_BUTTON_ALPHA);
        String intButtonTextColor = fpCaptureUIMap.get(UIConfigConstants.CFC_INSTRUCTION_BUTTON_TXT_COLOR);
        String intButtonTextAlpha = fpCaptureUIMap.get(UIConfigConstants.CFC_INSTRUCTION_BUTTON_TXT_ALPHA);

        fpTextLabelColorEdtTxt.setText(textFontType);
        fpTextLabelAlphaEdtTxt.setText(textFontStyle);
        fpInstructionContinueBtnColorEdtTxt.setText(instButtonColor);
        fpInstructionContinueBtnAlphaEdtTxt.setText(intButtonAlpha);
        fpInstructionContinueBtnTxtColorEdtTxt.setText(intButtonTextColor);
        fpInstructionContinueBtnTxtAlphaEdtTxt.setText(intButtonTextAlpha);

    }

    private void updateFingerPrintConfig(String featureType) {

        HashMap<String, String> ui = null;
        ui = CustomizeUIConfigManager.getDefaultFingerPrintCaptureConfig().getUIConfiguration();

        if (ui != null) {

            ui.put(UIConfigConstants.CFC_LABEL_TEXT_COLOR, fpTextLabelColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.CFC_LABEL_TEXT_COLOR_ALPHA, fpTextLabelAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.CFC_INSTRUCTION_BUTTON_COLOR, fpInstructionContinueBtnColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.CFC_INSTRUCTION_BUTTON_ALPHA, fpInstructionContinueBtnAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.CFC_INSTRUCTION_BUTTON_TXT_COLOR, fpInstructionContinueBtnTxtColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.CFC_INSTRUCTION_BUTTON_TXT_ALPHA, fpInstructionContinueBtnTxtAlphaEdtTxt.getText().toString().trim());

            CustomizeUIConfigManager.storeConfig(getActivity(), featureType);
            //Log.d("", "" + ui);
        }
    }

    private void setVoiceRecordingDefaultValues() {
        CustomizeUIConfig uiConfig = null;
        String current_selected = ((Pair) spinnerUIConfig.getSelectedItem()).second.toString();

        if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.VOICE_RECORDING)) {
            uiConfig = CustomizeUIConfigManager.getDefaultVoiceRecordingConfig();
        }

        if (null != uiConfig) {
            setVoiceRecordingField(uiConfig);
        }
    }

    private void setVoiceRecordingField(CustomizeUIConfig customizeUIConfig) {

        HashMap<String, String> voiceRecordingUIMap = customizeUIConfig.getUIConfiguration();
        HashMap<String, String> voiceRecordingLabelMap = customizeUIConfig.getLabelConfiguration();

        String buttonColor = voiceRecordingUIMap.get(UIConfigConstants.VOICE_BUTTON_COLOR);
        String buttonColorAlpha = voiceRecordingUIMap.get(UIConfigConstants.VOICE_BUTTON_COLOR_ALPHA);
        String backgroundColor = voiceRecordingUIMap.get(UIConfigConstants.VOICE_BACKGROUND_COLOR);
        String backgroundColorAlpha = voiceRecordingUIMap.get(UIConfigConstants.VOICE_BACKGROUND_COLOR_ALPHA);
        String textLabelColor = voiceRecordingUIMap.get(UIConfigConstants.VOICE_TEXT_LABEL_COLOR);
        String textLabelColorAlpha = voiceRecordingUIMap.get(UIConfigConstants.VOICE_TEXT_LABEL_COLOR_ALPHA);
        String titleLabelColor = voiceRecordingUIMap.get(UIConfigConstants.VOICE_TITLE_LABEL_COLOR);
        String titleLabelColorAlpha = voiceRecordingUIMap.get(UIConfigConstants.VOICE_TITLE_LABEL_COLOR_ALPHA);
        String titleLabelSize = voiceRecordingUIMap.get(UIConfigConstants.VOICE_TITLE_LABEL_SIZE);
        String textLabelSize = voiceRecordingUIMap.get(UIConfigConstants.VOICE_TEXT_LABEL_SIZE);
        String counterLabelSize = voiceRecordingUIMap.get(UIConfigConstants.VOICE_COUNTER_LABEL_SIZE);
        String displayPosition = voiceRecordingUIMap.get(UIConfigConstants.VOICE_DISPLAY_POSITION);
        String autoPlay = voiceRecordingUIMap.get(UIConfigConstants.VOICE_AUTO_PLAY);
        String titleLabelTop = voiceRecordingUIMap.get(UIConfigConstants.VOICE_TITLE_LABEL_TOP);

        voiceDisplayPositionSpinner.setSelection(getPositionInPair(voiceDisplayPositionList, displayPosition));

        voiceTitleOnTopSwitch.setChecked(titleLabelTop.equalsIgnoreCase("Y") ? true : false);
        voiceAutoPlaySwitch.setChecked(autoPlay.equalsIgnoreCase("Y") ? true : false);

        voiceButtonColorEdtTxt.setText(buttonColor);
        voiceButtonColorAlphaEdtTxt.setText(buttonColorAlpha);
        voiceBackgroundColorEdtTxt.setText(backgroundColor);
        voiceBackgroundColorAlphaEdtTxt.setText(backgroundColorAlpha);
        voiceTextColorEdtTxt.setText(textLabelColor);
        voiceTextColorAlphaEdtTxt.setText(textLabelColorAlpha);
        voiceTitleColorEdtTxt.setText(titleLabelColor);
        voiceTitleColorAlphaEdtTxt.setText(titleLabelColorAlpha);
        voiceTitleLabelSizeEdtTxt.setText(titleLabelSize);
        voiceTextLabelSizeEdtTxt.setText(textLabelSize);
        voiceCounterLabelSizeEdtTxt.setText(counterLabelSize);

        voiceLabelSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, setToArray(voiceRecordingLabelMap.keySet())));

    }

    private void updateVoiceRecordingConfig(String featureType) {

        HashMap<String, String> ui = null;

        ui = CustomizeUIConfigManager.getDefaultVoiceRecordingConfig().getUIConfiguration();

        if (ui != null) {
            ui.put(UIConfigConstants.VOICE_BUTTON_COLOR, voiceButtonColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.VOICE_BUTTON_COLOR_ALPHA, voiceButtonColorAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.VOICE_BACKGROUND_COLOR, voiceBackgroundColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.VOICE_BACKGROUND_COLOR_ALPHA, voiceBackgroundColorAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.VOICE_TEXT_LABEL_COLOR, voiceTextColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.VOICE_TEXT_LABEL_COLOR_ALPHA, voiceTextColorAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.VOICE_TITLE_LABEL_COLOR, voiceTitleColorEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.VOICE_TITLE_LABEL_COLOR_ALPHA, voiceTitleColorAlphaEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.VOICE_TITLE_LABEL_SIZE, voiceTitleLabelSizeEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.VOICE_TEXT_LABEL_SIZE, voiceTextLabelSizeEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.VOICE_COUNTER_LABEL_SIZE, voiceCounterLabelSizeEdtTxt.getText().toString().trim());
            ui.put(UIConfigConstants.VOICE_DISPLAY_POSITION, ((Pair) voiceDisplayPositionSpinner.getSelectedItem()).second.toString());
            ui.put(UIConfigConstants.VOICE_TITLE_LABEL_TOP, voiceTitleOnTopSwitch.isChecked() ? "Y" : "N");
            ui.put(UIConfigConstants.VOICE_AUTO_PLAY, voiceAutoPlaySwitch.isChecked() ? "Y" : "N");

            CustomizeUIConfigManager.storeConfig(getActivity(), featureType);
            //Log.d("", "" + ui);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String current_selected = ((Pair) spinnerUIConfig.getSelectedItem()).second.toString();

                if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_FRONT)) {
                    PreferenceUtils.setPreference(getActivity(), CustomizeUIConfigManager.ID_CAPTURE_FRONT, "");
                    CustomizeUIConfigManager.initCustomizeUIConfig(getActivity());
                    setIDCaptureDefaultValues();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_BACK)) {
                    PreferenceUtils.setPreference(getActivity(), CustomizeUIConfigManager.ID_CAPTURE_BACK, "");
                    CustomizeUIConfigManager.initCustomizeUIConfig(getActivity());
                    setIDCaptureDefaultValues();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FACE_CAPTURE)) {
                    PreferenceUtils.setPreference(getActivity(), CustomizeUIConfigManager.FACE_CAPTURE, "");
                    CustomizeUIConfigManager.initCustomizeUIConfig(getActivity());
                    setSelfieCaptureDefaultValues();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.DOCUMENT_CAPTURE)) {
                    PreferenceUtils.setPreference(getActivity(), CustomizeUIConfigManager.DOCUMENT_CAPTURE, "");
                    CustomizeUIConfigManager.initCustomizeUIConfig(getActivity());
                    setIDCaptureDefaultValues();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SNIPPET_CAPTURE)) {
                    PreferenceUtils.setPreference(getActivity(), CustomizeUIConfigManager.SNIPPET_CAPTURE, "");
                    CustomizeUIConfigManager.initCustomizeUIConfig(getActivity());
                    setIDCaptureDefaultValues();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FINGER_4F_CAPTURE)) {
                    PreferenceUtils.setPreference(getActivity(), CustomizeUIConfigManager.FINGER_4F_CAPTURE, "");
                    CustomizeUIConfigManager.initCustomizeUIConfig(getActivity());
                    setFPCaptureDefaultValues();
                } else if(current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_FRONT)) {
                    PreferenceUtils.setPreference(getActivity(), CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_FRONT, "");
                    CustomizeUIConfigManager.initCustomizeUIConfig(getActivity());
                    setIDCaptureDefaultValues();
                } else if(current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_BACK)) {
                    PreferenceUtils.setPreference(getActivity(), CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_BACK, "");
                    CustomizeUIConfigManager.initCustomizeUIConfig(getActivity());
                    setIDCaptureDefaultValues();
                }else if(current_selected.equalsIgnoreCase(CustomizeUIConfigManager.VOICE_RECORDING)) {
                    PreferenceUtils.setPreference(getActivity(), CustomizeUIConfigManager.VOICE_RECORDING, "");
                    CustomizeUIConfigManager.initCustomizeUIConfig(getActivity());
                    setVoiceRecordingDefaultValues();
                }

            }
        });

        addLabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String current_selected = ((Pair) spinnerUIConfig.getSelectedItem()).second.toString();

                HashMap<String, String> labelConfig = null;
                if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_FRONT)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultIDCaptureFrontConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_BACK)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultIDCaptureBackConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FACE_CAPTURE)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultFaceCaptureConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.DOCUMENT_CAPTURE)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultDocCaptureConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SNIPPET_CAPTURE)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultSnippetCaptureConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FINGER_4F_CAPTURE)) {
//                    labelConfig = CustomizeUIConfigManager.get.getLabelConfiguration();
                }

                if (null != labelConfig) {
                    labelConfig.put(spinnerLabel.getSelectedItem().toString().toString(), labelValueEdtTxt.getText().toString().trim());
                }

                labelValueEdtTxt.setText("");
                //Log.d("", "" + labelConfig);
            }
        });

        addFaceLabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String current_selected = ((Pair) spinnerUIConfig.getSelectedItem()).second.toString();

                HashMap<String, String> labelConfig = null;
                if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_FRONT)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultIDCaptureFrontConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_BACK)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultIDCaptureBackConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FACE_CAPTURE)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultFaceCaptureConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.DOCUMENT_CAPTURE)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultDocCaptureConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SNIPPET_CAPTURE)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultSnippetCaptureConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FINGER_4F_CAPTURE)) {
//                    labelConfig = CustomizeUIConfigManager.get.getLabelConfiguration();
                }

                if (null != labelConfig) {
                    labelConfig.put(faceSpinnerLabel.getSelectedItem().toString().toString(), labelValueFaceEdtTxt.getText().toString().trim());
                }

                Log.d("", "" + labelConfig);
                labelValueFaceEdtTxt.setText("");

            }
        });

        voiceAddLabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current_selected = ((Pair) spinnerUIConfig.getSelectedItem()).second.toString();

                HashMap<String, String> labelConfig = null;
                if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_FRONT)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultIDCaptureFrontConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_BACK)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultIDCaptureBackConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FACE_CAPTURE)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultFaceCaptureConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.DOCUMENT_CAPTURE)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultDocCaptureConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SNIPPET_CAPTURE)) {
                    labelConfig = CustomizeUIConfigManager.getDefaultSnippetCaptureConfig().getLabelConfiguration();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FINGER_4F_CAPTURE)) {
//                    labelConfig = CustomizeUIConfigManager.get.getLabelConfiguration();
                }else if(current_selected.equalsIgnoreCase(CustomizeUIConfigManager.VOICE_RECORDING)){
                    labelConfig = CustomizeUIConfigManager.getDefaultVoiceRecordingConfig().getLabelConfiguration();
                }

                if (null != labelConfig) {
                    labelConfig.put(voiceLabelSpinner.getSelectedItem().toString().toString(), voiceLabelValueEdtTxt.getText().toString().trim());
                }

                Log.d("", "" + labelConfig);
                voiceLabelValueEdtTxt.setText("");
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String current_selected = ((Pair) spinnerUIConfig.getSelectedItem()).second.toString();

                if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FACE_CAPTURE)) {
                    updateSelfieConfig(current_selected);
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FINGER_4F_CAPTURE)) {
                    updateFingerPrintConfig(current_selected);
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.VOICE_RECORDING)) {
                    updateVoiceRecordingConfig(current_selected);
                } else {
                    updateIDConfig(current_selected);
                }

                FragmentManager fragmentManager = getFragmentManager();
                IDValidationFaceMatch idValidationFaceMatch = new IDValidationFaceMatch();
                Bundle bundle = new Bundle();
                bundle.putSerializable(IDDetails.SKIP_FEATURES_LIST, skipFeaturesList);
                idValidationFaceMatch.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.flContent, idValidationFaceMatch).addToBackStack(null).commit();
                NavigationActivity.toolbar.setTitle(R.string.id_validation);

                hideKeyboard(getActivity(), view);

                ImageProcessingSDK.getInstance().customizeUserInterface(CustomizeUIConfigManager.getCompleteUIConfigJSON());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
                hideKeyboard(getActivity(), view);
            }
        });

        spinnerUIConfig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String current_selected = ((Pair) adapterView.getSelectedItem()).second.toString();

                if (!StringUtil.isEmpty(previously_selected_feature)) {
                    if (previously_selected_feature.equalsIgnoreCase(CustomizeUIConfigManager.FACE_CAPTURE)) {
                        updateSelfieConfig(previously_selected_feature);
                    } else if (previously_selected_feature.equalsIgnoreCase(CustomizeUIConfigManager.FINGER_4F_CAPTURE)) {
                        updateFingerPrintConfig(previously_selected_feature);
                    } else if (previously_selected_feature.equalsIgnoreCase(CustomizeUIConfigManager.VOICE_RECORDING)) {
                        updateVoiceRecordingConfig(previously_selected_feature);
                    } else if (previously_selected_feature.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_FRONT)
                            || previously_selected_feature.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_BACK)
                            || previously_selected_feature.equalsIgnoreCase(CustomizeUIConfigManager.DOCUMENT_CAPTURE)
                            || previously_selected_feature.equalsIgnoreCase(CustomizeUIConfigManager.SNIPPET_CAPTURE)
                            || previously_selected_feature.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_FRONT)
                            || previously_selected_feature.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_BACK)) {
                        updateIDConfig(previously_selected_feature);
                    }
                }

                //Assign only after calling above function(updateIDConfig)
                previously_selected_feature = current_selected;

                idCaptureView.setVisibility(View.GONE);
                selfieCaptureView.setVisibility(View.GONE);
                fingerprintCaptureView.setVisibility(View.GONE);
                voiceRecordingView.setVisibility(View.GONE);

                if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_FRONT)) {
                    idCaptureView.setVisibility(View.VISIBLE);
                    //setIDFrontValues();
                    setIDCaptureDefaultValues();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_BACK)) {
                    idCaptureView.setVisibility(View.VISIBLE);
                    //setIDBackValues();
                    setIDCaptureDefaultValues();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FACE_CAPTURE)) {
                    selfieCaptureView.setVisibility(View.VISIBLE);
                    setSelfieCaptureDefaultValues();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.DOCUMENT_CAPTURE)) {
                    idCaptureView.setVisibility(View.VISIBLE);
                    setIDCaptureDefaultValues();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SNIPPET_CAPTURE)) {
                    idCaptureView.setVisibility(View.VISIBLE);
                    setIDCaptureDefaultValues();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FINGER_4F_CAPTURE)) {
                    setFPCaptureDefaultValues();
                    fingerprintCaptureView.setVisibility(View.VISIBLE);
                } else if(current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_FRONT)) {
                    idCaptureView.setVisibility(View.VISIBLE);
                    setIDCaptureDefaultValues();
                } else if(current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_BACK)) {
                    idCaptureView.setVisibility(View.VISIBLE);
                    setIDCaptureDefaultValues();
                } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.VOICE_RECORDING)) {
                    voiceRecordingView.setVisibility(View.VISIBLE);
                    setVoiceRecordingDefaultValues();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        CustomizeUIConfigManager.initCustomizeUIConfig(getActivity());

        String current_selected = ((Pair) spinnerUIConfig.getSelectedItem()).second.toString();
        if (!StringUtil.isEmpty(current_selected)) {
            if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FACE_CAPTURE)) {
                setSelfieCaptureDefaultValues();
            } else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.FINGER_4F_CAPTURE)) {
                setFPCaptureDefaultValues();
            } else if(current_selected.equalsIgnoreCase(CustomizeUIConfigManager.VOICE_RECORDING)){
                setVoiceRecordingDefaultValues();
            }else if (current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_FRONT)
                    || current_selected.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_BACK)
                    || current_selected.equalsIgnoreCase(CustomizeUIConfigManager.DOCUMENT_CAPTURE)
                    || current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SNIPPET_CAPTURE) || current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_BACK) || current_selected.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_FRONT)) {
                setIDCaptureDefaultValues();
            }
        }

    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private int getPositionInPair(ArrayList<Pair<String, String>> pairList, String value) {
        for (int i = 0; i < pairList.size(); i++) {
            Pair<String, String> pair = pairList.get(i);
            if (value.equalsIgnoreCase(pair.second)) {
                return i;
            }
        }
        return -1;
    }

    private String getFacePosition(String value) {
        String faceOnTop = "Top";
        if (value.equalsIgnoreCase("N")) {
            faceOnTop = "Bottom";
        } else {
            faceOnTop = "Top";
        }
        return faceOnTop;
    }

    private String setJsonFormatFacePosition(String value) {
        String faceOnTop = "Y";
        if (value.equalsIgnoreCase("Bottom")) {
            faceOnTop = "N";
        } else {
            faceOnTop = "Y";
        }
        return faceOnTop;
    }

    private String[] setToArray(Set<String> set) {
        int siz = set.size();
        String[] arr = new String[siz];

        int i = 0;
        for (String s : set) {
            arr[i++] = s;
        }
        return arr;
    }

    private String getConvertedBaset64Image(String selectedImage) {
        Bitmap titleBitmap = null;
        String base64Image=null;
        if (!selectedImage.equalsIgnoreCase("None")) {
            titleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title_image);
            base64Image= BitmapUtils.bitmapToBase64(titleBitmap);
        }else{
            base64Image="";
        }

        return base64Image;

    }
}
