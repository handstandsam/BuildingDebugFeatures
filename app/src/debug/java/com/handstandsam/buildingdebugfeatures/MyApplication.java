package com.handstandsam.buildingdebugfeatures;

import java.io.IOException;

import com.handstandsam.buildingdebugfeatures.debug.DebugDispatcher;
import com.handstandsam.buildingdebugfeatures.debug.DebugPreferences;
import com.handstandsam.buildingdebugfeatures.di.AppComponent;
import com.handstandsam.buildingdebugfeatures.di.AppModule;
import com.handstandsam.buildingdebugfeatures.di.DaggerAppComponent;
import com.handstandsam.buildingdebugfeatures.di.DebugNetworkModule;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.widget.Toast;

import okhttp3.mockwebserver.MockWebServer;
import timber.log.Timber;

public class MyApplication extends MyAbstractApplication {

    public static MockWebServer server;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }

    private void startMockWebServer() {
        if (server != null) {
            Timber.w("Server already started on port: " + server.getPort());
            return;
        }

        try {
            // Have to do this to start the server synchronously on the main thread (not recommended, but this is a
            // debug feature)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            server = new MockWebServer();
            server.start();
            Timber.w("Server started on port: " + server.getPort());
            server.setDispatcher(new DebugDispatcher(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected AppComponent createAppComponent() {
        String endpoint = "https://api.github.com/";

        DebugPreferences debugPreferences = new DebugPreferences(this);
        if (debugPreferences.getBaseUrl() != null && debugPreferences.getBaseUrl().length() > 0) {
            endpoint = debugPreferences.getBaseUrl();
        }
        if (new DebugPreferences(this).isMockMode()) {
            startMockWebServer();
            endpoint = server.url("/").toString();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyApplication.this, "Using Mock Server", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return DaggerAppComponent.builder().appModule(new AppModule(this))
                .networkModule(new DebugNetworkModule(endpoint)).build();
    }

}
