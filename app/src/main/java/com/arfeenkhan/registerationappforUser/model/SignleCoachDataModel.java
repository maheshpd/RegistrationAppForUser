package com.arfeenkhan.registerationappforUser.model;

public class SignleCoachDataModel {
    private String id, coachname, username, userid, date, time, userphone;

    public SignleCoachDataModel(String username, String userid) {
        this.username = username;
        this.userid = userid;
    }

    public SignleCoachDataModel(String id, String coachname, String username, String userid, String date, String time, String userphone) {
        this.id = id;
        this.coachname = coachname;
        this.username = username;
        this.userid = userid;
        this.date = date;
        this.time = time;
        this.userphone = userphone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoachname() {
        return coachname;
    }

    public void setCoachname(String coachname) {
        this.coachname = coachname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }
}
