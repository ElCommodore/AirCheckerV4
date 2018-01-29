package com.aircheckersolutions.airchecker.aircheckerv4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private DataManager dm_detail;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        RelativeLayout rl_main_detail = (RelativeLayout)findViewById(R.id.rl_main_detail);

        mProgress = new ProgressDialog (DetailActivity.this);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage("Loading... please wait");
        mProgress.setIndeterminate(false);
        mProgress.setMax(100);
        mProgress.setProgress(0);
        mProgress.show();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                dm_detail = new DataManager(DetailActivity.this);

                HandlerThread handlerThread = new HandlerThread("HandlerThread");
                handlerThread.start();

                final Handler responseHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        RefreshDetailView();
                        if(dm_detail.boolString.charAt(14) == '0')
                            dm_detail.CallFirstQuest();
                    }
                };

                Message msg = new Message();

                msg.obj = "Hello World";
                responseHandler.sendMessage(msg);
                mProgress.dismiss();

            }
        };

        new Thread(runnable).start();

        //InitListViews(pollutantsValues,pollenValues, statusPollutants, statusPollen, boolString);
        //SwipeRight für Wechsel der Activity
        rl_main_detail.setOnTouchListener(new OnSwipeTouchListener(DetailActivity.this) {
            public void onSwipeRight() {
                startActivity(new Intent(DetailActivity.this,MainActivity.class));
                finish();
            }
        });

        final Button btn_refresh_d = (Button)findViewById(R.id.btn_refresh_d);
        final Button btn_settings_d = (Button)findViewById(R.id.btn_settings_d);
        btn_refresh_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                startActivity(getIntent());
            }
        });
        btn_settings_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DetailActivity.this, SettingsActivity.class);
                intent.putExtra("boolString", dm_detail.boolString);
                intent.putExtra("caller", "DetailActivity");
                startActivity(intent);

            }
        });
    }
    private void RefreshDetailView(){

        ListView listViewPollutant = (ListView)findViewById(R.id.lv_pollutants);
        ListView listViewPollen = (ListView)findViewById(R.id.lv_pollen);

        List<Pollutant> listPollutants = new ArrayList<Pollutant>();
        List<Pollen> listPollen = new ArrayList<Pollen>();

        String booleanString = dm_detail.boolString;

        if(booleanString.charAt(0) == '1')
            listPollutants.add(new Pollutant("PM25",dm_detail.pollutants[0].currentValue, dm_detail.pollutants[0].status,true, R.drawable.bgpollutantpm25));
        if(booleanString.charAt(1) == '1')
            listPollutants.add(new Pollutant("PM10",dm_detail.pollutants[1].currentValue, dm_detail.pollutants[1].status,true, R.drawable.bgpollutantpm10));
        if(booleanString.charAt(2) == '1')
            listPollutants.add(new Pollutant("Ozone",dm_detail.pollutants[2].currentValue, dm_detail.pollutants[2].status,true, R.drawable.bgpollutantozone));
        if(booleanString.charAt(3) == '1')
            listPollutants.add(new Pollutant("Nitrogene Dioxide",dm_detail.pollutants[3].currentValue, dm_detail.pollutants[3].status,true, R.drawable.bgpollutantno2));
        if(booleanString.charAt(4) == '1')
            listPollutants.add(new Pollutant("Sulfur Dioxide",dm_detail.pollutants[4].currentValue, dm_detail.pollutants[4].status,true, R.drawable.bgpollutantso2));
        if(booleanString.charAt(5) == '1')
            listPollutants.add(new Pollutant("Carbone Monooxide",dm_detail.pollutants[5].currentValue, dm_detail.pollutants[5].status,true, R.drawable.bgpollutantco));

        Pollutant[] pollutants = new Pollutant[listPollutants.size()];
        pollutants = listPollutants.toArray(pollutants);

        if(booleanString.charAt(6) == '1')
            listPollen.add(new Pollen("Ambrosia",dm_detail.pollen[0].currentValue, dm_detail.pollen[0].status,true, R.drawable.bgpollenambrosia));
        if(booleanString.charAt(7) == '1')
            listPollen.add(new Pollen("Beifuß",dm_detail.pollen[1].currentValue, dm_detail.pollen[1].status,true, R.drawable.bgpollenbeifuss));
        if(booleanString.charAt(8) == '1')
            listPollen.add(new Pollen("Birke",dm_detail.pollen[2].currentValue, dm_detail.pollen[2].status,true, R.drawable.bgpollenbirke));
        if(booleanString.charAt(9) == '1')
            listPollen.add(new Pollen("Erle",dm_detail.pollen[3].currentValue, dm_detail.pollen[3].status,true, R.drawable.bgpollenerle));
        if(booleanString.charAt(10) == '1')
            listPollen.add(new Pollen("Esche",dm_detail.pollen[4].currentValue, dm_detail.pollen[4].status,true, R.drawable.bgpollenesche));
        if(booleanString.charAt(11) == '1')
            listPollen.add(new Pollen("Gräser",dm_detail.pollen[5].currentValue, dm_detail.pollen[5].status,true, R.drawable.bgpollengrass));
        if(booleanString.charAt(12) == '1')
            listPollen.add(new Pollen("Haselnuss",dm_detail.pollen[6].currentValue, dm_detail.pollen[6].status,true, R.drawable.bgpollenhaselnuss));
        if(booleanString.charAt(13) == '1')
            listPollen.add(new Pollen("Roggen",dm_detail.pollen[7].currentValue, dm_detail.pollen[7].status,true, R.drawable.bgpollenrye));

        Pollen[] pollen = new Pollen[listPollen.size()];
        pollen = listPollen.toArray(pollen);

        ListAdapterPollutant adapterPollutant = new ListAdapterPollutant(this, pollutants);
        ListAdapterPollen adapterPollen = new ListAdapterPollen(this, pollen);

        listViewPollutant.setAdapter(adapterPollutant);
        listViewPollen.setAdapter(adapterPollen);

    }

    private void InitListViews(float[] pollutantsValues, String[] pollenValues, int[] statusPollutants, int[] statusPollen, String booleanString){

        ListView listViewPollutant = (ListView)findViewById(R.id.lv_pollutants);
        ListView listViewPollen = (ListView)findViewById(R.id.lv_pollen);

        List<Pollutant> listPollutants = new ArrayList<Pollutant>();
        List<Pollen> listPollen = new ArrayList<Pollen>();

        if(booleanString.charAt(0) == '1')
            listPollutants.add(new Pollutant("PM25",pollutantsValues[0], statusPollutants[0],true, R.drawable.bgpollutantpm25));
        if(booleanString.charAt(1) == '1')
            listPollutants.add(new Pollutant("PM10",pollutantsValues[1], statusPollutants[1],true, R.drawable.bgpollutantpm10));
        if(booleanString.charAt(2) == '1')
            listPollutants.add(new Pollutant("Ozone",pollutantsValues[2], statusPollutants[2],true, R.drawable.bgpollutantozone));
        if(booleanString.charAt(3) == '1')
            listPollutants.add(new Pollutant("Nitrogene Dioxide",pollutantsValues[3], statusPollutants[3],true, R.drawable.bgpollutantno2));
        if(booleanString.charAt(4) == '1')
            listPollutants.add(new Pollutant("Sulfur Dioxide",pollutantsValues[4], statusPollutants[4],true, R.drawable.bgpollutantso2));
        if(booleanString.charAt(5) == '1')
            listPollutants.add(new Pollutant("Carbone Monooxide",pollutantsValues[5], statusPollutants[5],true, R.drawable.bgpollutantco));

        Pollutant[] pollutants = new Pollutant[listPollutants.size()];
        pollutants = listPollutants.toArray(pollutants);

        if(booleanString.charAt(6) == '1')
            listPollen.add(new Pollen("Ambrosia",pollenValues[0], statusPollen[0],true, R.drawable.bgpollenambrosia));
        if(booleanString.charAt(7) == '1')
            listPollen.add(new Pollen("Beifuß",pollenValues[1], statusPollen[1],true, R.drawable.bgpollenbeifuss));
        if(booleanString.charAt(8) == '1')
            listPollen.add(new Pollen("Birke",pollenValues[2], statusPollen[2],true, R.drawable.bgpollenbirke));
        if(booleanString.charAt(9) == '1')
            listPollen.add(new Pollen("Erle",pollenValues[3], statusPollen[3],true, R.drawable.bgpollenerle));
        if(booleanString.charAt(10) == '1')
            listPollen.add(new Pollen("Esche",pollenValues[4], statusPollen[4],true, R.drawable.bgpollenesche));
        if(booleanString.charAt(11) == '1')
            listPollen.add(new Pollen("Gräser",pollenValues[5], statusPollen[5],true, R.drawable.bgpollengrass));
        if(booleanString.charAt(12) == '1')
            listPollen.add(new Pollen("Haselnuss",pollenValues[6], statusPollen[6],true, R.drawable.bgpollenhaselnuss));
        if(booleanString.charAt(13) == '1')
            listPollen.add(new Pollen("Roggen",pollenValues[7], statusPollen[7],true, R.drawable.bgpollenrye));

        Pollen[] pollen = new Pollen[listPollen.size()];
        pollen = listPollen.toArray(pollen);

        ListAdapterPollutant adapterPollutant = new ListAdapterPollutant(this, pollutants);
        ListAdapterPollen adapterPollen = new ListAdapterPollen(this, pollen);

        listViewPollutant.setAdapter(adapterPollutant);
        listViewPollen.setAdapter(adapterPollen);
    }
}
