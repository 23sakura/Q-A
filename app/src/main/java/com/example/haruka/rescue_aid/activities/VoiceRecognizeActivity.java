package com.example.haruka.rescue_aid.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.recognition_list.ListYesNo;

import java.util.ArrayList;

/**
 * Created by Tomoya on 8/23/2017 AD.
 * This is a test activity for voice recognition
 */

public class VoiceRecognizeActivity extends AppCompatActivity{

    Button button1;
    TextView textView1;
    SpeechRecognizer sr;

    ArrayList<String>[] dictionary;

    class SpeechListener implements RecognitionListener {

        @Override
        public void onBeginningOfSpeech() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onEndOfSpeech() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onError(int error) {
            // TODO Auto-generated method stub
            if(error == 9) {
                //get Permission
                ActivityCompat.requestPermissions(VoiceRecognizeActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
            }else if (error != 7){
                Toast.makeText(getApplicationContext(), "エラー " + error, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            // TODO Auto-generated method stub
            //Toast.makeText(getApplicationContext(), "認識開始", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // TODO Auto-generated method stub


            int yes = 0;
            String recognizedList = "";
            ArrayList<String> candidates = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for(String recognized : candidates) {
                Log.d("onResults", recognized);
                recognizedList += recognized + "\n";
            }

            textView1.setText(recognizedList);

            for(yes = 0; yes < 2; yes++){
                for(int index = 0; index < dictionary[yes].size(); index++){
                    if(dictionary[yes].get(index).equals(candidates.get(0))){
                        Toast.makeText(getApplicationContext(), (yes == 0) ?"Yes":"No" , Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            /*
            ArrayList<String> candidates = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            */
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            // TODO Auto-generated method stub

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicerecognize);

        dictionary = ListYesNo.getDictionary();

        button1 = (Button)findViewById(R.id.vRecognizeBtn);
        textView1 = (TextView)findViewById(R.id.VRresultText);



        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                sr.setRecognitionListener(new SpeechListener());
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                sr.startListening(intent);

            }

        });
    }
}
