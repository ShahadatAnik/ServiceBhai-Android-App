package com.ewubd.servicebhai;

public class workerReviewClass {
    int rateid, raterID, userID, rate;
    String review;

    public workerReviewClass(int rateid, int raterID, int userID, int rate, String review) {
        this.rateid = rateid;
        this.raterID = raterID;
        this.userID = userID;
        this.rate = rate;
        this.review = review;
    }

    public int getRateid() {
        return rateid;
    }

    public void setRateid(int rateid) {
        this.rateid = rateid;
    }

    public int getRaterID() {
        return raterID;
    }

    public void setRaterID(int raterID) {
        this.raterID = raterID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
