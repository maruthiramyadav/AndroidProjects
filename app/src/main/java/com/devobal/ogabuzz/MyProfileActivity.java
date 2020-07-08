package com.devobal.ogabuzz;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.Modals.FollowModel;
import com.devobal.ogabuzz.SpecialClasses.InitApplication;
import com.devobal.ogabuzz.Modals.ListModel;
import com.devobal.ogabuzz.Modals.PostModel;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.devobal.ogabuzz.SpecialClasses.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Devobal on 2/8/2019 on 9:57 PM.
 */
public class MyProfileActivity extends AppCompatActivity{

    RecyclerView followrcView;
    ArrayList<FollowModel> followModelslist;
    ProfileFollowAdapter followAdapter;

    RecyclerView postrcView;
    ArrayList<PostModel> postModelslist;
    ProfilePostAdapter postAdapter;

    RecyclerView followingrcView;
    ArrayList<FollowModel> followingModelslist;
    FollowingAdapter followingAdapter;
    RelativeLayout addnewslayout;
    ImageView detailsback;
    LinearLayout aboutlayout,followerlayout,followinglayout,latestpostlayout,waletlayout;
    PreferenceUtils preferenceUtils;

    String sessionid,imgPath,userid, categoryname;
    ImageView imgedit,profileimg;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 0;
    ProgressDialog progressDialog;
    Button cancelButton;
    ImageView piclibrary, takephoto;
    TextView photoselectheader,name,email,username,useremail,walletbalance;
    AlertDialog editphotoDialog;
    CircleImageView profilecirimg;
    ListView postlistview;
    NewsPostListAdapter postListAdapter;
    ArrayList<ListModel> listModelArrayList;
    String firstname,lastname,mail_id,usname;
    TextView followerslist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (InitApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.myprofile);

        followrcView= (RecyclerView) findViewById(R.id.followrcView);
        postrcView = (RecyclerView) findViewById(R.id.postrcView);
        followingrcView = (RecyclerView) findViewById(R.id.followingrcView);
        addnewslayout = (RelativeLayout) findViewById(R.id.addnewslayout);

        detailsback = (ImageView) findViewById(R.id.detailsback);
        aboutlayout = (LinearLayout) findViewById(R.id.aboutlayout);
        followerlayout = (LinearLayout) findViewById(R.id.followerlayout);
        followinglayout = (LinearLayout) findViewById(R.id.followinglayout);

        imgedit = (ImageView) findViewById(R.id.imgedit);
        profileimg = (ImageView) findViewById(R.id.profileimg);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        username = (TextView) findViewById(R.id.username);
        useremail = (TextView) findViewById(R.id.useremail);
        profilecirimg = (CircleImageView) findViewById(R.id.profilecirimg);

        postlistview = (ListView) findViewById(R.id.postlistview);
        walletbalance = (TextView) findViewById(R.id.walletbalance);
        followerslist=(TextView)findViewById(R.id.followerslist);

        waletlayout=(LinearLayout)findViewById(R.id.waletlayout);

        listModelArrayList = new ArrayList<ListModel>();
        postListAdapter = new NewsPostListAdapter(MyProfileActivity.this,listModelArrayList);
        postlistview.setAdapter(postListAdapter);
        latestpostlayout = (LinearLayout) findViewById(R.id.latestpostlayout);

