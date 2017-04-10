package com.handstandsam.buildingdebugfeatures.di;

import com.handstandsam.buildingdebugfeatures.MainActivity;
import com.handstandsam.buildingdebugfeatures.UserActivity;

import dagger.Component;

@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(MainActivity clazz);
    void inject(UserActivity clazz);
}