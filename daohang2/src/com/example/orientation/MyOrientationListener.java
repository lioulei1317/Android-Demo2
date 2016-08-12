package com.example.orientation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * ���򴫸���
 * */
public class MyOrientationListener implements SensorEventListener {
	// ���򴫸����Ĺ���
	private SensorManager mSensorManager;
	private Context context;
	// ���򴫸���
	private Sensor mSensor;

	private float lastX;
	
	private OnOrientationListener mOnOrientationListener;

	public MyOrientationListener(Context context) {
		this.context = context;
	}

	// ��ʼ
	@SuppressWarnings("deprecation")
	public void start() {
		// ��ô�����������
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager != null) {
			// ��÷��򴫸���
			mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}
		// ע��
		if (mSensor != null) {
			mSensorManager.registerListener(this, mSensor,
					SensorManager.SENSOR_DELAY_UI);
		}
	}
	// ֹͣ���
	public void stop() {
		mSensorManager.unregisterListener(this);
	}

	// ���ȷ����ı�ʱʵ�ֵķ���
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	// �������仯ʱʵ�ֵķ���
	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		// ���ܷ����Ӧ��������  
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			// �������ǿ��Եõ����ݣ�Ȼ�������Ҫ������  
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
