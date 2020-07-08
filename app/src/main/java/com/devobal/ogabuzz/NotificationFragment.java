package com.devobal.ogabuzz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devobal.ogabuzz.Modals.BlogVRCModal;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class NotificationFragment extends Fragment {

    ImageView menu,menuwhite;
    ListView lView;
    NotificationAdapter notificationAdapter;
    ArrayList<BlogVRCModal> blogVRCModals;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    String sessionid;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_notification, container, false);
        menu=(ImageView)rootView.findViewById(R.id.nav_menu1);
        menuwhite=((Activity)getActivity()).findViewById(R.id.menuwhite);
        lView=(ListView)rootView.findViewById(R.id.lView);

        progressDialog = new ProgressDialog(getActivity());
        preferenceUtils = new PreferenceUtils(getActivity());
        sessionid = preferenceUtils.getSessionId();

        blogVRCModals=new ArrayList<BlogVRCModal>();
        notificationAdapter=new NotificationAdapter(NotificationFragment.this,blogVRCModals);
        lView.setAdapter(notificationAdapter);
       // preparedData();
       NotificationGet();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuwhite.performClick();
            }
        });

        menuwhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer;
                drawer=((Activity)getActivity()).findViewById(R.id.drawer_layout);

                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                not_popup(position);
                NotificationReadPost(blogVRCModals.get(position).getId());
            }
        });
        return rootView;
    }

    public void not_popup(int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View dialoglayout = layoutInflater.inflate(R.layout.notifipopup, null);
        builder.setView(dialoglayout);

        TextView notitype = (TextView) dialoglayout.findViewById(R.id.notification);
        TextView message = (TextView) dialoglayout.findViewById(R.id.date);
        TextView cancel=(TextView)dialoglayout.findViewById(R.id.cancel);

        notitype .setText(blogVRCModals.get(id).getNotitype());
        message.setText(blogVRCModals.get(id).getMessage());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void NotificationGet() {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("sessionid", sessionid)
                            .header("limit", "all")
                            .header("offset", "")
                            .header("notification_type", "")

                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });
            OkHttpClient client = httpClient.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            APICalls.NotificationGet apiService= retrofit.create(APICalls.NotificationGet.class);
            Call<ResponseBody> call = apiService.NotificationGet();
            showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        dismissProgressDislogue();
                        String s = response.body().string();
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.has("response")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                                if (jsonObject1.getString("status").equals("success")) {
                                    JSONArray jsonArray = new JSONArray(jsonObject1.getString("data"));
                                    blogVRCModals.clear();
                                    for(int i=0; i<jsonArray.length(); i++) {
                                        JSONObject dataobj = jsonArray.getJSONObject(i);
                                        BlogVRCModal blogVRCModal=new BlogVRCModal();
                                        blogVRCModal.setMessage(dataobj.getString("message"));
                                        blogVRCModal.setNotitype(dataobj.getString("notification_type"));
                                        blogVRCModal.setId(dataobj.getString("id"));
                                        blogVRCModal.setRead_status(dataobj.getString("read_status"));
                                        blogVRCModals.add(blogVRCModal);
                                    }
                                   notificationAdapter.notifyDataSetChanged();
                                }
                            }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismissProgressDislogue();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void NotificationReadPost(final String notificationid) {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("sessionid", sessionid)
                            .header("notification_id","["+notificationid+"]")

                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });
            OkHttpClient client = httpClient.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            APICalls.NotificationReadSet apiService= retrofit.create(APICalls.NotificationReadSet.class);
            Call<ResponseBody> call = apiService.notificationReadSet();
            //showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        //dismissProgressDislogue();
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.has("response")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                            if (jsonObject1.getString("status").equals("success")) {
                                String message = jsonObject1.getString("message");
                                //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                NotificationGet1();
                            }
                        } else {
                            String message = jsonObject.getString("message");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //dismissProgressDislogue();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void NotificationGet1() {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("sessionid", sessionid)
                            .header("limit", "all")
                            .header("offset", "")
                            .header("notification_type", "")

                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });
            OkHttpClient client = httpClient.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            APICalls.NotificationGet apiService= retrofit.create(APICalls.NotificationGet.class);
            Call<ResponseBody> call = apiService.NotificationGet();
            //showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        //dismissProgressDislogue();
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.has("response")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                            if (jsonObject1.getString("status").equals("success")) {
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("data"));
                                blogVRCModals.clear();
                                for(int i=0; i<jsonArray.length(); i++) {
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    BlogVRCModal blogVRCModal=new BlogVRCModal();
                                    blogVRCModal.setMessage(dataobj.getString("message"));
                                    blogVRCModal.setNotitype(dataobj.getString("notification_type"));
                                    blogVRCModal.setId(dataobj.getString("id"));
                                    blogVRCModal.setRead_status(dataobj.getString("read_status"));
                                    blogVRCModals.add(blogVRCModal);
                                }
                                notificationAdapter.notifyDataSetChanged();
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //dismissProgressDislogue();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading data....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void dismissProgressDislogue(){
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public void preparedData(){
        for (int i=0;i<=7;i++){
            BlogVRCModal blogVRCModal=new BlogVRCModal();
                blogVRCModal.setPhoto(R.drawable.tech);
                blogVRCModal.setDescription("mangdihg anasddkd i kasdf as i ksdfksadfj ifsdkjsdfk ijasdf sdfa i ajsfksadf as");
                blogVRCModal.setTitle("Moose Craft");

            blogVRCModals.add(blogVRCModal);
        }
        notificationAdapter.notifyDataSetChanged();
    }
}
