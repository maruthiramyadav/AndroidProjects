package com.devobal.ogabuzz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.Modals.YoutubeModel;

import java.util.ArrayList;

public class YoutubePlayerAdapter extends BaseAdapter/*RecyclerView.Adapter<YoutubePlayerAdapter.MyViewHolder>*/{

    YoutubePlayerActivity context;
    ArrayList<YoutubeModel> arrayList;
    public YoutubePlayerAdapter(YoutubePlayerActivity youtubePlayerActivity, ArrayList<YoutubeModel> youtubeModels) {
        this.context=youtubePlayerActivity;
        this.arrayList=youtubeModels;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView=inflater.inflate(R.layout.youtubeview,null);
        final ImageView imageView,view;
        TextView play,select;
        imageView=(ImageView)rootView.findViewById(R.id.thumbnail);
        view=(ImageView)rootView.findViewById(R.id.view);
        select=(TextView)rootView.findViewById(R.id.select);
        play=(TextView)rootView.findViewById(R.id.play);

        Glide.with(context).load(arrayList.get(position).getThumbnailimage())
                .asBitmap().placeholder(R.drawable.loading)
                .into(imageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               view.setVisibility(View.GONE);
                Intent intent=new Intent(context,VideoPlayActvity.class);
                intent.putExtra("video",arrayList.get(position).getVideopath());
                context.startActivity(intent);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               view.performClick();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AddNewsActivity.class);
                intent.putExtra("videopath","https://www.youtube.com/watch?v="+arrayList.get(position).getVideopath());
                intent.putExtra("thumbimage",arrayList.get(position).getThumbnailimage());
                intent.putExtra("title",arrayList.get(position).getTitle());
                context.startActivity(intent);
                context.finish();
            }
        });

        return rootView;
    }
}
