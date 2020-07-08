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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.SpecialClasses.GPSTracker;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.devobal.ogabuzz.SpecialClasses.Utility;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLngBounds;

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
 * Created by Devobal on 3/12/2019 on 4:52 PM.
 */
public class EditProfile extends AppCompatActivity{

    ImageView editprofileback,profileedit;
    CircleImageView registerprofileimg;
    //  AutoCompleteTextView place_autocompletetv;
    EditText edusername,edfname,edlname,edemail,edpwd,edconfpwd,edmobileno;
    TextView dobtext,register;
    protected GoogleApiClient mGoogleApiClient1;
    private LatLngBounds bounds1;
    //  private PlaceAutoCompleteAdapter mAdapter1;
    double latitude1, longitude1;
    GPSTracker gpsTracker;
    String responseBody,usrname,fname,lname,emailtxt,password,confpwd,mobileno;
    ProgressDialog progressDialog;
    PreferenceUtils preferenceUtils;
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 0;
    Button cancelButton;
    ImageView piclibrary, takephoto;
    TextView photoselectheader;

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    AlertDialog editphotoDialog;
    String sessionid,userid,imgPath;
    String facebookid, googleid,twitterid ,instagramid;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);

        editprofileback = (ImageView) findViewById(R.id.editprofileback);

        edusername = (EditText) findViewById(R.id.edusername);
        edfname = (EditText) findViewById(R.id.edfname);
        edlname = (EditText) findViewById(R.id.edlname);
        edemail = (EditText) findViewById(R.id.edemail);
        //  dobtext = (TextView) findViewById(R.id.dobtext);
        // place_autocompletetv = (AutoCompleteTextView) findViewById(R.id.place_autocompletetv);
      //  edpwd = (EditText) findViewById(R.id.edpwd);
     //   edconfpwd = (EditText) findViewById(R.id.edconfpwd);
        edmobileno = (EditText) findViewById(R.id.edmobileno);
        register = (TextView) findViewById(R.id.register);
        registerprofileimg = (CircleImageView) findViewById(R.id.registerprofileimg);
        profileedit = (ImageView) findViewById(R.id.profileedit);
        preferenceUtils = new PreferenceUtils(EditProfile.this);
        progressDialog = new ProgressDialog(EditProfile.this);
        sessionid = preferenceUtils.getSessionId();
        userid = preferenceUtils.getUserId();
        
        profileedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImagePopup();
            }
        });
        
        editprofileback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usrname = edusername.getText().toString().trim();
                fname = edfname.getText().toString().trim();
                lname = edlname.getText().toString().trim();
                // dateofbirth = dobtext.getText().toString().trim();
                emailtxt = edemail.getText().toString().trim();
                // birthplace = place_autocompletetv.getText().toString().trim();
                mobileno = edmobileno.getText().toString().trim();

                if(usrname.length()==0){
                    //   Toast.makeText(getApplicationContext(), "Please Enter User Name", Toast.LENGTH_SHORT).show();
                    edusername.setError("Please Enter User Name");
                    edusername.requestFocus();
                }else if(fname.length()==0){
                    //  Toast.makeText(getApplicationContext(), "Please Enter First Name", Toast.LENGTH_SHORT).show();
                    //  edfname.setBackgroundResource(R.drawable.edittextbgred);
                    edfname.requestFocus();
                    edfname.setError("Please Enter First Name");
                }else if(lname.length()==0){
                    // Toast.makeText(getApplicationContext(), "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                    // edlname.setBackgroundResource(R.drawable.edittextbgred);
                    edlname.requestFocus();
                    edlname.setError("Please Enter Last Name");
                }else if(emailtxt.length()==0) {
                    // Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
                    // edemail.setBackgroundResource(R.drawable.edittextbgred);
                    edemail.setError("Please Enter Email");
                    edemail.requestFocus();
                }else if(mobileno.length()==0){
                    //  Toast.makeText(getApplicationContext(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    // edmobileno.setBackgroundResource(R.drawable.edittextbgred);
                    edmobileno.setError("Please Enter Mobile Number");
                    edmobileno.refreshDrawableState();
                }else{
                    postEditProfileData();
                    edusername.setBackgroundResource(R.drawable.rectanglebgedittext);
                    edfname.setBackgroundResource(R.drawable.rectanglebgedittext);
                    edlname.setBackgroundResource(R.drawable.rectanglebgedittext);
                    edemail.setBackgroundResource(R.drawable.rectanglebgedittext);
                    edmobileno.setBackgroundResource(R.drawable.rectanglebgedittext);

                }
            }

        });
        LoggedUserGet();

    }

    private void postEditProfileData() {

        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    password = preferenceUtils.getPassword();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("action", "edit")
                            .header("sessionid",sessionid)
                            .header("first_name", fname)
                            .header("last_name", lname)
                            .header("username", usrname)
                            .header("telephone", mobileno)
                            .header("mail_id", emailtxt)
                            .header("password", password)
                            .header("facebookid", facebookid)
                            .header("googleid", googleid)
                            .header("twitterid", twitterid)
                            .header("instaid", instagramid)
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
            APICalls.EditProfilePost apiService= retrofit.create(APICalls.EditProfilePost.class);
            Call<ResponseBody> call = apiService.EditProfilePost();
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
                                 facebookid = dataobj.getString("facebookid");
                                 googleid = dataobj.getString("googleid");
                                 twitterid = dataobj.getString("twitterid");
                                 instagramid = dataobj.getString("instagramid");
                                String telephone = dataobj.getString("telephone");
                                String profileinformation = dataobj.getString("profileinformation");
                                String created_on = dataobj.getString("created_on");
                                if (dataobj.has("profile_image")) {
                                    if (Utility.checkValidStringOrNot(dataobj.getString("profile_image"))) {
                                        String image = dataobj.getString("profile_image");
                                        if (image != null && !image.equals("") && !image.equals("null")) {
                                            Glide.with(EditProfile.this).load(image)
                                                    .asBitmap().placeholder(R.drawable.loading)
                                                    .into(registerprofileimg);
                                        }
                                    }
                                }
                                edusername.setText(username);
                                edfname.setText(firstname);
                                edlname.setText(lastname);
                                edemail.setText(mail_id);
                                edmobileno.setText(telephone);


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
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
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
        if (ContextCompat.checkSelfPermission(EditProfile.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(EditProfile.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(EditProfile.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EditProfile.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_MEDIA);
            }
            if(ContextCompat.checkSelfPermission(EditProfile.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EditProfile.this,
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
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(EditProfile.this.getContentResolver(),
                                targetUri);
                        if (editphotoDialog!=null && editphotoDialog.isShowing()){
                            editphotoDialog.dismiss();
                        }
                        bitmap = getResizedBitmap(bitmap);
                        registerprofileimg.setImageBitmap(bitmap);
                        ImagePost();
                        // userbase64img = BitMapToString(bitmap);
                        //  performCrop(bitmap);
                    } catch (Exception e){
                        Toast.makeText(EditProfile.this,e.toString(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
          /*  if (requestCode == 12){
                if (resultCode == Activity.RESULT_OK){
                    Uri targetUri = data.getData();
                    imgPath = getRealPathFromURI(targetUri);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    if (editphotoDialog!=null && editphotoDialog.isShowing()){
                        editphotoDialog.dismiss();
                    }
                    photo = getResizedBitmap(photo);
                    registerprofileimg.setImageBitmap(photo);
                    ImagePost();
                    // performCrop(photo);
                }  }*/

         else if (requestCode == 12){
                if (resultCode == Activity.RESULT_OK) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    registerprofileimg.setImageBitmap(photo);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getApplicationContext(), photo);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    imgPath=getRealPathFromURI(tempUri);
                    if (editphotoDialog!=null && editphotoDialog.isShowing()){
                        editphotoDialog.dismiss();
                    }
                    ImagePost();
                }
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
            progressDialog = new ProgressDialog(EditProfile.this);
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
