package com.helping.foodservices;

public class Post {
    private String Amount;
    private  String location;
    private String mobilenumber;
    private  String ownername;
    private  String approvedby;

    public String getApprovedby() {
        return approvedby;
    }

    public void setApprovedby(String approvedby) {
        this.approvedby = approvedby;
    }



    public Post() {
    }

    @Override
    public String toString() {
        return "Post{" +
                "Amount='" + Amount + '\'' +
                ", location='" + location + '\'' +
                ", mobilenumber='" + mobilenumber + '\'' +
                ", ownername='" + ownername + '\'' +
                '}';
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }



}
