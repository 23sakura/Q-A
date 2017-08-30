package com.example.haruka.rescue_aid.activities;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.example.haruka.rescue_aid.R;

/**
 * Created by Tomoya on 8/26/2017 AD.
 */

public class LightTestActivity extends AppCompatActivity {

    private Button lightBtn;
    private boolean isFlashOn;
    private boolean hasFlash;
    private Camera camera;
    private Camera.Parameters parameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        lightBtn = (Button)findViewById(R.id.lightBtn);
        lightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                if (!isFlashOn) {
                    // device doesn't support flash
                    // Show alert message and close the application
                    parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();
                    isFlashOn = true;
                }else{
                    parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                    camera.startPreview();
                    isFlashOn = false;
                }
            }
        });

        camera = Camera.open();
    }

}