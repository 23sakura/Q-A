package com.example.haruka.rescue_aid.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.example.haruka.rescue_aid.utils.TempDataUtil;
import com.example.haruka.rescue_aid.utils.Utils;

/**
 * Created by Tomoya on 9/21/2017 AD.
 */

public class LoadDataActivity extends AppCompatActivity {

    Context contextLoadDataActivity;
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(linearLayout);
        contextLoadDataActivity = this;

        String[] filenames = fileList();
        for(int i = 1; i < filenames.length; i++){
            final String filename = filenames[i];

            Button b = new Button(this);
            b.setText(filename);
            linearLayout.addView(b);

            MedicalCertification medicalCertification = TempDataUtil.load(contextLoadDataActivity, filename);
            medicalCertification.showRecords();

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MedicalCertification medicalCertification = TempDataUtil.load(contextLoadDataActivity, filename);
                    Intent intent = new Intent(contextLoadDataActivity, Display_qr.class);
                    intent.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
                    startActivity(intent);
                }
            });

        }
    }
}
