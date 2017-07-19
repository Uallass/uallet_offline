package com.example.uallas.uallet.model;

import java.util.Date;

/**
 * Created by Uallas on 15/06/2017.
 */

public class Travel {

    private int id;
    private String location;
    private int country;
    private Date dateBeginning;
    private Date dateEnd;
    private boolean finished;

    public Travel(int id, String location, int country, Date dateBeginning, Date dateEnd, boolean finished) {
        this.id = id;
        this.location = location;
        this.country = country;
        this.dateBeginning = dateBeginning;
        this.dateEnd = dateEnd;
        this.finished = finished;
    }

    public Travel() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public Date getDateBeginning() {
        return dateBeginning;
    }

    public void setDateBeginning(Date dateBeginning) {
        this.dateBeginning = dateBeginning;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
