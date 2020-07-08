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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

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
 * Created by Devobal on 2/9/2019 on 6:03 PM.
 */
public class AboutProfileActivity extends AppCompatActivity {

    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    String sessionid,userid,imgPath;
    CircleImageView profileimg;
    TextView usrname;
    TextView loggedfname,loggedlastname,loggedemail;
    ImageView imgedit;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 0;
    Button cancelButton;
    ImageView piclibrary, takephoto,back;
    TextView photoselectheader;
    AlertDialog editphotoDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutprofile);

        profileimg = (CircleImageView) findViewById(R.id.profileimg);
        usrname = (TextView) findViewById(R.id.usrname);
        loggedfname = (TextView) findViewById(R.id.loggedfname);
        loggedlastname = (TextView) findViewById(R.id.loggedlastname);
        loggedemail = (TextView) findViewById(R.id.loggedemail);
        imgedit = (ImageView) findViewById(R.id.imgedit);
        back=(ImageView)findViewById(R.id.back);

        preferenceUtils = new PreferenceUtils(AboutProfileActivity.this);
        progressDialog = new ProgressDialog(AboutProfileActivity.this);
        sessionid = preferenceUtils.getSessionId();
        userid = preferenceUtils.getUserId();

        imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AboutProfileActivity.this, EditProfile.class);
                startActivityForResult(i,123);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LoggedUserGet();

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
                                String image = dataobj.getString("profile_image");
                                if(image!=null && !image.equals("")&& !image.equals("null")) {
                                    Glide.with(AboutProfileActivity.this).load(image)
                                            .asBitmap().placeholder(R.drawable.loading)
                                            .into(profileimg);
                                }
                                usrname.setText(username);
                                loggedfname.setText(firstname);
                                loggedlastname.setText(lastname);
                                String[] mail=mail_id.split("-");
                                mail_id=mail[0];
                                loggedemail.setText(mail_id);
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

    public void editImagePopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AboutProfileActivity.this);
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
        if (ContextCompat.checkSelfPermission(AboutProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(AboutProfileActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(AboutProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(AboutProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_MEDIA);
            }
            if(ContextCompat.checkSelfPermission(AboutProfileActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(AboutProfileActivity.this,
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
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(AboutProfileActivity.this.getContentResolver(),
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
                        Toast.makeText(AboutProfileActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
            if (requestCode == 12){
                if (resultCode == Activity.RESULT_OK){
                   /* Uri targetUri = data.getData();
                    imgPath = getRealPathFromURI(targetUri);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    if (editphotoDialog!=null && editphotoDialog.isShowing()){
                        editphotoDialog.dismiss();
                    }
                    photo = getResizedBitmap(photo);
                    profileimg.setImageBitmap(photo);
                    ImagePost();*/
                    // performCrop(photo);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    profileimg.setImageBitmap(photo);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getApplicationContext(), photo);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    imgPath=getRealPathFromURI(tempUri);
                    if (editphotoDialog!=null && editphotoDialog.isShowing()){
                        editphotoDialog.dismiss();
                    }
                    ImagePost();
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
            progressDialog = new ProgressDialog(AboutProfileActivity.this);
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
                   // finish();
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

}
