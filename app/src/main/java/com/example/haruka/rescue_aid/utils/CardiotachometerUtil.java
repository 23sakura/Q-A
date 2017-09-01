package com.example.haruka.rescue_aid.utils;

import android.graphics.Bitmap;

import org.opencv.core.Mat;
import org.opencv.android.*;


/**
 * Created by Tomoya on 8/26/2017 AD.
 */

public class CardiotachometerUtil {


    public static Mat bitmapToMat(Bitmap b) {
        return new Mat(nBitmapToMat(b));
    }

    public static boolean matToBitmap(Mat m, Bitmap b) {
        return nMatToBitmap(m.nativeObj, b);
    }

    // native stuff
    static {
        System.loadLibrary("opencv_java");
    }


    private static native long nBitmapToMat(Bitmap b);

    private static native boolean nMatToBitmap(long m, Bitmap b);

}
