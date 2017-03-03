package com.example.anup.searchapp;

import android.content.Intent;
import android.os.StrictMode;


import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import static com.example.anup.searchapp.R.id.listview;

public class MainActivity extends AppCompatActivity {
    String JSON_STRING;
    //Hier wird ein API Connector erstellt
    APIConnector Connector = new APIConnector();
    String[] liste2 = new String[0];

    int gewählteOberkategorie;
            int gewählteUnterkategorie;

    //ID der Oberkategorie
    int idok;
    //ID der Unterkategorie
    int iduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);


        // network access
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);




        //Searchview initialisieren
        final SearchView searchView = (SearchView) findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {




                String [] Suchergebnis= new String[0];

                    if (gewählteOberkategorie==0 && gewählteUnterkategorie==0){
                        try {
                            Suchergebnis = Connector.ErgebnisderSuche(0,0,query);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        zeigeAlleTräger(Suchergebnis);}
            else if (gewählteOberkategorie!= 0 && gewählteUnterkategorie==0) {

                        try {
                            Suchergebnis = Connector.ErgebnisderSuche(idok,0,query);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        zeigeAlleTräger(Suchergebnis);}
                        else if(gewählteOberkategorie!=0 && gewählteUnterkategorie!=0){

                        try {
                            Suchergebnis = Connector.ErgebnisderSuche(idok,iduk,query);
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
            liste2 = Connector.getOberkategorie();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //spinner1 wird erstellt und einfügen von Array
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liste2);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// The drop down view
        spinner.setAdapter(spinnerArrayAdapter);

        //spinner 2 wird erstellt
        Spinner spinnerU = (Spinner) findViewById(R.id.spinnerU);
        //spinnerU.setEnabled(false);
        final ArrayAdapter<String> spinnerArrayAdapterU = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[0]);
        spinnerArrayAdapterU.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// The drop down view

        //TESTEST
        //final TextView text = (TextView) findViewById(R.id.textView);

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
                        alleTraeger = Connector.ErgebnisderSuche(0,0,"null");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //String [] alleTraeger={"T1,T2"};
                    zeigeAlleTräger(alleTraeger);
                    Spinner spinnerU= (Spinner) findViewById(R.id.spinnerU);
                    spinnerU.setEnabled(false);
                    spinnerU.setAdapter(spinnerArrayAdapterU);

                }
                else {
                    //apiposition entspricht der oberkategorie id
                    int apiposition=gewählteOberkategorie-1;
                    String [] a={"1,2"};
                    zeigeAlleTräger(a);
                    //String [] Unterkategorien = Connector.getUnterkategorie(apiposition);

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

                        String [] alleTraegerderok= Connector.ErgebnisderSuche(idok,0,"null");
                        zeigeAlleTräger(alleTraegerderok);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }







                }


                //spinner 2 erhält unterkategorien

                //TESTEST
                //Spinner spinnerO = (Spinner) findViewById(R.id.spinner);
                // text.setText(spinnerO.getSelectedItem().toString());




            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Hier muss mit Connector ein Array mit allen Trägern erzeugt werden
                //String[] alleTräger = {"T1", "T2"};
                // Fülle die Listview mit dem Array alle Träger
                //zeigeAlleTräger(alleTräger);


            }

        };


        spinner.setOnItemSelectedListener(listener1);





        OnItemSelectedListener listener2 = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int gewählteUnterkategorie_, long id) {

                gewählteUnterkategorie = gewählteUnterkategorie_;
                //Wenn alles anzeigen ausgewählt ist, zeige alle Träger an
                if (gewählteUnterkategorie==0) {

                    //Zeige alle Ergebnisse für alle Unterkategorien der Oberkategorie
                    //zeigeAlleTräger(alleTraeger);
                    Spinner spinner = (Spinner)findViewById(R.id.spinner);
                    String namederOberkategorie = spinner.getSelectedItem().toString();

                    idok=Connector.getOberkategorieId(namederOberkategorie);

                    String [] alleTraegerderok= new String[0];
                    try {
                        alleTraegerderok = Connector.ErgebnisderSuche(idok,0,"null");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    zeigeAlleTräger(alleTraegerderok);



                }
                else {
                    //TextView aktualsieren, zeige alle Träger für gewählte Unterkategorie
                    String [] a= {"ausgewählte unterkategorie"};
                    zeigeAlleTräger(a);

                    //Unterkategorie bekommen
                    Spinner spinnerU = (Spinner)findViewById(R.id.spinnerU);
                    String namederUnterkategorie= spinnerU.getSelectedItem().toString();

                    iduk=Connector.getUnterkategorieId(namederUnterkategorie);

                    String [] alletraegerderuk= new String [0];
                    try {
                        alletraegerderuk=Connector.ErgebnisderSuche(idok,iduk,"null");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    zeigeAlleTräger(alletraegerderuk);


                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Hier muss mit Connector ein Array mit allen Trägern erzeugt werden
                //String[] alleTräger = {"T1", "T2"};
                // Fülle die Listview mit dem Array alle Träger
                //zeigeAlleTräger(alleTräger);


            }

        };
        spinnerU.setOnItemSelectedListener(listener2);



    }

    public void zeigeAlleTräger(String [] alleTräger){
        //Zeige alle Träger
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












}
