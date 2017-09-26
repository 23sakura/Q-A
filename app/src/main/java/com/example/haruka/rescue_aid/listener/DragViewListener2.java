package com.example.haruka.rescue_aid.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Tomoya on 9/26/2017 AD.
 */

public class DragViewListener2 implements View.OnTouchListener{

    private WindowManager windowManager;
    private View view;
    private WindowManager.LayoutParams params;

    private int oldx;
    private int oldy;

    public DragViewListener2(WindowManager dragView, View view, WindowManager.LayoutParams params) {
        this.windowManager = dragView;
        this.view = view;
        this.params = params;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                params.x += (x-oldx);
                params.y += (y-oldy);

                windowManager.updateViewLayout(view, params);
                //windowManager.removeView(view);
                //windowManager.addView(view, params);
                //int left = dragView.getLeft() + (x - oldx);
                //int top = dragView.getTop() + (y - oldy);
                //dragView.layout(left, top, left + dragView.getWidth(), top + dragView.getHeight());
                //dragView.
                break;
        }

        oldx = x;
        oldy = y;

        //return Math.pow((x-oldx), 2) + Math.pow((y-oldy), 2) > 100;
        //TODO find the way to judge either if the window is moved or just tapped
        return true;
    }



}
