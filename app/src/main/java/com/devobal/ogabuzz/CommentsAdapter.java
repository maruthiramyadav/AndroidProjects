package com.devobal.ogabuzz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.Modals.CommentsModel;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;

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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Devobal on 3/19/2019 on 11:03 PM.
 */
public class CommentsAdapter extends BaseAdapter{

    CommentsActivity context;
    ArrayList<CommentsModel> commentsModels;
    PreferenceUtils preferenceUtils;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    public CommentsAdapter(CommentsActivity commentsActivity, ArrayList<CommentsModel> listModelArrayList) {
        context = commentsActivity;
        this.commentsModels = listModelArrayList;
        preferenceUtils=new PreferenceUtils(context);
        progressDialog=new ProgressDialog(context);
    }

    @Override
    public int getCount() {
        return commentsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return commentsModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);

        itemView = inflater.inflate(R.layout.commentitem,parent,false);

        CircleImageView userimage=(CircleImageView)itemView.findViewById(R.id.commentimg);
        TextView username=(TextView)itemView.findViewById(R.id.name);
        TextView comment=(TextView)itemView.findViewById(R.id.comment);
        TextView commentdate=(TextView)itemView.findViewById(R.id.commentdate);
        ImageView delete=(ImageView)itemView.findViewById(R.id.delete);
        ImageView edit=(ImageView)itemView.findViewById(R.id.edit);
        ImageView comment1reply=(ImageView)itemView.findViewById(R.id.comment1reply);
        TextView replies=(TextView)itemView.findViewById(R.id.replies);

        comment.setText(commentsModels.get(position).getComment());
        commentdate.setText(commentsModels.get(position).getDate());
        username.setText(commentsModels.get(position).getName());
        replies.setText("replies:"+commentsModels.get(position).getReplycount());

        replies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,RepliesActivity.class);
                intent.putExtra("commentid",commentsModels.get(position).getCommentid());
                intent.putExtra("newsid",commentsModels.get(position).getPostid());
                context.startActivityForResult(intent,100);
            }
        });

        Glide.with(context).load(commentsModels.get(position).getImage())
                .asBitmap().placeholder(R.drawable.loading)
                .into(userimage);

        if (commentsModels.get(position).getUserid().equals(preferenceUtils.getUserId())){
            delete.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            comment1reply.setVisibility(View.GONE);
        }else {
            delete.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            comment1reply.setVisibility(View.VISIBLE);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePopup(commentsModels.get(position).getCommentid());
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPopup(commentsModels.get(position).getCommentid(),commentsModels.get(position).getComment());
            }
        });
        comment1reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyPopup(commentsModels.get(position).getCommentid(),commentsModels.get(position).getComment());
            }
        });

        return itemView;
    }
    public void deletePopup(final String commentid){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.editpopup,null,false);
        builder.setView(view);

        TextView heading=(TextView)view.findViewById(R.id.heading);
        TextView yes=(TextView)view.findViewById(R.id.edit);
        TextView no=(TextView)view.findViewById(R.id.cancel);
        EditText et_comment=(EditText)view.findViewById(R.id.et_comment);
        TextView comment=(TextView)view.findViewById(R.id.comment);

        comment.setText("Are You Sure Want to delete ?");
        et_comment.setVisibility(View.GONE);

        heading.setText("Delete");
        yes.setText("Yes");
        no.setText("No");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteComment(commentid);
                alertDialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog=builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void editPopup(final String commentid, final String comment){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.editpopup,null,false);
        builder.setView(view);

        TextView edit=(TextView)view.findViewById(R.id.edit);
        TextView cancel=(TextView)view.findViewById(R.id.cancel);
        final EditText et_comment=(EditText)view.findViewById(R.id.et_comment);
        TextView commenthead=(TextView)view.findViewById(R.id.comment);
        edit.setText("Edit");
        et_comment.setText(comment);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editComment(commentid,et_comment.getText().toString());
                alertDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog=builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void replyPopup(final String commentid, final String comment){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.editpopup,null,false);
        builder.setView(view);

        TextView heading=(TextView)view.findViewById(R.id.heading);
        TextView edit=(TextView)view.findViewById(R.id.edit);
        TextView cancel=(TextView)view.findViewById(R.id.cancel);
        final EditText et_comment=(EditText)view.findViewById(R.id.et_comment);
        TextView commenthead=(TextView)view.findViewById(R.id.comment);

        heading.setText("Reply");
        edit.setText("Reply");
        commenthead.setText("Reply :");
        et_comment.setHint("Please Post Your Reply");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyComment(commentid,et_comment.getText().toString());
                alertDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog=builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void editComment(final String commentid, final String comment){
        final String sessionid=preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")){
            Toast.makeText(context,"Please make login",Toast.LENGTH_LONG).show();
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
                APICalls.CommentEdit apiService = retrofit.create(APICalls.CommentEdit.class);
                Call<ResponseBody> call = apiService.commentEdit();
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
                                    context.CommentsGet();
                                }else{
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    public void deleteComment(final String commentid){
        final String sessionid=preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")){
            Toast.makeText(context,"Please make login",Toast.LENGTH_LONG).show();
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
                APICalls.CommentDelete apiService = retrofit.create(APICalls.CommentDelete.class);
                Call<ResponseBody> call = apiService.commentDelete();
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
                                    context.CommentsGet();
                                }else{
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    public void replyComment(final String commentid, final String comment){
        final String sessionid=preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")){
            Toast.makeText(context,"Please make login",Toast.LENGTH_LONG).show();
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
                                    context.CommentsGet();
                                }else{
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(context);
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
