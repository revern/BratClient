package com.flatstack.android;

import android.support.annotation.NonNull;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
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

    @POST(BASE_URL + "annotations") Observable<Annotation> addAnnnotation(
            @NonNull @Field("id") String id,
            @NonNull @Field("id") String body,
            @NonNull @Field("id") String target
    );

    @GET(BASE_URL + "documents") Observable<Graph> getAllDocuments();

    @GET(BASE_URL + "documents/{id}") Observable<ResponseBody> getDocument(
            @NonNull @Path("id") String id
    );
}
