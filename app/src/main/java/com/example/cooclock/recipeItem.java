package com.example.cooclock;

public class recipeItem {
    String title;
    int resId;
    int totalTime;
    int likeCnt;

    recipeItem(String title, int resId, int totalTime, int likeCnt){
        this.title = title;
        this.resId = resId;
        this.totalTime = totalTime;
        this.likeCnt = likeCnt;
    }

    public String getTitle(){
        return title;
    }

    public int getResId(){
        return resId;
    }

    public int getTotalTime(){
        return totalTime;
    }

    public int getLikeCnt(){
        return likeCnt;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setResId(int resId){
        this.resId = resId;
    }

    public void setTotalTime(int totalTime){
        this.totalTime = totalTime;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }
}
