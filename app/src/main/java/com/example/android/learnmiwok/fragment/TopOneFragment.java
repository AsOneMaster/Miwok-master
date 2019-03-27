package com.example.android.learnmiwok.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSONObject;
import com.example.android.learnmiwok.EditTextUtils;
import com.example.android.learnmiwok.MyApp;
import com.example.android.learnmiwok.R;
import com.example.android.learnmiwok.acticity.App_Activity;
import com.example.android.learnmiwok.adapter.ListViewDefaultAdapter;
import com.example.android.learnmiwok.bean.LocationBean;
import com.example.android.learnmiwok.common.ConnectionManager;
import com.example.android.learnmiwok.common.SessionManager;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class TopOneFragment extends Fragment {

    private String  msg;
    private String r_msg;
    private ListView listView;
    private EditText input_msg;
    private ImageView delete_mag;
    private LinearLayout send_msg;
    private static final List<String>data = new ArrayList<String>(){};
    private View view;
    private boolean is_me;

    MessageReceiver receiver = new MessageReceiver();


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView = (ListView)view.findViewById(R.id.card_listView);
        /*添加头和尾*/
        listView.addHeaderView(new View(getActivity().getApplicationContext()));
        listView.addFooterView(new View(getActivity().getApplicationContext()));
        listView.setAdapter(new TopOneFragment.MyAdapter(data));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_top_one, container, false);
//        registerReceiver
        IntentFilter filter = new IntentFilter(ConnectionManager.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(receiver, filter);
        initView(view);

        return view;
    }

    private void initView(View view){
        input_msg=(EditText)view.findViewById(R.id.input_msg);
        delete_mag=(ImageView)view.findViewById(R.id.delete_msg);
        send_msg=(LinearLayout) view.findViewById(R.id.send_msg);
        EditTextUtils.clearButtonListener(input_msg,delete_mag,send_msg);
        send_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject lObject = new JSONObject();
                    lObject.put("Userid",MyApp.getInstance().getUserip());
                    lObject.put("client","event");
                    msg=input_msg.getText().toString();
                    input_msg.setText("");
                    data.add(msg);
                    lObject.put("eventMsg",msg);
                    is_me=true;
                    SessionManager.getInstance().writeToServer(lObject.toString());
                    hideSoftInputFromWindow();
                }
            });



    }
//    隐藏软键盘
    private void hideSoftInputFromWindow(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
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
        private TextView s_time;
        private TextView s_msg;
        private TextView s_addr;
        private TextView user_name;

        //  当执行new MyHolder() 会调用该方法
        @Override
        protected void refreshView(String data) {
            this.s_time.setText(getTime());
            this.s_msg.setText(data);
            this.s_addr.setText(MyApp.getInstance().getAddr());
            if(is_me){
                this.user_name.setText("我");
                user_name.setTextColor(getActivity().getResources().getColor(R.color.red,null));
                is_me=false;
            }else {
                this.user_name.setText("匿名用户");
                user_name.setTextColor(getActivity().getResources().getColor(R.color.event_user,null));
            }
        }
        @Override
        protected View initView() {
            View view=View.inflate(getActivity().getApplicationContext(), R.layout.list_item_card, null);
            this.s_time=(TextView) view.findViewById(R.id.send_time);
            this.s_msg=(TextView) view.findViewById(R.id.event_find);
            this.s_addr=(TextView)view.findViewById(R.id.addr);
            this.user_name=(TextView)view.findViewById(R.id.user_name);
            return view;
        }
        public String getTime() {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return df.format(date);
        }
    }
    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            r_msg = intent.getStringExtra(ConnectionManager.MESSAGE);
            Log.e("said",r_msg);
            if(r_msg!=null) {
                if (isJson(r_msg)) {

                        JSONObject object = JSONObject.parseObject(r_msg);
                    if(object.getString("event")!=null&&object.getString("event").equals("event")){
                        data.add(object.getString("eventMsg"));
                        is_me=false;
                    }




                    //接受通知

                }
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
