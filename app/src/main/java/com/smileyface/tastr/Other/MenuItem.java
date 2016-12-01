package com.smileyface.tastr.Other;


public class MenuItem {
    String imagePath;
    String ingredients;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getIngredients() {
        return ingredients;
    }

    public MenuItem() {
        imagePath = "https://firebasestorage.googleapis.com/v0/b/unt-team-project.appspot.com/o/not%20found%20error.png?alt=media&token=faf7a4a4-08ce-418a-bf84-697f88ba4213";
        ingredients = "Unknown";
        name = "Unknown";
    }
}