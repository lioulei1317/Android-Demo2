package com.example.myview;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class RotatingRect extends View {
	private Paint p;
	private float degrees=0;
	public RotatingRect(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initProperties();
	}

	public RotatingRect(Context context, AttributeSet attrs) {
		super(context, attrs);
		initProperties();
	}

	public RotatingRect(Context context) {
		super(context);
		initProperties();
	}
	
	private void initProperties(){
		p=new Paint();
		p.setColor(Color.RED);
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		//保存canvans状态
		canvas.save();
		//位置坐标
		canvas.translate(200, 200);
		//旋转中心点
		canvas.rotate(degrees, 50, 50);
		canvas.drawRect(0, 0, 100, 100, p);
		degrees++;
		//恢复状态
		canvas.restore();
		//使这个view无效
		invalidate();
	}
}
