package com.devobal.ogabuzz;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devobal.ogabuzz.Modals.SourcesListModal;

import java.util.ArrayList;

public class SourceEntertainAdapter extends RecyclerView.Adapter<SourceEntertainAdapter.MyViewHolder>{
    Sources context;
    ArrayList<SourcesListModal> arrayList;
    public SourceEntertainAdapter(Sources sources, ArrayList<SourcesListModal> sourcesListModals) {
        this.context=sources;
        this.arrayList=sourcesListModals;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.sourcechild,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.paper.setText(arrayList.get(position).getPapername());
        holder.photo.setImageResource(arrayList.get(position).getPhoto());
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.follow.setVisibility(View.INVISIBLE);
                holder.following.setVisibility(View.VISIBLE);
            }
        });

        holder.following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.follow.setVisibility(View.VISIBLE);
                holder.following.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView paper;
        LinearLayout follow,following;
        ImageView photo;
        public MyViewHolder(View itemView) {
            super(itemView);
            photo=(ImageView)itemView.findViewById(R.id.photo);
            paper=(TextView)itemView.findViewById(R.id.paper);
            follow=(LinearLayout) itemView.findViewById(R.id.follow);
            following=(LinearLayout) itemView.findViewById(R.id.following);
        }
    }
}
