package com.arfeenkhan.registerationappforUser.model;

public class CityModel {

    String name;

    public String image;


    public CityModel() {
    }

    public CityModel(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
