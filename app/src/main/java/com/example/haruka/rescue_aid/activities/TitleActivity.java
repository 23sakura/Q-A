package com.example.haruka.rescue_aid.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.haruka.rescue_aid.R;

public class TitleActivity extends AppCompatActivity {

    private Button gotoInterviewBtn, gotoTestBtn, gotoCareBtn, historyBtn;
    private Intent interviewIntent, testIntent, qrIntent, careIntent;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        context = this;

        //interviewIntent = new Intent(this, InterviewActivity.class);
        interviewIntent = new Intent(this, SymptomCategorizeActivity.class);
        testIntent = new Intent(this, TestPlatformActivity.class);
        careIntent = new Intent(this, OptionActivity.class);
        qrIntent = new Intent(this, QRActivity.class);

        gotoInterviewBtn = (Button)findViewById(R.id.startbtn);
        gotoInterviewBtn.setTextColor(getResources().getColor(R.color.start));
        gotoInterviewBtn.setBackgroundColor(getResources().getColor(R.color.start_back));
        gotoInterviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(interviewIntent);
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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_title, menu);
        return true;
    }

    static String TAG = "Menu";


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_call_119:
                Uri uri = Uri.parse("tel:09052793706");
                Intent i = new Intent(Intent.ACTION_DIAL,uri);
                startActivity(i);
                break;
            case R.id.menu_titel_chest_compression:
                final Intent intent = new Intent(this, ExplainActivity.class);
                intent.putExtra("CARE_XML", "care_chest_compression");
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    protected void onStop(){
        super.onStop();
    }
}

