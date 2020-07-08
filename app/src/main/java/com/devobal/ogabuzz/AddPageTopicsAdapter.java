package com.devobal.ogabuzz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.devobal.ogabuzz.Modals.NewsModel;

import java.util.ArrayList;

public class AddPageTopicsAdapter extends RecyclerView.Adapter<AddPageTopicsAdapter.MyViewHolder>{
    AddPageTopicFragment context;
    ArrayList<NewsModel>arrayList;
    public AddPageTopicsAdapter(AddPageTopicFragment addPageTopicsFragment, ArrayList<NewsModel> myListModels) {
        this.arrayList=myListModels;
        this.context=addPageTopicsFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=(LayoutInflater)context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView=inflater.inflate(R.layout.addpagetopicschild,parent,false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.category.setText(arrayList.get(position).getCat_name());

        holder.addunfill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.get(position).setCategorycheck(true);
                notifyDataSetChanged();
            }
        });

        holder.addfill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.get(position).setCategorycheck(false);
                notifyDataSetChanged();
            }
        });

        if (arrayList.get(position).getCategorycheck().equals(true)){
            holder.addunfill.setVisibility(View.INVISIBLE);
            holder.addfill.setVisibility(View.VISIBLE);
        }else {
            holder.addunfill.setVisibility(View.VISIBLE);
            holder.addfill.setVisibility(View.INVISIBLE);
        }

    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        ImageView photo;
        ImageView addfill,addunfill;
        public MyViewHolder(View itemView) {
            super(itemView);
            category=(TextView)itemView.findViewById(R.id.category);
            photo=(ImageView)itemView.findViewById(R.id.photo);
            addunfill=(ImageView)itemView.findViewById(R.id.addunfill);
            addfill=(ImageView)itemView.findViewById(R.id.addfill);
        }
    }
}
