package com.devobal.ogabuzz;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devobal.ogabuzz.Modals.BlogVRCModal;

import java.util.ArrayList;

public class BlogVRcAdapter extends RecyclerView.Adapter<BlogVRcAdapter.MyViewHolder>{

    BlogActivity context;
    ArrayList<BlogVRCModal> arraylist;

    public BlogVRcAdapter(BlogActivity blogActivity, ArrayList<BlogVRCModal> blogVRCModals) {
        this.context=blogActivity;
        this.arraylist=blogVRCModals;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.bolgvchild,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.image.setImageResource(arraylist.get(position).getPhoto());
        holder.category.setText(arraylist.get(position).getCategory());
        holder.title.setText(arraylist.get(position).getTitle());
        holder.description.setText(arraylist.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView category,title,description;
        public MyViewHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.image);
            category=(TextView) itemView.findViewById(R.id.category);
            title=(TextView)itemView.findViewById(R.id.title);
            description=(TextView)itemView.findViewById(R.id.description);
            title=(TextView)itemView.findViewById(R.id.title);

        }
    }
}
