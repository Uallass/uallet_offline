package com.example.uallas.uallet.model;

/**
 * Created by Uallas on 15/06/2017.
 */

public class Country {

    private int id;
    private String initials;
    private String description;
    private String imageName;

    public Country() {
    }

    public Country(int id, String initials, String description, String imageName) {
        this.id = id;
        this.initials = initials;
        this.description = description;
        this.imageName = imageName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
