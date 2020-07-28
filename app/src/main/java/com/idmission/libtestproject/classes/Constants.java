package com.idmission.libtestproject.classes;

import java.io.File;

/**
 * Created by dipenp on 10/10/18.
 */

public class Constants {

    //Supported Services
    public static final String SERVICE_ID_VALIDATION = "ID Validation";
    public static final String SERVICE_FACE_MATCH = "Face Match";
    public static final String SERVICE_ID_VALIDATION_FACE_MATCH = "ID Validation and Face Match";
    public static final String SERVICE_CUSTOMER_VERIFICATION = "Customer Verification";
    public static final String SERVICE_CUSTOMER_UPDATE = "Customer Update";
    public static final String SERVICE_OTP_VERIFICATION = "OTP Verification";
    public static final String SERVICE_OTP_GENERATION = "OTP Generation";
    public static final String SERVICE_ADDRESS_MATCHING = "Address Matching";
    public static final String SERVICE_EMPLOYEE_VERIFICATION = "Employee Verification";
    public static final String SERVICE_FACE_MATCH_EMPLOYEE_ENROLLMENT = "Face Match employee enrollment";
    public static final String SERVICE_GENERATE_TOKEN = "Generate Token";
    public static final String SERVICE_EMPLOYEE_OVERRIDE = "Create Employee(Override)";
    public static final String SERVICE_CUSTOMER_OVERRIDE = "Create Customer(Override)";
    public static final String SERVICE_ID_VALIDATION_CUSTOMER_ENROLLMENT = "ID Validation with customer enrollment";
    public static final String SERVICE_ID_VALIDATION_EMPLOYEE_ENROLLMENT = "ID Validation with employee enrollment";
    public static final String SERVICE_ID_FACE_EMPLOYEE_ENROLLMENT = "ID Validation & Face match with employee enrollment";
    public static final String SERVICE_ID_FACE_VIDEO_RECORDING= "ID Validation & Face match with video recording";
    public static final String SERVICE_ID_FACE_VIDEO_CUSTOMER_ENROLLMENT= "ID Validation & Face match with video recording & customer enrollment";
    public static final String SERVICE_ID_FACE_VIDEO_EMPLOYEE_ENROLLMENT= "ID Validation & Face match with video recording & employee enrollment";
    public static final String SERVICE_FP_CUSTOMER_ENROLLMENT= "Customer enrollment with FP Biometrics";
    public static final String SERVICE_FP_EMPLOYEE_ENROLLMENT= "Employee enrollment with FP Biometrics";
    public static final String SERVICE_EMPLOYEE_UPDATE= "Employee Update";
    public static final String SERVICE_ID_FACE_MATCH_CUSTOMIZE = "ID Validation and Face Match(Customize)";
    public static final String SERVICE_FACE_MATCH_SECONDARY_CUSTOMIZE = "Face Match Secondary ID(Customize)";
    public static final String SERVICE_EMPLOYEE_SEARCH = "Employee Search";
    public static final String SERVICE_CUSTOMER_SEARCH = "Customer Search";
    public static final String SERVICE_IDV_VIDEOCONFERENCE = "ID Validation & Video Conference";
    public static final String SERVICE_VIDEOCONFERENCE_FACE_MATCH = "Video Conference Face Match";
    public static final String SERVICE_IDV_VIDEOCONFERENCE_FACE_MATCH_CUSTOMER_ENROLLMENT = "ID Validation & Video Conference Face match with customer enrollment";
    public static final String SERVICE_IDV_VIDEOCONFERENCE_FACE_MATCH_EMPLOYEE_ENROLLMENT = "ID Validation & Video Conference Face match with employee enrollment";
    public static final String SERVICE_SEARCH_CUSTOMER_BIOMETRICS= "Search Customer with Biometrics";
    public static final String SERVICE_SEARCH_EMPLOYEE_BIOMETRICS = "Search Employee with Biometrics";
    public static final String SERVICE_AUTO_FILL = "Auto Fill";
    public static final String SERVICE_OFFLINE_FACE = "Offline Face";

    //SDK feature
    public static final String FEATURE_ID_CAPTURE_FRONT = "Capture ID Front";
    public static final String FEATURE_ID_CAPTURE_BACK = "Capture ID Back";
    public static final String FEATURE_FACE_DETECTION = "Selfie Capture";
    public static final String FEATURE_SIGNATURE_CAPTURE = "Signature Capture";
    public static final String FEATURE_DOCUMENT_CAPTURE = "Document Capture";
    public static final String FEATURE_VOICE_CAPTURE = "Voice Capture";
    public static final String FEATURE_FINGERPRINT_CAPTURE = "Fingerprint Capture";
    public static final String FEATURE_FINGERPRINT4F_CAPTURE = "Fingerprint Capture(4F)";
    public static final String FEATURE_GPS_CAPTURE = "Location/GPS Capture";
    public static final String FEATURE_VIDEO_CAPTURE = "Video Capture";
    public static final String FEATURE_SNIPPET_CAPTURE = "Image Snippet OCR";
    public static final String FEATURE_SCAN_BARCODE = "Barcode Scan";
    public static final String FEATURE_POA_CAPTURE = "POA Capture";
    public static final String FEATURE_OVERRIDE_EMP = "Selfie Employee";
    public static final String FEATURE_OVERRIDE_CUSTOMER = "Selfie Customer";
    public static final String FEATURE_CUSTOMIZED_PRODUCT_DEFINITION = "Define Product";
    public static final String FEATURE_CARD_CAPTURE= "Card Capture";
    public static final String FEATURE_SLANT_ID_CAPTURE= "Slant ID Capture";
    public static final String FEATURE_VIDEO_CONFERENCE= "Video Conference";
    public static final String FEATURE_SECONDARY_ID_DETAILS = "Secondary ID Details";
    public static final String FEATURE_SECONDARY_ID_CAPTURE_FRONT = "Secondary ID Front";
    public static final String FEATURE_SECONDARY_ID_CAPTURE_BACK = "Secondary ID Back";
    public static final String FEATURE_AUTO_FILL_FRONT = "Auto Fill Front";
    public static final String FEATURE_AUTO_FILL_BACK = "Auto Fill Back";

