package com.example.anup.searchapp;



        import org.json.simple.JSONArray;
        import org.json.simple.JSONObject;
        import org.json.simple.parser.JSONParser;


        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.List;
        import java.util.Map;


/**
 * Die Klasse stellt die Verbindung zum Server her. In Hashmaps werden eingelesene Daten gespeichert.
 * Über Funktionen können Oberkategorien,Untergategorien,Ids und auch Ergebnisse abgerufen werden.
 */


public class APIConnector {

    Map<String, Integer> kategorieMap = new HashMap<>();
    Map<String, Integer> UkategorieMap= new HashMap<>();
    Map<String, JSONObject> traegerMap= new HashMap<>();

        //main zum Testen
    public static void main (String [] args)throws java.lang.Exception{

    }


    //Die URL dient als Grundlage, in den Funktionen wird sie angepasst
    String basisUrl = "http://ltdemos.informatik.uni-hamburg.de/socialapp/rest";

    /* Die Funktionen listet alle Oberkategorien auf
     * @return String Array mit Oberkategorien */

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

    /* Die Funktion gibt zu einer Oberkategorie die zugehörige ID zurück
     * @param name Name der Oberkategorie
      * @return ID der Oberkategorie */
    public int getOberkategorieId(String name) {
        return kategorieMap.get(name);
    }


    /* Die Funktion gibt zu einer Oberkategorie die Unterkategorie
     * @param Oberkategorie Name der Oberkategorie
      * @return Unterkategorien der Oberkategorie */
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

    /* Die Funktion gibt zu einer Unterkategorie die zugehörige ID zurück
     * @param name Name der Unterkategorie
     * @return ID der Unterkategorie
      * */
    public int getUnterkategorieId(String name){
        return UkategorieMap.get(name);
    }

    /* Die Funktion gibt zu einer Oberkategorie die zugehörige ID zurück. Die Ergebnisse werden nach der Distanz zum Träger gelistet.
     * @param Oberkategorie ID der Oberkategorie
     * @param Unterkategorie ID der Unterkategorie
     * @param keyword Suchwort
     * @param latitude Latitude
     * @param longitude Longitude
     * @return String Array mit Trägern, auf die die Suche zutrifft, aufgelistet nach geringster Distanz
      *
      *
      * */
    public String [] ErgebnisderSucheGPS(int Oberkategorie, int Unterkategorie, String keyword, int latitude, int longitude) throws Exception {


        String Kategorieanfrage=basisUrl+"/geosearch/"+keyword+"/"+Oberkategorie+"/"+Unterkategorie+"/"+latitude+"/"+longitude;
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


    /*Listet Details, wenn man auf ein Angebot klickt, Second Activity zeigt es an
    * @param name Träger
    * @return String Array mit Details zum Träger
    *
    * */

    public String[] getDetails(String name) throws NullPointerException {


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


    /* Die Funktion gibt zu einer URL einen String zurück.
    * @param urlString URL
    *
    * */
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
