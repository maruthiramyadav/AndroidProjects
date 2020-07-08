package com.devobal.ogabuzz.Modals;

/**
 * Created by Devobal on 2/21/2019 on 12:09 AM.
 */
public class NewsModel {
    String cat_id,cat_name;
    Boolean categorycheck;

    public Boolean getCategorycheck() {
        return categorycheck;
    }

    public void setCategorycheck(Boolean categorycheck) {
        this.categorycheck = categorycheck;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
}
