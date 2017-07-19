package com.example.uallas.uallet.model;

/**
 * Created by Uallas on 15/06/2017.
 */

public class Category {

    private int id;
    private String imageName;
    private String descCategory;
    private String language;

    public Category() {
    }

    public Category(int id, String imageName, String descCategory, String language) {
        this.id = id;
        this.imageName = imageName;
        this.descCategory = descCategory;
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDescCategory() {
        return descCategory;
    }

    public void setDescCategory(String descCategory) {
        this.descCategory = descCategory;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
