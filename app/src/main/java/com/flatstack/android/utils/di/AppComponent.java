package com.flatstack.android.utils.di;

import com.flatstack.android.annotation.AnnotationActivity;
import com.flatstack.android.settings.SettingsActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Revern on 04.04.2017.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(AnnotationActivity annotationActivity);

    void inject(SettingsActivity settingsActivity);
}
