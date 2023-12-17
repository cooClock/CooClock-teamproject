package com.example.cooclock;

public class recipeItem {
    String title;
    int resId;
    Long totalTime;
    Long likeCnt;
    // 기본 생성자
    public recipeItem() {
        // 기본 생성자 내용 (필요 시 추가)
    }


    public recipeItem(String title, int resId, Long totalTime, Long likeCnt){
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

    public Long getTotalTime(){
        return totalTime;
    }

    public Long getLikeCnt(){
        return likeCnt;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setResId(int resId){
        this.resId = resId;
    }

    public void setTotalTime(Long totalTime){
        this.totalTime = totalTime;
    }

    public void setLikeCnt(Long likeCnt) {
        this.likeCnt = likeCnt;
    }
}
