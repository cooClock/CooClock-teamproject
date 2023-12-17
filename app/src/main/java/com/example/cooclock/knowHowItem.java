package com.example.cooclock;

public class knowHowItem {
    String title;
    int resId;
    knowHowItem(){}
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

    public void setTitle(String title) {
        this.title = title;
    }
    public void setResId(int resId) {
        this.resId = resId;
    }

    public int setResId() {
        return resId;
    }
}
