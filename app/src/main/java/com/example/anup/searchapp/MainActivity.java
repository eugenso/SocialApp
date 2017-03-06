package com.example.anup.searchapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.StrictMode;


import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import static com.example.anup.searchapp.R.id.listview;

/* Diese Klasse trägt Daten bei den DropDownMenüs ein. Die Funktionen der DropDownMenüs und der Suchleiste werden definiert.
 Die Suchergebnisse werden in das Textview eingetragen. */

public class MainActivity extends AppCompatActivity {
    String JSON_STRING;
    //Hier wird ein API Connector erstellt
    APIConnector Connector = new APIConnector();
    String[] oberKategorieListe = new String[0];

    int gewählteOberkategorie;
    int gewählteUnterkategorie;

    //ID der Oberkategorie
    int idok;
    //ID der Unterkategorie
    int iduk;

    //Location Manager
    Context mContext;
    int latitude;
    int longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);


        // network access
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);



        //Location Manager
        mContext = this;

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            Toast.makeText(mContext,"Die Suchergebnisse werden nach kürzester Distanz sortiert",Toast.LENGTH_LONG).show();
            GPSTracker gps = new GPSTracker(mContext, MainActivity.this);


            // Checken, obs GPS aktiviert ist
            if (gps.canGetLocation()) {

                double reallatitude= gps.getLatitude();
                double reallongitude = gps.getLongitude();

                 latitude= (int) reallatitude;
                longitude= (int) reallongitude;

                //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // GPS oder Netzwerk sind nicht aktiviert
                gps.showSettingsAlert();
            }
        }


        //Searchview initialisieren
        final SearchView searchView = (SearchView) findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {




                String [] Suchergebnis= new String[0];

                    if (gewählteOberkategorie==0 && gewählteUnterkategorie==0){
                        try {
                            Suchergebnis = Connector.ErgebnisderSucheGPS(0,0,query,latitude,longitude);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        zeigeAlleTräger(Suchergebnis);}
                        else if (gewählteOberkategorie!= 0 && gewählteUnterkategorie==0) {

                        try {
                            Suchergebnis = Connector.ErgebnisderSucheGPS(idok,0,query,latitude,longitude);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        zeigeAlleTräger(Suchergebnis);}
                        else if(gewählteOberkategorie!=0 && gewählteUnterkategorie!=0){

                        try {
                            Suchergebnis = Connector.ErgebnisderSucheGPS(idok,iduk,query,latitude,longitude);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        zeigeAlleTräger(Suchergebnis);}







                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;

            }
        });





        try {
            oberKategorieListe = Connector.getOberkategorie();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //spinner1 wird erstellt und einfügen von Array
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, oberKategorieListe);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// The drop down view
        spinner.setAdapter(spinnerArrayAdapter);

        //spinner 2 wird erstellt
        Spinner spinnerU = (Spinner) findViewById(R.id.spinnerU);
        //spinnerU.setEnabled(false);
        final ArrayAdapter<String> spinnerArrayAdapterU = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[0]);
        spinnerArrayAdapterU.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// The drop down view

        // Es wird ein Listener für das Oberkategorien-DropDownMenü erstellt.

        OnItemSelectedListener listener1 = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int gewählteOberkategorie_, long id) {

                //Hier muss mit Connector ein Array mit den Unterkategorien erzeugt werden
                // String [] Unterkategorien= Connector.getUnterkategorie(selectedItemView);
                //String[] Unterkategorien = {"u1", "u2"};

                //Wenn die Ok geändert wird, setzte den Spinner 2 zurück

                MainActivity.this.gewählteOberkategorie = gewählteOberkategorie_;

                //Wenn alles anzeigen ausgewählt ist, zeige alle Träger an
                if (gewählteOberkategorie==0) {
                    String [] alleTraeger= new String[0];
                    try {
                        alleTraeger = Connector.ErgebnisderSucheGPS(0,0,"null",latitude,longitude);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    zeigeAlleTräger(alleTraeger);
                    Spinner spinnerU= (Spinner) findViewById(R.id.spinnerU);
                    spinnerU.setEnabled(false);
                    spinnerU.setAdapter(spinnerArrayAdapterU);

                }
                else {

                    //Setzte Unterkategorien in Spinner U ein -> unten
                    // Spinner mit Unterkategorien füllen
                    //TextView noch aktualisieren

                    try {


                        //Spinner mit UK füllen
                        Spinner spinner = (Spinner)findViewById(R.id.spinner);
                        String namederOberkategorie = spinner.getSelectedItem().toString();
                        String [] ukderok=Connector.getUnterkategorie(namederOberkategorie);

                        Spinner spinnerU = (Spinner) findViewById(R.id.spinnerU);

                        //spinnerUenable
                        spinnerU.setEnabled(true);
                        ArrayAdapter<String> spinnerArrayAdapterU = new ArrayAdapter<String>(MainActivity.super.getApplicationContext(), android.R.layout.simple_spinner_item, ukderok);
                        spinnerArrayAdapterU.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// The drop down view
                        spinnerU.setAdapter(spinnerArrayAdapterU);

                        //Textview mit Trägern der ok füllen
                         idok=Connector.getOberkategorieId(namederOberkategorie);

                        String [] alleTraegerderok= Connector.ErgebnisderSucheGPS(idok,0,"null",latitude,longitude);
                        zeigeAlleTräger(alleTraegerderok);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }







                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        };


        spinner.setOnItemSelectedListener(listener1);




        // Es wird ein zweiter Listener für das Unterkategorien-DropDownMenü erstellt
        OnItemSelectedListener listener2 = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int gewählteUnterkategorie_, long id) {

                gewählteUnterkategorie = gewählteUnterkategorie_;
                //Wenn alles anzeigen ausgewählt ist, zeige alle Träger an
                if (gewählteUnterkategorie==0) {

                    //Zeige alle Ergebnisse für alle Unterkategorien der Oberkategorie

                    Spinner spinner = (Spinner)findViewById(R.id.spinner);
                    String namederOberkategorie = spinner.getSelectedItem().toString();

                    idok=Connector.getOberkategorieId(namederOberkategorie);

                    String [] alleTraegerderok= new String[0];
                    try {
                        alleTraegerderok = Connector.ErgebnisderSucheGPS(idok,0,"null",latitude,longitude);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    zeigeAlleTräger(alleTraegerderok);



                }
                else {
                    //TextView aktualsieren, zeige alle Träger für gewählte Unterkategorie

                    //Unterkategorie bekommen
                    Spinner spinnerU = (Spinner)findViewById(R.id.spinnerU);
                    String namederUnterkategorie= spinnerU.getSelectedItem().toString();

                    iduk=Connector.getUnterkategorieId(namederUnterkategorie);

                    String [] alletraegerderuk= new String [0];
                    try {
                        alletraegerderuk=Connector.ErgebnisderSucheGPS(idok,iduk,"null",latitude,longitude);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    zeigeAlleTräger(alletraegerderuk);


                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        };
        spinnerU.setOnItemSelectedListener(listener2);



    }

    /* Diese Funktion lässt das String Array im ListView darstellen. */

    public void zeigeAlleTräger(String [] alleTräger){
        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alleTräger);

        final ListView mylist = (ListView) findViewById(listview);
        // Gehe auf die Seite des Trägers
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
                Intent intent= new Intent (MainActivity.this, DetailActivity.class);
                intent.putExtra("angebotsname", mylist.getItemAtPosition(i).toString());
                intent.putExtra("details", Connector.getDetails(mylist.getItemAtPosition(i).toString()));

                startActivity(intent);
            }
        });



        mylist.setAdapter((myadapter));

    }

    public void setUnterkategorien() {

    }


    //Info Button
    public boolean onCreateOptionsMenu (Menu menu){

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }
    /* Wenn auf den Info Button geklickt wird, rufe die Info Aktivität auf. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if (res_id==R.id.action_info){

            Intent intent= new Intent (MainActivity.this, InformationActivity.class);
            startActivity(intent);

        }
        return true;
    }
}
