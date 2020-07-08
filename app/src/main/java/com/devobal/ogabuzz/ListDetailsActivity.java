package com.devobal.ogabuzz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.SpecialClasses.CallBacks;
import com.devobal.ogabuzz.Modals.CommentsModel;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.devobal.ogabuzz.SpecialClasses.Utility;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

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
 * Created by Devobal on 2/8/2019 on 2:43 PM.
 */
public class ListDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView dtvideoimg,detailsback,commentsend;
    EditText edcomment;
    ImageView dtimgivew,listdtsharearrow,listdtoptmenu,likeimage,unlikeimage;

    SimpleExoPlayerView simpleExoPlayer;
    PreferenceUtils preferenceUtils;
    String sessionid, newsid,userid;
    int listposition;
    ProgressDialog progressDialog;
    LinearLayout commentlayout;
    TextView commentsviewmore,
            listdtdate,listdtviews,listdtcomment,listdttitle,listdtdescription,commentscount,fo_unfollow,likecount;
    ArrayList<CommentsModel>commentsModels;
    LinearLayout followlayout,dtviewslayout;
    String IPaddress,imagepath,videopath,date,viewcount,commentcount,description,title,news_from,videolink="";
    LinearLayout bottom_dialog;
    View listdtview;
    TextView optionshare,optioncomment,optionfollow;
    EditText edcomments;
    AlertDialog commentdialog;
    ArrayList<String> imagesarray = new ArrayList<String>();
    LinearLayout layout,pagerLayout;
    ViewPager page;
    ViewPagerAdapter adapter;
    private TextView[] dots;
    RecyclerView rcView;
    Comments3Adapter comments3Adapter;
    RelativeLayout viewpaerlayout,likeslayout;
    TabLayout tablayout;
    Handler handler;
    public static boolean isFullscreenclick=false;

    ProgressBar bar;
    SeekBar sbar;
    Runnable runnable;
    ImageView play,pause,forward,backward;
    RelativeLayout videoLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        detailsback = (ImageView) findViewById(R.id.detailsback);
        edcomment = (EditText) findViewById(R.id.edcomment);
        commentsend = (ImageView) findViewById(R.id.commentsend);
        commentlayout = (LinearLayout) findViewById(R.id.commentlayout);

        commentsviewmore = (TextView) findViewById(R.id.commentsviewmore);
        followlayout=(LinearLayout)findViewById(R.id.followlayout);
        dtviewslayout = (LinearLayout) findViewById(R.id.dtviewslayout);
        dtimgivew = (ImageView) findViewById(R.id.dtimgivew);
        dtvideoimg = (ImageView) findViewById(R.id.dtvideoimg);
        simpleExoPlayer =findViewById(R.id.exoplayer1);
        listdtdate = (TextView) findViewById(R.id.listdtdate);
        listdtviews = (TextView) findViewById(R.id.listdtviews);
        listdtcomment = (TextView) findViewById(R.id.listdtcomment);
        listdttitle = (TextView) findViewById(R.id.listdttitle);
        listdtdescription = (TextView) findViewById(R.id.listdtdescription);
        listdtsharearrow = (ImageView) findViewById(R.id.listdtsharearrow);
        listdtoptmenu = (ImageView) findViewById(R.id.listdtoptmenu);
        bottom_dialog=(LinearLayout) findViewById(R.id.bottom_dialog);
        listdtview = (View) findViewById(R.id.listdtview);
        optionfollow = (TextView) findViewById(R.id.optionfollow);
        optionshare = (TextView) findViewById(R.id.optionshare);
        optioncomment = (TextView) findViewById(R.id.optioncomment);
        page = (ViewPager) findViewById(R.id.viewpager);
        rcView=(RecyclerView)findViewById(R.id.rcView);
        commentscount=(TextView)findViewById(R.id.commentscount);
        tablayout=(TabLayout)findViewById(R.id.tablayout);
        viewpaerlayout=(RelativeLayout)findViewById(R.id.viewpaerlayout);
        fo_unfollow=(TextView)findViewById(R.id.fo_unfollow);
        unlikeimage=(ImageView)findViewById(R.id.unlikeimage);
        likeimage=(ImageView)findViewById(R.id.likeimage);
        likecount=(TextView)findViewById(R.id.likecount);
        likeslayout=(RelativeLayout)findViewById(R.id.likeslayout);
        videoLayout=(RelativeLayout)findViewById(R.id.videoLayout);

        handler=new Handler();
        detailsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        preferenceUtils = new PreferenceUtils(ListDetailsActivity.this);
        progressDialog = new ProgressDialog(ListDetailsActivity.this);

        commentsModels = new ArrayList<CommentsModel>();

        sessionid = preferenceUtils.getSessionId();

        comments3Adapter=new Comments3Adapter(ListDetailsActivity.this,commentsModels);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ListDetailsActivity.this);
        rcView.setAdapter(comments3Adapter);
        rcView.setItemAnimator(new DefaultItemAnimator());
        rcView.setLayoutManager(layoutManager);

        NetwordDetect();

        if(getIntent().getExtras()!=null){
            newsid = getIntent().getExtras().getString("newsid");
            userid = getIntent().getExtras().getString("userid");

            if (getIntent().getExtras().containsKey("listposition")){
                listposition=getIntent().getExtras().getInt("listposition");
            }

            FollowersList();

            if(getIntent().getExtras().getString("image")!=null &&
                    !getIntent().getExtras().getString("image").equals("")
                    && !getIntent().getExtras().getString("image").equals("null")){

                 dtvideoimg.setVisibility(View.GONE);
                imagepath = getIntent().getExtras().getString("image");
                dtimgivew.setVisibility(View.VISIBLE);
                 simpleExoPlayer.setVisibility(View.GONE);
                Glide.with(ListDetailsActivity.this).load(imagepath)
                        .asBitmap().placeholder(R.drawable.loading)
                        .into(dtimgivew);
                ViewsPost();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ViewsPost5secs();
                    }
                },5000);
            } else if(getIntent().getExtras().getString("videopath")!=null && ! getIntent().getExtras().getString
                    ("videopath").equals("")){
                dtvideoimg.setVisibility(View.VISIBLE);
                videopath = getIntent().getExtras().getString("videopath");
                dtimgivew.setVisibility(View.GONE);
                videoLayout.setVisibility(View.VISIBLE);
                simpleExoPlayer.setVisibility(View.VISIBLE);
                //videoView.setVideoURI(Uri.parse(getIntent().getExtras().getString("videopath")));
                ViewsPost();
            } else if (!Utility.isNotAnValidString(getIntent().getExtras().getString("videolink"))){
                dtvideoimg.setVisibility(View.VISIBLE);
                videolink = getIntent().getExtras().getString("videolink");
                dtimgivew.setVisibility(View.VISIBLE);
                videoLayout.setVisibility(View.GONE);
                simpleExoPlayer.setVisibility(View.VISIBLE);
                String[] thumbnail=videolink.split("=");
                Glide.with(ListDetailsActivity.this).load("https://i.ytimg.com/vi/"+thumbnail[1]+"/hqdefault.jpg")
                        .asBitmap().placeholder(R.drawable.loading)
                        .into(dtimgivew);
                ViewsPost();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ViewsPost5secs();
                    }
                },5000);
            } else if (getIntent().getExtras().containsKey("video_thumb_image")){
                if (getIntent().getExtras().getString("video_thumb_image")!=null && !getIntent().getExtras().getString
                        ("video_thumb_image").equals("") && !getIntent().getExtras().getString
                        ("video_thumb_image").equals("null")){
                    dtvideoimg.setVisibility(View.GONE);
                    imagepath = getIntent().getExtras().getString("video_thumb_image");
                    dtimgivew.setVisibility(View.VISIBLE);
                    videoLayout.setVisibility(View.GONE);
                    Glide.with(ListDetailsActivity.this).load(imagepath)
                            .asBitmap().placeholder(R.drawable.loading)
                            .into(dtimgivew);
                    ViewsPost();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ViewsPost5secs();
                        }
                    },5000);
            }
            } else if(getIntent().getExtras().getStringArrayList("imageslist")!= null &&
                    !getIntent().getExtras().getStringArrayList("imageslist").equals("")){

                imagesarray.addAll(getIntent().getExtras().getStringArrayList("imageslist"));
                dtvideoimg.setVisibility(View.GONE);
                if(imagesarray.size()>0){
                    if (imagesarray.size()==1){
                        videoLayout.setVisibility(View.GONE);
                        dtimgivew.setVisibility(View.VISIBLE);
                        Glide.with(ListDetailsActivity.this).load(imagesarray.get(0))
                                .asBitmap().placeholder(R.drawable.loading)
                                .into(dtimgivew);
                        ViewsPost();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ViewsPost5secs();
                            }
                        },5000);
                    }else {
                        adapter =new ViewPagerAdapter();
                        page.setAdapter(adapter);
                        tablayout.setupWithViewPager(page);
                        dtimgivew.setVisibility(View.GONE);
                        videoLayout.setVisibility(View.GONE);
                        viewpaerlayout.setVisibility(View.VISIBLE);
                        ViewsPost();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ViewsPost5secs();
                            }
                        },5000);
                    }
                }
            } else if(imagesarray.size()==0 && (imagepath==null || imagepath.equals(""))
                    && (videopath==null ||   videopath.equals(""))
                    && (videolink==null || videolink.equals(""))){
                dtimgivew.setVisibility(View.GONE);
                videoLayout.setVisibility(View.GONE);
                dtvideoimg.setVisibility(View.GONE);
                ViewsPost();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ViewsPost5secs();
                    }
                },5000);
            }
            if(getIntent().getExtras().getString("date")!=null){
                date = getIntent().getExtras().getString("date");
                listdtdate.setText(date);
            }
            if(getIntent().getExtras().getString("viewscount")!=null){
                listdtviews.setText(getIntent().getExtras().getString("viewscount"));
            }
            if(getIntent().getExtras().getString("commentcount")!=null){
                listdtcomment.setText(getIntent().getExtras().getString("commentcount"));
            }
            if(getIntent().getExtras().getString("title")!=null){
                String title = getIntent().getExtras().getString("title");
                listdttitle.setText(Html.fromHtml(String.valueOf(Html.fromHtml(title))));
            }
            if(getIntent().getExtras().getString("description")!=null){
                String desc =getIntent().getExtras().getString("description");
                listdtdescription.setText(Html.fromHtml(String.valueOf(Html.fromHtml(desc))));
            }
            if (getIntent().getExtras().containsKey("likescount")){
                likecount.setText(getIntent().getExtras().getString("likescount"));
            }
            if (getIntent().getExtras().containsKey("news_from")){
                news_from=getIntent().getExtras().getString("news_from");
            }
        }

        new CountDownTimer(20000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
               /* timertxt.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));*/
            }
            @Override
            public void onFinish() {
                Post20secsView();
            }
        }.start();

       page.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        newsViewSave();
        NewsLikeRetrive();
        CommentsGet();

        listdtsharearrow.setOnClickListener(this);
        listdtview.setOnClickListener(this);
        listdtoptmenu.setOnClickListener(this);
        dtvideoimg.setOnClickListener(this);
        commentsend.setOnClickListener(this);
        followlayout.setOnClickListener(this);
        commentlayout.setOnClickListener(this);
        optionfollow.setOnClickListener(this);
        optionshare.setOnClickListener(this);
        optioncomment.setOnClickListener(this);
        likeslayout.setOnClickListener(this);

        Exoplayer.getSharedInstance(ListDetailsActivity.this).setPlayerListener(new CallBacks.playerCallBack() {
            @Override
            public void onItemClickOnItem(Integer albumId) {

            }

            @Override
            public void onPlayingEnd() {
                dtvideoimg.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.likeslayout:
                if (Utility.isNotAnValidString(preferenceUtils.getSessionId())) {
                    Toast.makeText(ListDetailsActivity.this, "Please Make Login to Post Like", Toast.LENGTH_LONG).show();
                }else {
                    postlike();
                }
                break;
            case R.id.listdtsharearrow:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = news_from;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "News From");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.listdtview:
                bottom_dialog.setVisibility(View.GONE);
                listdtview.setVisibility(View.GONE);
                break;
            case R.id.listdtoptmenu:
                OptionsMenuSelected();
                break;
            case R.id.dtvideoimg:
                if (!Utility.isNotAnValidString(videolink)){
                    Intent intent=new Intent(ListDetailsActivity.this,VideoPlayActvity.class);
                    String[] thumbnail=videolink.split("=");
                    intent.putExtra("video",thumbnail[1]);
                    startActivityForResult(intent,123);
                }else {
                    dtvideoimg.setVisibility(View.GONE);
                    simpleExoPlayer.setPlayer(Exoplayer.getSharedInstance(ListDetailsActivity.this).getSimpleExoPlayerView().getPlayer());
                    Exoplayer.getSharedInstance(ListDetailsActivity.this).playStream(videopath);
                }
                PlayPost();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playpost5sec();
                    }
                },5000);
                break;
            case R.id.commentsend:
                if (Utility.isNotAnValidString(preferenceUtils.getSessionId())) {
                    Toast.makeText(ListDetailsActivity.this, "Please Make Login to Post Comment", Toast.LENGTH_LONG).show();
                }else {
                    if (edcomment.getText().toString().length() == 0) {
                        edcomment.setError("Please Enter Comments");
                    } else {
                        CommentPost(newsid, edcomment.getText().toString());
                        edcomment.setText("");
                           hideKeyboard();
                    }
                }
                break;
            case R.id.followlayout:
                if (Utility.isNotAnValidString(preferenceUtils.getSessionId())) {
                    Toast.makeText(ListDetailsActivity.this, "Please Make Login to Make Follow", Toast.LENGTH_LONG).show();
                }else {
                    FollowPost();
                }
                break;
            case R.id.commentlayout:
                /*Intent i = new Intent(ListDetailsActivity.this, RecomendedList.class);
                startActivity(i);*/
               /* i.putExtra("newsid", newsid);
                startActivityForResult(i,100);*/
                break;
            case R.id.optionfollow:
                if (Utility.isNotAnValidString(preferenceUtils.getSessionId())){
                    Toast.makeText(ListDetailsActivity.this,"Please Make Login to Make Follow",Toast.LENGTH_LONG).show();
                }else {
                    FollowPost();
                }
                break;
            case R.id.optionshare:
                Intent sharingIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent1.setType("text/plain");
                String shareBody1= "Testing Share";
                sharingIntent1.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
                sharingIntent1.putExtra(android.content.Intent.EXTRA_TEXT, shareBody1);
                startActivity(Intent.createChooser(sharingIntent1, "Share via"));
                break;
            case R.id.optioncomment:
                if (Utility.isNotAnValidString(preferenceUtils.getSessionId())) {
                    Toast.makeText(ListDetailsActivity.this, "Please Make Login to Post Comment", Toast.LENGTH_LONG).show();
                }else {
                    TextView commentsave, commentcancel;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListDetailsActivity.this);
                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    final View dialoglayout = layoutInflater.inflate(R.layout.commentpopup, null);
                    builder.setView(dialoglayout);

                    edcomments = (EditText) dialoglayout.findViewById(R.id.edcomments);
                    commentsave = (TextView) dialoglayout.findViewById(R.id.commentsave);
                    commentcancel = (TextView) dialoglayout.findViewById(R.id.commentcancel);

                    commentsave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (edcomments.getText().toString().length() == 0) {
                                edcomments.setError("Please Enter Comments");
                            } else {
                                CommentPost(newsid, edcomments.getText().toString());
                                edcomments.setText("");
                                hideKeyboard();
                                commentdialog.dismiss();
                            }
                        }
                    });

                    commentcancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            commentdialog.dismiss();
                        }
                    });
                    commentdialog = builder.create();
                    commentdialog.setCanceledOnTouchOutside(false);
                    commentdialog.setCancelable(false);
                    commentdialog.show();

                    break;
                }
        }
    }

    public void postlike(){
        final String sessionid=preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")){
            Toast.makeText(ListDetailsActivity.this,"Please make login",Toast.LENGTH_LONG).show();
        }else {
            try {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("authenticate_key", "abcd123XYZ")
                                .header("sessionid",sessionid)
                                .header("newsid", newsid)
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
                APICalls.SaveLike apiService = retrofit.create(APICalls.SaveLike.class);
                Call<ResponseBody> call = apiService.savelike();
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
                                    if (jsonObject1.getString("message").equals("News unlike successful")){
                                        likeimage.setVisibility(View.INVISIBLE);
                                        unlikeimage.setVisibility(View.VISIBLE);
                                    }else if (jsonObject1.getString("message").equals("News like successful.")){
                                        likeimage.setVisibility(View.VISIBLE);
                                        unlikeimage.setVisibility(View.INVISIBLE);
                                    }
                                    NewsLikeRetrive();
                                }else{
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(ListDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
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
    }

    private void NewsLikeRetrive(){
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("limit", "all")
                            .header("newsid", newsid)
                            .header("action", "news_like")
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
            APICalls.NewsEffectiveGet apiService= retrofit.create(APICalls.NewsEffectiveGet.class);
            Call<ResponseBody> call = apiService.newsEffectiveGet();
            // showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = response.body().string();
                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.has("response")) {
                            JSONObject jsonObject1=jsonObject.getJSONObject("response");
                            if (jsonObject1.getString("status").equals("error")){
                                likecount.setText("0");
                                //Toast.makeText(ListDetailsActivity.this,jsonObject1.getString("message"),Toast.LENGTH_LONG).show();
                            }else if (jsonObject1.getString("status").equals("success")){
                                String id=preferenceUtils.getUserId();
                                JSONArray jsonArray=new JSONArray(jsonObject1.getString("data"));
                                likecount.setText(String.valueOf(jsonArray.length()));
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject2=jsonArray.getJSONObject(i);
                                    if (id.equals(jsonObject2.getString("user_id"))){
                                        likeimage.setVisibility(View.VISIBLE);
                                        unlikeimage.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        }
                    }catch (Exception e){
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

    private void FollowersList() {
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
                                ArrayList<String> followerslist=new ArrayList<String>();
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    followerslist.add(dataobj.getString("follow_user_id"));
                                }
                                if (followerslist.contains(userid)){
                                    fo_unfollow.setText("Un Follow");
                                }else {
                                    fo_unfollow.setText("Follow");
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

    private void CommentPost(final String newsid, final String comment) {

        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    String sessionid = preferenceUtils.getSessionId();
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("sessionid",sessionid)
                            .header("newsid",newsid)
                            .header("comment",comment)
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
            APICalls.PostComment apiService= retrofit.create(APICalls.PostComment.class);
            Call<ResponseBody> call = apiService.PostComment();
            // showProgressdialog();
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
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                String message = obj.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                CommentsGetTotal();
                                CommentsGet();
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

    private void Post20secsView() {
        final String sessionid = preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")) {
            //Toast.makeText(ListDetailsActivity.this, "Please Make Login", Toast.LENGTH_LONG).show();
        } else {
            try {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("authenticate_key", "abcd123XYZ")
                                .header("newsid", newsid)
                                .header("sessionid", sessionid)
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
                APICalls.PostViewearning apiService = retrofit.create(APICalls.PostViewearning.class);
                Call<ResponseBody> call = apiService.PostViewearning();
                //  showProgressdialog();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        //  dismissProgressDislogue();
                        try {
                            String s = response.body().string();
                            JSONObject jObject = new JSONObject(s);
                            if (jObject.has("response")) {
                                JSONObject jsonObject = jObject.getJSONObject("response");
                                if (jsonObject.getString("status").equals("success")) {
                                    String message = jsonObject.getString("message");
                                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                } else {
                                    String message = jsonObject.getString("message");
                                    //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
    }

    private void ViewsPost5secs() {
        final String sessionid = preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")) {
            //Toast.makeText(ListDetailsActivity.this, "Please Make Login", Toast.LENGTH_LONG).show();
        } else {
            try {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("authenticate_key", "abcd123XYZ")
                                .header("sessionid", sessionid)
                                .header("newsid", newsid)
                                .header("ip_address", IPaddress)
                                .header("action", "news_effective_view")
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
                APICalls.ViewsPost apiService = retrofit.create(APICalls.ViewsPost.class);
                Call<ResponseBody> call = apiService.ViewsPost();
              //  showProgressdialog();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                      //  dismissProgressDislogue();
                        try {
                            String s = response.body().string();
                            JSONObject jObject = new JSONObject(s);
                            if (jObject.has("response")) {
                                JSONObject jsonObject = jObject.getJSONObject("response");
                                if (jsonObject.getString("status").equals("success")) {
                                    String message = jsonObject.getString("message");
                                     //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                } else {
                                    String message = jsonObject.getString("message");
                                 //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewsPost5secs();
                                    }
                                },5000);
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
    }

    private void NetwordDetect() {

        boolean WIFI = false;

        boolean MOBILE = false;

        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true;
        }

        if(WIFI == true) {
            IPaddress = GetDeviceipWiFiData();
        }

        if(MOBILE == true) {
            IPaddress = GetDeviceipMobileData();
        }
    }

    private void ViewsPost() {
        final String sessionid = preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")) {
          //  Toast.makeText(ListDetailsActivity.this, "Please Make Login", Toast.LENGTH_LONG).show();
        } else {
            try {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("authenticate_key", "abcd123XYZ")
                                .header("sessionid", sessionid)
                                .header("newsid", newsid)
                                .header("ip_address", IPaddress)
                                .header("action", "news_click")
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
                APICalls.ViewsPost apiService = retrofit.create(APICalls.ViewsPost.class);
                Call<ResponseBody> call = apiService.ViewsPost();
              //  showProgressdialog();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                     //   dismissProgressDislogue();
                        try {
                            String s = response.body().string();
                            JSONObject jObject = new JSONObject(s);
                            if (jObject.has("response")) {
                                JSONObject jsonObject = jObject.getJSONObject("response");
                                if (jsonObject.getString("status").equals("success")) {
                                    String message = jsonObject.getString("message");
                                   // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                } else {
                                    String message = jsonObject.getString("message");
                                   // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                      //  dismissProgressDislogue();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void PlayPost() {
        final String sessionid = preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")) {
            //  Toast.makeText(ListDetailsActivity.this, "Please Make Login", Toast.LENGTH_LONG).show();
        } else {
            try {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("authenticate_key", "abcd123XYZ")
                                .header("sessionid", sessionid)
                                .header("newsid", newsid)
                                .header("ip_address", IPaddress)
                                .header("action", "news_play")
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
                APICalls.ViewsPost apiService = retrofit.create(APICalls.ViewsPost.class);
                Call<ResponseBody> call = apiService.ViewsPost();
             //   showProgressdialog();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                     //   dismissProgressDislogue();
                        try {
                            String s = response.body().string();
                            JSONObject jObject = new JSONObject(s);
                            if (jObject.has("response")) {
                                JSONObject jsonObject = jObject.getJSONObject("response");
                                if (jsonObject.getString("status").equals("success")) {
                                    String message = jsonObject.getString("message");
                                    // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                } else {
                                    String message = jsonObject.getString("message");
                                  //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
    }

    private void playpost5sec() {
        final String sessionid = preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")) {
            //  Toast.makeText(ListDetailsActivity.this, "Please Make Login", Toast.LENGTH_LONG).show();
        } else {
            try {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("authenticate_key", "abcd123XYZ")
                                .header("sessionid", sessionid)
                                .header("newsid", newsid)
                                .header("ip_address", IPaddress)
                                .header("action", "news_effective_play")
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
                APICalls.ViewsPost apiService = retrofit.create(APICalls.ViewsPost.class);
                Call<ResponseBody> call = apiService.ViewsPost();
               // showProgressdialog();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                     //   dismissProgressDislogue();
                        try {
                            String s = response.body().string();
                            JSONObject jObject = new JSONObject(s);
                            if (jObject.has("response")) {
                                JSONObject jsonObject = jObject.getJSONObject("response");
                                if (jsonObject.getString("status").equals("success")) {
                                    String message = jsonObject.getString("message");
                                     //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                } else {
                                    String message = jsonObject.getString("message");
                                   // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        playpost5sec();
                                    }
                                },5000);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                      //  dismissProgressDislogue();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void CommentsGet() {
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
                            /*.header("sessionid", sessionid)*/
                            .header("newsid", newsid)
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
            APICalls.GetComments apiService= retrofit.create(APICalls.GetComments.class);
            Call<ResponseBody> call = apiService.GetComments();
           // showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = response.body().string();
                        postCommentData(s);
                    }catch (Exception e){
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

    private void CommentsGetTotal() {
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
                            .header("newsid", newsid)
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
            APICalls.GetComments apiService= retrofit.create(APICalls.GetComments.class);
            Call<ResponseBody> call = apiService.GetComments();
            // showProgressdialog();
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
                            } else {
                                if (obj.getString("status").equals("success")) {
                                    JSONArray jsonArray = new JSONArray(obj.getString("data"));
                                    int count=0;
                                    for (int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                                        if (jsonObject.getString("post_id").equals(newsid)){
                                            count++;
                                        }
                                    }
                                    listdtcomment.setText(String.valueOf(count));
                                }
                            }
                        }

                    }catch (Exception e){
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

    private void newsViewSave() {
        final String sessionid = preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")) {
            //  Toast.makeText(ListDetailsActivity.this, "Please Make Login", Toast.LENGTH_LONG).show();
        } else {
            try {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("authenticate_key", "abcd123XYZ")
                                .header("sessionid", sessionid)
                                .header("newsid", newsid)
                                .header("ip_address",IPaddress )
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
                APICalls.ViewsPost apiService = retrofit.create(APICalls.ViewsPost.class);
                Call<ResponseBody> call = apiService.ViewsPost();
                //  showProgressdialog();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        //   dismissProgressDislogue();
                        try {
                            String s = response.body().string();
                            JSONObject jObject = new JSONObject(s);
                            if (jObject.has("response")) {
                                JSONObject jsonObject = jObject.getJSONObject("response");
                                if (jsonObject.getString("status").equals("success")) {
                                    String message = jsonObject.getString("message");
                                    // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                } else {
                                    String message = jsonObject.getString("message");
                                    // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        //  dismissProgressDislogue();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*private void postCommentData(String s) {
        try {
            JSONObject jObject = new JSONObject(s);
            commentsModels.clear();
            if(jObject.has("response")) {
                JSONObject obj = jObject.getJSONObject("response");
                if (obj.getString("status").equals("error")) {
                    String message = obj.getString("message");
                    commentlayout.setVisibility(View.GONE);
                } else {
                    String message = obj.getString("message");
                    JSONArray jsonArray = new JSONArray(obj.getString("data"));
                    if(jsonArray.length()>0) {
                        commentlayout.setVisibility(View.VISIBLE);
                        commentscount.setText(jsonArray.length() + " Comments");
                        int k=0;
                        for (int j = 0; j<jsonArray.length(); j++) {
                            int replycount=0;
                            CommentsModel commentsModel = new CommentsModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(j);

                            String commentid=jsonObject.getString("comment_id");
                            for (int m=0;m<jsonArray.length();m++){
                                JSONObject reply= jsonArray.getJSONObject(m);
                                if (reply.getString("parent_comment_id").equals(commentid)){
                                    replycount++;
                                }
                            }
                            commentsModel.setReplycount(String.valueOf(replycount));

                            if (jsonObject.getString("parent_comment_id").equals("0")){
                                k++;
                                if (k>3) {
                                  break;
                                }
                                commentsModel.setDate(jsonObject.getString("created"));
                                commentsModel.setComment(jsonObject.getString("comment"));
                                commentsModel.setUserid(jsonObject.getString("userid"));
                                commentsModel.setCommentid(jsonObject.getString("comment_id"));
                                commentsModel.setName(jsonObject.getString("comment_user_name"));
                                commentsModel.setImage(jsonObject.getString("comment_user_image"));
                                commentsModel.setPostid(jsonObject.getString("post_id"));
                                commentsModels.add(commentsModel);
                            }
                        }
                    }else{
                        commentlayout.setVisibility(View.GONE);
                    }
                    comments3Adapter.notifyDataSetChanged();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    private void postCommentData(String s) {
        try {
            //dismissProgressDislogue();
            JSONObject jObject = new JSONObject(s);
            commentsModels.clear();
            if(jObject.has("response")) {
                JSONObject obj = jObject.getJSONObject("response");
                if (obj.getString("status").equals("error")) {
                    String message = obj.getString("message");
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    String message = obj.getString("message");
                    JSONArray jsonArray = new JSONArray(obj.getString("data"));
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            int replycount=0;
                            CommentsModel model = new CommentsModel();
                            JSONObject dataobj3 = jsonArray.getJSONObject(i);
                            String commentid=dataobj3.getString("comment_id");
                            for (int k=0;k<jsonArray.length();k++){
                                JSONObject reply= jsonArray.getJSONObject(k);
                                if (reply.getString("parent_comment_id").equals(commentid)){
                                    replycount++;
                                }
                            }
                            model.setReplycount(String.valueOf(replycount));
                            if (dataobj3.getString("parent_comment_id").equals("0")) {
                                model.setUserid(dataobj3.getString("userid"));
                                model.setPostid(dataobj3.getString("post_id"));
                                model.setComment(dataobj3.getString("comment"));
                                model.setIsactive(dataobj3.getString("is_active"));
                                model.setDate(dataobj3.getString("created"));
                                model.setCommentid(dataobj3.getString("comment_id"));
                                model.setModifieddate(dataobj3.getString("modified"));
                                model.setName(dataobj3.getString("comment_user_name"));
                                model.setImage(dataobj3.getString("comment_user_image"));
                                if (dataobj3.getString("parent_comment_id").equals("0")) {
                                    model.setParent_comment_id(dataobj3.getString("parent_comment_id"));
                                } else {
                                    //m++;
                                }
                                commentsModels.add(model);
                            }
                        }
                        comments3Adapter.notifyDataSetChanged();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(ListDetailsActivity.this);
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

    private void FollowPost() {
        final String sessionid = preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")) {
            Toast.makeText(ListDetailsActivity.this, "Please Make Login", Toast.LENGTH_LONG).show();
        } else {
            try {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("authenticate_key", "abcd123XYZ")
                                .header("sessionid", sessionid)
                                .header("follow_user_id", userid)
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
                APICalls.MakeFollow apiService = retrofit.create(APICalls.MakeFollow.class);
                Call<ResponseBody> call = apiService.makefollow();
                showProgressdialog();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dismissProgressDislogue();
                        try {
                            String s = response.body().string();
                            JSONObject jObject = new JSONObject(s);
                            if (jObject.has("response")) {
                                JSONObject jsonObject = jObject.getJSONObject("response");
                                if (jsonObject.getString("status").equals("success")) {
                                    if (jsonObject.getString("message").equals("Unfollowed successful")){
                                        fo_unfollow.setText("Follow");
                                    }else if (jsonObject.getString("message").equals("Follwed successfully.")){
                                        fo_unfollow.setText("Un Follow");
                                    }
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
    }

    public String GetDeviceipMobileData(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }

    public String GetDeviceipWiFiData() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        @SuppressWarnings("deprecation")
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }

    public void OptionsMenuSelected(){
        if (bottom_dialog.getVisibility()==View.VISIBLE){
            bottom_dialog.setVisibility(View.GONE);
            listdtview.setVisibility(View.GONE);
        }else {
            bottom_dialog.setVisibility(View.VISIBLE);
            listdtview.setVisibility(View.VISIBLE);
        }
    }

    /*@Override
    public void onPrepared(MediaPlayer mp) {

        simpleExoPlayer.start();
    }*/

    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imagesarray.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            // TODO Auto-generated method stub
            return view == obj;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub

            ImageView imageView = new ImageView(ListDetailsActivity.this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with(ListDetailsActivity.this).load(imagesarray.get(position))
                    //  .placeholder(R.drawable.loadingplaceholder)
                    .into(imageView);

            //    imageView.setImageResource(imagesarray.get(position));
            ((ViewPager) container).addView(imageView, 0);
           // adapter.notifyDataSetChanged();
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            View view = (View) object;
            container.removeView(view);
        }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) { }

        @Override
        public void onPageScrollStateChanged(int arg0) { }
    };

    @SuppressLint("NewApi")
    private void changeStatusBarColor() {
        // TODO Auto-generated method stub
    }

    private void addBottomDots(int currentPage) {
        // TODO Auto-generated method stub

        dots = new TextView[imagesarray.size()];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorInActive = getResources().getIntArray(R.array.array_dot_inactive);
        int[] colorInActive1 = getResources().getIntArray(R.array.array_dot_inactive);
        layout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(ListDetailsActivity.this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#9b9797"));
            //colorInActive[currentPage]);
            layout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[currentPage].setTextColor(Color.parseColor("#7DBA34"));
            //colorsActive[currentPage]);
        }
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacksAndMessages(null);
        /*startActivity(new Intent(ListDetailsActivity.this,OGAMainActivity.class));*/
        Intent intent=new Intent();
        intent.putExtra("listposition",listposition);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void hideKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if(v!=null){
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100 && resultCode==RESULT_OK){
            CommentsGet();
        }else if (requestCode==123 && requestCode==RESULT_OK){

        }
    }
}
