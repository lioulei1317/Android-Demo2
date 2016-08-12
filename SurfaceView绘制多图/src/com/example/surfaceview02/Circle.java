package com.example.surfaceview02;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Circle extends Contanier {
	private Paint paint = null;

	public Circle() {
		paint = new Paint();
		paint.setColor(Color.BLUE);
	}

	@Override
	public void childrenView(Canvas canvas) {
		// TODO Auto-generated method stub
		super.childrenView(canvas);
		canvas.drawCircle(50, 50, 50, paint);
	}
}
