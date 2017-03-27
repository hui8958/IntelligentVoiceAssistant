package edu.hui.vassistant.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import edu.hui.vassistant.R;

public class HelpActivity extends Activity implements OnClickListener {

	ImageButton weather = null;
	ImageButton map = null;
	ImageButton phone = null;
	ImageButton sms = null;
	ImageButton email = null;
	ImageButton google = null;
	ImageButton wiki = null;
	ImageButton reminder = null;
	ImageButton alarm = null;
	ImageButton music = null;
	ImageButton camera = null;
	ImageButton chat = null;
	ImageButton translate = null;
	ImageView fPage = null;
	ImageView sPage = null;
	public ViewFlipper flipper;
	private GestureDetector detector;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.helpview);
		weather = (ImageButton) findViewById(R.id.imageButton1);
		weather.setOnClickListener(this);
		map = (ImageButton) findViewById(R.id.ImageButton01);
		map.setOnClickListener(this);
		phone = (ImageButton) findViewById(R.id.ImageButton02);
		phone.setOnClickListener(this);

		sms = (ImageButton) findViewById(R.id.ImageButton03);
		sms.setOnClickListener(this);
		email = (ImageButton) findViewById(R.id.ImageButton08);
		email.setOnClickListener(this);
		google = (ImageButton) findViewById(R.id.ImageButton09);
		google.setOnClickListener(this);
		wiki = (ImageButton) findViewById(R.id.ImageButton04);
		wiki.setOnClickListener(this);
		reminder = (ImageButton) findViewById(R.id.ImageButton05);
		reminder.setOnClickListener(this);
		alarm = (ImageButton) findViewById(R.id.ImageButton06);
		alarm.setOnClickListener(this);
		music = (ImageButton) findViewById(R.id.ImageButton12);
		music.setOnClickListener(this);
		camera = (ImageButton) findViewById(R.id.ImageButton13);
		camera.setOnClickListener(this);
		chat = (ImageButton) findViewById(R.id.ImageButton14);
		chat.setOnClickListener(this);
		translate = (ImageButton) findViewById(R.id.imageButton2);
		translate.setOnClickListener(this);
		fPage = (ImageView) findViewById(R.id.imageView2);
		sPage = (ImageView) findViewById(R.id.imageView1);
		flipper = (ViewFlipper) findViewById(R.id.details);
		flipper.setLongClickable(true);
		flipper.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				detector.onTouchEvent(event);
				return false;
			}
		});

		detector = new GestureDetector(this, new OnGestureListener() {
			public boolean onDown(MotionEvent e) {
				// 用户轻触屏幕。（单击）
				return true;
			}

			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {

				int x = (int) (e2.getX() - e1.getX());
				if (x > 0) {
					flipper.setInAnimation(HelpActivity.this,
							R.anim.push_right_in);
					flipper.setOutAnimation(HelpActivity.this,
							R.anim.push_right_out);
					flipper.showPrevious();
					changePageView();
				} else {
					flipper.setInAnimation(HelpActivity.this,
							R.anim.push_left_in);
					flipper.setOutAnimation(HelpActivity.this,
							R.anim.push_left_out);
					flipper.showNext();
					changePageView();
				}
				return true;
			}

			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				// 用户长按屏幕

			}

			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				// TODO Auto-generated method stub
				return false;// 用户按下屏幕并拖动

			}

			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				// 用户轻触屏幕，尚末松开或拖动，注意，强调的是没有没有松开或者拖动状态
			}

			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;// 用户轻触屏幕后松开。
			}
		});

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v instanceof ImageButton) {
			Intent i = new Intent();
			i.setClass(HelpActivity.this, HelpContentActivity.class);
			ImageButton ib = (ImageButton) v;
			switch (ib.getId()) {
			case R.id.imageButton1:
				i.putExtra("name", "weather");
				break;
			case R.id.ImageButton01:
				i.putExtra("name", "map");
				break;
			case R.id.ImageButton02:
				i.putExtra("name", "phone");
				break;
			case R.id.ImageButton03:
				i.putExtra("name", "sms");
				break;
			case R.id.ImageButton08:
				i.putExtra("name", "email");
				break;
			case R.id.ImageButton09:
				i.putExtra("name", "google");
				break;
			case R.id.ImageButton04:
				i.putExtra("name", "wikipedia");
				break;
			case R.id.ImageButton05:
				i.putExtra("name", "reminder");
				break;
			case R.id.ImageButton06:
				i.putExtra("name", "alarm");
				break;
			case R.id.ImageButton12:
				i.putExtra("name", "music");
				break;
			case R.id.ImageButton13:
				i.putExtra("name", "camera");
				break;
			case R.id.ImageButton14:
				i.putExtra("name", "chat");
				break;
			case R.id.imageButton2:
				i.putExtra("name", "translate");
				break;
			}
			startActivity(i);
		}
	}

	public void changePageView() {
		System.out.println("changePageView");
		Drawable f = fPage.getDrawable();
		Drawable s = sPage.getDrawable();
		fPage.setImageDrawable(s);
		sPage.setImageDrawable(f);
	}

}
