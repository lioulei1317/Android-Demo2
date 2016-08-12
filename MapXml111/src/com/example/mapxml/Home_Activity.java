package com.example.mapxml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.baidu.android.voice.ApiActivity;
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
import com.baidu.mapapi.poioverlay.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.example.json_.Gson_;
import com.example.orientation.MyOrientationListener;
import com.example.orientation.MyOrientationListener.OnOrientationListener;
import com.imooc.arcmenu.view.ArcMenu;
import com.imooc.arcmenu.view.ArcMenu.OnMenuItemClickListener;
import com.luxianguihua.DrivingRouteOverlay;
import com.luxianguihua.OverlayManager;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

//https://teacher/svn/team_8/
/**
 * 入口，定位
 */
public class Home_Activity extends Activity {
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
	/**
	 * 地图实例
	 */
	private BaiduMap mBaiduMap;
	private Context context;

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
	Button shouye_Buttons;
	/**
	 * 导航
	 */
	OverlayManager routeOverlay = null;
	RoutePlanSearch mRoutePlanSearch = null; // 路径规划搜索接口

	private Marker mMarkerA;// 标记
	BitmapDescriptor marker_bitmap;// 构建Marker图标

	GeoCoder geoCoder1 = null;// 地理编码：地址信息与坐标的相互转换
	GeoCoder geoCoder2 = null;// 地理编码：地址信息与坐标的相互转换
	LatLng dingwei_weizhi, marker_weizhi;// 定位和标记的位置

	View layout_daohang1;// 导航布局1
	View layout_daohang2;// 导航布局2
	RouteLine route = null;// 节点
	private int c;
	private SharedPreferences pre;
	ImageView shouye_yuyin;// 语音

