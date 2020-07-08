package com.devobal.ogabuzz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devobal.ogabuzz.Modals.BlogVRCModal;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {
    NotificationFragment context;
    ArrayList<BlogVRCModal> arrayList;
    public NotificationAdapter(NotificationFragment activity, ArrayList<BlogVRCModal> blogVRCModals) {
        this.context=activity;
        this.arrayList=blogVRCModals;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView=inflater.inflate(R.layout.notificationchild,viewGroup,false);

        ImageView photo=(ImageView)rootView.findViewById(R.id.photo);
        TextView message=(TextView)rootView.findViewById(R.id.message);
        TextView notitype=(TextView)rootView.findViewById(R.id.notitype);
        LinearLayout linear1=(LinearLayout)rootView.findViewById(R.id.linear1);

        message.setText(arrayList.get(i).getMessage());
        notitype.setText(arrayList.get(i).getNotitype());
        if (arrayList.get(i).getRead_status().equals("Un-read")){
            linear1.setBackgroundColor(context.getResources().getColor(R.color.shimmercolor));
        }else {
            linear1.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

        return rootView;
    }
}
