package com.example.haruka.rescue_aid.views;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haruka.rescue_aid.R;

/**
 * Created by skriulle on 9/22/2017 AD.
 */

public class CallOverlay extends Service {

    private View view;
    private WindowManager windowManager;
    private int dpScale ;
    private static TextView textView;

    public static String text = "hogehoge";

    @Override
    public void onCreate() {
        super.onCreate();

        // dipを取得
        dpScale = (int)getResources().getDisplayMetrics().density;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        windowManager = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity=  Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        view = layoutInflater.inflate(R.layout.service_layer, null);

        // Viewを画面上に追加
        windowManager.addView(view, params);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView = (TextView) ((LinearLayout)view).getChildAt(0);
                textView.setText(text);

            }
        });

        textView = (TextView) ((LinearLayout)view).getChildAt(0);
        textView.setText(text);

        return super.onStartCommand(intent, flags, startId);
    }

    public static void setText(String text){
        CallOverlay.text = text;
        try {
            textView.setText(text);
        } catch (NullPointerException e){
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("debug","onDestroy");
        // Viewを削除
        windowManager.removeView(view);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}