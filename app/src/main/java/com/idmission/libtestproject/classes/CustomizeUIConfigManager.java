package com.idmission.libtestproject.classes;

import android.content.Context;


import com.idmission.libtestproject.fragments.AccountSetup;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class CustomizeUIConfigManager {
    private static CustomizeUIConfig id_capture_front_ui_config = new CustomizeUIConfig();
    private static CustomizeUIConfig id_capture_back_ui_config = new CustomizeUIConfig();
    private static CustomizeUIConfig doc_capture_ui_config = new CustomizeUIConfig();
    private static CustomizeUIConfig snippet_capture_ui_config = new CustomizeUIConfig();
    private static CustomizeUIConfig face_capture_ui_config = new CustomizeUIConfig();
    private static CustomizeUIConfig fp_capture_ui_config = new CustomizeUIConfig();
    private static CustomizeUIConfig sec_id_capture_front_ui_config = new CustomizeUIConfig();
    private static CustomizeUIConfig sec_id_capture_back_ui_config = new CustomizeUIConfig();
    private static CustomizeUIConfig voice_recording_ui_config = new CustomizeUIConfig();

    public static String ID_CAPTURE_FRONT = "id_capture_front", ID_CAPTURE_BACK = "id_capture_back",
            DOCUMENT_CAPTURE = "document_capture", SNIPPET_CAPTURE = "snippet_capture", FACE_CAPTURE = "face_capture", FINGER_4F_CAPTURE = "camera_finger_capture",
    SECONDARY_ID_CAPTURE_FRONT = "secondary_id_capture_front", SECONDARY_ID_CAPTURE_BACK = "secondary_id_capture_back", VOICE_RECORDING = "voice_recording";

    //Default UI configuration getter/setter
    public static CustomizeUIConfig getDefaultIDCaptureFrontConfig() {
        return id_capture_front_ui_config;
    }

    public static CustomizeUIConfig getDefaultIDCaptureBackConfig() {
        return id_capture_back_ui_config;
    }

    public static CustomizeUIConfig getDefaultDocCaptureConfig() {
        return doc_capture_ui_config;
    }

    public static CustomizeUIConfig getDefaultSnippetCaptureConfig() {
        return snippet_capture_ui_config;
    }

    public static CustomizeUIConfig getDefaultFaceCaptureConfig() {
        return face_capture_ui_config;
    }

    public static CustomizeUIConfig getDefaultFingerPrintCaptureConfig() {
        return fp_capture_ui_config;
    }

    public static CustomizeUIConfig getDefaultSecIDCaptureFrontConfig() {
        return sec_id_capture_front_ui_config;
    }

    public static CustomizeUIConfig getDefaultSecIDCaptureBackConfig() {
        return sec_id_capture_back_ui_config;
    }

    public static CustomizeUIConfig getDefaultVoiceRecordingConfig() {
        return voice_recording_ui_config;
    }

    public static void initCustomizeUIConfig(Context _context) {
        String idCaptureFrontConfig = PreferenceUtils.getPreference(_context, ID_CAPTURE_FRONT, "");
        String idCaptureBackConfig = PreferenceUtils.getPreference(_context, ID_CAPTURE_BACK, "");
        String docCaptureConfig = PreferenceUtils.getPreference(_context, DOCUMENT_CAPTURE, "");
        String snippetCaptureConfig = PreferenceUtils.getPreference(_context, SNIPPET_CAPTURE, "");
        String faceCaptureConfig = PreferenceUtils.getPreference(_context, FACE_CAPTURE, "");
        String fpCaptureConfig = PreferenceUtils.getPreference(_context, FINGER_4F_CAPTURE, "");
        String secIdCaptureFrontConfig = PreferenceUtils.getPreference(_context, SECONDARY_ID_CAPTURE_FRONT, "");
        String secIdCaptureBackConfig = PreferenceUtils.getPreference(_context, SECONDARY_ID_CAPTURE_BACK, "");
        String voiceRecordingConfig = PreferenceUtils.getPreference(_context, VOICE_RECORDING, "");

        String uiConfigJsonString = "";

        String language = PreferenceUtils.getPreference(_context, AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE);
        if (language.equals("es")) {
            uiConfigJsonString = CommonUtils.readStringFromAssetFile(_context, "evolv_ui_customization_es.json");
        } else if (language.equals("my")) {
            uiConfigJsonString = CommonUtils.readStringFromAssetFile(_context, "evolv_ui_customization_my.json");
        } else {
            uiConfigJsonString = CommonUtils.readStringFromAssetFile(_context, "evolv_ui_customization.json");
        }

        try {
            JSONObject uiconfig = new JSONObject(uiConfigJsonString);

            JSONObject id_capture_front_config;
            if(!StringUtil.isEmpty(idCaptureFrontConfig)){
                id_capture_front_config = new JSONObject(idCaptureFrontConfig);
            }else {
                id_capture_front_config = uiconfig.optJSONObject("id_capture_front");
            }
            id_capture_front_ui_config.setUiConfiguration(generateUIConfigKeyValuePair(id_capture_front_config));
            id_capture_front_ui_config.setLabelConfiguration(generateLabelConfigKeyValuePair(id_capture_front_config));

            JSONObject id_capture_back_config;
            if(!StringUtil.isEmpty(idCaptureBackConfig)){
                id_capture_back_config = new JSONObject(idCaptureBackConfig);
            }else {
                id_capture_back_config = uiconfig.optJSONObject("id_capture_back");
            }
            id_capture_back_ui_config.setUiConfiguration(generateUIConfigKeyValuePair(id_capture_back_config));
            id_capture_back_ui_config.setLabelConfiguration(generateLabelConfigKeyValuePair(id_capture_back_config));

            JSONObject doc_capture_config;
            if(!StringUtil.isEmpty(docCaptureConfig)){
                doc_capture_config = new JSONObject(docCaptureConfig);
            }else {
                doc_capture_config = uiconfig.optJSONObject("document_capture");
            }
            doc_capture_ui_config.setUiConfiguration(generateUIConfigKeyValuePair(doc_capture_config));
            doc_capture_ui_config.setLabelConfiguration(generateLabelConfigKeyValuePair(doc_capture_config));

            JSONObject snippet_capture_config;
            if(!StringUtil.isEmpty(snippetCaptureConfig)){
                snippet_capture_config = new JSONObject(snippetCaptureConfig);
            }else {
                snippet_capture_config = uiconfig.optJSONObject("snippet_capture");
            }
            snippet_capture_ui_config.setUiConfiguration(generateUIConfigKeyValuePair(snippet_capture_config));
            snippet_capture_ui_config.setLabelConfiguration(generateLabelConfigKeyValuePair(snippet_capture_config));

            JSONObject face_capture_config;
            if(!StringUtil.isEmpty(faceCaptureConfig)){
                face_capture_config = new JSONObject(faceCaptureConfig);
            }else {
                face_capture_config = uiconfig.optJSONObject("face_capture");
            }
            face_capture_ui_config.setUiConfiguration(generateUIConfigKeyValuePair(face_capture_config));
            face_capture_ui_config.setLabelConfiguration(generateLabelConfigKeyValuePair(face_capture_config));

            JSONObject fp_capture_config;
            if(!StringUtil.isEmpty(fpCaptureConfig)){
                fp_capture_config = new JSONObject(fpCaptureConfig);
            }else {
                fp_capture_config = uiconfig.optJSONObject("camera_finger_capture");
            }
            fp_capture_ui_config.setUiConfiguration(generateUIConfigKeyValuePair(fp_capture_config));
            fp_capture_ui_config.setLabelConfiguration(generateLabelConfigKeyValuePair(fp_capture_config));

            JSONObject secondary_id_capture_front_config;
            if(!StringUtil.isEmpty(secIdCaptureFrontConfig)){
                secondary_id_capture_front_config = new JSONObject(secIdCaptureFrontConfig);
            }else {
                secondary_id_capture_front_config = uiconfig.optJSONObject("secondary_id_capture_front");
            }
            sec_id_capture_front_ui_config.setUiConfiguration(generateUIConfigKeyValuePair(secondary_id_capture_front_config));
            sec_id_capture_front_ui_config.setLabelConfiguration(generateLabelConfigKeyValuePair(secondary_id_capture_front_config));

            JSONObject secondary_id_capture_back_config;
            if(!StringUtil.isEmpty(secIdCaptureBackConfig)){
                secondary_id_capture_back_config = new JSONObject(secIdCaptureBackConfig);
            }else {
                secondary_id_capture_back_config = uiconfig.optJSONObject("secondary_id_capture_back");
            }
            sec_id_capture_back_ui_config.setUiConfiguration(generateUIConfigKeyValuePair(secondary_id_capture_back_config));
            sec_id_capture_back_ui_config.setLabelConfiguration(generateLabelConfigKeyValuePair(secondary_id_capture_back_config));

            JSONObject voice_recording_config;
            if(!StringUtil.isEmpty(voiceRecordingConfig)){
                voice_recording_config = new JSONObject(voiceRecordingConfig);
            }else {
                voice_recording_config = uiconfig.optJSONObject("voice_recording");
            }
            voice_recording_ui_config.setUiConfiguration(generateUIConfigKeyValuePair(voice_recording_config));
            voice_recording_ui_config.setLabelConfiguration(generateLabelConfigKeyValuePair(voice_recording_config));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static HashMap<String, String> generateUIConfigKeyValuePair(JSONObject jsonObject) {
        HashMap<String, String> keyvalue = new HashMap<>();

        if (jsonObject != null) {
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object val = jsonObject.opt(key);
                if (val != null && val instanceof String) {
                    keyvalue.put(key, (String) val);
                }
            }
        }

        return keyvalue;
    }

    private static HashMap<String, String> generateLabelConfigKeyValuePair(JSONObject jsonObject) {
        HashMap<String, String> keyvalue = new HashMap<>();

        if (jsonObject != null) {
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object val = jsonObject.opt(key);
                if (key.equalsIgnoreCase("labels")) {
                    JSONObject labels = (JSONObject) val;
                    Iterator<String> labelkeys = labels.keys();
                    while (labelkeys.hasNext()) {
                        String labelkey = labelkeys.next();
                        String labelval = labels.optString(labelkey, "");
                        keyvalue.put(labelkey, labelval);
                    }
                }
            }
        }

        return keyvalue;
    }

    public static JSONObject getUIConfigJson(String featureType){
        HashMap<String, String> uiConfig = null;
        HashMap<String, String> labelConfig = null;

        if (featureType.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_FRONT)) {
            uiConfig = CustomizeUIConfigManager.getDefaultIDCaptureFrontConfig().getUIConfiguration();
            labelConfig = CustomizeUIConfigManager.getDefaultIDCaptureFrontConfig().getLabelConfiguration();
        } else if (featureType.equalsIgnoreCase(CustomizeUIConfigManager.ID_CAPTURE_BACK)) {
            uiConfig = CustomizeUIConfigManager.getDefaultIDCaptureBackConfig().getUIConfiguration();
            labelConfig = CustomizeUIConfigManager.getDefaultIDCaptureBackConfig().getLabelConfiguration();
        }else if (featureType.equalsIgnoreCase(CustomizeUIConfigManager.DOCUMENT_CAPTURE)) {
            uiConfig = CustomizeUIConfigManager.getDefaultDocCaptureConfig().getUIConfiguration();
            labelConfig = CustomizeUIConfigManager.getDefaultDocCaptureConfig().getLabelConfiguration();
        }else if (featureType.equalsIgnoreCase(CustomizeUIConfigManager.SNIPPET_CAPTURE)) {
            uiConfig = CustomizeUIConfigManager.getDefaultSnippetCaptureConfig().getUIConfiguration();
            labelConfig = CustomizeUIConfigManager.getDefaultSnippetCaptureConfig().getLabelConfiguration();
        }else if (featureType.equalsIgnoreCase(CustomizeUIConfigManager.FACE_CAPTURE)) {
            uiConfig = CustomizeUIConfigManager.getDefaultFaceCaptureConfig().getUIConfiguration();
            labelConfig = CustomizeUIConfigManager.getDefaultFaceCaptureConfig().getLabelConfiguration();
        }else if (featureType.equalsIgnoreCase(CustomizeUIConfigManager.FINGER_4F_CAPTURE)) {
            uiConfig = CustomizeUIConfigManager.getDefaultFingerPrintCaptureConfig().getUIConfiguration();
            labelConfig = CustomizeUIConfigManager.getDefaultFingerPrintCaptureConfig().getLabelConfiguration();
        }else if(featureType.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_FRONT)) {
            uiConfig = CustomizeUIConfigManager.getDefaultSecIDCaptureFrontConfig().getUIConfiguration();
            labelConfig = CustomizeUIConfigManager.getDefaultSecIDCaptureFrontConfig().getLabelConfiguration();
        }else if(featureType.equalsIgnoreCase(CustomizeUIConfigManager.SECONDARY_ID_CAPTURE_BACK)) {
            uiConfig = CustomizeUIConfigManager.getDefaultSecIDCaptureBackConfig().getUIConfiguration();
            labelConfig = CustomizeUIConfigManager.getDefaultSecIDCaptureBackConfig().getLabelConfiguration();
        }else if(featureType.equalsIgnoreCase(CustomizeUIConfigManager.VOICE_RECORDING)) {
            uiConfig = CustomizeUIConfigManager.getDefaultVoiceRecordingConfig().getUIConfiguration();
            labelConfig = CustomizeUIConfigManager.getDefaultVoiceRecordingConfig().getLabelConfiguration();
        }

        JSONObject uiJson = null;
        if (uiConfig != null) {
            uiJson = new JSONObject(uiConfig);
            if (labelConfig != null) {
                JSONObject labelJson = new JSONObject(labelConfig);
                try {
                    uiJson.put("labels", labelJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return uiJson;
    }

    public static void storeConfig(Context _context, String featureType) {
        JSONObject uiJson = getUIConfigJson(featureType);

        if (uiJson != null) {
            PreferenceUtils.setPreference(_context, featureType, uiJson.toString());
        }
    }

    public static JSONObject getCompleteUIConfigJSON(){
        JSONObject uiconfig = new JSONObject();

        JSONObject idFrontConfig = getUIConfigJson(ID_CAPTURE_FRONT);
        JSONObject idBacktConfig = getUIConfigJson(ID_CAPTURE_BACK);
        JSONObject docConfig = getUIConfigJson(DOCUMENT_CAPTURE);
        JSONObject snippetConfig = getUIConfigJson(SNIPPET_CAPTURE);
        JSONObject faceCaptureConfig = getUIConfigJson(FACE_CAPTURE);
        JSONObject fingerConfig = getUIConfigJson(FINGER_4F_CAPTURE);
        JSONObject secIdFrontConfig = getUIConfigJson(SECONDARY_ID_CAPTURE_FRONT);
        JSONObject secIdBackConfig = getUIConfigJson(SECONDARY_ID_CAPTURE_BACK);
        JSONObject voiceRecordingConfig = getUIConfigJson(VOICE_RECORDING);

        try {
            uiconfig.put(ID_CAPTURE_FRONT, idFrontConfig);
            uiconfig.put(ID_CAPTURE_BACK, idBacktConfig);
            uiconfig.put(DOCUMENT_CAPTURE, docConfig);
            uiconfig.put(SNIPPET_CAPTURE, snippetConfig);
            uiconfig.put(FACE_CAPTURE, faceCaptureConfig);
            uiconfig.put(FINGER_4F_CAPTURE, fingerConfig);
            uiconfig.put(SECONDARY_ID_CAPTURE_FRONT, secIdFrontConfig);
            uiconfig.put(SECONDARY_ID_CAPTURE_BACK, secIdBackConfig);
            uiconfig.put(VOICE_RECORDING, voiceRecordingConfig);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return uiconfig;
    }

    public static void reinitWithLanguageChange(Context _context, String language) {

        String idCaptureFrontConfig = PreferenceUtils.getPreference(_context, ID_CAPTURE_FRONT, "");
        String idCaptureBackConfig = PreferenceUtils.getPreference(_context, ID_CAPTURE_BACK, "");
        String docCaptureConfig = PreferenceUtils.getPreference(_context, DOCUMENT_CAPTURE, "");
        String snippetCaptureConfig = PreferenceUtils.getPreference(_context, SNIPPET_CAPTURE, "");
        String faceCaptureConfig = PreferenceUtils.getPreference(_context, FACE_CAPTURE, "");
        String fpCaptureConfig = PreferenceUtils.getPreference(_context, FINGER_4F_CAPTURE, "");
        String secIdCaptureFrontConfig = PreferenceUtils.getPreference(_context, SECONDARY_ID_CAPTURE_FRONT, "");
        String secIdCaptureBackConfig = PreferenceUtils.getPreference(_context, SECONDARY_ID_CAPTURE_BACK, "");
        String voiceRecordingConfig = PreferenceUtils.getPreference(_context, VOICE_RECORDING, "");

        String uiConfigJsonString;
        if (language.equals("es")) {
            uiConfigJsonString = CommonUtils.readStringFromAssetFile(_context, "evolv_ui_customization_es.json");
        } else if (language.equals("my")) {
            uiConfigJsonString = CommonUtils.readStringFromAssetFile(_context, "evolv_ui_customization_my.json");
        } else {
            uiConfigJsonString = CommonUtils.readStringFromAssetFile(_context, "evolv_ui_customization.json");
        }

        try {
            JSONObject uiconfig = new JSONObject(uiConfigJsonString);

            if (!StringUtil.isEmpty(idCaptureFrontConfig)) {
                JSONObject defaultConfig = uiconfig.optJSONObject("id_capture_front");

                JSONObject id_capture_front_config = new JSONObject(idCaptureFrontConfig);
                id_capture_front_config.put("labels", defaultConfig.get("labels"));
                PreferenceUtils.setPreference(_context, ID_CAPTURE_FRONT, id_capture_front_config.toString());
            }

            if (!StringUtil.isEmpty(idCaptureBackConfig)) {
                JSONObject defaultConfig = uiconfig.optJSONObject("id_capture_back");

                JSONObject id_capture_back_config = new JSONObject(idCaptureBackConfig);
                id_capture_back_config.put("labels", defaultConfig.get("labels"));
                PreferenceUtils.setPreference(_context, ID_CAPTURE_BACK, id_capture_back_config.toString());
            }

            if (!StringUtil.isEmpty(docCaptureConfig)) {
                JSONObject defaultConfig = uiconfig.optJSONObject("document_capture");

                JSONObject doc_capture_config = new JSONObject(docCaptureConfig);
                doc_capture_config.put("labels", defaultConfig.get("labels"));
                PreferenceUtils.setPreference(_context, DOCUMENT_CAPTURE, doc_capture_config.toString());
            }

            if (!StringUtil.isEmpty(snippetCaptureConfig)) {
                JSONObject defaultConfig = uiconfig.optJSONObject("snippet_capture");

                JSONObject snippet_capture_config = new JSONObject(snippetCaptureConfig);
                snippet_capture_config.put("labels", defaultConfig.get("labels"));
                PreferenceUtils.setPreference(_context, SNIPPET_CAPTURE, snippet_capture_config.toString());
            }

            if (!StringUtil.isEmpty(faceCaptureConfig)) {
                JSONObject defaultConfig = uiconfig.optJSONObject("face_capture");

                JSONObject face_capture_config = new JSONObject(faceCaptureConfig);
                face_capture_config.put("labels", defaultConfig.get("labels"));
                PreferenceUtils.setPreference(_context, FACE_CAPTURE, face_capture_config.toString());
            }

            if (!StringUtil.isEmpty(fpCaptureConfig)) {
                JSONObject defaultConfig = uiconfig.optJSONObject("camera_finger_capture");

                JSONObject finger_capture_config = new JSONObject(fpCaptureConfig);
                finger_capture_config.put("labels", defaultConfig.get("labels"));
                PreferenceUtils.setPreference(_context, FINGER_4F_CAPTURE, finger_capture_config.toString());
            }

            if (!StringUtil.isEmpty(secIdCaptureFrontConfig)) {
                JSONObject defaultConfig = uiconfig.optJSONObject("secondary_id_capture_front");

                JSONObject sec_id_capture_front_config = new JSONObject(secIdCaptureFrontConfig);
                sec_id_capture_front_config.put("labels", defaultConfig.get("labels"));
                PreferenceUtils.setPreference(_context, SECONDARY_ID_CAPTURE_FRONT, sec_id_capture_front_config.toString());
            }

            if (!StringUtil.isEmpty(secIdCaptureBackConfig)) {
                JSONObject defaultConfig = uiconfig.optJSONObject("secondary_id_capture_back");

                JSONObject sec_id_capture_back_config = new JSONObject(secIdCaptureBackConfig);
                sec_id_capture_back_config.put("labels", defaultConfig.get("labels"));
                PreferenceUtils.setPreference(_context, SECONDARY_ID_CAPTURE_BACK, sec_id_capture_back_config.toString());
            }

            if (!StringUtil.isEmpty(voiceRecordingConfig)) {
                JSONObject defaultConfig = uiconfig.optJSONObject("voice_recording");

                JSONObject voice_recording_config = new JSONObject(voiceRecordingConfig);
                voice_recording_config.put("labels", defaultConfig.get("labels"));
                PreferenceUtils.setPreference(_context, VOICE_RECORDING, voice_recording_config.toString());
            }
        } catch (Exception e) {
        }
    }
}
