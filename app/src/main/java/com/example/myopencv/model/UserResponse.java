package com.example.myopencv.model;

import java.io.Serializable;

public class UserResponse implements Serializable {


    private String code;
    private UserData data;
    private String image;
    private boolean status;
    private String timestamp;
    private String url;


    public UserResponse() {
        this.code = "THH000";
        this.data = new UserData();
        this.image = "";
        this.status = false;
        this.timestamp = "";
        this.url = "";
    }

    public UserResponse(String code, String image) {
        this.code = code;
        this.data = new UserData();
        this.image = image;
        this.status = false;
        this.timestamp = "";
        this.url = image;
    }

    public UserResponse(String code, UserData data, String image, boolean status, String timestamp, String url) {
        this.code = code;
        this.data = data;
        this.image = image;
        this.status = status;
        this.timestamp = timestamp;
        this.url = url;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "code='" + code + '\'' +
                ", data=" + data.toString() +
                ", image='" + image + '\'' +
                ", status=" + status +
                ", timestamp='" + timestamp + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
