package com.idmission.libtestproject.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.classes.Constants;
import com.idmission.libtestproject.utils.BitmapUtils;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;

public class SelfieOverrideOvalFace extends Fragment {
    private TextView textViewSelfie;
    private ImageView imageViewSelfieImage;
    private EVolvApp eVolvApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.selfie_view_pager_image, container, false);
        textViewSelfie = (TextView) view.findViewById(R.id.selfie_name);
        imageViewSelfieImage = (ImageView) view.findViewById(R.id.selfie_image);

        eVolvApp = (EVolvApp) getActivity().getApplicationContext();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Bitmap bitmapFace = BitmapUtils.getBitmapFromFile(eVolvApp.getBaseDirectoryPath() + Constants.OVAL_FACE_IMAGE_OVERRIDE);
        imageViewSelfieImage.setImageBitmap(bitmapFace);
        //textViewSelfie.setText(FaceImageType.OVAL_FACE.toString());
        textViewSelfie.setText(getString(R.string.oval_face));
        super.onActivityCreated(savedInstanceState);
    }
}
