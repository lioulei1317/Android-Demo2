package com.example.mapxml;

import com.example.sql.MyDatabaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Reg_Activity extends Activity {
	ImageView reg;
	EditText user, pass;
	MyDatabaseHelper helper;
	SQLiteDatabase db;
	Context context;
	String word, detail;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reg_);
		user = (EditText) findViewById(R.id.user_reg);
		pass = (EditText) findViewById(R.id.pass_reg);
		reg = (ImageView) findViewById(R.id.reg);
		context = this;
		helper = new MyDatabaseHelper(this, "myMap.db3", 1);
		reg.setOnClickListener(new OnClickListener() {

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
					Toast.makeText(Reg_Activity.this, "用户名重复", 4000).show();

				} else if (word.equals("") || detail.equals("")) {
					Toast.makeText(Reg_Activity.this, "用户名或者密码不能为空", 8000)
							.show();
				} else {

					insertData(helper.getReadableDatabase(), word, detail);
					Intent intent = new Intent(Reg_Activity.this,
							Log_Activity.class);
					startActivity(intent);
					Toast.makeText(Reg_Activity.this, "注册成功", 8000).show();
				}
				cursor.close();
			}
		});
	}

	private void insertData(SQLiteDatabase db, String word, String detail) {
		// 插入查询语句
		db.execSQL("insert into dict_ values(null , ? , ?)", new String[] {
				word, detail });
	}

}
