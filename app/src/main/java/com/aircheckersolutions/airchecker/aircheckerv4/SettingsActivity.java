package com.aircheckersolutions.airchecker.aircheckerv4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SettingsActivity extends AppCompatActivity {

    Class callerClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();

        callerClass = null;


        final String message = intent.getStringExtra("boolString");
        final String caller  = getIntent().getStringExtra("caller");

        final CheckBox cb_pm25 = (CheckBox) findViewById(R.id.checkBox);
        final CheckBox cb_pm10 = (CheckBox) findViewById(R.id.checkBox2);
        final CheckBox cb_o3 = (CheckBox) findViewById(R.id.checkBox3);
        final CheckBox cb_no2 = (CheckBox) findViewById(R.id.checkBox4);
        final CheckBox cb_so2 = (CheckBox) findViewById(R.id.checkBox5);
        final CheckBox cb_co = (CheckBox) findViewById(R.id.checkBox6);

        final CheckBox cb_ambrosia = (CheckBox) findViewById(R.id.checkBox7);
        final CheckBox cb_beifuss = (CheckBox) findViewById(R.id.checkBox8);
        final CheckBox cb_birke = (CheckBox) findViewById(R.id.checkBox9);
        final CheckBox cb_erle = (CheckBox) findViewById(R.id.checkBox10);
        final CheckBox cb_esche = (CheckBox) findViewById(R.id.checkBox11);
        final CheckBox cb_graeser = (CheckBox) findViewById(R.id.checkBox12);
        final CheckBox cb_haselnuss = (CheckBox) findViewById(R.id.checkBox13);
        final CheckBox cb_roggen = (CheckBox) findViewById(R.id.checkBox14);

        if (message.charAt(0) == '1')
            cb_pm25.setChecked(true);
        if (message.charAt(1) == '1')
            cb_pm10.setChecked(true);
        if (message.charAt(2) == '1')
            cb_o3.setChecked(true);
        if (message.charAt(3) == '1')
            cb_no2.setChecked(true);
        if (message.charAt(4) == '1')
            cb_so2.setChecked(true);
        if (message.charAt(5) == '1')
            cb_co.setChecked(true);

        if (message.charAt(6) == '1')
            cb_ambrosia.setChecked(true);
        if (message.charAt(7) == '1')
            cb_beifuss.setChecked(true);
        if (message.charAt(8) == '1')
            cb_birke.setChecked(true);
        if (message.charAt(9) == '1')
            cb_erle.setChecked(true);
        if (message.charAt(10) == '1')
            cb_esche.setChecked(true);
        if (message.charAt(11) == '1')
            cb_graeser.setChecked(true);
        if (message.charAt(12) == '1')
            cb_haselnuss.setChecked(true);
        if (message.charAt(13) == '1')
            cb_roggen.setChecked(true);

        Button btn_save = (Button) findViewById(R.id.button);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuilder sb = new StringBuilder();

                    if(cb_pm25.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }
                    if(cb_pm10.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }
                    if(cb_o3.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }
                    if(cb_no2.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }
                    if(cb_so2.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }
                    if(cb_co.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }

                    if(cb_ambrosia.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }
                    if(cb_beifuss.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }
                    if(cb_birke.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }
                    if(cb_erle.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }
                    if(cb_esche.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }
                    if(cb_graeser.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }
                    if(cb_haselnuss.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }
                    if(cb_roggen.isChecked()){
                        sb.append("1");
                    }
                    else{
                        sb.append("0");
                    }

                sb.append(message.charAt(14));

                String finalString = sb.toString();

                WriteConfig(finalString);
                Intent i;
                if(caller.equals("MainActivity")){
                    i = new Intent(SettingsActivity.this, MainActivity.class);
                }
                else{
                    i = new Intent(SettingsActivity.this, DetailActivity.class);
                }

                startActivity(i);

            }
        });
    }

    private void WriteConfig(String _boolString) {
        String FILENAME = "config_v152.cfg";
        BufferedWriter writer;
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILENAME, MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(_boolString);
            writer.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
