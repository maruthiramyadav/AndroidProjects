package com.devobal.ogabuzz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.devobal.ogabuzz.SpecialClasses.CallBacks;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.SpecialClasses.Utility;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

public class RecomendedDetails extends AppCompatActivity implements View.OnClickListener{

    ImageView detailsback,dtimgivew,dtvideoimg;
    TextView listdtdate,listdttitle,listdtdescription,fo_unfollow;
    LinearLayout followlayout;
    String videopath,videolink,image,userid,sessionid;
    ArrayList<String> imagesarray;
    SimpleExoPlayerView exoplayer1;
    RelativeLayout videoLayout;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomended_details);

        detailsback=(ImageView)findViewById(R.id.detailsback);
        dtimgivew=(ImageView)findViewById(R.id.dtimgivew);
        dtvideoimg=(ImageView)findViewById(R.id.dtvideoimg);
        listdtdate=(TextView)findViewById(R.id.listdtdate);
        followlayout=(LinearLayout)findViewById(R.id.followlayout);
        exoplayer1 =findViewById(R.id.exoplayer1);
        videoLayout=(RelativeLayout)findViewById(R.id.videoLayout);
        listdtdescription=(TextView)findViewById(R.id.listdtdescription);
        listdttitle=(TextView)findViewById(R.id.listdttitle);
        fo_unfollow=(TextView)findViewById(R.id.fo_unfollow);
        imagesarray=new ArrayList<String>();
        progressDialog=new ProgressDialog(RecomendedDetails.this);

        preferenceUtils=new PreferenceUtils(RecomendedDetails.this);
        sessionid=preferenceUtils.getSessionId();

        if (getIntent().getExtras()!=null){
            videopath=getIntent().getStringExtra("videopath");
            videolink=getIntent().getStringExtra("videolink");
            image=getIntent().getStringExtra("image");
            imagesarray=getIntent().getStringArrayListExtra("imagesarray");
            listdttitle.setText(getIntent().getStringExtra("title"));
            listdtdescription.setText(getIntent().getStringExtra("description"));
            userid=getIntent().getStringExtra("userid");
        }

        if (!Utility.isNotAnValidString(image)){
            Glide.with(RecomendedDetails.this).load(image).asBitmap().placeholder(R.drawable.loading).into(dtimgivew);
            dtimgivew.setVisibility(View.VISIBLE);
            dtvideoimg.setVisibility(View.GONE);
            videoLayout.setVisibility(View.GONE);
        }else if (!Utility.isNotAnValidString(videopath)){
            dtimgivew.setVisibility(View.GONE);
            dtvideoimg.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.VISIBLE);
        }else if (!Utility.isNotAnValidString(videolink)){
            String[] thumbnail=videolink.split("=");
            Glide.with(RecomendedDetails.this).load("https://i.ytimg.com/vi/"+thumbnail[1]+"/hqdefault.jpg")
                    .asBitmap().placeholder(R.drawable.loading)
                    .into(dtimgivew);
            dtimgivew.setVisibility(View.VISIBLE);
            dtvideoimg.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.GONE);
        }else if (imagesarray.size()!=0){
            dtimgivew.setVisibility(View.GONE);
            dtvideoimg.setVisibility(View.GONE);
            videoLayout.setVisibility(View.GONE);
        }

        Exoplayer.getSharedInstance(RecomendedDetails.this).setPlayerListener(new CallBacks.playerCallBack() {
            @Override
            public void onItemClickOnItem(Integer albumId) {

            }

            @Override
            public void onPlayingEnd() {
                dtvideoimg.setVisibility(View.VISIBLE);
            }
        });

        dtvideoimg.setOnClickListener(this);
        fo_unfollow.setOnClickListener(this);

        FollowersList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dtvideoimg:
                if (!Utility.isNotAnValidString(videolink)){
                    Intent intent=new Intent(RecomendedDetails.this,VideoPlayActvity.class);
                    String[] thumbnail=videolink.split("=");
                    intent.putExtra("video",thumbnail[1]);
                    startActivityForResult(intent,123);
                }else {
                    dtvideoimg.setVisibility(View.GONE);
                    exoplayer1.setPlayer(Exoplayer.getSharedInstance(RecomendedDetails.this).getSimpleExoPlayerView().getPlayer());
                    Exoplayer.getSharedInstance(RecomendedDetails.this).playStream(videopath);
                }
                break;
            case R.id.fo_unfollow:
                if (Utility.isNotAnValidString(preferenceUtils.getSessionId())) {
                    Toast.makeText(RecomendedDetails.this, "Please Make Login to Make Follow", Toast.LENGTH_LONG).show();
                }else {
                    FollowPost();
                }
                break;
            case R.id.detailsback:
                finish();
                break;
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

    private void FollowPost() {
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")) {
            Toast.makeText(RecomendedDetails.this, "Please Make Login", Toast.LENGTH_LONG).show();
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
    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(RecomendedDetails.this);
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
