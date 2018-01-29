package com.aircheckersolutions.airchecker.aircheckerv4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    Context context;
    String boolString;

    public Pollutant[] pollutants;
    public Pollen[] pollen;

    public DataManager(Context _context){
        context = _context;
        Init();
        Refresh();
        System.out.println("Ready");
    }

    public void Init(){
        boolString = ReadFromFile();

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

    }

    private void WriteToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.cfg", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String ReadFromFile() {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.cfg");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            ret = "111111111111110";
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
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
        }
        return  resString;
    }

    public void CallFirstQuest(){
        final CharSequence[] items = {" Breathing Difficulties "," Allergies "," Nothing "};
// arraylist to keep the selected items
        final ArrayList seletedItems=new ArrayList();

        AlertDialog dialog = new AlertDialog.Builder(context)
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
        WriteToFile(actBoolString);
    }
}
