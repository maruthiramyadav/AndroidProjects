package com.devobal.ogabuzz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.SpecialClasses.InitApplication;
import com.devobal.ogabuzz.Modals.ListOfPagesModel;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.devobal.ogabuzz.SpecialClasses.Sample;

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

public class OGAMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,View.OnClickListener {

    //ImageView nav_menu,search;
    //TabLayout tabtitles;
   // ViewPager fragviewpager;
    ArrayList<ListOfPagesModel> listOfPagesModels;
    FragTitlesAdapter titlesAdapter;
    TextView signin,loggedname;
    CircleImageView profileimg;
    LinearLayout fallowlayout;
    LinearLayout homelayout,videoLayout,followlayout,favoritelayout,settingslayout,helplayout,changepwdlayout,recomendlayout,
            footerhomelayout,foottrendvideolayout,footernewslayout,notificationlayout;
    TextView hometext,trendtext,trendnewstext,notitext;
    ImageView footerhome,trendvideo,trendnews,notificationimg;
    AlertDialog logoutdialog;
    FrameLayout main_content;
    ImageView listtypeview,gridtypeview;
    PreferenceUtils preferenceUtils;
    String sessionid;
    ProgressDialog progressDialog;

   @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (InitApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_ogamain);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        signin = (TextView) findViewById(R.id.signin);
        loggedname = (TextView) findViewById(R.id.loggedname);
        profileimg = (CircleImageView) findViewById(R.id.profileimg);

        preferenceUtils = new PreferenceUtils(OGAMainActivity.this);
        progressDialog = new ProgressDialog(OGAMainActivity.this);
        homelayout = (LinearLayout) findViewById(R.id.homelayout);
        videoLayout = (LinearLayout) findViewById(R.id.videoLayout);
        followlayout = (LinearLayout) findViewById(R.id.followlayout);
        favoritelayout = (LinearLayout) findViewById(R.id.favoritelayout);
        settingslayout = (LinearLayout) findViewById(R.id.settingslayout);
        helplayout = (LinearLayout) findViewById(R.id.helplayout);
        changepwdlayout = (LinearLayout) findViewById(R.id.changepwdlayout);
        recomendlayout = (LinearLayout) findViewById(R.id.recomendlayout);

        footerhomelayout = (LinearLayout) findViewById(R.id.footerhomelayout);
        foottrendvideolayout = (LinearLayout) findViewById(R.id.foottrendvideolayout);
        footernewslayout = (LinearLayout) findViewById(R.id.footernewslayout);
        notificationlayout = (LinearLayout) findViewById(R.id.notificationlayout);

        footerhome = (ImageView) findViewById(R.id.footerhome);
        trendvideo = (ImageView) findViewById(R.id.trendvideo);
        trendnews = (ImageView) findViewById(R.id.trendnews);
        notificationimg = (ImageView) findViewById(R.id.notificationimg);

        hometext = (TextView) findViewById(R.id.hometext);
        trendtext = (TextView) findViewById(R.id.trendtext);
        trendnewstext = (TextView) findViewById(R.id.trendnewstext);
        notitext = (TextView) findViewById(R.id.notitext);
        main_content = (FrameLayout) findViewById(R.id.main_content);

        listtypeview = (ImageView) findViewById(R.id.listtypeview);
        gridtypeview = (ImageView) findViewById(R.id.gridtypeview);

        sessionid = preferenceUtils.getSessionId();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        titlesAdapter = new FragTitlesAdapter(getSupportFragmentManager());

        listOfPagesModels = new ArrayList<ListOfPagesModel>();

       updateNavigationBar();
       LoggedUserGet();
       appliedTabView(R.id.footerhomelayout);

