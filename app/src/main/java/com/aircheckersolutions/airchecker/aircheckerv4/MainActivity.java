package com.aircheckersolutions.airchecker.aircheckerv4;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.json.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.appsee.Appsee;

public class MainActivity extends AppCompatActivity {


    private DataManager dm_main;

    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Appsee.start("ad88793b5fe944b1be886ab9e2a888d5");

        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action != null && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        setContentView(R.layout.activity_main);
        RelativeLayout rl_main = (RelativeLayout)findViewById(R.id.rl_main);

        mProgress = new ProgressDialog (MainActivity.this);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage("Loading... please wait");
        mProgress.setIndeterminate(false);
        mProgress.setMax(100);
        mProgress.setProgress(0);
        mProgress.show();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                dm_main = new DataManager(MainActivity.this);

                HandlerThread handlerThread = new HandlerThread("HandlerThread");
                handlerThread.start();

                final Handler responseHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        RefreshSimpleView();
                        if(dm_main.boolString.charAt(14) == '0')
                            dm_main.CallFirstQuest();
                    }
                };

                Message msg = new Message();

                msg.obj = "Hello World";
                responseHandler.sendMessage(msg);
                mProgress.dismiss();

            }
        };

        new Thread(runnable).start();

        //SwipeLeft für Wechsel der Activity
        rl_main.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeLeft() {
                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(i);
            }
        });
        /////////////////////////////////////////////////

        final Button btn_refresh = (Button)findViewById(R.id.btn_refresh);
        final Button btn_settings = (Button)findViewById(R.id.btn_settings);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("boolString", dm_main.boolString);
                intent.putExtra("caller", "MainActivity");
                startActivity(intent);

            }
        });

    }

    private void RefreshSimpleView(){

        ImageView ivPollutant = (ImageView)findViewById(R.id.iv_pollutants);
        ImageView ivPollen = (ImageView)findViewById(R.id.iv_pollen);

        Pollutant[] arrPollutant = dm_main.pollutants;
        Pollen[] arrPollen = dm_main.pollen;
        int statusMaxPollutant = 1;
        int statusMaxPollen = 0;

        for (int i = 0; i < arrPollutant.length; i++){
            if(arrPollutant[i].status > statusMaxPollutant && arrPollutant[i].activated)
                statusMaxPollutant = arrPollutant[i].status;
        }
        for (int i = 0; i < arrPollen.length; i++){
            if(arrPollen[i].status > statusMaxPollen && arrPollen[i].activated)
                statusMaxPollen = arrPollen[i].status;
        }

        if(statusMaxPollutant == 1)
            ivPollutant.setImageResource(R.drawable.smiley_happy);
        if(statusMaxPollutant == 2)
            ivPollutant.setImageResource(R.drawable.smiley_neutral);
        if(statusMaxPollutant == 3)
            ivPollutant.setImageResource(R.drawable.smiley_sad);
        if(statusMaxPollutant == 4)
            ivPollutant.setImageResource(R.drawable.smiley_hazard);

        if(statusMaxPollen == 0)
            ivPollen.setImageResource(R.drawable.smiley_happy);
        if(statusMaxPollen == 30)
            ivPollen.setImageResource(R.drawable.smiley_neutral);
        if(statusMaxPollen == 60)
            ivPollen.setImageResource(R.drawable.smiley_sad);
        if(statusMaxPollen == 90)
            ivPollen.setImageResource(R.drawable.smiley_hazard);

    }

}
