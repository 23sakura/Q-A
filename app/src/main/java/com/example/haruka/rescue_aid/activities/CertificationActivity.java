package com.example.haruka.rescue_aid.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.Care;
import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.example.haruka.rescue_aid.utils.Question;
import com.example.haruka.rescue_aid.utils.Utils;
import com.example.haruka.rescue_aid.views.DrawingView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;


/**
 * Created by skriulle on 9/17/2017 AD.
 * This is an activity to show your certification after the interview or care.
 * It shows not only contents of interview and care, but time and location.
 * Showing it to your family, friends or doctor make it easier to tell the situation
 */

public class CertificationActivity extends OptionActivity {

    private DrawingView drawingView;

    ArrayList<Question> questions;
    ArrayList<Care> cares;

    private boolean throughInterview;

    private void loadQuestions(int scenarioID){
        AssetManager assetManager = getResources().getAssets();
        questions = new ArrayList<>();
        try{
            //InputStream is = assetManager.open("scenarios/" + scenario);
            String scenario = Utils.getScenario(scenarioID);
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

                    q = new Question(index, text, yesIndex, noIndex);
                } catch (Exception e){
                    q = new Question(index, text, yesIndex, noIndex);
                }
                questions.add(q);

                Log.d(" question" , q.getQuestion());
            }

            is.close();
        } catch (IOException e) {
            Log.e(CertificationActivity.this.getClass().getSimpleName(), e.toString());
            e.printStackTrace();
        }
    }

    private void loadCare(){
        AssetManager assetManager = getResources().getAssets();

        cares = new ArrayList<>();
        try{
            //InputStream is = assetManager.open("scenarios/" + scenario);
            String _careList = Utils.LIST_CARE;
            String careList = "care/" + _careList;
            Log.d("Care", careList);
            InputStream is = assetManager.open(careList);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line = "";
            int _i = 0;
            while ((line = bufferReader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ",");
                Log.d("scenario line", line);
                _i++;
                String id = st.nextToken();
                if(id == "id") continue;
                int index = parseInt(id);
                String name = st.nextToken();
                Log.d("text", name);
                Care c = new Care(index, name, "");
                cares.add(c);
            }
            is.close();
        } catch (IOException e) {
            Log.e(CertificationActivity.this.getClass().getSimpleName(), e.toString());
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("診断書");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);
        this.drawingView = (DrawingView) findViewById(R.id.drawing_view);
        //b = (Button) findViewById(R.id.delete_button);
        //b.setOnClickListener(deleteDrawing);

        Button QRButton, resultButton;
        QRButton = (Button)findViewById(R.id.btn_result_qr_display);
        resultButton = (Button)findViewById(R.id.btn_result_certification);

        QRButton.setText(getString(R.string.gotoQR));
        QRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CertificationActivity.this, QRDisplayActivity.class);
                intent.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
                intent.putExtra(Utils.TAG_INTENT_THROUGH_INTERVIEW, throughInterview);
                startActivity(intent);
                finish();
            }
        });
        resultButton.setText(getString(R.string.gotoResult));
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CertificationActivity.this, ResultActivity.class);
                intent.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
                intent.putExtra(Utils.TAG_INTENT_THROUGH_INTERVIEW, throughInterview);
                startActivity(intent);
                finish();
            }
        });

        try {
            medicalCertification = (MedicalCertification) getIntent().getSerializableExtra("CERTIFICATION");
            medicalCertification.getAddress(this);
        } catch (Exception e){
            medicalCertification = new MedicalCertification();
        }
        throughInterview = getIntent().getBooleanExtra(Utils.TAG_INTENT_THROUGH_INTERVIEW, false);
        loadQuestions(medicalCertification.getScenarioID());
        loadCare();

        drawingView.setCertification(medicalCertification, questions, cares);
        medicalCertification.save(this);
    }

    View.OnClickListener deleteDrawing = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //saveCapture(drawingView);
            drawingView.delete();
            //savehoge();
            /*bitmap = getViewCapture(drawingView);
            Intent intent = new Intent(MainActivity.this, Sub2Activity.class);
            //intent.putExtra("BITMAP", bitmap);
            startActivity(intent);
            */
        }
    };

    public Bitmap getViewCapture(View view) {
        view.setDrawingCacheEnabled(true);

        Bitmap cache = view.getDrawingCache();
        Bitmap screenShot = Bitmap.createBitmap(cache);
        view.setDrawingCacheEnabled(false);
        //Bit.bitmap = screenShot;
        return screenShot;
    }

    public void saveCapture(View view) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/capture.jpg");
        //File file = new File(getFilesDir().getPath() + "/capture.jpg");
        //File file = new File("/storage/emulated/0/Download" + "/capture.jpg");
        file.getParentFile().mkdir();
        Bitmap capture = getViewCapture(view);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            capture.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ie) {
                    fos = null;
                }
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if (throughInterview) {
                new AlertDialog.Builder(CertificationActivity.this)
                        .setTitle("終了")
                        .setMessage("タイトルに戻りますか")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(CertificationActivity.this, TitleActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("いいえ", null)
                        .show();
            } else {
                finish();
            }
            return true;
        }
        return false;
    }

}