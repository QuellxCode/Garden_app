package com.example.androiddeveloper.gardenapp.Models;

/**
 * Created by Android Developer on 4/11/2018.
 */

public class savephotomodel {
    String name, link;
    int url;


    public savephotomodel() {
    }

    public savephotomodel(int url, String name, String link) {
        this.url = url;
        this.name = name;
        this.link = link;

    }


    public int getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
