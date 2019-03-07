package acticity;

import android.os.Handler;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import android.view.View;import android.widget.ProgressBar;
import android.widget.RadioGroup;


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
        setContentView(R.layout.activity_app_);
        fragments.add(new app_safety_Fragment());
        fragments.add(new app_squareFragment());
        fragments.add(new app_mineFragment());
        rgs = (RadioGroup) findViewById(R.id.tab_group);
        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments,R.id.fragment, rgs);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener(){
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                System.out.println("Extra---- " + index + " checked!!! ");
            }
        });

    }
}
