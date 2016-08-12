package com.example.daohang1;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
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
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import com.example.orientation.MyOrientationListener;
import com.example.orientation.MyOrientationListener.OnOrientationListener;
import com.imooc.arcmenu.view.ArcMenu;
import com.imooc.arcmenu.view.ArcMenu.OnMenuItemClickListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

public class MainActivity extends Activity {

	/**
	 * ��ͼ�ؼ�
	 */
	private MapView mMapView;
	private ArcMenu mArcMenu;
	/**
	 * ��ͼʵ��
	 */
	private BaiduMap mBaiduMap;
	private Context context;
	/**
	 * ��λ�Ŀͻ���
	 */
	private LocationClient mLocClient;
	/**
	 * ��λ�ļ�����
	 */
	public MyLocationListenner myListener;
	/**
	 * ��ǰ��λ��ģʽ
	 */
	private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;
	// private static final int accuracyCircleFillColor = 0xAAFFFF88;
	// private static final int accuracyCircleStrokeColor = 0xAA00FF00;

	// UI���
	// OnCheckedChangeListener radioButtonListener;
	private ImageButton requestLocButton;
	/***
	 * �Ƿ��ǵ�һ�ζ�λ
	 */
	private boolean isFirstLoc = true;
	/**
	 * ����һ�εľ�γ��
	 */
	private double mLatitude;
	private double mLongtitude;
	/**
	 * �Զ���ͼ��
	 * */
	private BitmapDescriptor mIconLocation;

	/**
	 * ��λ�ļ�����
	 */
	private MyOrientationListener myOrientationListener;

	private float mCurrentX;
	/**
	 * ��ǰ�ľ���
	 */
	private float mCurrentAccracy;

	// ���·�߽ڵ����
	Button mBtnPre = null; // ��һ���ڵ�
	Button mBtnNext = null; // ��һ���ڵ�
	int nodeIndex = -1; // �ڵ�����,������ڵ�ʱʹ��
	RouteLine route = null;
	OverlayManager routeOverlay = null;
	boolean useDefaultIcon = false;
	private TextView popupText = null; // ����view

	// ��ͼ��أ�ʹ�ü̳�MapView��MyRouteMapViewĿ������дtouch�¼�ʵ�����ݴ���
	// ���������touch�¼���������̳У�ֱ��ʹ��MapView����
	RoutePlanSearch mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��

	private Marker mMarkerA;
	BitmapDescriptor marker_bitmap;// ����Markerͼ��

	GeoCoder geoCoder1 = null;
	LatLng dingwei_weizhi, marker_weizhi;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		this.context = this;
		initView();
		initEvent();
		// ����logo
		View child = mMapView.getChildAt(1);
		if (child != null
				&& (child instanceof ImageView || child instanceof ZoomControls)) {
			child.setVisibility(View.INVISIBLE);
		}

