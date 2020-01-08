package com.example.slproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends AppCompatActivity{
    Button speechbutton;
    Button slbutton;
    Button helpbutton;

    Document doc[] = new Document[44];
    TextView textview;

    static SQLiteHelper sqLiteHelper;
    static SQLiteDatabase db;
    Cursor cursor;

    String titles[] = new String[12931];
    String links[] = new String[12931];


    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speechbutton = (Button)findViewById(R.id.speechbutton);
        slbutton = (Button)findViewById(R.id.slbutton);
        helpbutton = (Button)findViewById(R.id.helpbutton);
        textview = (TextView)findViewById(R.id.result);

        sqLiteHelper = new SQLiteHelper(this);
        db = sqLiteHelper.getWritableDatabase();

        GetXMLTask task = new GetXMLTask();
        String sqlSelect = "SELECT * FROM Dictionary";
        boolean checkDB = false;
        Cursor checkresult = db.rawQuery(sqlSelect, null);
        if(checkresult.moveToFirst())
            checkDB = true;
        if(checkDB==false)
            task.execute("http://175.125.91.94/oasis/service/rest/meta13/getCTE01701?numOfRows=300&pageNo=");

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
                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
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
    private class GetXMLTask extends AsyncTask<String, Void, Document[]> {

        @Override
        protected Document[] doInBackground(String... urls) {

            URL url;
            try {
                for(int i = 0; i<44; i++) {
                    String pageno = Integer.toString(i+1);
                    url = new URL(urls[0]+pageno);
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    doc[i] = db.parse(new InputSource(url.openStream()));
                    doc[i].getDocumentElement().normalize();
                }
            }catch (Exception e){
                Looper.prepare();
                Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            return doc;
        } @Override
        protected void onPostExecute(Document doc[]) {

            String path = "";
            String title = "";
            int idx = 0;
            String urlResult = "";
            String result = "";
            int id = 1000;
            for(int j=0;j<44;j++){
                NodeList nodeList = doc[j].getElementsByTagName("item");
                    for(int i=0; i< nodeList.getLength(); i++){

                        Node node = nodeList.item(i);
                        Element fstElmnt = (Element)node;
                        NodeList nameList = fstElmnt.getElementsByTagName("subDescription");
                        Element nameElement = (Element)nameList.item(0);
                        nameList = nameElement.getChildNodes();
                        path = ((Node)nameList.item(0)).getNodeValue();

                        idx = path.indexOf("||");

                        urlResult = path.substring(5,idx);

                        NodeList websiteList = fstElmnt.getElementsByTagName("title");
                        title = websiteList.item(0).getChildNodes().item(0).getNodeValue();

                        result += title + " " + urlResult;
                        db.execSQL("INSERT INTO Dictionary VALUES('"+id+"', '" +title+"', '"+ urlResult +"');");

                        id+=1;
                    }

                /*select
                String sqlSelect = "SELECT * FROM Dictionary";
                cursor = db.rawQuery(sqlSelect, null);
                startManagingCursor(cursor);
                while(cursor.moveToNext()) {
                    titles[i] = cursor.getString(1);
                    links[i] = cursor.getString(2);
                 }*/
            }


            super.onPostExecute(doc);
        }
    }

}

