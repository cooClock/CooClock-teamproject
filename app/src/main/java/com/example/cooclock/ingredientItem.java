package com.example.cooclock;

public class ingredientItem {
    String name;
    String weight;
    String kind;

    ingredientItem(){
        this.name = "";
        this.weight = "";
        this.kind = "";
    }

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
