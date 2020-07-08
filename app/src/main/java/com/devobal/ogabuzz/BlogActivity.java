package com.devobal.ogabuzz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.devobal.ogabuzz.Modals.BlogVRCModal;
import com.devobal.ogabuzz.Modals.SourcesListModal;

import java.util.ArrayList;

public class BlogActivity extends AppCompatActivity {

    RecyclerView hrc,vrc;
    BlogHRcAdapter hRcAdapter;
    BlogVRcAdapter vRcAdapter;
    ArrayList<SourcesListModal> blogHRCModals;
    ArrayList<BlogVRCModal> blogVRCModals;
    ImageView backarrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        hrc=(RecyclerView)findViewById(R.id.hrc);
        vrc=(RecyclerView)findViewById(R.id.vrc);
        backarrow=(ImageView)findViewById(R.id.backarrow);
        blogHRCModals=new ArrayList<>();
        blogVRCModals=new ArrayList<>();
        hRcAdapter=new BlogHRcAdapter(this,blogHRCModals);
        vRcAdapter=new BlogVRcAdapter(this,blogVRCModals);
        vrc.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(BlogActivity.this,LinearLayoutManager.HORIZONTAL,false);
        hrc.setLayoutManager(layoutManager);
        hrc.setItemAnimator(new DefaultItemAnimator());
        hrc.setAdapter(hRcAdapter);
        HPreparedData();

        RecyclerView.LayoutManager layoutManager1=new LinearLayoutManager(BlogActivity.this,LinearLayoutManager.VERTICAL,false);
        vrc.setLayoutManager(layoutManager1);
        vrc.setItemAnimator(new DefaultItemAnimator());
        vrc.setAdapter(vRcAdapter);
        VPreparedData();
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public void HPreparedData(){
        for (int i=0;i<=10;i++){
            SourcesListModal SourcesListModal=new SourcesListModal();
            if(i==0){
                SourcesListModal.setPhoto(R.drawable.tech);
                SourcesListModal.setPapername("jdfsksdfl");
            }else if(i==1){
                SourcesListModal.setPhoto(R.drawable.trending2);
                SourcesListModal.setPapername("jdfsksdfl");
            }else if(i==2){
                SourcesListModal.setPhoto(R.drawable.slide);
                SourcesListModal.setPapername("jdfsksdfl");
            }else if(i==3){
                SourcesListModal.setPhoto(R.drawable.tech);
                SourcesListModal.setPapername("jdfsksdfl");
            }else if(i==4){
                SourcesListModal.setPhoto(R.drawable.trending2);
                SourcesListModal.setPapername("jdfsksdfl");
            }else if(i==5){
                SourcesListModal.setPhoto(R.drawable.slide);
                SourcesListModal.setPapername("jdfsksdfl");
            }else if(i==6){
                SourcesListModal.setPhoto(R.drawable.tech);
                SourcesListModal.setPapername("jdfsksdfl");
            }else if(i==7){
                SourcesListModal.setPhoto(R.drawable.trending2);
                SourcesListModal.setPapername("jdfsksdfl");
            }else if(i==8){
                SourcesListModal.setPhoto(R.drawable.slide);
                SourcesListModal.setPapername("jdfsksdfl");
            }else if(i==9){
                SourcesListModal.setPhoto(R.drawable.tech);
                SourcesListModal.setPapername("jdfsksdfl");
            }else if(i==10){
                SourcesListModal.setPhoto(R.drawable.trending2);
                SourcesListModal.setPapername("jdfsksdfl");
            }
            blogHRCModals.add(SourcesListModal);
        }
        hRcAdapter.notifyDataSetChanged();
    }

