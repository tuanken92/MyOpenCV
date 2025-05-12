package com.example.myopencv.model;

public class UserResponse {
    public String code;
    public UserData data;
    public String image;
    public boolean status;
    public String timestamp;
    public String url;

    @Override
    public String toString() {
        return "UserResponse{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", image='" + image + '\'' +
                ", status=" + status +
                ", timestamp='" + timestamp + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
