package com.flatstack.android;

import android.support.annotation.NonNull;

import com.flatstack.android.model.Annotation;
import com.flatstack.android.model.Annotations;
import com.flatstack.android.model.Graph;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Revern on 04.04.2017.
 */

public class BratInteractor {
    @NonNull private final Api api;

    public BratInteractor(@NonNull Api api) {
        this.api = api;
    }

    public ArrayList<String> loadAnnotations() {
        //TODO REPLACE : DELETE
        ArrayList<String> annotationList = new ArrayList<>();
        annotationList.add("Medicine");
        annotationList.add("Drug");
        annotationList.add("Death");
        return annotationList;
    }

    public Observable<Annotations> loadAllAnnotations(){
        return api.getAllAnnotations();
    }

    public Observable<ResponseBody> loadDocument(String id) {
        return api.getDocument(id);
    }

    public Observable<Graph> loadDocuments() {
        return api.getAllDocuments();
    }

    public Observable<Annotation> addAnnotation(@NonNull String body, @NonNull String target) {
        return api.addAnnnotation(body, target);
    }

}
