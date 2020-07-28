package com.idmission.libtestproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.idmission.client.BiometricType;
import com.idmission.client.CardImageType;
import com.idmission.client.ColorCode;
import com.idmission.client.FaceImageType;
import com.idmission.client.FingerType;
import com.idmission.client.FingerprintDeviceType;
import com.idmission.client.IDImageType;
import com.idmission.client.IdType;
import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.ImageType;
import com.idmission.client.InitializationException;
import com.idmission.client.RequestResponseFragment;
import com.idmission.client.RequestResponseInterface;
import com.idmission.client.Response;
import com.idmission.client.ResponseStatusCode;
import com.idmission.libtestproject.fragments.ResultData;
import com.idmission.libtestproject.fragments.ResultImageFragment;
import com.idmission.libtestproject.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class MainActivity extends FragmentActivity implements ImageProcessingResponseListener ,RequestResponseInterface {

    private static final String LOGTAG = "TESTAPP";

    //Default values
    private final String mLanguage = "en";

    //SDK Instance
    private ImageProcessingSDK imageProcessingSDK = null;

    //
    private TextView topHeading;
    private Spinner mySpinner, idSpinner, faceImageTypeSpinner, idImageTypeSpinner, faceImageTypeSpinnerM, verificationType, labelKeySpinner, fingerprintDeviceSpinner, fingerTypeSpinner, requestResponseTypeSpinner;
    private Spinner faceTitleAlignmentSpinner, faceHintAlignmentSpinner, faceHintIconAlignmentSpinner, faceTitleImageAlignmentSpinner, titleImageTypeSpinner,idTitleAlignmentSpinner,idHintMsgAlignmentSpinner,idTitleImgMsgAlignmentSpinner,titleImageCaptureTypeSpinner,titleImageCapturePOASpinner;
    private EditText urlEdtTxt, loginIdEdtTxt, passEdtTxt, merchantIdEdtTxt, productIdEdtTxt, productNameEdtTxt, languageEdtTxt;
    private EditText labelValueEngEdtTxt, labelValueEspEdtTxt;
    private EditText faceLightThresholdEdtTxt, faceFocusThresholdEdtTxt, faceDetectionThresholdEdtTxt, maxImageSizeForFaceDetect,
            faceOutlineColorEdtTxt, detectedFaceOutlineColorEdtTxt, outsideFaceOutlineColorEdtTxt,
            faceOutlineColorAlphaEdtTxt, detectedFaceOutlineColorAlphaEdtTxt, outsideFaceOutlineColorAlphaEdtTxt,outsideDetectedFaceOutlineColor,
            outsideDetectedFaceOutlineColorAlpha;
    //            deltaLeftThreshold, deltaRightThreshold, deltaUpThreshold, deltaDownThreshold;;
    private EditText imageLightThresholdEdtTxt, imageMaxFocusThresholdEdtTxt, imageMinFocusThresholdEdtTxt, imageGlarePercentageEdtTxt,
            enableCaptureButtonTimeEdtTxt, maxImageSize, imageHeight, imageWidth, idImageOutlineColorEdtTxt, idImageOutlineAlphaEdtTxt,
            detectedIdOutlineColorEdtTxt, detectedIdOutlineAlphaEdtTxt, idImgOutsideOutlineColorEdtTxt, idImgOutsideOutlineAlphaEdtTxt,detectedidImgOutsideOutlineColorEdtTxt,detectedidImgOutsideOutlineAlphaEdtTxt;
    private EditText imageLightThresholdPOAEdtTxt, imageMaxFocusThresholdPOAEdtTxt, imageMinFocusThresholdPOAEdtTxt, imageGlarePercentagePOAEdtTxt,
            enableCaptureButtonTimePOAEdtTxt, maxImageSizePOAEdtTxt, imageHeightPOAEdtTxt, imageWidthPOAEdtTxt,
            idImageOutlineColorPOAEdtTxt, idImageOutlineAlphaPOAEdtTxt,detectedIdOutlineColorPOAEdtTxt,detectedIdOutlineColorAlphaPOA, idImageOutsideOutlineColorPOAEdtTxt, idImageOutsideOutlineAlphaPOAEdtTxt,detectedidImageOutsideOutlineColorPOAEdtTxt,detectedidImageOutsideOutlineAlphaPOAEdtTxt;
    private EditText customerName, uniqueCustomerNumber, uniqueMerchantNumber, serviceID, customerType, customerAttribute, customerPhone,
            customerEmail, uniqueEmployeeCode, uniqueEmployeeNumber, oldClientCustomerNumber;
    private EditText voiceRecordingTimingEdtTxt, fourFingerCaptureFocusedRatio, fourFingerCaptureGlareRatio,  numOfFingerToCheckFocus, videoRecordingTimingEdtTxt,textOnVideoScreen;
    private EditText deviceTimeoutEdtTxt, minImageSizeEdtTxt, minNFIQEdtTxt;
    private EditText emailID, mobileNumber,received_otp;
    private Spinner otpNotificationType;
    private ProgressDialog progressDialog;
    private CheckBox launchFrontCameraCheckBox, toggleCameraCheckBox, showPreviewScreenCheckBox, capturePortraitCB, capturePortraitChcBoxPOA, enableDebugCB,
            isFinalSubmitProcessImage, isFinalSubmitMatchFace, isFinalSubmitProcImgMatchFace, clearFormKeyProcessImage, clearFormKeyMatchFace, clearFormKeyProcImgMatchFace,hide_Id_Capture_Title,hide_Id_Capture_HintMsg,hide_Id_Capture_TitleImg,hide_Face_Title,hide_Face_hintMsg,hide_Face_hintIcon,hide_Face_titleImage,
            clearFormKeyFingerprint, showInstructionIdCapture, showInstructionFaceDetect, showInstructionPOA;
    private Button captureFrontButton, captureBackButton, captureFaceButton, capturePOAButton, captureBankStatementButton, captureBirthCertificateButton,
            verifyCustomerButton, addLabelButton, initLabelsButton, startVoiceRecordingButton, stopVoiceRecordingButton, getGPSLocationButton, fourFingerCapture,
            customizeUIButton, captureFingerprintButton, clearFingerprintButton, clearAllFingerprintButton, enrollFingerprintButton, verifyFingerprintButton, videoCaptureButton;
    private Button pickFrontImageBtn, pickBackImageBtn, pickFaceImageBtn, setImagesBtn,requestResponseBtn;

    //Constants for storing preference values
    private static final String SDK_SETTINGS = "SDKSettings", URL = "URL", LOGINID = "LOGINID", PASSWORD = "PASSWORD",
            MERCHANTID = "MERCHANTID", PRODUCTID = "PRODUCTID", PRODUCTNAME = "PRODUCTNAME", LANGUAGE = "LANGUAGE",AUTHORIZE_TOKEN="AUTHORIZETOKEN";
    private static final String IMAGE_LIGHT_THRESHOLD = "IMAGE_LIGHT_THRESHOLD", IMAGE_MAX_FOCUS_THRESHOLD = "IMAGE_MAX_FOCUS_THRESHOLD", IMAGE_MIN_FOCUS_THRESHOLD = "IMAGE_MIN_FOCUS_THRESHOLD",
            IMAGE_GLARE_PERCENTAGE = "IMAGE_GLARE_PERCENTAGE", CAPTURE_BUTTON_TIME = "CAPTURE_BUTTON_TIME", MAX_IMAGE_SIZE = "MAX_IMAGE_SIZE",
            IMAGE_HEIGHT = "IMAGE_HEIGHT", IMAGE_WIDTH = "IMAGE_WIDTH", ID_IMG_OUTLINE_COLOR = "ID_IMG_OUTLINE_COLOR", ID_IMG_OUTLINE_ALPHA = "ID_IMG_OUTLINE_ALPHA",
            DETECTED_ID_IMG_OUTLINE_COLOR = "DETECTED_ID_IMG_OUTLINE_COLOR", DETECTED_ID_IMG_OUTLINE_ALPHA = "DETECTED_ID_IMG_OUTLINE_ALPHA", ID_IMG_OUTSIDE_OUTLINE_COLOR = "ID_IMG_OUTSIDE_OUTLINE_COLOR", ID_IMG_OUTSIDE_OUTLINE_ALPHA = "ID_IMG_OUTSIDE_OUTLINE_ALPHA",
            DETECTED_ID_IMG_OUTSIDE_OUTLINE = "DETECTED_ID_IMG_OUTSIDE_OUTLINE",DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA = "DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA";;
    private static final String IMAGE_LIGHT_THRESHOLD_POA = "IMAGE_LIGHT_THRESHOLD_POA", IMAGE_MAX_FOCUS_THRESHOLD_POA = "IMAGE_MAX_FOCUS_THRESHOLD_POA", IMAGE_MIN_FOCUS_THRESHOLD_POA = "IMAGE_MIN_FOCUS_THRESHOLD_POA",
            IMAGE_GLARE_PERCENTAGE_POA = "IMAGE_GLARE_PERCENTAGE_POA", CAPTURE_BUTTON_TIME_POA = "CAPTURE_BUTTON_TIME_POA", MAX_IMAGE_SIZE_POA = "MAX_IMAGE_SIZE_POA",
            IMAGE_HEIGHT_POA = "IMAGE_HEIGHT_POA", IMAGE_WIDTH_POA = "IMAGE_WIDTH_POA", ID_IMG_POA_OUTLINE_COLOR = "ID_IMG_POA_OUTLINE_COLOR", ID_IMG_POA_OUTLINE_ALPHA = "ID_IMG_POA_OUTLINE_ALPHA",
            DETECTED_ID_POA_OUTLINE_COLOR = "DETECTED_ID_POA_OUTLINE_COLOR", DETECTED_ID_POA_OUTLINE_ALPHA = "DETECTED_ID_POA_OUTLINE_ALPHA", ID_IMG_POA_OUTSIDE_OUTLINE_COLOR = "ID_IMG_POA_OUTSIDE_OUTLINE_COLOR", ID_IMG_POA_OUTSIDE_OUTLINE_ALPHA = "ID_IMG_POA_OUTSIDE_OUTLINE_ALPHA",
            DETECTED_ID_IMG_POA_OUTSIDE_OUTLINE_COLOR = "DETECTED_ID_IMG_POA_OUTSIDE_OUTLINE_COLOR",DETECTED_ID_IMG_POA_OUTSIDE_OUTLINE_ALPHA = "DETECTED_ID_IMG_POA_OUTSIDE_OUTLINE_ALPHA";
    private static final String CUSTOMER_NAME="Customer_Name", UNIQUE_CUSTOMER_NUMBER="Unique_Customer_Number", UNIQUE_MERCHANT_NUMBER="Unique_Merchant_Number", SERVICE_ID="Service_ID", CUSTOMER_TYPE="Customer_Type",
            CUSTOMER_ATTRIBUTE="Customer_Attribute", CUSTOMER_PHONE="Customer_Phone", CUSTOMER_EMAIL="Customer_Email", UNIQUE_EMPLOYEE_CODE="Unique_Employee_Code", UNIQUE_EMPLOYEE_NUMBER="Unique_Employee_Number";
    private static final String FACE_LIGHT_THRESHOLD = "FACE_LIGHT_THRESHOLD", FACE_FOCUS_THRESHOLD = "FACE_FOCUS_THRESHOLD", FACE_DETECTION_THRESHOLD = "FACE_DETECTION_THRESHOLD",
            FACE_IMAGE_SIZE = "FACE_IMAGE_SIZE", FACE_OUTLINE_COLOR = "FACE_OUTLINE_COLOR", DETECTED_FACE_OUTLINE_COLOR = "DETECTED_FACE_OUTLINE_COLOR", OUTSIDE_FACE_OUTLINE_COLOR = "OUTSIDE_FACE_OUTLINE_COLOR",
            FACE_OUTLINE_COLOR_ALPHA = "FACE_OUTLINE_COLOR_ALPHA", DETECTED_FACE_OUTLINE_COLOR_ALPHA = "DETECTED_FACE_OUTLINE_COLOR_ALPHA", OUTSIDE_FACE_OUTLINE_COLOR_ALPHA = "OUTSIDE_FACE_OUTLINE_COLOR_ALPHA",
            DETECTED_FACE_OUTSIDE_OUTLINE_COLOR="DETECTED_FACE_OUTSIDE_OUTLINE_COLOR",DETECTED_FACE_OUTSIDE_OUTLINE_COLOR_ALPHA="DETECTED_FACE_OUTSIDE_OUTLINE_COLOR_ALPHA", DELTA_LEFT_THRESHOLD="DELTA_LEFT_THRESHOLD", DELTA_RIGHT_THRESHOLD = "DELTA_RIGHT_THRESHOLD", DELTA_UP_THRESHOLD = "DELTA_UP_THRESHOLD", DELTA_DOWN_THRESHOLD = "DELTA_DOWN_THRESHOLD";
    private static final String FINGER_DEVICE_TIMEOUT = "FINGER_DEVICE_TIMEOUT", FINGER_MIN_IMAGESIZE = "FINGER_MIN_IMAGESIZE", FINGER_MIN_NFIQ = "FINGER_MIN_NFIQ";

    private static final String[] LABELS_KEY = {"align_document_img_capture", "subject_is_too_dark_img_capture",
            "out_of_focus_img_capture", "too_much_glare_img_capture", "subject_is_too_dark_fc_detect", "out_of_focus_fc_detect", "move_camera_closer_to_your_face",
            "camera_movement_fc_detect","keep_face_steady", "smile_please", "face_detected", "light", "focus", "glare", "smile", "page_title_image_capture", "page_title_face_detection",
            "id_capture_instruction", "face_detection_instruction", "face_detection_keep_mobile_straight"};
    private static final String[] FACE_TITLE_ALIGNMENT = {"Bottom", "Top"};
    private static final String[] FACE_HINT_MSG_ALIGNMENT = {"Top", "Bottom"};
    private static final String[] FACE_HINT_ICON_ALIGNMENT = {"Top", "Bottom"};
    private static final String[] FACE_TITILE_IMAGE_ALIGNMENT = {"Top", "Bottom"};
    private static final String[] TITILE_IMAGE_TYPE = {"None", "Title Image 1"};
    private static final String[] EMPLOYEE_TYPE = {"ADMIN", "MANAGER","SUPERVISOR"};
    private static final String[] EMPLOYEE_GENDER_TYPE = {"M", "F"};
    private static final String[] OTP_NOTIFICATION_TYPE = {"Email","SMS","Both"};

    //Added
    private static final String[] ID_CAPTURE_TITLE_ALIGNMENT = {"Top", "Center","Bottom"};
    private static final String[] ID_CAPTURE_HINT_MSG_ALIGNMENT = {"Center", "Bottom","Top"};
    private static final String[] ID_CAPTURE_TITLE_IMG_MSG_ALIGNMENT = {"Bottom", "Top","Center"};
    //ResultView Fields

    //Request Response
    private static final String[] REQUEST_RESPONSE_KEY = {"Process_Image","Match_Face","Verify_Customer","Process_Image_And_Match_Face","Complete_Operation"
    };

    private TextView resultTv;
    private ImageView voiceRecordingPlayer;
    private ViewPager viewPager;
    private ResultImagePagerAdapter resultImagePagerAdapter;
    private MediaPlayer mediaPlayer;
    //put captured image in this object to display in viewpager
    public static LinkedHashMap<String, ResultData> capturedImageData = new LinkedHashMap<>();

    private HashMap<String, String> englishLabelMap = new HashMap<>();
    private HashMap<String, String> spanishLabelMap = new HashMap<>();
    private HashMap<String, String> snippetFieldNameMap = new HashMap<>();

    private Button signatureButton, captureGenericDocButton, capturePOAVerification, generateTokenButton, verifyToken;
    private EditText fieldNameEdtTxt, address1EdtTxt, address2EdtTxt, countryEdtTxt, stateEdtTxt, zipCodeEdtTxt, addFieldEdtTxt;
    private EditText empCodeEdtTxt, empLoginIdEdtTxt, empEmailEdtTxt, companyIdEdtTxt, departmentEdtTxt, empNameEdtTxt, empMobNumberEdtTxt, empCountryEdtTxt, empAddressLine1, empAddressLine2, empZipCode, empSpouseName, empNoOfChildren;//for Employee
    private CheckBox doVerify, doExtract, isFinalSubmit, empIsFinalSubmit,clearFormKeyCustVerification,clearFormKeyEmpVerification,captureEnable;//for POA
    private Spinner empTypeSpinner, employeeGenderSpinner;
    private Button createEmployeeButton, verifyEmployeeButton, matchFaceCreateEmployee, addField, snippetCapture;

    VideoView videoView;
    Button playPauseVideoButton;
    FrameLayout layoutVideoView;
    LinearLayout layoutButtonPlayPAuse;

    private final int FRONT_IMG_REQ_CODE = 101, BACK_IMG_REQ_CODE = 102, FACE_IMG_REQ_CODE = 103;
    private HashMap<String, String> requestImageMap = new HashMap<>();

    private boolean enableFingerprint = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        setDefaultValue();

        initializeListener();

    }

    private void initializeViews(){

        topHeading = (TextView)findViewById(R.id.top_heading);
        topHeading.setText(topHeading.getText() + "( " + StringUtil.getApplicationVersionName(this) + " )");

        //Initialization parameter
        urlEdtTxt = (EditText) findViewById(R.id.url_ET);
        loginIdEdtTxt = (EditText) findViewById(R.id.loginid_ET);
        passEdtTxt = (EditText) findViewById(R.id.password_ET);
        merchantIdEdtTxt = (EditText) findViewById(R.id.merchantid_ET);
        productIdEdtTxt = (EditText) findViewById(R.id.productid_ET);
        productNameEdtTxt = (EditText) findViewById(R.id.productname_ET);
        languageEdtTxt = (EditText) findViewById(R.id.language_ET);
        enableDebugCB = (CheckBox)findViewById(R.id.enable_debug);

        //Label setting parameter
        labelKeySpinner = (Spinner)findViewById(R.id.label_key_spinner);
        labelValueEngEdtTxt = (EditText)findViewById(R.id.label_value_eng_edttxt);
        labelValueEspEdtTxt = (EditText)findViewById(R.id.label_value_esp_edttxt);
        addLabelButton = (Button)findViewById(R.id.add_label_btn);
        initLabelsButton = (Button)findViewById(R.id.initialize_labels);

        //UI Alignment
        faceTitleAlignmentSpinner = (Spinner) findViewById(R.id.face_title_alignment);
        faceHintAlignmentSpinner = (Spinner) findViewById(R.id.face_hint_msg_alignment);
        faceHintIconAlignmentSpinner = (Spinner) findViewById(R.id.face_hint_icon_alignment);
        faceTitleImageAlignmentSpinner = (Spinner) findViewById(R.id.face_title_image_alignment);
        titleImageTypeSpinner = (Spinner) findViewById(R.id.set_title_image);

        hide_Face_Title = (CheckBox) findViewById(R.id.hide_Face_Title);
        hide_Face_hintMsg = (CheckBox) findViewById(R.id.hide_Face_hintMsg);
        hide_Face_hintIcon = (CheckBox) findViewById(R.id.hide_Face_hintIcon);
        hide_Face_titleImage = (CheckBox) findViewById(R.id.hide_Face_titleImage);

        idTitleAlignmentSpinner = (Spinner) findViewById(R.id.id_capture_title_alignment);
        idHintMsgAlignmentSpinner = (Spinner) findViewById(R.id.id_capture_hint_msg_alignment);
        idTitleImgMsgAlignmentSpinner = (Spinner) findViewById(R.id.id_capture_title_img_alignment);
        titleImageCaptureTypeSpinner = (Spinner) findViewById(R.id.set_title_imageCapture);
        titleImageCapturePOASpinner = (Spinner) findViewById(R.id.set_title_image_poa);
        hide_Id_Capture_Title = (CheckBox) findViewById(R.id.hide_IDCapture_Title);
        hide_Id_Capture_HintMsg = (CheckBox) findViewById(R.id.hide_IDCapture_hintMsg);
        hide_Id_Capture_TitleImg = (CheckBox) findViewById(R.id.hide_IDCapture_titleImg);

        faceTitleAlignmentSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, FACE_TITLE_ALIGNMENT));
        faceHintAlignmentSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, FACE_HINT_MSG_ALIGNMENT));
        faceHintIconAlignmentSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, FACE_HINT_ICON_ALIGNMENT));
        faceTitleImageAlignmentSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, FACE_TITILE_IMAGE_ALIGNMENT));
        titleImageTypeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TITILE_IMAGE_TYPE));

        idTitleAlignmentSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ID_CAPTURE_TITLE_ALIGNMENT));
        idHintMsgAlignmentSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ID_CAPTURE_HINT_MSG_ALIGNMENT));
        idTitleImgMsgAlignmentSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ID_CAPTURE_TITLE_IMG_MSG_ALIGNMENT));
        titleImageCaptureTypeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TITILE_IMAGE_TYPE));
        titleImageCapturePOASpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TITILE_IMAGE_TYPE));
        customizeUIButton = (Button)findViewById(R.id.customize_ui_btn);

        //Additional parameter
        customerName = (EditText)findViewById(R.id.customer_name);
        uniqueCustomerNumber = (EditText)findViewById(R.id.unique_customer_number);
        uniqueMerchantNumber = (EditText)findViewById(R.id.unique_merchant_number);
        serviceID = (EditText)findViewById(R.id.service_id);
        customerType = (EditText)findViewById(R.id.customer_type);
        customerAttribute = (EditText)findViewById(R.id.customer_attribute);
        customerPhone = (EditText)findViewById(R.id.customer_phone);
        customerEmail = (EditText)findViewById(R.id.customer_email);
        uniqueEmployeeCode = (EditText)findViewById(R.id.unique_employee_code);
        uniqueEmployeeNumber = (EditText)findViewById(R.id.unique_employee_number);
        oldClientCustomerNumber = (EditText)findViewById(R.id.Old_Client_Customer_Number);

        //Image-Capture configuration edittext
        imageLightThresholdEdtTxt = (EditText) findViewById(R.id.imageCaptureLightThreshold_ET);
        imageMaxFocusThresholdEdtTxt = (EditText) findViewById(R.id.imageCaptureMaxFocusThreshold_ET);
        imageMinFocusThresholdEdtTxt = (EditText) findViewById(R.id.imageCaptureMinFocusThreshold_ET);
        imageGlarePercentageEdtTxt = (EditText) findViewById(R.id.imageCaptureGlarePercentage_ET);
        enableCaptureButtonTimeEdtTxt = (EditText) findViewById(R.id.enableCaptureButtonTiming_ET);
        maxImageSize = (EditText) findViewById(R.id.maxImageSize_ET);
        imageHeight = (EditText) findViewById(R.id.imageHeight_ET);
        imageWidth = (EditText) findViewById(R.id.imageWidth_ET);
        idImageOutlineColorEdtTxt = (EditText)findViewById(R.id.id_outline_color);
        idImageOutlineAlphaEdtTxt = (EditText)findViewById(R.id.id_outline_color_alpha);
        detectedIdOutlineColorEdtTxt = (EditText)findViewById(R.id.detected_id_outline_color);
        detectedIdOutlineAlphaEdtTxt = (EditText)findViewById(R.id.detected_id_outline_color_alpha);
        idImgOutsideOutlineColorEdtTxt = (EditText)findViewById(R.id.outside_id_outline_color);
        idImgOutsideOutlineAlphaEdtTxt = (EditText)findViewById(R.id.outside_id_outline_color_alpha);
        detectedidImgOutsideOutlineColorEdtTxt = (EditText) findViewById(R.id.detected_outside_id_outline_color);
        detectedidImgOutsideOutlineAlphaEdtTxt = (EditText) findViewById(R.id.detected_outside_id_outline_color_alpha);

        capturePortraitCB = (CheckBox)findViewById(R.id.capture_portrait);
        showInstructionIdCapture = (CheckBox)findViewById(R.id.show_instruction_id_capture);
        captureFrontButton = (Button)findViewById(R.id.startImageProcessing);
        captureBackButton = (Button)findViewById(R.id.startImageProcessing1);
        isFinalSubmitProcessImage = (CheckBox)findViewById(R.id.isFinalSubmit_processimage);
        clearFormKeyProcessImage = (CheckBox)findViewById(R.id.clearFormKey_processimage);

        //Face-Detection configuration edittext
        faceLightThresholdEdtTxt = (EditText) findViewById(R.id.faceLightThreshold_ET);
        faceFocusThresholdEdtTxt = (EditText) findViewById(R.id.faceFocusThreshold_ET);
        faceDetectionThresholdEdtTxt = (EditText) findViewById(R.id.faceDetectionThreshold_ET);
        maxImageSizeForFaceDetect = (EditText) findViewById(R.id.maxImageSize_FACE_ET);
        launchFrontCameraCheckBox = (CheckBox)findViewById(R.id.launchFrontCameraCheckBox);
        toggleCameraCheckBox = (CheckBox) findViewById(R.id.toggleCameraCheckBox);
        showPreviewScreenCheckBox = (CheckBox)findViewById(R.id.showPreviewScreenCheckBox);
        faceOutlineColorEdtTxt = (EditText)findViewById(R.id.face_outline_normal);
        faceOutlineColorAlphaEdtTxt = (EditText)findViewById(R.id.face_outline_normal_alpha);
        detectedFaceOutlineColorEdtTxt = (EditText)findViewById(R.id.face_outline_detected);
        detectedFaceOutlineColorAlphaEdtTxt = (EditText)findViewById(R.id.face_outline_detected_alpha);
        outsideFaceOutlineColorEdtTxt = (EditText)findViewById(R.id.outside_face_outline_color);
        outsideFaceOutlineColorAlphaEdtTxt = (EditText)findViewById(R.id.outside_face_outline_color_alpha);
        outsideDetectedFaceOutlineColor = (EditText) findViewById(R.id.outside_detected_face_outline_color);
        outsideDetectedFaceOutlineColorAlpha = (EditText) findViewById(R.id.outside_detected_face_outline_color_alpha);
