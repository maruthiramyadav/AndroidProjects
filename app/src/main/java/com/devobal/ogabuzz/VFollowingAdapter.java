package com.devobal.ogabuzz;

import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.Modals.RecomendListModel;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class VFollowingAdapter extends BaseAdapter {

    Following context;
    ArrayList<RecomendListModel> RecomendListModelArrayList;
    //private ViewPagerAdapter adapter;
    private TextView[] dots;
    private LayoutInflater layoutInflater;
    ProgressDialog progressDialog;
    PreferenceUtils preferenceUtils;
    String newsid, image,videopath;
    //  private ViewPagerAdapter adapter;
    ArrayList<String> imagesarray = new ArrayList<String>();
    private LinearLayout layout;
    private boolean doNotifyDataSetChangedOnce = false;

    public VFollowingAdapter(Following ctx, ArrayList<RecomendListModel> RecomendListModelArrayList) {
        context = ctx;
        this.RecomendListModelArrayList = RecomendListModelArrayList;
        progressDialog=new ProgressDialog(ctx.getActivity());
        preferenceUtils=new PreferenceUtils(ctx.getActivity());
        this.layoutInflater = (LayoutInflater)this.context.getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return RecomendListModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return RecomendListModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.recomendfollowlistitem,parent,false);

        TextView newstitle = (TextView) convertView.findViewById(R.id.recnewstitle);
        ImageView newsimg = (ImageView) convertView.findViewById(R.id.recnewsimg);
        //ImageView videoimg = (ImageView) convertView.findViewById(R.id.recvideoimg);
        TextView shrtdesc = (TextView) convertView.findViewById(R.id.recshrtdesc);
        LinearLayout mainlinear = (LinearLayout) convertView.findViewById(R.id.mainlinear);
        //final SimpleExoPlayerView simpleExoPlayer = (SimpleExoPlayerView) convertView.findViewById(R.id.recexoplayer);
        //final JCVideoPlayerStandard simpleExoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.recexoplayer);
        //  page = (ViewPager) convertView.findViewById(R.id.viewpager);
        //  layout = (LinearLayout) convertView.findViewById(R.id.layoutdots);

        newstitle.setText(RecomendListModelArrayList.get(position).getTitle());
        shrtdesc.setText(RecomendListModelArrayList.get(position).getDescription());
        image = RecomendListModelArrayList.get(position).getImagelink();
        videopath= RecomendListModelArrayList.get(position).getVideopath();
        if(image!=null && !image.equals("")&& !image.equals("null")) {
            Glide.with(context).load(image)
                    .asBitmap().placeholder(R.drawable.loading)
                    .into(newsimg);
            //videoimg.setVisibility(View.GONE);
            //simpleExoPlayer.setVisibility(View.GONE);
            newsimg.setVisibility(View.VISIBLE);
        }else if(videopath!=null && !videopath.equals("")&& !videopath.equals("null")){
            //videoimg.setVisibility(View.VISIBLE);
            //simpleExoPlayer.setVisibility(View.VISIBLE);
            newsimg.setVisibility(View.GONE);

        }else{
            //videoimg.setVisibility(View.GONE);
            //simpleExoPlayer.setVisibility(View.GONE);
            newsimg.setVisibility(View.GONE);
        }

        /*simpleExoPlayer.setUp(videopath
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"");*/

        /*videoimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* simpleExoPlayer.setPlayer(Exoplayer.getSharedInstance(context.getContext()).getSimpleExoPlayerView().getPlayer());
                Exoplayer.getSharedInstance(context.getContext()).playStream(videopath);*//*
            }
        });*/

        return convertView;
    }
}