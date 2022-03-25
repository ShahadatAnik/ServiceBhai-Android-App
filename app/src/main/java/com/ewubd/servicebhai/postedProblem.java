package com.ewubd.servicebhai;

public class postedProblem {
    int id;
    String name,helptype,postdetail;

    public postedProblem(int id, String name, String helptype, String postdetail) {
        this.id = id;
        this.name = name;
        this.helptype = helptype;
        this.postdetail = postdetail;
    }
    public postedProblem(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHelptype() {
        return helptype;
    }

    public void setHelptype(String helptype) {
        this.helptype = helptype;
    }

    public String getPostdetail() {
        return postdetail;
    }

    public void setPostdetail(String postdetail) {
        this.postdetail = postdetail;
    }
}