//        deltaLeftThreshold = (EditText) findViewById(R.id.delta_left_threshold);
//        deltaRightThreshold = (EditText) findViewById(R.id.delta_right_threshold);
//        deltaUpThreshold = (EditText) findViewById(R.id.delta_up_threshold);
//        deltaDownThreshold = (EditText) findViewById(R.id.delta_down_threshold);

        showInstructionFaceDetect = (CheckBox)findViewById(R.id.show_instruction_face_detect);
        captureFaceButton = (Button)findViewById(R.id.captureFace);
        isFinalSubmitMatchFace = (CheckBox)findViewById(R.id.isFinalSubmit_matchface);
        clearFormKeyMatchFace = (CheckBox)findViewById(R.id.clearFormKey_matchface);

        //Verify customer button
        verifyCustomerButton = (Button)findViewById(R.id.verifyCustomerButton);


        isFinalSubmitProcImgMatchFace = (CheckBox)findViewById(R.id.isFinalSubmit_process_and_match);
        clearFormKeyProcImgMatchFace = (CheckBox)findViewById(R.id.clearFormKey_process_and_match);

        //Proof-Of-Address configuration edittext
        imageLightThresholdPOAEdtTxt = (EditText) findViewById(R.id.imageCaptureLightThreshold_POA_ET);
        imageMaxFocusThresholdPOAEdtTxt = (EditText) findViewById(R.id.imageCaptureMaxFocusThreshold_POA_ET);
        imageMinFocusThresholdPOAEdtTxt = (EditText) findViewById(R.id.imageCaptureMinFocusThreshold_POA_ET);
        imageGlarePercentagePOAEdtTxt = (EditText) findViewById(R.id.imageCaptureGlarePercentage_POA_ET);
        enableCaptureButtonTimePOAEdtTxt = (EditText) findViewById(R.id.enableCaptureButtonTiming_POA_ET);
        maxImageSizePOAEdtTxt = (EditText) findViewById(R.id.maxImageSize_POA_ET);
        imageHeightPOAEdtTxt = (EditText) findViewById(R.id.imageHeight_POA_ET);
        imageWidthPOAEdtTxt = (EditText) findViewById(R.id.imageWidth_POA_ET);
        idImageOutlineColorPOAEdtTxt = (EditText) findViewById(R.id.id_outline_color_for_POA);
        idImageOutlineAlphaPOAEdtTxt = (EditText) findViewById(R.id.id_outline_color_alpha_for_POA);
        detectedIdOutlineColorPOAEdtTxt = (EditText) findViewById(R.id.detected_id_outline_color_for_POA);
        detectedIdOutlineColorAlphaPOA = (EditText) findViewById(R.id.detected_id_outline_color_alpha_for_POA);
        idImageOutsideOutlineColorPOAEdtTxt = (EditText) findViewById(R.id.id_outside_outline_color_for_POA);
        idImageOutsideOutlineAlphaPOAEdtTxt = (EditText) findViewById(R.id.id_outside_outline_alpha_for_POA);
        detectedidImageOutsideOutlineColorPOAEdtTxt = (EditText) findViewById(R.id.detected_id_outside_outline_color_for_POA);
        detectedidImageOutsideOutlineAlphaPOAEdtTxt = (EditText) findViewById(R.id.detected_id_outside_outline_alpha_for_POA);
        capturePortraitChcBoxPOA = (CheckBox)findViewById(R.id.capture_portrait_for_POA);
        showInstructionPOA = (CheckBox)findViewById(R.id.show_instruction_poa);

        //Proof Of Address button
        capturePOAButton = (Button)findViewById(R.id.capturePOA);

        //Bank-Statement Fields
        captureBankStatementButton = (Button)findViewById(R.id.captureBankStatement);

        //Birth-Certificate Fields
        captureBirthCertificateButton = (Button)findViewById(R.id.captureBirthCertificate);

        //Set images
        pickFrontImageBtn = (Button)findViewById(R.id.pickFrontImage);
        pickBackImageBtn = (Button)findViewById(R.id.pickBackImage);
        pickFaceImageBtn = (Button)findViewById(R.id.pickFaceImage);
        setImagesBtn = (Button)findViewById(R.id.setImages);

        //request response
        requestResponseBtn = (Button) findViewById(R.id.request_response_button);

        //Voice recording Fields
        voiceRecordingTimingEdtTxt = (EditText)findViewById(R.id.voice_recording_timing);
        startVoiceRecordingButton = (Button) findViewById(R.id.start_voice_record_button);
        stopVoiceRecordingButton = (Button)findViewById(R.id.stop_voice_record_button);

        //Video Recording Capture
        videoRecordingTimingEdtTxt = (EditText)findViewById(R.id.video_recording_timing);
        textOnVideoScreen = (EditText)findViewById(R.id.textOnVideoScreen);
        videoCaptureButton = (Button)findViewById(R.id.videoCaptureButton);
        layoutVideoView = (FrameLayout) findViewById(R.id.layout_videoView);
        layoutVideoView.setVisibility(View.GONE);
        videoView = (VideoView) findViewById(R.id.videoviewMain);
        videoView.setVisibility(View.GONE);
        playPauseVideoButton = (Button) findViewById(R.id.playPauseButton);
        layoutButtonPlayPAuse = (LinearLayout) findViewById(R.id.layoutButtonPlayPAuse);

        //GPS-Location
        getGPSLocationButton = (Button)findViewById(R.id.get_GPS_location_button);

        //four finger capture
        fourFingerCapture = (Button)findViewById(R.id.fourFingerCaptureBtn);
        fourFingerCaptureFocusedRatio = (EditText)findViewById(R.id.four_Finger_Capture_FocusedRatio);
        fourFingerCaptureGlareRatio = (EditText)findViewById(R.id.four_Finger_Capture_GlareRatio);
        numOfFingerToCheckFocus = (EditText)findViewById(R.id.no_of_finger_to_check_focus);

        //Fingerprint capture
        fingerprintDeviceSpinner = (Spinner)findViewById(R.id.fingerprint_device_list_spinner);
        fingerTypeSpinner = (Spinner)findViewById(R.id.finger_type_spinner);
        deviceTimeoutEdtTxt = (EditText)findViewById(R.id.timeout_value_edttxt);
        minImageSizeEdtTxt = (EditText)findViewById(R.id.min_img_size_edttxt);
        minNFIQEdtTxt = (EditText)findViewById(R.id.nfiq_value_edttxt);
        captureFingerprintButton = (Button)findViewById(R.id.captureFingerprintButton);
        clearFingerprintButton = (Button)findViewById(R.id.clearFingerprintButton);
        clearAllFingerprintButton = (Button)findViewById(R.id.clearAllFingerprintButton);
        clearFormKeyFingerprint  = (CheckBox) findViewById(R.id.clearFormKey_fingerprint);
        enrollFingerprintButton = (Button)findViewById(R.id.enrollFingerprintButton);
        verifyFingerprintButton = (Button)findViewById(R.id.verifyFingerprintButton);

        //Generate and Verify OTP
        emailID = (EditText) findViewById(R.id.email_id);
        mobileNumber = (EditText) findViewById(R.id.mobile_number);
        otpNotificationType = (Spinner) findViewById(R.id.notification_type);
        received_otp = (EditText) findViewById(R.id.received_otp);

        //Signature
        signatureButton = (Button) findViewById(R.id.signature_button);
        captureGenericDocButton= (Button) findViewById(R.id.captureGenericDocument);
        fieldNameEdtTxt=(EditText) findViewById(R.id.id_field_name);

        //ResultView fields
        resultTv = (TextView) findViewById(R.id.resultTv);
        voiceRecordingPlayer = (ImageView)findViewById(R.id.voice_recording_player);
        viewPager = (ViewPager) findViewById(R.id.view_pager_for_image);

        //Initializing ProgressDialog
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        //ResultView image adapter
        resultImagePagerAdapter = new ResultImagePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(resultImagePagerAdapter);

        //Verification POA
        address1EdtTxt = (EditText) findViewById(R.id.id_address1);
        address2EdtTxt = (EditText) findViewById(R.id.id_address2);
        countryEdtTxt = (EditText) findViewById(R.id.id_country);
        stateEdtTxt = (EditText) findViewById(R.id.id_state);
        zipCodeEdtTxt = (EditText) findViewById(R.id.id_zipcode);
        capturePOAVerification=(Button)findViewById(R.id.capturePOAVerification);

        mySpinner = (Spinner) findViewById(R.id.idcode_ET);
        faceImageTypeSpinner = (Spinner) findViewById(R.id.faceImageType_ET);
        idImageTypeSpinner = (Spinner) findViewById(R.id.idImageType_ET);
        verificationType = (Spinner) findViewById(R.id.verification_type);
        requestResponseTypeSpinner = (Spinner) findViewById(R.id.request_response_type);

        //verify POA
        doVerify =(CheckBox)findViewById(R.id.id_do_verify);
        doExtract=(CheckBox)findViewById(R.id.id_do_extract);
        isFinalSubmit=(CheckBox)findViewById(R.id.id_is_final_submit);

        //Employee create & verification
        empCodeEdtTxt = (EditText) findViewById(R.id.emp_code);
        empLoginIdEdtTxt = (EditText) findViewById(R.id.login_id);
        empEmailEdtTxt = (EditText) findViewById(R.id.emp_email);
        companyIdEdtTxt = (EditText) findViewById(R.id.company_id);
        departmentEdtTxt = (EditText) findViewById(R.id.emp_dept);
        empNameEdtTxt = (EditText) findViewById(R.id.emp_name_edttext);
        empMobNumberEdtTxt = (EditText) findViewById(R.id.emp_mob_no_edttext);
        empCountryEdtTxt = (EditText) findViewById(R.id.emp_country_edttext);
        empAddressLine1 = (EditText) findViewById(R.id.emp_addressline1_edttext);
        empAddressLine2 = (EditText) findViewById(R.id.emp_addressline2_edttext);
        empZipCode = (EditText) findViewById(R.id.emp_zipcode_edttext);
        empSpouseName = (EditText) findViewById(R.id.emp_spousename_edttext);
        empNoOfChildren = (EditText) findViewById(R.id.emp_no_of_children_edttext);
        empTypeSpinner=(Spinner)findViewById(R.id.emp_type);
        employeeGenderSpinner=(Spinner)findViewById(R.id.emp_gender_spinner);
        createEmployeeButton =(Button)findViewById(R.id.emp_create_button);
        verifyEmployeeButton =(Button)findViewById(R.id.emp_verify_button);
        empIsFinalSubmit=(CheckBox)findViewById(R.id.emp_is_final_submit);
        matchFaceCreateEmployee =(Button)findViewById(R.id.face_match_create_emp_button);


        //token generate
        generateTokenButton=(Button)findViewById(R.id.generate_token_button);
        verifyToken=(Button)findViewById(R.id.verify_token_button);

        //snippet capture
        addFieldEdtTxt=(EditText) findViewById(R.id.snippetFieldName);
