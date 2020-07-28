package com.idmission.libtestproject.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.idmission.client.ImageProcessingSDK;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by dipenp on 26/11/18.
 */

public class CustomizeProduct extends Fragment {

    private static final String CUSTOMIZE_PRODUCT = "CUSTOMIZE_PRODUCT";

    private static final String OTHER = "Other";

    private static final String[] GROUP_NAME = {"ID_Image_Processing", "Customer_Photo_Processing", "Employee_Information", "Proof_Of_Address", "Biometric_Information", "Signature_Data", OTHER};
    private static final String[] FIELD_NAME = {"ID_Type", "ID_Country", "ID_State", "ID_Image_Front", "ID_Image_Front_Secondary", "ID_Image_Back", "ID_Image_Back_Secondary", "Live_Customer_Photo", "Live_Customer_Photo_Secondary", "Customer_Photo_for_Face_Matching", "Customer_Photo_for_Face_Matching_Secondary", "Signature_Image", "Voice_Data", OTHER};
    private static final String[] FIELD_VALUE = {"FRONT_IMAGE", "BACK_IMAGE", "FACE_IMAGE", "PROCESSED_FACE_IMAGE", "OVAL_FACE_IMAGE", "SIGNATURE_IMAGE", "VOICE_DATA", "FINGERPRINT_DATA", OTHER};

    private Button buttonBack, buttonNext, addButton;
    private EVolvApp eVolvApp;

    private Spinner groupNameSpinner, fieldNameSpinner, fieldValueSpinner, productConfigListSpinner;
    private EditText otherGroupNameEdtTxt, otherFieldNameEdtTxt, otherFieldValueEdtTxt;
    private RelativeLayout otherGroupNameContainer, otherFieldNameContainer, otherFieldValueContainer;

