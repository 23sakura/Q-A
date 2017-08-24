package com.example.haruka.rescue_aid.activities;

import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.haruka.rescue_aid.R;

/**
 * Created by Tomoya on 8/24/2017 AD.
 */

public class ShakeTestActivity extends AppCompatActivity{


    public class ShakeListener implements SensorListener{
        private static final int FORCE_THRESHOLD = 350;
        private static final int TIME_THRESHOLD = 100;
        private static final int SHAKE_TIMEOUT = 500;
        private static final int SHAKE_DURATION = 100;
        private static final int SHAKE_COUNT = 3;

        private SensorManager mSensorManager;
        private float mLastX = -1.0f, mLastY = -1.0f, mLastZ = -1.0f;
        private long mLastTime;
        private OnShakeListener mShakeListener;
        private Context mContext;
        private int mShakeCount = 0;
        private long mLastShake;
        private long mLastForce;


        @Override
        public void onSensorChanged(int sensor, float[] values) {
            if ( sensor != SensorManager.SENSOR_ACCELEROMETER ) return;
            long now = System.currentTimeMillis();
            if ( ( now - mLastForce ) > SHAKE_TIMEOUT ) {
                mShakeCount = 0;
            }
            if ( ( now - mLastTime ) > TIME_THRESHOLD ) {
                long diff = now - mLastTime;
                float speed = Math.abs(values[SensorManager.DATA_X] +
                        values[SensorManager.DATA_Y] +
                        values[SensorManager.DATA_Z] -
                        mLastX - mLastY - mLastZ ) / diff * 10000;

                if ( speed > FORCE_THRESHOLD ) {
                    if ( ( ++mShakeCount >= SHAKE_COUNT ) && now - mLastShake > SHAKE_DURATION ) {
                        mLastShake = now;
                        mShakeCount = 0;
                        if ( mShakeListener != null ) {
                            mShakeListener.onShake();
                        }
                    }
                    mLastForce = now;
                }
                mLastTime = now;
                mLastX = values[SensorManager.DATA_X];
                mLastY = values[SensorManager.DATA_Y];
                mLastZ = values[SensorManager.DATA_Z];
            }
        }

        public void setOnShakeListener ( OnShakeListener listener ) {
            mShakeListener = listener;

        }

        public ShakeListener ( Context context ) {
            mContext = context;
            resume();
        }

        public void resume () {
            mSensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
            if ( mSensorManager == null ) {
                throw new UnsupportedOperationException("Sensor not suported");
            }
            boolean supported = mSensorManager.registerListener(this,
                    SensorManager.SENSOR_ACCELEROMETER,
                    SensorManager.SENSOR_DELAY_GAME);
            if ( !supported ) {
                mSensorManager.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
                throw new UnsupportedOperationException("Acceleroneter not supported");
            }
        }

        public void pause() {
            if ( mSensorManager != null ) {
                mSensorManager.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
                mSensorManager = null;
            }
        }
        @Override
        public void onAccuracyChanged(int sensor, int accuracy) {

        }

    }

    private TextView shakeResult;
    private ShakeListener shakeListener;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        shakeResult = (TextView)findViewById(R.id.shake_result);
        shakeResult.setText("0");

        shakeListener = new ShakeListener(this);
        shakeListener.setOnShakeListener(new OnShakeListener() {
            @Override
            public void onShake() {
                shakeResult.setText(Integer.toString(++counter));

            }
        });

        counter = 0;
    }


    public interface OnShakeListener {
        public void onShake();
    }


}
