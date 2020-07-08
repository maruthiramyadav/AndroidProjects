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
import android.widget.Toast;

import com.devobal.ogabuzz.Modals.FollowModel;
import com.devobal.ogabuzz.Modals.SourcesListModal;
import com.devobal.ogabuzz.Modals.VFollowingList;
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

public class Following extends Fragment {

    RecyclerView VrcView,HrcView;
    ArrayList<FollowModel> followingModelslist;
    HFollowingAdapter followingAdapter;
    VFollowingAdapter vFollowingAdapter;
    ArrayList<SourcesListModal>hFollowingLists;
    ArrayList<VFollowingList>vFollowingLists;
    ArrayList<RecomendListModel> listModelArrayList;
    String sessionid;
    ListView listview;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_following, container, false);

        HrcView=(RecyclerView)rootView.findViewById(R.id.HrcView);
        listview = (ListView) rootView.findViewById(R.id.listview);

        progressDialog = new ProgressDialog(getActivity());
        preferenceUtils = new PreferenceUtils(getActivity());
        sessionid = preferenceUtils.getSessionId();

        hFollowingLists=new ArrayList<SourcesListModal>();
        vFollowingLists=new ArrayList<VFollowingList>();
        followingModelslist=new ArrayList<FollowModel>();
        followingAdapter=new HFollowingAdapter(Following.this,followingModelslist);

        RecyclerView.LayoutManager layoutManager2=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        HrcView.setLayoutManager(layoutManager2);
        HrcView.setItemAnimator(new DefaultItemAnimator());
        HrcView.setAdapter(followingAdapter);

        listModelArrayList = new ArrayList<RecomendListModel>();
        vFollowingAdapter = new VFollowingAdapter(Following.this,listModelArrayList);
        listview.setAdapter(vFollowingAdapter);
        FollowingList();
        //RecommendedGet();

        return rootView;
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
                                followingModelslist.clear();
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    FollowModel followModel=new FollowModel();
                                    followModel.setFollowerid(dataobj.getString("follow_user_id"));
                                    followModel.setName(dataobj.getString("follow_user_name"));
                                    followModel.setFollowimg(dataobj.getString("follow_user_image"));
                                    followingModelslist.add(followModel);
                                }
                                followingAdapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(getActivity(), jsonObject1.getString("message"),Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                } else {
                    String message = obj.getString("message");
                    JSONArray jsonArray = new JSONArray(obj.getString("data"));
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject dataobj = jsonArray.getJSONObject(i);
                        RecomendListModel model = new RecomendListModel();
                        model.setNewsid(dataobj.getString("newsid"));
                        model.setRecmmendedby(dataobj.getString("recommend_by"));
                        model.setDate(dataobj.getString("created"));

//                        JSONArray categoryArray = new JSONArray(dataobj.getString("category"));
                        JSONObject contentobj = dataobj.getJSONObject("newsContent");
                        if(contentobj.length()>0) {
                            model.setUserid(contentobj.getString("user_id"));
                            model.setTitle(contentobj.getString("title"));
                            model.setSlug(contentobj.getString("slug"));
                            model.setDescription(contentobj.getString("description"));
                            model.setImagelink(contentobj.getString("img_link"));
                            model.setVideopath(contentobj.getString("video_path"));
                        }
                        listModelArrayList.add(model);
                    }
                    vFollowingAdapter.notifyDataSetChanged();

                }
            }
        }catch (Exception e){
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