package edu.hui.vassistant.entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import edu.hui.vassistant.R;


import edu.hui.vassistant.activities.ImageViewActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class DetailAdapter implements ListAdapter {
	private ArrayList<DetailEntity> coll;
	private Context ctx;

	public DetailAdapter(Context context, ArrayList<DetailEntity> coll) {
		ctx = context;
		this.coll = coll;
	}

	public boolean areAllItemsEnabled() {
		return false;
	}

	public boolean isEnabled(int arg0) {
		return false;
	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

	
		
		if (coll.get(position).getDetailName().equals("text")) {

		//	Log.v("Find", coll.get(position).getDetailName());
			DetailText entity = (DetailText) coll.get(position);

			int itemLayout = entity.getLayoutID();

			LinearLayout layout = new LinearLayout(ctx);
			LayoutInflater vi = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi.inflate(itemLayout, layout, true);

			TextView tvText = (TextView) layout
					.findViewById(R.id.messagedetail_row_text);
			if(entity.getName().toLowerCase().equals("me")){
				tvText.setText("“ "+entity.getText()+" ”");
				tvText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
				tvText.getPaint().setFakeBoldText(true);//
			}else{
				tvText.setText(entity.getText());
			}
		
			return layout;
		} else if (coll.get(position).getDetailName().equals("weather")) {
		//	Log.v("Find", "Weather");

			DetailWeather dw = (DetailWeather) coll.get(position);
			Log.v("Find", dw.getDetailName());
			int itemLayout = dw.getLayoutID();

			LinearLayout layout = new LinearLayout(ctx);
			LayoutInflater vi = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi.inflate(itemLayout, layout, true);

			ImageView icon = (ImageView) layout
					.findViewById(R.id.imageViewIcon);
			
			
		
			icon.setImageResource(getImageID(dw.getIcon()));		
			
			TextView Temperature = (TextView) layout
					.findViewById(R.id.textViewTemperature);
			Temperature.setText(dw.getTemperature());
			TextView DayInfo = (TextView) layout
					.findViewById(R.id.textViewDayInfo);
			DayInfo.setText(dw.getDayInfo());
			TextView condition = (TextView) layout
					.findViewById(R.id.textViewCondition);
			Log.v("condition", dw.getCondition());
			condition.setText(dw.getCondition());

			TextView Humidity = (TextView) layout
					.findViewById(R.id.textViewHumidity);
			Humidity.setText(dw.getHumidity());

			TextView wind = (TextView) layout.findViewById(R.id.textViewwind);
			wind.setText(dw.getWind());
			TextView cityName = (TextView) layout
					.findViewById(R.id.textViewCityName);
			cityName.setText(dw.getCityName());

			return layout;
		} else if (coll.get(position).getDetailName().equals("weatherweek")) {
			//Log.v("Find", "Weatherweek");

			WeatherWeek ww = (WeatherWeek) coll.get(position);
			int itemLayout = ww.getLayoutID();

			LinearLayout layout = new LinearLayout(ctx);
			LayoutInflater vi = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi.inflate(itemLayout, layout, true);

			for (int i = 0; i < ww.getDays().size(); i++) {
				System.out.println(ww.getIcons().get(i));
				switch (i) {
				case 0:
					TextView fistday = (TextView) layout
							.findViewById(R.id.textViewfirstday);
					fistday.setText(ww.getDays().get(i));

					ImageView icon1 = (ImageView) layout
							.findViewById(R.id.imageViewfirstdayicon);
				//	icon1.setImageBitmap(getHttpBitmap(ww.getIcons().get(i)));
					icon1.setImageResource(this.getImageID(ww.getIcons().get(i)));
					TextView hight1 = (TextView) layout
							.findViewById(R.id.textViewfirstdayhigh);
					hight1.setText(ww.getHts().get(i));

					TextView lowt1 = (TextView) layout
							.findViewById(R.id.textViewfirstdaylow);
					lowt1.setText(ww.getLts().get(i));

					TextView condition1 = (TextView) layout
							.findViewById(R.id.textViewfirstdaycondition);

					condition1.setText(ww.getConditions().get(i));

					break;
				case 1:
					TextView secondday = (TextView) layout
							.findViewById(R.id.TextViewsecondday);
					secondday.setText(ww.getDays().get(i));

					ImageView icon2 = (ImageView) layout
							.findViewById(R.id.ImageViewseconddayicon);
				//	icon2.setImageBitmap(getHttpBitmap(ww.getIcons().get(i)));
					icon2.setImageResource(this.getImageID(ww.getIcons().get(i)));
					TextView hight2 = (TextView) layout
							.findViewById(R.id.TextViewseconddayhigh);
					hight2.setText(ww.getHts().get(i));

					TextView lowt2 = (TextView) layout
							.findViewById(R.id.TextViewseconddaylow);
					lowt2.setText(ww.getLts().get(i));

					TextView condition2 = (TextView) layout
							.findViewById(R.id.TextViewseconddaycondition);

					condition2.setText(ww.getConditions().get(i));

					break;
				case 2:
					TextView thirdday = (TextView) layout
							.findViewById(R.id.TextViewthirdday);
					thirdday.setText(ww.getDays().get(i));

					ImageView icon3 = (ImageView) layout
							.findViewById(R.id.ImageViewthirddayicon);
					//icon3.setImageBitmap(getHttpBitmap(ww.getIcons().get(i)));
					icon3.setImageResource(this.getImageID(ww.getIcons().get(i)));
					TextView hight3 = (TextView) layout
							.findViewById(R.id.TextViewthirddayhigh);
					hight3.setText(ww.getHts().get(i));

					TextView lowt3 = (TextView) layout
							.findViewById(R.id.TextViewthirddaylow);
					lowt3.setText(ww.getLts().get(i));

					TextView condition3 = (TextView) layout
							.findViewById(R.id.TextViewthriddaycondition);

					condition3.setText(ww.getConditions().get(i));

					break;
				case 3:

					TextView fourthday = (TextView) layout
							.findViewById(R.id.TextViewfourthday);
					fourthday.setText(ww.getDays().get(i));

					ImageView icon4 = (ImageView) layout
							.findViewById(R.id.ImageViewfourthdayicon);
					//icon4.setImageBitmap(getHttpBitmap(ww.getIcons().get(i)));
					icon4.setImageResource(this.getImageID(ww.getIcons().get(i)));
					TextView hight4 = (TextView) layout
							.findViewById(R.id.TextViewfourthdayhigh);
					hight4.setText(ww.getHts().get(i));

					TextView lowt4 = (TextView) layout
							.findViewById(R.id.TextViewfourthdaylow);
					lowt4.setText(ww.getLts().get(i));

					TextView condition4 = (TextView) layout
							.findViewById(R.id.TextViewfourthdaycondition);

					condition4.setText(ww.getConditions().get(i));

					break;
				}

			}
			return layout;
		} else if (coll.get(position).getDetailName().equals("image")) {
			Log.v("Find", coll.get(position).getDetailName());

			//
			//
			final DetailImage entity = (DetailImage) coll.get(position);
			int itemLayout = entity.getLayoutID();
			LinearLayout layout = new LinearLayout(ctx);
			LayoutInflater vi = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi.inflate(itemLayout, layout, true);
			ImageView ib = (ImageView) layout
					.findViewById(R.id.imageButtonPreview);
			// File f= new File(entity.getPath());
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inSampleSize=15;
			// options.inTempStorage = new byte[5*1024];
			// Bitmap bitMap = BitmapFactory.decodeFile(entity.getPath(),
			// options);

			ib.setBackgroundDrawable(entity.getDw());

			//
			//
			// Matrix m = new Matrix();
			// int width = bitMap.getWidth();
//			int height = bitMap.getHeight();
//			m.setRotate(90);
//			Bitmap b = Bitmap
//					.createBitmap(bitMap, 0, 0, width, height, m, true);
//			ib.setImageBitmap(b);
			 
			 
//			LinearLayout ll = (LinearLayout) layout
//					.findViewById(R.id.linearLayout1);
//			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ll
//					.getLayoutParams();
//			linearParams.width = height+36;
//			linearParams.height = width+45;
//			ll.setLayoutParams(linearParams);
			
			 
			 
			 
			 
			 
			// Bitmap myBitmap = decodeFile(f);
			// Bitmap resize = Bitmap.createScaledBitmap(myBitmap, 600, 300,
			// true);
			// Matrix m = new Matrix();
			// int width = resize.getWidth();
			// int height = resize.getHeight();
//			m.setRotate(90);
//			Bitmap b = Bitmap.createBitmap(resize, 0, 0, width, height, m, true);
			// ib.setImageBitmap(b);

			ib.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("path", entity.getPath());
					intent.setClass(entity.getContext(), ImageViewActivity.class);
					entity.getContext().startActivity(intent);
				}
			});

			return layout;

		} else if (coll.get(position).getDetailName().equals("translate")) {
			//
			DetailTranslate entity = (DetailTranslate) coll.get(position);
			int itemLayout = entity.getLayoutID();
			LinearLayout layout = new LinearLayout(ctx);
			LayoutInflater vi = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi.inflate(itemLayout, layout, true);
			TextView txo = (TextView) layout.findViewById(R.id.textView1);
			txo.setText(entity.getText());
			TextView txt = (TextView) layout.findViewById(R.id.textView2);
			txt.setText(entity.getTranslateResult());
			return layout;
		}
		// }

		return null;

	}
	
	private Bitmap decodeFile(File f){ 
	    try { 
	        //Decode image size 
	        BitmapFactory.Options o = new BitmapFactory.Options(); 
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 300;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {

		}
		return null;
	}


	public int getViewTypeCount() {
		return coll.size();
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isEmpty() {
		return false;
	}

/*	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			// 这句可有可无，没有影响
			// conn.connect();
			// 得到数据流
			InputStream is = conn.getInputStream();
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			// 关闭数据流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;

	}
	*/
	
	


	public int getImageID(String name){
	
		
		
		return ctx.getResources().getIdentifier(name, "drawable", "com.android");
	
	}
	
	

	public void registerDataSetObserver(DataSetObserver observer) {
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

}
