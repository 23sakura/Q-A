package com.example.haruka.rescue_aid.views;

import android.content.Context;
import android.widget.Button;

import com.example.haruka.rescue_aid.utils.Question;

/**
 * Created by Tomoya on 9/14/2017 AD.
 */

public class HistoryButton extends Button {

    public int index;
    public int level;

    public HistoryButton(Context context, int index) {
        super(context);
        this.index = index;
        this.level = Question.MIN_LEVEL;
        setHeight(180);
    }

    public void setText(Question q){
        setText(q.getQuestion() + "\n" + q.getAnswerString());
        this.level = q.getLevel();
    }
}

