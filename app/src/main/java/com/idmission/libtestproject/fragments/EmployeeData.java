package com.idmission.libtestproject.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.Response;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class EmployeeData extends Fragment implements ImageProcessingResponseListener {

    private Button buttonBack, buttonNext;
    private EditText empCodeEdtText, empLoginIdEdtText, empEmailEdtTxt, empCompanyIdEdtTxt, empDeptEdtTxt, empNameEdtTxt, empMobNoEdtTxt,
            empCountryEdtTxt, empTypeEdtTxt, empAddress1EdtTxt, empAddress2EdtTxt, empZipCodeEdtTxt, spouseNameEdtTxt, noOfChildrenEdtTxt, stateEdtTxt, cityEdtTxt, maritalStatusEdtTxt;
    private static final String LOGTAG = "EmployeeData";
    private Spinner spinnerGender;
    private static final String[] EMPLOYEE_GENDER_TYPE = {"M", "F"};
    private EVolvApp eVolvApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.employee_data, container, false);

        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);
        empCodeEdtText = (EditText) view.findViewById(R.id.edit_text_emp_code);
        empLoginIdEdtText = (EditText) view.findViewById(R.id.edit_text_emp_login_id);
        empEmailEdtTxt = (EditText) view.findViewById(R.id.edit_text_emp_email);
        empCompanyIdEdtTxt = (EditText) view.findViewById(R.id.edit_text_emp_company_id);
        empDeptEdtTxt = (EditText) view.findViewById(R.id.edit_text_emp_dept);
        empNameEdtTxt = (EditText) view.findViewById(R.id.edit_text_emp_name);
        empMobNoEdtTxt = (EditText) view.findViewById(R.id.edit_text_emp_mob_no);
        empCountryEdtTxt = (EditText) view.findViewById(R.id.edit_text_emp_country);
        empTypeEdtTxt = (EditText) view.findViewById(R.id.edit_text_emp_type);
        empAddress1EdtTxt = (EditText) view.findViewById(R.id.edit_text_emp_address_1);
        empAddress2EdtTxt = (EditText) view.findViewById(R.id.edit_text_emp_address_2);
        empZipCodeEdtTxt = (EditText) view.findViewById(R.id.edit_text_zip_code);
        spouseNameEdtTxt = (EditText) view.findViewById(R.id.edit_text_spouse_name);
        noOfChildrenEdtTxt = (EditText) view.findViewById(R.id.edit_text_no_of_children);
        spinnerGender=(Spinner)view.findViewById(R.id.spinner_gender);
        stateEdtTxt = (EditText) view.findViewById(R.id.edit_text_emp_state);
        cityEdtTxt = (EditText) view.findViewById(R.id.edit_text_emp_city);
        maritalStatusEdtTxt = (EditText) view.findViewById(R.id.edit_text_marital_status);

        eVolvApp = (EVolvApp) getActivity().getApplicationContext();

        spinnerGender.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, EMPLOYEE_GENDER_TYPE));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((IDValidationFaceMatch.listAdditionalFeatures.size() - IDValidationFaceMatch.viewPager.getCurrentItem()) == 1) {
                    eVolvApp.setEmpCode(empCodeEdtText.getText().toString());
                    eVolvApp.setJsonObjEmployeeData(getEmployeeDataJSON());

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

    @Override
    public void onPause() {
        super.onPause();
        eVolvApp.setEmpCode(empCodeEdtText.getText().toString());
        eVolvApp.setJsonObjEmployeeData(getEmployeeDataJSON());
    }

    private JSONObject getEmployeeDataJSON() {
        String empCode = empCodeEdtText.getText().toString().trim();
        String empLoginId = empLoginIdEdtText.getText().toString().trim();
        String empEmailId = empEmailEdtTxt.getText().toString().trim();
        String empCompany = empCompanyIdEdtTxt.getText().toString().trim();
        String empDept = empDeptEdtTxt.getText().toString().trim();
        String empName = empNameEdtTxt.getText().toString().trim();
        String empMobNumber = empMobNoEdtTxt.getText().toString().trim();
        String empCountry = empCountryEdtTxt.getText().toString().trim();
        String empType = empTypeEdtTxt.getText().toString().trim();
        String empAddress1 = empAddress1EdtTxt.getText().toString().trim();
        String empAddress2 = empAddress2EdtTxt.getText().toString().trim();
        String empZipCode = empZipCodeEdtTxt.getText().toString().trim();
        String spouseName = spouseNameEdtTxt.getText().toString().trim();
        String noOfChildren = noOfChildrenEdtTxt.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String state = stateEdtTxt.getText().toString().trim();
        String city = cityEdtTxt.getText().toString().trim();
        String maritalStatus = maritalStatusEdtTxt.getText().toString().trim();

        JSONObject jObject = new JSONObject();
        try {
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

            if (!StringUtil.isEmpty(empName)) {
                jObject.put("Employee_Name", empName);
            }

            if (!StringUtil.isEmpty(empMobNumber)) {
                jObject.put("Employee_MobileNumber", empMobNumber);
            }

            if (!StringUtil.isEmpty(empCountry)) {
                jObject.put("Employee_Country", empCountry);
            }

            if (!StringUtil.isEmpty(empType)) {
                jObject.put("Employee_Type", empType);
            }

            if (!StringUtil.isEmpty(empAddress1)) {
                jObject.put("Employee_AddressLine1", empAddress1);
            }

            if (!StringUtil.isEmpty(empAddress2)) {
                jObject.put("Employee_AddressLine2", empAddress2);
            }

            if (!StringUtil.isEmpty(empAddress1)) {
                jObject.put("Employee_AddresLine1", empAddress1);
            }

            if (!StringUtil.isEmpty(empAddress2)) {
                jObject.put("Employee_AddresLine2", empAddress2);
            }

            if (!StringUtil.isEmpty(empZipCode)) {
                jObject.put("Employee_PostalCode", empZipCode);
            }

            if (!StringUtil.isEmpty(empZipCode)) {
                jObject.put("Employee_ZipCode", empZipCode);
            }

            if (!StringUtil.isEmpty(spouseName)) {
                jObject.put("Spouse_Name", spouseName);
            }

            if (!StringUtil.isEmpty(noOfChildren)) {
                jObject.put("Number_of_Children", noOfChildren);
            }

            if (!StringUtil.isEmpty(gender)) {
                jObject.put("Employee_Gender", gender);
            }

            if (!StringUtil.isEmpty(gender)) {
                jObject.put("Gender", gender);
            }

            if (!StringUtil.isEmpty(state)) {
                jObject.put("Employee_State", state);
            }

            if (!StringUtil.isEmpty(city)) {
                jObject.put("Employee_City", city);
            }

            if (!StringUtil.isEmpty(maritalStatus)) {
                jObject.put("Marital_Status", maritalStatus);
            }
        } catch (JSONException exc) {
            Log.d(LOGTAG, "EmployeeDataJSON Exc : " + exc);
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
