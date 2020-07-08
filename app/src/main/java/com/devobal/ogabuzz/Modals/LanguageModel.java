package com.devobal.ogabuzz.Modals;

/**
 * Created by Devobal on 2/26/2019 on 11:35 PM.
 */
public class LanguageModel {

    String language,langid,selectlangid;
    boolean isselect;
    int image;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getSelectlangid() {
        return selectlangid;
    }

    public void setSelectlangid(String selectlangid) {
        this.selectlangid = selectlangid;
    }

    public String getLangid() {
        return langid;
    }

    public void setLangid(String langid) {
        this.langid = langid;
    }

    public boolean isIsselect() {
        return isselect;
    }

    public void setIsselect(boolean isselect) {
        this.isselect = isselect;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
