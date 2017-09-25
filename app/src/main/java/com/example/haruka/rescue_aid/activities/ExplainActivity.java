package com.example.haruka.rescue_aid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
 */

public class ExplainActivity extends ReadAloudTestActivity {

    MedicalCertification medicalCertification;

    Metronome mMetronome;
    Button explainButton;
    Button finishButton;
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

        // 4分音符の長さを0..1としたときに、クリックがどこにあるかを表す
        // Clickを作るもとになる

        /** いつ発音するか */
        private final double[] beats;
        /** 音の長さ */
        private final double length;

        /** コンストラクタ */
        private NoteEnum(double[] beats, double length) {
            this.beats = beats;
            this.length = length;
        }

        /**
         * 指定されたリストに、この{@code NoteEnum}が表す音を追加する。
         *
         * @param destination Clickの書き込み先
         * @param lengthOfQuarter 4分音符の長さ（サンプル）
         * @param index 何個目の4分音符か。0はじまり。
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
                if (useSwitchTimer) {
                    nextExplanation();
                }
            }
        }, mainEmergencyExplanation.getDuration(explainIndex));

    }

    public void setExplain(int index){
        setTextView(mainEmergencyExplanation.getText(index));
        imageView.setImageDrawable(mainEmergencyExplanation.getImage(index));
        explainButton.setText(mainEmergencyExplanation.getButtonText(index));
        finishButton.setText(mainEmergencyExplanation.getButton2Text(index));
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

        explainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sub medical care", (subEmergencyExplanation != null) ? "exist" : "null");
                if (subEmergencyExplanation != null){
                    subExplaination();
                }else{
                    //TODO implement behavior when there is no sub explain.
                    //finish operation
                }
            }
        });


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
        explainButton.setText(subEmergencyExplanation.getButtonText(index));
        finishButton.setText(subEmergencyExplanation.getButton2Text(index));
    }

    void subExplaination(){
        medicalCertification.addRecord(new Record(Utils.TAG_CARE, subEmergencyExplanation.name));

        _handler.removeCallbacksAndMessages(null);
        stopMetronome();
        if (subEmergencyExplanation.isMetronomeRequired){
            startMetronome();
        }


        setSubExplanation(0);

        explainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subExplaination();
                    }
                });
                mainExplain();
                nextExplanation();
            }
        });
    }

    void finishRescue(){
        stopMetronome();
        _handler.removeCallbacksAndMessages(null);
        ExplainCare recover = new ExplainCare(this, "care_recovery_position");

        setTextView(recover.getText(0));
        imageView.setImageDrawable(recover.getImage(0));
        explainButton.setText(recover.getButtonText(0));
        finishButton.setText(recover.getButton2Text(0));

        final Intent QRIntent = new Intent(this, QRDisplayActivity.class);
        QRIntent.putExtra("THROUGH_INTERVIEW", true);
        QRIntent.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
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
    }


    public void onStopClick(View view) {
        stopMetronome();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);

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

        textView = (TextView) findViewById(R.id.textview_explain_heart_massage);
        imageView = (ImageView) findViewById(R.id.imageview_explain_heart_massage);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (!useSwitchTimer) {
                    nextExplanation();
                }
            }
        });
        explainButton = (Button) findViewById(R.id.btn_explain);
        explainButton.setText("");
        finishButton = (Button)findViewById(R.id.btn_finish);
        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finishRescue();
                medicalCertification.showRecords();
            }
        });
        finishButton.setText("");
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
