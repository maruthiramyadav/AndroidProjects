package com.devobal.ogabuzz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devobal.ogabuzz.R;

public class SearchActivity extends AppCompatActivity {

    ImageView searchback;
    EditText searchtext;
    TextView searchbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchback = (ImageView) findViewById(R.id.searchback);
        searchtext = (EditText) findViewById(R.id.searchtext);
        searchbtn = (TextView) findViewById(R.id.searchbtn);

        searchback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srhtext = searchtext.getText().toString().trim();
                if(searchtext.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "Please enter Search Text", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("search_news", srhtext);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}