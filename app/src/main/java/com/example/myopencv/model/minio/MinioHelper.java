package com.example.myopencv.model.minio;

import android.util.Log;
import java.io.File;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import okhttp3.OkHttpClient;

public class MinioHelper {
    private static final String ENDPOINT = "http://192.168.1.225:9000";
    private static final String ACCESS_KEY = "q5PBhI17XVkfvuAyXIEu";
    private static final String SECRET_KEY = "IwtMQAIxyrTepXtAUwkaRDpSypmF7D7WNycHGXP6";
    private static final String BUCKET_NAME = "my-bucket";
    private static MinioClient minioClient;

    public static void uploadImageToMinIO(File fileImage) {
        try {
            // 1. Khởi tạo MinIO client
            minioClient = MinioClient.builder()
                    .endpoint(ENDPOINT)
                    .credentials(ACCESS_KEY, SECRET_KEY)
                    .httpClient(new OkHttpClient())
                    .build();


            // 2. Tên bucket & tên object
            String bucketName = BUCKET_NAME;
            String objectName = fileImage.getName();

            // 3. Upload file
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(fileImage.getAbsolutePath())
                            .build()
            );
            Log.d("MinIO", "✅ Upload thành công: " + objectName);
        } catch (Exception e) {
            Log.e("MinIO", "❌ Lỗi khi upload: " + e.getMessage(), e);
        }
    }
    public static String TAG = "MinIO";

    public static boolean uploadImage(String fileName) {
        try {

            // 0. Set XML parser factories explicitly for Android
            System.setProperty("javax.xml.stream.XMLInputFactory", "com.ctc.wstx.stax.WstxInputFactory");
            System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
            System.setProperty("javax.xml.stream.XMLEventFactory", "com.ctc.wstx.stax.WstxEventFactory");


            // 1. Khởi tạo MinIO client
            minioClient = MinioClient.builder()
                    .endpoint(ENDPOINT)
                    .credentials(ACCESS_KEY, SECRET_KEY)
                    .httpClient(new OkHttpClient())
                    .build();

            File fileImage = new File(fileName);

            // 2. Tên bucket & tên object
            String bucketName = BUCKET_NAME;
            String objectName = fileImage.getName();
            // Optional: ensure bucket exists
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                Log.d(TAG, "Create bucket name = " + bucketName);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }else {
                Log.d(TAG, "OK <= bucket name = " + bucketName);
            }


            // 3. Upload file
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .filename(fileImage.getAbsolutePath())
                            .build()
            );
            Log.d("MinIO", "✅ Upload thành công: " + objectName);
        } catch (Exception e) {
            Log.e("MinIO", "❌ Lỗi khi upload: " + e.getMessage(), e);
            return false;
        }
        return true;
    }

}
