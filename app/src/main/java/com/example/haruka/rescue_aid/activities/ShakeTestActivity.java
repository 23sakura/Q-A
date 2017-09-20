package com.example.haruka.rescue_aid.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private void initShake(){
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initShake();

        //mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mPlanetTitles = new String[]{"aaa", "bbb", "ccc", "ddd"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }


    public interface OnShakeListener {
        public void onShake();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public static class PlanetFragment extends Fragment {

        public static final String ARG_PLANET_TEXT = "planet_text";


        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_result, container, false);

            String str = getArguments().getString(ARG_PLANET_TEXT);
            TextView text2 = (TextView)rootView.findViewById(R.id.text2);
            text2.setText(str + " Selected! ");

            //getActivity().setTitle(str);
            return rootView;
        }
    }

    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putString(PlanetFragment.ARG_PLANET_TEXT, mPlanetTitles[position]);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_content, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

}