//        addFieldEdtTxt.setText("Name");//Temp
        addField=(Button)findViewById(R.id.addFieldName);
        snippetCapture=(Button)findViewById(R.id.snippetCapture);
        clearFormKeyCustVerification = (CheckBox)findViewById(R.id.clearFormKey_verifyCustomer);
        clearFormKeyEmpVerification= (CheckBox)findViewById(R.id.clearFormKey_verifyEmployee);
        captureEnable= (CheckBox)findViewById(R.id.capture_enable_cb);

        labelKeySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, LABELS_KEY));
        mySpinner.setAdapter(new ArrayAdapter<IdType>(this, android.R.layout.simple_spinner_item, IdType.values()));
        faceImageTypeSpinner.setAdapter(new ArrayAdapter<FaceImageType>(this, android.R.layout.simple_spinner_item, FaceImageType.values()));
        idImageTypeSpinner.setAdapter(new ArrayAdapter<IDImageType>(this, android.R.layout.simple_spinner_item, IDImageType.values()));
        verificationType.setAdapter(new ArrayAdapter<BiometricType>(this, android.R.layout.simple_spinner_item, BiometricType.values()));
        fingerprintDeviceSpinner.setAdapter(new ArrayAdapter<FingerprintDeviceType>(this, android.R.layout.simple_spinner_item, FingerprintDeviceType.values()));
        fingerTypeSpinner.setAdapter(new ArrayAdapter<FingerType>(this, android.R.layout.simple_spinner_item, FingerType.values()));
        requestResponseTypeSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,REQUEST_RESPONSE_KEY));
        empTypeSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,EMPLOYEE_TYPE));
        employeeGenderSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,EMPLOYEE_GENDER_TYPE));
        otpNotificationType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, OTP_NOTIFICATION_TYPE));

    }

    private void setDefaultValue(){

        //Setting previously set values-Start
        urlEdtTxt.setText(retrieveSetting(URL, "https://demo.idmission.com/IDS/service/integ/idm/thirdparty/upsert"));
        loginIdEdtTxt.setText(retrieveSetting(LOGINID, null));
        passEdtTxt.setText(retrieveSetting(PASSWORD, null));
        merchantIdEdtTxt.setText(retrieveSetting(MERCHANTID, null));
        productIdEdtTxt.setText(retrieveSetting(PRODUCTID, null));
        productNameEdtTxt.setText(retrieveSetting(PRODUCTNAME, "Identity_Validation_and_Face_Matching"));
        languageEdtTxt.setText(retrieveSetting(LANGUAGE, mLanguage));

        customerName.setText(retrieveSetting(CUSTOMER_NAME, null));
        uniqueCustomerNumber.setText(retrieveSetting(UNIQUE_CUSTOMER_NUMBER, null));
        uniqueMerchantNumber.setText(retrieveSetting(UNIQUE_MERCHANT_NUMBER, null));
        serviceID.setText(retrieveSetting(SERVICE_ID, null));
        customerType.setText(retrieveSetting(CUSTOMER_TYPE, null));
        customerAttribute.setText(retrieveSetting(CUSTOMER_ATTRIBUTE, null));
        customerPhone.setText(retrieveSetting(CUSTOMER_PHONE, null));
        customerEmail.setText(retrieveSetting(CUSTOMER_EMAIL, null));
        uniqueEmployeeCode.setText(retrieveSetting(UNIQUE_EMPLOYEE_CODE, null));
        uniqueEmployeeNumber.setText(retrieveSetting(UNIQUE_EMPLOYEE_NUMBER, null));

        imageLightThresholdEdtTxt.setText(retrieveSetting(IMAGE_LIGHT_THRESHOLD, ""+ImageProcessingSDK.dLightThreshold));
        imageMaxFocusThresholdEdtTxt.setText(retrieveSetting(IMAGE_MAX_FOCUS_THRESHOLD, ""+ImageProcessingSDK.dMaxFocusThreshold));
        imageMinFocusThresholdEdtTxt.setText(retrieveSetting(IMAGE_MIN_FOCUS_THRESHOLD, ""+ImageProcessingSDK.dMinFocusThreshold));
        imageGlarePercentageEdtTxt.setText(retrieveSetting(IMAGE_GLARE_PERCENTAGE, ""+ImageProcessingSDK.dGlarePercentage));
        enableCaptureButtonTimeEdtTxt.setText(retrieveSetting(CAPTURE_BUTTON_TIME, ""+ImageProcessingSDK.dCaptureBtnEnableTime));
        maxImageSize.setText(retrieveSetting(MAX_IMAGE_SIZE, ""+ImageProcessingSDK.dImageSize));
        imageHeight.setText(retrieveSetting(IMAGE_HEIGHT, ""+ImageProcessingSDK.dImageHeight));
        imageWidth.setText(retrieveSetting(IMAGE_WIDTH, ""+ImageProcessingSDK.dImgaeWidth));
        idImageOutlineColorEdtTxt.setText(retrieveSetting(ID_IMG_OUTLINE_COLOR, ""+ImageProcessingSDK.dIdOutlineColor));
        idImageOutlineAlphaEdtTxt.setText(retrieveSetting(ID_IMG_OUTLINE_ALPHA, ""+ImageProcessingSDK.dAlpha));
        detectedIdOutlineColorEdtTxt.setText(retrieveSetting(DETECTED_ID_IMG_OUTLINE_COLOR, ""+ImageProcessingSDK.dDetectedIdOutlineColor));
        detectedIdOutlineAlphaEdtTxt.setText(retrieveSetting(DETECTED_ID_IMG_OUTLINE_ALPHA, ""+ImageProcessingSDK.dAlpha));
        idImgOutsideOutlineColorEdtTxt.setText(retrieveSetting(ID_IMG_OUTSIDE_OUTLINE_COLOR, ""+ImageProcessingSDK.dOutsideIdOutlineColor));
        idImgOutsideOutlineAlphaEdtTxt.setText(retrieveSetting(ID_IMG_OUTSIDE_OUTLINE_ALPHA, ""+ImageProcessingSDK.dAlpha));

        detectedidImgOutsideOutlineColorEdtTxt.setText(retrieveSetting(DETECTED_ID_IMG_OUTSIDE_OUTLINE,""+ImageProcessingSDK.dDetectedOutsideIdOutlineColor));
        detectedidImgOutsideOutlineAlphaEdtTxt.setText(retrieveSetting(DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA,""+ImageProcessingSDK.dAlpha));

        imageLightThresholdPOAEdtTxt.setText(retrieveSetting(IMAGE_LIGHT_THRESHOLD_POA, ""+ImageProcessingSDK.dLightThresholdPOA));
        imageMaxFocusThresholdPOAEdtTxt.setText(retrieveSetting(IMAGE_MAX_FOCUS_THRESHOLD_POA, ""+ImageProcessingSDK.dMaxFocusThresholdPOA));
        imageMinFocusThresholdPOAEdtTxt.setText(retrieveSetting(IMAGE_MIN_FOCUS_THRESHOLD_POA, ""+ImageProcessingSDK.dMinFocusThresholdPOA));
        imageGlarePercentagePOAEdtTxt.setText(retrieveSetting(IMAGE_GLARE_PERCENTAGE_POA, ""+ImageProcessingSDK.dGlarePercentagePOA));
        enableCaptureButtonTimePOAEdtTxt.setText(retrieveSetting(CAPTURE_BUTTON_TIME_POA, ""+ImageProcessingSDK.dCaptureBtnEnableTimePOA));
        maxImageSizePOAEdtTxt.setText(retrieveSetting(MAX_IMAGE_SIZE_POA, ""+ImageProcessingSDK.dImageSizePOA));
        imageHeightPOAEdtTxt.setText(retrieveSetting(IMAGE_HEIGHT_POA, ""+ImageProcessingSDK.dImageHeightPOA));
        imageWidthPOAEdtTxt.setText(retrieveSetting(IMAGE_WIDTH_POA, ""+ImageProcessingSDK.dImgaeWidthPOA));
        idImageOutlineColorPOAEdtTxt.setText(retrieveSetting(ID_IMG_POA_OUTLINE_COLOR, ""+ImageProcessingSDK.dIdOutlineColor));
        idImageOutlineAlphaPOAEdtTxt.setText(retrieveSetting(ID_IMG_POA_OUTLINE_ALPHA, ""+ImageProcessingSDK.dAlpha));
        detectedIdOutlineColorPOAEdtTxt.setText(retrieveSetting(DETECTED_ID_POA_OUTLINE_COLOR,""+ImageProcessingSDK.dDetectedIdOutlineColor));
        detectedIdOutlineColorAlphaPOA.setText(retrieveSetting(DETECTED_ID_POA_OUTLINE_ALPHA,""+ImageProcessingSDK.dAlpha));
        idImageOutsideOutlineColorPOAEdtTxt.setText(retrieveSetting(ID_IMG_POA_OUTSIDE_OUTLINE_COLOR, ""+ImageProcessingSDK.dOutsideIdOutlineColor));
        idImageOutsideOutlineAlphaPOAEdtTxt.setText(retrieveSetting(ID_IMG_POA_OUTSIDE_OUTLINE_ALPHA, ""+ImageProcessingSDK.dAlpha));

        detectedidImageOutsideOutlineColorPOAEdtTxt.setText(retrieveSetting(DETECTED_ID_IMG_POA_OUTSIDE_OUTLINE_COLOR,""+ImageProcessingSDK.dDetectedOutsideIdOutlineColor));
        detectedidImageOutsideOutlineAlphaPOAEdtTxt.setText(retrieveSetting(DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA,""+ ImageProcessingSDK.dAlpha));

        faceLightThresholdEdtTxt.setText(retrieveSetting(FACE_LIGHT_THRESHOLD, ""+ImageProcessingSDK.dFaceLightThreshold));
        faceFocusThresholdEdtTxt.setText(retrieveSetting(FACE_FOCUS_THRESHOLD, ""+ImageProcessingSDK.dFaceFocusThreshold));
        faceDetectionThresholdEdtTxt.setText(retrieveSetting(FACE_DETECTION_THRESHOLD, ""+ImageProcessingSDK.dFaceDetectionThreshold));
        maxImageSizeForFaceDetect.setText(retrieveSetting(FACE_IMAGE_SIZE, ""+ImageProcessingSDK.dImageSizeForFace));
        faceOutlineColorEdtTxt.setText(retrieveSetting(FACE_OUTLINE_COLOR, ""+ImageProcessingSDK.dFaceOutlineColor));
        faceOutlineColorAlphaEdtTxt.setText(retrieveSetting(FACE_OUTLINE_COLOR_ALPHA, ""+ImageProcessingSDK.dAlpha));
        detectedFaceOutlineColorEdtTxt.setText(retrieveSetting(DETECTED_FACE_OUTLINE_COLOR, ""+ImageProcessingSDK.dDetectedFaceOutlineColor));
        detectedFaceOutlineColorAlphaEdtTxt.setText(retrieveSetting(DETECTED_FACE_OUTLINE_COLOR_ALPHA, ""+ImageProcessingSDK.dAlpha));
        outsideFaceOutlineColorEdtTxt.setText(retrieveSetting(OUTSIDE_FACE_OUTLINE_COLOR, ""+ImageProcessingSDK.dOutsideFaceOutlineColor));
        outsideFaceOutlineColorAlphaEdtTxt.setText(retrieveSetting(OUTSIDE_FACE_OUTLINE_COLOR_ALPHA, ""+ImageProcessingSDK.dAlpha));

        deviceTimeoutEdtTxt.setText(retrieveSetting(FINGER_DEVICE_TIMEOUT, ""+ImageProcessingSDK.dFingerprintDeviceTimeout));
        minImageSizeEdtTxt.setText(retrieveSetting(FINGER_MIN_IMAGESIZE, ""+ImageProcessingSDK.dFingerprintImageSize));
        minNFIQEdtTxt.setText(retrieveSetting(FINGER_MIN_NFIQ, ""+ImageProcessingSDK.dFingerprintNFIQValue));
        outsideDetectedFaceOutlineColor.setText(retrieveSetting(DETECTED_FACE_OUTSIDE_OUTLINE_COLOR,""+ImageProcessingSDK.dDetectedOutsideFaceOutlineColor));
        outsideDetectedFaceOutlineColorAlpha.setText(retrieveSetting(DETECTED_FACE_OUTSIDE_OUTLINE_COLOR_ALPHA,""+ImageProcessingSDK.dAlpha));
//        deltaLeftThreshold.setText(retrieveSetting(DELTA_LEFT_THRESHOLD,""+ImageProcessingSDK.deltaLeftThreshold));
//        deltaRightThreshold.setText(retrieveSetting(DELTA_RIGHT_THRESHOLD,""+ImageProcessingSDK.deltaRightThreshold));
//        deltaUpThreshold.setText(retrieveSetting(DELTA_UP_THRESHOLD,""+ImageProcessingSDK.deltaUpThreshold));
//        deltaDownThreshold.setText(retrieveSetting(DELTA_DOWN_THRESHOLD,""+ImageProcessingSDK.deltaDownThreshold));
        //Setting previously set values-End

    }


    private void initializeListener(){

        findViewById(R.id.initBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = urlEdtTxt.getText().toString().trim();
                String loginID = loginIdEdtTxt.getText().toString().trim();
                String password = passEdtTxt.getText().toString().trim();
                String merchantID = merchantIdEdtTxt.getText().toString().trim();
                String productid = productIdEdtTxt.getText().toString().trim();
                String productName = productNameEdtTxt.getText().toString().trim();
                String language = languageEdtTxt.getText().toString().trim();

                clearResultFields();
                if (enableFingerprint && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    int productID = 0;

                    if (!StringUtil.isEmpty(productid))
                        productID = Integer.parseInt(productid);

                    try {

//                        imageProcessingSDK = ImageProcessingSDK.initialize(MainActivity.this, url, loginID, password, merchantID, productID, productName, language);
                        imageProcessingSDK = ImageProcessingSDK.initialize(MainActivity.this, url, loginID, password, merchantID, productID, productName, language, enableDebugCB.isChecked());
                        imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                        enableButton();

                        saveSetting(URL, url);
                        saveSetting(LOGINID, loginID);
                        saveSetting(PASSWORD, password);
                        saveSetting(MERCHANTID, merchantID);
                        saveSetting(PRODUCTID, productid);
                        saveSetting(PRODUCTNAME, productName);
                        saveSetting(LANGUAGE, language);

                    } catch (InitializationException e) {
                        StringUtil.printLogInDebugMode("INIT Exception", e.getMessage());
                        Toast.makeText(MainActivity.this, "Not Initialized : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        addLabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String engLabel = labelValueEngEdtTxt.getText().toString().trim();
                if(!StringUtil.isEmpty(engLabel)){
                    englishLabelMap.put((String) labelKeySpinner.getSelectedItem(), engLabel);
                }

                String espLabel = labelValueEspEdtTxt.getText().toString().trim();
                if(!StringUtil.isEmpty(espLabel)){
                    spanishLabelMap.put((String) labelKeySpinner.getSelectedItem(), espLabel);
                }
                labelValueEngEdtTxt.setText("");
                labelValueEspEdtTxt.setText("");
            }
        });

        initLabelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageProcessingSDK.initializeLabels(englishLabelMap, spanishLabelMap);
            }
        });

        customizeUIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean faceTitleOnTop, faceHintOnTop, faceHintIconOnTop, faceTitleImageAlignment;
                String faceTitle = (String) faceTitleAlignmentSpinner.getSelectedItem();
                String faceHint = (String) faceHintAlignmentSpinner.getSelectedItem();
                String faceIcon = (String) faceHintIconAlignmentSpinner.getSelectedItem();
                String faceTitleImage = (String) faceTitleImageAlignmentSpinner.getSelectedItem();

                if(faceTitle.equalsIgnoreCase("Bottom")){
                    faceTitleOnTop = false;
                }else {
                    faceTitleOnTop = true;
                }

                if(faceHint.equalsIgnoreCase("Bottom")){
                    faceHintOnTop = false;
                }else {
                    faceHintOnTop = true;
                }

                if(faceIcon.equalsIgnoreCase("Bottom")){
                    faceHintIconOnTop = false;
                }else {
                    faceHintIconOnTop = true;
                }

                if(faceTitleImage.equalsIgnoreCase("Bottom")){
                    faceTitleImageAlignment = false;
                }else {
                    faceTitleImageAlignment = true;
                }

                // face capture checkbox
                //hide_Face_Title,hide_Face_hintMsg,hide_Face_hintIcon,hide_Face_titleImage
                //hideFaceTitle,hideFacehintMsg,hideFacehintIcon,hideFacetitleImage

                boolean hideFaceTitle = false;
                if(hide_Face_Title.isChecked()) {
                    hideFaceTitle = true;
                }

                boolean hideFacehintMsg = false;
                if(hide_Face_hintMsg.isChecked()) {
                    hideFacehintMsg = true;
                }

                boolean hideFacehintIcon = false;
                if(hide_Face_hintIcon.isChecked()) {
                    hideFacehintIcon = true;
                }

                boolean hideFacetitleImage = false;
                if(hide_Face_titleImage.isChecked()) {
                    hideFacetitleImage = true;
                }

                // id capture checkbox start

                boolean hideIdCaptureTitle = false;
                if(hide_Id_Capture_Title.isChecked()) {
                    hideIdCaptureTitle = true;
                }

                boolean hideIdCaptureHintMsg = false;
                if(hide_Id_Capture_HintMsg.isChecked()) {
                    hideIdCaptureHintMsg = true;
                }

                boolean hideIdCaptureTitleImg = false;
                if(hide_Id_Capture_TitleImg.isChecked()) {
                    hideIdCaptureTitleImg = true;
                }

                // id capture checkbox end
                //hideFaceTitle,hideFacehintMsg,hideFacehintIcon,hideFacetitleImage
                imageProcessingSDK.imageProcessingSDK.customizeUserInterface(faceTitleOnTop, hideFaceTitle,
                        faceHintOnTop, hideFacehintMsg,
                        faceHintIconOnTop, hideFacehintIcon,
                        faceTitleImageAlignment, hideFacetitleImage,
                        idTitleAlignmentSpinner.getSelectedItem().toString(), hideIdCaptureTitle,
                        idHintMsgAlignmentSpinner.getSelectedItem().toString(), hideIdCaptureHintMsg,
                        idTitleImgMsgAlignmentSpinner.getSelectedItem().toString(), hideIdCaptureTitleImg, imageProcessingSDK);
            }
        });

        captureFrontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearResultFields();
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                } else {

                    String lightThreshold = imageLightThresholdEdtTxt.getText().toString().trim();
                    String minFocusThreshold = imageMinFocusThresholdEdtTxt.getText().toString().trim();
                    String maxFocusThreshold = imageMaxFocusThresholdEdtTxt.getText().toString().trim();
                    String glareThreshold = imageGlarePercentageEdtTxt.getText().toString().trim();
                    String captureBtnTime = enableCaptureButtonTimeEdtTxt.getText().toString().trim();
                    String maxImagesize = maxImageSize.getText().toString().trim();
                    String imageheight = imageHeight.getText().toString().trim();
                    String imagewidth = imageWidth.getText().toString().trim();
                    String idImgOutlineColor = idImageOutlineColorEdtTxt.getText().toString().trim();
                    String idImgOutlineAlpha = idImageOutlineAlphaEdtTxt.getText().toString().trim();
                    String detectedIdImgOutlineColor = detectedIdOutlineColorEdtTxt.getText().toString().trim();
                    String detectedIdImgOutlineAlpha = detectedIdOutlineAlphaEdtTxt.getText().toString().trim();
                    String idImgOutsideOutlineColor = idImgOutsideOutlineColorEdtTxt.getText().toString().trim();
                    String idImgOutsideOutlineAlpha = idImgOutsideOutlineAlphaEdtTxt.getText().toString().trim();

                    String detectedIdImgOutsideOutlineColor = detectedidImgOutsideOutlineColorEdtTxt.getText().toString().trim();
                    String detectedIdImgOutsideOutlineAlpha = detectedidImgOutsideOutlineAlphaEdtTxt.getText().toString().trim();
                    String titleImageType = (String) titleImageCaptureTypeSpinner.getSelectedItem();

                    Bitmap titleBitmap = null;
                    if(!titleImageType.equalsIgnoreCase("None")){
                        titleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title_image);
                    }

                    int minimumLightThreshold = (!StringUtil.isEmpty(lightThreshold) ? Integer.parseInt(lightThreshold) : ImageProcessingSDK.dLightThreshold);
                    int minFocusScoreThreshold = (!StringUtil.isEmpty(minFocusThreshold) ? Integer.parseInt(minFocusThreshold) : ImageProcessingSDK.dMinFocusThreshold);
                    int focusScoreThreshold = (!StringUtil.isEmpty(maxFocusThreshold) ? Integer.parseInt(maxFocusThreshold) : ImageProcessingSDK.dMaxFocusThreshold);
                    int glarePercentageThreshold = (!StringUtil.isEmpty(glareThreshold) ? Integer.parseInt(glareThreshold) : ImageProcessingSDK.dGlarePercentage);
                    int enableCaptureButtonTime = (!StringUtil.isEmpty(captureBtnTime) ? Integer.parseInt(captureBtnTime) : ImageProcessingSDK.dCaptureBtnEnableTime);
                    int maxImageSize = (!StringUtil.isEmpty(maxImagesize) ? Integer.parseInt(maxImagesize) : ImageProcessingSDK.dImageSize);
                    int highResolutionImageHeight = (!StringUtil.isEmpty(imageheight) ? Integer.parseInt(imageheight) : ImageProcessingSDK.dImageHeight);
                    int highResolutionImageWidth = (!StringUtil.isEmpty(imagewidth) ? Integer.parseInt(imagewidth) : ImageProcessingSDK.dImgaeWidth);
                    String idImageOutlineColor = (!StringUtil.isEmpty(idImgOutlineColor) ? idImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                    int idImageOutlineAlpha = (!StringUtil.isEmpty(idImgOutlineAlpha) ? Integer.parseInt(idImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                    String detectedIdImageOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutlineColor) ? detectedIdImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                    int detectedIdImageOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutlineAlpha) ? Integer.parseInt(detectedIdImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                    String idImageOutsideOutlineColor = (!StringUtil.isEmpty(idImgOutsideOutlineColor) ? idImgOutsideOutlineColor : ImageProcessingSDK.dOutsideIdOutlineColor);
                    int idImageOutsideOutlineAlpha = (!StringUtil.isEmpty(idImgOutsideOutlineAlpha) ? Integer.parseInt(idImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

                    String detectedIdImageOutsideOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineColor) ? detectedIdImgOutsideOutlineColor : ImageProcessingSDK.dDetectedOutsideIdOutlineColor);
                    int detectedIdImageOutsideOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineAlpha) ? Integer.parseInt(detectedIdImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

//                    imageProcessingSDK.autoCapture(MainActivity.this, ImageType.FRONT);//It will launch with default values
//                    imageProcessingSDK.autoCapture(MainActivity.this, ImageType.FRONT, capturePortraitCB.isChecked());//It will launch with default values
//                    imageProcessingSDK.autoCapture(MainActivity.this, ImageType.FRONT, capturePortraitCB.isChecked(), minimumLightThreshold, minFocusScoreThreshold,
//                            focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
//                            highResolutionImageHeight, highResolutionImageWidth);
                    imageProcessingSDK.autoCapture(MainActivity.this, ImageType.FRONT, capturePortraitCB.isChecked(), minimumLightThreshold, minFocusScoreThreshold,
                            focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
                            highResolutionImageHeight, highResolutionImageWidth, null,
                            new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha),new ColorCode(detectedIdImageOutsideOutlineColor,detectedIdImageOutsideOutlineAlpha),titleBitmap,
                            showInstructionIdCapture.isChecked(), 0);

                    saveSetting(IMAGE_LIGHT_THRESHOLD, lightThreshold);
                    saveSetting(IMAGE_MAX_FOCUS_THRESHOLD, maxFocusThreshold);
                    saveSetting(IMAGE_MIN_FOCUS_THRESHOLD, minFocusThreshold);
                    saveSetting(IMAGE_GLARE_PERCENTAGE, glareThreshold);
                    saveSetting(CAPTURE_BUTTON_TIME, captureBtnTime);
                    saveSetting(MAX_IMAGE_SIZE, maxImagesize);
                    saveSetting(IMAGE_HEIGHT, imageheight);
                    saveSetting(IMAGE_WIDTH, imagewidth);
                    saveSetting(ID_IMG_OUTLINE_COLOR, idImageOutlineColor);
                    saveSetting(ID_IMG_OUTLINE_ALPHA, ""+idImageOutlineAlpha);
                    saveSetting(DETECTED_ID_IMG_OUTLINE_COLOR, detectedIdImageOutlineColor);
                    saveSetting(DETECTED_ID_IMG_OUTLINE_ALPHA, ""+detectedIdImageOutlineAlpha);
                    saveSetting(ID_IMG_OUTSIDE_OUTLINE_COLOR, idImageOutsideOutlineColor);
                    saveSetting(ID_IMG_OUTSIDE_OUTLINE_ALPHA, ""+idImageOutsideOutlineAlpha);
                    saveSetting(DETECTED_ID_IMG_OUTSIDE_OUTLINE,""+detectedIdImgOutsideOutlineColor);
                    saveSetting(DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA,""+detectedIdImgOutsideOutlineAlpha);
                }
            }
        });

        captureBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearResultFields();
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                } else {

                    String lightThreshold = imageLightThresholdEdtTxt.getText().toString().trim();
                    String minFocusThreshold = imageMinFocusThresholdEdtTxt.getText().toString().trim();
                    String maxFocusThreshold = imageMaxFocusThresholdEdtTxt.getText().toString().trim();
                    String glareThreshold = imageGlarePercentageEdtTxt.getText().toString().trim();
                    String captureBtnTime = enableCaptureButtonTimeEdtTxt.getText().toString().trim();
                    String maxImagesize = maxImageSize.getText().toString().trim();
                    String imageheight = imageHeight.getText().toString().trim();
                    String imagewidth = imageWidth.getText().toString().trim();
                    String idImgOutlineColor = idImageOutlineColorEdtTxt.getText().toString().trim();
                    String idImgOutlineAlpha = idImageOutlineAlphaEdtTxt.getText().toString().trim();
                    String detectedIdImgOutlineColor = detectedIdOutlineColorEdtTxt.getText().toString().trim();
                    String detectedIdImgOutlineAlpha = detectedIdOutlineAlphaEdtTxt.getText().toString().trim();
                    String idImgOutsideOutlineColor = idImgOutsideOutlineColorEdtTxt.getText().toString().trim();
                    String idImgOutsideOutlineAlpha = idImgOutsideOutlineAlphaEdtTxt.getText().toString().trim();
                    String titleImageType = (String) titleImageCaptureTypeSpinner.getSelectedItem();

                    String detectedIdImgOutsideOutlineColor = detectedidImgOutsideOutlineColorEdtTxt.getText().toString().trim();
                    String detectedIdImgOutsideOutlineAlpha = detectedidImgOutsideOutlineAlphaEdtTxt.getText().toString().trim();
                    Bitmap titleBitmap = null;
                    if(!titleImageType.equalsIgnoreCase("None")){
                        titleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title_image);
                    }

                    int minimumLightThreshold = (!StringUtil.isEmpty(lightThreshold) ? Integer.parseInt(lightThreshold) : ImageProcessingSDK.dLightThreshold);
                    int minFocusScoreThreshold = (!StringUtil.isEmpty(minFocusThreshold) ? Integer.parseInt(minFocusThreshold) : ImageProcessingSDK.dMinFocusThreshold);
                    int focusScoreThreshold = (!StringUtil.isEmpty(maxFocusThreshold) ? Integer.parseInt(maxFocusThreshold) : ImageProcessingSDK.dMaxFocusThreshold);
                    int glarePercentageThreshold = (!StringUtil.isEmpty(glareThreshold) ? Integer.parseInt(glareThreshold) : ImageProcessingSDK.dGlarePercentage);
                    int enableCaptureButtonTime = (!StringUtil.isEmpty(captureBtnTime) ? Integer.parseInt(captureBtnTime) : ImageProcessingSDK.dCaptureBtnEnableTime);
                    int maxImageSize = (!StringUtil.isEmpty(maxImagesize) ? Integer.parseInt(maxImagesize) : ImageProcessingSDK.dImageSize);
                    int highResolutionImageHeight = (!StringUtil.isEmpty(imageheight) ? Integer.parseInt(imageheight) : ImageProcessingSDK.dImageHeight);
                    int highResolutionImageWidth = (!StringUtil.isEmpty(imagewidth) ? Integer.parseInt(imagewidth) : ImageProcessingSDK.dImgaeWidth);
                    String idImageOutlineColor = (!StringUtil.isEmpty(idImgOutlineColor) ? idImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                    int idImageOutlineAlpha = (!StringUtil.isEmpty(idImgOutlineAlpha) ? Integer.parseInt(idImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                    String detectedIdImageOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutlineColor) ? detectedIdImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                    int detectedIdImageOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutlineAlpha) ? Integer.parseInt(detectedIdImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                    String idImageOutsideOutlineColor = (!StringUtil.isEmpty(idImgOutsideOutlineColor) ? idImgOutsideOutlineColor : ImageProcessingSDK.dOutsideIdOutlineColor);
                    int idImageOutsideOutlineAlpha = (!StringUtil.isEmpty(idImgOutsideOutlineAlpha) ? Integer.parseInt(idImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

                    String detectedIdImageOutsideOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineColor) ? detectedIdImgOutsideOutlineColor : ImageProcessingSDK.dDetectedOutsideIdOutlineColor);
                    int detectedIdImageOutsideOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineAlpha) ? Integer.parseInt(detectedIdImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

//                    imageProcessingSDK.autoCapture(MainActivity.this, ImageType.BACK);//It will launch with default values
//                    imageProcessingSDK.autoCapture(MainActivity.this, ImageType.BACK, capturePortraitCB.isChecked());//It will launch with default values
//                    imageProcessingSDK.autoCapture(MainActivity.this, ImageType.BACK, capturePortraitCB.isChecked(), minimumLightThreshold, minFocusScoreThreshold,
//                            focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
//                            highResolutionImageHeight, highResolutionImageWidth);
                    imageProcessingSDK.autoCapture(MainActivity.this, ImageType.BACK, capturePortraitCB.isChecked(), minimumLightThreshold, minFocusScoreThreshold,
                            focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
                            highResolutionImageHeight, highResolutionImageWidth, null,
                            new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha),new ColorCode(detectedIdImageOutsideOutlineColor,detectedIdImageOutsideOutlineAlpha), titleBitmap,
                            showInstructionIdCapture.isChecked(), 0);

                    saveSetting(IMAGE_LIGHT_THRESHOLD, lightThreshold);
                    saveSetting(IMAGE_MAX_FOCUS_THRESHOLD, maxFocusThreshold);
                    saveSetting(IMAGE_MIN_FOCUS_THRESHOLD, minFocusThreshold);
                    saveSetting(IMAGE_GLARE_PERCENTAGE, glareThreshold);
                    saveSetting(CAPTURE_BUTTON_TIME, captureBtnTime);
                    saveSetting(MAX_IMAGE_SIZE, maxImagesize);
                    saveSetting(IMAGE_HEIGHT, imageheight);
                    saveSetting(IMAGE_WIDTH, imagewidth);
                    saveSetting(ID_IMG_OUTLINE_COLOR, idImageOutlineColor);
                    saveSetting(ID_IMG_OUTLINE_ALPHA, ""+idImageOutlineAlpha);
                    saveSetting(DETECTED_ID_IMG_OUTLINE_COLOR, detectedIdImageOutlineColor);
                    saveSetting(DETECTED_ID_IMG_OUTLINE_ALPHA, ""+detectedIdImageOutlineAlpha);
                    saveSetting(ID_IMG_OUTSIDE_OUTLINE_COLOR, idImageOutsideOutlineColor);
                    saveSetting(ID_IMG_OUTSIDE_OUTLINE_ALPHA, ""+idImageOutsideOutlineAlpha);
                    saveSetting(DETECTED_ID_IMG_OUTSIDE_OUTLINE,""+detectedIdImgOutsideOutlineColor);
                    saveSetting(DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA,""+detectedIdImgOutsideOutlineAlpha);
                }
            }
        });

        findViewById(R.id.processImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show(); //Displaying progress
                clearResultFields();
                String countryId = ((TextView) findViewById(R.id.countryId_ET)).getText().toString();
                String stateId = ((TextView) findViewById(R.id.stateId_ET)).getText().toString();
                IdType idType = (IdType) mySpinner.getSelectedItem();
                JSONObject addJSON = getAdditionalDataJSON();

                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
//                imageProcessingSDK.processImage(MainActivity.this, countryId, stateId, idType, addJSON, isFinalSubmitProcessImage.isChecked());
                imageProcessingSDK.processImage(MainActivity.this, countryId, stateId, idType, addJSON, isFinalSubmitProcessImage.isChecked(), clearFormKeyProcessImage.isChecked());
            }
        });

        addField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StringUtil.isEmpty(addFieldEdtTxt.getText().toString())) {
                    snippetFieldNameMap.put(addFieldEdtTxt.getText().toString(), addFieldEdtTxt.getText().toString());
                    addFieldEdtTxt.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Field name should not be blank", Toast.LENGTH_LONG).show();
                }
            }
        });

        snippetCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResultFields();
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                } else {
                    String lightThreshold = imageLightThresholdEdtTxt.getText().toString().trim();
                    String minFocusThreshold = imageMinFocusThresholdEdtTxt.getText().toString().trim();
                    String maxFocusThreshold = imageMaxFocusThresholdEdtTxt.getText().toString().trim();
                    String glareThreshold = imageGlarePercentageEdtTxt.getText().toString().trim();
                    String captureBtnTime = enableCaptureButtonTimeEdtTxt.getText().toString().trim();
                    String maxImagesize = maxImageSize.getText().toString().trim();
                    String imageheight = imageHeight.getText().toString().trim();
                    String imagewidth = imageWidth.getText().toString().trim();
                    String idImgOutlineColor = idImageOutlineColorEdtTxt.getText().toString().trim();
                    String idImgOutlineAlpha = idImageOutlineAlphaEdtTxt.getText().toString().trim();
                    String detectedIdImgOutlineColor = detectedIdOutlineColorEdtTxt.getText().toString().trim();
                    String detectedIdImgOutlineAlpha = detectedIdOutlineAlphaEdtTxt.getText().toString().trim();
                    String idImgOutsideOutlineColor = idImgOutsideOutlineColorEdtTxt.getText().toString().trim();
                    String idImgOutsideOutlineAlpha = idImgOutsideOutlineAlphaEdtTxt.getText().toString().trim();
                    String titleImageType = (String) titleImageCaptureTypeSpinner.getSelectedItem();

                    String detectedIdImgOutsideOutlineColor = detectedidImgOutsideOutlineColorEdtTxt.getText().toString().trim();
                    String detectedIdImgOutsideOutlineAlpha = detectedidImgOutsideOutlineAlphaEdtTxt.getText().toString().trim();
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
                    int highResolutionImageHeight = (!StringUtil.isEmpty(imageheight) ? Integer.parseInt(imageheight) : ImageProcessingSDK.dImageHeight);
                    int highResolutionImageWidth = (!StringUtil.isEmpty(imagewidth) ? Integer.parseInt(imagewidth) : ImageProcessingSDK.dImgaeWidth);
                    String idImageOutlineColor = (!StringUtil.isEmpty(idImgOutlineColor) ? idImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                    int idImageOutlineAlpha = (!StringUtil.isEmpty(idImgOutlineAlpha) ? Integer.parseInt(idImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                    String detectedIdImageOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutlineColor) ? detectedIdImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                    int detectedIdImageOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutlineAlpha) ? Integer.parseInt(detectedIdImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                    String idImageOutsideOutlineColor = (!StringUtil.isEmpty(idImgOutsideOutlineColor) ? idImgOutsideOutlineColor : ImageProcessingSDK.dOutsideIdOutlineColor);
                    int idImageOutsideOutlineAlpha = (!StringUtil.isEmpty(idImgOutsideOutlineAlpha) ? Integer.parseInt(idImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

                    String detectedIdImageOutsideOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineColor) ? detectedIdImgOutsideOutlineColor : ImageProcessingSDK.dDetectedOutsideIdOutlineColor);
                    int detectedIdImageOutsideOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineAlpha) ? Integer.parseInt(detectedIdImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);


                    imageProcessingSDK.autoCapture(MainActivity.this, ImageType.SNIPPET_CAPTURE, capturePortraitCB.isChecked(), minimumLightThreshold, minFocusScoreThreshold,
                            focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
                            highResolutionImageHeight, highResolutionImageWidth, null,
                            new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor, detectedIdImageOutsideOutlineAlpha), titleBitmap,
                            showInstructionIdCapture.isChecked(), 0, "", snippetFieldNameMap,captureEnable.isChecked());

                    saveSetting(IMAGE_LIGHT_THRESHOLD, lightThreshold);
                    saveSetting(IMAGE_MAX_FOCUS_THRESHOLD, maxFocusThreshold);
                    saveSetting(IMAGE_MIN_FOCUS_THRESHOLD, minFocusThreshold);
                    saveSetting(IMAGE_GLARE_PERCENTAGE, glareThreshold);
                    saveSetting(CAPTURE_BUTTON_TIME, captureBtnTime);
                    saveSetting(MAX_IMAGE_SIZE, maxImagesize);
                    saveSetting(IMAGE_HEIGHT, imageheight);
                    saveSetting(IMAGE_WIDTH, imagewidth);
                    saveSetting(ID_IMG_OUTLINE_COLOR, idImageOutlineColor);
                    saveSetting(ID_IMG_OUTLINE_ALPHA, "" + idImageOutlineAlpha);
                    saveSetting(DETECTED_ID_IMG_OUTLINE_COLOR, detectedIdImageOutlineColor);
                    saveSetting(DETECTED_ID_IMG_OUTLINE_ALPHA, "" + detectedIdImageOutlineAlpha);
                    saveSetting(ID_IMG_OUTSIDE_OUTLINE_COLOR, idImageOutsideOutlineColor);
                    saveSetting(ID_IMG_OUTSIDE_OUTLINE_ALPHA, "" + idImageOutsideOutlineAlpha);
                    saveSetting(DETECTED_ID_IMG_OUTSIDE_OUTLINE, "" + detectedIdImgOutsideOutlineColor);
                    saveSetting(DETECTED_ID_IMG_OUTSIDE_OUTLINE_ALPHA, "" + detectedIdImgOutsideOutlineAlpha);
                }
            }
        });

        captureFaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearResultFields();
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                } else {
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

                    String titleImageType = (String) titleImageTypeSpinner.getSelectedItem();

                    Bitmap titleBitmap = null;
                    if(!titleImageType.equalsIgnoreCase("None")){
                        titleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title_image);
                    }

                    saveSetting(FACE_LIGHT_THRESHOLD, lightThreshold);
                    saveSetting(FACE_FOCUS_THRESHOLD, focusThreshold);
                    saveSetting(FACE_DETECTION_THRESHOLD, detectionThreshold);
                    saveSetting(FACE_OUTLINE_COLOR, faceOutlineColor);
                    saveSetting(DETECTED_FACE_OUTLINE_COLOR, detectedFaceOutlineColor);
                    saveSetting(OUTSIDE_FACE_OUTLINE_COLOR, outsideFaceOutlineColor);
                    saveSetting(FACE_OUTLINE_COLOR_ALPHA, faceOutlineColorAlpha);
                    saveSetting(DETECTED_FACE_OUTLINE_COLOR_ALPHA, detectedFaceOutlineColorAlpha);
                    saveSetting(OUTSIDE_FACE_OUTLINE_COLOR_ALPHA, outsideFaceOutlineColorAlpha);
                    saveSetting(DETECTED_FACE_OUTSIDE_OUTLINE_COLOR,detectedOutsideFaceOutlineColor);
                    saveSetting(DETECTED_FACE_OUTSIDE_OUTLINE_COLOR_ALPHA,detectedOutsideFaceOutlineColorAlpha);

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

