package com.example.haruka.rescue_aid.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.haruka.rescue_aid.R;


/**
 * This is a title activity
 */
public class TitleActivity extends ReadAloudTestActivity {

    private Button gotoInterviewBtn, gotoTestBtn, gotoCareBtn, historyBtn;
    private Intent interviewIntent, testIntent, qrIntent, careIntent;
    Handler _handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        //interviewIntent = new Intent(this, InterviewActivity.class);
        interviewIntent = new Intent(this, SymptomCategorizeActivity.class);
        testIntent = new Intent(this, TestPlatformActivity.class);
        careIntent = new Intent(this, CareChooseActivity.class);
        qrIntent = new Intent(this, QRActivity.class);

        gotoInterviewBtn = (Button)findViewById(R.id.startbtn);
        gotoInterviewBtn.setTextColor(getResources().getColor(R.color.start));
        gotoInterviewBtn.setBackgroundColor(getResources().getColor(R.color.start_back));
        gotoInterviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "問診、手当を行う前に、身の周りの安全を確保してください";
                speechText(text);
                _handler = new Handler();
                final AlertDialog alertDialog = new AlertDialog.Builder(TitleActivity.this)
                        .setTitle("身の安全を確保")
                        .setMessage(text)
                        .setNegativeButton("次へ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(interviewIntent);
                                tts.stop();
                            }
                        })
                        .setIcon(getResources().getDrawable(R.drawable.attention))
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                tts.stop();
                                _handler.removeCallbacksAndMessages(null);
                            }
                        })
                        .show();


                _handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(interviewIntent);
                        tts.stop();
                        alertDialog.cancel();
                    }
                }, 10000);
            }
        });

        gotoCareBtn = (Button)findViewById(R.id.btn_title_care);
        gotoCareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(careIntent);
            }
        });

        gotoTestBtn = (Button)findViewById(R.id.btn_title_qr);
        gotoTestBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(qrIntent);
            }
        });

        final Intent intent = new Intent(this, TestPlatformActivity.class);
        historyBtn =(Button) findViewById(R.id.btn_title_history);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        medicalCertification = null;
    }

    @Override
    protected void onResume(){
        super.onResume();
    }


    @Override
    protected void onStop(){
        super.onStop();
        try {
            if (_handler != null){
                _handler.removeCallbacksAndMessages(null);
            }
        } catch (NullPointerException e){

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(TitleActivity.this)
                    .setTitle("終了")
                    .setMessage("救&援を終了しますか")
                    .setNegativeButton("はい", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAndRemoveTask();
                        }
                    })
                    .setPositiveButton("いいえ", null)
                    .show();

            return true;
        }
        return false;
    }

}

