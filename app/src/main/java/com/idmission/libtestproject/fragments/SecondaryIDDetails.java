package com.idmission.libtestproject.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.idmission.client.IDImageType;
import com.idmission.client.IdType;
import com.idmission.client.ImageProcessingResponseListener;
import com.idmission.client.ImageProcessingSDK;
import com.idmission.client.ImageType;
import com.idmission.client.Response;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.activity.NavigationActivity;
import com.idmission.libtestproject.adapter.SpinnerAdapterForPair;
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.classes.UIConfigConstants;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SecondaryIDDetails extends Fragment implements ImageProcessingResponseListener {

    private LinearLayout idValidationFilter, faceMatchFilter, idValidationFilter_Secondary;
    private Button buttonBack, buttonNext;
    private EditText countryEdtTxt, stateEdtTxt, stateEdtTxt_secondary;
    private Spinner spinnerIDType, faceImageTypeSpinner, idImageTypeSpinner, spinnerIDType_Secondary;
    private String currentService;
    private RelativeLayout relativeLayoutState, relativeLayoutState_secondary, relativeLayoutIDType, relativeLayoutIDCountry;
    private EVolvApp eVolvApp;
    public static String SKIP_FEATURES_LIST = "SKIP_FEATURES_LIST";
    private AppCompatAutoCompleteTextView autoCompleteTextIDCountry, autoCompleteTextIDCountry_Secondary;
    private LinkedHashMap<String, String> listCountry = new LinkedHashMap<>();
    private View lineView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.id_details, container, false);

        eVolvApp = (EVolvApp) getActivity().getApplicationContext();
        NavigationActivity.toolbar.setTitle(eVolvApp.getCurrentServiceName());
        currentService = eVolvApp.getCurrentService();
        //currentService=eVolvApp.getCurrentServiceName();

        relativeLayoutIDType = (RelativeLayout) view.findViewById(R.id.relative_layout_id_type);
        relativeLayoutIDCountry = (RelativeLayout) view.findViewById(R.id.relative_layout_id_country);
        relativeLayoutState = (RelativeLayout) view.findViewById(R.id.relative_layout_state);
        relativeLayoutState_secondary = (RelativeLayout) view.findViewById(R.id.relative_layout_state_secondary);
        idValidationFilter = (LinearLayout) view.findViewById(R.id.id_validation_filter);
        idValidationFilter_Secondary = (LinearLayout) view.findViewById(R.id.secondary_id_validation_filter);
        spinnerIDType = (Spinner) view.findViewById(R.id.spinner_id_type);
        spinnerIDType_Secondary = (Spinner) view.findViewById(R.id.spinner_id_type_secondary);
        autoCompleteTextIDCountry = (AppCompatAutoCompleteTextView) view.findViewById(R.id.spinner_id_country);
        autoCompleteTextIDCountry_Secondary = (AppCompatAutoCompleteTextView) view.findViewById(R.id.spinner_id_country_secondary);
        countryEdtTxt = (EditText) view.findViewById(R.id.edit_text_country);
        stateEdtTxt = (EditText) view.findViewById(R.id.edit_text_state);
        stateEdtTxt_secondary = (EditText) view.findViewById(R.id.edit_text_state_secondary);
        lineView = (View) view.findViewById(R.id.line_view);

        faceMatchFilter = (LinearLayout) view.findViewById(R.id.face_match_filter);
        //faceImageTypeSpinner = (Spinner) view.findViewById(R.id.faceImageType_ET);
        idImageTypeSpinner = (Spinner) view.findViewById(R.id.idImageType_ET);

        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);

        stateEdtTxt.setHint(getString(R.string.enter_iso_code));
        stateEdtTxt_secondary.setHint(getString(R.string.enter_iso_code));

        //spinnerIDType.setAdapter(new ArrayAdapter<IdType>(getActivity(), android.R.layout.simple_dropdown_item_1line, IdType.values()));

        ArrayList<Pair<String, String>> idTypeList = new ArrayList<Pair<String, String>>();
        idTypeList.add(new Pair<String, String>(getString(R.string.PP), "PASSPORT"));
        idTypeList.add(new Pair<String, String>(getString(R.string.NID), "NATIONAL_ID"));
        idTypeList.add(new Pair<String, String>(getString(R.string.RID), "RESIDENCE_CARD"));
        idTypeList.add(new Pair<String, String>(getString(R.string.DL), "DRIVERS_LICENSE"));
        idTypeList.add(new Pair<String, String>(getString(R.string.PID), "PHOTO_ID_CARD"));
        idTypeList.add(new Pair<String, String>(getString(R.string.VID), "VOTER_ID_CARD"));
        idTypeList.add(new Pair<String, String>(getString(R.string.TID), "TAX_ID_CARD"));
        idTypeList.add(new Pair<String, String>(getString(R.string.WV), "WORK_VISA_PERMIT"));
        idTypeList.add(new Pair<String, String>(getString(R.string.SV), "STUDENT_VISA_PERMIT"));
        idTypeList.add(new Pair<String, String>(getString(R.string.GID), "MILITARY_POLICE_GOVERNMENT_ID"));
        idTypeList.add(new Pair<String, String>(getString(R.string.BID), "BOAT_SHIP_ID_CARD"));
        idTypeList.add(new Pair<String, String>(getString(R.string.IDM), "IDM"));
        //idTypeList.add(new Pair<String, String>(getString(R.string.UBL),"UTILITY_BILL"));
        idTypeList.add(new Pair<String, String>(getString(R.string.OTH), "OTHERS"));
        SpinnerAdapterForPair idTypeAdapter = new SpinnerAdapterForPair(
                getActivity(), android.R.layout.simple_list_item_1,
                idTypeList);
        spinnerIDType.setAdapter(idTypeAdapter);
        spinnerIDType_Secondary.setAdapter(idTypeAdapter);

        // ArrayList<String> country_list = bindIDCountrySpinner(getIDCountry());
        autoCompleteTextIDCountry.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, bindIDCountrySpinner(getIDCountry())));
        // spinnerIDCountry.setSelection(country_list.indexOf("United States of America"));
        autoCompleteTextIDCountry.setText("United States of America");

        // ArrayList<String> country_list = bindIDCountrySpinner(getIDCountry());
        autoCompleteTextIDCountry_Secondary.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, bindIDCountrySpinner(getIDCountry())));
        // spinnerIDCountry.setSelection(country_list.indexOf("United States of America"));
        autoCompleteTextIDCountry_Secondary.setText("United States of America");

        // faceImageTypeSpinner.setAdapter(new ArrayAdapter<FaceImageType>(getActivity(), android.R.layout.simple_dropdown_item_1line, FaceImageType.values()));
        idImageTypeSpinner.setAdapter(new ArrayAdapter<IDImageType>(getActivity(), android.R.layout.simple_dropdown_item_1line, IDImageType.values()));

        if (currentService.equals(Constants.SERVICE_ID_VALIDATION)) {
            faceMatchFilter.setVisibility(View.GONE);
            idValidationFilter.setVisibility(View.VISIBLE);
        } else if (currentService.equals(Constants.SERVICE_FACE_MATCH)) {
            faceMatchFilter.setVisibility(View.VISIBLE);
            idValidationFilter.setVisibility(View.GONE);
        } else if (currentService.equals(Constants.SERVICE_ID_VALIDATION_FACE_MATCH)) {
            faceMatchFilter.setVisibility(View.VISIBLE);
            idValidationFilter.setVisibility(View.VISIBLE);
        } else if (currentService.equals(Constants.SERVICE_EMPLOYEE_VERIFICATION)) {
            faceMatchFilter.setVisibility(View.VISIBLE);
            idValidationFilter.setVisibility(View.GONE);
        } else if (currentService.equals(Constants.SERVICE_FACE_MATCH_EMPLOYEE_ENROLLMENT)) {
            faceMatchFilter.setVisibility(View.VISIBLE);
            idValidationFilter.setVisibility(View.GONE);
        } else if (currentService.equals(Constants.SERVICE_EMPLOYEE_OVERRIDE)) {
            faceMatchFilter.setVisibility(View.VISIBLE);
            idValidationFilter.setVisibility(View.GONE);
        } else if (currentService.equals(Constants.SERVICE_CUSTOMER_OVERRIDE)) {
            faceMatchFilter.setVisibility(View.VISIBLE);
            idValidationFilter.setVisibility(View.GONE);
        } else if (currentService.equals(Constants.SERVICE_ID_VALIDATION_CUSTOMER_ENROLLMENT)) {
            faceMatchFilter.setVisibility(View.GONE);
            idValidationFilter.setVisibility(View.VISIBLE);
        } else if (currentService.equals(Constants.SERVICE_ID_VALIDATION_EMPLOYEE_ENROLLMENT)) {
            faceMatchFilter.setVisibility(View.GONE);
            idValidationFilter.setVisibility(View.VISIBLE);
        } else if (currentService.equals(Constants.SERVICE_ID_FACE_EMPLOYEE_ENROLLMENT)) {
            faceMatchFilter.setVisibility(View.VISIBLE);
            idValidationFilter.setVisibility(View.VISIBLE);
        } else if (currentService.equals(Constants.SERVICE_CUSTOMER_VERIFICATION)) {
            faceMatchFilter.setVisibility(View.VISIBLE);
            idValidationFilter.setVisibility(View.GONE);
        } else if (currentService.equals(Constants.SERVICE_ID_FACE_VIDEO_RECORDING)) {
            faceMatchFilter.setVisibility(View.GONE);
            idValidationFilter.setVisibility(View.VISIBLE);
        } else if (currentService.equals(Constants.SERVICE_ID_FACE_VIDEO_CUSTOMER_ENROLLMENT)) {
            faceMatchFilter.setVisibility(View.GONE);
            idValidationFilter.setVisibility(View.VISIBLE);
        } else if (currentService.equals(Constants.SERVICE_ID_FACE_VIDEO_EMPLOYEE_ENROLLMENT)) {
            faceMatchFilter.setVisibility(View.GONE);
            idValidationFilter.setVisibility(View.VISIBLE);
        }

        ArrayList<String> additionalFeatureList = eVolvApp.getListAdditionalFeatures();

        //Check if Secondary ID Capture feature is present in feature list, if not hide Secondary ID Capture Layout

        if (null != additionalFeatureList && additionalFeatureList.contains(Constants.FEATURE_SECONDARY_ID_CAPTURE_FRONT)) {
            idValidationFilter_Secondary.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);

            relativeLayoutIDType.setVisibility(View.GONE);
            relativeLayoutIDCountry.setVisibility(View.GONE);
            relativeLayoutState.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
        } else {
            idValidationFilter_Secondary.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
        }

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
//
//        spinnerIDCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                String id_type = ((IdType)spinnerIDType.getSelectedItem()).name();
//                //String country = spinnerIDCountry.getSelectedItem().toString();
//                String country = spinnerIDCountry.getText().toString();
//
//                if (id_type.equalsIgnoreCase(IdType.DRIVERS_LICENSE.name()) && (country.equalsIgnoreCase("United States of America") || country.equalsIgnoreCase("Australia") || country.equalsIgnoreCase("Canada"))) {
//                    relativeLayoutState.setVisibility(View.VISIBLE);
//                } else {
//                    stateEdtTxt.setText("");
//                    relativeLayoutState.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        autoCompleteTextIDCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // String id_type = ((IdType) spinnerIDType.getSelectedItem()).name();
//                //String country = spinnerIDCountry.getSelectedItem().toString();
//
//                String id_type = ((Pair)spinnerIDType.getSelectedItem()).second.toString();
//                String country = autoCompleteTextIDCountry.getAdapter().getItem(position).toString();
//
//                if (id_type.equalsIgnoreCase(IdType.DRIVERS_LICENSE.name()) && (country.equalsIgnoreCase("United States of America") || country.equalsIgnoreCase("Australia") || country.equalsIgnoreCase("Canada"))) {
//                    relativeLayoutState.setVisibility(View.VISIBLE);
//                } else {
//                    stateEdtTxt.setText("");
//                    relativeLayoutState.setVisibility(View.GONE);
//                }
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
//            }
//        });

        autoCompleteTextIDCountry_Secondary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String id_type = ((Pair) spinnerIDType_Secondary.getSelectedItem()).second.toString();
                String country = autoCompleteTextIDCountry_Secondary.getAdapter().getItem(position).toString();

                if (id_type.equalsIgnoreCase(IdType.DRIVERS_LICENSE.name()) && (country.equalsIgnoreCase("United States of America") || country.equalsIgnoreCase("Australia") || country.equalsIgnoreCase("Canada"))) {
                    relativeLayoutState_secondary.setVisibility(View.VISIBLE);
                } else {
                    stateEdtTxt_secondary.setText("");
                    relativeLayoutState_secondary.setVisibility(View.GONE);
                }
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

            }
        });


