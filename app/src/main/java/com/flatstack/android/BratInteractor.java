package com.flatstack.android;

import android.support.annotation.NonNull;

import com.flatstack.android.model.Annotation;
import com.flatstack.android.model.Annotations;
import com.flatstack.android.model.Graph;

import rx.Observable;

/**
 * Created by Revern on 04.04.2017.
 */

public class BratInteractor {
    @NonNull private final Api api;

    public BratInteractor(@NonNull Api api) {
        this.api = api;
    }

    public Observable<Annotations> loadAllAnnotations(){
        return api.getAllAnnotations();
    }

    public Observable<Graph> loadDocuments() {
        return api.getAllDocuments();
    }

    public Observable<Annotation> addAnnotation(@NonNull Annotation annotation) {
        return api.addAnnotation(annotation.getBody(), annotation.getTarget());
    }
}
