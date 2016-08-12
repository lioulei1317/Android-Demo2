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
		//����canvans״̬
		canvas.save();
		//λ������
		canvas.translate(200, 200);
		//��ת���ĵ�
		canvas.rotate(degrees, 50, 50);
		canvas.drawRect(0, 0, 100, 100, p);
		degrees++;
		//�ָ�״̬
		canvas.restore();
		//ʹ���view��Ч
		invalidate();
	}
}
