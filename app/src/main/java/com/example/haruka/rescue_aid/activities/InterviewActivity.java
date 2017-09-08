package com.example.haruka.rescue_aid.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.recognition_list.YesClass;
import com.example.haruka.rescue_aid.utils.InterviewAnswers;
import com.example.haruka.rescue_aid.utils.InterviewData;
import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.example.haruka.rescue_aid.utils.Question;
import com.example.haruka.rescue_aid.utils.Record;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import static com.example.haruka.rescue_aid.R.id.interview;

public class InterviewActivity extends AppCompatActivity implements LocationListener, TextToSpeech.OnInitListener {
    private Context context;
    private Button mBtnYes;
    private Button mBtnNo;
    private TextView mInterviewContent;
    private HorizontalScrollView historyScroll;
    private LinearLayout historyScrollLayout;
    //TODO check ListView
    private LayoutInflater inflater;

    private ArrayList<Question> questions;
    private Question currentQuestion;
    private ArrayList<Question> usedQuestions;
    private MedicalCertification medicalCertification;

    private ArrayList<String>[] dictionary;

    private InterviewData interviewData;
    private LocationManager mLocationManager;

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
    private TextToSpeech tts;

    private static final String TAG = "TestTTS";

    @Override
    public void onInit(int status) {
        if (TextToSpeech.SUCCESS == status) {
            Log.d(TAG, "initialized");
            showReadQuestion();
        } else {
            Log.e(TAG, "faile to initialize");
        }
    }

    // 読み上げのスピード
    private void setSpeechRate(float rate){
        if (null != tts) {
            tts.setSpeechRate(rate);
        } else {
            Log.d(TAG, "tts is null");
        }
    }

    // 読み上げのピッチ
    private void setSpeechPitch(float pitch){
        if (null != tts) {
            tts.setPitch(pitch);
        } else {
            Log.d(TAG, "tts is null");
        }
    }

    private void setTtsListener(){
        if (Build.VERSION.SDK_INT >= 15)
        {
            int listenerResult = tts.setOnUtteranceProgressListener(new UtteranceProgressListener()
            {
                @Override
                public void onDone(String utteranceId)
                {
                    Log.d(TAG,"progress on Done " + utteranceId);
                }

                @Override
                public void onError(String utteranceId)
                {
                    Log.d(TAG,"progress on Error " + utteranceId);
                }

                @Override
                public void onStart(String utteranceId)
                {
                    Log.d(TAG,"progress on Start " + utteranceId);
                }

            });
            if (listenerResult != TextToSpeech.SUCCESS)
            {
                Log.e(TAG, "failed to add utterance progress listener");
            }
        }
        else {
            Log.e(TAG, "Build VERSION is less than API 15");
        }

    }


    private void speechText(String text) {
        Log.d(TAG, "text is " + text);
        if (text.length() > 0) {
            if (tts.isSpeaking()) {
                tts.stop();
            }
            setSpeechRate(1.0f);
            setSpeechPitch(1.0f);

            // tts.speak(text, TextToSpeech.QUEUE_FLUSH, null) に
            // KEY_PARAM_UTTERANCE_ID を HasMap で設定
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"messageID");

            setTtsListener();
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
        }
    }

    private void showReadQuestion(){
        mInterviewContent.setText(currentQuestion.getQuestion());
        try{
            speechText(currentQuestion.getQuestion());
        }catch (Exception e){

        }
    }

    private void loadQuestions(){
        AssetManager assetManager = this.context.getResources().getAssets();
        try{
            // CSVファイルの読み込み
            InputStream is = assetManager.open("scenarios/scenario.csv");
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
        //final Intent intentCertification = new Intent(this, ExplainActivity.class);
        final Intent intentCertification = new Intent(this, ResultActivity.class);
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
        currentQuestion.answer(answer);
        usedQuestions.add(currentQuestion);
        Record r = new Record(Integer.toString(currentQuestion.getIndex()), Boolean.toString(currentQuestion.getAnswer()));
        medicalCertification.addRecord(r);

        LinearLayout incLayout =(LinearLayout)inflater.inflate(R.layout.history_slide_view, null);
        Button btn = new Button(this);
        btn.setText(currentQuestion.getQuestion() + "\n" + currentQuestion.getAnswer());
        historyScrollLayout.addView(btn);

        nextIndex = currentQuestion.getNextIndex(answer);
        if (nextIndex >= 0) {
            currentQuestion = questions.get(nextIndex);


            showReadQuestion();
            //mInterviewContent.setText(currentQuestion.getQuestion());
            //speechText(currentQuestion.getQuestion());

            Intent intent_listener = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent_listener.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent_listener.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
            sr.startListening(intent_listener);
        }else {
            interviewData.setListOfQuestions(usedQuestions);
            new AlertDialog.Builder(context).setMessage("問診は終了です").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //makeMedicalCertification();
                    intentCertification.putExtra("certification", medicalCertification);

                    startActivity(intentCertification);
                    finish();
                }
            }).show();

        }
    }

    void makeMedicalCertification(){
        medicalCertification = new MedicalCertification();
        for (Question q : usedQuestions){
            Record r = new Record(Integer.toString(q.getIndex()), Boolean.toString(q.getAnswer()));
            medicalCertification.addRecord(r);
        }

        medicalCertification.showRecords();
    }

    private void setLayout(){
        mBtnYes = (Button) findViewById(R.id.btn_yes);
        mBtnNo = (Button) findViewById(R.id.btn_no);
        mBtnYes.setOnClickListener(interAnsBtnListener);
        mBtnNo.setOnClickListener(interAnsBtnListener);
        mInterviewContent = (TextView) findViewById(interview);

        //mInterviewContent.setText(currentQuestion.getQuestion());
        showReadQuestion();

        historyScroll = (HorizontalScrollView)findViewById(R.id.history_scroll);
        historyScrollLayout = (LinearLayout) findViewById(R.id.history_scroll_layout);
        inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void setSpeechRecognizer(){
        sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        sr.setRecognitionListener(new SpeechListener());
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        //sr.startListening(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
        context = this;

        dictionary = YesClass.getDictionary();

        questions = new ArrayList<>();
        usedQuestions = new ArrayList<>();
        loadQuestions();
        medicalCertification = new MedicalCertification();

        setLayout();
        setSpeechRecognizer();

        interviewData = new InterviewData(null);

        tts = new TextToSpeech(this, this);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Location Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 0);
            return;
        }

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
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

    }

    @Override
    protected void onPause() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }
        tts.stop();
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        interviewData.updateLocation(location);
        Log.d("Longitude", String.valueOf(location.getLongitude()));
        Log.d("Latitude", String.valueOf(location.getLatitude()));
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.v("Status", "AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.v("Status", "OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.v("Status", "TEMPORARILY_UNAVAILABLE");
                break;
        }
    }
}
