package com.example.pconchat;

import com.google.firebase.firestore.Exclude;

import io.reactivex.annotations.NonNull;

public class BlogPostid {


@Exclude
    public String BlogPostid;
    public <T extends  BlogPostid> T withId(@NonNull final String id){
        this.BlogPostid=id;
        return (T)this;
    }
}
