package com.example.haruka.rescue_aid.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.example.haruka.rescue_aid.utils.TempDataUtil;
import com.example.haruka.rescue_aid.utils.Utils;
import com.example.haruka.rescue_aid.views.CertificationAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Tomoya on 9/21/2017 AD.
 * This is an activity to show the list of certifications you've got ever.
 * You tap a certification and go to CertficationEditActivity
 */

public class CertificationLoadActivity extends OptionActivity {

    Context contextLoadDataActivity;
    LinearLayout linearLayout;
    ListView listView;
    ArrayAdapter<MedicalCertification> arrayAdapter;

    private void setView(){
        String[] filenames = fileList();
        final ArrayList<MedicalCertification> medicalCertifications = new ArrayList<>();
        Log.d("certification ", String.valueOf(filenames.length));
        for (int i = 0; i < filenames.length; i++){
            final String filename = filenames[i];
            if (filename.equals("instant-run")) continue;
            Log.d("certification filename", filename);
            MedicalCertification medicalCertification = TempDataUtil.load(this, filename);
            medicalCertifications.add(medicalCertification);
            Log.d("certification ", String.valueOf(medicalCertifications.size()));
            Log.d("certification number", String.valueOf(medicalCertification.name));

        }
        Collections.sort(medicalCertifications);

        CertificationAdapter certificationAdapter = new CertificationAdapter(CertificationLoadActivity.this);

        certificationAdapter.setMedicalCertification(medicalCertifications);
        listView.setAdapter(certificationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MedicalCertification medicalCertification = medicalCertifications.get(position);
                Intent intent = new Intent(contextLoadDataActivity, CertificationEditActivity.class);
                intent.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        /*
        ScrollView scrollView = new ScrollView(this);
        setContentView(scrollView);
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);
        */
        contextLoadDataActivity = this;

        setContentView(R.layout.activity_load_certification);
        listView = (ListView)findViewById(R.id.listview_certification);
        //setView();

        /*
        String[] files = fileList();
        for (String file : files){
            deleteFile(file);
        }
        */
    }


    @Override
    protected void onResume(){
        super.onResume();

        setView();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_load_data, menu);
        return true;
    }

    static String TAG = "Menu";


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_delete_certification:
                new AlertDialog.Builder(contextLoadDataActivity).setMessage("すべての問診履歴を消去します\nよろしいですか").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] filenames = fileList();
                        for (String filename : filenames){
                            deleteFile(filename);
                        }
                        setView();
                    }
                }).show();
        }
        return true;
    }
*/

}
