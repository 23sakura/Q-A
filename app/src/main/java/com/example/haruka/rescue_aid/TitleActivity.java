package com.example.haruka.rescue_aid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class TitleActivity extends AppCompatActivity {

    private Button mStartbtn;
    private Intent mToInterview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        mToInterview = new Intent(this, InterviewActivity.class);
        mStartbtn = (Button)findViewById(R.id.startbtn);
        mStartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mToInterview);

            }
        });
    }
}