		// ��ȡ��Ļ�Ŀ�͸�
		WindowManager wm = this.getWindowManager();
		final int width = (int) (wm.getDefaultDisplay().getWidth() * 0.86);
		final int height = (int) (wm.getDefaultDisplay().getHeight() * 0.46);
		mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {

				mMapView.setZoomControlsPosition(new Point(width, height));

			}
		});

		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		// ��ͼ����¼�����
		mBaiduMap.setOnMapClickListener(mblistener);
		// ��ʼ������ģ�飬ע���¼�����
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(msListener);

		marker_weizhi_ff();// ������ͼ����Ӹ����ﶨλ

	}

	/**
	 * ���λ��
	 * 
	 * 
	 * */

	private void marker_weizhi_ff() {
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);// ����һ�����µ�ͼ��msu����Ȼ�����øö���Ϊ���ŵȼ�14.0��������õ�ͼ״̬��
		marker_bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.marker_wz);// ����Markerͼ��

		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override						//��ǰ�û�ѡȡ������
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

				marker_weizhi = arg0;
			}
		});
	}

	/**
	 * ��س�ʼ��
	 * */
	private void initView() {
		// TODO Auto-generated method stub
		mMapView = (MapView) findViewById(R.id.shouye_bmapView);
		mBaiduMap = mMapView.getMap();
		// ������λͼ��
		mArcMenu = (ArcMenu) findViewById(R.id.id_menu);
		requestLocButton = (ImageButton) findViewById(R.id.shouye_dingwei);
		mCurrentMode = LocationMode.NORMAL;
		// requestLocButton.setText("Ĭ��");
		requestLocButton.setImageResource(R.drawable.dingwei_moren);
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					// ��ͨ
					requestLocButton
							.setImageResource(R.drawable.dingwei_putong);
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					// ����
					requestLocButton
							.setImageResource(R.drawable.dingwei_luopan);
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					// ����
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

		// radioButtonListener = new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		//
		// // ����null�򣬻ָ�Ĭ��ͼ��
		// mCurrentMarker = null;
		// mBaiduMap
		// .setMyLocationConfigeration(new MyLocationConfiguration(
		// mCurrentMode, true, null));
		//
		// }
		// };

		mBaiduMap.setMyLocationEnabled(true);
		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		myListener = new MyLocationListenner();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// ��ʼ���Զ���ͼ��
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
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(mCurrentX)
					//
					.latitude(location.getLatitude())
					.longitude(location.getLongitude())//
					.build();
			mCurrentAccracy = location.getRadius();
			// ���ö�λ����
			mBaiduMap.setMyLocationData(locData);
			// �����Զ���ͼ��
			// MyLocationConfiguration config = new MyLocationConfiguration(
			// mCurrentMode, true, mIconLocation);
			// mBaiduMap.setMyLocationConfigeration(config);

			// ���¾�γ��
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

			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// ������λ
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocClient.isStarted())
			mLocClient.start();
		// �������򴫸���
		myOrientationListener.start();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// ֹͣ��λ
		mBaiduMap.setMyLocationEnabled(false);
		mLocClient.stop();
		// ֹͣ���򴫸���
		myOrientationListener.stop();
	}

	/**
	 * ����·�߹滮����ʾ��
	 */
	public void searchButtonProcess(View v) {
		// ��������ڵ��·������
		route = null;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);

		// ����·�ߵ���㡢�յ�
		PlanNode stNode = PlanNode.withLocation(dingwei_weizhi);
		PlanNode enNode = PlanNode.withLocation(marker_weizhi);

		// ʵ��ʹ�����������յ���н�����ȷ���趨
		if (v.getId() == R.id.drive) {
			mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode)
					.to(enNode));
		}
		mBaiduMap.clear();// ���ͼ��
	}

	/**
	 * �ڵ����ʾ��
	 */
	public void nodeClick(View v) {
		if (route == null || route.getAllStep() == null) {
			return;
		}
		if (nodeIndex == -1 && v.getId() == R.id.pre) {
			return;
		}
		// ���ýڵ�����
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
		// ��ȡ�ڽ����Ϣ
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
		// �ƶ��ڵ�������
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

	// ����RouteOverly���ݳ�·�߹滮��
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
		// ��ͼ�����¼������ӿ�
		@Override
		public boolean onMapPoiClick(MapPoi arg0) {
			//��ͼ�����¼��ص�����
			return false;
		}

		@Override
		public void onMapClick(LatLng arg0) {
			//��ͼ�� Poi �����¼��ص�����
			mBaiduMap.hideInfoWindow();
			//���ص�ǰ InfoWindow
		}
	};

	OnGetRoutePlanResultListener msListener = new OnGetRoutePlanResultListener() {
		// ·�߹滮����ص�

		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult result) {
			// �ݳ�·�߽���ص�onGetDrivingRouteResult()
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(MainActivity.this, "��Ǹ��δ�ҵ����",
						Toast.LENGTH_SHORT).show();
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// ���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
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
			// ����·�߽���ص�

		}

		@Override
		public void onGetBikingRouteResult(BikingRouteResult arg0) {
			// ����·�߽���ص�

		}

		@Override
		public void onGetTransitRouteResult(TransitRouteResult arg0) {
			// ����·�߽���ص�

		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSearch.destroy();
		// �˳�ʱ���ٶ�λ
		mLocClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onDestroy();
		mMapView = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
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
