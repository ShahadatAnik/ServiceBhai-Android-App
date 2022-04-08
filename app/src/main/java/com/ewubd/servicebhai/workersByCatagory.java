package com.ewubd.servicebhai;

public class workersByCatagory {
    int workersid;
    String workersName, bio;

    public workersByCatagory(int workersid , String workersName, String bio) {
        this.workersid = workersid;
        this.workersName = workersName;
        this.bio = bio;
    }
    public workersByCatagory(){}

    public int getWorkersid() {
        return workersid;
    }

    public void setWorkersid(int workersid) {
        this.workersid = workersid;
    }

    public String getWorkesrName() {
        return workersName;
    }

    public void setWorkersName(String workersName) {
        this.workersName = workersName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
