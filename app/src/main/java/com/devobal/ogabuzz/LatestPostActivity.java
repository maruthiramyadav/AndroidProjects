package com.devobal.ogabuzz;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devobal.ogabuzz.Modals.ListModel;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
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
 * Created by Devobal on 3/19/2019 on 12:57 AM.
 */
public class LatestPostActivity extends AppCompatActivity{

    ListView postlistview;
    LatestPostListAdapter postListAdapter;
    ArrayList<ListModel> listModelArrayList;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    String sessionid,userid, categoryname;
    ImageView postback;
    LinearLayout bottomlayout;
    TextView optionfollow,optionshare,optioncomment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.latestpostlist);

        postlistview = (ListView) findViewById(R.id.postlistview);
        postback = (ImageView) findViewById(R.id.postback);
        bottomlayout=(LinearLayout)findViewById(R.id.bottomlayout);
        optionfollow=(TextView)findViewById(R.id.optioncomment);
        optioncomment=(TextView)findViewById(R.id.optioncomment);
        optionshare=(TextView)findViewById(R.id.optionshare);

        postback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listModelArrayList = new ArrayList<ListModel>();
        postListAdapter = new LatestPostListAdapter(LatestPostActivity.this,listModelArrayList);
        postlistview.setAdapter(postListAdapter);
        progressDialog = new ProgressDialog(LatestPostActivity.this);
        preferenceUtils = new PreferenceUtils(LatestPostActivity.this);

        sessionid= preferenceUtils.getSessionId();
        userid = preferenceUtils.getUserId();

        categoryname = preferenceUtils.getCategoryName();
        NewsListGet();
    }

    public void NewsListGet() {
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
                            .header("sessionid", sessionid)
                            .header("category", "")
                            .header("searchtxt", "")
                            .header("language", "")
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
            APICalls.GetNews apiService= retrofit.create(APICalls.GetNews.class);
            Call<ResponseBody> call = apiService.GetNews();
            showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = response.body().string();
                        postListdata(s);

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

    private void postListdata(String s) {
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
                        ListModel model = new ListModel();
                        model.setTitle(dataobj.getString("title"));
                        model.setDescription(dataobj.getString("description"));
                        model.setImgstring(dataobj.getString("img_link"));
                        model.setSlug(dataobj.getString("slug"));
                        model.setUserid(dataobj.getString("user_id"));
                        model.setNewsid(dataobj.getString("id"));
                        model.setVideopath(dataobj.getString("video_path"));
                        model.setNooflike(dataobj.getString("news_like_count"));
                        model.setNoofview(dataobj.getString("news_view_count"));
                        model.setNoofcomnts(dataobj.getString("news_comment_count"));
                        model.setNewsfrom(dataobj.getString("news_from"));
                        model.setNewsfrom(dataobj.getString("news_from"));
                        model.setVideolink(dataobj.getString("video_link"));
                        model.setVideotype(dataobj.getString("video_type"));

//                        JSONArray categoryArray = new JSONArray(dataobj.getString("category"));
                        JSONArray categoryArray = dataobj.getJSONArray("category");
                        if(categoryArray.length()>0) {
                            JSONObject catobj = categoryArray.getJSONObject(0);
                            model.setCategory(catobj.getString("category_name"));
                        }
                        JSONArray imagesarray = new JSONArray(dataobj.getString("images"));
                        ArrayList<String> imgarray = new ArrayList<String>();
                        if(imagesarray.length()>0){
                            for(int t=0; t<imagesarray.length(); t++) {
                                JSONObject imgobj = imagesarray.getJSONObject(t);
                                imgarray.add(imgobj.getString("img"));
                            }
                        }
                        model.setPhotosArray(imgarray);
                        model.setDateandtime(dataobj.getString("created"));

                        listModelArrayList.add(model);
                    }
                    postListAdapter.notifyDataSetChanged();

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(LatestPostActivity.this);
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
