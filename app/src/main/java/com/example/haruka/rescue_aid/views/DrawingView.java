package com.example.haruka.rescue_aid.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.Care;
import com.example.haruka.rescue_aid.utils.MedicalCertification;
import com.example.haruka.rescue_aid.utils.QADateFormat;
import com.example.haruka.rescue_aid.utils.Question;
import com.example.haruka.rescue_aid.utils.Record;
import com.example.haruka.rescue_aid.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Integer.parseInt;


/**
 * Created by Tomoya on 9/17/2017 AD.
 */

public class DrawingView extends View {
    private Paint paint, paintText;
    private Path path;
    private final int TEXT_SIZE_TITLE = 50;
    private final int TEXT_SIZE = 20;
    private final int DEFAULT_BLANK = 10;
    private final int MIDDLE_BLANK = 30;
    private final int BIG_BLANK = 40;
    private ArrayList<Record> interviewRecords;
    private ArrayList<Record> careRecords;

    public Canvas canvas;

    private int lastHight;
    private MedicalCertification medicalCertification;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.path = new Path();

        this.paint = new Paint();
        paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(3);
        this.paint.setTextSize(TEXT_SIZE_TITLE);
        lastHight = 0;
    }

    private void setText(Canvas canvas, String text, int x, boolean newLine){
        int y = lastHight + (int)paint.getTextSize() + DEFAULT_BLANK;
        canvas.drawText(text, x, y, paint);
        if (newLine) {
            lastHight = y;
        }
    }

    private void setText(Canvas canvas, String text, int startX, int endX, boolean newLine){
        if (text.length() * this.paint.getTextSize() < endX - startX) {
            setText(canvas, text, startX, newLine);
        } else {
            int startIndex = 0;
            int textLength = (int)((endX - startX)/paint.getTextSize());

            while(startIndex < text.length()) {
                setText(canvas, text.substring(startIndex, startIndex + textLength), startX, false);
                lastHight += paint.getTextSize();
                startIndex += textLength;
                if (startIndex + textLength >= text.length()) {
                    textLength = text.length() - startIndex;
                }
            }
        }
    }

    private void newLine(){
        lastHight += (int)paint.getTextSize() + DEFAULT_BLANK;
    }

    private void setBlank(){
        lastHight += DEFAULT_BLANK;
    }

    private void setMiddleBlank(){
        lastHight += MIDDLE_BLANK;
    }

    private void setBigBlank(){
        lastHight += BIG_BLANK;
    }

    private void initPaint(){
        this.paint = new Paint();
        paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(3);
        this.paint.setTextSize(TEXT_SIZE);
    }

    private void setTitle(Canvas canvas){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo2);
        bmp =  Bitmap.createScaledBitmap(bmp, canvas.getWidth()/3, canvas.getWidth()/6, false);
        canvas.drawBitmap(bmp, canvas.getWidth()/15, canvas.getHeight()/20, paint);
        initPaint();
        this.paint.setTextSize(TEXT_SIZE_TITLE);
        int centerX = canvas.getWidth()/2;
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText("診断書", centerX-TEXT_SIZE_TITLE*3/2, canvas.getHeight()/10, paint);
        lastHight = canvas.getHeight()/10;
        this.paint.setStyle(Paint.Style.STROKE);
    }

    private void setTime(Canvas canvas){
        initPaint();
        paint.setTextAlign(Paint.Align.RIGHT);
        int centerX = canvas.getWidth()/10;
        this.paint.setStyle(Paint.Style.FILL);
        if(medicalCertification == null) {
            setText(canvas, "2017年04月01日 12時00分 開始", canvas.getWidth() / 10 * 9, true);
            setText(canvas, "2017年04月01日 12時34分 発行", canvas.getWidth() / 10 * 9, true);
        } else {
            setText(canvas, medicalCertification.getStartAtJap() + " 開始", canvas.getWidth()/10*9, true);
            setText(canvas, QADateFormat.getInstanceJapanese() + " 発行", canvas.getWidth()/10*9, true);
        }
        this.paint.setStyle(Paint.Style.STROKE);
    }

    private void setLocation(Canvas canvas){
        try {
            initPaint();
            paint.setTextAlign(Paint.Align.LEFT);
            String addressText1 = "場所：" + medicalCertification.getAddress();
            if (!medicalCertification.getAddress().equals(MedicalCertification.DEFAULT_ADDRESS)) {
                String addressText2 = "　　" + " （東経" + Utils.getDMSLocation(medicalCertification.location[0]) + "、北緯" + Utils.getDMSLocation(medicalCertification.location[1]) + "）";
                setText(canvas, addressText1, canvas.getWidth() / 20, true);
                setText(canvas, addressText2, canvas.getWidth() / 20, true);
            } else {
                addressText1 += " （東経" + Utils.getDMSLocation(medicalCertification.location[0]) + "、北緯" + Utils.getDMSLocation(medicalCertification.location[1]) + "）";
                setText(canvas, addressText1, canvas.getWidth() / 20, true);
            }
        }catch (Exception e){
            String location = " - ";
            setText(canvas, "場所：" + location, canvas.getWidth() / 20, true);
        }
    }

    private void setSymptoms(Canvas canvas) {
        final int MIDDLE_LINE = canvas.getWidth() / 20 * 17;
        initPaint();

        setText(canvas, "以下のような問診結果になりました", canvas.getWidth() / 20, true);
        int upper = lastHight+DEFAULT_BLANK;
        canvas.drawLine(canvas.getWidth() / 30, upper, canvas.getWidth() / 30 * 29, upper, paint);
        setBlank();

        initPaint();
        paint.setTextSize((int)(TEXT_SIZE*1.2));
        medicalCertification.getScenarioID();

        int startPos = canvas.getWidth() / 18;
        for (Record record : interviewRecords){
            String question = record.getTag();
            String answer = record.getValue();
            initPaint();
            if (answer.equals(Utils.ANSWER_JP_YES)){
                paint.setTextSize((int)(TEXT_SIZE*1.3));
                paint.setColor(getResources().getColor(R.color.yes));
            } else {
                paint.setTextSize((int)(TEXT_SIZE*1.2));
                paint.setColor(getResources().getColor(R.color.no));
            }
            setText(canvas, answer, MIDDLE_LINE+8, false);
            initPaint();
            paint.setTextSize((int)(TEXT_SIZE*1.2));
            setText(canvas, question, startPos, MIDDLE_LINE, true);
            setBlank();
        }
        /*
        String[] questions = {"意識はありますか", "わーわーわーわーわーわーわーわーわーわーわーわーわーわーわーわーわーわーわーわーわーわーわーわー", "今日は何日ですか", "男ですか", "誰ですか"};
        String[] answers = {"Yes", "Yes", "Yes", "Yes", "No"};
        int startPos = canvas.getWidth() / 18;
        for (int i = 0; i < 5; i++) {
            setText(canvas, answers[i], MIDDLE_LINE+10, false);
            setText(canvas, questions[i], startPos, MIDDLE_LINE, true);
            setBlank();
        }
        */
        canvas.drawLine(canvas.getWidth() / 30, upper, canvas.getWidth() / 30, lastHight+DEFAULT_BLANK, paint);
        canvas.drawLine(MIDDLE_LINE, upper, MIDDLE_LINE, lastHight+DEFAULT_BLANK, paint);
        canvas.drawLine(canvas.getWidth() / 30 * 29, upper, canvas.getWidth() / 30 * 29, lastHight+DEFAULT_BLANK, paint);
        canvas.drawLine(canvas.getWidth() / 30, lastHight+DEFAULT_BLANK, canvas.getWidth() / 30 * 29, lastHight+DEFAULT_BLANK, paint);
    }

    private void setCares(Canvas canvas){
        initPaint();
        //String[] cares = {"胸骨圧迫", "AED", "胸骨圧迫"};
        //int[] time = {100, 100, 1000};
        setText(canvas, "以下の処置を行いました", canvas.getWidth() / 20, true);

        int upper = lastHight+DEFAULT_BLANK;
        canvas.drawLine(canvas.getWidth() / 30, upper, canvas.getWidth() / 30 * 29, upper, paint);
        setBlank();


        final int MIDDLE_LINE = canvas.getWidth() / 20 * 15;
        int startPos = canvas.getWidth() / 18;

        for (Record record : careRecords){
            paint.setTextAlign(Paint.Align.RIGHT);
            setText(canvas, record.getValue()+"秒", canvas.getWidth() / 30 * 29 -10, false);

            paint.setTextAlign(Paint.Align.LEFT);
            setText(canvas, record.getTag(), startPos, MIDDLE_LINE, true);
            setBlank();
        }
        /*
        for (int i = 0; i < 3; i++) {
            paint.setTextAlign(Paint.Align.RIGHT);
            setText(canvas, Integer.toString(time[i])+"秒", canvas.getWidth() / 30 * 29 -10, false);

            paint.setTextAlign(Paint.Align.LEFT);
            setText(canvas, cares[i], startPos, MIDDLE_LINE, true);
            setBlank();
        }
        */

        canvas.drawLine(canvas.getWidth() / 30, upper, canvas.getWidth() / 30, lastHight+DEFAULT_BLANK, paint);
        canvas.drawLine(MIDDLE_LINE, upper, MIDDLE_LINE, lastHight+DEFAULT_BLANK, paint);
        canvas.drawLine(canvas.getWidth() / 30 * 29, upper, canvas.getWidth() / 30 * 29, lastHight+DEFAULT_BLANK, paint);
        canvas.drawLine(canvas.getWidth() / 30, lastHight+DEFAULT_BLANK, canvas.getWidth() / 30 * 29, lastHight+DEFAULT_BLANK, paint);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);

        setTitle(canvas);
        setTime(canvas);
        setLocation(canvas);
        setBigBlank();
        setSymptoms(canvas);
        setBigBlank();
        setCares(canvas);

        canvas.translate(100,100);
        canvas.rotate(10);
        this.canvas = canvas;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //this.path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                //this.path.lineTo(x, y);
                break;
        }
        invalidate();
        return true;
    }

    public void delete() {
        this.path.reset();
        invalidate();
    }

    public void setCertification(MedicalCertification medicalCertification, ArrayList<Question> questions, ArrayList<Care> cares){
        interviewRecords = new ArrayList<>();
        careRecords = new ArrayList<>();
        this.medicalCertification = medicalCertification;

        for (int i = 0; i < medicalCertification.records.size(); i++){
        //for (Record r : medicalCertification.records){
            Record r = medicalCertification.records.get(i);
            try{
                //Integer.parseInt(r.getTag());
                int index = parseInt(r.getTag());
                Question question = questions.get(index);
                boolean _ans = r.getValue().equals(Utils.ANSWER_SHORT_YES);
                String answer = _ans ? Utils.ANSWER_JP_YES : Utils.ANSWER_JP_NO;
                interviewRecords.add(new Record(question.getQuestion(), answer));
            } catch (Exception e){
                if (r.getTag().equals(Utils.TAG_CARE)){
                    try{
                        Date start = QADateFormat.getDate(r.getTime());
                        Date end = QADateFormat.getDate(medicalCertification.records.get(i+1).getTime());
                        Log.d("care record", "start");
                        int careIndex = Integer.parseInt(r.getValue());
                        Care c = cares.get(careIndex);
                        String careTitle = c.name;
                        String time = Long.toString((end.getTime() - start.getTime())/1000);
                        careRecords.add(new Record(careTitle, time));

                    } catch (Exception e1){
                        Log.e("care record", e1.toString());
                    }
                } else if (r.getTag().equals(Utils.TAG_END)){

                }
            }
        }

    }


}
