package com.example.myflutter;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;
import org.json.JSONException;
import org.json.JSONObject;
public class MainActivity extends FlutterActivity implements TencentLocationListener {

//public class MainActivity extends FlutterActivity {
	MapView mapview=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GeneratedPluginRegistrant.registerWith(this);
//		setContentView(R.layout.activity_main);
		TencentLocationRequest request = TencentLocationRequest.create();
		request
			.setInterval(2000)
			.setRequestLevel(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
			.setAllowCache(true);
		//开启定位监听器
		init(request);

	}
	public void init(TencentLocationRequest request) {
		Context context = this;
		TencentLocationListener listener = this;
		TencentLocationManager locationManager = TencentLocationManager.getInstance(context);
		int error = locationManager.requestLocationUpdates(request, listener);
		if (error == 0) {
			Log.v("this", "注册位置监听器成功！");
		} else {
			Log.v("this", "注册位置监听器失败！");
		}
	}
	@Override
	public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
		Double lat = 28.6561900000;
		Double lon = 115.8768300000;
		JSONObject json= new JSONObject();
		try {
			json.put("lat",lat);
			json.put("lon",lon);
			json.put("address","4545");
			json.put("abcode","110101");
			json.put("is_succ",false);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (TencentLocation.ERROR_OK == i) {
			// 定位成功
			Log.v("this", "定位成功！");
			if (tencentLocation != null) {
				try {
					json.put("lat",tencentLocation.getLatitude());
					json.put("lon",tencentLocation.getLongitude());
					json.put("address",tencentLocation.getAddress());
					json.put("abcode", tencentLocation.getCityCode());
					json.put("is_succ",true);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				lat = tencentLocation.getLatitude();
				lon = tencentLocation.getLongitude();
				//textView.setText("当前经纬度："+lat+","+lon+nation+province+city+district+town+village+street+streetNo);
			}
		} else {
			Log.v("this", "定位失败！");
		}
		mapview = new MapView(this);
		TencentMap tencentMap = mapview.getMap();
		ViewRegistrant.registerWith(this, mapview, json);
		tencentMap.setCenter(new LatLng(lat, lon));
		tencentMap.setZoom(13);

		Marker marker = null;
		try {
			marker = tencentMap.addMarker(new MarkerOptions()
				.position(new LatLng(lat, lon))
				.title(json.get("address").toString())
				.anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory
					.defaultMarker())
				.draggable(true));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (marker != null) {
			marker.showInfoWindow();// 设置默认显示一个infoWindow
		}

		Context context = this;
		TencentLocationListener listener = this;
		TencentLocationManager locationManager = TencentLocationManager.getInstance(context);
		locationManager.removeUpdates(listener);
	}

	@Override
	public void onStatusUpdate(String s, int i, String s1) {

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//关闭定位监听器
		TencentLocationManager locationManager =
			TencentLocationManager.getInstance(this);
		locationManager.removeUpdates(this);
		mapview.onDestroy();
	}

	@Override
	protected void onPause() {
//		mapview.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
//		mapview.onResume();
		super.onResume();
	}

	@Override
	protected void onStop() {
//		mapview.onStop();
		super.onStop();
	}
}
