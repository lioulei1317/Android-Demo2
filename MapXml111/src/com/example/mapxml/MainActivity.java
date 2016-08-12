package com.example.mapxml;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	private SharedPreferences preference;
	private int count;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x11) {
				aniMation();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 放在setContentView前面
		setContentView(R.layout.activity_main);
		preference = getSharedPreferences("count", MODE_WORLD_READABLE);
		count = preference.getInt("count", 0);
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0x11);
			}
		}, 1000);
	}

	private void aniMation() {// 动画属性
		RelativeLayout splash_layout = (RelativeLayout) findViewById(R.id.home_r);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);// 透明度动画
		alphaAnimation.setDuration(1000);// 动画的时间
		alphaAnimation.setFillAfter(true);// //设置动画结束后保留结束状态
		alphaAnimation.setAnimationListener(new AnimationListener() {
			// 三个方法是Animation开始的时候调用，完成的时候调用，重复的时候调用。

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				if (count == 0) {// 第一次进入引导界面
					Intent intent = new Intent(MainActivity.this,
							Guide_Activity.class);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(MainActivity.this,
							Home_Activity.class);
					startActivity(intent);
					finish();

				}
				Editor editor = preference.edit();
				editor.putInt("count", ++count);
				editor.commit();
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}
		});
		splash_layout.startAnimation(alphaAnimation);// 加载动画
	}

}
