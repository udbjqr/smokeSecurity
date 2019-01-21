package com.example.myflutter;

import android.content.Context;
import android.view.View;
import io.flutter.app.FlutterApplication;
import io.flutter.plugin.common.MessageCodec;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class MainApplication extends FlutterApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        setContentView(R.layout.activity_main);
//        TencentMap tencentMap = mapView.getMap();

    }

}



class BMapViewFactory extends PlatformViewFactory {

    private Object mapView;

    public BMapViewFactory(MessageCodec<Object> createArgsCodec, Object mapView) {
        super(createArgsCodec);
        this.mapView = mapView;
    }

    @Override
    public PlatformView create(final Context context, int i, Object o) {
        return new PlatformView() {
            @Override
            public View getView() {
                return (View) mapView;
            }

            @Override
            public void dispose() {

            }
        };
    }
}


final class ViewRegistrant {
    public static void registerWith(PluginRegistry registry, Object mapView) {
        final String key = ViewRegistrant.class.getCanonicalName();

        if (registry.hasPlugin(key)) return;

        PluginRegistry.Registrar registrar = registry.registrarFor(key);
        registrar.platformViewRegistry().registerViewFactory("MapView", new BMapViewFactory(new StandardMessageCodec(), mapView));
    }
}




