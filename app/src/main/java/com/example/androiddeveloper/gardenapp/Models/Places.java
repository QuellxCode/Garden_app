package com.example.androiddeveloper.gardenapp.Models;



public class Places {
    public double plat, plng;
    public String pname, pdesc;
    public int pimg, pid;

    public Places(double plat, double plng, String pname, String pdesc, int pimg, int pid) {
        this.plat = plat;
        this.plng = plng;
        this.pname = pname;
        this.pdesc = pdesc;
        this.pimg = pimg;
        this.pid = pid;

    }
}
