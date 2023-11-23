package com.example.cooclock;

public class recipeStepItem {
    int resId;
    int number;
    String description;
    int minute;
    int second;

    recipeStepItem(int number, String description, int minute, int second, int resId){
        this.number = number;
        this.description = description;
        this.minute = minute;
        this.second = second;
        this.resId = resId;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getResId() {
        return resId;
    }
}


