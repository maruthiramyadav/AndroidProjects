package com.devobal.ogabuzz;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

public class AddPageTopicFragment extends Fragment {
    RecyclerView rcView;
    ListView hrcView;
    AddPageTopicsAdapter topicsAdapter;
    AddpageFeaturedAdapter addpageFeaturedAdapter;
    ArrayList<NewsModel> myListModels,featuredlist;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_add_page_topic, container, false);

        rcView=(RecyclerView)rootView.findViewById(R.id.rcView);
        hrcView=(ListView)rootView.findViewById(R.id.hrcView);
        myListModels=new ArrayList<NewsModel>();
        featuredlist=new ArrayList<NewsModel>();
        preferenceUtils=new PreferenceUtils(getActivity());

        topicsAdapter=new AddPageTopicsAdapter(AddPageTopicFragment.this,myListModels);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rcView.setAdapter(topicsAdapter);
        rcView.setItemAnimator(new DefaultItemAnimator());
        rcView.setLayoutManager(layoutManager);

        addpageFeaturedAdapter=new AddpageFeaturedAdapter(AddPageTopicFragment.this,featuredlist);
        hrcView.setAdapter(addpageFeaturedAdapter);
        getCategoriesList();
        return rootView;
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
           //  showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                 //    dismissProgressDislogue();
                    try {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.has("response")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                            if (jsonObject1.getString("status").equals("success")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("data"));
                                myListModels.clear();
                                featuredlist.clear();
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    NewsModel newsModel=new NewsModel();
                                    if (i<=7){
                                        newsModel.setCategorycheck(true);
                                    }else {
                                        newsModel.setCategorycheck(false);
                                    }
                                    newsModel.setCat_id(dataobj.getString("cat_id"));
                                    newsModel.setCat_name(dataobj.getString("cat_name"));
                                    myListModels.add(newsModel);
                                    featuredlist.add(newsModel);
                                }
                                topicsAdapter.notifyDataSetChanged();
                                addpageFeaturedAdapter.notifyDataSetChanged();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(getActivity());
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