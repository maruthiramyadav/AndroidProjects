package com.devobal.ogabuzz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devobal.ogabuzz.Modals.LanguageModel;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Devobal on 2/26/2019 on 11:21 PM.
 */
public  class LanguageSettings extends AppCompatActivity{

    GridView langList;
    ArrayList<LanguageModel>languagelArrayList,selectlangarraylist;
    LanguageCustomGridAdapter gridAdapter;
    TextView langsave;
    String langid,langname,sessionid,userid;
    ImageView langback;
    PreferenceUtils preferenceUtils;
    ArrayList<String>userlanguages= new ArrayList<String>();
    ProgressDialog progressDialog;
    HashSet<String> userlangs;
    boolean hadSavedLanguages=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.languages);
        langList = (GridView) findViewById(R.id.langList);
        langsave = (TextView) findViewById(R.id.langsave);
        langback = (ImageView) findViewById(R.id.langback);

        preferenceUtils = new PreferenceUtils(LanguageSettings.this);
        languagelArrayList = new ArrayList<LanguageModel>();
        selectlangarraylist = new ArrayList<LanguageModel>();
        gridAdapter = new LanguageCustomGridAdapter(LanguageSettings.this, languagelArrayList,selectlangarraylist);
        langList.setAdapter(gridAdapter);

        sessionid = preferenceUtils.getSessionId();
        userid = preferenceUtils.getUserId();
        progressDialog=new ProgressDialog(LanguageSettings.this);

        langback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(sessionid!=null && !sessionid.equals("0")&& !sessionid.equals("")) {
            UserLanguagesGet();
        }else {
            hadSavedLanguages=false;
            LanguagesGet();
        }
    }

    private void UserLanguagesGet() {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("limit", "all")
                                     .header("offset", "")
                                     .header("userid", userid)
                                   //  .header("langid", "")
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });
            OkHttpClient client = httpClient.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            APICalls.UserLanguagesGet apiService= retrofit.create(APICalls.UserLanguagesGet.class);
            Call<ResponseBody> call = apiService.UserLanguagesGet();
            showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.has("response")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                            if (jsonObject1.getString("status").equals("success")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("data"));
                                //selectlangarraylist.clear();
                                userlanguages.clear();
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    userlanguages.add(dataobj.getString("langid"));
                                    //newsModel.setLangid(dataobj.getString("langid"));
                                }
                                userlangs=new HashSet<>(userlanguages);
                                hadSavedLanguages=true;
                            }else{
                                hadSavedLanguages=false;
                                //Toast.makeText(getApplicationContext(), jsonObject1.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                            LanguagesGet();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismissProgressDislogue();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void LanguagePost(final String langname, final String langid) {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("language", langname)
                                     .header("sessionid", sessionid)
                                     .header("lang_id", langid)
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });
            OkHttpClient client = httpClient.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            APICalls.LanguagePost apiService = retrofit.create(APICalls.LanguagePost.class);
            Call<ResponseBody> call = apiService.LanguagePost();
            // showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.has("response")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                            if (jsonObject1.getString("status").equals("success")) {
                                Intent intent = new Intent();
                                intent.putExtra("language", langid);
                                setResult(RESULT_OK, intent);
                                finish();
                               /* JSONArray jsonArray = new JSONArray(jsonObject1.getString("data"));
                                languagelArrayList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    LanguageModel newsModel = new LanguageModel();
                                    newsModel.setLanguage(dataobj.getString("language"));
                                    newsModel.setLangid(dataobj.getString("langid"));
                                    newsModel.setIsselect(false);
                                    languagelArrayList.add(newsModel);
                                }*/
                                Toast.makeText(getApplicationContext(), jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Please login to save settings", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // dismissProgressDislogue();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void LanguagesGet() {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("limit", "all")
                   /*         .header("offset", "")
                            .header("language", "")
                            .header("langid", "")*/
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });
            OkHttpClient client = httpClient.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            APICalls.GetLanguages apiService= retrofit.create(APICalls.GetLanguages.class);
            Call<ResponseBody> call = apiService.GetLanguages();
            showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismissProgressDislogue();
                    try {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.has("response")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                            if (jsonObject1.getString("status").equals("success")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("data"));
                                languagelArrayList.clear();
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    LanguageModel newsModel=new LanguageModel();
                                    newsModel.setLanguage(dataobj.getString("language"));
                                    newsModel.setLangid(dataobj.getString("langid"));
                                    if(hadSavedLanguages) {
                                        if(userlangs.contains(dataobj.getString("langid"))){
                                            newsModel.setIsselect(true);
                                        }else {
                                            newsModel.setIsselect(false);
                                        }
                                    }
                                    languagelArrayList.add(newsModel);
                                }
                                gridAdapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(getApplicationContext(), jsonObject1.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismissProgressDislogue();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showProgressdialog(){
        if (!progressDialog.isShowing()){
            progressDialog=new ProgressDialog(LanguageSettings.this);
            progressDialog.setMessage("Loading data.....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }
    public void dismissProgressDislogue(){
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
