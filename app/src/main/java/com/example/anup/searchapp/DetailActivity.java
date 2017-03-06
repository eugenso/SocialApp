package com.example.anup.searchapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/* Die Klasse stellt die Detailansicht eines ausgewählten Angebots dar. */

public class DetailActivity extends AppCompatActivity {

    ListView suchErgebnis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        suchErgebnis = (ListView) findViewById(R.id.Suchergebnis);

        Bundle bundle =getIntent().getExtras();
        if (bundle!= null)
        {
            // In den Details sind die Informationen zum ausgewählten Angebot gespeichert.
            String[] details = bundle.getStringArray("details");
            ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, details);
            suchErgebnis.setAdapter((myadapter));


        }

    }
}
