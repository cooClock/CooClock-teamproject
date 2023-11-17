package com.example.cooclock;

public class subCategroyItem {
    String subCategoryName;
    int resId;

    subCategroyItem(String subCategoryName, int resId) {
        this.subCategoryName = subCategoryName;
        this.resId = resId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }
    public void setSubCategoryName(String name) {
        this.subCategoryName = name;
    }

    public int getResId() {
        return resId;
    }
    public void setResId(int resId) {
        this.resId = resId;
    }
}