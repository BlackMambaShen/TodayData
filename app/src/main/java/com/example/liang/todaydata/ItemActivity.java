package com.example.liang.todaydata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Intent intent = getIntent();
//        intent.putExtra("des",info.des);
//        intent.putExtra("title",info.title);
//        intent.putExtra("pic",info.pic);
//        intent.putExtra("time",time);
        String des = intent.getStringExtra("des");
        String title = intent.getStringExtra("title");
        String pic = intent.getStringExtra("pic");
        String time = intent.getStringExtra("time");
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_time = (TextView) findViewById(R.id.tv_time);
        TextView tv_des = (TextView) findViewById(R.id.tv_des);
        ImageView iv_pic = (ImageView) findViewById(R.id.iv_pic);
        Glide.with(this).load(pic).into(iv_pic);
        tv_title.setText(title);
        tv_time.setText(time);
        tv_des.setText(des);
    }
}
