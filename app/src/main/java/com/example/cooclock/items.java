package com.example.cooclock;

//public class items {
//}
public class items {
    public String itemtitle;
    public String category;
    public Integer likeCnt;
    public Integer totaltime;
    public Integer servings;
    public String writer;

    // Constructor
    public items(String itemtitle, String category, Integer likeCnt, Integer totaltime, Integer servings, String writer) {
        this.itemtitle = itemtitle;
        this.category = category;
        this.likeCnt = likeCnt;
        this.totaltime = totaltime;
        this.servings = servings;
        this.writer = writer;
    }
}