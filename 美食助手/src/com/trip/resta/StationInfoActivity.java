package com.trip.resta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.trip.resta.bean.Station;

public class StationInfoActivity extends Activity implements OnClickListener {

	private Context mContext;
	private TextView tv_title_right, tv_name, tv_distance, tv_area, tv_addr;
	private ImageView iv_back;

	private ScrollView sv;
	private ListView lv_gast_price, lv_price;
	private Station s;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		mContext = this;
		initView();
		setText();
	}

	private void initView() {
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_area = (TextView) findViewById(R.id.tv_area);
		tv_addr = (TextView) findViewById(R.id.tv_addr);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		tv_title_right = (TextView) findViewById(R.id.tv_title_button);
		tv_title_right.setText("������ >");
		tv_title_right.setOnClickListener(this);
		tv_title_right.setVisibility(View.VISIBLE);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(this);
		lv_gast_price = (ListView) findViewById(R.id.lv_gast_price);

		lv_price = (ListView) findViewById(R.id.lv_price);
		sv = (ScrollView) findViewById(R.id.sv);
	}

	private void setText() {
		s = getIntent().getParcelableExtra("s");
		tv_name.setText(s.getName() + " - " + s.getCity());
		tv_addr.setText(s.getAddress());
		tv_distance.setText(s.getCity());
		tv_area.setText(s.getTags());
		sv.smoothScrollTo(0, 0);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_title_button:
			Intent intent = new Intent(mContext, RouteActivity.class);
			intent.putExtra("lat", s.getLat());
			intent.putExtra("lon", s.getLon());
			intent.putExtra("locLat", getIntent().getDoubleExtra("locLat", 0));
			intent.putExtra("locLon", getIntent().getDoubleExtra("locLon", 0));
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
