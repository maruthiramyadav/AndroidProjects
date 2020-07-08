package com.devobal.ogabuzz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devobal.ogabuzz.Modals.LocationsGroupModal;

import java.util.ArrayList;

public class LocationsAdapter extends BaseExpandableListAdapter {
    Locations context;
    ArrayList<LocationsGroupModal> grlist;
    Locations locations;

    public LocationsAdapter(Locations activity, ArrayList<LocationsGroupModal> grouplist) {
        this.context=activity;
        this.grlist=grouplist;
        locations=new Locations();
    }

    @Override
    public int getGroupCount() {
        return grlist.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return grlist.get(i).getLocationsChildModals().size();
    }

    @Override
    public Object getGroup(int i) {
        return grlist.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return grlist.get(i).getLocationsChildModals().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int i, final boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootview2=inflater.inflate(R.layout.locationchildview,null);
        TextView number=(TextView)rootview2.findViewById(R.id.state);
        final ImageView downimage=(ImageView)rootview2.findViewById(R.id.downarrow);
        LinearLayout layout=(LinearLayout)rootview2.findViewById(R.id.layout);

        number.setText(grlist.get(i).getState());

        if (b==true){
            downimage.setImageResource(R.drawable.downarrow);
        }else {
            downimage.setImageResource(R.drawable.uparrow);
        }


        return rootview2;
    }

    @Override
    public View getChildView(final int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootview2=inflater.inflate(R.layout.locationchild,null);
        TextView number=(TextView)rootview2.findViewById(R.id.state);
        final LinearLayout follow=(LinearLayout)rootview2.findViewById(R.id.follow);
        final LinearLayout following=(LinearLayout)rootview2.findViewById(R.id.following);
        number.setText(grlist.get(i).getLocationsChildModals().get(i).getCity());
        follow.setVisibility(View.VISIBLE);
        following.setVisibility(View.INVISIBLE);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow.setVisibility(View.INVISIBLE);
                following.setVisibility(View.VISIBLE);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow.setVisibility(View.VISIBLE);
                following.setVisibility(View.INVISIBLE);
            }
        });
        return rootview2;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
