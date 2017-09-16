package com.example.haruka.rescue_aid.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Tomoya on 9/16/2017 AD.
 */

public class ResultLineLayout extends LinearLayout {

    TextView title;
    TextView description;
    Button button;

    public ResultLineLayout(Context context){
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        title = new TextView(context);
        description = new TextView(context);
        button = new Button(context);
    }

    public void setTitle(String text){
        title.setText(text);
    }

    public void setDescription(String text){
        description.setText(text);
    }

    public void setButton(String text){
        button.setText(text);
    }

    public void setView(){
        addView(title);
        addView(description);
        addView(button);

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)description.getLayoutParams();
        mlp.leftMargin = 100;
        description.setLayoutParams(mlp);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)button.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.leftMargin = 40;
        layoutParams.rightMargin = 40;
        button.setLayoutParams(layoutParams);

    }
}
