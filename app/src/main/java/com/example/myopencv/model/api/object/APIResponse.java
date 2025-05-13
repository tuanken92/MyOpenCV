package com.example.myopencv.model.api.object;

import com.google.gson.annotations.SerializedName;

public class APIResponse {
    @SerializedName("message")
    private String message;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
