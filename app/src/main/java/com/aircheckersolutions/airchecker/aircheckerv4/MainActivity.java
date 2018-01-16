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

    private ProgressDialog mProgress;

    int array[] = {1,2,3};

    public Pollutant[] pollutants;
    public Pollen[] pollen;

    public Float[] pollutantsValues;
    public String[] pollenValues;

    public String boolString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Appsee.start("ad88793b5fe944b1be886ab9e2a888d5");

        RelativeLayout rl_main = (RelativeLayout)findViewById(R.id.rl_main);

        HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();

        final Handler responseHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                RefreshSimpleView();
                if(boolString.charAt(14) == '0')
                    CallFirstQuest();
            }
        };

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

                Init();
                Message msg = new Message();

                msg.obj = "Hello World";
                responseHandler.sendMessage(msg);
                mProgress.dismiss();
                //RefreshSimpleView();
            }
        };



        new Thread(runnable).start();

        //SwipeLeft für Wechsel der Activity
        rl_main.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeLeft() {
                List<Float> listPollutantsValues = new ArrayList<Float>();
                List<String> listPollenValues = new ArrayList<String>();
                for(Pollutant pollutant : pollutants ){
                    listPollutantsValues.add(pollutant.currentValue);
                }
                for(Pollen polle : pollen ){
                    listPollenValues.add(polle.currentValue);
                }

                int[] statusPollutants = new int[6];
                int[] statusPollen = new int[8];

                for(int i = 0; i<6;i++){
                    statusPollutants[i] = pollutants[i].status;
                }
                for(int i = 0; i<8;i++){
                    statusPollen[i] = pollen[i].status;
                }



                pollutantsValues = new Float[listPollutantsValues.size()];
                pollenValues = new String[listPollenValues.size()];
                pollutantsValues = listPollutantsValues.toArray(pollutantsValues);
                pollenValues = listPollenValues.toArray(pollenValues);

                float[] pollutantsMessage = new float[6];
                for(int i=0;i<6;i++){
                    pollutantsMessage[i] = pollutantsValues[i];
                }


                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra("pollutantsValues",pollutantsMessage);
                i.putExtra("pollenValues",pollenValues);
                i.putExtra("pollutantsStatus",statusPollutants);
                i.putExtra("pollenStatus",statusPollen);
                i.putExtra("booleanString",boolString);
                startActivity(i);
                finish();
            }
        });
        /////////////////////////////////////////////////

        final Button btn_refresh = (Button)findViewById(R.id.btn_refresh);
        final Button btn_settings = (Button)findViewById(R.id.btn_settings);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TimeZone zone = TimeZone.getTimeZone("UTC");
                //Calendar cal = Calendar.getInstance(zone);

                //btn_refresh.setText("Last Refresh: " + cal.getTime());

                finish();
                startActivity(getIntent());
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("boolString", boolString);
                startActivity(intent);

            }
        });

    }

    public Pollutant[] GetPollutants(){
        return pollutants;
    }

    public Pollen[] GetPollen(){
        return pollen;
    }

    private void Init(){

        //Auslesen der Settings bzw. Standardwert festlegen
        String FILENAME = "config.cfg";
        BufferedReader reader;
        FileInputStream fis = null;
        boolString = "";
        try{
            fis = openFileInput(FILENAME);
            reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb =  new StringBuilder();
            while((boolString = reader.readLine()) != null){
                sb.append(boolString);
            }
            boolString = sb.toString();
            reader.close();
            fis.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            boolString = "111111111111110";
            WriteConfig(boolString);

        } catch (IOException e) {
            e.printStackTrace();
        }

        final String message = boolString;
        ///////////////////////////////////////////////

        //Erstellen der Listen für aktivierte Werte
        List<Pollutant> listPollutant = new ArrayList<Pollutant>();
        List<Pollen> listPollen = new ArrayList<Pollen>();

        if(boolString.charAt(0) == '1')
            listPollutant.add(new Pollutant("PM25",0.0f, 1,true, R.drawable.bgpollutantpm25));
        else{
            listPollutant.add(new Pollutant("PM25",0.0f, 1,false, R.drawable.bgpollutantpm25));
        }
        if(boolString.charAt(1) == '1')
            listPollutant.add(new Pollutant("PM10",0.0f,1,true, R.drawable.bgpollutantpm10));
        else{
            listPollutant.add(new Pollutant("PM10",0.0f, 1,false, R.drawable.bgpollutantpm25));
        }
        if(boolString.charAt(2) == '1')
            listPollutant.add(new Pollutant("Ozone",0.0f,1,true, R.drawable.bgpollutantozone));
        else{
            listPollutant.add(new Pollutant("Ozone",0.0f, 1,false, R.drawable.bgpollutantpm25));
        }
        if(boolString.charAt(3) == '1')
            listPollutant.add(new Pollutant("Nitrogene Dioxide",0.0f,1,true, R.drawable.bgpollutantno2));
        else{
            listPollutant.add(new Pollutant("Nitrogene Dioxide",0.0f, 1,false, R.drawable.bgpollutantpm25));
        }
        if(boolString.charAt(4) == '1')
            listPollutant.add(new Pollutant("Sulfur Dioxide",0.0f,1,true, R.drawable.bgpollutantso2));
        else{
            listPollutant.add(new Pollutant("Sulfur Dioxide",0.0f, 1,false, R.drawable.bgpollutantpm25));
        }
        if(boolString.charAt(5) == '1')
            listPollutant.add(new Pollutant("Carbone Monooxide",0.0f,1,true, R.drawable.bgpollutantco));
        else{
            listPollutant.add(new Pollutant("Carbone Monooxide",0.0f, 1,false, R.drawable.bgpollutantpm25));
        }

        pollutants = new Pollutant[listPollutant.size()];
        pollutants = listPollutant.toArray(pollutants);

        if(boolString.charAt(6) == '1')
            listPollen.add(new Pollen("Ambrosia","",0,true, R.drawable.bgpollenambrosia));
        else{
            listPollen.add(new Pollen("Ambrosia","",0,false, R.drawable.bgpollenambrosia));
        }
        if(boolString.charAt(7) == '1')
            listPollen.add(new Pollen("Beifuß","",0,true, R.drawable.bgpollenbeifuss));
        else{
            listPollen.add(new Pollen("Beifuß","",0,false, R.drawable.bgpollenambrosia));
        }
        if(boolString.charAt(8) == '1')
            listPollen.add(new Pollen("Birke","",0,true, R.drawable.bgpollenbirke));
        else{
            listPollen.add(new Pollen("Birke","",0,false, R.drawable.bgpollenambrosia));
        }
        if(boolString.charAt(9) == '1')
            listPollen.add(new Pollen("Erle","",0,true, R.drawable.bgpollenerle));
        else{
            listPollen.add(new Pollen("Erle","",0,false, R.drawable.bgpollenambrosia));
        }
        if(boolString.charAt(10) == '1')
            listPollen.add(new Pollen("Esche","",0,true, R.drawable.bgpollenesche));
        else{
            listPollen.add(new Pollen("Esche","",0,false, R.drawable.bgpollenambrosia));
        }
        if(boolString.charAt(11) == '1')
            listPollen.add(new Pollen("Gräser","",0,true, R.drawable.bgpollengrass));
        else{
            listPollen.add(new Pollen("Gräser","",0,false, R.drawable.bgpollenambrosia));
        }
        if(boolString.charAt(12) == '1')
            listPollen.add(new Pollen("Haselnuss","",0,true, R.drawable.bgpollenhaselnuss));
        else{
            listPollen.add(new Pollen("Haselnuss","",0,false, R.drawable.bgpollenambrosia));
        }
        if(boolString.charAt(13) == '1')
            listPollen.add(new Pollen("Roggen","",0,true, R.drawable.bgpollenrye));
        else{
            listPollen.add(new Pollen("Roggen","",0,false, R.drawable.bgpollenambrosia));
        }

        pollen = new Pollen[listPollen.size()];
        pollen = listPollen.toArray(pollen);

        Refresh();


    }

    private void Refresh(){
        String httpCode = GetWebsite("https://api.waqi.info/feed/here/?token=16be779fb1a58ee9de6a3f1848a345ada4f4bfc6");
        try {
            JSONObject obj = new JSONObject(httpCode);
            Float[] pollutantArray = new Float[6];

            try{
                pollutantArray[0] = Float.parseFloat(obj.getJSONObject("data").getJSONObject("iaqi").getJSONObject("pm25").getString("v"));
            }catch(Exception e){
                pollutantArray[0] = 0.0f;
            }
            try{
                pollutantArray[1] = Float.parseFloat(obj.getJSONObject("data").getJSONObject("iaqi").getJSONObject("pm10").getString("v"));
            }catch(Exception e){
                pollutantArray[1] = 0.0f;
            }
            try{
                pollutantArray[2] = Float.parseFloat(obj.getJSONObject("data").getJSONObject("iaqi").getJSONObject("o3").getString("v"));
            }catch(Exception e){
                pollutantArray[2] = 0.0f;
            }
            try{
                pollutantArray[3] = Float.parseFloat(obj.getJSONObject("data").getJSONObject("iaqi").getJSONObject("no2").getString("v"));
            }catch(Exception e){
                pollutantArray[3] = 0.0f;
            }
            try{
                pollutantArray[4] = Float.parseFloat(obj.getJSONObject("data").getJSONObject("iaqi").getJSONObject("so2").getString("v"));
            }catch(Exception e){
                pollutantArray[4] = 0.0f;
            }
            try{
                pollutantArray[5] = Float.parseFloat(obj.getJSONObject("data").getJSONObject("iaqi").getJSONObject("co").getString("v"));
            }catch(Exception e){
                pollutantArray[5] = 0.0f;
            }

            for(int i = 0; i < pollutants.length; i++){
                pollutants[i].currentValue = pollutantArray[i];
                pollutants[i].percentValue = pollutants[i].currentValue / pollutants[i].max;
                if(pollutants[i].percentValue < 0.2f){
                    pollutants[i].status = 1;
                }
                if(pollutants[i].percentValue < 0.5f && pollutants[i].percentValue >= 0.2f){
                    pollutants[i].status  = 2;
                }
                if(pollutants[i].percentValue < 0.8f && pollutants[i].percentValue >= 0.5f){
                    pollutants[i].status  = 3;
                }
                if(pollutants[i].percentValue <= 1.0f && pollutants[i].percentValue >= 0.8f){
                    pollutants[i].status  = 4;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        GetWebsiteJS();
    }

    private void RefreshSimpleView(){

        ImageView ivPollutant = (ImageView)findViewById(R.id.iv_pollutants);
        ImageView ivPollen = (ImageView)findViewById(R.id.iv_pollen);

        Pollutant[] arrPollutant = GetPollutants();
        Pollen[] arrPollen = GetPollen();
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

    private void GetWebsiteJS() {

        final StringBuilder builder = new StringBuilder();
        final String[] pollenValues = new String[8];
        try {
            Document doc = Jsoup.connect("http://www.wetter.com/gesundheit/pollenflug/").get();
            String title = doc.title();
            Elements links = doc.select("a[href]");
            Elements myin = doc.getElementsByClass("text--small portable-hide");
            int counter = 0;
            for (Element item : myin) {
                System.out.println(item.childNode(0).toString());
                pollenValues[counter] = item.childNode(0).toString();
                counter++;
            }

        } catch (IOException e) {
            builder.append("Error : ").append(e.getMessage()).append("\n");
        }

        for(int i = 0; i<pollen.length;i++){
            pollen[i].currentValue = pollenValues[i];
            if(pollenValues[i].equals("Keine Belastung"));
            pollen[i].status = 0;
            if(pollenValues[i].equals("Geringe Belastung"))
                pollen[i].status = 30;
            if(pollenValues[i].equals("Hohe Belastung"))
                pollen[i].status = 60;
        }
    }

    private String GetWebsite(String website){
        String resString = "";


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(website);
        try{
            HttpResponse response;
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"windows-1251"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) !=  null){
                sb.append(line + "\n");
            }
            resString = sb.toString();
            is.close();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "ERROR!", Toast.LENGTH_SHORT).show();
        }
        return  resString;
    }

    private void WriteConfig(String _boolString) {
        String FILENAME = "config.cfg";
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

    private void CallFirstQuest(){
        final CharSequence[] items = {" Breathing Difficulties "," Allergies "," Nothing "};
// arraylist to keep the selected items
        final ArrayList seletedItems=new ArrayList();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Welcome! Do you suffer from one of the diseases?")
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            seletedItems.add(indexSelected);
                        } else if (seletedItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            seletedItems.remove(Integer.valueOf(indexSelected));
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                    }
                }).create();
        dialog.show();

        StringBuilder sb = new StringBuilder();
        sb.append(boolString);
        sb.setCharAt(14,'1');
        String actBoolString = sb.toString();
        WriteConfig(actBoolString);
    }
}
