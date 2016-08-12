package com.example.mapxml;

import com.imooc.arcmenu.view.ArcMenu;
import com.imooc.arcmenu.view.ArcMenu.OnMenuItemClickListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Mine_Activity extends Activity {
	private ArcMenu mArcMenu;
	Bitmap mine_img;
	TextView mine_text;
	ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mine_);
		mArcMenu = (ArcMenu) findViewById(R.id.id_menu);
		image = (ImageView) findViewById(R.id.mine_img);
		mine_text = (TextView) findViewById(R.id.mine_text);
		initEvent();
	}

	private void initEvent() {
		mArcMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onClick(View view, int pos) {
				if (pos == 1) {
					Intent intent = new Intent(Mine_Activity.this,
							Log_Activity.class);
					startActivity(intent);
				}else if(pos == 3){
					Intent intent = new Intent(Mine_Activity.this,
							Home_Activity.class);
					startActivity(intent);
					finish();
				}
				Toast.makeText(Mine_Activity.this, pos + ":" + view.getTag(),
						Toast.LENGTH_SHORT).show();
			}
		});
		mine_text.setText(Log_Activity.info_text);
		Bitmap bb = Log_Activity.logo_bitmap;
		image.setImageBitmap(bb);

	}
}
