package com.flatstack.android.utils.di;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.flatstack.android.App;
import com.flatstack.android.annotation.AnnotationActivity;

/**
 * Created by Revern on 04.04.2017.
 */

public class Injector {

    private static AppComponent getAppComponent(@NonNull FragmentActivity initialScreen) {
        return ((App) initialScreen.getApplicationContext()).getAppComponent();
    }

    public static void inject(AnnotationActivity annotationActivity) {
        getAppComponent(annotationActivity).inject(annotationActivity);
    }
}
