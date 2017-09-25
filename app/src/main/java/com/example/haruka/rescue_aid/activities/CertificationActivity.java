package com.example.haruka.rescue_aid.activities;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
 */

public class CertificationActivity extends OptionActivity {

    private MedicalCertification medicalCertification;
    private DrawingView drawingView;
    Button b;
    Bitmap bitmap;

    ArrayList<Question> questions;
    ArrayList<Care> cares;

    private void loadQuestions(int scenarioID){
        AssetManager assetManager = getResources().getAssets();
        questions = new ArrayList<>();
        try{
            // CSVファイルの読み込み
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
            // CSVファイルの読み込み
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);
        this.drawingView = (DrawingView) findViewById(R.id.drawing_view);
        b = (Button) findViewById(R.id.delete_button);
        b.setOnClickListener(deleteDrawing);

        try {
            medicalCertification = (MedicalCertification) getIntent().getSerializableExtra("CERTIFICATION");
            medicalCertification.getAddress(this);
        } catch (Exception e){
            medicalCertification = new MedicalCertification();
        }
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

        // Viewのキャッシュを取得
        Bitmap cache = view.getDrawingCache();
        Bitmap screenShot = Bitmap.createBitmap(cache);
        view.setDrawingCacheEnabled(false);
        //Bit.bitmap = screenShot;
        return screenShot;
    }

    public void saveCapture(View view) {
        // キャプチャを撮る
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
            Log.d("success oooo", Integer.toString((int) Environment.getExternalStorageDirectory().length()));
            Log.d("save ", "success " + getFilesDir());
            Log.d("save ", "success " + Environment.getExternalStorageState());
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

}