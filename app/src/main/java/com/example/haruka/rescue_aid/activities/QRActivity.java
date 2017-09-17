package com.example.haruka.rescue_aid.activities;

/**
 * Created by skriulle on 9/16/2017 AD.
 */

import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.List;

public class QRActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView = new SurfaceView(this);
        mSurfaceView.setOnClickListener(onClickListener);
        setContentView(mSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(callback);
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            Camera.Size previewSize = previewSizes.get(0);
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mCamera.release();
            mCamera = null;
        }
    };

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCamera != null) {
                mCamera.autoFocus(autoFocusCallback);
            }
        }
    };

    private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                camera.setOneShotPreviewCallback(previewCallback);
            }
        }
    };

    private void showQR(MedicalCertification medicalCertification){
        final Intent QRIntent = new Intent(this, Display_qr.class);

        QRIntent.putExtra("RESULT", medicalCertification.toString());
        Log.d("RESULT", medicalCertification.toString());
        startActivity(QRIntent);
    }

    private void showCertification(MedicalCertification medicalCertification){
        final Intent QRIntent = new Intent(this, CertificationActivity.class);

        QRIntent.putExtra("CERTIFICATION", medicalCertification);
        Log.d("RESULT", medicalCertification.toString());
        startActivity(QRIntent);

    }

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            int previewWidth = camera.getParameters().getPreviewSize().width;
            int previewHeight = camera.getParameters().getPreviewSize().height;

            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                    data, previewWidth, previewHeight, 0, 0, previewWidth, previewHeight, false);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Reader reader = new MultiFormatReader();
            Result result = null;
            try {
                result = reader.decode(bitmap);
                String text = result.getText();
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                Log.d("QR", text);
                MedicalCertification medicalCertification = new MedicalCertification(text);
                Log.i("medical certification", "read");
                medicalCertification.showRecords();
                //showQR(medicalCertification);
                showCertification(medicalCertification);
            } catch (Exception e) {
                Log.e("QR reader", e.toString());
                Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
            }
        }
    };

}