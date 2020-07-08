package com.devobal.ogabuzz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.devobal.ogabuzz.Explore;
import com.devobal.ogabuzz.Following;
import com.devobal.ogabuzz.R;

public class FollowActivity extends AppCompatActivity {

    FrameLayout flayout;
    LinearLayout Followinglayout,Explorelayout;
    View viewFollowing,viewExplore;
    ImageView followimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        flayout=(FrameLayout)findViewById(R.id.flayout);
        Explorelayout=(LinearLayout)findViewById(R.id.Explorelayout);
        Followinglayout=(LinearLayout)findViewById(R.id.Followinglayout);
        viewExplore=(View)findViewById(R.id.viewExplore);
        viewFollowing=(View)findViewById(R.id.viewFollowing);

        followimage = (ImageView) findViewById(R.id.followimage);
        followimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.flayout, new Explore())
                .addToBackStack(null).commit();
        viewExplore.setVisibility(View.VISIBLE);
        viewFollowing.setVisibility(View.GONE);

        Explorelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flayout, new Explore())
                        .addToBackStack(null).commit();
                viewExplore.setVisibility(View.VISIBLE);
                viewFollowing.setVisibility(View.GONE);
            }
        });
        Followinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flayout, new Following())
                        .addToBackStack(null).commit();
                viewExplore.setVisibility(View.GONE);
                viewFollowing.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
