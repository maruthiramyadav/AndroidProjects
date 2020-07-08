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

public class Sources extends Fragment {
    ImageView sourcesindicator,topicsindicator,locationsindicator;
    RecyclerView rcRecommended,rcMostPopular,rcEntertain;
    SourceRecommendedAdapter sourceRecommendedAdapter;
    SourceMostPopulerAdapter sourceMostPopulerAdapter;
    SourceEntertainAdapter sourceEntertainAdapter;
    ArrayList<SourcesListModal> sourcesListModals;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_sources, container, false);

        sourcesindicator=(ImageView)rootView.findViewById(R.id.sourcesindicator);
        topicsindicator=(ImageView)rootView.findViewById(R.id.topicsindicator);
        locationsindicator=(ImageView)rootView.findViewById(R.id.locationsindicator);
        rcMostPopular=(RecyclerView)rootView.findViewById(R.id.rcMostPopular);
        rcRecommended=(RecyclerView)rootView.findViewById(R.id.rcRecommended);
        rcEntertain=(RecyclerView)rootView.findViewById(R.id.rcEntertain);

        sourcesListModals =new ArrayList<SourcesListModal>();

        sourceMostPopulerAdapter=new SourceMostPopulerAdapter(this, sourcesListModals);
        sourceRecommendedAdapter=new SourceRecommendedAdapter(this, sourcesListModals);
        sourceEntertainAdapter= new SourceEntertainAdapter(this, sourcesListModals);

        RecyclerView.LayoutManager reclayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rcRecommended.setLayoutManager(reclayoutManager);
        rcRecommended.setItemAnimator(new DefaultItemAnimator());
        rcRecommended.setAdapter(sourceRecommendedAdapter);

        RecyclerView.LayoutManager populerlayourManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rcMostPopular.setLayoutManager(populerlayourManager);
        rcMostPopular.setItemAnimator(new DefaultItemAnimator());
        rcMostPopular.setAdapter(sourceMostPopulerAdapter);

        RecyclerView.LayoutManager entertainlayourManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rcEntertain.setLayoutManager(entertainlayourManager);
        rcEntertain.setItemAnimator(new DefaultItemAnimator());
        rcEntertain.setAdapter(sourceEntertainAdapter);

        preparedData();

        sourcesindicator.setVisibility(View.VISIBLE);
        topicsindicator.setVisibility(View.INVISIBLE);
        locationsindicator.setVisibility(View.INVISIBLE);

        return rootView;
    }
    public static Sources newInstance(String text) {
        Sources f = new Sources();
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
                sourcesListModal.setPhoto(R.drawable.ent);
            } else if(i==3){
                sourcesListModal.setPapername("TOI");
                sourcesListModal.setPhoto(R.drawable.slide);
            }else if(i==4){
                sourcesListModal.setPapername("deccan chronicle");
                sourcesListModal.setPhoto(R.drawable.trending2);
            }else if(i==5){
                sourcesListModal.setPapername("deccan chronicle");
                sourcesListModal.setPhoto(R.drawable.tech);
            }
            sourcesListModals.add(sourcesListModal);
        }
        sourceRecommendedAdapter.notifyDataSetChanged();
        sourceMostPopulerAdapter.notifyDataSetChanged();
        sourceEntertainAdapter.notifyDataSetChanged();
    }
}
