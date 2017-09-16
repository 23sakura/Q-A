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
import com.example.haruka.rescue_aid.utils.Care;
import com.example.haruka.rescue_aid.utils.CareList;
import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.example.haruka.rescue_aid.utils.Utils;
import com.example.haruka.rescue_aid.views.ResultLineLayout;

import java.util.ArrayList;

/**
 * Created by Tomoya on 9/7/2017 AD.
 */

public class ResultActivity extends AppCompatActivity {

    private MedicalCertification medicalCertification;
    private int urgency;
    private ArrayList<Care> cares;
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
        LinearLayout ll = (LinearLayout)findViewById(R.id.linearlayout_result);
        scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ScrollView.LayoutParams(MATCH_P, MATCH_P));
        ll.addView(scrollView);
    }

    private void setTextView(){
        textView = (TextView)findViewById(R.id.textview_notice_result); //
        //textView = new TextView(this);
        textView.setTextSize(50);

        textView.setTextColor(Utils.URGENCY_COLORS[urgency]);
        textView.setText("死にそうです");
        /*
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textLayoutParams);
        */
        //linearLayout.addView(textView);
    }

    private void showCareList(){
        for (final Care c : cares){
            ResultLineLayout resultLineLayout = new ResultLineLayout(this);
            resultLineLayout.setTitle(c.name);
            resultLineLayout.setDescription("hogehoge");
            resultLineLayout.setView();
            linearLayout.addView(resultLineLayout);
            android.view.ViewGroup.LayoutParams layoutParams = resultLineLayout.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            resultLineLayout.setLayoutParams(layoutParams);

            resultLineLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int i = c.index;
                    Log.d("result line", Integer.toString(i));
                }
            });
        }

    }

    private void setInflate(){
        inflateLayout = new LinearLayout(this);
        inflateLayout.setOrientation(LinearLayout.VERTICAL);

        inflateLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        inflateLayout.removeAllViews();
        getLayoutInflater().inflate(R.layout.inflate_result_, inflateLayout);
        linearLayout.addView(inflateLayout);

        String t = "";
        for(Care c : cares){
            t += c.name;
        }
        TextView tv = (TextView)findViewById(R.id.textview_inflate);
        tv.setText(t);

    }

    private void setDealingBtn(){
        final Intent intent = new Intent(this, ExplainActivity.class);

        dealingBtn = (Button)findViewById(R.id.btn_start_care); //new Button(this);
        dealingBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                intent.putExtra("CERTIFICATION", medicalCertification);
                startActivity(intent);
                finish();
            }
        });
        dealingBtn.setText("応急手当開始");
        /*
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dealingBtn.setLayoutParams(buttonLayoutParams);
        */
        //linearLayout.addView(dealingBtn);
    }

    public String getCareString(boolean[] care_boolean){
        cares = new ArrayList<>();
        String s = "";
        for (int i = 0; i < care_boolean.length; i++){
            if (care_boolean[i]){
                s += "Y";
                cares.add(CareList.getCare(i));
            } else {
                s += "N";
            }
        }

        return s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        careList = new CareList(this);
        careList.showCareList();

        try {
            medicalCertification = (MedicalCertification) getIntent().getSerializableExtra("CERTIFICATION");
        } catch (Exception e){
            medicalCertification = new MedicalCertification();
        }
        urgency = getIntent().getIntExtra("URGENCY", 1);
        Log.d("URGENCY", Integer.toString(urgency));
        boolean[] _cares = getIntent().getBooleanArrayExtra("CARES");
        Log.d("CARES", getCareString(_cares));
        for (Care c : cares){
            Log.d("care required" , c.name);
        }

        medicalCertification.showRecords("ResultActivity");


        setScrollView();
        setLinearLayout();
        setTextView();
        //setInflate();
        showCareList();
        setDealingBtn();

    }

}
