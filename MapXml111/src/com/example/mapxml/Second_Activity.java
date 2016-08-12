package com.example.mapxml;

import com.baidu.android.voice.ApiActivity;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class Second_Activity extends Activity {
	// SuggestionSearch建议查询类
	private SuggestionSearch mSuggestionSearch = null;
	EditText keyWorldsView;
	Button seconds_shousuo;
	private ListView second_listview;
	public static String guanjianzi;
	private ArrayAdapter<String> sugAdapter = null;
	public static String seconds_shousous;
	private ImageView shuosuo_fanhui;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.second_shousuo_title);
		initview();
		ListenerMethod();
	}

	private void ListenerMethod() {
		/**
		 * 当输入关键字变化时，动态更新建议列表
		 */
		keyWorldsView.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					return;
				}

				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(
										Home_Activity.dangqian_City));
			}
		});

		mSuggestionSearch
				.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {

					@Override
					public void onGetSuggestionResult(SuggestionResult res) {
						// TODO Auto-generated method stub
						if (res == null || res.getAllSuggestions() == null) {
							return;
						}
						sugAdapter.clear();
						for (SuggestionResult.SuggestionInfo info : res
								.getAllSuggestions()) {
							if (info.pt != null) {
								sugAdapter.add(info.city + info.district
										+ info.key);
							}

						}
						sugAdapter.notifyDataSetChanged();
					}
				});
		seconds_shousuo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Home_Activity.OneTouchSearch_boolean = false;
				guanjianzi = keyWorldsView.getText().toString();
				Intent intent = new Intent(Second_Activity.this,
						Home_Activity.class);
				startActivity(intent);

			}
		});
		second_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				seconds_shousous = (String) arg0.getItemAtPosition(arg2);
				// keyWorldsView.setText(seconds_shousous);
				Intent intent = new Intent(Second_Activity.this,
						Home_Activity.class);
				startActivity(intent);
			}
		});
		shuosuo_fanhui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private void initview() {
		// TODO Auto-generated method stub
		keyWorldsView = (EditText) findViewById(R.id.second_shousuok);
		seconds_shousuo = (Button) findViewById(R.id.seconds_shousuo);
		second_listview = (ListView) findViewById(R.id.second_listview);
		shuosuo_fanhui = (ImageView) findViewById(R.id.shuosuo_fanhui);
		mSuggestionSearch = SuggestionSearch.newInstance();

		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		second_listview.setAdapter(sugAdapter);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (ApiActivity.speakings != null) {
			keyWorldsView.setText(ApiActivity.speakings);
		}
	}
}
