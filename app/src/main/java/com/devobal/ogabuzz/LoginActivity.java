package com.devobal.ogabuzz;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devobal.ogabuzz.SpecialClasses.PreferenceUtils;
import com.devobal.ogabuzz.Retrofit.APICalls;
import com.devobal.ogabuzz.Retrofit.ApiUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import twitter4j.Twitter;
import twitter4j.auth.RequestToken;

/**
 * Created by Devobal on 2/1/2019 on 5:39 PM.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    ImageView loginback;
    ImageView emailicon,twitterimg;
    ImageView facebook;
    ImageView googleplus;
    EditText edusrname,edpwd;
    TextView login,forgotpwd,register;
    SpannableString registeryet;
    String username,password;
    PreferenceUtils preferenceUtils;
    ProgressDialog progressDialog;
    private TwitterLoginButton twitterLoginButton;
    private TwitterAuthClient client;
    private static RequestToken requestToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;

    private Twitter mTwitter;

    static String TWITTER_CONSUMER_KEY = "6B57fuTDRNDuJIYVs57B69STf";
    static String TWITTER_CONSUMER_SECRET = "hLbI0Wkw30RA0H7hTM3DzO6rMrQu3dpZLQDeuX9vFOVTW41fWA";

    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
//facebook data
    LoginButton loginButton;
    CallbackManager callbackManager;
//gmail integration data
    SignInButton signin;
    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.login);
        loginback = (ImageView) findViewById(R.id.loginback);
        edusrname = (EditText) findViewById(R.id.edusrname);
        edpwd = (EditText) findViewById(R.id.edpwd);
        login = (TextView) findViewById(R.id.login);
        forgotpwd = (TextView) findViewById(R.id.forgotpwd);
        register = (TextView) findViewById(R.id.register);
        emailicon = (ImageView) findViewById(R.id.emailicon);
        twitterimg = (ImageView) findViewById(R.id.twitter);
        facebook = (ImageView) findViewById(R.id.facebook);
        googleplus = (ImageView) findViewById(R.id.googleplus);
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitterLoginButton);
        client = new TwitterAuthClient();
        loginButton=(LoginButton)findViewById(R.id.login_button);

        preferenceUtils = new PreferenceUtils(LoginActivity.this);
        progressDialog = new ProgressDialog(LoginActivity.this);

        gmailIntegrartion();
       //facebook integration
        facebookIntegration();
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

        registeryet = new SpannableString("Not One of us Yet? Register");
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(i,125);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.colorGreen));
                ds.setUnderlineText(false);
            }
        };
        registeryet.setSpan(span1,19,27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        register.setText(registeryet);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        loginback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             username = edusrname.getText().toString().trim();
             password = edpwd.getText().toString().trim();
             if(username.length()==0){
                 Toast.makeText(getApplicationContext(), "Please Enter User Name", Toast.LENGTH_SHORT).show();
             }else if(password.length()==0){
                 Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
             }else{
                 LoginPost();
             }

            }
        });
        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(i);
                finish();
            }
        });
      //  FacebookSdk.sdkInitialize(this.getApplicationContext());
        //defaultLoginTwitter();
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession twitterSession = result.data;

                //call fetch email only when permission is granted
                fetchTwitterEmail(twitterSession);

            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(LoginActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
        twitterimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterLoginButton.performClick();
            }
        });

        twitterLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultLoginTwitter();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        if (com.facebook.AccessToken.getCurrentAccessToken().getToken()!=null){
                            com.facebook.AccessToken accessToken1 = com.facebook.AccessToken.getCurrentAccessToken();
                            if (com.facebook.AccessToken.getCurrentAccessToken() != null
                                    && com.facebook.Profile.getCurrentProfile() != null) {
                                LoginManager.getInstance().logOut();
                            }else {
                                getUserProfile(accessToken1);
                            }
                        }

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });

            }
        });
        emailicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //signin.performClick();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 111);
            }
        });
        /*signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 111);
            }
        });*/
    }


    public void defaultLoginTwitter() {
        //check if user is already authenticated or not
        if (getTwitterSession() == null) {
            //if user is not authenticated start authenticating
            twitterLoginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {

                    // Do something with result, which provides a TwitterSession for making API calls
                    TwitterSession twitterSession = result.data;

                    //call fetch email only when permission is granted
                    fetchTwitterEmail(twitterSession);

                }

                @Override
                public void failure(TwitterException exception) {
                    // Do something on failure
                    Toast.makeText(LoginActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //if user is already authenticated direct call fetch twitter email api
            Toast.makeText(this, "User already authenticated", Toast.LENGTH_SHORT).show();
            fetchTwitterEmail(getTwitterSession());
        }
    }

    public void fetchTwitterEmail(final TwitterSession twitterSession) {
        client.requestEmail(twitterSession, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                if(result!=null){
                    long tw_id = twitterSession.getUserId();
                    String first_name =  twitterSession.getUserName();
                    LoginTwitterPost(tw_id,first_name);
                    //Toast.makeText(LoginActivity.this,  twitterSession.getUserId() + "\nScreen Name : " + twitterSession.getUserName(), Toast.LENGTH_SHORT).show();
                //   mTwitter.setOAuthAccessToken(null);
                    client.cancelAuthorize();

                }
                //here it will give u only email and rest of other information u can get from TwitterSession
               // userDetailsLabel.setText("User Id : " + twitterSession.getUserId() + "\nScreen Name : " + twitterSession.getUserName() + "\nEmail Id : " + result.data);
            }



            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TwitterSession getTwitterSession() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        //NOTE : if you want to get token and secret too use uncomment the below code
        /*TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;*/

        return session;
    }

    private void LoginTwitterPost(final long tw_id, final String first_name) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                String twitterid = String.valueOf(tw_id);
                Request request = original.newBuilder()
                        .header("authenticate_key", "abcd123XYZ")
                        .header("action", "twlogin")
                        .header("tw_id", twitterid)
                        .header("first_name", first_name)
                        .header("last_name", "")
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
        APICalls.Login apiService= retrofit.create(APICalls.Login.class);
        Call<ResponseBody> call = apiService.Login();
        showProgressdialog();
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    preferenceUtils.saveBooleanToSp(LoginActivity.this,"SocialLogin",true);
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
    }

    private void LoginFaceBookPost(final String id, final String email, final String first_name, final String last_name) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
            //    String twitterid = id;
                Request request = original.newBuilder()
                        .header("authenticate_key", "abcd123XYZ")
                        .header("action", "fblogin")
                        .header("fb_id", id)
                        .header("mail_id",email)
                        .header("first_name", first_name)
                        .header("last_name", last_name)
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
        APICalls.Login apiService= retrofit.create(APICalls.Login.class);
        Call<ResponseBody> call = apiService.Login();
        showProgressdialog();
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    preferenceUtils.saveBooleanToSp(LoginActivity.this,"SocialLogin",true);
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

    }

    private void LoginGooglePost(final String id, final String email, final String first_name, final String last_name) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                //    String twitterid = id;
                Request request = original.newBuilder()
                        .header("authenticate_key", "abcd123XYZ")
                        .header("action", "glogin")
                        .header("google_id", id)
                        .header("mail_id",email)
                        .header("first_name", first_name)
                        .header("last_name", last_name)
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
        APICalls.Login apiService= retrofit.create(APICalls.Login.class);
        Call<ResponseBody> call = apiService.Login();
        showProgressdialog();
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    preferenceUtils.saveBooleanToSp(LoginActivity.this,"SocialLogin",true);
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

    }

    private void LoginPost() {
        try {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("authenticate_key", "abcd123XYZ")
                            .header("action", "login")
                            .header("login_id", username)
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
            APICalls.Login apiService= retrofit.create(APICalls.Login.class);
            Call<ResponseBody> call = apiService.Login();
            showProgressdialog();
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = response.body().string();
                        preferenceUtils.saveBooleanToSp(LoginActivity.this,"SocialLogin",false);
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

    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }

    private void postdata(String s) {
        try {
            dismissProgressDislogue();

            JSONObject jObject = new JSONObject(s);
            if(jObject.has("response")) {
                JSONObject obj = jObject.getJSONObject("response");
                if (obj.getString("status").equals("error")) {
                    String message = obj.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                } else {
                    String message = obj.getString("message");
                    JSONArray jsonArray = new JSONArray(obj.getString("data"));
                    JSONObject dataobj = jsonArray.getJSONObject(0);
                    String userid = dataobj.getString("user_id");
                    preferenceUtils.setUserId(dataobj.getString("user_id"));
                    preferenceUtils.setSessionId(dataobj.getString("sessionid"));
                    PreferenceUtils.saveBooleanToSp(LoginActivity.this,"LoginStatus",true);
                    preferenceUtils.setPassword(password);
                    Intent i = new Intent(LoginActivity.this, OGAMainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void showProgressdialog(){
        if (!progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(LoginActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the twitterAuthClient.
        if (client != null)
            client.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);

//facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
//gmail
       if (requestCode == 111) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            /*Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);*/
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            handleSignInResult1(result);
        }else if (requestCode==125 && resultCode ==RESULT_OK){
           finish();
       }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void getUserProfile(com.facebook.AccessToken currentAccessToken) {

        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = null;
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            if(object.has("email")){
                                 email=object.getString("email");
                            }
                            String id = object.getString("id");
                            //String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                            String url="https://graph.facebook.com/{userID}?fields=picture.width(720).height(720)";
                          
                            /*Glide.with(FBIntegrationActivity.this).load(image_url)
                                    .asBitmap().placeholder(R.drawable.ic_launcher)
                                    .into(image);*/
                            LoginFaceBookPost(id,email,first_name,last_name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }



    public void facebookIntegration(){
        callbackManager = CallbackManager.Factory.create();
        final String EMAIL = "email";

        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        /*final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();*/
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }
    public void gmailIntegrartion(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }

    }
    public void handleSignInResult1(GoogleSignInResult result){

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String name=acct.getDisplayName()+"-"+acct.getGivenName();
            String gmail=acct.getEmail()+"-"+acct.getFamilyName();
            String id=acct.getId();
            String firstname = acct.getGivenName();
            String lastname = acct.getFamilyName();
                LoginGooglePost(id,gmail,firstname,lastname);
            /*String image_url= acct.getPhotoUrl().toString();
            Glide.with(GoogleIntegration.this).load(image_url)
                    .asBitmap().placeholder(R.drawable.ic_launcher)
                    .into(imgProfilePic);*/
            getUsername();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                        }
                    });
        }
    }

    public String getUsername() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");

            if (parts.length > 1)
                return parts[0];
        }
        return null;
    }
}
