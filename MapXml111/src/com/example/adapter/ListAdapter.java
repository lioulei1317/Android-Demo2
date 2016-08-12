package com.example.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.model.LatLng;
import com.example.json_.Price_1;
import com.example.json_.Price_2;
import com.example.json_.Price_3;
import com.example.json_.Price_4;
import com.example.mapxml.R;
import com.example.mapxml.R.layout;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	Context context;
	LayoutInflater layoutinflater;
	Price_4 p4;
	List<Price_2> list_2;
	Price_3 p3;
	Price_2 p2;
	// List<Price_4> list;
	List<Map<String, Object>> list;
	Map<String, Object> map;


	public ListAdapter(List<Map<String, Object>> list, Context class1) {
		this.list = list;
		this.context = class1;
		layoutinflater = LayoutInflater.from(class1);
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (null != list) {
			return list.size();
		}

		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		ViewPrice vp;
		if (view == null || view.getTag() == null) {
			view = layoutinflater.inflate(R.layout.list_layout, null);
			vp = new ViewPrice();
			vp.text1 = (TextView) view.findViewById(R.id.list_text1);
			vp.text2 = (TextView) view.findViewById(R.id.list_text2);
			vp.text4 = (TextView) view.findViewById(R.id.list_text4);
			vp.text5 = (TextView) view.findViewById(R.id.list_text5);
			vp.text6 = (TextView) view.findViewById(R.id.list_text6);
			vp.list_latlng = (TextView) view.findViewById(R.id.list_latlng);

			view.setTag(vp);
		}
		View layout1 = view.findViewById(R.id.list_linear);
		map = list.get(position);
		vp = (ViewPrice) view.getTag();
		vp.text2.setText("地址:" + map.get("s2").toString());
		vp.text1.setText(map.get("s8").toString() + "汽油:"
				+ map.get("s9").toString() + "元/升\n"
				+ map.get("s10").toString() + "汽油:" + map.get("s11").toString()
				+ "元/升\n" + map.get("s12").toString() + "汽油:"
				+ map.get("s13").toString() + "元/升\n"
				+ map.get("s14").toString() + "汽油:" + map.get("s15").toString()
				+ "元/升\n");
		vp.text4.setText(map.get("s5").toString());
		vp.text5.setText("企业:" + map.get("s6").toString());
		vp.text6.setText("其它:" + map.get("s7").toString());
		// layout1.setBackgroundResource(Color.BLACK);
		vp.list_latlng.setText(map.get("latlng").toString());
		
		for (int i = 0; i < list.size(); i++) {
			if (position % 3 == 0) {
				layout1.setBackgroundResource(R.drawable.jyz_01);
			} else if (position % 3 == 1) {
				layout1.setBackgroundResource(R.drawable.jyz_02);
			} else {
				layout1.setBackgroundResource(R.drawable.jyz_03);
			}
		}
		return view;
	}

	class ViewPrice {
		TextView text1, text2, text3, text4, text5, text6, list_latlng;
	}

}
