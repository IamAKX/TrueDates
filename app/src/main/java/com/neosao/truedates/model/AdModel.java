package com.neosao.truedates.model;

import java.io.Serializable;

public class AdModel implements Serializable {
    private String id;

    public AdModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AdModel{" +
                "id='" + id + '\'' +
                '}';
    }
}
