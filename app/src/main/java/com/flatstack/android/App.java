package com.flatstack.android;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.flatstack.android.utils.di.AppComponent;
import com.flatstack.android.utils.di.AppModule;
import com.flatstack.android.utils.di.DaggerAppComponent;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        // Dagger%COMPONENT_NAME%
        appComponent = DaggerAppComponent.builder()
//             list of modules that are part of this component need to be created here too
//             This also corresponds to the name of your module: %component_name%Module
                .appModule(new AppModule(this))
                .build();

        Stetho.initializeWithDefaults(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}