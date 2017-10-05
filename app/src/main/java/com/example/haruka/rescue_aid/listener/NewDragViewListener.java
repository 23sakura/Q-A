package com.example.haruka.rescue_aid.listener;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Tomoya on 10/5/2017 AD.
 */

public class NewDragViewListener implements View.OnTouchListener{

    private WindowManager windowManager;
    public static View view;
    public static TextView textView;
    public static TableLayout tableLayout;
    private WindowManager.LayoutParams params;
    private LayoutInflater layoutInflater;
    private static Context context;

    static boolean big = true;
    public static String text = "";
    public static ArrayList<String[]> list;
    private int oldx;
    private int oldy;

    Date start;

    public NewDragViewListener(WindowManager dragView, View view, WindowManager.LayoutParams params, LayoutInflater layoutInflater) {
        this.windowManager = dragView;
        this.view = view;
        this.params = params;
        this.layoutInflater = layoutInflater;
    }

    public static void setContext(Context context){
        NewDragViewListener.context = context;
    }

    private void makeBig(){
        windowManager.removeView(view);
        view = layoutInflater.inflate(R.layout.service_layout, null);

        // Viewを画面上に追加
        windowManager.addView(view, params);
        Log.d("LayoutContens", Integer.toString(((LinearLayout)((LinearLayout)((LinearLayout)view).getChildAt(0)).getChildAt(0)).getChildCount()));
        View close = ((LinearLayout)((LinearLayout)(((LinearLayout)view).getChildAt(0))).getChildAt(0)).getChildAt(0);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(view);
                view = null;
            }
        });
        NewDragViewListener dragViewListener = new NewDragViewListener(windowManager, view, params, layoutInflater);
        NewDragViewListener.setContext(context);
        (view).setOnTouchListener(dragViewListener);
        dragViewListener.setText(text);

        tableLayout = (TableLayout) (((LinearLayout)(((LinearLayout)view).getChildAt(0))).getChildAt(1));
        Log.d("LayoutContens", Integer.toString(tableLayout.getChildCount()));
        Log.d("LayoutContenslistsize", Integer.toString(list.size()));

        setTable(list);
        textView = (TextView) (((LinearLayout)(((LinearLayout)view).getChildAt(0))).getChildAt(2));
        textView.setText(text);
    }




    public static void setTable(ArrayList<String[]> list){
        NewDragViewListener.list = list;
        Log.e("errorr", Integer.toString(list.size()));

        try {
            for (String[] question : list) {

                Log.d("errorFinder", "index0");
                TableRow tableRow = new TableRow(context);
                TableRow _tableRow = new TableRow(context);
                Log.d("errorFinder", "index1");
                for (int i = 0; i < 2; i++){
                    String val = question[i];
                    TextView textview = new TextView(context);
                    textview.setMaxWidth(340);
                    textview.setTextSize(15);
                    if (i == 1){
                        Log.d("errorFinder", "index2");
                        if (val.equals(Utils.ANSWER_JP_YES)){
                            textview.setTextColor(context.getResources().getColor(R.color.yes));
                        } else if (val.equals(Utils.ANSWER_JP_NO)){
                        } else {
                            textview.setTextColor(context.getResources().getColor(R.color.unsure));
                        }
                        val = "　" + val;
                        textview.setTextColor(context.getResources().getColor(R.color.no));

                        Log.d("errorFinder", "index3");
                    } else {
                        textview.setTextColor(context.getResources().getColor(R.color.black));
                    }
                    textview.setText(val);

                    tableRow.addView(textview);

                    Log.d("errorFinder", "index4");
                    TextView _textView = new TextView(context);
                    _textView.setText(" ");
                    _textView.setMaxHeight(17);
                    _tableRow.addView(_textView);
                }


                Log.d("errorFinder", "index5 ");
                tableLayout.addView(tableRow);
                tableLayout.addView(_tableRow);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void setText(String text){
        this.text = text;
    }

    private void makeSmall(){
        windowManager.removeView(view);
        view = layoutInflater.inflate(R.layout.service_layer_small, null);
        windowManager.addView(view, params);
        NewDragViewListener dragViewListener = new NewDragViewListener(windowManager, view, params, layoutInflater);
        (view).setOnTouchListener(dragViewListener);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start = new Date();
                break;

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
            case MotionEvent.ACTION_UP:
                Date end = new Date();
                Log.d("timedayodayo", Long.toString(end.getTime() - start.getTime()));
                Log.d("timedayodayo", "jogejogjeo");
                if ((end.getTime() - start.getTime()) < 200){
                    if(big) {
                        makeSmall();
                        Log.d("timedayodayo", "small");
                        big = false;
                    } else {
                        makeBig();
                        Log.d("timedayodayo", "big");
                        big = true;
                    }
                }
        }
        oldx = x;
        oldy = y;

        //return Math.pow((x-oldx), 2) + Math.pow((y-oldy), 2) > 100;
        //TODO find the way to judge either if the window is moved or just tapped
        return true;
    }



}
