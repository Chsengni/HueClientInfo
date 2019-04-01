package com.ch.hueclientinfo.Activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ch.hueclientinfo.R;

public class ContentActivity extends AppCompatActivity {
    private TextView textView;
    private TextView mtitle,msource;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        setTitle("详细内容");
        textView = findViewById(R.id.textview);
        mtitle = findViewById(R.id.mtitle);
        msource = findViewById(R.id.source);
        String contents =  getIntent().getStringExtra("contents");
        String title = getIntent().getStringExtra("title");
        String source = getIntent().getStringExtra("source");
        if (contents!=null){
            textView.setText("\t\t\t\t"+contents);
        }
        if (title!=null){
            mtitle.setText(title);
        }
        if (source!=null){
            msource.setText(source);
        }
    }
}
