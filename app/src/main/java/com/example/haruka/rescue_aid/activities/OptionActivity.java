package com.example.haruka.rescue_aid.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.views.CallOverlay;

/**
 * Created by Tomoya on 9/23/2017 AD.
 * AbstracyActivity implement menu and overlay
 */

public class OptionActivity extends AppCompatActivity {

    Intent overlayIntent;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1000;
    protected String callNote = "";

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_call_119:
                CallOverlay.setText(callNote);
                Log.d("call note", callNote);
                //CallOverlay.setText("意識はありますか？：はい\n吐き気はありますか？：いいえ\n息が苦しいですか？：いいえ\nのどは痛いですか？：はい\nつばが飲み込めないほど痛いですか？：はい");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        overlayIntent = new Intent(getApplication(), CallOverlay.class);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_title);

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
    };
}
