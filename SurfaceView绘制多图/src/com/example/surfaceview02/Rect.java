package com.example.surfaceview02;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Rect extends Contanier{
	private Paint paint;
	public Rect(){
		paint=new Paint();
		paint.setColor(Color.RED);
	}
	
	@Override
	public void childrenView(Canvas canvas) {
		// TODO Auto-generated method stub
		super.childrenView(canvas);
		canvas.drawRect(0, 0, 100, 100, paint);
		this.setY(this.getY()+50);
		this.setX(this.getX()+(int)(Math.random()*50-20));
		if(getY()>1280){
			setY(0);
		}
		if(getX()>720){
			setX(0);
			}
	}
}
