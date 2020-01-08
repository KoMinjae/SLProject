package com.example.slproject;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.example.slproject.MainActivity.db;

public class VideoActivity extends AppCompatActivity {
    VideoView videoView;
    TextView textView;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        VideoView videoView = (VideoView)findViewById(R.id.videoView);
        String path = "";
        videoView.setVideoPath(path);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setPadding(0,0,0,88);
        videoView.setMediaController(mediaController);
        videoView.start();
        String sqlSelect = "SELECT * FROM Dictionary";
        TextView textView = (TextView)findViewById(R.id.textView4);
        cursor = db.rawQuery(sqlSelect, null);
        startManagingCursor(cursor);
        ArrayList textlist = new ArrayList();
        textlist = getIntent().getParcelableArrayListExtra("key");
        System.out.println(textlist);
        if(cursor.moveToFirst()) {
            String a= cursor.getString(1);
            textView.setText(a);
        }

    }
}
