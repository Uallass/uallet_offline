package com.example.uallas.uallet.model;

import java.util.Date;

/**
 * Created by Uallas on 15/06/2017.
 */

public class Transaction {

    private int id;
    private Double value;
    private int codCategory;
    private String description;
    private Date date;
    private int codTravel;
    private String direction; // in/out

    public Transaction(int id, Double value, int codCategory, String description, Date date, int codTravel, String direction) {
        this.id = id;
        this.value = value;
        this.codCategory = codCategory;
        this.description = description;
        this.date = date;
        this.codTravel = codTravel;
        this.direction = direction;
    }

    public Transaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int getCodCategory() {
        return codCategory;
    }

    public void setCodCategory(int codCategory) {
        this.codCategory = codCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCodTravel() {
        return codTravel;
    }

    public void setCodTravel(int codTravel) {
        this.codTravel = codTravel;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
