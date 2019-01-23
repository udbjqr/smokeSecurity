package com.example.myflutter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import com.tencent.tencentmap.mapsdk.map.MapView;
import io.flutter.app.FlutterApplication;
import io.flutter.plugin.common.*;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;
import org.json.JSONObject;
import io.flutter.plugin.common.BinaryMessenger;

import static io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import static io.flutter.plugin.common.MethodChannel.Result;

public class MainApplication extends FlutterApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        setContentView(R.layout.activity_main);
//        TencentMap tencentMap = mapView.getMap();

    }

}

class FlutterMapView implements PlatformView, MethodCallHandler {
    private MapView mapView;
    private JSONObject jsonObject;

    FlutterMapView(Context context, MapView mapViews, int id, JSONObject jsonObject, BinaryMessenger messenger) {
        this.jsonObject = jsonObject;
//        mapView = new MapView(context);
        this.mapView = mapViews;
        MethodChannel methodChannel = new MethodChannel(messenger, "MapView_" + id);
        methodChannel.setMethodCallHandler(this);

    }

    @Override
    public View getView() {
        return mapView;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void onMethodCall(MethodCall methodCall, Result result) {
        switch (methodCall.method) {
            case "HandleChange":
                HandleChange(methodCall, result);
                break;
            default:
                result.notImplemented();
        }
    }

    private void HandleChange(MethodCall methodCall, Result result) {
//        String text = (String) methodCall.arguments;
//        MapView.setText(text);
        result.success(String.valueOf(jsonObject));
    }
}


class BMapViewFactory extends PlatformViewFactory {
    private BinaryMessenger messenger;
    private MapView mapView;
    private JSONObject jsonObject;
    BMapViewFactory(JSONObject jsonObject, MapView mapView, BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.mapView = mapView;
        this.jsonObject = jsonObject;
        this.messenger = messenger;
    }

    @Override
    public PlatformView create(final Context context, int id, Object o) {
        return new FlutterMapView(context, mapView, id, jsonObject, messenger);
    }
}

final class ViewRegistrant {
    static void registerWith(PluginRegistry registry, MapView mapView, JSONObject jsonObject) {
        final String key = ViewRegistrant.class.getCanonicalName();

        if (registry.hasPlugin(key)) return;
        PluginRegistry.Registrar registrar = registry.registrarFor(key);

        registrar.platformViewRegistry().registerViewFactory("MapView",
          new BMapViewFactory(jsonObject, mapView, registrar.messenger()));
    }
}







