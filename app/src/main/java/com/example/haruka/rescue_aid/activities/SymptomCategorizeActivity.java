package com.example.haruka.rescue_aid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.haruka.rescue_aid.R;

/**
 * Created by Tomoya on 9/13/2017 AD.
 */

public class SymptomCategorizeActivity extends AppCompatActivity {

    Button BtnToIll, BtnToInjury;
    Intent interviewIntent;

    final String scenarioIll = "scenario.csv";
    final String scenarioInjury = "text4.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_categorize);

        interviewIntent = new Intent(this, InterviewActivity.class);

        BtnToIll = (Button)findViewById(R.id.btn_to_ill);
        BtnToIll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO go to ill interview
                interviewIntent.putExtra("SCENARIO", scenarioIll);
                startActivity(interviewIntent);
            }
        });
        BtnToInjury = (Button)findViewById(R.id.btn_to_injury);
        BtnToInjury.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO go to injury interview
                interviewIntent.putExtra("SCENARIO", scenarioInjury);
                startActivity(interviewIntent);
            }
        });
    }
}
