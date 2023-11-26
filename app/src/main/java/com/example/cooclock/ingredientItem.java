package com.example.cooclock;

import java.math.BigDecimal;

public class ingredientItem {
    String name;
    String weight;
    String kind;

    ingredientItem(String name, String weight,String kind){
        this.name = name;
        this.weight = weight;
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public String getWeight() {
        return weight;
    }

    public String getKind() {
        return kind;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public float getWeightF(){return Float.parseFloat(weight);}
    public void setName(String name) {
        this.name = name;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

}
