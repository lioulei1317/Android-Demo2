package com.example.broadcastreceivertext;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {
	private IntentFilter intentFilter;
	private NetWorkChangedReceiver netreceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		intentFilter = new IntentFilter();
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		netreceiver = new NetWorkChangedReceiver();
		registerReceiver(netreceiver, intentFilter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(netreceiver);
	}

	public class NetWorkChangedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(arg0.CONNECTIVITY_SERVICE);
			NetworkInfo networkinfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkinfo != null && networkinfo.isAvailable()) {
				Toast.makeText(MainActivity.this, "已打开网络连接", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(MainActivity.this, "已断开网络连接", Toast.LENGTH_SHORT)
						.show();
			}

		}

	}
}
