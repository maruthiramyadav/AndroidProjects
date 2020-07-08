package com.devobal.ogabuzz.SpecialClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class PreferenceUtils {

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private static int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "OGABuzz";
    public static String network_state="network_state";

    public static ConnectivityManager cm;
    public static NetworkInfo info;

    public PreferenceUtils(Context context){
        pref = context.getSharedPreferences("OGABuzz",Context.MODE_PRIVATE);
    }

    public static void saveBooleanToSp(Context context,String keyName,boolean value) {
        // TODO Auto-generated method stub
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.putBoolean(keyName, value);
        editor.commit();
    }

    public  static boolean getBooleanFromSP(Context context,String keyName) {
        // TODO Auto-generated method stub
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getBoolean(keyName,false);
    }

    public static void clearSP(Context context) {
        // TODO Auto-generated method stub
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit().clear();
        editor.commit();
    }

    public void setUserId(String userId) {
        pref.edit().putString("user_id", userId).apply();
    }

    public String getUserId(){

        return pref.getString("user_id", "0");    }


    public void setSessionId(String sessionId) {
        pref.edit().putString("sessionid", sessionId).apply();
    }

    public String getSessionId(){
        return pref.getString("sessionid", "0");
    }
    public void setUserName(String username) {
        pref.edit().putString("username", username).apply();
    }

    public String getUserName(){
        return pref.getString("username", "");
    }

    public void setFirstname(String fname) {
        pref.edit().putString("Firstname", fname).apply();
    }

    public String getFirstname(){
        return pref.getString("Firstname", "");
    }

    public void setPassword(String password) {
        pref.edit().putString("Password", password).apply();
    }

    public String getPassword(){
        return pref.getString("Password", "");
    }

    public void setLastname(String lname) {
        pref.edit().putString("Lastname", lname).apply();
    }

    public String getLastname(){
        return pref.getString("Lastname", "");
    }

    public void setCurrentPage(String currentpage) {
        pref.edit().putString("Currentpage", currentpage).apply();
    }

    public String getCurrentpage(){
        return pref.getString("Currentpage", "");
    }
    public void setCategoryName(String categoryname) {
        pref.edit().putString("CategoryName", categoryname).apply();
    }

    public String getCategoryName(){
        return pref.getString("CategoryName", "");
    }
    public void setLanguages(String language) {
        pref.edit().putString("Language", language).apply();
    }

    public String getLanguages(){
        return pref.getString("Language", "");
    }

    public void setSelNewsId(String selnewsId){
        pref.edit().putString("selnewsid",selnewsId).apply();
    }
    public String getSelNewsId(){
        return pref.getString("selnewsid","");
    }

    public void setSearchText(String searchText){
        pref.edit().putString("search",searchText).apply();
    }
    public String getSearchText(){
        return pref.getString("search","");
    }

    public void setLastVisibleItemPosition(int itemPosition){
        pref.edit().putInt("itemPositon",itemPosition).apply();
    }
    public int getLastVisibleItemPosition(){
        return pref.getInt("itemPositon",0);
    }
}
