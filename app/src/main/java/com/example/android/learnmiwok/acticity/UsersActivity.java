package com.example.android.learnmiwok.acticity;

import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.learnmiwok.MyApp;
import com.example.android.learnmiwok.R;
import com.example.android.learnmiwok.common.MinaService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import org.json.JSONException;
import org.json.JSONObject;

public class UsersActivity extends AppCompatActivity {

    public static String UserID;
    private EditText userName;
    private EditText passWord;
    private Button login;
    private Button enter;
    public static String isSuccess;

    private static String uri="http://192.168.43.162:8080/Te/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        //解决主线程不能连接连接服务器
//        StrictMode.setThreadPolicy(new
//                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
//        StrictMode.setVmPolicy(
//                new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_users);
//        setTitle("Numbers");
        userName=(EditText) findViewById(R.id.user_name);
        userName.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        passWord=(EditText)findViewById(R.id.pass_word);
        login= (Button) findViewById(R.id.loginN);
        enter= (Button) findViewById(R.id.enterN);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    public void login_u(View view){
        String uname=userName.getText().toString();
        String upass=passWord.getText().toString();

        AsyncHttpClient httpClient=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("userName",uname);
        params.put("passWord",upass);
        httpClient.post(uri,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(statusCode==200){


                    for(int i=0;i<response.length();i++){
                        try {
                            isSuccess=response.getString("login");
                            UserID=response.getString("userid");
                            if(UserID!=null){
                               MyApp.getInstance().setUserip(UserID);
                            }


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if ("success".equals(isSuccess)) {
                        Toast.makeText(UsersActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), App_Activity.class);
                        if (intent != null) {
                            startActivity(intent);

                            Log.e("tag", "connect to server");
                            finish();
                        }

                    } else {
                        Toast.makeText(UsersActivity.this, "密码或账户错误", Toast.LENGTH_LONG).show();
                    }


                }
            }
        });
}

}
