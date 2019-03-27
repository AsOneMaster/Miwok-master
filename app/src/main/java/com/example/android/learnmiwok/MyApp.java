package com.example.android.learnmiwok;

import android.app.Application;

public class MyApp extends Application {
    private String userip;
    private String msg;
    private String addr;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    private Double latitude;
    private Double longitude;
    private String name;
    //单例模式
    private static MyApp singleInstance;

    public static MyApp getInstance() {
        return singleInstance;

    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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
