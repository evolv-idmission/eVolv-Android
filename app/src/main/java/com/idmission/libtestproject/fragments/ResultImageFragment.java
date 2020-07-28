package com.idmission.libtestproject.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.idmission.libtestproject.MainActivity;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.utils.StringUtil;

public class ResultImageFragment extends Fragment {

    public static final String RESULT_IMAGE_NAME = "RESULT_IMAGE_NAME";
    public static final String RESULT_IMAGE = "RESULT_IMAGE";

    public ResultImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_result_image, container, false);
        ImageView imageImgView = (ImageView) rootView.findViewById(R.id.result_image);
        TextView imagenameTxtView = (TextView) rootView.findViewById(R.id.result_image_name);

        String name = getArguments().getString(RESULT_IMAGE_NAME);
//        String image = getArguments().getString(RESULT_IMAGE);
        String image = null;
        if (!StringUtil.isEmpty(name)) {
            imagenameTxtView.setText(name);
            ResultData resultData = MainActivity.capturedImageData.get(name);
            if (null != resultData) {
                image = resultData.getImage();
            }
        }

        if(!StringUtil.isEmpty(image)){
            Bitmap bmp = decodeBase64ToBitmap(image);
            if(null != bmp){
                imageImgView.setImageBitmap(bmp);
            }
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * @param input : base64 of image
     * @return bitmap
     */
    public static Bitmap decodeBase64ToBitmap(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
