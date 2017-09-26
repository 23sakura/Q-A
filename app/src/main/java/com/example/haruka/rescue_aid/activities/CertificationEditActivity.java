package com.example.haruka.rescue_aid.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.example.haruka.rescue_aid.utils.TempDataUtil;
import com.example.haruka.rescue_aid.utils.Utils;

/**
 * Created by Tomoya on 9/21/2017 AD.
 * This is an activity to edit your certifications.
 * You can change the name to take note.
 * You can see the certification, result of the interview and QR code to share.
 * Also, you can delete the Certification if you want
 */

public class CertificationEditActivity extends OptionActivity {

    Button button1, button2, button3, button4;
    Intent intent1, intent2, intent3;
    MedicalCertification medicalCertification;
    EditText filenameEditText;
    Button filenameButton;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit_certification);
        medicalCertification = (MedicalCertification)getIntent().getSerializableExtra(Utils.TAG_INTENT_CERTIFICATION);

        button1 = (Button)findViewById(R.id.btn_edit_qr);
        button2 = (Button)findViewById(R.id.btn_edit_show);
        button3 = (Button)findViewById(R.id.btn_edit_result);
        button4 = (Button)findViewById(R.id.btn_edit_delete);
        filenameEditText = (EditText)findViewById(R.id.edit_edit_filename);
        filenameButton = (Button)findViewById(R.id.btn_edit_filename);


        button1.setText("QRコード表示");
        button2.setText("診断書表示");
        button3.setText("結果画面表示");
        filenameEditText.setText(medicalCertification.name);

        intent1 = new Intent(this, QRDisplayActivity.class);
        intent2 = new Intent(this, CertificationActivity.class);
        intent3 = new Intent(this, ResultActivity.class);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
                startActivity(intent1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent2.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
                startActivity(intent2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent3.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
                startActivity(intent3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CertificationEditActivity.this).setMessage("このデータを消去します\nよろしいですか").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFile(medicalCertification.FILENAME);
                        finish();
                    }
                }).show();
            }
        });
        filenameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = filenameEditText.getText().toString();
                medicalCertification.name = filename;
                Log.d("filename", filename);
                TempDataUtil.store(CertificationEditActivity.this, medicalCertification);
            }
        });

    }
}
