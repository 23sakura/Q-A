package com.example.haruka.rescue_aid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.haruka.rescue_aid.R;

public class TitleActivity extends AppCompatActivity {

    private Button mStartbtn, vRecognizeBtn;
    private Intent mToInterview, mToVRecognition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        mToInterview = new Intent(this, InterviewActivity.class);
        mToVRecognition = new Intent(this, VoiceRecognizeActivity.class);

        mStartbtn = (Button)findViewById(R.id.startbtn);
        mStartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mToInterview);
            }
        });

        vRecognizeBtn = (Button)findViewById(R.id.voiceRecognizeBtn);
        vRecognizeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mToVRecognition.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                mToVRecognition.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplication().getPackageName());
                mToVRecognition.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
                startActivity(mToVRecognition);

            }
        });
    }
}
