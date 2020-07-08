package com.devobal.ogabuzz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.Modals.CommentsModel;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;

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

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.MyViewHolder> {

    RepliesActivity context;
    ArrayList<CommentsModel> arrayList;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    public RepliesAdapter(RepliesActivity repliesActivity, ArrayList<CommentsModel> commentsModel) {
        this.context=repliesActivity;
        this.arrayList=commentsModel;
        preferenceUtils=new PreferenceUtils(context);
        progressDialog=new ProgressDialog(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView=inflater.inflate(R.layout.commentitem,parent,false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.comment.setText(arrayList.get(position).getComment());
        holder.commentdate.setText(arrayList.get(position).getDate());
        holder.username.setText(arrayList.get(position).getName());
        holder.replies.setVisibility(View.GONE);

        Glide.with(context).load(arrayList.get(position).getImage())
                .asBitmap().placeholder(R.drawable.loading)
                .into(holder.userimage);

        if (arrayList.get(position).getUserid().equals(preferenceUtils.getUserId())){
            holder.delete.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
            holder.comment1reply.setVisibility(View.GONE);
        }else {
            holder.delete.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
            holder.comment1reply.setVisibility(View.GONE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePopup(arrayList.get(position).getCommentid());
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPopup(arrayList.get(position).getCommentid(),arrayList.get(position).getComment());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userimage;
        TextView comment,commentdate,username,replies;
        ImageView delete,edit,comment1reply;
        public MyViewHolder(View itemView) {
            super(itemView);
            userimage=(CircleImageView)itemView.findViewById(R.id.commentimg);
            username=(TextView)itemView.findViewById(R.id.name);
            comment=(TextView)itemView.findViewById(R.id.comment);
            commentdate=(TextView)itemView.findViewById(R.id.commentdate);
            delete=(ImageView)itemView.findViewById(R.id.delete);
            edit=(ImageView)itemView.findViewById(R.id.edit);
            comment1reply=(ImageView)itemView.findViewById(R.id.comment1reply);
            replies=(TextView) itemView.findViewById(R.id.replies);
        }
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
                et_comment.setText("");
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
