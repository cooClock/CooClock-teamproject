package com.example.cooclock;

public class myRefrigeratorCategoryItem {
    String categoryName;
    int resID;

    myRefrigeratorCategoryItem(String categoryName, int resID){
        this.categoryName = categoryName;
        this.resID = resID;
    }

    public int getResID() {
        return resID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
