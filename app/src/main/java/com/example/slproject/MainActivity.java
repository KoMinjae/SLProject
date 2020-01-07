package com.example.slproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{
    Button speechbutton;
    Button slbutton;
    Button helpbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speechbutton = (Button)findViewById(R.id.speechbutton);
        slbutton = (Button)findViewById(R.id.slbutton);
        helpbutton = (Button)findViewById(R.id.helpbutton);

        /*
        textview = (TextView)findViewById(R.id.textView);
        GetXMLTask task = new GetXMLTask();
        task.execute("http://175.125.91.94/oasis/service/rest/meta13/getCTE01701");
        */

        speechbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, STTactivity.class);
                startActivity(intent);
            }
        });

        slbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SLActivity.class);
                startActivity(intent);
            }
        });

        helpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
    }
/*
    private class GetXMLTask extends AsyncTask<String, Void, Document>{
        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

            }catch (Exception e){
                Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_SHORT).show();
            }
            return doc;
        } @Override
        protected void onPostExecute(Document doc) {
            String s = "";
            NodeList nodeList = doc.getElementsByTagName("item");
            for(int i=0; i< nodeList.getLength(); i++){
                s += ""+ i + "정보 : ";
                Node node = nodeList.item(i);
                Element fstElmnt = (Element)node;
                NodeList nameList = fstElmnt.getElementsByTagName("subDescription");
                Element nameElement = (Element)nameList.item(0);
                nameList = nameElement.getChildNodes();
                s +="주소 = " + ((Node)nameList.item(0)).getNodeValue() + ", ";
                NodeList websiteList = fstElmnt.getElementsByTagName("title");
                s +="명칭 = " + websiteList.item(0).getChildNodes().item(0).getNodeValue()+"\n";
            }
            textview.setText(s);
            super.onPostExecute(doc);
        }

    }*/
}

