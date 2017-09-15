package com.example.haruka.rescue_aid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.CareList;
import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.example.haruka.rescue_aid.utils.Utils;

/**
 * Created by Tomoya on 9/7/2017 AD.
 */

public class ResultActivity extends AppCompatActivity {

    private MedicalCertification medicalCertification;
    private int level;
    LinearLayout linearLayout;
    ScrollView scrollView;
    LinearLayout inflateLayout;
    TextView textView;
    Button dealingBtn;

    final int MATCH_P = ViewGroup.LayoutParams.MATCH_PARENT;

    CareList careList;

    private void setLinearLayout(){
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        scrollView.addView(linearLayout);
    }

    private void setScrollView(){
        scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ScrollView.LayoutParams(MATCH_P, MATCH_P));
        setContentView(scrollView);
    }

    private void setTextView(){
        textView = new TextView(this);
        textView.setTextSize(50);

        textView.setTextColor(Utils.LEVEL_COLORS[level]);
        textView.setText("死にそうです");
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textLayoutParams);
        linearLayout.addView(textView);
    }

    private void setInflate(){
        inflateLayout = new LinearLayout(this);
        inflateLayout.setOrientation(LinearLayout.VERTICAL);

        inflateLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        inflateLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.inflate_result, inflateLayout);
        linearLayout.addView(inflateLayout);
    }

    private void setDealingBtn(){
        final Intent intent = new Intent(this, ExplainActivity.class);

        dealingBtn = new Button(this);
        dealingBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                intent.putExtra("CERTIFICATION", medicalCertification);
                startActivity(intent);
                finish();
            }
        });
        dealingBtn.setText("応急手当開始");
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dealingBtn.setLayoutParams(buttonLayoutParams);
        linearLayout.addView(dealingBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            medicalCertification = (MedicalCertification) getIntent().getSerializableExtra("CERTIFICATION");
        } catch (Exception e){
            medicalCertification = new MedicalCertification();
        }
        level = getIntent().getIntExtra("DANGEROUS_LEVEL", 1);
        Log.d("DANGEROUS_LEVEL", Integer.toString(level));
        medicalCertification.showRecords("ResultActivity");
        careList = new CareList(this);
        careList.showCareList();


        setScrollView();
        setLinearLayout();
        setTextView();
        setInflate();
        setDealingBtn();

    }

}
