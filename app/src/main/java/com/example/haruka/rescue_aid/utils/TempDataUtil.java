package com.example.haruka.rescue_aid.utils;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Tomoya on 9/21/2017 AD.
 */

public class TempDataUtil {


    public final static String FILE_NAME = "ViewDto.obj";

    public static void store(Context context, Serializable object){
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    context.openFileOutput(QADateFormat.getInstance() + ".obj", Context.MODE_PRIVATE));
            out.writeObject(object);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static MedicalCertification load(Context context){
        Object retObj = null;
        try {
            ObjectInputStream in = new ObjectInputStream(
                    context.openFileInput(FILE_NAME)
            );
            retObj = in.readObject();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return (MedicalCertification)retObj;
    }
}
