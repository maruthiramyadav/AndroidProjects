package com.devobal.ogabuzz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.devobal.ogabuzz.Modals.ListModel;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
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

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Devobal on 3/8/2019 on 9:48 PM.
 */
public class OnlyNewsTrendingFragment extends Fragment {

    RecyclerView listview;
    NewsTrendListViewAdapter adapter;
    NewsTrendGridAdapter gridAdapter;
    ArrayList<ListModel> listModelArrayList;
    boolean isvideo=false;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    LinearLayout bottom_dialog;
    View view;
    String sessionid;
    String category;
    String searchtext="";
    static String language="",sellanglist="";
    ShimmerFrameLayout sflayout;
    LinearLayoutManager linearLayoutManager;
    ProgressBar pbar;
    int listlenght=20,listposition;
    boolean isLoading = false,scrollLoop=false,fromlistdetails=false;
    ArrayList<String> langid=new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootview = inflater.inflate(R.layout.foryou_fragment,container,false);

        listview = (RecyclerView) rootview.findViewById(R.id.listview);
        bottom_dialog=((Activity)getActivity()).findViewById(R.id.bottom_dialog);
        view=((Activity)getActivity()).findViewById(R.id.view);
        sflayout=(ShimmerFrameLayout)rootview.findViewById(R.id.sflayout);
        pbar=(ProgressBar)rootview.findViewById(R.id.pbar);
        preferenceUtils=new PreferenceUtils(getActivity());

        listModelArrayList = new ArrayList<ListModel>();
        adapter = new NewsTrendListViewAdapter(OnlyNewsTrendingFragment.this,listModelArrayList,listview);

        if (preferenceUtils.getBooleanFromSP(getActivity(),"LoginStatus")){
            UserLanguagesGet();
        }else {
            sellanglist="";
            LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
            listview.setLayoutManager(layoutManager);
            listview.setItemAnimator(new DefaultItemAnimator());
            listview.setAdapter(adapter);
            NewsListGet();
        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if( bundle.getString("msg")!=null) {
                category = bundle.getString("msg");
            }
            if (bundle.getString("searchtext") != null) {
                searchtext = bundle.getString("searchtext");
            }
            if(bundle.getString("language")!=null){
                language = bundle.getString("language");
            }
        }

