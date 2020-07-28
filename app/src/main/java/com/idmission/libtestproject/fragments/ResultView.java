package com.idmission.libtestproject.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.Response;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;

import java.io.Serializable;
import java.util.Map;

public class ResultView extends Fragment implements ImageProcessingResponseListener {

    private LinearLayout linearLayoutTop;
    private Map<String, String> resultData;
    private Button buttonShowRawData, buttonDone;
    private EVolvApp eVolvApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.result_view, container, false);

        eVolvApp = (EVolvApp) getActivity().getApplicationContext();
        linearLayoutTop = (LinearLayout) view.findViewById(R.id.linear_layout_top);
        buttonShowRawData = (Button) view.findViewById(R.id.button_show_raw_data);
        buttonDone= (Button) view.findViewById(R.id.done_button);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LinearLayout linearLayoutParent = null;
        if (getArguments() != null) {
            resultData = (Map<String, String>) getArguments().getSerializable(FinalSteps.RESULT_DATA);
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
                linearLayoutKey.setPadding(10, 30, 10, 30);
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
                linearLayoutValue.setPadding(10, 30, 10, 30);
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

        }

        buttonShowRawData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                ResultRawData resultRawData = new ResultRawData();
                Bundle bundle = new Bundle();
                bundle.putSerializable(FinalSteps.RESULT_DATA, (Serializable) resultData);
                resultRawData.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.flContent, resultRawData).addToBackStack(null).commit();
            }
        });
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageProcessingSDK.getInstance().setImageProcessingResponseListener(ResultView.this);
//                ImageProcessingSDK.getInstance().deleteData();

                clearBackStack();

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
        super.onActivityCreated(savedInstanceState);
    }

    private void clearBackStack() {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
    public void onExecuteCustomProductCall(Map<String, String> resultMap, Response response) {

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
