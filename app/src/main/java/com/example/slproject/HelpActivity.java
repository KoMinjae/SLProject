package com.example.slproject;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    float density;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        textView1 = (TextView)findViewById(R.id.hptext1);
        textView2 = (TextView)findViewById(R.id.hptext2);
        textView3 = (TextView)findViewById(R.id.hptext3);
        textView4 = (TextView)findViewById(R.id.hptext4);
        textView5 = (TextView)findViewById(R.id.hptext5);
        textView1.setTextSize((float) (getStandardSizeX()/50 ));
        textView2.setTextSize((float) (getStandardSizeX()/80 ));
        textView3.setTextSize((float) (getStandardSizeX()/80 ));
        textView4.setTextSize((float) (getStandardSizeX()/80 ));
        textView5.setTextSize((float) (getStandardSizeX()/80 ));
        textView1.setTextSize((float) (getStandardSizeY()/45));
        textView2.setTextSize((float) (getStandardSizeY()/45));
        textView3.setTextSize((float) (getStandardSizeY()/45));
        textView4.setTextSize((float) (getStandardSizeY()/45));
        textView5.setTextSize((float) (getStandardSizeY()/45));

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
