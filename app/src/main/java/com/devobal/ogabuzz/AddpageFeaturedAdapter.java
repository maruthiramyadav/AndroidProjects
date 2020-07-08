package com.devobal.ogabuzz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devobal.ogabuzz.Modals.NewsModel;

import java.util.ArrayList;

public class AddpageFeaturedAdapter extends BaseAdapter{
    AddPageTopicFragment context;
    ArrayList<NewsModel> arrayList;
    public AddpageFeaturedAdapter(AddPageTopicFragment addPageTopicFragment, ArrayList<NewsModel> myListModels) {
        this.context=addPageTopicFragment;
        this.arrayList=myListModels;
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
        LayoutInflater inflater=(LayoutInflater)context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView=inflater.inflate(R.layout.addpagefeatured,null);
        TextView name;
        final ImageView addfill,addunfill;
        name=(TextView)rootView.findViewById(R.id.name);
        addfill=(ImageView)rootView.findViewById(R.id.addfill);
        addunfill=(ImageView)rootView.findViewById(R.id.addunfill);

        name.setText(arrayList.get(position).getCat_name());

        addunfill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.get(position).setCategorycheck(true);
                notifyDataSetChanged();
            }
        });

       addfill.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               arrayList.get(position).setCategorycheck(false);
               notifyDataSetChanged();
           }
       });

       if (arrayList.get(position).getCategorycheck().equals(true)){
           addfill.setVisibility(View.VISIBLE);
           addunfill.setVisibility(View.GONE);
       }else {
           addfill.setVisibility(View.GONE);
           addunfill.setVisibility(View.VISIBLE);
       }

        return rootView;
    }
}
