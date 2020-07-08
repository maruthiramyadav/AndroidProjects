package com.devobal.ogabuzz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Devobal on 2/2/2019 on 2:31 PM.
 */
public class HelpCenterActivity extends AppCompatActivity {

    ImageView helpcenterback;
    LinearLayout sendfeedbacklayout,checkupdatelayout,ratinglayout,sharehelp;
    RelativeLayout aboutuslayout;
    String currentVersion, latestVersion;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_center_activity);
        helpcenterback = (ImageView) findViewById(R.id.helpcenterback);
        sendfeedbacklayout = (LinearLayout) findViewById(R.id.sendfeedbacklayout);
        checkupdatelayout = (LinearLayout) findViewById(R.id.checkupdatelayout);
        aboutuslayout = (RelativeLayout) findViewById(R.id.aboutuslayout);
        ratinglayout = (LinearLayout) findViewById(R.id.ratinglayout);
        sharehelp = (LinearLayout) findViewById(R.id.sharehelp);

        helpcenterback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendfeedbacklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HelpCenterActivity.this, SendFeedbackActivity.class);
                startActivity(i);
            }
        });
        aboutuslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HelpCenterActivity.this, AboutUs.class);
                startActivity(i);
            }
        });
        checkupdatelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new FetchAppVersionFromGooglePlayStore().execute();
                try {
                    String version=HelpCenterActivity.this.getPackageManager().getPackageInfo(
                            HelpCenterActivity.this.getPackageName(),0).versionName;
                    getCurrentVersion();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
               /*// new FetchAppVersionFromGooglePlayStore().execute();
                getCurrentVersion();*/
 //play store :
               /*final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }*/
            }
        });
        ratinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + "com.devobal.ogabuzz" + "&hl=en"));
                startActivity(i);
            }
        });

        sharehelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "OGABuzz");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
    }

    private void getCurrentVersion(){
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;

        new GetLatestVersion().execute();

    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.devobal.ogabuzz").get();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();

            }catch (Exception e){
                e.printStackTrace();

            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(latestVersion!=null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)){
                    /*if(!isFinishing()){ //This would help to prevent Error : BinderProxy@45d459c0 is not valid; is your activity running? error
                        showUpdateDialog();
                    }else{
                        Toast.makeText(getApplicationContext(), "No Update Available", Toast.LENGTH_SHORT).show();
                    }*/
                    showUpdateDialog();
                }else {
                    Toast.makeText(getApplicationContext(), "No Update Available", Toast.LENGTH_SHORT).show();
                }
            }
            super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.devobal.ogabuzz")));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // background.start();
            }
        });

        builder.setCancelable(false);
        dialog = builder.show();
    }
}
