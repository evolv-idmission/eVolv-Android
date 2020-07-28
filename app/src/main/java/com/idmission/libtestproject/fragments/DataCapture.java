package com.idmission.libtestproject.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.adapter.CustomListViewAdapter;
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataCapture extends Fragment {

    private Button buttonBack, buttonNext;
    private EditText customerNameEdtText, uniqueCustomerNumberEdtText, uniqueMerchantNumberEdtTxt, serviceIDEdtTxt, customerTypeEdtTxt, customerAttributeEdtTxt, customerPhoneEdtTxt,
            customerEmailEdtTxt, uniqueEmployeeCodeEdtTxt, uniqueEmployeeNumberEdtTxt, oldClientCustomerNumberEdtTxt, addressLine1EdtTxt, addressLine2EdtTxt, countryEdtTxt, stateEdtTxt, cityEdtTxt, postalCodeEdtTxt, agentCountryEdtTxt, encryptedDataEdtTxt, formIdEdtTxt, keyEdtTxt, valueEdtTxt;
    private Spinner serviceIDSpinner;
    private TextView serviceIdInfoTxtView;
    private static final String CUSTOMER_NAME = "Customer_Name", UNIQUE_CUSTOMER_NUMBER = "Unique_Customer_Number", UNIQUE_MERCHANT_NUMBER = "Unique_Merchant_Number", SERVICE_ID = "Service_ID", CUSTOMER_TYPE = "Customer_Type",
            CUSTOMER_ATTRIBUTE = "Customer_Attribute", CUSTOMER_PHONE = "Customer_Phone", CUSTOMER_EMAIL = "Customer_Email", UNIQUE_EMPLOYEE_CODE = "Unique_Employee_Code", UNIQUE_EMPLOYEE_NUMBER = "Unique_Employee_Number",
            OLD_CLIENT_CUSTOMER_NO = "Old_Client_Customer_No", ADDRESSLINE_1 = "Addressline_1", ADDRESSLINE_2 = "Addressline_2", COUNTRY = "Country", STATE = "State", CITY = "City",
            MANUAL_REVIEW_REQUIRED = "Manual_Review_Required", BYPASS_AGE_VALIDATION = "Bypass_Age_Validation", BYPASS_NAME_MATCHING = "Bypass_Name_Matching", DEDUPLICATION_REQUIRED = "Deduplication_Required", NEED_IMMEDIDATE_RESPONSE = "Need_Immediate_Response", ID_CAPTURE_SECONDARY = "ID_CAPTURE_SECONDARY";
    private static final String LOGTAG = "DataCapture";
    // public static String ADDITIONAL_DATA = "ADDITIONAL_DATA";

    private String[] serviceIdInfo = {"10 ID Validation and Face Match",
            "50 ID Validation and Face Match",
            "51 ID Validation and Face Match",
            "60 Face Match with customer enrollment",
            "65 Face Match without customer enrollment",
            "77 ID Validation and Face Match",
            "78 ID Validation and Face Match",
            "79 Bank Account Statement",
            "80 Birth Certificate",
            "100 Customer Verification",
            "110 Customer Verification",
            "111 Customer Verification",
            "112 Customer Verification",
            "113 Customer Verification",
            "114 Customer Verification",
            "115 Customer Verification",
            "116 Customer Verification",
            "150 Customer Verification",
            "151 Customer Verification",
            "152 Customer Verification",
            "153 Customer Verification",
            "154 Customer Verification",
            "200 Address Matching",
            "300 Employee Verification",
            "310 Face Match with employee enrollment",
            "320 Create Customer(Override)",
            "330 Create Employee(Override)",
            "70 Customer Update"};

    private String[] serviceIdDetail = {"10 ID Validation and Face Match without customer enrollment",
            "50 ID Validation and Face Match with customer enrollment",
            "51 ID Validation and Face Match with customer enrollment and No Name Match",
            "60 Face Match with customer enrollment",
            "65 Face Match without customer enrollment",
            "77 ID Validation and Face Match with customer enrollment ( Two Step Process)",
            "78 ID Validation and Face Match with customer enrollment ( Two Step Process)",
            "79 Bank Account Statement",
            "80 Birth Certificate",
            "100 Customer Verification",
            "110 Customer Verification",
            "111 Customer Verification",
            "112 Customer Verification",
            "113 Customer Verification",
            "114 Customer Verification",
            "115 Customer Verification",
            "116 Customer Verification",
            "150 Customer Verification",
            "151 Customer Verification",
            "152 Customer Verification",
            "153 Customer Verification",
            "154 Customer Verification",
            "200 Address Matching",
            "300 Employee Verification",
            "310 Face Match with employee enrollment",
            "320 Create Customer using override feature",
            "330 Create Employee using override feature",
            "70 Customer Update"};

    private SwitchCompat manualReviewReqSwitch, bypassAgeValidationSwitch, bypassNameMatchingSwitch, deduplicatonReqSwitch, needImmediateResponseSwitch, captureSecondaryIdSwitch;
    private String manualReviewReq, bypassAgeValidation, bypassNameMatching, deduplicationReq, needImmediateResponse, captureSecondaryId;
    private Spinner spinnerGender;
    private static final String[] CUSTOMER_GENDER_TYPE = {"M", "F"};
    private LinearLayout secondaryIdLinearLayout;
    private ImageView imageViewAddField;
    private EVolvApp eVolvApp;
    public static LinkedHashMap<String, String> mapAddKeyValue = new LinkedHashMap<>();
    private View addCustomView;
    public static ListView listView;
    public static CustomListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.data_capture, container, false);

        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);
        customerNameEdtText = (EditText) view.findViewById(R.id.edit_text_customer_name);
        uniqueCustomerNumberEdtText = (EditText) view.findViewById(R.id.edit_text_customer_number);
        uniqueMerchantNumberEdtTxt = (EditText) view.findViewById(R.id.edit_text_unique_merchant_number);
        serviceIDEdtTxt = (EditText) view.findViewById(R.id.edit_text_service_id);
        customerTypeEdtTxt = (EditText) view.findViewById(R.id.edit_text_customer_type);
        customerAttributeEdtTxt = (EditText) view.findViewById(R.id.edit_text_customer_attribute);
        customerPhoneEdtTxt = (EditText) view.findViewById(R.id.edit_text_customer_phone);
        customerEmailEdtTxt = (EditText) view.findViewById(R.id.edit_text_customer_email);
        uniqueEmployeeCodeEdtTxt = (EditText) view.findViewById(R.id.edit_text_unique_emp_code);
        uniqueEmployeeNumberEdtTxt = (EditText) view.findViewById(R.id.edit_text_unique_emp_number);
        oldClientCustomerNumberEdtTxt = (EditText) view.findViewById(R.id.edit_text_old_client_cus_number);
        addressLine1EdtTxt = (EditText) view.findViewById(R.id.edit_text_address_line1);
        addressLine2EdtTxt = (EditText) view.findViewById(R.id.edit_text_address_line2);
        countryEdtTxt = (EditText) view.findViewById(R.id.edit_text_country);
        stateEdtTxt = (EditText) view.findViewById(R.id.edit_text_state);
        cityEdtTxt = (EditText) view.findViewById(R.id.edit_text_city);
        serviceIDSpinner = (Spinner) view.findViewById(R.id.spinner_service_id);
        serviceIdInfoTxtView = (TextView) view.findViewById(R.id.info_service_id);
        manualReviewReqSwitch = (SwitchCompat) view.findViewById(R.id.manual_review_req_switch);
        bypassAgeValidationSwitch = (SwitchCompat) view.findViewById(R.id.bypass_age_validation_switch);
        bypassNameMatchingSwitch = (SwitchCompat) view.findViewById(R.id.bypass_name_matching_switch);
        deduplicatonReqSwitch = (SwitchCompat) view.findViewById(R.id.deduplication_required_switch);
        needImmediateResponseSwitch = (SwitchCompat) view.findViewById(R.id.need_immediate_response);
        spinnerGender = (Spinner) view.findViewById(R.id.spinner_gender);
        postalCodeEdtTxt = (EditText) view.findViewById(R.id.edit_text_postal_code);
        agentCountryEdtTxt = (EditText) view.findViewById(R.id.edit_text_agent_country);
        encryptedDataEdtTxt = (EditText) view.findViewById(R.id.edit_text_encrypted_data);
        captureSecondaryIdSwitch = (SwitchCompat) view.findViewById(R.id.capture_secondary_id);
        secondaryIdLinearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_secondary_id);
        formIdEdtTxt = (EditText) view.findViewById(R.id.edit_text_form_id);
        keyEdtTxt = (EditText) view.findViewById(R.id.edit_text_form_key);
        valueEdtTxt = (EditText) view.findViewById(R.id.edit_text_form_value);
        imageViewAddField=(ImageView)view.findViewById(R.id.add_key_value);
        addCustomView = view.findViewById(R.id.add_key_value_lay);
        listView = (ListView) addCustomView.findViewById(R.id.list_view);

        serviceIDSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, serviceIdInfo));
        spinnerGender.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, CUSTOMER_GENDER_TYPE));

        eVolvApp = (EVolvApp) getActivity().getApplicationContext();

        serviceIDEdtTxt.setText(eVolvApp.getCurrentServiceID());

        if (eVolvApp.isSecondaryIdCapture()) {
            secondaryIdLinearLayout.setVisibility(View.VISIBLE);
        } else {
            secondaryIdLinearLayout.setVisibility(View.GONE);
        }

        displayPreviousValue();

        if (eVolvApp.getCurrentServiceID().equalsIgnoreCase("620") || eVolvApp.getCurrentServiceID().equalsIgnoreCase("660")) {
            customerNameEdtText.setEnabled(false);
            uniqueCustomerNumberEdtText.setEnabled(false);
            uniqueMerchantNumberEdtTxt.setEnabled(false);
            serviceIDEdtTxt.setEnabled(false);
            customerTypeEdtTxt.setEnabled(false);
            customerAttributeEdtTxt.setEnabled(false);
            customerPhoneEdtTxt.setEnabled(false);
            customerEmailEdtTxt.setEnabled(false);
            uniqueEmployeeCodeEdtTxt.setEnabled(false);
            uniqueEmployeeNumberEdtTxt.setEnabled(false);
            oldClientCustomerNumberEdtTxt.setEnabled(false);
            addressLine1EdtTxt.setEnabled(false);
            addressLine2EdtTxt.setEnabled(false);
            countryEdtTxt.setEnabled(false);
            stateEdtTxt.setEnabled(false);
            cityEdtTxt.setEnabled(false);
            postalCodeEdtTxt.setEnabled(false);
            agentCountryEdtTxt.setEnabled(false);
            encryptedDataEdtTxt.setEnabled(false);
            formIdEdtTxt.setEnabled(false);
            keyEdtTxt.setEnabled(false);
            valueEdtTxt.setEnabled(false);

            serviceIDSpinner.setEnabled(false);
            spinnerGender.setEnabled(false);

            manualReviewReqSwitch.setEnabled(false);
            bypassAgeValidationSwitch.setEnabled(false);
            bypassNameMatchingSwitch.setEnabled(false);
            deduplicatonReqSwitch.setEnabled(false);
            needImmediateResponseSwitch.setEnabled(false);
            captureSecondaryIdSwitch.setEnabled(false);

            imageViewAddField.setEnabled(false);
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        eVolvApp.setAdditonalData(getAdditionalDataJSON());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eVolvApp.setAdditonalData(getAdditionalDataJSON());
                ImageProcessingSDK.getInstance().setCallExecutionParameter(getCallExecutionParameterJSON());
                if ((IDValidationFaceMatch.listAdditionalFeatures.size() - IDValidationFaceMatch.viewPager.getCurrentItem()) == 1) {
                    FragmentManager fragmentManager = getParentFragment().getFragmentManager();
                    FinalSteps finalSteps = new FinalSteps();
                    fragmentManager.beginTransaction().replace(R.id.flContent, finalSteps).addToBackStack(null).commit();
                } else {
                    IDValidationFaceMatch.viewPager.setCurrentItem(IDValidationFaceMatch.viewPager.getCurrentItem() + 1);
                }

            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eVolvApp.setAdditonalData(getAdditionalDataJSON());
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

        serviceIDSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String serviceid = serviceIdDetail[i];
                serviceIdInfoTxtView.setText(serviceIdDetail[i]);

                String[] serviceid_split = serviceid.split(" ");
                serviceIDEdtTxt.setText(serviceid_split[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        IDValidationFaceMatch.viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try{
                    String selectTab = IDValidationFaceMatch.tabLayout.getTabAt(IDValidationFaceMatch.viewPager.getCurrentItem()).getText().toString();
                    if(selectTab.equalsIgnoreCase(Constants.ADDITIONAL_DATA)) {
                        eVolvApp.setAdditonalData(getAdditionalDataJSON());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        });

        imageViewAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isEmpty(keyEdtTxt.getText().toString()) && !StringUtil.isEmpty(valueEdtTxt.getText().toString())) {

                    mapAddKeyValue.put(keyEdtTxt.getText().toString(), valueEdtTxt.getText().toString());

                    keyEdtTxt.setText("");
                    valueEdtTxt.setText("");

                    adapter = new CustomListViewAdapter(getActivity());
                    listView.setAdapter(adapter);
                    setListViewHeightBasedOnItems(listView);
                } else {
                    showErrorMessage(getView(), "Please enter Key & value");
                }
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    private JSONObject getCallExecutionParameterJSON() {

        if (manualReviewReqSwitch.isChecked()) {
            manualReviewReq = "Y";
        } else {
            manualReviewReq = "N";
        }
        if (bypassAgeValidationSwitch.isChecked()) {
            bypassAgeValidation = "Y";
        } else {
            bypassAgeValidation = "N";
        }
        if (bypassNameMatchingSwitch.isChecked()) {
            bypassNameMatching = "Y";
        } else {
            bypassNameMatching = "N";
        }
        if (deduplicatonReqSwitch.isChecked()) {
            deduplicationReq = "Y";
        } else {
            deduplicationReq = "N";
        }
        if (needImmediateResponseSwitch.isChecked()) {
            needImmediateResponse = "Y";
        } else {
            needImmediateResponse = "N";
        }

        String serviceID = eVolvApp.getCurrentServiceID();
        JSONObject jObject = new JSONObject();

        try {
            if (!StringUtil.isEmpty(serviceID)) {
                jObject.put("Service_ID", serviceID);
            }

            if (!StringUtil.isEmpty(manualReviewReq)) {
                jObject.put("Manual_Review_Required", manualReviewReq);
            }

            if (!StringUtil.isEmpty(bypassAgeValidation)) {
                jObject.put("Bypass_Age_Validation", bypassAgeValidation);
            }

            if (!StringUtil.isEmpty(bypassNameMatching)) {
                jObject.put("Bypass_Name_Matching", bypassNameMatching);
            }

            if (!StringUtil.isEmpty(deduplicationReq)) {
                jObject.put("Deduplication_Required", deduplicationReq);
            }

            if (!StringUtil.isEmpty(needImmediateResponse)) {
                jObject.put("Need_Immediate_Response", needImmediateResponse);
            }

        } catch (Exception exc) {
            Log.d(LOGTAG, "getCallExecutionParameterJSON Exc : " + exc);
        }

        if (jObject.length() > 0)
            return jObject;

        return null;

    }

    private JSONObject getAdditionalDataJSON() {
        String customername = customerNameEdtText.getText().toString().trim();
        String uniqueCustomerNumber = uniqueCustomerNumberEdtText.getText().toString().trim();
        String uniqueMerchantNumber = uniqueMerchantNumberEdtTxt.getText().toString().trim();
        String serviceId = serviceIDEdtTxt.getText().toString().trim();
        String custType = customerTypeEdtTxt.getText().toString().trim();
        String custAttribute = customerAttributeEdtTxt.getText().toString().trim();
        String custPhone = customerPhoneEdtTxt.getText().toString().trim();
        String custEmail = customerEmailEdtTxt.getText().toString().trim();
        String uniqueEmployeeCode = uniqueEmployeeCodeEdtTxt.getText().toString().trim();
        String uEmployeeNumber = uniqueEmployeeNumberEdtTxt.getText().toString().trim();
        String oldClientCustNumber = oldClientCustomerNumberEdtTxt.getText().toString().trim();
        String addressLine1 = addressLine1EdtTxt.getText().toString().trim();
        String addressLine2 = addressLine2EdtTxt.getText().toString().trim();
        String country = countryEdtTxt.getText().toString().trim();
        String state = stateEdtTxt.getText().toString().trim();
        String city = cityEdtTxt.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String postalCode = postalCodeEdtTxt.getText().toString().trim();
        String agentCountry = agentCountryEdtTxt.getText().toString().trim();
        String encryptedData = encryptedDataEdtTxt.getText().toString().trim();
        String formId = formIdEdtTxt.getText().toString().trim();

        if (manualReviewReqSwitch.isChecked()) {
            manualReviewReq = "Y";
        } else {
            manualReviewReq = "N";
        }
        if (bypassAgeValidationSwitch.isChecked()) {
            bypassAgeValidation = "Y";
        } else {
            bypassAgeValidation = "N";
        }
        if (bypassNameMatchingSwitch.isChecked()) {
            bypassNameMatching = "Y";
        } else {
            bypassNameMatching = "N";
        }
        if (deduplicatonReqSwitch.isChecked()) {
            deduplicationReq = "Y";
        } else {
            deduplicationReq = "N";
        }
        if (needImmediateResponseSwitch.isChecked()) {
            needImmediateResponse = "Y";
        } else {
            needImmediateResponse = "N";
        }
        if (eVolvApp.isSecondaryIdCapture()) {
            captureSecondaryId = captureSecondaryIdSwitch.isChecked() ? "Y" : "N";
        }

        JSONObject jObject = new JSONObject();
        try {
            /*
             * Only add key without spaces
             * */
            if (!StringUtil.isEmpty(customername)) {
                jObject.put("Customer_Name", customername);
                PreferenceUtils.setPreference(getActivity(), CUSTOMER_NAME, customername);
            }

            if (!StringUtil.isEmpty(uniqueCustomerNumber)) {
                jObject.put("Unique_Customer_Number", uniqueCustomerNumber);
                PreferenceUtils.setPreference(getActivity(), UNIQUE_CUSTOMER_NUMBER, uniqueCustomerNumber);
            }

            if (!StringUtil.isEmpty(uniqueMerchantNumber)) {
                jObject.put("Unique_Merchant_Number", uniqueMerchantNumber);
                PreferenceUtils.setPreference(getActivity(), UNIQUE_MERCHANT_NUMBER, uniqueMerchantNumber);
            }

            if (!StringUtil.isEmpty(serviceId)) {
                jObject.put("Service_ID", serviceId);
                PreferenceUtils.setPreference(getActivity(), SERVICE_ID, serviceId);
            }

            if (!StringUtil.isEmpty(custType)) {
                jObject.put("Customer_Type", custType);
                PreferenceUtils.setPreference(getActivity(), CUSTOMER_TYPE, custType);
            }

            if (!StringUtil.isEmpty(custAttribute)) {
                jObject.put("Customer_Attribute", custAttribute);
                PreferenceUtils.setPreference(getActivity(), CUSTOMER_ATTRIBUTE, custAttribute);
            }

            if (!StringUtil.isEmpty(custPhone)) {
                jObject.put("Customer_Phone", custPhone);
                PreferenceUtils.setPreference(getActivity(), CUSTOMER_PHONE, custPhone);
            }

            if (!StringUtil.isEmpty(custEmail)) {
                jObject.put("Customer_Email", custEmail);
                PreferenceUtils.setPreference(getActivity(), CUSTOMER_EMAIL, custEmail);
            }

            if (!StringUtil.isEmpty(uniqueEmployeeCode)) {
                jObject.put("Unique_Employee_Code", uniqueEmployeeCode);
                PreferenceUtils.setPreference(getActivity(), UNIQUE_EMPLOYEE_CODE, uniqueEmployeeCode);
            }

            if (!StringUtil.isEmpty(uEmployeeNumber)) {
                jObject.put("Unique_Employee_Number", uEmployeeNumber);
                PreferenceUtils.setPreference(getActivity(), UNIQUE_EMPLOYEE_NUMBER, uEmployeeNumber);
            }

            if (!StringUtil.isEmpty(oldClientCustNumber)) {
                jObject.put("Old_Client_Customer_Number", oldClientCustNumber);
                PreferenceUtils.setPreference(getActivity(), OLD_CLIENT_CUSTOMER_NO, oldClientCustNumber);
            }

            if (!StringUtil.isEmpty(addressLine1)) {
                jObject.put("AddressLine1", addressLine1);
                PreferenceUtils.setPreference(getActivity(), ADDRESSLINE_1, addressLine1);
            }

            if (!StringUtil.isEmpty(addressLine2)) {
                jObject.put("AddressLine2", addressLine2);
                PreferenceUtils.setPreference(getActivity(), ADDRESSLINE_2, addressLine2);
            }

            if (!StringUtil.isEmpty(country)) {
                jObject.put("Country", country);
                PreferenceUtils.setPreference(getActivity(), COUNTRY, country);
            }

            if (!StringUtil.isEmpty(state)) {
                jObject.put("State", state);
                PreferenceUtils.setPreference(getActivity(), STATE, state);
            }

            if (!StringUtil.isEmpty(city)) {
                jObject.put("City", city);
                PreferenceUtils.setPreference(getActivity(), CITY, city);
            }

            if (!StringUtil.isEmpty(manualReviewReq)) {
                jObject.put("Manual_Review_Required", manualReviewReq);
                PreferenceUtils.setPreference(getActivity(), MANUAL_REVIEW_REQUIRED, manualReviewReq);
            }

            if (!StringUtil.isEmpty(bypassAgeValidation)) {
                jObject.put("Bypass_Age_Validation", bypassAgeValidation);
                PreferenceUtils.setPreference(getActivity(), BYPASS_AGE_VALIDATION, bypassAgeValidation);
            }

            if (!StringUtil.isEmpty(bypassNameMatching)) {
                jObject.put("Bypass_Name_Matching", bypassNameMatching);
                PreferenceUtils.setPreference(getActivity(), BYPASS_NAME_MATCHING, bypassNameMatching);
            }

            if (!StringUtil.isEmpty(deduplicationReq)) {
                jObject.put("Deduplication_Required", deduplicationReq);
                PreferenceUtils.setPreference(getActivity(), DEDUPLICATION_REQUIRED, deduplicationReq);
            }

            if (!StringUtil.isEmpty(needImmediateResponse)) {
                jObject.put("Need_Immediate_Response", needImmediateResponse);
                PreferenceUtils.setPreference(getActivity(), NEED_IMMEDIDATE_RESPONSE, needImmediateResponse);
            }

            if (!StringUtil.isEmpty(captureSecondaryId)) {
                jObject.put("Capture_Secondary_ID", captureSecondaryId);
                PreferenceUtils.setPreference(getActivity(), ID_CAPTURE_SECONDARY, captureSecondaryId);
            }

            if (!StringUtil.isEmpty(gender)) {
                jObject.put("Customer_Gender", gender);
            }

            if (!StringUtil.isEmpty(postalCode)) {
                jObject.put("Postal_Code", postalCode);
            }

            if (!StringUtil.isEmpty(agentCountry)) {
                jObject.put("AgentCountry", agentCountry);
            }

            if (!StringUtil.isEmpty(encryptedData)) {
                jObject.put("Encrypted_Data", encryptedData);
            }

            if (!StringUtil.isEmpty(formId)) {
                jObject.put("PreviousFormId", formId);
            }

            if (null != mapAddKeyValue && mapAddKeyValue.size() > 0) {
                for (Map.Entry<String, String> entry : mapAddKeyValue.entrySet()) {

                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (!StringUtil.isEmpty(key) && !StringUtil.isEmpty(value)) {
                        jObject.put(key, value);
                    }
                }
            }
        } catch (JSONException exc) {
            Log.d(LOGTAG, "getAdditionalDataJSON Exc : " + exc);
        }

        if (jObject.length() > 0)
            return jObject;

        return null;
    }

    private void displayPreviousValue() {
        JSONObject additonalData = eVolvApp.getAdditonalData();
        if (additonalData != null) {
            customerNameEdtText.setText(additonalData.optString("Customer_Name"));
            uniqueCustomerNumberEdtText.setText(additonalData.optString("Unique_Customer_Number"));
            uniqueMerchantNumberEdtTxt.setText(additonalData.optString("Unique_Merchant_Number"));
            serviceIDEdtTxt.setText(additonalData.optString("Service_ID"));
            customerTypeEdtTxt.setText(additonalData.optString("Customer_Type"));
            customerAttributeEdtTxt.setText(additonalData.optString("Customer_Attribute"));
            customerPhoneEdtTxt.setText(additonalData.optString("Customer_Phone"));
            customerEmailEdtTxt.setText(additonalData.optString("Customer_Email"));
            uniqueEmployeeCodeEdtTxt.setText(additonalData.optString("Unique_Employee_Code"));
            uniqueEmployeeNumberEdtTxt.setText(additonalData.optString("Unique_Employee_Number"));
            oldClientCustomerNumberEdtTxt.setText(additonalData.optString("Old_Client_Customer_Number"));
            addressLine1EdtTxt.setText(additonalData.optString("AddressLine1"));
            addressLine2EdtTxt.setText(additonalData.optString("AddressLine2"));
            countryEdtTxt.setText(additonalData.optString("Country"));
            stateEdtTxt.setText(additonalData.optString("State"));
            cityEdtTxt.setText(additonalData.optString("City"));
        }
    }

    public void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }

    }
}
