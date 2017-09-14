package com.example.haruka.rescue_aid.views;

import android.content.Context;
import android.widget.Button;

/**
 * Created by Tomoya on 9/14/2017 AD.
 */

public class HistoryButton extends Button {

    public int index;

    public HistoryButton(Context context, int index) {
        super(context);
        this.index = index;
    }
}