	private PoiSearch mPoiSearch = null;// 类PoiSearch继承poi检索接口
	public static String dangqian_City;// 定位当前所在城市（静态全局变量）
	MarkerOptions ooA;
	TextView biaoji_tv1, biaoji_tv2;
	TextView biaoji_name, biaoji_name2;
	Button daohang_bt2;// 导航按钮
	private static final String APP_FOLDER_NAME = "BNSDKSimplebaiduditu";
	private String mSDCardPath = null;
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	PoiNearbySearchOption poiNearbySearchOption;
	static boolean OneTouchSearch_boolean = true;
	ImageView shouye_tuceng;
	LinearLayout tuceng_linearlayout;
	ImageView putongditu, weixingditu, relitu;
	int tuceng = 0;
	int relitus = 0;
	/**
	 * handler 解析数据
	 */
	public static List<Map<String, Object>> list;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			String view = bundle.getString("KEY");
			if (view != null) {
				Gson_ gs = new Gson_();
				list = gs.Prase(view);
			}
			Intent intent = new Intent(Home_Activity.this, Price_Activity.class);
			startActivity(intent);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext（注意该方法要再setContentView方法之前实现）
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_home_);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 强制竖屏
		this.context = this;
		poiNearbySearchOption = new PoiNearbySearchOption();
		initView();
		initEvent();
		// 隐藏logo
		View child = mMapView.getChildAt(1);
		if (child != null
				&& (child instanceof ImageView || child instanceof ZoomControls)) {
			child.setVisibility(View.INVISIBLE);
		}
		//
		pre = getSharedPreferences("count", MODE_WORLD_READABLE);
		c = pre.getInt("counts", 0);
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
		// 获取RoutePlan检索实例
		mRoutePlanSearch = RoutePlanSearch.newInstance();
		// 设置路线检索监听者
		mRoutePlanSearch.setOnGetRoutePlanResultListener(msListener);
		// 地图单击事件监听
		mBaiduMap.setOnMapClickListener(mblistener);
		// 长按地图，添加覆盖物定位
		marker_weizhi_biaoji();
		marker_shousuo();
		// 初始化导航相关
		if (initDirs()) {
			initNavi();
		}

	}

	private boolean initDirs() {
		mSDCardPath = getSdcardDir();
		if (mSDCardPath == null) {
			return false;
		}
		File f = new File(mSDCardPath, APP_FOLDER_NAME);
		if (!f.exists()) {
			try {
				f.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	String authinfo = null;

	@SuppressWarnings("deprecation")
	private void initNavi() {

		// BNOuterTTSPlayerCallback ttsCallback = null;

		BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME,
				new NaviInitListener() {
					@Override
					public void onAuthResult(int status, String msg) {
						if (0 == status) {
							authinfo = "key校验成功!";
							System.out.println("key校验成功!");
						} else {
							authinfo = "key校验失败, " + msg;
							System.out.println("key校验失败, ");
						}
						Home_Activity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(Home_Activity.this, authinfo,
										Toast.LENGTH_LONG).show();
							}
						});
					}

					public void initSuccess() {
						Toast.makeText(Home_Activity.this, "百度导航引擎初始化成功",
								Toast.LENGTH_SHORT).show();
						// initSetting();
					}

					public void initStart() {
						Toast.makeText(Home_Activity.this, "百度导航引擎初始化开始",
								Toast.LENGTH_SHORT).show();
					}

					public void initFailed() {
						Toast.makeText(Home_Activity.this, "百度导航引擎初始化失败",
								Toast.LENGTH_SHORT).show();
					}

				}, null);

	}

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	private void routeplanToNavi() {
		CoordinateType coType = CoordinateType.GCJ02;
		BNRoutePlanNode sNode = null;
		BNRoutePlanNode eNode = null;

		// sNode = new BNRoutePlanNode(116.30142, 40.05087, "百度大厦", null,
		// coType);
		// eNode = new BNRoutePlanNode(116.39750, 39.90882, "北京天安门", null,
		// coType);
		BDLocation destBdLocation = new BDLocation();
		destBdLocation.setLatitude(dingwei_weizhi.latitude);
		destBdLocation.setLongitude(dingwei_weizhi.longitude);
		BDLocation destGcj1 = bd2gcj(destBdLocation);

		destBdLocation.setLatitude(marker_weizhi.latitude);
		destBdLocation.setLongitude(marker_weizhi.longitude);
		BDLocation destGcj2 = bd2gcj(destBdLocation);

		sNode = new BNRoutePlanNode(destGcj1.getLongitude(),
				destGcj1.getLatitude(), "我的地点", null, coType);
		eNode = new BNRoutePlanNode(destGcj2.getLongitude(),
				destGcj2.getLatitude(), "目标地点", null, coType);

		if (sNode != null && eNode != null) {
			List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
			list.add(sNode);
			list.add(eNode);
			BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true,
					new DemoRoutePlanListener(sNode));
		}
	}

	private BDLocation bd2gcj(BDLocation loc) {
		return LocationClient.getBDLocationInCoorType(loc,
				BDLocation.BDLOCATION_BD09LL_TO_GCJ02);

	}

	public class DemoRoutePlanListener implements RoutePlanListener {

		private BNRoutePlanNode mBNRoutePlanNode = null;

		public DemoRoutePlanListener(BNRoutePlanNode node) {
			mBNRoutePlanNode = node;
		}

		@Override
		public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 */
			Intent intent = new Intent(Home_Activity.this,
					BNDemoGuideActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(ROUTE_PLAN_NODE,
					(BNRoutePlanNode) mBNRoutePlanNode);
			intent.putExtras(bundle);
			startActivity(intent);

		}

		@Override
		public void onRoutePlanFailed() {
			// TODO Auto-generated method stub
			Toast.makeText(Home_Activity.this, "算路失败", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void marker_shousuo() {
		// TODO Auto-generated method stub
		// 初始化搜索模块
		mPoiSearch = PoiSearch.newInstance();
		// 注册搜索事件监听
		// mPoiSearch.setOnGetPoiSearchResultListener(mPoiSearchListener);

	}

	/**
	 * 标记位置
	 * 
	 * 
	 * */
	private void marker_weizhi_biaoji() {
		biaoji_tv1 = (TextView) findViewById(R.id.biaoji_tv1);
		biaoji_tv2 = (TextView) findViewById(R.id.biaoji_tv2);
		biaoji_name = (TextView) findViewById(R.id.biaoji_name);
		biaoji_name2 = (TextView) findViewById(R.id.biaoji_name2);
		layout_daohang1 = findViewById(R.id.layout_daohang1);
		geoCoder1 = GeoCoder.newInstance();
		geoCoder2 = GeoCoder.newInstance();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);// 构造一个更新地图的msu对象，然后设置该对象为缩放等级14.0，最后设置地图状态。
		marker_bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.marker_wz_1);// 构建Marker图标

		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng arg0) {
				if (mMarkerA == null) {
					ooA.position(arg0).icon(marker_bitmap).zIndex(1)
							.draggable(true);
				} else {
					mMarkerA.remove();
					ooA.position(arg0).icon(marker_bitmap).zIndex(1)
							.draggable(true);
				}
				ooA.animateType(MarkerAnimateType.grow);
				mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

				geoCoder1.reverseGeoCode(new ReverseGeoCodeOption()
						.location(arg0));// 发起反地理编码请求(经纬度->地址信息)

				geoCoder1
						.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
							// 设置查询结果监听者
							@Override
							public void onGetReverseGeoCodeResult(
									ReverseGeoCodeResult arg0) {
								// TODO Auto-generated method stub
								biaoji_name.setText("");
								biaoji_name2.setText("");
								biaoji_tv1.setText(arg0.getAddress());// 显示地址信息
								biaoji_tv2.setText(arg0.getAddress());// 显示地址信息
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

	private void marker_weizhi_biaoji2() {
		geoCoder2.geocode(new GeoCodeOption().city(dangqian_City).address(
				Second_Activity.seconds_shousous));// 发起反地理编码请求(地址信息->经纬度)
		geoCoder2
				.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

					@Override
					public void onGetReverseGeoCodeResult(
							ReverseGeoCodeResult result) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onGetGeoCodeResult(GeoCodeResult result) {
						// TODO Auto-generated method stub
						if (mMarkerA == null) {
							ooA.position(result.getLocation())
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.marker_wz))
									.zIndex(2).draggable(false);
						} else {
							mMarkerA.remove();
							ooA.position(result.getLocation())
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.marker_wz))
									.zIndex(2).draggable(false);
						}
						ooA.animateType(MarkerAnimateType.grow);
						mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
						biaoji_tv1.setText(result.getAddress());// 显示地址信息
						biaoji_tv2.setText(result.getAddress());// 显示地址信息
						layout_daohang1.setVisibility(View.VISIBLE);// 显示控件
						marker_weizhi = result.getLocation();

						// 将搜索到的位置移动到当前界面
						MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
								.newLatLng(marker_weizhi);
						mBaiduMap.animateMapStatus(mapStatusUpdate);
					}
				});

	}

	/**
	 * 发起路线规划搜索示例
	 */
	public void searchButtonProcess1(View v) {
		// 重置浏览节点的路线数据
		route = null;

		// 设置路线的起点、终点
		PlanNode stNode = PlanNode.withLocation(dingwei_weizhi);
		PlanNode enNode = PlanNode.withLocation(marker_weizhi);

		// 实际使用中请对起点终点城市进行正确的设定
		if (v.getId() == R.id.daohang_bt1) {
			mRoutePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(
					stNode).to(enNode));
			layout_daohang1.setVisibility(View.GONE);
			layout_daohang2.setVisibility(View.VISIBLE);
		}
		mBaiduMap.clear();// 清除图层
	}

	/**
	 * 地图单击事件监听接口
	 */
	OnMapClickListener mblistener = new OnMapClickListener() {

		@Override
		public boolean onMapPoiClick(MapPoi arg0) {

			// 地图内 Poi 单击事件回调函数
			return false;
		}

		@Override
		public void onMapClick(LatLng arg0) {
			// 地图单击事件回调函数
			mBaiduMap.hideInfoWindow();
			// 隐藏当前 InfoWindow

			if (mMarkerA != null) {
				mMarkerA.remove();
				// layout_daohang1.setVisibility(View.GONE);

			}
		}
	};

	// 定制RouteOverly（驾车路线规划）
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (false) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (false) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	/**
	 * 路线规划结果回调
	 */

	OnGetRoutePlanResultListener msListener = new OnGetRoutePlanResultListener() {
		//

		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult result) {
			// 驾车路线结果回调onGetDrivingRouteResult()
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(Home_Activity.this, "抱歉，未找到结果",
						Toast.LENGTH_SHORT).show();
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				result.getSuggestAddrInfo();
				return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				// nodeIndex = -1;
				// mBtnPre.setVisibility(View.VISIBLE);
				// mBtnNext.setVisibility(View.VISIBLE);
				// route = result.getRouteLines().get(0);
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

	/**
	 * 相关初始化
	 * */
	private void initView() {
		// TODO Auto-generated method stub
		mMapView = (MapView) findViewById(R.id.shouye_bmapView);
		mArcMenu = (ArcMenu) findViewById(R.id.id_menu);
		requestLocButton = (ImageButton) findViewById(R.id.shouye_dingwei);
		shouye_Buttons = (Button) findViewById(R.id.shouye_Buttons);
		shouye_yuyin = (ImageView) findViewById(R.id.shouye_yuyin);
		daohang_bt2 = (Button) findViewById(R.id.daohang_bt2);
		layout_daohang2 = findViewById(R.id.layout_daohang2);
		shouye_tuceng = (ImageView) findViewById(R.id.shouye_tuceng);
		tuceng_linearlayout = (LinearLayout) findViewById(R.id.tuceng_linearlayout);
		putongditu = (ImageView) findViewById(R.id.putongditu);
		weixingditu = (ImageView) findViewById(R.id.weixingditu);
		relitu = (ImageView) findViewById(R.id.relitu);
		mCurrentMode = LocationMode.NORMAL;
		// requestLocButton.setText("默认");
		requestLocButton.setImageResource(R.drawable.dingwei_moren);
		ooA = new MarkerOptions();
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					// 正常
					requestLocButton
							.setImageResource(R.drawable.dingwei_putong);
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					// 跟随
					requestLocButton
							.setImageResource(R.drawable.dingwei_gengshui);
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					// 罗盘
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
		 * 跳转到搜索界面
		 */
		shouye_Buttons.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Home_Activity.this,
						Second_Activity.class);
				startActivity(intent);
			}
		});
		/**
		 * 跳转到语音界面
		 */
		shouye_yuyin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Home_Activity.this,
						ApiActivity.class);
				startActivity(intent);
			}
		});
		/**
		 * 语音导航监听
		 */
		daohang_bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				routeplanToNavi();
				layout_daohang2.setVisibility(View.GONE);
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
		// // 初始化自定义图标
		// mIconLocation = BitmapDescriptorFactory
		// .fromResource(R.drawable.dingwei_zidingyi);
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
								.latitude(mLatitude)//
								.longitude(mLongtitude)//
								.build();
						mBaiduMap.setMyLocationData(locData);
					}
				});
		/**
		 * 弹出图层框
		 */
		shouye_tuceng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (tuceng % 2 == 0) {
					tuceng_linearlayout.setVisibility(View.VISIBLE);
					shouye_tuceng
							.setBackgroundResource(R.drawable.abc_ic_clear_search_api_holo_light);
					tuceng++;
				} else {
					tuceng_linearlayout.setVisibility(View.GONE);
					shouye_tuceng.setBackgroundResource(R.drawable.traffice2);
					tuceng++;
				}
			}
		});
		putongditu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			}
		});
		weixingditu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			}
		});
		relitu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (relitus % 2 == 0) {
					mBaiduMap.setBaiduHeatMapEnabled(true);
					relitus++;
				} else {
					mBaiduMap.setBaiduHeatMapEnabled(false);
					relitus++;
				}
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
				dingwei_weizhi = new LatLng(mLatitude, mLongtitude);
				dangqian_City = location.getCity();
				if (OneTouchSearch_boolean) {
					OneTouchSearch_boolean = false;
					ooA.position(dingwei_weizhi)
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.onekey_normal_1))
							.zIndex(2111111119).draggable(false)
							.isPerspective();
					mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
					POIyijiansousuo();
				}
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
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	private void initEvent() {
		mArcMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onClick(View view, int pos) {

				if (pos == 1) {
					if (c == 0) {
						Intent intent = new Intent(Home_Activity.this,
								Log_Activity.class);
						startActivity(intent);
					} else {
						Intent in = new Intent(Home_Activity.this,
								Mine_Activity.class);
						startActivity(in);
					}
					Editor editor = pre.edit();
					editor.putInt("counts", ++c);
					editor.commit();
				}
				if (pos == 2) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							InputStream inputStream;
							// TODO Auto-generated method stub
							HttpGet get = new HttpGet(
							/*
							 * 512dfb7ad131b39b93dee0d9e0f1cef9
							 */
							"http://apis.juhe.cn/oil/local?key=512dfb7ad131b39b93dee0d9e0f1cef9&lon="
									+ mLongtitude + "&lat=" + mLatitude
									+ "&format=2&r=10000");// 实列化
							HttpClient client = new DefaultHttpClient();// 实列化一个默认的客户端
							try {
								HttpResponse httpResponse = client.execute(get);// 返回的是httprequest的请求结果
								if (httpResponse.getStatusLine()
										.getStatusCode()/* 状态相应码 */== 200) {
									inputStream = httpResponse.getEntity()
									/* 获得的网络数据 */.getContent()/* 转化成字节流 */;
									// String info =
									// EntityUtils.toString(httpResponse.getEntity());//
									// 可以把entity对象转化成一个字符串
									BufferedReader bufferedReader = new BufferedReader(
											new InputStreamReader(inputStream)/* 把字节流转化成字符流 */);
									String lin = "";
									String sb = "";
									while ((lin = bufferedReader.readLine()) != null) {
										sb += lin;
									}
									System.out.println(sb);
									Message message = new Message();
									Bundle bundle = new Bundle();
									bundle.putString("KEY", sb);
									message.setData(bundle);
									handler.sendMessage(message);
								} else {// 当相应请求失败的是是时候
									Log.d("TEST", "false");
								}
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
				}

				Toast.makeText(Home_Activity.this, pos + ":" + view.getTag(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 检索
	 */
	private void poijiansuo() {
		mPoiSearch = PoiSearch.newInstance();
		mBaiduMap.clear();
		final PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
		PoiCitySearchOption poiCitySearchOption = new PoiCitySearchOption();
		mPoiSearch.searchInCity(poiCitySearchOption.city(dangqian_City)
				// 每页容量
				.pageCapacity(50).keyword(Second_Activity.guanjianzi)
				.pageNum(0));

		mPoiSearch
				.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

					@Override
					public void onGetPoiResult(PoiResult result) {
						if (result == null
								|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
							Toast.makeText(Home_Activity.this, "未找到结果",
									Toast.LENGTH_LONG).show();
							return;
						}
						if (result.error == SearchResult.ERRORNO.NO_ERROR) {

							mBaiduMap.setOnMarkerClickListener(overlay);
							overlay.setData(result);
							overlay.addToMap();
							overlay.zoomToSpan();
							return;
						}
						if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

							// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
							String strInfo = "在";
							for (CityInfo cityInfo : result
									.getSuggestCityList()) {
								strInfo += cityInfo.city;
								strInfo += ",";
							}
							strInfo += "找到结果";
							Toast.makeText(Home_Activity.this, strInfo,
									Toast.LENGTH_LONG).show();
						}

					}

					@Override
					public void onGetPoiDetailResult(PoiDetailResult result) {
						// TODO Auto-generated method stub
						if (result.error != SearchResult.ERRORNO.NO_ERROR) {
							Toast.makeText(Home_Activity.this, "抱歉，未找到结果",
									Toast.LENGTH_SHORT).show();
						} else {
							if (mMarkerA == null) {

								ooA.position(result.getLocation())
										.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.marker_wz_1))
										.zIndex(2).draggable(false);
							} else {
								mMarkerA.remove();
								ooA.position(result.getLocation())
										.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.marker_wz_1))
										.zIndex(2).draggable(false);
							}
							ooA.animateType(MarkerAnimateType.grow);
							mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

							biaoji_tv1.setText(result.getAddress());// 显示地址信息
							biaoji_tv2.setText(result.getAddress());// 显示地址信息
							biaoji_name.setText(result.getName());// 显示名字
							biaoji_name2.setText(result.getName());// 显示名字
							layout_daohang1.setVisibility(View.VISIBLE);// 显示控件
							marker_weizhi = result.getLocation();

						}
					}
				});

		Second_Activity.guanjianzi = null;

	}

	/**
	 * Poi一键检索
	 */
	private void POIyijiansousuo() {
		mBaiduMap
				.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

					@Override
					public boolean onMarkerClick(Marker arg0) {

						// mBaiduMap.clear();
						if (mMarkerA != null) {
							mMarkerA.remove();
						}

						mPoiSearch.searchNearby(poiNearbySearchOption
								.keyword("加油站").location(dingwei_weizhi)
								.radius(5000).pageNum(0));
						mPoiSearch
								.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

									@Override
									public void onGetPoiResult(PoiResult result) {
										if (result == null
												|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
											Toast.makeText(Home_Activity.this,
													"未找到结果", Toast.LENGTH_LONG)
													.show();
											return;
										}
										if (result.error == SearchResult.ERRORNO.NO_ERROR) {
											PoiOverlay overlay = new MyPoiOverlay(
													mBaiduMap);
											mBaiduMap
													.setOnMarkerClickListener(overlay);
											overlay.setData(result);
											overlay.addToMap();
											overlay.zoomToSpan();
											return;
										}
										if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

											// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
											String strInfo = "在";
											for (CityInfo cityInfo : result
													.getSuggestCityList()) {
												strInfo += cityInfo.city;
												strInfo += ",";
											}
											strInfo += "找到结果";
											Toast.makeText(Home_Activity.this,
													strInfo, Toast.LENGTH_LONG)
													.show();
										}

									}

									@Override
									public void onGetPoiDetailResult(
											PoiDetailResult result) {
										if (result.error != SearchResult.ERRORNO.NO_ERROR) {
											Toast.makeText(Home_Activity.this,
													"抱歉，未找到结果",
													Toast.LENGTH_SHORT).show();
										} else {

											if (mMarkerA == null) {
												ooA.position(
														result.getLocation())
														.icon(BitmapDescriptorFactory
																.fromResource(R.drawable.marker_wz_1))
														.zIndex(2)
														.draggable(false)
														.isPerspective();
											} else {
												mMarkerA.remove();
												ooA.position(
														result.getLocation())
														.icon(BitmapDescriptorFactory
																.fromResource(R.drawable.marker_wz_1))
														.zIndex(2)
														.draggable(false)
														.isPerspective();
											}
											ooA.animateType(MarkerAnimateType.grow);
											mMarkerA = (Marker) (mBaiduMap
													.addOverlay(ooA));

											biaoji_tv1.setText(result
													.getAddress());// 显示地址信息
											biaoji_tv2.setText(result
													.getAddress());// 显示地址信息
											biaoji_name.setText(result
													.getName());// 显示名字
											biaoji_name2.setText(result
													.getName());// 显示名字
											layout_daohang1
													.setVisibility(View.VISIBLE);// 显示控件
											marker_weizhi = result
													.getLocation();

										}

									}
								});

						return false;
					}
				});

	}

	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
			mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
					.poiUid(poi.uid));
			// }
			return true;
		}
	}

	// 油价排行加油站
	public void jiayou() {
		mBaiduMap.clear();
		marker_weizhi = Price_Activity.Price_latlng;
		ooA.position(marker_weizhi)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.marker_wz_1)).zIndex(1)
				.draggable(false);

		ooA.animateType(MarkerAnimateType.grow);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

		System.out.println("==========" + marker_weizhi);
		biaoji_tv1.setText(Price_Activity.Price_address);// 显示地址信息
		biaoji_tv2.setText(Price_Activity.Price_address);// 显示地址信息
		biaoji_name.setText(Price_Activity.Price_name);// 显示名字
		biaoji_name2.setText(Price_Activity.Price_name);// 显示名字
		layout_daohang1.setVisibility(View.VISIBLE);
		// 将搜索到的位置移动到当前界面
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
				.newLatLng(marker_weizhi);
		mBaiduMap.animateMapStatus(mapStatusUpdate);

		Price_Activity.Price_latlng = null;

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (Price_Activity.Price_latlng != null) {
			jiayou();
		}
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
		mPoiSearch.destroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		if (Second_Activity.guanjianzi != null) {
			poijiansuo();
		}
		if (Second_Activity.seconds_shousous != null) {
			marker_weizhi_biaoji2();
		}
	}

}