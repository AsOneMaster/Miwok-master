package com.example.android.learnmiwok.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;


import com.alibaba.fastjson.JSONObject;
import com.example.android.learnmiwok.R;
import com.example.android.learnmiwok.adapter.ListViewDefaultAdapter;
import com.example.android.learnmiwok.common.ConnectionManager;


import java.util.ArrayList;
import java.util.List;


public class TopOneFragment extends Fragment {

    private String  msg;
    private ListView listView;
    private static final List<String>data = new ArrayList<String>(){};
    private View view;

    MessageReceiver receiver = new MessageReceiver();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_top_one, container, false);
//        registerReceiver
        IntentFilter filter = new IntentFilter(ConnectionManager.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(receiver, filter);

        listView = (ListView)view.findViewById(R.id.card_listView);
        /*添加头和尾*/
        listView.addHeaderView(new View(getActivity().getApplicationContext()));
        listView.addFooterView(new View(getActivity().getApplicationContext()));
        listView.setAdapter(new TopOneFragment.MyAdapter(data));


        return view;
    }
    private class MyAdapter extends ListViewDefaultAdapter<String> {

        public MyAdapter(List<String> data) {
            super(data);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected BaseHolder<String> getHolder() {
            return new MyHolder();
        }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(receiver);
    }

    class MyHolder extends BaseHolder<String>{
        private TextView s_t;
        private TextView e_t;
        private TextView s_loc;
        private TextView e_loc;
        //  当执行new MainHolder() 会调用该方法
        @Override
        protected void refreshView(String data) {
            this.s_t.setText("出发时间："+data);

        }
        @Override
        protected View initView() {
            View view=View.inflate(getActivity().getApplicationContext(), R.layout.list_event, null);
            this.s_t=(TextView) view.findViewById(R.id.start_time);
            return view;
        }
    }
    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            msg = intent.getStringExtra(ConnectionManager.MESSAGE);
            if (isJson(msg)) {

                JSONObject object = JSONObject.parseObject(msg);
                //接受通知

            }


        }

        public boolean isJson(String content) {
            try {
                JSONObject.parseObject(content);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }


}
