package fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.learnmiwok.R;

import java.util.ArrayList;
import java.util.List;


public class app_squareFragment extends Fragment {
    private View view;
    private TabLayout my_table;
    private ViewPager viewPager;

    //放进集合
    private List<String> tas=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_app_square, container, false);
        initView(view);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tas.add("发现");
        tas.add("心理");
        //设置适配器  注意：getChildFragmentManager
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        //建立关联
        my_table.setupWithViewPager(viewPager);
        //一次加载所有的页面
        viewPager.setOffscreenPageLimit(tas.size());
    }
        //获得控件
    private void initView(View view) {
        my_table = (TabLayout) view.findViewById(R.id.tab);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
    }
        //    写一个适配器
    class MyAdapter extends FragmentPagerAdapter {
        //得到页面的title,会添加到tabLayout控件上
        @Override
        public CharSequence getPageTitle(int position) {

            return tas.get(position);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment f=null;
            //            进行判断
            switch (position){
                case 0: f=new TopOneFragment(); break;
                case 1: f=new TopTwoFragment(); break;

            }
            return f;
        }
                //view的页数
        @Override
        public int getCount() {

            return tas.size();
        }
        public MyAdapter(FragmentManager fm) {

            super(fm);
        }
    }
}

