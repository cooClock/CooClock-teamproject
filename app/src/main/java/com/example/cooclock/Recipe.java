package com.example.cooclock;

import java.util.List;

public class Recipe {
    private String title;
    private long totaltime;
    private String writer;
    private String category;
    private long servings;
    private List<List<String>> recipeIngredient;
    private List<List<String>> recipeStep;
    private List<Long> rating;


    public Recipe() {
    }

    public Recipe(String category,List<List<String>> recipeIngredient,
                  List<List<String>> recipeStep, List<Long> rating,
                  long servings, String title, long totaltime, String writer) {
        this.category = category;
        this.recipeIngredient = recipeIngredient;
        this.recipeStep = recipeStep;
        this.rating = rating;
        this.servings = servings;
        this.title = title;
        this.totaltime = totaltime;
        this.writer = writer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<List<String>> getRecipeIngredient() {
        return recipeIngredient;
    }

    public void setRecipeIngredient(List<List<String>> recipeIngredient) {
        this.recipeIngredient = recipeIngredient;
    }

    public List<List<String>> getRecipeStep() {
        return recipeStep;
    }

    public void setRecipeStep(List<List<String>> recipeStep) {
        this.recipeStep = recipeStep;
    }

    public List<Long> getRating() {
        return rating;
    }

    public void setRating(List<Long> rating) {
        this.rating = rating;
    }

    public long getServings() {
        return servings;
    }

    public void setServings(long servings) {
        this.servings = servings;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(Long totaltime) {
        this.totaltime = totaltime;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}
