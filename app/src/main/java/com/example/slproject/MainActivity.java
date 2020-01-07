package com.example.slproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

        Document doc = null;
        TextView textview;

        private final String dbName = "slp";
        private final String tableName = "dictionary";

        String titles[] = new String[10];
        String links[] = new String[10];

        ArrayList<HashMap<String, String>> wordList;

        ListView list;

        private static final String TAG_TITLE = "title";
        private static final String TAG_URL ="url";

        SQLiteDatabase sampleDB = null;
        ListAdapter adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

        textview = (TextView)findViewById(R.id.result);
        GetXMLTask task = new GetXMLTask();
        task.execute("http://175.125.91.94/oasis/service/rest/meta13/getCTE01701?numOfRows=5");

        try {
            System.out.println(titles[0] + "DB연결이전");
            sampleDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
            //테이블이 존재하지 않으면 새로 생성합니다.
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                    + " (title VARCHAR(255), url VARCHAR(255) );");
            //테이블이 존재하는 경우 기존 데이터를 지우기 위해서 사용합니다.
            sampleDB.execSQL("DELETE FROM " + tableName  );

            //새로운 데이터를 테이블에 집어넣습니다..
            for (int i=0; i<titles.length; i++ ) {

                System.out.println(titles[i] + " @@ " + links[i] );

                sampleDB.execSQL("INSERT INTO " + tableName
                        + " (title, url)  Values ('" + titles[i] + "', '" + links[i]+"');");
            }
            sampleDB.close();
        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("", se.getMessage());
        }

        showList();
    }

    private class GetXMLTask extends AsyncTask<String, Void, Document> {
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

            String url = "";
            String title = "";
            int idx = 0;
            String urlResult = "";
            String result = "";

            NodeList nodeList = doc.getElementsByTagName("item");
            for(int i=0; i< nodeList.getLength(); i++){

                Node node = nodeList.item(i);
                Element fstElmnt = (Element)node;
                NodeList nameList = fstElmnt.getElementsByTagName("subDescription");
                Element nameElement = (Element)nameList.item(0);
                nameList = nameElement.getChildNodes();
                url = ((Node)nameList.item(0)).getNodeValue();

                idx = url.indexOf("||");

                urlResult = url.substring(5,idx);

                NodeList websiteList = fstElmnt.getElementsByTagName("title");
                title = websiteList.item(0).getChildNodes().item(0).getNodeValue();

                result += title + " " + urlResult;

                titles[i] = title;
                links[i] = urlResult;

            }

            super.onPostExecute(doc);
        }
    }

    protected void showList(){

        try {

            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);


            //SELECT문을 사용하여 테이블에 있는 데이터를 가져옵니다..
            Cursor c = ReadDB.rawQuery("SELECT * FROM " + tableName, null);

            if (c != null) {


                if (c.moveToFirst()) {
                    do {

                        //테이블에서 두개의 컬럼값을 가져와서
                        String title = c.getString(c.getColumnIndex("title"));
                        String url = c.getString(c.getColumnIndex("url"));

                        //HashMap에 넣습니다.
                        HashMap<String,String> words = new HashMap<String,String>();

                        words.put(TAG_TITLE,title);
                        words.put(TAG_URL,url);

                        //ArrayList에 추가합니다..
                        wordList.add(words);

                    } while (c.moveToNext());
                }
            }

            ReadDB.close();


            //새로운 apapter를 생성하여 데이터를 넣은 후..
            adapter = new SimpleAdapter(
                    this, wordList, R.layout.list_item,
                    new String[]{TAG_TITLE,TAG_URL},
                    new int[]{ R.id.title, R.id.url}
            );


            //화면에 보여주기 위해 Listview에 연결합니다.
            list.setAdapter(adapter);


        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("",  se.getMessage());
        }

    }
}