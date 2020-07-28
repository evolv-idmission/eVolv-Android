package com.idmission.libtestproject.activity;

import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.idmission.client.ImageProcessingSDK;
import com.idmission.libtestproject.R;
import com.idmission.libtestproject.fragments.AccountSetup;
import com.idmission.libtestproject.fragments.ProcessFlow;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static Toolbar toolbar;
    private TextView sdkVersion, appName;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.updateLanguage(this, PreferenceUtils.getPreference(this, AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        setContentView(R.layout.nav_drawer_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = AccountSetup.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            toolbar.setTitle(R.string.account_setting);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.GRAY));
        navigationView.setItemIconTintList(ColorStateList.valueOf(Color.GRAY));
        toolbar.setNavigationIcon(R.drawable.drawer_menu);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        navigationView.setNavigationItemSelectedListener(this);

//        View header = navigationView.getHeaderView(0);
//        sdkVersion = (TextView) header.findViewById(R.id.text_view_sdk_version);
//        appName = (TextView) header.findViewById(R.id.text_view_app);
//        sdkVersion.setText(getString(R.string.sdk_version) + " " + StringUtil.getApplicationVersionName(this) + "");

        initializeNavigationMenu();

        ActionBarDrawerToggle  drawerToggle = new  androidx.appcompat.app.ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                CommonUtils.updateLanguage(NavigationActivity.this, PreferenceUtils.getPreference(NavigationActivity.this, AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
                initializeNavigationMenu();
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                CommonUtils.updateLanguage(NavigationActivity.this, PreferenceUtils.getPreference(NavigationActivity.this, AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
                initializeNavigationMenu();
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(drawerToggle);
    }

    private void initializeNavigationMenu() {

        //set Header
        View header = navigationView.getHeaderView(0);
        sdkVersion = (TextView) header.findViewById(R.id.text_view_sdk_version);
        appName = (TextView) header.findViewById(R.id.text_view_app);
        sdkVersion.setText(getString(R.string.sdk_version) + " " + StringUtil.getApplicationVersionName(this) + "");
        appName.setText(getString(R.string.evolve_test_application));

        // get menu from navigationView
        Menu menu = navigationView.getMenu();
        MenuItem nav_ac_setup = menu.findItem(R.id.nav_ac_setup);
        nav_ac_setup.setTitle(getString(R.string.account_setup));

        MenuItem nav_process_flow = menu.findItem(R.id.nav_process_flow);
        nav_process_flow.setTitle(getString(R.string.process_flow));

//        MenuItem nav_about_us = menu.findItem(R.id.nav_about_us);
//        nav_about_us.setTitle(getString(R.string.about_us));
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//
//        } else {
//            //Toast.makeText(getApplicationContext(),"onBack", Toast.LENGTH_LONG).show();
//            super.onBackPressed();
////            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
////            if (fm.getBackStackEntryCount() >= 1) {
////
////                fm.popBackStack();
////            } else {
////                finish();
////            }
//        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 0){
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_ac_setup) {
            fragmentClass = AccountSetup.class;
            toolbar.setTitle(R.string.account_setup);
        } else if (id == R.id.nav_process_flow) {
            if(ImageProcessingSDK.getInstance().getServiceDetails().size()>0){
            fragmentClass = ProcessFlow.class;
            toolbar.setTitle(R.string.process_flow);
            }else{
                showErrorMessage(findViewById(android.R.id.content),"Initialize Account Setup First");
            }
        }

        if (fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    public void showErrorMessage(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //    @Override
//    protected void onPause() {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        super.onPause();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        //Toast.makeText(getApplicationContext(),"onConfigurationChanged",Toast.LENGTH_LONG).show();
//        super.onConfigurationChanged(newConfig);
//    }
}
