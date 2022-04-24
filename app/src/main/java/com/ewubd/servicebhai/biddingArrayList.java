package com.ewubd.servicebhai;

public class biddingArrayList {
    int biddingid, postid, userid, biddingamount;
    String username, comment;

    public biddingArrayList(int biddingid, int postid, int userid, int biddingamount, String comment, String username) {
        this.biddingid = biddingid;
        this.postid = postid;
        this.userid = userid;
        this.biddingamount = biddingamount;
        this.comment = comment;
        this.username = username;
    }

    public int getBiddingid() {
        return biddingid;
    }

    public void setBiddingid(int biddingid) {
        this.biddingid = biddingid;
    }

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getBiddingamount() {
        return biddingamount;
    }

    public void setBiddingamount(int biddingamount) {
        this.biddingamount = biddingamount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
