package com.devobal.ogabuzz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devobal.ogabuzz.Modals.CommentsModel;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;

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

public class RepliesActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView rcView;
    RepliesAdapter repliesAdapter;
    ArrayList<CommentsModel> commentsModel;
    String newsid,commentid;
    ImageView commentback,commentsend;
    EditText edcomment;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replies);

        commentback=(ImageView)findViewById(R.id.commentback);
        commentsend=(ImageView)findViewById(R.id.commentsend);
        edcomment=(EditText)findViewById(R.id.edcomment);
        rcView=(RecyclerView)findViewById(R.id.rcView);
        progressDialog=new ProgressDialog(RepliesActivity.this);
        commentsModel=new ArrayList<CommentsModel>();
        preferenceUtils=new PreferenceUtils(RepliesActivity.this);

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            commentid=bundle.getString("commentid");
            newsid=bundle.getString("newsid");
        }

        repliesAdapter=new RepliesAdapter(RepliesActivity.this,commentsModel);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(RepliesActivity.this);
        rcView.setLayoutManager(layoutManager);
        rcView.setItemAnimator(new DefaultItemAnimator());
        rcView.setAdapter(repliesAdapter);

        CommentsGet();

        commentsend.setOnClickListener(this);
        commentback.setOnClickListener(this);

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

    private void postCommentData(String s) {
        try {
            //dismissProgressDislogue();
            JSONObject jObject = new JSONObject(s);
            commentsModel.clear();
            if(jObject.has("response")) {
                JSONObject obj = jObject.getJSONObject("response");
                if (obj.getString("status").equals("error")) {
                    String message = obj.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    String message = obj.getString("message");
                    JSONArray jsonArray = new JSONArray(obj.getString("data"));
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CommentsModel model = new CommentsModel();
                            JSONObject dataobj3 = jsonArray.getJSONObject(i);
                            if (dataobj3.getString("parent_comment_id").equals(commentid)) {
                                model.setUserid(dataobj3.getString("userid"));
                                model.setPostid(dataobj3.getString("post_id"));
                                model.setComment(dataobj3.getString("comment"));
                                model.setIsactive(dataobj3.getString("is_active"));
                                model.setDate(dataobj3.getString("created"));
                                model.setCommentid(dataobj3.getString("comment_id"));
                                model.setModifieddate(dataobj3.getString("modified"));
                                model.setName(dataobj3.getString("comment_user_name"));
                                model.setImage(dataobj3.getString("comment_user_image"));
                                commentsModel.add(model);
                            }
                        }
                        repliesAdapter.notifyDataSetChanged();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void replyComment(final String commentid, final String comment){

        final String sessionid=preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")){
            Toast.makeText(RepliesActivity.this,"Please make login",Toast.LENGTH_LONG).show();
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
                                .header("commentid",commentid)
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
                APICalls.CommentReply apiService = retrofit.create(APICalls.CommentReply.class);
                Call<ResponseBody> call = apiService.commentReply();
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
                                    CommentsGet();
                                }else{
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(RepliesActivity.this, message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commentback:
                finish();
                break;
            case R.id.commentsend:
                replyComment(commentid,edcomment.getText().toString());
                edcomment.setText("");
                dismisKeyboard();
                break;
        }
    }

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(RepliesActivity.this);
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

    public void dismisKeyboard(){
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        View v=getCurrentFocus();
        if (v!=null){
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }

}
