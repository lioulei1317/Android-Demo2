package com.example.map;
//https://teacher/svn/team_8/
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.example.orientation.MyOrientationListener;
import com.example.orientation.MyOrientationListener.OnOrientationListener;
import com.imooc.arcmenu.view.ArcMenu;
import com.imooc.arcmenu.view.ArcMenu.OnMenuItemClickListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 入口，定位
 */
public class MainActivity extends Activity {
	/**
	 * 地图控件
	 */
	private MapView mMapView;
	private ArcMenu mArcMenu;

	/**
	 * 定位的客户端
	 */
	private LocationClient mLocClient;
	/**
	 * 定位的监听器
	 */
	public MyLocationListenner myListener;
	/**
	 * 当前定位的模式
	 */
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;
	// private static final int accuracyCircleFillColor = 0xAAFFFF88;
	// private static final int accuracyCircleStrokeColor = 0xAA00FF00;
	/**
	 * 地图实例
	 */
	private BaiduMap mBaiduMap;
	private Context context;

	// UI相关
	// OnCheckedChangeListener radioButtonListener;
	private ImageButton requestLocButton;
	/***
	 * 是否是第一次定位
	 */
	private boolean isFirstLoc = true;
	/**
	 * 最新一次的经纬度
	 */
	private double mLatitude;
	private double mLongtitude;
	/**
	 * 自定义图标
	 * */
	private BitmapDescriptor mIconLocation;

	/**
	 * 定位的监听器
	 */
	private MyOrientationListener myOrientationListener;

	private float mCurrentX;
	/**
	 * 当前的精度
	 */
	private float mCurrentAccracy;
	/**
	 * 交通图的计数器
	 */
	int traffice = 0;
	EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		this.context = this;
		initView();
		initEvent();
		// 隐藏logo
		View child = mMapView.getChildAt(1);
		if (child != null
				&& (child instanceof ImageView || child instanceof ZoomControls)) {
			child.setVisibility(View.INVISIBLE);
		}

		// 获取屏幕的宽和高
		WindowManager wm = this.getWindowManager();

		final int width = (int) (wm.getDefaultDisplay().getWidth() * 0.86);
		final int height = (int) (wm.getDefaultDisplay().getHeight() * 0.46);
		mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {

				mMapView.setZoomControlsPosition(new Point(width, height));
				/**
				 * 指定指南针的位置
				 */
				mBaiduMap.setCompassPosition(new Point(110, 280));

			}
		});

	}

	/**
	 * 相关初始化
	 * */
	private void initView() {
		// TODO Auto-generated method stub
		mMapView = (MapView) findViewById(R.id.shouye_bmapView);
		mArcMenu = (ArcMenu) findViewById(R.id.id_menu);
		requestLocButton = (ImageButton) findViewById(R.id.shouye_dingwei);
		editText = (EditText) findViewById(R.id.shouye_Editexts);
		mCurrentMode = LocationMode.NORMAL;
		// requestLocButton.setText("默认");
		requestLocButton.setImageResource(R.drawable.dingwei_moren);
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					// 普通
					requestLocButton
							.setImageResource(R.drawable.dingwei_putong);
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					// 罗盘
					requestLocButton
							.setImageResource(R.drawable.dingwei_luopan);
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					// 跟随
					requestLocButton
							.setImageResource(R.drawable.dingwei_gengshui);
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;

				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);

		/**
		 * Editext获取焦点时调用
		 */
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {

				if (hasFocus) {
					// 获取焦点时
					Intent intent = new Intent(MainActivity.this,
							PoiSearch.class);
					startActivity(intent);

				} else {
					// 不做处理
				}
			}
		});

		// radioButtonListener = new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		//
		// // 传入null则，恢复默认图标
		// mCurrentMarker = null;
		// mBaiduMap
		// .setMyLocationConfigeration(new MyLocationConfiguration(
		// mCurrentMode, true, null));
		//
		// }
		// };

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.shouye_bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		myListener = new MyLocationListenner();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// 初始化自定义图标
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.dingwei_zidingyi);
		myOrientationListener = new MyOrientationListener(context);
		myOrientationListener
				.setmOnOrientationListener(new OnOrientationListener() {

					@Override
					public void onOrientationChanged(float x) {
						// TODO Auto-generated method stub
						mCurrentX = x;
						MyLocationData locData = new MyLocationData.Builder()
								.accuracy(mCurrentAccracy)//
								.direction(mCurrentX)//
								.latitude(mLatitude).longitude(mLongtitude)//
								.build();
						mBaiduMap.setMyLocationData(locData);
					}
				});
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(mCurrentX)
					//
					.latitude(location.getLatitude())
					.longitude(location.getLongitude())//
					.build();
			mCurrentAccracy = location.getRadius();
			// 设置定位数据
			mBaiduMap.setMyLocationData(locData);
			// 设置自定义图标
			// MyLocationConfiguration config = new MyLocationConfiguration(
			// mCurrentMode, true, mIconLocation);
			// mBaiduMap.setMyLocationConfigeration(config);

			// 更新经纬度
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();

			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
				Toast.makeText(context, location.getAddrStr(),
						Toast.LENGTH_LONG).show();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 设置是否显示交通图
	 * 
	 * @param view
	 */
	public void setTraffice(View view) {
		// mBaiduMap.setTrafficEnabled(((CheckBox) view).isChecked());
		ImageView trafficeView = (ImageView) findViewById(R.id.shouye_traffice);
		if (traffice % 2 == 0) {
			trafficeView.setBackgroundResource(R.drawable.traffice);
			mBaiduMap.setTrafficEnabled(true);
			traffice++;
		} else {
			trafficeView.setBackgroundResource(R.drawable.traffice2);
			mBaiduMap.setTrafficEnabled(false);
			traffice++;
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// 开启定位
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocClient.isStarted())
			mLocClient.start();
		// 开启方向传感器
		myOrientationListener.start();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// 停止定位
		mBaiduMap.setMyLocationEnabled(false);
		mLocClient.stop();
		// 停止方向传感器
		myOrientationListener.stop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
		mMapView = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	private void initEvent() {
		mArcMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onClick(View view, int pos) {
				Toast.makeText(MainActivity.this, pos + ":" + view.getTag(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
