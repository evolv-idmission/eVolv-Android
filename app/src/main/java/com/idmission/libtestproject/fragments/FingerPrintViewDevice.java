package com.idmission.libtestproject.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class FingerPrintViewDevice extends Fragment {
    private TextView textViewFP;
    private ImageView imageViewFP;
    private EVolvApp eVolvApp;
    public static Handler handler = new Handler();
    private int position = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.finger_print_view, container, false);
        textViewFP = (TextView) view.findViewById(R.id.text_view_finger_print);
        imageViewFP = (ImageView) view.findViewById(R.id.image_view_finger);

        eVolvApp = (EVolvApp) getActivity().getApplicationContext();

        setFingerPrintCaptureData(position);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int position = msg.getData().getInt("position");
                setFingerPrintCaptureData(position);
                super.handleMessage(msg);
            }
        };
        return view;
    }

    private void setFingerPrintCaptureData(int position) {
        try {
            HashMap<String, String> mapFP = eVolvApp.getDeviceResultFPMap();
            String value = (new ArrayList<String>(mapFP.values())).get(position);
            String key = (new ArrayList<String>(mapFP.keySet())).get(position);
            byte[] decodedString = Base64.decode(value, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            imageViewFP.setImageBitmap(bitmap);
            textViewFP.setText(key);

        } catch (Exception e) {
        }
    }
}
