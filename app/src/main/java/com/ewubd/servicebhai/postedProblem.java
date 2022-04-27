package com.ewubd.servicebhai;

public class postedProblem {
    int id,personid, markAsDone;
    String name, helptype, postdetail, title;



    public postedProblem(int id , int personid, String title , String name, String helptype, String postdetail, int markAsDone) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.helptype = helptype;
        this.postdetail = postdetail;
        this.personid = personid;
        this.markAsDone = markAsDone;
    }
    public postedProblem(){}

    public int getPersonid() {
        return personid;
    }

    public void setPersonid(int personid) {
        this.personid = personid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public int getMarkAsDone() {
        return markAsDone;
    }

    public void setMarkAsDone(int markAsDone) {
        this.markAsDone = markAsDone;
    }
}
