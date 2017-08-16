package com.example.haruka.rescue_aid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InterviewActivity extends AppCompatActivity {

    private Context context;
    private Intent mToQr;
    private Button mBtnYes;
    private Button mBtnNo;
    private TextView mInterviewContent;
    private String[] contents = {"意識はありますか？", "息をしていますか？", "頭からの出血はありますか？", "周りに危険物はありますか？"};
    private int offset = 0;
    private String mResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        context = this;
        mToQr = new Intent(this, Display_qr.class);
        mBtnYes = (Button)findViewById(R.id.btn_yes);
        mBtnNo  = (Button)findViewById(R.id.btn_no);
        mInterviewContent = (TextView)findViewById(R.id.interview);
        mInterviewContent.setText(contents[offset]);


        mBtnYes.setOnClickListener(interAnsBtnListener);
        mBtnNo.setOnClickListener(interAnsBtnListener);
    }
    View.OnClickListener interAnsBtnListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            if(offset < contents.length ){
                switch(v.getId()){
                    case R.id.btn_yes:
                        mResult += contents[offset] + " : YES\n";
                        break;
                    case R.id.btn_no:
                        mResult += contents[offset] + " : No\n";
                        break;
                }
                mInterviewContent.setText(contents[offset++]);
            }
            else {
                new AlertDialog.Builder(context).setMessage("問診は終了です\nQRコードを表示します").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mToQr.putExtra("RESULT", mResult);
                        startActivity(mToQr);
                    }
                }).show();
            }
        }
    };

    @Override
    protected void onResume(){
        super.onResume();

        offset = 0;
    }
}
