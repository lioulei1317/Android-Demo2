package com.trip.resta;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.trip.resta.bean.Station;
import com.trip.resta.util.StationData;

public class MainActivity extends Activity implements OnClickListener {

	private Context mContext;
	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private LocationClient mLocationClient = null;
	private BDLocationListener mListener = new MyLocationListener();

	private ImageView iv_list, iv_loc;
	private Toast mToast;
	private TextView tv_title_right, tv_name, tv_distance, tv_price_a,
			tv_price_b;
	private LinearLayout ll_summary;
	private Dialog selectDialog, loadingDialog;
	private StationData stationData;
	private BDLocation loc;
	private ArrayList<Station> mList;
	private Station mStation = null;
	private int mDistance = 300;
	private Marker lastMarker = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		stationData = new StationData(mHandler);
		initView();
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0x01:
				mList = (ArrayList<Station>) msg.obj;
				setMarker(mList);
				loadingDialog.dismiss();
				break;
			case 0x02:
				loadingDialog.dismiss();
				showToast(String.valueOf(msg.obj));
				break;
			default:
				break;
			}
		}

	};

	private void initView() {
		// TODO Auto-generated method stub
		iv_list = (ImageView) findViewById(R.id.iv_list);
		iv_list.setOnClickListener(this);
		iv_loc = (ImageView) findViewById(R.id.iv_loc);
		iv_loc.setOnClickListener(this);
		tv_title_right = (TextView) findViewById(R.id.tv_title_button);
		tv_title_right.setText("300m" + " >");
		tv_title_right.setVisibility(View.VISIBLE);
		tv_title_right.setOnClickListener(this);

		ll_summary = (LinearLayout) findViewById(R.id.ll_summary);
		ll_summary.setOnClickListener(this);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_distance = (TextView) findViewById(R.id.tv_distance);
		tv_price_a = (TextView) findViewById(R.id.tv_price_a);
		tv_price_b = (TextView) findViewById(R.id.tv_price_b);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.showScaleControl(false);
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();

		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().zoom(15).build()));

		mLocationClient = new LocationClient(mContext);
		mLocationClient.registerLocationListener(mListener);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd0911");
		option.setScanSpan(0);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);
		mLocationClient.setLocOption(option);
	}

	protected void setMarker(ArrayList<Station> mList2) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(mContext)
				.inflate(R.layout.marker, null);
		final TextView tv = (TextView) view.findViewById(R.id.tv_marker);
		for (int i = 0; i < mList2.size(); i++) {
			Station s = mList2.get(i);
			tv.setText((i + 1) + "");
			if (i == 0) {
				tv.setBackgroundResource(R.drawable.icon_focus_mark);
			} else {
				tv.setBackgroundResource(R.drawable.icon_mark);
			}
			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(tv);
			LatLng latLng = new LatLng(s.getLat(), s.getLon());
			Bundle b = new Bundle();
			b.putParcelable("s", mList2.get(i));
			OverlayOptions oo = new MarkerOptions().position(latLng)
					.icon(bitmap).title((i + 1) + "").extraInfo(b);
			if (i == 0) {
				lastMarker = (Marker) mBaiduMap.addOverlay(oo);
				mStation = s;
				showLayoutInfo((i + 1) + "", mStation);
			} else {
				mBaiduMap.addOverlay(oo);
			}
		}
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				if (lastMarker != null) {
					tv.setText(lastMarker.getTitle());
					tv.setBackgroundResource(R.drawable.icon_mark);
					BitmapDescriptor bitmap = BitmapDescriptorFactory
							.fromView(tv);
					lastMarker.setIcon(bitmap);
				}
				lastMarker = marker;
				String position = marker.getTitle();
				tv.setText(position);
				tv.setBackgroundResource(R.drawable.icon_focus_mark);
				BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(tv);
				marker.setIcon(bitmap);
				mStation = marker.getExtraInfo().getParcelable("s");
				showLayoutInfo(position, mStation);
				return false;
			}
		});
	}

	private void showLayoutInfo(String position, Station s) {
		// TODO Auto-generated method stub
		tv_name.setText(position + "." + s.getName());
		tv_distance.setText(s.getCity() + "");
		ll_summary.setVisibility(View.VISIBLE);
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location == null) {
				return;
			}
			loc = location;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.direction(location.getDirection())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			searchStation(location.getLatitude(), location.getLongitude(),
					mDistance);
		}
	}

	public void onDialogClick(View v) {
		switch (v.getId()) {
		case R.id.bt_3km:
			distanceSearch("300m >", 300);
			break;
		case R.id.bt_5km:
			distanceSearch("500m >", 500);
			break;
		case R.id.bt_8km:
			distanceSearch("800m >", 800);
			break;
		case R.id.bt_10km:
			distanceSearch("1000m >", 1000);
			break;
		default:
			break;
		}
	}

	public void distanceSearch(String text, int distance) {
		mDistance = distance;
		tv_title_right.setText(text);
		searchStation(loc.getLatitude(), loc.getLongitude(), distance);
		selectDialog.dismiss();
	}

	private void searchStation(double latitude, double longitude, int mDistance) {
		// TODO Auto-generated method stub
		showLoadingDialog();
		mBaiduMap.clear();
		ll_summary.setVisibility(View.GONE);
		stationData.getStationData(latitude, longitude, mDistance);
	}

	private void showSelectDialog() {
		if (selectDialog != null) {
			selectDialog.show();
			return;
		}
		selectDialog = new Dialog(mContext, R.style.dialog);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_distance, null);
		selectDialog.setContentView(view, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		selectDialog.setCanceledOnTouchOutside(true);
		selectDialog.show();
	}

	private void showLoadingDialog() {
		if (loadingDialog != null) {
			loadingDialog.show();
			return;
		}
		loadingDialog = new Dialog(mContext, R.style.dialog_loading);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_loading, null);
		loadingDialog.setContentView(view, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		loadingDialog.setCancelable(false);
		loadingDialog.show();
	}

	private void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, msg, 1);
		}
		mToast.setText(msg);
		mToast.show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
		mLocationClient.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
		mLocationClient.stop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMapView.onDestroy();
		mHandler = null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_list:
			Intent listIntent = new Intent(mContext, StationListActivity.class);
			listIntent.putParcelableArrayListExtra("list", mList);
			listIntent.putExtra("locLat", loc.getLatitude());
			listIntent.putExtra("locLon", loc.getLongitude());
			startActivity(listIntent);
			break;
		case R.id.iv_loc:
			int r = mLocationClient.requestLocation();
			switch (r) {
			case 1:
				showToast("我还没有启动。");
				break;
			case 2:
				showToast("没有监听函数。");
				break;
			case 6:
				showToast("您的请求过于频繁，我有点受不了。");
				break;

			default:
				break;
			}

			break;
		case R.id.tv_title_button:
			showSelectDialog();
			break;
		case R.id.ll_summary:
			Intent infoIntent = new Intent(mContext, StationInfoActivity.class);
			infoIntent.putExtra("s", mStation);
			infoIntent.putExtra("locLat", loc.getLatitude());
			infoIntent.putExtra("locLon", loc.getLongitude());
			startActivity(infoIntent);
			break;
		default:
			break;
		}
	}
}