//                    imageProcessingSDK.detectFace(MainActivity.this); //It will launch with default values
//                    imageProcessingSDK.detectFace(MainActivity.this, lightThresholdVal, focusThresholdVal, detectionThresholdVal,
//                            maxImageSize, launchFrontCameraCheckBox.isChecked(), showPreviewScreenCheckBox.isChecked());
                    imageProcessingSDK.detectFace(MainActivity.this, lightThresholdVal, focusThresholdVal, detectionThresholdVal,
                            maxImageSize, launchFrontCameraCheckBox.isChecked(), showPreviewScreenCheckBox.isChecked(),
                            new ColorCode(faceOutlineColor, faceOutlineAlpha), new ColorCode(detectedFaceOutlineColor, detectedFaceOutlineAlpha), new ColorCode(outsideFaceOutlineColor, outsideFaceOutlineAlpha),new ColorCode(detectedOutsideFaceOutlineColor,detectedOutsideFaceOutlineAlpha), titleBitmap,
                            showInstructionFaceDetect.isChecked(), R.mipmap.face_combine_instruction, toggleCameraCheckBox.isChecked());
                }
            }
        });

        findViewById(R.id.matchFaceImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show(); //Displaying progress
                clearResultFields();
                String faceImageType = ((FaceImageType) faceImageTypeSpinner.getSelectedItem()).getFaceImageType().toString();
                String idImageType = ((IDImageType) idImageTypeSpinner.getSelectedItem()).getIDImageType().toString();
                JSONObject addJSON = getAdditionalDataJSON();

                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
//                imageProcessingSDK.matchFaceImage(MainActivity.this, faceImageType, idImageType, addJSON, isFinalSubmitMatchFace.isChecked());
                imageProcessingSDK.matchFaceImage(MainActivity.this, faceImageType, idImageType, addJSON, isFinalSubmitMatchFace.isChecked(), clearFormKeyMatchFace.isChecked());
            }
        });

        findViewById(R.id.updateCustomerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String faceImageType = ((FaceImageType) faceImageTypeSpinner.getSelectedItem()).getFaceImageType().toString();
                JSONObject addJSON = getAdditionalDataJSON();
                imageProcessingSDK.updateCustomer(MainActivity.this, faceImageType, addJSON);
            }
        });

        verifyCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show(); //Displaying progress
                clearResultFields();
                String biometricType = ((BiometricType) verificationType.getSelectedItem()).getBiometricType().toString();
                JSONObject addJSON = getAdditionalDataJSON();

                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                if(null != addJSON){
                    imageProcessingSDK.verifyCustomer(MainActivity.this, biometricType, addJSON,clearFormKeyCustVerification.isChecked());
                }else {
                    imageProcessingSDK.verifyCustomer(MainActivity.this, biometricType,null,clearFormKeyCustVerification.isChecked());
                }
            }
        });

        findViewById(R.id.processImageAndMatchFace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show(); //Displaying progress
                clearResultFields();
                String countryId = ((TextView) findViewById(R.id.countryId_ET)).getText().toString();
                String stateId = ((TextView) findViewById(R.id.stateId_ET)).getText().toString();
                IdType idType = (IdType) mySpinner.getSelectedItem();
                String faceImageType = ((FaceImageType) faceImageTypeSpinner.getSelectedItem()).getFaceImageType().toString();
                JSONObject addJSON = getAdditionalDataJSON();

                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
//                imageProcessingSDK.processImageAndMatchFace(MainActivity.this, countryId, stateId, idType, faceImageType, addJSON, isFinalSubmitProcImgMatchFace.isChecked());
                imageProcessingSDK.processImageAndMatchFace(MainActivity.this, countryId, stateId, idType, faceImageType, addJSON, isFinalSubmitProcImgMatchFace.isChecked(), clearFormKeyProcImgMatchFace.isChecked());
            }
        });

        findViewById(R.id.detectCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearResultFields();
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                JSONObject addJSON = getAdditionalDataJSON();
                if(null != addJSON){
                    imageProcessingSDK.detectCard(MainActivity.this, addJSON);
                }else {
                    imageProcessingSDK.detectCard(MainActivity.this);
                }
            }
        });

        capturePOAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearResultFields();
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                } else {

                    String lightThreshold = imageLightThresholdPOAEdtTxt.getText().toString().trim();
                    String minFocusThreshold = imageMinFocusThresholdPOAEdtTxt.getText().toString().trim();
                    String maxFocusThreshold = imageMaxFocusThresholdPOAEdtTxt.getText().toString().trim();
                    String glareThreshold = imageGlarePercentagePOAEdtTxt.getText().toString().trim();
                    String captureBtnTime = enableCaptureButtonTimePOAEdtTxt.getText().toString().trim();
                    String maxImagesize = maxImageSizePOAEdtTxt.getText().toString().trim();
                    String imageheight = imageHeightPOAEdtTxt.getText().toString().trim();
                    String imagewidth = imageWidthPOAEdtTxt.getText().toString().trim();
                    String idImgOutlineColor = idImageOutlineColorPOAEdtTxt.getText().toString().trim();
                    String idImgOutlineAlpha = idImageOutlineAlphaPOAEdtTxt.getText().toString().trim();
                    String detectedIdImgOutlineColor = detectedIdOutlineColorPOAEdtTxt.getText().toString().trim();
                    String detectedIdImgOutlineAlpha = detectedIdOutlineColorAlphaPOA.getText().toString().trim();
                    String idImgOutsideOutlineColor = idImageOutsideOutlineColorPOAEdtTxt.getText().toString().trim();
                    String idImgOutsideOutlineAlpha = idImageOutsideOutlineAlphaPOAEdtTxt.getText().toString().trim();

                    String detectedIdImgOutsideOutlineColor = detectedidImageOutsideOutlineColorPOAEdtTxt.getText().toString().trim();
                    String detectedIdImgOutsideOutlineAlpha = detectedidImageOutsideOutlineAlphaPOAEdtTxt.getText().toString().trim();
                    String titleImageType = (String) titleImageCapturePOASpinner.getSelectedItem();

                    Bitmap titleBitmap = null;
                    if(!titleImageType.equalsIgnoreCase("None")){
                        titleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title_image);
                    }

                    int minimumLightThreshold = (!StringUtil.isEmpty(lightThreshold) ? Integer.parseInt(lightThreshold) : ImageProcessingSDK.dLightThresholdPOA);
                    int minFocusScoreThreshold = (!StringUtil.isEmpty(minFocusThreshold) ? Integer.parseInt(minFocusThreshold) : ImageProcessingSDK.dMinFocusThresholdPOA);
                    int focusScoreThreshold = (!StringUtil.isEmpty(maxFocusThreshold) ? Integer.parseInt(maxFocusThreshold) : ImageProcessingSDK.dMaxFocusThresholdPOA);
                    int glarePercentageThreshold = (!StringUtil.isEmpty(glareThreshold) ? Integer.parseInt(glareThreshold) : ImageProcessingSDK.dGlarePercentagePOA);
                    int enableCaptureButtonTime = (!StringUtil.isEmpty(captureBtnTime) ? Integer.parseInt(captureBtnTime) : ImageProcessingSDK.dCaptureBtnEnableTimePOA);
                    int maxImageSize = (!StringUtil.isEmpty(maxImagesize) ? Integer.parseInt(maxImagesize) : ImageProcessingSDK.dImageSizePOA);
                    int highResolutionImageHeight = (!StringUtil.isEmpty(imageheight) ? Integer.parseInt(imageheight) : ImageProcessingSDK.dImageHeightPOA);
                    int highResolutionImageWidth = (!StringUtil.isEmpty(imagewidth) ? Integer.parseInt(imagewidth) : ImageProcessingSDK.dImgaeWidthPOA);
                    String idImageOutlineColor = (!StringUtil.isEmpty(idImgOutlineColor) ? idImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                    int idImageOutlineAlpha = (!StringUtil.isEmpty(idImgOutlineAlpha) ? Integer.parseInt(idImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                    String detectedIdImageOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutlineColor) ? detectedIdImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                    int detectedIdImageOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutlineAlpha) ? Integer.parseInt(detectedIdImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                    String idImageOutsideOutlineColor = (!StringUtil.isEmpty(idImgOutsideOutlineColor) ? idImgOutsideOutlineColor : ImageProcessingSDK.dOutsideIdOutlineColor);
                    int idImageOutsideOutlineAlpha = (!StringUtil.isEmpty(idImgOutsideOutlineAlpha) ? Integer.parseInt(idImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

                    String detectedIdImageOutsideOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineColor) ? detectedIdImgOutsideOutlineColor : ImageProcessingSDK.dOutsideIdOutlineColor);
                    int detectedIdImageOutsideOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineAlpha) ? Integer.parseInt(detectedIdImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

//                imageProcessingSDK.captureProofOfAddress(MainActivity.this);
//                imageProcessingSDK.captureProofOfAddress(MainActivity.this, capturePortraitChcBoxPOA.isChecked());
                    JSONObject addJSON = getAdditionalDataJSON();
                    if(null != addJSON){
                        imageProcessingSDK.captureProofOfAddress(MainActivity.this, capturePortraitChcBoxPOA.isChecked(), minimumLightThreshold, minFocusScoreThreshold,
                                focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
                                highResolutionImageHeight, highResolutionImageWidth, addJSON,
                                new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor,detectedIdImageOutsideOutlineAlpha),titleBitmap,
                                showInstructionPOA.isChecked(), 0);
                    }else{
                        imageProcessingSDK.captureProofOfAddress(MainActivity.this, capturePortraitChcBoxPOA.isChecked(), minimumLightThreshold, minFocusScoreThreshold,
                                focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
                                highResolutionImageHeight, highResolutionImageWidth, null,
                                new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor,detectedIdImageOutsideOutlineAlpha),titleBitmap,
                                showInstructionPOA.isChecked(), 0);
                    }

                    saveSetting(IMAGE_LIGHT_THRESHOLD_POA, lightThreshold);
                    saveSetting(IMAGE_MAX_FOCUS_THRESHOLD_POA, maxFocusThreshold);
                    saveSetting(IMAGE_MIN_FOCUS_THRESHOLD_POA, minFocusThreshold);
                    saveSetting(IMAGE_GLARE_PERCENTAGE_POA, glareThreshold);
                    saveSetting(CAPTURE_BUTTON_TIME_POA, captureBtnTime);
                    saveSetting(MAX_IMAGE_SIZE_POA, maxImagesize);
                    saveSetting(IMAGE_HEIGHT_POA, imageheight);
                    saveSetting(IMAGE_WIDTH_POA, imagewidth);
                    saveSetting(ID_IMG_POA_OUTLINE_COLOR, idImageOutlineColor);
                    saveSetting(ID_IMG_POA_OUTLINE_ALPHA, ""+idImageOutlineAlpha);
                    saveSetting(DETECTED_ID_POA_OUTLINE_COLOR, detectedIdImageOutlineColor);
                    saveSetting(DETECTED_ID_POA_OUTLINE_ALPHA, ""+detectedIdImageOutlineAlpha);
                    saveSetting(ID_IMG_POA_OUTSIDE_OUTLINE_COLOR, idImageOutsideOutlineColor);
                    saveSetting(ID_IMG_POA_OUTSIDE_OUTLINE_ALPHA, ""+idImageOutsideOutlineAlpha);
                    saveSetting(DETECTED_ID_IMG_POA_OUTSIDE_OUTLINE_COLOR,""+detectedIdImageOutsideOutlineColor);
                    saveSetting(DETECTED_ID_IMG_POA_OUTSIDE_OUTLINE_ALPHA,""+detectedIdImageOutsideOutlineAlpha);
                }
            }
        });

        captureBankStatementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearResultFields();
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                } else {

                    String lightThreshold = imageLightThresholdPOAEdtTxt.getText().toString().trim();
                    String minFocusThreshold = imageMinFocusThresholdPOAEdtTxt.getText().toString().trim();
                    String maxFocusThreshold = imageMaxFocusThresholdPOAEdtTxt.getText().toString().trim();
                    String glareThreshold = imageGlarePercentagePOAEdtTxt.getText().toString().trim();
                    String captureBtnTime = enableCaptureButtonTimePOAEdtTxt.getText().toString().trim();
                    String maxImagesize = maxImageSizePOAEdtTxt.getText().toString().trim();
                    String imageheight = imageHeightPOAEdtTxt.getText().toString().trim();
                    String imagewidth = imageWidthPOAEdtTxt.getText().toString().trim();
                    String idImgOutlineColor = idImageOutlineColorPOAEdtTxt.getText().toString().trim();
                    String idImgOutlineAlpha = idImageOutlineAlphaPOAEdtTxt.getText().toString().trim();
                    String detectedIdImgOutlineColor = detectedIdOutlineColorPOAEdtTxt.getText().toString().trim();
                    String detectedIdImgOutlineAlpha = detectedIdOutlineColorAlphaPOA.getText().toString().trim();
                    String idImgOutsideOutlineColor = idImageOutsideOutlineColorPOAEdtTxt.getText().toString().trim();
                    String idImgOutsideOutlineAlpha = idImageOutsideOutlineAlphaPOAEdtTxt.getText().toString().trim();

                    String detectedIdImgOutsideOutlineColor = detectedidImageOutsideOutlineColorPOAEdtTxt.getText().toString().trim();
                    String detectedIdImgOutsideOutlineAlpha = detectedidImageOutsideOutlineAlphaPOAEdtTxt.getText().toString().trim();
                    String titleImageType = (String) titleImageCapturePOASpinner.getSelectedItem();

                    Bitmap titleBitmap = null;
                    if(!titleImageType.equalsIgnoreCase("None")){
                        titleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title_image);
                    }

                    int minimumLightThreshold = (!StringUtil.isEmpty(lightThreshold) ? Integer.parseInt(lightThreshold) : ImageProcessingSDK.dLightThresholdPOA);
                    int minFocusScoreThreshold = (!StringUtil.isEmpty(minFocusThreshold) ? Integer.parseInt(minFocusThreshold) : ImageProcessingSDK.dMinFocusThresholdPOA);
                    int focusScoreThreshold = (!StringUtil.isEmpty(maxFocusThreshold) ? Integer.parseInt(maxFocusThreshold) : ImageProcessingSDK.dMaxFocusThresholdPOA);
                    int glarePercentageThreshold = (!StringUtil.isEmpty(glareThreshold) ? Integer.parseInt(glareThreshold) : ImageProcessingSDK.dGlarePercentagePOA);
                    int enableCaptureButtonTime = (!StringUtil.isEmpty(captureBtnTime) ? Integer.parseInt(captureBtnTime) : ImageProcessingSDK.dCaptureBtnEnableTimePOA);
                    int maxImageSize = (!StringUtil.isEmpty(maxImagesize) ? Integer.parseInt(maxImagesize) : ImageProcessingSDK.dImageSizePOA);
                    int highResolutionImageHeight = (!StringUtil.isEmpty(imageheight) ? Integer.parseInt(imageheight) : ImageProcessingSDK.dImageHeightPOA);
                    int highResolutionImageWidth = (!StringUtil.isEmpty(imagewidth) ? Integer.parseInt(imagewidth) : ImageProcessingSDK.dImgaeWidthPOA);
                    String idImageOutlineColor = (!StringUtil.isEmpty(idImgOutlineColor) ? idImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                    int idImageOutlineAlpha = (!StringUtil.isEmpty(idImgOutlineAlpha) ? Integer.parseInt(idImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                    String detectedIdImageOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutlineColor) ? detectedIdImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                    int detectedIdImageOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutlineAlpha) ? Integer.parseInt(detectedIdImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                    String idImageOutsideOutlineColor = (!StringUtil.isEmpty(idImgOutsideOutlineColor) ? idImgOutsideOutlineColor : ImageProcessingSDK.dOutsideIdOutlineColor);
                    int idImageOutsideOutlineAlpha = (!StringUtil.isEmpty(idImgOutsideOutlineAlpha) ? Integer.parseInt(idImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

                    String detectedIdImageOutsideOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineColor) ? detectedIdImgOutsideOutlineColor : ImageProcessingSDK.dOutsideIdOutlineColor);
                    int detectedIdImageOutsideOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineAlpha) ? Integer.parseInt(detectedIdImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

//                imageProcessingSDK.captureBankStatement(MainActivity.this);
//                imageProcessingSDK.captureBankStatement(MainActivity.this, capturePortraitChcBoxPOA.isChecked());
                    JSONObject addJSON = getAdditionalDataJSON();
                    if(null != addJSON){
                        imageProcessingSDK.captureBankStatement(MainActivity.this, capturePortraitChcBoxPOA.isChecked(), minimumLightThreshold, minFocusScoreThreshold,
                                focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
                                highResolutionImageHeight, highResolutionImageWidth, addJSON,
                                new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor,detectedIdImageOutsideOutlineAlpha),titleBitmap,
                                showInstructionPOA.isChecked(), 0);
                    }else {
                        imageProcessingSDK.captureBankStatement(MainActivity.this, capturePortraitChcBoxPOA.isChecked(), minimumLightThreshold, minFocusScoreThreshold,
                                focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
                                highResolutionImageHeight, highResolutionImageWidth, null,
                                new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor,detectedIdImageOutsideOutlineAlpha),titleBitmap,
                                showInstructionPOA.isChecked(), 0);
                    }

                    saveSetting(IMAGE_LIGHT_THRESHOLD_POA, lightThreshold);
                    saveSetting(IMAGE_MAX_FOCUS_THRESHOLD_POA, maxFocusThreshold);
                    saveSetting(IMAGE_MIN_FOCUS_THRESHOLD_POA, minFocusThreshold);
                    saveSetting(IMAGE_GLARE_PERCENTAGE_POA, glareThreshold);
                    saveSetting(CAPTURE_BUTTON_TIME_POA, captureBtnTime);
                    saveSetting(MAX_IMAGE_SIZE_POA, maxImagesize);
                    saveSetting(IMAGE_HEIGHT_POA, imageheight);
                    saveSetting(IMAGE_WIDTH_POA, imagewidth);
                    saveSetting(ID_IMG_POA_OUTLINE_COLOR, idImageOutlineColor);
                    saveSetting(ID_IMG_POA_OUTLINE_ALPHA, ""+idImageOutlineAlpha);
                    saveSetting(DETECTED_ID_POA_OUTLINE_COLOR, detectedIdImageOutlineColor);
                    saveSetting(DETECTED_ID_POA_OUTLINE_ALPHA, ""+detectedIdImageOutlineAlpha);
                    saveSetting(ID_IMG_POA_OUTSIDE_OUTLINE_COLOR, idImageOutsideOutlineColor);
                    saveSetting(ID_IMG_POA_OUTSIDE_OUTLINE_ALPHA, ""+idImageOutsideOutlineAlpha);
                    saveSetting(DETECTED_ID_IMG_POA_OUTSIDE_OUTLINE_COLOR,""+detectedIdImageOutsideOutlineColor);
                    saveSetting(DETECTED_ID_IMG_POA_OUTSIDE_OUTLINE_ALPHA,""+detectedIdImageOutsideOutlineAlpha);
                }
            }
        });

        captureGenericDocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearResultFields();
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                } else {

                    if (!StringUtil.isEmpty(fieldNameEdtTxt.getText().toString())) {

                        String lightThreshold = imageLightThresholdPOAEdtTxt.getText().toString().trim();
                        String minFocusThreshold = imageMinFocusThresholdPOAEdtTxt.getText().toString().trim();
                        String maxFocusThreshold = imageMaxFocusThresholdPOAEdtTxt.getText().toString().trim();
                        String glareThreshold = imageGlarePercentagePOAEdtTxt.getText().toString().trim();
                        String captureBtnTime = enableCaptureButtonTimePOAEdtTxt.getText().toString().trim();
                        String maxImagesize = maxImageSizePOAEdtTxt.getText().toString().trim();
                        String imageheight = imageHeightPOAEdtTxt.getText().toString().trim();
                        String imagewidth = imageWidthPOAEdtTxt.getText().toString().trim();
                        String idImgOutlineColor = idImageOutlineColorPOAEdtTxt.getText().toString().trim();
                        String idImgOutlineAlpha = idImageOutlineAlphaPOAEdtTxt.getText().toString().trim();
                        String detectedIdImgOutlineColor = detectedIdOutlineColorPOAEdtTxt.getText().toString().trim();
                        String detectedIdImgOutlineAlpha = detectedIdOutlineColorAlphaPOA.getText().toString().trim();
                        String idImgOutsideOutlineColor = idImageOutsideOutlineColorPOAEdtTxt.getText().toString().trim();
                        String idImgOutsideOutlineAlpha = idImageOutsideOutlineAlphaPOAEdtTxt.getText().toString().trim();

                        String detectedIdImgOutsideOutlineColor = detectedidImageOutsideOutlineColorPOAEdtTxt.getText().toString().trim();
                        String detectedIdImgOutsideOutlineAlpha = detectedidImageOutsideOutlineAlphaPOAEdtTxt.getText().toString().trim();
                        String titleImageType = (String) titleImageCapturePOASpinner.getSelectedItem();
                        String fieldName=fieldNameEdtTxt.getText().toString().trim();

                        Bitmap titleBitmap = null;
                        if (!titleImageType.equalsIgnoreCase("None")) {
                            titleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title_image);
                        }

                        int minimumLightThreshold = (!StringUtil.isEmpty(lightThreshold) ? Integer.parseInt(lightThreshold) : ImageProcessingSDK.dLightThresholdPOA);
                        int minFocusScoreThreshold = (!StringUtil.isEmpty(minFocusThreshold) ? Integer.parseInt(minFocusThreshold) : ImageProcessingSDK.dMinFocusThresholdPOA);
                        int focusScoreThreshold = (!StringUtil.isEmpty(maxFocusThreshold) ? Integer.parseInt(maxFocusThreshold) : ImageProcessingSDK.dMaxFocusThresholdPOA);
                        int glarePercentageThreshold = (!StringUtil.isEmpty(glareThreshold) ? Integer.parseInt(glareThreshold) : ImageProcessingSDK.dGlarePercentagePOA);
                        int enableCaptureButtonTime = (!StringUtil.isEmpty(captureBtnTime) ? Integer.parseInt(captureBtnTime) : ImageProcessingSDK.dCaptureBtnEnableTimePOA);
                        int maxImageSize = (!StringUtil.isEmpty(maxImagesize) ? Integer.parseInt(maxImagesize) : ImageProcessingSDK.dImageSizePOA);
                        int highResolutionImageHeight = (!StringUtil.isEmpty(imageheight) ? Integer.parseInt(imageheight) : ImageProcessingSDK.dImageHeightPOA);
                        int highResolutionImageWidth = (!StringUtil.isEmpty(imagewidth) ? Integer.parseInt(imagewidth) : ImageProcessingSDK.dImgaeWidthPOA);
                        String idImageOutlineColor = (!StringUtil.isEmpty(idImgOutlineColor) ? idImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                        int idImageOutlineAlpha = (!StringUtil.isEmpty(idImgOutlineAlpha) ? Integer.parseInt(idImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                        String detectedIdImageOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutlineColor) ? detectedIdImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                        int detectedIdImageOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutlineAlpha) ? Integer.parseInt(detectedIdImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                        String idImageOutsideOutlineColor = (!StringUtil.isEmpty(idImgOutsideOutlineColor) ? idImgOutsideOutlineColor : ImageProcessingSDK.dOutsideIdOutlineColor);
                        int idImageOutsideOutlineAlpha = (!StringUtil.isEmpty(idImgOutsideOutlineAlpha) ? Integer.parseInt(idImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

                        String detectedIdImageOutsideOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineColor) ? detectedIdImgOutsideOutlineColor : ImageProcessingSDK.dOutsideIdOutlineColor);
                        int detectedIdImageOutsideOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineAlpha) ? Integer.parseInt(detectedIdImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

                        JSONObject addJSON = getAdditionalDataJSON();
                        if (null != addJSON) {
                            imageProcessingSDK.captureGenericDocument(MainActivity.this, capturePortraitChcBoxPOA.isChecked(), minimumLightThreshold, minFocusScoreThreshold,
                                    focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
                                    highResolutionImageHeight, highResolutionImageWidth, addJSON,
                                    new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor, detectedIdImageOutsideOutlineAlpha), titleBitmap,
                                    showInstructionPOA.isChecked(), 0,fieldName);
                        } else {
                            imageProcessingSDK.captureGenericDocument(MainActivity.this, capturePortraitChcBoxPOA.isChecked(), minimumLightThreshold, minFocusScoreThreshold,
                                    focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
                                    highResolutionImageHeight, highResolutionImageWidth, null,
                                    new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha), new ColorCode(detectedIdImageOutsideOutlineColor, detectedIdImageOutsideOutlineAlpha), titleBitmap,
                                    showInstructionPOA.isChecked(), 0,fieldName);
                        }

                        saveSetting(IMAGE_LIGHT_THRESHOLD_POA, lightThreshold);
                        saveSetting(IMAGE_MAX_FOCUS_THRESHOLD_POA, maxFocusThreshold);
                        saveSetting(IMAGE_MIN_FOCUS_THRESHOLD_POA, minFocusThreshold);
                        saveSetting(IMAGE_GLARE_PERCENTAGE_POA, glareThreshold);
                        saveSetting(CAPTURE_BUTTON_TIME_POA, captureBtnTime);
                        saveSetting(MAX_IMAGE_SIZE_POA, maxImagesize);
                        saveSetting(IMAGE_HEIGHT_POA, imageheight);
                        saveSetting(IMAGE_WIDTH_POA, imagewidth);
                        saveSetting(ID_IMG_POA_OUTLINE_COLOR, idImageOutlineColor);
                        saveSetting(ID_IMG_POA_OUTLINE_ALPHA, "" + idImageOutlineAlpha);
                        saveSetting(DETECTED_ID_POA_OUTLINE_COLOR, detectedIdImageOutlineColor);
                        saveSetting(DETECTED_ID_POA_OUTLINE_ALPHA, "" + detectedIdImageOutlineAlpha);
                        saveSetting(ID_IMG_POA_OUTSIDE_OUTLINE_COLOR, idImageOutsideOutlineColor);
                        saveSetting(ID_IMG_POA_OUTSIDE_OUTLINE_ALPHA, "" + idImageOutsideOutlineAlpha);
                        saveSetting(DETECTED_ID_IMG_POA_OUTSIDE_OUTLINE_COLOR, "" + detectedIdImageOutsideOutlineColor);
                        saveSetting(DETECTED_ID_IMG_POA_OUTSIDE_OUTLINE_ALPHA, "" + detectedIdImageOutsideOutlineAlpha);
                    }else {
                        fieldNameEdtTxt.setError("Please Enter Field Name");
                    }
                }
            }
        });


        captureBirthCertificateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearResultFields();
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                } else {

                    String lightThreshold = imageLightThresholdPOAEdtTxt.getText().toString().trim();
                    String minFocusThreshold = imageMinFocusThresholdPOAEdtTxt.getText().toString().trim();
                    String maxFocusThreshold = imageMaxFocusThresholdPOAEdtTxt.getText().toString().trim();
                    String glareThreshold = imageGlarePercentagePOAEdtTxt.getText().toString().trim();
                    String captureBtnTime = enableCaptureButtonTimePOAEdtTxt.getText().toString().trim();
                    String maxImagesize = maxImageSizePOAEdtTxt.getText().toString().trim();
                    String imageheight = imageHeightPOAEdtTxt.getText().toString().trim();
                    String imagewidth = imageWidthPOAEdtTxt.getText().toString().trim();
                    String idImgOutlineColor = idImageOutlineColorPOAEdtTxt.getText().toString().trim();
                    String idImgOutlineAlpha = idImageOutlineAlphaPOAEdtTxt.getText().toString().trim();
                    String detectedIdImgOutlineColor = detectedIdOutlineColorPOAEdtTxt.getText().toString().trim();
                    String detectedIdImgOutlineAlpha = detectedIdOutlineColorAlphaPOA.getText().toString().trim();
                    String idImgOutsideOutlineColor = idImageOutsideOutlineColorPOAEdtTxt.getText().toString().trim();
                    String idImgOutsideOutlineAlpha = idImageOutsideOutlineAlphaPOAEdtTxt.getText().toString().trim();

                    String detectedIdImgOutsideOutlineColor = detectedidImageOutsideOutlineColorPOAEdtTxt.getText().toString().trim();
                    String detectedIdImgOutsideOutlineAlpha = detectedidImageOutsideOutlineAlphaPOAEdtTxt.getText().toString().trim();
                    String titleImageType = (String) titleImageCapturePOASpinner.getSelectedItem();

                    Bitmap titleBitmap = null;
                    if(!titleImageType.equalsIgnoreCase("None")){
                        titleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title_image);
                    }

                    int minimumLightThreshold = (!StringUtil.isEmpty(lightThreshold) ? Integer.parseInt(lightThreshold) : ImageProcessingSDK.dLightThresholdPOA);
                    int minFocusScoreThreshold = (!StringUtil.isEmpty(minFocusThreshold) ? Integer.parseInt(minFocusThreshold) : ImageProcessingSDK.dMinFocusThresholdPOA);
                    int focusScoreThreshold = (!StringUtil.isEmpty(maxFocusThreshold) ? Integer.parseInt(maxFocusThreshold) : ImageProcessingSDK.dMaxFocusThresholdPOA);
                    int glarePercentageThreshold = (!StringUtil.isEmpty(glareThreshold) ? Integer.parseInt(glareThreshold) : ImageProcessingSDK.dGlarePercentagePOA);
                    int enableCaptureButtonTime = (!StringUtil.isEmpty(captureBtnTime) ? Integer.parseInt(captureBtnTime) : ImageProcessingSDK.dCaptureBtnEnableTimePOA);
                    int maxImageSize = (!StringUtil.isEmpty(maxImagesize) ? Integer.parseInt(maxImagesize) : ImageProcessingSDK.dImageSizePOA);
                    int highResolutionImageHeight = (!StringUtil.isEmpty(imageheight) ? Integer.parseInt(imageheight) : ImageProcessingSDK.dImageHeightPOA);
                    int highResolutionImageWidth = (!StringUtil.isEmpty(imagewidth) ? Integer.parseInt(imagewidth) : ImageProcessingSDK.dImgaeWidthPOA);
                    String idImageOutlineColor = (!StringUtil.isEmpty(idImgOutlineColor) ? idImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                    int idImageOutlineAlpha = (!StringUtil.isEmpty(idImgOutlineAlpha) ? Integer.parseInt(idImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                    String detectedIdImageOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutlineColor) ? detectedIdImgOutlineColor : ImageProcessingSDK.dIdOutlineColor);
                    int detectedIdImageOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutlineAlpha) ? Integer.parseInt(detectedIdImgOutlineAlpha) : ImageProcessingSDK.dAlpha);
                    String idImageOutsideOutlineColor = (!StringUtil.isEmpty(idImgOutsideOutlineColor) ? idImgOutsideOutlineColor : ImageProcessingSDK.dOutsideIdOutlineColor);
                    int idImageOutsideOutlineAlpha = (!StringUtil.isEmpty(idImgOutsideOutlineAlpha) ? Integer.parseInt(idImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

                    String detectedIdImageOutsideOutlineColor = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineColor) ? detectedIdImgOutsideOutlineColor : ImageProcessingSDK.dOutsideIdOutlineColor);
                    int detectedIdImageOutsideOutlineAlpha = (!StringUtil.isEmpty(detectedIdImgOutsideOutlineAlpha) ? Integer.parseInt(detectedIdImgOutsideOutlineAlpha) : ImageProcessingSDK.dAlpha);

