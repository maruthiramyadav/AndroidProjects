package com.devobal.ogabuzz.Modals;

import java.util.ArrayList;

public class ListModel {

    String title;
    String description;
    String posttype;
    String author;
    String dateandtime;
    String noofview;
    String nooflike;
    String nooshares;
    String noofcomnts;
    String imgstring;
    String slug;
    String category;
    String userid;
    String newsid;
    String videopath;
    String imagearray;
    String newsfrom;
    String videolink,videotype;

    public String getVideolink() {
        return videolink;
    }

    public void setVideolink(String videolink) {
        this.videolink = videolink;
    }

    public String getVideotype() {
        return videotype;
    }

    public void setVideotype(String videotype) {
        this.videotype = videotype;
    }

    public String getNewsfrom() {
        return newsfrom;
    }

    public void setNewsfrom(String newsfrom) {
        this.newsfrom = newsfrom;
    }

    public String getVideo_thumb_image() {
        return video_thumb_image;
    }

    public void setVideo_thumb_image(String video_thumb_image) {
        this.video_thumb_image = video_thumb_image;
    }

    String video_thumb_image;
    boolean mainpage,isvideo,isthumb;
    int image;
    ArrayList<String> photosArray;

    public ArrayList<String> getPhotosArray() {
        return photosArray;
    }

    public void setPhotosArray(ArrayList<String> photosArray) {
        this.photosArray = photosArray;
    }

    public String getImagearray() {
        return imagearray;
    }

    public void setImagearray(String imagearray) {
        this.imagearray = imagearray;
    }

    public String getNewsid() {
        return newsid;
    }

    public String getVideopath() {
        return videopath;
    }

    public String getNooflike() {
        return nooflike;
    }

    public void setNooflike(String nooflike) {
        this.nooflike = nooflike;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImgstring() {
        return imgstring;
    }

    public void setImgstring(String imgstring) {
        this.imgstring = imgstring;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPosttype() {
        return posttype;
    }

    public void setPosttype(String posttype) {
        this.posttype = posttype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime(String dateandtime) {
        this.dateandtime = dateandtime;
    }

    public String getNoofview() {
        return noofview;
    }

    public void setNoofview(String noofview) {
        this.noofview = noofview;
    }

    public String getNooshares() {
        return nooshares;
    }

    public void setNooshares(String nooshares) {
        this.nooshares = nooshares;
    }

    public String getNoofcomnts() {
        return noofcomnts;
    }

    public void setNoofcomnts(String noofcomnts) {
        this.noofcomnts = noofcomnts;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isMainpage() {
        return mainpage;
    }

    public void setMainpage(boolean mainpage) {
        this.mainpage = mainpage;
    }

    public boolean isIsvideo() {
        return isvideo;
    }

    public void setIsvideo(boolean isvideo) {
        this.isvideo = isvideo;
    }

    public boolean isIsthumb() {
        return isthumb;
    }

    public void setIsthumb(boolean isthumb) {
        this.isthumb = isthumb;
    }
}
