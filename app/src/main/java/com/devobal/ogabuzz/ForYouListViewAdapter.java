package com.devobal.ogabuzz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.Modals.ListModel;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.devobal.ogabuzz.SpecialClasses.Utility;

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

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ForYouListViewAdapter extends RecyclerView.Adapter {

    ForYouFragment context;
    ArrayList<ListModel> listModelArrayList;
    ProgressDialog progressDialog;
    PreferenceUtils preferenceUtils;
    String newsid, image,videopath,videolink,videotype;
    ArrayList<String> imagesarray ;
    EditText edcomments;
    AlertDialog commentdialog;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public ForYouListViewAdapter(ForYouFragment context, ArrayList<ListModel> listModelArrayList, RecyclerView listview) {
        this.context = context;
        this.listModelArrayList = listModelArrayList;
        progressDialog=new ProgressDialog(context.getActivity());
        preferenceUtils=new PreferenceUtils(context.getActivity());
        imagesarray = new ArrayList<String>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
       /* if (viewtype == VIEW_TYPE_ITEM) {*/
            if (preferenceUtils.getBooleanFromSP(context.getActivity(),"gridtype")){
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.foryougriditem, viewGroup, false);
                return new MyViewHolder(v);
            }else {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.foryoulistitem, viewGroup, false);
                return new MyViewHolder(v);
            }
       /* } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress, viewGroup, false);
            return new MyProgress(v);
        }*/
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
       /* if (holder instanceof MyProgress){
            ((MyProgress) holder).progressBar.setIndeterminate(true);
        }else {*/
            context.optionshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = listModelArrayList.get(position).getNewsfrom();
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "News From");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });

            context.optioncomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView commentsave, commentcancel;
                    AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
                    LayoutInflater layoutInflater = (LayoutInflater) context.getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    final View dialoglayout = layoutInflater.inflate(R.layout.commentpopup, null);
                    builder.setView(dialoglayout);

                    edcomments = (EditText) dialoglayout.findViewById(R.id.edcomments);
                    commentsave = (TextView) dialoglayout.findViewById(R.id.commentsave);
                    commentcancel = (TextView) dialoglayout.findViewById(R.id.commentcancel);
                    commentsave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CommentPost(preferenceUtils.getSelNewsId());
                            commentdialog.dismiss();
                        }
                    });
                    commentcancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            commentdialog.dismiss();
                        }
                    });
                    commentdialog = builder.create();
                    commentdialog.setCanceledOnTouchOutside(false);
                    commentdialog.setCancelable(false);
                    commentdialog.show();
                }
            });

            context.followmoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FollowPost(listModelArrayList.get(position).getUserid());
                }
            });

            imagesarray = (listModelArrayList.get(position).getPhotosArray());

           ((MyViewHolder)holder).itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context.getActivity(), ListDetailsActivity.class);
                    String newsid = listModelArrayList.get(position).getNewsid();
                    String path = listModelArrayList.get(position).getUserid();
                    i.putExtra("userid", listModelArrayList.get(position).getUserid());
                    i.putExtra("newsid", newsid);
                    i.putStringArrayListExtra("imageslist", listModelArrayList.get(position).getPhotosArray());
                    i.putExtra("videopath", listModelArrayList.get(position).getVideopath());
                    i.putExtra("image", listModelArrayList.get(position).getImgstring());
                    i.putExtra("date", listModelArrayList.get(position).getDateandtime());
                    i.putExtra("likescount", listModelArrayList.get(position).getNooflike());
                    i.putExtra("viewscount", listModelArrayList.get(position).getNoofview());
                    i.putExtra("commentcount", listModelArrayList.get(position).getNoofcomnts());
                    i.putExtra("title", listModelArrayList.get(position).getTitle());
                    i.putExtra("description", listModelArrayList.get(position).getDescription());
                    i.putExtra("news_from", listModelArrayList.get(position).getNewsfrom());
                    i.putExtra("videolink", listModelArrayList.get(position).getVideolink());
                    i.putExtra("videotype", listModelArrayList.get(position).getVideotype());
                    i.putExtra("listposition",position);
                    context.startActivityForResult(i, 111);
                }
            });

           ((MyViewHolder)holder).sharearrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = listModelArrayList.get(position).getNewsfrom();
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "News From");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });

           ((MyViewHolder)holder).optionsMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    preferenceUtils.setSelNewsId(listModelArrayList.get(position).getNewsid());
                    context.OptionsMenuSelected();
                }
            });
           ((MyViewHolder)holder).likeslayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newsid=listModelArrayList.get(position).getNewsid();
                /*if (holder.likeimage.getVisibility()==View.VISIBLE){
                   ((MyViewHolder)holder).unlike.setVisibility(View.VISIBLE);
                   ((MyViewHolder)holder).likeimage.setVisibility(View.INVISIBLE);
                }else {
                   ((MyViewHolder)holder).unlike.setVisibility(View.INVISIBLE);
                   ((MyViewHolder)holder).likeimage.setVisibility(View.VISIBLE);
                }*/
                    postlike();
                }
            });

            String title = Html.fromHtml(listModelArrayList.get(position).getTitle()).toString();
            String slug = Html.fromHtml(listModelArrayList.get(position).getSlug()).toString();
            String desc = Html.fromHtml(listModelArrayList.get(position).getDescription()).toString();

           ((MyViewHolder)holder).newstitle.setText(Html.fromHtml(title));
           ((MyViewHolder)holder).shrtdesc.setText(Html.fromHtml(desc));
           ((MyViewHolder)holder).author.setText(Html.fromHtml(desc));
            //postcat.setText(listModelArrayList.get(position).getCategory());
           ((MyViewHolder)holder).newsimg.setImageResource(listModelArrayList.get(position).getImage());

           ((MyViewHolder)holder).postdate.setText(listModelArrayList.get(position).getDateandtime());
           ((MyViewHolder)holder).likecount.setText(listModelArrayList.get(position).getNooflike());
           ((MyViewHolder)holder).viewcount.setText(listModelArrayList.get(position).getNoofview());
           ((MyViewHolder)holder).commentcount.setText(listModelArrayList.get(position).getNoofcomnts());
            image = listModelArrayList.get(position).getImgstring();
            videopath = listModelArrayList.get(position).getVideopath();
            videolink = listModelArrayList.get(position).getVideolink();
            videotype = listModelArrayList.get(position).getVideotype();

            if (imagesarray != null && imagesarray.size() > 0) {
                Glide.with(context).load(imagesarray.get(0))
                        .asBitmap().placeholder(R.drawable.loading)
                        .into(((MyViewHolder)holder).newsimg);
               ((MyViewHolder)holder).videoimg.setVisibility(View.GONE);
               ((MyViewHolder)holder).videoview.setVisibility(View.GONE);
               ((MyViewHolder)holder).newsimg.setVisibility(View.VISIBLE);
            } else if (!Utility.isNotAnValidString(videopath)) {
               ((MyViewHolder)holder).videoimg.setVisibility(View.VISIBLE);
               ((MyViewHolder)holder).videoview.setVisibility(View.VISIBLE);
               ((MyViewHolder)holder).newsimg.setVisibility(View.GONE);
            } else if (!Utility.isNotAnValidString(videolink)) {
               ((MyViewHolder)holder).videoimg.setVisibility(View.VISIBLE);
               ((MyViewHolder)holder).videoview.setVisibility(View.GONE);
               ((MyViewHolder)holder).newsimg.setVisibility(View.VISIBLE);
                String[] thumbnail = videolink.split("=");
                Glide.with(context).load("https://i.ytimg.com/vi/" + thumbnail[1] + "/hqdefault.jpg")
                        .asBitmap().placeholder(R.drawable.loading)
                        .into(((MyViewHolder)holder).newsimg);
            } else if (image != null && !image.equals("") && !image.equals("null")) {
                Glide.with(context).load(image)
                        .asBitmap().placeholder(R.drawable.loading)
                        .into(((MyViewHolder)holder).newsimg);
               ((MyViewHolder)holder).videoimg.setVisibility(View.GONE);
               ((MyViewHolder)holder).videoview.setVisibility(View.GONE);
               ((MyViewHolder)holder).newsimg.setVisibility(View.VISIBLE);
            } else {
               ((MyViewHolder)holder).videoimg.setVisibility(View.GONE);
               ((MyViewHolder)holder).videoview.setVisibility(View.GONE);
               ((MyViewHolder)holder).newsimg.setVisibility(View.GONE);
                //postcat.setVisibility(View.GONE);
            }
        /*}*/
    }

    @Override
    public int getItemCount() {
        return /*listModelArrayList == null ? 0 :*/ listModelArrayList.size();
    }

    /*@Override
    public int getItemViewType(int position) {
        return listModelArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }*/

    public void postlike(){
      final String sessionid=preferenceUtils.getSessionId();
      if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")){
          Toast.makeText(context.getActivity(),"Please make login",Toast.LENGTH_LONG).show();
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
              APICalls.SaveLike apiService = retrofit.create(APICalls.SaveLike.class);
              Call<ResponseBody> call = apiService.savelike();
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
                                  context.NewsListGet();
                              }else{
                                  String message = jsonObject.getString("message");
                                  Toast.makeText(context.getActivity(), message, Toast.LENGTH_SHORT).show();
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
            progressDialog = new ProgressDialog(context.getActivity());
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

    private void CommentPost(final String newsid) {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    String sessionid = preferenceUtils.getSessionId();
                    Request original = chain.request();
                    String comment = edcomments.getText().toString().trim();
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
            showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismissProgressDislogue();
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
                                context.NewsListGet();
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

    private void FollowPost(final String userid) {
        final String sessionid = preferenceUtils.getSessionId();
        if (sessionid.equals("0") || sessionid.equals(null) || sessionid.equals("null")) {
            Toast.makeText(context.getActivity(), "Please Make Login", Toast.LENGTH_LONG).show();
        } else {
            try {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("authenticate_key", "abcd123XYZ")
                                .header("sessionid", sessionid)
                                .header("follow_user_id", userid)
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
                APICalls.MakeFollow apiService = retrofit.create(APICalls.MakeFollow.class);
                Call<ResponseBody> call = apiService.makefollow();
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
                                    Toast.makeText(context.getActivity(), "Followed Successfully", Toast.LENGTH_LONG).show();
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView newstitle,author,shrtdesc,postcat,postdate,likecount,viewcount,commentcount;
        ImageView newsimg,videoimg,sharearrow,optionsMenu,likeimage;
        LinearLayout datelayout,shareslayout,cmntlayout,mainlinear,itemview;
        VideoView videoview;
        RelativeLayout likeslayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            newstitle = (TextView) itemView.findViewById(R.id.newstitle);
            author = (TextView) itemView.findViewById(R.id.author);
            shrtdesc = (TextView) itemView.findViewById(R.id.shrtdesc);
            newsimg = (ImageView) itemView.findViewById(R.id.newsimg);
            videoimg = (ImageView) itemView.findViewById(R.id.videoimg);
            postcat = (TextView) itemView.findViewById(R.id.postcat);
            datelayout = (LinearLayout) itemView.findViewById(R.id.datelayout);
            postdate = (TextView)itemView.findViewById(R.id.postdate);
            likecount = (TextView) itemView.findViewById(R.id.likecount);
            shareslayout = (LinearLayout) itemView.findViewById(R.id.shareslayout);
            viewcount = (TextView) itemView.findViewById(R.id.viewcount);
            cmntlayout = (LinearLayout) itemView.findViewById(R.id.cmntlayout);
            commentcount = (TextView) itemView.findViewById(R.id.commentcount);
            sharearrow = (ImageView) itemView.findViewById(R.id.sharearrow);
            mainlinear = (LinearLayout) itemView.findViewById(R.id.mainlinear);
            optionsMenu=(ImageView)itemView.findViewById(R.id.optionsMenu);
            likeimage=(ImageView)itemView.findViewById(R.id.likeimage);
            itemview=(LinearLayout)itemView.findViewById(R.id.itemview);
            videoview = (VideoView) itemView.findViewById(R.id.videoview);
            likeslayout = (RelativeLayout)itemView.findViewById(R.id.likeslayout);
        }
    }

    public class MyProgress extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public MyProgress(@NonNull View itemView) {
            super(itemView);
            progressBar=(ProgressBar)itemView.findViewById(R.id.progressBar1);
        }
    }
}
