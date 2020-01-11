package com.example.slproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.impl.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import kr.co.shineware.nlp.komoran.model.Token;

public class STTactivity extends AppCompatActivity implements  View.OnClickListener, SpeechRecognizeListener {
    private SpeechRecognizerClient client;
    TextView textView;
    List<Token> stemtext;
    ArrayList textlist = new ArrayList();
    ArrayList poslist = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sttactivity);
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        //findViewById(R.id.cancelbutton).setOnClickListener(this);
        findViewById(R.id.restartbutton).setOnClickListener(this);
        findViewById(R.id.okbutton).setOnClickListener(this);
        //findViewById(R.id.stopbutton).setOnClickListener(this);
        textView = (TextView)findViewById(R.id.textView);
        if(PermissionUtils.checkAudioRecordPermission(this)) {
            SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION);
            client = builder.build();
            client.setSpeechRecognizeListener(this);
            client.startRecording(true);
            System.out.println("녹음중");
            setButtonsStatus(true);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // API를 더이상 사용하지 않을 때 finalizeLibrary()를 호출한다.
        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }
    private void setButtonsStatus(boolean enabled) {
        //findViewById(R.id.cancelbutton).setEnabled(!enabled);
        findViewById(R.id.restartbutton).setEnabled(!enabled);
        findViewById(R.id.okbutton).setEnabled(!enabled);
        //findViewById(R.id.stopbutton).setEnabled(!enabled);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.restartbutton) {

            SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION);
            client = builder.build();
            client.setSpeechRecognizeListener(this);
            System.out.println("재시작");
            client.cancelRecording();
            client.startRecording(true);
        }
        else if(id == R.id.okbutton){

            Intent intent = new Intent(STTactivity.this, VideoActivity.class);
            intent.putExtra("text",textlist);
            intent.putExtra("pos",poslist);

            startActivity(intent);


        }
        /*else if (id == R.id.stopbutton) {
            if (client != null) {
                System.out.println("스탑");
                client.stopRecording();
            }
        }*/
    }



    @Override
    public void onReady() {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int errorCode, String errorMsg) {

    }

    @Override
    public void onPartialResult(String partialResult) {

    }

    @Override
    public void onResults(Bundle results) {
        final StringBuilder builder = new StringBuilder();
        Log.i("SpeechActivity", "onResults");
        ArrayList<String> texts = new ArrayList<String>();
        texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);

        stemtext = stemmer.getStem(texts.get(0));

        textlist.clear();
        poslist.clear();
        for(Token token : stemtext){
             textlist.add(token.getMorph());
             poslist.add(token.getPos());
        }

        System.out.println(textlist.get(0));
        System.out.println(poslist.get(0));


        textView.setText(texts.get(0));
        setButtonsStatus(false);

        client = null;
    }

    @Override
    public void onAudioLevel(float audioLevel) {

    }

    @Override
    public void onFinished() {

    }
}