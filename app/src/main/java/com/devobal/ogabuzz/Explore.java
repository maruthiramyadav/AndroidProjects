package com.devobal.ogabuzz;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

public class Explore extends Fragment {

    ListView trendlistview;
    ExploreTrendAdapter adapter;
    ArrayList<ListModel> listModelArrayList;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_explore, container, false);

        trendlistview = (ListView) rootView.findViewById(R.id.trendlistview);

        preferenceUtils=new PreferenceUtils(getActivity());
        progressDialog = new ProgressDialog(getActivity());

        listModelArrayList = new ArrayList<ListModel>();
        adapter = new ExploreTrendAdapter(Explore.this,listModelArrayList);
        trendlistview.setAdapter(adapter);
        NewsListGet();

        return rootView;
    }

    private void NewsListGet() {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("limit", "all")
                            .header("action_type", "trending_news")
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
            APICalls.TrendingNews apiService= retrofit.create(APICalls.TrendingNews.class);
            Call<ResponseBody> call = apiService.TrendingNews();
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
                        model.setVideolink(dataobj.getString("video_link"));
                        model.setVideotype(dataobj.getString("video_type"));

//                        JSONArray categoryArray = new JSONArray(dataobj.getString("category"));
                        JSONArray categoryArray = dataobj.getJSONArray("category");
                        if(categoryArray.length()>0) {
                            JSONObject catobj = categoryArray.getJSONObject(0);
                            model.setCategory(catobj.getString("category_name"));
                        }

                        model.setDateandtime(dataobj.getString("created"));

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
