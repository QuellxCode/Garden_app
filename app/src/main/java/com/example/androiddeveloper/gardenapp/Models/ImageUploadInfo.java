package com.example.androiddeveloper.gardenapp.Models;



public class ImageUploadInfo {

    public String imgid;

    public String status;
    public String title;
    public String details;
    public String dkey;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String dkey, String imgid, String status, String title, String details) {

        this.imgid = imgid;
        this.status = status;
        this.title = title;
        this.dkey = dkey;
        this.details = details;

    }

    public String getDetails() {
        return details;
    }

    public String getTitle() {
        return title;
    }

    public String getKey() {
        return dkey;
    }

    public String getImageName() {
        return status;
    }

    public String getImageURL() {
        return imgid;
    }

}
