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
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.example.haruka.rescue_aid.utils.Utils;
import com.example.haruka.rescue_aid.views.HistoryButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.example.haruka.rescue_aid.R.id.interview;
import static java.lang.Integer.parseInt;

public class InterviewActivity extends ReadAloudTestActivity implements LocationListener{
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

    String scenario;

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

    private void showReadQuestion(){
        mInterviewContent.setText(currentQuestion.getQuestion());
        try{
            speechText(currentQuestion.getQuestion());
        }catch (Exception e){

        }
    }

    private void setNextQuestion(Question q){
        currentQuestion = q;
        showReadQuestion();
    }

    private boolean[] makeCareList(String care_){
        String[] careNums = care_.split(":");
        boolean[] careList = new boolean[Utils.NUM_CARE];
        for(String c : careNums){
            try {
                int index = Integer.parseInt(c.trim());
                careList[index] = true;
            } catch (Exception e){
                Log.e("make care list", e.toString());
            }
        }
        return careList;
    }

    public String getCareString(boolean[] cares){
        String s = "";
        for (boolean c : cares){
            s += c ? "Y" : "N";
        }

        return s;
    }

    private void loadQuestions(){
        AssetManager assetManager = this.context.getResources().getAssets();
        try{
            // CSVファイルの読み込み
            //InputStream is = assetManager.open("scenarios/" + scenario);
            String scenario_ = "scenarios/" + scenario;
            Log.d("Scenario", scenario_);
            InputStream is = assetManager.open(scenario_);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line = "";
            line = bufferReader.readLine();
            int _i = 0;
            while ((line = bufferReader.readLine()) != null) {
                Question q;
                StringTokenizer st = new StringTokenizer(line, ",");
                Log.d("scenario line", line);
                _i++;
                String id = st.nextToken();
                if(id == "id") continue;
                int index = parseInt(id);
                String text = st.nextToken();
                Log.d("text", text);
                int yesIndex = parseInt(st.nextToken());
                Log.d("yes_index", Integer.toString(yesIndex));
                int noIndex = parseInt(st.nextToken());
                Log.d("no_index", Integer.toString(noIndex));
                try {
                    int yesUrgency = parseInt(st.nextToken());
                    Log.d("yes_urgency", Integer.toString(yesUrgency));
                    int noUrgency = parseInt(st.nextToken());
                    Log.d("no_urgency", Integer.toString(noUrgency));
                    boolean[] yesCare = new boolean[Utils.NUM_CARE], noCare = new boolean[Utils.NUM_CARE];
                    try{
                        String yesCare_ = st.nextToken();
                        Log.d("yes care", yesCare_);
                        yesCare = makeCareList(yesCare_);
                        String noCare_ = st.nextToken();
                        Log.d("no care", noCare_);
                        noCare = makeCareList(noCare_);
                        Log.i("Question", "has been made perfectly");
                    } catch (Exception e) {
                        Log.e("load question", e.toString());
                    }
                    q = new Question(index, text, yesIndex, noIndex, yesUrgency, noUrgency, yesCare, noCare);
                } catch (Exception e){
                    q = new Question(index, text, yesIndex, noIndex);
                }
                questions.add(q);

                Log.d(" question" , q.getQuestion());
            }

            is.close();
        } catch (IOException e) {
            Log.e(InterviewActivity.this.getClass().getSimpleName(), e.toString());
            e.printStackTrace();
        }

        currentQuestion = questions.get(0);
    }

    private void backToQuestion(int index){ //the index given is to quote usedQuestions
        Question q = usedQuestions.remove(index);
        setNextQuestion(q);
    }

    private boolean isAnswered(int index){
        Question question = questions.get(index);

        for (Question q : usedQuestions){
            if (q.getIndex() == index){
                //the question is used
                return true;
            }
        }

        return false;
    }

    private void addUsedQuestion(Question q){
        addUsedQuestion(q, false);
    }

