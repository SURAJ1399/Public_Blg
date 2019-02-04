package com.example.pconchat;

import java.util.Date;

public class BlogPost extends BlogPostid{
    public String user,desc,image_url;
    public Date timestamp;




    public BlogPost(){

    }

    public BlogPost(String user, String desc, String image_url, Date timestamp) {
        this.user = user;
        this.desc = desc;
        this.image_url = image_url;
        this.timestamp = timestamp;

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }




}
