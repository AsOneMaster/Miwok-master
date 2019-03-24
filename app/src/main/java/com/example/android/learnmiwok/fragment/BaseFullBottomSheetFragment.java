package com.example.android.learnmiwok.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.android.learnmiwok.MyApp;
import com.example.android.learnmiwok.R;
import com.example.android.learnmiwok.acticity.UsersActivity;
import com.example.android.learnmiwok.bean.LocationBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class BaseFullBottomSheetFragment extends BottomSheetDialogFragment {
    private static final String path="http://192.168.43.162:8080/Te/location";
    /**
     * 顶部向下偏移量
     */
    private int topOffset = 155;
    private ListView listView;
    public  static String msg;
    private List<LocationBean> datas;
    private BottomSheetBehavior<FrameLayout> behavior;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        return new BottomSheetDialog(getContext(), R.style.TransparentBottomSheetStyle);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView = (ListView)view .findViewById(R.id.event_listView);
        /*添加头和尾*/
        listView.addHeaderView(new View(getActivity().getApplicationContext()));
        listView.addFooterView(new View(getActivity().getApplicationContext()));
        listView.setAdapter(new MyAdapter(datas));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.bottom_sheet, container, false);


            msg=MyApp.getInstance().getMsg();

                JSONArray jsonarray = JSONArray.parseArray(msg);
                List<LocationBean> listGet = JSONObject.parseArray(jsonarray.toJSONString(), LocationBean.class);
                datas= new ArrayList<>(listGet.size());
                for (LocationBean entry:listGet){
                    LocationBean district = new LocationBean();
                    district.setAddr(entry.getAddr());
                    district.setEnd_addr(entry.getEnd_addr());
                    district.setLocationDescribe(entry.getLocationDescribe());
                    district.setEnd_locationDescribe(entry.getEnd_locationDescribe());
                    district.setDate(entry.getDate());
                    district.setEnd_date(entry.getEnd_date());
                    datas.add(district);
                }





        return dialogView;
    }
    @Override
    public void onStart() {
        super.onStart();
        // 设置软键盘不自动弹出
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = getHeight();
            behavior = BottomSheetBehavior.from(bottomSheet);
            // 初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }


    }
    /**
     * 获取屏幕高度
     *
     * @return height
     */
    private int getHeight() {
        int height = 1920;
        if (getContext() != null) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            if (wm != null) {
                // 使用Point已经减去了状态栏高度
                wm.getDefaultDisplay().getSize(point);
                height = point.y - getTopOffset();
            }
        }
        return height;
    }
    public int getTopOffset() {
        return topOffset;
    }
    public void setTopOffset(int topOffset) {
        this.topOffset = topOffset;
    }
    public BottomSheetBehavior<FrameLayout> getBehavior() {
        return behavior;
    }

    private class MyAdapter extends ListViewDefaultAdapter<LocationBean>{

        public MyAdapter(List<LocationBean> datas) {
            super(datas);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected BaseHolder<LocationBean> getHolder() {
            return new MyHolder();
        }



    }
    class MyHolder extends BaseHolder<LocationBean>{
        private TextView s_t;
        private TextView e_t;
        private TextView s_loc;
        private TextView e_loc;
        //  当执行new MainHolder() 会调用该方法
        @Override
        protected void refreshView(LocationBean data) {
            this.s_t.setText("出发时间："+data.getDate().toString());
            this.e_t.setText("到达时间："+data.getEnd_date().toString());
            this.s_loc.setText("出发地点："+(data.getAddr()+data.getLocationDescribe()));
            this.e_loc.setText("到达地点"+(data.getEnd_addr()+data.getEnd_locationDescribe()));
        }
        @Override
        protected View initView() {
            View view=View.inflate(getActivity().getApplicationContext(), R.layout.list_event, null);
            this.s_t=(TextView) view.findViewById(R.id.start_time);
            this.e_t=(TextView) view.findViewById(R.id.end_time);
            this.s_loc=(TextView) view.findViewById(R.id.start_location);
            this.e_loc=(TextView) view.findViewById(R.id.end_location);
            return view;
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
