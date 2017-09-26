package com.example.haruka.rescue_aid.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.ExplainCare;
import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.example.haruka.rescue_aid.utils.Record;
import com.example.haruka.rescue_aid.utils.Utils;

import java.util.ArrayList;

import jp.fsoriented.cactusmetronome.lib.Click;
import jp.fsoriented.cactusmetronome.lib.DefaultHighClickCallback;
import jp.fsoriented.cactusmetronome.lib.Metronome;

/**
 * Created by Tomoya on 9/2/2017 AD.
 * This is a main explain activity to help care.
 * It shows text, image and read it.
 * It has function to play metronome to keep tempo when you go on a chest compression care.
 */

public class ExplainActivity extends ReadAloudTestActivity {

    MedicalCertification medicalCertification;

    Metronome mMetronome;
    //Button explainButton;
    //Button finishButton;
    Button finishBtn, aedBtn;
    ImageView  forwardBtn, backBtn;
    ExplainCare mainEmergencyExplanation, subEmergencyExplanation;

    TextView textView;
    ImageView imageView;

    int explainIndex;
    Handler _handler;
    boolean useSwitchTimer;


    private static class BpmUtil {
        public static int getSampleLength(double bpm) {
            // 1beatあたりの長さ（care_sample）
            return (int)(60 * Metronome.FREQUENCY / bpm);
        }
    }

    private static enum NoteEnum {
        // basic notes
        BASIC_4(new double[]{0}, 1.0/8),
        BASIC_8(new double[]{0, 0.5}, 1.0/8);


        private final double[] beats;
        private final double length;

        private NoteEnum(double[] beats, double length) {
            this.beats = beats;
            this.length = length;
        }

        /**
         *
         * @param destination
         * @param lengthOfQuarter
         * @param index
         */
        public void addNewClicks(ArrayList<Click> destination, int lengthOfQuarter, int index) {
            for (int i=0; i<beats.length; i++) {
                double beat = beats[i];
                int when = (int)(beat * lengthOfQuarter) + lengthOfQuarter * index;
                int len = (int)(length * lengthOfQuarter);
                Click c;
                if (index == 0 && i == 0) {
                    c = new Click(when, len, new DefaultHighClickCallback());
                } else {
                    c = new Click(when, len);
                }
                destination.add(c);
            }
        }
    }

    public void startMetronome() {
        stopMetronome();

        int tempo = 100;

        ArrayList<Click> list = new ArrayList<Click>();
        int samples = BpmUtil.getSampleLength(tempo);
        int beatsPerMeasure = 4;
        NoteEnum note = NoteEnum.BASIC_4;
        for (int i=0; i<beatsPerMeasure ; i++) {
            note.addNewClicks(list, samples, i);
        }

        mMetronome.start();
        mMetronome.setPattern(list, samples * beatsPerMeasure);
    }

    public void stopMetronome() {
        try{
            mMetronome.finish();
        }catch (Exception e){
            Log.i("stop metronome", e.toString());
        }

    }

    void setTextView(String text){
        textView.setText(text);
        speechText(text);
    }

