package com.example.cooclock;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.willy.ratingbar.BaseRatingBar;

public class recipe_init_item {
    String recipe_writer; // 작성자
    String recipe_servings; // 기준 인원
    String recipe_title; // 제목
    boolean favorite; // 좋아요 버튼
    double rating_average; // 평균 평점
    int rating_cnt; // 리뷰 개수
    int five_point; // 5점
    int four_point; // 4점
    int three_point; // 3점
    int two_point; // 2점
    int one_point; // 1점
    int total_step; // 전체 레시피 단계 수

    recipe_init_item(String recipe_writer, String recipe_servings, String recipe_title, boolean favorite,
                     double rating_average, int rating_cnt, int five_point, int four_point, int three_point,
                     int two_point, int one_point, int total_step){
        this.recipe_writer = recipe_writer;
        this.recipe_servings = recipe_servings;
        this.recipe_title = recipe_title;
        this.favorite = favorite;
        this.rating_average = rating_average;
        this.rating_cnt = rating_cnt;
        this.five_point = five_point;
        this.four_point = four_point;
        this.three_point = three_point;
        this.two_point = two_point;
        this.one_point = one_point;
        this.total_step = total_step;
    }

    public String getRecipe_writer() {
        return recipe_writer;
    }

    public String getRecipe_servings() {
        return recipe_servings;
    }

    public String getRecipe_title() {
        return recipe_title;
    }
    public boolean getFavorite(){
        return favorite;
    }

    public double getRating_average() {
        return rating_average;
    }

    public int getRating_cnt() {
        return rating_cnt;
    }

    public int getFive_point() {
        return five_point;
    }

    public int getFour_point() {
        return four_point;
    }

    public int getThree_point() {
        return three_point;
    }

    public int getTwo_point() {
        return two_point;
    }

    public int getOne_point() {
        return one_point;
    }

    public int getTotal_step() { return total_step; }
}
