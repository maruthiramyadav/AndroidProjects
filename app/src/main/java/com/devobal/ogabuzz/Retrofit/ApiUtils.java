package com.devobal.ogabuzz.Retrofit;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://www.ogabuzz.com/";
            //"http://maasinfotech24x7.com/";

    public static final  String GET_USER_URL = "http://maasinfotech24x7.com";

    public static final String GET_URl="https://www.googleapis.com/youtube/v3/";

    public static APICalls.Register postAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APICalls.Register.class);
    }
    public static APICalls.Youtubedata YoutubeUtil(){
        return RetrofitClient.getClient(GET_URl).create(APICalls.Youtubedata.class);
    }
}