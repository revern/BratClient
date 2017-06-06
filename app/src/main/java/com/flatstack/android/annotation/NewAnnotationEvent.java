package com.flatstack.android.annotation;

import android.support.annotation.NonNull;

/**
 * Created by Revern on 06.06.2017.
 */

public class NewAnnotationEvent {

    private String annotation;

    public NewAnnotationEvent(@NonNull String annotation) {
        this.annotation = annotation;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
}
