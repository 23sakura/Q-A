package com.example.haruka.rescue_aid.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.views.CallOverlay;

public class TitleActivity extends AppCompatActivity {

    private Button gotoInterviewBtn, gotoTestBtn, historyBtn;
    private Intent interviewIntent, testIntent, qrIntent, overlayIntent;
    private Context context;


    public static int OVERLAY_PERMISSION_REQ_CODE = 1000;

    void closeOverLay(){
        stopService(overlayIntent);
    }

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
                // SYSTEM_ALERT_WINDOW permission not granted...
                // nothing to do !
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        overlayIntent = new Intent(getApplication(), CallOverlay.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        context = this;

        //interviewIntent = new Intent(this, InterviewActivity.class);
        interviewIntent = new Intent(this, SymptomCategorizeActivity.class);
        testIntent = new Intent(this, TestPlatformActivity.class);
        qrIntent = new Intent(this, QRActivity.class);

        gotoInterviewBtn = (Button)findViewById(R.id.startbtn);
        gotoInterviewBtn.setTextColor(getResources().getColor(R.color.start));
        gotoInterviewBtn.setBackgroundColor(getResources().getColor(R.color.start_back));
        gotoInterviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(interviewIntent);
            }
        });

        gotoTestBtn = (Button)findViewById(R.id.btn_title_qr);
        gotoTestBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(qrIntent);
            }
        });

        final Intent intent = new Intent(this, TestPlatformActivity.class);
        historyBtn =(Button) findViewById(R.id.btn_title_history);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        CallOverlay.setText("ほげほげ");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_title, menu);
        return true;
    }

    static String TAG = "Menu";


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_call_119:
                Uri uri = Uri.parse("tel:09052793706");
                startService(overlayIntent);
                Intent i = new Intent(Intent.ACTION_DIAL,uri);
                startActivity(i);
                break;
            case R.id.menu_titel_chest_compression:
                final Intent intent = new Intent(this, ExplainActivity.class);
                intent.putExtra("CARE_XML", "care_chest_compression");
                startActivity(intent);
                break;
        }
        return true;
    }
}

