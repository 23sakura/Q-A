package com.example.haruka.rescue_aid.activities;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.Care;
import com.example.haruka.rescue_aid.utils.CareList;
import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.example.haruka.rescue_aid.utils.Question;
import com.example.haruka.rescue_aid.utils.Record;
import com.example.haruka.rescue_aid.utils.Utils;
import com.example.haruka.rescue_aid.views.CareAdapter;
import com.example.haruka.rescue_aid.views.ResultLineLayout;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

/**
 * Created by Tomoya on 9/7/2017 AD.
 */

public class ResultActivity extends OptionActivity {

    private MedicalCertification medicalCertification;
    private int urgency;
    private ArrayList<Care> cares;
    private ArrayList<Question> questions;
    LinearLayout linearLayout;
    ScrollView scrollView;
    LinearLayout inflateLayout;
    TextView textView;
    Button dealingBtn;
    protected ListView listView;
    final int MATCH_P = ViewGroup.LayoutParams.MATCH_PARENT;

    CareList careList;

    private String[] menuActivities = null;
    private DrawerLayout mDrawerLayout = null;
    private ListView mDrawerList = null;
    private ActionBarDrawerToggle mDrawerToggle = null;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    private void setLinearLayout(){
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        scrollView.addView(linearLayout);
    }

    private void setScrollView(){
        /*
        LinearLayout ll = (LinearLayout)findViewById(R.id.linearlayout_result);
        scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ScrollView.LayoutParams(MATCH_P, MATCH_P));
        ll.addView(scrollView);
        */
    }

    private void setListView(){
        listView = (ListView)findViewById(R.id.listview_care);
        CareAdapter careAdapter = new CareAdapter(this);
        Log.d("set listview", Integer.toString(cares.size()));
        careAdapter.setCareList(cares);
        listView.setAdapter(careAdapter);
    }

    private void setTextView(){
        textView = (TextView)findViewById(R.id.textview_notice_result); //
        //textView = new TextView(this);
        textView.setTextSize(34);

        textView.setTextColor(Utils.URGENCY_COLORS[urgency]);
        textView.setText(Utils.URGENCY_WARNING[urgency]);
        /*
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textLayoutParams);
        */
        //linearLayout.addView(textView);
    }

