package com.devobal.ogabuzz;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devobal.ogabuzz.Modals.NewsModel;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
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

public class SendFeedbackActivity extends AppCompatActivity {

    ImageView feedback;
    EditText edfeedback,edemail,edphone;
    TextView feedbacksend;
    String feedbacktext,emailtxt,phonetxt, catid;
    PreferenceUtils preferenceUtils;
    Spinner concernspin;
    ArrayList<NewsModel> catlist;
    ArrayList<String>stringcatlistarray;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_feedback_activity);

        feedback = (ImageView) findViewById(R.id.feedback);
        edfeedback = (EditText) findViewById(R.id.edfeedback);
        edemail = (EditText) findViewById(R.id.edemail);
        edphone = (EditText) findViewById(R.id.edphone);
        feedbacksend = (TextView) findViewById(R.id.feedbacksend);
        concernspin = (Spinner) findViewById(R.id.concernspin);

        preferenceUtils = new PreferenceUtils(SendFeedbackActivity.this);

        catlist=new ArrayList<NewsModel>();
        stringcatlistarray = new ArrayList<String>();
        progressDialog = new ProgressDialog(SendFeedbackActivity.this);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        feedbacksend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                feedbacktext = edfeedback.getText().toString().trim();
                emailtxt = edemail.getText().toString().trim();
                phonetxt = edphone.getText().toString().trim();
                if(feedbacktext.length()==0){
                    edfeedback.setError("Please Enter Content");
                }else if(emailtxt.length()==0){
                    edemail.setError("Please Enter Email");
                }else{
                    FeedbackPost(feedbacktext,catid,emailtxt,phonetxt);
                }

            }
        });
        getCategoriesList();
        concernspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catid = catlist.get(position).getCat_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(concernspin);

            // Set popupWindow height to 500px
            popupWindow.setHeight(300);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
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
                                ArrayAdapter arrayAdapter = new ArrayAdapter(SendFeedbackActivity.this,
                                        R.layout.textview,stringcatlistarray);

                                arrayAdapter.setDropDownViewResource(R.layout.textview);

                                concernspin.setAdapter(arrayAdapter);
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

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(SendFeedbackActivity.this);
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
    private void FeedbackPost(final String feedbacktext, final String catid, final String emailtxt, final String phonetxt) {
        try {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("content", feedbacktext)
                            .header("cat_id", catid)
                            .header("email_address", emailtxt)
                            .header("phone_number", phonetxt)
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
            APICalls.FeedbackPost apiService = retrofit.create(APICalls.FeedbackPost.class);
            Call<ResponseBody> call = apiService.FeedbackPost();
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
                                finish();
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
}
