package com.flatstack.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Revern on 10.04.2017.
 */

public class Annotations {

    @SerializedName("@context")
    private String context;

    @SerializedName("@graph")
    private List<Annotation> annotations;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }
}
