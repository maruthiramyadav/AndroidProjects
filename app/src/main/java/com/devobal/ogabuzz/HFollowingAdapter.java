package com.devobal.ogabuzz;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.Modals.FollowModel;

import java.util.ArrayList;

public class HFollowingAdapter extends RecyclerView.Adapter<HFollowingAdapter.MyViewHolder>{
    Following context;
    ArrayList<FollowModel> arrayList;

    public HFollowingAdapter(Following ctx, ArrayList<FollowModel> hFollowingLists) {
        this.context=ctx;
        this.arrayList=hFollowingLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.followingchild,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.followname.setText(arrayList.get(position).getName());
        holder.followphoto.setImageResource(arrayList.get(position).getImage());

        String image = arrayList.get(position).getFollowimg();
        if(image!=null && !image.equals("")&& !image.equals("null")) {
            Glide.with(context).load(image)
                    .asBitmap().placeholder(R.drawable.loading)
                    .into(holder.followphoto);
        }else{
            holder.followphoto.setImageResource(R.drawable.user_placeholder);
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView followphoto;
        TextView followname,unfollow;
        public MyViewHolder(View itemView) {
            super(itemView);
            followphoto=(CircleImageView)itemView.findViewById(R.id.followingphoto);
            followname=(TextView) itemView.findViewById(R.id.followingname);
            unfollow = (TextView) itemView.findViewById(R.id.unfollow);
        }
    }
}

