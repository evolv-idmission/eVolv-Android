package com.idmission.libtestproject.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.idmission.libtestproject.R;
import com.idmission.libtestproject.fragments.AccountSetup;
import com.idmission.libtestproject.utils.CommonUtils;
import com.idmission.libtestproject.utils.PreferenceUtils;
import com.idmission.libtestproject.utils.StringUtil;

public class SplashActivity extends AppCompatActivity {

    private Button btnContinue;
    private TextView sdkVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.updateLanguage(this, PreferenceUtils.getPreference(this, AccountSetup.LANGUAGE, AccountSetup.DEFAULT_LANGUAGE));
        setContentView(R.layout.splash_screen);

        new UploadCrashReportsTasks().execute();
        btnContinue = (Button) findViewById(R.id.button_continue);
        sdkVersion = (TextView) findViewById(R.id.text_view_version);
        sdkVersion.setText(sdkVersion.getText() + " " + StringUtil.getApplicationVersionName(this) + "");

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceUtils.setPreference(getApplicationContext(), AccountSetup.CUSTOM_UI_CONFIG, false);

                Intent intent = new Intent(SplashActivity.this, NavigationActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        });
    }

    class UploadCrashReportsTasks extends AsyncTask<String, Void, String> {

        public UploadCrashReportsTasks() {
        }

        @Override
        protected String doInBackground(String... args) {
            EVolvApp appitApp = (EVolvApp) getApplicationContext();
            appitApp.uploadAllCrashReports();
            return null;
        }
    }
}
