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
 * ��ڣ���λ
 */
public class MainActivity extends Activity {
	/**
	 * ��ͼ�ؼ�
	 */
	private MapView mMapView;
	private ArcMenu mArcMenu;

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
	/**
	 * ��ͼʵ��
	 */
	private BaiduMap mBaiduMap;
	private Context context;

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
	/**
	 * ��ͨͼ�ļ�����
	 */
	int traffice = 0;
	EditText editText;

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
				/**
				 * ָ��ָ�����λ��
				 */
				mBaiduMap.setCompassPosition(new Point(110, 280));

			}
		});

	}

	/**
	 * ��س�ʼ��
	 * */
	private void initView() {
		// TODO Auto-generated method stub
		mMapView = (MapView) findViewById(R.id.shouye_bmapView);
		mArcMenu = (ArcMenu) findViewById(R.id.id_menu);
		requestLocButton = (ImageButton) findViewById(R.id.shouye_dingwei);
		editText = (EditText) findViewById(R.id.shouye_Editexts);
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

		/**
		 * Editext��ȡ����ʱ����
		 */
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {

				if (hasFocus) {
					// ��ȡ����ʱ
					Intent intent = new Intent(MainActivity.this,
							PoiSearch.class);
					startActivity(intent);

				} else {
					// ��������
				}
			}
		});

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

		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.shouye_bmapView);
		mBaiduMap = mMapView.getMap();
		// ������λͼ��
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
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * �����Ƿ���ʾ��ͨͼ
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
