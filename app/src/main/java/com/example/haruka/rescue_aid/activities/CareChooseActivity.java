package com.example.haruka.rescue_aid.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.Care;
import com.example.haruka.rescue_aid.utils.Utils;
import com.example.haruka.rescue_aid.views.CareListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

/**
 * Created by skriulle on 9/18/2017 AD.
 */

public class CareChooseActivity extends OptionActivity {

    ArrayList<Care> cares;

    private void loadCare(){
        AssetManager assetManager = getResources().getAssets();

        cares = new ArrayList<>();
        try{
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
                String xml;
                try{
                    xml = st.nextToken();
                } catch (Exception e){
                    xml = "_";
                }
                Care c = new Care(index, name, xml);
                cares.add(c);
            }
            is.close();
        } catch (IOException e) {
            Log.e(CareChooseActivity.this.getClass().getSimpleName(), e.toString());
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
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


        /*
        Button[] buttons = new Button[cares.size()];
        for (int i = 0; i < cares.size(); i++){
            Care care = cares.get(i);
            buttons[i] = new Button(this);
            buttons[i].setId(i);
            buttons[i].setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            );
            buttons[i].setText(care.name);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intent);
                }
            });
            linearLayout.addView(buttons[i]);
            Log.d("button", care.name);
        }
        */


    }
}
