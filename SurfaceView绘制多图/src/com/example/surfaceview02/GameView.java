package com.example.surfaceview02;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Callback {
	private Contanier contanier;
	private Rect rect;
	private Circle circle;
	private Timer timer = null;
	private TimerTask task = null;

	public GameView(Context context) {
		super(context);
		contanier = new Contanier();
		rect = new Rect();
		circle = new Circle();
		rect.addChildrenView(circle);
		contanier.addChildrenView(rect);
		getHolder().addCallback(this);
	}

	public void draw() {
		Canvas canvas = getHolder().lockCanvas();
		canvas.drawColor(Color.WHITE);
		contanier.draw(canvas);
		getHolder().unlockCanvasAndPost(canvas);
	}

	public void startTimer() {
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				draw();
			}
		};
		timer.schedule(task, 100, 100);
	}

	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		startTimer();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		stopTimer();
	}

}
