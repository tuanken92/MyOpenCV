package com.example.myopencv.model.api;


import com.example.myopencv.model.UserResponse;
import com.example.myopencv.model.api.object.APIResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("/api/get_guests")
    Call<List<UserResponse>> getCustomer();

    @POST("/api/update_guest")
    Call<APIResponse> updateCustomer(@Body UserResponse updateCustomer);

    @POST("/api/insert_guest")
    Call<APIResponse> insertCustomer(@Body UserResponse insertCustomer);


}
