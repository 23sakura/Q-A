package com.example.haruka.rescue_aid.utils;

import android.content.Context;
import android.media.MediaPlayer;


/**
 * Created by Tomoya on 9/1/2017 AD.
 * Metronome class
 */

class Metronome {
    private Context context;
    MediaPlayer mediaPlayer;

    static int mod = 0;

    public static void main (String args[]) {
        try {
            SetBPM(140);

            while(true) {
                if (MetronomeWillPlay() == true) {
                    playSound("sounds/pop.wav");
                }
            }
        }
        catch (Exception ex) {

        }
    }

    public static void SetBPM(int bpm) {
        if (bpm == 0) {
            mod = 1000;
        }
        else {
            mod = 60000 / bpm;
        }
    }

    public static boolean MetronomeWillPlay() {
        if ((System.currentTimeMillis() % mod) == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments

            public void run() {
                try {

                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
}