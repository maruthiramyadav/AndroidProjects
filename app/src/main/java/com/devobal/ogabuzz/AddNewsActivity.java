package com.devobal.ogabuzz;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devobal.ogabuzz.Modals.LanguageModel;
import com.devobal.ogabuzz.SpecialClasses.FileUtils;
import com.devobal.ogabuzz.Modals.ImageModel;
import com.devobal.ogabuzz.SpecialClasses.InitApplication;
import com.devobal.ogabuzz.Modals.NewsModel;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.devobal.ogabuzz.SpecialClasses.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

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
 * Created by Devobal on 2/9/2019 on 5:18 PM.
 */
public class AddNewsActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView addnewsback,imagesdropdown,galleryimage;
    TextView imageupload,videoupload,postnews,singleimg,multipleimg,videopathtext;
    EditText edtitle,description;
    Spinner categoryspinner,Languagespinner;
    private static int RESULT_LOAD_VIDEO = 1;
    LinearLayout imgsel_layout,videopathlayout;
    String mediaPath,decodableString, title,desc,todayString, userbase64img,imgPath,post_type="1",categoryid,languageid;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 0;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    Button cancelButton;
    ImageView piclibrary, takephoto;
    TextView photoselectheader;
    AlertDialog editphotoDialog;
    ArrayList<NewsModel> catlist;
    ArrayList<String>stringcatlistarray,imgarray;
    ArrayList<Uri> imagesUriArrayList ;
    private final int PICK_IMAGE_MULTIPLE =1;
    String[] imagesPath= new String[0];
    RelativeLayout singlelayout;
    RecyclerView multimglist;
    ArrayList<ImageModel>multiimgArraylist;
    MultiImageAdpater imageAdpater;
    Bitmap bitmap;
    BitmapFactory.Options bmOptions;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    TabLayout tablayout;
    String videopath,thumbimage,videotitle;
    ArrayList<String> langname=new ArrayList<String>();
    ArrayList<String> langid=new ArrayList<String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (InitApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_add_news);
        addnewsback = (ImageView) findViewById(R.id.addnewsback);
        postnews = (TextView) findViewById(R.id.postnews);
        edtitle = (EditText) findViewById(R.id.edtitle);
        categoryspinner = (Spinner) findViewById(R.id.categoryspinner);
        Languagespinner=(Spinner)findViewById(R.id.Languagespinner);
        description = (EditText) findViewById(R.id.description);
        imageupload = (TextView) findViewById(R.id.imageupload);
        videoupload = (TextView) findViewById(R.id.videoupload);
        imagesdropdown=(ImageView)findViewById(R.id.imagesdropdown);
        imgsel_layout=(LinearLayout)findViewById(R.id.imgsel_layout);
        singleimg=(TextView) findViewById(R.id.singleimg);
        multipleimg=(TextView) findViewById(R.id.multipleimg);
        singlelayout = (RelativeLayout) findViewById(R.id.singlelayout);
        galleryimage = (ImageView) findViewById(R.id.galleryimage);
        multimglist = (RecyclerView) findViewById(R.id.multimglist);
        videopathlayout = (LinearLayout) findViewById(R.id.videopathlayout);
        videopathtext = (TextView) findViewById(R.id.videopathtext);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        tablayout=(TabLayout)findViewById(R.id.tablayout);

        if (getIntent().getExtras()!=null){
            videopath = getIntent().getExtras().getString("videopath");
            thumbimage=getIntent().getExtras().getString("thumbimage");
            videotitle=getIntent().getExtras().getString("title");
            edtitle.setText(videotitle);
            videopathlayout.setVisibility(View.VISIBLE);
            videopathtext.setText(videopath);
        }

        progressDialog = new ProgressDialog(AddNewsActivity.this);
        preferenceUtils = new PreferenceUtils(AddNewsActivity.this);
        catlist=new ArrayList<NewsModel>();
        imgarray = new ArrayList<String>();
        stringcatlistarray = new ArrayList<String>();

        multiimgArraylist = new ArrayList<ImageModel>();
        imageAdpater = new MultiImageAdpater(AddNewsActivity.this, multiimgArraylist);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(AddNewsActivity.this);
        multimglist.setLayoutManager(layoutManager);
        multimglist.setItemAnimator(new DefaultItemAnimator());
        multimglist.setAdapter(imageAdpater);
       /* Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        todayString = formatter.format(todayDate);*/
        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH)+1;
        int dd = c.get(Calendar.DAY_OF_MONTH);
        todayString = yy+"-"+mm+"-"+dd;

        addnewsback.setOnClickListener(this);
        imageupload.setOnClickListener(this);
        videoupload.setOnClickListener(this);
        postnews.setOnClickListener(this);
        singleimg.setOnClickListener(this);
        multipleimg.setOnClickListener(this);
        imageupload.setOnClickListener(this);

