package edu.hui.vassistant.activities;

import edu.hui.vassistant.R;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {

	ImageView imv = null;
	String path = "";
	Intent intent = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.imageview);
		imv = (ImageView) this.findViewById(R.id.imageViewFull);
		intent = getIntent();
		path = intent.getStringExtra("path");
		Bitmap myBitmap = BitmapFactory.decodeFile(path);
		Display display = getWindowManager().getDefaultDisplay(); 

		Bitmap resize = Bitmap.createScaledBitmap(myBitmap, display.getHeight(), display.getWidth(), true);
		Matrix m = new Matrix();
		int width = resize.getWidth();
		int height = resize.getHeight();
		m.setRotate(90);
		Bitmap b = Bitmap.createBitmap(resize, 0, 0, width, height, m, true);  
  
		imv.setImageBitmap(b);
	}

	public void onFinish() {
		this.finish();
	}
}
