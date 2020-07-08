package com.devobal.ogabuzz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Devobal on 3/8/2019 on 10:24 PM.
 */
public class VideosTrendListViewAdapter extends RecyclerView.Adapter {

    OnlyVideosTrendingFragment context;
    ArrayList<ListModel> listModelArrayList;
    //private ViewPagerAdapter adapter;
    private TextView[] dots;
    private LayoutInflater layoutInflater;
    ProgressDialog progressDialog;
    PreferenceUtils preferenceUtils;
    String newsid, image,videopath,video_thumb_image,videolink,videotype;
    //  private ViewPagerAdapter adapter;
    ArrayList<String> imagesarray = new ArrayList<String>();
    private LinearLayout layout;
    private boolean doNotifyDataSetChangedOnce = false;
    EditText edcomments;
    AlertDialog commentdialog;

    public VideosTrendListViewAdapter(OnlyVideosTrendingFragment context, ArrayList<ListModel> listModelArrayList, RecyclerView listview) {
        this.context = context;
        this.listModelArrayList = listModelArrayList;
        progressDialog=new ProgressDialog(context.getActivity());
        preferenceUtils=new PreferenceUtils(context.getActivity());
        this.layoutInflater = (LayoutInflater)this.context.getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        /*imglayout = new int[]{R.drawable.finance, R.drawable.comment_avatar_01,R.drawable.article_list_img1,R.drawable.article_list_img_2};
        this.layoutInflater = (LayoutInflater)this.context.getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);*/
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)context.getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.foryoulistitem,parent,false);
        return new MyViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        context.optionshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Testing Share";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        context.optioncomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView commentsave,commentcancel;
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
                        CommentPost(listModelArrayList.get(position).getNewsid());
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
        //context.breakingnews.setText(listModelArrayList.get(position).getDescription());

        imagesarray=(listModelArrayList.get(position).getPhotosArray());

        ((MyViewHolder)holder).itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getActivity(), ListDetailsActivity.class);
                i.putExtra("userid",listModelArrayList.get(position).getUserid());
                i.putExtra("newsid",listModelArrayList.get(position).getNewsid());
                i.putStringArrayListExtra("imageslist", listModelArrayList.get(position).getPhotosArray());
                i.putExtra("videopath", listModelArrayList.get(position).getVideopath());
                i.putExtra("video_thumb_image",listModelArrayList.get(position).getVideo_thumb_image());
                i.putExtra("image", listModelArrayList.get(position).getImgstring());
                i.putExtra("date", listModelArrayList.get(position).getDateandtime());
                i.putExtra("viewscount", listModelArrayList.get(position).getNoofview());
                i.putExtra("commentcount", listModelArrayList.get(position).getNoofcomnts());
                i.putExtra("title", listModelArrayList.get(position).getTitle());
                i.putExtra("description",listModelArrayList.get(position).getDescription());
                i.putExtra("news_from",listModelArrayList.get(position).getNewsfrom());
                i.putExtra("videolink",listModelArrayList.get(position).getVideolink());
                i.putExtra("videotype",listModelArrayList.get(position).getVideotype());
                context.startActivityForResult(i,126);
            }
        });
        ((MyViewHolder)holder).sharearrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Testing Share";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        ((MyViewHolder)holder).optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.OptionsMenuSelected();
            }
        });

        /*viewslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsid=listModelArrayList.get(position).getNewsid();
                postlike();
            }
        });*/

        ((MyViewHolder)holder).newstitle.setText(Html.fromHtml(listModelArrayList.get(position).getTitle()));
        ((MyViewHolder)holder).shrtdesc.setText(Html.fromHtml(listModelArrayList.get(position).getDescription()));
        ((MyViewHolder)holder).author.setText(Html.fromHtml(listModelArrayList.get(position).getDescription()));
        // postcat.setText(listModelArrayList.get(position).getCategory());
        ((MyViewHolder)holder).newsimg.setImageResource(listModelArrayList.get(position).getImage());

        ((MyViewHolder)holder).postdate.setText(listModelArrayList.get(position).getDateandtime());
        ((MyViewHolder)holder).likecount.setText(listModelArrayList.get(position).getNooflike());
        ((MyViewHolder)holder).viewcount.setText(listModelArrayList.get(position).getNoofview());
        ((MyViewHolder)holder).commentcount.setText(listModelArrayList.get(position).getNoofcomnts());
        image =listModelArrayList.get(position).getImgstring();
        videopath= listModelArrayList.get(position).getVideopath();
        video_thumb_image=listModelArrayList.get(position).getVideo_thumb_image();
        videolink=listModelArrayList.get(position).getVideolink();
        videotype=listModelArrayList.get(position).getVideotype();

        if(imagesarray!=null && imagesarray.size()>0){
            Glide.with(context).load(imagesarray.get(0))
                    .asBitmap().placeholder(R.drawable.loading)
                    .into(((MyViewHolder)holder).newsimg);
            ((MyViewHolder)holder).videoimg.setVisibility(View.GONE);
            ((MyViewHolder)holder).videoview.setVisibility(View.GONE);
            ((MyViewHolder)holder).newsimg.setVisibility(View.VISIBLE);

        } else if(!Utility.isNotAnValidString(videopath)) {
            ((MyViewHolder)holder).videoimg.setVisibility(View.VISIBLE);
            ((MyViewHolder)holder).videoview.setVisibility(View.VISIBLE);
            ((MyViewHolder)holder).newsimg.setVisibility(View.GONE);
        }else if (!Utility.isNotAnValidString(videolink)) {
            ((MyViewHolder)holder).videoimg.setVisibility(View.VISIBLE);
            ((MyViewHolder)holder).videoview.setVisibility(View.GONE);
            ((MyViewHolder)holder).newsimg.setVisibility(View.VISIBLE);
            String[] thumbnail=videolink.split("=");
            Glide.with(context).load("https://i.ytimg.com/vi/"+thumbnail[1]+"/hqdefault.jpg")
                    .asBitmap().placeholder(R.drawable.loading)
                    .into(((MyViewHolder)holder).newsimg);
        } else if(image!=null && !image.equals("")&& !image.equals("null")) {
            Glide.with(context).load(image)
                    .asBitmap().placeholder(R.drawable.loading)
                    .into(((MyViewHolder)holder).newsimg);
            ((MyViewHolder)holder).videoimg.setVisibility(View.GONE);
            ((MyViewHolder)holder).videoview.setVisibility(View.GONE);
            ((MyViewHolder)holder).newsimg.setVisibility(View.VISIBLE);

        }else if (video_thumb_image!=null && !video_thumb_image.equals("") && !video_thumb_image.equals("null")){
            Glide.with(context).load(video_thumb_image)
                    .asBitmap().placeholder(R.drawable.loading)
                    .into(((MyViewHolder)holder).newsimg);
            ((MyViewHolder)holder).videoimg.setVisibility(View.GONE);
            ((MyViewHolder)holder).videoview.setVisibility(View.GONE);
            ((MyViewHolder)holder).newsimg.setVisibility(View.VISIBLE);

        } else{
            ((MyViewHolder)holder).videoimg.setVisibility(View.GONE);
            ((MyViewHolder)holder).videoview.setVisibility(View.GONE);
            ((MyViewHolder)holder).newsimg.setVisibility(View.GONE);
            //postcat.setVisibility(View.GONE);
        }
    }

   /* @Override
    public long getItemId(int position) {
        return position;
    }*/
   /*@Override
    public int getCount() {
        return listModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return listModelArrayList.get(position);
    }*/

    @Override
    public int getItemCount() {
        return listModelArrayList.size();
    }

   /* @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.foryoulistitem,parent,false);

        TextView newstitle = (TextView) convertView.findViewById(R.id.newstitle);
        TextView author = (TextView) convertView.findViewById(R.id.author);
        ImageView newsimg = (ImageView) convertView.findViewById(R.id.newsimg);
        VideoView videoview = (VideoView) convertView.findViewById(R.id.videoview);
        ImageView videoimg = (ImageView) convertView.findViewById(R.id.videoimg);
        TextView postcat = (TextView) convertView.findViewById(R.id.postcat);
        LinearLayout datelayout = (LinearLayout) convertView.findViewById(R.id.datelayout);
        TextView postdate = (TextView)convertView.findViewById(R.id.postdate);
        LinearLayout viewslayout = (LinearLayout) convertView.findViewById(R.id.viewslayout);
        TextView likecount = (TextView) convertView.findViewById(R.id.likecount);
        LinearLayout shareslayout = (LinearLayout) convertView.findViewById(R.id.shareslayout);
        TextView viewcount = (TextView) convertView.findViewById(R.id.viewcount);
        LinearLayout cmntlayout = (LinearLayout) convertView.findViewById(R.id.cmntlayout);
        TextView commentcount = (TextView) convertView.findViewById(R.id.commentcount);
        TextView shrtdesc = (TextView) convertView.findViewById(R.id.shrtdesc);
        ImageView sharearrow = (ImageView) convertView.findViewById(R.id.sharearrow);
        LinearLayout mainlinear = (LinearLayout) convertView.findViewById(R.id.mainlinear);
        ImageView optionsMenu=(ImageView)convertView.findViewById(R.id.optionsMenu);
        ImageView likeimage=(ImageView)convertView.findViewById(R.id.likeimage);
        ImageView images_viewpager = (ImageView) convertView.findViewById(R.id.images_viewpager);
        ImageView prev = (ImageView) convertView.findViewById(R.id.leftarrow);
        ImageView next = (ImageView) convertView.findViewById(R.id.rightarrow);
        LinearLayout itemview=(LinearLayout)convertView.findViewById(R.id.itemview);
        //  ViewPager page = (ViewPager) convertView.findViewById(R.id.viewpager);
        //  layout = (LinearLayout) convertView.findViewById(R.id.layoutdots);
        //LinearLayout pagerLayout = (LinearLayout) convertView.findViewById(R.id.pagerLayout);

        context.optionshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Testing Share";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        context.optioncomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView commentsave,commentcancel;
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
                        CommentPost(listModelArrayList.get(position).getNewsid());
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
        //context.breakingnews.setText(listModelArrayList.get(position).getDescription());

        imagesarray=(listModelArrayList.get(position).getPhotosArray());

        itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getActivity(), ListDetailsActivity.class);
                i.putExtra("userid",listModelArrayList.get(position).getUserid());
                i.putExtra("newsid",listModelArrayList.get(position).getNewsid());
                i.putStringArrayListExtra("imageslist", listModelArrayList.get(position).getPhotosArray());
                i.putExtra("videopath", listModelArrayList.get(position).getVideopath());
                i.putExtra("video_thumb_image",listModelArrayList.get(position).getVideo_thumb_image());
                i.putExtra("image", listModelArrayList.get(position).getImgstring());
                i.putExtra("date", listModelArrayList.get(position).getDateandtime());
                i.putExtra("viewscount", listModelArrayList.get(position).getNoofview());
                i.putExtra("commentcount", listModelArrayList.get(position).getNoofcomnts());
                i.putExtra("title", listModelArrayList.get(position).getTitle());
                i.putExtra("description",listModelArrayList.get(position).getDescription());
                i.putExtra("news_from",listModelArrayList.get(position).getNewsfrom());
                i.putExtra("videolink",listModelArrayList.get(position).getVideolink());
                i.putExtra("videotype",listModelArrayList.get(position).getVideotype());
                context.getActivity().startActivityForResult(i,126);
            }
        });
        sharearrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Testing Share";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        optionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.OptionsMenuSelected();
            }
        });

        *//*viewslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsid=listModelArrayList.get(position).getNewsid();
                postlike();
            }
        });*//*

        newstitle.setText(Html.fromHtml(listModelArrayList.get(position).getTitle()));
        shrtdesc.setText(Html.fromHtml(listModelArrayList.get(position).getDescription()));
        author.setText(Html.fromHtml(listModelArrayList.get(position).getDescription()));
       // postcat.setText(listModelArrayList.get(position).getCategory());
        newsimg.setImageResource(listModelArrayList.get(position).getImage());

        postdate.setText(listModelArrayList.get(position).getDateandtime());
        likecount.setText(listModelArrayList.get(position).getNooflike());
        viewcount.setText(listModelArrayList.get(position).getNoofview());
        commentcount.setText(listModelArrayList.get(position).getNoofcomnts());
        image =listModelArrayList.get(position).getImgstring();
        videopath= listModelArrayList.get(position).getVideopath();
        video_thumb_image=listModelArrayList.get(position).getVideo_thumb_image();
        videolink=listModelArrayList.get(position).getVideolink();
        videotype=listModelArrayList.get(position).getVideotype();

        if(imagesarray!=null && imagesarray.size()>0){
            Glide.with(context).load(imagesarray.get(0))
                    .asBitmap().place((MyViewHolder)holder)(R.drawable.loading)
                    .into(newsimg);
            videoimg.setVisibility(View.GONE);
            videoview.setVisibility(View.GONE);
            newsimg.setVisibility(View.VISIBLE);

        } else if(!Utility.isNotAnValidString(videopath)) {
            videoimg.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.VISIBLE);
            newsimg.setVisibility(View.GONE);
            }else if (!Utility.isNotAnValidString(videolink)) {
            videoimg.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.GONE);
            newsimg.setVisibility(View.VISIBLE);
            String[] thumbnail=videolink.split("=");
            Glide.with(context).load("https://i.ytimg.com/vi/"+thumbnail[1]+"/hqdefault.jpg")
                    .asBitmap().placeholder(R.drawable.loading)
                    .into(newsimg);
        } else if(image!=null && !image.equals("")&& !image.equals("null")) {
            Glide.with(context).load(image)
                    .asBitmap().placeholder(R.drawable.loading)
                    .into(newsimg);
            videoimg.setVisibility(View.GONE);
            videoview.setVisibility(View.GONE);
            newsimg.setVisibility(View.VISIBLE);

        }else if (video_thumb_image!=null && !video_thumb_image.equals("") && !video_thumb_image.equals("null")){
            Glide.with(context).load(video_thumb_image)
                    .asBitmap().placeholder(R.drawable.loading)
                    .into(newsimg);
            videoimg.setVisibility(View.GONE);
            videoview.setVisibility(View.GONE);
            newsimg.setVisibility(View.VISIBLE);

        } else{
            videoimg.setVisibility(View.GONE);
            videoview.setVisibility(View.GONE);
            newsimg.setVisibility(View.GONE);
            //postcat.setVisibility(View.GONE);
        }
        return convertView;
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
                                    Toast.makeText(context.getActivity(), "Like Posted Successfully", Toast.LENGTH_LONG).show();
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
                            .header("sessionid", sessionid)
                            .header("newsid", newsid)
                            .header("comment", comment)
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });
            OkHttpClient client = httpClient.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.ogabuzz.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            APICalls.PostComment apiService= retrofit.create(APICalls.PostComment.class);
            Call<ResponseBody> call = apiService.PostComment();
            showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        dismissProgressDislogue();
                        String s = response.body().string();
                        JSONObject jObject = new JSONObject(s);
                        if(jObject.has("response")) {
                            JSONObject obj = jObject.getJSONObject("response");
                            if (obj.getString("status").equals("error")) {
                                String message = obj.getString("message");
                                Toast.makeText(context.getActivity(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                String message = obj.getString("message");
                                Toast.makeText(context.getActivity(), message, Toast.LENGTH_SHORT).show();
                                // finish();
                            }
                        }

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

    @SuppressLint("NewApi") private void changeStatusBarColor() {
        // TODO Auto-generated method stub
    }

    private void addBottomDots(int currentPage) {
        // TODO Auto-generated method stub

        dots = new TextView[imagesarray.size()];
        int[] colorsActive = context.getActivity().getResources().getIntArray(R.array.array_dot_active);
        int[] colorInActive = context.getActivity().getResources().getIntArray(R.array.array_dot_inactive);
        int[] colorInActive1 = context.getActivity().getResources().getIntArray(R.array.array_dot_inactive);
        layout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(context.getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInActive[currentPage]);
            layout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[currentPage].setTextColor(colorsActive[currentPage]);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView newstitle,author,postcat,postdate,likecount,viewcount,commentcount,shrtdesc;
        ImageView newsimg,videoimg,sharearrow,optionsMenu,likeimage,images_viewpager,prev,next;
        LinearLayout datelayout,viewslayout,shareslayout,ocmntlayout,mainlinear,itemview;
        VideoView videoview;
        public MyViewHolder(View itemView) {
            super(itemView);
            newstitle = (TextView) itemView.findViewById(R.id.newstitle);
            author = (TextView) itemView.findViewById(R.id.author);
            newsimg = (ImageView) itemView.findViewById(R.id.newsimg);
            videoview = (VideoView) itemView.findViewById(R.id.videoview);
            videoimg = (ImageView) itemView.findViewById(R.id.videoimg);
            postcat = (TextView) itemView.findViewById(R.id.postcat);
            datelayout = (LinearLayout) itemView.findViewById(R.id.datelayout);
            postdate = (TextView)itemView.findViewById(R.id.postdate);
            //viewslayout = (LinearLayout) itemView.findViewById(R.id.viewslayout);
            likecount = (TextView) itemView.findViewById(R.id.likecount);
            shareslayout = (LinearLayout) itemView.findViewById(R.id.shareslayout);
            viewcount = (TextView) itemView.findViewById(R.id.viewcount);
            ocmntlayout = (LinearLayout) itemView.findViewById(R.id.cmntlayout);
            commentcount = (TextView) itemView.findViewById(R.id.commentcount);
            shrtdesc = (TextView) itemView.findViewById(R.id.shrtdesc);
            sharearrow = (ImageView) itemView.findViewById(R.id.sharearrow);
            mainlinear = (LinearLayout) itemView.findViewById(R.id.mainlinear);
            optionsMenu=(ImageView)itemView.findViewById(R.id.optionsMenu);
            likeimage=(ImageView)itemView.findViewById(R.id.likeimage);
            images_viewpager = (ImageView) itemView.findViewById(R.id.images_viewpager);
            prev = (ImageView) itemView.findViewById(R.id.leftarrow);
            next = (ImageView) itemView.findViewById(R.id.rightarrow);
            itemview=(LinearLayout)itemView.findViewById(R.id.itemview);
        }
    }
}