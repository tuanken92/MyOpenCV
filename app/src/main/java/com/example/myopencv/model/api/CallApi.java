package com.example.myopencv.model.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;


import com.example.myopencv.model.UserResponse;
import com.example.myopencv.model.api.object.APIResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallApi{
    static String TAG = "TuanNA-CallAPI";

    public static void getCustomer() {

        ApiInterface apiInterface = Retrofit2.getInstance().getApiInterface();

        Call<List<UserResponse>> call = apiInterface.getCustomer();
        call.enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null)  {
                    List<UserResponse> customers = response.body();
                    Log.i(TAG, "getCustomer-onResponse: customers size = " + customers.size());
                    Log.i(TAG, "customers[0] = " + customers.get(0).toString());

                } else {
                    Log.e(TAG, "Request failed: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                Log.e(TAG, "Request failed: " + t.getMessage());
            }
        });
    }



    public static void insertCustomer(UserResponse insertCustomer) {
        ApiInterface apiInterface = Retrofit2.getInstance().getApiInterface();
        Call<APIResponse> call = apiInterface.insertCustomer(insertCustomer);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.isSuccessful()) {
//                    Customer postedCustomer = response.body();
                    Log.d("CallApi", "insertCustomer successfully: " + response.body().getMessage());
                    // Xử lý response ở đây nếu cần (ví dụ: cập nhật UI)
                } else {
                    Log.e("CallApi", "Failed to post customer. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.e("CallApi", "Failed to insertCustomer. Error: " + t.getMessage());
            }
        });
    }

    public static void updateCustomer(UserResponse updateCustomer) {
        Log.i(TAG, updateCustomer.toString());
        ApiInterface apiInterface = Retrofit2.getInstance().getApiInterface();
        Call<APIResponse> call = apiInterface.updateCustomer(updateCustomer);
        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("CallApi", "updateCustomer successfully: " + response.body().getMessage());
                } else {
                    Log.d("CallApi", "updateCustomer fail: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.e("CallApi", "Failed to insertCustomer. Error: " + t.getMessage());
            }
        });
    }

}
