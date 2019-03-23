package com.example.android.learnmiwok.acticity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.learnmiwok.R;

public class MyMessageActivity extends AppCompatActivity {
    private TextView myTitle;
    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_message);
        myTitle=(TextView)findViewById(R.id.atm);
        myTitle.setText("消息");
        img_back=(ImageView)findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }
    @Override
    public void finish() {
        super.finish();
        //设置当前Activity退出的动画和新Activity进入的动画
        overridePendingTransition(R.anim.not_ture_in, R.anim.slide_top_out);
    }

}
