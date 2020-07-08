package com.devobal.ogabuzz;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devobal.ogabuzz.Modals.PostModel;

import java.util.ArrayList;

/**
 * Created by Devobal on 2/9/2019 on 12:43 PM.
 */
public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.MyViewHolder>{

    ArrayList<PostModel> arrayList;
    MyProfileActivity activity;

    public ProfilePostAdapter(MyProfileActivity profileActivity, ArrayList<PostModel> postLists) {
        activity=profileActivity;
        this.arrayList=postLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.profilepostchild,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.postname.setText(arrayList.get(position).getName());
        holder.postphoto.setImageResource(arrayList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView postphoto;
        TextView postname;
        public MyViewHolder(View itemView) {
            super(itemView);
            postphoto=(CircleImageView)itemView.findViewById(R.id.postphoto);
            postname=(TextView) itemView.findViewById(R.id.postname);
        }
    }
}

