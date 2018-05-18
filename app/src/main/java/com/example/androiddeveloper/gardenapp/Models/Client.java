package com.example.androiddeveloper.gardenapp.Models;

public class Client {
    private String sname;
    private String name;

    private String email;

    public Client() {
    }

    public Client(String sname, String name, String email) {
        this.sname = sname;
        this.name = name;

        this.email = email;
    }

    public String getName() {
        return name;
    }


    public String getSname() {
        return sname;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}