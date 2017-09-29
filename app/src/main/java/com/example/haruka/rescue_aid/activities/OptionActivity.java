package com.example.haruka.rescue_aid.activities;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.example.haruka.rescue_aid.utils.Question;
import com.example.haruka.rescue_aid.utils.Utils;
import com.example.haruka.rescue_aid.views.CallOverlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

/**
 * Created by Tomoya on 9/23/2017 AD.
 * AbstracyActivity implement menu and overlay
 */

public class OptionActivity extends AppCompatActivity {

    Intent overlayIntent;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1000;
    protected String callNote = "";
    protected MedicalCertification medicalCertification;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_title, menu);
        return true;
    }

    static String TAG = "Menu";


    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
            }
        }
    }

    protected void QRDisplay(){
        if (medicalCertification != null){
            Intent intent = new Intent(this, QRDisplayActivity.class);
            intent.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
            startActivity(intent);
        } else {
            Toast.makeText(this, "QRコードで表示するデータがありません", Toast.LENGTH_LONG).show();
        }
    }

    protected void call119(){
        if (medicalCertification != null) {
            callNote = medicalCertification.getCallNote(loadQuestions(Utils.getScenario(medicalCertification.getScenarioID())));
            if (!callNote.equals("")) {
                CallOverlay.setText(callNote);
                Log.d("call note", callNote);
                startService(overlayIntent);
            }
        }
        Uri uri = Uri.parse("tel:119");
        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        startActivity(intent);
    }

    protected void showAEDmap(){
        Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("http://aedm.jp"));
        startActivity(i);
    }


    private ArrayList<Question> loadQuestions(String scenario){
        ArrayList<Question> questions = new ArrayList<>();

        AssetManager assetManager = this.getResources().getAssets();
        try{
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
                        yesCare = MedicalCertification.makeCareList(yesCare_);
                        String noCare_ = st.nextToken();
                        Log.d("no care", noCare_);
                        noCare = MedicalCertification.makeCareList(noCare_);
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
            Log.e(this.getClass().getSimpleName(), e.toString());
            e.printStackTrace();
        }

        return questions;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Intent intent;
        switch(item.getItemId()){
            case R.id.menu_call_119:
                call119();
                break;
            /*
            case R.id.menu_title_chest_compression:
                intent = new Intent(this, ExplainActivity.class);
                intent.putExtra("CARE_XML", "care_chest_compression");
                startActivity(intent);
                break;
            */
            case R.id.menu_title_QR_display:
                QRDisplay();
                break;
            case R.id.menu_title_QR:
                intent = new Intent(this, QRActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_title_care_list:
                intent = new Intent(this, CareChooseActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_title_AED:
                showAEDmap();
                break;
            case R.id.menu_title_title:
                if(!this.getClass().getSimpleName().equals(TitleActivity.class.getSimpleName())) {

                    new AlertDialog.Builder(this)
                            .setTitle("終了")
                            .setMessage("タイトルに戻りますか")
                            .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent _intent = new Intent(OptionActivity.this, TitleActivity.class);
                                    _intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(_intent);
                                }
                            })
                            .setNegativeButton("いいえ", null)
                            .show();

                    return true;
                }
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        overlayIntent = new Intent(getApplication(), CallOverlay.class);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void setCallNote(String text){
        callNote = text;
    }

    protected void resetCallNote(String text){

    }

    @Override
    protected void onResume(){
        super.onResume();

        CallOverlay.removeCallOver();
    }
}
