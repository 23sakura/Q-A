package com.example.haruka.rescue_aid.utils;

/**
 * Created by Tomoya on 8/29/2017 AD.
 */

public class Question{

    private int index;
    private int yesIndex, noIndex;
    private String question;
    private boolean answer;

    public Question(){
        index = -1;
        yesIndex = -100;
        noIndex = -100;
        question = "This question is invalid";
        answer = false;
    }

    public Question(int index, String question, int yesIndex, int noIndex){
        this.index = index;
        this.yesIndex = yesIndex;
        this.noIndex = noIndex;
        this.question = question;

        answer = false;
        //Log.i("Question class", this.toString());
    }

    public String toString(){
        String res = this.getClass().getSimpleName().toString();
        res += "  , index : " + Integer.toString(index);
        res += "  , text : " + question;
        res += "  , yes index : " + Integer.toString(yesIndex);
        res += "  , no index : " + Integer.toString(noIndex);
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

    public int getYesIndex(){
        return yesIndex;
    }

    public int getNoIndex(){
        return noIndex;
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
}