    public void VPreparedData(){
        for (int i=0;i<=10;i++){
            BlogVRCModal SourcesListModal=new BlogVRCModal();
            if(i==0){
                SourcesListModal.setPhoto(R.drawable.tech);
                SourcesListModal.setTitle("Match in between India and Pakistan");
                SourcesListModal.setDescription("India won by 3 wickets.MS Dhoni finished in his style and got man of the match.Mohammad Amer got 4 Wickets ");
                SourcesListModal.setCategory("Sports");
            }else if(i==1){
                SourcesListModal.setPhoto(R.drawable.trending2);
                SourcesListModal.setTitle("Match in between India and Pakistan");
                SourcesListModal.setDescription("India won by 3 wickets.MS Dhoni finished in his style and got man of the match.Mohammad Amer got 4 Wickets ");
                SourcesListModal.setCategory("Sports");
            }else if(i==2){
                SourcesListModal.setPhoto(R.drawable.slide);
                SourcesListModal.setTitle("Match in between India and Pakistan");
                SourcesListModal.setDescription("India won by 3 wickets.MS Dhoni finished in his style and got man of the match.Mohammad Amer got 4 Wickets ");
                SourcesListModal.setCategory("Sports");
            }else if(i==3){
                SourcesListModal.setPhoto(R.drawable.tech);
                SourcesListModal.setTitle("Match in between India and Pakistan");
                SourcesListModal.setDescription("India won by 3 wickets.MS Dhoni finished in his style and got man of the match.Mohammad Amer got 4 Wickets ");
                SourcesListModal.setCategory("Sports");
            }else if(i==4){
                SourcesListModal.setPhoto(R.drawable.trending2);
                SourcesListModal.setTitle("Match in between India and Pakistan");
                SourcesListModal.setDescription("India won by 3 wickets.MS Dhoni finished in his style and got man of the match.Mohammad Amer got 4 Wickets ");
                SourcesListModal.setCategory("Sports");
            }else if(i==5){
                SourcesListModal.setPhoto(R.drawable.slide);
                SourcesListModal.setTitle("Match in between India and Pakistan");
                SourcesListModal.setDescription("India won by 3 wickets.MS Dhoni finished in his style and got man of the match.Mohammad Amer got 4 Wickets ");
                SourcesListModal.setCategory("Sports");
            }else if(i==6){
                SourcesListModal.setPhoto(R.drawable.tech);
                SourcesListModal.setTitle("Match in between India and Pakistan");
                SourcesListModal.setDescription("India won by 3 wickets.MS Dhoni finished in his style and got man of the match.Mohammad Amer got 4 Wickets ");
                SourcesListModal.setCategory("Sports");
            }else if(i==7){
                SourcesListModal.setPhoto(R.drawable.trending2);
                SourcesListModal.setTitle("Match in between India and Pakistan");
                SourcesListModal.setDescription("India won by 3 wickets.MS Dhoni finished in his style and got man of the match.Mohammad Amer got 4 Wickets ");
                SourcesListModal.setCategory("Sports");
            }else if(i==8){
                SourcesListModal.setPhoto(R.drawable.slide);
                SourcesListModal.setTitle("Match in between India and Pakistan");
                SourcesListModal.setDescription("India won by 3 wickets.MS Dhoni finished in his style and got man of the match.Mohammad Amer got 4 Wickets ");
                SourcesListModal.setCategory("Sports");
            }else if(i==9){
                SourcesListModal.setPhoto(R.drawable.tech);
                SourcesListModal.setTitle("Match in between India and Pakistan");
                SourcesListModal.setDescription("India won by 3 wickets.MS Dhoni finished in his style and got man of the match.Mohammad Amer got 4 Wickets ");
                SourcesListModal.setCategory("Sports");
            }else if(i==10){
                SourcesListModal.setPhoto(R.drawable.trending2);
                SourcesListModal.setTitle("Match in between India and Pakistan");
                SourcesListModal.setDescription("India won by 3 wickets.MS Dhoni finished in his style and got man of the match.Mohammad Amer got 4 Wickets ");
                SourcesListModal.setCategory("Sports");
            }
                blogVRCModals.add(SourcesListModal);
        }
        vRcAdapter.notifyDataSetChanged();
    }
}