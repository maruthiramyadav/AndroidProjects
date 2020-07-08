package com.devobal.ogabuzz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.devobal.ogabuzz.SpecialClasses.InitApplication;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;

/**
 * Created by Devobal on 2/2/2019 on 12:33 PM.
 */
public class SettingsActivity extends AppCompatActivity{

    ImageView settingsback;
    SwitchCompat switchCompat;
    Switch notific_switch;
    RelativeLayout autolayout,langaugelayout;
    PreferenceUtils preferenceUtils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (InitApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
       // getActionBar().hide();
        setContentView(R.layout.settings_activity);
        settingsback = (ImageView) findViewById(R.id.settingsback);
        settingsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        switchCompat = (SwitchCompat) findViewById(R.id.switchCompat);
        notific_switch=(Switch)findViewById(R.id.notific_switch);
        langaugelayout = (RelativeLayout) findViewById(R.id.langaugelayout);
        autolayout = (RelativeLayout) findViewById(R.id.autolayout);
        preferenceUtils=new PreferenceUtils(SettingsActivity.this);

        langaugelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final String sessionid=preferenceUtils.getSessionId();
                if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")){
                    Toast.makeText(SettingsActivity.this,"Please make login",Toast.LENGTH_LONG).show();
                }else {*/
                    Intent i = new Intent(SettingsActivity.this, LanguageSettings.class);
                    startActivityForResult(i, 200);
               /* }*/
            }
        });

        autolayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, AutoPlayActivity.class);
                startActivity(i);
            }
        });

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switchCompat.setChecked(true);
        }

        /*switchCompat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (switchCompat.isChecked()){
                    switchCompat.setChecked(false);
                    InitApplication.getInstance().setIsNightModeEnabled(true);
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }else {
                    switchCompat.setChecked(true);
                    InitApplication.getInstance().setIsNightModeEnabled(false);
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
                return false;
            }
        });*/

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    InitApplication.getInstance().setIsNightModeEnabled(true);
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                } else {
                    InitApplication.getInstance().setIsNightModeEnabled(false);
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
