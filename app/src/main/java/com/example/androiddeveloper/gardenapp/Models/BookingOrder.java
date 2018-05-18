package com.example.androiddeveloper.gardenapp.Models;

public class BookingOrder {


    private String name;
    private String sname;
    private String email;
    private String requesttime;
    private String date;
    private String time;
    private String noofpersons;
    private String aditionalinfo;
    private String odrid;
    private String requeststate;

    public BookingOrder() {
    }

    public BookingOrder(String name, String sname, String email, String requesttime, String date, String time, String noofpersons, String aditionalinfo, String odrid, String requeststate) {
        this.name = name;
        this.sname = sname;
        this.email = email;
        this.requesttime = requesttime;
        this.date = date;
        this.time = time;
        this.noofpersons = noofpersons;
        this.aditionalinfo = aditionalinfo;
        this.odrid = odrid;
        this.requeststate = requeststate;
    }

    public String getRequeststate() {
        return requeststate;
    }

    public String getOdrid() {
        return odrid;
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

    public String getRequesttime() {
        return requesttime;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getNoofpersons() {
        return noofpersons;
    }

    public String getAditionalinfo() {
        return aditionalinfo;
    }
}
