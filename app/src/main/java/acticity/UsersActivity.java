package acticity;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.learnmiwok.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UsersActivity extends AppCompatActivity {
    private EditText userName;
    private EditText passWord;
    private Button login;
    private Button enter;
    public static String pass;
    public static String user;
    private static String uri="http://192.168.42.33:8080/Te/login.d1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        //解决主线程不能连接连接服务器
//        StrictMode.setThreadPolicy(new
//                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
//        StrictMode.setVmPolicy(
//                new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());


        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_users);
//        setTitle("Numbers");
        userName=(EditText) findViewById(R.id.user_name);
        passWord=(EditText)findViewById(R.id.pass_word);
        login= (Button) findViewById(R.id.loginN);
        enter= (Button) findViewById(R.id.enterN);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        httpClient.post(uri,params,new TextHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {

                Toast.makeText(UsersActivity.this,""+responseBody,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Intent intent = new Intent(getApplicationContext(), App_Activity.class);
                if (intent != null) {

                    startActivity(intent);
                    finish();
                }

            }
        });
        httpClient.get(uri, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(statusCode==200){


                    for(int i=0;i<response.length();i++){
                        try {
                            user=response.getString("name");
                            pass=response.getString("pass");
                            Log.e("user",user);
                            Log.e("pass",pass);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }



                }
            }
        });
}

}
