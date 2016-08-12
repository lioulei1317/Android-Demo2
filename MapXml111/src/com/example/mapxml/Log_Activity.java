package com.example.mapxml;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sql.MyDatabaseHelper;
import com.example.util.Util;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class Log_Activity extends Activity {
	private static final String TAG = Log_Activity.class.getName();
	public static String mAppid;
	EditText user, pass;
	Context context;
	SQLiteDatabase db;
	Cursor cursor;
	MyDatabaseHelper helper;
	String word, detail;
	TextView zhuce, qq_nickname;
	ImageView log_wechat, log_qq, qq_logo, login;
	private UserInfo mInfo;
	private Tencent mTencent;
	private final String APP_ID = "222222";// 测试时使用，真正发布的时候要换成自己的APP_ID
	public static QQAuth mQQAuth;
	public static Bitmap logo_bitmap;
	public static String info_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_log_);
		context = this;
		initview();
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "-->onStart");
		final Context context = Log_Activity.this;
		final Context ctxContext = context.getApplicationContext();
		mAppid = APP_ID;
		mQQAuth = QQAuth.createInstance(mAppid, ctxContext);
		mTencent = Tencent.createInstance(mAppid, Log_Activity.this);
		super.onStart();
	}

	public void initview() {
		login = (ImageView) findViewById(R.id.login);
		user = (EditText) findViewById(R.id.user_log);
		pass = (EditText) findViewById(R.id.pass_log);
		zhuce = (TextView) findViewById(R.id.zhuce);
		log_wechat = (ImageView) findViewById(R.id.log_wechat);
		log_qq = (ImageView) findViewById(R.id.log_qq);

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.log_container);
		OnClickListener listener = new NewClickListener();
		for (int i = 0; i < linearLayout.getChildCount(); i++) {
			View view = linearLayout.getChildAt(i);
			if (view instanceof ImageView) {
				view.setOnClickListener(listener);
			}
		}

		helper = new MyDatabaseHelper(this, "myMap.db3", 1);
		zhuce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(Log_Activity.this, Reg_Activity.class);
				startActivity(in);
			}
		});
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				word = user.getText().toString();
				detail = pass.getText().toString();
				cursor = helper
						.getReadableDatabase()
						.rawQuery(
								"select * from dict_ where word like ? and detail like ?",
								new String[] { word, detail });
				if (cursor.moveToNext()) {
					Intent intent = new Intent(Log_Activity.this,
							Mine_Activity.class);
					startActivity(intent);
					Toast.makeText(Log_Activity.this, "登陆成功", 8000).show();
				} else if (word.equals("") || detail.equals("")) {
					Toast.makeText(Log_Activity.this, "用户名或者密码不能为空", 8000)
							.show();
				} else {
					Toast.makeText(Log_Activity.this, "登录失败", 8000).show();
				}
				cursor.close();

			}
		});

	}

	private void updateUserInfo() {
		if (mQQAuth != null && mQQAuth.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onComplete(final Object response) {
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					mHandler.sendMessage(msg);
					new Thread() {

						@Override
						public void run() {
							JSONObject json = (JSONObject) response;
							if (json.has("figureurl")) {
								Bitmap bitmap = null;
								try {
									bitmap = Util.getbitmap(json
											.getString("figureurl_qq_2"));
								} catch (JSONException e) {

								}
								Message msg = new Message();
								msg.obj = bitmap;
								msg.what = 1;
								mHandler.sendMessage(msg);
							}
						}

					}.start();
				}

				@Override
				public void onCancel() {
				}
			};
			mInfo = new UserInfo(this, mQQAuth.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
			// qq_nickname.setText("");
			// qq_nickname.setVisibility(android.view.View.GONE);
			// qq_logo.setVisibility(android.view.View.GONE);
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				JSONObject response = (JSONObject) msg.obj;
				if (response.has("nickname")) {
					try {
						info_text = response.getString("nickname");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (msg.what == 1) {

				logo_bitmap = (Bitmap) msg.obj;
				Intent intent = new Intent(Log_Activity.this,
						Mine_Activity.class);
//				intent.putExtra("name", info_text);

				startActivity(intent);
			}

		}

	};

	private void onClickLogin() {
		if (!mQQAuth.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					updateUserInfo();

				}
			};
			mQQAuth.login(this, "all", listener);

			mTencent.login(this, "all", listener);
		} else {
			mQQAuth.logout(this);
			updateUserInfo();

		}
	}

	public static boolean ready(Context context) {
		if (mQQAuth == null) {
			return false;
		}
		boolean ready = mQQAuth.isSessionValid()
				&& mQQAuth.getQQToken().getOpenId() != null;
		if (!ready)
			Toast.makeText(context, "login and get openId first, please!",
					Toast.LENGTH_SHORT).show();
		return ready;
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {

			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			Util.toastMessage(Log_Activity.this, "onError: " + e.errorDetail);
			Util.dismissDialog();
		}

		@Override
		public void onCancel() {
			Util.toastMessage(Log_Activity.this, "onCancel: ");
			Util.dismissDialog();
		}
	}

	class NewClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Context context = v.getContext();
			Class<?> cls = null;
			switch (v.getId()) {
			case R.id.log_qq:

				onClickLogin();
				finish();
				return;
			}
			if (cls != null) {
				Intent intent = new Intent(context, cls);
				context.startActivity(intent);
			}
		}
	}
}