    private void showCareList(){
        for (final Care c : cares){
            ResultLineLayout resultLineLayout = new ResultLineLayout(this);
            addDescription(c);
            resultLineLayout.setTitle(c.name);
            resultLineLayout.setDescription(c.description);
            resultLineLayout.setView();
            linearLayout.addView(resultLineLayout);
            android.view.ViewGroup.LayoutParams layoutParams = resultLineLayout.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            resultLineLayout.setLayoutParams(layoutParams);

            resultLineLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int i = c.index;
                    Log.d("result line", Integer.toString(i));
                }
            });
        }

    }

    private void setDealingBtn(){
        final Intent intent = new Intent(this, ExplainActivity.class);

        dealingBtn = (Button)findViewById(R.id.btn_start_care); //new Button(this);
        dealingBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                intent.putExtra("CERTIFICATION", medicalCertification);
                startActivity(intent);
                finish();
            }
        });
        dealingBtn.setText("応急手当開始");
    }

    private void addDescription(Care c){
        int xmlID = Utils.getXMLID(c.xml);
        if (xmlID >= 0) {
            XmlResourceParser xpp = this.getResources().getXml(xmlID);


            try {
                int eventType = xpp.getEventType();
                while (eventType != XmlResourceParser.END_DOCUMENT) {
                    final String name = xpp.getName();
                    //Log.d("tag loop", " " + name);
                    if (name == null) {
                        Log.d("xpp", "name is null");
                        eventType = xpp.next();
                        continue;
                    }
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            Log.d("tag", "Start Document");
                            break;

                        case XmlPullParser.START_TAG:
                            if ("notice".equals(name)){

                            } else if ("notice_description".equals(name)){
                                c.setDescription(xpp.nextText());
                                Log.d("notice_description", c.description);
                            }
                        case XmlPullParser.END_TAG:
                            if ("notice".equals(name)){
                                //
                            }
                            break;
                        default:
                            break;
                    }

                    eventType = xpp.next();
                }
                Log.d("tag end document", Boolean.toString(eventType == XmlResourceParser.END_DOCUMENT));

            } catch (Exception e) {
                Log.e("Emergency", e.toString());
            }
        }
    }

    private void loadQuestions(){
        questions = new ArrayList<>();
        AssetManager assetManager = getResources().getAssets();
        try{
            // CSVファイルの読み込み
            //InputStream is = assetManager.open("scenarios/" + scenario);
            String scenario = Utils.getScenario(medicalCertification.getScenarioID());
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
            Log.e(ResultActivity.this.getClass().getSimpleName(), e.toString());
            e.printStackTrace();
        }
    }

    public String getCareString(boolean[] care_boolean){
        if (care_boolean == null){
            //TODO get care_boolean by MedicalCertification
            care_boolean = new boolean[7];
        }

        cares = new ArrayList<>();
        String s = "";
        for (int i = 0; i < care_boolean.length; i++){
            if (care_boolean[i]){
                s += "Y";
                Care c = CareList.getCare(i);

                cares.add(c);
            } else {
                s += "N";
            }
        }

        return s;
    }

    private void analyzeCertification(){
        urgency = 0;
        medicalCertification.showRecords("ResultActivity");

        boolean[] cares_flag = new boolean[Utils.NUM_CARE];
        for (Record record : medicalCertification.records){
            try{
                int questionIndex = Integer.parseInt(record.getTag());
                boolean answer = Utils.getAnswerBoolean(record.getValue());
                Question q = questions.get(questionIndex);
                q.answer(answer);
                boolean[] q_care = q.getCares();
                for (int i = 0; i < q_care.length; i++){
                    cares_flag[i] = cares_flag[i] | q_care[i];
                }
                urgency = Math.max(q.getUrgency(), urgency);
                Log.d("Result", "i" + Integer.toString(questionIndex) + ", a" + Boolean.toString(answer) + ", u" +  Integer.toString(urgency) + "m " +record.getTagValue());
            } catch (Exception e){

            }
        }
        getCareString(cares_flag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        careList = new CareList(this);
        careList.showCareList();

        try {
            medicalCertification = (MedicalCertification) getIntent().getSerializableExtra("CERTIFICATION");
        } catch (Exception e) {
            medicalCertification = new MedicalCertification();
        }
        boolean[] cares_flag = getIntent().getBooleanArrayExtra("CARES");
        String careString = getCareString(cares_flag);
        Log.d("CARES", careString);
        for (Care c : this.cares){
            Log.d("care required" , c.name);
        }

        loadQuestions();
        analyzeCertification();

        setListView();
        setTextView();
        setDealingBtn();

        //setDrawerLayout();

        medicalCertification.save(this);
    }


    @SuppressLint("ValidFragment")
    public class PlanetFragment extends Fragment {

        public static final String ARG_TEXT = "menu_choice";


        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_result, container, false);

            String str = getArguments().getString(ARG_TEXT);
            Log.d("ResultAct side menu", str + "is selected");

            if (str.equals("QRコード")){
                Intent intent = new Intent(ResultActivity.this, QRDisplayActivity.class);
                intent.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
                startActivity(intent);
            } else if (str.equals("診断書")){
                Intent intent = new Intent(ResultActivity.this, CertificationActivity.class);
                intent.putExtra(Utils.TAG_INTENT_CERTIFICATION, medicalCertification);
                startActivity(intent);
            } else if (str.equals("応急手当")){
                Intent intent = new Intent(ResultActivity.this, CareChooseActivity.class);
                startActivity(intent);
            } else if (str.equals("AEDマップ")){
                //TODO implement AED map
            }
            //getActivity().setTitle(str);
            return rootView;
        }
    }

}
