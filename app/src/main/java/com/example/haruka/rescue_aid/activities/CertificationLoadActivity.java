package com.example.haruka.rescue_aid.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

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

    public void setListView(){
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
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText editView = new EditText(CertificationLoadActivity.this);
                final String[] items = {"名前変更", "削除"};
                new AlertDialog.Builder(contextLoadDataActivity)
                        .setTitle(medicalCertifications.get(position).name)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("which", Integer.toString(which));
                                if (which == 0){
                                    new AlertDialog.Builder(contextLoadDataActivity)
                                            .setTitle("ファイル名を変更する")
                                            .setView(editView)
                                            .setPositiveButton("決定", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    MedicalCertification medicalCertification = medicalCertifications.get(position);
                                                    medicalCertification.name = editView.getText().toString();
                                                    medicalCertification.save(contextLoadDataActivity);

                                                    Toast.makeText(contextLoadDataActivity,
                                                            "ファイル名を\"" + medicalCertification.name + "\"に変更しました",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                }
                                            })
                                            .show();
                                    editView.setText(medicalCertifications.get(position).name);
                                    editView.setSelection(editView.getText().length());
                                } else {
                                    new AlertDialog.Builder(contextLoadDataActivity).setMessage("この問診データを消去しますか").setPositiveButton("はい", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            MedicalCertification medicalCertification = medicalCertifications.get(position);
                                            deleteFile(medicalCertification.FILENAME);
                                            setListView();
                                        }
                                    }).show();
                                }
                            }
                        })
                        .show();

                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle bundle){
        setTitle("問診履歴");
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

        /*
        String[] files = fileList();
        for (String file : files){
            deleteFile(file);
        }
        */
    }

    private void setListView2(){
        String[] text = {
                "2017.09.01 12.53.26,start,\n" +
                        "0,loc,134.562286:34.065508",
                "2017.09.19 19.03.22,start,\n" +
                        "0,0,N\n" +
                        "0,2,Y\n" +
                        "0,5,N\n" +
                        "0,"+Utils.TAG_CARE+",2\n" +
                        "1,"+Utils.TAG_CARE+",1\n" +
                        "0,"+Utils.TAG_CARE+",2\n" +
                        "3,"+Utils.TAG_CARE+",1\n" +
                        "4,"+Utils.TAG_CARE+",2\n" +
                        "300,"+Utils.TAG_END+", \n" +
                        "0,loc,134.667686:33.898343\n",
                "2017.09.20 09.20.51,start,\n" +
                        "0,loc,999999:33.898343",
                "2017.09.20 18.09.10,start,\n" +
                        "0,loc,134.608678:34.179186",
                "2017.09.22 10.42.41,start,\n" +
                        "0,loc,134.607412:34.182434",
                "2017.09.25 22.01.36,start,\n" +
                        "0,loc,134.601447:34.137024",
        };

        String[] names = {
                "",
                "",
                "",
                "お父さん　高熱",
                "50代女性　転倒",
                "先生　救急搬送",
        };


        final ArrayList<MedicalCertification> medicalCertifications = new ArrayList<>();
        for (int i = 0; i < text.length; i++){
            String t = text[i];
            MedicalCertification medicalCertification = new MedicalCertification(t);
            if(!names[i].equals(""))
                medicalCertification.name = names[i];
            Log.d("GetAddress", medicalCertification.getAddress(this));
            medicalCertifications.add(medicalCertification);
        }
        Collections.sort(medicalCertifications);



        CertificationAdapter certificationAdapter = new CertificationAdapter(CertificationLoadActivity.this);
        certificationAdapter.setMedicalCertification(medicalCertifications);
        listView.setAdapter(certificationAdapter);

    }


    @Override
    protected void onResume(){
        super.onResume();
        medicalCertification = null;
        setListView();
    }


}
