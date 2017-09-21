package com.example.haruka.rescue_aid.utils;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Tomoya on 9/21/2017 AD.
 */

public class TempDataUtil {

    public static void store(Context context, MedicalCertification medicalCertification){
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    context.openFileOutput(medicalCertification.FILENAME, Context.MODE_PRIVATE));
            out.writeObject(medicalCertification);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MedicalCertification load(Context context, String filename){
        Object retObj = null;
        try {
            ObjectInputStream in = new ObjectInputStream(
                    context.openFileInput(filename)
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
