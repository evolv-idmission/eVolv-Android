package com.idmission.libtestproject.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idmission.libtestproject.R;
import com.idmission.libtestproject.activity.EVolvApp;
import com.idmission.libtestproject.activity.NavigationActivity;
import com.idmission.libtestproject.adapter.PageAdapter;
import com.idmission.libtestproject.classes.Tab;
import com.idmission.libtestproject.classes.features.FeatureFlow;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;

import java.util.ArrayList;

public class IDValidationFaceMatch extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    private PageAdapter pageAdapter;
    public static ArrayList<Tab> listAdditionalFeatures = new ArrayList<>();
    public static ArrayList<String> featuresForService = new ArrayList<>();
    public static ArrayList<String> skipFeaturesList = new ArrayList<>();
    private EVolvApp eVolvApp;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CommonUtils.updateLanguage(getActivity(), PreferenceUtils.getPreference(getActivity(), AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        View view = inflater.inflate(R.layout.id_validation_face_match, container, false);

        eVolvApp = (EVolvApp) getActivity().getApplicationContext();

        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        String selectedService = eVolvApp.getCurrentServiceName();
        NavigationActivity.toolbar.setTitle(selectedService);

        listAdditionalFeatures = new ArrayList<>();
        featuresForService = eVolvApp.getListAdditionalFeatures();
        skipFeaturesList = (ArrayList<String>) getArguments().getSerializable(IDDetails.SKIP_FEATURES_LIST);

        if (null != skipFeaturesList || skipFeaturesList.size() == 0) {
            getFeaturesTabList(skipFeaturesList);
        } else {
            getFeaturesTabList(featuresForService);
        }

        for (Tab tab : listAdditionalFeatures) {
            tabLayout.addTab(tabLayout.newTab().setText(tab.getTabName()));
        }

        pageAdapter = new PageAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        for (Tab tab : listAdditionalFeatures) {
            pageAdapter.addFrag(tab.getFragment());
        }

        viewPager.setAdapter(pageAdapter);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        super.onActivityCreated(savedInstanceState);
    }

    private void getFeaturesTabList(ArrayList<String> featuresForService) {
        for (String feature : featuresForService) {
            Tab featureTab = FeatureFlow.getFeatureTab(getActivity(), feature);
            if (null != featureTab) {
                listAdditionalFeatures.add(featureTab);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
