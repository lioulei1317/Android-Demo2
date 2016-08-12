package com.example.map;

//https://teacher/svn/team_8/
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteResult;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

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

	// 浏览路线节点相关
	Button mBtnPre = null; // 上一个节点
	Button mBtnNext = null; // 下一个节点
	int nodeIndex = -1; // 节点索引,供浏览节点时使用
	RouteLine route = null;
	OverlayManager routeOverlay = null;
	boolean useDefaultIcon = false;
	private TextView popupText = null; // 泡泡view

	// 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	private Marker mMarkerA;
	BitmapDescriptor marker_bitmap;// 构建Marker图标

	GeoCoder geoCoder1 = null;// 地理编码：地址信息与坐标的相互转换
	LatLng dingwei_weizhi, marker_weizhi;

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
			// 默认开启地图路况
			mBaiduMap.setTrafficEnabled(true);
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

		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		// 地图点击事件处理
		mBaiduMap.setOnMapClickListener(mblistener);
		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(msListener);

		marker_weizhi_ff();// 长按地图，添加覆盖物定位

	}

	/**
	 * 标记位置
	 * 
	 * 
	 * */
	private void marker_weizhi_ff() {
		final TextView biaoji_tv1 = (TextView) findViewById(R.id.biaoji_tv1);
		final View layout_daohang1 = findViewById(R.id.layout_daohang1);
		geoCoder1 = GeoCoder.newInstance();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);// 构造一个更新地图的msu对象，然后设置该对象为缩放等级14.0，最后设置地图状态。
		marker_bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.marker_wz);// 构建Marker图标

		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			// 当前用户选取的坐标
			public void onMapLongClick(LatLng arg0) {
				if (mMarkerA == null) {
					MarkerOptions ooA = new MarkerOptions().position(arg0)
							.icon(marker_bitmap).zIndex(1).draggable(true);
					ooA.animateType(MarkerAnimateType.grow);
					mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
				} else {
					mMarkerA.remove();
					MarkerOptions ooA = new MarkerOptions().position(arg0)
							.icon(marker_bitmap).zIndex(1).draggable(true);
					ooA.animateType(MarkerAnimateType.grow);
					mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

				}
				geoCoder1.reverseGeoCode(new ReverseGeoCodeOption()
						.location(arg0));// 发起反地理编码请求(经纬度->地址信息)
				geoCoder1
						.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
							// 设置查询结果监听者
							@Override
							public void onGetReverseGeoCodeResult(
									ReverseGeoCodeResult result) {
								// TODO Auto-generated method stub
								biaoji_tv1.setText(result.getAddress());// 显示地址信息
								layout_daohang1.setVisibility(View.VISIBLE);// 显示控件
							}

							@Override
							public void onGetGeoCodeResult(GeoCodeResult arg0) {
								// TODO Auto-generated method stub

							}
						});

				marker_weizhi = arg0;// 标记坐标
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
							.setImageResource(R.drawable.dingwei_gengshui);
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					// 跟随
					requestLocButton
							.setImageResource(R.drawable.dingwei_luopan);
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
							PoiSearchs.class);
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
		option.setOpenGps(true); // 设置是否需要gps，默认不需要
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);// 可选，默认为0，即定位一次，设置发起定位请求的间隔，必须大于或等于1000ms
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
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
				Toast.makeText(context, "你的当前位置：" + location.getAddrStr(),
						Toast.LENGTH_LONG).show();
				dingwei_weizhi = new LatLng(mLatitude, mLongtitude);
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
			trafficeView.setBackgroundResource(R.drawable.traffice2);
			mBaiduMap.setTrafficEnabled(false);
			traffice++;
		} else {
			trafficeView.setBackgroundResource(R.drawable.traffice);
			mBaiduMap.setTrafficEnabled(true);
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
		// 回收 marker_bitmap 资源
		marker_bitmap.recycle();
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

	/**
	 * 发起路线规划搜索示例
	 */
	public void searchButtonProcess(View v) {
		final View layout_daohang1 = findViewById(R.id.layout_daohang1);
		layout_daohang1.setVisibility(View.GONE);// 隐藏控件
		// 重置浏览节点的路线数据
		route = null;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);

		// 设置路线的起点、终点
		PlanNode stNode = PlanNode.withLocation(dingwei_weizhi);
		PlanNode enNode = PlanNode.withLocation(marker_weizhi);

		// 实际使用中请对起点终点城市进行正确的设定
		if (v.getId() == R.id.daohang_bt1) {
			mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode)
					.to(enNode));
		}
		mBaiduMap.clear();// 清除图层
	}

	/**
	 * 节点浏览示例
	 */
	public void nodeClick(View v) {
		if (route == null || route.getAllStep() == null) {
			return;
		}
		if (nodeIndex == -1 && v.getId() == R.id.pre) {
			return;
		}
		// 设置节点索引
		if (v.getId() == R.id.next) {
			if (nodeIndex < route.getAllStep().size() - 1) {
				nodeIndex++;
			} else {
				return;
			}
		} else if (v.getId() == R.id.pre) {
			if (nodeIndex > 0) {
				nodeIndex--;
			} else {
				return;
			}
		}
		// 获取节结果信息
		LatLng nodeLocation = null;
		String nodeTitle = null;
		Object step = route.getAllStep().get(nodeIndex);
		if (step instanceof DrivingRouteLine.DrivingStep) {
			nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrance()
					.getLocation();
			nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
		} else if (step instanceof WalkingRouteLine.WalkingStep) {
			nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrance()
					.getLocation();
			nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
		} else if (step instanceof TransitRouteLine.TransitStep) {
			nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrance()
					.getLocation();
			nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
		} else if (step instanceof BikingRouteLine.BikingStep) {
			nodeLocation = ((BikingRouteLine.BikingStep) step).getEntrance()
					.getLocation();
			nodeTitle = ((BikingRouteLine.BikingStep) step).getInstructions();
		}

		if (nodeLocation == null || nodeTitle == null) {
			return;
		}
		// 移动节点至中心
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
		// show popup
		popupText = new TextView(MainActivity.this);
		popupText.setBackgroundResource(R.drawable.popup);
		popupText.setTextColor(0xFF000000);
		popupText.setText(nodeTitle);
		mBaiduMap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	// 定制RouteOverly（驾车路线规划）
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	OnMapClickListener mblistener = new OnMapClickListener() {
		// 地图单击事件监听接口
		@Override
		public boolean onMapPoiClick(MapPoi arg0) {
			// 地图单击事件回调函数
			return false;
		}

		@Override
		public void onMapClick(LatLng arg0) {
			// 地图内 Poi 单击事件回调函数
			mBaiduMap.hideInfoWindow();
			// 隐藏当前 InfoWindow
		}
	};

	OnGetRoutePlanResultListener msListener = new OnGetRoutePlanResultListener() {
		// 路线规划结果回调

		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult result) {
			// 驾车路线结果回调onGetDrivingRouteResult()
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(MainActivity.this, "抱歉，未找到结果",
						Toast.LENGTH_SHORT).show();
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				result.getSuggestAddrInfo();
				return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				nodeIndex = -1;
				mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
				route = result.getRouteLines().get(0);
				DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(
						mBaiduMap);
				routeOverlay = overlay;
				mBaiduMap.setOnMarkerClickListener(overlay);
				overlay.setData(result.getRouteLines().get(0));
				overlay.addToMap();
				overlay.zoomToSpan();
			}

		}

		@Override
		public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
			// 步行路线结果回调

		}

		@Override
		public void onGetBikingRouteResult(BikingRouteResult arg0) {
			// 骑行路线结果回调

		}

		@Override
		public void onGetTransitRouteResult(TransitRouteResult arg0) {
			// 换乘路线结果回调

		}
	};

}
