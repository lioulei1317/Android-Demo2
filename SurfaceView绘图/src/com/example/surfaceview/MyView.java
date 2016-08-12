package com.example.surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MyView extends SurfaceView implements Callback {
	private Paint paint;
	public MyView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		paint=new Paint();
		paint.setColor(Color.RED);
		getHolder().addCallback(this);
	}

	public void draw() {
		//Ëø¶¨»­²¼
		Canvas canvas=getHolder().lockCanvas();
		canvas.drawColor(Color.WHITE);
		//±£´æ×´Ì¬
		canvas.save();
		canvas.rotate(90, getWidth()/2, getHeight()/2);
		canvas.drawLine(0, getHeight()/2, getWidth(), getHeight(), paint);
		//¸´Ô­×´Ì¬
		canvas.restore();
		canvas.drawLine(0, getHeight()/2+100, getWidth(), getHeight()+100, paint);
		//½âËø»­²¼
		getHolder().unlockCanvasAndPost(canvas);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		draw();
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		draw();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

}
