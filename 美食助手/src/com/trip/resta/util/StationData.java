package com.trip.resta.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.trip.resta.bean.Station;

public class StationData {
	Handler mHandler;

	public StationData(Handler mHandler2) {
		// TODO Auto-generated constructor stub
		this.mHandler = mHandler2;
	}

	public void getStationData(double lat, double lon, int distance) {
		Parameters params = new Parameters();
		params.add("lat", lat);
		params.add("lng", lon);
		params.add("radius", distance);
		params.add("key", "446afb0329a525d1740633aaa224cf19");
		JuheData.executeWithAPI(45, "http://apis.juhe.cn/catering/query",
				JuheData.GET, params, new DataCallBack() {

					@Override
					public void resultLoaded(int code, String arg1, String json) {
						// TODO Auto-generated method stub
						if (code == 0) {
							ArrayList<Station> list = parser(json);
							if (list != null & mHandler != null) {
								Message msg = Message.obtain(mHandler, 0x01,
										list);
								msg.sendToTarget();
							} else {
								Message msg = Message.obtain(mHandler, 0x02,
										code);
								msg.sendToTarget();
							}
						}
					}
				});
	}

	protected ArrayList<Station> parser(String str) {
		// TODO Auto-generated method stub
		ArrayList<Station> list = null;
		try {
			JSONObject json = new JSONObject(str);
			int code = json.getInt("error_code");
			System.out.println("搜索返回的数据-->>" + str);
			if (code == 0) {
				list = new ArrayList<Station>();
				JSONArray arr = json.getJSONArray("result");
				for (int i = 0; i < arr.length(); i++) {
					JSONObject dataJSON = arr.getJSONObject(i);
					Station s = new Station();
					s.setName(dataJSON.getString("name"));
					s.setNavigation(dataJSON.getString("navigation"));
					s.setCity(dataJSON.getString("city"));
					s.setAddress(dataJSON.getString("address"));
					s.setPhone(dataJSON.getString("phone"));
					s.setAvg_price(dataJSON.getString("avg_price"));
					s.setLat(dataJSON.getDouble("latitude"));
					s.setLon(dataJSON.getDouble("longitude"));
					s.setStars(dataJSON.getDouble("stars"));
					s.setGood_remarks(dataJSON.getString("good_remarks"));
					s.setCommon_remarks(dataJSON.getString("common_remarks"));
					s.setBad_remarks(dataJSON.getString("bad_remarks"));
					s.setVery_bad_remarks(dataJSON
							.getString("very_bad_remarks"));
					s.setTags(dataJSON.getString("tags"));
					list.add(s);
				}

			} else {
				Message msg = Message.obtain(mHandler, 0x02, code);
				msg.sendToTarget();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
}
