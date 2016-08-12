package com.example.json_;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;

public class Gson_ {
	LatLng latlng;
	public static Map<String, Object> map;
	public static Map<String, Object> map1;

	List<Map<String, Object>> list;
	List<Map<String, Object>> list1;
	String ss1, ss2;

	public List<Map<String, Object>> Prase(String Data) {

		Gson gson = new Gson();
		Price_3 p3 = gson.fromJson(Data, Price_3.class);
		Price_2 p2 = p3.getResult();
		List<Price_4> p4 = p2.getData();
		list = new ArrayList<Map<String, Object>>();

		List<Price_1> p1 = p4.get(0).getPrice();
		if (p4 != null) {
			for (int i = 0; i < p4.size(); i++) {
				map = new HashMap<String, Object>();
				list1 = new ArrayList<Map<String, Object>>();
				Price_4 price_4 = p4.get(i);
				for (int j = 0; j < p1.size(); j++) {
					map1 = new HashMap<String, Object>();

					Price_1 price_1 = p1.get(j);
					ss1 = price_1.getType();
					ss2 = price_1.getPrice();
					map1.put("ss1", ss1);
					map1.put("ss2", ss2);
					list1.add(map1);
				}
				String s2 = price_4.getAddress();
				String s3 = price_4.getLat();
				
				String s4 = price_4.getLon();
				String s5 = price_4.getName();
				String s6 = price_4.getBrandname();
				String s7 = price_4.getFwlsmc();
				latlng=new LatLng(Double.parseDouble(s3), Double.parseDouble(s4));
				map.put("latlng", latlng);
				map.put("s2", s2);
				map.put("s3", s3);
				map.put("s4", s4);
				map.put("s5", s5);
				map.put("s6", s6);
				map.put("s7", s7);
				map.put("s8", list1.get(0).get("ss1"));
				map.put("s9", list1.get(0).get("ss2"));
				map.put("s10", list1.get(1).get("ss1"));
				map.put("s11", list1.get(1).get("ss2"));
				map.put("s12", list1.get(2).get("ss1"));
				map.put("s13", list1.get(2).get("ss2"));
				map.put("s14", list1.get(3).get("ss1"));
				map.put("s15", list1.get(3).get("ss2"));

				list.add(map);
			}
		}
		System.out.println(p4.size() + "++++23333333333333333333");
		System.out.println(list.size()
				+ "+++11111111111111111111111111111111111111111111111");
		return list;

	}
}