    //Additional data
    public static final String GENERATE_OTP_DATA = "Generate OTP Data";
    public static final String VERIFY_OTP_DATA = "Verify OTP Data";
    public static final String ADDITIONAL_DATA = "Data Capture";
    public static final String EMPLOYEE_DATA = "Employee Data";
    public static final String CRASH_REPORT_FILENAME = "imageprocessing_crash_report";
    public static final String CRASH_REPORTS_DIR_NAME = "CrashReports";

//    //Currently selected service
//    public static final String CURRENT_SERVICE_TYPE = "CURRENT_SERVICE_TYPE";

    //Directory path
    public static final String EVOLVE_BASE_FOLDER_PATH = File.separator + "Evolv"; //base folder for evolv app
    public static final String EVOLVE_IMAGE_FOLDER_PATH = EVOLVE_BASE_FOLDER_PATH + File.separator + "Images"; //base image-folder directory
    public static final String EVOLVE_REQ_RESP_FOLDER_PATH = EVOLVE_BASE_FOLDER_PATH + File.separator + "RequestResponse"; //base Req-Resp folder directory

    //Image file path
    public static final String FACE_IMAGE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "FACE_IMAGE.jpg";
    public static final String BACK_IMAGE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "BACK_IMAGE.jpg";
    public static final String FRONT_IMAGE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "FRONT_IMAGE.jpg";
    public static final String BACK_IMAGE_SECONDARY = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "BACK_IMAGE_SECONDARY.jpg";
    public static final String FRONT_IMAGE_SECONDARY = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "FRONT_IMAGE_SECONDARY.jpg";
    public static final String SLANT_IMAGE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "SLANT_IMAGE.jpg";
    public static final String DOCUMENT_CAPTURE_IMAGE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "DOCUMENT_CAPTURE_IMAGE.jpg";
    public static final String SIGNATURE_IMAGE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "SIGNATURE_IMAGE.jpg";
    public static final String SCAN_BARCODE_IMAGE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "SCAN_BARCODE_IMAGE.jpg";
    public static final String FINGERPRINT_IMAGE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "FINGERPRINT_IMAGE.jpg";
    public static final String PROCESSED_FACE_IMAGE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "PROCESSED_FACE_IMAGE.jpg";
    public static final String OVAL_FACE_IMAGE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "OVAL_FACE_IMAGE.jpg";
    public static final String POA_CAPTURE_IMAGE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "POA_CAPTURE_IMAGE.jpg";
    public static final String FACE_IMAGE_OVERRIDE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "FACE_IMAGE_OVERRIDE.jpg";
    public static final String PROCESSED_FACE_IMAGE_OVERRIDE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "PROCESSED_FACE_IMAGE_OVERRIDE.jpg";
    public static final String OVAL_FACE_IMAGE_OVERRIDE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "OVAL_FACE_IMAGE_OVERRIDE.jpg";
    public static final String CARD_CAPTURE_IMAGE = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "CARD_CAPTURE_IMAGE.jpg";
    public static final String VOICE_RECORDING_DATA = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "VOICE_RECORDING.mp3";
    public static final String VIDEO_RECORDING_DATA = EVOLVE_IMAGE_FOLDER_PATH + File.separator + "VIDEO_RECORDING.mp3";

    //Req-Response XML path
    public static final String REQ_XML_PATH = EVOLVE_REQ_RESP_FOLDER_PATH + File.separator + "Request.txt";
    public static final String RESP_XML_PATH = EVOLVE_REQ_RESP_FOLDER_PATH + File.separator + "Response.txt";
    public static final String CRASH_REPORTS_PATH = EVOLVE_BASE_FOLDER_PATH + File.separator + CRASH_REPORTS_DIR_NAME;
    public static final String CRASH_REPORT_FILEPATH = Constants.CRASH_REPORTS_PATH + File.separator + Constants.CRASH_REPORT_FILENAME;

    //Constant service call
    public static final String COUNTRY_ID = "country_id";
    public static final String STATE_ID = "state_id";
    public static final String ID_TYPE = "id_type";
    public static final String COUNTRY_ID_SECONDARY = "country_id_secondary";
    public static final String STATE_ID_SECONDARY = "state_id_secondary";
    public static final String ID_TYPE_SECONDARY = "id_type_secondary";

    public static final String FINAL_SUBMIT = "final_submit";
    public static final String CLEAR_FORM_KEY = "clear_form_key";

    public static final String EMAIL_ID = "emailId";
    public static final String MOBILE_NO = "mobileNo";
    public static final String NOTIFICATION_TYPE = "notificationType";
    public static final String RECEIVE_OTP = "receivedOTP";
    public static final String ADDRESS_JSON = "addressJson";
    public static final String DO_EXTRACT = "doExtract";
    public static final String DO_VERIFY = "doVerify";
    public static final String SERVICE_CALL_JSON = "serviceCallJson";
    public static final String ADDITIONAL_DATA_JSON="additionalDataJSON";

    public static final String TEMPLATE_FACE_IMAGE_TYPE="faceImageType";
    public static final String TEMPLATE_ID_IMAGE_TYPE="idImageType";

    public static final String EMPLOYEE_DATA_JSON="employeeDataJSON";
    public static final String EMP_CODE = "empCode";
    public static final String PRODUCT_DEFINITION_JSON="productDefinitionJSON";
}
