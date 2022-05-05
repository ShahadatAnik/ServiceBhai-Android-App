package com.ewubd.servicebhai;

public class CatagoryListHomePageUser {

    private String category_name;
    private int imgid;

    public CatagoryListHomePageUser(String category_name, int imgid) {
        this.category_name = category_name;
        this.imgid = imgid;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }
}