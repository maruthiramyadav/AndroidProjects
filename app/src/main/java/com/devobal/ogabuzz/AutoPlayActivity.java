package com.devobal.ogabuzz;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.devobal.ogabuzz.R;

/**
 * Created by Devobal on 2/9/2019 on 5:16 PM.
 */
public class AutoPlayActivity extends AppCompatActivity {
    ImageView autoplayback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_play);
        autoplayback = (ImageView) findViewById(R.id.autoplayback);
        autoplayback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
