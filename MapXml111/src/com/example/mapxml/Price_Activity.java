package com.example.mapxml;

import java.util.Map;

import com.baidu.mapapi.model.LatLng;
import com.example.adapter.ListAdapter;
import com.example.json_.Price_3;
import com.example.json_.Price_4;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class Price_Activity extends Activity {
	static ListView price_list;
	Price_4 p4;
	ListAdapter adapter1;
	public static LatLng Price_latlng;
	public static String Price_name;
	public static String Price_address;
	Map<String, Object> map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_price_);
		price_list = (ListView) findViewById(R.id.price_list);
		adapter1 = new ListAdapter(Home_Activity.list, Price_Activity.this);
		price_list.setAdapter(adapter1);
		price_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				map = (Map<String, Object>) adapter1.getItem(arg2);
				Price_latlng=(LatLng) map.get("latlng");
				Price_address=(String) map.get("s2");
				Price_name=(String) map.get("s5");
				finish();

			}
		});
	}

}