    private LinkedHashMap<String, LinkedHashMap<String, String>> customizeProductConfig = new LinkedHashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.customize_product_definition_layout, container, false);

        eVolvApp = (EVolvApp) (getActivity().getApplicationContext());

        groupNameSpinner = (Spinner) view.findViewById(R.id.group_name_spinner);
        fieldNameSpinner = (Spinner) view.findViewById(R.id.field_name_spinner);
        fieldValueSpinner = (Spinner) view.findViewById(R.id.field_value_spinner);
        productConfigListSpinner = (Spinner)view.findViewById(R.id.custom_product_config_spinner);

        otherGroupNameEdtTxt = (EditText) view.findViewById(R.id.other_group_name_edttxt);
        otherFieldNameEdtTxt = (EditText) view.findViewById(R.id.other_field_name_edttxt);
        otherFieldValueEdtTxt = (EditText) view.findViewById(R.id.other_field_value_edttxt);

        otherGroupNameContainer = (RelativeLayout)view.findViewById(R.id.other_group_name_container);
        otherFieldNameContainer = (RelativeLayout)view.findViewById(R.id.other_field_name_container);
        otherFieldValueContainer = (RelativeLayout)view.findViewById(R.id.other_field_value_container);

        buttonBack = (Button) view.findViewById(R.id.back_button);
        buttonNext = (Button) view.findViewById(R.id.next_button);
        addButton = (Button) view.findViewById(R.id.add_product_definition_button);

        groupNameSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, GROUP_NAME));
        fieldNameSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, FIELD_NAME));
        fieldValueSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, FIELD_VALUE));

        groupNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(groupNameSpinner.getSelectedItem().toString().equalsIgnoreCase(OTHER)){
                    otherGroupNameContainer.setVisibility(View.VISIBLE);
                }else {
                    otherGroupNameContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        fieldNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(fieldNameSpinner.getSelectedItem().toString().equalsIgnoreCase(OTHER)){
                    otherFieldNameContainer.setVisibility(View.VISIBLE);
                }else {
                    otherFieldNameContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        fieldValueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(fieldValueSpinner.getSelectedItem().toString().equalsIgnoreCase(OTHER)){
                    otherFieldValueContainer.setVisibility(View.VISIBLE);
                }else {
                    otherFieldValueContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String groupname;
                String fieldname;
                String fieldvalue;

                groupname = groupNameSpinner.getSelectedItem().toString();
                if (groupname.equals(OTHER)) {
                    groupname = otherGroupNameEdtTxt.getText().toString();
                }

                fieldname = fieldNameSpinner.getSelectedItem().toString();
                if (fieldname.equals(OTHER)) {
                    fieldname = otherFieldNameEdtTxt.getText().toString();
                }

                fieldvalue = fieldValueSpinner.getSelectedItem().toString();
                if (fieldvalue.equals(OTHER)) {
                    fieldvalue = otherFieldValueEdtTxt.getText().toString();
                }

                updateProductConfigurationMap(groupname, fieldname, fieldvalue);
                updateProductConfigListView();
            }
        });

        initProductConfigurationMap();

        return view;
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
        eVolvApp.setJsonObjCustomProduct(getProductConfigurationJSONFromMap(customizeProductConfig));
    }

    @Override
    public void onResume() {
        super.onResume();
        updateProductConfigListView();
    }

    private void updateProductConfigurationMap(String groupName, String fieldName, String fieldValue) {

        if (customizeProductConfig.containsKey(groupName)) {
            LinkedHashMap<String, String> fields = customizeProductConfig.get(groupName);
            fields.put(fieldName, fieldValue);
        } else {
            LinkedHashMap<String, String> field = new LinkedHashMap<>();
            field.put(fieldName, fieldValue);
            customizeProductConfig.put(groupName, field);
        }
    }

    private JSONObject getProductConfigurationJSONFromMap(LinkedHashMap<String, LinkedHashMap<String, String>> customProductConfigMap) {

        JSONObject customProductConfigJSON = new JSONObject();

        try {
            Set<String> groupNames = customProductConfigMap.keySet();

            for (String groupName : groupNames) {
                LinkedHashMap<String, String> fields = customProductConfigMap.get(groupName);
                Set<String> fieldKeys = fields.keySet();

                JSONObject customProductField = new JSONObject();

                for (String fieldKey : fieldKeys) {
                    String fieldValue = fields.get(fieldKey);
                    customProductField.put(fieldKey, fieldValue);
                }

                customProductConfigJSON.put(groupName, customProductField);
            }
        } catch (Exception e) {
            Log.d(CUSTOMIZE_PRODUCT, "getProductConfigurationJSONFromMap Exc : " + e);
        }
        return customProductConfigJSON;
    }

    private LinkedHashMap<String, LinkedHashMap<String, String>> getProductConfigurationMapFromJSON(JSONObject productConfigJSON) {
        LinkedHashMap<String, LinkedHashMap<String, String>> customizeProductConfigMap = new LinkedHashMap<>();

        if (productConfigJSON != null && productConfigJSON.length() > 0) {
            //Convert JSON to MAP
            Iterator<String> groups = productConfigJSON.keys();
            while (groups.hasNext()) {
                String group = groups.next();

                JSONObject fieldsJson = productConfigJSON.optJSONObject(group);
                Iterator<String> fields = fieldsJson.keys();

                LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();

                while (fields.hasNext()) {
                    String fieldName = fields.next();
                    String fieldValue = fieldsJson.optString(fieldName);
                    fieldMap.put(fieldName, fieldValue);
                }

                customizeProductConfigMap.put(group, fieldMap);
            }
        }

        return customizeProductConfigMap;
    }

    private ArrayList<String> getProductConfigListFromMap(LinkedHashMap<String, LinkedHashMap<String, String>> customProductConfigMap) {
        ArrayList<String> productConfigList = new ArrayList<>();

        Set<String> groupNames = customProductConfigMap.keySet();

        for (String groupName : groupNames) {
            LinkedHashMap<String, String> fields = customProductConfigMap.get(groupName);

            Set<String> fieldKeys = fields.keySet();
            for (String fieldKey : fieldKeys) {
                String fieldValue = fields.get(fieldKey);

                productConfigList.add(groupName + "," + fieldKey + "," + fieldValue);
            }
        }
        return productConfigList;
    }

    private LinkedHashMap<String, LinkedHashMap<String, String>> initProductConfigurationMap() {

        JSONObject productConfig = eVolvApp.getJsonObjCustomProduct();

        if (productConfig != null && productConfig.length() > 0) {

            LinkedHashMap<String, LinkedHashMap<String, String>> productConfigMap = getProductConfigurationMapFromJSON(productConfig);
            customizeProductConfig.putAll(productConfigMap);

        } else if (eVolvApp.getCurrentServiceID().equals("360")) {

            LinkedHashMap<String, String> ID_Image_Processing_Fields = new LinkedHashMap<>();
            ID_Image_Processing_Fields.put("ID_Type", eVolvApp.getSelectIdType().getIdType());
            ID_Image_Processing_Fields.put("ID_Country", eVolvApp.getCountry());
            ID_Image_Processing_Fields.put("ID_State", eVolvApp.getState());
            ID_Image_Processing_Fields.put("ID_Image_Front", ImageProcessingSDK.FRONT_IMAGE);
            ID_Image_Processing_Fields.put("ID_Image_Back", ImageProcessingSDK.BACK_IMAGE);
            customizeProductConfig.put("ID_Image_Processing", ID_Image_Processing_Fields);

            LinkedHashMap<String, String> Customer_Photo_Processing_Fields = new LinkedHashMap<>();
            Customer_Photo_Processing_Fields.put("Live_Customer_Photo", ImageProcessingSDK.FACE_IMAGE);
            Customer_Photo_Processing_Fields.put("Customer_Photo_for_Face_Matching", ImageProcessingSDK.FRONT_IMAGE);

            customizeProductConfig.put("Customer_Photo_Processing", Customer_Photo_Processing_Fields);

        } else if (eVolvApp.getCurrentServiceID().equals("361")) {
            LinkedHashMap<String, String> ID_Image_Processing_Fields = new LinkedHashMap<>();
//            ID_Image_Processing_Fields.put("ID_Type", eVolvApp.getSelectIdType().getSide());
            ID_Image_Processing_Fields.put("ID_Type_Secondary", eVolvApp.getSelectIdType().getIdType());
//            ID_Image_Processing_Fields.put("ID_Country", eVolvApp.getCountry());
            ID_Image_Processing_Fields.put("ID_Country_Secondary", eVolvApp.getCountry());
            ID_Image_Processing_Fields.put("ID_State", eVolvApp.getState());
            ID_Image_Processing_Fields.put("ID_Image_Front_Secondary", ImageProcessingSDK.FRONT_IMAGE);
            ID_Image_Processing_Fields.put("ID_Image_Back_Secondary", ImageProcessingSDK.BACK_IMAGE);
            customizeProductConfig.put("ID_Image_Processing", ID_Image_Processing_Fields);

            LinkedHashMap<String, String> Customer_Photo_Processing_Fields = new LinkedHashMap<>();
            Customer_Photo_Processing_Fields.put("Live_Customer_Photo_Secondary", ImageProcessingSDK.FACE_IMAGE);
            Customer_Photo_Processing_Fields.put("Customer_Photo_for_Face_Matching_Secondary", ImageProcessingSDK.FRONT_IMAGE);

            customizeProductConfig.put("Customer_Photo_Processing", Customer_Photo_Processing_Fields);
        }
        return customizeProductConfig;
    }

    public class CustomSpinnerAdapter extends ArrayAdapter<String> {
        private ArrayList<String> list = new ArrayList<String>();

        public CustomSpinnerAdapter(Context ctx, int txtViewResourceId, ArrayList<String> list) {
            super(ctx, txtViewResourceId, list);
            this.list = list;
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(final int position, final View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.product_config_list_layout, parent,false);

            String[] config = list.get(position).toString().split(",");

            TextView groupName = (TextView) mySpinner.findViewById(R.id.custom_product_group_name);
            TextView fieldName = (TextView) mySpinner.findViewById(R.id.custom_product_field_name);
            TextView fieldValue = (TextView) mySpinner.findViewById(R.id.custom_product_field_value);

            ImageView imageView = (ImageView) mySpinner.findViewById(R.id.custom_product_delete_imgview);

            if (config.length > 0) {
                groupName.setText(config[0]);
            }
            if (config.length > 1) {
                fieldName.setText(config[1]);
            }
            if (config.length > 2) {
                fieldValue.setText(config[2]);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] config = list.get(position).toString().split(",");
                    String fieldname = config[1];

                    Iterator<String> grp = customizeProductConfig.keySet().iterator();
                    while (grp.hasNext()) {
                        String groupName = grp.next();
                        LinkedHashMap<String, String> fieldMap = customizeProductConfig.get(groupName);

                        Iterator<String> fieldIterator = fieldMap.keySet().iterator();

                        while (fieldIterator.hasNext()) {
                            String field_name = fieldIterator.next();
                            if (field_name.equals(fieldname)) {
                                fieldIterator.remove();
                                break;
                            }
                        }
                    }

                    updateProductConfigListView();
                }
            });

            return mySpinner;
        }
    }

    private void updateProductConfigListView() {
        ArrayList<String> productConfigList = getProductConfigListFromMap(customizeProductConfig);
        productConfigListSpinner.setAdapter(new CustomSpinnerAdapter(getActivity(), R.layout.product_config_list_layout, productConfigList));
    }
}
