package com.devobal.ogabuzz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.devobal.ogabuzz.Modals.LocationsChildModal;
import com.devobal.ogabuzz.Modals.LocationsGroupModal;

import java.util.ArrayList;

public class Locations extends Fragment {
    ImageView sourcesindicator,topicsindicator,locationsindicator;
    ExpandableListView rcView;
    LocationsAdapter locationsAdapter;
    ArrayList<LocationsGroupModal> grouplist;
    ArrayList<LocationsChildModal> childlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_locations, container, false);
        sourcesindicator=(ImageView)rootView.findViewById(R.id.sourcesindicator);
        topicsindicator=(ImageView)rootView.findViewById(R.id.topicsindicator);
        locationsindicator=(ImageView)rootView.findViewById(R.id.locationsindicator);

        sourcesindicator.setVisibility(View.INVISIBLE);
        topicsindicator.setVisibility(View.INVISIBLE);
        locationsindicator.setVisibility(View.VISIBLE);

        grouplist=new ArrayList<LocationsGroupModal>();
        childlist=new ArrayList<LocationsChildModal>();
        rcView=(ExpandableListView) rootView.findViewById(R.id.elView);
        locationsAdapter=new LocationsAdapter(this,grouplist);
        rcView.setAdapter(locationsAdapter);
        preparedData();
        rcView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

            }
        });
        return rootView;
    }

    public static Locations newInstance(String text) {

        Locations f = new Locations();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    public void preparedData(){

        for (int i=0;i<=3;i++){
            LocationsGroupModal locationsModal=new LocationsGroupModal();
            if (i==0) {
                locationsModal.setState("Andhara pradesh");
            }else if (i==1){
                locationsModal.setState("Tamil Nadu");
            }else if (i==2) {
                locationsModal.setState("Kerala");
            }else if (i==3){
                locationsModal.setState("Karnataka");
            }
            childlist.clear();
            for (int j=0;j<=3;j++){
                LocationsChildModal locationsChildModal=new LocationsChildModal();
                if (j==0){
                    locationsChildModal.setCity("vijayawada");
                }else if (j==1){
                    locationsChildModal.setCity("Visakapatnam");
                }else if (j==2){
                    locationsChildModal.setCity("Ongole");
                }else if (j==3){
                    locationsChildModal.setCity("vijayawada");
                }
                childlist.add(locationsChildModal);
            }
            locationsModal.setLocationsChildModals(childlist);
            grouplist.add(locationsModal);
        }
        locationsAdapter.notifyDataSetChanged();
    }

    public void ExpandOrCollapse(int i){
        if (rcView.isGroupExpanded(i)){
            rcView.collapseGroup(i);
        }else {
            rcView.expandGroup(i);
        }
    }
}
