package com.devobal.ogabuzz;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.Modals.RecomendListModel;
import com.devobal.ogabuzz.SpecialClasses.Utility;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;

/**
 * Created by Devobal on 2/28/2019 on 12:46 AM.
 */
public class RecomendAdapter extends RecyclerView.Adapter<RecomendAdapter.MyViewHolder> {
    ArrayList<RecomendListModel> RecomendListModelArrayList;
    FragmentActivity context;
    String newsid, image,videopath,videolink;
    ArrayList<String> imagesarray;

    public RecomendAdapter(FragmentActivity activity, ArrayList<RecomendListModel> RecomendListModelArrayList) {
        this.RecomendListModelArrayList=RecomendListModelArrayList;
        this.context=activity;
        imagesarray=new ArrayList<String>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recomedlistitem,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.newstitle.setText(RecomendListModelArrayList.get(position).getTitle());
        holder.shrtdesc.setText(RecomendListModelArrayList.get(position).getDescription());

        image = RecomendListModelArrayList.get(position).getImagelink();
        videopath= RecomendListModelArrayList.get(position).getVideopath();
        videolink=RecomendListModelArrayList.get(position).getVideolink();
        imagesarray=RecomendListModelArrayList.get(position).getPhotosArray();

        if(!Utility.isNotAnValidString(image)) {
            Glide.with(context).load(image)
                    .asBitmap().placeholder(R.drawable.loading)
                    .into( holder.recnewsimg);
            holder.recnewsimg.setVisibility(View.VISIBLE);
        }else if(!Utility.isNotAnValidString(videopath)){
            holder.recnewsimg.setVisibility(View.VISIBLE);
            holder.videoimg.setVisibility(View.VISIBLE);
        }else if (!Utility.isNotAnValidString(videolink)){
            holder.recnewsimg.setVisibility(View.VISIBLE);
            holder.videoimg.setVisibility(View.VISIBLE);
        }else if (imagesarray.size()!=0){
            holder.recnewsimg.setVisibility(View.VISIBLE);
            holder.videoimg.setVisibility(View.GONE);
        }

       holder.videoimg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(context,RecomendedDetails.class);
               intent.putExtra("videopath",videopath);
               intent.putExtra("videolink",videolink);
               intent.putExtra("image",image);
               intent.putExtra("title",RecomendListModelArrayList.get(position).getTitle());
               intent.putExtra("description",RecomendListModelArrayList.get(position).getDescription());
               intent.putStringArrayListExtra("imagesarray",imagesarray);
               intent.putExtra("userid",RecomendListModelArrayList.get(position).getUserid());
               context.startActivity(intent);
           }
       });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return RecomendListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView newstitle,shrtdesc;
        ImageView recnewsimg,videoimg;
        ProgressBar progressBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            newstitle = (TextView) itemView.findViewById(R.id.recnewstitle);
            recnewsimg = (ImageView) itemView.findViewById(R.id.recnewsimg);
            videoimg = (ImageView) itemView.findViewById(R.id.recvideoimg);
            shrtdesc = (TextView) itemView.findViewById(R.id.recshrtdesc);
        }
    }
}