       /* postlistview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/

        imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyProfileActivity.this, EditProfile.class);
                startActivityForResult(i,123);
            }
        });

        progressDialog = new ProgressDialog(MyProfileActivity.this);
        preferenceUtils = new PreferenceUtils(MyProfileActivity.this);
        sessionid= preferenceUtils.getSessionId();
        userid = preferenceUtils.getUserId();
        categoryname = preferenceUtils.getCategoryName();

        aboutlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyProfileActivity.this, AboutProfileActivity.class);
                startActivity(i);
            }
        });

        detailsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        followModelslist=new ArrayList<FollowModel>();
        followAdapter=new ProfileFollowAdapter(this,followModelslist, new ProfileFollowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
              /*  Intent i = new Intent(MyProfileActivity.this, BlogActivity.class);
                startActivity(i);*/
            }
        });

        followrcView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(MyProfileActivity.this,LinearLayoutManager.HORIZONTAL,false);
        followrcView.setLayoutManager(layoutManager);
        followrcView.setItemAnimator(new DefaultItemAnimator());
        followrcView.setAdapter(followAdapter);

        postModelslist=new ArrayList<PostModel>();
        postAdapter=new ProfilePostAdapter(this,postModelslist);
        postrcView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager1=new LinearLayoutManager(MyProfileActivity.this,LinearLayoutManager.HORIZONTAL,false);
        postrcView.setLayoutManager(layoutManager1);
        postrcView.setItemAnimator(new DefaultItemAnimator());
        postrcView.setAdapter(postAdapter);

        followingModelslist=new ArrayList<FollowModel>();
        followingAdapter=new FollowingAdapter(this,followingModelslist);
        followingrcView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager2=new LinearLayoutManager(MyProfileActivity.this,LinearLayoutManager.HORIZONTAL,false);
        followingrcView.setLayoutManager(layoutManager2);
        followingrcView.setItemAnimator(new DefaultItemAnimator());
        followingrcView.setAdapter(followingAdapter);

        followerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyProfileActivity.this, AllFollowersList.class);
                startActivityForResult(i,123);
            }
        });

        followinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyProfileActivity.this, AllFollowingList.class);
                startActivityForResult(i,123);
            }
        });
        addnewslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionid = preferenceUtils.getSessionId();
                if(!sessionid.equals("0")) {
                    Intent i = new Intent(MyProfileActivity.this, AddNewsActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
        latestpostlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MyProfileActivity.this, LatestPostActivity.class);
                startActivity(i);
            }
        });
        waletlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfileActivity.this, PaymentPageActivity.class));
            }
        });

        LoggedUserGet();
        FollowersList();
        FollowingList();
        NewsListGet();
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
                            .header("user_id", userid)
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

                                if (Utility.checkValidStringOrNot(dataobj.getString("first_name"))){
                                    firstname = dataobj.getString("first_name");
                                }
                                if (Utility.checkValidStringOrNot(dataobj.getString("last_name"))){
                                    lastname = dataobj.getString("last_name");
                                }
                                if (Utility.checkValidStringOrNot(dataobj.getString("username"))){
                                    usname = dataobj.getString("username");
                                }
                                if (Utility.checkValidStringOrNot(dataobj.getString("mail_id"))){
                                    mail_id = dataobj.getString("mail_id");
                                    String[] mail=mail_id.split("-");
                                    mail_id=mail[0];
                                }
                                String facebookid = dataobj.getString("facebookid");
                                String googleid = dataobj.getString("googleid");
                                String twitterid = dataobj.getString("twitterid");
                                String instagramid = dataobj.getString("instagramid");
                                String telephone = dataobj.getString("telephone");
                                String profileinformation = dataobj.getString("profileinformation");
                                String created_on = dataobj.getString("created_on");
                                String balance = dataobj.getString("wallet_amnt");
                                walletbalance.setText(balance);
                                if (dataobj.has("profile_image")) {
                                    if (Utility.checkValidStringOrNot(dataobj.getString("profile_image"))) {
                                        String image = dataobj.getString("profile_image");
                                        if (image != null && !image.equals("") && !image.equals("null")) {
                                            Glide.with(MyProfileActivity.this).load(image)
                                                    .asBitmap().placeholder(R.drawable.loading)
                                                    .into(profileimg);
                                        }
                                    }
                                }
                                if (dataobj.has("profile_image")) {
                                    if (Utility.checkValidStringOrNot(dataobj.getString("profile_image"))) {
                                        String image = dataobj.getString("profile_image");
                                        if (image != null && !image.equals("") && !image.equals("null")) {
                                            Glide.with(MyProfileActivity.this).load(image)
                                                    .asBitmap().placeholder(R.drawable.loading)
                                                    .into(profilecirimg);
                                        }
                                    }
                                }
                                name.setText(firstname+" "+lastname);
                                username.setText(firstname+" "+lastname);
                                useremail.setText(mail_id);
                                email.setText(mail_id);
                            } else {
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
                              //  Toast.makeText(getApplicationContext(), jsonObject1.getString("message"),Toast.LENGTH_SHORT).show();
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
                                     .header("action_type", "follower")
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
                                    //followerslist.setText(jsonArray.length());
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    FollowModel followModel=new FollowModel();
                                    followModel.setFollowerid(dataobj.getString("follow_user_id"));
                                    followModel.setName(dataobj.getString("follow_user_name"));
                                    followModel.setFollowimg(dataobj.getString("follow_user_image"));
                                    followModelslist.add(followModel);
                                }
                                followAdapter.notifyDataSetChanged();
                            }else{
                               // Toast.makeText(getApplicationContext(), jsonObject1.getString("message"),Toast.LENGTH_SHORT).show();
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

    public void editImagePopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.myphotos_popup, null);
        builder.setView(dialogLayout);
        photoselectheader = (TextView) dialogLayout.findViewById(R.id.photoselectheader);
        photoselectheader.setText("Take Photo");
        cancelButton = (Button) dialogLayout.findViewById(R.id.photoscancel);
        piclibrary = (ImageView) dialogLayout.findViewById(R.id.piclibrary);
        takephoto = (ImageView) dialogLayout.findViewById(R.id.takephoto);
        // take photo button onclick
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(storagepermission()) {
                    // sending intent for ACTION_IMAGE_CAPTURE
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 12);
//                    editphotoDialog.dismiss();
                }
            }
        });
        // piclibrary onclick
        piclibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storagepermission()) {
                    // sending intent ACTION_PICK
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 15);
//                    editphotoDialog.dismiss();
                }
            }
        });
        // cancel button onclcik
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editphotoDialog.dismiss();
            }
        });
        editphotoDialog = builder.create();
        editphotoDialog.setCanceledOnTouchOutside(false);
        // show alter diaolg
        editphotoDialog.show();
    }

    public boolean storagepermission(){
        if (ContextCompat.checkSelfPermission(MyProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MyProfileActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(MyProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MyProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_MEDIA);
            }
            if(ContextCompat.checkSelfPermission(MyProfileActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MyProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_MEDIA);

            }
            return false;
            // Should we show an explanation?

        }
        else{
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When a video is picked
             if (requestCode == 15){
                if (resultCode == Activity.RESULT_OK){

                    Uri targetUri = data.getData();
                    imgPath = getRealPathFromURI(targetUri);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(MyProfileActivity.this.getContentResolver(),
                                targetUri);
                        if (editphotoDialog!=null && editphotoDialog.isShowing()){
                            editphotoDialog.dismiss();
                        }
                        bitmap = getResizedBitmap(bitmap);
                        profileimg.setImageBitmap(bitmap);
                        ImagePost();
                       // userbase64img = BitMapToString(bitmap);
                      //  performCrop(bitmap);
                    } catch (Exception e){
                        Toast.makeText(MyProfileActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

            }
            if (requestCode == 12){
                if (resultCode == Activity.RESULT_OK){
                    Uri targetUri = data.getData();
                    imgPath = getRealPathFromURI(targetUri);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    if (editphotoDialog!=null && editphotoDialog.isShowing()){
                        editphotoDialog.dismiss();
                    }
                    photo = getResizedBitmap(photo);
                    profileimg.setImageBitmap(photo);
                    ImagePost();
                    // performCrop(photo);
                }
            } else if (requestCode==123 && resultCode ==RESULT_OK){
                startActivity(new Intent(getIntent()));
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void ImagePost() {
        MultipartBody.Part fileToUpload=null;
        if(imgPath!=null && !imgPath.equals("")) {
            File file = new File(imgPath);

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("profile_image", file.getName(), requestFile);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    String sessionid = preferenceUtils.getSessionId();

                    // Parsing any Media type file

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("action", "edit_profile_img")
                            .header("sessionid", sessionid)

                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });

            // RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            OkHttpClient client = httpClient.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            APICalls.ProfileImgPost apiService = retrofit.create(APICalls.ProfileImgPost.class);
            Call<ResponseBody> call = apiService.ProfileImgPost(body);
            showProgressdialog();
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = response.body().string();
                        postdata(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismissProgressDislogue();
                }
            });
        }
    }

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(MyProfileActivity.this);
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

    private void postdata(String s) {
        dismissProgressDislogue();
        try {
            JSONObject jObject = new JSONObject(s);
            if(jObject.has("response")) {
                JSONObject obj = jObject.getJSONObject("response");
                if (obj.getString("status").equals("error")) {
                    String message = obj.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    String message = obj.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = 500;
            height = (int) (width / bitmapRatio);
        } else {
            height = 500;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path1 = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path1;

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
                            .header("offset", "")
                            .header("sessionid", sessionid)
                            .header("category", "")
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
            APICalls.GetNews apiService= retrofit.create(APICalls.GetNews.class);
            Call<ResponseBody> call = apiService.GetNews();
            showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = response.body().string();
                         postListdata(s);
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

    private void postListdata(String s) {
        try {
            dismissProgressDislogue();
            JSONObject jObject = new JSONObject(s);
            listModelArrayList.clear();
            if(jObject.has("response")) {
                JSONObject obj = jObject.getJSONObject("response");
                if (obj.getString("status").equals("error")) {
                    String message = obj.getString("message");
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
                        JSONArray imagesarray = new JSONArray(dataobj.getString("images"));
                        ArrayList<String> imgarray = new ArrayList<String>();
                        if(imagesarray.length()>0){
                            for(int t=0; t<imagesarray.length(); t++) {
                                JSONObject imgobj = imagesarray.getJSONObject(t);
                                imgarray.add(imgobj.getString("img"));
                            }
                        }
                         model.setPhotosArray(imgarray);
                         model.setDateandtime(dataobj.getString("created"));
                         listModelArrayList.add(model);
                    }
                    postListAdapter.notifyDataSetChanged();
                }
            }
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
