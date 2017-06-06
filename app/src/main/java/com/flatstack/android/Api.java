package com.flatstack.android;

import android.support.annotation.NonNull;

import com.flatstack.android.model.Annotation;
import com.flatstack.android.model.Annotations;
import com.flatstack.android.model.Document;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Revern on 04.04.2017.
 */

public interface Api {

    String BASE_URL = "http://weaver.nlplab.org/api/";

    @GET(BASE_URL + "annotations") Observable<Annotations> getAllAnnotations();

    @GET(BASE_URL + "annotations/{id}") Observable<Annotation> getAnnotation(
            @NonNull @Path("id") String id
    );

    @GET(BASE_URL + "documents") Observable<List<Document>> getAllDocuments();

    @GET(BASE_URL + "documents/{id}") Observable<ResponseBody> getDocument(
            @NonNull @Path("id") String id
    );
}