//                imageProcessingSDK.captureBirthCertificate(MainActivity.this);
//                imageProcessingSDK.captureBirthCertificate(MainActivity.this, capturePortraitChcBoxPOA.isChecked());
                    JSONObject addJSON = getAdditionalDataJSON();
                    if(null != addJSON){
                        imageProcessingSDK.captureBirthCertificate(MainActivity.this, capturePortraitChcBoxPOA.isChecked(), minimumLightThreshold, minFocusScoreThreshold,
                                focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
                                highResolutionImageHeight, highResolutionImageWidth, addJSON,
                                new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha),new ColorCode(detectedIdImageOutsideOutlineColor,detectedIdImageOutsideOutlineAlpha),titleBitmap,
                                showInstructionPOA.isChecked(), 0);
                    }else {
                        imageProcessingSDK.captureBirthCertificate(MainActivity.this, capturePortraitChcBoxPOA.isChecked(), minimumLightThreshold, minFocusScoreThreshold,
                                focusScoreThreshold, glarePercentageThreshold, enableCaptureButtonTime, maxImageSize,
                                highResolutionImageHeight, highResolutionImageWidth, null,
                                new ColorCode(idImageOutlineColor, idImageOutlineAlpha), new ColorCode(detectedIdImageOutlineColor, detectedIdImageOutlineAlpha), new ColorCode(idImageOutsideOutlineColor, idImageOutsideOutlineAlpha),new ColorCode(detectedIdImageOutsideOutlineColor,detectedIdImageOutsideOutlineAlpha),titleBitmap,
                                showInstructionPOA.isChecked(), 0);
                    }

                    saveSetting(IMAGE_LIGHT_THRESHOLD_POA, lightThreshold);
                    saveSetting(IMAGE_MAX_FOCUS_THRESHOLD_POA, maxFocusThreshold);
                    saveSetting(IMAGE_MIN_FOCUS_THRESHOLD_POA, minFocusThreshold);
                    saveSetting(IMAGE_GLARE_PERCENTAGE_POA, glareThreshold);
                    saveSetting(CAPTURE_BUTTON_TIME_POA, captureBtnTime);
                    saveSetting(MAX_IMAGE_SIZE_POA, maxImagesize);
                    saveSetting(IMAGE_HEIGHT_POA, imageheight);
                    saveSetting(IMAGE_WIDTH_POA, imagewidth);
                    saveSetting(ID_IMG_POA_OUTLINE_COLOR, idImageOutlineColor);
                    saveSetting(ID_IMG_POA_OUTLINE_ALPHA, ""+idImageOutlineAlpha);
                    saveSetting(DETECTED_ID_POA_OUTLINE_COLOR, detectedIdImageOutlineColor);
                    saveSetting(DETECTED_ID_POA_OUTLINE_ALPHA, ""+detectedIdImageOutlineAlpha);
                    saveSetting(ID_IMG_POA_OUTSIDE_OUTLINE_COLOR, idImageOutsideOutlineColor);
                    saveSetting(ID_IMG_POA_OUTSIDE_OUTLINE_ALPHA, ""+idImageOutsideOutlineAlpha);
                    saveSetting(DETECTED_ID_IMG_POA_OUTSIDE_OUTLINE_COLOR,""+detectedIdImageOutsideOutlineColor);
                    saveSetting(DETECTED_ID_IMG_POA_OUTSIDE_OUTLINE_ALPHA,""+detectedIdImageOutsideOutlineAlpha);
                }
            }
        });

        capturePOAVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearResultFields();
                progressDialog.show(); //Displaying progress
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);

                JSONObject addressJSON = getAddressDataJSON();
                JSONObject addJSON = getAdditionalDataJSON();
                if(null!=addJSON){
                    imageProcessingSDK.verifyAddress(MainActivity.this, addressJSON, addJSON,doExtract.isChecked(), doVerify.isChecked(), isFinalSubmit.isChecked());
                }else {
                    imageProcessingSDK.verifyAddress(MainActivity.this, addressJSON, null,doExtract.isChecked(), doVerify.isChecked(), isFinalSubmit.isChecked());
                }

            }
        });

        startVoiceRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearResultFields();
                JSONObject addJSON = getAdditionalDataJSON();
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
                } else {
                    if(!StringUtil.isEmpty(voiceRecordingTimingEdtTxt.getText().toString())){
//                        imageProcessingSDK.startVoiceRecording(MainActivity.this, Integer.parseInt(voiceRecordingTimingEdtTxt.getText().toString()));
                        imageProcessingSDK.startVoiceRecording(MainActivity.this, Integer.parseInt(voiceRecordingTimingEdtTxt.getText().toString()), true, addJSON);
                    }else {
                        voiceRecordingTimingEdtTxt.setError("Please enter time");
                    }
                }
            }
        });

        stopVoiceRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                imageProcessingSDK.stopVoiceRecording(MainActivity.this);
            }
        });

        getGPSLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
