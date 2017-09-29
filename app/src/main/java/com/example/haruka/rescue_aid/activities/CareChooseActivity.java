package com.example.haruka.rescue_aid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.Care;
import com.example.haruka.rescue_aid.utils.CareList;
import com.example.haruka.rescue_aid.views.CareListAdapter;

import java.util.ArrayList;

/**
 * Created by Tomoya on 9/18/2017 AD.
 * This is an activity to show the list of cares.
 * When you tap choose a care, you will see the explain of the care
 */

public class CareChooseActivity extends OptionActivity {

    ArrayList<Care> cares;

    private void loadCare(){
        CareList careList = new CareList(this);
        cares = CareList.careList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTitle("応急手当");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_care);
        ListView listView = (ListView)findViewById(R.id.listview_carelist);


        loadCare();

        ArrayList<Care> cares1 = (ArrayList<Care>)cares.clone();
        cares1.remove(0);
        cares1.remove(0);
        CareListAdapter careListAdapter = new CareListAdapter(this);
        careListAdapter.setCareList(cares1);
        listView.setAdapter(careListAdapter);

        final Intent intent = new Intent(this, ExplainActivity.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Care care = cares.get(position+2);
                intent.putExtra("CARE_XML", care.getXml());
                startActivity(intent);
            }
        });


        medicalCertification = null;
    }
}
