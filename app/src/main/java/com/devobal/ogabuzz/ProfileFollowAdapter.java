package com.devobal.ogabuzz;

import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.Modals.FollowModel;
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
 * Created by Devobal on 2/9/2019 on 12:16 PM.
 */
public class ProfileFollowAdapter extends RecyclerView.Adapter<ProfileFollowAdapter.MyViewHolder>{
   
    ArrayList<FollowModel> arrayList;
    MyProfileActivity activity;
    AlertDialog removedialog;
    OnItemClickListener listener;
    PreferenceUtils preferenceUtils;
    String sessionid;

    public interface OnItemClickListener{
        void onItemClick(View view, int postion);
    }

    public ProfileFollowAdapter(MyProfileActivity profileActivity, ArrayList<FollowModel> hFollowingLists,
                                OnItemClickListener onItemClickListener) {
        activity=profileActivity;
        this.arrayList=hFollowingLists;
        this.listener = onItemClickListener;
        preferenceUtils = new PreferenceUtils(profileActivity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.profilefollowchild,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.followname.setText(arrayList.get(position).getName());
        holder.followphoto.setImageResource(arrayList.get(position).getImage());
        String image = arrayList.get(position).getFollowimg();
        if(image!=null && !image.equals("")&& !image.equals("null")) {
            Glide.with(activity).load(image)
                    .asBitmap().placeholder(R.drawable.loading)
                    .into(holder.followphoto);
        }else{
            holder.followphoto.setImageResource(R.drawable.user_placeholder);
        }

        holder.followremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView removeyes,removeno;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View dialoglayout = layoutInflater.inflate(R.layout.removepopup, null);
                builder.setView(dialoglayout);

                removeyes = (TextView) dialoglayout.findViewById(R.id.removeyes);
                removeno = (TextView) dialoglayout.findViewById(R.id.removeno);
                removeyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RemovePost(arrayList.get(position).getFollowerid());
                        removedialog.dismiss();
                        // startActivity(i);
                        // finishAffinity();
                    }
                });
                removeno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removedialog.dismiss();
                    }
                });
                removedialog = builder.create();
                removedialog.setCanceledOnTouchOutside(false);
                removedialog.setCancelable(false);
                removedialog.show();
            }
        });

        holder.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,position);
            }
        });
    }

    private void RemovePost(final String followerid) {
        try {
            sessionid = preferenceUtils.getSessionId();
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("sessionid", sessionid)
                            .header("follow_user_id", followerid)
                            .header("action_type", "follower")
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
            APICalls.FolloweRemovePost apiService = retrofit.create(APICalls.FolloweRemovePost.class);
            Call<ResponseBody> call = apiService.FolloweRemovePost();
            // showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = response.body().string();
                        JSONObject obj = new JSONObject(s);
                        if (obj.getString("status").equals("error")) {
                            String message = obj.getString("message");
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        } else {
                            String message = obj.getString("message");
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                           notifyDataSetChanged();
                        }
                      /*  if (jsonObject.has("response")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                            if (jsonObject1.getString("status").equals("success")) {

                               *//* JSONArray jsonArray = new JSONArray(jsonObject1.getString("data"));
                                languagelArrayList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    LanguageModel newsModel = new LanguageModel();
                                    newsModel.setLanguage(dataobj.getString("language"));
                                    newsModel.setLangid(dataobj.getString("langid"));
                                    newsModel.setIsselect(false);
                                    languagelArrayList.add(newsModel);
                                }*//*
                                Toast.makeText((Activity)mContext, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText((Activity)mContext, "Please login to save settings", Toast.LENGTH_SHORT).show();
                            }

                        }*/

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


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView followphoto;
        TextView followname,followremove;
        LinearLayout linear1;

        public MyViewHolder(View itemView) {
            super(itemView);
            followphoto=(CircleImageView)itemView.findViewById(R.id.followphoto);
            followname=(TextView) itemView.findViewById(R.id.followname);
            followremove = (TextView) itemView.findViewById(R.id.followremove);
            linear1 = (LinearLayout) itemView.findViewById(R.id.linear1);
        }
    }
}

