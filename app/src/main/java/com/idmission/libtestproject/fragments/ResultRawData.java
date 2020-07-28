package com.idmission.libtestproject.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.Response;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.FileUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import java.util.Map;

public class ResultRawData extends Fragment implements ImageProcessingResponseListener {
    private Map<String, String> resultData;
    private Button buttonClearData,buttonBack;
    private TextView textViewRequest, textViewResponse;
    private EVolvApp eVolvApp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.result_raw_data, container, false);

        eVolvApp = (EVolvApp) (getActivity().getApplicationContext());

        buttonBack = (Button) view.findViewById(R.id.button_back);
        buttonClearData = (Button) view.findViewById(R.id.button_clear_data);
        textViewRequest = (TextView) view.findViewById(R.id.text_view_request);
        textViewResponse = (TextView) view.findViewById(R.id.text_view_response);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        if (getArguments() != null) {
            resultData = (Map<String, String>) getArguments().getSerializable(FinalSteps.RESULT_DATA);
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        buttonClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageProcessingSDK.getInstance().setImageProcessingResponseListener(ResultRawData.this);
                ImageProcessingSDK.getInstance().deleteData();
                eVolvApp.clearEvolvImageDirectory();
                eVolvApp.clearEvolvData();

                clearBackStack();

                FragmentManager fragmentManager = getFragmentManager();
                AccountSetup accountSetup = new AccountSetup();
                fragmentManager.beginTransaction().replace(R.id.flContent, accountSetup).commit();
            }
        });


        //Display request xml on textview
        String reqPath = ImageProcessingSDK.getInstance().lastRequestXMLWithoutImage();
        if (!StringUtil.isEmpty(reqPath)) {
            String request = FileUtils.getStringFromFile(reqPath);
            textViewRequest.setText(request);
        }
        //Display response xml on textview
        String respPath = ImageProcessingSDK.getInstance().lastResponseXMLWithoutImage();
        if (!StringUtil.isEmpty(respPath)) {
            String response = FileUtils.getStringFromFile(respPath);
            textViewResponse.setText(response);
        }

        super.onActivityCreated(savedInstanceState);
    }

    private void clearBackStack() {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.download_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_download) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                FileUtils.createDir(eVolvApp.getExternalDirectoryPath() + Constants.EVOLVE_BASE_FOLDER_PATH);
                FileUtils.createDir(eVolvApp.getExternalDirectoryPath() + Constants.EVOLVE_REQ_RESP_FOLDER_PATH);

                String reqPath = ImageProcessingSDK.getInstance().lastRequestXML();
                if (!StringUtil.isEmpty(reqPath)) {
                    String request = FileUtils.getStringFromFile(reqPath);
                    FileUtils.writeStringToFile(eVolvApp.getRequestXMLPath(), request);
                }

                String respPath = ImageProcessingSDK.getInstance().lastResponseXML();
                if (!StringUtil.isEmpty(respPath)) {
                    String response = FileUtils.getStringFromFile(respPath);
                    FileUtils.writeStringToFile(eVolvApp.getResponseXMLPath(), response);
                }

                //showErrorMessage(getView(), "Downloaded on " + Constants.EVOLVE_REQ_RESP_FOLDER_PATH);
                showErrorMessage(getView(), getString(R.string.download_on)+ " " + Constants.EVOLVE_REQ_RESP_FOLDER_PATH);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
        return true;
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
