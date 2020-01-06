package com.example.slproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.impl.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import kr.co.shineware.nlp.komoran.model.Token;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener, SpeechRecognizeListener{
    private SpeechRecognizerClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        findViewById(R.id.speechbutton).setOnClickListener(this);
        findViewById(R.id.cancelbutton).setOnClickListener(this);
        findViewById(R.id.restartbutton).setOnClickListener(this);
        findViewById(R.id.stopbutton).setOnClickListener(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        // API를 더이상 사용하지 않을 때 finalizeLibrary()를 호출한다.
        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }
    private void setButtonsStatus(boolean enabled) {
        findViewById(R.id.speechbutton).setEnabled(enabled);
        findViewById(R.id.cancelbutton).setEnabled(!enabled);
        findViewById(R.id.restartbutton).setEnabled(!enabled);
        findViewById(R.id.stopbutton).setEnabled(!enabled);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.speechbutton){
            if(PermissionUtils.checkAudioRecordPermission(this)) {
                SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION);
                client = builder.build();
                client.setSpeechRecognizeListener(this);
                client.startRecording(true);
                System.out.println("녹음중");

                setButtonsStatus(false);
            }
        }
        else if(id == R.id.cancelbutton){
            if(client != null){
                System.out.println("캔슬");
                client.cancelRecording();
            }
            setButtonsStatus(true);
        }
        else if (id == R.id.restartbutton) {
            if (client != null) {
                System.out.println("재시작");
                client.cancelRecording();
                client.startRecording(true);
            }
        }
        else if (id == R.id.stopbutton) {
            if (client != null) {
                System.out.println("스탑");
                client.stopRecording();
            }
        }
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
        Log.i("SpeechSampleActivity", "onResults");
        ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        List<Token> stemtext = stemmer.getStem(texts.get(0));
        for(Token token : stemtext){
            builder.append(token.getMorph());
            builder.append(token.getPos());
            builder.append("\n");
        }
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(activity.isFinishing()) return;

                AlertDialog.Builder dialog = new AlertDialog.Builder(activity).setMessage(builder.toString()).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                setButtonsStatus(true);
            }
        });
        client = null;
    }

    @Override
    public void onAudioLevel(float audioLevel) {

    }

    @Override
    public void onFinished() {

    }
}

