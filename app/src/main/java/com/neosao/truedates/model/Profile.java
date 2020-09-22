package com.neosao.truedates.model;

public class Profile {

    int id;
    private String name;
    private String imageUrl;
    private int age;
    private String location;

    public Profile(int id, String imageUrl, String name, int age, String location) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.age = age;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}