//        spinnerIDType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                //String id_type = ((IdType)spinnerIDType.getSelectedItem()).name();
//                String id_type = ((Pair)spinnerIDType.getSelectedItem()).second.toString();
//                // String country = spinnerIDCountry.getSelectedItem().toString();
//                String country = autoCompleteTextIDCountry.getText().toString();
//
//                if (id_type.equalsIgnoreCase(IdType.DRIVERS_LICENSE.name()) && (country.equalsIgnoreCase("United States of America") || country.equalsIgnoreCase("Australia") || country.equalsIgnoreCase("Canada"))) {
//                    relativeLayoutState.setVisibility(View.VISIBLE);
//                } else {
//                    relativeLayoutState.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        spinnerIDType_Secondary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //String id_type = ((IdType)spinnerIDType_Secondary.getSelectedItem()).name();
                String id_type = ((Pair) spinnerIDType_Secondary.getSelectedItem()).second.toString();
                // String country = spinnerIDCountry.getSelectedItem().toString();
                String country = autoCompleteTextIDCountry_Secondary.getText().toString();

                if (id_type.equalsIgnoreCase(IdType.DRIVERS_LICENSE.name()) && (country.equalsIgnoreCase("United States of America") || country.equalsIgnoreCase("Australia") || country.equalsIgnoreCase("Canada"))) {
                    relativeLayoutState_secondary.setVisibility(View.VISIBLE);
                } else {
                    relativeLayoutState_secondary.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String country = getCountryIDCode(getIDCountry(), autoCompleteTextIDCountry.getText().toString());
//                String state = stateEdtTxt.getText().toString();
//                String value=((Pair)spinnerIDType.getSelectedItem()).second.toString();
//                IdType idType=IdType.valueOf(value);
//
//                String idImageType = ((IDImageType) idImageTypeSpinner.getSelectedItem()).getIDImageType().toString();
//
//                eVolvApp.setCountry(country);
//                eVolvApp.setState(state);
//                eVolvApp.setIdType(idType);
//                eVolvApp.setIdImageType(idImageType);

                //Secondary Id Capture Changes
                String countrySecondary = getCountryIDCode(getIDCountry(), autoCompleteTextIDCountry_Secondary.getText().toString());
                String stateSecondary = stateEdtTxt_secondary.getText().toString();
                String valueSecondary = ((Pair) spinnerIDType_Secondary.getSelectedItem()).second.toString();
                IdType idTypeSecondary = IdType.valueOf(valueSecondary);

                eVolvApp.setSecondaryCountry(countrySecondary);
                eVolvApp.setSecondaryState(stateSecondary);
                eVolvApp.setSecondaryIdType(idTypeSecondary);

//                JSONObject commonFunctioObject = new JSONObject();
//                try {
//                    commonFunctioObject.put(UIConfigConstants.ID_COUNTRY, eVolvApp.getCountry());
//                    commonFunctioObject.put(UIConfigConstants.ID_STATE, eVolvApp.getState());
//                    if (eVolvApp.getSelectIdType() != null) {
//                        commonFunctioObject.put(UIConfigConstants.ID_TYPE, IdType.valueOf(eVolvApp.getSelectIdType().toString()).getIdType());
//                    }
//                } catch (Exception e) {
//                }
//
//                ImageProcessingSDK.getInstance().setImageProcessingResponseListener(SecondaryIDDetails.this);
//                ImageProcessingSDK.getInstance().getAutoFillFieldInformation(getActivity(), commonFunctioObject);

                hideKeyboard(getActivity(), view);

                if ((IDValidationFaceMatch.listAdditionalFeatures.size() - IDValidationFaceMatch.viewPager.getCurrentItem()) == 1) {
                    FragmentManager fragmentManager = getParentFragment().getFragmentManager();
                    FinalSteps keyView = new FinalSteps();
                    fragmentManager.beginTransaction().replace(R.id.flContent, keyView).addToBackStack(null).commit();
                } else {
                    IDValidationFaceMatch.viewPager.setCurrentItem(IDValidationFaceMatch.viewPager.getCurrentItem() + 1);
                }
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  IDValidationFaceMatch.viewPager.setCurrentItem(IDValidationFaceMatch.viewPager.getCurrentItem() - 1);
//                FragmentManager fragmentManager = getFragmentManager();
//                ProcessFlow processFlow = new ProcessFlow();
//                fragmentManager.beginTransaction().replace(R.id.flContent, processFlow).addToBackStack(null).commit();
//                NavigationActivity.toolbar.setTitle(R.string.id_validation);
                //getActivity().getSupportFragmentManager().popBackStack();

                if ((IDValidationFaceMatch.listAdditionalFeatures.size() - IDValidationFaceMatch.viewPager.getCurrentItem()) == IDValidationFaceMatch.listAdditionalFeatures.size()) {
//                    FragmentManager fragmentManager = getParentFragment().getFragmentManager();
//                    IDDetails processFlow = new IDDetails();
//                    fragmentManager.beginTransaction().replace(R.id.flContent, processFlow).addToBackStack(null).commit();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();
                } else {
                    IDValidationFaceMatch.viewPager.setCurrentItem(IDValidationFaceMatch.viewPager.getCurrentItem() - 1);
                }

                hideKeyboard(getActivity(), view);
            }
        });

        IDValidationFaceMatch.viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    String selectTab = IDValidationFaceMatch.tabLayout.getTabAt(IDValidationFaceMatch.viewPager.getCurrentItem()).getText().toString();
                    if (selectTab.equalsIgnoreCase(Constants.FEATURE_SECONDARY_ID_DETAILS)) {
                        //Secondary Id Capture Changes
                        String countrySecondary = getCountryIDCode(getIDCountry(), autoCompleteTextIDCountry_Secondary.getText().toString());
                        String stateSecondary = stateEdtTxt_secondary.getText().toString();
                        String valueSecondary = ((Pair) spinnerIDType_Secondary.getSelectedItem()).second.toString();
                        IdType idTypeSecondary = IdType.valueOf(valueSecondary);

                        eVolvApp.setSecondaryCountry(countrySecondary);
                        eVolvApp.setSecondaryState(stateSecondary);
                        eVolvApp.setSecondaryIdType(idTypeSecondary);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private ArrayList<String> getSkipFeaturesList() {
        ArrayList<String> featuresForServiceList = new ArrayList<>();
        // String id_type = ((IdType)spinnerIDType.getSelectedItem()).name();
        // String id_type = ((Pair)spinnerIDType.getSelectedItem()).second.toString();

        String id_type_scondary = ((Pair) spinnerIDType_Secondary.getSelectedItem()).second.toString();
        featuresForServiceList.addAll(eVolvApp.getListAdditionalFeatures());

        //  if(id_type.equalsIgnoreCase(IdType.PASSPORT.name())) {
        //    featuresForServiceList.remove(Constants.FEATURE_ID_CAPTURE_BACK);
        //}

        if (id_type_scondary.equalsIgnoreCase(IdType.PASSPORT.name())) {
            featuresForServiceList.remove(Constants.FEATURE_SECONDARY_ID_CAPTURE_BACK);
        }

//        if (id_type.equalsIgnoreCase(IdType.PASSPORT.name())) {
//            featuresForServiceList.addAll(eVolvApp.getListAdditionalFeatures());
//            featuresForServiceList.remove(Constants.FEATURE_ID_CAPTURE_BACK);
//        } else {
//            featuresForServiceList.addAll(eVolvApp.getListAdditionalFeatures());
//        }

//        if (id_type_scondary.equalsIgnoreCase(IdType.PASSPORT.name())) {
//            featuresForServiceList.addAll(eVolvApp.getListAdditionalFeatures());
//            featuresForServiceList.remove(Constants.FEATURE_SECONDARY_ID_CAPTURE_BACK);
//        } else {
//            featuresForServiceList.addAll(eVolvApp.getListAdditionalFeatures());
//        }

        return featuresForServiceList;
    }

    private ArrayList<String> bindIDCountrySpinner(LinkedHashMap<String, String> listIDCountry) {
        ArrayList<String> listCountry = new ArrayList<String>();
        for (Map.Entry<String, String> entry : listIDCountry.entrySet()) {
            String value = entry.getValue();
            listCountry.add(value);
        }
        return listCountry;
    }

    private String getCountryIDCode(LinkedHashMap<String, String> listIDCountry, String country) {
        for (Map.Entry<String, String> entry : listIDCountry.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(country)) {
                return entry.getKey();
            }
        }
        return "";
    }

    private LinkedHashMap<String, String> getIDCountry() {

        if (listCountry.isEmpty()) {
//            listCountry.put("AFG", "AFGHANISTAN");
//            listCountry.put("ALA", "ALAND ISLANDS");
//            listCountry.put("ALB", "ALBANIA");
//            listCountry.put("DZA", "ALGERIA");
//            listCountry.put("ALL", "ALL");
//            listCountry.put("ASM", "AMERICAN SAMOA");
//            listCountry.put("AND", "ANDORRA");
//            listCountry.put("AGO", "ANGOLA");
//            listCountry.put("AIA", "ANGUILLA");
//            listCountry.put("ATA", "ANTARCTICA");
//            listCountry.put("ATG", "ANTIGUA AND BARBUDA");
//            listCountry.put("ARG", "ARGENTINA");
//            listCountry.put("ARM", "ARMENIA");
//            listCountry.put("ABW", "ARUBA");
//            listCountry.put("AUS", "AUSTRALIA");
//            listCountry.put("AUT", "AUSTRIA");
//            listCountry.put("AZE", "AZERBAIJAN");
//            listCountry.put("BHS", "BAHAMAS");
//            listCountry.put("BHR", "BAHRAIN");
//            listCountry.put("BGD", "BANGLADESH");
//            listCountry.put("BRB", "BARBADOS");
//            listCountry.put("BLR", "BELARUS");
//            listCountry.put("BEL", "BELGIUM");
//            listCountry.put("BLZ", "BELIZE");
//            listCountry.put("BEN", "BENIN");
//            listCountry.put("BMU", "BERMUDA");
//            listCountry.put("BTN", "BHUTAN");
//            listCountry.put("BOL", "BOLIVIA");
//            listCountry.put("BIH", "BOSNIA AND HERZEGOWINA");
//            listCountry.put("BWA", "BOTSWANA");
//            listCountry.put("BVT", "BOUVET ISLAND");
//            listCountry.put("BRA", "BRAZIL");
//            listCountry.put("IOT", "BRITISH INDIAN OCEAN TERRITORY");
//            listCountry.put("BRN", "BRUNEI DARUSSALAM");
//            listCountry.put("BGR", "BULGARIA");
//            listCountry.put("BFA", "BURKINA FASO");
//            listCountry.put("BDI", "BURUNDI");
//            listCountry.put("KHM", "CAMBODIA");
//            listCountry.put("CMR", "CAMEROON");
//            listCountry.put("CAN", "CANADA");
//            listCountry.put("CPV", "CAPE VERDE");
//            listCountry.put("BES", "CARIBBEAN NETHERLANDS");
//            listCountry.put("CYM", "CAYMAN ISLANDS");
//            listCountry.put("CAF", "CENTRAL AFRICAN REPUBLIC");
//            listCountry.put("TCD", "CHAD");
//            listCountry.put("CHL", "CHILE");
//            listCountry.put("CHN", "CHINA");
//            listCountry.put("CXR", "CHRISTMAS ISLAND");
//            listCountry.put("CCK", "COCOS (KEELING) ISLANDS");
//            listCountry.put("COL", "COLOMBIA");
//            listCountry.put("COM", "COMOROS");
//            listCountry.put("COG", "CONGO");
//            listCountry.put("COD", "CONGO THE DRC");
//            listCountry.put("COK", "COOK ISLANDS");
//            listCountry.put("CRI", "COSTA RICA");
//            listCountry.put("CIV", "COTE D'IVOIRE");
//            listCountry.put("HRV", "CROATIA (local name: Hrvatska)");
//            listCountry.put("CUB", "CUBA");
//            listCountry.put("CUW", "CURAÇAO");
//            listCountry.put("CYP", "CYPRUS");
//            listCountry.put("CZE", "CZECH REPUBLIC");
//            listCountry.put("DNK", "DENMARK");
//            listCountry.put("DJI", "DJIBOUTI");
//            listCountry.put("DMA", "DOMINICA");
//            listCountry.put("DOM", "DOMINICAN REPUBLIC");
//            listCountry.put("TMP", "EAST TIMOR");
//            listCountry.put("ECU", "ECUADOR");
//            listCountry.put("EGY", "EGYPT");
//            listCountry.put("SLV", "EL SALVADOR");
//            listCountry.put("GNQ", "EQUATORIAL GUINEA");
//            listCountry.put("ERI", "ERITREA");
//            listCountry.put("EST", "ESTONIA");
//            listCountry.put("ETH", "ETHIOPIA");
//            listCountry.put("FLK", "FALKLAND ISLANDS (MALVINAS)");
//            listCountry.put("FRO", "FAROE ISLANDS");
//            listCountry.put("FJI", "FIJI");
//            listCountry.put("FIN", "FINLAND");
//            listCountry.put("FRA", "FRANCE");
//            listCountry.put("FXX", "FRANCE METROPOLITAN");
//            listCountry.put("GUF", "FRENCH GUIANA");
//            listCountry.put("PYF", "FRENCH POLYNESIA");
//            listCountry.put("ATF", "FRENCH SOUTHERN TERRITORIES");
//            listCountry.put("GAB", "GABON");
//            listCountry.put("GMB", "GAMBIA");
//            listCountry.put("GEO", "GEORGIA");
//            listCountry.put("DEU", "GERMANY");
//            listCountry.put("GHA", "GHANA");
//            listCountry.put("GIB", "GIBRALTAR");
//            listCountry.put("GRC", "GREECE");
//            listCountry.put("GRL", "GREENLAND");
//            listCountry.put("GRD", "GRENADA");
//            listCountry.put("GLP", "GUADELOUPE");
//            listCountry.put("GUM", "GUAM");
//            listCountry.put("GTM", "GUATEMALA");
//            listCountry.put("GGY", "GUERNSEY");
//            listCountry.put("GIN", "GUINEA");
//            listCountry.put("GNB", "GUINEA-BISSAU");
//            listCountry.put("GUY", "GUYANA");
//            listCountry.put("HTI", "HAITI");
//            listCountry.put("HMD", "HEARD AND MC DONALD ISLANDS");
//            listCountry.put("VAT", "HOLY SEE (VATICAN CITY STATE)");
//            listCountry.put("HND", "HONDURAS");
//            listCountry.put("HKG", "HONG KONG");
//            listCountry.put("HUN", "HUNGARY");
//            listCountry.put("ISL", "ICELAND");
//            listCountry.put("IND", "INDIA");
//            listCountry.put("IDN", "INDONESIA");
//            listCountry.put("IRN", "IRAN (ISLAMIC REPUBLIC OF)");
//            listCountry.put("IRQ", "IRAQ");
//            listCountry.put("IRL", "IRELAND");
//            listCountry.put("IMN", "ISLE OF MAN");
//            listCountry.put("ISR", "ISRAEL");
//            listCountry.put("ITA", "ITALY");
//            listCountry.put("JAM", "JAMAICA");
//            listCountry.put("JPN", "JAPAN");
//            listCountry.put("JEY", "JERSEY");
//            listCountry.put("JOR", "JORDAN");
//            listCountry.put("KAZ", "KAZAKHSTAN");
//            listCountry.put("KEN", "KENYA");
//            listCountry.put("KIR", "KIRIBATI");
//            listCountry.put("PRK", "KOREA D.P.R.O.");
//            listCountry.put("KOR", "KOREA REPUBLIC OF");
//            listCountry.put("RKS", "KOSOVO");
//            listCountry.put("KWT", "KUWAIT");
//            listCountry.put("KGZ", "KYRGYZSTAN");
//            listCountry.put("LAO", "LAOS");
//            listCountry.put("LVA", "LATVIA");
//            listCountry.put("LBN", "LEBANON");
//            listCountry.put("LSO", "LESOTHO");
//            listCountry.put("LBR", "LIBERIA");
//            listCountry.put("LBY", "LIBYAN ARAB JAMAHIRIYA");
//            listCountry.put("LIE", "LIECHTENSTEIN");
//            listCountry.put("LTU", "LITHUANIA");
//            listCountry.put("LUX", "LUXEMBOURG");
//            listCountry.put("MAC", "MACAU");
//            listCountry.put("MKD", "MACEDONIA");
//            listCountry.put("MDG", "MADAGASCAR");
//            listCountry.put("MWI", "MALAWI");
//            listCountry.put("MYS", "MALAYSIA");
//            listCountry.put("MDV", "MALDIVES");
//            listCountry.put("MLI", "MALI");
//            listCountry.put("MLT", "MALTA");
//            listCountry.put("MHL", "MARSHALL ISLANDS");
//            listCountry.put("MTQ", "MARTINIQUE");
//            listCountry.put("MRT", "MAURITANIA");
//            listCountry.put("MUS", "MAURITIUS");
//            listCountry.put("MYT", "MAYOTTE");
//            listCountry.put("MEX", "MEXICO");
//            listCountry.put("FSM", "MICRONESIA FEDERATED STATES OF");
//            listCountry.put("MDA", "MOLDOVA REPUBLIC OF");
//            listCountry.put("MCO", "MONACO");
//            listCountry.put("MNG", "MONGOLIA");
//            listCountry.put("MNE", "MONTENEGRO");
//            listCountry.put("MSR", "MONTSERRAT");
//            listCountry.put("MAR", "MOROCCO");
//            listCountry.put("MOZ", "MOZAMBIQUE");
//            listCountry.put("MMR", "MYANMAR (Burma)");
//            listCountry.put("NAM", "NAMIBIA");
//            listCountry.put("NRU", "NAURU");
//            listCountry.put("NPL", "NEPAL");
//            listCountry.put("NLD", "NETHERLANDS");
//            listCountry.put("ANT", "NETHERLANDS ANTILLES");
//            listCountry.put("NCL", "NEW CALEDONIA");
//            listCountry.put("NZL", "NEW ZEALAND");
//            listCountry.put("NIC", "NICARAGUA");
//            listCountry.put("NER", "NIGER");
//            listCountry.put("NGA", "NIGERIA");
//            listCountry.put("NIU", "NIUE");
//            listCountry.put("NFK", "NORFOLK ISLAND");
//            listCountry.put("MNP", "NORTHERN MARIANA ISLANDS");
//            listCountry.put("NOR", "NORWAY");
//            listCountry.put("OMN", "OMAN");
//            listCountry.put("PAK", "PAKISTAN");
//            listCountry.put("PLW", "PALAU");
//            listCountry.put("PSE", "PALESTINIAN TERRITORY");
//            listCountry.put("PAN", "PANAMA");
//            listCountry.put("PNG", "PAPUA NEW GUINEA");
//            listCountry.put("PRY", "PARAGUAY");
//            listCountry.put("PER", "PERU");
//            listCountry.put("PHL", "PHILIPPINES");
//            listCountry.put("PCN", "PITCAIRN");
//            listCountry.put("POL", "POLAND");
//            listCountry.put("PRT", "PORTUGAL");
//            listCountry.put("PRI", "PUERTO RICO");
//            listCountry.put("QAT", "QATAR");
//            listCountry.put("REU", "REUNION");
//            listCountry.put("ROU", "ROMANIA");
//            listCountry.put("RUS", "RUSSIAN FEDERATION");
//            listCountry.put("RWA", "RWANDA");
//            listCountry.put("BLM", "SAINT BARTHELEMY");
//            listCountry.put("KNA", "SAINT KITTS AND NEVIS");
//            listCountry.put("LCA", "SAINT LUCIA");
//            listCountry.put("MAF", "SAINT MARTIN");
//            listCountry.put("VCT", "SAINT VINCENT AND THE GRENADINES");
//            listCountry.put("WSM", "SAMOA");
//            listCountry.put("SMR", "SAN MARINO");
//            listCountry.put("STP", "SAO TOME AND PRINCIPE");
//            listCountry.put("SAU", "SAUDI ARABIA");
//            listCountry.put("SEN", "SENEGAL");
//            listCountry.put("SRB", "SERBIA");
//            listCountry.put("SYC", "SEYCHELLES");
//            listCountry.put("SLE", "SIERRA LEONE");
//            listCountry.put("SGP", "SINGAPORE");
//            listCountry.put("SXM", "SINT MAARTEN");
//            listCountry.put("SVK", "SLOVAKIA (Slovak Republic)");
//            listCountry.put("SVN", "SLOVENIA");
//            listCountry.put("SLB", "SOLOMON ISLANDS");
//            listCountry.put("SOM", "SOMALIA");
//            listCountry.put("ZAF", "SOUTH AFRICA");
//            listCountry.put("SGS", "SOUTH GEORGIA AND SOUTH S.S.");
//            listCountry.put("SSD", "SOUTH SUDAN");
//            listCountry.put("ESP", "SPAIN");
//            listCountry.put("LKA", "SRI LANKA");
//            listCountry.put("SHN", "ST. HELENA");
//            listCountry.put("SPM", "ST. PIERRE AND MIQUELON");
//            listCountry.put("SDN", "SUDAN");
//            listCountry.put("SUR", "SURINAME");
//            listCountry.put("SJM", "SVALBARD AND JAN MAYEN ISLANDS");
//            listCountry.put("SWZ", "SWAZILAND");
//            listCountry.put("SWE", "SWEDEN");
//            listCountry.put("CHE", "SWITZERLAND");
//            listCountry.put("SYR", "SYRIAN ARAB REPUBLIC");
//            listCountry.put("TWN", "TAIWAN PROVINCE OF CHINA");
//            listCountry.put("TJK", "TAJIKISTAN");
//            listCountry.put("TZA", "TANZANIA UNITED REPUBLIC OF");
//            listCountry.put("THA", "THAILAND");
//            listCountry.put("TGO", "TOGO");
//            listCountry.put("TKL", "TOKELAU");
//            listCountry.put("TON", "TONGA");
//            listCountry.put("TTO", "TRINIDAD AND TOBAGO");
//            listCountry.put("TUN", "TUNISIA");
//            listCountry.put("TUR", "TURKEY");
//            listCountry.put("TKM", "TURKMENISTAN");
//            listCountry.put("TCA", "TURKS AND CAICOS ISLANDS");
//            listCountry.put("TUV", "TUVALU");
//            listCountry.put("UMI", "U.S. MINOR ISLANDS");
//            listCountry.put("UGA", "UGANDA");
//            listCountry.put("UKR", "UKRAINE");
//            listCountry.put("ARE", "UNITED ARAB EMIRATES");
//            listCountry.put("GBR", "UNITED KINGDOM");
//            listCountry.put("USA", "United States of America");
//            listCountry.put("URY", "URUGUAY");
//            listCountry.put("UZB", "UZBEKISTAN");
//            listCountry.put("VUT", "VANUATU");
//            listCountry.put("VEN", "VENEZUELA");
//            listCountry.put("VNM", "VIET NAM");
//            listCountry.put("VGB", "VIRGIN ISLANDS (BRITISH)");
//            listCountry.put("VIR", "VIRGIN ISLANDS (U.S.)");
//            listCountry.put("WLF", "WALLIS AND FUTUNA ISLANDS");
//            listCountry.put("ESH", "WESTERN SAHARA");
//            listCountry.put("YEM", "YEMEN");
//            listCountry.put("ZMB", "ZAMBIA");
//            listCountry.put("ZWE", "ZIMBABWE");

            listCountry.put("AFG", getString(R.string.AFG));
            listCountry.put("ALA", getString(R.string.ALA));
            listCountry.put("ALB", getString(R.string.ALB));
            listCountry.put("DZA", getString(R.string.DZA));
            listCountry.put("ALL", getString(R.string.ALL));
            listCountry.put("ASM", getString(R.string.ASM));
            listCountry.put("AND", getString(R.string.AND));
            listCountry.put("AGO", getString(R.string.AGO));
            listCountry.put("AIA", getString(R.string.AIA));
            listCountry.put("ATA", getString(R.string.ATA));
            listCountry.put("ATG", getString(R.string.ATG));
            listCountry.put("ARG", getString(R.string.ARG));
            listCountry.put("ARM", getString(R.string.ARM));
            listCountry.put("ABW", getString(R.string.ABW));
            listCountry.put("AUS", getString(R.string.AUS));
            listCountry.put("AUT", getString(R.string.AUT));
            listCountry.put("AZE", getString(R.string.AZE));
            listCountry.put("BHS", getString(R.string.BHS));
            listCountry.put("BHR", getString(R.string.BHR));
            listCountry.put("BGD", getString(R.string.BGD));
            listCountry.put("BRB", getString(R.string.BRB));
            listCountry.put("BLR", getString(R.string.BLR));
            listCountry.put("BEL", getString(R.string.BEL));
            listCountry.put("BLZ", getString(R.string.BLZ));
            listCountry.put("BEN", getString(R.string.BEN));
            listCountry.put("BMU", getString(R.string.BMU));
            listCountry.put("BTN", getString(R.string.BTN));
            listCountry.put("BOL", getString(R.string.BOL));
            listCountry.put("BIH", getString(R.string.BIH));
            listCountry.put("BWA", getString(R.string.BWA));
            listCountry.put("BVT", getString(R.string.BVT));
            listCountry.put("BRA", getString(R.string.BRA));
            listCountry.put("IOT", getString(R.string.IOT));
            listCountry.put("BRN", getString(R.string.BRN));
            listCountry.put("BGR", getString(R.string.BGR));
            listCountry.put("BFA", getString(R.string.BFA));
            listCountry.put("BDI", getString(R.string.BDI));
            listCountry.put("KHM", getString(R.string.KHM));
            listCountry.put("CMR", getString(R.string.CMR));
            listCountry.put("CAN", getString(R.string.CAN));
            listCountry.put("CPV", getString(R.string.CPV));
            listCountry.put("BES", getString(R.string.BES));
            listCountry.put("CYM", getString(R.string.CYM));
            listCountry.put("CAF", getString(R.string.CAF));
            listCountry.put("TCD", getString(R.string.TCD));
            listCountry.put("CHL", getString(R.string.CHL));
            listCountry.put("CHN", getString(R.string.CHN));
            listCountry.put("CXR", getString(R.string.CXR));
            listCountry.put("CCK", getString(R.string.CCK));
            listCountry.put("COL", getString(R.string.COL));
            listCountry.put("COM", getString(R.string.COM));
            listCountry.put("COG", getString(R.string.COG));
            listCountry.put("COD", getString(R.string.COD));
            listCountry.put("COK", getString(R.string.COK));
            listCountry.put("CRI", getString(R.string.CRI));
            listCountry.put("CIV", getString(R.string.CIV));
            listCountry.put("HRV", getString(R.string.HRV));
            listCountry.put("CUB", getString(R.string.CUB));
            listCountry.put("CUW", getString(R.string.CUW));
            listCountry.put("CYP", getString(R.string.CYP));
            listCountry.put("CZE", getString(R.string.CZE));
            listCountry.put("DNK", getString(R.string.DNK));
            listCountry.put("DJI", getString(R.string.DJI));
            listCountry.put("DMA", getString(R.string.DMA));
            listCountry.put("DOM", getString(R.string.DOM));
            listCountry.put("TMP", getString(R.string.TMP));
            listCountry.put("ECU", getString(R.string.ECU));
            listCountry.put("EGY", getString(R.string.EGY));
            listCountry.put("SLV", getString(R.string.SLV));
            listCountry.put("GNQ", getString(R.string.GNQ));
            listCountry.put("ERI", getString(R.string.ERI));
            listCountry.put("EST", getString(R.string.EST));
            listCountry.put("ETH", getString(R.string.ETH));
            listCountry.put("FLK", getString(R.string.FLK));
            listCountry.put("FRO", getString(R.string.FRO));
            listCountry.put("FJI", getString(R.string.FJI));
            listCountry.put("FIN", getString(R.string.FIN));
            listCountry.put("FRA", getString(R.string.FRA));
            listCountry.put("FXX", getString(R.string.FXX));
            listCountry.put("GUF", getString(R.string.GUF));
            listCountry.put("PYF", getString(R.string.PYF));
            listCountry.put("ATF", getString(R.string.ATF));
            listCountry.put("GAB", getString(R.string.GAB));
            listCountry.put("GMB", getString(R.string.GMB));
            listCountry.put("GEO", getString(R.string.GEO));
            listCountry.put("DEU", getString(R.string.DEU));
            listCountry.put("GHA", getString(R.string.GHA));
            listCountry.put("GIB", getString(R.string.GIB));
            listCountry.put("GRC", getString(R.string.GRC));
            listCountry.put("GRL", getString(R.string.GRL));
            listCountry.put("GRD", getString(R.string.GRD));
            listCountry.put("GLP", getString(R.string.GLP));
            listCountry.put("GUM", getString(R.string.GUM));
            listCountry.put("GTM", getString(R.string.GTM));
            listCountry.put("GGY", getString(R.string.GGY));
            listCountry.put("GIN", getString(R.string.GIN));
            listCountry.put("GNB", getString(R.string.GNB));
            listCountry.put("GUY", getString(R.string.GUY));
            listCountry.put("HTI", getString(R.string.HTI));
            listCountry.put("HMD", getString(R.string.HMD));
            listCountry.put("VAT", getString(R.string.VAT));
            listCountry.put("HND", getString(R.string.HND));
            listCountry.put("HKG", getString(R.string.HKG));
            listCountry.put("HUN", getString(R.string.HUN));
            listCountry.put("ISL", getString(R.string.ISL));
            listCountry.put("IND", getString(R.string.IND));
            listCountry.put("IDN", getString(R.string.IDN));
            listCountry.put("IRN", getString(R.string.IRN));
            listCountry.put("IRQ", getString(R.string.IRQ));
            listCountry.put("IRL", getString(R.string.IRL));
            listCountry.put("IMN", getString(R.string.IMN));
            listCountry.put("ISR", getString(R.string.ISR));
            listCountry.put("ITA", getString(R.string.ITA));
            listCountry.put("JAM", getString(R.string.JAM));
            listCountry.put("JPN", getString(R.string.JPN));
            listCountry.put("JEY", getString(R.string.JEY));
            listCountry.put("JOR", getString(R.string.JOR));
            listCountry.put("KAZ", getString(R.string.KAZ));
            listCountry.put("KEN", getString(R.string.KEN));
            listCountry.put("KIR", getString(R.string.KIR));
            listCountry.put("PRK", getString(R.string.PRK));
            listCountry.put("KOR", getString(R.string.KOR));
            listCountry.put("RKS", getString(R.string.RKS));
            listCountry.put("KWT", getString(R.string.KWT));
            listCountry.put("KGZ", getString(R.string.KGZ));
            listCountry.put("LAO", getString(R.string.LAO));
            listCountry.put("LVA", getString(R.string.LVA));
            listCountry.put("LBN", getString(R.string.LBN));
            listCountry.put("LSO", getString(R.string.LSO));
            listCountry.put("LBR", getString(R.string.LBR));
            listCountry.put("LBY", getString(R.string.LBY));
            listCountry.put("LIE", getString(R.string.LIE));
            listCountry.put("LTU", getString(R.string.LTU));
            listCountry.put("LUX", getString(R.string.LUX));
            listCountry.put("MAC", getString(R.string.MAC));
            listCountry.put("MKD", getString(R.string.MKD));
            listCountry.put("MDG", getString(R.string.MDG));
            listCountry.put("MWI", getString(R.string.MWI));
            listCountry.put("MYS", getString(R.string.MYS));
            listCountry.put("MDV", getString(R.string.MDV));
            listCountry.put("MLI", getString(R.string.MLI));
            listCountry.put("MLT", getString(R.string.MLT));
            listCountry.put("MHL", getString(R.string.MHL));
            listCountry.put("MTQ", getString(R.string.MTQ));
            listCountry.put("MRT", getString(R.string.MRT));
            listCountry.put("MUS", getString(R.string.MUS));
            listCountry.put("MYT", getString(R.string.MYT));
            listCountry.put("MEX", getString(R.string.MEX));
            listCountry.put("FSM", getString(R.string.FSM));
            listCountry.put("MDA", getString(R.string.MDA));
            listCountry.put("MCO", getString(R.string.MCO));
            listCountry.put("MNG", getString(R.string.MNG));
            listCountry.put("MNE", getString(R.string.MNE));
            listCountry.put("MSR", getString(R.string.MSR));
            listCountry.put("MAR", getString(R.string.MAR));
            listCountry.put("MOZ", getString(R.string.MOZ));
            listCountry.put("MMR", getString(R.string.MMR));
            listCountry.put("NAM", getString(R.string.NAM));
            listCountry.put("NRU", getString(R.string.NRU));
            listCountry.put("NPL", getString(R.string.NPL));
            listCountry.put("NLD", getString(R.string.NLD));
            listCountry.put("ANT", getString(R.string.ANT));
            listCountry.put("NCL", getString(R.string.NCL));
            listCountry.put("NZL", getString(R.string.NZL));
            listCountry.put("NIC", getString(R.string.NIC));
            listCountry.put("NER", getString(R.string.NER));
            listCountry.put("NGA", getString(R.string.NGA));
            listCountry.put("NIU", getString(R.string.NIU));
            listCountry.put("NFK", getString(R.string.NFK));
            listCountry.put("MNP", getString(R.string.MNP));
            listCountry.put("NOR", getString(R.string.NOR));
            listCountry.put("OMN", getString(R.string.OMN));
            listCountry.put("PAK", getString(R.string.PAK));
            listCountry.put("PLW", getString(R.string.PLW));
            listCountry.put("PSE", getString(R.string.PSE));
            listCountry.put("PAN", getString(R.string.PAN));
            listCountry.put("PNG", getString(R.string.PNG));
            listCountry.put("PRY", getString(R.string.PRY));
            listCountry.put("PER", getString(R.string.PER));
            listCountry.put("PHL", getString(R.string.PHL));
            listCountry.put("PCN", getString(R.string.PCN));
            listCountry.put("POL", getString(R.string.POL));
            listCountry.put("PRT", getString(R.string.PRT));
            listCountry.put("PRI", getString(R.string.PRI));
            listCountry.put("QAT", getString(R.string.QAT));
            listCountry.put("REU", getString(R.string.REU));
            listCountry.put("ROU", getString(R.string.ROU));
            listCountry.put("RUS", getString(R.string.RUS));
            listCountry.put("RWA", getString(R.string.RWA));
            listCountry.put("BLM", getString(R.string.BLM));
            listCountry.put("KNA", getString(R.string.KNA));
            listCountry.put("LCA", getString(R.string.LCA));
            listCountry.put("MAF", getString(R.string.MAF));
            listCountry.put("VCT", getString(R.string.VCT));
            listCountry.put("WSM", getString(R.string.WSM));
            listCountry.put("SMR", getString(R.string.SMR));
            listCountry.put("STP", getString(R.string.STP));
            listCountry.put("SAU", getString(R.string.SAU));
            listCountry.put("SEN", getString(R.string.SEN));
            listCountry.put("SRB", getString(R.string.SRB));
            listCountry.put("SYC", getString(R.string.SYC));
            listCountry.put("SLE", getString(R.string.SLE));
            listCountry.put("SGP", getString(R.string.SGP));
            listCountry.put("SXM", getString(R.string.SXM));
            listCountry.put("SVK", getString(R.string.SVK));
            listCountry.put("SVN", getString(R.string.SVN));
            listCountry.put("SLB", getString(R.string.SLB));
            listCountry.put("SOM", getString(R.string.SOM));
            listCountry.put("ZAF", getString(R.string.ZAF));
            listCountry.put("SGS", getString(R.string.SGS));
            listCountry.put("SSD", getString(R.string.SSD));
            listCountry.put("ESP", getString(R.string.ESP));
            listCountry.put("LKA", getString(R.string.LKA));
            listCountry.put("SHN", getString(R.string.SHN));
            listCountry.put("SPM", getString(R.string.SPM));
            listCountry.put("SDN", getString(R.string.SDN));
            listCountry.put("SUR", getString(R.string.SUR));
            listCountry.put("SJM", getString(R.string.SJM));
            listCountry.put("SWZ", getString(R.string.SWZ));
            listCountry.put("SWE", getString(R.string.SWE));
            listCountry.put("CHE", getString(R.string.CHE));
            listCountry.put("SYR", getString(R.string.SYR));
            listCountry.put("TWN", getString(R.string.TWN));
            listCountry.put("TJK", getString(R.string.TJK));
            listCountry.put("TZA", getString(R.string.TZA));
            listCountry.put("THA", getString(R.string.THA));
            listCountry.put("TGO", getString(R.string.TGO));
            listCountry.put("TKL", getString(R.string.TKL));
            listCountry.put("TON", getString(R.string.TON));
            listCountry.put("TTO", getString(R.string.TTO));
            listCountry.put("TUN", getString(R.string.TUN));
            listCountry.put("TUR", getString(R.string.TUR));
            listCountry.put("TKM", getString(R.string.TKM));
            listCountry.put("TCA", getString(R.string.TCA));
            listCountry.put("TUV", getString(R.string.TUV));
            listCountry.put("UMI", getString(R.string.UMI));
            listCountry.put("UGA", getString(R.string.UGA));
            listCountry.put("UKR", getString(R.string.UKR));
            listCountry.put("ARE", getString(R.string.ARE));
            listCountry.put("GBR", getString(R.string.GBR));
            listCountry.put("USA", getString(R.string.USA));
            listCountry.put("URY", getString(R.string.URY));
            listCountry.put("UZB", getString(R.string.UZB));
            listCountry.put("VUT", getString(R.string.VUT));
            listCountry.put("VEN", getString(R.string.VEN));
            listCountry.put("VNM", getString(R.string.VNM));
            listCountry.put("VGB", getString(R.string.VGB));
            listCountry.put("VIR", getString(R.string.VIR));
            listCountry.put("WLF", getString(R.string.WLF));
            listCountry.put("ESH", getString(R.string.ESH));
            listCountry.put("YEM", getString(R.string.ZMB));
            listCountry.put("ZMB", getString(R.string.ZMB));
            listCountry.put("ZWE", getString(R.string.ZWE));
        }
        return listCountry;
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
//        Log.d("AUTO", "onAutoFillFieldInformationAvailable:  " + resultMap.toString());
//
//        ArrayList<String> skipFeaturesList = getSkipFeaturesList();
//
//        if (Integer.parseInt(eVolvApp.getCurrentServiceID()) == 620) {
//            if (resultMap.isEmpty()) {
//                // Toast.makeText(getActivity(), R.string.barcode_mrz_not_found, Toast.LENGTH_LONG).show();
//                Toast.makeText(getActivity(), R.string.barcode_mrz_not_download, Toast.LENGTH_LONG).show();
//                return;
//            } else {
//                if (resultMap != null) {
//                    if (!resultMap.containsKey(ImageType.FRONT.toString())) {
//                        skipFeaturesList.remove(Constants.FEATURE_AUTO_FILL_FRONT);
//                    }
//
//                    if (!resultMap.containsKey(ImageType.BACK.toString())) {
//                        skipFeaturesList.remove(Constants.FEATURE_AUTO_FILL_BACK);
//                    }
//                }
//            }
//        }
//
//        FragmentManager fragmentManager = getFragmentManager();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(IDDetails.SKIP_FEATURES_LIST, skipFeaturesList);
//
//        if (PreferenceUtils.getPreference(getActivity(), AccountSetup.CUSTOM_UI_CONFIG, false)) {
//            CustomizeUIConfiguration customizeUIConfig = new CustomizeUIConfiguration();
//            customizeUIConfig.setArguments(bundle);
//            fragmentManager.beginTransaction().replace(R.id.flContent, customizeUIConfig).addToBackStack(null).commit();
//            NavigationActivity.toolbar.setTitle(getString(R.string.customize_ui_configuration));
//        } else {
//            IDValidationFaceMatch idValidationFaceMatch = new IDValidationFaceMatch();
//            idValidationFaceMatch.setArguments(bundle);
//            fragmentManager.beginTransaction().replace(R.id.flContent, idValidationFaceMatch).addToBackStack(null).commit();
//            NavigationActivity.toolbar.setTitle(R.string.id_validation);
//        }
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

    @Override
    public void onInitializationResultAvailable(Map<String, String> resultMap, Response responses) {

    }

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
    public void onVideoConferencingFinished(Map<String, String> resultMap, Response response) {

    }

    @Override
    public void onDownloadXsltResultAvailable(Map<String, String> resultMap, Response response) {

    }
}