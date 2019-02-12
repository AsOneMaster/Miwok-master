package acticity;

import android.support.v4.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.android.learnmiwok.R;

import java.util.ArrayList;
import java.util.List;

import adapter.FragmentTabAdapter;
import fragment.app_mineFragment;
import fragment.app_safety_Fragment;
import fragment.app_squareFragment;


public class App_Activity extends FragmentActivity {
//    private  Menu_Activity menu_activity;
//    private  ImageView menu_back;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_app_);
//        //建立图片按钮事件监听
//        Button safe=(Button) findViewById(R.id.image_safety);
//        Button square=(Button)findViewById(R.id.image_square);
//        Button mine=(Button)findViewById(R.id.image_mine);
//        safe.setOnClickListener(l);
//        square.setOnClickListener(l);
//        mine.setOnClickListener(l);
//        menu_back=(ImageView)findViewById(R.id.menu_back);
//        menu_activity=(Menu_Activity)findViewById(R.id.menu_activity);
//        menu_back.setOnClickListener(m);
//
//    }
//    View.OnClickListener m=new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            menu_activity.switchMenu();
//        }
//    };
//    View.OnClickListener l=new View.OnClickListener(){
//        @Override
//        public void onClick(View view) {
//            FragmentManager fm=getFragmentManager();
//            FragmentTransaction ft=fm.beginTransaction();
//            Fragment fr=null;
//            //图片对应打开的Fragment
//            switch (view.getId()) {
//
//                case R.id.image_safety:
//                    fr=new app_safety_Fragment();
//                    break;
//                case R.id.image_square:
//                    fr=new app_squareFragment();
//                    break;
//                case R.id.image_mine:
//                    fr=new app_mineFragment();
//                    break;
//                case R.id.menu_back:
//                    menu_activity.switchMenu();
//                default:
//                    break;
//            }
//            ft.replace(R.id.fragment,fr);
//            ft.commit();
//    }
//
//    };

    private RadioGroup rgs;
    public List<Fragment> fragments = new ArrayList<Fragment>();

    public String hello = "hello ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main);

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
