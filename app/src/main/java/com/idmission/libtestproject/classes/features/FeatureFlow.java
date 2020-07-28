package com.idmission.libtestproject.classes.features;

import android.app.Activity;

import com.idmission.libtestproject.R;
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.classes.Tab;
import com.idmission.libtestproject.fragments.AutoFillBack;
import com.idmission.libtestproject.fragments.AutoFillFront;
import com.idmission.libtestproject.fragments.CaptureBack;
import com.idmission.libtestproject.fragments.CaptureBackSecondary;
import com.idmission.libtestproject.fragments.CaptureFront;
import com.idmission.libtestproject.fragments.CaptureFrontSecondary;
import com.idmission.libtestproject.fragments.CardCapture;
import com.idmission.libtestproject.fragments.CustomizeProduct;
import com.idmission.libtestproject.fragments.DataCapture;
import com.idmission.libtestproject.fragments.DocumentCapture;
import com.idmission.libtestproject.fragments.EmployeeData;
import com.idmission.libtestproject.fragments.FingerprintCapture;
import com.idmission.libtestproject.fragments.FingerprintCapture4F;
import com.idmission.libtestproject.fragments.GenerateOTPTab;
import com.idmission.libtestproject.fragments.LocationCapture;
import com.idmission.libtestproject.fragments.POACapture;
import com.idmission.libtestproject.fragments.ScanBarcodeCapture;
import com.idmission.libtestproject.fragments.SecondaryIDDetails;
import com.idmission.libtestproject.fragments.SelfieCapture;
import com.idmission.libtestproject.fragments.SelfieOverrideCapture;
import com.idmission.libtestproject.fragments.SignatureCapture;
import com.idmission.libtestproject.fragments.SlantCapture;
import com.idmission.libtestproject.fragments.SnippetCapture;
import com.idmission.libtestproject.fragments.VerifyOTPTab;
import com.idmission.libtestproject.fragments.VideoCapture;
import com.idmission.libtestproject.fragments.VideoConferenceFragment;
import com.idmission.libtestproject.fragments.VoiceCapture;
import com.idmission.libtestproject.utils.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * Created by dipenp on 10/10/18.
 */

public class FeatureFlow {

    public static final String[] functionality = {Constants.SERVICE_ID_VALIDATION, Constants.SERVICE_FACE_MATCH, Constants.SERVICE_ID_VALIDATION_FACE_MATCH, Constants.SERVICE_CUSTOMER_UPDATE, Constants.SERVICE_CUSTOMER_VERIFICATION};

    public static LinkedHashMap<String, ArrayList<String>> requiredFeatureForService = new LinkedHashMap<>();
    public static LinkedHashMap<String, ArrayList<String>> additionalFeatureForService = new LinkedHashMap<>();

    public static LinkedHashMap<String, ArrayList<String>> getRequiredFeatureForService() {

        if (requiredFeatureForService.isEmpty()) {
            String[] id_validation_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_ID_CAPTURE_BACK};
            requiredFeatureForService.put(Constants.SERVICE_ID_VALIDATION, new ArrayList(Arrays.asList(id_validation_service)));

            String[] id_validation_with_customer_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_ID_CAPTURE_BACK};
            requiredFeatureForService.put(Constants.SERVICE_ID_VALIDATION_CUSTOMER_ENROLLMENT, new ArrayList(Arrays.asList(id_validation_with_customer_service)));

