package com.devobal.ogabuzz;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by Devobal-2018-2 on 20-04-2018.
 */

public class ForgotPassword extends AppCompatActivity {
    EditText forgetemail;
    ImageView frgtpwdback;
    TextView forgotsubmit;
    String emailtext;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_activity);

        forgetemail = (EditText) findViewById(R.id.forgetemail);
        frgtpwdback = (ImageView) findViewById(R.id.frgtpwdback);
        forgotsubmit = (TextView) findViewById(R.id.forgotsubmit);

        progressDialog = new ProgressDialog(ForgotPassword.this);
        frgtpwdback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        forgotsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailtext= forgetemail.getText().toString().trim();
                if(forgetemail.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
                }else if (!isEmailValid(emailtext)){
                    Toast.makeText(getApplicationContext(),"Please Enter Valid Email Format",Toast.LENGTH_SHORT).show();
                } else {
                    ForgotpwdPost();

                }
            }
        });
    }

    private void ForgotpwdPost() {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("action", "forgotpwd")
                            .header("email_id", emailtext)
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

    boolean isEmailValid(CharSequence email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(ForgotPassword.this);
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