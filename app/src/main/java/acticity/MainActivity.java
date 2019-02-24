package acticity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.Window;

import android.widget.ImageButton;

import com.example.android.learnmiwok.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉屏幕上的标题
        setContentView(R.layout.activity_main);

        Button numbers = (Button) findViewById(R.id.numbers);
        numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });

        Button family = (Button) findViewById(R.id.family);
        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FamilyActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }            }
        });
        ImageButton colors = (ImageButton) findViewById(R.id.colors);
        colors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalledActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }            }
        });
        ImageButton phrases = (ImageButton) findViewById(R.id.phrases);
        phrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MyMessageActivity.class);
//                if (intent != null) {
//                    startActivity(intent);
//                }
            }
        });

    }

//    @Override
       public void onClick(View view) {
            switch (view.getId()) {
                case R.id.numbers:
                    openNumbers(view);
                   break;
                case R.id.colors:
                    openColors(view);
                    break;
                case R.id.phrases:
                    openPhrases(view);
                    break;
                case R.id.family:
                    openFamily(view);
                    break;
                default:
                   break;
            }
        }

        public void openNumbers(View view) {
            Log.d("MainActiviy","in openNumbers");
            Intent intent = new Intent(this, UsersActivity.class);
            if (intent != null) {
                startActivity(intent);
            }
       }

        public void openColors(View view) {
            Intent intent = new Intent(this, CalledActivity.class);
            if (intent != null) {
                startActivity(intent);
            }
        }

       public void openFamily(View view) {
           Intent intent = new Intent(this, FamilyActivity.class);
            if (intent != null) {
                startActivity(intent);
           }
        }

       public void openPhrases(View view) {
             Intent intent = new Intent(this, MyMessageActivity.class);
            if (intent != null) {
                startActivity(intent);
            }
        }

    }
