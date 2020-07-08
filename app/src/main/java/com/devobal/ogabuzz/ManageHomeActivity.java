package com.devobal.ogabuzz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.devobal.ogabuzz.SpecialClasses.ItemMoveCallback;
import com.devobal.ogabuzz.Modals.NewsModel;
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

public class ManageHomeActivity extends AppCompatActivity {

    RecyclerView rcView;
    ManageHomeAdapter manageHomeAdapter;
    ArrayList<NewsModel> myListModels;
    PreferenceUtils preferenceUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_home);

        rcView=(RecyclerView)findViewById(R.id.rcView);
        myListModels=new ArrayList<NewsModel>();
        preferenceUtils=new PreferenceUtils(ManageHomeActivity.this);
        manageHomeAdapter=new ManageHomeAdapter(ManageHomeActivity.this,myListModels);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ManageHomeActivity.this);
        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(manageHomeAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rcView);
        rcView.setAdapter(manageHomeAdapter);
        rcView.setItemAnimator(new DefaultItemAnimator());
        rcView.setLayoutManager(layoutManager);
       // preparedDAta();
        getCategoriesList();
    }
    public void getCategoriesList(){
        final String sessionid=preferenceUtils.getSessionId();
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
                            .header("cat_name", "")
                            .header("slug", "")
                            .header("cat_id", "")
                            .header("parent_id", "")*/
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
            APICalls.Categories apiService = retrofit.create(APICalls.Categories.class);
            Call<ResponseBody> call = apiService.categorieslist();
            // showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //   dismissProgressDislogue();
                    try {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.has("response")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                            if (jsonObject1.getString("status").equals("success")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("data"));
                                myListModels.clear();
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    NewsModel newsModel=new NewsModel();
                                    newsModel.setCat_id(dataobj.getString("cat_id"));
                                    newsModel.setCat_name(dataobj.getString("cat_name"));
                                    myListModels.add(newsModel);
                                }
                                manageHomeAdapter.notifyDataSetChanged();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void preparedDAta(){
     for (int i=0;i<20;i++){
         NewsModel newsModel=new NewsModel();
         newsModel.setCat_name("jfks");
         myListModels.add(newsModel);
     }
     manageHomeAdapter.notifyDataSetChanged();
    }
}
