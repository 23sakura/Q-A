package com.example.haruka.rescue_aid.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haruka.rescue_aid.controller.HttpPostTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tomoya on 9/30/2017 AD.
 */

public class HTTPPostActivity extends AppCompatActivity {

    public TextView textView;
    public EditText editText;

    // HTTPボタン押下
    public void onBtnHttpClicked(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 大阪の天気予報XMLデータ
                    URL url = new URL("https://boiling-river-76880.herokuapp.com/");
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    String str = InputStreamToString(con.getInputStream());
                    Log.d("HTTP", str);
                } catch(Exception ex) {
                    System.out.println(ex);
                }
            }
        }).start();
    }

    static String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }


    private void startAsync(String str) {
        //HttpResponsAsync testTask = new HttpResponsAsync(this);
        // executeを呼んでAsyncTaskを実行する、パラメータは１番目
        //testTask.text = str;
        //testTask.execute();
        try {
            new HttpPostTask().execute(new URL("https://boiling-river-76880.herokuapp.com/post"));
        } catch (Exception e){

        }
    }

    @Override
    protected void onCreate(final Bundle bundle){
        super.onCreate(bundle);

        LinearLayout linearLayout = new LinearLayout(this);
        final Button button = new Button(this);
        linearLayout.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String text = (String)button.getText();
                //button.setText(text+"n");
                //onBtnHttpClicked(v);
                startAsync("");
                Log.d("response", "done");
            }
        });

        textView = new TextView(this);
        linearLayout.addView(textView);

        editText = new EditText(this);
        editText.setMinimumWidth(100);
        linearLayout.addView(editText);

        setContentView(linearLayout);
    }

}
