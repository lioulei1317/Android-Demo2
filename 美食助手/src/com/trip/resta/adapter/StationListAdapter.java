package com.trip.resta.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.trip.resta.R;
import com.trip.resta.bean.Station;

public class StationListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Station> list;
	private LayoutInflater mInflater;

	public StationListAdapter(Context context, List<Station> list) {
		this.list = list;
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Station getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View rowView = null;
		if (convertView == null) {
			rowView = mInflater.inflate(R.layout.item_station_list, null);
		} else {
			rowView = convertView;
		}
		TextView tv_id = (TextView) rowView.findViewById(R.id.tv_id);
		TextView tv_name = (TextView) rowView.findViewById(R.id.tv_name);
		TextView tv_distance = (TextView) rowView
				.findViewById(R.id.tv_distance);
		TextView tv_addr = (TextView) rowView.findViewById(R.id.tv_addr);
		Station s = getItem(position);
		tv_id.setText((position + 1) + ".");
		tv_name.setText(s.getName());
		tv_distance.setText(s.getCity());
		tv_addr.setText(s.getAddress());
		return rowView;
	}

}
