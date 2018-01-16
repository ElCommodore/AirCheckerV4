package com.aircheckersolutions.airchecker.aircheckerv4;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        RelativeLayout rl_main_detail = (RelativeLayout)findViewById(R.id.rl_main_detail);

        Bundle extras = getIntent().getExtras();
        float[] pollutantsValues = extras.getFloatArray("pollutantsValues");
        String[] pollenValues = extras.getStringArray("pollenValues");
        int[] statusPollutants = extras.getIntArray("pollutantsStatus");
        int[] statusPollen = extras.getIntArray("pollenStatus");
        String booleanString = extras.getString("booleanString");

        InitListViews(pollutantsValues,pollenValues, statusPollutants, statusPollen, booleanString);
        //SwipeRight für Wechsel der Activity
        rl_main_detail.setOnTouchListener(new OnSwipeTouchListener(DetailActivity.this) {
            public void onSwipeRight() {
                startActivity(new Intent(DetailActivity.this,MainActivity.class));
                finish();
            }
        });
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