    private void addUsedQuestion(Question q, boolean isAnswered){
        final Question q_ = q;
        usedQuestions.add(q_);
        if (!isAnswered) {
            Record r = new Record(Integer.toString(q_.getIndex()), Boolean.toString(q_.getAnswer()));
            medicalCertification.addRecord(r);
        }
        LinearLayout incLayout =(LinearLayout)inflater.inflate(R.layout.history_slide_view, null);
        final HistoryButton btn = new HistoryButton(this, q_.getIndex());
        //btn.setText(q_.getQuestion() + "\n" + q_.getAnswer());
        btn.setText(q_);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = btn.index;
                int i = 0;
                for (i = 0; i < usedQuestions.size(); i++){
                    if (usedQuestions.get(i).getIndex() == x){
                        Log.d("Interview", usedQuestions.get(i).getQuestion());
                        historyScrollLayout.removeView(btn);
                        break;
                    }
                }
                if (q_.isAnswered){
                    addUsedQuestion(q_, true);
                }
                backToQuestion(i);
            }
        });
        historyScrollLayout.addView(btn);
    }

    private int getUrgency(){
        int maxUrgency = 0;
        for(int i = 0; i < historyScrollLayout.getChildCount(); i++) {
            HistoryButton hb = (HistoryButton)historyScrollLayout.getChildAt(i);
            maxUrgency = Math.max(maxUrgency, hb.urgency);
        }
        return maxUrgency;
    }

    private boolean[] getCares(){
        Log.d("care", "is required");
        boolean[] cares = new boolean[Utils.NUM_CARE];
        for(int i = 0; i < historyScrollLayout.getChildCount(); i++) {
            HistoryButton hb = (HistoryButton)historyScrollLayout.getChildAt(i);
            Log.d(Integer.toString(i),hb.getCareString());
            for (int j = 0; j < Utils.NUM_CARE; j++) {
                cares[j] = (cares[j] | hb.cares[j]);
            }
        }

        for (int i = 0; i < Utils.NUM_CARE; i++){
            if (cares[i]){
                Log.d("care", i + " is used");
            }
        }

        return cares;
        //TODO this method must be tested
    }

    private void showFinishAlart(){
        final Intent intentCertification = new Intent(this, ResultActivity.class);
        interviewData.setListOfQuestions(usedQuestions);
        new AlertDialog.Builder(context).setMessage("問診は終了です").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int urgency = getUrgency();
                boolean[] cares = getCares();
                Log.d("CERTIFICATION", Integer.toString(urgency));
                //makeMedicalCertification();
                intentCertification.putExtra("URGENCY", urgency);
                intentCertification.putExtra("CARES", cares);
                intentCertification.putExtra("CERTIFICATION", medicalCertification);
                medicalCertification.showRecords("InterviewActivity");
                startActivity(intentCertification);
                finish();
            }
        }).show();
    }

    private void produceNextQuestion(int viewID){
        int nextIndex = 0;
        boolean answer = false;
        switch(viewID){
            case R.id.btn_yes:
                answer = InterviewAnswers.YES;
                //currentQuestion.isAnswered = true;
                break;
            case R.id.btn_no:
                answer = InterviewAnswers.NO;
                //currentQuestion.isAnswered = true;
                break;
        }
        currentQuestion.answer(answer);

        addUsedQuestion(currentQuestion);

        nextIndex = currentQuestion.getNextIndex();
        if (nextIndex >= 0) {
            while(nextIndex >= 0) {
                if (isAnswered(nextIndex)) {
                    Question q = questions.get(nextIndex);
                    if (q.getAnswer()) {
                        nextIndex = q.getYesIndex();
                    } else {
                        nextIndex = q.getNoIndex();
                    }
                } else {
                    break;
                }
            }
            if(nextIndex == -1){
                showFinishAlart();
            } else {
                currentQuestion.isAnswered = true;
                setNextQuestion(questions.get(nextIndex));


                Intent intent_listener = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent_listener.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent_listener.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                sr.startListening(intent_listener);
            }
        }else {
            showFinishAlart();
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

        Intent intent = getIntent();
        scenario = intent.getStringExtra("SCENARIO");

        dictionary = YesClass.getDictionary();

        questions = new ArrayList<>();
        usedQuestions = new ArrayList<>();
        loadQuestions();
        medicalCertification = new MedicalCertification();

        setLayout();
        setSpeechRecognizer();

        interviewData = new InterviewData(null);

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
