package com.example.haruka.rescue_aid.activities;

import android.Manifest;
import android.graphics.Bitmap;
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

import java.util.List;

/**
 * Created by Tomoya on 8/24/2017 AD.
 */

public class CardiotachometerActivity extends AppCompatActivity {
    private Camera myCamera;
    private TextView cameraTestView;
    private SurfaceView mySurfaceView;
    private Bitmap bitmap;
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
            List<Camera.Size> supported = parameters.getSupportedPreviewSizes();
            if (supported != null){
                for (Camera.Size size : supported){
                    if(size.width <= width && size.height<= height){
                        parameters.setPreviewSize(size.width, size.height);
                        myCamera.setParameters(parameters);
                    }
                }
            }
            myCamera.startPreview();

            bitmap = mySurfaceView.getDrawingCache();


        }


    };
/*
    Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            int previewWidth = camera.getParameters().getPreviewSize().width;
            int previewHeight = camera.getParameters().getPreviewSize().height;

            Bitmap bmp = getBitmapImageFromYUV(data, previewWidth, previewHeight);
        }

        public Bitmap getBitmapImageFromYUV(byte[] data, int width, int height) {
            cameraTestView.setText(Byte.toString(data[0]) + " : " + Integer.toString(data.length) + " : " + Integer.toString(width) + " : " + Integer.toString(height));

            YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, width, height, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            yuvimage.compressToJpeg(new Rect(0, 0, width, height), 80, baos);
            byte[] jdata = baos.toByteArray();
            BitmapFactory.Options bitmapFatoryOptions = new BitmapFactory.Options();
            bitmapFatoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length, bitmapFatoryOptions);
            return bmp;
        }
    };
*/



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
