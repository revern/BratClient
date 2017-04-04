package com.flatstack.android.main_screen;

import android.support.annotation.NonNull;

import com.flatstack.android.Api;

import java.util.ArrayList;
import java.util.List;

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

    public String loadText() {
        //TODO REPLACE : DELETE
        return "some text, hello, how are you? HAHA haha Something something went wrong, annotation is very bad thing!";
    }

}
