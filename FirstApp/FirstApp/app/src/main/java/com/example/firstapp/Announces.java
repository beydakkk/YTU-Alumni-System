package com.example.firstapp;

public class Announces {

    String title;
    String announce;
    String date;
    String image;

    public Announces() {
    }

    public Announces(String title, String announce, String date, String image) {
        this.title = title;
        this.announce = announce;
        this.date = date;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
