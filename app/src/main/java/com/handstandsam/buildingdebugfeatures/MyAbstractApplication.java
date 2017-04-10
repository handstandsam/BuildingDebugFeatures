package com.handstandsam.buildingdebugfeatures;

import android.app.Application;

import com.handstandsam.buildingdebugfeatures.di.AppComponent;
import com.handstandsam.buildingdebugfeatures.di.AppModule;
import com.handstandsam.buildingdebugfeatures.di.DaggerAppComponent;
import com.handstandsam.buildingdebugfeatures.di.NetworkModule;

public class MyAbstractApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = createAppComponent();
    }

    protected AppComponent createAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule("https://api.github.com/"))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
