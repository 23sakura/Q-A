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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.listener.NewDragViewListener;
import com.example.haruka.rescue_aid.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Tomoya on 9/22/2017 AD.
 * This is call when you open phone app to call ambulance
 */

public class CallOverlay extends Service {

    private static View view;
    private static WindowManager windowManager;
    private static TableLayout tableLayout;
    private int dpScale ;
    private static TextView textView;
    public static String text = "abc";
    public static ArrayList<String[]> list;
    public String title;
    LayoutInflater layoutInflater;
    WindowManager.LayoutParams params;
    static Context context;

    final static String notext = "　　[拡大]　　";
    @Override
    public void onCreate() {
        super.onCreate();
        dpScale = (int)getResources().getDisplayMetrics().density;
        title = "";
    }

    private void makeSmall(){
        windowManager.removeView(view);
        view = layoutInflater.inflate(R.layout.service_layer_small, null);
        windowManager.addView(view, params);
        NewDragViewListener dragViewListener = new NewDragViewListener(windowManager, view, params, layoutInflater);
        (view).setOnTouchListener(dragViewListener);
    }

    private void makeBig(){
        view = layoutInflater.inflate(R.layout.service_layout, null);

        // Viewを画面上に追加
        windowManager.addView(view, params);
        Log.d("LayoutContens", Integer.toString(((LinearLayout)((LinearLayout)((LinearLayout)view).getChildAt(0)).getChildAt(0)).getChildCount()));
        TextView close = (TextView) ((LinearLayout)((LinearLayout)(((LinearLayout)view).getChildAt(0))).getChildAt(0)).getChildAt(1);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(view);
                view = null;
            }
        });
        NewDragViewListener dragViewListener = new NewDragViewListener(windowManager, view, params, layoutInflater);
        NewDragViewListener.setContext(context);
        NewDragViewListener.list = list;
        (view).setOnTouchListener(dragViewListener);
        dragViewListener.setText(text);

        tableLayout = (TableLayout) (((LinearLayout)(((LinearLayout)view).getChildAt(0))).getChildAt(1));
        Log.d("LayoutContens", Integer.toString(tableLayout.getChildCount()));



        setTable(list);
        textView = (TextView) (((LinearLayout)(((LinearLayout)view).getChildAt(0))).getChildAt(2));
        textView.setText(text);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        layoutInflater = LayoutInflater.from(this);

        windowManager = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity=  Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        makeBig();
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

    public static void setContext(Context context){
        CallOverlay.context = context;
    }

    public static void setTable(ArrayList<String[]> list){
        CallOverlay.list = list;
        NewDragViewListener.list = list;

        try {
            for (String[] question : list) {

                TableRow tableRow = new TableRow(context);
                TableRow _tableRow = new TableRow(context);
                TextView textview = new TextView(context);
                textview.setText("・");
                textview.setTextSize(15);
                tableRow.addView(textview);
                for (int i = 0; i < 2; i++){
                    String val = question[i];
                    textview = new TextView(context);
                    textview.setMaxWidth(340);
                    textview.setTextSize(15);
                    if (i == 1){
                        if (val.equals(Utils.ANSWER_JP_YES)){
                            textview.setTextColor(context.getResources().getColor(R.color.yes));
                        } else if (val.equals(Utils.ANSWER_JP_NO)){
                        } else {
                            textview.setTextColor(context.getResources().getColor(R.color.unsure));
                        }
                        val = "　" + val;
                        textview.setTextColor(context.getResources().getColor(R.color.no));

                    } else {
                        textview.setTextColor(context.getResources().getColor(R.color.black));
                    }
                    textview.setText(val);

                    tableRow.addView(textview);

                    TextView _textView = new TextView(context);
                    _textView.setText(" ");
                    _textView.setMaxHeight(17);
                    _tableRow.addView(_textView);
                }


                tableLayout.addView(tableRow);
                tableLayout.addView(_tableRow);
            }
        } catch (Exception e){
            Log.e("errororrrorrorororrorrr", "error");
            e.printStackTrace();
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
        return null;
    }

    public static void removeCallOver() {
        try {
            if(NewDragViewListener.view != null) {
                windowManager.removeView(NewDragViewListener.view);
            }
            view = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}