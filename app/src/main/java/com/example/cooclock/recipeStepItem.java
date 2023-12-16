package com.example.cooclock;

public class recipeStepItem {
    int resId;
    int number;
    String description;
    String time;

    recipeStepItem(int number, String description, String time, int resId){
        this.number = number;
        this.description = description;
        this.time = time;
        this.resId = resId;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public int getMinute() {
        return Integer.parseInt(time);
    }

    public int getSecond() {
        return (int) ((Double.parseDouble(time)-Integer.parseInt(time))*100);
    }

    public int getResId() {
        return resId;
    }
}


