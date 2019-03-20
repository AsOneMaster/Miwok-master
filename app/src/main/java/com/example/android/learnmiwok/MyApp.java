package com.example.android.learnmiwok;

import android.app.Application;

public class MyApp extends Application {
    private String userip;
    private Double latitude;
    private Double longitude;
    private String name;
    private String otherip;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtherip() {
        return otherip;
    }

    public void setOtherip(String otherid) {
        this.otherip = otherid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUserip() {

        return userip;
    }
    public void setUserip(String s) {
        userip = s;
    }
}
