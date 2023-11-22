package com.example.cooclock;

public class ingredientItem {
    String name;
    String weight;

    ingredientItem(String name, String weight){
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getWeight() {
        return weight;
    }

}
