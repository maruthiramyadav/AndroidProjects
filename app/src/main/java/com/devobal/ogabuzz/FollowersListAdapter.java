package com.devobal.ogabuzz;

import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devobal.ogabuzz.Modals.FollowModel;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Devobal on 2/11/2019 on 5:11 PM.
 */
public class FollowersListAdapter extends RecyclerView.Adapter<FollowersListAdapter.MyViewHolder>{

    ArrayList<FollowModel> arrayList;
    AllFollowersList activity;
    AlertDialog removedialog;

    public FollowersListAdapter(AllFollowersList Activity, ArrayList<FollowModel> hFollowingLists) {
        activity=Activity;
        this.arrayList=hFollowingLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.followerschild,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.followname.setText(arrayList.get(position).getName());
        holder.followphoto.setImageResource(arrayList.get(position).getImage());

        String image = arrayList.get(position).getFollowimg();
        if(image!=null && !image.equals("")&& !image.equals("null")) {
            Glide.with(activity).load(image)
                    .asBitmap().placeholder(R.drawable.loading)
                    .into(holder.followphoto);
        }else{
            holder.followphoto.setImageResource(R.drawable.user_placeholder);
        }

        holder.followremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView removeyes,removeno;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View dialoglayout = layoutInflater.inflate(R.layout.removepopup, null);
                builder.setView(dialoglayout);

                removeyes = (TextView) dialoglayout.findViewById(R.id.removeyes);
                removeno = (TextView) dialoglayout.findViewById(R.id.removeno);
                removeyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*removedialog.dismiss();
                        Intent i = new Intent(AdminHomeActivity.this,AdminLoginActivity.class);
                        startActivity(i);
                        finish();*/
                        //       PreferenceUtils.saveBooleanToSp(AdminHomeActivity.this,"loginstatus",false);
                        // Intent i = new Intent(OGAMainActivity.this,LoginActivity.class);
                        removedialog.dismiss();
                        // startActivity(i);
                        // finishAffinity();
                    }
                });
                removeno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removedialog.dismiss();
                    }
                });
                removedialog = builder.create();
                removedialog.setCanceledOnTouchOutside(false);
                removedialog.setCancelable(false);
                removedialog.show();
            }
        });

        holder.followernext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(activity,  BlogActivity.class);
                activity.startActivity(i);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView followphoto;
        TextView followname,followremove;
        ImageView followernext;

        public MyViewHolder(View itemView) {
            super(itemView);
            followphoto=(CircleImageView)itemView.findViewById(R.id.followphoto);
            followname=(TextView) itemView.findViewById(R.id.followname);
            followremove = (TextView) itemView.findViewById(R.id.followremove);
            followernext = (ImageView) itemView.findViewById(R.id.followernext);
        }
    }
}


