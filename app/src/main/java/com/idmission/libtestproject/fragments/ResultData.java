package com.idmission.libtestproject.fragments;

/**
 * Created by dipenp on 24-01-2017.
 */

public class ResultData {
    public static final String FRONT_IMAGE_DATA = "FRONT_IMAGE", BACK_IMAGE_DATA = "BACK_IMAGE",
            FACE_IMAGE_DATA = "FACE_IMAGE", PROCESSED_FACE_IMAGE_DATA = "PROCESSED_FACE_IMAGE", OVAL_FACE_IMAGE_DATA = "OVAL_FACE_IMAGE_DATA",
            CARD_IMAGE_DATA = "CARD_IMAGE", POA_IMAGE_DATA = "POA_IMAGE", BANK_STATEMENT_IMG_DATA = "BANK_STATEMENT_IMAGE",
            BIRTH_CERTIFICATE_IMG_DATA = "BIRTH_CERTIFICATE_IMAGE", SCAN_BARCODE_DATA = "SCAN_BARCODE_DATA",CAPTURE_SIGNATURE_IMG="CAPTURE_SIGNATURE_IMG";

    String name;
    String image;

    public ResultData(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
