package com.arfeenkhan.registerationappforUser.Model;

public class SelectTagModel {

    String name, place, tagno, time, ctf, date,Tf;


    public SelectTagModel(String name, String place, String tagno, String time, String ctf, String date, String tf) {
        this.name = name;
        this.place = place;
        this.tagno = tagno;
        this.time = time;
        this.ctf = ctf;
        this.date = date;
        Tf = tf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTagno() {
        return tagno;
    }

    public void setTagno(String tagno) {
        this.tagno = tagno;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCtf() {
        return ctf;
    }

    public void setCtf(String ctf) {
        this.ctf = ctf;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTf() {
        return Tf;
    }

    public void setTf(String tf) {
        Tf = tf;
    }
}
