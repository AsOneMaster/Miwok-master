package com.example.android.learnmiwok.view.adapter;

import java.util.List;



import com.example.android.learnmiwok.R;
import com.example.android.learnmiwok.ycontact.model.SortModel;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SortAdapter extends BaseAdapter implements SectionIndexer
{
	private List<SortModel> list = null;
	private Context mContext;

	public SortAdapter( Context mContext , List<SortModel> list)
	{
		super();
		this.list = list;
		this.mContext = mContext;
	}
	
	// when the data changed , call updateListView() to update
	public void updateListView(List<SortModel> list)
	{
		this.list = list;
		notifyDataSetChanged();
	}
	
	
	public int getCount()
	{
		return this.list.size();
	}

	public Object getItem(int pos)
	{
		return this.list.get(pos);
	}

	public long getItemId(int pos)
	{
		return pos;
	}

	public View getView(int pos, View view, ViewGroup group)
	{
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(pos);
		if (view == null)
		{
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.user_list_item, null);
			viewHolder.tvId = (TextView) view.findViewById(R.id.txt_user_id);
			viewHolder.tvName = (TextView) view.findViewById(R.id.txt_user_name);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.txt_catalog);
			viewHolder.tvInfo = (TextView) view.findViewById(R.id.txt_user_list_info);
			view.setTag(viewHolder);
		}
		else 
			viewHolder = (ViewHolder) view.getTag();
		
		// get position and get the first letter
		int section = getSectionForPosition(pos);
		
		if(pos == getPositionForSection(section))
		{
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		}
		else
			viewHolder.tvLetter.setVisibility(View.GONE);
		
		viewHolder.tvId.setText(this.list.get(pos).getId());
		viewHolder.tvName.setText(this.list.get(pos).getName());
		viewHolder.tvInfo.setText(this.list.get(pos).getInfo());
		
		return view;
	}

	final static class ViewHolder 
	{
		TextView tvId;
		TextView tvLetter;
		TextView tvName;
		TextView tvInfo;
	}
	
	public int getPositionForSection(int section)
	{
		for (int i = 0; i < getCount(); i++)
		{
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section)
				return i;
		}
		
		return -1;
	}

	public int getSectionForPosition(int arg0)
	{
		return this.list.get(arg0).getSortLetters().charAt(0);
	}

	
	public Object[] getSections()
	{
		return null;
	}

	private String getAlpha(String str) 
	{
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		if (sortStr.matches("[A-Z]")) 
			return sortStr;
		else 
			return "#";
	}
	
}
