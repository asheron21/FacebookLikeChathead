package com.example.chatheads;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ChatHeadService extends Service {

	private WindowManager windowManager;
	private ImageView chatHead;
	WindowManager.LayoutParams params;

	Globals sharedData = Globals.getInstance();



	@Override
	public void onCreate() {
		super.onCreate();

        int h = 0,w = 0;
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		//2880×1440 resolution
        final DisplayMetrics metrics = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        Method mGetRawH = null, mGetRawW = null;

        try {
            // For JellyBean 4.2 (API 17) and onward
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealMetrics(metrics);

                w = metrics.widthPixels;
                h = metrics.heightPixels;
            } else {
                mGetRawH = Display.class.getMethod("getRawHeight");
                mGetRawW = Display.class.getMethod("getRawWidth");

                try {
                    w = (Integer) mGetRawW.invoke(display);
                    h = (Integer) mGetRawH.invoke(display);
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        }

		//String s = "w: " + w + " h: " + h;
		//Toast toast1 = Toast.makeText(getBaseContext(),s, Toast.LENGTH_SHORT);
		//toast1.show();

		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		final Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(5);
		paint.setAlpha(60);
		Rect rect=new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
		canvas.drawRect(rect,paint);
		paint.setColor(Color.GREEN);
		canvas.drawLine(0, 0, 0, h, paint);
		canvas.drawLine(0, 0, w, 0, paint);
		canvas.drawLine(w, 0, h, w, paint);
		canvas.drawLine(0, 0, 0, w, paint);


		chatHead = new ImageView(this);
		chatHead.setImageResource(R.drawable.face1);

		params= new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 1000;







		//this code is for dragging the chat head around
		chatHead.setOnTouchListener(new View.OnTouchListener() {
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;
			boolean isExpanded = false;


			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = params.x;
					initialY = params.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();

					//int n = sharedData.getValue();
					//Toast toast1 = Toast.makeText(getBaseContext(),String.valueOf(n), Toast.LENGTH_SHORT);
					//toast1.show();
					if (isExpanded == true){
						chatHead.setImageResource(R.drawable.face1);
						isExpanded=false;
						params.x = 0;
						params.y = 1000;
						chatHead.getLayoutParams().width = 300;
						chatHead.getLayoutParams().height = 300;
						chatHead.requestLayout();
						windowManager.updateViewLayout(chatHead, params);
					}
					else{ //TO BECOME BIG

						sendMessage();
						Bitmap bmp2 = sharedData.getScreenshot();

						chatHead.setImageBitmap(bmp);
						if (bmp2!=null) chatHead.setImageBitmap(bmp2);

						isExpanded = true;
						params.x = 0;
						params.y = 0;
						chatHead.getLayoutParams().width = 2880;
						chatHead.getLayoutParams().height = 1440;
						chatHead.requestLayout();
						windowManager.updateViewLayout(chatHead, params);
					}
					return true;
				case MotionEvent.ACTION_UP:
					//chatHead.setImageResource(R.drawable.test);
					return true;
				/*case MotionEvent.ACTION_MOVE:
					params.x = initialX
							+ (int) (event.getRawX() - initialTouchX);
					params.y = initialY
							+ (int) (event.getRawY() - initialTouchY);
					windowManager.updateViewLayout(chatHead, params);
					return true;*/
				}
				return false;
			}
		});
		windowManager.addView(chatHead, params);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatHead != null)
			windowManager.removeView(chatHead);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void sendMessage() {
		Intent intent = new Intent("my-event");
		// add data
		intent.putExtra("message", "takeShot");
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
}