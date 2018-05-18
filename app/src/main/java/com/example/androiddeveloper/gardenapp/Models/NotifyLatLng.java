package com.example.androiddeveloper.gardenapp.Models;

public class NotifyLatLng {
    double notilat, notilng;
    String address;

    public NotifyLatLng(double notilat, double notilng, String address) {
        this.notilat = notilat;
        this.notilng = notilng;
        this.address = address;
    }

    public double getNotilat() {
        return notilat;
    }

    public double getNotilng() {
        return notilng;
    }

    public String getAddress() {
        return address;
    }
}
