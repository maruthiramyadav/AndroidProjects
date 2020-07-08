package com.devobal.ogabuzz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.devobal.ogabuzz.Modals.FollowModel;
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
 * Created by Devobal on 2/11/2019 on 5:29 PM.
 */
public class AllFollowingList extends AppCompatActivity {

    RecyclerView followingrclist;
    ImageView followingdtback;
    ArrayList<FollowModel> followModelslist;
    FollowingListAdapter followAdapter;
    PreferenceUtils preferenceUtils;
    String sessionid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilefollowinglist);

        followingrclist = (RecyclerView) findViewById(R.id.followingrclist);
        followingdtback = (ImageView) findViewById(R.id.followingdtback);

        preferenceUtils = new PreferenceUtils(AllFollowingList.this);
        sessionid = preferenceUtils.getSessionId();
        followModelslist=new ArrayList<FollowModel>();
        followAdapter=new FollowingListAdapter(this,followModelslist);
        followingrclist.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(AllFollowingList.this,LinearLayoutManager.VERTICAL,false);
        followingrclist.setLayoutManager(layoutManager);
        followingrclist.setItemAnimator(new DefaultItemAnimator());
        followingrclist.setAdapter(followAdapter);

        followingdtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        FollowingList();
    }
    private void FollowingList() {
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
                            .header("action_type", "following")
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
            APICalls.FollowersList apiService= retrofit.create(APICalls.FollowersList.class);
            Call<ResponseBody> call = apiService.followerslist();
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
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("data"));
                                followModelslist.clear();
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    FollowModel followModel=new FollowModel();
                                    followModel.setFollowerid(dataobj.getString("follow_user_id"));
                                    followModel.setName(dataobj.getString("follow_user_name"));
                                    followModel.setFollowimg(dataobj.getString("follow_user_image"));
                                    followModelslist.add(followModel);
                                }
                                followAdapter.notifyDataSetChanged();
                            }else{
                                if (jsonObject1.getString("message").equals("No Data Found")){
                                    Toast.makeText(getApplicationContext(), "you are no following anything",Toast.LENGTH_SHORT).show();
                                }
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
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}

