package com.example.haruka.rescue_aid.activities;

import android.Manifest;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.haruka.rescue_aid.R;

/**
 * Created by Tomoya on 8/24/2017 AD.
 */

public class CardiotachometerActivity extends AppCompatActivity {
    private Camera myCamera;
    private TextView cameraTestView;
    private SurfaceView mySurfaceView;
    private Button shutter;


    private SurfaceHolder.Callback mSurfaceListener = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            myCamera = Camera.open();
            myCamera.setDisplayOrientation(90);
            //myCamera.setPreviewCallback(previewCallback);
            cameraTestView.setText(myCamera.toString());
            try {
                myCamera.setPreviewDisplay(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Camera.Parameters parameters = myCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            myCamera.setParameters(parameters);
            myCamera.startPreview();
        }
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            myCamera.release();
            myCamera = null;
        }
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
            Camera.Parameters parameters = myCamera.getParameters();
            /*
            List<Camera.Size> supported = parameters.getSupportedPreviewSizes();
            if (supported != null){
                for (Camera.Size size : supported){
                    if(size.width <= width && size.height<= height){
                    }
                }
            }
            */

            parameters.setPreviewSize(width, height);
            parameters.setPreviewFormat(ImageFormat.NV21);
            myCamera.setParameters(parameters);
            myCamera.startPreview();

        }


    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardiotachometer);
        ActivityCompat.requestPermissions(CardiotachometerActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
        ActivityCompat.requestPermissions(CardiotachometerActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        cameraTestView = (TextView)findViewById(R.id.cameraTestView);

        mySurfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        SurfaceHolder holder = mySurfaceView.getHolder();
        holder.addCallback(mSurfaceListener);
        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);


        shutter = (Button)findViewById(R.id.shutter);
        shutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

}