package com.example.uallas.uallet.model;

/**
 * Created by Uallas on 27/06/2017.
 */

public class User {

    private int id;
    private String user;
    private boolean logged;
    private boolean first_time;

    public User() {
    }

    public User(int id, String user, boolean logged, boolean first_time) {
        this.id = id;
        this.user = user;
        this.logged = logged;
        this.first_time = first_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean isFirst_time() {
        return first_time;
    }

    public void setFirst_time(boolean first_time) {
        this.first_time = first_time;
    }
}
