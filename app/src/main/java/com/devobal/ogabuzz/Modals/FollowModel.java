package com.devobal.ogabuzz.Modals;

/**
 * Created by Devobal on 2/9/2019 on 12:10 PM.
 */
public class FollowModel {

    String name,followerid,followimg;

    public String getFollowimg() {
        return followimg;
    }

    public void setFollowimg(String followimg) {
        this.followimg = followimg;
    }

    public String getFollowerid() {
        return followerid;
    }

    public void setFollowerid(String followerid) {
        this.followerid = followerid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    int image;
}
