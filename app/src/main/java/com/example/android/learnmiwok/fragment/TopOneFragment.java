package com.example.android.learnmiwok.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.learnmiwok.R;


public class TopOneFragment extends Fragment {

    private ListView listView;
    private static final String[] data = { "北京", "上海", "武汉", "广州", "西安", "南京", "合肥","上海", "武汉", "广州", "西安", "南京", "合肥" };
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_top_one, container, false);
        listView = (ListView)view.findViewById(R.id.card_listView);
        /*添加头和尾*/
        listView.addHeaderView(new View(getActivity().getApplicationContext()));
        listView.addFooterView(new View(getActivity().getApplicationContext()));
        listView.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item_card, R.id.line1, data));


        return view;
    }


}
