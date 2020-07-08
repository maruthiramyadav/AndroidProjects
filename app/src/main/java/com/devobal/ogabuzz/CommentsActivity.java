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
import android.widget.ListView;
import android.widget.Toast;

import com.devobal.ogabuzz.Modals.CommentsModel;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;

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

/**
 * Created by Devobal on 3/19/2019 on 10:42 PM.
 */
public class CommentsActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView commentback,commentsend;
    ListView commentlist;
    String newsid,sessionid;
    PreferenceUtils preferenceUtils;
    ArrayList<CommentsModel> commentsModels;
    CommentsAdapter commentsAdapter;
    int m=0;
    EditText edcomment;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentslist);

        commentback = (ImageView) findViewById(R.id.commentback);
        commentlist = (ListView) findViewById(R.id.commentlist);
        commentsend=(ImageView)findViewById(R.id.commentsend);
        edcomment=(EditText)findViewById(R.id.edcomment);
        progressDialog=new ProgressDialog(CommentsActivity.this);
        preferenceUtils = new PreferenceUtils(CommentsActivity.this);

        sessionid = preferenceUtils.getSessionId();

        commentsModels = new ArrayList<CommentsModel>();
        commentsAdapter = new CommentsAdapter(CommentsActivity.this, commentsModels);
        commentlist.setAdapter(commentsAdapter);

        commentback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(getIntent().getExtras().getString("newsid")!=null){
            newsid = getIntent().getExtras().getString("newsid");
        }
        commentback.setOnClickListener(this);
        commentsend.setOnClickListener(this);

        CommentsGet();
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
            commentsModels.clear();
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
                            int replycount=0;
                            CommentsModel model = new CommentsModel();
                            JSONObject dataobj3 = jsonArray.getJSONObject(i);
                            String commentid=dataobj3.getString("comment_id");
                            for (int k=0;k<jsonArray.length();k++){
                                JSONObject reply= jsonArray.getJSONObject(k);
                                if (reply.getString("parent_comment_id").equals(commentid)){
                                    replycount++;
                                }
                            }
                            model.setReplycount(String.valueOf(replycount));
                            if (dataobj3.getString("parent_comment_id").equals("0")) {
                                model.setUserid(dataobj3.getString("userid"));
                                model.setPostid(dataobj3.getString("post_id"));
                                model.setComment(dataobj3.getString("comment"));
                                model.setIsactive(dataobj3.getString("is_active"));
                                model.setDate(dataobj3.getString("created"));
                                model.setCommentid(dataobj3.getString("comment_id"));
                                model.setModifieddate(dataobj3.getString("modified"));
                                model.setName(dataobj3.getString("comment_user_name"));
                                model.setImage(dataobj3.getString("comment_user_image"));
                                if (dataobj3.getString("parent_comment_id").equals("0")) {
                                    model.setParent_comment_id(dataobj3.getString("parent_comment_id"));
                                } else {
                                    m++;
                                }
                                commentsModels.add(model);
                            }
                        }
                         commentsAdapter.notifyDataSetChanged();
                    }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commentback:
                finish();
                break;
            case R.id.commentsend:
                CommentPost(newsid,edcomment.getText().toString());
                edcomment.setText("");
                dismisKeyboard();
                break;
        }
    }

    public void dismisKeyboard(){
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        View v=getCurrentFocus();
        if (v!=null){
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }

    private void CommentPost(final String newsid, final String comment) {

        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    String sessionid = preferenceUtils.getSessionId();
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("sessionid",sessionid)
                            .header("newsid",newsid)
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
            APICalls.PostComment apiService= retrofit.create(APICalls.PostComment.class);
            Call<ResponseBody> call = apiService.PostComment();
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
                                CommentsGet();
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

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(CommentsActivity.this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_OK && resultCode==100){
            CommentsGet();
        }
    }
}
