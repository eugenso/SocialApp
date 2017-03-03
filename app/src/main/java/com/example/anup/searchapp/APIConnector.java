package com.example.anup.searchapp;


        import android.support.annotation.Nullable;
        import android.util.JsonReader;
        import android.widget.TextView;

        import org.json.simple.JSONArray;
        import org.json.simple.JSONObject;
        import org.json.simple.parser.JSONParser;
        import org.json.simple.parser.ParseException;

        import java.io.BufferedReader;
        import java.io.FileNotFoundException;
        import java.io.FileReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.StringReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Map;

        import static android.R.attr.id;
        import static android.R.attr.name;
        import static android.R.id.input;
        import static android.R.id.list;


/**
 * Created by Home on 01.03.17.
 */

public class APIConnector {

    Map<String, Integer> kategorieMap = new HashMap<>();
    Map<String, Integer> UkategorieMap= new HashMap<>();
    Map<String, JSONObject> traegerMap= new HashMap<>();

        //main zum Testen
    public static void main (String [] args)throws java.lang.Exception{

        APIConnector Connector = new APIConnector();
        String [] movies = Connector.getMovie();
        System.out.println(Arrays.toString(movies));

    }


    //String basisUrl="https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoList.txt";
    String basisUrl = "http://ltannotation.informatik.uni-hamburg.de/socialapp/rest";

    public String [] getMovie() throws Exception {
        String Kategorieanfrage=basisUrl;
        String zuParsen= readUrl(Kategorieanfrage);

        JSONParser jsonParser = new JSONParser();
        org.json.simple.JSONObject jsonObject =  (org.json.simple.JSONObject)jsonParser.parse(zuParsen);

        JSONArray MoviesListe = (JSONArray) jsonObject.get("movies");


        List<String> list = new ArrayList<String>();
        list.add("Alles anzeigen");
        for(int i = 0; i < MoviesListe.size(); i++){

            JSONObject obj = (JSONObject) MoviesListe.get(i);
            list.add(obj.get("movie").toString());
        }
        return (String[]) list.toArray(new String [MoviesListe.size()]);

    }
   /*public String zuParsen() throws Exception {
        String Kategorieanfrage=basisUrl;
        String zuParsen=readUrl(Kategorieanfrage);
        return zuParsen;
    }*/



    public String [] getOberkategorie() throws Exception {
        String Kategorieanfrage=basisUrl+"/offers/";
        String zuParsen= readUrl(Kategorieanfrage);

        JSONParser jsonParser = new JSONParser();
        org.json.simple.JSONObject jsonObject =  (org.json.simple.JSONObject)jsonParser.parse(zuParsen);

        JSONArray OberKategorieListe = (JSONArray) jsonObject.get("kategorie");

        kategorieMap.clear();

        List<String> list = new ArrayList<String>();
        list.add("Alles anzeigen");
        for(int i = 0; i < OberKategorieListe.size(); i++){

            JSONObject obj = (JSONObject) OberKategorieListe.get(i);
            list.add(obj.get("name").toString());

            kategorieMap.put(obj.get("name").toString(), Integer.parseInt(obj.get("id").toString()));
        }
        return (String[]) list.toArray(new String [OberKategorieListe.size()]);

    }

    public int getOberkategorieId(String name) {
        return kategorieMap.get(name);
    }



    public String [] getUnterkategorie(String Oberkategorie) throws Exception {

        int oberkategorieId = kategorieMap.get(Oberkategorie);
        String Kategorieanfrage=basisUrl+"/suboffersu/"+oberkategorieId;
        String zuParsen= readUrl(Kategorieanfrage);

        JSONParser jsonParser = new JSONParser();
        org.json.simple.JSONObject jsonObject =  (org.json.simple.JSONObject)jsonParser.parse(zuParsen);

        JSONArray OberKategorieListe = (JSONArray) jsonObject.get("unterkategorie");

        UkategorieMap.clear();

        List<String> list = new ArrayList<String>();
        list.add("Alles anzeigen");
        for(int i = 0; i < OberKategorieListe.size(); i++){

            JSONObject obj = (JSONObject) OberKategorieListe.get(i);
            list.add(obj.get("name").toString());

            UkategorieMap.put(obj.get("name").toString(), Integer.parseInt(obj.get("id").toString()));

        }
        return (String[]) list.toArray(new String [OberKategorieListe.size()]);

    }
    public int getUnterkategorieId(String name){
        return UkategorieMap.get(name);
    }



    public String [] listCarrier(String Oberkategorie, String Unterkategorie) throws Exception {

        int ok= kategorieMap.get(Oberkategorie);
        //int uk= kategorieMap;
        //String []okukcarrier=ErgebnisderSuche(Oberkategorie, Unterkategorie, null);
       // return okukcarrier;
        String [] test={"1","2"};
        return test;
    }


    public String [] ErgebnisderSuche(int Oberkategorie, int Unterkategorie, String keyword) throws Exception {


        String Kategorieanfrage=basisUrl+"/search/"+keyword+"/"+Oberkategorie+"/"+Unterkategorie;
        String zuParsen= readUrl(Kategorieanfrage);

        JSONParser jsonParser = new JSONParser();
        org.json.simple.JSONObject jsonObject =  (org.json.simple.JSONObject)jsonParser.parse(zuParsen);

        JSONArray ErgebnisSucheListe = (JSONArray) jsonObject.get("traeger");

        traegerMap.clear();

        //NULLPOINTER EXCEPTION ABFANGEN
        List<String> list = new ArrayList<String>();
        try {
        for(int i = 0; i < ErgebnisSucheListe.size(); i++){

            JSONObject obj = (JSONObject) ErgebnisSucheListe.get(i);
            list.add(obj.get("name").toString());

            traegerMap.put(obj.get("name").toString(), obj);

        }
            return (String[]) list.toArray(new String [ErgebnisSucheListe.size()]);

        }
        catch (NullPointerException e){
            //keine Ergebnisse für die Suche
            String [] keineergebnisse= new String [1];
            keineergebnisse[0]= "Keine Ergebnisse";
            return keineergebnisse;


        }

    }
    //Listet Details, wenn man auf ein Angebot klickt, Second Activity zeigt es an
    public String[] getDetails(String name) throws NullPointerException {

        //List<String> list = new ArrayList<String>();

       /* if (name.compareTo("a") == 0) {
            list.add("name a");
            list.add("adresse x");
        } else {
            list.add("name b");
            list.add("adresse y");
        }*/

        JSONParser jsonParser = new JSONParser();
        JSONObject tr = traegerMap.get(name);


        List<String> list = new ArrayList<String>();
        //Prüft ob Name,Angebot,Ansprechpartner verfügbar sind
        if (tr.get("name")!= null){
        list.add("Angebot:"+"\n"+tr.get("name").toString());
        }
        if (tr.get("angebot")!= null) {
            list.add("Beschreibung:"+"\n" + tr.get("angebot").toString());
        }
        if (tr.get("ansprechpartner")!= null) {
            list.add("Ansprechpartner:"+"\n" + tr.get("ansprechpartner").toString());
        }

        return (String[]) list.toArray(new String [list.size()]);

    }



    //neue Funktion für die neue Seite getdata
    //list.add("Adresse: " + obj.get("adresse"));
    //list.add("Zielpubli")
    @Nullable
    /*private String getResult(String Url){
        String Result="";
        String JSON_STRING="";

        try
        {
            URL url = new URL(Url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine())!= null)
            {
                stringBuilder.append(JSON_STRING+"\n");
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Result= stringBuilder.toString().trim();
            return Result;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }*/

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }


}
