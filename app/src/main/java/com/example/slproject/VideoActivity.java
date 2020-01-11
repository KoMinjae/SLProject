package com.example.slproject;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.slproject.MainActivity.db;



public class VideoActivity extends AppCompatActivity {
    ArrayList textlist;
    ArrayList poslist;
    RecyclerAdapter adapter;

    Cursor cursor;
    Data data;

    //RecyclerView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        init();

        textlist = new ArrayList();
        poslist = new ArrayList();

        textlist = getIntent().getExtras().getStringArrayList("text");
        poslist = getIntent().getExtras().getStringArrayList("pos");

        System.out.println("111");

        for (int i = 0; i < textlist.size(); i++) {

            System.out.println(textlist.get(i));
            System.out.println(poslist.get(i));

            String path = "";
            String title = "";
            String url = "";
            String exp = "";
            String sqlSelectVV = "SELECT * FROM Dictionary WHERE title IN " + "(\'" + textlist.get(i) + "다" + "\')";
            String sqlSelectVA = "SELECT * FROM Dictionary WHERE TITLE LIKE \'" + textlist.get(i) + "%다" + "\'" + "AND POS=\'형용사\'";
            String sqlSelect = "SELECT * FROM (SELECT * FROM Dictionary WHERE pos != '동사' OR pos != '형용사') WHERE title IN " + "(\'" + textlist.get(i) + "\')";

            if(poslist.get(i).equals("VV")){

                cursor = db.rawQuery(sqlSelectVV, null);
                startManagingCursor(cursor);

            }

            else if(poslist.get(i).equals("VA")){

                cursor = db.rawQuery(sqlSelectVA, null);
                startManagingCursor(cursor);

            }

            else{

                cursor = db.rawQuery(sqlSelect, null);
                startManagingCursor(cursor);

            }

            while(cursor.moveToNext()) {

                title = cursor.getString(1);
                url = cursor.getString(2);
                exp = cursor.getString(4);

                System.out.println(exp + "설명입니다.");
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@");

                data = new Data();
                data.setTitle(title);
                data.setUrl(url);

                adapter.addItem(data);
            }

        }

        adapter.notifyDataSetChanged();

    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

        // adapter에 들어갈 list 입니다.
        private ArrayList<Data> listData = new ArrayList<>();


        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
            // return 인자는 ViewHolder 입니다.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_part, parent, false);
            return new ItemViewHolder(view);
        }



        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
            holder.onBind(listData.get(position));
        }

        @Override
        public int getItemCount() {
            // RecyclerView의 총 개수 입니다.
            return listData.size();
        }

        void addItem(Data data) {
            // 외부에서 item을 추가시킬 함수입니다.
            listData.add(data);
        }

        // RecyclerView의 핵심인 ViewHolder 입니다.
        // 여기서 subView를 setting 해줍니다.
        class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;
            private VideoView videoView;

            ItemViewHolder(View itemView) {
                super(itemView);

                textView = itemView.findViewById(R.id.textView1);
                videoView = itemView.findViewById(R.id.videoView);
                MediaController controller = new MediaController(VideoActivity.this);
                videoView.setMediaController(controller);


            }

            void onBind(Data data) {
                textView.setText(data.getTitle());
                videoView.setVideoPath(data.getUrl());
            }
        }
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    public class Data {

        private String title;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}