            String[] id_validation_with_employee_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_ID_CAPTURE_BACK};
            requiredFeatureForService.put(Constants.SERVICE_ID_VALIDATION_EMPLOYEE_ENROLLMENT, new ArrayList(Arrays.asList(id_validation_with_employee_service)));

            String[] face_match_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_FACE_DETECTION};
            requiredFeatureForService.put(Constants.SERVICE_FACE_MATCH, new ArrayList(Arrays.asList(face_match_service)));

            String[] id_face_match_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_ID_CAPTURE_BACK, Constants.FEATURE_FACE_DETECTION};
            requiredFeatureForService.put(Constants.SERVICE_ID_VALIDATION_FACE_MATCH, new ArrayList(Arrays.asList(id_face_match_service)));

            String[] cust_verify_service = {Constants.FEATURE_FACE_DETECTION};
            requiredFeatureForService.put(Constants.SERVICE_CUSTOMER_VERIFICATION, new ArrayList(Arrays.asList(cust_verify_service)));

            String[] proof_of_address_service = {Constants.FEATURE_POA_CAPTURE};
            requiredFeatureForService.put(Constants.SERVICE_ADDRESS_MATCHING, new ArrayList(Arrays.asList(proof_of_address_service)));

            String[] employee_verification_service = {Constants.FEATURE_FACE_DETECTION};
            requiredFeatureForService.put(Constants.SERVICE_EMPLOYEE_VERIFICATION, new ArrayList(Arrays.asList(employee_verification_service)));

            String[] face_match_employee_enrollment_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_FACE_DETECTION};
            requiredFeatureForService.put(Constants.SERVICE_FACE_MATCH_EMPLOYEE_ENROLLMENT, new ArrayList(Arrays.asList(face_match_employee_enrollment_service)));

            String[] employee_override_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_FACE_DETECTION, Constants.FEATURE_OVERRIDE_EMP};
            requiredFeatureForService.put(Constants.SERVICE_EMPLOYEE_OVERRIDE, new ArrayList(Arrays.asList(employee_override_service)));

            String[] customer_override_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_FACE_DETECTION, Constants.FEATURE_OVERRIDE_CUSTOMER};
            requiredFeatureForService.put(Constants.SERVICE_CUSTOMER_OVERRIDE, new ArrayList(Arrays.asList(customer_override_service)));

            String[] id_face_match_with_emp_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_ID_CAPTURE_BACK, Constants.FEATURE_FACE_DETECTION};
            requiredFeatureForService.put(Constants.SERVICE_ID_FACE_EMPLOYEE_ENROLLMENT, new ArrayList(Arrays.asList(id_face_match_with_emp_service)));

            String[] id_face_match_with_video_recording_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_ID_CAPTURE_BACK, Constants.FEATURE_VIDEO_CAPTURE};
            requiredFeatureForService.put(Constants.SERVICE_ID_FACE_VIDEO_RECORDING, new ArrayList(Arrays.asList(id_face_match_with_video_recording_service)));

            String[] id_face_match_with_video_recording_cust_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_ID_CAPTURE_BACK, Constants.FEATURE_VIDEO_CAPTURE};
            requiredFeatureForService.put(Constants.SERVICE_ID_FACE_VIDEO_CUSTOMER_ENROLLMENT, new ArrayList(Arrays.asList(id_face_match_with_video_recording_cust_service)));

            String[] id_face_match_with_video_recording_emp_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_ID_CAPTURE_BACK, Constants.FEATURE_VIDEO_CAPTURE};
            requiredFeatureForService.put(Constants.SERVICE_ID_FACE_VIDEO_EMPLOYEE_ENROLLMENT, new ArrayList(Arrays.asList(id_face_match_with_video_recording_emp_service)));

            String[] customer_enrollment_with_FP_service = {Constants.FEATURE_FINGERPRINT_CAPTURE, Constants.FEATURE_FINGERPRINT4F_CAPTURE, Constants.FEATURE_FACE_DETECTION, Constants.FEATURE_VOICE_CAPTURE};
            requiredFeatureForService.put(Constants.SERVICE_FP_CUSTOMER_ENROLLMENT, new ArrayList(Arrays.asList(customer_enrollment_with_FP_service)));

            String[] employee_enrollment_with_FP_service = {Constants.FEATURE_FINGERPRINT_CAPTURE, Constants.FEATURE_FINGERPRINT4F_CAPTURE, Constants.FEATURE_FACE_DETECTION, Constants.FEATURE_VOICE_CAPTURE};
            requiredFeatureForService.put(Constants.SERVICE_FP_EMPLOYEE_ENROLLMENT, new ArrayList(Arrays.asList(employee_enrollment_with_FP_service)));

            String[] id_face_match_custom = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_ID_CAPTURE_BACK, Constants.FEATURE_FACE_DETECTION, Constants.FEATURE_OVERRIDE_CUSTOMER};
            requiredFeatureForService.put(Constants.SERVICE_ID_FACE_MATCH_CUSTOMIZE, new ArrayList(Arrays.asList(id_face_match_custom)));

            String[] face_match_secondary_id_custom = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_ID_CAPTURE_BACK, Constants.FEATURE_FACE_DETECTION, Constants.FEATURE_OVERRIDE_CUSTOMER};
            requiredFeatureForService.put(Constants.SERVICE_FACE_MATCH_SECONDARY_CUSTOMIZE, new ArrayList(Arrays.asList(face_match_secondary_id_custom)));

            // String[] customer_update_service = {Constants.FEATURE_FINGERPRINT_CAPTURE, Constants.FEATURE_FINGERPRINT4F_CAPTURE, Constants.FEATURE_SIGNATURE_CAPTURE};
            //requiredFeatureForService.put(Constants.SERVICE_CUSTOMER_UPDATE, new ArrayList(Arrays.asList(customer_update_service)));

            String[] employee_update_service = {Constants.FEATURE_FACE_DETECTION};
            requiredFeatureForService.put(Constants.SERVICE_EMPLOYEE_UPDATE, new ArrayList(Arrays.asList(employee_update_service)));

            String[] customer_update_service = {Constants.FEATURE_FACE_DETECTION};
            requiredFeatureForService.put(Constants.SERVICE_CUSTOMER_UPDATE, new ArrayList(Arrays.asList(customer_update_service)));

            String[] id_with_video_conferencing_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_ID_CAPTURE_BACK, Constants.FEATURE_VIDEO_CONFERENCE};
            requiredFeatureForService.put(Constants.SERVICE_IDV_VIDEOCONFERENCE, new ArrayList(Arrays.asList(id_with_video_conferencing_service)));

            String[] video_conferencing_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_VIDEO_CONFERENCE};
            requiredFeatureForService.put(Constants.SERVICE_VIDEOCONFERENCE_FACE_MATCH, new ArrayList(Arrays.asList(video_conferencing_service)));

            String[] id_face_with_video_conferencing_with_customer_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_ID_CAPTURE_BACK, Constants.FEATURE_VIDEO_CONFERENCE};
            requiredFeatureForService.put(Constants.SERVICE_IDV_VIDEOCONFERENCE_FACE_MATCH_CUSTOMER_ENROLLMENT, new ArrayList(Arrays.asList(id_face_with_video_conferencing_with_customer_service)));

            String[] id_face_with_video_conferencing_with_employee_service = {Constants.FEATURE_ID_CAPTURE_FRONT, Constants.FEATURE_ID_CAPTURE_BACK, Constants.FEATURE_VIDEO_CONFERENCE};
            requiredFeatureForService.put(Constants.SERVICE_IDV_VIDEOCONFERENCE_FACE_MATCH_EMPLOYEE_ENROLLMENT, new ArrayList(Arrays.asList(id_face_with_video_conferencing_with_employee_service)));

            String[] search_customer_biometrics = {Constants.FEATURE_FACE_DETECTION, Constants.FEATURE_VOICE_CAPTURE, Constants.FEATURE_FINGERPRINT_CAPTURE, Constants.FEATURE_FINGERPRINT4F_CAPTURE};
            requiredFeatureForService.put(Constants.SERVICE_SEARCH_CUSTOMER_BIOMETRICS, new ArrayList(Arrays.asList(search_customer_biometrics)));

            String[] search_employee_biometrics = {Constants.FEATURE_FACE_DETECTION, Constants.FEATURE_VOICE_CAPTURE, Constants.FEATURE_FINGERPRINT_CAPTURE, Constants.FEATURE_FINGERPRINT4F_CAPTURE};
            requiredFeatureForService.put(Constants.SERVICE_SEARCH_EMPLOYEE_BIOMETRICS, new ArrayList(Arrays.asList(search_employee_biometrics)));

            String[] auto_fill = {Constants.FEATURE_AUTO_FILL_FRONT, Constants.FEATURE_AUTO_FILL_BACK};
            requiredFeatureForService.put(Constants.SERVICE_AUTO_FILL, new ArrayList(Arrays.asList(auto_fill)));

            String[] offline_face = {Constants.FEATURE_FACE_DETECTION};
            requiredFeatureForService.put(Constants.SERVICE_OFFLINE_FACE, new ArrayList(Arrays.asList(offline_face)));
        }

        return requiredFeatureForService;
    }

    public static LinkedHashMap<String, ArrayList<String>> getAdditionalFeatureForService() {

        if (additionalFeatureForService.isEmpty()) {
            String[] id_validation_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_ID_VALIDATION, new ArrayList(Arrays.asList(id_validation_service)));

            String[] id_validation_with_customer_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_ID_VALIDATION_CUSTOMER_ENROLLMENT, new ArrayList(Arrays.asList(id_validation_with_customer_service)));

            String[] id_validation_with_employee_service = {Constants.EMPLOYEE_DATA, Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_ID_VALIDATION_EMPLOYEE_ENROLLMENT, new ArrayList(Arrays.asList(id_validation_with_employee_service)));

            String[] face_match_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_FACE_MATCH, new ArrayList(Arrays.asList(face_match_service)));

            String[] id_face_match_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_ID_VALIDATION_FACE_MATCH, new ArrayList(Arrays.asList(id_face_match_service)));

            String[] customer_update_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_CUSTOMER_UPDATE, new ArrayList(Arrays.asList(customer_update_service)));

            String[] cust_verify_service = {Constants.FEATURE_VOICE_CAPTURE, Constants.FEATURE_FINGERPRINT_CAPTURE, Constants.FEATURE_FINGERPRINT4F_CAPTURE, Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_CUSTOMER_VERIFICATION, new ArrayList(Arrays.asList(cust_verify_service)));

            String[] generate_otp_service = {Constants.GENERATE_OTP_DATA, Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_OTP_GENERATION, new ArrayList(Arrays.asList(generate_otp_service)));

            String[] verify_otp_service = {Constants.VERIFY_OTP_DATA, Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_OTP_VERIFICATION, new ArrayList(Arrays.asList(verify_otp_service)));

            String[] proof_of_address_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_ADDRESS_MATCHING, new ArrayList(Arrays.asList(proof_of_address_service)));

            String[] employee_verification_service = {Constants.FEATURE_FINGERPRINT_CAPTURE, Constants.FEATURE_VOICE_CAPTURE, Constants.FEATURE_FINGERPRINT4F_CAPTURE, Constants.EMPLOYEE_DATA, Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_EMPLOYEE_VERIFICATION, new ArrayList(Arrays.asList(employee_verification_service)));

            String[] face_match_employee_enrollment_service = {Constants.EMPLOYEE_DATA, Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_FACE_MATCH_EMPLOYEE_ENROLLMENT, new ArrayList(Arrays.asList(face_match_employee_enrollment_service)));

            String[] generate_token_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_GENERATE_TOKEN, new ArrayList(Arrays.asList(generate_token_service)));

            String[] employee_override_service = {Constants.EMPLOYEE_DATA, Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_EMPLOYEE_OVERRIDE, new ArrayList(Arrays.asList(employee_override_service)));

            String[] customer_override_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_CUSTOMER_OVERRIDE, new ArrayList(Arrays.asList(customer_override_service)));

            String[] id_face_match_with_emp_service = {Constants.EMPLOYEE_DATA, Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_ID_FACE_EMPLOYEE_ENROLLMENT, new ArrayList(Arrays.asList(id_face_match_with_emp_service)));

            String[] video_recording_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_ID_FACE_VIDEO_RECORDING, new ArrayList(Arrays.asList(video_recording_service)));

            String[] id_face_match_with_video_recording_cust_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_ID_FACE_VIDEO_CUSTOMER_ENROLLMENT, new ArrayList(Arrays.asList(id_face_match_with_video_recording_cust_service)));

            String[] id_face_match_with_video_recording_emp_service = {Constants.EMPLOYEE_DATA, Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_ID_FACE_VIDEO_EMPLOYEE_ENROLLMENT, new ArrayList(Arrays.asList(id_face_match_with_video_recording_emp_service)));

            String[] customer_enrollment_with_FP_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_FP_CUSTOMER_ENROLLMENT, new ArrayList(Arrays.asList(customer_enrollment_with_FP_service)));

            String[] employee_enrollment_with_FP_service = {Constants.EMPLOYEE_DATA, Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_FP_EMPLOYEE_ENROLLMENT, new ArrayList(Arrays.asList(employee_enrollment_with_FP_service)));