//Category Spinner
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(categoryspinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(300);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryid = catlist.get(position).getCat_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//language spinner
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(Languagespinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(300);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        Languagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                languageid=langid.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getCategoriesList();
        UserLanguagesGet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addnewsback:
                finish();
                break;
            case R.id.imageupload:
                imageupload.setBackground(getResources().getDrawable(R.drawable.rectbackground12));
                videoupload.setBackground(getResources().getDrawable(R.drawable.rectanglebgedittext));
                imagesdropdown.setVisibility(View.VISIBLE);
                imgsel_layout.setVisibility(View.VISIBLE);
                videopathlayout.setVisibility(View.GONE);
                break;
            case R.id.singleimg:
                singleimg.setBackground(getResources().getDrawable(R.drawable.rectbackground13));
                multipleimg.setBackground(getResources().getDrawable(R.color.colorTransparent));
                singleimg.setTextColor(getResources().getColor(R.color.colorWhite));
                multipleimg.setTextColor(getResources().getColor(R.color.colorBlack));
                mediaPath="";
                imagesPath = new String[0];
                editImagePopup();
                videopathlayout.setVisibility(View.GONE);
                break;
            case R.id.multipleimg:
                singleimg.setBackground(getResources().getDrawable(R.color.colorTransparent));
                multipleimg.setBackground(getResources().getDrawable(R.drawable.rectbackground14));
                singleimg.setTextColor(getResources().getColor(R.color.colorBlack));
                multipleimg.setTextColor(getResources().getColor(R.color.colorWhite));
                mediaPath="";
                imgPath="";
                if (storagepermission()){
                    Intent i = new Intent(AddNewsActivity.this, CustomPhotoGalleryActivity.class);
                    startActivityForResult(i,15);
                }
                videopathlayout.setVisibility(View.GONE);
                break;
            case R.id.videoupload:
                videoPopup();
                break;
            case R.id.postnews:
                title = edtitle.getText().toString().trim();
                desc = description.getText().toString().trim();
                if (title.length()==0){
                    Toast.makeText(AddNewsActivity.this,"Please Enter News Title",Toast.LENGTH_LONG).show();
                }else if (desc.length()==0){
                    Toast.makeText(AddNewsActivity.this,"Please Enter News Description",Toast.LENGTH_LONG).show();
                }else if (languageid.equals("0")){
                    Toast.makeText(AddNewsActivity.this,"Please Select News Language",Toast.LENGTH_LONG).show();
                }else {
                    if (imagesPath.length > 0) {
                        uploadpostimages(title, desc);
                    } else if (Utility.checkValidStringOrNot(imgPath)) {
                        uploadpostimages(title, desc);
                    } else if (getIntent().getExtras() != null) {
                        ImportVideo(videotitle, description.getText().toString(), videopath, String.valueOf(1), categoryid);
                    } else {
                        UploadPost(title, desc);
                    }
                }
                break;
        }
    }

    private void UserLanguagesGet() {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    String userid=preferenceUtils.getUserId();
                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("limit", "all")
                            .header("offset", "")
                            .header("userid",userid)
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
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("data"));
                                langname.clear();
                                langid.clear();
                                langname.add("Select Language");
                                langid.add("0");
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    langname.add(dataobj.getString("lang_name"));
                                    langid.add(dataobj.getString("langid"));
                                }
                                ArrayAdapter arrayAdapter = new ArrayAdapter(AddNewsActivity.this,
                                        R.layout.textview,langname);

                                arrayAdapter.setDropDownViewResource(R.layout.textview);

                                Languagespinner.setAdapter(arrayAdapter);

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

    private void uploadpostimages(final String title, final String desc) {
        if(imagesPath.length>0) {
            MultipartBody.Part[] multipartTypedOutput = new MultipartBody.Part[imagesPath.length];
            for (int index = 0; index < imagesPath.length; index++) {
                File file = new File(imagesPath[index]);
                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
                multipartTypedOutput[index] = MultipartBody.Part.createFormData("article_images[]", file.getName(), surveyBody);
            }
            post_type = "1";

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    String sessionid = preferenceUtils.getSessionId();

                    // Parsing any Media type file

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("title", title)
                            .header("sessionid", sessionid)
                            .header("description", desc)
                            .header("post_type", post_type)
                            .header("publish_date", todayString)
                            .header("cat_id", categoryid)
                            .header("video_link", "")
                            .header("is_draft", "0")
                            .header("language",languageid)
                            // .header("video_file","")
                            // .header("video_thumb_file","")

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
            // MultipartBody requestBody = builder.build();
            APICalls.AddNewsArticleImg apiService = retrofit.create(APICalls.AddNewsArticleImg.class);
            Call<ResponseBody> call = apiService.AddNewsArticleImg(multipartTypedOutput);
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
        if(imgPath!=null && !imgPath.equals("")&& !imgPath.equals("null")){
            File file = new File(imgPath);

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("article_images[]", file.getName(), requestFile);

            post_type = "1";

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    String sessionid = preferenceUtils.getSessionId();

                    // Parsing any Media type file

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("title", title)
                            .header("sessionid", sessionid)
                            .header("description", desc)
                            .header("post_type", post_type)
                            .header("publish_date", todayString)
                            .header("cat_id", categoryid)
                            .header("video_link", "")
                            .header("is_draft", "0")
                            .header("language",languageid)
                            // .header("video_file","")
                            // .header("video_thumb_file","")

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
            // MultipartBody requestBody = builder.build();
            APICalls.SingleImgPost apiService = retrofit.create(APICalls.SingleImgPost.class);
            Call<ResponseBody> call = apiService.SingleImgPost(body);
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

    public void videoPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewsActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.video_popup, null);
        builder.setView(dialogLayout);
        Button cancelButton = (Button) dialogLayout.findViewById(R.id.photoscancel);
        Button gallery = (Button) dialogLayout.findViewById(R.id.gallery);
        Button youtube = (Button) dialogLayout.findViewById(R.id.youtube);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgsel_layout.setVisibility(View.GONE);
                imageupload.setBackground(getResources().getDrawable(R.drawable.rectanglebgedittext));
                videoupload.setBackground(getResources().getDrawable(R.drawable.rectbackground12));
                imagesdropdown.setVisibility(View.GONE);
                imagesPath = new String[0];

                imgPath="";
                if(storagepermission()) {
                    loadVideoFromGallery(videoupload);
                }
                singlelayout.setVisibility(View.GONE);
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddNewsActivity.this,YoutubePlayerActivity.class);
                startActivityForResult(intent,123);
                finish();
                editphotoDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editphotoDialog.dismiss();
            }
        });
        editphotoDialog = builder.create();
        editphotoDialog.setCanceledOnTouchOutside(false);
        editphotoDialog.show();
    }

    public void editImagePopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewsActivity.this);
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
                    startActivityForResult(intent, 14);
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

    private void UploadPost(final String title, final String desc) {

        MultipartBody.Part imgbody = null;

        if(mediaPath!=null && !mediaPath.equals("")) {
            MultipartBody.Part fileToUpload=null;
            post_type="2";
            File file = new File(mediaPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            fileToUpload = MultipartBody.Part.createFormData("video_file", file.getName(), requestBody);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    String sessionid = preferenceUtils.getSessionId();

                    // Parsing any Media type file

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("title", title)
                            .header("sessionid", sessionid)
                            .header("description", desc)
                            .header("post_type", post_type)
                            .header("publish_date", todayString)
                            .header("video_link", "")
                            .header("is_draft","0")
                            .header("cat_id", categoryid)
                            .header("video_thumb_file","")
                            .header("language",languageid)

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

            APICalls.AddNewsArticle apiService= retrofit.create(APICalls.AddNewsArticle.class);
            Call<ResponseBody> call = apiService.AddNewsArticle(fileToUpload);
            showProgressdialog();
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
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismissProgressDislogue();
                }
            });
        }else {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    String sessionid = preferenceUtils.getSessionId();

                    // Parsing any Media type file

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("title", title)
                            .header("sessionid", sessionid)
                            .header("description", desc)
                            .header("post_type", post_type)
                            .header("publish_date", todayString)
                            .header("video_link", "")
                            .header("is_draft","0")
                            .header("video_thumb_file","")
                            .header("cat_id", categoryid)
                            .header("video_file","")
                            .header("language",languageid)
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

            APICalls.AddNewsArticle1 apiService= retrofit.create(APICalls.AddNewsArticle1.class);
            Call<ResponseBody> call = apiService.AddNewsArticle1();
            showProgressdialog();
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
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismissProgressDislogue();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    public boolean storagepermission(){
      /*  if (ContextCompat.checkSelfPermission(AddNewsActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(AddNewsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(AddNewsActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(AddNewsActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_MEDIA);
            }
            if(ContextCompat.checkSelfPermission(AddNewsActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(AddNewsActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_MEDIA);
            }
            return false;
            // Should we show an explanation?

        }
        else{
            return true;
        }*/
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck!=0) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        125);
            }
            return false;
        }else {
            return true;
        }
    }

    public void loadVideoFromGallery(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When a video is picked
            if (requestCode == RESULT_LOAD_VIDEO && resultCode == RESULT_OK
                    && null != data) {
                // Get the video from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                videopathlayout.setVisibility(View.VISIBLE);
                videopathtext.setText(mediaPath);
                // Set the Image in ImageView for Previewing the Media
                // imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();
                singlelayout.setVisibility(View.GONE);
                multimglist.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                tablayout.setVisibility(View.GONE);
                editphotoDialog.dismiss();
            }
            if (requestCode == 15) {
                imagesPath = data.getStringExtra("data").split("\\|");
                singlelayout.setVisibility(View.GONE);
                multimglist.setVisibility(View.VISIBLE);
               /* viewPager.setVisibility(View.VISIBLE);
                tablayout.setVisibility(View.VISIBLE);*/
                prepareData();

             /*imagesUriimageView.setImageBitmap(bitmap);ArrayList =  new ArrayList();
                    imageView.setImageBitmap(bitmap);for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        imagesUriArrayList.add(data.getClipData().getItemAt(i).getUri());
                        imgarray.add(getRealPathFromURI(imagesUriArrayList.get(i)));
                    }*/

                if (editphotoDialog!=null && editphotoDialog.isShowing()){
                    editphotoDialog.dismiss();
                }
                    /*Uri targetUri = data.getData();
                    imgPath = getRealPathFromURI(targetUri);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(AddNewsActivity.this.getContentResolver(),
                                targetUri);
                        if (editphotoDialog!=null && editphotoDialog.isShowing()){
                            editphotoDialog.dismiss();
                        }
                        bitmap = getResizedBitmap(bitmap);
                       // userbase64img = BitMapToString(bitmap);
                      //  performCrop(bitmap);
                    } catch (Exception e){
                        Toast.makeText(AddNewsActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }*/
            }

            if (requestCode == 12){
                if (resultCode == Activity.RESULT_OK){
                   /* Uri targetUri = data.getData();
                    imgPath = getRealPathFromURI(targetUri);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    if (editphotoDialog!=null && editphotoDialog.isShowing()){
                        editphotoDialog.dismiss();
                    }
                    singlelayout.setVisibility(View.VISIBLE);
                    multimglist.setVisibility(View.GONE);
                    photo = getResizedBitmap(photo);
                    galleryimage.setImageBitmap(photo);
                    userbase64img = BitMapToString(photo);*/
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    galleryimage.setImageBitmap(photo);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getApplicationContext(), photo);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    imgPath=getRealPathFromURI(tempUri);
                    if (editphotoDialog!=null && editphotoDialog.isShowing()){
                        editphotoDialog.dismiss();
                    }
                    singlelayout.setVisibility(View.VISIBLE);
                    multimglist.setVisibility(View.VISIBLE);
                    /*viewPager.setVisibility(View.GONE);
                    tablayout.setVisibility(View.GONE);*/
                    userbase64img = BitMapToString(photo);
                    // performCrop(photo);
                }
            }
            if (requestCode == 14){
                if (resultCode == Activity.RESULT_OK){

                    Uri targetUri = data.getData();
                    imgPath = getRealPathFromURI(targetUri);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(AddNewsActivity.this.getContentResolver(),
                                targetUri);
                        if (editphotoDialog!=null && editphotoDialog.isShowing()){
                            editphotoDialog.dismiss();
                        }
                        singlelayout.setVisibility(View.VISIBLE);
                        multimglist.setVisibility(View.GONE);
                        bitmap = getResizedBitmap(bitmap);
                        galleryimage.setImageBitmap(bitmap);
                        // userbase64img = BitMapToString(bitmap);
                        //  performCrop(bitmap);
                    } catch (Exception e){
                        Toast.makeText(AddNewsActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

            if (requestCode==123 && resultCode==RESULT_OK){

            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void prepareData() {

        multiimgArraylist.clear();
        for(int i=0; i<imagesPath.length; i++){
            ImageModel model = new ImageModel();
            model.setPath(imagesPath[i]);
            multiimgArraylist.add(model);
        }
//        imageAdpater = new MultiImageAdpater(AddNewsActivity.this, multiimgArraylist);
        imageAdpater.notifyDataSetChanged();

        adapter =new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(AddNewsActivity.this);
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

    private String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap = compressImageInput(bitmap);
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);

        byte[] b = baos.toByteArray();
        long lengthbmp = b.length;
        System.out.println(lengthbmp);
        long fileSizeInKB = lengthbmp / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println("width:"+ w);
        System.out.println("height"+ h);
        userbase64img = Base64.encodeToString(b, Base64.DEFAULT);
        return userbase64img;
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
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("data"));
                                catlist.clear();
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    NewsModel newsModel=new NewsModel();
                                    newsModel.setCat_id(dataobj.getString("cat_id"));
                                    newsModel.setCat_name(dataobj.getString("cat_name"));
                                    stringcatlistarray.add(dataobj.getString("cat_name"));
                                    catlist.add(newsModel);
                                }
                                ArrayAdapter arrayAdapter = new ArrayAdapter(AddNewsActivity.this,
                                        R.layout.textview,stringcatlistarray);

                                arrayAdapter.setDropDownViewResource(R.layout.textview);

                                categoryspinner.setAdapter(arrayAdapter);
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
        //}
    }

    public String getRealPathFromUri(Uri uri) { // function for file path from uri,
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);
        //compress the image using Compressor lib

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        @Override
        public int getCount() {
            return multiimgArraylist.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(AddNewsActivity.this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            File file = new File(multiimgArraylist.get(position).getPath());
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
            }
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void ImportVideo(final String title, final String description, final String videopath, final String videotype, final String catid) {
        final String sessionid = preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")) {
            //  Toast.makeText(ListDetailsActivity.this, "Please Make Login", Toast.LENGTH_LONG).show();
        } else {
            /*try {*/
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("authenticate_key", "abcd123XYZ")
                                .header("title",edtitle.getText().toString())
                                .header("sessionid",sessionid)
                                .header("description",description)
                                .header("publish_date", todayString)
                                .header("video_link",videopath)
                                .header("is_draft",String.valueOf(1))
                                .header("video_type",videotype)
                                .header("cat_id",catid)
                                .header("language",languageid)
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
                APICalls.ImportVideoPost apiService = retrofit.create(APICalls.ImportVideoPost.class);
                Call<ResponseBody> call = apiService.importVideoPost();
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
                                     Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                     finish();
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

                    }
                });
        }
    }
}
