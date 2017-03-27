package edu.hui.vassistant.activities;

import android.support.v4.app.FragmentActivity;

import edu.hui.vassistant.R;



import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;


public class MapsActivity extends FragmentActivity implements LocationListener {
//	private MapView mv;
//	MapController controller;
	Intent intent;
	
	private GoogleMap myMap;
	private SupportMapFragment mMapFragment;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maps);
		
		mMapFragment = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.my_map));
		
		mMapFragment.getView().setVisibility(View.VISIBLE);
		myMap = mMapFragment.getMap();
		myMap.setMyLocationEnabled(true);
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		
		intent =  getIntent();
	/*	mv = (MapView) findViewById(R.id.my_map);
		mv.setBuiltInZoomControls(true);
		mv.setClickable(true);
		controller = (MapController) mv.getController();
		controller.setZoom(17);
		*/
		Log.v("style", intent.getStringExtra("style"));

		if (!intent.getStringExtra("style").equals("home")) {
		
			String Fromlatitude = intent.getStringExtra("Fromlatitude");
			String Fromlongtitude = intent.getStringExtra("Fromlongtitude");
			String tolatitude = intent.getStringExtra("tolatitude");
			String tolongtitude = intent.getStringExtra("tolongtitude");
			String cityName = intent.getStringExtra("cityName");
	
			
		/*	String mail = drawRoute(Fromlatitude, Fromlongtitude, tolatitude, tolongtitude);
			Toast.makeText(this, "Total: "+mail,
					Toast.LENGTH_LONG).show();
					
			*/

		} else {
			
		
			Location mylocation =  getMyLocation();
			if (mylocation != null){
			LatLng current = new LatLng(mylocation.getLatitude(),mylocation.getLongitude());
			myMap.moveCamera(CameraUpdateFactory.newLatLng(current));
			myMap.animateCamera(CameraUpdateFactory.newLatLng(current));
	         
			myMap.animateCamera(CameraUpdateFactory.zoomTo(myMap
                             .getMaxZoomLevel() - 8));
			}else{
				System.out.println(mylocation.getLatitude()+"|" + mylocation.getLongitude() );
			}
		//	myMap.animateCamera(CameraUpdateFactory.zoomTo( 17.0f ));
			
	    }
	}


	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}


	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}


	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}


	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
		/*	List<Overlay> overlays = mv.getOverlays();
			final MyLocationOverlay mylocTest = new MyLocationOverlay(this, mv);
			mylocTest.enableCompass();
			mylocTest.enableMyLocation();
			mylocTest.runOnFirstFix(new Runnable()
			{
				 public void run()
				 {
					 controller.animateTo(mylocTest.getMyLocation());
				 }
			});
			overlays.add(mylocTest);*/
		
	
	
	

//	protected void onResume(){
//		intent =  getIntent();
//		String Fromlatitude = intent.getStringExtra("Fromlatitude");
//		String Fromlongtitude = intent.getStringExtra("Fromlongtitude");
//		String tolatitude = intent.getStringExtra("tolatitude");
//		String tolongtitude = intent.getStringExtra("tolongtitude");
//		mv = (MapView) findViewById(R.id.my_map);
//		mv.setBuiltInZoomControls(true);
//		controller = (MapController) mv.getController();
//		drawRoute(Fromlatitude,Fromlongtitude,tolatitude,tolongtitude);
//		mv.setClickable(true);
//		controller.setZoom(17);
//		 super.onResume();
//	}
	

	
	
	/*
	
	public String drawRoute(String Fromlatitude , String Fromlongtitude, String tolatitude ,String tolongtitude) {

		String url = "http://maps.google.com/maps/api/directions/xml?origin="+Fromlatitude+","+Fromlongtitude+
				"&destination="+tolatitude+","+tolongtitude+"&sensor=false&mode=driving";
//http://maps.google.com/maps/api/directions/xml?origin=56.157173,13.758584&destination=59.3289300,18.0649100&sensor=false&mode=driving
		HttpGet get = new HttpGet(url);
		String strResult = "";
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			HttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpResponse httpResponse = null;
			httpResponse = httpClient.execute(get);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			
		}

		if (-1 == strResult.indexOf("<status>OK</status>")) {
			Toast.makeText(this, "Failed to get navigation routes!", Toast.LENGTH_SHORT).show();
			this.finish();
			return "";
		}

		int pos = strResult.indexOf("<overview_polyline>");
		pos = strResult.indexOf("<points>", pos + 1);
		int pos2 = strResult.indexOf("</points>", pos);
		String substrResult = strResult.substring(pos + 8, pos2);
		// System.out.println();
		Log.println(0, "", "str: " + substrResult);
		List<GeoPoint> points = decodePoly(substrResult);

		MyOverLay mOverlay = new MyOverLay(points);
		List<Overlay> overlays = mv.getOverlays();
		
		
		final MyLocationOverlay mylocTest = new MyLocationOverlay(this, mv);
		mylocTest.enableCompass();
		mylocTest.enableMyLocation();
		mylocTest.runOnFirstFix(new Runnable()
		{
			 public void run()
			 {
				 controller.animateTo(mylocTest.getMyLocation());
			 }
		});
		overlays.add(mylocTest);
		
		
		overlays.add(mOverlay);

		if (points.size() >= 2) {
			controller.animateTo(points.get(0));
		}

		mv.invalidate();
		int distance= strResult.indexOf("<distance>");
		int miles = strResult.indexOf("<text>",distance+1);
		int milesend = strResult.indexOf("</text>",miles);
		String mile = strResult.substring(miles+6,milesend);
		if(mile.contains("m")){
			mile.replace("m", "miles");
		}
		return mile;
	}

	/**
	 * 解析返回xml中overview_polyline的路线编码
	 * 
	 * @param encoded
	 * @return
	 */
	/*
	private List<GeoPoint> decodePoly(String encoded) {

		List<GeoPoint> poly = new ArrayList<GeoPoint>();
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

			GeoPoint p = new GeoPoint((int) (((double) lat / 1E5) * 1E6),
					(int) (((double) lng / 1E5) * 1E6));
			poly.add(p);
		}

		return poly;
	}
*/
	private Location getMyLocation() {
	    // Get location from GPS if it's available
	    LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);
	    Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

	    // Location wasn't found, check the next most accurate place for the current location
	    if (myLocation == null) {
	        Criteria criteria = new Criteria();
	        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
	        // Finds a provider that matches the criteria
	        String provider = lm.getBestProvider(criteria, true);
	        // Use the provider to get the last known location
	        myLocation = lm.getLastKnownLocation(provider);
	    }

	    return myLocation;
	}
}
