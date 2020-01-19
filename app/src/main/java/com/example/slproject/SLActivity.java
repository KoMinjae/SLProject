package com.example.slproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SLActivity extends AppCompatActivity {
    TextView textView1;
    TextView textView3;
    TextView textView2;
    float density;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sl);
        textView1 = (TextView)findViewById(R.id.sltext1);
        textView2 = (TextView)findViewById(R.id.sltext2);
        imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView2 = (ImageView)findViewById(R.id.imageView2);
        imageView3 = (ImageView)findViewById(R.id.imageView3);
        imageView4 = (ImageView)findViewById(R.id.imageView4);
        imageView4.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);
        textView1.setText("해당 어플리케이션은 수어를 모르는 사람이 수어를 통하여 청각장애인과 소통을 돕기 위해 제작되었습니다. 또한 수어를 배우고자 하는 사람도 유용하게 사용 가능합니다.");
        textView2.setText("카카오 음성 API와 한글형태소 분석기 KOMORAN을 사용하였습니다.");

        textView1.setTextSize((float) (getStandardSizeX()/80 ));
        textView2.setTextSize((float) (getStandardSizeX()/80 ));
        textView1.setTextSize((float) (getStandardSizeY()/45));
        textView2.setTextSize((float) (getStandardSizeY()/45));
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView3.setVisibility(View.VISIBLE);
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView3.setVisibility(View.GONE);
            }
        });
        imageView2.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView4.setVisibility(View.VISIBLE);
            }
        }));
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView4.setVisibility(View.GONE);

            }
        });
        bottomNavigationView = findViewById(R.id.bottomNavi);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){

                    case R.id.action_mic:
                        Intent intent = new Intent(SLActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.action_qna:
                        Intent intent1 = new Intent(SLActivity.this, HelpActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.action_cloud:
                        Intent intent2 = new Intent(SLActivity.this, SLActivity.class);
                        startActivity(intent2);
                        break;

                }
                return true;
            }
        });

    }
    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return  size;
    }
    public int getStandardSizeX() {
        int standardSize_X=0;
        Point ScreenSize = getScreenSize(this);
        density  = getResources().getDisplayMetrics().density;
        standardSize_X = (int) (ScreenSize.x / density);
        return standardSize_X;
    }
    public int getStandardSizeY() {
        int standardSize_Y=0;
        Point ScreenSize = getScreenSize(this);
        density  = getResources().getDisplayMetrics().density;
        standardSize_Y = (int) (ScreenSize.y / density);
        return standardSize_Y;
    }
}
