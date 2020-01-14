package com.example.slproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.impl.util.PermissionUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import kr.co.shineware.nlp.komoran.model.Token;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SpeechRecognizeListener {
    Button speechbutton;
    Button slbutton;
    Button helpbutton;
    EditText editText;
    Document doc[] = new Document[44];
    TextView textview;
    static SQLiteHelper sqLiteHelper;
    static SQLiteDatabase db;
    Cursor cursor;
    String titles[] = new String[12931];
    String links[] = new String[12931];
    List<Token> stemtext;
    ArrayList textlist = new ArrayList();
    ArrayList poslist = new ArrayList();
    TextView textView;
    ProgressDialog dialog;
    private BottomNavigationView bottomNavigationView;
    private SpeechRecognizerClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new ProgressDialog(this);

        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        findViewById(R.id.restartbutton).setOnClickListener(this);
        findViewById(R.id.okbutton).setOnClickListener(this);
        findViewById(R.id.searchbutton).setOnClickListener(this);
        editText = (EditText)findViewById(R.id.editText2);

        //findViewById(R.id.stopbutton).setOnClickListener(this);
        textView = (TextView) findViewById(R.id.textView);
        sqLiteHelper = new SQLiteHelper(this);

        db = sqLiteHelper.getWritableDatabase();
        GetXMLTask task = new GetXMLTask();
        String sqlSelect = "SELECT * FROM Dictionary";
        boolean checkDB = false;
        Cursor checkresult = db.rawQuery(sqlSelect, null);
        if (checkresult.moveToFirst())
            checkDB = true;
        if (checkDB == false)
            task.execute("http://175.125.91.94/oasis/service/rest/meta13/getCTE01701?numOfRows=300&pageNo=");

        bottomNavigationView = findViewById(R.id.bottomNavi);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.action_mic:
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.action_qna:
                        Intent intent1 = new Intent(MainActivity.this, HelpActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.action_cloud:
                        Intent intent2 = new Intent(MainActivity.this, SLActivity.class);
                        startActivity(intent2);
                        break;

                }
                return true;
            }
        });
    }

    private class GetXMLTask extends AsyncTask<String, Void, Document[]> {

        @Override
        protected void onPreExecute() {

            dialog.setMessage("수어사전을 다운로드 중입니다");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Document[] doInBackground(String... urls) {

            URL url;
            try {
                for (int i = 0; i < 44; i++) {
                    String pageno = Integer.toString(i + 1);
                    url = new URL(urls[0] + pageno);
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    doc[i] = db.parse(new InputSource(url.openStream()));
                    doc[i].getDocumentElement().normalize();
                }
            } catch (Exception e) {
                Looper.prepare();
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc[]) {

            String abs = "";
            String title = "";
            String path = "";

            int idxTemp = 0;
            String absResult = "";
            String exp = "";

            int idx = 0;
            String urlResult = "";

            String result = "";
            int id = 1000;
            for (int j = 0; j < 44; j++) {
                idxTemp = 0;
                NodeList nodeList = doc[j].getElementsByTagName("item");
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Node node = nodeList.item(i);
                    Element fstElmnt = (Element) node;
                    NodeList nameList = fstElmnt.getElementsByTagName("subDescription");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();
                    path = ((Node) nameList.item(0)).getNodeValue();

                    idx = path.indexOf("||");

                    urlResult = path.substring(5, idx);

                    NodeList websiteList = fstElmnt.getElementsByTagName("title");
                    title = websiteList.item(0).getChildNodes().item(0).getNodeValue();

                    NodeList absList = fstElmnt.getElementsByTagName("abstractDesc");
                    abs = absList.item(0).getChildNodes().item(0).getNodeValue();

                    idxTemp = abs.indexOf("]");

                    if (idxTemp != -1)
                        absResult = abs.substring(1, idxTemp);
                    else
                        absResult = "";

                    if (idxTemp != -1) {

                        exp = abs.substring(idxTemp + 1);

                        exp = exp.replace("\'", "\"");

                    } else
                        exp = "";

                    result += title + " " + urlResult;
                    db.execSQL("INSERT INTO Dictionary VALUES('" + id + "', '" + title + "', '" + urlResult + "', '" + absResult + "', '" + exp + "' );");

                    id += 1;
                }

            }


            super.onPostExecute(doc);
            dialog.dismiss();


        }
    }

    private void setButtonsStatus(boolean enabled) {

        findViewById(R.id.restartbutton).setEnabled(enabled);
        findViewById(R.id.okbutton).setEnabled(enabled);
        findViewById(R.id.searchbutton).setEnabled(enabled);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.restartbutton) {
            if (PermissionUtils.checkAudioRecordPermission(this)) {
                SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION);
                client = builder.build();
                client.setSpeechRecognizeListener(this);
                client.cancelRecording();
                client.startRecording(true);
            }
        } else if (id == R.id.okbutton) {

            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
            intent.putExtra("text", textlist);
            intent.putExtra("pos", poslist);

            startActivity(intent);


        } else if (id == R.id.searchbutton) {

            String texts = editText.getText().toString();

            textlist.clear();
            poslist.clear();

            textlist.add(texts);
            poslist.add("단어검색");
            System.out.println("111");
            System.out.println(textlist.get(0));
            System.out.println(poslist.get(0));
            System.out.println("222");

            textView.setText(texts);


        }
    }
        public void onDestroy () {
            super.onDestroy();
            // API를 더이상 사용하지 않을 때 finalizeLibrary()를 호출한다.
            SpeechRecognizerManager.getInstance().finalizeLibrary();
        }
        @Override
        public void onReady () {

        }

        @Override
        public void onBeginningOfSpeech () {

        }

        @Override
        public void onEndOfSpeech () {

        }

        @Override
        public void onError ( int errorCode, String errorMsg){

        }

        @Override
        public void onPartialResult (String partialResult){

        }

        @Override
        public void onResults (Bundle results){
            final StringBuilder builder = new StringBuilder();
            Log.i("SpeechActivity", "onResults");
            ArrayList<String> texts = new ArrayList<String>();
            texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);

            stemtext = stemmer.getStem(texts.get(0));

            textlist.clear();
            poslist.clear();
            for (Token token : stemtext) {
                textlist.add(token.getMorph());
                poslist.add(token.getPos());
            }

            System.out.println("111");
            System.out.println(textlist.get(0));
            System.out.println(poslist.get(0));
            System.out.println("222");

            textView.setText(texts.get(0));

            client = null;
        }

        @Override
        public void onAudioLevel ( float audioLevel){

        }

        @Override
        public void onFinished () {

        }

    }


