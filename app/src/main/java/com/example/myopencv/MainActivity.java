package com.example.myopencv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.icu.text.LocaleDisplayNames;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import android.widget.ListView;
import android.widget.TextView;
import com.example.myopencv.model.camera.ImageUtils;
import com.example.myopencv.model.minio.MinioHelper;
import com.example.myopencv.model.minio.MinioUploader;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements ImageReader.OnImageAvailableListener,
                                                                View.OnClickListener{

    String TAG = "TuanNA";


    //minio
    String mFilename = null;
    MinioHelper mMinioHelper = null;

    private int sensorOrientation;

    CameraConnectionFragment fragment;

    Button btnCapture, btnStart, btnStop, btnPushMinIO;
    Spinner spinnerAngle;
    TextView tvStatus;

    ArrayAdapter<String> adapter_angle;
    String[] angle = {"0", "90", "180", "270"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permission
        reqPermission();



        //init view
        initView();


        //set fragment
        setFragment();

        //init variable
        initVar();

    }

    void initVar(){
        mMinioHelper = new MinioHelper();
    }
    void initView(){
        btnCapture = findViewById(R.id.btnCapture);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnPushMinIO = findViewById(R.id.btnPushMinio);

        btnCapture.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnPushMinIO.setOnClickListener(this);


        //spinner
        spinnerAngle = findViewById(R.id.spnAngle);
        adapter_angle = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, angle);
        adapter_angle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAngle.setAdapter(adapter_angle);
        spinnerAngle.setSelection(3);


        //status
        tvStatus = findViewById(R.id.cameraStatus);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPushMinio:
                boolean bUpload = MinioHelper.uploadImage(mFilename);
                tvStatus.setText("upload to minio = " + bUpload);
                break;

                case R.id.btnStart:
                fragment.openCamera2();
                tvStatus.setText("play camera");
                break;

            case R.id.btnStop:
                fragment.closeCamera2();
                tvStatus.setText("close camera");
                break;
            case R.id.btnCapture:
                int angle = Integer.parseInt(spinnerAngle.getSelectedItem().toString());
                Log.i(TAG, "getSelectedItem = " + angle);
                if (rgbFrameBitmap != null) {
                    saveBitmap(rotateBitmap(rgbFrameBitmap,angle));
                } else {
                    Toast.makeText(getApplicationContext(),
                            "No image available yet",
                            Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }


    private Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }



    //Permission listener
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            //TODO show live camera footage

        }
        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };


    //TODO fragment which show llive footage from camera
    int previewHeight = 0,previewWidth = 0;
    protected void setFragment() {

        //get camera id from camera manager
        final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null;
        try {
            for (String cam:  manager.getCameraIdList()
                 ) {
                Log.d(TAG, "camera-id = " + cam);
            }

            cameraId = manager.getCameraIdList()[1];
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        //prepare fragment
        CameraConnectionFragment camera2Fragment =
                CameraConnectionFragment.newInstance(
                        new CameraConnectionFragment.ConnectionCallback() {
                            @Override
                            public void onPreviewSizeChosen(final Size size, final int rotation) {
                                previewHeight = size.getHeight();
                                previewWidth = size.getWidth();
                                sensorOrientation = rotation - getScreenOrientation();
                                Log.d("tryOrientation","rotation: "+rotation+
                                        "   orientation: "+getScreenOrientation()+"  "
                                        +previewWidth+"   "+previewHeight +
                                        "sensor Orientation = " + sensorOrientation);
                            }
                        },
                        this,
                        R.layout.camera_fragment,
                        //new Size(640, 480));
                        //new Size(1280, 720));
                        new Size(720, 1280));
                        //new Size(1920, 1080));

        camera2Fragment.setCamera(cameraId);

        fragment = camera2Fragment;
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
    //TODO getting frames of live camera footage and passing them to model
    private boolean isProcessingFrame = false;
    private byte[][] yuvBytes = new byte[3][];
    private int[] rgbBytes = null;
    private int yRowStride;
    private Runnable postInferenceCallback;
    private Runnable imageConverter;
    private Bitmap rgbFrameBitmap;
    @Override
    public void onImageAvailable(ImageReader reader) {
        // We need wait until we have some size from onPreviewSizeChosen
        if (previewWidth == 0 || previewHeight == 0) {
            return;
        }
        if (rgbBytes == null) {
            rgbBytes = new int[previewWidth * previewHeight];
        }
        try {
            final Image image = reader.acquireLatestImage();

            if (image == null) {
                return;
            }

            if (isProcessingFrame) {
                image.close();
                return;
            }
            isProcessingFrame = true;
            final Image.Plane[] planes = image.getPlanes();
            fillBytes(planes, yuvBytes);
            yRowStride = planes[0].getRowStride();
            final int uvRowStride = planes[1].getRowStride();
            final int uvPixelStride = planes[1].getPixelStride();

            imageConverter =
                    new Runnable() {
                        @Override
                        public void run() {
                            ImageUtils.convertYUV420ToARGB8888(
                                    yuvBytes[0],
                                    yuvBytes[1],
                                    yuvBytes[2],
                                    previewWidth,
                                    previewHeight,
                                    yRowStride,
                                    uvRowStride,
                                    uvPixelStride,
                                    rgbBytes);
                        }
                    };

            postInferenceCallback =
                    new Runnable() {
                        @Override
                        public void run() {
                            image.close();
                            isProcessingFrame = false;
                        }
                    };

            processImage();

        } catch (final Exception e) {
            Log.e(TAG, e.getMessage());
            return;
        }

    }


    private void processImage() {
        imageConverter.run();
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
        rgbFrameBitmap.setPixels(rgbBytes, 0, previewWidth, 0, 0, previewWidth, previewHeight);
        postInferenceCallback.run();
    }

    protected void fillBytes(final Image.Plane[] planes, final byte[][] yuvBytes) {
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (int i = 0; i < planes.length; ++i) {
            final ByteBuffer buffer = planes[i].getBuffer();
            if (yuvBytes[i] == null) {
                yuvBytes[i] = new byte[buffer.capacity()];
            }
            buffer.get(yuvBytes[i]);
        }
    }

    protected int getScreenOrientation() {
        switch (getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_90:
                return 90;
            default:
                return 0;
        }
    }


    private boolean saveBitmap(Bitmap bitmap) {
        File dir = new File(getExternalFilesDir(null), "Pictures");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filename = "IMG_" + System.currentTimeMillis() + ".jpg";
        File file = new File(dir, filename);

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            mFilename = file.getAbsolutePath();
            Toast.makeText(this, "Saved: " + mFilename, Toast.LENGTH_LONG).show();
            Log.d(TAG, "Saved image to: " + mFilename);
        } catch (IOException e) {
            Log.e(TAG, "Failed to save image: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    void reqPermission(){
        //Require permission
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET
                )
                .check();
    }

}