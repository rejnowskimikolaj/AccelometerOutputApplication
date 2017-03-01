package com.example.rent.accelometeroutputapplication;

import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RENT on 2017-02-18.
 */

public class FileManager {

    public static final FileManager instance = new FileManager();
    private static final String FOLDER_NAME = "accelometer_outputs";
    private static final String FILE_NAME = "accelometer";


    private DataInputStream reader;
    private DataOutputStream writer;


    private void openInputStreamReader() {

        //File containingFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME);

        try {
            reader = new DataInputStream(new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME + "/" + FILE_NAME + ".bin"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void openOutputStreamWriter(String fileName) {

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME);
        if (!folder.exists()) {
            folder.mkdir();
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME + "/" + fileName + ".bin");
            writer = new DataOutputStream(fileOutputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveAccOutputToFile(float[] output){


        try {
            writer.writeFloat(output[0]);
            writer.writeFloat(output[1]);
            writer.writeFloat(output[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeOutputStream(){

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile() throws IOException {
        openInputStreamReader();
        StringBuilder builder = new StringBuilder();

        while (reader.available() >= 12) {

            builder.append(String.valueOf(reader.readFloat()));
            builder.append(String.valueOf(reader.readFloat()));
            builder.append(String.valueOf(reader.readFloat()));
            builder.append("\n");
        }

        reader.close();
        return builder.toString();
    }



}
