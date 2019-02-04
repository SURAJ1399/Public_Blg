package com.example.pconchat;

import java.util.Date;

public class Comments {


    String message,currentuserid;
    Date timestamp;
    public  Comments(){

    }

    public Comments(String message, String currentuserid, Date timestamp) {
        this.message = message;
        this.currentuserid = currentuserid;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrentuserid() {
        return currentuserid;
    }

    public void setCurrentuserid(String currentuserid) {
        this.currentuserid = currentuserid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }



}
