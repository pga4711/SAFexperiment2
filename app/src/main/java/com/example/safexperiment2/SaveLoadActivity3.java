package com.example.safexperiment2;

import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
//import android.support.v4.content.FileProvider;  //In gradle you need "compile 'com.android.support:support-v4:25.3.1'"
import android.app.Activity;
import android.os.Bundle;
import android.net.Uri;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

//Good for SAF
import android.content.ContentResolver;
import android.provider.DocumentsContract;

//Till async
import android.os.AsyncTask;

public class SaveLoadActivity3 extends AppCompatActivity {
    private static final String TAG = "com.example.safexperiment2";

    public final static char CR  = (char) 0x0D;
    public final static char LF  = (char) 0x0A;

    public final static String CRLF  = "" + CR + LF;

    static final int REQUEST_OPEN_DOCUMENT = 1;
    static final int REQUEST_CREATE_DOCUMENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_load3);


        int z;
        //TEXT FOR FIRST COMMIT

        if (getIntent().getStringExtra("choice").equals("ACTION_OPEN_DOCUMENT")) {
            Log.d(TAG, "Doing ACTION_OPEN_DOCUMENT");
            String pathAsString = Environment.getExternalStorageDirectory() + "/myscientificapp/measurements/";
            File pathAsFile = new File(pathAsString);

            if (pathAsFile.isDirectory()) {
                Log.d(TAG, "pathAsFile is directory!");
            } else {
                Log.d(TAG, "pathAsFile is NOT a directory!");
            }
            if (pathAsFile.exists()) {
                Log.d(TAG, "pathAsFile exists!");
            } else {
                Log.d(TAG, "pathAsFile does NOT exists!");
            }
            android.net.Uri pathAsUri = Uri.fromFile(pathAsFile);
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pathAsUri);
            startActivityForResult(intent, REQUEST_OPEN_DOCUMENT);
        }
        else if (getIntent().getStringExtra("choice").equals("ACTION_CREATE_DOCUMENT")) {
            Log.d(TAG, "Doing ACTION_CREATE_DOCUMENT");
            String pathAsString = Environment.getExternalStorageDirectory() + "/myscientificapp/measurements/";
            File pathAsFile = new File(pathAsString);

            if (pathAsFile.isDirectory()) {
                Log.d(TAG, "pathAsFile is directory!");
            } else {
                Log.d(TAG, "pathAsFile is NOT a directory!");
            }
            if (pathAsFile.exists()) {
                Log.d(TAG, "pathAsFile exists!");
            } else {
                Log.d(TAG, "pathAsFile does NOT exists!");
            }

            android.net.Uri pathAsUri = Uri.fromFile(pathAsFile);

            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pathAsUri);
            intent.putExtra(Intent.EXTRA_TITLE, "newfile.txt");

            startActivityForResult(intent, REQUEST_CREATE_DOCUMENT);
        }
    }

    //Denna anropas alltid när en activity är typ klar. Den funkar osm ett slot i QT.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Returned from activity.");

        Intent output = new Intent();
        if (resultCode == RESULT_OK)
        {
            if(requestCode == REQUEST_OPEN_DOCUMENT)
            {
                String[] theUrisAsStr = { data.getData().toString() };
                new CacheFileAsyncTask().execute(theUrisAsStr);
            }
        }
        else if (resultCode == RESULT_OK)
        {
            if(requestCode == REQUEST_CREATE_DOCUMENT)
            {
                String[] theUrisAsStr = { data.getData().toString() };
                output.putExtra("status", "Done with REQUEST_CREATE_DOCUMENT. This is theUrisAsStr: " + theUrisAsStr);
                setResult(RESULT_OK, output); //OK REALLY? OR RESULT_CANCEL?
                finish();
            }
        }
        else
        {
            output.putExtra("status", "NO FILE OR ERROR");
            setResult(RESULT_OK, output); //OK REALLY? OR RESULT_CANCEL?
            finish();
        }
    }

    private void finishWithCreatedPath(String path) {
        Intent output = new Intent();
        output.putExtra("satus", path); //Send an uri back through the intent-extra-pipeline

        setResult(RESULT_OK, output);
        Log.d(TAG, "Before finish();");
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "SaveLoadActivity Activity stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SaveLoadActivity Activity destroyed");
    }

    public class CacheFileAsyncTask extends AsyncTask<String,String,String>
    {
        private final String TAG = CacheFileAsyncTask.class.getName();

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... theUrisAsStr) {
            Uri theUri =  Uri.parse(theUrisAsStr[0]);
            Context theContext = getApplicationContext(); //FUNKAR NOG

            try {
                InputStreamReader attachmentISR = new InputStreamReader(theContext.getContentResolver().openInputStream(theUri), Charset.forName("UTF-8"));
                StringBuilder output = new StringBuilder();
                if (attachmentISR != null) {

                    BufferedReader reader = new BufferedReader(attachmentISR);
                    String line = reader.readLine();

                    while (line != null) {
                        output.append(line);
                        output.append(CRLF);
                        line = reader.readLine();
                    }
                    reader.close();
                }

                attachmentISR.close();
                Log.d(TAG, getContentName(theContext.getContentResolver(), theUri));
                Log.d(TAG, output.toString());

                return "File reading finished. Please look at JNI callback";
            }

            catch (Exception e) {
                Log.e(TAG, "Explanation of what was being attempted", e);
                //fileSelected("NO FILE", "NO CONTENT");
                return "There was an exception";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            finishWithCreatedPath(s);
        }

        private String getContentName(ContentResolver resolver, Uri uri) {
            Cursor cursor = resolver.query(uri, null, null, null, null);
            cursor.moveToFirst();
            int nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            if (nameIndex >= 0) {
                String name = cursor.getString(nameIndex);
                cursor.close();
                return name;
            }
            cursor.close();
            return null;
        }
    }
}

