package com.example.cooclock;

public class recipeItem {
    String title;
    String resId;
    int totalTime;
    int likeCnt;
    // 기본 생성자
    public recipeItem() {
        // 기본 생성자 내용 (필요 시 추가)
    }


    public recipeItem(String title, String resId, int totalTime, int likeCnt){
        this.title = title;
        this.resId = resId;
        this.totalTime = totalTime;
        this.likeCnt = likeCnt;
    }

    public String getTitle(){
        return title;
    }

    public String getResId(){
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
    public void setResId(String resId){
        this.resId = resId;
    }

    public void setTotalTime(int totalTime){
        this.totalTime = totalTime;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }
}
