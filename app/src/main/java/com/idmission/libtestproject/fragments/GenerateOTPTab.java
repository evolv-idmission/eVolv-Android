package com.idmission.libtestproject.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;

/**
 * Created by dipenp on 19/10/18.
 */

public class GenerateOTPTab extends Fragment {
    private static final String LOGTAG = "GenerateOTPTab";

    private Button buttonBack, buttonNext;
    private EditText emailIdEdtText, phoneNumberEdtText;
    private Spinner notificationTypeSpinner;

    private static final String[] OTP_NOTIFICATION_TYPE = {"EMAIL", "SMS", "BOTH"};

    private EVolvApp eVolvApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.generate_otp, container, false);

        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);
        emailIdEdtText = (EditText) view.findViewById(R.id.edit_text_email_id);
        phoneNumberEdtText = (EditText) view.findViewById(R.id.edit_text_mobile_number);
        notificationTypeSpinner = (Spinner) view.findViewById(R.id.spinner_notification_type);

        notificationTypeSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, OTP_NOTIFICATION_TYPE));

        eVolvApp = (EVolvApp) getActivity().getApplicationContext();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        eVolvApp.setOtpEmailId(emailIdEdtText.getText().toString().trim());
        eVolvApp.setOtpMobileNumber(phoneNumberEdtText.getText().toString().trim());
        eVolvApp.setOtpNotificationType(notificationTypeSpinner.getSelectedItem().toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
}