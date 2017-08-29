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
    private String[] contents = {"意識はありますか？", "息をしていますか？", "頭からの出血はありますか？", "周りに危険物はありますか？"};
    private int offset = 0;
    private String mResult = "";

    private ArrayList<Question> questions;
    private Question currentQuestion;

    private ArrayList<String>[] dictionary;


    class Question{

        private int index;
        private int yesIndex, noIndex;
        private String question;

        public Question(){
            index = -1;
            yesIndex = -100;
            noIndex = -100;
            question = "This question is invalid";
        }

        public Question(int index, String question, int yesIndex, int noIndex){
            this.index = index;
            this.yesIndex = yesIndex;
            this.noIndex = noIndex;
            this.question = question;
        }

        public int getYesIndex(){
            return yesIndex;
        }

        public int getNoIndex(){
            return noIndex;
        }

        public String getQuestion(){
            return question;
        }
    }

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
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "認識開始", Toast.LENGTH_SHORT).show();
        }

        private void produceQuestion_(ArrayList<String> candidates){
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

            /*
            ArrayList<String> candidates = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            */
        }

        private void produceQuestion(ArrayList<String> candidates){

        }

        @Override
        public void onResults(Bundle results) {
            // TODO Auto-generated method stub
            ArrayList<String> candidates = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            produceQuestion_(candidates);
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
            while ((line = bufferReader.readLine()) != null) {
                // 各行が","で区切られていて4つの項目があるとする
                StringTokenizer st = new StringTokenizer(line, ",");
                String id = st.nextToken();
                if(id == "id") continue;
                int index = Integer.parseInt(id);
                String text = st.nextToken();
                int yesIndex = Integer.parseInt(st.nextToken());
                int noIndex = Integer.parseInt(st.nextToken());
                Question q = new Question(index, text, yesIndex, noIndex);
                questions.add(q);
            }
        } catch (IOException e) {
            Log.i(InterviewActivity.this.getClass().getSimpleName(), e.toString());
            e.printStackTrace();
        }
        
        currentQuestion = questions.get(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        context = this;
        mToQr = new Intent(this, Display_qr.class);
        mBtnYes = (Button) findViewById(R.id.btn_yes);
        mBtnNo = (Button) findViewById(R.id.btn_no);
        mInterviewContent = (TextView) findViewById(R.id.interview);
        mInterviewContent.setText(contents[offset]);


        mBtnYes.setOnClickListener(interAnsBtnListener);
        mBtnNo.setOnClickListener(interAnsBtnListener);

        dictionary = YesClass.getDictionary();

        sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        sr.setRecognitionListener(new SpeechListener());
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        sr.startListening(intent);

        questions = new ArrayList<Question>();
        loadQuestion();
    }
    View.OnClickListener interAnsBtnListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            mResult += Integer.toString(offset+1) + "." + questions.get(offset).getQuestion();
            switch(v.getId()){
                case R.id.btn_yes:
                    mResult += " : YES\n";
                    break;
                case R.id.btn_no:
                    mResult += " : No\n";
                    break;
            }
            if(++offset < questions.size()) {
                mInterviewContent.setText(questions.get(offset).getQuestion());
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
    };



    @Override
    protected void onResume(){
        super.onResume();

        offset = 0;
    }
}
