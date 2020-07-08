package com.devobal.ogabuzz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddAPageActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView settings,backarrow;
    FrameLayout flayout;
    LinearLayout topicslayout,Locationlayout;
    View viewLocation,viewtopics;
    TextView topics,location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apage);

        settings=(ImageView)findViewById(R.id.settings);
        backarrow=(ImageView)findViewById(R.id.backarrow);
        flayout=(FrameLayout)findViewById(R.id.flayout);
        Locationlayout=(LinearLayout)findViewById(R.id.Locationlayout);
        topicslayout=(LinearLayout)findViewById(R.id.topicslayout);
        viewtopics=(View)findViewById(R.id.viewtopics);
        viewLocation=(View)findViewById(R.id.viewLocation);
        topics=(TextView)findViewById(R.id.topics);
        location=(TextView)findViewById(R.id.location);

        topicslayout.setOnClickListener(this);
        Locationlayout.setOnClickListener(this);
        settings.setOnClickListener(this);
        backarrow.setOnClickListener(this);

        topicslayout.performClick();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings:
                startActivity(new Intent(AddAPageActivity.this,ManageHomeActivity.class));
                break;
            case R.id.backarrow:
                finish();
                break;
            case R.id.topicslayout:
                viewtopics.setVisibility(View.VISIBLE);
                viewLocation.setVisibility(View.GONE);
                topics.setTextColor(getResources().getColor(R.color.colorRed));
                location.setTextColor(getResources().getColor(R.color.unselectedBackground));
                getSupportFragmentManager().beginTransaction().replace(R.id.flayout, new AddPageTopicFragment())
                        .addToBackStack(null).commit();
                break;
            case R.id.Locationlayout:
                viewtopics.setVisibility(View.GONE);
                viewLocation.setVisibility(View.VISIBLE);
                topics.setTextColor(getResources().getColor(R.color.unselectedBackground));
                location.setTextColor(getResources().getColor(R.color.colorRed));
                getSupportFragmentManager().beginTransaction().replace(R.id.flayout, new AddPageTopicFragment())
                        .addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
