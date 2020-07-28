package com.idmission.libtestproject.classes;

import java.util.HashMap;

public class CustomizeUIConfig {

    private HashMap<String, String> uiConfiguration = new HashMap<>();
    private HashMap<String, String> labelConfiguration = new HashMap<>();

    public HashMap<String, String> getUIConfiguration() {
        return uiConfiguration;
    }

    public void setUiConfiguration(HashMap<String, String> uiConfiguration) {
        this.uiConfiguration = uiConfiguration;
    }

    public HashMap<String, String> getLabelConfiguration() {
        return labelConfiguration;
    }

    public void setLabelConfiguration(HashMap<String, String> labelConfiguration) {
        this.labelConfiguration = labelConfiguration;
    }
}
