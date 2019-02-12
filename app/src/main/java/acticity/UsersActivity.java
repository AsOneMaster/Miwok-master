package acticity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.android.learnmiwok.R;

public class UsersActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_users);
//        setTitle("Numbers");
        Button login= (Button) findViewById(R.id.loginN);
        Button enter= (Button) findViewById(R.id.enterN);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), App_Activity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });

    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginN:
                openLogin(view);
                break;
            case R.id.enterN:
                openEnter(view);

            default:
                break;
        }
    }

    public void openLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        if (intent != null) {
            startActivity(intent);
        }
    }
    public void openEnter(View v) {
        Intent intent = new Intent(this, App_Activity.class);
        if (intent != null) {
            startActivity(intent);
        }
    }

}
