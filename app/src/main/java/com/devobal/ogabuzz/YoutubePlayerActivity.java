package com.devobal.ogabuzz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.devobal.ogabuzz.Modals.YoutubeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YoutubePlayerActivity extends AppCompatActivity {

    ListView rcView;
    ProgressDialog progressDialog;
    ArrayList<YoutubeModel> youtubeModels;
    YoutubePlayerAdapter youtubePlayerAdapter;
    EditText et_search;
    ImageView back;
    TextView searchbtn;
    PreferenceUtils preferenceUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        progressDialog=new ProgressDialog(YoutubePlayerActivity.this);
        youtubeModels=new ArrayList<YoutubeModel>();
        rcView=(ListView)findViewById(R.id.rcView);
        youtubePlayerAdapter=new YoutubePlayerAdapter(YoutubePlayerActivity.this,youtubeModels);
        searchbtn=(TextView) findViewById(R.id.searchbtn);
        et_search=(EditText)findViewById(R.id.searchtext);
        back=(ImageView)findViewById(R.id.searchback);
        preferenceUtils=new PreferenceUtils(YoutubePlayerActivity.this);

        hideKeyboard();

        rcView.setAdapter(youtubePlayerAdapter);

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            YoutubeVideos(preferenceUtils.getSearchText());
        }else {
            YoutubeVideos("");
        }

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(YoutubePlayerActivity.this,YoutubePlayerActivity.class);
                intent.putExtra(et_search.getText().toString(),"search");
                preferenceUtils.setSearchText(et_search.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void YoutubeVideos(String searchtext){
        /*youtubePlayerAdapter.notifyDataSetChanged();*/
        showProgressDialouge();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.GET_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        APICalls.Youtubedata apiService= retrofit.create(APICalls.Youtubedata.class);
        Call<ResponseBody> call = apiService.youtubedata("snippet",String.valueOf(50),searchtext,"strict",
                "AIzaSyA1DRsQ7RvOnASyYoAhAJgoa6Mgxid8Cp8");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDislogue();
                try {
                    String result=response.body().string();
                    youtubeModels.clear();
                    JSONObject jsonObject=new JSONObject(result);
                    String data=jsonObject.getString("items");
                    JSONArray jsonArray=new JSONArray(data);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject kindobject=jsonArray.getJSONObject(i);
                        YoutubeModel youtubeModel=new YoutubeModel();
                        JSONObject idobject=new JSONObject(kindobject.getString("id"));
                        youtubeModel.setVideopath(idobject.getString("videoId"));
                        String url=idobject.getString("videoId");

                        JSONObject snippetobject=new JSONObject(kindobject.getString("snippet"));
                        youtubeModel.setTitle(snippetobject.getString("title"));

                        JSONObject thumbnailsObject=new JSONObject(snippetobject.getString("thumbnails"));
                        JSONObject defaultobject=new JSONObject(thumbnailsObject.getString("high"));
                        youtubeModel.setThumbnailimage(defaultobject.getString("url"));
                        youtubeModel.setThumbnailwidth(defaultobject.getString("width"));
                        youtubeModel.setThumbnailheight(defaultobject.getString("height"));

                        youtubeModels.add(youtubeModel);
                    }
                    youtubePlayerAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    dismissProgressDislogue();
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgressDislogue();
            }
        });
    }

    public void showProgressDialouge(){
        if (!progressDialog.isShowing()){
            progressDialog=new ProgressDialog(YoutubePlayerActivity.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    public void dismissProgressDislogue(){
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public void hideKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if(v!=null){
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }
}
