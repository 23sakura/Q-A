package com.example.haruka.rescue_aid.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.ExplainCare;

import java.util.ArrayList;

import jp.fsoriented.cactusmetronome.lib.Click;
import jp.fsoriented.cactusmetronome.lib.DefaultHighClickCallback;
import jp.fsoriented.cactusmetronome.lib.Metronome;

/**
 * Created by Tomoya on 9/1/2017 AD.
 */

public class MetronomeTestActivity  extends AppCompatActivity {

    Metronome mMetronome;
    Button metronomeButton;
    ExplainCare emergencyExplanation;

    TextView textView;
    ImageView imageView;

    int explainIndex;
    Handler _handler;
    int[] explainTime;


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

    public void onStartClick(View view) {
        mMetronome.finish();

        int tempo = 100;

        // 再生するクリック音のリストを作成する
        ArrayList<Click> list = new ArrayList<Click>();
        int samples = BpmUtil.getSampleLength(tempo);
        int beatsPerMeasure = 4;
        NoteEnum note = NoteEnum.BASIC_4;
        for (int i=0; i<beatsPerMeasure ; i++) {
            note.addNewClicks(list, samples, i);
        }

        // 再生する
        mMetronome.start();
        mMetronome.setPattern(list, samples * beatsPerMeasure);

        nextExplanation();
    }

    void nextExplanation(){
        explainIndex = (explainIndex+1) % 2;
        setExplain(explainIndex);
        _handler.removeCallbacksAndMessages(null);
        _handler = new Handler();
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextExplanation();
            }
        }, explainTime[explainIndex]);

    }

    public void onStopClick(View view) {
        mMetronome.finish();
    }

    public void setExplain(int id){
        textView.setText(emergencyExplanation.getText(id));
        imageView.setImageDrawable(emergencyExplanation.getImage(id));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);

        emergencyExplanation = new ExplainCare(this, "care_chest_compression");
        explainTime = new int[emergencyExplanation.getProcesses()];
        explainTime[0] = 1000;
        explainTime[1] = 3000;
        textView = (TextView)findViewById(R.id.textview_explain_heart_massage);
        imageView = (ImageView)findViewById(R.id.imageview_explain_heart_massage);
        explainIndex = 0;
        setExplain(explainIndex);
        _handler = new Handler();
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextExplanation();
            }
        }, explainTime[explainIndex]);

        mMetronome = new Metronome();
        metronomeButton = (Button)findViewById(R.id.btn_metronome);
        metronomeButton.setText("AEDが到着した");
        metronomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();

        mMetronome.finish();
    }

    @Override
    protected void onResume(){
        super.onResume();

        explainIndex = 0;
        setExplain(explainIndex);
        _handler = new Handler();
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextExplanation();
            }
        }, explainTime[explainIndex]);
    }



}
