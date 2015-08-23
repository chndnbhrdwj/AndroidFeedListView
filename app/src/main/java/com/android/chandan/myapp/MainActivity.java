package com.android.chandan.myapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listViewApps;
    Button apps, movies, songs, clear;
    String appsXml,songsXml,moviesXml;
    String topSongs="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=10/xml";
    String topApps="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml";
    String topMovies="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml";
    ParseTop10Xml parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewApps = (ListView)findViewById(R.id.listViewApps);

        apps =(Button)findViewById(R.id.buttonApps);
        movies =(Button)findViewById(R.id.buttonMovies);
        songs =(Button)findViewById(R.id.buttonSongs);
        clear =(Button)findViewById(R.id.buttonClearApps);


        apps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new GetData().execute(topApps);
                setupView();
            }
        });
        songs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new GetData().execute(topSongs);
                setupView();
            }
        });
        movies.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new GetData().execute(topMovies);
                setupView();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewApps.setVisibility(listViewApps.INVISIBLE);
            }
        });
    }

    private void setupView(){
        parser= new ParseTop10Xml(appsXml);
        if (parser.process()) {
            ArrayList<Application> allApps = parser.getApplications();
            ArrayAdapter<Application> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.allapps_layout, allApps);
            listViewApps.setVisibility(listViewApps.VISIBLE);
            listViewApps.setAdapter(adapter);
        }
    }

    private class GetData extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            try{
                appsXml= downloadXml(urls[0]);
            }catch (IOException e){
                return "Unable to download xml.";
            }
            return appsXml;
        }

        private String downloadXml(String theUrl) throws IOException{
            int BUFFERSIZE=2000;
            InputStream is=null;
            String xmlContents="";

            try{
                URL url = new URL(theUrl);
                HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);

                Log.d("DownloadXml", "Response is " + conn.getResponseCode());

                is= conn.getInputStream();
                InputStreamReader isr= new InputStreamReader(is);
                int charRead;
                char[] inputBuffer = new char[BUFFERSIZE];

                try{
                    while ((charRead=isr.read(inputBuffer))>0){
                        String readString= String.copyValueOf(inputBuffer,0,charRead);
                        xmlContents +=readString;
                        inputBuffer = new char[BUFFERSIZE];
                    }
                    return xmlContents;

                }catch (IOException e){
                    e.printStackTrace();
                    return null;
                }
            }finally {
                if(is != null)
                is.close();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
