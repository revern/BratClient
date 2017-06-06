package com.flatstack.android.utils.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.flatstack.android.Api;
import com.flatstack.android.BratInteractor;
import com.flatstack.android.settings.SettingsActivity;
import com.flatstack.android.utils.network.RxErrorHandlingCallAdapterFactory;
import com.flatstack.android.utils.network.StringConverterFactory;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Revern on 04.04.2017.
 */

@Module
public class AppModule {

    public static final String KEY_PREFS = "prefs";

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides @Singleton Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(){
        return mApplication.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE);
    }
    @Provides
    @Singleton
    public OkHttpClient provideHttpClient(/*@NonNull File cachedDir*/) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
//        httpClientBuilder.cache(new Cache(cachedDir, 20 * 1024 * 1024));
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS);

        httpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
        httpClientBuilder.addInterceptor(chain -> {
//            if (userSettings.userHasToken()) {
//                Request request = chain.request().newBuilder()
//                        .addHeader("X-User-Token", userSettings.getToken())
//                        .addHeader("X-User-Phone-Number", userSettings.getPhone())
//                        .build();
//                return chain.proceed(request);
//            }
            return chain.proceed(chain.request());
        });
        return httpClientBuilder.build();
    }

//    @Provides @Singleton public File getCacheDir(@NonNull Context context) {
//        final File external = context.getExternalCacheDir();
//        return external != null ? external : context.getCacheDir();
//    }

    @Provides @Singleton
    public Api providesApi(@NonNull OkHttpClient httpClient, @NonNull Gson mapper, SharedPreferences prefs) {
        return new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(prefs.getString(SettingsActivity.KEY_BASE_URL, Api.BASE_URL))
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mapper))
                .build()
                .create(Api.class);
    }

    @Provides @Singleton public Gson getMapper() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides @Singleton public BratInteractor providesBratInteractor(@NonNull Api api) {
        return new BratInteractor(api);
    }

}
