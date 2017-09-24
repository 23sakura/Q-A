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
import com.example.haruka.rescue_aid.listener.DragViewListener;

/**
 * Created by skriulle on 9/22/2017 AD.
 */

public class CallOverlay extends Service {

    private static View view;
    private static WindowManager windowManager;
    private int dpScale ;
    private static TextView textView;
    public static String text = "hogehoge";

    final static String notext = "　　[拡大]　　";
    @Override
    public void onCreate() {
        super.onCreate();
        dpScale = (int)getResources().getDisplayMetrics().density;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        windowManager = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity=  Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        view = layoutInflater.inflate(R.layout.service_layer, null);

        // Viewを画面上に追加
        windowManager.addView(view, params);

        View close = ((LinearLayout)((LinearLayout)(((LinearLayout)view).getChildAt(0))).getChildAt(0)).getChildAt(1);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(view);
                view = null;
            }
        });
        ((LinearLayout) view).getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!textView.getText().equals(notext)){
                    textView.setText(notext);
                } else {
                    textView.setText(text);
                }
            }
        });
        DragViewListener dragViewListener = new DragViewListener(((LinearLayout) view).getChildAt(0));
        ((LinearLayout) view).getChildAt(0).setOnTouchListener(dragViewListener);

        textView = (TextView) (((LinearLayout)(((LinearLayout)view).getChildAt(0))).getChildAt(1));
        textView.setText(text);

        return super.onStartCommand(intent, flags, startId);
    }



    public static void setText(String text){
        CallOverlay.text = text;
        try {
            textView.setText(text);
        } catch (NullPointerException e){
            Log.e("CallOverlay", e.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("debug","onDestroy");
        windowManager.removeView(view);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public static void removeCallOver() {
        try {
            if (windowManager != null && view != null) {
                windowManager.removeView(view);
                view = null;
            }
        } catch (Exception e) {

        }
    }
}