package com.devobal.ogabuzz.Modals;

import java.util.ArrayList;

/**
 * Created by Devobal on 3/2/2019 on 5:36 PM.
 */
public class RecomendListModel {

    String newsid;
    String recmmendedby;
    String recomendto;
    String date;
    String userid;
    String title;
    String slug;
    String description;
    String imagelink;
    String videopath;
    String dateandtime;
    String noofview;
    String nooflike;
    String nooshares;
    String noofcomnts;
    String imgstring;
    String category;
    String imagearray;

    public String getVideolink() {
        return videolink;
    }

    public void setVideolink(String videolink) {
        this.videolink = videolink;
    }

    String videolink;

    int image;
    ArrayList<String> photosArray;

    public ArrayList<String> getPhotosArray() {
        return photosArray;
    }

    public void setPhotosArray(ArrayList<String> photosArray) {
        this.photosArray = photosArray;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImagearray() {
        return imagearray;
    }

    public void setImagearray(String imagearray) {
        this.imagearray = imagearray;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getRecmmendedby() {
        return recmmendedby;
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

    public String getNooflike() {
        return nooflike;
    }

    public void setNooflike(String nooflike) {
        this.nooflike = nooflike;
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

    public String getImgstring() {
        return imgstring;
    }

    public void setImgstring(String imgstring) {
        this.imgstring = imgstring;
    }

    public void setRecmmendedby(String recmmendedby) {
        this.recmmendedby = recmmendedby;
    }

    public String getRecomendto() {
        return recomendto;
    }

    public void setRecomendto(String recomendto) {
        this.recomendto = recomendto;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    public String getVideopath() {
        return videopath;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }
}
