package com.devobal.ogabuzz;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.devobal.ogabuzz.SpecialClasses.CallBacks;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Modals.RecomendListModel;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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
 * Created by Devobal on 2/28/2019 on 12:33 AM.
 */
public class RecomendedList extends AppCompatActivity{
    RecyclerView recomendlistview;
    RecomendAdapter adapter;
    ArrayList<RecomendListModel> listModelArrayList;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    ImageView recommendback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recomendlist);

        recomendlistview = (RecyclerView) findViewById(R.id.recomendlistview);
        recommendback = (ImageView) findViewById(R.id.recommendback);

        listModelArrayList = new ArrayList<RecomendListModel>();
        preferenceUtils=new PreferenceUtils(RecomendedList.this);
        progressDialog = new ProgressDialog(RecomendedList.this);

        adapter = new RecomendAdapter(RecomendedList.this, listModelArrayList);
        RecyclerView.LayoutManager reclayoutManager=new LinearLayoutManager(RecomendedList.this);
        recomendlistview.setLayoutManager(reclayoutManager);
        recomendlistview.setItemAnimator(new DefaultItemAnimator());
        recomendlistview.setAdapter(adapter);

        recommendback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecommendedGet();

    }

    private void RecommendedGet() {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("limit", "all")
                            /*.header("offset", "")
                            .header("recommend_by", "")
                            .header("recommend_to", "")*/
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
            APICalls.GetRecomendnews apiService= retrofit.create(APICalls.GetRecomendnews.class);
            Call<ResponseBody> call = apiService.GetRecomendnews();
            showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = response.body().string();
                        postdata(s);

                    }catch (Exception e){
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

    private void postdata(String s) {
        try {
            dismissProgressDislogue();
            JSONObject jObject = new JSONObject(s);
            listModelArrayList.clear();
            if(jObject.has("response")) {
                JSONObject obj = jObject.getJSONObject("response");
                if (obj.getString("status").equals("error")) {
                    String message = obj.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    String message = obj.getString("message");
                    JSONArray jsonArray = new JSONArray(obj.getString("data"));

                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject dataobj = jsonArray.getJSONObject(i);
                        RecomendListModel model = new RecomendListModel();
                        model.setNewsid(dataobj.getString("newsid"));
                        model.setRecmmendedby(dataobj.getString("recommend_by"));
                        model.setDate(dataobj.getString("created"));
                        JSONObject contentobj = dataobj.getJSONObject("newsContent");

                        if(contentobj.length()>0) {
                            model.setUserid(contentobj.getString("user_id"));
                            model.setTitle(contentobj.getString("title"));
                            model.setDescription(contentobj.getString("description"));
                            model.setImagelink(contentobj.getString("img_link"));
                            model.setVideopath(contentobj.getString("video_path"));
                            model.setVideolink(contentobj.getString("video_link"));

                            ArrayList<String> imgarray = new ArrayList<String>();
                            JSONArray imagesarray = new JSONArray(contentobj.getString("images"));
                            if(imagesarray.length()>0){
                                for(int t=0; t<imagesarray.length(); t++) {
                                    JSONObject imgobj = imagesarray.getJSONObject(t);
                                    imgarray.add(imgobj.getString("img"));
                                }
                            }
                            model.setPhotosArray(imgarray);
                        }
                        listModelArrayList.add(model);
                    }
                    adapter.notifyDataSetChanged();

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(RecomendedList.this);
            progressDialog.setMessage("Loading data....");
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
