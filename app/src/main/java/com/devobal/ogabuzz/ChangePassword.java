package com.devobal.ogabuzz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;

import org.json.JSONObject;

import java.io.IOException;

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
 * Created by Devobal on 2/13/2019 on 9:21 PM.
 */
public class ChangePassword extends AppCompatActivity{

    ImageView cp_backArrow;
    TextView changepasswordcancel,changepasswordsave;
    EditText currentpassword,newpassword,reenterpassword;
    ProgressDialog progressDialog;
    PreferenceUtils preferenceUtils;
    String userId,  crntpass,newpass,cnfmpass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        cp_backArrow = (ImageView) findViewById(R.id.cp_backArrow);
        changepasswordcancel = (TextView) findViewById(R.id.changepasswordcancel);
         currentpassword = (EditText) findViewById(R.id.currentpassword);
        newpassword = (EditText) findViewById(R.id.newpassword);
        reenterpassword = (EditText) findViewById(R.id.reenterpassword);
        changepasswordsave = (TextView) findViewById(R.id.changepasswordsave);

        preferenceUtils = new PreferenceUtils(ChangePassword.this);
        progressDialog = new ProgressDialog(ChangePassword.this);
        userId = preferenceUtils.getUserId();

        cp_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        changepasswordcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        changepasswordsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = getCurrentFocus();
                if (v != null) {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                       crntpass = currentpassword.getText().toString().trim();
                     newpass = newpassword.getText().toString().trim();
                     cnfmpass = reenterpassword.getText().toString().trim();
                   /* if (crntpass.length() == 0) {
                        Toast.makeText(getApplicationContext(),"Please Enter Current Password",Toast.LENGTH_SHORT).show();
                    } else if (!Utility.checkValidStringOrNot(crntpass)) {
                        Toast.makeText(getApplicationContext(),"Please Enter Valid Current Password",Toast.LENGTH_SHORT).show();
                    } else if (!Utility.checkValidStringOrNot(oldpass) || !oldpass.equals(crntpass)) {
                        Toast.makeText(getApplicationContext(),"The Current Password you have entered is incorrect", Toast.LENGTH_SHORT).show();
                    } else*/
                if (crntpass.length() == 0) {
                    Toast.makeText(getApplicationContext(),"Please Enter Old Password",Toast.LENGTH_SHORT).show();
                }else if (newpass.length() == 0) {
                        Toast.makeText(getApplicationContext(),"Please Enter New Password",Toast.LENGTH_SHORT).show();
                    }  else if (cnfmpass.length() == 0) {
                        Toast.makeText(getApplicationContext(),"Please Enter Confirm Password",Toast.LENGTH_SHORT).show();
                    } else if (!cnfmpass.equals(newpass)) {
                        Toast.makeText(getApplicationContext(),"Password and Confirm Password Must be Same",Toast.LENGTH_SHORT).show();
                    } else {
                        forgetnewpasspost();
                    }
                }
        });
    }

    private void forgetnewpasspost(){
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    String sessionid = preferenceUtils.getSessionId();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("action", "change_password")
                            .header("sessionid", sessionid)
                            .header("current_pwd", crntpass)
                            .header("new_pwd", newpass)
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
            APICalls.Register apiService= retrofit.create(APICalls.Register.class);
            Call<ResponseBody> call = apiService.insertUser();
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
                    Intent i = new Intent(ChangePassword.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(ChangePassword.this);
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
