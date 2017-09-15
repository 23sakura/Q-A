package com.example.haruka.rescue_aid.utils;

/**
 * Created by Tomoya on 8/29/2017 AD.
 */

public class Question{

    public final static int MAX_LEVEL = 3, MIN_LEVEL = 1;

    private int index;
    private int yesIndex, noIndex;
    private int yesLevel, noLevel;
    private String question;
    private boolean answer;
    public boolean isAnswered;

    public Question(){
        index = -1;
        yesIndex = -100;
        noIndex = -100;
        yesLevel = MIN_LEVEL;
        noLevel = MIN_LEVEL;
        question = "This question is invalid";
        answer = false;
        isAnswered = false;
    }

    public Question(int index, String question, int yesIndex, int noIndex){
        this.index = index;
        this.yesIndex = yesIndex;
        this.noIndex = noIndex;
        this.question = question;
        this.yesLevel = MIN_LEVEL;
        this.noLevel = MIN_LEVEL;

        answer = false;
        isAnswered = false;
    }

    public Question(int index, String question, int yesIndex, int noIndex, int yesLevel, int noLevel){
        this.index = index;
        this.yesIndex = yesIndex;
        this.noIndex = noIndex;
        this.question = question;
        this.yesLevel = Math.min(Math.max(yesLevel, MIN_LEVEL), MAX_LEVEL);
        this.noLevel = Math.min(Math.max(noLevel, MIN_LEVEL), MAX_LEVEL);

        answer = false;
        isAnswered = false;
    }

    public String toString(){
        String res = this.getClass().getSimpleName().toString();
        res += "  , index : " + Integer.toString(index);
        res += "  , text : " + question;
        res += "  , yes index : " + Integer.toString(yesIndex);
        res += "  , no index : " + Integer.toString(noIndex);
        res += "  , yes emergency level : " + Integer.toString(yesLevel);
        res += "  , no emergency level : " + Integer.toString(noLevel);
        return res;
    }

    public int getIndex(){
        return index;
    }

    public int getNextIndex(boolean answer){
        if (answer == InterviewAnswers.YES){
            return getYesIndex();
        }else{
            return getNoIndex();
        }
    }

    public int getNextIndex(){
        if (answer == InterviewAnswers.YES){
            return getYesIndex();
        }else{
            return getNoIndex();
        }
    }

    public int getYesIndex(){
        return yesIndex;
    }

    public int getNoIndex(){
        return noIndex;
    }

    public int getYesLevel(){
        return yesLevel;
    }

    public int getNoLevel(){
        return noLevel;
    }

    public int getLevel(){
        if (answer){
            return yesLevel;
        } else {
            return noLevel;
        }
    }

    public String getQuestion(){
        return question;
    }

    public void answer(boolean a){
        answer = a;
    }

    public boolean getAnswer() {
        return answer;
    }

    public String getAnswerString() {
        if (answer){
            return "YES";
        } else {
            return "NO";
        }
    }


}
