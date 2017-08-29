package com.example.haruka.rescue_aid.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.recognition_list.YesClass;
import com.example.haruka.rescue_aid.utils.InterviewAnswers;
import com.example.haruka.rescue_aid.utils.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class InterviewActivity extends AppCompatActivity {

    private Context context;
    private Intent mToQr;
    private Button mBtnYes;
    private Button mBtnNo;
    private TextView mInterviewContent;
    private String mResult = "";

    private ArrayList<Question> questions;
    private Question currentQuestion;

    private ArrayList<String>[] dictionary;

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
            if(error == 9) {
                //get Permission
                ActivityCompat.requestPermissions(InterviewActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
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
            Toast.makeText(getApplicationContext(), "認識開始", Toast.LENGTH_SHORT).show();
        }

        private void voiceAnswer(ArrayList<String> candidates){
            int yes = 0;

            for(yes = 0; yes < 2; yes++){
                for(int index = 0; index < dictionary[yes].size(); index++){
                    if(dictionary[yes].get(index).equals(candidates.get(0))){
                        Toast.makeText(getApplicationContext(), (yes == 0) ?"Yes":"No" , Toast.LENGTH_SHORT).show();
                        if(yes == 0){
                            mBtnYes.callOnClick();
                        }else{
                            mBtnNo.callOnClick();
                        }
                        break;
                    }
                }
            }

        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> candidates = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            voiceAnswer(candidates);
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            // TODO Auto-generated method stub

        }

    }

    private SpeechRecognizer sr;

    private void loadQuestion(){
        AssetManager assetManager = this.context.getResources().getAssets();
        try{
            // CSVファイルの読み込み
            InputStream is = assetManager.open("scenario.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line = "";
            line = bufferReader.readLine();
            int _i = 0;
            while ((line = bufferReader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ",");
                Log.d("回数" , Integer.toString(_i));

                _i++;
                String id = st.nextToken();
                if(id == "id") continue;
                int index = Integer.parseInt(id);
                String text = st.nextToken();
                int yesIndex = Integer.parseInt(st.nextToken());
                int noIndex = Integer.parseInt(st.nextToken());
                Question q = new Question(index, text, yesIndex, noIndex);
                questions.add(q);

                Log.d(" 質問" , q.getQuestion());
            }
        } catch (IOException e) {
            Log.i(InterviewActivity.this.getClass().getSimpleName(), e.toString());
            e.printStackTrace();
        }

        currentQuestion = questions.get(0);
    }

    private void produceNextQuestion(int viewID){
        mResult += currentQuestion.getQuestion();
        int nextIndex = 0;
        boolean answer = false;
        switch(viewID){
            case R.id.btn_yes:
                answer = InterviewAnswers.YES;
                break;
            case R.id.btn_no:
                answer = InterviewAnswers.NO;
                break;
        }

        nextIndex = currentQuestion.getNextIndex(answer);
        mResult += String.format(" : %s\n", InterviewAnswers.AnswerToString(answer)) + " : " + InterviewAnswers.AnswerToString(answer) + "\n";

        if(nextIndex >= 0) {
            currentQuestion = questions.get(nextIndex);

            mInterviewContent.setText(currentQuestion.getQuestion());
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
            sr.startListening(intent);
        }else {
            new AlertDialog.Builder(context).setMessage("問診は終了です\nQRコードを表示します").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mToQr.putExtra("RESULT", mResult);
                    startActivity(mToQr);
                }
            }).show();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
        context = this;

        dictionary = YesClass.getDictionary();

        questions = new ArrayList<>();
        loadQuestion();

        mToQr = new Intent(this, Display_qr.class);
        mBtnYes = (Button) findViewById(R.id.btn_yes);
        mBtnNo = (Button) findViewById(R.id.btn_no);
        mBtnYes.setOnClickListener(interAnsBtnListener);
        mBtnNo.setOnClickListener(interAnsBtnListener);
        mInterviewContent = (TextView) findViewById(R.id.interview);
        mInterviewContent.setText(currentQuestion.getQuestion());

        sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        sr.setRecognitionListener(new SpeechListener());
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        sr.startListening(intent);
    }

    View.OnClickListener interAnsBtnListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            produceNextQuestion(v.getId());
        }
    };



    @Override
    protected void onResume(){
        super.onResume();
    }
}
