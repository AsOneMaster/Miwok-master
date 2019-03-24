package com.example.android.learnmiwok.bean;

import java.sql.Date;

public class LocationBean {
    private int userId;                //本机用户名
    private int otherId;               //监控的用户名
    private Date event_date;
    public Date getEvent_date() {
		return event_date;
	}
	public void setEvent_date(Date event_date) {
		this.event_date = event_date;
	}

	private String radius ;                //精度
    private String addr;                   //地址
    private String locationDescribe;      //位置描述
    private String date;
    private String end_addr;                   //地址
    private String end_locationDescribe;      //位置描述
    private String end_date;
    public String getEnd_addr() {
		return end_addr;
	}
	public void setEnd_addr(String end_addr) {
		this.end_addr = end_addr;
	}
	public String getEnd_locationDescribe() {
		return end_locationDescribe;
	}
	public void setEnd_locationDescribe(String end_locationDescribe) {
		this.end_locationDescribe = end_locationDescribe;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	private String speed;
    private String height;

    public LocationBean(){

    }
    public LocationBean(int userId,String addr, String locationDescribe,String date,String end_addr,String end_locationDescribe,String end_date) {
        this.userId = userId;     
        this.event_date=event_date;      
        this.addr = addr;
        this.locationDescribe = locationDescribe;
        this.date=date;
        this.end_addr=end_addr;
        this.end_locationDescribe=end_locationDescribe;
        this.end_date=end_date;
    }
    public LocationBean(int userId,Date event_date,String addr, String locationDescribe,String date) {
        this.userId = userId;
        this.event_date=event_date;
        this.addr = addr;
        this.locationDescribe = locationDescribe;
        this.date=date;

    }
    public LocationBean(int userId, String end_addr, String end_locationDescribe,String end_date) {
        this.userId = userId;
        this.end_addr = addr;
        this.end_locationDescribe =end_locationDescribe;
        this.end_date=end_date;

    }
    public LocationBean(int userId) {
        this.userId = userId;

    }
    public int getOtherId() {
        return otherId;
    }

    public void setOtherId(int otherId) {
        this.otherId = otherId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    

    public String  getRadius() {
        return radius;
    }

    public void setRadius(String  radius) {
        this.radius = radius;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLocationDescribe() {
        return locationDescribe;
    }

    public void setLocationDescribe(String locationDescribe) {
        this.locationDescribe = locationDescribe;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
