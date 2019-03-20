package com.example.android.learnmiwok.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.android.learnmiwok.acticity.ContactSettingActivity;
import com.example.android.learnmiwok.acticity.PersonSettingActivity;
import com.example.android.learnmiwok.R;


public class app_mineFragment extends Fragment {
    private RelativeLayout mine_setting;
    private RelativeLayout contact_setting;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_app_head_mine, container, false);
        mine_setting=(RelativeLayout)view.findViewById(R.id.mine_setting);
        contact_setting=(RelativeLayout)view.findViewById(R.id.contact_personal_info_layout);
        mine_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity().getApplicationContext(),PersonSettingActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
        contact_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity().getApplicationContext(),ContactSettingActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
        return view;
    }


}
