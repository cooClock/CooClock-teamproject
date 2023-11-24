package com.example.cooclock;

public class knowHowItem {
    String title;
    int resId;

    knowHowItem(String title, int resId){
        this.title = title;
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public int getResId() {
        return resId;
    }
}