//            String[] employee_update_service = {Constants.EMPLOYEE_DATA, Constants.ADDITIONAL_DATA};
//            additionalFeatureForService.put(Constants.SERVICE_EMPLOYEE_UPDATE, new ArrayList(Arrays.asList(employee_update_service)));

            String[] employee_update_service = {Constants.EMPLOYEE_DATA};
            additionalFeatureForService.put(Constants.SERVICE_EMPLOYEE_UPDATE, new ArrayList(Arrays.asList(employee_update_service)));

            String[] id_face_match_custom = {Constants.ADDITIONAL_DATA, Constants.EMPLOYEE_DATA, Constants.FEATURE_CUSTOMIZED_PRODUCT_DEFINITION};
            additionalFeatureForService.put(Constants.SERVICE_ID_FACE_MATCH_CUSTOMIZE, new ArrayList(Arrays.asList(id_face_match_custom)));

            String[] face_match_secondary_custom = {Constants.ADDITIONAL_DATA, Constants.EMPLOYEE_DATA, Constants.FEATURE_CUSTOMIZED_PRODUCT_DEFINITION};
            additionalFeatureForService.put(Constants.SERVICE_FACE_MATCH_SECONDARY_CUSTOMIZE, new ArrayList(Arrays.asList(face_match_secondary_custom)));

            String[] customer_search = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_CUSTOMER_SEARCH, new ArrayList(Arrays.asList(customer_search)));

            String[] employee_search = {Constants.EMPLOYEE_DATA};
            additionalFeatureForService.put(Constants.SERVICE_EMPLOYEE_SEARCH, new ArrayList(Arrays.asList(employee_search)));

            String[] id_with_video_conferencing_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_IDV_VIDEOCONFERENCE, new ArrayList(Arrays.asList(id_with_video_conferencing_service)));

            String[] video_conferencing_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_VIDEOCONFERENCE_FACE_MATCH, new ArrayList(Arrays.asList(video_conferencing_service)));

            String[] id_face_with_video_conferencing_with_customer_service = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_IDV_VIDEOCONFERENCE_FACE_MATCH_CUSTOMER_ENROLLMENT, new ArrayList(Arrays.asList(id_face_with_video_conferencing_with_customer_service)));

            String[] id_face_with_video_conferencing_with_employee_service = {Constants.EMPLOYEE_DATA, Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_IDV_VIDEOCONFERENCE_FACE_MATCH_EMPLOYEE_ENROLLMENT, new ArrayList(Arrays.asList(id_face_with_video_conferencing_with_employee_service)));

            String[] auto_fill = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_AUTO_FILL, new ArrayList(Arrays.asList(auto_fill)));

            String[] offline_face = {Constants.ADDITIONAL_DATA};
            additionalFeatureForService.put(Constants.SERVICE_OFFLINE_FACE, new ArrayList(Arrays.asList(offline_face)));

        }

        return additionalFeatureForService;
    }

    public static Tab getFeatureTab(Activity activity, String feature) {

        if (!StringUtil.isEmpty(feature)) {
            if (feature.equals(Constants.FEATURE_ID_CAPTURE_FRONT)) {
                return new Tab(activity.getString(R.string.capture_id_front_tab), new CaptureFront());
            } else if (feature.equals(Constants.FEATURE_ID_CAPTURE_BACK)) {
                return new Tab(activity.getString(R.string.capture_id_back_tab), new CaptureBack());
            } else if (feature.equals(Constants.FEATURE_FACE_DETECTION)) {
                return new Tab(activity.getString(R.string.selfie_capture_tab), new SelfieCapture());
            } else if (feature.equals(Constants.FEATURE_SIGNATURE_CAPTURE)) {
                return new Tab(activity.getString(R.string.signature_capture_tab), new SignatureCapture());
            } else if (feature.equals(Constants.FEATURE_DOCUMENT_CAPTURE)) {
                return new Tab(activity.getString(R.string.document_capture_tab), new DocumentCapture());
            } else if (feature.equals(Constants.FEATURE_VOICE_CAPTURE)) {
                return new Tab(activity.getString(R.string.voice_capture_tab), new VoiceCapture());
            } else if (feature.equals(Constants.FEATURE_FINGERPRINT_CAPTURE)) {
                return new Tab(activity.getString(R.string.fingerprint_capture_tab), new FingerprintCapture());
            } else if (feature.equals(Constants.FEATURE_FINGERPRINT4F_CAPTURE)) {
                return new Tab(activity.getString(R.string.fingerprint_capture_4f_tab), new FingerprintCapture4F());
            } else if (feature.equals(Constants.FEATURE_GPS_CAPTURE)) {
                return new Tab(activity.getString(R.string.location_capture_tab), new LocationCapture());
            } else if (feature.equals(Constants.FEATURE_VIDEO_CAPTURE)) {
                return new Tab(activity.getString(R.string.video_capture_tab), new VideoCapture());
            } else if (feature.equals(Constants.FEATURE_SNIPPET_CAPTURE)) {
                return new Tab(activity.getString(R.string.image_snippet_OCR_tab), new SnippetCapture());
            } else if (feature.equals(Constants.FEATURE_SCAN_BARCODE)) {
                return new Tab(activity.getString(R.string.barcode_scan), new ScanBarcodeCapture());
            } else if (feature.equals(Constants.ADDITIONAL_DATA)) {
                return new Tab(activity.getString(R.string.data_captur_tab), new DataCapture());
            } else if (feature.equals(Constants.GENERATE_OTP_DATA)) {
                return new Tab(activity.getString(R.string.generate_OTP_data_tab), new GenerateOTPTab());
            } else if (feature.equals(Constants.VERIFY_OTP_DATA)) {
                return new Tab(activity.getString(R.string.verify_OTP_data_tab), new VerifyOTPTab());
            } else if (feature.equals(Constants.FEATURE_POA_CAPTURE)) {
                return new Tab(activity.getString(R.string.poa_capture_tab), new POACapture());
            } else if (feature.equals(Constants.EMPLOYEE_DATA)) {
                return new Tab(activity.getString(R.string.employee_data_tab), new EmployeeData());
            } else if (feature.equals(Constants.FEATURE_OVERRIDE_EMP)) {
                return new Tab(activity.getString(R.string.selfie_employee_tab), new SelfieOverrideCapture());
            } else if (feature.equals(Constants.FEATURE_OVERRIDE_CUSTOMER)) {
                return new Tab(activity.getString(R.string.selfie_employee_tab), new SelfieOverrideCapture());
            } else if (feature.equals(Constants.FEATURE_CUSTOMIZED_PRODUCT_DEFINITION)) {
                return new Tab(activity.getString(R.string.define_product_tab), new CustomizeProduct());
            } else if (feature.equals(Constants.FEATURE_CARD_CAPTURE)) {
                return new Tab(activity.getString(R.string.card_capture_tab), new CardCapture());
            } else if (feature.equals(Constants.FEATURE_SLANT_ID_CAPTURE)) {
                return new Tab(activity.getString(R.string.slant_capture_tab), new SlantCapture());
            } else if (feature.equals(Constants.FEATURE_VIDEO_CONFERENCE)) {
                return new Tab(activity.getString(R.string.video_conference_tab), new VideoConferenceFragment());
            }else if (feature.equals(Constants.FEATURE_SECONDARY_ID_DETAILS)) {
                return new Tab(activity.getString(R.string.sec_id_details_tab), new SecondaryIDDetails());
            } else if (feature.equals(Constants.FEATURE_SECONDARY_ID_CAPTURE_FRONT)) {
                return new Tab(activity.getString(R.string.capture_sec_id_front_tab), new CaptureFrontSecondary());
            } else if (feature.equals(Constants.FEATURE_SECONDARY_ID_CAPTURE_BACK)) {
                return new Tab(activity.getString(R.string.capture_sec_id_back_tab), new CaptureBackSecondary());
            } else if (feature.equals(Constants.FEATURE_AUTO_FILL_FRONT)) {
                return new Tab(activity.getString(R.string.auto_fill_tab), new AutoFillFront());
            }else if (feature.equals(Constants.FEATURE_AUTO_FILL_BACK)) {
                return new Tab(activity.getString(R.string.auto_fill_back_tab), new AutoFillBack());
            }

        }
        return null;
    }

    public static String getServiceTypeForServiceID(int serviceId) {
        if (serviceId == 10 || serviceId == 50 || serviceId == 77 || serviceId == 78 || serviceId == 80) {
            return Constants.SERVICE_ID_VALIDATION_FACE_MATCH;
            //} else if (serviceId == 65 || serviceId == 60 || serviceId == 310 || serviceId == 320 || serviceId == 330 || serviceId == 300) {
        } else if (serviceId == 65 || serviceId == 60) {
            return Constants.SERVICE_FACE_MATCH;
        } else if (serviceId == 100 || serviceId == 105) {
            return Constants.SERVICE_CUSTOMER_VERIFICATION;
        } else if (serviceId == 70) {
            return Constants.SERVICE_CUSTOMER_UPDATE;
        } else if (serviceId == 400) {
            return Constants.SERVICE_OTP_GENERATION;
        } else if (serviceId == 410) {
            return Constants.SERVICE_OTP_VERIFICATION;
        } else if (serviceId == 200) {
            return Constants.SERVICE_ADDRESS_MATCHING;
        } else if (serviceId == 300 || serviceId == 305) {
            return Constants.SERVICE_EMPLOYEE_VERIFICATION;
        } else if (serviceId == 310) {
            return Constants.SERVICE_FACE_MATCH_EMPLOYEE_ENROLLMENT;
        } else if (serviceId == 401) {
            return Constants.SERVICE_GENERATE_TOKEN;
        } else if (serviceId == 330) {
            return Constants.SERVICE_EMPLOYEE_OVERRIDE;
        } else if (serviceId == 320) {
            return Constants.SERVICE_CUSTOMER_OVERRIDE;
        } else if (serviceId == 20) {
            return Constants.SERVICE_ID_VALIDATION;
        } else if (serviceId == 25) {
            return Constants.SERVICE_ID_VALIDATION_CUSTOMER_ENROLLMENT;
        } else if (serviceId == 30) {
            return Constants.SERVICE_ID_VALIDATION_EMPLOYEE_ENROLLMENT;
        } else if (serviceId == 55) {
            return Constants.SERVICE_ID_FACE_EMPLOYEE_ENROLLMENT;
        } else if (serviceId == 155) {
            return Constants.SERVICE_ID_FACE_VIDEO_RECORDING;
        } else if (serviceId == 160) {
            return Constants.SERVICE_ID_FACE_VIDEO_CUSTOMER_ENROLLMENT;
        } else if (serviceId == 165) {
            return Constants.SERVICE_ID_FACE_VIDEO_EMPLOYEE_ENROLLMENT;
        } else if (serviceId == 175) {
            return Constants.SERVICE_FP_CUSTOMER_ENROLLMENT;
        } else if (serviceId == 180) {
            return Constants.SERVICE_FP_EMPLOYEE_ENROLLMENT;
        } else if (serviceId == 75) {
            return Constants.SERVICE_EMPLOYEE_UPDATE;
        } else if (serviceId == 360) {
            return Constants.SERVICE_ID_FACE_MATCH_CUSTOMIZE;
        } else if (serviceId == 361) {
            return Constants.SERVICE_FACE_MATCH_SECONDARY_CUSTOMIZE;
        } else if (serviceId == 186) {
            return Constants.SERVICE_CUSTOMER_SEARCH;
        } else if (serviceId == 191) {
            return Constants.SERVICE_EMPLOYEE_SEARCH;
        } else if (serviceId == 505) {
            return Constants.SERVICE_IDV_VIDEOCONFERENCE;
        } else if (serviceId == 500) {
            return Constants.SERVICE_VIDEOCONFERENCE_FACE_MATCH;
        } else if (serviceId == 510) {
            return Constants.SERVICE_IDV_VIDEOCONFERENCE_FACE_MATCH_CUSTOMER_ENROLLMENT;
        } else if (serviceId == 515) {
            return Constants.SERVICE_IDV_VIDEOCONFERENCE_FACE_MATCH_EMPLOYEE_ENROLLMENT;
        } else if (serviceId == 185) {
            return Constants.SERVICE_SEARCH_CUSTOMER_BIOMETRICS;
        } else if (serviceId == 190) {
            return Constants.SERVICE_SEARCH_EMPLOYEE_BIOMETRICS;
        } else if (serviceId == 620) {
            return Constants.SERVICE_AUTO_FILL;
        } else if (serviceId == 660) {
            return Constants.SERVICE_OFFLINE_FACE;
        }
        return "";
    }
}