        profileimg.setOnClickListener(this);
        settingslayout.setOnClickListener(this);
        helplayout.setOnClickListener(this);
        changepwdlayout.setOnClickListener(this);
        recomendlayout.setOnClickListener(this);
        homelayout.setOnClickListener(this);
        videoLayout.setOnClickListener(this);
        footerhomelayout.setOnClickListener(this);
        foottrendvideolayout.setOnClickListener(this);
        footernewslayout.setOnClickListener(this);
        notificationlayout.setOnClickListener(this);
        followlayout.setOnClickListener(this);
        favoritelayout.setOnClickListener(this);
        signin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profileimg:
                if (PreferenceUtils.getBooleanFromSP(OGAMainActivity.this, "LoginStatus")) {
                    Intent i = new Intent(OGAMainActivity.this, MyProfileActivity.class);
                    startActivityForResult(i,123);
                    manageSlider();
                }else{
                    Toast toast = Toast.makeText(OGAMainActivity.this,"Please Login to View Profile", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case R.id.settingslayout:
                Intent i = new Intent(OGAMainActivity.this, SettingsActivity.class);
                startActivityForResult(i,99);
                manageSlider();
                break;
            case R.id.helplayout:
                Intent i1= new Intent(OGAMainActivity.this, HelpCenterActivity.class);
                startActivity(i1);
                manageSlider();
                break;
            case R.id.changepwdlayout:
                Intent i2 = new Intent(OGAMainActivity.this, ChangePassword.class);
                startActivity(i2);
                manageSlider();
                break;
            case R.id.recomendlayout:
                Intent i4 = new Intent(OGAMainActivity.this, RecomendedList.class);
                startActivity(i4);
                manageSlider();
                break;
            case R.id.homelayout:
                appliedTabView(R.id.footerhomelayout);
                /*startActivity(new Intent(OGAMainActivity.this, HomeFragment.class));*/
                manageSlider();
                break;
            case R.id.videoLayout:
                appliedTabView(R.id.foottrendvideolayout);
                manageSlider();
                break;
            case R.id.footerhomelayout:
                preferenceUtils.setLastVisibleItemPosition(0);
                appliedTabView(R.id.footerhomelayout);
                break;
            case R.id.foottrendvideolayout:
                appliedTabView(R.id.foottrendvideolayout);
                break;
            case R.id.footernewslayout:
                appliedTabView(R.id.footernewslayout);
                break;
            case R.id.notificationlayout:
                appliedTabView(R.id.notificationlayout);
                break;
            case R.id.followlayout:
                Intent i5 = new Intent(OGAMainActivity.this,FollowActivity.class);
                startActivity(i5);
                break;
            case R.id.favoritelayout:
                Intent i6 = new Intent(OGAMainActivity.this,FavouritesActivity.class);
                startActivity(i6);
                break;
            case R.id.signin:
                if(!PreferenceUtils.getBooleanFromSP(OGAMainActivity.this,"LoginStatus")) {
                    // signin.setText("Sign Out");
                    Intent i7 = new Intent(OGAMainActivity.this, LoginActivity.class);
                    startActivityForResult(i7,125);
                }else  {
                    TextView logoutyes,logoutno;
                    AlertDialog.Builder builder = new AlertDialog.Builder(OGAMainActivity.this);
                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    final View dialoglayout = layoutInflater.inflate(R.layout.logout, null);
                    builder.setView(dialoglayout);

                    logoutyes = (TextView) dialoglayout.findViewById(R.id.logoutyes);
                    logoutno = (TextView) dialoglayout.findViewById(R.id.logoutno);
                    logoutyes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LogoutPost();
                            logoutdialog.dismiss();
                        }
                    });
                    logoutno.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            logoutdialog.dismiss();
                            updateNavigationBar();
                        }
                    });
                    logoutdialog = builder.create();
                    logoutdialog.setCanceledOnTouchOutside(false);
                    logoutdialog.setCancelable(false);
                    logoutdialog.show();
                    updateNavigationBar();
                }
                break;
        }
    }

    private void LoggedUserGet() {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
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
            APICalls.LoggedProfile apiService = retrofit.create(APICalls.LoggedProfile.class);
            Call<ResponseBody> call = apiService.LoggedProfile();
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
                                String message = jsonObject.getString("message");
                                JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                                JSONObject dataobj = jsonArray.getJSONObject(0);
                                String userid = dataobj.getString("userid");
                                String firstname = dataobj.getString("first_name");
                                String lastname = dataobj.getString("last_name");
                                String username = dataobj.getString("username");
                                String mail_id = dataobj.getString("mail_id");
                                String facebookid = dataobj.getString("facebookid");
                                String googleid = dataobj.getString("googleid");
                                String twitterid = dataobj.getString("twitterid");
                                String instagramid = dataobj.getString("instagramid");
                                String telephone = dataobj.getString("telephone");
                                String profileinformation = dataobj.getString("profileinformation");
                                String created_on = dataobj.getString("created_on");
                                loggedname.setText(firstname+" "+lastname);
                                String image = dataobj.getString("profile_image");
                                if(image!=null && !image.equals("")&& !image.equals("null")) {
                                    Glide.with(OGAMainActivity.this).load(image)
                                            .asBitmap().placeholder(R.drawable.loading)
                                            .into(profileimg);
                                }
                                loggedname.setVisibility(View.VISIBLE);

                            } else {
                                String message = jsonObject.getString("message");
                                loggedname.setText("");
                                loggedname.setVisibility(View.GONE);
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

    private void LogoutPost() {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    String sessionid = preferenceUtils.getSessionId();
                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("action", "logout")
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
            APICalls.Logout apiService= retrofit.create(APICalls.Logout.class);
            Call<ResponseBody> call = apiService.Logout();
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
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
                public void onFailure(Call<ResponseBody> call, Throwable t) { }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode==123 /*&& resultCode == RESULT_OK*/){
            startActivity(new Intent(getIntent()));
            finish();
        }else if (requestCode==99 /*&& resultCode==RESULT_OK*/){
             startActivity(new Intent(getIntent()));
             finish();
         }else if (requestCode==126  && resultCode==RESULT_OK){
             appliedTabView(R.id.foottrendvideolayout);
         }else if (requestCode==127  && resultCode==RESULT_OK){
             appliedTabView(R.id.footernewslayout);
         } else if (requestCode==125 && resultCode==RESULT_OK){
             manageSlider();
         }
    }

    private void postdata(String s) {
        try {
            JSONObject jObject = new JSONObject(s);
            if(jObject.has("response")) {
                JSONObject obj = jObject.getJSONObject("response");
                if (obj.getString("status").equals("error")) {
                    String message = obj.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    PreferenceUtils.saveBooleanToSp(OGAMainActivity.this,"LoginStatus",false);
                    preferenceUtils.saveBooleanToSp(OGAMainActivity.this,"SocialLogin",false);
                    updateNavigationBar();
                    String message = obj.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    loggedname.setText("");
                    loggedname.setVisibility(View.GONE);
                    preferenceUtils.clearSP(OGAMainActivity.this);
                    profileimg.setImageResource(R.drawable.profile);
                    appliedTabView(R.id.footerhomelayout);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateNavigationBar(){
       if (preferenceUtils.getBooleanFromSP(OGAMainActivity.this,"LoginStatus")){
           if (preferenceUtils.getBooleanFromSP(OGAMainActivity.this,"SocialLogin")){
               signin.setText("Sign Out");
               changepwdlayout.setVisibility(View.GONE);
           }else {
               signin.setText("Sign Out");
               changepwdlayout.setVisibility(View.VISIBLE);
           }
       }else {
           signin.setText("Sign In");
           changepwdlayout.setVisibility(View.GONE);
       }
    }

    public void manageSlider(){
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void appliedTabView(int id) {
        if (id == R.id.footerhomelayout) {
            footerhomelayout.setBackgroundColor(getResources().getColor(R.color.selectedTabbackground));
            foottrendvideolayout.setBackgroundColor(getResources().getColor(R.color.unselectedBackground));
            footernewslayout.setBackgroundColor(getResources().getColor(R.color.unselectedBackground));
            notificationlayout.setBackgroundColor(getResources().getColor(R.color.unselectedBackground));
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content,new HomeFragment()).
                    addToBackStack(null).commit();
        }

        if(id==R.id.foottrendvideolayout){
            footerhomelayout.setBackgroundColor(getResources().getColor(R.color.unselectedBackground));
            foottrendvideolayout.setBackgroundColor(getResources().getColor(R.color.selectedTabbackground));
            footernewslayout.setBackgroundColor(getResources().getColor(R.color.unselectedBackground));
            notificationlayout.setBackgroundColor(getResources().getColor(R.color.unselectedBackground));
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content,new TrendingVideosFragment()).
                    addToBackStack(null).commit();
        }

        if(id==R.id.footernewslayout) {
            footerhomelayout.setBackgroundColor(getResources().getColor(R.color.unselectedBackground));
            foottrendvideolayout.setBackgroundColor(getResources().getColor(R.color.unselectedBackground));
            footernewslayout.setBackgroundColor(getResources().getColor(R.color.selectedTabbackground));
            notificationlayout.setBackgroundColor(getResources().getColor(R.color.unselectedBackground));
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content,new TrendingNewsFragment()).
                    addToBackStack(null).commit();
        }

        if (id == R.id.notificationlayout) {
            if (PreferenceUtils.getBooleanFromSP(OGAMainActivity.this, "LoginStatus")) {
                footerhomelayout.setBackgroundColor(getResources().getColor(R.color.unselectedBackground));
                foottrendvideolayout.setBackgroundColor(getResources().getColor(R.color.unselectedBackground));
                footernewslayout.setBackgroundColor(getResources().getColor(R.color.unselectedBackground));
                notificationlayout.setBackgroundColor(getResources().getColor(R.color.selectedTabbackground));
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content,new NotificationFragment()).
                        addToBackStack(null).commit();
            }else {
                Toast.makeText(OGAMainActivity.this,"Please Login to read notifications",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(OGAMainActivity.this);
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