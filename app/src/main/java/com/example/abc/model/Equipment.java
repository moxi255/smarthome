package com.example.abc.model;

import java.io.Serializable;

public class Equipment implements Serializable
{
    //环境传感器=0，灯光=1，窗帘=2，多媒体=3，空调=4。。。
    private int type;
    private String id;
    private String name;
    private int imageId;
    private int state;

    public Equipment() { }

    public Equipment(int type, String id, String name, int imageId, int state) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.imageId = imageId;
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }


}
