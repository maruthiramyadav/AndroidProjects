package com.devobal.ogabuzz;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.devobal.ogabuzz.Modals.SourcesListModal;

import java.util.ArrayList;

public class BlogHRcAdapter extends RecyclerView.Adapter<BlogHRcAdapter.MyViewHolder>{

    BlogActivity context;
    ArrayList<SourcesListModal> arraylist;
    public BlogHRcAdapter(BlogActivity blogActivity, ArrayList<SourcesListModal> blogHRCModals) {
        this.context=blogActivity;
        this.arraylist=blogHRCModals;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.hfollowchild,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.paper.setText(arraylist.get(position).getPapername());
        holder.photo.setImageResource(arraylist.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView photo;
        TextView paper;
        public MyViewHolder(View itemView) {
            super(itemView);
            photo=(CircleImageView)itemView.findViewById(R.id.photo);
            paper=(TextView) itemView.findViewById(R.id.paper);
        }
    }
}
