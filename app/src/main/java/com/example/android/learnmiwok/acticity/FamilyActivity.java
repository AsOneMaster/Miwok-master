package com.example.android.learnmiwok.acticity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.android.learnmiwok.MyApp;
import com.example.android.learnmiwok.R;

public class FamilyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉屏幕上的标题
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_family);
//        setTitle("Family");
        Button login= (Button) findViewById(R.id.loginS);
        Button enter= (Button) findViewById(R.id.enterS);
        MyApp.getInstance().setUserip("111");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                if (intent != null) {
                    startActivity(intent);
                    finish();
                }
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), App_Activity.class);
                if (intent != null) {
                    startActivity(intent);
                    finish();
                }
            }
        });


    }
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.loginS:
//                openLogin(view);
//                break;
//            case R.id.enterS:
//                openEnter(view);
//
//            default:
//                break;
//        }
//    }
//    public void openLogin(View v) {
//        Intent intent = new Intent(this, LoginActivity.class);
//        if (intent != null) {
//            startActivity(intent);
//            finish();
//        }
//    }
//    public void openEnter(View v) {
//        Intent intent = new Intent(this, App_Activity.class);
//        if (intent != null) {
//            startActivity(intent);
//            finish();
//        }
//    }

}
