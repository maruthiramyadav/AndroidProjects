package com.devobal.ogabuzz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devobal.ogabuzz.SpecialClasses.GPSTracker;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLngBounds;


import org.json.JSONObject;

import java.io.IOException;


import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Devobal on 2/1/2019 on 6:55 PM.
 */
public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    ImageView registerback,profileedit;
    CircleImageView registerprofileimg;
  //  AutoCompleteTextView place_autocompletetv;
    EditText edusername,edfname,edlname,edemail,edpwd,edconfpwd,edmobileno;
    TextView dobtext,register;
    protected GoogleApiClient mGoogleApiClient1;
    private LatLngBounds bounds1;
  //  private PlaceAutoCompleteAdapter mAdapter1;
    double latitude1, longitude1;
    GPSTracker gpsTracker;
    String responseBody,usrname,fname,lname,emailtxt,password,confpwd,mobileno;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        edusername = (EditText) findViewById(R.id.edusername);
        edfname = (EditText) findViewById(R.id.edfname);
        edlname = (EditText) findViewById(R.id.edlname);
        edemail = (EditText) findViewById(R.id.edemail);
       //  dobtext = (TextView) findViewById(R.id.dobtext);
       // place_autocompletetv = (AutoCompleteTextView) findViewById(R.id.place_autocompletetv);
        edpwd = (EditText) findViewById(R.id.edpwd);
        edconfpwd = (EditText) findViewById(R.id.edconfpwd);
        edmobileno = (EditText) findViewById(R.id.edmobileno);
        register = (TextView) findViewById(R.id.register);
        registerback = (ImageView) findViewById(R.id.registerback);
        registerprofileimg = (CircleImageView) findViewById(R.id.registerprofileimg);
        profileedit = (ImageView) findViewById(R.id.profileedit);

        progressDialog = new ProgressDialog(RegisterActivity.this);
        registerback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Drawable searchleftdrawable = AppCompatResources.getDrawable(RegisterActivity.this, R.drawable.location);
       //  place_autocompletetv.setCompoundDrawablesWithIntrinsicBounds(null,null,searchleftdrawable,null);
        gpsTracker = new GPSTracker(RegisterActivity.this);
       /* dobtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });*/
        profileedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             usrname = edusername.getText().toString().trim();
             fname = edfname.getText().toString().trim();
             lname = edlname.getText().toString().trim();
            // dateofbirth = dobtext.getText().toString().trim();
             emailtxt = edemail.getText().toString().trim();
            // birthplace = place_autocompletetv.getText().toString().trim();
             password = edpwd.getText().toString().trim();
             confpwd = edconfpwd.getText().toString().trim();
             mobileno = edmobileno.getText().toString().trim();

             if(usrname.length()==0){
             //   Toast.makeText(getApplicationContext(), "Please Enter User Name", Toast.LENGTH_SHORT).show();
                 edusername.setError("Please Enter User Name");
                 edusername.requestFocus();
             }else if(fname.length()==0){
               //  Toast.makeText(getApplicationContext(), "Please Enter First Name", Toast.LENGTH_SHORT).show();
               //  edfname.setBackgroundResource(R.drawable.edittextbgred);
                 edfname.requestFocus();
                 edfname.setError("Please Enter First Name");
             }else if(lname.length()==0){
                // Toast.makeText(getApplicationContext(), "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                // edlname.setBackgroundResource(R.drawable.edittextbgred);
                 edlname.requestFocus();
                 edlname.setError("Please Enter Last Name");
             }else if(emailtxt.length()==0){
                // Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
                // edemail.setBackgroundResource(R.drawable.edittextbgred);
                 edemail.setError("Please Enter Email");
                 edemail.requestFocus();
             }else if(password.length()==0){
                 //Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                // edpwd.setBackgroundResource(R.drawable.edittextbgred);
                 edpwd.setError("Please Enter Password");
                 edpwd.requestFocus();
             }else if(confpwd.length()==0){
                 //Toast.makeText(getApplicationContext(), "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                 edconfpwd.setError("Please Enter Confirm Password");
                 edconfpwd.requestFocus();
                // edconfpwd.setBackgroundResource(R.drawable.edittextbgred);
             }else if(!password.equals(confpwd)){
                 Toast.makeText(getApplicationContext(), "Please Enter Password and Confirm Password Must be same", Toast.LENGTH_SHORT).show();
                // edconfpwd.setBackgroundResource(R.drawable.edittextbgred);
                 edconfpwd.requestFocus();
             }else if(mobileno.length()==0){
               //  Toast.makeText(getApplicationContext(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                // edmobileno.setBackgroundResource(R.drawable.edittextbgred);
                 edmobileno.setError("Please Enter Mobile Number");
                 edmobileno.refreshDrawableState();
             }else{
                  postRegistrationData();
                  edusername.setBackgroundResource(R.drawable.rectanglebgedittext);
                  edfname.setBackgroundResource(R.drawable.rectanglebgedittext);
                  edlname.setBackgroundResource(R.drawable.rectanglebgedittext);
                  edemail.setBackgroundResource(R.drawable.rectanglebgedittext);
                  edpwd.setBackgroundResource(R.drawable.rectanglebgedittext);
                  edconfpwd.setBackgroundResource(R.drawable.rectanglebgedittext);
                  edmobileno.setBackgroundResource(R.drawable.rectanglebgedittext);
             }
            }
        });

      /*  mGoogleApiClient1 = new GoogleApiClient.Builder(RegisterActivity.this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient1.connect();

        bounds1 = Utility.toBounds(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude()), 300);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .build();
        mAdapter1 = new PlaceAutoCompleteAdapter(RegisterActivity.this, R.layout.textview,
                mGoogleApiClient1, bounds1, typeFilter);

        place_autocompletetv.setAdapter(mAdapter1);
//        place_autocompletetv.setThreshold(1);
        place_autocompletetv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaceAutoCompleteAdapter.PlaceAutocomplete placeAutocomplete =
                        (PlaceAutoCompleteAdapter.PlaceAutocomplete) mAdapter1.getItem(position);
                Places.GeoDataApi.getPlaceById(mGoogleApiClient1,placeAutocomplete.placeId.toString())
                        .setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(@NonNull PlaceBuffer places) {
                                if (places.getStatus().isSuccess()) {
                                    final Place myPlace = places.get(0);
                                    LatLng latLng = myPlace.getLatLng();
                                    String toastMsg = String.format("%s", myPlace.getAddress());
                                    InputMethodManager inputManager = (InputMethodManager)
                                            getSystemService(INPUT_METHOD_SERVICE);
                                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                            InputMethodManager.HIDE_NOT_ALWAYS);
                                    place_autocompletetv.setText(toastMsg);
                                }
                                places.release();
                                place_autocompletetv.clearFocus();
                            }
                        });
            }
        });*/
    }
    private void postRegistrationData() {

        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("action", "register")
                            .header("first_name", fname)
                            .header("last_name", lname)
                            .header("username", usrname)
                            .header("telephone", mobileno)
                            .header("mail_id", emailtxt)
                            .header("password", password)
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
            APICalls.Register apiService= retrofit.create(APICalls.Register.class);
            Call<ResponseBody> call = apiService.insertUser();
            showProgressdialog();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = response.body().string();
                        postdata(s);

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

    private void postdata(String s) {
        dismissProgressDislogue();
        try {
            JSONObject jObject = new JSONObject(s);
             if(jObject.has("response")) {
                 JSONObject obj = jObject.getJSONObject("response");
                 if (obj.getString("status").equals("error")) {
                     String message = obj.getString("message");
                     Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                 } else {
                     String message = obj.getString("message");
                     Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                     finish();
                 }
             }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(RegisterActivity.this);
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

   /* private void openDatePicker(){
        Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                Calendar userAge = new GregorianCalendar(year,month,dayOfMonth);
                if (minAdultAge.before(userAge)) {
                    Toast.makeText(RegisterActivity.this,
                            "Age should be above 18 years",Toast.LENGTH_SHORT).show();
                }else {
                    dateofbirth = (month+1)+"/"+dayOfMonth+"/"+year;
                    dobtext.setText(dateofbirth);
                }
            }
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }*/
    @Override
    public void onConnected(@Nullable Bundle bundle) { }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