    void nextExplanation(){
        explainIndex = (explainIndex+1) % mainEmergencyExplanation.numSituation;
        setExplain(explainIndex);

        _handler.removeCallbacksAndMessages(null);
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextExplanation();
            }
        }, mainEmergencyExplanation.getDuration(explainIndex));
    }

    void previousExplanation(){
        explainIndex = (explainIndex+mainEmergencyExplanation.numSituation-1) % mainEmergencyExplanation.numSituation;
        setExplain(explainIndex);

        _handler.removeCallbacksAndMessages(null);
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextExplanation();
            }
        }, mainEmergencyExplanation.getDuration(explainIndex));

    }

    public void setExplain(int index){
        setTextView(mainEmergencyExplanation.getText(index));
        imageView.setImageDrawable(mainEmergencyExplanation.getImage(index));
        //explainButton.setText(mainEmergencyExplanation.getButtonText(index));
        //finishButton.setText(mainEmergencyExplanation.getButton2Text(index));
    }

    void mainExplain(){

        //medicalCertification.addRecord(new Record("Care", mainEmergencyExplanation.name));
        Log.d("Care", Integer.toString(mainEmergencyExplanation.id_));
        Record r = new Record("Care", Integer.toString(mainEmergencyExplanation.id_));
        medicalCertification.addRecord(r);

        _handler.removeCallbacksAndMessages(null);
        stopMetronome();
        if (mainEmergencyExplanation.isMetronomeRequired){
            startMetronome();
        }else{
            stopMetronome();
        }

        explainIndex = 0;
        setExplain(explainIndex);

        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (useSwitchTimer) {
                    nextExplanation();
                }
            }
        }, mainEmergencyExplanation.getDuration(0));

    }

    void setSubExplanation(int index){
        setTextView(subEmergencyExplanation.getText(index));
        imageView.setImageDrawable(subEmergencyExplanation.getImage(index));
        //explainButton.setText(subEmergencyExplanation.getButtonText(index));
        //finishButton.setText(subEmergencyExplanation.getButton2Text(index));
    }

    void subExplaination(){
        medicalCertification.addRecord(new Record(Utils.TAG_CARE, subEmergencyExplanation.name));

        _handler.removeCallbacksAndMessages(null);
        stopMetronome();
        if (subEmergencyExplanation.isMetronomeRequired){
            startMetronome();
        }


        aedBtn.setText("胸骨圧迫を\n再開する");
        setSubExplanation(0);
        aedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subExplaination();
                    }
                });
                mainExplain();
                aedBtn.setText("AEDが到着した");
                //nextExplanation();
            }
        });

    }

    void finishRescue(){
        stopMetronome();
        _handler.removeCallbacksAndMessages(null);
        ExplainCare recover = new ExplainCare(this, "care_recovery_position");

        setTextView(recover.getText(0));
        imageView.setImageDrawable(recover.getImage(0));
        //explainButton.setText(recover.getButtonText(0));
        //finishButton.setText(recover.getButton2Text(0));

        final Intent QRIntent = new Intent(this, QRDisplayActivity.class);
        QRIntent.putExtra("THROUGH_INTERVIEW", true);
        QRIntent.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
        /*
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                medicalCertification.addRecord(new Record(Utils.TAG_END, "_"));
                Log.d("RESULT", medicalCertification.toString());

                startActivity(QRIntent);
                finish();
            }
        });
        explainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RESULT", medicalCertification.toString());

                startActivity(QRIntent);
                finish();
            }
        });
        */
    }


    public void onStopClick(View view) {
        stopMetronome();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            medicalCertification = (MedicalCertification) getIntent().getSerializableExtra("CERTIFICATION");
        }catch (Exception e){
            Log.e("ExplainActivity", e.toString());
        }
        if(medicalCertification == null){
            Log.i("medical certification", "is null");
            medicalCertification = new MedicalCertification();
        }
        String careXML = getIntent().getStringExtra("CARE_XML");

        if(!careXML.equals("care_chest_compression")) {
            setContentView(R.layout.activity_explain);
            textView = (TextView) findViewById(R.id.textview_explain_heart_massage);
            imageView = (ImageView) findViewById(R.id.imageview_explain_heart_massage);
            finishBtn = (Button) findViewById(R.id.btn_explain_finish);
            backBtn = (ImageView) findViewById(R.id.btn_explain_back);
            forwardBtn = (ImageView) findViewById(R.id.btn_explain_next);
        } else {
            setContentView(R.layout.activity_explain_2);
            textView = (TextView) findViewById(R.id.textview_explain_heart_massage2);
            imageView = (ImageView) findViewById(R.id.imageview_explain_heart_massage2);
            finishBtn = (Button) findViewById(R.id.btn_explain_finish2);
            backBtn = (ImageView) findViewById(R.id.btn_explain_back2);
            forwardBtn = (ImageView) findViewById(R.id.btn_explain_next2);
            aedBtn = (Button)findViewById(R.id.btn_explain_aed);
            aedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subExplaination();
                }
            });
        }
        /*
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (!useSwitchTimer) {
                    nextExplanation();
                }
            }
        });
        */

        //linearLayout.removeAllViews();

        //explainButton = (Button) findViewById(R.id.btn_explain);
        //explainButton.setText("");
        //finishButton = (Button)findViewById(R.id.btn_finish);
        /*
        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finishRescue();
                medicalCertification.showRecords();
            }
        });
        */
        //finishButton.setText("");
        /*
        finishBtn = new Button(this);
        finishBtn.setText("hogehoge");
        linearLayout.addView(finishBtn);
        backBtn = new Button(this);
        backBtn.setText("◁");
        linearLayout.addView(backBtn);
        forwardBtn = new Button(this);
        forwardBtn.setText("▷");
        linearLayout.addView(forwardBtn);
        */

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ExplainActivity.this).setMessage("応急手当を終了しますか").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousExplanation();
            }
        });
        forwardBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                nextExplanation();
            }
        });

        mMetronome = new Metronome();
        _handler = new Handler();

        mainEmergencyExplanation = new ExplainCare(this, careXML);
        //mainEmergencyExplanation = new ExplainCare(this, "care_bleed_stopping");

        if (mainEmergencyExplanation.sub.equals("")) {
            subEmergencyExplanation = null;
        } else {
            subEmergencyExplanation = new ExplainCare(this, mainEmergencyExplanation.sub);
            if (!subEmergencyExplanation.isActive) {
                subEmergencyExplanation = null;
            }
        }

        mainExplain();
        useSwitchTimer = true;

        medicalCertification.save(this);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(ExplainActivity.this).setMessage("応急手当を終了しますか").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).show();
            return true;
        }
        return false;
    }


    @Override
    protected void onPause(){
        super.onPause();

        stopMetronome();
        _handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume(){
        super.onResume();

        /*
        explainIndex = 0;
        setExplain(explainIndex);


        _handler.removeCallbacksAndMessages(null);
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextExplanation();
            }
        }, explainTime[explainIndex]);
        */
    }



}
