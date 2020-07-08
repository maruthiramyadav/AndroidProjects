package com.devobal.ogabuzz.Retrofit;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface APICalls {

    public interface Register{
        //https://www.ogabuzz.com/api/user
        @POST("api/user")
        Call<ResponseBody> insertUser();
    }
    public interface Login{
        //https://www.ogabuzz.com/api/user
        @POST("api/user")
        Call<ResponseBody> Login();
    }

    public interface Logout{
        //https://www.ogabuzz.com/api/user
        @POST("api/user")
        Call<ResponseBody> Logout();
    }
    public interface AddNewsArticleImg{
        //https://www.ogabuzz.com/api/user
        @Multipart
        @POST("api/article")
        Call<ResponseBody> AddNewsArticleImg(@Part MultipartBody.Part[] surveyImage);
        //Call<ResponseBody> AddNewsArticleImg(@Part List<MultipartBody.Part> images);
    }
    public interface AddNewsArticle{
        //https://www.ogabuzz.com/api/user
        @Multipart
        @POST("api/article")
        Call<ResponseBody> AddNewsArticle(@Part MultipartBody.Part file);
    }

    public interface AddNewsArticle1{
        //https://www.ogabuzz.com/api/user
        @POST("api/article")
        Call<ResponseBody> AddNewsArticle1();
    }
    public interface GetNews{
        //https://www.ogabuzz.com/api/news
        @GET("api/news")
        Call<ResponseBody> GetNews();
    }
    public interface PostComment{
        //https://www.ogabuzz.com/api/comment
        @POST("api/comment")
        Call<ResponseBody> PostComment();
    }
    public interface GetComments{
        //https://www.ogabuzz.com/api/comment
        @GET("api/comment")
        Call<ResponseBody> GetComments();
    }
    public interface MakeFollow{
        //https://www.ogabuzz.com/api/follow
        @POST("api/follow")
        Call<ResponseBody> makefollow();
    }
    public interface SaveLike{
        //https://www.ogabuzz.com/api/newslike
        @POST("api/newslike")
        Call<ResponseBody> savelike();
    }
    public interface FollowersList{
        //https://www.ogabuzz.com/api/follow
        @GET("api/follow")
        Call<ResponseBody> followerslist();
    }
    public interface Categories{
        //https://www.ogabuzz.com/api/newscategory
        @GET("api/newscategory")
        Call<ResponseBody> categorieslist();
    }
    public interface ViewsPost{
        //https://www.ogabuzz.com/api/newsview
        @POST("api/newsview")
        Call<ResponseBody> ViewsPost();
    }
    public interface LoggedProfile{
        //https://www.ogabuzz.com/api/user
        @GET("api/user")
        Call<ResponseBody> LoggedProfile();
    }
    public interface GetLanguages{
        //https://www.ogabuzz.com/api/language
        @GET("api/language")
        Call<ResponseBody> GetLanguages();
    }
    public interface LanguagePost{
        //https://www.ogabuzz.com/api/userlanguage
        @POST("api/userlanguage")
        Call<ResponseBody> LanguagePost();
    }

    public interface GetRecomendnews{
        //https://www.ogabuzz.com/api/newsrecommend
        @GET("api/newsrecommend")
        Call<ResponseBody> GetRecomendnews();
    }
    public interface ProfileImgPost{
        //https://www.ogabuzz.com/api/user
        @Multipart
        @POST("api/user")
        Call<ResponseBody> ProfileImgPost(@Part MultipartBody.Part file);
    }
    public interface UserLanguagesGet{
        //https://www.ogabuzz.com/api/userlanguage
        @GET("api/userlanguage")
        Call<ResponseBody> UserLanguagesGet();
    }
    public interface FolloweRemovePost{
        //https://www.ogabuzz.com/api/followremove
        @POST("api/followremove")
        Call<ResponseBody> FolloweRemovePost();
    }
    public interface TrendingNews{
        //https://www.ogabuzz.com/api/trendingpost
        @GET("api/trendingpost")
        Call<ResponseBody> TrendingNews();
    }
    public interface FeedbackPost{
        //https://www.ogabuzz.com/api/feedback
        @POST("api/feedback")
        Call<ResponseBody> FeedbackPost();
    }
    public interface SinglePageGet{
        //https://www.ogabuzz.com/api/pageList
        @GET("api/pageList")
        Call<ResponseBody> SinglePageGet();
    }
    public interface EditProfilePost{
        //https://www.ogabuzz.com/api/user
        @POST("api/user")
        Call<ResponseBody> EditProfilePost();
    }
    public interface PostViewearning{
        //http://www.ogabuzz.com/api/postviewearning
        @POST("api/postviewearning")
        Call<ResponseBody> PostViewearning();
    }
    public interface SingleImgPost{
            //https://www.ogabuzz.com/api/user
        @Multipart
        @POST("api/article")
        Call<ResponseBody> SingleImgPost(@Part MultipartBody.Part file);
    }
    public interface NotificationGet{
        //https://www.ogabuzz.com/api/notificationList
        @GET("api/notificationList")
        Call<ResponseBody> NotificationGet();
    }
    public interface NotificationReadSet{
        //https://www.ogabuzz.com/api/notificationreadsave
        @POST("api/notificationreadsave")
        Call<ResponseBody> notificationReadSet();
    }
    public interface BreakingNews{
        //https://www.ogabuzz.com/api/news
        @GET("api/news")
        Call<ResponseBody> breakingNews();
    }
    public interface NewsEffectiveGet{
        //https://www.ogabuzz.com/api/news
        @GET("api/news")
        Call<ResponseBody> newsEffectiveGet();
    }
    public interface NewsViewSave{
        //https://www.ogabuzz.com/api/news
        @POST("api/news")
        Call<ResponseBody> newsViewsave();
    }
    public interface CommentEdit{
        //https://www.ogabuzz.com/api/commentedit
        @POST("api/commentedit")
        Call<ResponseBody> commentEdit();
    }
    public interface CommentDelete{
        //https://www.ogabuzz.com/api/deletecomment
        @POST("api/deletecomment")
        Call<ResponseBody> commentDelete();
    }
    public interface CommentReply{
        //https://www.ogabuzz.com/api/commentreply
        @POST("api/commentreply")
        Call<ResponseBody> commentReply();
    }
    public interface Youtubedata{
        //https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=50&q=tajmahal&key=AIzaSyCa0FliruUqNpQ6Cmz1wQwSOXTcG6r3lFA
        @GET("search?")
        //@GET("search?part=snippet&maxResults=50&q=tajmahal&key=AIzaSyCa0FliruUqNpQ6Cmz1wQwSOXTcG6r3lFA")
        Call<ResponseBody> youtubedata(@Query("part")String part,
                                       @Query("maxResults")String maxResults,
                                       @Query("q")String q,
                                       @Query("safeSearch")String strict,
                                       @Query("key")String key);
    }
    public interface SpAccountDetailsGet{
        //http://speedynanny.com/AccountDetailsGet?Fk_UserID=
        @GET("/AccountDetailsGet")
        Call<ResponseBody> Sp_Get_AccountDetails(@Query("Fk_UserID")String userId);
    }
    public interface ImportVideoPost{
        //https://www.ogabuzz.com/api/importvideo
        @POST("api/importvideo")
        Call<ResponseBody> importVideoPost();
    }
}
