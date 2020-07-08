package com.devobal.ogabuzz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devobal.ogabuzz.Modals.LanguageModel;
import com.devobal.ogabuzz.R;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.devobal.ogabuzz.SpecialClasses.CustomTextView;
import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class LanguageCustomGridAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<LanguageModel> selectedlanguageslist;
    LayoutInflater inflater;
    PreferenceUtils preferenceUtils;
    int selectedpos;
    boolean checked;
    ArrayList<String>ids= new ArrayList<String>();
    String sessionid;

    public LanguageCustomGridAdapter(Context ctx, ArrayList<LanguageModel> gridarray,
                                     ArrayList<LanguageModel> languagelArrayList) {
        mContext = ctx;
        selectedlanguageslist = gridarray;
        inflater = LayoutInflater.from(ctx);
        preferenceUtils = new PreferenceUtils(ctx);
        sessionid=preferenceUtils.getSessionId();
    }

    @Override
    public int getCount() {
        return selectedlanguageslist.size();
    }

    @Override
    public Object getItem(int position) {
        return selectedlanguageslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        TextView gridname,grid_text_select;
        TextView save;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        holder = new ViewHolder();
        convertView = inflater.inflate(R.layout.grid_single,parent, false);
        holder.gridname= (TextView) convertView.findViewById(R.id.grid_text);
        holder.grid_text_select= (TextView) convertView.findViewById(R.id.grid_text_select);
        holder.save=((Activity)mContext).findViewById(R.id.langsave);
        convertView.setTag(holder);
        
        holder.grid_text_select.setText(selectedlanguageslist.get(position).getLanguage());
        holder.gridname.setText(selectedlanguageslist.get(position).getLanguage());

        if (selectedlanguageslist.get(position).isIsselect()==true) {
            holder.grid_text_select.setVisibility(View.VISIBLE);
            holder.gridname.setVisibility(View.GONE);
            ids.add(selectedlanguageslist.get(position).getLangid());
        } else {
            holder.grid_text_select.setVisibility(View.GONE);
            holder.gridname.setVisibility(View.VISIBLE);
            ids.remove(selectedlanguageslist.get(position).getLangid());
        }

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sessionid!=null && !sessionid.equals("0")&& !sessionid.equals("")) {
                    LanguagePost();
                }else {
                    Toast.makeText(mContext,"Please Make Login to Save Languages",Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.grid_text_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedlanguageslist.get(position).setIsselect(false);
                ids.clear();
                notifyDataSetChanged();
            }
        });

        holder.gridname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedlanguageslist.get(position).setIsselect(true);
                ids.clear();
                notifyDataSetChanged();
            }
        });

        return convertView;

    }
    private void LanguagePost() {
        try {
            sessionid = preferenceUtils.getSessionId();
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    HashSet<String> listToSet = new HashSet<String>(ids);
                    String sellanglist = listToSet.toString();
                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("language", "")
                            .header("sessionid", sessionid)
                            .header("lang_id", sellanglist)
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
            APICalls.LanguagePost apiService = retrofit.create(APICalls.LanguagePost.class);
            Call<ResponseBody> call = apiService.LanguagePost();
            // showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        HashSet<String> listToSet = new HashSet<String>(ids);
                        System.out.println(listToSet);
                        String s = response.body().string();
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.has("response")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                            if (jsonObject1.getString("status").equals("success")) {
                                preferenceUtils.setLanguages(listToSet.toString());
                                Intent intent = new Intent();
                                intent.putExtra("language", listToSet);
                                ((Activity)mContext).setResult(RESULT_OK, intent);
                                ((Activity)mContext).finish();
                                Toast.makeText((Activity)mContext, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText((Activity)mContext, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // dismissProgressDislogue();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}