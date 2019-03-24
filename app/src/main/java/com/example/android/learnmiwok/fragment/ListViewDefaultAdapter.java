package com.example.android.learnmiwok.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

abstract class ListViewDefaultAdapter<T> extends BaseAdapter {
    private List<T> datas;
    public ListViewDefaultAdapter(List<T> datas){
        this.datas=datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //convertView 划出屏幕的view对象
    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder<T> holder;
        if(convertView==null){
            holder=getHolder();
        }else{
        holder=(BaseHolder<T>)convertView.getTag();
        }
        T string = datas.get(position);
        //holder.refreshView(string);
        holder.setData(string);

        return holder.getContentView();
        }

protected abstract BaseHolder<T> getHolder();

        }
