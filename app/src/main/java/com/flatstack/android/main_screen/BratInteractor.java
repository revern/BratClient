package com.flatstack.android.main_screen;

import android.support.annotation.NonNull;

import com.flatstack.android.Annotation;
import com.flatstack.android.Annotations;
import com.flatstack.android.Api;
import com.flatstack.android.Document;
import com.flatstack.android.Graph;

import java.util.ArrayList;
import java.util.List;

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

    public Observable<List<Document>> loadDocuments() {
        return api.getAllDocuments();
    }

}
