package com.devobal.ogabuzz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devobal.ogabuzz.Modals.SourcesListModal;

import java.util.ArrayList;


public class Topics extends Fragment {

    ImageView sourcesindicator,topicsindicator,locationsindicator;
    RecyclerView rctrending,rcFeatured;
    TopicsTrendingAdapter topicsTreding;
    TopicsFeaturedAdapter topicsfeatured;
    ArrayList<SourcesListModal> sourcesListModals,topicslist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_topics, container, false);

        sourcesindicator=(ImageView)rootView.findViewById(R.id.sourcesindicator);
        topicsindicator=(ImageView)rootView.findViewById(R.id.topicsindicator);
        locationsindicator=(ImageView)rootView.findViewById(R.id.locationsindicator);
        rctrending=(RecyclerView)rootView.findViewById(R.id.rctrending);
        rcFeatured=(RecyclerView)rootView.findViewById(R.id.rcFeatured);

        sourcesListModals =new ArrayList<SourcesListModal>();
        topicslist=new ArrayList<SourcesListModal>();
        topicsTreding=new TopicsTrendingAdapter(this, topicslist);
        topicsfeatured=new TopicsFeaturedAdapter(this, sourcesListModals);

        RecyclerView.LayoutManager reclayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rctrending.setLayoutManager(reclayoutManager);
        rctrending.setItemAnimator(new DefaultItemAnimator());
        rctrending.setAdapter(topicsTreding);
        preparedDataFeatured();

        RecyclerView.LayoutManager populerlayourManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rcFeatured.setLayoutManager(populerlayourManager);
        rcFeatured.setItemAnimator(new DefaultItemAnimator());
        rcFeatured.setAdapter(topicsfeatured);
        preparedData();

        sourcesindicator.setVisibility(View.INVISIBLE);
        topicsindicator.setVisibility(View.VISIBLE);
        locationsindicator.setVisibility(View.INVISIBLE);

        return rootView;
    }
    public static Topics newInstance(String text) {

        Topics f = new Topics();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    public void preparedData(){
        for (int i=0;i<=5;i++){
            SourcesListModal sourcesListModal=new SourcesListModal();
            if (i==0){
                sourcesListModal.setPapername("Sakshi");
                sourcesListModal.setPhoto(R.drawable.tech);
            }else if(i==1){
                sourcesListModal.setPapername("Eenadu");
                sourcesListModal.setPhoto(R.drawable.trending2);
            }else if(i==2){
                sourcesListModal.setPapername("Hindhu");
                sourcesListModal.setPhoto(R.drawable.tech);
            } else if(i==3){
                sourcesListModal.setPapername("TOI");
                sourcesListModal.setPhoto(R.drawable.trending2);
            }else if(i==4){
                sourcesListModal.setPapername("deccan chronicle");
                sourcesListModal.setPhoto(R.drawable.tech);
            }else if(i==5){
                sourcesListModal.setPapername("deccan chronicle");
                sourcesListModal.setPhoto(R.drawable.trending2);
            }
            sourcesListModals.add(sourcesListModal);
        }
        topicsfeatured.notifyDataSetChanged();
    }

    public void preparedDataFeatured(){
        for (int i=0;i<=5;i++){
            SourcesListModal sourcesListModal=new SourcesListModal();
            if (i==0){
                sourcesListModal.setPapername("Sakshi");
                sourcesListModal.setPhoto(R.drawable.tech);
            }else if(i==1){
                sourcesListModal.setPapername("Eenadu");
                sourcesListModal.setPhoto(R.drawable.trending2);
            }else if(i==2){
                sourcesListModal.setPapername("Hindhu");
                sourcesListModal.setPhoto(R.drawable.tech);
            } else if(i==3){
                sourcesListModal.setPapername("TOI");
                sourcesListModal.setPhoto(R.drawable.trending2);
            }else if(i==4){
                sourcesListModal.setPapername("deccan chronicle");
                sourcesListModal.setPhoto(R.drawable.trending2);
            }else if(i==5){
                sourcesListModal.setPapername("deccan chronicle");
                sourcesListModal.setPhoto(R.drawable.trending2);
            }
            topicslist.add(sourcesListModal);
        }
        topicsTreding.notifyDataSetChanged();
    }

}
