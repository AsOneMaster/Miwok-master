package acticity;

import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.android.learnmiwok.R;
import fragment.app_mineFragment;
import fragment.app_safety_Fragment;
import fragment.app_squareFragment;


public class App_Activity extends AppCompatActivity {
    private  Menu_Activity menu_activity;
    private  ImageView menu_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉屏幕上的标题
        setContentView(R.layout.activity_app_);
        //建立图片按钮事件监听
        Button safe=(Button) findViewById(R.id.image_safety);
        Button square=(Button)findViewById(R.id.image_square);
        Button mine=(Button)findViewById(R.id.image_mine);
        safe.setOnClickListener(l);
        square.setOnClickListener(l);
        mine.setOnClickListener(l);
        menu_back=(ImageView)findViewById(R.id.menu_back);
        menu_activity=(Menu_Activity)findViewById(R.id.menu_activity);
        menu_back.setOnClickListener(m);

    }
    View.OnClickListener m=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menu_activity.switchMenu();
        }
    };
    View.OnClickListener l=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            Fragment fr=null;
            //图片对应打开的Fragment
            switch (view.getId()) {

                case R.id.image_safety:
                    fr=new app_safety_Fragment();
                    break;
                case R.id.image_square:
                    fr=new app_squareFragment();
                    break;
                case R.id.image_mine:
                    fr=new app_mineFragment();
                    break;
                case R.id.menu_back:
                    menu_activity.switchMenu();
                default:
                    break;
            }
            ft.replace(R.id.fragment,fr);
            ft.commit();
    }

    };


}
