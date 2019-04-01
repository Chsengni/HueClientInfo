package com.ch.hueclientinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ch.hueclientinfo.Activity.MediaActivity;
import com.ch.hueclientinfo.Activity.NewsActivity;
import com.ch.hueclientinfo.Activity.NoticesActivity;
import com.ch.hueclientinfo.Activity.StudyActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button news,notices,media,study;
    //private SweetAlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        news  = findViewById(R.id.news);
        notices = findViewById(R.id.notices);
        media = findViewById(R.id.media);
        study = findViewById(R.id.study);
        news.setOnClickListener(this);
        notices.setOnClickListener(this);
        media.setOnClickListener(this);
        study.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.news:
                Intent intent = new Intent(MainActivity.this,NewsActivity.class);
                startActivity(intent);
                break;
            case  R.id.notices:
                Intent intent1 = new Intent(MainActivity.this,NoticesActivity.class);
                startActivity(intent1);
                break;
            case R.id.media:
                Intent intent2 = new Intent(MainActivity.this,MediaActivity.class);
                startActivity(intent2);

                break;
            case  R.id.study:
                Intent intent3 = new Intent(MainActivity.this,StudyActivity.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }




}
