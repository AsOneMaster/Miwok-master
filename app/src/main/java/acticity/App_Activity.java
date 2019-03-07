package acticity;

import android.os.Handler;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.android.learnmiwok.R;

import java.util.ArrayList;
import java.util.List;

import adapter.FragmentTabAdapter;
import fragment.app_mineFragment;
import fragment.app_safety_Fragment;
import fragment.app_squareFragment;


public class App_Activity extends FragmentActivity {


    private RadioGroup rgs;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    public String hello = "hello ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_);
        fragments.add(new app_safety_Fragment());
        fragments.add(new app_squareFragment());
        fragments.add(new app_mineFragment());
        rgs = (RadioGroup) findViewById(R.id.tab_group);
        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.fragment, rgs);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                System.out.println("Extra---- " + index + " checked!!! ");
            }
        });

    }

    /**
     * 点击两次back退出程序
     */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(),
                    "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
        else{
                finish();
                System.exit(0);
            }

        }
    }

