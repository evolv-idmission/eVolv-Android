package com.idmission.libtestproject.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.idmission.client.IdType;
import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.Response;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.activity.NavigationActivity;
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

 public class FinalSteps extends Fragment implements ImageProcessingResponseListener {
     private LinearLayout finalSubmitBlock, clearFormKeyBlock, verifyBlock, extractBlock, finalStepLayout;
     private Button buttonProcessImage, buttonBack, buttonGenericCall, buttonDone;
     private CheckBox finalSubmitCB, clearFormKeyCB, verifyCB, extractCB;
     public static String RESULT_DATA = "RESULT_DATA";
     private ProgressDialog progressDialog;
     private String currentService;
     private EVolvApp eVolvApp;
     private String faceImgType;
     private String Live_Customer_Photo_Node = "Live_Customer_Photo";
     private String Customer_Photo_for_Face_Matching_Node = "Customer_Photo_for_Face_Matching";
     private String ID_Image_Back_Node = "ID_Image_Back";
     private String ID_Image_Front_Node = "ID_Image_Front";
     private String Signature_Image_Node = "Signature_Image";
     private String Processed_ID_Image_Secondary_Node = "Processed_ID_Image_Secondary";
     private String Processed_ID_Image_back_secondary_Node = "Processed_ID_image_back_secondary";
     private String Employee_Photo_Node = "Employee_Photo";
     private String OCR_SIGNATURE_NODE = "OCR_Signature";

     @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.final_steps, container, false);

        eVolvApp = (EVolvApp) getActivity().getApplicationContext();
        currentService = eVolvApp.getCurrentService();
        finalSubmitBlock = (LinearLayout) view.findViewById(R.id.final_submit_block);
        clearFormKeyBlock = (LinearLayout) view.findViewById(R.id.clear_formkey_block);
        verifyBlock = (LinearLayout) view.findViewById(R.id.verify_block);
        extractBlock = (LinearLayout) view.findViewById(R.id.extract_block);
        buttonProcessImage = (Button) view.findViewById(R.id.button_process_image);
        buttonBack = (Button) view.findViewById(R.id.button_back);
        buttonGenericCall = (Button) view.findViewById(R.id.button_generic_call);
        finalSubmitCB = (CheckBox) view.findViewById(R.id.checkbox_final_submit);
        clearFormKeyCB = (CheckBox) view.findViewById(R.id.checkbox_clear_form_key);
        verifyCB = (CheckBox) view.findViewById(R.id.checkbox_verify);
        extractCB = (CheckBox) view.findViewById(R.id.checkbox_extract);
        buttonDone = (Button) view.findViewById(R.id.button_done);

        finalStepLayout = (LinearLayout) view.findViewById(R.id.linear_layout_final_step);

        initilizeProgressBar();

        NavigationActivity.toolbar.setTitle(eVolvApp.getCurrentServiceName());

        clearFormKeyCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                eVolvApp.setFormKeyClear(checked);
            }
        });

        if (currentService.equals(Constants.SERVICE_ID_VALIDATION)) {
            buttonProcessImage.setText(getString(R.string.process_image));
        } else if (currentService.equals(Constants.SERVICE_FACE_MATCH)) {
            buttonProcessImage.setText(getString(R.string.match_face));
        } else if (currentService.equals(Constants.SERVICE_ID_VALIDATION_FACE_MATCH)) {
            buttonProcessImage.setText(getString(R.string.process_image_and_match_face));
        } else if (currentService.equals(Constants.SERVICE_OTP_GENERATION)) {
            finalSubmitBlock.setVisibility(View.GONE);
            clearFormKeyBlock.setVisibility(View.GONE);
            buttonProcessImage.setText(getString(R.string.generate_otp));
        } else if (currentService.equals(Constants.SERVICE_OTP_VERIFICATION)) {
            finalSubmitBlock.setVisibility(View.GONE);
            clearFormKeyBlock.setVisibility(View.GONE);
            buttonProcessImage.setText(getString(R.string.verify_otp));
        } else if (currentService.equals(Constants.SERVICE_CUSTOMER_UPDATE)) {
            finalSubmitBlock.setVisibility(View.GONE);
            clearFormKeyBlock.setVisibility(View.GONE);
            buttonProcessImage.setText(getString(R.string.customer_update));
        } else if (currentService.equals(Constants.SERVICE_CUSTOMER_VERIFICATION)) {
            finalSubmitBlock.setVisibility(View.GONE);
            buttonProcessImage.setText(getString(R.string.customer_Verification));
        } else if (currentService.equals(Constants.SERVICE_ADDRESS_MATCHING)) {
            clearFormKeyBlock.setVisibility(View.GONE);
            verifyBlock.setVisibility(View.VISIBLE);
            extractBlock.setVisibility(View.VISIBLE);
            buttonProcessImage.setText(getString(R.string.address_matching));
        } else if (currentService.equals(Constants.SERVICE_EMPLOYEE_VERIFICATION)) {
            buttonProcessImage.setText(getString(R.string.emp_verification));
        } else if (currentService.equals(Constants.SERVICE_FACE_MATCH_EMPLOYEE_ENROLLMENT)) {
            finalSubmitBlock.setVisibility(View.GONE);
            buttonProcessImage.setText(getString(R.string.emp_enrollment));
        } else if (currentService.equals(Constants.SERVICE_GENERATE_TOKEN)) {
            finalSubmitBlock.setVisibility(View.GONE);
            clearFormKeyBlock.setVisibility(View.GONE);
            buttonProcessImage.setText(getString(R.string.generate_token));
        } else if (currentService.equals(Constants.SERVICE_EMPLOYEE_OVERRIDE)) {
            finalSubmitBlock.setVisibility(View.GONE);
            buttonProcessImage.setText(getString(R.string.create_emp_override));
        } else if (currentService.equals(Constants.SERVICE_CUSTOMER_OVERRIDE)) {
            buttonProcessImage.setText(getString(R.string.create_customer_override));
        } else if (currentService.equals(Constants.SERVICE_ID_VALIDATION_CUSTOMER_ENROLLMENT)) {
            buttonProcessImage.setText(getString(R.string.process_image));
        } else if (currentService.equals(Constants.SERVICE_ID_VALIDATION_EMPLOYEE_ENROLLMENT)) {
            buttonProcessImage.setText(getString(R.string.process_image));
        } else if (currentService.equals(Constants.SERVICE_ID_FACE_EMPLOYEE_ENROLLMENT)) {
            buttonProcessImage.setText(getString(R.string.process_image_and_match_face));
        } else if (currentService.equals(Constants.SERVICE_ID_FACE_VIDEO_RECORDING)) {
            buttonProcessImage.setText(getString(R.string.id_validation_video_match));
        } else if (currentService.equals(Constants.SERVICE_ID_FACE_VIDEO_CUSTOMER_ENROLLMENT)) {
            buttonProcessImage.setText(getString(R.string.id_validation_video_match));
        } else if (currentService.equals(Constants.SERVICE_ID_FACE_VIDEO_EMPLOYEE_ENROLLMENT)) {
            buttonProcessImage.setText(getString(R.string.id_validation_video_match));
        } else if (currentService.equals(Constants.SERVICE_FP_CUSTOMER_ENROLLMENT)) {
            finalSubmitBlock.setVisibility(View.GONE);
            buttonProcessImage.setText(getString(R.string.customer_fp_enrollment));
        } else if (currentService.equals(Constants.SERVICE_FP_EMPLOYEE_ENROLLMENT)) {
            buttonProcessImage.setText(getString(R.string.employee_fp_enrollment));
        } else if (currentService.equals(Constants.SERVICE_EMPLOYEE_UPDATE)) {
            finalSubmitBlock.setVisibility(View.VISIBLE);
            clearFormKeyBlock.setVisibility(View.VISIBLE);
            buttonProcessImage.setText(getString(R.string.employee_update));
        } else if (currentService.equals(Constants.SERVICE_ID_FACE_MATCH_CUSTOMIZE)) {
            buttonProcessImage.setText(getString(R.string.id_validation_face_match));
        } else if (currentService.equals(Constants.SERVICE_FACE_MATCH_SECONDARY_CUSTOMIZE)) {
            buttonProcessImage.setText(getString(R.string.id_validation_face_match));
        } else if (currentService.equals(Constants.SERVICE_EMPLOYEE_SEARCH)) {
            //buttonProcessImage.setText(getString(R.string.employee_search));
            finalSubmitBlock.setVisibility(View.GONE);
            buttonProcessImage.setVisibility(View.GONE);
        } else if (currentService.equals(Constants.SERVICE_CUSTOMER_SEARCH)) {
            //buttonProcessImage.setText(getString(R.string.customer_search));
            finalSubmitBlock.setVisibility(View.GONE);
            buttonProcessImage.setVisibility(View.GONE);
        } else if (currentService.equals(Constants.SERVICE_IDV_VIDEOCONFERENCE)) {
            finalSubmitBlock.setVisibility(View.GONE);
            buttonProcessImage.setVisibility(View.GONE);
            clearFormKeyCB.setChecked(false);
        }else if (currentService.equals(Constants.SERVICE_VIDEOCONFERENCE_FACE_MATCH)) {
            finalSubmitBlock.setVisibility(View.GONE);
            buttonProcessImage.setVisibility(View.GONE);
            clearFormKeyCB.setChecked(false);
        }else if (currentService.equals(Constants.SERVICE_IDV_VIDEOCONFERENCE_FACE_MATCH_CUSTOMER_ENROLLMENT)) {
            finalSubmitBlock.setVisibility(View.GONE);
            buttonProcessImage.setVisibility(View.GONE);
            clearFormKeyCB.setChecked(false);
        }else if (currentService.equals(Constants.SERVICE_IDV_VIDEOCONFERENCE_FACE_MATCH_EMPLOYEE_ENROLLMENT)) {
            finalSubmitBlock.setVisibility(View.GONE);
            buttonProcessImage.setVisibility(View.GONE);
            clearFormKeyCB.setChecked(false);
        }else if (currentService.equals(Constants.SERVICE_SEARCH_CUSTOMER_BIOMETRICS)) {
            finalSubmitBlock.setVisibility(View.GONE);
            buttonProcessImage.setVisibility(View.GONE);
        }else if (currentService.equals(Constants.SERVICE_SEARCH_EMPLOYEE_BIOMETRICS)) {
            finalSubmitBlock.setVisibility(View.GONE);
            buttonProcessImage.setVisibility(View.GONE);
        }

        if (eVolvApp.getCurrentServiceID().equalsIgnoreCase("620") || eVolvApp.getCurrentServiceID().equalsIgnoreCase("660")) {
            finalStepLayout.setVisibility(View.GONE);
            buttonProcessImage.setVisibility(View.GONE);
            buttonGenericCall.setVisibility(View.GONE);
            buttonDone.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

//        final JSONObject additionalData = eVolvApp.getAdditonalData();
//        final IdType idType = eVolvApp.getSelectIdType();
//        final String country = eVolvApp.getCountry();
//        final String state = eVolvApp.getState();
//
//        if(!eVolvApp.getFaceImageType().isEmpty()) {
//            faceImgType = eVolvApp.getFaceImageType();
//        }else {
//            faceImgType = "FACE";
//        }
//        final String idImgType = eVolvApp.getIdImageType();
//
//        final JSONObject addressDetails = eVolvApp.getJsonObjAddressDetails();
//
//        final String empCode = eVolvApp.getEmpCode();
//        final JSONObject empData = eVolvApp.getJsonObjEmployeeData();
//
//        final JSONObject customProductDefinition = eVolvApp.getJsonObjCustomProduct();
//
//        final boolean isFinalSubmit = finalSubmitCB.isChecked();
//        final boolean isClearFormKey = clearFormKeyCB.isChecked();
//
//        eVolvApp.setFormKeyClear(isClearFormKey);
//        if (currentService.equals(Constants.SERVICE_OTP_GENERATION)
//                || currentService.equals(Constants.SERVICE_OTP_VERIFICATION)) {
//            eVolvApp.setFormKeyClear(false);
//        }

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //clear back stack
                FragmentManager fm = getFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                FragmentManager fragmentManager = getFragmentManager();
                if(eVolvApp.isFormKeyClear()){
                    AccountSetup accountSetup = new AccountSetup();
                    fragmentManager.beginTransaction().replace(R.id.flContent, accountSetup).commit();
                }else {
                    ProcessFlow accountSetup = new ProcessFlow();
                    fragmentManager.beginTransaction().replace(R.id.flContent, accountSetup).commit();
                }
            }
        });

        buttonProcessImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                JSONObject additionalData = eVolvApp.getAdditonalData();
                IdType idType = eVolvApp.getSelectIdType();
                String country = eVolvApp.getCountry();
                String state = eVolvApp.getState();

                if(!eVolvApp.getFaceImageType().isEmpty()) {
                     faceImgType = eVolvApp.getFaceImageType();
                }else {
                    faceImgType = "FACE";
                }
                String idImgType = eVolvApp.getIdImageType();

                JSONObject addressDetails = eVolvApp.getJsonObjAddressDetails();

                String empCode = eVolvApp.getEmpCode();
                JSONObject empData = eVolvApp.getJsonObjEmployeeData();

                JSONObject customProductDefinition = eVolvApp.getJsonObjCustomProduct();

                boolean isFinalSubmit = finalSubmitCB.isChecked();
                boolean isClearFormKey = clearFormKeyCB.isChecked();

                eVolvApp.setFormKeyClear(isClearFormKey);
                if (currentService.equals(Constants.SERVICE_OTP_GENERATION)
                        || currentService.equals(Constants.SERVICE_OTP_VERIFICATION)) {
                    eVolvApp.setFormKeyClear(false);
                }

                ImageProcessingSDK.getInstance().setImageProcessingResponseListener(FinalSteps.this);

                if (currentService.equals(Constants.SERVICE_ID_VALIDATION)) {
                    ImageProcessingSDK.getInstance().processImage(getActivity(), country, state, idType, additionalData, finalSubmitCB.isChecked(), isClearFormKey);
                } else if (currentService.equals(Constants.SERVICE_FACE_MATCH)) {
                    ImageProcessingSDK.getInstance().matchFaceImage(getActivity(), faceImgType, idImgType, additionalData, finalSubmitCB.isChecked(), isClearFormKey);
                } else if (currentService.equals(Constants.SERVICE_ID_VALIDATION_FACE_MATCH)) {
                    ImageProcessingSDK.getInstance().processImageAndMatchFace(getActivity(), country, state, idType, faceImgType, additionalData, finalSubmitCB.isChecked(), isClearFormKey);
                } else if (currentService.equals(Constants.SERVICE_OTP_GENERATION)) {
                    String email = eVolvApp.getOtpEmailId();
                    String phone = eVolvApp.getOtpMobileNumber();
                    String notification = eVolvApp.getOtpNotificationType();
                    ImageProcessingSDK.getInstance().generateOTP(getActivity(), additionalData, email, phone, notification);
                } else if (currentService.equals(Constants.SERVICE_OTP_VERIFICATION)) {
                    String otp = eVolvApp.getVerifyOTPValue();
                    ImageProcessingSDK.getInstance().verifyOTP(getActivity(), additionalData, otp);
                } else if (currentService.equals(Constants.SERVICE_CUSTOMER_UPDATE)) {
                    ImageProcessingSDK.getInstance().updateCustomer(getActivity(), faceImgType, additionalData);
                } else if (currentService.equals(Constants.SERVICE_CUSTOMER_VERIFICATION)) {
               //  ImageProcessingSDK.getInstance().verifyCustomer(getActivity(), "FACE", additionalData, isClearFormKey);
                    ImageProcessingSDK.getInstance().verifyCustomer(getActivity(), additionalData, isClearFormKey);
                } else if (currentService.equals(Constants.SERVICE_ADDRESS_MATCHING)) {
                    ImageProcessingSDK.getInstance().verifyAddress(getActivity(), addressDetails, additionalData, extractCB.isChecked(), verifyCB.isChecked(), finalSubmitCB.isChecked());
                } else if (currentService.equals(Constants.SERVICE_EMPLOYEE_VERIFICATION)) {
                    ImageProcessingSDK.getInstance().verifyEmployee(getActivity(), empCode, additionalData, finalSubmitCB.isChecked(), isClearFormKey);
                } else if (currentService.equals(Constants.SERVICE_FACE_MATCH_EMPLOYEE_ENROLLMENT)) {
                    ImageProcessingSDK.getInstance().matchFaceImage(getActivity(), faceImgType, idImgType, additionalData, isClearFormKey, empData);
                } else if (currentService.equals(Constants.SERVICE_GENERATE_TOKEN)) {
                    ImageProcessingSDK.getInstance().generateToken(getActivity());
                } else if (currentService.equals(Constants.SERVICE_EMPLOYEE_OVERRIDE)) {
                    ImageProcessingSDK.getInstance().verifyEmployee(getActivity(), eVolvApp.getOverrideEmpCode(), additionalData, isFinalSubmit, isClearFormKey);
                } else if (currentService.equals(Constants.SERVICE_CUSTOMER_OVERRIDE)) {
                    ImageProcessingSDK.getInstance().verifyEmployee(getActivity(), eVolvApp.getOverrideEmpCode(), additionalData, isFinalSubmit, isClearFormKey);
                } else if (currentService.equals(Constants.SERVICE_ID_VALIDATION_CUSTOMER_ENROLLMENT)) {
                    ImageProcessingSDK.getInstance().processImage(getActivity(), country, state, idType, additionalData, finalSubmitCB.isChecked(), isClearFormKey);
                } else if (currentService.equals(Constants.SERVICE_ID_VALIDATION_EMPLOYEE_ENROLLMENT)) {
                     ImageProcessingSDK.getInstance().processImage(getActivity(), country, state, idType, additionalData, empData,finalSubmitCB.isChecked(), isClearFormKey);
                } else if (currentService.equals(Constants.SERVICE_ID_FACE_EMPLOYEE_ENROLLMENT)) {
                    ImageProcessingSDK.getInstance().processImageAndMatchFace(getActivity(), country, state, idType, faceImgType, additionalData, empData, finalSubmitCB.isChecked(), isClearFormKey);
                } else if (currentService.equals(Constants.SERVICE_ID_FACE_VIDEO_RECORDING)) {
                    ImageProcessingSDK.getInstance().idValidationAndMatchVideo(getActivity(), country, state, idType, additionalData, empData, finalSubmitCB.isChecked(), clearFormKeyCB.isChecked());
                } else if (currentService.equals(Constants.SERVICE_ID_FACE_VIDEO_CUSTOMER_ENROLLMENT)) {
                     ImageProcessingSDK.getInstance().idValidationAndMatchVideo(getActivity(), country, state, idType, additionalData, empData, finalSubmitCB.isChecked(), clearFormKeyCB.isChecked());
                } else if (currentService.equals(Constants.SERVICE_ID_FACE_VIDEO_EMPLOYEE_ENROLLMENT)) {
                    ImageProcessingSDK.getInstance().idValidationAndMatchVideo(getActivity(), country, state, idType, additionalData, empData, finalSubmitCB.isChecked(), clearFormKeyCB.isChecked());
                } else if (currentService.equals(Constants.SERVICE_FP_CUSTOMER_ENROLLMENT)) {
                    ImageProcessingSDK.getInstance().enrollFingerprint(getActivity(), additionalData, isClearFormKey);
                } else if (currentService.equals(Constants.SERVICE_FP_EMPLOYEE_ENROLLMENT)) {
                    ImageProcessingSDK.getInstance().createEmployee(getActivity(), empData, additionalData, isFinalSubmit, isClearFormKey);
                }else if (currentService.equals(Constants.SERVICE_EMPLOYEE_UPDATE)) {
                    ImageProcessingSDK.getInstance().updateEmployee(getActivity(), faceImgType, empData, additionalData, isFinalSubmit, isClearFormKey);
                }else if (currentService.equals(Constants.SERVICE_ID_FACE_MATCH_CUSTOMIZE)) {
                    ImageProcessingSDK.getInstance().executeGenericRequest(getActivity(), customProductDefinition, additionalData, empData, isFinalSubmit, isClearFormKey);
                }else if (currentService.equals(Constants.SERVICE_FACE_MATCH_SECONDARY_CUSTOMIZE)) {
                    ImageProcessingSDK.getInstance().executeGenericRequest(getActivity(), customProductDefinition, additionalData, empData, isFinalSubmit, isClearFormKey);
                }
            }
        });
        buttonGenericCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                JSONObject additionalData = eVolvApp.getAdditonalData();
                IdType idType = eVolvApp.getSelectIdType();
                String country = eVolvApp.getCountry();
                String state = eVolvApp.getState();

                IdType idType_secondary = eVolvApp.getSecondarySelectIdType();
                String country_secondary = eVolvApp.getSecondaryCountry();
                String state_secondary = eVolvApp.getSecondaryState();

                if(!eVolvApp.getFaceImageType().isEmpty()) {
                    faceImgType = eVolvApp.getFaceImageType();
                }else {
                    faceImgType = "FACE";
                }
                String idImgType = eVolvApp.getIdImageType();

                JSONObject addressDetails = eVolvApp.getJsonObjAddressDetails();

                String empCode = eVolvApp.getEmpCode();
                JSONObject empData = eVolvApp.getJsonObjEmployeeData();

                JSONObject customProductDefinition = eVolvApp.getJsonObjCustomProduct();

                boolean isFinalSubmit = finalSubmitCB.isChecked();
                boolean isClearFormKey = clearFormKeyCB.isChecked();

                eVolvApp.setFormKeyClear(isClearFormKey);
                if (currentService.equals(Constants.SERVICE_OTP_GENERATION)
                        || currentService.equals(Constants.SERVICE_OTP_VERIFICATION)) {
                    eVolvApp.setFormKeyClear(false);
                }

                JSONObject serviceCallJson = new JSONObject();
                try {
                    serviceCallJson.put(Constants.COUNTRY_ID_SECONDARY, country_secondary);
                    serviceCallJson.put(Constants.STATE_ID_SECONDARY, state_secondary);
                    serviceCallJson.put(Constants.ID_TYPE_SECONDARY, idType_secondary.getIdType().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (currentService.equals(Constants.SERVICE_ID_VALIDATION)) {
                    try {
                        serviceCallJson.put(Constants.COUNTRY_ID, country);
                        serviceCallJson.put(Constants.STATE_ID, state);
                        serviceCallJson.put(Constants.ID_TYPE, idType.getIdType().toString());
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_FACE_MATCH)) {
                    try {
                        serviceCallJson.put(Constants.TEMPLATE_FACE_IMAGE_TYPE, faceImgType);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_ID_VALIDATION_FACE_MATCH)) {
                    try {
                        serviceCallJson.put(Constants.COUNTRY_ID, country);
                        serviceCallJson.put(Constants.STATE_ID, state);
                        serviceCallJson.put(Constants.ID_TYPE, idType.getIdType().toString());
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.TEMPLATE_FACE_IMAGE_TYPE, faceImgType);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_OTP_GENERATION)) {
                    String email = eVolvApp.getOtpEmailId();
                    String phone = eVolvApp.getOtpMobileNumber();
                    String notification = eVolvApp.getOtpNotificationType();

                    try {
                        serviceCallJson.put(Constants.EMAIL_ID, email);
                        serviceCallJson.put(Constants.MOBILE_NO, phone);
                        serviceCallJson.put(Constants.NOTIFICATION_TYPE, notification);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_OTP_VERIFICATION)) {
                    String otp = eVolvApp.getVerifyOTPValue();
                    try {
                        serviceCallJson.put(Constants.RECEIVE_OTP, otp);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_CUSTOMER_UPDATE)) {
                    try {
                        serviceCallJson.put(Constants.TEMPLATE_FACE_IMAGE_TYPE, faceImgType);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_CUSTOMER_VERIFICATION)) {
                    try {
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_ADDRESS_MATCHING)) {
                    // ImageProcessingSDK.getInstance().verifyAddress(getActivity(), addressDetails, additionalData, extractCB.isChecked(), verifyCB.isChecked(), finalSubmitCB.isChecked());
                    try {
                        serviceCallJson.put(Constants.ADDRESS_JSON, addressDetails);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        //serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_EMPLOYEE_VERIFICATION)) {
                    //ImageProcessingSDK.getInstance().verifyEmployee(getActivity(), empCode, additionalData, finalSubmitCB.isChecked(), isClearFormKey);
                    try {
                        serviceCallJson.put(Constants.EMP_CODE, empCode);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_FACE_MATCH_EMPLOYEE_ENROLLMENT)) {
                    try {
                        serviceCallJson.put(Constants.TEMPLATE_FACE_IMAGE_TYPE, faceImgType);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.EMPLOYEE_DATA_JSON, empData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//               else if (currentService.equals(Constants.SERVICE_GENERATE_TOKEN)) {
//                    ImageProcessingSDK.getInstance().generateToken(getActivity());
//                }
                else if (currentService.equals(Constants.SERVICE_EMPLOYEE_OVERRIDE)) {
                    // ImageProcessingSDK.getInstance().verifyEmployee(getActivity(), eVolvApp.getOverrideEmpCode(), additionalData, isFinalSubmit, isClearFormKey);
                    try {
                        serviceCallJson.put(Constants.EMP_CODE, eVolvApp.getOverrideEmpCode());
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_CUSTOMER_OVERRIDE)) {
                    //ImageProcessingSDK.getInstance().verifyEmployee(getActivity(), eVolvApp.getOverrideEmpCode(), additionalData, isFinalSubmit, isClearFormKey);
                    try {
                        serviceCallJson.put(Constants.EMP_CODE, eVolvApp.getOverrideEmpCode());
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_ID_VALIDATION_CUSTOMER_ENROLLMENT)) {
                    try {
                        serviceCallJson.put(Constants.COUNTRY_ID, country);
                        serviceCallJson.put(Constants.STATE_ID, state);
                        serviceCallJson.put(Constants.ID_TYPE, idType.getIdType().toString());
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_ID_VALIDATION_EMPLOYEE_ENROLLMENT)) {
                    try {
                        serviceCallJson.put(Constants.COUNTRY_ID, country);
                        serviceCallJson.put(Constants.STATE_ID, state);
                        serviceCallJson.put(Constants.ID_TYPE, idType.getIdType().toString());
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.EMPLOYEE_DATA_JSON, empData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_ID_FACE_EMPLOYEE_ENROLLMENT)) {
                    try {
                        serviceCallJson.put(Constants.COUNTRY_ID, country);
                        serviceCallJson.put(Constants.STATE_ID, state);
                        serviceCallJson.put(Constants.ID_TYPE, idType.getIdType().toString());
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.TEMPLATE_FACE_IMAGE_TYPE, faceImgType);
                        serviceCallJson.put(Constants.EMPLOYEE_DATA_JSON, empData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_ID_FACE_VIDEO_RECORDING)) {
                    try {
                        serviceCallJson.put(Constants.COUNTRY_ID, country);
                        serviceCallJson.put(Constants.STATE_ID, state);
                        serviceCallJson.put(Constants.ID_TYPE, idType.getIdType().toString());
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.EMPLOYEE_DATA_JSON, empData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_ID_FACE_VIDEO_CUSTOMER_ENROLLMENT)) {
                    try {
                        serviceCallJson.put(Constants.COUNTRY_ID, country);
                        serviceCallJson.put(Constants.STATE_ID, state);
                        serviceCallJson.put(Constants.ID_TYPE, idType.getIdType().toString());
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.EMPLOYEE_DATA_JSON, empData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_ID_FACE_VIDEO_EMPLOYEE_ENROLLMENT)) {
                    try {
                        serviceCallJson.put(Constants.COUNTRY_ID, country);
                        serviceCallJson.put(Constants.STATE_ID, state);
                        serviceCallJson.put(Constants.ID_TYPE, idType.getIdType().toString());
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.EMPLOYEE_DATA_JSON, empData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_FP_CUSTOMER_ENROLLMENT)) {
                    // ImageProcessingSDK.getInstance().enrollFingerprint(getActivity(), additionalData, isClearFormKey);
                    try {
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_FP_EMPLOYEE_ENROLLMENT)) {
                    //ImageProcessingSDK.getInstance().createEmployee(getActivity(), empData, additionalData, isFinalSubmit, isClearFormKey);
                    try {
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.TEMPLATE_FACE_IMAGE_TYPE, faceImgType);
                        serviceCallJson.put(Constants.EMPLOYEE_DATA_JSON, empData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_EMPLOYEE_UPDATE)) {
                    try {
                        serviceCallJson.put(Constants.TEMPLATE_FACE_IMAGE_TYPE, faceImgType);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.EMPLOYEE_DATA_JSON, empData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_ID_FACE_MATCH_CUSTOMIZE)) {
                    // ImageProcessingSDK.getInstance().executeGenericRequest(getActivity(), customProductDefinition, additionalData, empData, isFinalSubmit, isClearFormKey);
                    try {
                        serviceCallJson.put(Constants.PRODUCT_DEFINITION_JSON, customProductDefinition);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.EMPLOYEE_DATA_JSON, empData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (currentService.equals(Constants.SERVICE_FACE_MATCH_SECONDARY_CUSTOMIZE)) {
                    //ImageProcessingSDK.getInstance().executeGenericRequest(getActivity(), customProductDefinition, additionalData, empData, isFinalSubmit, isClearFormKey);
                    try {
                        serviceCallJson.put(Constants.PRODUCT_DEFINITION_JSON, customProductDefinition);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.EMPLOYEE_DATA_JSON, empData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (currentService.equals(Constants.SERVICE_EMPLOYEE_SEARCH)) {
                    try {
                        serviceCallJson.put(Constants.EMPLOYEE_DATA_JSON, empData);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (currentService.equals(Constants.SERVICE_CUSTOMER_SEARCH)) {
                    try {
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(currentService.equals(Constants.SERVICE_IDV_VIDEOCONFERENCE)) {
                    try {
                        serviceCallJson.put(Constants.COUNTRY_ID, country);
                        serviceCallJson.put(Constants.STATE_ID, state);
                        serviceCallJson.put(Constants.ID_TYPE, idType.getIdType().toString());
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
//                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, isClearFormKey == true ? "Y" : "N");
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, clearFormKeyCB.isChecked() == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(currentService.equals(Constants.SERVICE_VIDEOCONFERENCE_FACE_MATCH)) {
                    try {

                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, clearFormKeyCB.isChecked() == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(currentService.equals(Constants.SERVICE_IDV_VIDEOCONFERENCE_FACE_MATCH_CUSTOMER_ENROLLMENT)) {
                    try {
                        serviceCallJson.put(Constants.COUNTRY_ID, country);
                        serviceCallJson.put(Constants.STATE_ID, state);
                        serviceCallJson.put(Constants.ID_TYPE, idType.getIdType().toString());
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, clearFormKeyCB.isChecked() == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(currentService.equals(Constants.SERVICE_IDV_VIDEOCONFERENCE_FACE_MATCH_EMPLOYEE_ENROLLMENT)) {
                    try {
                        serviceCallJson.put(Constants.COUNTRY_ID, country);
                        serviceCallJson.put(Constants.STATE_ID, state);
                        serviceCallJson.put(Constants.ID_TYPE, idType.getIdType().toString());
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.EMPLOYEE_DATA_JSON, empData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, clearFormKeyCB.isChecked() == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(currentService.equals(Constants.SERVICE_SEARCH_CUSTOMER_BIOMETRICS)) {
                    try {
                        serviceCallJson.put(Constants.TEMPLATE_FACE_IMAGE_TYPE, faceImgType);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, clearFormKeyCB.isChecked() == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(currentService.equals(Constants.SERVICE_SEARCH_EMPLOYEE_BIOMETRICS)) {
                    try {
                        serviceCallJson.put(Constants.TEMPLATE_FACE_IMAGE_TYPE, faceImgType);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, clearFormKeyCB.isChecked() == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(currentService.equals(Constants.SERVICE_FP_CUSTOMER_ENROLLMENT)) {
                    try {
                        serviceCallJson.put(Constants.TEMPLATE_FACE_IMAGE_TYPE, faceImgType);
                        serviceCallJson.put(Constants.ADDITIONAL_DATA_JSON, additionalData);
                        serviceCallJson.put(Constants.CLEAR_FORM_KEY, clearFormKeyCB.isChecked() == true ? "Y" : "N");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //API Call
                ImageProcessingSDK.getInstance().setImageProcessingResponseListener(FinalSteps.this);
                ImageProcessingSDK.getInstance().genericApiCall(getActivity(), serviceCallJson);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    public void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onImageProcessingResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK", "CALLBACK:::: onImageProcessingResultAvailable");
        displayResult(resultMap, response);
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
        displayResult(resultMap, response);
    }

    @Override
    public void onFaceMatchingResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK", "CALLBACK:::: onFaceMatchingResultAvailable");
        displayResult(resultMap, response);
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
        Log.d("SDK", "CALLBACK:::: onImageProcessingAndFaceMatchingResultAvailable");
        displayResult(resultMap, response);
    }

    @Override
    public void onOperationResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onCustomerVerificationResultAvailable(Map<String, String> resultMap, Response response) {
        Log.d("SDK", "CALLBACK:::: onCustomerVerificationResultAvailable");
        displayResult(resultMap, response);
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
        Log.d("SDK", "CALLBACK:::: onFingerprintEnrolmentFinished");
        displayResult(resultMap, response);
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
        Log.d("SDK", "CALLBACK:::: onVerifyAddressFinished");
        displayResult(resultMap, responses);
    }

    @Override
    public void onCreateEmployeeFinished(Map<String, String> resultMap, Response responses) {
        Log.d("SDK", "CALLBACK:::: onCreateEmployeeFinished");
        displayResult(resultMap, responses);
    }

    @Override
    public void onVerifyEmployeeFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK", "CALLBACK:::: onVerifyEmployeeFinished");
        displayResult(resultMap, response);
    }

    @Override
    public void onGenerateTokenFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK", "CALLBACK:::: onGenerateTokenFinished");
        displayResult(resultMap, response);
    }

    @Override
    public void onVerifyTokenFinished(Map<String, String> resultMap, Response responses) {

    }

    @Override
    public void onSnippetImageCaptureResultAvailable(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onUpdateCustomerFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK", "CALLBACK:::: onUpdateCustomerFinished");
        displayResult(resultMap, response);
    }

    @Override
    public void onGenerateOTPFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK", "CALLBACK:::: onGenerateOTPFinished");
        displayResult(resultMap, response);
    }

    @Override
    public void onVerifyOTPFinished(Map<String, String> resultMap, Response response) {
        Log.d("SDK", "CALLBACK:::: onVerifyOTPFinished");
        displayResult(resultMap, response);
    }

//    @Override
//    public void onInitializationResultAvailable(Map<String, String> resultMap, Response responses) {
//
//    }

    @Override
    public void onExecuteCustomProductCall(Map<String, String> resultMap, Response response) {
        displayResult(resultMap, response);
    }

     @Override
     public void onUpdateEmployeeFinished(Map<String, String> resultMap, Response responses) {
         Log.d("SDK", "CALLBACK:::: onUpdateEmployeeFinished");
         displayResult(resultMap, responses);
     }

     @Override
     public void onIDValidationAndVideoMatchingFinished(Map<String, String> resultMap, Response responses) {
         Log.d("SDK", "CALLBACK:::: onIDValidationAndVideoMatchingFinished");
         displayResult(resultMap, responses);
     }

     @Override
     public void genericApiCallResponse(Map<String, String> resultMap, Response responses) {
         Log.d("SDK", "CALLBACK:::: genericApiCallResponse");
         displayResult(resultMap, responses);
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

     private void displayResult(Map<String, String> resultMap, Response response) {
         if (null == resultMap || resultMap.isEmpty()) {
             resultMap = new HashMap<>();
             resultMap.put("" + response.getStatusCode(), response.getStatusMessage());
         }
         if (resultMap.containsKey(Live_Customer_Photo_Node)) {
             resultMap.remove(Live_Customer_Photo_Node);
         }
         if (resultMap.containsKey(ID_Image_Back_Node)) {
             resultMap.remove(ID_Image_Back_Node);
         }
         if (resultMap.containsKey(ID_Image_Front_Node)) {
             resultMap.remove(ID_Image_Front_Node);
         }
         if (resultMap.containsKey(Customer_Photo_for_Face_Matching_Node)) {
             resultMap.remove(Customer_Photo_for_Face_Matching_Node);
         }
         if (resultMap.containsKey(Signature_Image_Node)) {
             resultMap.remove(Signature_Image_Node);
         }
         if (resultMap.containsKey(Processed_ID_Image_Secondary_Node)) {
             resultMap.remove(Processed_ID_Image_Secondary_Node);
         }
         if (resultMap.containsKey(Processed_ID_Image_back_secondary_Node)) {
             resultMap.remove(Processed_ID_Image_back_secondary_Node);
         }
         if(resultMap.containsKey(Employee_Photo_Node)){
             resultMap.remove(Employee_Photo_Node);
         }
         if(resultMap.containsKey(OCR_SIGNATURE_NODE)){
             resultMap.remove(OCR_SIGNATURE_NODE);
         }
         resultMap.put("" + response.getStatusCode(), response.getStatusMessage());

         FragmentManager fragmentManager = getFragmentManager();
         ResultView finalSteps = new ResultView();
         Bundle bundle = new Bundle();
         bundle.putSerializable(RESULT_DATA, (Serializable) resultMap);
         finalSteps.setArguments(bundle);
         fragmentManager.beginTransaction().replace(R.id.flContent, finalSteps).addToBackStack(null).commit();
         NavigationActivity.toolbar.setTitle(R.string.result);

         cancelProgressDialog();
    }

    public void initilizeProgressBar() {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.please_wait_msg));
        progressDialog.setCancelable(false);
    }

    public void cancelProgressDialog() {
        if (null != progressDialog || progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }



}
