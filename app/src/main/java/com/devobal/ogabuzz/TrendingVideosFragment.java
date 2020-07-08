package com.devobal.ogabuzz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.content.res.AppCompatResources;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class TrendingVideosFragment extends Fragment implements View.OnClickListener {

    ViewPager viewPager;
    TabLayout tabtitles;
    ImageView nav_menu, listtypeview, gridtypeview, menuwhite,searchimg;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    ArrayList<NewsModel> catlist;
    MyPagerAdapter pagerAdapter;
    String searchtext,languagetext,breakingnewsString="";
    TextView breakingnews;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View rootView=inflater.inflate(R.layout.fragment_trending_news, container, false);
        final View rootView=inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager)rootView.findViewById(R.id.vpPager);
        nav_menu=(ImageView)rootView.findViewById(R.id.nav_menu1);
        listtypeview=(ImageView)rootView.findViewById(R.id.listtypeview);
        gridtypeview=(ImageView)rootView.findViewById(R.id.gridtypeview);
        menuwhite=((Activity)getActivity()).findViewById(R.id.menuwhite);
        breakingnews=(TextView)rootView.findViewById(R.id.breakingnews);
        preferenceUtils=new PreferenceUtils(getActivity());
        progressDialog=new ProgressDialog(getActivity());
        searchimg=(ImageView)rootView.findViewById(R.id.searchimg);
        view=(View)rootView.findViewById(R.id.view);
        view.setVisibility(View.GONE);
        searchimg.setVisibility(View.GONE);

        tabtitles=(TabLayout)rootView.findViewById(R.id.tabtitles);
        catlist=new ArrayList<NewsModel>();
        getCategoriesList();

        if (preferenceUtils.getBooleanFromSP(getActivity(),"gridtype")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                listtypeview.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.listblack));
                gridtypeview.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.gridgreen));
            }else{
                listtypeview.setBackgroundResource(R.drawable.listgreen);
                gridtypeview.setBackgroundResource(R.drawable.gridblack);
            }
        }

        nav_menu.setOnClickListener(this);
        menuwhite.setOnClickListener(this);
        listtypeview.setOnClickListener(this);
        gridtypeview.setOnClickListener(this);

        tabtitles.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                preferenceUtils.saveBooleanToSp(getActivity(),"glClick",false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        breakingNewsGet();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nav_menu1:
                    menuwhite.performClick();
                    break;
            case R.id.menuwhite:
                DrawerLayout drawer;
                drawer=((Activity)getActivity()).findViewById(R.id.drawer_layout);

                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.listtypeview:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listtypeview.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.listgreen));
                    gridtypeview.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.gridblack));
                }else{
                    listtypeview.setBackgroundResource(R.drawable.listblack);
                    gridtypeview.setBackgroundResource(R.drawable.gridgreen);
                }
                preferenceUtils.saveBooleanToSp(getActivity(),"gridtype",false);
                preferenceUtils.saveBooleanToSp(getActivity(),"glClick",true);
                pagerAdapter.notifyDataSetChanged();

                new Handler().postDelayed(
                        new Runnable() {
                            @Override public void run() {
                                tabtitles.getTabAt(tabtitles.getSelectedTabPosition()).select();
                                //viewPager.setCurrentItem(tabtitles.getSelectedTabPosition());
                            }
                        }, 100);

                break;
            case R.id.gridtypeview:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listtypeview.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.listblack));
                    gridtypeview.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.gridgreen));
                }else{
                    listtypeview.setBackgroundResource(R.drawable.listgreen);
                    gridtypeview.setBackgroundResource(R.drawable.gridblack);
                }
                preferenceUtils.saveBooleanToSp(getActivity(),"gridtype",true);
                preferenceUtils.saveBooleanToSp(getActivity(),"glClick",true);
                pagerAdapter.notifyDataSetChanged();
                new Handler().postDelayed(
                        new Runnable() {
                            @Override public void run() {
                                tabtitles.getTabAt(tabtitles.getSelectedTabPosition()).select();
                            }
                        }, 100);
                break;
        }
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        Fragment fragment=null;
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {

            for (int i = 0; i < catlist.size() ; i++) {
                if (i == pos) {
                    //fragment = YourFragment.newInstance();
                    fragment= OnlyVideosTrendingFragment.newInstance(catlist.get(pos).getCat_name(),searchtext, languagetext);
                    break;
                }
            }
            return OnlyVideosTrendingFragment.newInstance(catlist.get(pos).getCat_name(),searchtext, languagetext);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title=null;

            for (int i=0;i<catlist.size();i++){
                if (position==i) {
                    title = catlist.get(i).getCat_name();
                }
            }
            super.getPageTitle(position);
            return catlist.get(position).getCat_name();
        }

        @Override
        public int getCount() {
            return catlist.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
    public void getCategoriesList(){
        final String sessionid=preferenceUtils.getSessionId();
       /* if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")){
            Toast.makeText(getActivity(),"Please make login",Toast.LENGTH_LONG).show();
        }else {*/
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
                                catlist.clear();
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    NewsModel newsModel=new NewsModel();
                                    newsModel.setCat_id(dataobj.getString("cat_id"));
                                    newsModel.setCat_name(dataobj.getString("cat_name"));
                                    catlist.add(newsModel);
                                }
                                pagerAdapter =new MyPagerAdapter(getChildFragmentManager());
                                viewPager.setAdapter(pagerAdapter);
                                tabtitles.setupWithViewPager(viewPager);
                                setRetainInstance(true);
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
        //}
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
    public void breakingNewsGet(){
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
                            .header("category", "headlines")
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
            APICalls.BreakingNews apiService= retrofit.create(APICalls.BreakingNews.class);
            Call<ResponseBody> call = apiService.breakingNews();
            //showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = response.body().string();
                        JSONObject jObject = new JSONObject(s);
                        if(jObject.has("response")) {
                            JSONObject obj = jObject.getJSONObject("response");
                            if (obj.getString("status").equals("error")) {
                                String message = obj.getString("message");
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                String message = obj.getString("message");
                                JSONArray jsonArray = new JSONArray(obj.getString("data"));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    breakingnewsString=breakingnewsString+"        "+dataobj.getString("title");
                                }
                                breakingnews.setSelected(true);
                                breakingnews.setText(Html.fromHtml(String.valueOf(Html.fromHtml(breakingnewsString))));
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
