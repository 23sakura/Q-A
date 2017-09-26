package com.example.haruka.rescue_aid.listener;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Tomoya on 9/24/2017 AD.
 * This is a listener for CallOverlay
 */

public class DragViewListener implements View.OnTouchListener{

    private View dragView;

    private int oldx;
    private int oldy;

    public DragViewListener(View dragView) {
        this.dragView = dragView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int left = dragView.getLeft() + (x - oldx);
                int top = dragView.getTop() + (y - oldy);
                dragView.layout(left, top, left + dragView.getWidth(), top + dragView.getHeight());
                break;
        }

        oldx = x;
        oldy = y;

        //return Math.pow((x-oldx), 2) + Math.pow((y-oldy), 2) > 100;
        //TODO find the way to judge either if the window is moved or just tapped
        return true;
    }



}
