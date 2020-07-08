package com.devobal.ogabuzz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devobal.ogabuzz.SpecialClasses.ItemMoveCallback;
import com.devobal.ogabuzz.Modals.NewsModel;

import java.util.ArrayList;
import java.util.Collections;

public class ManageHomeAdapter extends RecyclerView.Adapter<ManageHomeAdapter.MyViewHolder>implements ItemMoveCallback.ItemTouchHelperContract {

    ManageHomeActivity context;
    ArrayList<NewsModel>arrayList;
    public ManageHomeAdapter(ManageHomeActivity manageHomeActivity, ArrayList<NewsModel> myListModel) {
    this.arrayList=myListModel;
    this.context=manageHomeActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView=inflater.inflate(R.layout.managechild,parent,false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.category.setText(arrayList.get(position).getCat_name());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(arrayList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(arrayList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {

    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        public MyViewHolder(View itemView) {
            super(itemView);
            category=(TextView)itemView.findViewById(R.id.name);
        }
    }
}