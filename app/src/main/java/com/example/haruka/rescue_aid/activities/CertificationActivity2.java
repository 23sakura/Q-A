package com.example.haruka.rescue_aid.activities;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.haruka.rescue_aid.R;

/**
 * Created by skriulle on 10/4/2017 AD.
 */

public class CertificationActivity2 extends OptionActivity {

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.layout_certification);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.tablelayout_certification1);
        for (int i = 0; i < 10; i++){
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText("hoge");
            tableRow.addView(textView);
            textView = new TextView(this);
            textView.setText("hoge");
            tableRow.addView(textView);
            tableLayout.addView(tableRow);
        }

        tableLayout = (TableLayout)findViewById(R.id.tablelayout_certification2);
        for (int i = 0; i < 10; i++){
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText("hoge");
            tableRow.addView(textView);
            textView = new TextView(this);
            textView.setText("hoge");
            tableRow.addView(textView);
            tableLayout.addView(tableRow);
        }
    }
}