//                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
//                }else {
                imageProcessingSDK.getGPSCoordinate(MainActivity.this);
//                }
            }
        });

        fourFingerCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},0);
                } else {
                    if(StringUtil.isEmpty(numOfFingerToCheckFocus.getText().toString())) {
                        numOfFingerToCheckFocus.setError("Please enter finger count ");
                        return;
                    }
                    if(StringUtil.isEmpty(fourFingerCaptureGlareRatio.getText().toString())) {
                        fourFingerCaptureGlareRatio.setError("Please enter glare percentage ");
                        return;
                    }

                    if(!StringUtil.isEmpty(fourFingerCaptureFocusedRatio.getText().toString())) {
                        int fourFingFocusedRatio = Integer.valueOf(fourFingerCaptureFocusedRatio.getText().toString());
                        int fourFingGlarePerc = Integer.valueOf(fourFingerCaptureGlareRatio.getText().toString());
                        int numOfFingerToCheck = Integer.valueOf(numOfFingerToCheckFocus.getText().toString());
                        try {
                            imageProcessingSDK.captureFourFingerprint(MainActivity.this, fourFingFocusedRatio, fourFingGlarePerc, numOfFingerToCheck);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        fourFingerCaptureFocusedRatio.setError("Please enter focus threshold ");
                    }
                }
            }
        });

        captureFingerprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FingerprintDeviceType fingerprintDeviceType = (FingerprintDeviceType)fingerprintDeviceSpinner.getSelectedItem();
                FingerType fingerType = (FingerType)fingerTypeSpinner.getSelectedItem();

                String nfiq = minNFIQEdtTxt.getText().toString().trim();
                String deviceTimeout = deviceTimeoutEdtTxt.getText().toString().trim();
                String imageSize = minImageSizeEdtTxt.getText().toString().trim();

                int nfiqVal = (!StringUtil.isEmpty(nfiq) ? Integer.parseInt(nfiq) : ImageProcessingSDK.dFingerprintNFIQValue);
                int deviceTimeoutVal = (!StringUtil.isEmpty(deviceTimeout) ? Integer.parseInt(deviceTimeout) : ImageProcessingSDK.dFingerprintDeviceTimeout);
                int imageSizeVal = (!StringUtil.isEmpty(imageSize) ? Integer.parseInt(imageSize) : ImageProcessingSDK.dFingerprintImageSize);

                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                imageProcessingSDK.captureFingerprint(MainActivity.this, fingerprintDeviceType, fingerType, nfiqVal, deviceTimeoutVal, imageSizeVal);
            }
        });

        clearFingerprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FingerType fingerType = (FingerType)fingerTypeSpinner.getSelectedItem();

                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                imageProcessingSDK.clearFingerprint(fingerType);

                Iterator<String> keySet = capturedImageData.keySet().iterator();
                while (keySet.hasNext()) {
                    String key = keySet.next();
                    if (key.contains(fingerType.toString())) {
                        keySet.remove();
                    }
                }

                resultImagePagerAdapter.notifyDataSetChanged();
            }
        });

        clearAllFingerprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                imageProcessingSDK.clearFingerprint(null); //For clearing all fingerprint data

                Iterator<String> keySet = capturedImageData.keySet().iterator();
                while (keySet.hasNext()) {
                    String key = keySet.next();
                    if (key.contains("FINGER") || key.contains("THUMB") || key.contains("UNKNOWN")) {
                        keySet.remove();
                    }
                }

                resultImagePagerAdapter.notifyDataSetChanged();
            }
        });

        enrollFingerprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show(); //Displaying progress
                clearResultFields();

                JSONObject addJSON = getAdditionalDataJSON();

                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                imageProcessingSDK.enrollFingerprint(MainActivity.this, addJSON, clearFormKeyFingerprint.isChecked());
            }
        });

        verifyFingerprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show(); //Displaying progress
                clearResultFields();

                JSONObject addJSON = getAdditionalDataJSON();

                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                imageProcessingSDK.verifyFingerprint(MainActivity.this, addJSON, clearFormKeyFingerprint.isChecked());
            }
        });

        videoCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                JSONObject addJSON = getAdditionalDataJSON();
                if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO},0);
                } else {
                    if(!StringUtil.isEmpty(videoRecordingTimingEdtTxt.getText().toString())) {
                        layoutVideoView.setVisibility(View.GONE);
                        videoView.setVisibility(View.GONE);
                        imageProcessingSDK.startVideoRecording(MainActivity.this, Integer.parseInt(videoRecordingTimingEdtTxt.getText().toString()),addJSON,textOnVideoScreen.getText().toString());
                    } else {
                        videoRecordingTimingEdtTxt.setError("Please enter time");
                    }
                }
            }
        });

        pickFrontImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, FRONT_IMG_REQ_CODE);
            }
        });

        pickBackImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, BACK_IMG_REQ_CODE);
            }
        });

        pickFaceImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, FACE_IMG_REQ_CODE);
            }
        });

        setImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                imageProcessingSDK.setImages(requestImageMap);
            }
        });

        findViewById(R.id.scan_barcode_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                JSONObject addJSON = getAdditionalDataJSON();
                imageProcessingSDK.scanBarcode(MainActivity.this,addJSON);
            }
        });


        findViewById(R.id.completeOperation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show(); //Displaying progress
                clearResultFields();
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                imageProcessingSDK.completeOperation(MainActivity.this);
            }
        });

        findViewById(R.id.request_response_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean filePresent = false;
                if(0 == requestResponseTypeSpinner.getSelectedItemId()) {
                    String path = filename(0, ImageProcessingSDK.PROCESS_IMAGE_REQUEST, "", "", ".txt");
                    File file = new File(path);
                    if(file.exists()) {
                        filePresent = true;
                    }
                } else if (1 == requestResponseTypeSpinner.getSelectedItemId()) {
                    String path = filename(0, ImageProcessingSDK.MATCH_FACE_REQUEST, "", "", ".txt");
                    File file = new File(path);
                    if(file.exists()) {
                        filePresent = true;
                    }
                } else if (2 == requestResponseTypeSpinner.getSelectedItemId()) {
                    String path = filename(0, ImageProcessingSDK.VERIFY_CUST_REQUEST, "", "", ".txt");
                    File file = new File(path);
                    if(file.exists()) {
                        filePresent = true;
                    }
                } else if (3 == requestResponseTypeSpinner.getSelectedItemId()) {
                    String path = filename(0, ImageProcessingSDK.PROCESS_IMAGE_MATCH_FACE_REQUEST, "", "", ".txt");
                    File file = new File(path);
                    if(file.exists()) {
                        filePresent = true;
                    }
                } else if (4 == requestResponseTypeSpinner.getSelectedItemId())  {
                    String path = filename(0, ImageProcessingSDK.COMPLETE_OPERATION_REQUEST, "", "", ".txt");
                    File file = new File(path);
                    if(file.exists()) {
                        filePresent = true;
                    }
                }

                if(filePresent) {
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putLong("Position",requestResponseTypeSpinner.getSelectedItemId());
                            FragmentManager fragmentMan = ((FragmentActivity) MainActivity.this).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentMan.beginTransaction();

                            RequestResponseFragment reqresFrag = new RequestResponseFragment();

                            reqresFrag.setArguments(bundle);

                            fragmentTransaction.add(android.R.id.content,reqresFrag);
                            fragmentTransaction.commit();
                        }
                    }, 500);

                } else {
                    Toast.makeText(MainActivity.this,R.string.request_response_not_found,Toast.LENGTH_LONG).show();
                }
            }
        }) ;

        findViewById(R.id.deleteData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearResultFields();
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                imageProcessingSDK.deleteData();

                capturedImageData.clear();
                resultImagePagerAdapter.notifyDataSetChanged();
                layoutVideoView.setVisibility(View.GONE);
                videoView.stopPlayback();
                videoView.setVideoURI(null);
                videoView.setVisibility(View.GONE);

                snippetFieldNameMap.clear();
                requestImageMap.clear();

            }
        });

        voiceRecordingPlayer.setVisibility(View.GONE);
        voiceRecordingPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        voiceRecordingPlayer.setImageResource(R.drawable.play_recording);
                    }else {
                        mediaPlayer.start();
                        voiceRecordingPlayer.setImageResource(R.drawable.pause_recording);
                    }
                }
            }
        });

        findViewById(R.id.playPauseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoView.isPlaying()) {
                    videoView.pause();
                    playPauseVideoButton.setBackground(getResources().getDrawable(R.drawable.play_icon));
                } else {
                    videoView.start();
                    playPauseVideoButton.setBackground(getResources().getDrawable(R.drawable.pause_icon));
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                playPauseVideoButton.setBackground(getResources().getDrawable(R.drawable.play_icon));
            }
        });

        signatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                imageProcessingSDK.captureSignature(MainActivity.this);
            }
        });

        createEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearResultFields();
                progressDialog.show(); //Displaying progress
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);

                JSONObject empJSON = getEmployeeDataJSON();
                JSONObject additionalJSON = getAdditionalDataJSON();
                if (null != additionalJSON) {
                    imageProcessingSDK.createEmployee(MainActivity.this, empJSON, additionalJSON, empIsFinalSubmit.isChecked(), clearFormKeyEmpVerification.isChecked());
                } else {
                    imageProcessingSDK.createEmployee(MainActivity.this, empJSON, null, empIsFinalSubmit.isChecked(), clearFormKeyEmpVerification.isChecked());
                }

            }
        });

        verifyEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String empCode = empCodeEdtTxt.getText().toString();
                clearResultFields();
                progressDialog.show(); //Displaying progress
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);

                JSONObject additionalJSON = getAdditionalDataJSON();

                if (null != additionalJSON) {
                    imageProcessingSDK.verifyEmployee(MainActivity.this, empCode, additionalJSON, empIsFinalSubmit.isChecked(),clearFormKeyEmpVerification.isChecked());

                } else {
                    imageProcessingSDK.verifyEmployee(MainActivity.this, empCode, null, empIsFinalSubmit.isChecked(),clearFormKeyEmpVerification.isChecked());
                }

            }
        });

        matchFaceCreateEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show(); //Displaying progress
                clearResultFields();
                String faceImageType = ((FaceImageType) faceImageTypeSpinner.getSelectedItem()).getFaceImageType().toString();
                String idImageType = ((IDImageType) idImageTypeSpinner.getSelectedItem()).getIDImageType().toString();

                JSONObject addJSON = getAdditionalDataJSON();
                JSONObject empJSON = getEmployeeDataJSON();

                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                imageProcessingSDK.matchFaceImage(MainActivity.this, faceImageType, idImageType, addJSON, clearFormKeyMatchFace.isChecked(), empJSON);

            }
        });

        generateTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clearResultFields();
                progressDialog.show(); //Displaying progress
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);

                JSONObject additionalJSON = getAdditionalDataJSON();

                if (null != additionalJSON) {
                    imageProcessingSDK.generateToken(MainActivity.this);
                } else {
                    imageProcessingSDK.generateToken(MainActivity.this);
                }

            }
        });
        verifyToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResultFields();
                progressDialog.show(); //Displaying progress
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);

                String token= retrieveSetting(AUTHORIZE_TOKEN,"");
                JSONObject additionalJSON = getAdditionalDataJSON();

                if (null != additionalJSON) {
                    imageProcessingSDK.verifyToken(MainActivity.this);
                } else {
                    imageProcessingSDK.verifyToken(MainActivity.this);
                }
            }
        });

        findViewById(R.id.generate_OTP_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResultFields();
                progressDialog.show(); //Displaying progress
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                String notificationType = otpNotificationType.getSelectedItem().toString();
                JSONObject addJSON = getAdditionalDataJSON();
                imageProcessingSDK.generateOTP(MainActivity.this, addJSON, emailID.getText().toString().trim(), mobileNumber.getText().toString().trim(), notificationType);
            }
        });

        findViewById(R.id.verify_OTP_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearResultFields();
                progressDialog.show(); //Displaying progress
                imageProcessingSDK.setImageProcessingResponseListener(MainActivity.this);
                JSONObject addJSON = getAdditionalDataJSON();
                imageProcessingSDK.verifyOTP(MainActivity.this, addJSON, received_otp.getText().toString().trim());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            String base64Image = "";
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                base64Image = StringUtil.encodeBitmapTobase64(selectedImage);
                Log.d("", "");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            switch(requestCode) {
                case FRONT_IMG_REQ_CODE:
                    requestImageMap.put(ImageType.FRONT.toString(), base64Image);
                    capturedImageData.put(ResultData.FRONT_IMAGE_DATA, new ResultData(ResultData.FRONT_IMAGE_DATA, base64Image));
                    break;
                case BACK_IMG_REQ_CODE:
                    requestImageMap.put(ImageType.BACK.toString(), base64Image);
                    capturedImageData.put(ResultData.BACK_IMAGE_DATA, new ResultData(ResultData.BACK_IMAGE_DATA, base64Image));
                    break;
                case FACE_IMG_REQ_CODE:
                    requestImageMap.put(FaceImageType.FACE.toString(), base64Image);
                    capturedImageData.put(ResultData.FACE_IMAGE_DATA, new ResultData(ResultData.FACE_IMAGE_DATA, base64Image));
                    break;
            }
        }
        resultImagePagerAdapter.notifyDataSetChanged();
    }

    private void enableButton() {
        boolean isEnable = false;
        if (null != imageProcessingSDK) {
            isEnable = true;
        }

        captureFrontButton.setEnabled(isEnable);
        captureBackButton.setEnabled(isEnable);
        findViewById(R.id.processImage).setEnabled(isEnable);
        captureFaceButton.setEnabled(isEnable);
        findViewById(R.id.matchFaceImage).setEnabled(isEnable);
        findViewById(R.id.processImageAndMatchFace).setEnabled(isEnable);
        findViewById(R.id.completeOperation).setEnabled(isEnable);
        findViewById(R.id.deleteData).setEnabled(isEnable);
        findViewById(R.id.detectCard).setEnabled(isEnable);
        capturePOAButton.setEnabled(isEnable);
        captureBankStatementButton.setEnabled(isEnable);
        captureBirthCertificateButton.setEnabled(isEnable);
        verifyCustomerButton.setEnabled(isEnable);
        addLabelButton.setEnabled(isEnable);
        initLabelsButton.setEnabled(isEnable);
        startVoiceRecordingButton.setEnabled(isEnable);
        stopVoiceRecordingButton.setEnabled(isEnable);
        customizeUIButton.setEnabled(isEnable);
        getGPSLocationButton.setEnabled(isEnable);
        fourFingerCapture.setEnabled(isEnable);
        captureFingerprintButton.setEnabled(isEnable);
        videoCaptureButton.setEnabled(isEnable);
        pickFrontImageBtn.setEnabled(isEnable);
        pickBackImageBtn.setEnabled(isEnable);
        pickFaceImageBtn.setEnabled(isEnable);
        setImagesBtn.setEnabled(isEnable);
        requestResponseBtn.setEnabled(isEnable);
        findViewById(R.id.scan_barcode_button).setEnabled(isEnable);
        captureGenericDocButton.setEnabled(isEnable);
        signatureButton.setEnabled(isEnable);
        addField.setEnabled(isEnable);
        snippetCapture.setEnabled(isEnable);
        capturePOAVerification.setEnabled(isEnable);
        clearFingerprintButton.setEnabled(isEnable);
        clearAllFingerprintButton.setEnabled(isEnable);
        enrollFingerprintButton.setEnabled(isEnable);
        verifyFingerprintButton.setEnabled(isEnable);
        createEmployeeButton.setEnabled(isEnable);
        matchFaceCreateEmployee.setEnabled(isEnable);
        verifyEmployeeButton.setEnabled(isEnable);
        generateTokenButton.setEnabled(isEnable);
        verifyToken.setEnabled(isEnable);
        findViewById(R.id.generate_OTP_button).setEnabled(isEnable);
        findViewById(R.id.verify_OTP_button).setEnabled(isEnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableButton();
    }

    @Override
    public void onCustomizeUserInterfaceResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onCustomizeUserInterfaceResultAvailable");
    }

    @Override
    public void onImageProcessingResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onImageProcessingResultAvailable");
        String data = "";
        if (null != resultMap
                && !resultMap.isEmpty()) {
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if ("ProcessedImage".equals(key)
                        || "ProcessedImageID_BACK".equals(key)) {
                    data += key + " : \n";
                } else {
                    data += key + " : " + value + "\n";
                }

                System.out.println("Response Map : " + data);
            }
        }

        if (!StringUtil.isEmpty(data)) {
            resultTv.setText(data);
        } else {
            resultTv.setText( response.getStatusCode() + ": " + response.getStatusMessage());
            if(response.getStatusCode() == 15){
                Toast.makeText(this, response.getStatusCode() + ": " + response.getStatusMessage(), Toast.LENGTH_LONG).show();
            }
        }
        progressDialog.cancel();//Stopping progress
    }

    @Override
    public void onImageProcessingAndFaceMatchingResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onImageProcessingAndFaceMatchingResultAvailable");
        String data = "";
        if (null != resultMap
                && !resultMap.isEmpty()) {
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if ("ProcessedImage".equals(key)
                        || "ProcessedImageID_BACK".equals(key)) {
                    data += key + " : \n";
                } else {
                    data += key + " : " + value + "\n";
                }

                System.out.println("Response Map : " + data);
            }
        }

        if (!StringUtil.isEmpty(data)) {
            resultTv.setText(data);
        } else {
            resultTv.setText( response.getStatusCode() + ": " + response.getStatusMessage());
            if(response.getStatusCode() == 15){
                Toast.makeText(this, response.getStatusCode() + ": " + response.getStatusMessage(), Toast.LENGTH_LONG).show();
            }
        }
        progressDialog.cancel();//Stopping progress
    }

    @Override
    public void onAutoImageCaptureResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onAutoImageCaptureResultAvailable");
        if (null != response) {
            Toast.makeText(MainActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();

            String imageBase64 = null;
            if(null != resultMap){
                if (resultMap.containsKey(ImageType.FRONT.toString())) {
                    imageBase64 = resultMap.get(ImageType.FRONT.toString());
                    if (!StringUtil.isEmpty(imageBase64)) {
                        capturedImageData.put(ResultData.FRONT_IMAGE_DATA, new ResultData(ResultData.FRONT_IMAGE_DATA, imageBase64));
                    }
                } else if (resultMap.containsKey(ImageType.BACK.toString())) {
                    imageBase64 = resultMap.get(ImageType.BACK.toString());
                    if (!StringUtil.isEmpty(imageBase64)) {
                        capturedImageData.put(ResultData.BACK_IMAGE_DATA, new ResultData(ResultData.BACK_IMAGE_DATA, imageBase64));
                    }
                }
            }
            resultImagePagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAutoFillResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onAutoFillFieldInformationAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onFaceDetectionResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onFaceDetectionResultAvailable");
        if (null != response) {
            Toast.makeText(MainActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();

            String faceImageBase64 = null, processedFaceImageBase64 = null,ovalFaceImageBase64 = null;
            if(null != resultMap){
                if (resultMap.containsKey(FaceImageType.FACE.toString())) {
                    faceImageBase64 = resultMap.get(FaceImageType.FACE.toString());
                }
                if (resultMap.containsKey(FaceImageType.PROCESSED_FACE.toString())) {
                    processedFaceImageBase64 = resultMap.get(FaceImageType.PROCESSED_FACE.toString());
                }
                if (resultMap.containsKey(FaceImageType.OVAL_FACE.toString())) {
                    ovalFaceImageBase64 = resultMap.get(FaceImageType.OVAL_FACE.toString());
                }
            }

            if (!StringUtil.isEmpty(faceImageBase64)) {
                capturedImageData.put(ResultData.FACE_IMAGE_DATA, new ResultData(ResultData.FACE_IMAGE_DATA, faceImageBase64));
            }

            if (!StringUtil.isEmpty(processedFaceImageBase64)) {
                capturedImageData.put(ResultData.PROCESSED_FACE_IMAGE_DATA, new ResultData(ResultData.PROCESSED_FACE_IMAGE_DATA, processedFaceImageBase64));
            }

            if (!StringUtil.isEmpty(ovalFaceImageBase64)) {
                capturedImageData.put(ResultData.OVAL_FACE_IMAGE_DATA, new ResultData(ResultData.OVAL_FACE_IMAGE_DATA, ovalFaceImageBase64));
            }
            resultImagePagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFaceMatchingResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onFaceMatchingResultAvailable");
        String data = "";
        if (null != resultMap && !resultMap.isEmpty()) {
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                data += key + " : " + value + "\n";

                System.out.println("Response Map : " + data);
            }
        }

        if (!StringUtil.isEmpty(data)) {
            resultTv.setText(data);
        } else {
            resultTv.setText( response.getStatusCode() + ": " + response.getStatusMessage());
            if(response.getStatusCode() == 15){
                Toast.makeText(this, response.getStatusCode() + ": " + response.getStatusMessage(), Toast.LENGTH_LONG).show();
            }
        }
        progressDialog.cancel();//Stopping progress
    }

    @Override
    public void onCustomerVerificationResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onCustomerVerificationResultAvailable");
        String data = "";
        if (null != resultMap && !resultMap.isEmpty()) {
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                data += key + " : " + value + "\n";

                System.out.println("Response Map : " + data);
            }
        }

        if (!StringUtil.isEmpty(data)) {
            resultTv.setText(data);
        } else {
            resultTv.setText( response.getStatusCode() + ": " + response.getStatusMessage());
            if(response.getStatusCode() == 15){
                Toast.makeText(this, response.getStatusCode() + ": " + response.getStatusMessage(), Toast.LENGTH_LONG).show();
            }
        }
        progressDialog.cancel();//Stopping progress
    }

    @Override
    public void onUpdateCustomerFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onUpdateCustomerFinished");
        String data = "";
        if (null != resultMap && !resultMap.isEmpty()) {
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                data += key + " : " + value + "\n";

                System.out.println("Response Map : " + data);
            }
        }

        if (!StringUtil.isEmpty(data)) {
            resultTv.setText(data);
        } else {
            resultTv.setText( response.getStatusCode() + ": " + response.getStatusMessage());
            if(response.getStatusCode() == 15){
                Toast.makeText(this, response.getStatusCode() + ": " + response.getStatusMessage(), Toast.LENGTH_LONG).show();
            }
        }
        progressDialog.cancel();
    }

    @Override
    public void onVideoConferencingFinished(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onDownloadXsltResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onCardDetectionResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onCardDetectionResultAvailable");
        if (null != response) {
            Toast.makeText(MainActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();

            String data = "";
            String cardImageBase64 = null;
            if (null != resultMap && !resultMap.isEmpty()) {
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (!key.contains(CardImageType.CARD.toString()))
                        data += key + " : " + value + "\n";
                }

                if (resultMap.containsKey(CardImageType.CARD.toString())) {
                    cardImageBase64 = resultMap.get(CardImageType.CARD.toString());
                }
            }

            if (!StringUtil.isEmpty(cardImageBase64)) {
                capturedImageData.put(ResultData.CARD_IMAGE_DATA, new ResultData(ResultData.CARD_IMAGE_DATA, cardImageBase64));
            }
            resultImagePagerAdapter.notifyDataSetChanged();

            if (!StringUtil.isEmpty(data)) {
                resultTv.setText(data);
            } else {
                resultTv.setText(response.getStatusMessage());
            }
        }
    }

    @Override
    public void onCaptureProofOfAddressResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onCaptureProofOfAddressResultAvailable");
        if (null != response) {
            Toast.makeText(MainActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();

            String poaImageBase64 = null;
            if(null != resultMap){
                if (resultMap.containsKey(ImageType.POA_IMAGE.toString())) {
                    poaImageBase64 = resultMap.get(ImageType.POA_IMAGE.toString());
                }
            }
            if (!StringUtil.isEmpty(poaImageBase64)) {
                capturedImageData.put(ResultData.POA_IMAGE_DATA, new ResultData(ResultData.POA_IMAGE_DATA, poaImageBase64));
            }
            resultImagePagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCaptureBankStatementResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onCaptureBankStatementResultAvailable");
        if (null != response) {
            Toast.makeText(MainActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();

            String bankStmtImageBase64 = null;
            if(null != resultMap){
                if (resultMap.containsKey(ImageType.BANK_STATEMENT.toString())) {
                    bankStmtImageBase64 = resultMap.get(ImageType.BANK_STATEMENT.toString());
                }
            }
            if (!StringUtil.isEmpty(bankStmtImageBase64)) {
                capturedImageData.put(ResultData.BANK_STATEMENT_IMG_DATA, new ResultData(ResultData.BANK_STATEMENT_IMG_DATA, bankStmtImageBase64));
            }
            resultImagePagerAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onCaptureGenericDocumentResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK", "CALLBACK:::: onCaptureGenericDocumentResultAvailable");
        if (null != response) {
            Toast.makeText(MainActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();

            String bankStmtImageBase64 = null,fieldName=null;
            if (null != resultMap) {
                if (resultMap.containsKey(ImageType.GENERIC_DOCUMENT.toString())) {
                    bankStmtImageBase64 = resultMap.get(ImageType.GENERIC_DOCUMENT.toString());
                }
                if (resultMap.containsKey("fieldName")){
                    fieldName=resultMap.get("fieldName");
                    // System.out.println("******Field Name******"  +fieldName);
                }
            }
            if (!StringUtil.isEmpty(bankStmtImageBase64)) {
                capturedImageData.put(fieldName, new ResultData(fieldName, bankStmtImageBase64));
            }
            resultImagePagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCaptureBirthCertificateResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onCaptureBirthCertificateResultAvailable");
        if (null != response) {
            Toast.makeText(MainActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();

            String bankStmtImageBase64 = null;
            if(null !=  resultMap){
                if (resultMap.containsKey(ImageType.BIRTH_CERTIFICATE.toString())) {
                    bankStmtImageBase64 = resultMap.get(ImageType.BIRTH_CERTIFICATE.toString());
                }
            }
            if (!StringUtil.isEmpty(bankStmtImageBase64)) {
                capturedImageData.put(ResultData.BIRTH_CERTIFICATE_IMG_DATA, new ResultData(ResultData.BIRTH_CERTIFICATE_IMG_DATA, bankStmtImageBase64));
            }
            resultImagePagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onVoiceRecordingFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onVoiceRecordingFinished");
        if (null != response) {
            if(response.getStatusCode() == ResponseStatusCode.VOICE_RECORDING_ALREADY_RUNNING.getStatusCode()){
                Toast.makeText(MainActivity.this, "Failed : "+response.getStatusMessage(), Toast.LENGTH_SHORT).show();
                voiceRecordingPlayer.setVisibility(View.GONE);
            }else if(response.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()){
                Toast.makeText(MainActivity.this, "Success : "+response.getStatusMessage(), Toast.LENGTH_SHORT).show();
                mediaPlayer = MediaPlayer.create(this, Uri.parse(resultMap.get("FILEPATH")));
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        voiceRecordingPlayer.setImageResource(R.drawable.play_recording);
                    }
                });
                voiceRecordingPlayer.setVisibility(View.VISIBLE);
            }else if(response.getStatusCode() == ResponseStatusCode.OPERATION_CANCEL.getStatusCode()){
                Toast.makeText(MainActivity.this, "Cancel : "+response.getStatusMessage(), Toast.LENGTH_SHORT).show();
                voiceRecordingPlayer.setVisibility(View.GONE);
            }
            resultTv.setText(response.getStatusMessage());

        }
    }

    @Override
    public void onGPSCoordinateAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onGPSCoordinateAvailable");
        if (null != response) {
            Toast.makeText(MainActivity.this, "GPS : "+response.getStatusMessage(), Toast.LENGTH_SHORT).show();
            resultTv.setText(response.getStatusMessage());
        }
    }

    @Override
    public void onFourFingerCaptureFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onFourFingerCaptureFinished");
        if(null != response) {
            Toast.makeText(MainActivity.this, "" + response.getStatusMessage(), Toast.LENGTH_SHORT).show();
            if (response.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()) {
                for (Map.Entry<String, String> map : resultMap.entrySet()) {
                    capturedImageData.put(map.getKey(), new ResultData(map.getKey(), map.getValue()));
                }
                resultImagePagerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onFingerprintCaptureFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onFingerprintCaptureFinished");
        if(null != response) {
            Toast.makeText(MainActivity.this, "" + response.getStatusMessage(), Toast.LENGTH_SHORT).show();
            if (response.getStatusCode() == ResponseStatusCode.SUCCESS.getStatusCode()) {
                String fingerType = null;
                String fingerprint = null;
                String fingerprintcount = "0";
                for (Map.Entry<String, String> map : resultMap.entrySet()) {
                    if(map.getKey().equals(ImageProcessingSDK.FINGERPRINT)){
                        fingerprint = map.getValue();
                    }
                    if(map.getKey().equals(ImageProcessingSDK.FINGER_TYPE)){
                        fingerType = map.getValue();
                    }
                    if(map.getKey().equals(ImageProcessingSDK.FINGERPRINT_COUNT)){
                        fingerprintcount = map.getValue();
                    }
                }
                if(null != fingerType && null != fingerprint){
                    String type = fingerType + fingerprintcount;
                    capturedImageData.put(type, new ResultData(type, fingerprint));
                }
                resultImagePagerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onFingerprintEnrolmentFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onFingerprintEnrolmentFinished");
        if(null != response) {
            Toast.makeText(MainActivity.this, "" + response.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
        String data = "";
        if (null != resultMap && !resultMap.isEmpty()) {
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                data += key + " : " + value + "\n";

                System.out.println("Response Map : " + data);
            }
        }

        if (!StringUtil.isEmpty(data)) {
            resultTv.setText(data);
        } else {
            resultTv.setText( response.getStatusCode() + ": " + response.getStatusMessage());
        }
        progressDialog.cancel();//Stopping progress
    }

    @Override
    public void onFingerprintVerificationFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onFingerprintVerificationFinished");
        if(null != response) {
            Toast.makeText(MainActivity.this, "" + response.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
        String data = "";
        if (null != resultMap && !resultMap.isEmpty()) {
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                data += key + " : " + value + "\n";

                System.out.println("Response Map : " + data);
            }
        }

        if (!StringUtil.isEmpty(data)) {
            resultTv.setText(data);
        } else {
            resultTv.setText( response.getStatusCode() + ": " + response.getStatusMessage());
        }
        progressDialog.cancel();//Stopping progress
    }

    @Override
    public void onVideoRecordingFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onVideoRecordingFinished");
        resultTv.setText(response.getStatusMessage());
        Toast.makeText(MainActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();
        if(null != resultMap) {
            String filePath = resultMap.get("FILEPATH");
            if (null != response) {
                layoutVideoView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.VISIBLE);
                Uri uri = Uri.parse(filePath);
                videoView.setVideoURI(uri);
                // videoView.requestFocus();
                videoView.start();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        videoView.pause();
                        FrameLayout.LayoutParams buttonParams = (FrameLayout.LayoutParams) layoutButtonPlayPAuse.getLayoutParams();
                        buttonParams.width = videoView.getWidth();
                        layoutButtonPlayPAuse.setLayoutParams(buttonParams);
                        layoutButtonPlayPAuse.setX(videoView.getX());
                    }
                }, 500);
            }
        }
    }

    @Override
    public void onScanBarcodeFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onScanBarcodeFinished");
        if (null != response) {
            Toast.makeText(MainActivity.this, response.getStatusMessage(), Toast.LENGTH_SHORT).show();

            String imageBase64 = null;
            String data = "";
            if(null != resultMap){
                if (resultMap.containsKey("defaultBarcodeImage")) {
                    imageBase64 = resultMap.get("defaultBarcodeImage");
                    if (!StringUtil.isEmpty(imageBase64)) {
                        capturedImageData.put(ResultData.SCAN_BARCODE_DATA, new ResultData(ResultData.SCAN_BARCODE_DATA, imageBase64));
                    }
                    for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if(!key.equals("defaultBarcodeImage")) {
                            data += key + " : " + value + "\n";
                        }

                    }
                }
            }
            if(!StringUtil.isEmpty(data)) {
                resultTv.setText(data);
            } else{
                resultTv.setText( response.getStatusCode() + ": " + response.getStatusMessage());
            }
            resultImagePagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCaptureSignatureFinished(Map<String, String> resultMap, Response responses) {
        Log.d("SDK", "CALLBACK:::: onCaptureSignatureFinished");
        if (null != responses) {
            Toast.makeText(MainActivity.this, " " + responses.getStatusMessage(), Toast.LENGTH_LONG).show();

            String imageBase64 = null;
            if (null != resultMap) {
                if (resultMap.containsKey("SignatureImage")) {
                    imageBase64 = resultMap.get("SignatureImage");
                    if (!StringUtil.isEmpty(imageBase64)) {
                        capturedImageData.put(ResultData.CAPTURE_SIGNATURE_IMG, new ResultData(ResultData.CAPTURE_SIGNATURE_IMG, imageBase64));

                    }

                }
            }
            resultImagePagerAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onVerifyAddressFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onVerifyAddressFinished");
        progressDialog.cancel();//Stopping progress
        if(null!=response){
            Toast.makeText(MainActivity.this, " " + response.getStatusMessage(), Toast.LENGTH_LONG).show();

            String data = "";
            if (null != resultMap && !resultMap.isEmpty()) {
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    data += key + " : " + value + "\n";

                    System.out.println("Response Map : " + data);
                }
            }

            if (!StringUtil.isEmpty(data)) {
                resultTv.setText(data);
            } else {
                resultTv.setText( response.getStatusCode() + ": " + response.getStatusMessage());

            }
        }

    }

    @Override
    public void onCreateEmployeeFinished(Map<String, String> resultMap, Response responses) {
        Log.d("SDK","CALLBACK:::: onCreateEmployeeFinished");
        progressDialog.cancel();//Stopping progress
        if(null!=responses){
            Toast.makeText(MainActivity.this, " " + responses.getStatusMessage(), Toast.LENGTH_LONG).show();

            String data = "";
            if (null != resultMap && !resultMap.isEmpty()) {
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    data += key + " : " + value + "\n";

                    System.out.println("Response Map : " + data);
                }
            }

            if (!StringUtil.isEmpty(data)) {
                resultTv.setText(data);
            } else {
                resultTv.setText( responses.getStatusCode() + ": " + responses.getStatusMessage());

            }
        }

    }

    @Override
    public void onVerifyEmployeeFinished(Map<String, String> resultMap, Response responses) {
        Log.d("SDK","CALLBACK:::: onVerifyEmployeeFinished");
        progressDialog.cancel();//Stopping progress
        if(null!=responses){
            Toast.makeText(MainActivity.this, " " + responses.getStatusMessage(), Toast.LENGTH_LONG).show();

            String data = "";
            if (null != resultMap && !resultMap.isEmpty()) {
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    data += key + " : " + value + "\n";

                    System.out.println("Response Map : " + data);
                }
            }

            if (!StringUtil.isEmpty(data)) {
                resultTv.setText(data);
            } else {
                resultTv.setText( responses.getStatusCode() + ": " + responses.getStatusMessage());

            }
        }

    }

    @Override
    public void onGenerateTokenFinished(Map<String, String> resultMap, Response responses) {
        Log.d("SDK","CALLBACK:::: onGenerateTokenFinished");
        progressDialog.cancel();//Stopping progress
        if(null!=responses){
            Toast.makeText(MainActivity.this, " " + responses.getStatusMessage(), Toast.LENGTH_LONG).show();

            String data = "";
            if (null != resultMap && !resultMap.isEmpty()) {
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    data += key + " : " + value + "\n";

                    if (key.equalsIgnoreCase("Auth_Token")){

                        saveSetting(AUTHORIZE_TOKEN,value);

                    }
                }
            }

            if (!StringUtil.isEmpty(data)) {
                resultTv.setText(data);
            } else {
                resultTv.setText( responses.getStatusCode() + ": " + responses.getStatusMessage());

            }
        }

    }

    @Override
    public void onVerifyTokenFinished(Map<String, String> resultMap, Response responses) {
        Log.d("SDK","CALLBACK:::: onVerifyTokenFinished");
        progressDialog.cancel();//Stopping progress
        if(null!=responses){
            Toast.makeText(MainActivity.this, " " + responses.getStatusMessage(), Toast.LENGTH_LONG).show();

            String data = "";
            if (null != resultMap && !resultMap.isEmpty()) {
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    data += key + " : " + value + "\n";

                }
            }

            if (!StringUtil.isEmpty(data)) {
                resultTv.setText(data);
            } else {
                resultTv.setText( responses.getStatusCode() + ": " + responses.getStatusMessage());

            }
        }
    }

    public void onGenerateOTPFinished(Map<String,String> resultMap, Response responses) {
        Log.d("SDK","CALLBACK:::: onGenerateOTPFinished");
        progressDialog.cancel();//Stopping progress
        if(null!=responses){
            Toast.makeText(MainActivity.this, " " + responses.getStatusMessage(), Toast.LENGTH_LONG).show();

            String data = "";
            if (null != resultMap && !resultMap.isEmpty()) {
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    data += key + " : " + value + "\n";

//                    if (key.equalsIgnoreCase("Auth_Token")){
//
//                        saveSetting(AUTHORIZE_TOKEN,value);
//
//                    }
                }
            }

            if (!StringUtil.isEmpty(data)) {
                resultTv.setText(data);
            } else {
                resultTv.setText( responses.getStatusCode() + ": " + responses.getStatusMessage());

            }
        }
    }

    public void onVerifyOTPFinished(Map<String,String> resultMap, Response responses) {
        Log.d("SDK","CALLBACK:::: onVerifyOTPFinished");
        progressDialog.cancel();//Stopping progress
        if(null!=responses){
            Toast.makeText(MainActivity.this, " " + responses.getStatusMessage(), Toast.LENGTH_LONG).show();

            String data = "";
            if (null != resultMap && !resultMap.isEmpty()) {
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    data += key + " : " + value + "\n";

                }
            }

            if (!StringUtil.isEmpty(data)) {
                resultTv.setText(data);
            } else {
                resultTv.setText( responses.getStatusCode() + ": " + responses.getStatusMessage());

            }
        }
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
    public void onSnippetImageCaptureResultAvailable(Map<String, String> resultMap, Response response) {

        //Toast.makeText(getApplicationContext(),""+resultMap.size(), Toast.LENGTH_LONG).show();

        if(null!=response){
//            Toast.makeText(MainActivity.this, " " + response.getStatusMessage(), Toast.LENGTH_LONG).show();
            String data = "";
            if (null != resultMap && !resultMap.isEmpty()) {
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    data += key + " : " + value + "\n";

                    System.out.println("Response Map : " + data);
                }
            }

            if (!StringUtil.isEmpty(data)) {
                resultTv.setText(data);
            } else {
                resultTv.setText( response.getStatusCode() + ": " + response.getStatusMessage());

            }
        }


    }

    @Override
    public void onOperationResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK","CALLBACK:::: onOperationResultAvailable");
        String data = "";
        if (null != resultMap && !resultMap.isEmpty()) {
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                data += key + " : " + value + "\n";

                System.out.println("Response Map : " + data);
            }
        }

        if (!StringUtil.isEmpty(data)) {
            resultTv.setText(data);
        } else {
            resultTv.setText( response.getStatusCode() + ": " + response.getStatusMessage());
            if(response.getStatusCode() == 15){
                Toast.makeText(this, response.getStatusCode() + ": " + response.getStatusMessage(), Toast.LENGTH_LONG).show();
            }
        }
        progressDialog.cancel();//Stopping progress
    }

    public void saveSetting(String key, String value) {
        SharedPreferences sharedpreferences = getSharedPreferences(SDK_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String retrieveSetting(String key, String def) {
        SharedPreferences sharedpreferences = getSharedPreferences(SDK_SETTINGS, Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, def);
    }

    public void clearResultFields() {
        resultTv.setText("");
    }

    private class ResultImagePagerAdapter extends FragmentStatePagerAdapter {

        public ResultImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ResultData rd = (new ArrayList<ResultData>(capturedImageData.values())).get(position);

            Bundle bundle = new Bundle();
            bundle.putString(ResultImageFragment.RESULT_IMAGE_NAME, rd.getName());
//            bundle.putString(ResultImageFragment.RESULT_IMAGE, rd.getImage());

            ResultImageFragment resFrag = new ResultImageFragment();
            resFrag.setArguments(bundle);
            return resFrag;
        }

        @Override
        public int getCount() {
            return capturedImageData.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    /**
     * @return JSONObject of additional data fields
     */
    private JSONObject getAdditionalDataJSON(){
        String customername = customerName.getText().toString().trim();
        String uCustomerNumber = uniqueCustomerNumber.getText().toString().trim();
        String uMerchantNumber = uniqueMerchantNumber.getText().toString().trim();
        String serviceid = serviceID.getText().toString().trim();
        String custType = customerType.getText().toString().trim();
        String custAttribute = customerAttribute.getText().toString().trim();
        String custPhone = customerPhone.getText().toString().trim();
        String custEmail = customerEmail.getText().toString().trim();
        String uEmployeeCode = uniqueEmployeeCode.getText().toString().trim();
        String uEmployeeNumber = uniqueEmployeeNumber.getText().toString().trim();
        String oldClientCustNumber = oldClientCustomerNumber.getText().toString().trim();
        String addressLine1 = ((EditText) findViewById(R.id.addressLine1)).getText().toString().trim();
        String addressLine2 = ((EditText) findViewById(R.id.addressLine2)).getText().toString().trim();
        String country = ((EditText) findViewById(R.id.country)).getText().toString().trim();
        String state = ((EditText) findViewById(R.id.state)).getText().toString().trim();

        JSONObject jObject = new JSONObject();
        try{
            /*
             * Only add key without spaces
             * */
            if (!StringUtil.isEmpty(customername)){
                jObject.put("Customer_Name", customername);
                saveSetting(CUSTOMER_NAME, customername);
            }

            if (!StringUtil.isEmpty(uCustomerNumber)){
                jObject.put("Unique_Customer_Number", uCustomerNumber);
                saveSetting(UNIQUE_CUSTOMER_NUMBER, uCustomerNumber);
            }

            if (!StringUtil.isEmpty(uMerchantNumber)){
                jObject.put("Unique_Merchant_Number", uMerchantNumber);
                saveSetting(UNIQUE_MERCHANT_NUMBER, uMerchantNumber);
            }

            if (!StringUtil.isEmpty(serviceid)){
                jObject.put("Service_ID", serviceid);
                saveSetting(SERVICE_ID, serviceid);
            }

            if (!StringUtil.isEmpty(custType)){
                jObject.put("Customer_Type", custType);
                saveSetting(CUSTOMER_TYPE, custType);
            }

            if (!StringUtil.isEmpty(custAttribute)){
                jObject.put("Customer_Attribute", custAttribute);
                saveSetting(CUSTOMER_ATTRIBUTE, custAttribute);
            }

            if (!StringUtil.isEmpty(custPhone)){
                jObject.put("Customer_Phone", custPhone);
                saveSetting(CUSTOMER_PHONE, custPhone);
            }

            if (!StringUtil.isEmpty(custEmail)){
                jObject.put("Customer_Email", custEmail);
                saveSetting(CUSTOMER_EMAIL, custEmail);
            }

            if (!StringUtil.isEmpty(uEmployeeCode)){
                jObject.put("Unique_Employee_Code", uEmployeeCode);
                saveSetting(UNIQUE_EMPLOYEE_CODE, uEmployeeCode);
            }

            if (!StringUtil.isEmpty(uEmployeeNumber)){
                jObject.put("Unique_Employee_Number", uEmployeeNumber);
                saveSetting(UNIQUE_EMPLOYEE_NUMBER, uEmployeeNumber);
            }

            if (!StringUtil.isEmpty(oldClientCustNumber)){
                jObject.put("Old_Client_Customer_Number", oldClientCustNumber);
            }

            if(!StringUtil.isEmpty(addressLine1)) {
                jObject.put("AddressLine1",addressLine1);
            }

            if(!StringUtil.isEmpty(addressLine2)) {
                jObject.put("AddressLine2",addressLine2);
            }

            if(!StringUtil.isEmpty(country)) {
                jObject.put("Country",country);
            }

            if(!StringUtil.isEmpty(state)) {
                jObject.put("State",state);
            }

        }catch (JSONException exc){
            Log.d(LOGTAG, "getAdditionalDataJSON Exc : "+exc);
        }

        if(jObject.length() > 0)
            return jObject;

        return null;
    }

    private JSONObject getAddressDataJSON() {
        String address1 = address1EdtTxt.getText().toString().trim();
        String address2 = address2EdtTxt.getText().toString().trim();
        String country = countryEdtTxt.getText().toString().trim();
        String state = stateEdtTxt.getText().toString().trim();
        String zipCode = zipCodeEdtTxt.getText().toString().trim();

        JSONObject jObject = new JSONObject();
        try{
            /*
             * Only add key without spaces
             * */
            if (!StringUtil.isEmpty(address1)){
                jObject.put("AddrLine1", address1);

            }

            if (!StringUtil.isEmpty(address2)){
                jObject.put("AddrLine2", address2);

            }

            if (!StringUtil.isEmpty(country)){
                jObject.put("Country", country);
            }

            if (!StringUtil.isEmpty(state)){
                jObject.put("State", state);
            }

            if (!StringUtil.isEmpty(zipCode)){
                jObject.put("ZipCode", zipCode);
            }

        }catch (JSONException exc){
            Log.d(LOGTAG, "getAddressDataJSON Exc : "+exc);
        }

        if(jObject.length() > 0)
            return jObject;

        return null;
    }

    private JSONObject getEmployeeDataJSON() {
        String empCode = empCodeEdtTxt.getText().toString().trim();
        String empLoginId = empLoginIdEdtTxt.getText().toString().trim();
        String empEmailId = empEmailEdtTxt.getText().toString().trim();
        String empCompany = companyIdEdtTxt.getText().toString().trim();
        String empDept = departmentEdtTxt.getText().toString().trim();
//        String empType = (String) empTypeSpinner.getSelectedItem();
        String empName = empNameEdtTxt.getText().toString().trim();
        String empMobNumber = empMobNumberEdtTxt.getText().toString().trim();
        String empCountry = empCountryEdtTxt.getText().toString().trim();
        String gender = employeeGenderSpinner.getSelectedItem().toString();
        String address1 = empAddressLine1.getText().toString().trim();
        String address2 = empAddressLine2.getText().toString().trim();
        String zipcode = empZipCode.getText().toString().trim();
        String spousename = empSpouseName.getText().toString().trim();
        String noofchildren = empNoOfChildren.getText().toString().trim();


        JSONObject jObject = new JSONObject();
        try{
            /*
             * Only add key without spaces
             * */
            if (!StringUtil.isEmpty(empCode)) {
                jObject.put("Employee_Code", empCode);
            }

            if (!StringUtil.isEmpty(empLoginId)) {
                jObject.put("Login_ID", empLoginId);
            }

            if (!StringUtil.isEmpty(empEmailId)) {
                jObject.put("Employee_Email", empEmailId);
            }

            if (!StringUtil.isEmpty(empCompany)) {
                jObject.put("Company_ID", empCompany);
            }

            if (!StringUtil.isEmpty(empDept)) {
                jObject.put("Dept", empDept);
            }

            if (!StringUtil.isEmpty("ADMIN")) {
                jObject.put("Employee_Type", "ADMIN");
            }

            if (!StringUtil.isEmpty(empName)) {
                jObject.put("Employee_Name", empName);
            }

            if (!StringUtil.isEmpty(empMobNumber)) {
                jObject.put("Employee_MobileNumber", empMobNumber);
            }

            if (!StringUtil.isEmpty(empCountry)) {
                jObject.put("Employee_Country", empCountry);
            }

            if (!StringUtil.isEmpty(gender)) {
                jObject.put("Employee_Gender", gender);
            }

            if (!StringUtil.isEmpty(address1)) {
                jObject.put("Employee_AddressLine1", address1);
            }

            if (!StringUtil.isEmpty(address2)) {
                jObject.put("Employee_AddressLine2", address2);
            }

            if (!StringUtil.isEmpty(zipcode)) {
                jObject.put("Employee_ZipCode", zipcode);
            }

            if (!StringUtil.isEmpty(spousename)) {
                jObject.put("Spouse_Name", spousename);
            }

            if (!StringUtil.isEmpty(noofchildren)) {
                jObject.put("Number_of_Children", noofchildren);
            }

        }catch (JSONException exc){
            Log.d(LOGTAG, "getEmployeeDataJSON Exc : "+exc);
        }

        if(jObject.length() > 0)
            return jObject;

        return null;
    }

    public static String getApplicationVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    "com.idmission.imageprocessingdemo", 0);
            versionName = info.versionName;
        } catch (Exception e) {
            versionName = BuildConfig.VERSION_NAME;
        }
        return versionName;
    }

    @Override
    public void cancelPogressBar() {
        progressDialog.cancel();
    }

    private String filename(int count, String dataFolder, String prefix, String suffix, String extension) {
        File dir = new File(path() + "/" + dataFolder + "/");
        dir.mkdirs();

        suffix = suffix.isEmpty() ? suffix : "_" + suffix;
        prefix = prefix.isEmpty() ? prefix : prefix + "_";

        return dir.getAbsolutePath() + "/" + prefix + String.format("%03d", count) + suffix + extension;
    }

    private String path() {
        String BASE_PATH_INTERNAL = (getBaseContext() != null && getBaseContext().getFilesDir() != null) ? getBaseContext().getFilesDir().getParent() : "";
        String BASE_PATH = BASE_PATH_INTERNAL;
        String ID_VALIDATION_PATH = BASE_PATH + File.separator + "IDValidation";
        String TESSDATA_BASE_PATH = ID_VALIDATION_PATH + File.separator;
        String IDVALIDATION_IDMISSION_BASE_PATH = TESSDATA_BASE_PATH + "IDMission";
        return IDVALIDATION_IDMISSION_BASE_PATH;
    }
}
