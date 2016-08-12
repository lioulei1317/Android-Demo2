package com.example.orientation;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 方向传感器
 * */
public class MyOrientationListener implements SensorEventListener {
	// 方向传感器的管理
	private SensorManager mSensorManager;
	private Context context;
	// 方向传感器
	private Sensor mSensor;

	private float lastX;
	
	private OnOrientationListener mOnOrientationListener;

	public MyOrientationListener(Context context) {
		this.context = context;
	}

	// 开始
	@SuppressWarnings("deprecation")
	public void start() {
		// 获得传感器管理器
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager != null) {
			// 获得方向传感器
			mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		// 注册
		if (mSensor != null) {
			mSensorManager.registerListener(this, mSensor,
					SensorManager.SENSOR_DELAY_UI);
		}
	}
	// 停止检测
	public void stop() {
		mSensorManager.unregisterListener(this);
	}

	// 经度发生改变时实现的方法
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	// 方向发生变化时实现的方法
	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		// 接受方向感应器的类型  
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			// 这里我们可以得到数据，然后根据需要来处理  
			float x = event.values[SensorManager.DATA_X];
			if (Math.abs(x - lastX) > 1.0) {
//				if (mOnOrientationListener != null) {
					mOnOrientationListener.onOrientationChanged(x);
//				}
			}
			lastX = x;
		}
	}



	public void setmOnOrientationListener(
			OnOrientationListener mOnOrientationListener) {
		this.mOnOrientationListener = mOnOrientationListener;
	}

	public interface OnOrientationListener {
		void onOrientationChanged(float x);
	}

}
