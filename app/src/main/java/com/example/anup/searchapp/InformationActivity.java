package com.example.anup.searchapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/* Diese Activity liest info.txt ein. Auf dieser Seite können Informationen zur App stehen.*/
public class InformationActivity extends AppCompatActivity {


    TextView tv_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        tv_text=(TextView) findViewById(R.id.textView2);

        // Im TextView info.txt anzeigen

        String text="";
        try {
            InputStream inputStream = getAssets().open("info.txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            text= new String(buffer);
        }catch (IOException e){
            e.printStackTrace();
        }

        tv_text.setText(text);
    }
}
