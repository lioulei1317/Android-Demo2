package com.trip.resta;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class PetrolStationApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
		com.thinkland.sdk.android.SDKInitializer
				.initialize(getApplicationContext());
	}

}
