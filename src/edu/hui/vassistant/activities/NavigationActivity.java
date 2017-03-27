package edu.hui.vassistant.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import edu.hui.vassistant.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

import android.graphics.Color;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class NavigationActivity extends FragmentActivity {
	
	Intent intent;
	List<LatLng> list;
	Polyline polyline;
	private Location location;
	private CameraPosition cameraPosition;
	private GoogleMap map;
	String start = "";
	String end = "";
	double lat = 0.0;
	double lng = 0.0;
	double endlat = 0.0;
	double endlng = 0.0;
	LocationManager locationManager;
	ImageButton local;
	LocationListener llistener;
	Marker  marker;
	String cityName = "";
	EditText st,en;
	
	
	private ProgressDialog progressdialog;

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
									
			case 1:
				
                progressdialog.dismiss();
				
				if(polyline!=null){
				polyline.remove();
				map.clear();
				markMysition();
				}		
				
				
				map.addMarker(new MarkerOptions().position(
						new LatLng(endlat, endlng)).title(
								NavigationActivity.this.getLocation(endlat, endlng)));
				
				
				
				LatLng last = null;
				
				for (int i = 0; i < list.size() - 1; i++) {
					LatLng src = list.get(i);
					LatLng dest = list.get(i + 1);
					last = dest;
					polyline = map.addPolyline(new PolylineOptions()
							.add(new LatLng(src.latitude, src.longitude),
									new LatLng(dest.latitude, dest.longitude))
							.width(4).color(Color.GREEN));
				}

				break;

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation);
		local = (ImageButton) findViewById(R.id.local);
		//local.setImageResource(R.drawable.ww);
		intent =  getIntent();
		cityName = intent.getStringExtra("cityName");
		
		// 点击按钮视图回到我的位置
		local.setOnClickListener(new android.view.View.OnClickListener() {
			
			public void onClick(View v) {
				NavigationActivity.this.setCameraPosition();

			}

		});
		map = ((SupportMapFragment) (this.getSupportFragmentManager()
				.findFragmentById(R.id.map))).getMap();
	
		this.getPointLocation();

		NavigationActivity.this.setCameraPosition();
	}

	// 标记我的位置
	void markMysition() {
	 	 marker=map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(
				this.getLocation(lat, lng)).snippet("我的位置"));
	
	}

	// 获取我的位置
	private void getPointLocation() {

		Criteria criteria = new Criteria();
		// ACCURACY_FINE 较高精确度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		String provider = locationManager.getBestProvider(criteria, true);

		Log.d("provider", provider);
		llistener = new LocationListener() {
			public void onLocationChanged(Location location) {
				/*if(marker!=null){			
					marker.remove();
				}
*/				lat = location.getLatitude();
				lng = location.getLongitude();
			//	MainActivity.this.markMysition();
				
			}

			public void onProviderDisabled(String provider) {
				Log.i("onProviderDisabled", "come in");
			}

			public void onProviderEnabled(String provider) {
				Log.i("onProviderEnabled", "come in");
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}
		};
		
		location = locationManager
				.getLastKnownLocation(locationManager.GPS_PROVIDER);

		
        
		locationManager.requestLocationUpdates(provider, 3000, (float) 10.0,
				llistener);
		
		updateLocation();

		NavigationActivity.this.markMysition();

		NavigationActivity.this.setCameraPosition();
		editpoint();
		
		
	}

	// 位置更新
	private void updateLocation() {
		if (location != null) {
			lat = location.getLatitude();
			lng = location.getLongitude();
		}
	}

	// 将视图镜头定位在我的位置
	public void setCameraPosition() {
		// 获取视图镜头
		cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(lat, lng)) // Sets the center of the map to
				.zoom(17) // 缩放比例
				.bearing(0) // Sets the orientation of the camera to east
				.tilt(20) // Sets the tilt of the camera to 30 degrees
				.build(); // Creates a CameraPosition from the builder
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}


	private String getLocation(double lat, double lng) {
		String address = "";
		String resultString = "";
		String urlString = String
				.format("http://maps.google.com/maps/api/geocode/json?latlng="
						+ lat + "," + lng + "&sensor=true&language=zh-CN");
		Log.i("URL", urlString);
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(urlString);

		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			BufferedReader buffReader = new BufferedReader(
					new InputStreamReader(entity.getContent()));
			StringBuffer strBuff = new StringBuffer();
			String result = null;
			while ((result = buffReader.readLine()) != null) {
				strBuff.append(result);
			}
			resultString = strBuff.toString();

			// 解析josn数据，获得物理地址
			if (resultString != null && resultString.length() > 0) {
				JSONObject jsonobject = new JSONObject(resultString);
				JSONArray jsonArray = new JSONArray(jsonobject.get("results")
						.toString());
				resultString = "";

				for (int i = 0; i < jsonArray.length(); i++) {
					resultString = jsonArray.getJSONObject(i).getString(
							"formatted_address");
					address += resultString;
				}
			}
		} catch (Exception e) {
		} finally {
			get.abort();
			client = null;
		}
		return address;
	}

	// 通过输入信息解析json获取路线
	private void getDirection(String start, String endposition) {

		String DresultString = "";

		String Durl = "http://maps.google.com/maps/api/directions/json?origin="
				+ lat + "," + lng + "&destination=" + endlat + "," + endlng
				+ "&sensor=true&mode=driving";
		HttpClient Dclient = new DefaultHttpClient();
		HttpGet Dget = new HttpGet(Durl);

		HttpResponse response;

		try {
			response = Dclient.execute(Dget);
			HttpEntity Dentity = response.getEntity();
			BufferedReader DbuffReader = new BufferedReader(
					new InputStreamReader(Dentity.getContent()));
			StringBuffer DstrBuff = new StringBuffer();
			String Dresult = null;
			while ((Dresult = DbuffReader.readLine()) != null) {
				DstrBuff.append(Dresult);
			}
			DresultString = DstrBuff.toString();
			

		} catch (Exception e) {

		}
		try {
			final JSONObject jsonObject = new JSONObject(DresultString);
			JSONArray routeArray = jsonObject.getJSONArray("routes");
			JSONObject routes = routeArray.getJSONObject(0);
			JSONObject overviewPolylines = routes
					.getJSONObject("overview_polyline");
			String encodedString = overviewPolylines.getString("points");
			Log.d("test: ", encodedString);
			
			list = decodePoly(encodedString);

			

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Caught ArrayIndexOutOfBoundsException: "
					+ e.getMessage());
		}
	}

	// 填写起始地点以得到路线导航
	private void editpoint() {
		
		LayoutInflater factory = LayoutInflater.from(NavigationActivity.this);
		// 获得自定义对话框
		final View view = factory.inflate(R.layout.getdirections, null);
		st = (EditText) view.findViewById(R.id.start);
		 en = (EditText) view.findViewById(R.id.end);
			en.setText(cityName,TextView.BufferType.EDITABLE);
		AlertDialog navigate = new AlertDialog.Builder(NavigationActivity.this)
				.setIcon(android.R.drawable.ic_menu_edit).setTitle("Get route")
				.setView(view).setPositiveButton("Done", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						dialog.dismiss();
						
					
						
						
						start = st.getText().toString();
						end = en.getText().toString();
						
						
						
						
						progressdialog = ProgressDialog.show(NavigationActivity.this,
								"Calculating", "Please wait……", true);

						

						new Thread(new Runnable() {

							public void run() {
								// TODO Auto-generated method stub
								
								getlatlng(end);
								getDirection("我的位置", end);
								mHandler.sendEmptyMessage(0);
								mHandler.sendEmptyMessage(1);
							}
						}).start();

					}
				}).setNegativeButton("Cancel", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						dialog.dismiss();
						locationManager.removeUpdates(llistener);
						NavigationActivity.this.finish();
						
					}
				}).create();
		navigate.show();
		

	}

	// 根据地理名称获得起始点和终点的经纬度
	private void getlatlng(String end) {

		StringBuilder stringBuilder = new StringBuilder();
		try {

			HttpPost httppost = new HttpPost(
					"http://maps.google.com/maps/api/geocode/json?address="
							+ end + "&sensor=false");
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			stringBuilder = new StringBuilder();

			response = client.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();

			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			endlng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");

			endlat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	// 解析json文件里面的polyline下的poly得出导航上面路径的点
	private List<LatLng> decodePoly(String encoded) {
		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((lat / 1E5), lng / 1E5);
			poly.add(p);
		}
		return poly;
	}

}
