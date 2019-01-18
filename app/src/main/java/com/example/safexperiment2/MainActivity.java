package com.example.safexperiment2;

import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    EditText num1, num2;
    Button btn;
    Button loadBtn;
    Button saveBtn;

    String resultX;


    String strUrl = "foooooo";

    int ACTIVITY_ONE_CODE = 1;
    int ACTIVITY_TWO_CODE = 1;

    int WRITE_EXTERNAL_STORAGE_CODE = 0;
    int READ_EXTERNAL_STORAGE_CODE = 1;
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        num1 = (EditText) findViewById(R.id.num1);
        num2 = (EditText) findViewById(R.id.num2);


        btn = (Button) findViewById(R.id.btnAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(num1.getText().toString());
                //int j = Integer.parseInt(num2.getText().toString());
                //int k = i * j;
                Integer[] intArr = new Integer[1];
                intArr[0] = i;

                new MultiplyTask().execute(intArr);
            }
        });

        loadBtn = (Button) findViewById(R.id.loadFile);
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here, thisActivity is the current activity


                grantPermissions();


                Log.d(TAG, "Intent startup");


                Intent intent = new Intent(MainActivity.this, SaveLoadActivity3.class);

                intent.putExtra("choice", "ACTION_OPEN_DOCUMENT");

                MainActivity.this.startActivityForResult(intent, ACTIVITY_ONE_CODE);

            }
        });

        saveBtn = (Button) findViewById(R.id.saveFile);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here, thisActivity is the current activity
                grantPermissions();

                Log.d(TAG, "Intent startup");

                Intent intent = new Intent(MainActivity.this, SaveLoadActivity3.class);

                intent.putExtra("choice", "ACTION_CREATE_DOCUMENT");

                MainActivity.this.startActivityForResult(intent, ACTIVITY_TWO_CODE);
            }
        });


    }

    protected void grantPermissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Not enough WRITE_EXTERNAL_STORAGE permissions");
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
        }
        else
            Log.d(TAG, "We have WRITE_EXTERNAL_STORAGE permissions");

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Not enough READ_EXTERNAL_STORAGE permissions");
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
        }
        else
            Log.d(TAG, "We have READ_EXTERNAL_STORAGE permissions");

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == ACTIVITY_ONE_CODE) {

            if (resultCode == RESULT_OK) {

                String status = data.getStringExtra("status");
                Log.d(TAG, "ACTIVITY_ONE_CODE This is status: " + status);

            }
        }
        else if (requestCode == ACTIVITY_TWO_CODE) {

            if (resultCode == RESULT_OK) {

                String status = data.getStringExtra("status");
                Log.d(TAG, "ACTIVITY_TWO_CODE This is status: " + status);

            }
        }


    }

    public class MultiplyTask extends AsyncTask<Integer,String,String>
    {

        private final String TAG = MultiplyTask.class.getName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MainActivity.this, "The output: " + s + ", hihi", Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Integer... input) {

            try {
                /*
                URL url = new URL(strUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String value = bf.readLine();

                */

                int a = 29;
                int b = 49;
                Integer res = a*b;
                res = input[0]*input[0];

                return res.toString();
                //Log.d(TAG, "Result is: " + res);
                //System.out.println("result is: " + res);


            }
            catch (Exception e) {
                //Log.e(TAG, "Error is. " + res);
                //System.out.println(e);
                Log.e(TAG, "Explanation of what was being attempted", e);
            }


            return null;
        }
    }


}