        listview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                preferenceUtils.setLastVisibleItemPosition(linearLayoutManager.findLastCompletelyVisibleItemPosition());
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition()
                            == listModelArrayList.size() - 1) {
                        //bottom of list!
                        pbar.setVisibility(View.VISIBLE);
                        loadMoreData();
                        isLoading = true;
                    }
                }
            }
        });

        /*listtypeview = ((Activity)getActivity()).findViewById(R.id.listtypeview);
        gridtypeview = ((Activity)getActivity()).findViewById(R.id.gridtypeview);*/
        /*listModelArrayList = new ArrayList<ListModel>();
        adapter = new NewsTrendListViewAdapter(OnlyNewsTrendingFragment.this,listModelArrayList);
        gridAdapter = new NewsTrendGridAdapter(OnlyNewsTrendingFragment.this,listModelArrayList);

        preferenceUtils=new PreferenceUtils(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        sessionid = preferenceUtils.getSessionId();*/

       /* if (preferenceUtils.getBooleanFromSP(getActivity(),"glClick")){

            if (preferenceUtils.getBooleanFromSP(getActivity(),"gridtype")){
                listview.setAdapter(gridAdapter);
                postGridData(preferenceUtils.getattempt());
            }else {
                listview.setAdapter(adapter);
                postdata(preferenceUtils.getattempt());
            }
        }else {*/

        /*if (preferenceUtils.getBooleanFromSP(getActivity(),"gridtype")){
            listModelArrayList.size();
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
            listview.setLayoutManager(layoutManager);
            listview.setItemAnimator(new DefaultItemAnimator());
            listview.setAdapter(gridAdapter);
            NewsGridGet();
        }else {
            listModelArrayList.size();
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
            listview.setLayoutManager(layoutManager);
            listview.setItemAnimator(new DefaultItemAnimator());
            listview.setAdapter(adapter);
            NewsListGet();
        }*/

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_dialog.setVisibility(View.GONE);
            }
        });
        return rootview;
    }

    public static OnlyNewsTrendingFragment newInstance(String text, String searchtext, String languagetext) {
        OnlyNewsTrendingFragment f = new OnlyNewsTrendingFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        b.putString("searchtext",searchtext);
        b.putString("language", language);
        f.setArguments(b);
        return f;
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
                            .header("userid",preferenceUtils.getUserId())
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
            //showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //dismissProgressDislogue();
                    try {
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.has("response")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                            if (jsonObject1.getString("status").equals("success")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("data"));
                                langid.clear();
                                sellanglist="[";
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    langid.add(dataobj.getString("langid"));
                                    if (i==jsonArray.length()-1){
                                        sellanglist = sellanglist + dataobj.getString("langid")+"]";
                                    }else {
                                        sellanglist = sellanglist + dataobj.getString("langid")+",";
                                    }
                                }
                                if (!scrollLoop){
                                    LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
                                    listview.setLayoutManager(layoutManager);
                                    listview.setItemAnimator(new DefaultItemAnimator());
                                    listview.setAdapter(adapter);
                                    NewsListGet();
                                }else {
                                    NewsListGet();
                                }
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

    private void loadMoreData() {
        /*listModelArrayList.add(null);
        adapter.notifyItemInserted(listModelArrayList.size() - 1);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listModelArrayList.remove(listModelArrayList.size() - 1);
                int scrollPosition = listModelArrayList.size();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                listlenght = currentSize +10;
                scrollLoop=true;
                NewsListGet();
                //UserLanguagesGet();
            }
        }, 2000);*/
        int scrollPosition = listModelArrayList.size();
        adapter.notifyItemRemoved(scrollPosition);
        int currentSize = scrollPosition;
        listlenght = currentSize +10;
        scrollLoop=true;
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
                            .header("limit", String.valueOf(listlenght))
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
            //showProgressdialog();
            sflayout.startShimmerAnimation();
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
                    //dismissProgressDislogue();
                    sflayout.stopShimmerAnimation();
                    sflayout.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void NewsGridGet() {
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
           // showProgressdialog();
            sflayout.startShimmerAnimation();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = response.body().string();
                        postGridData(s);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //dismissProgressDislogue();
                    sflayout.stopShimmerAnimation();
                    sflayout.setVisibility(View.GONE);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void postGridData(String s) {
        try {
           // dismissProgressDislogue();
            sflayout.stopShimmerAnimation();
            sflayout.setVisibility(View.GONE);
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

                        JSONArray categoryArray = dataobj.getJSONArray("category");
                        if(categoryArray.length()>0) {
                            JSONObject catobj = categoryArray.getJSONObject(0);
                            model.setCategory(catobj.getString("category_name"));
                        }
                        model.setDateandtime(dataobj.getString("created"));

                      /*  JSONArray imagesarray = new JSONArray(dataobj.getString("images"));
                        if(imagesarray.length()>0){
                            for(int t=0; i<imagesarray.length(); t++) {
                                JSONObject imgobj = jsonArray.getJSONObject(i);
                                model.setImagearray(imgobj.getString("img"));
                            }
                        }*/
                        listModelArrayList.add(model);

                    }
                    gridAdapter.notifyDataSetChanged();
                    // String userid = dataobj.getString("user_id");

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void postdata(String s) {
        try {
            //dismissProgressDislogue();
            sflayout.stopShimmerAnimation();
            sflayout.setVisibility(View.GONE);
            JSONObject jObject = new JSONObject(s);
            if (!scrollLoop){
                listModelArrayList.clear();
            }
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
                        if (!scrollLoop){
                            loopData(dataobj);
                        }else {
                            if (i > listModelArrayList.size()-1 && i <= listlenght) {
                                loopData(dataobj);
                                adapter.notifyDataSetChanged();
                                scrollLoop=false;
                                pbar.setVisibility(View.GONE);
                            }else {
                                if (pbar!=null){
                                    pbar.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                if (fromlistdetails==true){
                    adapter.notifyDataSetChanged();
                }
                isLoading=false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loopData(JSONObject dataobj){
        try {
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
        } catch (JSONException e) {
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

    public void OptionsMenuSelected(){
        if (bottom_dialog.getVisibility()==View.VISIBLE){
            bottom_dialog.setVisibility(View.GONE);
        }else {
            bottom_dialog.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==111 && resultCode==RESULT_OK){
            listposition= data.getIntExtra("listposition",0);
            String lp=String.valueOf(listposition);
            fromlistdetails=true;
            listlenght=listposition+10;
            NewsListGet();
        }
    }
}