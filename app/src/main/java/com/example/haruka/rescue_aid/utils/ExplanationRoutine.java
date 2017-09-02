package com.example.haruka.rescue_aid.utils;

import java.util.ArrayList;

/**
 * Created by Tomoya on 9/2/2017 AD.
 */

public class ExplanationRoutine extends EmergencySituation{

    ArrayList<EmergencySituation> routine;
    int[] roop;

    public ExplanationRoutine(String situation){
        super(situation);
        routine = new ArrayList();

        routine.add(new EmergencySituation(situation, 0));
        ExplanationRoutine _routine = new ExplanationRoutine("");
        _routine.routine.add(new EmergencySituation(situation, 1));
        _routine.routine.add(new EmergencySituation(situation, 0));
        routine.add(_routine);

        roop = new int[routine.size()];
    }

    void setRoop(){
        roop[0] = 1;
        roop[1] = 2;
    }

    void run(){
        int index = 0;
        do {
            for (EmergencySituation emergencySituation : routine) {
                for (int i = 0; i < roop[index]; i++) {
                    this.drawable = emergencySituation.drawable;
                    this.text = emergencySituation.text;
                }
            }
        }while(true);
    }

}
