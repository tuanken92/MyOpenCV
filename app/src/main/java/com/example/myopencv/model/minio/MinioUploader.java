package com.example.myopencv.model.minio;

import android.util.Base64;
import android.util.Log;

import java.io.File;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MinioUploader {
//    private static final String ENDPOINT = "http://192.168.1.225:9000";
//    private static final String ACCESS_KEY = "q5PBhI17XVkfvuAyXIEu";
//    private static final String SECRET_KEY = "IwtMQAIxyrTepXtAUwkaRDpSypmF7D7WNycHGXP6";
//    private static final String BUCKET_NAME = "my-bucket";

    private static final String ENDPOINT = "http://192.168.3.69:9000";
    private static final String ACCESS_KEY = "CLR1bkuA5m9VT5DQtBms";
    private static final String SECRET_KEY = "yM1yfUIbTxeFcdXzx8gCf6xFHNhWbWpJ1lPVax8w";
    private static final String BUCKET_NAME = "image";

    public static void uploadImage(File imageFile, String objectName) {
        if (!imageFile.exists()) {
            Log.e("MinioUploader", "File does not exist: " + imageFile.getAbsolutePath());
            return;
        }
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("image/jpeg");

        RequestBody body = RequestBody.create(mediaType, imageFile);

        // PUT URL format: http://endpoint/bucket-name/object-name
        String url = ENDPOINT + "/" + BUCKET_NAME + "/" + objectName;

        // Basic Auth header
        String credentials = ACCESS_KEY + ":" + SECRET_KEY;
        String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "image/jpeg")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, java.io.IOException e) {
                Log.e("MinioUploader", "Upload failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws java.io.IOException {
                if (response.isSuccessful()) {
                    Log.i("MinioUploader", "Upload successful");
                } else {
                    Log.e("MinioUploader", "Upload failed: " + response.code() + " - " + response.message());
                }
            }
        });
    }

    public static boolean uploadImage(String fileName) {
        try {
            File imageFile = new File(fileName);
            if (!imageFile.exists()) {
                Log.e("MinioUploader", "File does not exist: " + imageFile.getAbsolutePath());
                return false;
            }
            String objectName = imageFile.getName();
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("image/jpeg");

            RequestBody body = RequestBody.create(mediaType, imageFile);

            // PUT URL format: http://endpoint/bucket-name/object-name
            String url = ENDPOINT + "/" + BUCKET_NAME + "/" + objectName;

            // Basic Auth header
            String credentials = ACCESS_KEY + ":" + SECRET_KEY;
            String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .addHeader("Content-Type", "image/jpeg")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, java.io.IOException e) {
                    Log.e("MinioUploader", "Upload failed: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws java.io.IOException {
                    if (response.isSuccessful()) {
                        Log.i("MinioUploader", "Upload successful");
                    } else {
                        Log.e("MinioUploader", "Upload failed: " + response.code() + " - " + response.message());
                    }
                }
            });
        } catch (Exception ex) {
            Log.e("TuanNA", "upload Image faild " + ex.getMessage());
            return false;
        }

        return true;
    }
}
