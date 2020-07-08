package com.devobal.ogabuzz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devobal.ogabuzz.Modals.ImageModel;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Devobal on 3/14/2019 on 10:49 PM.
 */
public class MultiImageAdpater extends RecyclerView.Adapter<MultiImageAdpater.MyViewHolder>{
    ArrayList<ImageModel> modelArrayList;
    Context ctx;

    public MultiImageAdpater(AddNewsActivity addNewsActivity,
                             ArrayList<ImageModel> multiimgArraylist) {
        ctx = addNewsActivity;
        modelArrayList = multiimgArraylist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.multimglistitem,parent,false);
        return new MyViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //holder.galleryimage.setImageBitmap(modelArrayList.get(position).getBitmap());
        File file = new File(modelArrayList.get(position).getPath());
        if (file.exists()) {
          Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
          holder.galleryimage.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView galleryimage;
        public MyViewHolder(View itemView) {
            super(itemView);
            galleryimage=(ImageView)itemView.findViewById(R.id.galleryimage);
        }
    }
}
