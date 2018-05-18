package com.abhiinteractive.cricketscorecounter;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Abhijeet on 21-12-2017.
 */

public class Data {
    public static void putStringData(Context context, int players, String fileName, String[] value) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter
                    (context.openFileOutput(fileName, Context.MODE_PRIVATE));
            for (int i = 0; i < players; i++) {
                outputStreamWriter.write(value[i] + "\n");
            }
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        } catch (NullPointerException e){
            Log.e("Exception", "NPE: " + e.toString());
        }
    }

    public static void putIntData(Context context, int noOfLines, String fileName, int[] value) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter
                    (context.openFileOutput(fileName, Context.MODE_PRIVATE));
            for (int i = 0; i < noOfLines; i++) {
                outputStreamWriter.write(value[i] + "\n");
            }
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String[] getStringData(Context context/*, int playerNumber*/, String fileName){
        String[] data = new String[11];
        try {
            if(fileName != "" && context != null) {
                InputStream inputStream = context.openFileInput(fileName);
                String receiveString = "";
                int i = -1;

                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    while ((receiveString = bufferedReader.readLine()) != null) {
                        i++;
                        data[i] = receiveString;
                    }

                    inputStream.close();
                }
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return data;
    }

    public static int[] getIntData(Context context, /*int playerNumber,*/ String fileName){
        int[] data = new int[20];
        try {
            if(fileName != "" && context != null) {
                InputStream inputStream = context.openFileInput(fileName);
                String receiveString = "";
                int i = -1;

                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    while ((receiveString = bufferedReader.readLine()) != null) {
                        i++;
                        data[i] = Integer.parseInt(receiveString);
                    }

                    inputStream.close();
                }
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return data;
    }

}
