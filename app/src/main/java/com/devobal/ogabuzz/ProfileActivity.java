package com.devobal.ogabuzz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.devobal.ogabuzz.R;

/**
 * Created by Devobal on 2/2/2019 on 12:21 PM.
 */
public class ProfileActivity extends AppCompatActivity{
    ImageView profileback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        profileback = (ImageView) findViewById(R.id.profileback);

        profileback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
